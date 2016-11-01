<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*,com.disney.admin.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Application Servers</TITLE>
</HEAD>
<BODY>

	<%
		java.lang.String node = AbstractInitializer.getInitializer().getNodeName();
	%>

	<CENTER><H3>Application Servers<BR/>on <%=node%></H2></CENTER>
	<UL>
	<%
	
	java.util.Map appServers = null;
	try
	{
		appServers = AppServerNameUtility.getAppServerList();
		java.util.Iterator names = appServers.keySet().iterator();
		while (names.hasNext())
		{
			String appServerName = (String)names.next();
			String appServerUrl = (String)appServers.get(appServerName);
	%>
		<LI><B><A href="<%=appServerUrl%>" target="<%=appServerName%>"><%=appServerName%></A></B></LI>	
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
		<B>Error: <%=throwString%></B><BR/>
		<I>Stack Trace: <%=throwTrace%></I><BR/>
	</FONT>
	<BR/>
	<%
	}
	
	if ((appServers == null) || (appServers.size() == 0))
	{
	%>
	<SCRIPT language="JavaScript">
		this.window.close()
	</SCRIPT>
	<%
	}
	%>
	</UL>
</BODY>
</HTML>
