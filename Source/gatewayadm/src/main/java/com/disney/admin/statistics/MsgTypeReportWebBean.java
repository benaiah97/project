package com.disney.admin.statistics;

import com.disney.stats.report.MsgTypeReport;

/**
 * @author FAV2
 * Created on Sep 10, 2003
 */
public class MsgTypeReportWebBean extends AbstractReportWebBean
{
	private MsgTypeReport msgTypeReport = null;
	
	/**
	 * Constructor for MsgTypeReportWebBean.
	 */
	public MsgTypeReportWebBean()
	{
		super();
	}

	/**
	 * Returns the msgTypeReport.
	 * @return MsgTypeReport
	 */
	public MsgTypeReport getMsgTypeReport()
	{
		return msgTypeReport;
	}

	/**
	 * Sets the msgTypeReport.
	 * @param msgTypeReport The msgTypeReport to set
	 */
	public void setMsgTypeReport(MsgTypeReport msgTypeReport)
	{
		this.msgTypeReport = msgTypeReport;
	}

}
