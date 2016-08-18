package com.disney.admin.logging;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.admin.jdbc.ResultBean;
import com.disney.jdbc.ConnectionManager;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.Converter;
import com.disney.util.GUIDFactory;
import com.disney.util.PropertyHelper;


/**
 * GUI Interface to the JDBC Logs existing in the EVENT Table.
 *
 * @author fav2
 * @version 2.1
 */
public class JdbcLogViewerCommand extends SharedAdminCommand
{
	/** A constant value for the jdbc.properties file. */
	private static final String JDBC_PROPS_FILE = "jdbc.properties";
	
    //~ Methods ----------------------------------------------------------------


    /**
     * Process incoming requests for information
     *
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     *
     * @return String
     */
    public String performTask(
        HttpServletRequest request, HttpServletResponse response
    )
    {
        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        String jdbcDS = "";
        JdbcLogBean jdbcBean = null;
        String searchSql = null;

        try
        {
            String action = request.getParameter("ACTION");
            jdbcDS = request.getParameter("DATA_SOURCE_NAME");

            if ((action == null) || (action.equals(""))) {}
            else if (action.equalsIgnoreCase("OPEN"))
            {
                jdbcBean =
                    (JdbcLogBean)request.getSession(true).getAttribute(
                        "jdbcBean"
                    );

                if (jdbcBean == null)
                {
                    con = ConnectionManager.getInstance().getConnection(jdbcDS);
                    jdbcBean = new JdbcLogBean();
                    jdbcBean.loadStats(con);
                }
            }
            else if (action.equalsIgnoreCase("REFRESH"))
            {
                con = ConnectionManager.getInstance().getConnection(jdbcDS);
                jdbcBean = new JdbcLogBean();
                jdbcBean.loadStats(con);
            }
            else if (action.equalsIgnoreCase("SEARCH"))
            {
                String eventType = this.getParameter(request, "EVENT_TYPE");
                String errorCode = this.getParameter(request, "ERROR_CODE");
                String componentId = this.getParameter(request, "COMPONENT_ID");
                String bpId = this.getParameter(request, "BP_ID");
                String bpStep = this.getParameter(request, "BP_STEP");
                String sessionId = request.getParameter("SESSION_ID");
                String messageId = request.getParameter("MESSAGE_ID");
                String wasNode = this.getParameter(request, "WAS_NODE");
                String wasApp = this.getParameter(request, "WAS_APP");
                java.util.Date before = null;
                String beforeString = request.getParameter("BEFORE");

                if ((beforeString != null) && !beforeString.equals(""))
                {
                    before = Converter.parseISODateMS(beforeString);
                }

                String afterString = request.getParameter("AFTER");
                java.util.Date after = null;

                if ((afterString != null) && !afterString.equals(""))
                {
                    after = Converter.parseISODateMS(afterString);
                }

                jdbcBean =
                    (JdbcLogBean)request.getSession(true).getAttribute(
                        "jdbcBean"
                    );

                if (jdbcBean == null)
                {
                    jdbcBean = new JdbcLogBean();
                }

                jdbcBean.setSearchParams(
                    eventType, errorCode, componentId, bpId, bpStep, sessionId,
                    messageId, after, before, wasNode, wasApp
                );

                searchSql = this.buildQuery(jdbcBean);

                evl.sendEvent(
                    "Executing EVENT Search SQL on " + jdbcDS + ": "
                    + searchSql, EventType.DEBUG, this
                );

                ResultBean resultBean = new ResultBean();

                con = ConnectionManager.getInstance().getConnection(jdbcDS);
                st = con.createStatement();
                rs = st.executeQuery(searchSql);

                resultBean.setResultSet(rs);

                request.setAttribute("resultBean", resultBean);

                String queryId = GUIDFactory.createGUID(searchSql);
                request.getSession(true).setAttribute(queryId, resultBean);
                request.setAttribute("queryId", queryId);
            }
        }
        catch (Throwable th)
        {
            evl.sendException(
                "Error performing retrieve with " + searchSql,
                EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this
            );
            request.setAttribute("throwable", th);
        }
        finally
        {
            try
            {
                if (rs != null)
                {
                    try
                    {
                        rs.close();
                    }
                    catch (Throwable t) {}
                }

                if (st != null)
                {
                    try
                    {
                        st.close();
                    }
                    catch (Throwable t) {}
                }

                if (con != null)
                {
                    try
                    {
                        con.close();
                    }
                    catch (Throwable t) {}
                }
            }
            catch (Exception e)
            {
                evl.sendException(
                    "Error Closing Database connections.", EventType.EXCEPTION,
                    ErrorCode.DATABASE_CONNECTION_EXCEPTION, e, this
                );
            }

            request.setAttribute(
                "dataSourceNames",
                ConnectionManager.getInstance().getDataSourceNames()
            );
            request.setAttribute("currentDataSourceName", jdbcDS);

            if (jdbcBean == null)
            {
                jdbcBean =
                    (JdbcLogBean)request.getSession(true).getAttribute(
                        "jdbcBean"
                    );
            }

            if (jdbcBean != null)
            {
                request.setAttribute("jdbcBean", jdbcBean);
                request.getSession(true).setAttribute("jdbcBean", jdbcBean);
            }

            return "JdbcLogViewerMain.jsp";
        }
    }


    /**
     * Cleans the format of the ISO Date
     *
     * @param date Input Date
     *
     * @return Clean Date String
     */
    private String cleanDate(java.util.Date date)
    {
        // 2002-05-23 13:26:20
        String raw = Converter.printISODate(date);

        if ((raw != null) && (raw.length() > 0))
        {
            int dash = raw.lastIndexOf("-");

            return raw.substring(0, dash).replace('T', ' ');
        }

        return "";
    }


    /**
     * Retreives the requested parameter.  If the value returned is 'OTHER',
     * returns the value of the parameter [name]_OTHER instead.
     *
     * @param request HttpRequest
     * @param name String
     *
     * @return String value
     */
    private String getParameter(HttpServletRequest request, String name)
    {
        String value = request.getParameter(name);

        if ((value != null) && value.equals("OTHER"))
        {
            value = request.getParameter(name + "_OTHER");
        }

        return value;
    }


    /**
     * buildQuery
     *
     * @param jlBean 
     *
     * @return String
     */
    protected String buildQuery(JdbcLogBean jlBean)
    {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(this.getTableName(jlBean) + " ");
        sql.append(this.getWhereClause(jlBean) + " ");
        sql.append(this.getDatePart(jlBean) + " ");
        sql.append(this.getOrderByClause(jlBean));

        return sql.toString();
    }


    /**
     * The <code>getDatePart()</code> method creates or adds to the where
     * clause for this query based on the most likely date partition to
     * contain the data for the query. The <code>jdbc.properties</code> file
     * must be updated to include the <code>USE_PARTITIONS</code> entry with a
     * value of TRUE in order for the date partition clause to be added to the 
     * query. The default behaviour is to assume that partitions are not in use.
     *
     * @param jlBean and instance of the JdbcLogBean to be used by this method
     *        to build this portion of the query
     *
     * @return the datePart portion of the query that can be used to identify
     *         the  correct date partition to use for this query
     */
    protected String getDatePart(JdbcLogBean jlBean)
    {
        StringBuffer datePart = new StringBuffer();

        // Below we check to see if the Log viewer should be concerned about partitions
        String usePart = PropertyHelper.readPropsValue("USE_PARTITIONS", JDBC_PROPS_FILE, 
        		null, "FALSE");
        Boolean part = new Boolean(usePart);
        Calendar c = new GregorianCalendar();
        int dow = 0;

        // Now that we know if we're using partitions or not, do the work
        if (part.booleanValue())
        {
            // First see if the where clause was already started if not start it, otherwise just add to it
            if (this.getWhereClause(jlBean).equalsIgnoreCase(""))
            {
                datePart.append(" WHERE DATEPART = ");
            }
            else
            {
                datePart.append(" AND DATEPART = ");
            }

            if (jlBean.getAfter() != null)
            {
                // if we have a 'created after' entry - use that to determine the partition
                c.setTime(jlBean.getAfter());
                dow = c.get(Calendar.DAY_OF_WEEK) - 2;
            }
            else if (jlBean.getBefore() != null)
            {
                // if we have a 'created before' entry - use that to determine the partition
                c.setTime(jlBean.getBefore());
                dow = c.get(Calendar.DAY_OF_WEEK) - 2;
            }
            else
            {
                // otherwise just check today
                Date d = new Date();
                c.setTime(d);
                dow = c.get(Calendar.DAY_OF_WEEK) - 2;
            }

            if (dow < 0)
            {
                datePart.append("6");
            }
            else
            {
                datePart.append(dow);
            }
        }
        else
        {
            return "";
        }

        return datePart.toString();
    }


    /**
     * DOCUMENT ME!
     *
     * @param jlBean DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getTableName(JdbcLogBean jlBean)
    {
        return "EVENT";
    }


    /**
     * DOCUMENT ME!
     *
     * @param jlBean DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getWhereClause(JdbcLogBean jlBean)
    {
        StringBuffer where = new StringBuffer("where ");
        boolean andNeeded = false;

        if (jlBean.getEventType().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(TYPE = '" + jlBean.getEventType() + "')");
            andNeeded = true;
        }

        if (jlBean.getErrorCode().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(CODE = '" + jlBean.getErrorCode() + "')");
            andNeeded = true;
        }

        if (jlBean.getComponentId().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(COMPONENT_ID = '" + jlBean.getComponentId() + "')");
            andNeeded = true;
        }

        if (jlBean.getBpId().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(BP_ID = '" + jlBean.getBpId() + "')");
            andNeeded = true;
        }

        if (jlBean.getBpStep().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(BP_STEP = '" + jlBean.getBpStep() + "')");
            andNeeded = true;
        }

        if (jlBean.getSessionId().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append(
                "(CONVERSATION_ID like '" + jlBean.getSessionId() + "%')"
            );
            andNeeded = true;
        }

        if (jlBean.getMessageId().length() > 0)
        {
            this.insertAnd(where, andNeeded);
            where.append("(MESSAGE_ID like '" + jlBean.getMessageId() + "%')");
            andNeeded = true;
        }

        if (jlBean.getAfter() != null)
        {
            this.insertAnd(where, andNeeded);
            where.append(
                "(CREATION_TIME >= " + jlBean.getAfter().getTime() + ")"
            );
            andNeeded = true;
        }

        if (jlBean.getBefore() != null)
        {
            this.insertAnd(where, andNeeded);
            where.append(
                "(CREATION_TIME <= " + jlBean.getBefore().getTime() + ")"
            );
            andNeeded = true;
        }

        if ((jlBean.getBefore() == null) && (jlBean.getAfter() == null))
        {
            this.insertAnd(where, andNeeded);
            where.append("(CREATION_TIME IS NOT NULL)");
            andNeeded = true;
        }

        if (
            (jlBean.getWasNode().length() > 0)
            && !jlBean.getWasNode().equals("ALL")
        )
        {
            this.insertAnd(where, andNeeded);
            where.append("(WAS_NODE='" + jlBean.getWasNode() + "')");
            andNeeded = true;
        }

        if (
            (jlBean.getWasApp().length() > 0)
            && !jlBean.getWasApp().equals("ALL")
        )
        {
            this.insertAnd(where, andNeeded);
            where.append("(WAS_APP='" + jlBean.getWasApp() + "')");
            andNeeded = true;
        }

        if (!andNeeded)
        {
            return "";
        }

        return where.toString();
    }


    /**
     * DOCUMENT ME!
     *
     * @param buff DOCUMENT ME!
     * @param andNeeded DOCUMENT ME!
     */
    private void insertAnd(StringBuffer buff, boolean andNeeded)
    {
        if (andNeeded)
        {
            buff.append(" AND ");
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param jlBean DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getOrderByClause(JdbcLogBean jlBean)
    {
        return "order by CREATION_TIME, EVENT_ID";
    }
}
