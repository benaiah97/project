package com.disney.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.filter.ServletFilterManagerFactory;
import com.disney.logging.EventLogger;

/**
 * @author FAV2
 * Created on Jun 10, 2003
 */
public abstract class AdminCommand
{
	protected EventLogger evl;
	
	/**
	 * Constructor for AdminCommand.
	 */
	public AdminCommand()
	{
		super();
        evl = EventLogger.getLogger("ADMIN");
	}

	/**
	 * Determines whether or not the Request should proceed according to authorization settings.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return boolean
	 */
	protected boolean shouldProceed(HttpServletRequest request, HttpServletResponse response)
	{
		return ServletFilterManagerFactory.getServletFilterManager(this).doFilter(request, response);
	}
	
	/**
	 * Executes the Admin Command.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String Page to forward control to.
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response)
	{
		if (this.shouldProceed(request, response))
		{
			return this.performTask(request, response);
		}
		
		return null;
	}

	/**
	 * Perform the Admin Command Task.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return String Page to forward control to.
	 */
	protected abstract String performTask(HttpServletRequest request, HttpServletResponse response);
	
}
