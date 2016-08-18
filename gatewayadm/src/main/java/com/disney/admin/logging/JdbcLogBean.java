package com.disney.admin.logging;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.disney.admin.AdminException;
import com.disney.util.AbstractInitializer;

/**
 * Web Bean for passing information between JdbcLogViewerServlet and JdbcLogViewerMain.jsp.
 *
 * @version 2.1
 * @author fav2
 */
public class JdbcLogBean
{
    //~ Instance variables -------------------------------------------------------------------------

	private final static String ADMIN_PROPS_FILE = "admin.properties";

    private int numRows = 0;
    private List componentList = null;
    private List bpIdList = null;
    private List bpStepList = null;
    private List eventTypeList = null;
    private List errorCodeList = null;
    private List wasNodeList = null;
    private List wasAppList = null;
    private String eventType = "";
    private String errorCode = "";
    private String componentId = "";
    private String bpId = "";
    private String bpStep = "";
    private String sessionId = "";
    private String messageId = "";
    private String wasNode = AbstractInitializer.getInitializer().getNodeName();
    private String wasApp = AbstractInitializer.getInitializer().getAppName();
    private java.util.Date after = null;
    private java.util.Date before = null;
    private int queryTime = 15;

    /**
     * Creates a new JdbcLogBean object.
     */
    public JdbcLogBean()
    {
        super ();
        Properties props = AbstractInitializer.getInitializer().getProps(ADMIN_PROPS_FILE);
        String queryTimeStr = props.getProperty("JDBC_LOGS_QUERY_INTERVAL", "15");
        try
        {
        	this.queryTime = Integer.parseInt(queryTimeStr);
        }
        catch (Exception e)
        {
        	// Ignore... 
        	this.queryTime = 15;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     *
     * @throws Throwable DOCUMENT ME!
     */
    public void loadStats(Connection con) throws Throwable
    {
        this.numRows = countRows (con);
        this.componentList = buildCompleteList (con, "COMPONENT_ID");
        this.bpIdList = buildCompleteList (con, "BP_ID");
        this.bpStepList = buildCompleteList (con, "BP_STEP");
        this.eventTypeList = buildCompleteList (con, "TYPE");
        this.errorCodeList = buildCompleteList (con, "CODE");
        this.wasNodeList = buildCompleteList (con, "WAS_NODE");
        this.wasAppList = buildCompleteList (con, "WAS_APP");
    }


    /**
     * Gets the numRows
     * 
     * @return Returns a int
     */
    public int getNumRows()
    {
        return numRows;
    }


    /**
     * Gets the componentList
     * 
     * @return Returns a List
     */
    public List getComponentList()
    {
        return componentList;
    }


    /**
     * Gets the bpIdList
     * 
     * @return Returns a List
     */
    public List getBpIdList()
    {
        return bpIdList;
    }


    /**
     * Gets the bpStepList
     * 
     * @return Returns a List
     */
    public List getBpStepList()
    {
        return bpStepList;
    }


    /**
     * Counts the log rows for this node/app instance, for the last X minutes.
     * 
     * @param con Database Connection
     * @return Returns a List
     * @throws Throwable Any Exception
     */
    private int countRows(Connection con) throws Throwable
    {
        int count = 0;

        // Only retrieve the lookup values for the last X minutes
        long time = System.currentTimeMillis () - this.getQueryTimeInMillis();
        String countSql = "select count(1) from EVENT where WAS_NODE='" + wasNode + "' AND creation_time > " + time;

        Statement st = null;
        ResultSet rs = null;
        try
        {
            st = con.createStatement ();
            rs = st.executeQuery (countSql);

            if (rs.next ())
            {
                count = rs.getInt (1);
            }
        }
        catch (Throwable th)
        {
            throw th;
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close ();
                }
                catch (Throwable t) {}
            }

            if (st != null)
            {
                try
                {
                    st.close ();
                }
                catch (Throwable t) {}
            }
        }

        return count;
    }


    /**
     * Counts the list of values for lookup for this node/app instance, for the last X minutes.
     * 
     * @param con Database Connection
     * @param field Field Name to search for
     * @return Returns a List
     * @throws Throwable Any Exception
     */
    private List buildCompleteList(Connection con, String field) throws AdminException
    {
        ArrayList l = new ArrayList();

        long time = System.currentTimeMillis () - this.getQueryTimeInMillis();
        String listSql = "select distinct " + field + " from EVENT WHERE creation_time > " + time;

        Statement st = null;
        ResultSet rs = null;

        try
        {
            st = con.createStatement ();
            rs = st.executeQuery (listSql);

            while (rs.next ())
            {
                String nextResult = rs.getString (1);

                if (nextResult != null)
                {
                    l.add (nextResult.trim ());
                }
            }
        }
        catch (Throwable th)
        {
            throw new AdminException("Error building field list: " + listSql, th);
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close ();
                }
                catch (Throwable t) {}
            }

            if (st != null)
            {
                try
                {
                    st.close ();
                }
                catch (Throwable t) {}
            }
        }

        return l;
    }


    /**
     * Gets the eventTypeList
     * 
     * @return Returns a List
     */
    public List getEventTypeList()
    {
        return eventTypeList;
    }

    /**
     * Gets the errorCodeList
     * 
     * @return Returns a List
     */
    public List getErrorCodeList()
    {
        return errorCodeList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param eventType DOCUMENT ME!
     * @param componentId DOCUMENT ME!
     * @param bpId DOCUMENT ME!
     * @param bpStep DOCUMENT ME!
     * @param sessionId DOCUMENT ME!
     * @param messageId DOCUMENT ME!
     * @param after DOCUMENT ME!
     * @param before DOCUMENT ME!
     * @param wasNode DOCUMENT ME!
     * @param wasApp DOCUMENT ME!
     */
    public void setSearchParams(String eventType, String errorCode, 
    							String componentId, String bpId, String bpStep, 
                                String sessionId, String messageId, java.util.Date after, 
                                java.util.Date before, String wasNode, String wasApp)
    {
        this.eventType = eventType;
        this.errorCode = errorCode;
        this.componentId = componentId;
        this.bpId = bpId;
        this.bpStep = bpStep;
        this.sessionId = sessionId;
        this.messageId = messageId;
        this.after = after;
        this.before = before;
        this.wasNode = wasNode;
        this.wasApp = wasApp;
    }


    /**
     * Gets the eventType
     * 
     * @return Returns a String
     */
    public String getEventType()
    {
        return eventType;
    }

    /**
     * Gets the errorCode
     * 
     * @return Returns a String
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * Gets the componentId
     * 
     * @return Returns a String
     */
    public String getComponentId()
    {
        return componentId;
    }


    /**
     * Gets the bpId
     * 
     * @return Returns a String
     */
    public String getBpId()
    {
        return bpId;
    }


    /**
     * Gets the bpStep
     * 
     * @return Returns a String
     */
    public String getBpStep()
    {
        return bpStep;
    }


    /**
     * Gets the sessionId
     * 
     * @return Returns a String
     */
    public String getSessionId()
    {
        return sessionId;
    }


    /**
     * Gets the after Date
     * 
     * @return Returns a String
     */
    public java.util.Date getAfter()
    {
        return after;
    }


    /**
     * Gets the before Date
     * 
     * @return Returns a String
     */
    public java.util.Date getBefore()
    {
        return before;
    }


    /**
     * Gets the messageId
     * 
     * @return Returns a String
     */
    public String getMessageId()
    {
        return messageId;
    }


    /**
     * Gets the wasNodeList
     * 
     * @return Returns a List
     */
    public List getWasNodeList()
    {
        return wasNodeList;
    }


    /**
     * Gets the wasAppList
     * 
     * @return Returns a List
     */
    public List getWasAppList()
    {
        return wasAppList;
    }


    /**
     * Gets the wasNode
     * 
     * @return Returns a String
     */
    public String getWasNode()
    {
        return wasNode;
    }


    /**
     * Gets the wasApp
     * 
     * @return Returns a String
     */
    public String getWasApp()
    {
        return wasApp;
    }
    
	/**
	 * Returns the queryTime.
	 * @return int
	 */
	public int getQueryTime()
	{
		return queryTime;
	}

	/**
	 * Returns the queryTimeInMillis.
	 * @return long
	 */
	public long getQueryTimeInMillis()
	{
		return new Long(queryTime * 60000).longValue();
	}

}