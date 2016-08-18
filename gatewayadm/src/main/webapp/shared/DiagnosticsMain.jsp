<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*, java.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="../admin.css" rel="stylesheet" type="text/css">
<TITLE>Diagnostics Viewer</TITLE>

</HEAD>
<BODY>

	<CENTER><H2>Diagnostics Viewer</H2></CENTER>

	<B>Node Name:</B> <%=AbstractInitializer.getNodeName()%> <BR/>

	<TABLE width="50%" border="1" align="center">
		<TR>
			<TH align="left" bgcolor="#cccccc"><B>Name</B></TH>
			<TH align="left" bgcolor="#cccccc"><B>Value</B></TH>
		</TR>
		<TR>
			<TD>Free Heap Size:</TD>
			<TD><%=Runtime.getRuntime().freeMemory()%></TD>
		</TR>
		<TR>
			<TD>Total Heap Size:</TD>
			<TD><%=Runtime.getRuntime().totalMemory()%></TD>
		</TR>
	<%
	try
	{
		Diagnostics d = new Diagnostics();
		String details = d.getPropertiesAsString();
		java.io.BufferedReader in = new java.io.BufferedReader(new java.io.StringReader(details));
		String eachLine = null;
		while ((eachLine = in.readLine()) != null)
		{
			java.util.StringTokenizer nameValue = new java.util.StringTokenizer(eachLine, "=");
			String name = "";
			if (nameValue.hasMoreTokens())
			{
				name = nameValue.nextToken();
			}

			String value = "";
			while (nameValue.hasMoreTokens())
			{
				value += nameValue.nextToken();
			}
			
			if ( (name.length() > 0) || (value.length() > 0) )
			{
	%>
		<TR>
			<TD><%=name%></TD>
			<TD><%=value%></TD>
		</TR>
	<%
			}
		}
	%>
	</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH align="left" bgcolor="#cccccc">Application Attributes</TH>
			<TH align="left" bgcolor="#cccccc"><B>Value</B></TH>
		</TR>
		<% // Print the Application Attributes
			Enumeration appAttributeEnum = application.getAttributeNames();
			while (appAttributeEnum.hasMoreElements()) {
				String name = (String)appAttributeEnum.nextElement();
		%>
		<TR>
			<TH><%=name %></TH>
			<TD><%=application.getAttribute(name) %></TD>
		</TR>
			<% } %>
		</TABLE>

		<P>
		<% // Print the Request Information %>
		<TABLE border="1">
		<TR>
			<TH align="left" bgcolor="#cccccc">Request Information</TH>
			<TH align="left" bgcolor="#cccccc"><B>Value</B></TH>
		</TR>
		<TR><TH>Application Class</TH><TD><%=application.getClass() %></TD></TR>
		<TR><TH>Application MajorVersion</TH><TD><%=application.getMajorVersion() %></TD></TR>
		<TR><TH>Application MinorVersion</TH><TD><%=application.getMinorVersion() %></TD></TR>
		<TR><TH>Application ServerInfo</TH><TD><%=application.getServerInfo() %></TD></TR>
		<TR><TH>AuthType</TH><TD><%=request.getAuthType() %></TD></TR>
		<TR><TH>CharacterEncoding</TH><TD><%=request.getCharacterEncoding() %></TD></TR>
		<TR><TH>Class</TH><TD><%=request.getClass().getName() %></TD></TR>
		<TR><TH>ContentLength</TH><TD><%=request.getContentLength() %></TD></TR>
		<TR><TH>ContentType</TH><TD><%=request.getContentType() %></TD></TR>
		<TR><TH>ContextPath</TH><TD><%=request.getContextPath() %></TD></TR>
		<TR><TH>Locale</TH><TD><%=request.getLocale().toString() %></TD></TR>
		<TR><TH>Method</TH><TD><%=request.getMethod() %></TD></TR>
		<TR><TH>PathInfo</TH><TD><%=request.getPathInfo() %></TD></TR>
		<TR><TH>PathTranslated</TH><TD><%=request.getPathTranslated() %></TD></TR>
		<TR><TH>Protocol</TH><TD><%=request.getProtocol() %></TD></TR>
		<TR><TH>QueryString</TH><TD><%=request.getQueryString() %></TD></TR>
		<TR><TH>RemoteAddr</TH><TD><%=request.getRemoteAddr() %></TD></TR>
		<TR><TH>RemoteHost</TH><TD><%=request.getRemoteHost() %></TD></TR>
		<TR><TH>RemoteUser</TH><TD><%=request.getRemoteUser() %></TD></TR>
		<TR><TH>RequestURI</TH><TD><%=request.getRequestURI() %></TD></TR>
		<TR><TH>RequestedSessionId</TH><TD><%=request.getRequestedSessionId() %></TD></TR>
		<TR><TH>Scheme</TH><TD><%=request.getScheme() %></TD></TR>
		<TR><TH>ServerName</TH><TD><%=request.getServerName() %></TD></TR>
		<TR><TH>ServerPort</TH><TD><%=request.getServerPort() %></TD></TR>
		<TR><TH>ServletPath</TH><TD><%=request.getServletPath() %></TD></TR>
		</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH align="left" bgcolor="#cccccc">Request Header</TH>
			<TH align="left" bgcolor="#cccccc"><B>Value</B></TH>
		</TR>
		<% // Print the Request Header Information
			Enumeration parameterEnum = request.getHeaderNames();
			while (parameterEnum.hasMoreElements()) {
				String name = (String)parameterEnum.nextElement();
		%>
		<TR>
			<TH><%=name %></TH>
			<TD><%=request.getHeader(name) %></TD>
		</TR>
			<% } %>
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
	%>
	
</BODY>
</HTML>
