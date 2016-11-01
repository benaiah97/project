package pvt.disney.dti.gateway.common;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import oracle.sql.CLOB;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;
import com.ibm.websphere.ce.cm.StaleConnectionException;

/**
 * This class groups all the common methods that will be used by any object
 * that wants to access the database. 
 * These methodes are: <pre>
 * getConnection - sets the specs for the wanted connection
 * releaseConnection - returns the connection to WebSphere
 * invoke - performs query 
 * logInbound - logs ticket request to the inbound database table.</pre>
 *
 * Other notes:  12/21 - JTL - Implemented core logging and altered 
 * releaseConnection methods by placing a return after the conn or c.close.  
 * That way, the log reader will know if the releaseConnection performed.
 * Removed "final" modifier from class - doesn't work with protected attributes
 * and methods, which indicate a subclass is possible.  
 * Cleaned up unused private instance variables.
 * 
 * @author Andy Anon
 * @version %version: % 
 */
public class DBTransaction {

  /** The standard core logging mechanism. */
  private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

  /** IBM proprietary objects. These objects are used to get
  a connection from a WebSphere database connections pool. */
  private DataSource ds = null;

  /** JDBC connection object returned by the WebSphere EJB datasource. */
  private Connection conn = null;

  /** Core properties management initializer. */
  private AbstractInitializer abstrInit = null;
  /** Properties variable to store properties from AbstractInitializer. */
  private Properties props = null;

  /**
   * The object where the result of the query is stored. This object is used
   * by the <I>createResponse()<I> method to transfer the content of the 
   * result set to an IValueSet data holder.
   */
  protected ResultSet rs = null;

  /** Contains an SQL statement that will be executed by the database. */
  private java.sql.PreparedStatement aStatement = null;

  /** Contains all the rows returned by the sql query. */
  private HashMap<String, String> aValueSetCollection = null;

  private String sqlStatement;

  /** DBTransaction Constructor (default).  Gets the properties instance. */
  public DBTransaction() {

    try {
      
	    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
	    props = ResourceLoader.convertResourceBundleToProperties(rb);

    } catch (Throwable e) {
      eventLogger.sendEvent("THROWABLE initing props: " + e.toString(), EventType.FATAL, this);
    }

  }

  /**
   * Get a reference to the WebSphere datasource EJB.
   *
   * In this method, the <I>Remote Home Object<I> is retrieved and then used 
   * to create a reference to a connection object.
   *
   * @throws DTIException 
   */
  private void initConnection() throws DTIException {

    eventLogger.sendEvent("Entering initConnection()", EventType.DEBUG, this);

    try {
      // Create the initial naming context.
      Hashtable<String, String> parms = new Hashtable<String, String>();
      
      /*
      parms.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
      InitialContext ctx = new InitialContext(parms);
	  */
      
      InitialContext ctx = new InitialContext();

      /*
       * Perform a naming service lookup to get a DataSource object.
       * The single DataSource object is a "factory" used by all
       * requests to get an individual connection for each request.
       * The Web administrator can provide the lookup parameters.
       * The code below uses a value read in from an external property
       * file. The text string source is typically something like
       * "jdbc/sample" where "jdbc" is the context for the
       * lookup and "sample" is the logicalname of the DataSource
       * object to be retrieved.
       */
      
      
      String dataSource = PropertyHelper.readPropsValue(PropertyName.DATA_SOURCE, props, null);
      
      eventLogger.sendEvent("DTI Datasource (from PropertyFile): " + dataSource, EventType.DEBUG, this);

      ds = (javax.sql.DataSource)ctx.lookup(dataSource);

    } catch (Exception e) {
      eventLogger.sendEvent("Naming service exception: " + e.getMessage(), EventType.FATAL, this);
      e.printStackTrace();
      throw new DTIException(
        "initConnection()",
        DBTransaction.class,
        3,
        PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
        "Exception in initilizing the connection to the Data Source",
        e);
    }
  }

  /**
   * Method used to close the Statement object, the ResultSet object 
   * and the connection object.  The <I>releaseConnection<I> method must 
   * be used after every attempt to read or write to the database.
   * If the resources are not deallocated, the database will keep 
   * reference to allocated but unused internal threads
   * and therefore will not be able to accept new connection.
   * 
   * @throws DTIException 
   */
  private void releaseConnection() throws DTIException {

    eventLogger.sendEvent("Entering releaseConnection()", EventType.DEBUG, this);

    if (conn != null) {
      try {
        //close the statement object
        if (aStatement != null)
          aStatement.close();

        //close the ResultSet Object
        if (rs != null)
          rs.close();

        if (conn == null) {
          eventLogger.sendEvent("Object DB connection to be closed was null.", EventType.DEBUG, this);
          return;
        } else if (conn.isClosed()) {
          eventLogger.sendEvent(
            "Object DB connection to be closed was already closed.",
            EventType.DEBUG,
            this);
          return;
        } else {
          conn.close();
          eventLogger.sendEvent("Successfully closed object DB connection.", EventType.DEBUG, this);
          return;
        }

      } catch (Exception e) {
        eventLogger.sendEvent(
          "releaseConnection() encountered EXCEPTION: " + e.toString(),
          EventType.WARN,
          this);

        throw new DTIException(
          "releaseConnection()",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
          "Exception in releasing the connection to the Data Source",
          e);
      }
    }

    eventLogger.sendEvent("Did NOT close object DB connection.", EventType.WARN, this);
    return;

  }

  /**
   * Method used to access a connection object from the WebSphere
   * datasource factory.  The connection properties are defined in WebSphere.
   *
   * @throws DTIException 
   */
  private void getConnection() throws DTIException {

    eventLogger.sendEvent("Entering getConnection()", EventType.DEBUG, this);

    try {
      //set the right properties for a connection and creates a reference to the connection manager.
      if (ds == null)
        initConnection();

      // Get a Connection object conn using the DataSource factory.
      if (ds == null) {
        eventLogger.sendEvent("Data source is null.", EventType.DEBUG, this);
      }
      conn = ds.getConnection();
      eventLogger.sendEvent("Opening an object DB connection.", EventType.DEBUG, this);

    } catch (SQLException exc) {
      if (exc instanceof StaleConnectionException) {
        boolean tryagain = true;
        int count = 0;
        while (tryagain) {
          try {
            conn = ds.getConnection();
            eventLogger.sendEvent("Opening an object DB connection.", EventType.DEBUG, this);
          } catch (SQLException ex) {
            count++;
            if (count == 2) {
              tryagain = false;
            } else {
              if (exc instanceof StaleConnectionException) {
                tryagain = true;
              }
            }
          }
        }

        if (conn == null) {
          throw new DTIException(
            "getConnection()",
            DBTransaction.class,
            3,
            PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
            "Exception in getting the connection from the connection pool",
            exc);
        }
      } else {
        throw new DTIException(
          "getConnection()",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
          "Exception in getting the connection from the connection pool",
          exc);
      }
    } catch (Exception exc) {
      throw new DTIException(
        "getConnection()",
        DBTransaction.class,
        3,
        PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
        "Exception in getting the connection from the connection pool",
        exc);
    }
  }

  /**
   * Gets a connection to a persistent object, run the class internal query and return 
   * the result wrapped in an IValueSetCollection. 
   * At the end, release the connection to the persistent object.
   *
   * This unique access method was added because of a known problem with utilizing Oracle CLOB
   * objects under database connection pooling.  We needed to use CLOB objects in order
   * to pass XML data larger than 4000k.
   * 
   * Additonal notes.  As a rule, make sure method variables do not namespace collide with
   * instance variables.  In this case, conn (created locally) was out of scope
   * when a close was attempted. 
   * 
   * @exception DTIException
   * @param payloadId The unique identifier for this transaction.
   * @param entityId The numeric identifier for the ticket seller (may be zero).
   * @param newXMLString The new xml that was received from the ticket seller.
   */
  public boolean logInbound(String payloadId, int entityId, String inXMLString) throws DTIException {

    eventLogger.sendEvent("Entering logInbound(String, int, String)", EventType.DEBUG, this);
    CLOB xmlClob = null;
    Connection clobConn = null;
    PreparedStatement preparedStatement = null;
    String newXMLString = null;

    try {
      // PCI CONTROL DO NOT REMOVE
      newXMLString = PCIControl.overwritePciDataInXML(inXMLString);
      
      //Open a connection to the Oracle Database
      eventLogger.sendEvent("DBTransaction: Connecting to DTIDataSource...", EventType.DEBUG, this);

      //Set Oracle JDBC Driver to be used for connection object
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

      // Get the properties.
      String dataSourceURL = PropertyHelper.readPropsValue(PropertyName.DATA_SOURCE_URL, props, null);
      String databaseUser = PropertyHelper.readPropsValue(PropertyName.DBUSER, props, null);
      String databasePassword = PropertyHelper.readPropsValue(PropertyName.DBPASSWORD, props, null);

      eventLogger.sendEvent("Opening a CLOB DB connection.", EventType.DEBUG, this);

      // Connect to the database
      clobConn = DriverManager.getConnection(dataSourceURL, databaseUser, databasePassword);

      clobConn.setAutoCommit(true);

      //Set and Get Oracle CLOB object		
      xmlClob = getXMLCLOB(newXMLString, clobConn);

      String statement =
        new String(
          "INSERT INTO INBOUND_TS_LOG (TRANS_ID, TRANS_DATE, TS_TRANSID, XML_DOC, ENTITYID) VALUES (InBound_TS_SeqNo.NEXTVAL, SYSDATE, '"
            + payloadId
            + "', ? ,"
            + entityId
            + ")");

      //Create the prepared statement with a SQL that doesn't contain any parameter
      eventLogger.sendEvent("DBTransaction: Creating prepared SQL statement...", EventType.DEBUG, this);
      preparedStatement = clobConn.prepareStatement(statement);

      //Executes the statement
      preparedStatement.setObject(1, xmlClob);

      eventLogger.sendEvent("DBTransaction: Execute prepared SQL query...", EventType.DEBUG, this);

      if (preparedStatement.executeUpdate() == 1) {
        eventLogger.sendEvent(
          "Inserted new ticket seller request into INBOUND_TS_LOG!",
          EventType.DEBUG,
          this);
      }

      // Close the connection.
      preparedStatement.close();
      clobConn.close();
      eventLogger.sendEvent("Successfully closed CLOB DB connection.", EventType.DEBUG, this);

    } catch (SQLException exc) {

      eventLogger.sendEvent(
        "SQLException on inbound TS log insert: " + exc.toString(),
        EventType.DEBUG,
        this);

      try {
        // Attempt to shut down connections
        if (preparedStatement != null)
          preparedStatement.close();
        if (clobConn != null)
          clobConn.close();
        eventLogger.sendEvent("Successfully closed CLOB DB connection.", EventType.DEBUG, this);
      } catch (SQLException exc2) {
        eventLogger.sendEvent(
          "Did NOT successfully close CLOB DB connection: " + exc2.toString(),
          EventType.WARN,
          this);
      }

      if (exc.getErrorCode() == 1) {
        eventLogger.sendEvent(
          "Recognized a duplicate request, did not insert to INBOUND_TS_LOG.",
          EventType.WARN,
          this);
        return false; // I was NOT able to insert this record (duplicate)
      } else {
        throw new DTIException(
          "logInbound(String, int, String)",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
          "Exception in the execution of query",
          exc);
      }

    }

    return true;
  }

  /**
   * Gets a connection to a persistent object, run the class internal query
   * and return the result wrapped in an IValueSetCollection. 
   * At the end, release the connection to the persistent object.
   * 
   * @param payloadId
   * @throws DTIException
   */
  public void invoke(String payloadId) throws DTIException {

    eventLogger.sendEvent("Entering invoke(String)", EventType.DEBUG, this);

    try {
      //get a connection from WebSphere
      eventLogger.sendEvent("DBTransaction: Connecting to DTIDataSource....", EventType.DEBUG, this);
      getConnection();

      //Create the prepared statement with a SQL that doesn't contain any parameter
      eventLogger.sendEvent("DBTransaction: Creating prepared SQL statement...", EventType.DEBUG, this);
      aStatement = conn.prepareStatement(getSqlStatement());

      //Executes the statement
      eventLogger.sendEvent("DBTransaction: Execute prepared SQL query...", EventType.DEBUG, this);
      rs = aStatement.executeQuery();

      // parse results
      if (getSqlStatement().indexOf("SELECT") != -1) {
        createResponse();
      }

    } catch (SQLException exc) {
      releaseConnection(); // First attempt to release connection.
      if (exc.getErrorCode() == 1) {
        handleDuplicate(payloadId); // creates its own connection.
      } else {
        throw new DTIException(
          "invoke()",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
          "Exception in the execution of query",
          exc);
      }
    } finally {
      // Release the connection
      releaseConnection();
    }
  }

  /**
   * Gets a connection to a persistent object, run the class internal query
   * and return the result wrapped in an IValueSetCollection. 
   * At the end, release the connection to the persistent object.
   * 
   * @throws DTIException
   */
  public void invoke() throws DTIException {
    invoke(null);
  }

  /**
   * Sorts through the return data and constructs the appropriate return type.
   *
   * @throws DTIException  
   */
  protected void createResponse() throws DTIException {

    eventLogger.sendEvent("Entering createResponse()", EventType.DEBUG, this);

    aValueSetCollection = new HashMap<String, String>();
    ResultSetMetaData meta = null;
    int cols = 0;
    try {
      meta = rs.getMetaData();
      if (meta != null) {
        cols = meta.getColumnCount();
      }
      while (rs.next()) {

        for (int i = 1; i <= cols; i++) {
          Object ob = rs.getObject(i);
          Clob clob = null;
          if (ob != null && ob instanceof Clob) {
            clob = rs.getClob(1);
          }
          if (clob != null) {
            ob = null;
            StringBuffer clobData = new StringBuffer();

            // get character stream to retrieve clob data
            Reader instream = clob.getCharacterStream();

            // create temporary buffer for read
            char[] buffer = new char[4096];

            // length of characters read
            int length = 0;

            // fetch data  
            while ((length = instream.read(buffer)) != -1) {
              for (int j = 0; j < length; j++) {
                clobData.append(buffer[j]);
              }
            }

            // Close input stream
            instream.close();

            aValueSetCollection.put(DTIFormatter.toString(aValueSetCollection.size()), clobData.toString());
            clob = null;

          }
          if (ob != null) {
            if (!rs.wasNull()) {
              // set the payloadId 
              aValueSetCollection.put(DTIFormatter.toString(aValueSetCollection.size()), ob.toString());
            }
          }
        }
      }
    } catch (Exception exc) {
      throw new DTIException(
        "createResponse()",
        DBTransaction.class,
        3,
        PropertyHelper.readPropsValue(PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
        "Exception in populating the result set",
        exc);
    }
  }

  /** Handles duplicates by selecting the completed response XML out of the database. 
   * 
   * @param newPayloadId
   * @throws DTIException
   */
  public void handleDuplicate(String newPayloadId) throws DTIException {

    eventLogger.sendEvent("Entering handleDuplicate(String)", EventType.DEBUG, this);

    // the record already exists so check to see if it's complete
    // select the xml_file where trans_id = payloadId and errorCode == null
    setSqlStatement(
      "SELECT XML_DOC FROM OUTBOUND_TS_LOG WHERE TS_TRANSID='"
        + newPayloadId
        + "' AND ERR_RETURN_CODE IS NULL");
    try {
      invoke();
    } catch (Exception e) {
      eventLogger.sendEvent(
        "EXCEPTION retrieveing xml from outbound log: " + e.toString(),
        EventType.WARN,
        this);
      throw new DTIException(
        "handleDuplicate()",
        DBTransaction.class,
        3,
        PropertyHelper.readPropsValue(PropertyName.ERROR_OUTBOUND_LOG, props, null),
        "Exception in SQL Execution",
        e);
      // if there is an error code present then resubmit if not then
      // do nothing bucause the resultSet contains the file url
    }
  }

  /**
   * Creates CLOB object and assigns it a value.  This method was added in
   * an effort insert XMLData larger than 4000k.  This is a known issue with
   * sending string literals larger 4000k in an insert statement.
   * 
   * @since 01/16/04 16:23:00 PM
   * 
   * @param xmlData - A string containing XML.
   * @param c - an instance of java.sql.Connection
   * @return oracle.sql.CLOB;
   * @throws DTIException
   */
private CLOB getXMLCLOB(String xmlData, java.sql.Connection c) throws DTIException {
    CLOB tempClob = null;
    Writer tempClobWriter = null;

    eventLogger.sendEvent("Entering getXMLCLOB(String,Connection)", EventType.DEBUG, this);

    try {
      // If the temporary CLOB has not yet been created, create new
      tempClob = CLOB.createTemporary(c, true, CLOB.DURATION_SESSION);

      // Open the temporary CLOB in readwrite mode to enable writing
      tempClob.open(CLOB.MODE_READWRITE);

      // Get the output stream to write
      //Per initial Java doc search for deprecation:
      // Deprecated. This method is deprecated. Use setCharacterStream( 0L ).
      tempClobWriter = tempClob.getCharacterOutputStream();
      

      // Write the data into the temporary CLOB
      tempClobWriter.write(xmlData);

    } catch (Exception exc) {
      exc.printStackTrace();
      throw new DTIException(
        "getXMLCLOB()",
        DBTransaction.class,
        3,
        PropertyHelper.readPropsValue(PropertyName.ERROR_INBOUND_LOG, props, null),
        "Exception in populating the CLOB",
        exc);
    } finally {
      try {
        // Flush and close the stream
        tempClobWriter.flush();
        tempClobWriter.close();

        // Close the temporary CLOB 
        tempClob.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
        throw new DTIException(
          "getXMLCLOB()",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_INBOUND_LOG, props, null),
          "Exception flushing Writer object.",
          ioe);
      } catch (SQLException sqe) {
        sqe.printStackTrace();
        throw new DTIException(
          "getXMLCLOB()",
          DBTransaction.class,
          3,
          PropertyHelper.readPropsValue(PropertyName.ERROR_INBOUND_LOG, props, null),
          "Exception closing CLOB instance.",
          sqe);
      }

    }

    return tempClob;
  }

  // Accessors (Getters and Setters) -------------

  /**
   * Return the aValueSetCollection.
   * 
   * @return HashMap
   */
  public HashMap<String, String> getResponseData() {
    return aValueSetCollection;
  }

  /**
   * Gets the valueSetCollection
   * @return HashMap
   */
  public HashMap<String, String> getValueSetCollection() {
    return aValueSetCollection;
  }

  /**
   * Returns the SQL Statement
   * 
   * @return java.lang.String
   */
  public java.lang.String getSqlStatement() {
    return sqlStatement;
  }

  /**
   * Sets the valueSetCollection
   * @param valueSetCollection The valueSetCollection to set
   */
  public void setValueSetCollection(HashMap<String, String> valueSetCollection) {
    aValueSetCollection = valueSetCollection;
  }

  /**
   * Sets the SQL Statement
   * 
   * @param newSqlStatement
   */
  public void setSqlStatement(java.lang.String newSqlStatement) {
    sqlStatement = newSqlStatement;
  }

}
