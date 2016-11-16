<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*,com.disney.xml.*,com.disney.admin.util.*,pvt.disney.dti.gateway.connection.*,com.disney.admin.statistics.*,com.disney.graph.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<jsp:useBean id="bean" class="com.disney.admin.statistics.MessageStatsBean" scope="request"/>
<TITLE>Message Statistics Query Results</TITLE>

<SCRIPT Language="JavaScript">
	function toggle(divId)
	{
		myDiv = document.getElementById(divId);
		if (myDiv.style.display == 'none')
		{
			myDiv.style.display = 'inline';
		}
		else
		{
			myDiv.style.display = 'none';
		}
	}
	function display(formName)
	{
		myForm = document.getElementById(formName);
		myForm.action = "shared/XmlMessageDisplay.jsp";
		myForm.target = formName + "Data";
		myForm.submit();
	}
</SCRIPT>

</HEAD>
<BODY>

<%
MessageStats stats = bean.getMsgStats();
if ( (stats == null) || (stats.getAppName() == null) )
{
%>
	<H5>No Matching information found in the logging database.</H5>
	<b>Ensure that the database logging is enabled.</b>
<%
}
else
{
	try
	{
%>
		<!-- Show the Results Only -->
		<CENTER><H2>Message Statistics</H2></CENTER>
		<TABLE width="100%" border="1">
			<TR>
				<TD width="50%">
					<B>Message Id: </B><%=stats.getMessageId()%><BR/>
					<B>Conversation Id: </B><%=stats.getConversationId()%><BR/>
					<B>Message Type: </B><%=stats.getServiceType()%><BR/>
					<B>Processing Server: </B><%=stats.getNode()%><BR/>
					<B>Application Server: </B><%=stats.getAppName()%><BR/>
					
					<H5>Timings Gathered:</H5>
<%
		java.util.ArrayList timings = (java.util.ArrayList) stats.getTimedEvents();
		if (timings != null)
		{
			java.util.Iterator timingIt = timings.iterator();
			MessageStatsUtility msu = MessageStatsUtility.getInstance();
			while (timingIt.hasNext())
			{
				TimedEvent te = (TimedEvent) timingIt.next();
%>
				<H6><%=msu.getName(te)%></H6>
				<B>Timing Type: </B><%=te.getType()%><BR/>
				<B>Component Id: </B><%=te.getComponentId()%><BR/>
				<B>Start Time: </B><%=(new java.util.Date(te.getStartTime())).toString() %><BR/>
				<B>Elapsed Time: </B><%=(te.getElapsedTime() / 1000.000)%> seconds<BR/>
<%
			}
		}
%>					
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
			
			<TR>
				<TD colspan="2">
					<H5>All Message Logs:</H5>
<%
		java.util.ArrayList events = (java.util.ArrayList) stats.getEvents();
		if (events != null)
		{
%>
					<TABLE width="90%" align="center" border="1">
						<TR>
							<TH bgcolor="#cccccc"><B>Creation Date</B></TH>
							<TH bgcolor="#cccccc"><B>Event<BR/>Type</B></TH>
							<TH bgcolor="#cccccc"><B>Error<BR/>Code</B></TH>
							<TH bgcolor="#cccccc"><B>Component<BR/>Id</B></TH>
							<TH bgcolor="#cccccc"><B>BP<BR/>Id</B></TH>
							<TH bgcolor="#cccccc"><B>BP<BR/>Step</B></TH>
							<TH bgcolor="#cccccc"><B>Service<BR/>Name</B></TH>
							<TH bgcolor="#cccccc"><B>Service<BR/>Type</B></TH>
							<TH bgcolor="#cccccc"><B>Service<BR/>Action</B></TH>
							<TH bgcolor="#cccccc"><B>Conversation<BR/>Id</B></TH>
							<TH bgcolor="#cccccc"><B>Message<BR/>Id</B></TH>
							<TH bgcolor="#cccccc"><B>Message Timestamp</B></TH>
							<TH bgcolor="#cccccc"><B>From</B></TH>
							<TH bgcolor="#cccccc"><B>Log<BR/>Level</B></TH>
						</TR>
<%
			java.util.Iterator eventIt = events.iterator();
			String colorStr = "";
			String redStr = "";
			int eventId = 0;
			while (eventIt.hasNext())
			{
				eventId++;
				com.disney.logging.audit.Event e = (com.disney.logging.audit.Event) eventIt.next();
				com.disney.logging.audit.TraceInfo tInfo = e.getTraceInfo();
				com.disney.logging.audit.MessageInfo mInfo = e.getMessageInfo();
				colorStr = "#99ccff";
%>					
						<TR>
							<TD bgcolor="<%=colorStr%>" nowrap><%=Converter.printISOTimeMSOnly(e.getCreationDate())%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(e.getType()==null) ? "" : e.getType().toString()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(e.getCode()==null) || (e.getCode().toString()==null) || e.getCode().toString().equals("null") ? "" : e.getCode().toString()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(tInfo==null) || (tInfo.getComponentId()==null) || tInfo.getComponentId().equals("null") ? "" : tInfo.getComponentId()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(tInfo==null) || (tInfo.getBpId()==null) || tInfo.getBpId().equals("null") ? "" : tInfo.getBpId()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(tInfo==null) || (tInfo.getBpStep()==null) || tInfo.getBpStep().equals("null") ? "" : tInfo.getBpStep()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(mInfo==null) || (mInfo.getServiceName()==null) || mInfo.getServiceName().equals("null") ? "" : mInfo.getServiceName()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(mInfo==null) || (mInfo.getServiceType()==null) || mInfo.getServiceType().equals("null") ? "" : mInfo.getServiceType()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(mInfo==null) || (mInfo.getAction()==null) || mInfo.getAction().equals("null") ? "" : mInfo.getAction()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=(mInfo==null) || (mInfo.getConversationId()==null) || mInfo.getConversationId().equals("null") ? "" : mInfo.getConversationId()%></TD>
							<TD bgcolor="<%=colorStr%>" nowrap><%=(mInfo==null) || (mInfo.getMessageId()==null) || mInfo.getMessageId().equals("null") ? "" : mInfo.getMessageId()%></TD>
							<TD bgcolor="<%=colorStr%>" nowrap><%=((mInfo==null) || (mInfo.getTimestamp()==null)) ? "" : mInfo.getTimestamp().toString()%></TD>
							<TD bgcolor="<%=colorStr%>" nowrap><%=(mInfo==null) || (mInfo.getFrom()==null) || mInfo.getFrom().equals("null") ? "" : mInfo.getFrom()%></TD>
							<TD bgcolor="<%=colorStr%>"><%=e.getLogLevel()%></TD>
						</TR>
<% 				
				colorStr = "#ffffff"; 
%>
						<TR>
							<TD bgcolor="<%=colorStr%>" colspan="14"><B>Caller: </B><%=e.getWhere()%></TD>
						</TR>
<%
				String msgString = (e.getMessage() == null)?"null":e.getMessage();
				if (msgString.indexOf("RROR")>-1 || msgString.indexOf("xception")>-1 || msgString.indexOf("XCEPTION")>-1 || msgString.indexOf("rror:")>-1 || msgString.indexOf("not found")>-1) {
					redStr = "<font color='red'>";
				} else {
					redStr = "";
				}
				
				int pageSize = 10000;
				XmlWriter writer = XmlWriter.getInstance();
				writer.leaf("root", msgString);
				String escaped = writer.toXml(true);
				String[] pages = HtmlConverter.separate(escaped, pageSize);
%>
						<TR>
							<TD bgcolor="<%=colorStr%>" colspan="14"><B>Message: </B><%=redStr %><%=pages[0]%><BR/>
<%
				for (int pageId = 1; pageId < pages.length; pageId++)
				{
					String divName = "div" + eventId + "-" + pageId;
%>
						<A HREF="javascript:toggle('<%=divName%>')" alt="Page <%=pageId%>"><B>(<%=pageId%>)</B></A>
						<DIV style="display:none" id="<%=divName%>">
						<%=pages[pageId]%>
						</DIV>
<%					
				}
%>
							</TD>
						</TR>
<%
				String stack = e.getStackTrace();
				if (stack != null && !stack.equals("") && !stack.equals("null"))
				{
%>
						<TR>
							<TD bgcolor="<%=colorStr%>" colspan="14"><font color="red"><B>Stack Trace: </B><%=stack%></font></TD>
						</TR>
<%
				}
			}
		}
%>					
		</TABLE>
		</TABLE>
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
}
%>

</BODY>
</HTML>
