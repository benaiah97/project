<%--
 * ----------------------------- COPYRIGHT NOTICE ----------------------------- 
 * This file contains confidential and proprietary information of The Walt 
 * Disney Company.  No license or permission is hereby granted to use such 
 * information in any manner.  (c)Disney.  All rights reserved.
 * 
 * -------------------------- CHANGE LOG INSTRUCTIONS -------------------------
 * Information documenting changes to this file should be logged here.  Please
 * indent line wraps 4 spaces.  Log entries should be made in the format: 
 * 
 * <Date> - <Name/ID> - <CM Synergy Task #> - <Defect # (Opt.)> - <Description>
 * 
 * -------------------------------- CHANGE LOG --------------------------------
 * Oct 07, 2004 - Antonio Rey - <TASK # 220> - <Description: 
 *              - Enhance View Message Stats to take advantage of event table 
 *                partitions.  
 *              - Added processing for new field: afterStr
 *              - END >
 * 
 * ----------------------------------- END ------------------------------------
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page errorPage="JSPError.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="com.disney.util.*" %>
<%@ page import="com.disney.xml.*" %>
<%@ page import="com.disney.admin.util.*" %>
<%@ page import="com.disney.admin.statistics.*" %>

<jsp:useBean id="jmsDefList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="testMessageMap" class="java.util.HashMap" scope="request"/>
<jsp:useBean id="jmsDefName" class="java.lang.String" scope="request"/>
<jsp:useBean id="currentDir" class="java.lang.String" scope="request"/>
<jsp:useBean id="currentMessage" class="java.lang.String" scope="request"/>
<jsp:useBean id="currentMsgName" class="java.lang.String" scope="request"/>
<jsp:useBean id="responseMessage" class="java.lang.String" scope="request"/>
<jsp:useBean id="timeout" class="java.lang.String" scope="request"/>
<jsp:useBean id="elapsedTime" class="java.lang.String" scope="request"/>
<jsp:useBean id="startTime" class="java.lang.String" scope="request"/>
<jsp:useBean id="endTime" class="java.lang.String" scope="request"/>
<jsp:useBean id="messageId" class="java.lang.String" scope="request"/>
<jsp:useBean id="conversationId" class="java.lang.String" scope="request"/>

<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Message Test Utility</TITLE>
	<SCRIPT language="JavaScript">
		function onLoad(){
			document.MsgTestForm.CURRENT_MSG.focus();
			document.MsgTestForm.CURRENT_MSG.select();
		}
		function pleaseReadFile()
		{
			document.MsgTestForm.ACTION.value = "READ_MSG";
			document.MsgTestForm.action = "AdminController";
			document.MsgTestForm.target = "mainFrame";
			document.MsgTestForm.submit();
		}
		function changeDir()
		{
			document.MsgTestForm.ACTION.value = "CHANGE_DIR";
			document.MsgTestForm.action = "AdminController";
			document.MsgTestForm.target = "mainFrame";
			document.MsgTestForm.submit();
		}
		function display(name)
		{
			document.MsgTestForm.ACTION.value = name;
			document.MsgTestForm.action = "shared/XmlMessageDisplay.jsp";
			document.MsgTestForm.target = name + "Data";
			document.MsgTestForm.submit();
		}	 		
		function sendMsg()
		{
			document.MsgTestForm.ACTION.value = "SEND_MSG";
			document.MsgTestForm.action = "AdminController";
			document.MsgTestForm.target = "mainFrame";
			document.MsgTestForm.submit();
		}

	</SCRIPT>
<%	out.println("<!-- timeout='" + timeout + "' -->");
	out.println("<!-- jmsDefName='" + jmsDefName + "' -->");
%>
</HEAD>
<BODY onload="onLoad()">

	<CENTER><H2>Message Test Utility</H2></CENTER>

	<FORM name="MsgTestForm" method="post">
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	<INPUT TYPE="hidden" NAME="CMD" value="JMS_MSG_TEST"/>
	
	<TABLE BORDER="0" WIDTH="100%">
	<TR>
	<TD WIDTH="30%" valign="top">
	  	<H4>Test Message Directories:</H4>
		<SELECT NAME="CURRENT_DIR" onchange="javascript:changeDir()">
	<%
	try
	{
		java.util.Iterator dirs = testMessageMap.keySet().iterator();
		while (dirs.hasNext())
		{
			String dirName = (String)dirs.next();
	%>
			<OPTION VALUE="<%=dirName%>" <%=(dirName.equalsIgnoreCase(currentDir)?"selected":"")%>><%=dirName%></OPTION>
	<%		
		}
	%>
		</SELECT>
	</TD>
	<TD WIDTH="30%" valign="top">	
	  	<H4>Available Test Messages:<%=currentDir%></H4>
		<SELECT NAME="FILE_TO_READ" onchange="javascript:pleaseReadFile()">
			<OPTION VALUE="">Select Message to Load...</OPTION>
	<%
		if (currentMessage == null)
		{
			currentMessage = "";
		}

		if (timeout == null)
		{
			timeout = "90";
		}

		if (jmsDefName == null || jmsDefName.length() == 0)
		{
			jmsDefName = "FB";
		}

		if (testMessageMap.containsKey(currentDir))
		{
			java.util.ArrayList testMessageList = (java.util.ArrayList)testMessageMap.get(currentDir);
			Sorter.sortStringList(testMessageList);
			java.util.Iterator i = testMessageList.iterator();
			while (i.hasNext())
			{
				String msgFileName = (String)i.next();
	%>
			<OPTION VALUE="<%=msgFileName%>" <%=(msgFileName.equalsIgnoreCase(currentMsgName)?"selected":"")%>><%=msgFileName%></OPTION>
	<%		
			}
		}
	%>
		</SELECT></BR>
		<INPUT type="button" name="SEND_MSG" value="SEND MESSAGE" onclick="javascript:sendMsg()"/>
	</TD>	
	<TD WIDTH="30%" valign="top">
		<H4>Jms Definition used for Test:</H4>
		<SELECT NAME="JMS_DEF">
			<OPTION VALUE="">Select Jms Definition...</OPTION>
	<%
		// Sort the jms def list
		Sorter.sortStringList(jmsDefList);
		java.util.Iterator j = jmsDefList.iterator();
		while (j.hasNext())
		{
			String nextJmsDefName = (String)j.next();
	%>
			<OPTION VALUE="<%=nextJmsDefName%>" <%=(nextJmsDefName.equalsIgnoreCase(jmsDefName)?"selected":"")%>><%=nextJmsDefName%></OPTION>
	<%		
		}
	%>
		</SELECT><BR/>
		Timeout:<INPUT type="text" name="TIMEOUT" value="<%=timeout%>" size="10"/> (seconds)
	</TD>
	</TABLE>

	<% if (responseMessage.length() > 0) {
	%>
	<H4>Response Message:</H4>
	<%
	 if (elapsedTime.length() > 0)
	 	{
	%>
	<a href="javascript:display('RESPONSE_MSG')"><img alt="View XML" src="images/xml.gif"></a>
		<B>Start Time: </B> <font color="blue"><%=startTime%></font>
		<B> Elapsed Time: </B> <font color="blue"><%=elapsedTime%></font>
<%
	 	}
	if ((messageId.length() > 0) || (conversationId.length() > 0))
	{
		messageId = HtmlConverter.fixHtmlFormData(messageId);
		conversationId = HtmlConverter.fixHtmlFormData(conversationId);
		String msgStatsUrl = MessageStatsCommand.getMessageStatsUrl();
		String afterStr = Converter.printISODate(new java.util.Date(System.currentTimeMillis() - 120000));
%>
		<b><a href="<%=msgStatsUrl%>&ACTION=QUERY&CONV_ID=<%=conversationId%>&MSG_ID=<%=messageId%>&AFTER=<%=afterStr%>" target="StatWindow">View Message Statistics</a></b>
<%
	}
%>
	 <BR>

	<% 
	   }
	
	String deCurrent = HtmlConverter.doubleEscapeData(currentMessage);
	%>

	<H4>Current Message:  <%=currentMsgName%></H4>
	<TABLE BORDER="0"">
	<TR><TD>
	<a href="javascript:display('CURRENT_MSG')"><img alt="View XML" src="images/xml.gif"></a><BR>
	<TEXTAREA NAME='CURRENT_MSG' ROWS=20 COLS=100><%out.print(deCurrent); %></TEXTAREA>
	</TD><TD align="left" valign="top">
	<% if (responseMessage.length() > 0) {
	%>
	<INPUT type="button" name="SEND_MSG" value="SEND MESSAGE" onclick="javascript:sendMsg()"/>
	<%	 	
	 	}
	 %>
	</TD></TR>
	</TABLE>
	</FORM>

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
