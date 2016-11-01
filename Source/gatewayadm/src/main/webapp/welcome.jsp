<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.admin.util.*" errorPage="./shared/JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="./shared/admin.css" rel="stylesheet" type="text/css">
<TITLE>WELCOME!!</TITLE>
	<%
		Boolean loggedIn = (Boolean) request.getSession(true).getAttribute("LOGGED_IN");
		if ( (loggedIn == null) || (!(loggedIn.booleanValue())) )
		{
	%>
<script>
	function loadPage()
	{
		document.all.USERNAME.focus();
	}
</script>
</HEAD>
<BODY onload='loadPage()'>
	<%
		}
		else
		{
	%>
</HEAD>
<BODY>
	<%
		}
	%>

<BR/>
<BR/>
<BR/>
	<CENTER>
	<%
		ApplicationInfoUtility appInfo = ApplicationInfoUtility.getInstance();
	%>
		<H2>Welcome to the <%=appInfo.getApplicationName()%><BR/>Administration Console</H2>
		<IMG src="images/<%=appInfo.getLogoImageFileName()%>"/>
		
		<BR/><BR/>
		<BR/><BR/>
		<BR/><BR/>
		
		<%
			if ( (loggedIn == null) || (!(loggedIn.booleanValue())) )
			{
				String loginServlet = MenuUtility.getLoginUrl();
		%>
			<FORM name="LoginForm" target="_parent" action="<%=loginServlet%>" method="post">
				Username:<INPUT type="text" name="USERNAME" value="" size="20" maxlength="50"><BR/>
				Password:<INPUT type="password" name="PASSWORD" value="" size="20" maxlength="20"><BR/>
				<INPUT type="submit" name="LOGIN" value="LOGIN">
			</FORM>
		<%
			}
		%>
		
	</CENTER>
	
</BODY>
</HTML>
