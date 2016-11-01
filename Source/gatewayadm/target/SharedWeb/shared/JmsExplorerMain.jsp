<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.jms.*,com.disney.util.*,com.disney.admin.util.*" errorPage="JSPError.jsp" %>
<%!
	public String getJndiValue(String jndiName)
	{
		if (jndiName == null)
		{
			return null;
		}
			
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
		return jndiValue;
	}
%>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>JMS Explorer</TITLE>
<jsp:useBean id="jmsDefList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="jmsDefName" class="java.lang.String" scope="request"/>
<jsp:useBean id="jmsDetailsBean" class="com.disney.admin.jms.JmsDetailsBean" scope="request"/>
<jsp:useBean id="throwable" class="java.lang.Throwable" scope="request"/>

	<SCRIPT language="JavaScript">
		function exploreJmsDef(aName)
		{
			document.JmsForm.JMS_DEF_NAME.value = aName;
			document.JmsForm.ACTION.value = "EXPLORE";
			document.JmsForm.action = "AdminController";
			document.JmsForm.target = "mainFrame";
			document.JmsForm.submit();
		}	 		
		function display(msgId)
		{
			document.JmsForm.ACTION.value = msgId + "TEXT";
			document.JmsForm.action = "shared/XmlMessageDisplay.jsp";
			document.JmsForm.target = msgId + "Data";
			document.JmsForm.submit();
		}
		function viewObject(jndiObjName)
		{
			document.JndiForm.JndiName.value = jndiObjName;
			document.JndiForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>JMS Explorer</H2></CENTER>

	<FORM name="JndiForm" method="post" target="JndiPopup" action="AdminController">
		<INPUT TYPE="hidden" NAME="JndiName" value=""/>
		<INPUT TYPE="hidden" NAME="ACTION" value="LOOKUP"/>
		<INPUT TYPE="hidden" NAME="CMD" value="JNDI"/>
	</FORM>
	
	<FORM name="JmsForm" method="post">
		<INPUT TYPE="hidden" NAME="JMS_DEF_NAME" value="<%=jmsDefName%>"/>
		<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
		<INPUT TYPE="hidden" NAME="CMD" value="JMS_EXPLORER"/>
	
		<TABLE width="90%" border="1">	
			<TR>
				<TD width="25%" valign="top" nowrap>
					<H4>All Jms Definitions found:</H4>
					<UL>
	<%
	try
	{
		if (jmsDefList != null)
		{
			Sorter.sortStringList(jmsDefList);
			java.util.Iterator defs = jmsDefList.iterator();
			while (defs.hasNext())
			{
				String nextDefName = (String)defs.next();
	%>
						<LI><A href="javascript:exploreJmsDef('<%=nextDefName%>')"><%=nextDefName%></A>	
	<%
			}
		}
	%>
					</UL>
				</TD>

				<TD width="75%" valign="top">
	<%
		if ( (jmsDetailsBean != null) && (jmsDetailsBean.getJmsDefName() != null) )
		{
			String name = jmsDetailsBean.getJmsDefName();
	%>
					<H4>Details of: <%=name%></H4>
	<%
			com.disney.jms.JmsDefinition def = jmsDetailsBean.getJmsDef();
			if (def != null)
			{
	%>
					<H5>Jms Definition:</H5>
					<UL>
						<LI><B>Initial Context Factory: </B><I><%=def.getInitialContextFactoryName()%></I></LI>
						<LI><B>Provider URL: </B><I><%=def.getProviderURL()%></I></LI>
						<LI><B>Connection Type: </B><I><%=def.getJmsConnectionType()%></I></LI>
						<LI><B>Connection Factory Name: </B><A href="javascript:viewObject('<%=this.getJndiValue(def.getJmsConnectionFactoryName())%>')"><%=def.getJmsConnectionFactoryName()%></A></LI>
						<LI><B>Destination: </B><A href="javascript:viewObject('<%=this.getJndiValue(def.getJmsDestination())%>')"><%=def.getJmsDestination()%></A></LI>
						<LI><B>Reply To: </B><A href="javascript:viewObject('<%=this.getJndiValue(def.getJmsReplyTo())%>')"><%=def.getJmsReplyTo()%></A></LI>
						<LI><B>Message Selector: </B><I><%=def.getMsgSelector()%></I></LI>
						<LI><B>Message Listener Class: </B><I><%=def.getMsgListenerClass()%></I></LI>
						<LI><B>Username: </B><I><%=def.getUsername()%></I></LI>
						<LI><B>Password: </B><I>******</I></LI>
					</UL>
	<%
			}	// End Definition output
			
			if(def.getRQEmpty() || def.getRSEmpty())
			{
	%>
				<H5>Empty Queues:</H5>
	<%
			}
			
			if (def.getRQEmpty())
			{
	%>	
				<INPUT type="submit" name="REQ_CLEAR" value="Empty RQ Queue" onclick="document.JmsForm.ACTION.value='REQ_EMPTY'">
	<%
			}
			
			if (def.getRSEmpty())
			{
	%>
				<INPUT type="submit" name="RSP_CLEAR" value="Empty RS Queue" onclick="document.JmsForm.ACTION.value='RSP_EMPTY'">
	<%
			}
			if (jmsDetailsBean.getJmsBrowser() != null)
			{
				JmsBrowser browser = jmsDetailsBean.getJmsBrowser();
				java.util.Enumeration msgs = browser.getMessages();
	%>
					<H5>Contents of: <%=def.getJmsDestination()%> </H5>
					<TABLE width="100%" border="1">
						<TR>
							<TH bgcolor="#cccccc">Type</TH>
							<TH bgcolor="#cccccc">Message Id</TH>
							<TH bgcolor="#cccccc">Correlation Id</TH>
							<TH bgcolor="#cccccc">Reply To</TH>
							<TH bgcolor="#cccccc">Timestamp</TH>
							<TH bgcolor="#cccccc">Expiration</TH>
							<TH bgcolor="#cccccc">Priority</TH>
						</TR>
	<%				
				int count = 0;
				while (msgs.hasMoreElements())
				{
					count++;
					javax.jms.Message msg = (javax.jms.Message) msgs.nextElement();
					if (msg != null)
					{
						String className = msg.getClass().getName();
						String type = className.substring(className.lastIndexOf(".") + 1);
						String msgIdData = null;
						try
						{
							javax.jms.TextMessage tMsg = (javax.jms.TextMessage)msg;
							msgIdData = tMsg.getText();
						}
						catch(Throwable th)
						{
							//do nothin
						}	
	%>
						<TR>
							<TD><%=type%></TD>
							<TD><A href="javascript:display('<%=count%>')"><%=msg.getJMSMessageID()%></A></TD>
							<TD><%=msg.getJMSCorrelationID()%></TD>
							<TD><%=msg.getJMSReplyTo()%></TD>
							<TD nowrap><%=new java.util.Date(msg.getJMSTimestamp()).toString()%></TD>
							<TD><%=msg.getJMSExpiration()%></TD>
							<TD><%=msg.getJMSPriority()%></TD>
						<TR>
						
						<DIV style="visibility: hidden;position: absolute;">
							<TEXTAREA name="<%=count%>TEXT"><%=msgIdData%></TEXTAREA>
						</DIV>				
	<%
					}
				}
	%>
					</TABLE>
					<B><%=count%> Messages Found.</B>

	<%
				// Output reply messages
				java.util.Enumeration replies = null;
				try
				{
					replies = browser.getReplyMessages();
				}
				catch (Throwable th)
				{	}
				
	%>
					<H5>Contents of: <%=def.getJmsReplyTo()%> </H5>
					<TABLE width="100%" border="1">
						<TR>
							<TH bgcolor="#cccccc">Type</TH>
							<TH bgcolor="#cccccc">Message Id</TH>
							<TH bgcolor="#cccccc">Correlation Id</TH>
							<TH bgcolor="#cccccc">Reply To</TH>
							<TH bgcolor="#cccccc">Timestamp</TH>
							<TH bgcolor="#cccccc">Expiration</TH>
							<TH bgcolor="#cccccc">Priority</TH>
						</TR>
	<%				
				int rCount = 0;
				while ( (replies !=null) && (replies.hasMoreElements()) )
				{
					rCount++;
					javax.jms.Message msg = (javax.jms.Message) replies.nextElement();
					if (msg != null)
					{
						String className = msg.getClass().getName();
						String type = className.substring(className.lastIndexOf(".") + 1);
						String msgIdData = null;
						try
						{
							javax.jms.TextMessage tMsg = (javax.jms.TextMessage)msg;
							msgIdData = HtmlConverter.doubleEscapeData(tMsg.getText());
						}
						catch(Throwable th)
						{
							//do nothin
						}	
	%>
						<TR>
							<TD><%=type%></TD>
							<TD><A href="javascript:display('<%=rCount + count%>')"><%=msg.getJMSMessageID()%></A></TD>
							<TD><%=msg.getJMSCorrelationID()%></TD>
							<TD><%=msg.getJMSReplyTo()%></TD>
							<TD nowrap><%=new java.util.Date(msg.getJMSTimestamp()).toString()%></TD>
							<TD><%=msg.getJMSExpiration()%></TD>
							<TD><%=msg.getJMSPriority()%></TD>
						<TR>
						
						<DIV style="visibility: hidden;position: absolute;">
							<TEXTAREA name="<%=rCount + count%>TEXT"><%=msgIdData%></TEXTAREA>
						</DIV>
	<%
					}
				}
	%>
					</TABLE>
					
					<B><%=rCount%> Reply Messages Found.</B>
	<%
			}	// End browser output (both Destination and ReplyTo)
		}	// End details output
	%>
				</TD>
			</TR>
		</TABLE>
	
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