<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*,com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Statistics Reporter</TITLE>
<jsp:useBean id="bean" class="com.disney.admin.statistics.StatReporterWebBean" scope="request"/>

	<SCRIPT language="JavaScript">
		function display(formName)
		{
			myForm = document.getElementById(formName);
			myForm.action = "shared/XmlMessageDisplay.jsp";
			myForm.target = formName + "Data";
			myForm.submit();
		}
		function getReport()
		{
			document.StatForm.ACTION.value = "GET_REPORT";
			document.StatForm.action = "AdminController";
			document.StatForm.target = "mainFrame";
			document.StatForm.submit();
		}
		function getSnapshot()
		{
			document.StatForm.ACTION.value = "GET_SNAPSHOT";
			document.StatForm.action = "AdminController";
			document.StatForm.target = "mainFrame";
			document.StatForm.submit();
		}
		function chooseMsgType(aName)
		{
			document.StatForm.MSG_TYPE.value = aName;
			document.StatForm.EXT_SYS_NAME.value = "";
			document.StatForm.ACTION.value = "VIEW";
			document.StatForm.action = "AdminController";
			document.StatForm.target = aName + "Window";
			document.StatForm.submit();
		}
		function chooseExtSys(aName)
		{
			document.StatForm.EXT_SYS_NAME.value = aName;
			document.StatForm.MSG_TYPE.value = "";
			document.StatForm.ACTION.value = "VIEW";
			document.StatForm.action = "AdminController";
			document.StatForm.target = aName + "Window";
			document.StatForm.submit();
		}
		function checkAll()
		{
			field = document.StatForm.APP_SERVER;
			for (i = 0; i < field.length; i++) 
			{
				field[i].checked = true;
			}
		}
		function uncheckAll()
		{
			field = document.StatForm.APP_SERVER;
			for (i = 0; i < field.length; i++) 
			{
				field[i].checked = false;
			}
		}
		function showHide(divId1, divId2)
		{
			myDiv1 = document.getElementById(divId1);
			myDiv1.style.display = 'inline';
			myDiv2 = document.getElementById(divId2);
			myDiv2.style.display = 'none';
		}
<%
		Date now = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		String todayStart = Converter.printISODate(cal.getTime());
		
		cal.set(Calendar.DAY_OF_MONTH, day + 1);
		String tomorrowStart = Converter.printISODate(cal.getTime());

		cal.set(Calendar.DAY_OF_MONTH, day - 1);
		String yesterdayStart = Converter.printISODate(cal.getTime());
		
		cal.set(Calendar.DAY_OF_MONTH, day - 7);
		String lastWeekStart = Converter.printISODate(cal.getTime());

		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, day);
		String lastMonthStart = Converter.printISODate(cal.getTime());
%>

		function today()
		{
			document.StatForm.FROM_TIME.value = '<%=todayStart%>';
			document.StatForm.TO_TIME.value = '<%=tomorrowStart%>';
			getReport();
		}
		function yesterday()
		{
			document.StatForm.FROM_TIME.value = '<%=yesterdayStart%>';
			document.StatForm.TO_TIME.value = '<%=todayStart%>';
			getReport();
		}
		function lastWeek()
		{
			document.StatForm.FROM_TIME.value = '<%=lastWeekStart%>';
			document.StatForm.TO_TIME.value = '<%=tomorrowStart%>';
			getReport();
		}
		function lastMonth()
		{
			document.StatForm.FROM_TIME.value = '<%=lastMonthStart%>';
			document.StatForm.TO_TIME.value = '<%=tomorrowStart%>';
			getReport();
		}
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>Statistics Reporter</H2></CENTER>

	<FORM name="StatForm" method="post">
		<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
		<INPUT TYPE="hidden" NAME="CMD" value="STATS"/>
		<INPUT TYPE="hidden" NAME="MSG_TYPE" value=""/>
		<INPUT TYPE="hidden" NAME="EXT_SYS_NAME" value=""/>

	<TABLE width="100%" border="1">
	<TR>
	<TD valign="top" rowspan="2">

	<B><U>Query Type:</U></B><BR/>
	<INPUT type="radio" name="QUERY_TYPE" value="SNAPSHOT" onclick="javascript:showHide('S_DIV','D_DIV')">In-Memory Snapshot</INPUT><BR/>
	<INPUT type="radio" name="QUERY_TYPE" value="DBREPORT" onclick="javascript:showHide('D_DIV','S_DIV')">Database Report</INPUT><BR/>
	<BR/>
	
	<DIV id="S_DIV" style="display:none">
		<H4>In Memory Statistics:</H4>
		<INPUT type="button" value="Generate Report" onclick="javascript:getSnapshot()">
	</DIV>
	
	<DIV id="D_DIV" style="display:none">
	<%
	try
	{
		java.util.List appServers = bean.getAppServers();
		java.util.List selectedServers = bean.getSelectedServers();
		// Ouput the list of available app servers.
		if ((appServers != null) && (appServers.size() > 0))
		{
	%>
		<H4>Application Servers Found:</H4>
	<%
			java.util.Iterator it = appServers.iterator();
			while (it.hasNext())
			{
				String nextName = (String)it.next();
				String checked = (selectedServers.contains(nextName))?" checked":"";
	%>
			<INPUT type="checkbox" name="APP_SERVER" value="<%=nextName%>"<%=checked%>/><%=nextName%><BR/>
	<%
			}
		}
	%>
		<INPUT type="button" onclick="javascript:checkAll()" value="Check All"/>
		<INPUT type="button" onclick="javascript:uncheckAll()" value="Un-Check All"/>

		<H4>Choose Beginning and Ending Times for Report.</H4>
		<TABLE border="0">
			<TR>
				<TD colspan="2"><I>(In the form: <%=Converter.printISODate(new java.util.Date())%>)</I></TD>
			<TR>
				<TD><B>From: </B></TD>
				<TD><input type="text" name="FROM_TIME" value="<%=bean.getFromTimeValue()%>"></TD>
			</TR>
			<TR>
				<TD><B>To: </B></TD>
				<TD><input type="text" name="TO_TIME" value="<%=bean.getToTimeValue()%>"></TD>
			</TR>
			<TR>
				<TD colspan="2">
					<INPUT type="button" onclick="javascript:today()" value="Today"/>
					<INPUT type="button" onclick="javascript:yesterday()" value="Yesterday"/>
					<INPUT type="button" onclick="javascript:lastWeek()" value="Last Week"/>
					<INPUT type="button" onclick="javascript:lastMonth()" value="Last Month"/>
				</TD>
			</TR>
			<TR>
				<TD colspan="2"><INPUT type="button" value="Generate Report" onclick="javascript:getReport()"></TD>
			</TR>
		
		</TABLE>
	</DIV>
	</FORM>
	
	</TD>
	<TD valign="top">

	<%	
		com.disney.stats.report.StatsReport statsReport = bean.getStatsReport();
		com.disney.stats.report.MsgTypesReport msgTypesReport = (statsReport == null)?null:statsReport.getMsgTypesReport();
		java.util.List msgTypes = (msgTypesReport == null)?null:msgTypesReport.getMsgNames();
		// Ouput the list of available Messages
		if ((msgTypes != null) && (msgTypes.size() > 0))
		{
	%>
		<H4>Message Types Found:</H4>
		<UL>
	<%
			java.util.Iterator it = msgTypes.iterator();
			while (it.hasNext())
			{
				String nextName = (String)it.next();
	%>
			<LI><A href="javascript:chooseMsgType('<%=nextName%>')"><%=nextName%></A>
	<%
			}
	%>
	</TD>
		</UL>

	<TD valign="top" align="left" colspan="2">
		<H4>Total Messages Found: <%=msgTypesReport.getTotalMessages()%></H4>
	<%
		String graphXml = msgTypesReport.getMsgTotalChart().toString();
	%>
		<A href="javascript:display('GraphFormMT')">
			<IMG src="images/xml.gif" alt="View XML"/>
		</A><BR/>
		
		<DIV style="display:none" id="GRAPH_DIV">
			<FORM name="GraphFormMT" method="post" target="GraphFrameMT" action="AdminController">
			<INPUT type="hidden" name="CMD" value="GRAPH"/>
			<INPUT type="hidden" name="ACTION" value="GRAPH"/>
				<TEXTAREA name="GRAPH"><%=graphXml%></TEXTAREA>
			</FORM>
		</DIV>

		<IFRAME name="GraphFrameMT" width="410" height="600">
		</IFRAME><BR/><BR/>

		<SCRIPT language="JavaScript">
			document.GraphFormMT.submit();
		</SCRIPT>					
	<%
		}
	%>
		
	</TD>
	</TR>
	<TR>
	<TD valign="top">

	<%	
		com.disney.stats.report.ExternalSystemsReport extSysReport = (statsReport == null)?null:statsReport.getExternalSystemsReport();
		java.util.List extSysNames = (extSysReport == null)?null:extSysReport.getExternalSystemNames();
		// Ouput the list of External Systems
		if ((extSysNames != null) && (extSysNames.size() > 0))
		{
	%>
		<H4>External Systems Found:</H4>
		<UL>
	<%
			java.util.Iterator it = extSysNames.iterator();
			while (it.hasNext())
			{
				String nextName = (String)it.next();
	%>
			<LI><A href="javascript:chooseExtSys('<%=nextName%>')"><%=nextName%></A>
	<%
			}
	%>
	</TD>
		</UL>

	<TD valign="top" align="left" colspan="2">
		<H4>Total Messages Sent: <%=extSysReport.getTotalMsgs()%></H4>
	<%
		String esGraphXml = extSysReport.getSystemTotalsChart().toString();
	%>
		<A href="javascript:display('GraphFormES')">
			<IMG src="images/xml.gif" alt="View XML"/>
		</A><BR/>
		
		<DIV style="display:none" id="GRAPH_DIV">
			<FORM name="GraphFormES" method="post" target="GraphFrameES" action="AdminController">
			<INPUT type="hidden" name="CMD" value="GRAPH"/>
			<INPUT type="hidden" name="ACTION" value="GRAPH"/>
				<TEXTAREA name="GRAPH"><%=esGraphXml%></TEXTAREA>
			</FORM>
		</DIV>

		<IFRAME name="GraphFrameES" width="410" height="600">
		</IFRAME><BR/><BR/>

		<SCRIPT language="JavaScript">
			document.GraphFormES.submit();
		</SCRIPT>					
	<%
		}
	%>
		
	</TD>

	</TR>

	</TABLE>


	<%
		String printStack = bean.getPrintStack();
		if ( (printStack != null) && (printStack.length() > 0) )
		{
	%>
		<FONT color="red">
			<H4>Exception Occurred during last operation:</H4>
			<B>Error: </B><BR/>
			<I>Stack Trace: <%=printStack%></I><BR/>
		</FONT>
		<BR/>
	<%
		}
	%>

	<%
	}
	catch (Throwable th)
	{
		String throwString = th.toString();
		String throwTrace = PrintStack.getTraceString(th);
	%>
		<FONT color="red">
			<H4>Exception Occurred during display:</H4>
			<B>Error: <%=throwString%></B><BR/>
			<I>Stack Trace: <%=throwTrace%></I><BR/>
		</FONT>
		<BR/>
	<%
	}
	%>

</BODY>
</HTML>
