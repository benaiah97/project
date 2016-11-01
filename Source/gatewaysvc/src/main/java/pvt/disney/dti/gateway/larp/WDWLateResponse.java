package pvt.disney.dti.gateway.larp;

import java.io.Reader;
import java.math.BigInteger;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.bind.JAXBException;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.FlyweightAttribute;

import pvt.disney.dti.gateway.client.ClientUtility;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LogKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.dao.data.ITPLogEntry;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.service.DTIService;
import pvt.disney.dti.gateway.service.dtixml.TransmissionRespXML;
import pvt.disney.dti.gateway.service.dtixml.TransmissionRqstXML;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.DTITracker;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.context.BaseContext;
import com.disney.context.ContextManager;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;

/**
 * 
 * @author lewit019
 * 
 */
public class WDWLateResponse {

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(LogKey.class.getCanonicalName());

	/**
	 * IBM proprietary objects. These objects are used to get a connection from a WebSphere database connections pool.
	 */
	private DataSource ds = null;

	/** JDBC connection object returned by the WebSphere EJB datasource. */
	private Connection conn = null;

	/** Core properties management initializer. */
	private AbstractInitializer abstrInit = null;
	/** Properties variable to store properties from AbstractInitializer. */
	private Properties props = null;

	/**
	 * The object where the result of the query is stored. This object is used by the <I>createResponse()<I> method to transfer the content of the result set to an IValueSet data holder.
	 */
	protected ResultSet rs = null;

	/** Contains an SQL statement that will be executed by the database. */
	private java.sql.PreparedStatement aStatement = null;

	/** Contains all the rows returned by the sql query. */
	private HashMap aValueSetCollection = null;

	private String sqlStatement;

	/** DBTransaction Constructor (default). Gets the properties instance. */
	public WDWLateResponse() {

		try {
			
		    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
		    props = ResourceLoader.convertResourceBundleToProperties(rb);
			
		}
		catch (Throwable e) {
			logger.sendEvent("THROWABLE initing props: " + e.toString(),
					EventType.FATAL, this);
		}

	}

	/**
	 * @param xmlResponse
	 */
	public String processLateResponse(Element xmlObj) {

		logger.sendEvent("Entering processLateResponse()", EventType.INFO, this);

		try {

			String webSvcResponse = xmlObj.asXML();

			String xmlResponse = null;

			try {
				xmlResponse = ClientUtility.convertResponse(webSvcResponse);
				String pciSafeResponse = PCIControl
						.overwritePciDataInXML(xmlResponse);
				logger.sendEvent(
						"DTI LARP received late response: " + pciSafeResponse,
						EventType.WARN, this);
			}
			catch (Exception e) {
				logger.sendEvent("DTI LARP Service unable to remove prefixes.",
						EventType.EXCEPTION, this);
				DTIException dtie = new DTIException(WDWLateResponse.class,
						DTIErrorCode.INVALID_MSG_ELEMENT,
						"DTI LARP Service unable to remove prefixes.");
				return generateLarpResponse(dtie, "0");
			}

			/** Parse the response. */
			OTCommandTO otCommandTO = null;
			try {
				otCommandTO = OTCommandXML.getTO(xmlResponse);
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to parse XML to provider transfer object: " + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, "0");
			}

			/** Obtain key values from the response to populate a dtiTxn object. */
			OTHeaderTO otHeaderTO = otCommandTO.getHeaderTO();
			String refNumberString = otHeaderTO.getReferenceNumber();
			int tpRefNbr = Integer.parseInt(refNumberString.trim());
			ITPLogEntry itpLog = null;

			try {
				itpLog = LogKey.getITPLogEntry(tpRefNbr);
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to parse get ITP log entry matching " + refNumberString + ": " + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}

			// Cannot locate matching record.
			if (itpLog == null) {
				logger.sendEvent(
						"DTI LARP Service unable to locate an ITP log entry matching " + refNumberString + ".",
						EventType.EXCEPTION, this);
				DTIException dtie = new DTIException(
						DTIErrorCode.FAILED_DB_OPERATION_SVC);
				return generateLarpResponse(dtie, refNumberString);
			}

			// Extract the associated request XML.
			String itsRequest = null;
			try {
				itsRequest = retrieveRequest(itpLog.getPayloadId());
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to get input ticket seller XML request from database." + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}
			TransactionType requestType = DTIService
					.findRequestType(itsRequest);
			logger.sendEvent(
					"DTIService determined type of request is " + requestType,
					EventType.INFO, this);

			// Build the initial DTITransaction object that would have existed
			DTITransactionTO dtiTxn = null;
			try {
				dtiTxn = TransmissionRqstXML.getTO(itsRequest, requestType,
						"LARP");
			}
			catch (JAXBException jbe) {
				logger.sendEvent(
						"Exception parsing request xml:  " + jbe.toString(),
						EventType.WARN, this);
			}

			// Enable log file trackingby payload ID.
			String payloadId = dtiTxn.getRequest().getPayloadHeader()
					.getPayloadID();
			// Using core logging, establish a way to track this
			// transaction by payloadID
			DTITracker dtitr = new DTITracker();
			dtitr.setPayloadId(payloadId);
			BaseContext bctx = new BaseContext(dtitr);
			ContextManager.getInstance().storeContext(bctx);
			logger.sendEvent(
					"DTI LARP Service now attempting to process late answer to PayloadID: " + payloadId,
					EventType.WARN, this);

			TktSellerTO tktSellerTO = dtiTxn.getRequest().getPayloadHeader()
					.getTktSeller();
			EntityTO entityTO = null;
			try {
				entityTO = EntityKey.getEntity(tktSellerTO.getTsMac(),
						tktSellerTO.getTsLocation());
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to get entity from ticket seller XML request." + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}

			Integer transIdITS = null;
			try {
				transIdITS = LogKey.getITSLogTransId(itpLog.getPayloadId());
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to get ITS Log trans id." + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}

			// Set all DTITransactionTO fields required in the request transaction...
			dtiTxn.setTransIdITS(transIdITS);
			dtiTxn.setTransIdITP(itpLog.getTransId());
			dtiTxn.setProvider(DTITransactionTO.ProviderType.WDWNEXUS);
			dtiTxn.setTpRefNum(tpRefNbr);
			dtiTxn.setEntityTO(entityTO);

			// If reservation, add required DB products
			if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.RESERVATION) {
				ReservationRequestTO resReqTO = (ReservationRequestTO) dtiTxn
						.getRequest().getCommandBody();
				// Get database information for all products on the order
				ArrayList<TicketTO> tktListTO = resReqTO.getTktList();
				ArrayList<DBProductTO> dbProdList = null;
				try {
					dbProdList = ProductKey.getOrderProducts(tktListTO);
					// NOTE: The getProductTicketTypes query modifies the dbProdList
					// members
					// to incidate tkt mappings.
					dbProdList = ProductKey.getProductTicketTypes(dbProdList);
				}
				catch (DTIException dtie) {
					logger.sendEvent(
							"DTI LARP Service unable to get order products." + dtie
									.toString(), EventType.EXCEPTION, this);
					return generateLarpResponse(dtie, refNumberString);
				}

				dtiTxn.setDbProdList(dbProdList);
			}

			// If upgradeAlpha, add required DB products
			if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.UPGRADEALPHA) {
				UpgradeAlphaRequestTO uaReqTO = (UpgradeAlphaRequestTO) dtiTxn
						.getRequest().getCommandBody();
				// Get database information for all products on the order
				ArrayList<TicketTO> tktListTO = uaReqTO.getTktList();
				ArrayList<DBProductTO> dbProdList = null;
				try {
					dbProdList = ProductKey.getOrderProducts(tktListTO);
					// NOTE: The getProductTicketTypes query modifies the dbProdList
					// members
					// to incidate tkt mappings.
					dbProdList = ProductKey.getProductTicketTypes(dbProdList);
				}
				catch (DTIException dtie) {
					logger.sendEvent(
							"DTI LARP Service unable to get order products." + dtie
									.toString(), EventType.EXCEPTION, this);
					return generateLarpResponse(dtie, refNumberString);
				}
				dtiTxn.setDbProdList(dbProdList);
			}

			// Log the Outbound TP Message
			try {
				LogKey.insertOTPLog(dtiTxn, xmlResponse);
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to insert into OTP Log." + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}
			logger.sendEvent("DTI LARP Service logged outbound TP message.",
					EventType.INFO, this);

			// Parse Response into DTITransactionTO object
			try {
				dtiTxn = TransformRules.changeToDtiFormat(dtiTxn, xmlResponse);
			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"DTI LARP Service unable to change provider to DTI format." + dtie
								.toString(), EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}
			logger.sendEvent(
					"DTI LARP Service applied transform rules (Provider to DTI).",
					EventType.INFO, this);

			//
			// Marshall the response.
			//
			String responseXML = null;
			try {
				responseXML = TransmissionRespXML.getXML(dtiTxn);
				if (responseXML == null) throw new DTIException(
						DTIService.class,
						DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"Unable to marshall XML from the objects passed as response");

				logger.sendEvent("DTI LARP Service marshalled the response.",
						EventType.INFO, this);

				//
				// Log the Output Ticket Seller XML
				//
				LogKey.insertOTSLog(dtiTxn, responseXML);
				logger.sendEvent(
						"DTI LARP Service logged outbound TS message.",
						EventType.INFO, this);

			}
			catch (JAXBException je) {

				logger.sendEvent(
						"Exception parsing resp object:  " + je.toString(),
						EventType.EXCEPTION, this);
				logger.sendEvent(
						"Error code was: " + je.getErrorCode() + ":  " + je
								.getLocalizedMessage(), EventType.EXCEPTION,
						this);
				DTIException dtie = new DTIException(
						WDWLateResponse.class,
						DTIErrorCode.UNDEFINED_FAILURE,
						"DTI LARP Service unable to marshal DTI object to XML: " + je
								.toString());
				return generateLarpResponse(dtie, refNumberString);

			}
			catch (DTIException dtie) {
				logger.sendEvent(
						"Exception retrieving resp xml:  " + dtie.toString(),
						EventType.EXCEPTION, this);
				return generateLarpResponse(dtie, refNumberString);
			}

			return generateLarpResponse(refNumberString);

		}
		catch (Throwable thrown) {
			thrown.printStackTrace();
			logger.sendEvent(
					"DTI LARP Service general failure: " + thrown.toString(),
					EventType.EXCEPTION, this);
			DTIException dtie = new DTIException(WDWLateResponse.class,
					DTIErrorCode.UNDEFINED_FAILURE,
					"DTI LARP Service general failure: " + thrown.toString());
			return generateLarpResponse(dtie, "0");
		}
		finally {
			// Get rid of logging context (prevents an object leak).
			ContextManager.getInstance().removeContext();
		}

	}

	/**
	 * 
	 * @param dtie
	 * @param referenceNumber
	 * @return
	 */
	private String generateLarpResponse(String referenceNumber) {
		return generateLarpResponse(null, referenceNumber);
	}

	/**
	 * 
	 * @param dtie
	 * @param referenceNumber
	 * @return
	 */
	private String generateLarpResponse(DTIException dtie,
			String referenceNumber) {

		String responseString = null;

		BigInteger cmdErrorCode = new BigInteger("0");
		String cmdErrorType = "Successful";
		String cmdErrorClass = "Command";
		String cmdErrorText = "Command completed successfully";

		if (dtie != null) {
			DTIErrorTO dtiError = ErrorKey.getErrorDetail(dtie
					.getDtiErrorCode());
			cmdErrorCode = dtiError.getErrorCode();
			cmdErrorType = dtiError.getErrorType();
			cmdErrorClass = dtiError.getErrorClass();
			cmdErrorText = dtiError.getErrorText();
		}

		Document document = DocumentHelper.createDocument();
		Element commandStanza = document.addElement("Command");
		ArrayList<Attribute> attribList = new ArrayList<Attribute>();

		// Set xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		Attribute xmlns = new FlyweightAttribute("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		attribList.add(xmlns);

		// Populate response fields
		commandStanza.addElement("ReferenceNumber").addText(referenceNumber);
		commandStanza.addElement("CmdErrorCode").addText(
				cmdErrorCode.toString());
		commandStanza.addElement("CmdErrorType").addText(cmdErrorType);
		commandStanza.addElement("CmdErrorClass").addText(cmdErrorClass);
		commandStanza.addElement("CmdErrorText").addText(cmdErrorText);

		responseString = document.asXML();

		logger.sendEvent(
				"DTI LARP Service returning status to caller of: " + responseString,
				EventType.INFO, this);

		return responseString;
	}

	/**
	 * Selects the input ticket seller request XML out of the database.
	 * 
	 * @param newPayloadId
	 * @throws DTIException
	 */
	public String retrieveRequest(String newPayloadId) throws DTIException {

		logger.sendEvent("Entering retrieveRequest(String)", EventType.DEBUG,
				this);

		String xmlRequest = null;

		// the record already exists so check to see if it's complete
		// select the xml_file where trans_id = payloadId and errorCode == null
		setSqlStatement("SELECT XML_DOC FROM INBOUND_TS_LOG WHERE TS_TRANSID='" + newPayloadId + "'");
		try {
			xmlRequest = invoke();
		}
		catch (Exception e) {
			logger.sendEvent(
					"EXCEPTION retrieveing xml from outbound log: " + e
							.toString(), EventType.WARN, this);
			throw new DTIException("retrieveRequest()", WDWLateResponse.class,
					3, PropertyHelper.readPropsValue(
							PropertyName.ERROR_OUTBOUND_LOG, props, null),
					"Exception in SQL Execution", e);
			// if there is an error code present then resubmit if not then
			// do nothing bucause the resultSet contains the file url
		}

		return xmlRequest;
	}

	/**
	 * Sets the SQL Statement
	 * 
	 * @param newSqlStatement
	 */
	public void setSqlStatement(java.lang.String newSqlStatement) {
		sqlStatement = newSqlStatement;
	}

	/**
	 * Gets a connection to a persistent object, run the class internal query and return the result wrapped in an IValueSetCollection. At the end, release the connection to the persistent object.
	 * 
	 * @param payloadId
	 * @throws DTIException
	 */
	public String invoke() throws DTIException {

		logger.sendEvent("Entering invoke(String)", EventType.DEBUG, this);

		String xmlRequest = null;

		try {
			// get a connection from WebSphere
			logger.sendEvent("DBTransaction: Connecting to DTIDataSource....",
					EventType.DEBUG, this);
			getConnection();

			// Create the prepared statement with a SQL that doesn't contain any
			// parameter
			logger.sendEvent(
					"DBTransaction: Creating prepared SQL statement...",
					EventType.DEBUG, this);
			aStatement = conn.prepareStatement(getSqlStatement());

			// Executes the statement
			logger.sendEvent("DBTransaction: Execute prepared SQL query...",
					EventType.DEBUG, this);
			rs = aStatement.executeQuery();

			// parse results
			if (getSqlStatement().indexOf("SELECT") != -1) {
				xmlRequest = createResponse();
			}

		}
		catch (SQLException exc) {
			releaseConnection(); // First attempt to release connection.
			throw new DTIException("invoke()", WDWLateResponse.class, 3,
					PropertyHelper.readPropsValue(
							PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
					"Exception in the execution of query", exc);

		}
		finally {
			// Release the connection
			releaseConnection();
		}

		return xmlRequest;

	}

	/**
	 * Method used to access a connection object from the WebSphere datasource factory. The connection properties are defined in WebSphere.
	 * 
	 * @throws DTIException
	 */
	private void getConnection() throws DTIException {

		logger.sendEvent("Entering getConnection()", EventType.DEBUG, this);

		try {
			// set the right properties for a connection and creates a reference to
			// the connection manager.
			if (ds == null) initConnection();

			// Get a Connection object conn using the DataSource factory.
			if (ds == null) {
				logger.sendEvent("Data source is null.", EventType.DEBUG, this);
			}
			conn = ds.getConnection();
			logger.sendEvent("Opening an object DB connection.",
					EventType.DEBUG, this);

		}
		catch (SQLException exc) {

			throw new DTIException(
					"getConnection()",
					WDWLateResponse.class,
					3,
					PropertyHelper.readPropsValue(
							PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
					"Exception in getting the connection from the connection pool",
					exc);
		}
		catch (Exception exc) {
			throw new DTIException(
					"getConnection()",
					WDWLateResponse.class,
					3,
					PropertyHelper.readPropsValue(
							PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
					"Exception in getting the connection from the connection pool",
					exc);
		}
	}

	/**
	 * Method used to close the Statement object, the ResultSet object and the connection object. The <I>releaseConnection<I> method must be used after every attempt to read or write to the database. If the resources are not deallocated,
	 * the database will keep reference to allocated but unused internal threads and therefore will not be able to accept new connection.
	 * 
	 * @throws DTIException
	 */
	private void releaseConnection() throws DTIException {

		logger.sendEvent("Entering releaseConnection()", EventType.DEBUG, this);

		if (conn != null) {
			try {
				// close the statement object
				if (aStatement != null) aStatement.close();

				// close the ResultSet Object
				if (rs != null) rs.close();

				if (conn == null) {
					logger.sendEvent(
							"Object DB connection to be closed was null.",
							EventType.DEBUG, this);
					return;
				}
				else if (conn.isClosed()) {
					logger.sendEvent(
							"Object DB connection to be closed was already closed.",
							EventType.DEBUG, this);
					return;
				}
				else {
					conn.close();
					logger.sendEvent(
							"Successfully closed object DB connection.",
							EventType.DEBUG, this);
					return;
				}

			}
			catch (Exception e) {
				logger.sendEvent(
						"releaseConnection() encountered EXCEPTION: " + e
								.toString(), EventType.WARN, this);

				throw new DTIException(
						"releaseConnection()",
						WDWLateResponse.class,
						3,
						PropertyHelper.readPropsValue(
								PropertyName.ERROR_POS_DEFAULT_CODE, props,
								null),
						"Exception in releasing the connection to the Data Source",
						e);
			}
		}

		logger.sendEvent("Did NOT close object DB connection.", EventType.WARN,
				this);

		return;

	}

	/**
	 * Return the aValueSetCollection.
	 * 
	 * @return HashMap
	 */
	public HashMap getResponseData() {
		return aValueSetCollection;
	}

	/**
	 * Gets the valueSetCollection
	 * 
	 * @return HashMap
	 */
	public HashMap getValueSetCollection() {
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
	 * 
	 * @param valueSetCollection
	 *            The valueSetCollection to set
	 */
	public void setValueSetCollection(HashMap valueSetCollection) {
		aValueSetCollection = valueSetCollection;
	}

	@SuppressWarnings("unchecked")
	private void initConnection() throws DTIException {

		logger.sendEvent("Entering initConnection()", EventType.DEBUG, this);

		try {
			// Create the initial naming context.
			Hashtable parms = new Hashtable();
		      
			/*
	      parms.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
	      InitialContext ctx = new InitialContext(parms);
			*/

			InitialContext ctx = new InitialContext();
			
			/*
			 * Perform a naming service lookup to get a DataSource object. The single DataSource object is a "factory" used by all requests to get an individual connection for each request. The Web administrator can provide the lookup
			 * parameters. The code below uses a value read in from an external property file. The text string source is typically something like "jdbc/sample" where "jdbc" is the context for the lookup and "sample" is the logicalname of
			 * the DataSource object to be retrieved.
			 */
			String dataSource = PropertyHelper.readPropsValue(
					PropertyName.DATA_SOURCE, props, null);
			ds = (javax.sql.DataSource) ctx.lookup(dataSource);

		}
		catch (Exception e) {
			logger.sendEvent("Naming service exception: " + e.getMessage(),
					EventType.EXCEPTION, this);
			e.printStackTrace();
			throw new DTIException(
					"initConnection()",
					WDWLateResponse.class,
					3,
					PropertyHelper.readPropsValue(
							PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
					"Exception in initilizing the connection to the Data Source",
					e);
		}
	}

	/**
	 * Sorts through the return data and constructs the appropriate return type.
	 * 
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	protected String createResponse() throws DTIException {

		logger.sendEvent("Entering createResponse()", EventType.DEBUG, this);

		aValueSetCollection = new HashMap();
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

						aValueSetCollection.put(DTIFormatter
								.toString(aValueSetCollection.size()), clobData
								.toString());
						clob = null;

					}
					if (ob != null) {
						if (!rs.wasNull()) {
							// set the payloadId
							aValueSetCollection.put(DTIFormatter
									.toString(aValueSetCollection.size()), ob
									.toString());
						}
					}
				}
			}
		}
		catch (Exception exc) {
			throw new DTIException("createResponse()", WDWLateResponse.class,
					3, PropertyHelper.readPropsValue(
							PropertyName.ERROR_POS_DEFAULT_CODE, props, null),
					"Exception in populating the result set", exc);
		}

		String completeXML = (String) getResponseData().get("0");

		return completeXML;

	}

}
