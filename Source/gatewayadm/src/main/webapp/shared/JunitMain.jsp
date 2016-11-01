<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<jsp:useBean id="tests" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="stdout" class="java.lang.String" scope="request"/>
<jsp:useBean id="stderr" class="java.lang.String" scope="request"/>
<jsp:useBean id="currentTest" class="java.lang.String" scope="request"/>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
	<LINK href="admin.css" rel="stylesheet" type="text/css">
	<TITLE>JUnit Test Page</TITLE>

	<SCRIPT language="JavaScript">
		function onLoad(){
			document.JunitForm.TEST_NAME.focus();
		}
	</SCRIPT>

</HEAD>
<BODY onload="onLoad()">
	<CENTER><H2>JUnit Tests</H2></CENTER>
	<H4>JUnit Tests found in Classpath</H4>

	<FORM name="JunitForm" method="post">
	<INPUT TYPE="hidden" NAME="CMD" value="JUNIT"/>
	<SELECT name="TEST_NAME">
	<%
	// List all JUnit Tests Found
	try
	{
		java.util.Iterator it = tests.iterator();
		while (it.hasNext())
		{
			String testName = (String) it.next();
			if (testName.indexOf("junit")<0) {
	%>
		<OPTION value="<%=testName%>" <%=(testName.equalsIgnoreCase(currentTest))?"SELECTED":""%>>
			<%=testName%>
		</OPTION>
	<%
			}		
		}
	%>
	</SELECT>
	<INPUT type="submit" name="ACTION" value="RUN">
	</FORM>
	
	<%
	if (stdout.length() > 0)
	{
	%>
		<H4>Standard Out:</H4>
		<FONT color="blue">
			<%=stdout%>
		</FONT>
	<%
	}
	%>
	
	<%
	if (stderr.length() > 0)
	{
	%>
		<H4>Standard Error:</H4>
		<FONT color="red">
			<%=stderr%>
		</FONT>
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
