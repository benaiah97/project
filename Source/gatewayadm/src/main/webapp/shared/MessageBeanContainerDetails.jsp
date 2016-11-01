<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*,com.disney.jms.*" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="../admin.css" rel="stylesheet" type="text/css">
	<%
	try
	{
		String containerName = request.getParameter("CONTAINER_NAME");
	%>
	
<TITLE><%=containerName%> Container Details</TITLE>
</HEAD>
<BODY>

	<CENTER><H3>Message Bean Container: <%=containerName%></H3></CENTER>

  	<TABLE BORDER="1" width="100%" align="center">
  		<TR>
  			<TH bgcolor="#cccccc">Bean<BR/>Number</TH>
  			<TH bgcolor="#cccccc">Connected</TH>
  			<TH bgcolor="#cccccc">Active</TH>
  			<TH bgcolor="#cccccc">Number of<BR/>Messages<BR/>Processed</TH>
  			<TH bgcolor="#cccccc">Status</TH>
  		</TR>
	<%
		MessageBeanContainerFacade container = MessageBeanContainerFacadeFactory.getMessageBeanContainerFacade(containerName);
		java.util.List infoList = container.getMessageBeanInfoList();
		
		java.util.Iterator it = infoList.iterator();
		int count = 0;
		while (it.hasNext())
		{
			count++;
			MessageBeanInfo info = (MessageBeanInfo)it.next();
			String connectedImage = (info.isConnected())?"green":"red";
			String activeImage = (info.isActive())?"running":"waiting";
			long aTime = info.getActiveTime();
			long lastATime = info.getLastActiveDuration();
			StringBuffer activeInfo = new StringBuffer();
			if (lastATime > 0)
			{
				activeInfo.append("Duration of previous message" + lastATime + " milliseconds\n");
			}
			if (aTime > 0)
			{
				activeInfo.append("Current message execution time " + aTime + " milliseconds\n");
			}
	%>
			<TR>
				<TD align="center"><B><%=count%></B></TD>
				<TD valign="top" align="center">
					<img src="../images/<%=connectedImage%>.jpg" height="30"/>
				</TD>
				<TD valign="top" align="center">
					<img src="../images/<%=activeImage%>.gif" height="30"/>
				</TD>
				<TD align="center"><B><%=info.getCount()%></B></TD>
				<TD align="left"><PRE><%=activeInfo.toString() + info.getStatus()%></PRE></TD>
			</TR>
	<%		
		}
	%>
	</TABLE><BR/><BR/>

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
