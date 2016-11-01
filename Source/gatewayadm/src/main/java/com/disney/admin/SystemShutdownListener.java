package com.disney.admin;

/**
 * Listener Interface for System Shutdown Events.
 * @author FAV2
 * Created on Jul 17, 2003
 */
public interface SystemShutdownListener
{
	/**
	 * Perform any activities needed at System Shutdown.
	 */
	public void onSystemShutdown();
}
