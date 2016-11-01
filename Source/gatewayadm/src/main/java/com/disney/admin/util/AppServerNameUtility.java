package com.disney.admin.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.util.AbstractInitializer;

/**
 * @author FAV2
 * Created on May 5, 2003
 */
public class AppServerNameUtility
{
	/**
	 * Constructor for AppServerNameUtility.
	 */
	private AppServerNameUtility()
	{
		super();
	}

	/**
	 * Method getAppServerList.
	 * @return HashMap
	 */
	public static Map getAppServerList()
	{
	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
		
		String node = AbstractInitializer.getNodeName().toUpperCase();
		
		// Find CLUSTERS
		HashMap clusters = new HashMap();
		Enumeration names = props.propertyNames();
		while (names.hasMoreElements())
		{
			String name = (String)names.nextElement();
			if (name.startsWith("CLUSTER_"))
			{
				String clusterName = name.substring(8).trim();
				String nodeListStr = props.getProperty(name);
				StringTokenizer st = new StringTokenizer(nodeListStr, ",");
				ArrayList nodeList = new ArrayList();
				while (st.hasMoreTokens())
				{
					nodeList.add(st.nextToken().trim().toUpperCase());
				}
				clusters.put(clusterName, nodeList);
			}
		}
		
		// Is this Node in a cluster?
		Iterator clusterNames = clusters.keySet().iterator();
		while (clusterNames.hasNext())
		{
			String cName = (String)clusterNames.next();
			List nodes = (List)clusters.get(cName);
			if (nodes.contains(node))
			{
				return getAppServerList(props, nodes);
			}
		}
		
		// Apparently not.. just get this one...
		ArrayList nodeNames = new ArrayList();
		nodeNames.add(node);
		return getAppServerList(props, nodeNames);
	}

	/**
	 * Method getAppServerList.
	 * @param props
	 * @param nodeNames
	 * @return Map
	 */
	private static Map getAppServerList(Properties props, List nodeNames)
	{
		TreeMap appServers = new TreeMap();
		
		Enumeration names = props.propertyNames();
		while (names.hasMoreElements())
		{
			String name = (String)names.nextElement();
			if (name.startsWith("APP_"))
			{
				int endName = name.indexOf("_", 4);
				String nodeName = name.substring(4, endName).toUpperCase();
				if (nodeNames.contains(nodeName))
				{
					String appName = name.substring(endName + 1).trim();
					String appUrl = props.getProperty(name);
					appServers.put(appName, appUrl);
				}
			}
		}
		
		return appServers;
	}

	/**
	 * Method main. -- for testing only.
	 * @param args
	 */
	public static void main(String[] args)
	{
		Map map = getAppServerList();
		
		Iterator names = map.keySet().iterator();
		while (names.hasNext())
		{
			String name = (String)names.next();
			String url = (String)map.get(name);
			System.out.println(name + " == " + url);
		}
	}

}
