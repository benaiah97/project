package pvt.disney.dti.gateway.connection;

import com.disney.util.AbstractInitializer;
import com.disney.util.CachedPropertiesListener;
import com.disney.util.PropertyHelper;
import com.disney.util.Timer;

import oracle.jdbc.driver.OracleConnection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import pvt.disney.dti.gateway.connection.ConnectionDefinition;
import pvt.disney.dti.gateway.connection.ConnectionException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * Manages all Connections to Databases, both direct JDBC Connections  and pooled DataSources from
 * an Application Server.
 *
 * @author Disney
 * @version 1
 */
public class ConnectionManager implements CachedPropertiesListener
{
    //~ Static variables/initializers --------------------------------------------------------------


    /** A constant value for the jdbc.properties file. */
    private static final String JDBC_PROPS_FILE = "jdbc.properties";
	/** instance of ConnectionMgr (singleton) */
    protected static ConnectionManager instance = null;

    //~ Instance variables -------------------------------------------------------------------------

    private Timer timer = null;
    private Hashtable connectionDefinitions = new Hashtable();

    //~ Constructors -------------------------------------------------------------------------------

    //private com.wdw.eai.foundation.logging.EventLogger evl = null;


    /**
     * ConnectionManager constructor
     *
     * @see #loadSettings()
     */
    protected ConnectionManager()
    {
        //this.evl = com.wdw.eai.foundation.logging.LogManager.getInstance().getLogger("JDBC");
        try
        {
            this.loadSettings();
        }
        catch (Exception e)
        {
            System.out.println("Error loading settings or initializing jdbc connection: "
                + e.toString());
        }

        AbstractInitializer.getInitializer().addPropertiesListener(JDBC_PROPS_FILE, this);
    }

    //~ Methods ------------------------------------------------------------------------------------


    /**
     * Return an audited connection from the connection pool, or a new connection.  Creation date:
     * (2/21/00 9:52:22 AM)
     *
     * @param dataSourceName Name of target Datasource
     * @param userID Name of the current user (for auditing purposes)
     *
     * @return java.sql.Connection
     *
     * @exception ConnectionException Wrapped Detailed Exception
     */
    public Connection getConnection(String dataSourceName, String userID)
        throws ConnectionException
    {
        ConnectionDefinition cd = (ConnectionDefinition)connectionDefinitions.get(dataSourceName);

        if (cd.getDriver().indexOf("oracle") > -1)
        {
            OracleConnection OraConn = (OracleConnection)getConnection(dataSourceName);

            try
            {
                OraConn.setClientIdentifier(userID);
            }
            catch (SQLException sqle)
            {
                // Non fatal exception, can only be thrown by setClientIdentifier.
                System.out.println("Error setting connection auditing information for user: "
                    + userID + ": " + cd.getDataSourceName() + "--"
                    + (cd.isAppServer() ? "DS"
                                        : "JDBC") + "--" + cd.getUserId() + "--" + cd.getDriver()
                    + "--" + cd.getUrl() + " --> " + sqle.toString());
            }

            return OraConn;
        }
		return getConnection(dataSourceName);
    }


    /**
     * Return a connection from the connection pool, or a new connection.  Creation date: (2/21/00
     * 9:52:22 AM)
     *
     * @param dataSourceName Name of target Datasource
     *
     * @return java.sql.Connection
     *
     * @exception ConnectionException Wrapped Detailed Exception
     */
    public Connection getConnection(String dataSourceName)
        throws ConnectionException
    {
        Connection conn = null;
        // DS caching not required - cached at the ConnectionDefinition after lookup.
        DataSource ds = null;
        String result = null;

        ConnectionDefinition cd = (ConnectionDefinition)connectionDefinitions.get(dataSourceName);

        if (cd == null)
        {
            ConnectionException ce = new ConnectionException("DataSource Definition Not Found : " + dataSourceName);

            //evl.sendException(EventType.DATABASE_CONNECTION_EXCEPTION, ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this);
            throw ce;
        }

        if (cd.isAppServer())
        {
            try
            {
                //ds = (DataSource) cd.getInitContext().lookup("jdbc/" + cd.getDataSourceName());
                ds = cd.lookupDataSource();

                if (ds == null)
                {
                    //ConnectionException ce = new ConnectionException(
                    //        "Null DataSource returned from lookup : jdbc/" + cd.getDataSourceName());

                    //evl.sendException(EventType.DATABASE_CONNECTION_EXCEPTION, ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this);
                    //throw ce;
                    // Changed to attempt the JDBC connection before throwing the connection exception
                    System.out.println("Datasource Name not found: " + dataSourceName); 
                }
            }
            catch (NamingException e)
            {
            	
            	System.out.println("Datasource Name not found or failure during initialization: " + dataSourceName); 
                ConnectionException ce = new ConnectionException(e.getMessage(), e);

                //evl.sendException(EventType.DATABASE_CONNECTION_EXCEPTION, ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this);
                throw ce;
            }
        }

        try
        {
            if (ds != null)
            {
                int retry = 0;
                int sleepInterval = 2;

                /*
                   Adding Time Checking for loop
                   added 2\14\2001 Shane Cleavelin
                   Changes Made:
                   1. Created Two Timestamp Objects to track start and end
                   2. Took out the check for 30 attemps (Will attempt to get connection for 3 minutes)
                   3. Did a check to see if conn is null after the attempt to connect.
                      If is null have the thread sleep then check again
                 */
                Timestamp start = new Timestamp(System.currentTimeMillis());
                GregorianCalendar cal = new GregorianCalendar();

                cal.setTime(new java.util.Date(start.getTime()));
                cal.add(Calendar.MINUTE, 3);

                Timestamp end = new Timestamp(cal.getTime().getTime());

                while ((System.currentTimeMillis() < end.getTime()) && (conn == null))
                {
                    try
                    {
                        // If ds is already configured, then we are using a Good AppServer
                        if ((cd.getUserId() == null) || (cd.getPassword() == null))
                        {
                            // WAS 4.0 allows getting connection using configured id/pwd
                            conn = ds.getConnection();
                        }
                        else
                        {
                            try
                            {
                            	//Tomcat doesn't want the User Id and Password provided...  - BIEST001
//                                conn = ds.getConnection(cd.getUserId(), cd.getPassword());
//                                System.out.println("Connection Established with user id & password");

                                conn = ds.getConnection();

                            }
                            catch (SQLException sqle)
                            {
                                // Make this code protable between Websphere 3, 4, 5.
                                if (sqle.toString().indexOf("StaleConnection") > 0)
                                {
                                    // Throw the connection exception up to the StaleConnectionException handler below.
                                    throw sqle;
                                }
								// Try connection without id/password
								conn = ds.getConnection();
								System.out.println("Id/Password for " + cd.getDataSourceName()
								    + " is incorrect in jdbc.properties: " + cd.getUserId()
								    + '/' + cd.getPassword());
                            }
                        }

                        /*
                           Part of Time Checking Code
                           Check if there is a valid connection if not
                           sleep, then try again.
                         */
                        if (conn == null)
                        {
                            Thread.currentThread().sleep(sleepInterval * 1000);

                            continue;
                        }

                        // Generate StaleConnectionException if pool connection has been closed / lost
                        // RJF3 - This apparently no longer generates exception in WAS 4 - have to catch this when 
                        // connection does something (i.e. Prepare Statement)
                        //conn.setTransactionIsolation(conn.TRANSACTION_READ_COMMITTED);
                        // Use default debug options if we can't get them from the properties file
                        if (cd.getDebugDatabase())
                        {
                            // We don't care if we get an error displaying the session count
                            result = getSessionsDate(conn, false, cd);

                            if (cd.getDebugConnect())
                            {
                                System.out.println("Pool: " + result);
                            }
                        }
                    }
                    catch (SQLException s)
                    {
                        // Make this code protable between Websphere 3, 4, 5.
                        if (s.toString().indexOf("StaleConnection") > 0)
                        {
                            // Stale Connection is when the pooled connection has been closed externally.
                            if (conn!=null) conn.close();
                            System.out.println("StaleConnectionException " + (++retry));

                            try
                            {
                                Thread.currentThread().sleep(sleepInterval * 1000);
                            }
                            catch (InterruptedException sleepException)
                            {
                                // who waked me up?
                                System.out.println("StaleConnection InterruptedException: "
                                    + sleepException.toString());
                                throw new ConnectionException("StaleConnectionException Interrupted",
                                    sleepException);
                            }
                        }
                        else
                        {
                            throw s;
                        }
                    }
                }
            }
            else
            {
                //no dataSource available, so use jdbc connection
                Class.forName(cd.getDriver()).newInstance();
                conn = DriverManager.getConnection(cd.getUrl(), cd.getUserId(), cd.getPassword());
            }

            getSessionsDate(conn, false, cd);

            return conn;
        }
        catch (ClassNotFoundException e)
        {
            ConnectionException ce = new ConnectionException("Error getting driver : "
                    + cd.getDriver() + " --> " + e.toString(), e);
            throw ce;
        }
        catch (Throwable t)
        {
            String appSvr = (cd.isAppServer() ? "DataSource" : "JDBC");
            
            //Adding some more meaningful error messaging - BIEST001
            String parms = "Datasource name: " + cd.getDataSourceName() + "; AppServer: " + appSvr + "; User Id: " + cd.getUserId() + "; DB Driver: "
                + cd.getDriver() + "; Datasource URL: " + cd.getUrl();

            if (cd.isAppServer())
            {
                parms += ("; Provider URL: " + cd.getProviderURL() + "; Context Factory: " + cd.getContextFactory());
            }

            System.out.println("Error Message: " + t.getMessage());
            
            ConnectionException ce = new ConnectionException("Error getting connection using parameters: " + parms + " --> " + t.toString(), t);
            throw ce;
        }
    }


    /**
     * Return the current session connection information (number of connections).  Creation date:
     * (2/21/00 9:52:22 AM)
     *
     * @param conn Database connection
     * @param debug Database user ID used for determining the connection count
     * @param cd Debug println on/off
     *
     * @return String Connection/Date string
     *
     * @throws Exception Native java.lang.Exception
     */
    public String getSessionsDate(Connection conn, boolean debug, ConnectionDefinition cd)
        throws Exception
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Timestamp now = null;
        int count = 0;
        String result = null;
        String sql = null;
        CallableStatement procin = null;

        // We don't care if we get an error displaying the current server time
        String stackReturn = "";
        PrintWriter pwError = null;

        try
        {
            if (cd.getDebugStack())
            {
                // Do something to determine which class/method called getConnection()...
                Exception eStackTrace = new Exception();
                eStackTrace.fillInStackTrace();

                // Parse stack trace to determine calling object(s)
                StringWriter swError = new StringWriter();
                pwError = new PrintWriter(swError);
                eStackTrace.printStackTrace(pwError);
                swError.flush();

                String stackTrace = swError.toString();
                String stackMethod = null;
                StringTokenizer st = new StringTokenizer(stackTrace, "\t");

                // Get the first two methods in the stack that don't have 'Exception' or 'ConnectionMgr' in them
                int i = 0;
                int iPos = -1;

                while ((i < 4) && st.hasMoreTokens())
                {
                    stackMethod = st.nextToken();

                    if ((stackMethod.indexOf("Exception") == -1)
                        && (stackMethod.indexOf("ConnectionMgr") == -1))
                    {
                        if (stackMethod.indexOf("Servlet") > -1)
                        {
                            stackMethod = stackMethod.substring(0, stackMethod.indexOf("(") + 2);
                            i = 4;
                        }
                        else
                        {
                            i++;
                        }

                        if (stackMethod.length() > 5)
                        {
                            iPos = stackMethod.indexOf("com.disney.");

                            if (iPos > -1)
                            {
                                stackMethod = stackMethod.substring(iPos + 11,
                                        stackMethod.length() - 1);
                            }

                            if (i > 1)
                            {
                                stackReturn += (' '
                                + stackMethod.substring(0, stackMethod.length() - 1));
                            }
                            else
                            {
                                stackReturn += stackMethod.substring(0, stackMethod.length() - 1);
                            }
                        }

                        //System.out.println(i+"="+stackMethod+" stackReturn="+stackReturn);
                    }
                }

                // System.out.println(stackReturn);
                // log_session procedure saves the current connection pool info in connection_log
                // and connection_history tables. Commit info as soon as we get it to avoid locks.
                // We always log the session but if debug flags are off the SProc returns immediately.
                if (cd.getDebugDBLog())
                {
                    procin = conn.prepareCall("begin log_session(?, ?); commit; end;");
                    procin.setString(2,
                        stackReturn.replace('\n', ' ').replace('\t', ' ').replace('\r', ' '));
                }
            }
            else if (cd.getDebugDBLog())
            {
                // We're not capturing the stack trace because that debug flag is turned off
                procin = conn.prepareCall("begin log_session(?); commit; end;");
            }

            try
            {
                /* Update the connection log / history tables from v$ tables - SProc */
                if (cd.getDebugDBLog() && cd.isAppServer() && procin!=null)
                {
                    procin.setString(1, cd.getUserId());
                    procin.execute();
                }
            }
            catch (Throwable t)
            {
                // We won't kill the rest of the method if the logger throws an exception.
                System.out.println("getSessionsDate log_session: " + t.toString());
            }
            finally
            {
                try
                {
                	if (procin != null)
                	{
						procin.close();
                	}
                }
                catch (Exception ignore) {}
            }

            if (!cd.getDebugConnect())
            {
                // Don't send the rest of the output to the stdout.log file
                return null;
            }

            if (cd.getDriver().indexOf("oracle") > -1)
            {
                if ((cd.getUserId() == null) || (cd.getUserId().equals("")))
                {
                    sql = "SELECT Sysdate as now, count(*) as sesscount FROM v$session";
                }
                else
                {
                    sql = "SELECT Sysdate as now, count(*) as sesscount FROM v$session where UPPER(username) = '"
                        + cd.getUserId().toUpperCase() + '\'';
                }
            }
            else if (cd.getDriver().indexOf("informix") > -1)
            {
                if ((cd.getUserId() == null) || (cd.getUserId().equals("")))
                {
                    sql = "SELECT CURRENT as now, count(*) as sesscount FROM sysmaster:syssessions";
                }
                else
                {
                    sql = "SELECT CURRENT as now, count(*) as sesscount FROM sysmaster:syssessions where username = '"
                        + cd.getUserId() + '\'';
                }
            }
            else if (cd.getDriver().indexOf("mssql") > -1)
            {
                if ((cd.getUserId() == null) || (cd.getUserId().equals("")))
                {
                    sql = "SELECT getdate() as now, count(*) as sesscount FROM master..sysprocesses where kpid>0";
                }
                else
                {
                    sql = "SELECT getdate() as now, count(*) as sesscount FROM master..sysprocesses where kpid>0 and loginame = '"
                        + cd.getUserId() + '\'';
                }
            }
            else
            {
                // Fail silently if the database table(s) aren't there...
                //System.out.println("ConnectionMgr driver not found: " + driver);
                return stackReturn;
            }

            if (conn == null)
            {
                throw new Exception("Null pointer");
            }

            if (debug)
            {
                System.out.println("Setting Statement");
            }

            ps = conn.prepareStatement(sql);

            if (debug)
            {
                System.out.println("Running Query");
            }

            rs = ps.executeQuery();

            if (debug)
            {
                System.out.println("Query Finished");
            }

            if (rs.next())
            {
                count = rs.getInt("sesscount");
                now = rs.getTimestamp("now");
                result = count + ' ' + cd.getUserId() + " sessions at "
                    + now.toString().substring(11, 19);

                if (debug)
                {
                    System.out.println(result);
                }
            }

            try
            {
                rs.close();
            }
            catch (Throwable t) {}

            try
            {
                ps.close();
            }
            catch (Throwable t) {}

            if (debug)
            {
                System.out.println("Connection Test successful!");
            }

            if (cd.getDebugSession())
            {
                if (cd.getDriver().indexOf("oracle") > -1)
                {
                    sql = "SELECT sid, serial#, audsid FROM v$session where audsid = userenv('sessionid')";
                }
                else if (cd.getDriver().indexOf("informix") > -1)
                {
                    sql = "SELECT sid FROM sysmaster:syssessions where sid = dbinfo('sessionid')";
                }
                else if (cd.getDriver().indexOf("mssql") > -1)
                {
                    sql = "SELECT spid FROM master..sysprocesses where spid = @@spid";
                }
                else
                {
                    return result + '\t' + stackReturn;
                }

                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                if (rs.next())
                {
                    if (cd.getDriver().indexOf("oracle") > -1)
                    {
                        //long lserial = rs.getLong("serial#");
                        //long laudsid = rs.getLong("audsid");
                        //System.out.println("sid=" + lsid + ", serial#=" + lserial + ", audsid=" +laudsid);
                    }
                    else if (cd.getDriver().indexOf("informix") > -1)
                    {
                        //long lsid = rs.getLong("sid");
                        //System.out.println("sid=" + lsid);
                    }
                    else if (cd.getDriver().indexOf("mssql") > -1)
                    {
                        //long lsid = rs.getLong("spid");
                        //System.out.println("sid=" + lsid);
                    }
                }

                rs.close();
                ps.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println("getSessionsDate " + e.toString());
        }
        catch (Throwable t)
        {
            System.out.println("getSessionsDate Error: " + t.toString());
        }
        finally
        {
            try
            {
                if (pwError != null)
                {
                    pwError.close();
                }
            }
            catch (Throwable t) {}

            try
            {
                if (rs != null)
                {
                    rs.close();
                }
            }
            catch (Throwable t) {}

            try
            {
                if (ps != null)
                {
                    ps.close();
                }
            }
            catch (Throwable t) {}
        }

        return result + '\t' + stackReturn;
    }


    /**
     * Retreives a list of all DataSource names as read from properties file. Creation date:
     * (12/11/2001 1:02:11 PM)
     *
     * @return java.util.Enumeration
     */
    public Enumeration getDataSourceNames()
    {
        return connectionDefinitions.keys();
    }


    /**
     * Returns the singleton instance. Creation date: (12/11/2001 8:52:01 AM)
     *
     * @return com.wdw.eai.foundation.jdbc.ConnectionMgr
     * @concurrency ConnectionManager.class
     */
    public static ConnectionManager getInstance()
    {
        if (instance == null)
        {
            synchronized (ConnectionManager.class)
            {
                if (instance == null)
                {
                    instance = new ConnectionManager();
                }
            }
        }

        return instance;
    }


    /**
     * Loads all settings for ConnectionDefinition objects from properties file. Creation date:
     * (12/11/2001 9:01:39 AM)
     * @throws ConnectionException When lookup fails
     *
     * @throws ConnecitonException Detailed Wrapped Exception
     */
    public void loadSettings() throws ConnectionException
    {
        Properties jdbcProps = AbstractInitializer.getInitializer().getProps(JDBC_PROPS_FILE);
        String jdbcConnectionList = PropertyHelper.readPropsValue("JDBC_DATA_SOURCES", jdbcProps,
                false, "");

        StringTokenizer st = new StringTokenizer(jdbcConnectionList, ",");
        sun.misc.BASE64Decoder encoder = null;
        ConnectionException ese = null;

        while (st.hasMoreTokens())
        {
            String thisDataSource = st.nextToken();

            ConnectionDefinition cd = new ConnectionDefinition(thisDataSource);
            String encoding = PropertyHelper.readPropsValue(thisDataSource + "_JDBC_ENC",
                    jdbcProps, false);

            // Right now encoding only supports Base64. To get encoded value, run EncryptUtil class
            // with the decoded value as the command line argument...
            if (encoding != null)
            {
                cd.setEnc(encoding);

                if (encoder == null)
                {
                    encoder = new sun.misc.BASE64Decoder();
                }

                try
                {
                    cd.setUserId(new String(encoder.decodeBuffer(PropertyHelper.readPropsValue(thisDataSource
                                    + "_JDBC_USER_ID", jdbcProps, false))));

                    cd.setPassword(new String(encoder.decodeBuffer(PropertyHelper.readPropsValue(thisDataSource
                                    + "_JDBC_PASSWORD", jdbcProps, false))));

                    cd.setDriver(new String(encoder.decodeBuffer(PropertyHelper.readPropsValue(thisDataSource
                                    + "_JDBC_DRIVER", jdbcProps, false))));

                    cd.setUrl(new String(encoder.decodeBuffer(PropertyHelper.readPropsValue(thisDataSource
                                    + "_JDBC_URL", jdbcProps, false))));
                }
                catch (java.io.IOException ioe)
                {
                    ese = new ConnectionException("IOException decrypting datasource: "
                            + thisDataSource, ioe);
                    System.out.println("IOException decrypting datasource " + thisDataSource + ": "
                        + ioe.toString());
                }
            }
            else
            {
                cd.setUserId(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_USER_ID",
                        jdbcProps, false));
                cd.setPassword(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_PASSWORD",
                        jdbcProps, false));
                cd.setDriver(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_DRIVER",
                        jdbcProps, false));
                cd.setUrl(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_URL", jdbcProps,
                        false));
            }

            cd.setAppServer(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_APPSERVER",
                    jdbcProps, false, "YES").equalsIgnoreCase("YES"));
            cd.setDebugConnect(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_DEBUG_CONNECT",
                    jdbcProps, false, "NO").equalsIgnoreCase("YES"));
            cd.setDebugDBLog(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_DEBUG_DBLOG",
                    jdbcProps, false, "NO").equalsIgnoreCase("YES"));
            cd.setDebugDatabase(PropertyHelper.readPropsValue(thisDataSource
                    + "_JDBC_DEBUG_DATABASE", jdbcProps, false, "NO").equalsIgnoreCase("YES"));
            cd.setDebugSession(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_DEBUG_SESSION",
                    jdbcProps, false, "NO").equalsIgnoreCase("YES"));
            cd.setDebugStack(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_DEBUG_STACK",
                    jdbcProps, false, "NO").equalsIgnoreCase("YES"));

            if (cd.isAppServer())
            {
                cd.setJndiPrefix(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_JNDI_PREFIX", jdbcProps, false, "java:comp/env/jdbc/"));
                cd.setProviderURL(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_PROVIDER_URL", jdbcProps, false, null));
                
                //Tomcat doesn't need an Initial Context... this is unnecessary and causes HUGE issues.  Commenting out. - BIEST001
                /*
                // Set the Initial Context
                
                //cd.setContextFactory(PropertyHelper.readPropsValue(thisDataSource + "_JDBC_CONTEXT_FACTORY", jdbcProps, false, "com.ibm.websphere.naming.WsnInitialContextFactory"));

                try
                {
                    Hashtable ctxParams = new Hashtable();
                    ctxParams.put(Context.INITIAL_CONTEXT_FACTORY, cd.getContextFactory());

                    if (cd.getProviderURL() != null)
                    {
                        ctxParams.put(Context.PROVIDER_URL, cd.getProviderURL());
                    }

                    cd.setInitContext(new InitialContext(ctxParams));
                }
                catch (Throwable t)
                {
                    ese = new ConnectionException("Error loading Initial Context for DataSource: "
                            + thisDataSource, t);
                    System.out.println("Error loading Initial Context for DataSource: "
                        + thisDataSource + ": " + t.toString());
                }
                */
            }

            // Add Connection Definitions to HashTable
            connectionDefinitions.put(thisDataSource, cd);
        }

        this.timer = new Timer(AbstractInitializer.getInitializer().getRELOAD_INTERVAL());

        if (ese != null)
        {
            // Only throw an exception after iterating through all DATA_SOURCES
            throw ese;
        }
    }


    /**
     * @see com.disney.util.CachedPropertiesListener#onPropertiesUpdate(java.util.Properties)
     */
    public void onPropertiesUpdate(Properties props)
    {
        try
        {
            this.loadSettings();
        }
        catch (Exception e)
        {
            System.out.println("Error loading jdbc settings: " + e.toString());
        }
    }
}
