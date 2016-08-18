package com.disney.admin;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;

/**
 * Front View Controller for the Admin Console.
 * 
 * @author FAV2
 * Created on Jun 10, 2003
 */
public class AdminControllerServlet extends HttpServlet
{
	private final static EventLogger evl = EventLogger.getLogger("ADMIN");
	private final static String ADMIN_PROPS_FILE_NAME = "admin.properties";
	
	/**
	 * Notifies all Classes listed in the property:
	 * STARTUP_LISTENERS=[Comma-Separated Class list]
	 * by invoking the method: onSystemStartup()
	 * 
	 * @see com.wdw.eai.admin.SystemStartupListener#onSystemStartup()
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig arg0) throws ServletException
	{
		super.init(arg0);
		AbstractInitializer init = AbstractInitializer.getInitializer();
		Properties props = init.getProps(ADMIN_PROPS_FILE_NAME);
		String slList = props.getProperty("STARTUP_LISTENERS", "");
		StringTokenizer listeners = new StringTokenizer(slList, ",");
		while (listeners.hasMoreTokens())
		{
			String listenerClassName = listeners.nextToken().trim();
			try
			{
				Class listenerClass = Loader.loadClass(listenerClassName);
				SystemStartupListener listener = (SystemStartupListener)listenerClass.newInstance();
				listener.onSystemStartup();
			}
			catch (Exception e)
			{
				evl.sendException("Error trying to notify SystemStartupListener: " + listenerClassName, 
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
			}
		}
	}

	/**
	 * Notifies all Classes listed in the property:
	 * SHUTDOWN_LISTENERS=[Comma-Separated Class list]
	 * by invoking the method: onSystemShutdown()
	 * 
	 * @see com.wdw.eai.admin.SystemShutdownListener#onSystemShutdown()
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy()
	{
		super.destroy();
		AbstractInitializer init = AbstractInitializer.getInitializer();
		Properties props = init.getProps(ADMIN_PROPS_FILE_NAME);
		String slList = props.getProperty("SHUTDOWN_LISTENERS", "");
		StringTokenizer listeners = new StringTokenizer(slList, ",");
		while (listeners.hasMoreTokens())
		{
			String listenerClassName = listeners.nextToken().trim();
			try
			{
				Class listenerClass = Loader.loadClass(listenerClassName);
				SystemShutdownListener listener = (SystemShutdownListener)listenerClass.newInstance();
				listener.onSystemShutdown();
			}
			catch (Exception e)
			{
				evl.sendException("Error trying to notify SystemShutdownListener: " + listenerClassName, 
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
			}
		}
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
	{
		// this.processRequest(arg0, arg1);
	  // Removed because of cross site scripting 
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException
	{
		// this.processRequest(arg0, arg1);
    // Removed because of cross site scripting 	  
	}

	/**
	 * @see javax.servlet.Servlet#getServletInfo()
	 */
	public String getServletInfo()
	{
		return "Admin Console Front Controller Servlet";
	}

	/**
	 * Processes both <code>GET</code> and <code>POST</code> HTTP Requests.
	 * Validates User Credentials, calls an AdminCommand, and forwards the control when finished.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String page = null;
		try
		{
			AdminCommand command = AdminCommandFactory.getCommand(request);
			page = command.execute(request, response);
		}
		catch (Exception e)
		{
			// This should never happen... it's worst case scenario only.
			// Forward the control to an Error Page with the Exception.
			throw new ServletException("Error trying to execute command.", e);
		}
		finally
		{
			evl.sendEvent("HTTP Request processed. Dispatching to: " + page, EventType.AUDIT, this, request, null);
		}
		
		dispatch(request, response, page);
	}

	/**
	 * Forwards the control to the input page.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param page Destination Page (null if none)
	 * @throws ServletException
	 * @throws IOException
	 */
	public void dispatch(HttpServletRequest request, HttpServletResponse response, String page) throws ServletException, IOException
	{
		if (page != null)
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
			dispatcher.forward(request, response);
		}
	}

}
