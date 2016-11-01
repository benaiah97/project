<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Connection Pool</TITLE>
<jsp:useBean id="poolName" class="java.lang.String" scope="request"/>
<jsp:useBean id="poolInfo" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="errorMsg" class="java.lang.String" scope="request"/>
<jsp:useBean id="printStack" class="java.lang.String" scope="request"/>
</HEAD>
<BODY>

	<CENTER><H2><%=poolName%> Connection Information</H2></CENTER>

	<FORM name="PoolForm" method="post">
	<INPUT type="hidden" name="ACTION" value="REFRESH"/>
	<INPUT type="hidden" name="CMD" value="<%=request.getParameter("CMD")%>"/>
	<INPUT type="hidden" name="POOL" value="<%=poolName%>"/>

	<INPUT type="submit" name="REFRESH" value="Refresh Listing" onclick="document.PoolForm.ACTION.value='REFRESH'"/>
	<INPUT type="submit" name="RESET" value="Reset Connection Pool" onclick="document.PoolForm.ACTION.value='RESET'"/>
	<INPUT type="submit" name="TEST" value="Test Connections" onclick="document.PoolForm.ACTION.value='TEST'"/>
	<BR>
	
	<%
	try
	{
		if (poolInfo.size() > 0)
		{
	%>
	
  	<TABLE BORDER="1" align="left" width="100%">
  	<TR>
  		<TD valign="top" align="left">
  			<H4><%=poolName%> Connection Pool</H4>
	<%
			java.util.Iterator it = poolInfo.iterator();
			while (it.hasNext())
			{
	%>
  			<TABLE BORDER="1">
				<tr>
					<td bgcolor="#cccccc">
						<h5><%=poolName%> Connection:</h5>
	<%
				java.util.HashMap info = (java.util.HashMap) it.next();
				java.util.Iterator keys = info.keySet().iterator();
				while (keys.hasNext())
				{
					String key = keys.next().toString();
					String value = info.get(key).toString();
	%>
	  				<b><%=key%> : </b><font color="blue"><%=value%></font><BR>
	  <%
	  			}
	  %>
					</td>
				</tr>
	  		</TABLE><BR>
	  <%
			}
	  %>
		</TD>
	</TR>
	</TABLE>
	  <%
		}
	  %>
	
	</FORM>
	<BR><BR>
	<%
		if (errorMsg.length() > 0)
		{
	%>
	<H4>Exception Occurred during collection of Pool Information:</H4>
	<FONT color="red">
		<B>Error: <%=errorMsg%></B><BR/>
		<I>Stack Trace: <%=printStack%></I><BR/>
	</FONT>
	<%	
		}
	}
	catch (Throwable th)
	{
		String throwString = th.toString();
		String throwTrace = PrintStack.getTraceString(th);
	%>
	
	<BR>
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
