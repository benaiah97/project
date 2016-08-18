package com.disney.admin;

import com.disney.exception.WrappedException;

/**
 * @author FAV2
 * Created on Jun 10, 2003
 */
public class AdminException extends WrappedException
{
	/**
	 * Constructor for AdminException.
	 * @param arg0
	 */
	public AdminException(String arg0)
	{
		super(arg0);
	}

	/**
	 * Constructor for AdminException.
	 * @param arg0
	 * @param arg1
	 */
	public AdminException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

}
