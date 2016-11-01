package pvt.disney.dti.gateway.connection;

import javax.naming.*;

import javax.sql.DataSource;


/**
 * Connection Definition. Stores all connection information for both  DataSources and pure JDBC
 * connections. Creation date: (12/11/2001 9:49:47 AM)
 */
public class ConnectionDefinition
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static boolean debugConnect = false;
    private static boolean debugSession = false;
    private static boolean debugStack = true;
    private static boolean debugDBLog = false;
    private static boolean debugDatabase = false;

    //~ Instance variables -------------------------------------------------------------------------

    private String dataSourceName = null;
    private boolean appServer = false;
    private String userId = null;
    private String password = null;
    private String driver = null;
    private String url = null;
    private String contextFactory = null;
    private String providerURL = null;
    private String jndiPrefix = null;
    private InitialContext initContext = null;
    private DataSource dataSource = null;
    private String enc = null; // RJF3 Encryption encoding. Default = none (for backwards compatibility)

    //~ Constructors -------------------------------------------------------------------------------


    /**
     * ConnectionDefinition constructor comment.
     *
     * @param aDataSourceName Name of the target DataSource
     */
    public ConnectionDefinition(String aDataSourceName)
    {
        super();
        this.dataSourceName = aDataSourceName;
    }

    //~ Methods ------------------------------------------------------------------------------------


    /**
     * Getter for dataSource. Creation date: (3/8/2002 5:29:43 PM)
     *
     * @return javax.sql.DataSource dataSource
     */
    public javax.sql.DataSource getDataSource()
    {
        return dataSource;
    }


    /**
     * Getter for dataSourceName. Creation date: (12/11/2001 10:30:29 AM)
     *
     * @return java.lang.String dataSourceName
     */
    public java.lang.String getDataSourceName()
    {
        return dataSourceName;
    }


    /**
     * Getter for Driver Name.  Creation date: (12/11/2001 10:02:24 AM)
     *
     * @return java.lang.String driver
     */
    public java.lang.String getDriver()
    {
        return driver;
    }


    /**
     * Getter for Encryption flag value. Creation date: (3/8/2002 5:29:43 PM)
     *
     * @return java.lang.String Encryption flag
     */
    public java.lang.String getEnc()
    {
        return enc;
    }


    /**
     * Getter for reference to InitialContext. Creation date: (12/11/2001 10:06:42 AM)
     *
     * @return javax.naming.InitialContext
     */
    public javax.naming.InitialContext getInitContext()
    {
        return initContext;
    }


    /**
     * Password used for creating connection.  Creation date: (12/11/2001 10:02:24 AM)
     *
     * @return java.lang.String
     */
    public java.lang.String getPassword()
    {
        return password;
    }


    /**
     * URL used for connecting to database directly.. not used with DataSource. Creation date:
     * (12/11/2001 10:02:24 AM)
     *
     * @return java.lang.String
     */
    public java.lang.String getUrl()
    {
        return url;
    }


    /**
     * User ID used for creating connection.  Creation date: (12/11/2001 10:02:24 AM)
     *
     * @return java.lang.String
     */
    public java.lang.String getUserId()
    {
        return userId;
    }


    /**
     * Boolean value indicating whether this is a DataSource conenction. Creation date: (12/11/2001
     * 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean isAppServer()
    {
        return appServer;
    }


    /**
     * Performs the JNDI Lookup for the DataSource.  Only performs this action one time.  Attempts
     * to load value 'jdbc/' + dataSourceName. Creation date: (2/19/2002 8:03:05 AM)
     *
     * @return javax.sql.DataSource
     *
     * @throws NamingException Native javax.naming.NamingException
     */
    public DataSource lookupDataSource() throws NamingException
    {
    	
    	//we are NEVER going to force an Initial Context with Tomcat... so...commenting this out. - BIEST001
    	
    	/*
        if (dataSource == null && initContext != null)
        {
        */
    	
    		//adding this line because initContext will be null - BIEST001
    		initContext = new InitialContext();
    	
        	String DSjndiPrefix = this.jndiPrefix;
        	
            try
            {
                // Globally scoped datasource
                dataSource = (DataSource)initContext.lookup(DSjndiPrefix + dataSourceName);
                
            }
            catch (NamingException ex)
            {
            	
            	if (DSjndiPrefix.equals("java:comp/env/jdbc/"))
            	{
            		DSjndiPrefix = "jdbc/";
            	}
            	else // if (DSjndiPrefix.equals("jdbc/"))
            	{
            		DSjndiPrefix = "java:comp/env/jdbc/";
            	}
            		
                try
                {
                    // Deprecated Websphere JNDI naming convention
                    dataSource = (DataSource)initContext.lookup(DSjndiPrefix + dataSourceName);
                }
                catch (NamingException nex)
                {
                	//DSjndiPrefix = "";
                    try
                    {
                        // Try weblogic application scoped datasource
                        dataSource = (DataSource)initContext.lookup(dataSourceName);
                    }
                    catch (NamingException nex2)
                    {
                        // Deprecated Websphere JNDI naming convention
                        dataSource = (DataSource)initContext.lookup("java:app/jdbc/" + dataSourceName);
                        
                    }
                }
                
        //    }
                
        }

        return dataSource;
    }


    /**
     * Sets the flag indicating use of DataSource. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newAppServer boolean
     */
    public void setAppServer(boolean newAppServer)
    {
        appServer = newAppServer;
    }


    /**
     * Sets the DataSource. Creation date: (3/8/2002 5:29:43 PM)
     *
     * @param newDataSource javax.sql.DataSource
     */
    public void setDataSource(javax.sql.DataSource newDataSource)
    {
        dataSource = newDataSource;
    }


    /**
     * Sets the Driver. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newDriver java.lang.String
     */
    public void setDriver(java.lang.String newDriver)
    {
        driver = newDriver;
    }


    /**
     * Sets the Encrypted flag. Creation date: (3/8/2002 5:29:43 PM)
     *
     * @param newEnc java.lang.String
     */
    public void setEnc(java.lang.String newEnc)
    {
        enc = newEnc;
    }


    /**
     * Sets the IntiialContext. Creation date: (12/11/2001 10:06:42 AM)
     *
     * @param newInitContext javax.naming.InitialContext
     */
    public void setInitContext(javax.naming.InitialContext newInitContext)
    {
        initContext = newInitContext;
    }


    /**
     * Sets the password used for creating connection. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newPassword java.lang.String
     */
    public void setPassword(java.lang.String newPassword)
    {
        password = newPassword;
    }


    /**
     * Sets the URL used for direct JDBC Connections. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newUrl java.lang.String
     */
    public void setUrl(java.lang.String newUrl)
    {
        url = newUrl;
    }


    /**
     * Sets the User ID used for connecting. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newUserId java.lang.String
     */
    public void setUserId(java.lang.String newUserId)
    {
        userId = newUserId;
    }


    /**
     * Debug the connection process - display user count / date. Off (false) by default. Creation
     * date: (12/11/2001 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean getDebugConnect()
    {
        return debugConnect;
    }


    /**
     * Debug the connection process - display user count / date. Off by default. Creation date:
     * (12/11/2001 10:02:24 AM)
     *
     * @param newDebugConnect boolean
     */
    public void setDebugConnect(boolean newDebugConnect)
    {
        debugConnect = newDebugConnect;
    }


    /**
     * Display current number of connected users. Off (false) by default. Creation date:
     * (12/11/2001 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean getDebugSession()
    {
        return debugSession;
    }


    /**
     * Display current number of connected users. Off (false) by default. Creation date:
     * (12/11/2001 10:02:24 AM)
     *
     * @param newDebugSession boolean
     */
    public void setDebugSession(boolean newDebugSession)
    {
        debugSession = newDebugSession;
    }


    /**
     * Display stack trace to component requesting connection. Off (false) by default. Creation
     * date: (12/11/2001 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean getDebugStack()
    {
        return debugStack;
    }


    /**
     * Display stack trace to component requesting connection. Off (false) by default. Creation
     * date: (12/11/2001 10:02:24 AM)
     *
     * @param newDebugStack boolean
     */
    public void setDebugStack(boolean newDebugStack)
    {
        debugStack = newDebugStack;
    }


    /**
     * Run sproc to log user count to database. Off (false) by default. Creation date: (12/11/2001
     * 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean getDebugDBLog()
    {
        return debugDBLog;
    }


    /**
     * Run sproc to log user count to database. Off (false) by default. Creation date: (12/11/2001
     * 10:02:24 AM)
     *
     * @param newDebugDBLog boolean
     */
    public void setDebugDBLog(boolean newDebugDBLog)
    {
        debugDBLog = newDebugDBLog;
    }


    /**
     * Console print output of sessions / users information after connecting. Off (false) by
     * default. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @return boolean
     */
    public boolean getDebugDatabase()
    {
        return debugDatabase;
    }


    /**
     * Console print output of sessions / users information after connecting. Off (false) by
     * default. Creation date: (12/11/2001 10:02:24 AM)
     *
     * @param newDebugDatabase boolean
     */
    public void setDebugDatabase(boolean newDebugDatabase)
    {
        debugDatabase = newDebugDatabase;
    }


    /**
     * Returns the contextFactory.
     *
     * @return String
     */
    public String getContextFactory()
    {
        return contextFactory;
    }


    /**
     * Sets the contextFactory.
     *
     * @param contextFactorySet The contextFactory to set
     */
    public void setContextFactory(String contextFactorySet)
    {
        this.contextFactory = contextFactorySet;
    }


    /**
     * Returns the providerURL.
     *
     * @return String
     */
    public String getProviderURL()
    {
        return providerURL;
    }


    /**
     * Sets the providerURL.
     *
     * @param providerURLSet The providerURL to set
     */
    public void setProviderURL(String providerURLSet)
    {
        this.providerURL = providerURLSet;
    }

	/**
	 * @return the jndiPrefix
	 */
	public String getJndiPrefix() {
		return jndiPrefix;
	}

	/**
	 * @param jndiPrefixIn JDBC JNDI lookup prefix, i.e. jdbc/ 
	 * @param jndiPrefix the jndiPrefix to set
	 */
	public void setJndiPrefix(String jndiPrefixIn) {
		this.jndiPrefix = jndiPrefixIn;
	}
}
