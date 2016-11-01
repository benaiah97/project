<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*,com.disney.admin.util.*" errorPage="./shared/JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="./shared/admin.css" rel="stylesheet" type="text/css">
<TITLE>Admin Console Menu</TITLE>
</HEAD>
<BODY>
	<UL>
<%
try
{
	java.util.Map menu = MenuUtility.createMenu(request);
	
	java.util.Iterator names = menu.keySet().iterator();
	while (names.hasNext())
	{
		String name = (String)names.next();
		String link = (String)menu.get(name);
%>
		<LI><A href="<%=link%>" target="mainFrame"><%=name%></A></LI>
<%
	}

%>
	</UL>
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
