package com.disney.admin.statistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.disney.admin.AbstractWebBean;
import com.disney.admin.AdminException;
import com.disney.admin.SharedAdminCommand;
import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.stats.StatsRecorder;
import com.disney.stats.report.ExternalSystemReport;
import com.disney.stats.report.MsgTypeReport;
import com.disney.stats.report.StatsReport;
import com.disney.util.Converter;

//import com.disney.stats.dao.DBStatRecorder;
import pvt.disney.dti.gateway.connection.DBStatRecorder;

/**
 * Statistics Reporter Admin Command.
 * This Admin Command utilizes the following DAOs:
 * STAT_APPS
 * STAT_SESSION
 * STAT_LOOKUP
 * 
 * @version 2.0
 * @author fav2
 */
public class StatReporterCommand extends SharedAdminCommand
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static final String APP_SERVER_DAO_NAME = "STAT_APPS";
    private static final String STAT_SESSION_DAO_NAME = "STAT_SESSION";
    private static final String LOOKUP_DAO_NAME = "STAT_LOOKUP";

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for StatReporterCommand
     */
    public StatReporterCommand()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String target = "StatReporterMain.jsp";
        AbstractWebBean webBean = null;
        try
        {
			StatReporterWebBean bean = new StatReporterWebBean();
            webBean = bean;
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

			// Get the list of selected servers
			ArrayList selectedServers = new ArrayList();
			String[] names = request.getParameterValues("APP_SERVER");
			for (int i = 0; names != null && i < names.length; i++)
			{
				selectedServers.add(names[i]);
			}
			bean.setSelectedServers(selectedServers);
			
			// Try to retreive the AppServer List
            HttpSession session = request.getSession (true);
            try {
	            bean.setAppServers((List)session.getAttribute("appServers"));
            } catch (Exception ignore) {	}

            if ((action.length () == 0) || (action.equalsIgnoreCase ("REFRESH")))
            {
                DAOHelper dao = DAOHelper.getInstance(APP_SERVER_DAO_NAME);
                Object[] inputs = {  };
                java.util.ArrayList appServers = (java.util.ArrayList)dao.processQuery (inputs);

                // Store to session for later use
                session.setAttribute ("appServers", appServers);

                // Store to Web Bean for JSP use.
                bean.setAppServers(appServers);
            }
            else if (action.equalsIgnoreCase ("GET_SNAPSHOT"))
            {
            	StatsRecorder stats = StatsRecorder.getInstance();
                StatReportUtility sru = StatReportUtility.getInstance();
                StatsReport report = sru.createReport(stats.cloneStats());
                
                Date fromTime = stats.getStartUpTime();
                String fromTimeValue = Converter.printISODate(fromTime);
                Date toTime = new Date(System.currentTimeMillis());
                String toTimeValue = Converter.printISODate(toTime);
                
                // Store to session for later use
                session.setAttribute ("report", report);
                
                // Store to Web Bean for JSP use.
                bean.setFromTimeValue(fromTimeValue);
                bean.setToTimeValue(toTimeValue);
                bean.setStatsReport(report);
            }
            else if (action.equalsIgnoreCase ("GET_REPORT"))
            {
            	ArrayList inputList = new ArrayList();

                String fromTimeValue = request.getParameter ("FROM_TIME");
                Date fromTimeDate = Converter.parseISODate(fromTimeValue);
                java.sql.Timestamp fromTime = new java.sql.Timestamp(fromTimeDate.getTime());
                inputList.add(fromTime);

                String toTimeValue = request.getParameter ("TO_TIME");
                Date toTimeDate = Converter.parseISODate(toTimeValue);
                java.sql.Timestamp toTime = new java.sql.Timestamp(toTimeDate.getTime());
                inputList.add(toTime);

            	inputList.addAll(selectedServers);

                DAOHelper dao = DAOHelper.getInstance (LOOKUP_DAO_NAME);
                Object[] inputs = inputList.toArray();
                DBStatRecorder stats = (DBStatRecorder)dao.processQuery (inputs, inputs);

                StatReportUtility sru = StatReportUtility.getInstance();
                StatsReport report = sru.createReport(stats.getStats());

                // Store to session for later use
                session.setAttribute ("report", report);

                // Store to Web Bean for JSP use.
                bean.setFromTimeValue(fromTimeValue);
                bean.setToTimeValue(toTimeValue);
                bean.setStatsReport(report);
            }
            else if (action.equalsIgnoreCase ("VIEW"))
            {
                String serverName = selectedServers.toString();

                String fromTimeValue = request.getParameter ("FROM_TIME");
                java.util.Date fromTime = Converter.parseISODate(fromTimeValue);

                String toTimeValue = request.getParameter ("TO_TIME");
                java.util.Date toTime = Converter.parseISODate(toTimeValue);

                // Retreive from session
                StatsReport report = (StatsReport)session.getAttribute ("report");

                StatReportUtility sru = StatReportUtility.getInstance();
                AbstractReportWebBean reportBean = null;

                String msgType = request.getParameter ("MSG_TYPE");
                String extSysName = request.getParameter ("EXT_SYS_NAME");
                if (msgType != null && msgType.trim().length() > 0)
                {
	            	MsgTypeReportWebBean mtBean = new MsgTypeReportWebBean();
	            	reportBean = mtBean;
	            	
	                MsgTypeReport mtr = report.getMsgTypesReport().getStatReport(msgType);
	                mtBean.setMsgTypeReport(mtr);
	                mtBean.setGraphList(sru.getGraphList(mtr));
	
	                target = "StatReport_MsgType.jsp";
                }
                else if (extSysName != null && extSysName.trim().length() > 0)
                {
                	ExternalSystemReportWebBean esBean = new ExternalSystemReportWebBean();
                	reportBean = esBean;
                	
                	ExternalSystemReport esr = report.getExternalSystemsReport()
                		.getExternalSystemReport(extSysName);
                	esBean.setExternalSystemReport(esr);
                	esBean.setGraphList(sru.getGraphList(esr));
                	
	                target = "StatReport_ExternalSystem.jsp";
                }
                else
                {
                	throw new AdminException("Must specify a valid MSG_TYPE or EXT_SYS_NAME.");
                }
                
                reportBean.setServerName(serverName);
                reportBean.setFromTime(fromTime);
                reportBean.setToTime(toTime);
            	webBean = reportBean;
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            webBean.setPrintStack(th);
        }
        finally
        {
            request.setAttribute("bean", webBean);
            return target;
        }
    }
}