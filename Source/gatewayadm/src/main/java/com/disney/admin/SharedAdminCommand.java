package com.disney.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author FAV2
 * Created on Sep 10, 2003
 */
public abstract class SharedAdminCommand extends AdminCommand
{
	private final static String SHARED_PREFIX = "shared/";
	
	/**
	 * Constructor for SharedAdminCommand.
	 */
	public SharedAdminCommand()
	{
		super();
	}

	/**
	 * Returns the value from super, appended by 'shared/'.
	 * @see com.disney.admin.AdminCommand#execute(HttpServletRequest, HttpServletResponse)
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response)
	{
		String page = super.execute(request, response);
		return (page == null)?null:SHARED_PREFIX + page;
	}

}
