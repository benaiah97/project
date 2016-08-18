package com.disney.admin.statistics;

import java.util.Date;
import java.util.List;

import com.disney.admin.AbstractWebBean;

/**
 * @author FAV2
 * Created on Sep 10, 2003
 */
public class AbstractReportWebBean extends AbstractWebBean
{
	private String serverName = null;
	private Date fromTime = null;
	private Date toTime = null;
	private List graphList = null;
	
	/**
	 * Constructor for MsgTypeReportWebBean.
	 */
	public AbstractReportWebBean()
	{
		super();
	}

	/**
	 * Returns the fromTime.
	 * @return Date
	 */
	public Date getFromTime()
	{
		return fromTime;
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
	 * Returns the serverName.
	 * @return String
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * Returns the toTime.
	 * @return Date
	 */
	public Date getToTime()
	{
		return toTime;
	}

	/**
	 * Sets the fromTime.
	 * @param fromTime The fromTime to set
	 */
	public void setFromTime(Date fromTime)
	{
		this.fromTime = fromTime;
	}

	/**
	 * Sets the graphList.
	 * @param graphList The graphList to set
	 */
	public void setGraphList(List graphList)
	{
		this.graphList = graphList;
	}

	/**
	 * Sets the serverName.
	 * @param serverName The serverName to set
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	/**
	 * Sets the toTime.
	 * @param toTime The toTime to set
	 */
	public void setToTime(Date toTime)
	{
		this.toTime = toTime;
	}

}
