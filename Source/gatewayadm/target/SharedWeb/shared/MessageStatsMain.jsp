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
 *              - Enhance Message Stats to take advantage of event table 
 *                partitions.  
 *              - Added processing for two new fields: AFTER, & BEFORE.
 *              - Added bean.
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

<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Message Statistics Main</TITLE>

<%-- BEAN CLASSPATH = "SharedFoundation\Code\SharedWeb\Java Source" --%>
<jsp:useBean id="bean" class="com.disney.admin.statistics.MessageStatsBean" scope="request"/>

	<SCRIPT language="JavaScript">
		function refresh()
		{
			document.StatForm.ACTION.value = "REFRESH";
			document.StatForm.submit();
		}
		function onLoad(){
			document.StatForm.AFTER.focus();
		}
	</SCRIPT>


</HEAD>
<BODY onload="onLoad()">

	<%
			
	%>

		<!-- Show the Form Only -->
		<FORM name="StatForm" target="StatWindow">
			<TABLE width="50%">
				<TR>
					<TD colspan="2">
						<INPUT name="ACTION" type="hidden" value="QUERY"/>
						<INPUT TYPE="hidden" NAME="CMD" value="MSG_STATS"/>
						<CENTER><H2>Query a Message's Statistics</H2></CENTER>
					</TD>
				</TR>
				<TR>
					<TD>
						<b>Message Id:</b>
					</TD>
					<TD>
						<INPUT name="MSG_ID" type="text" value="" size="50" maxlength="200"/>
					</TD>
				</TR>
				<TR>
					<TD>
						<b>Conversation Id:</b>
					</TD>
					<TD>
						<INPUT name="CONV_ID" type="text" value="" size="50" maxlength="200"/>
					</TD>
				</TR>


				<TR>
					<TD>
						<B>Created After:</B>
					</TD>
					<TD>
						<INPUT name="AFTER" value=" " type="text" size="30"/><BR/>
						In the Form:
						<font color="blue">
						<%=Converter.printISODateMS(new java.util.Date())%>
						</font>
					</TD>
				</TR>

				<TR>
					<TD>
						<B>Created Before:</B>
					</TD>
					<TD>
						<INPUT name="BEFORE" value=" " type="text" size="30"/><BR/>
						In the Form: <font color="blue">
						<%=Converter.printISODateMS(new java.util.Date())%>
						</font>
					</TD>
				</TR>

				<TR>
					<TD align="center">
						<INPUT type="submit" value="Query" name="QUERY"/>
					</TD>
				</TR>


			</TABLE>
		</FORM>

</BODY>
</HTML>
