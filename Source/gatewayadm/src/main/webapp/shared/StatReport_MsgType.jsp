<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.stats.report.*,com.disney.graph.*,com.disney.admin.statistics.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<jsp:useBean id="bean" class="com.disney.admin.statistics.MsgTypeReportWebBean" scope="request"/>

<%
	MsgTypeReport report = bean.getMsgTypeReport();
	StatReportUtility sru = StatReportUtility.getInstance();
	double eaiTime = 0;
	double backEndTime = 0;
	double totalTime = 0;
%>

<SCRIPT Language="JavaScript">
	function display(formName)
	{
		myForm = document.getElementById(formName);
		myForm.action = "shared/XmlMessageDisplay.jsp";
		myForm.target = formName + "Data";
		myForm.submit();
	}
</SCRIPT>

<TITLE>Statistics Report for <%=report.getName()%></TITLE>
</HEAD>
<BODY>

	<TABLE WIDTH="100%">
		<TR>
			<TD valign="top">
				<H2><%=report.getName()%> Message Statistics<BR/></H2>
				
				<h4>
					Application Server Name: <%=bean.getServerName()%><br/>
					Beginning Time of Report: <%=bean.getFromTime().toString()%><br/>
					Ending Time of Report: <%=bean.getToTime().toString()%><br/>
				</h4>
				<B>Total Messages:</B> <%=report.getCount()%><br/>
				<B>Average Time per Message:</B> <%=new Double(report.getAverageTotalTime() / 1000.000).toString()%> seconds<br/>
				<%
						totalTime = report.getTotalTime();
			    %>
				<B>Total Time taken:</B> <%=new Double(totalTime / 1000.000).toString()%> seconds<br/>
	<%
			java.text.NumberFormat pFormat = java.text.NumberFormat.getPercentInstance();
			java.util.Iterator icrIt = report.getInternalComponentReports().values().iterator();
			if (icrIt.hasNext())
			{
	%>
				<h5><%=sru.getInternalComponentLabel()%>s:</h5>	
	<%		
			}
			while (icrIt.hasNext())
			{
				InternalComponentMessageReport icReport = (InternalComponentMessageReport)icrIt.next();
				double p = new Integer(icReport.getCount()).doubleValue() / new Integer(report.getCount()).doubleValue();
	%>
				<h6><%=sru.getInternalComponentLabel()%> Statistics: <%=icReport.getName()%></h6> 
				<b>Message Received: </b><%=icReport.getMsgType()%><br/> 
				<B>Total Messages:</B> <%=icReport.getCount()%><br/>
				<B>Percent of Messages:</B> <%=pFormat.format(p)%><br/>
				<B>Average Time:</B> <%=new Double(icReport.getAverageTime() / 1000.000).toString()%> seconds<br/>
				
	<%		
			}
	%>
				<br/><br/>
	<%
			java.util.Iterator esrIt = report.getExternalSystemReports().values().iterator();
			if (esrIt.hasNext())
			{
	%>
				<h5><%=sru.getExternalSystemLabel()%>s:</h5>	
	<%		
			}
			double totalBackEndTime = 0;
			while (esrIt.hasNext())
			{
				ExternalSystemMessageReport esReport = (ExternalSystemMessageReport)esrIt.next();
				double p = new Integer(esReport.getCount()).doubleValue() / new Integer(report.getCount()).doubleValue();
	%>
				<h6><%=sru.getExternalSystemLabel()%> Statistics: <%=esReport.getName() + " - " + esReport.getMsgType()%></h6> 
				<B>Total Messages:</B> <%=esReport.getCount()%><br/>
				<B>Percent of Messages:</B> <%=pFormat.format(p)%><br/>
				<B>Average Time:</B> <%=new Double(esReport.getAverageTime() / 1000.000).toString()%> seconds<br/>
				
				<%
					totalBackEndTime = esReport.getCount() * esReport.getAverageTime();
					backEndTime = backEndTime + totalBackEndTime;
				%>
				<B>Total Time:</B> <%=totalBackEndTime / 1000.000%> seconds<br/>
	<%		
			}
	%>
			<h5>Total Times:</h5> 
				<b>Total Messages: </b><%=report.getCount()%><br/> 
				<B>Total Time Taken:</B> <%= new Double(totalTime / 1000.000).toString()%> seconds<br/>
				<B>Average BackEnd Time:</B> <%= new Double(backEndTime / (1000.000 * report.getCount()))%> seconds<br/>
				<B>Total BackEnd Time:</B> <%= new Double(backEndTime / 1000.000)%> seconds<br/>
				<B>Average EAI Time:</B> <%= new Double((totalTime - backEndTime) / (1000.000 * report.getCount()))%> seconds<br/>
				<B>Total EAI Time:</B> <%= new Double((totalTime - backEndTime) / 1000.000 )%> seconds<br/>
				<br/><br/>
			</TD>
			<TD valign="top">
<%
		java.util.List graphs = bean.getGraphList();
		java.util.Iterator graphIt = graphs.iterator();
		int graphNum = 0;
		while (graphIt.hasNext())
		{
			graphNum++;
			Graph graph = (Graph)graphIt.next();
			String graphXml = graph.toString();
%>
					<A href="javascript:display('GraphForm<%=graphNum%>')">
						<IMG src="images/xml.gif" alt="View XML"/>
					</A><BR/>
					
					<DIV style="display:none" id="GRAPH_DIV<%=graphNum%>">
						<FORM name="GraphForm<%=graphNum%>" method="post" target="GraphFrame<%=graphNum%>" action="AdminController">
						<INPUT type="hidden" name="CMD" value="GRAPH"/>
						<INPUT type="hidden" name="ACTION" value="GRAPH"/>
							<TEXTAREA name="GRAPH"><%=graphXml%></TEXTAREA>
						</FORM>
					</DIV>
	
					<IFRAME name="GraphFrame<%=graphNum%>" width="600" height="400">
					</IFRAME><BR/><BR/>

					<SCRIPT language="JavaScript">
						document.GraphForm<%=graphNum%>.submit();
					</SCRIPT>					
<%
		}
%>
			</TD>
		</TR>
	</TABLE>
	
</BODY>
</HTML>
