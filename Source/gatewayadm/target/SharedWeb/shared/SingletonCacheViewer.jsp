<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Singleton Cache Viewer</TITLE>
<jsp:useBean id="SingletonNames" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="ThrowableObject" class="java.lang.Throwable" scope="request"/>
<jsp:useBean id="SingletonName" class="java.lang.String" scope="request"/>
<jsp:useBean id="SingletonContents" class="java.lang.String" scope="request"/>
<jsp:useBean id="StackTrace" class="java.lang.String" scope="request"/>


	<SCRIPT language="JavaScript">

		function viewSingleton(singName)
		{
			document.SingletonForm.SINGLETON_NAME.value = singName;
			document.SingletonForm.ACTION.value = "GET_CONTENTS";
			document.SingletonForm.submit();
		}
		
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>Singleton Cache Viewer</H2></CENTER>

	<FORM name="SingletonForm" method="post">
	<INPUT TYPE="hidden" NAME="SINGLETON_NAME" value="<%=SingletonName%>"/>
	<INPUT TYPE="hidden" NAME="CMD" value="SINGLETON"/>
	
	<H4>Configured Singleton Caches</H4>
		<UL>
	<%	// List all found Singleton Caches
	try
	{
		String actionStr = "REFRESH";
		java.util.Iterator names = SingletonNames.iterator();
		while (names.hasNext())
		{
			String singName = (String) names.next();
	%>
			<LI><A href="javascript:viewSingleton('<%=singName%>')"><%=singName%></A></LI>
	<%		
		}
	%>
		</UL>
	<%	// Singleton Contents	
		if (SingletonName.length() > 0)
		{
			actionStr = "REFRESH";
	%>
			<H4>Contents of : <%=SingletonName%></H4>
			<%=SingletonContents%>
	<%
		}
	%>
	<INPUT TYPE="hidden" NAME="ACTION" value="<%=actionStr%>"/>

	</FORM>

	<%
		if (StackTrace.length() > 0)
		{
	%>
	<H4>Exception Occurred during last operation:</H4>
	<FONT color="red">
		<I>Stack Trace: <%=StackTrace%></I><BR/>
	</FONT>
	<BR/>
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
	<H4>Exception Occurred during display:</H4>
	<FONT color="red">
		<B>Error: <%=throwString%></B><BR/>
		<I>Stack Trace: <%=throwTrace%></I><BR/>
	</FONT>
	<BR/>
	<%
	}
	%>
	
</BODY>
</HTML>
