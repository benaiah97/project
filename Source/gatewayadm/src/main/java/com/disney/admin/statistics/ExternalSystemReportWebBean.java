package com.disney.admin.statistics;

import com.disney.stats.report.ExternalSystemReport;

/**
 * @author FAV2
 * Created on Sep 10, 2003
 */
public class ExternalSystemReportWebBean extends AbstractReportWebBean
{
	private ExternalSystemReport externalSystemReport = null;
	
	/**
	 * Constructor for MsgTypeReportWebBean.
	 */
	public ExternalSystemReportWebBean()
	{
		super();
	}
	/**
	 * Returns the externalSystemReport.
	 * @return ExternalSystemReport
	 */
	public ExternalSystemReport getExternalSystemReport()
	{
		return externalSystemReport;
	}

	/**
	 * Sets the externalSystemReport.
	 * @param externalSystemReport The externalSystemReport to set
	 */
	public void setExternalSystemReport(ExternalSystemReport externalSystemReport)
	{
		this.externalSystemReport = externalSystemReport;
	}

}
