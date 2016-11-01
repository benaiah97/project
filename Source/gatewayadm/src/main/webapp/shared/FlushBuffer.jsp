<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>

<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE>FlushBuffer.jsp</TITLE>
</HEAD>
<BODY>
<%
String appenderName = request.getParameter("APPENDER_NAME");
org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getRootLogger();

java.util.Enumeration appenders = logger.getAllAppenders();
if (appenders.hasMoreElements())
{
	while (appenders.hasMoreElements())
	{
		org.apache.log4j.Appender appender = (org.apache.log4j.Appender) appenders.nextElement();
		String name = appender.getName();
		if (name.equalsIgnoreCase(appenderName) &&
			appender instanceof com.disney.log4j.appender.MultiAsyncAppender)
		{
			com.disney.log4j.appender.MultiAsyncAppender mAppender =
				(com.disney.log4j.appender.MultiAsyncAppender) appender;

			int oldBufferSize = mAppender.getBufferSize();
			mAppender.setBufferSize(1);
			mAppender.setBufferSize(oldBufferSize);
			break;
		}
	}
}

response.sendRedirect("../AdminController?CMD=LOG_SETTINGS");
%>
</BODY>
</HTML>
