<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.util.*,java.sql.*,com.disney.admin.notification.*,com.disney.util.*" errorPage="JSPError.jsp"%>
<jsp:useBean
    id="vHistory" scope="request" class="java.util.Vector"/>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Trouble Ticket Agent Notification History</TITLE>
</HEAD>
<BODY>
<%
long now = System.currentTimeMillis();
%>
<form method="POST" action="AdminController">
<input type="hidden" name="CMD" value="TROUBLE_TICKET">
<table>
    <tr>
        <td>
        <table>
            <tr>
                <td>Start Date</td>
                <td><input type="text" name="histStart" maxlength="" value="<%=new java.sql.Timestamp(now - 600000)%>"></td>
            </tr>
            <tr>
                <td>End Date</td>
                <td><input type="text" name="histEnd" maxlength="" value="<%=new java.sql.Timestamp(now)%>"></td>
            </tr>
            <tr>
                <td colspan="2" align="center">Insert times in timestamp format <br>
                <strong><i>(yyyy-mm-dd HH:mm:ss)</i></strong></td>
            </tr>
        </table>
        </td>
        <td>
        <table>
            <tr>
                <td><input type="submit" value="Last 5 min" size="20" onclick="histStart.value='<%=new java.sql.Timestamp(now - 300000)%>'"></td>
                <td><input type="submit" value="Last 10 min" onclick="histStart.value='<%=new java.sql.Timestamp(now - 600000)%>'"></td>
            </tr>
            <tr>
                <td><input type="submit" value="Last 15 min" onclick="histStart.value='<%=new java.sql.Timestamp(now - 900000)%>'"></td>
                <td><input type="submit" value="Last 30 min" onclick="histStart.value='<%=new java.sql.Timestamp(now - 1800000)%>'"></td>
            </tr>
            <tr>
                <td><input type="submit" value="Last Hour" onclick="histStart.value='<%=new java.sql.Timestamp(now - 3600000)%>'"></td>
                <td><input type="submit" value="Last 24 hours" onclick="histStart.value='<%=new java.sql.Timestamp(now - 86400000)%>'"></td>
            </tr>
        </table>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center"><input type="submit" value="Get History"></td>
    </tr>
</table>
<input type="hidden" name="HISTORY" value="HISTORY"></form>
<%if (vHistory.size() >= 1){%>
<hr>
	<table>
	<tr>
	<th><h1>Send Time</h1></th>
	<th><h1>Key Data</h1></th>
	<th><h1>Message Type</h1></th>
	<th><h1>Group</h1></th>
	</tr>
	<%for (int i = 0; i < vHistory.size(); i++){
		TroubleTicketHistoryBean tthb = (TroubleTicketHistoryBean)vHistory.elementAt(i);
		if (i%2 == 0 ){%>
			<tr>
		<%}else {%>
			<tr bgcolor="AAAAFF">
		<%}%>
			<td><%=tthb.getSendTime()%></td>
			<td><%=tthb.getKeyCompId()%> <%=tthb.getKeyErrCd()%> <%=tthb.getKeyEvntType()%></td>
			<td><%=tthb.getTypeName()%></td>
			<td><%=tthb.getDescription()%></td>
		</tr>
	<%}%>
	</table>
<%}%>
</BODY>
</HTML>
