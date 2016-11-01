package com.disney.admin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;

/**
 * @author FAV2
 * Created on Jun 10, 2003
 */
public class AdminCommandFactory
{
	private static Map commands = new HashMap();
	
	/**
	 * Constructor for AdminCommandFactory.
	 */
	private AdminCommandFactory()
	{
		super();
	}
	
	/**
	 * Retreives a cached AdminCommand.
	 * 
	 * @param request HttpServletRequest
	 * @return AdminCommand
	 */
	public static AdminCommand getCommand(HttpServletRequest request) throws AdminException
	{
		String cmd = request.getParameter("CMD");
		String cmdClassName = null;
		try
		{
			if (commands.containsKey(cmd))
			{
				return (AdminCommand)commands.get(cmd);
			}
			
		    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
		    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

		    cmdClassName = props.getProperty("CMD_" + cmd + "_CLASS");
			Class cmdClass = Loader.loadClass(cmdClassName);
			AdminCommand command = (AdminCommand)cmdClass.newInstance();
			commands.put(cmd, command);
			return command;
		}
		catch (Exception e)
		{
			throw new AdminException("Error trying to create Command: " + cmd + " [" + cmdClassName + "]", e);
		}
	}
}
