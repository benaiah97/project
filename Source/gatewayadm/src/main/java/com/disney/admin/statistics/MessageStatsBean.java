package com.disney.admin.statistics;

import java.util.List;

//import com.disney.msgstats.dao.MessageStats;
import pvt.disney.dti.gateway.connection.MessageStats;


/**
 * @author FAV2
 * Created on Sep 10, 2003
 */
public class MessageStatsBean
{
	private MessageStats msgStats = null;
	private List graphList = null;
	
	/**
	 * Constructor for MessageStatsBean.
	 */
	public MessageStatsBean(MessageStats stats)
	{
		super();
		this.msgStats = stats;
        MessageStatsUtility msu = MessageStatsUtility.getInstance();
        this.graphList = msu.getGraphList(stats);
	}

	/**
	 * Returns the graphList.
	 * @return List
	 */
	public List getGraphList()
	{
		return graphList;
	}

	/**
	 * Returns the msgStats.
	 * @return MessageStats
	 */
	public MessageStats getMsgStats()
	{
		return msgStats;
	}

	/**
	 * Returns the msgStats.
	 * @return MessageStats
	 */
	public MessageStats getBefore()	{
		return msgStats;
	}

	/**
	 * 
	 * @return
	 */
	public MessageStats getAfter() {
		return null;
	}
	
	
}
