<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*,com.disney.jms.*" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Message Bean Containers</TITLE>
<jsp:useBean id="throwable" class="java.lang.Throwable" scope="request"/>

	<SCRIPT language="JavaScript">
		function buttonPush(actionVal, conName)
		{
			document.ContainerForm.ACTION.value = actionVal;
			document.ContainerForm.ContainerName.value = conName;
			document.ContainerForm.submit();
		}
		
		function openWindow(conName)
		{
			window.open('shared/MessageBeanContainerDetails.jsp?CONTAINER_NAME=' + conName, conName + 'Details', 'toolbar=no,menubar=no,width=400,height=600,resizable=yes,scrollbars=yes');
		}
	</SCRIPT>
</HEAD>
<BODY>

	<CENTER><H2>Message Bean Containers</H2></CENTER>

	<FORM name="ContainerForm" method="post">
	<INPUT TYPE="hidden" NAME="ContainerName" value="?????"/>
	<INPUT TYPE="hidden" NAME="ACTION" value="?????"/>
	<INPUT TYPE="hidden" NAME="CMD" value="MSG_BEANS"/>
	
	<%
	try
	{
		java.util.List names = MessageBeanContainerFacadeFactory.getContainerNames();
		Sorter.sortStringList(names);
		java.util.Iterator it = names.iterator();
		while (it.hasNext())
		{
			String containerName = (String)it.next();
			MessageBeanContainerFacade container = null;
			try
			{
				container = MessageBeanContainerFacadeFactory.getMessageBeanContainerFacade(containerName);
			}
			catch (Throwable th)
			{
				// If an error is caught, spit it out and continue to next name in the list.
	%>
		<H4>Message Bean Container: <%=containerName%></H4>
	  	<TABLE BORDER="0" width="80%" align="center">
			<TR>
				<TD valign="top" align="center">
					<img src="images/red.jpg" height="30"/>
				</TD>
				<TD>
					<font color="red">
						<B>Exception Thrown attempting to create Message Bean Container:</B><BR>
						<%=PrintStack.getTraceString(th)%>
					</font>	
				</TD>
			</TR>
		</TABLE>		
	<%
				continue;
			}	

			boolean isSubscribing = container.isSubscribing();
			JmsDefinition jmsDef = container.getJmsDef();
			
			String msgSelect = jmsDef.getMsgSelector();
			if (msgSelect == null)
			{
				msgSelect = "&nbsp;";
			}
			
	%>
		<H4>Message Bean Container: <A href="javascript:openWindow('<%=containerName%>')"><%=containerName%></A></H4>
	  	<TABLE BORDER="0" width="80%" align="center">
			<TR>
				<TD valign="top" align="center">
	<%
			if (isSubscribing)
			{
				%><img src="images/green.jpg" height="30"/><%
			}
			else 
			{
				%><img src="images/red.jpg" height="30"/><%
			}
	%>
				</TD>
				
				<TD><UL>
					<LI><B>Jms Destination: </B> <I><%=jmsDef.getJmsDestination()%></I> </LI>
					<LI><B>Jms Destination Type: </B> <I><%=jmsDef.getJmsConnectionType()%></I> </LI>
					<LI><B>Message Bean Class: </B> <I><%=jmsDef.getMsgListenerClass()%></I> </LI>
					<LI><B>Message Selector: </B> <I><%=jmsDef.getMsgSelector()%></I> </LI>
	<%
			if (container != null)
			{		
	%>
					<LI><B>Started: </B> <I><%=container.getStartDate()%></I> </LI>
					<LI><B>Number of Messages Read: </B> <I><%=container.getCount()%></I> </LI>
					<LI><B>Number of Message Beans: </B> <I><%=container.getMessageBeanCount()%></I> </LI>
	<%
			}
	%>
				</UL></TD>

				<TD valign="top" align="center">
	<%
			if (isSubscribing)
			{
	%>
				<INPUT type="button" name="STOP_ACTION" value="STOP" onclick="javascript:buttonPush('STOP', '<%=jmsDef.getDefinitionName()%>')"></INPUT><BR/>
				<INPUT type="button" name="RESET_ACTION" value="RESET" onclick="javascript:buttonPush('RESET', '<%=jmsDef.getDefinitionName()%>')"></INPUT>
	<%
			}
			else 
			{
	%>
				<INPUT type="button" name="START_ACTION" value="START" onclick="javascript:buttonPush('START', '<%=jmsDef.getDefinitionName()%>')"></INPUT>
	<%
			}
	%>
				</TD>
			</TR>
		</TABLE><BR/><BR/>
	<%		
		}
	%>

	</FORM>

	<%
		String throwString = throwable.toString();
		String throwTrace = PrintStack.getTraceString(throwable);
		if (! ((throwString.indexOf("java.lang.Throwable") >= 0) && (throwString.length() < 25)) )
		{
	%>
	<FONT color="red">
		<H4>Exception Occurred during last operation:</H4>
		<B>Error: <%=throwString%></B><BR/>
		<I>Stack Trace: <%=throwTrace%></I><BR/>
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
