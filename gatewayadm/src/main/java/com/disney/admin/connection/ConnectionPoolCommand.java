package com.disney.admin.connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.admin.SystemStartupListener;
import com.disney.connection.socket.SocketConnectionManagerFactory;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.PrintStack;


/**
 * Class ConnectionPoolCommand.java, created Oct 29, 2003 - This class handles the 
 * mangement of the Connection Pools via the Admin Console.
 * 
 * @author John Ives
 * @version 1
 */
public class ConnectionPoolCommand extends SharedAdminCommand implements SystemStartupListener
{

	/**
	 * Constructor for ConnectionPoolCommand
	 * 
	 */
	public ConnectionPoolCommand()
	{
		super();
	}


	/**
	 * Method onSystemStartup, created Oct 29, 2003.
	 * 
	 * @see com.disney.admin.SystemStartupListener#onSystemStartup()
	 */
	public void onSystemStartup()
	{
		try
		{
			// Retrieve a list of connections that were initialized and log them
			evl.sendEvent( "STATUS - Initialized connections for the following Connection "
					+ "Pools : " + SocketConnectionManagerFactory.getManagers(), 
					EventType.INFO, this);
			
		} catch( Throwable t )
		{
			// Log exception
			evl.sendException( "Exception caught while initializing Connection Managers.",
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, t, this );
		}
	}


	/**
	 * Method performTask, created Oct 29, 2003.
	 * 
	 * @see com.disney.admin.AdminCommand#performTask(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String performTask(HttpServletRequest request, HttpServletResponse response)
	{
		String pool = null;
		String jspPage = "ConPoolMain.jsp";
		try
		{
			// Retrieve the pool name and validate
			pool = request.getParameter("POOL");
			if( pool == null )
			{
				// No pool specified so use the default
				pool = "DEFAULT";
			}
			
			// Retrieve the action and validate it
			String action = request.getParameter("ACTION");
			if( action == null )
			{
				// No action specified so default to empty string
				action = "";
				jspPage = "SocketConPoolMain.jsp";
			}
			
			// Filter on action specified
			if( action.equalsIgnoreCase("RESET") )
			{
				/* 
				 * RESET THE CONNECTION POOL
				 */
				
				// Log info event
				evl.sendEvent( "Resetting Connection Pool " + pool, EventType.INFO, this );
				
				// Reset the connections
				SocketConnectionManagerFactory.getSocketManager(pool).resetConnections();
				
			} else if( action.equalsIgnoreCase("TEST") )
			{
				/* 
				 * TEST THE CONNECTION POOL
				 */
				
				//	Log info event
				evl.sendEvent("Testing Connection Pool " + pool, EventType.INFO, this);
				
				// Reset the connections
				SocketConnectionManagerFactory.getSocketManager(pool).testConnections();
			}
					
			// Retrieve the current pool info
			request.setAttribute( "poolInfo", SocketConnectionManagerFactory.
					getSocketManager(pool).getConnectionInfo() );
			
		} catch( Throwable t )
		{
			// Log the exception
			evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, t, this);
			
			// Set attributes
			request.setAttribute( "errorMsg", t.getMessage() );
			request.setAttribute( "printStack", PrintStack.getTraceString(t) );
			
		} finally
		{
			// Set attributes and return
			request.setAttribute( "CMD", request.getParameter("CMD") );
			request.setAttribute( "poolName", pool );
			
			return jspPage;
		}
	}
}
