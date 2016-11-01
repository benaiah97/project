<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Application Settings</TITLE>
<jsp:useBean id="propList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="currPropFileName" class="java.lang.String" scope="request"/>

	<SCRIPT language="JavaScript">
		function viewProps(propFileName)
		{
			document.AppForm.PROP_FILE_NAME.value = propFileName;
			document.AppForm.ACTION.value = "LOOKUP";
			document.AppForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>Application Settings</H2></CENTER>

	<FORM name="AppForm" method="post">
	<INPUT TYPE="hidden" NAME="PROP_FILE_NAME" value="<%=currPropFileName%>"/>
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	<INPUT TYPE="hidden" NAME="CMD" value="APP_SETTINGS"/>
	
  	<TABLE BORDER="1" align="left" width="100%">
  	<TR>
  		<TD valign="top" align="left" width="25%">
  		<H4>Current List of Properties Files:</H4>
  			<UL>
	<%
	try
	{
		Sorter.sortStringList(propList);
		java.util.Iterator i = propList.iterator();
		while (i.hasNext())
		{
			String propFileName = (String)i.next();
	%>
				<LI><A href="javascript:viewProps('<%=propFileName%>')"><%=propFileName%></A></LI>
	<%		
		}
	%>
		</UL>
		
		<BR/>
		
		<CENTER>
			<INPUT type="submit" name="FORCE_RELOAD" onclick="document.AppForm.ACTION.value='FORCE_RELOAD'" value="RELOAD"/>
		</CENTER>
		<BR/><BR/>
		<CENTER>
			<INPUT type="submit" name="RELOAD_ALL" onclick="javascript:reloadAll()" value="RELOAD ALL SERVERS"/>
		</CENTER>

		<BR/>
		<BR/>
		
		</TD>
	
		<TD valign="top" align="left" width="75%">
	<%
		
		if (currPropFileName.length() > 0)
		{
			com.disney.util.CachedProperties cachedProps = AbstractInitializer.getInitializer().getCachedProps(currPropFileName);
			if (cachedProps != null)
			{
				java.util.Properties props = cachedProps.getProperties();
				java.util.Date timestamp = new java.util.Date(cachedProps.getTimestamp());
	%>
			<H4>Complete Property Listing for: <%=currPropFileName%> <I>(<%=timestamp%>)</I></H4>
			
			<TABLE border="1" width="80%" align="center">
				<TR>
					<TH bgcolor="#cccccc">Name:</TH>
					<TH bgcolor="#cccccc">Value:</TH>
				</TR>	
	<%
				java.util.ArrayList sorted = new java.util.ArrayList();
				java.util.Enumeration e = props.propertyNames();
				while (e.hasMoreElements())
				{
					String propName = (String)e.nextElement();
					sorted.add(propName);
				}
	
				Sorter.sortStringList(sorted);
				java.util.Iterator it = sorted.iterator();
				while (it.hasNext())
				{
					String propName = (String)it.next();
					String propValue = props.getProperty(propName, "&nbsp;");
	%>	
				<TR>
					<TD><B><%=propName%></B></TD>
					<TD><%=propValue%></TD>
				</TR>	
	<%
				}
	%>
			</TABLE>
	<%
			}
		}
	%>
	
		</TD>
	</TR>
	</TABLE>
	
	<BR/><BR/>
	</FORM>

	<%
	}
	catch (Throwable th)
	{
		String throwString = th.toString();
		String throwTrace = PrintStack.getTraceString(th);
	%>
	<H4><FONT color="red">Exception Occurred during display:</FONT></H4>
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
