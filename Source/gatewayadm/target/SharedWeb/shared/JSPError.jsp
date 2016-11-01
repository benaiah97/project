<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" isErrorPage="true" import="java.util.*, java.sql.Date, java.text.*, com.disney.logging.audit.*,com.disney.logging.*" %>
<HTML>
	<HEAD>
		<META http-equiv="Content-Style-Type" content="text/css">
		<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
		<TITLE>JSP Error Page</TITLE>
<% String referrer = request.getHeader("referer");
if (referrer!=null) {
	// Remove the leading slashes
	if (referrer.startsWith("/") ) {
		referrer = referrer.substring(1);
	} else if (referrer.startsWith("http://") ) {
		referrer = referrer.substring(7);
	}
}
if (exception==null) {
	// No exception actually occurred - avoid NullPointerException
	exception = new Exception("No exception returned.");
}
%>
<!-- Use the implicit exception object -->
<!-- 
Exception:		<%=exception.toString() %>
Exception Class:	<%=exception.getClass().toString() %>
Referrer:		<%=referrer %>
Message:		<%=exception.getMessage() %>
LocalizedMessage:	<%=exception.getLocalizedMessage() %>
HashCode:		<%=exception.hashCode() %>
-->
		<Script>
			function go(){
				history.back();
			}
			function logout(){
				document.all.redirectForm.target='_top';
				document.all.redirectForm.action='/Logout';
				document.all.redirectForm.submit();
			}
		</Script>
	</HEAD>
	<body>
		<Center><H1>An Error occurred in <%=referrer %></H1></Center>

		<H2>
			<br><CENTER>
			<%= exception.toString() %>
			</CENTER>
		</H2>
		<Form name="redirectForm" method="post">
			<CENTER>
			<Input type="button" value="Return to Previous Page" onclick="go()">
			</CENTER>
		</Form>
		<% // Log error
		try {
			EventLogger evl = EventLogger.getLogger("ADMIN");
			evl.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, exception, this);
		} catch (Exception ex) {
			out.println("Logging Exception: " + ex);			
		}
		%>
<!-- This code below is from 'Professional JSP' by Wrox, Chapter 6 (Error Handling) -->
<!-- Comment out (hide) below for production use by deleting the end comment tag here: -->
		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Exception Information</TH>
		</TR>
		<% // Print the Exception Information
			// Still doesn't print referer information correctly though
		%>
		<TR><TH>Exception</TH><TD><%=exception.toString() %></TD></TR>
		<TR><TH>Class</TH><TD><%=exception.getClass().toString() %></TD></TR>
		<TR><TH>Referrer</TH><TD><%=referrer %></TD></TR>
		<TR><TH>Message</TH><TD><%=exception.getMessage() %></TD></TR>
		<TR><TH>LocalizedMessage</TH><TD><%=exception.getLocalizedMessage() %></TD></TR>
		<TR><TH>HashCode</TH><TD><%=exception.hashCode() %></TD></TR>
		</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Request Information</TH>
		</TR>
		<% // Print the Request Information
		%>
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
		<TR><TH>Application Class</TH><TD><%=application.getClass() %></TD></TR>
		<TR><TH>Application MajorVersion</TH><TD><%=application.getMajorVersion() %></TD></TR>
		<TR><TH>Application MinorVersion</TH><TD><%=application.getMinorVersion() %></TD></TR>
		<TR><TH>Application ServerInfo</TH><TD><%=application.getServerInfo() %></TD></TR>
		</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Request Header</TH>
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

		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Request Parameters</TH>
		</TR>
		<% // Print the Request Parameters
			parameterEnum = request.getParameterNames();
			while (parameterEnum.hasMoreElements()) {
				String name = (String)parameterEnum.nextElement();
		%>
		<TR>
			<TH><%=name %></TH>
			<TD><%=request.getParameter(name) %></TD>
		</TR>
			<% } %>
		</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Request Attributes</TH>
		</TR>
		<% // Print the Request Attributes
			Enumeration attributeEnum = request.getAttributeNames();
			while (attributeEnum.hasMoreElements()) {
				String name = (String)attributeEnum.nextElement();
		%>
		<TR>
			<TH><%=name %></TH>
			<TD><%=application.getAttribute(name) %></TD>
		</TR>
			<% } %>
		</TABLE>

		<P>
		<TABLE border="1">
		<TR>
			<TH colspan="2" align="left">Application Attributes</TH>
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
		<p>

		<% out.println("Exception Stack Trace:");
		// (note: can't display line numbers unless JIT compiler is turned off)
		exception.printStackTrace( new java.io.PrintWriter(out) ); %>  
<!-- Comment out (hide) above for production use -->

	</body>
</HTML>