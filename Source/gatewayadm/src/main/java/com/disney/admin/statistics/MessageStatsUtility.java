package com.disney.admin.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.graph.MultiBarGraph;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.msgstats.dao.MessageStats;
import com.disney.msgstats.dao.TimedEvent;
import com.disney.util.Loader;

/**
 * Factory used for providing Application Specific Logic and
 * Data to Message Statistics Reports.
 * 
 * @author FAV2
 */
public class MessageStatsUtility
{
	private final static String MSG_STATS_UTIL = "MSG_STATS_UTIL";
	private final static MessageStatsUtility instance = new MessageStatsUtility();
	
	protected static EventLogger evl = EventLogger.getLogger("ADMIN");
	
	/**
	 * Constructor for MessageStatsUtility.
	 */
	protected MessageStatsUtility()
	{
		super();
	}

	/**
	 * Returns an Instance of MessageStatsUtility.
	 * Looks in the admin.properties file for key: MSG_STATS_UTIL
	 * ... if not found, returns the default implementation.
	 * 
	 * @return MessageStatsUtility
	 */
	public static MessageStatsUtility getInstance()
	{
	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
		String msuClassName = props.getProperty(MSG_STATS_UTIL, null);
		
		MessageStatsUtility msu = null;
		if (msuClassName != null)
		{
			try
			{
				Class msuClass = Loader.loadClass(msuClassName);
				msu = (MessageStatsUtility) msuClass.newInstance();
			}
			catch (Exception e)
			{
				evl.sendException("Error loading MessageStatsUtility:" + msuClassName, 
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, instance);
			}
		}
		
		if (msu == null)
		{
			msu = new MessageStatsUtility();
		}
		
		return msu;
	}

	/**
	 * Returns a list containing Graph Objects.
	 * Default Implementation creates a MultiBarGraph.
	 * Sub-classes should override this method if they wish to provide
	 * more of different graphs.
	 * 
	 * @param msgStats MessageStats
	 * @return List Graph List
	 */
	public List getGraphList(MessageStats msgStats)
	{
		ArrayList graphs = new ArrayList();
		this.makeMultiBarGraph(msgStats, graphs); 
		return graphs;
	}

    /**
     * Business Logic applied to create an application defined named for the event.
     * Sub-classes should override this generic method.
     * 
     * @return String
     */
	public String getName(TimedEvent te)
	{
		String componentId = te.getComponentId();
		String type = te.getType();
		
		return "Type: [" + type + "] -- Component: [" + componentId + "]";
	}

	/**
     * Creates a Multiple Bar Graph from the stored data.
	 * @param msgStats
	 * @return MultiBarGraph
	 */
    private void makeMultiBarGraph(MessageStats msgStats, List graphs)
    {
    	try
    	{
	    	String serviceType = msgStats.getServiceType();
	    	MultiBarGraph graph = new MultiBarGraph(serviceType + " Message Breakdown", "milliseconds");
	
			List timedEvents = msgStats.getTimedEvents();
			if (timedEvents != null)
			{
		        Iterator it = timedEvents.iterator();
		        while (it.hasNext())
		        {
		            TimedEvent te = (TimedEvent)it.next();
		            graph.addBar(this.getName(te), new Long(te.getElapsedTime()).doubleValue(), 
		                               ((te.getType() != null)?te.getType().toString():""));
		        }
			}
			
			graphs.add(graph);
    	}
    	catch (Exception e)
    	{
			evl.sendException("Error creating Multi Bar Graph:", EventType.EXCEPTION,
				ErrorCode.APPLICATION_EXCEPTION, e, this);
    	}
    }

}
