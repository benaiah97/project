<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<%!
	public String displayLogger(org.apache.log4j.Logger logger)
	{
		StringBuffer buff = new StringBuffer();
		String loggerName = logger.getName();
		java.util.Enumeration appenders = logger.getAllAppenders();
		if (appenders.hasMoreElements())
		{
			buff.append("<H4>Logger: " + loggerName + "</H4>\n");
			buff.append("<TABLE border=\"1\">\n");
			buff.append("<TR><TH>Appender<BR/>Name</TH><TH>Details</TH></TR>");
			while (appenders.hasMoreElements())
			{
				org.apache.log4j.Appender appender =
					(org.apache.log4j.Appender) appenders.nextElement();
				buff.append(displayAppender(appender));
			}
			buff.append("</TABLE><BR/>\n");
		}
		return buff.toString();
	}
	
	public String displayAppender(org.apache.log4j.Appender appender)
	{
		StringBuffer buff = new StringBuffer();
		String name = appender.getName();
		String toString = appender.toString();
	
		buff.append("<TR><TD valign=\"top\">" + name + "</TD><TD>");
		if (appender instanceof com.disney.log4j.appender.MultiAsyncAppender)
		{
			buff.append("<INPUT type=\"button\" name=\"FLUSH_BUFFERS\" " +
				"onclick=\"javascript:flushBuffers('" + 
				name + "')\" value=\"Flush Buffers\"/>");
		}
		buff.append("<PRE>" + toString + "</PRE><BR/>\n");
	
		if (appender instanceof org.apache.log4j.spi.AppenderAttachable)
		{
			buff.append("<TABLE border=\"1\">\n");
			buff.append("<TR><TH>Appender<BR/>Name</TH><TH>Details</TH></TR>");
	
			org.apache.log4j.spi.AppenderAttachable aa =
				(org.apache.log4j.spi.AppenderAttachable) appender;
			java.util.Enumeration appenders = aa.getAllAppenders();
			while (appenders.hasMoreElements())
			{
				org.apache.log4j.Appender child = (org.apache.log4j.Appender) appenders.nextElement();
				buff.append(displayAppender(child));
			}
			buff.append("</TABLE><BR/>\n");
		}
		buff.append("</TD></TR>\n");
		return buff.toString();
	}
%>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Logging Settings</TITLE>
<jsp:useBean id="logObjects" class="java.lang.String" scope="request" />
<jsp:useBean id="throwable" class="java.lang.Throwable" scope="request" />

<SCRIPT language="JavaScript">
		function updateLogLevel()
		{
			document.LogForm.ACTION.value = "UPDATE_LOG_LEVEL";
			document.LogForm.action = "AdminController";
			document.LogForm.submit();
		}
		function display(name)
		{
			document.LogForm.ACTION.value = name;
			document.LogForm.action = "shared/XmlMessageDisplay.jsp";
			document.LogForm.target = name + "Data";
			document.LogForm.submit();
		}	 		
		function flushBuffers(name)
		{
		    if (confirm("ATTENTION! BUFFERS WILL BE FLUSHED AND LOG DATA LOST!") == true)
		    {
			    document.LogForm.ACTION.value = "REFRESH";
			    document.LogForm.APPENDER_NAME.value = name;
     			document.LogForm.action = "shared/FlushBuffer.jsp";
     			document.LogForm.submit();
			}
		}	 		
	</SCRIPT>

</HEAD>
<BODY>

<CENTER>
<H2>Logging Settings</H2>
</CENTER>

<FORM name="LogForm" method="post">
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH" />
	<INPUT TYPE="hidden" NAME="CMD" value="LOG_SETTINGS" /> 
	<INPUT TYPE="hidden" NAME="APPENDER_NAME" value="" /> 
<%
	try
	{
%>

<H4>Log Manager Settings</H4>

<TABLE border="0">
	<TR>
		<TD><B>Log Objects:</B></TD>
		<TD colspan="2"><INPUT name="LOG_OBJECTS" type="text"
			value="<%=logObjects%>" size="5" maxlength="5" /></TD>
	</TR>
	<TR>
		<TD><B><U>Logger</U></B></TD>
		<TD><B><U>Value</U></B></TD>
		<TD><B><U>Class</U></B></TD>
	</TR>
<%
		java.util.Set levelsSet = (java.util.Set) request.getAttribute("LEVELS");
		if (levelsSet != null)
		{
			java.util.Iterator levels = levelsSet.iterator();
			while (levels.hasNext())
			{
				com.disney.admin.logging.LoggingCommand.Level level =
					(com.disney.admin.logging.LoggingCommand.Level) levels.next();
%>
	<TR>
		<TD><B><%=level.getName()%> Level:</B></TD>
		<TD><INPUT name="<%=level.getName()%>_VALUE" type="text"
			value="<%=level.getValue()%>" size="10" maxlength="25" /></TD>
		<TD><INPUT name="<%=level.getName()%>_CLASS" type="text"
			value="<%=level.getClassName()%>" size="40" maxlength="100" /></TD>
	</TR>
<%
			}
		}
%>
	<TR>
		<TD valign="middle" colspan="3"><INPUT type="button"
			name="UPDATE_LOG_LEVEL" onclick="javascript:updateLogLevel()"
			value="Update" /></TD>
	</TR>
</TABLE>

<BR />
<BR />

<HR />

<TABLE>
	<TR>
		<TD><a href="javascript:display('CURRENT_MSG')"><img alt="View XML"
			src="images/xml.gif"></a><BR>
		<TEXTAREA NAME='CURRENT_MSG' ROWS=20 COLS=100 readonly="true"><%out.print(request.getAttribute("LOG4J_CONFIG_XML"));%></TEXTAREA>
		</TD>
		<TD valign="top">
		<CENTER><B><U>EventTypes</U></B></CENTER>
		<TABLE border="1">
			<TR>
				<TH>Name</TH>
				<TH>Level</TH>
				<TH>Class</TH>
			</TR>
<%
		com.disney.logging.audit.EventType[] types = com.disney.logging.audit.EventType.instances();
		for (int i = 0; i < types.length; i++)
		{
%>
			<TR>
				<TD><%=types[i].toString()%></TD>
				<TD><%=types[i].toInt()%></TD>
				<TD><%=types[i].getClass().getName()%></TD>
			</TR>
<%
		}
%>
		</TABLE>
		</TD>
	</TR>
</TABLE>

</FORM>

<CENTER>
<H2>Appender Status</H2>
</CENTER>

<%
		out.println(displayLogger(org.apache.log4j.LogManager.getRootLogger()));
		java.util.Enumeration loggers = org.apache.log4j.LogManager.getCurrentLoggers();
		while (loggers.hasMoreElements())
		{
			org.apache.log4j.Logger logger = (org.apache.log4j.Logger) loggers.nextElement();
			out.println(displayLogger(logger));
		}

		String throwString = throwable.toString();
		String throwTrace = PrintStack.getTraceString(throwable);
		if (!((throwString.indexOf("java.lang.Throwable") >= 0) && (throwString.length() < 25)))
		{
%>
<FONT color="red">
<H4>Exception Occurred during last operation:</H4>
<B>Error: <%=throwString%></B><BR />
<I>Stack Trace: <%=throwTrace%></I><BR />
</FONT>
<BR />
<%
		}
	}
	catch (Throwable th)
	{
		String throwString = th.toString();
		String throwTrace = PrintStack.getTraceString(th);
%>
<FONT color="red">
<H4>Exception Occurred during display:</H4>
<B>Error: <%=throwString%></B><BR />
<I>Stack Trace: <%=throwTrace%></I><BR />
</FONT>
<BR />
<%
	}
%>

</BODY>
</HTML>
