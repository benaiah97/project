<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>JNDI Settings</TITLE>
<%
	java.util.List jndiList = (java.util.List)request.getAttribute("jndiList");
%>
<jsp:useBean id="lookupResult" class="java.lang.Object" scope="request"/>

	<SCRIPT language="JavaScript">
		function viewObject(jndiObjName)
		{
			document.JndiForm.JndiName.value = jndiObjName;
			document.JndiForm.ACTION.value = "LOOKUP";
			document.JndiForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>JNDI Settings</H2></CENTER>

	<FORM name="JndiForm" method="post">
	<INPUT TYPE="hidden" NAME="JndiName" value="?????"/>
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	<INPUT TYPE="hidden" NAME="CMD" value="JNDI"/>
	
  	<TABLE BORDER="1" align="left" width="100%">
  	<TR>
  		<TD valign="top" align="left" width="50%">
  		<H4>Complete JNDI Listing:</H4>
  			<UL>
	<%
	try
	{
		if (jndiList != null)
		{
			java.util.Iterator i = jndiList.iterator();
			while (i.hasNext())
			{
				String jndiName = (String)i.next();
				String jndiValue = "";
				for (int j = 0; j < jndiName.length(); j++)
				{
					char c = jndiName.charAt(j);
					jndiValue += c;
					if (c == '\\')
					{
						jndiValue += c;
					}
				}
	%>
				<LI><A href="javascript:viewObject('<%=jndiValue%>')"><%=jndiName%></A></LI>
	<%		
			}
		}
	%>
		</UL>
		</TD>
	
	<%
		if (lookupResult != null)
		{
			StringBuffer buff = new StringBuffer(lookupResult.toString());
		
			/*
			if (lookupResult instanceof com.ibm.mq.jms.MQQueue)
			{
				com.ibm.mq.jms.MQQueue q = (com.ibm.mq.jms.MQQueue)lookupResult;
				int tc = q.getTargetClient();
				buff.append(" [TargetClient=" + ((tc ==1)?"MQ":"JMS") + "]");
			}
			else if (lookupResult instanceof com.ibm.mq.jms.MQQueueConnectionFactory)
			{
				com.ibm.mq.jms.MQQueueConnectionFactory qcf = (com.ibm.mq.jms.MQQueueConnectionFactory)lookupResult;
				int trans = qcf.getTransportType();
				String qmgr = qcf.getQueueManager();
				String hostname = qcf.getHostName();
				int port = qcf.getPort();
				
				buff.append(" [TransportType=" + ((trans == 1)?"CLIENT":"BIND") + "]");
				buff.append(" [QManager=" + qmgr + "]");
				buff.append(" [Hostname=" + hostname + "]");
				buff.append(" [Port=" + port + "]");
			}
			*/
	%>
		<TD valign="top" align="left" width="50%">
			<H4>Lookup Result:</H4>
			<B><%=lookupResult.getClass().getName()%></B><BR/>
			<I><%=buff.toString()%></I>
		</TD>
	<%
		}
	%>
	
	</TR>
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

	<BR/><BR/>
	</FORM>

</BODY>
</HTML>
