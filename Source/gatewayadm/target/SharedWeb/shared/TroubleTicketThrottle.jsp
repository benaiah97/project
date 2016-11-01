<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.util.*,com.disney.admin.notification.*,com.disney.util.*" errorPage="JSPError.jsp"%>
<%@ page import="pvt.disney.dti.gateway.util.ResourceLoader"%>
<%@ page import="java.util.ResourceBundle"%>

<%
/* Properties variable to store properties */
ResourceBundle rb = ResourceLoader.getResourceBundle("notification");
Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
String dsName = props.getProperty("TT_DATA_SOURCE");

//create instance of TroubleTicketKeyTypeXrefBean and continue getting properties	
TroubleTicketKeyTypeXRefBean ttktxrb = new TroubleTicketKeyTypeXRefBean();
TroubleTicketGroupBean ttgb = new TroubleTicketGroupBean();
TroubleTicketTypeBean tttb = new TroubleTicketTypeBean();
TroubleTicketKeyBean ttkb = new TroubleTicketKeyBean();

//create a vector of all TroubleTicketKeyTypeXrefBeans from the database
List vXrb = ttktxrb.getAll(dsName, props.getProperty(TroubleTicketKeyTypeXRefBean.TABLE+"_GETALL"));
List vTb = tttb.getAll(dsName, props.getProperty(TroubleTicketTypeBean.TABLE+"_GETALL"));
List vKb = ttkb.getAll(dsName, props.getProperty(TroubleTicketKeyBean.TABLE+"_GETALL"));
%>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Trouble Ticket Agent Throttling View</TITLE>
</HEAD>
<BODY>
<form method="POST" action="AdminController">
<input type="hidden" name="CMD" value="TROUBLE_TICKET">
<table>
    <tr>
        <th align="center">
        <h1>Key Info</h1>
        </th>
        <th align="center">
        <h1>Type Info</h1>
        </th>
        <th align="center">
        <h1>Node</h1>
        </th>
        <th align="center">
        <h1>Service Type</h1>
        </th>
        <th align="center">
        <h1>Max Allowed</h1>
        </th>
        <th align="center">
        <h1>Interval</h1>
        </th>
        <th align="center">
        <h1>Frequency</h1>
        </th>
    </tr>
    <% if (vXrb.size() > 0){%>
    <%for (int i = 0; i < vXrb.size(); i++){
	ttktxrb = (TroubleTicketKeyTypeXRefBean)vXrb.get(i);
	if (i%2 == 0 ){%>
    <tr>
        <%}else {%>
    <tr bgcolor="AAAAFF">
        <%}%>
        <td><%=ttktxrb.getKeyCompId()%> <%=ttktxrb.getKeyEaiErr()%> <%=ttktxrb.getKeyEvent()%></td>
        <td><%=ttktxrb.getTypeName()%></td>
        <td><%=ttktxrb.getEaiNode()%></td>
        <td><%=ttktxrb.getSvcType()%></td>
        <td align="center"><%if (ttktxrb.getMaxAllowed() >= 0){%> <%=ttktxrb.getMaxAllowed()%><%}%></td>
        <td align="center"><%if (ttktxrb.getInterval() >= 0){%> <%=ttktxrb.getInterval()%><%}%></td>
        <td align="center"><%if (ttktxrb.getFrequency() >= 0){%><%=ttktxrb.getFrequency()%><%}%></td>
        <td align="center"><input type="checkbox" name="throt<%=ttktxrb.getID()%>" value="<%=ttktxrb.getID()%>"></td>
    </tr>
    <%}%>
    <%}%>
    <%
    //if all the dropdowns can be populated from the database do this...
    if (vTb.size() > 0  && vKb.size() > 0 ){
    if (vXrb.size() % 2 == 0) {%>
    <tr>
        <% } else {%>
    <tr bgcolor="AAAAFF">
        <%}%>
        <td><SELECT name="key">
            <%for (int i = 0; i < vKb.size(); i++){%>
            <option value="<%=((TroubleTicketKeyBean)vKb.get(i)).getID()%>"><%=((TroubleTicketKeyBean)vKb.get(i)).getCompId()%> <%=((TroubleTicketKeyBean)vKb.get(i)).getErrCode()%> <%=((TroubleTicketKeyBean)vKb.get(i)).getEvent()%></option>
            <%}%>
        </SELECT></td>
        <td><SELECT name="type">
            <%for (int i = 0; i < vTb.size(); i++){%>
            <option value="<%=((TroubleTicketTypeBean)vTb.get(i)).getID()%>"><%=((TroubleTicketTypeBean)vTb.get(i)).getName()%></option>
            <%}%>
        </SELECT></td>
        <td align="center"><INPUT type="text" name="node" value=""></td>
        <td align="center"><INPUT type="text" name="svc" value=""></td>
        <td align="center"><INPUT type="text" name="max" value="" maxlength="3" size="4"></td>
        <td align="center"><INPUT type="text" name="int" value="" maxlength="3" size="4"></td>
        <td align="center"><INPUT type="text" name="freq" value="" maxlength="3" size="4"></td>
        <td align="center"><INPUT type="submit" name="ADDthrot" value="ADD SETTINGS"></td>
    </tr>
    <%}%>
    <tr>
        <td colspan="9" align="center"><input type="submit" name="DELETEthrot" value="Delete Checked"></td>
    </tr>
</table>
</form>

</BODY>
</HTML>
