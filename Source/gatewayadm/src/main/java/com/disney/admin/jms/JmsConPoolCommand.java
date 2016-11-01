package com.disney.admin.jms;

import com.disney.admin.SharedAdminCommand;
import com.disney.jms.JmsConnectionPool;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.PrintStack;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jms Connection Pools Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class JmsConPoolCommand extends SharedAdminCommand
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static final String JMS_CON_POOL_MAIN_PAGE = "JmsConPoolMain.jsp";
    private static final String CON_POOL_MAIN_PAGE = "ConPoolMain.jsp";

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        //ArrayList poolInfo = null; //HACK
    	List poolInfo = null;
        String forwardPage = JMS_CON_POOL_MAIN_PAGE;
        String poolName = "";

        try
        {
        	request.setAttribute("CMD", request.getParameter("CMD"));
            String action = request.getParameter ("ACTION");

            if (action == null)
            {
                action = "";
            }

            if ((action.equals ("")) || (action.equalsIgnoreCase ("REFRESH")))
            {
                forwardPage = JMS_CON_POOL_MAIN_PAGE;
                poolName = "";
            }
            else if (action.equalsIgnoreCase ("CON_FACTORIES"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Connection Factory";
                poolInfo = JmsConnectionPool.getInstance ().getConnectionFactoryInfo ();
            }
            else if (action.equalsIgnoreCase ("DESTINATIONS"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Destination";
                poolInfo = JmsConnectionPool.getInstance ().getDestinationInfo ();
            }
            else if (action.equalsIgnoreCase ("QUEUE_CONNECTIONS"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Queue Connection";
                poolInfo = JmsConnectionPool.getInstance ().getQueueConnectionInfo ();
            }
            else if (action.equalsIgnoreCase ("BROWSERS"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Jms Browser";
                poolInfo = JmsConnectionPool.getInstance ().getJmsBrowserInfo ();
            }
            else if (action.equalsIgnoreCase ("READERS"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Jms Reader";
                poolInfo = JmsConnectionPool.getInstance ().getJmsReaderInfo ();
            }
            else if (action.equalsIgnoreCase ("WRITERS"))
            {
                forwardPage = CON_POOL_MAIN_PAGE;
                poolName = "Jms Writer";
                poolInfo = JmsConnectionPool.getInstance ().getJmsWriterInfo ();
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("errorMsg", "Unknown Error in JmsConPoolServlet.");
            request.setAttribute ("printStack", PrintStack.getTraceString (th));
        }
        finally
        {
            request.setAttribute ("poolName", poolName);
            if (poolInfo != null)
            {
                request.setAttribute ("poolInfo", poolInfo);
            }
            return forwardPage;
        }
    }
}