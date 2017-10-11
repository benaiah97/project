package com.disney.admin.util;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.util.AbstractInitializer;

/**
 * @author FAV2
 * Created on Jul 23, 2003
 */
public class MenuUtility
{
	/**
	 * Constructor for MenuUtility.
	 */
	public MenuUtility()
	{
		super();
	}

	public static Map createMenu(HttpServletRequest request)
	{
		String groupName = (String) request.getSession(true).getAttribute("USERGROUP");
		if ((groupName == null) || (groupName.length() < 1))
		{
			groupName = "DEFAULT";
		}
		groupName = groupName.toUpperCase();

	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

		TreeMap menu = new TreeMap();
		Enumeration e = props.propertyNames();
		while (e.hasMoreElements())
		{
			String nextProp = (String) e.nextElement();
			if (nextProp.toUpperCase().startsWith(groupName + "_MENU"))
			{
				int menuIdx = nextProp.toUpperCase().indexOf("_MENU");
				String menuItemName = (nextProp.substring(menuIdx + 6)).replace('_', ' ');
				String menuItemLink = props.getProperty(nextProp);
				menu.put(menuItemName, menuItemLink);
			}
		}

		return menu;
	}

	public static String getLoginUrl()
	{
		String node = AbstractInitializer.getNodeName();
	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
		String loginServlet = props.getProperty(node+"_login_servlet", "AdminController?CMD=SIMPLE_LOGIN");
		System.out.println("login servlet: '"+loginServlet + "'");
		return loginServlet;
	}

}
