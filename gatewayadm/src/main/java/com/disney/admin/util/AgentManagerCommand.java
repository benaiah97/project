package com.disney.admin.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.admin.SystemShutdownListener;
import com.disney.admin.SystemStartupListener;
import com.disney.agent.Agent;
import com.disney.agent.AgentException;
import com.disney.agent.AgentFactory;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PrintStack;


/**
 * Agent Manager Admin Command
 * 
 * @version 1.0
 * @author fav2
 */
public class AgentManagerCommand extends SharedAdminCommand 
	implements SystemStartupListener, SystemShutdownListener
{
    //~ Methods ------------------------------------------------------------------------------------

    private static final String _INITIALIZER = "INITIALIZER";

	/**
	 * @see com.disney.admin.SystemStartupListener#onSystemStartup()
	 */
	public void onSystemStartup()
	{
		try
		{
			AgentFactory.createAgent(_INITIALIZER, AbstractInitializer.getInitializer());
			AgentFactory.startAgent(_INITIALIZER);
		}
		catch (Exception e)
		{
            evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
		}

        List aNames = AgentFactory.getAgentNameList();
        if (aNames != null)
        {
            Iterator it = aNames.iterator ();
            while (it.hasNext ())
            {
                String aName = (String)it.next ();
                try
                {
                    AgentFactory.startAgent(aName);
                }
                catch (AgentException e)
                {
                    evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
                }
            }
        }
    }

	/**
	 * @see com.disney.admin.SystemShutdownListener#onSystemShutdown()
	 */
	public void onSystemShutdown()
	{
        List agents = AgentFactory.getAgentNameList ();
        if (agents != null)
        {
            Iterator it = agents.iterator ();

            while (it.hasNext ())
            {
                String agentName = (String)it.next ();

                try
                {
                    AgentFactory.stopAgent (agentName);
                }
                catch (AgentException e)
                {
                    evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
                }
            }
        }
    }

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            if (action.equalsIgnoreCase ("STATUS"))
            {
                this.writeStatus (request, response);
                return null;
            }

            String agentName = request.getParameter ("AGENT_NAME");
            if (agentName != null)
            {
                //request.setAttribute("agentName", agentName);
                if (action.equalsIgnoreCase ("STOP"))
                {
                    AgentFactory.stopAgent (agentName);
                }
                else if (action.equalsIgnoreCase ("START"))
                {
                    AgentFactory.startAgent (agentName);
                }
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("printStack", PrintStack.getTraceString (th));
        }
		return "AgentMgrMain.jsp";
    }

	

    /**
     * Write a Status message for monitoring of Agents in plain text.
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    private void writeStatus(HttpServletRequest request, HttpServletResponse response)
    {
        javax.servlet.ServletOutputStream out = null;

        try
        {
            out = response.getOutputStream ();

            Iterator agents = AgentFactory.getAgentNameList ().iterator ();

            while (agents.hasNext ())
            {
                String nextName = (String)agents.next ();

                try
                {
                    Agent agent = AgentFactory.getAgent (nextName);

                    if (agent.isActive ())
                    {
                        out.println ("Agent: " + nextName + " is active.");
                    }
                    else
                    {
                        out.println ("Agent: " + nextName + " is inactive.");
                    }
                }
                catch (AgentException e)
                {
                    evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
                }
            }
        }
        catch (Throwable th)
        {
            evl.sendException ("Error publishing STATUS.", EventType.EXCEPTION, 
                               ErrorCode.APPLICATION_EXCEPTION, th, this);
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.flush ();
                    out.close ();
                }
                catch (java.io.IOException ioe)
                {
                    evl.sendException ("Error closing STATUS output stream.", EventType.EXCEPTION, 
                                       ErrorCode.APPLICATION_EXCEPTION, ioe, this);
                }
            }
        }
    }

	/**
	 * @see com.disney.admin.AdminCommand#shouldProceed(HttpServletRequest, HttpServletResponse)
	 */
	protected boolean shouldProceed(HttpServletRequest request, HttpServletResponse response)
	{
        String action = request.getParameter ("ACTION");
        if ((action != null) && (action.equalsIgnoreCase("STATUS")))
        {
        	return true;
        }
		return super.shouldProceed(request, response);
	}

}