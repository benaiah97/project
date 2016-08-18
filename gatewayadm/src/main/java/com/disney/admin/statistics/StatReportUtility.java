package com.disney.admin.statistics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.disney.admin.util.ApplicationInfoUtility;
import com.disney.graph.LineGraph;
import com.disney.graph.MultiBarGraph;
import com.disney.graph.PieChart;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.stats.StatsException;
import com.disney.stats.StatsRecorder;
import com.disney.stats.TransactionId;
import com.disney.stats.TransactionStats;
import com.disney.stats.report.ExternalSystemMessageReport;
import com.disney.stats.report.ExternalSystemReport;
import com.disney.stats.report.ExternalSystemsReport;
import com.disney.stats.report.InternalComponentMessageReport;
import com.disney.stats.report.MsgTypeReport;
import com.disney.stats.report.MsgTypesReport;
import com.disney.stats.report.StatsReport;
import com.disney.stats.report.StatsType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;

/**
 * Factory used for creating StatsReports from raw Statistics Data
 * and providing Specific Application Logic to Report Presentation.
 * 
 * @author FAV2
 */
public class StatReportUtility
{
	private final static String ADMIN_PROPS_FILE = "admin.properties";
	private final static String STAT_REPORT_UTIL = "STAT_REPORT_UTIL";
	private final static StatReportUtility instance = new StatReportUtility();
	
	protected static EventLogger evl = EventLogger.getLogger("ADMIN");

	/**
	 * Constructor for StatReportUtility.
	 */
	protected StatReportUtility()
	{
		super();
	}

	/**
	 * Returns an Instance of StatReportUtility.
	 * Looks in the admin.properties file for key: STAT_REPORT_UTIL
	 * ... if not found, returns the default implementation.
	 * 
	 * @return StatReportUtility
	 */
	public static StatReportUtility getInstance()
	{
		AbstractInitializer init = AbstractInitializer.getInitializer();
		Properties props = init.getProps(ADMIN_PROPS_FILE);
		String sruClassName = props.getProperty(STAT_REPORT_UTIL, null);
		
		StatReportUtility sru = null;
		if (sruClassName != null)
		{
			try
			{
				Class sruClass = Loader.loadClass(sruClassName);
				sru = (StatReportUtility) sruClass.newInstance();
			}
			catch (Exception e)
			{
				EventLogger evl = EventLogger.getLogger("ADMIN");
				evl.sendException("Error loading StatsReportUtility:" + sruClassName, 
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, instance);
			}
		}
		
		if (sru == null)
		{
			sru = new StatReportUtility();
		}
		
		return sru;
	}

	/**
	 * Create a StatsReport based on StatsRecorder In-Memory data.
	 */
	public StatsReport createReport() throws StatsException
	{
		StatsRecorder statsRecorder = StatsRecorder.getInstance();
		return this.createReport(statsRecorder.cloneStats());
	}

	/**
	 * Create a StatsReport based on input data.
	 * 
	 * @param stats Map of Statistics from a StatsRecorder
	 */
	public StatsReport createReport(Map stats) throws StatsException
	{
		try
		{
			Map reports = new TreeMap();
			Map extSystems = new TreeMap();

			Iterator it = stats.keySet().iterator();
			while (it.hasNext())
			{
				// Retrieve the name of the Transaction Statistic
				String name = (String) it.next();
				TransactionStats stat = (TransactionStats) stats.get(name);

				TransactionId transId = TransactionId.parseTransactionId(name);

				String msgName = transId.getMasterMsgType();
				String compId = transId.getComponentId();
				String eventType = transId.getEventType();
				String currMsgType = transId.getCurrentMsgType();

				MsgTypeReport report = null;
				if (reports.containsKey(msgName))
				{
					report = (MsgTypeReport)reports.get(msgName);
				}
				else
				{
					report = new MsgTypeReport(msgName);
					reports.put(msgName, report);
				}

				String codeString = (transId.getErrorCode().equals("0000"))?
					"Success" : this.getErrorCodePrefix() + transId.getErrorCode();
				String messageType = currMsgType + " (" + codeString + ")";

				StatsType type = this.determineType(transId);
				if (type.equals(StatsType.TOTAL_MSG_REPORT))
				{
					// Increment the total time & count
					int count = stat.getCount() + report.getCount();
					long totalTime = stat.getTotalTime() + report.getTotalTime();
					report.setTotalStats(count, totalTime,
						stat.getNextPosition(), stat.getLatestTimes());
						
					// Add as an internal component.
					report.addInternalComponentTime(compId, messageType, stat.getCount(), stat.getTotalTime());
				}
				else if (type.equals(StatsType.EXTERNAL_SYSTEM_REPORT))
				{ 
					report.addExternalSystemTime(compId, messageType, stat.getCount(), stat.getTotalTime());
					
					ExternalSystemReport extSys = null;
					if (extSystems.containsKey(compId))
					{
						extSys = (ExternalSystemReport)extSystems.get(compId);
					}
					else
					{
						extSys = new ExternalSystemReport(compId);
						extSystems.put(compId, extSys);
					}
					extSys.addExternalSystemTime(messageType, stat.getCount(), stat.getTotalTime());
				}
				else if (type.equals(StatsType.INTERNAL_COMPONENT_REPORT))
				{ 
					report.addInternalComponentTime(compId, messageType, stat.getCount(), stat.getTotalTime());
				}
			}

			// Make the Message Totals pie chart.
			PieChart msgTotalChart = new PieChart();
			msgTotalChart.setHeadLabel("Totals received by Message Type over the given time period.");
			msgTotalChart.setUnits("Messages");
			int totalMsgs = 0;

			Iterator rIt = reports.values().iterator();
			while (rIt.hasNext())
			{
				MsgTypeReport r = (MsgTypeReport) rIt.next();
				double thisTotal = new Integer(r.getCount()).doubleValue();
				totalMsgs += thisTotal;
				String msgName = r.getName();
				msgTotalChart.addWedge(msgName, thisTotal);
			}
			
			MsgTypesReport msgTypesReport = new MsgTypesReport(reports, msgTotalChart, totalMsgs);

			// Make the External Systems pie chart.
			PieChart extSystemsChart = new PieChart();
			extSystemsChart.setHeadLabel("Total Messages per " + this.getExternalSystemLabel() 
				+ " over the given time period.");
			extSystemsChart.setUnits("Messages");
			int extSysMsgs = 0;

			Iterator esIt = extSystems.values().iterator();
			while (esIt.hasNext())
			{
				ExternalSystemReport r = (ExternalSystemReport) esIt.next();
				double thisTotal = new Integer(r.getTotalMessages()).doubleValue();
				extSysMsgs += thisTotal;
				String esName = r.getName();
				extSystemsChart.addWedge(esName, thisTotal);
			}

			ExternalSystemsReport extSysReport = new ExternalSystemsReport(extSystems, extSystemsChart, extSysMsgs);
			
			return new StatsReport(msgTypesReport, extSysReport);
		}
		catch (Throwable th)
		{
			StatsException se = new StatsException("Error creating StatsReport:", th, ErrorCode.APPLICATION_EXCEPTION);
			evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, se, this);
			throw se;
		}
	}

	/**
	 * Returns an instance of the StatsType enumeration class 
	 * to identify the type of the timing.
	 * Sub-classes should override this implementation.
	 * @param transId TransactionId
	 * @return StatsType
	 */
	public StatsType determineType(TransactionId transId)
	{
		String compId = transId.getComponentId();
		String eventType = transId.getEventType();
		
		if (eventType.equalsIgnoreCase("TOTAL"))
		{
			return StatsType.TOTAL_MSG_REPORT;
		}
		if (eventType.equalsIgnoreCase("CORE"))
		{
			if ((compId.equalsIgnoreCase("DOMAIN")) || (compId.equalsIgnoreCase("BROKER")))
			{
				return StatsType.TOTAL_MSG_REPORT;
			}
			else
			{
				return StatsType.EXTERNAL_SYSTEM_REPORT;
			}
		}
		else if (eventType.equalsIgnoreCase("MAJOR"))
		{
			return StatsType.INTERNAL_COMPONENT_REPORT;
		}

		return StatsType.IGNORABLE;
	}

	/**
	 * Returns a list of Graph Objects.
	 * Default implementation returns a LineGraph (if in memory)
	 * and a MultiBarGraph.
	 * Sub-classes can override or extend this functionality.
	 * 
	 * @param msgTypeReport MsgTypeReport
	 * @return List Graph List
	 */
	public List getGraphList(MsgTypeReport msgTypeReport)
	{
		ArrayList graphs = new ArrayList();
		this.makeLineGraph(msgTypeReport, graphs);
		this.makeMultiBarGraph(msgTypeReport, graphs);
		return graphs;
	}

	/**
	 * Returns a list of Graph Objects.
	 * Default implementation returns ???
	 * Sub-classes can override or extend this functionality.
	 * 
	 * @param externalSystemReport ExternalSystemReport
	 * @return List Graph List
	 */
	public List getGraphList(ExternalSystemReport externalSystemReport)
	{
		ArrayList graphs = new ArrayList();
		this.makeMultiBarGraph(externalSystemReport, graphs);
		this.makePieChart(externalSystemReport, graphs);
		return graphs;
	}

	/**
	 * Returns a label for Internal Components.
	 * Default Implementation returns "Internal Component".
	 * Sub-classes can override this method.
	 * 
	 * @return String Label
	 */
	public String getInternalComponentLabel()
	{
		return "Internal Component";
	}

	/**
	 * Returns a label for External Systems.
	 * Default Implementation returns "External System".
	 * Sub-classes can override this method.
	 * 
	 * @return String Label
	 */
	public String getExternalSystemLabel()
	{
		return "External System";
	}

	/**
	 * Returns a prefix for Error Codes.
	 * Default Implementation returns "disney".
	 * Sub-classes can override this method.
	 * 
	 * @return String
	 */
	public String getErrorCodePrefix()
	{
		return "disney";
	}

	/**
	 * Creates a Line Graph if currentTimes are populated (normally only in memory).
	 * 
	 * @param msgTypeReport MsgTypeReport
	 * @param graphs List
	 */
	private void makeLineGraph(MsgTypeReport msgTypeReport, List graphs)
	{
		try
		{
			long[] currentTimes = msgTypeReport.getCurrentTimes();
			if ((currentTimes != null) && (currentTimes.length > 0))
			{
				LineGraph lineGraph = new LineGraph("50 most recent " 
					+ msgTypeReport.getName() + " messages.", "milliseconds", 1, "message");
				lineGraph.addLine("Total Time per message", currentTimes);
				graphs.add(lineGraph);
			}
		}
		catch (Exception e)
		{
			evl.sendException("Error creating Line Graph:", EventType.EXCEPTION,
				ErrorCode.APPLICATION_EXCEPTION, e, this);
		}		
	}

	/**
	 * Creates a MultiBarGraph.
	 * 
	 * @param msgTypeReport MsgTypeReport
	 * @param graphs List
	 */
	private void makeMultiBarGraph(MsgTypeReport msgTypeReport, List graphs)
	{
		try
		{
			String appName = ApplicationInfoUtility.getInstance().getApplicationName();
			MultiBarGraph graph = new MultiBarGraph(msgTypeReport.getName() 
				+ " Average Time Statistics", "milliseconds");
			graph.addBar("Total Time", msgTypeReport.getAverageTotalTime(), appName);

			Iterator aIt = msgTypeReport.getInternalComponentReports().values().iterator();
			while (aIt.hasNext())
			{
				InternalComponentMessageReport aReport = (InternalComponentMessageReport) aIt.next();
				graph.addBar(aReport.getName() + this.getInternalComponentLabel() 
					+ " - " + aReport.getMsgType(),	aReport.getAverageTime(), appName);
			}

			Iterator lIt = msgTypeReport.getExternalSystemReports().values().iterator();
			while (lIt.hasNext())
			{
				ExternalSystemMessageReport lReport = (ExternalSystemMessageReport) lIt.next();
				graph.addBar(this.getExternalSystemLabel() + ": " + lReport.getName() + 
					" - " + lReport.getMsgType(), lReport.getAverageTime(), 
					this.getExternalSystemLabel());
			}

			graphs.add(graph);
		}
		catch (Exception e)
		{
			evl.sendException("Error creating Multi Bar Graph:", EventType.EXCEPTION,
				ErrorCode.APPLICATION_EXCEPTION, e, this);
		}
	}
	
	/**
	 * Method makeMultiBarGraph.
	 * @param extSysReport
	 * @param graphs
	 */
	private void makeMultiBarGraph(ExternalSystemReport extSysReport, List graphs)
	{
		try
		{
			String extSysName = extSysReport.getName();
			MultiBarGraph graph = new MultiBarGraph(extSysName 
				+ " Message Average Times", "milliseconds");

			Iterator it = extSysReport.getMessageTypeList().iterator();
			while (it.hasNext())
			{
				String msgType = (String)it.next();
				ExternalSystemMessageReport esmReport = extSysReport.getReport(msgType);
				graph.addBar(esmReport.getMsgType(), esmReport.getAverageTime(), 
					"Average Time");
			}

			graphs.add(graph);
		}
		catch (Exception e)
		{
			evl.sendException("Error creating Multi Bar Graph:", EventType.EXCEPTION,
				ErrorCode.APPLICATION_EXCEPTION, e, this);
		}
	}
	
	private void makePieChart(ExternalSystemReport extSysReport, List graphs)
	{
		try
		{
			String extSysName = extSysReport.getName();
			PieChart graph = new PieChart(extSysName 
				+ " Message Totals", "messages");

			Iterator it = extSysReport.getMessageTypeList().iterator();
			while (it.hasNext())
			{
				String msgType = (String)it.next();
				ExternalSystemMessageReport esmReport = extSysReport.getReport(msgType);
				graph.addWedge(esmReport.getMsgType(), 
					new Integer(esmReport.getCount()).doubleValue());
			}

			graphs.add(graph);
		}
		catch (Exception e)
		{
			evl.sendException("Error creating Pie Chart:", EventType.EXCEPTION,
				ErrorCode.APPLICATION_EXCEPTION, e, this);
		}
	}
	
}
