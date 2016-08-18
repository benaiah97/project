package com.disney.admin.statistics;

import java.util.List;

import com.disney.admin.AbstractWebBean;
import com.disney.stats.report.StatsReport;

/**
 * @author FAV2
 * Created on May 13, 2003
 */
public class StatReporterWebBean extends AbstractWebBean
{
	private String fromTimeValue = null;
	private String toTimeValue = null;

	private List selectedServers = null;
	private List appServers = null;

	private StatsReport statsReport = null;
	
	/**
	 * Constructor for MsgTypeReportWebBean.
	 */
	public StatReporterWebBean()
	{
		super();
	}

	/**
	 * Returns the appServers.
	 * @return List
	 */
	public List getAppServers()
	{
		return appServers;
	}

	/**
	 * Returns the fromTimeValue.
	 * @return String
	 */
	public String getFromTimeValue()
	{
		return fromTimeValue;
	}

	/**
	 * Returns the selectedServers.
	 * @return List
	 */
	public List getSelectedServers()
	{
		return selectedServers;
	}

	/**
	 * Returns the toTimeValue.
	 * @return String
	 */
	public String getToTimeValue()
	{
		return toTimeValue;
	}

	/**
	 * Sets the appServers.
	 * @param appServers The appServers to set
	 */
	public void setAppServers(List appServers)
	{
		this.appServers = appServers;
	}

	/**
	 * Sets the fromTimeValue.
	 * @param fromTimeValue The fromTimeValue to set
	 */
	public void setFromTimeValue(String fromTimeValue)
	{
		this.fromTimeValue = fromTimeValue;
	}

	/**
	 * Sets the selectedServers.
	 * @param selectedServers The selectedServers to set
	 */
	public void setSelectedServers(List selectedServers)
	{
		this.selectedServers = selectedServers;
	}

	/**
	 * Sets the toTimeValue.
	 * @param toTimeValue The toTimeValue to set
	 */
	public void setToTimeValue(String toTimeValue)
	{
		this.toTimeValue = toTimeValue;
	}
	
	/**
	 * Returns the statsReport.
	 * @return StatsReport
	 */
	public StatsReport getStatsReport()
	{
		return statsReport;
	}

	/**
	 * Sets the statsReport.
	 * @param statsReport The statsReport to set
	 */
	public void setStatsReport(StatsReport statsReport)
	{
		this.statsReport = statsReport;
	}

}
