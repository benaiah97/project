package com.disney.admin.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.AdminException;
import com.disney.admin.SharedAdminCommand;
import com.disney.jdbc.ConnectionManager;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.GUIDFactory;

/**
 * @version 1.0
 * @author fav2
 */
public class JdbcCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    private HashMap tableListMap = null;

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     * @throws com.wdw.eai.foundation.exception.EAIException DOCUMENT ME!
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        if (tableListMap == null)
        {
            tableListMap = new java.util.HashMap();
        }

        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        String jdbcDS = "";
        String tableName = "";
        String action = request.getParameter ("ACTION");
        try
        {
            jdbcDS = request.getParameter ("DATA_SOURCE_NAME");
            tableName = request.getParameter ("TABLE_NAME");

            String showViews = request.getParameter ("SHOW_VIEWS");
            String showSystem = request.getParameter ("SHOW_SYSTEM");
            evl.sendEvent ("Action=" + action + ", DataSource=" + jdbcDS + ", tableName="
                           + tableName + ", showViews=" + showViews + "showSystem=" + showSystem, 
                           EventType.DEBUG, this);

            String jdbcCmd = request.getParameter ("QUERY_STRING");

            if ((action == null) || (action.equals ("")) || (action.equalsIgnoreCase ("REFRESH"))) {}
            else if (action.equalsIgnoreCase ("OPEN"))
            {
                java.util.ArrayList tableList = new java.util.ArrayList();

                try
                {
                    con = ConnectionManager.getInstance ().getConnection (jdbcDS);

                    DatabaseMetaData dmd = con.getMetaData ();

                    // Always show user schema tables. Optionally show views and system tables
                    if ((showSystem != null) && (showSystem.equalsIgnoreCase ("on")))
                    {
                        String[] types = 
                        {
                            "TABLE", 
                            "SYSTEM TABLE"
                        };
                        rs = dmd.getTables (null, null, "%", types);
                        evl.sendEvent ("rsSystem=" + rs.toString (), EventType.DEBUG, this);
                    }
                    else
                    {
                        String[] types = 
                        {
                            "TABLE"
                        };
                        rs = dmd.getTables (null, null, "%", types);
                        evl.sendEvent ("rsTable=" + rs.toString (), EventType.DEBUG, this);
                    }

                    while (rs.next ())
                    {
                        String thisTableName = rs.getString ("TABLE_NAME");
                        tableList.add (thisTableName);
                    }

                    if ((showViews != null) && (showViews.equalsIgnoreCase ("on")))
                    {
                        rs.close ();

                        String[] types = 
                        {
                            "VIEW"
                        };
                        java.util.ArrayList viewList = new java.util.ArrayList();
                        rs = dmd.getTables (null, null, "%", types);
                        evl.sendEvent ("rsView=" + rs.toString (), EventType.DEBUG, this);

                        while (rs.next ())
                        {
                            String thisViewName = rs.getString ("TABLE_NAME");
                            viewList.add (thisViewName);
                        }

                        request.setAttribute ("ViewNameList", viewList);
                        evl.sendEvent ("ViewNameList count=" + viewList.size (), EventType.DEBUG, 
                                       this);
                    }


                    // Keep the current setting for checked/unchecked in the jsp
                    request.setAttribute ("showViews", showViews);
                    request.setAttribute ("showSystem", showSystem);
                }
                catch (Throwable tableTh)
                {
                    evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, 
                                       tableTh, this);
                    request.setAttribute ("ThrowableObject", tableTh);
                }

                tableListMap.put (jdbcDS, tableList);
            }
            else if (action.equalsIgnoreCase ("QUERY"))
            {
                if ((jdbcCmd == null) || (jdbcCmd.length () < 1))
                {
                    // Don't throw exception if somebody hits query button with empty query...
                }
                else if (!(jdbcCmd.toLowerCase ().startsWith ("select")))
                {
                    throw new AdminException("Only SELECT statements are allowed through this interface.");
                }
                else
                {
                    evl.sendEvent ("Trying to execute SQL : " + jdbcCmd, EventType.DEBUG, this);

                    long connectStart = new java.util.Date().getTime ();
                    con = ConnectionManager.getInstance ().getConnection (jdbcDS);

                    long connectEnd = new java.util.Date().getTime ();

                    st = con.createStatement ();

                    long queryStart = new java.util.Date().getTime ();
                    rs = st.executeQuery (jdbcCmd);

                    long queryEnd = new java.util.Date().getTime ();

                    ResultBean resultBean = new ResultBean();
                    resultBean.setConnectionTime (connectEnd - connectStart);
                    resultBean.setQueryTime (queryEnd - queryStart);
                    resultBean.setResultSet (rs);

                    request.setAttribute ("QueryString", jdbcCmd);
                    request.setAttribute ("QueryResultBean", resultBean);
                    
                    String queryId = GUIDFactory.createGUID(jdbcCmd);
                    request.getSession(true).setAttribute(queryId, resultBean);
                    request.setAttribute ("QueryId", queryId);
                }
            }
            else if (action.equalsIgnoreCase ("EXPLORE_TABLE"))
            {
                //String tableQuery = "select table_name, column_name, data_type, data_length from user_tab_columns "
                //	+ "where table_name = '" + tableName + "'";
                //evl.sendEvent("Execute SQL : " + tableQuery, EventType.DEBUG, this);
                long connectStart = new java.util.Date().getTime ();
                con = ConnectionManager.getInstance ().getConnection (jdbcDS);

                long connectEnd = new java.util.Date().getTime ();

                DatabaseMetaData dmd = con.getMetaData ();
                long queryStart = new java.util.Date().getTime ();
                rs = dmd.getColumns (null, null, tableName, "%");

                long queryEnd = new java.util.Date().getTime ();

                ResultBean resultBean = new ResultBean();
                resultBean.setConnectionTime (connectEnd - connectStart);
                resultBean.setQueryTime (queryEnd - queryStart);
                resultBean.setResultSet (rs);

                request.setAttribute ("TableResultBean", resultBean);
            }
            else if (action.equalsIgnoreCase("SAVE_AS"))
            {
                String queryId = request.getParameter("QUERY_ID");
                ResultBean resultBean = (ResultBean)request.getSession(true).getAttribute(queryId);
                
                if (resultBean != null)
                {
	                javax.servlet.ServletOutputStream out = response.getOutputStream();
	                ResultBeanHelper.outputToCsv(out, resultBean);
	                out.flush();
	                out.close();
                }
                return null;
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("ThrowableObject", th);
        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close ();
                }
                catch (Throwable th)
                {
                    evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, 
                                       this);
                }
            }

            if (st != null)
            {
                try
                {
                    st.close ();
                }
                catch (Throwable th)
                {
                    evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, 
                                       this);
                }
            }

            if (con != null)
            {
                try
                {
                    con.close ();
                }
                catch (Throwable th)
                {
                    evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, 
                                       this);
                }
            }

            try
            {
				if (action == null || (!action.equals("SAVE_AS")) )
				{
	                request.setAttribute ("DataSourceNames", 
	                                      ConnectionManager.getInstance ().getDataSourceNames ());
	                request.setAttribute ("CurrentDataSourceName", jdbcDS);
	                request.setAttribute ("CurrentTableName", tableName);
	
	                java.util.ArrayList tableList = (java.util.ArrayList)tableListMap.get (jdbcDS);
	
	                if (tableList == null)
	                {
	                    tableList = new java.util.ArrayList();
	                }
	
	                request.setAttribute ("TableNameList", tableList);
				}
            }
            catch (Exception e)
            {
                evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
            }
        }
        
        return "JdbcMain.jsp";
    }
}