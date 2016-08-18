package com.disney.admin;

import com.disney.util.PrintStack;

/**
 * @author FAV2
 * Created on May 12, 2003
 */
public abstract class AbstractWebBean
{
	private String printStack = null;
	
	/**
	 * Constructor for AbstractWebBean.
	 */
	public AbstractWebBean()
	{
		super();
	}

	/**
	 * Returns the printStack.
	 * @return String
	 */
	public String getPrintStack()
	{
		return printStack;
	}

	/**
	 * Sets the printStack.
	 * @param printStack The printStack to set
	 */
	public void setPrintStack(Throwable th)
	{
		this.printStack = (th == null)?null:PrintStack.getTraceString(th);
	}

}
