<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.agent.*,com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Agent Manager</TITLE>
<jsp:useBean id="printStack" class="java.lang.String" scope="request"/>

	<SCRIPT language="JavaScript">
		function buttonPush(action, aName)
		{
			document.AgentForm.AGENT_NAME.value = aName;
			document.AgentForm.ACTION.value = action;
			document.AgentForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>Agent Manager</H2></CENTER>

	<FORM name="AgentForm" method="post">
		<INPUT TYPE="hidden" NAME="AGENT_NAME" value="??"/>
		<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	
		<H4>All Agents found:</H4>
	<%
	try
	{
	%>
		<TABLE width="90%" border="0">	
	<%
		java.util.List agentNames = AgentFactory.getAgentNameList();
		Sorter.sortStringList(agentNames);
		
		java.util.Iterator names = agentNames.iterator();
		while (names.hasNext())
		{
			String nextName = (String)names.next();
			Agent agent = AgentFactory.getAgent(nextName);

			boolean isActive = false;
			if (agent != null)
			{
				isActive = agent.isActive();
			}
	%>
			<TR>
				<TD width="60">
					<img src="images/<%=(isActive)?"green":"red"%>.jpg" height="30"/>
				</TD>
				<TD>
					<H5>Agent: <%=nextName%></H5>
	<%
			if (agent != null)
			{
	%>
					<B>Start Time:</B><I><%=agent.getStartTime()%></I><BR>
					<B>Last Status Message:</B><I><%=agent.getLastStatus()%></I><BR>
					<B>Last Work Time: </B><I><%=agent.getLastWorkTime()%></I><BR>
					<B>Last Elapsed Time: </B><I><%=agent.getLastWorkElapsedTime()%> milliseconds</I><BR>
					<B>Current Sleep Interval:</B><I><%=agent.getSleepInterval()%> milliseconds</I><BR>
	<%
			}

			if (isActive)
			{
	%>
					<INPUT type="button" value="STOP" name="STOP_BUTTON" onclick="javascript:buttonPush('STOP', '<%=nextName%>')">
	<%
			}
			else
			{
	%>
					<INPUT type="button" value="START" name="START_BUTTON" onclick="javascript:buttonPush('START', '<%=nextName%>')">
	<%
			}
	%>
				</TD>
			</TR>
	<%
		}
	%>
		</TABLE>

	<%
		if ( (printStack != null) && (printStack.length() > 0) )
		{
	%>
	<FONT color="red">
		<H4>Exception Occurred during last operation:</H4>
		<I>Stack Trace: <%=printStack%></I><BR/>
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
