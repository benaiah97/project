<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.admin.util.*" errorPage="./shared/JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="./shared/admin.css" rel="stylesheet" type="text/css">
<TITLE>Admin Console Title Bar</TITLE>
</HEAD>
<BODY>
<%
	ApplicationInfoUtility appInfo = ApplicationInfoUtility.getInstance();
%>
	<CENTER><H1><%=appInfo.getApplicationName()%> Admin Console: <FONT size="-1"><%=appInfo.getAppServerName()%>  Build Version: <%=appInfo.getBuildVersion()%>  Build Date: <%=appInfo.getBuildDate()%></FONT></H1></CENTER>
</BODY>
</HTML>
