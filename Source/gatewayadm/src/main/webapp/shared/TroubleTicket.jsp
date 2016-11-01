<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.util.*,com.disney.admin.notification.*,com.disney.util.*" errorPage="JSPError.jsp"%>
<%@ page import="pvt.disney.dti.gateway.util.ResourceLoader"%>
<%@ page import="java.util.ResourceBundle"%>

<%
	/* Properties variable to store properties */
	ResourceBundle rb = ResourceLoader.getResourceBundle("notification");
	Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
	String dsName = props.getProperty("TT_DATA_SOURCE");
	
	
	//get groups - always want at least a vector even if it is empty
	TroubleTicketGroupBean ttgb = new TroubleTicketGroupBean();
	String gQuery = props.getProperty(TroubleTicketGroupBean.TABLE + "_GETALL");
	List vGroups = ttgb.getAll(dsName, gQuery);
	
	//get types - always want at least a vector even if it is empty
	TroubleTicketTypeBean tttb = new TroubleTicketTypeBean();
	String tQuery = props.getProperty(TroubleTicketTypeBean.TABLE + "_GETALL");
	List vTypes = tttb.getAll(dsName, tQuery);
	
	//get keys - always want at least a vector even if it is empty
	TroubleTicketKeyBean ttkb = new TroubleTicketKeyBean();
	String kQuery = props.getProperty(TroubleTicketKeyBean.TABLE + "_GETALL");
	List vKeys = ttkb.getAll(dsName, kQuery);
	
	//get keygroups - always want at least a vector even if it is empty
	TroubleTicketKeyGroupXRefBean ttkgxrb = new TroubleTicketKeyGroupXRefBean();
	String kgQuery = props.getProperty(TroubleTicketKeyGroupXRefBean.TABLE + "_GETALL");
	List vKeyGroup = ttkgxrb.getAll(dsName, kgQuery);
	List vGb = ttgb.getAll(dsName, props.getProperty(TroubleTicketGroupBean.TABLE+"_GETALL"));
	List vKb = ttkb.getAll(dsName, props.getProperty(TroubleTicketKeyBean.TABLE+"_GETALL"));
%>

<HTML>
<HEAD>

<META name="GENERATOR" content="IBM WebSphere Studio">

<LINK href="admin.css" rel="stylesheet" type="text/css">

<TITLE>Trouble Ticket Agent Management View</TITLE>

<SCRIPT language="JavaScript">
	function clearMe(theField) {
		theField.value = "";
	}
	function toggle(divId)
	{
		myDiv = document.getElementById(divId);
		if (myDiv.style.display == 'none')
		{
			myDiv.style.display = 'inline';
		}
		else
		{
			myDiv.style.display = 'none';
		}
	}
</SCRIPT>
</HEAD>
<BODY>
<TABLE>
    <TR>
        <TH width="25%">
        <h1>Groups</h1>
        <A href="javascript:toggle('GROUPS_DIV')">Show/Hide</A>
        </TH>
        <TH width="25%">
        <h1>Types</h1>
        <A href="javascript:toggle('TYPES_DIV')">Show/Hide</A>
        </TH>
        <TH width="25%">
        <h1>Keys</h1>
        <A href="javascript:toggle('KEYS_DIV')">Show/Hide</A>
        </TH>
        <TH width="25%">
        <h1>Key:Group</h1>
        <A href="javascript:toggle('KEY_GROUP_DIV')">Show/Hide</A>
        </TH>
    </TR>
    <TR>
        <TD valign="top">
		<DIV style="display:inline" id="GROUPS_DIV">
        <TABLE align="center" width="95%">
            <tr>
                <th>
                <h2>Name</h2>
                </th>
                <th>
                <h2>Description</h2>
                </th>
            </tr>
            <form method="POST" action="AdminController">
            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
            <%for (int i = 0; i < vGroups.size(); i ++){
            if (i%2 == 0){
					out.print("<tr><td><strong>" + ((TroubleTicketGroupBean)vGroups.get(i)).getGroupName() + "&nbsp;</strong></td>");
					out.print("<td>" + ((TroubleTicketGroupBean)vGroups.get(i)).getDescription() + "&nbsp;</td>");
			} else {
					out.print("<tr bgcolor=\"AAAAFF\"><td><strong>" + ((TroubleTicketGroupBean)vGroups.get(i)).getGroupName() + "&nbsp;</strong></td>");
					out.print("<td>" + ((TroubleTicketGroupBean)vGroups.get(i)).getDescription() + "&nbsp;</td>");
			}%>
            <td><input type="checkbox" value="<%=((TroubleTicketGroupBean)vGroups.get(i)).getID()%>" name="groups<%=((TroubleTicketGroupBean)vGroups.get(i)).getID()%>" UNCHECKED></td>
            </tr>
            <%}%>
            <tr>
                <td colspan="3" align="center"><input type="submit" value="Delete Checked" name="DELETEgroups"></td>
            </tr>
            </form>
            <tr>
	            <form method="POST" action="AdminController">
	            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
                <td><input name="groupName" type="text" value=""></td>
                <td><input name="groupDescription" type="text" value=""></td>
                <td><input type="submit" value="add"></td>
                <input type="hidden" value="ADDgroup" name="ADDgroup">
                </form>
            </tr>
            <tr>
                <th align="left" colspan="3"><i><strong>Groups are Service Center queues and can be used to</strong>
                <ul>
                    <li>Generate Tickets</li>
                    <li>Create eMails</li>
                    <li>Send Pages</li>
                    <ul>
                </i></th>
            </tr>
        </TABLE>
        </DIV>
        </TD>
        <TD valign="top">
		<DIV style="display:inline" id="TYPES_DIV">
        <TABLE align="center" width="95%">
            <tr>
                <th>
                <h2>Name</h2>
                </th>
                <th>
                <h2>Value</h2>
                </th>
            </tr>
            <form method="POST" action="AdminController">
            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
            <%for (int i = 0; i < vTypes.size(); i ++){
            if (i%2 == 0){
					out.print("<tr><td><strong>" + ((TroubleTicketTypeBean)vTypes.get(i)).getName() + "&nbsp;</strong></td>");
					out.print("<td>" + ((TroubleTicketTypeBean)vTypes.get(i)).getValue() + "&nbsp;</td>");
			} else {
					out.print("<tr bgcolor=\"AAAAFF\"><td><strong>" + ((TroubleTicketTypeBean)vTypes.get(i)).getName() + "&nbsp;</strong></td>");
					out.print("<td>" + ((TroubleTicketTypeBean)vTypes.get(i)).getValue() + "&nbsp;</td>");
			}%>
            <td><input type="checkbox" value="<%=((TroubleTicketTypeBean)vTypes.get(i)).getID()%>" name="types<%=((TroubleTicketTypeBean)vTypes.get(i)).getID()%>" UNCHECKED></td>
            </tr>
            <%}%>
            <tr>
                <td colspan="3" align="center"><input type="submit" value="Delete Checked" name="DELETEtypes"></td>
            </tr>
            </form>

            <tr>
		        <form method="POST" action="AdminController">
		        <input type="hidden" name="CMD" value="TROUBLE_TICKET">
                <td><input name="typeName" type="text" value=""></td>
                <td><input name="typeValue" type="text" value=""></td>
                <td><input type="submit" value="add"></td>
                <input type="hidden" value="ADDtype" name="ADDtype">
                </form>
            </tr>
            <tr>
                <th colspan="3" align="left"><i><strong>Types are OPC Action Codes. Valid Types are:</strong>
                <ul>
                    <li><strong>2</strong>-Email</li>
                    <li><strong>3</strong>-Page Only</li>
                    <li><strong>4</strong>-SC Ticket</li>
                    <li><strong>5</strong>-SC Ticket & Page</li>
                </ul>
                <strong>or any combination thereof</strong></i></th>
            </tr>
        </TABLE>
        </DIV>
        </TD>
        <TD valign="top">
		<DIV style="display:inline" id="KEYS_DIV">
        <TABLE align="center" width="95%">
            <tr>
                <th>
                <h2>Comp ID</h2>
                </th>
                <th>
                <h2>Error</h2>
                </th>
                <th>
                <h2>Event</h2>
                </th>
            </tr>
            <form method="POST" action="AdminController">
            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
            <%for (int i = 0; i < vKeys.size(); i ++){
            if (i%2 == 0){
					out.print("<tr><td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getCompId() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getErrCode() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getEvent() + "&nbsp;</td>");
			} else {
					out.print("<tr bgcolor=\"AAAAFF\"><td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getCompId() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getErrCode() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyBean)vKeys.get(i)).getEvent() + "&nbsp;</td>");
			}%>
            <td><input type="checkbox" value="<%=((TroubleTicketKeyBean)vKeys.get(i)).getID()%>" name="keys<%=((TroubleTicketKeyBean)vKeys.get(i)).getID()%>" UNCHECKED></td>
            </tr>
            <%}%>
            <tr>
                <td colspan="4" align="center"><input type="submit" value="Delete Checked" name="DELETEkeys"></td>
            </tr>
            </form>
            <tr>
	            <form method="POST" action="AdminController">
	            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
                <td><input name="keyComp" type="text" value=""></td>
                <td><input name="keyErrCd" type="text" value=""></td>
                <td><input name="keyEvnt" type="text" value=""></td>
                <td><input type="submit" value="add"></td>
                <input type="hidden" value="ADDkey" name="ADDkey">
                </form>
            </tr>
            <tr>
                <th align="left" colspan="4"><i><strong>Keys are:</strong>
                <ul>
                    <li>Component IDs</li>
                    <li>Error Codes</li>
                    <li>Event Codes</li>
                </ul>
                <strong>To be monitored by the Agent</strong></i></th>
            </tr>
        </TABLE>
        </DIV>
        </TD>
        <TD valign="top">
		<DIV style="display:inline" id="KEY_GROUP_DIV">
        <TABLE align="center" width="95%">
            <tr>
                <th>
                <h2>Key</h2>
                </th>
                <th>
                <h2>Group</h2>
                </th>
                <th>
                <h2>SVC</h2>
                </th>
            </tr>
            <form method="POST" action="AdminController">
            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
            <%for (int i = 0; i < vKeyGroup.size(); i ++){
            if (i%2 == 0){
					out.print("<tr><td>" 
					+ ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getCompId() 
					+ "&nbsp;" 
					+ ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getErrCd() 
					+ "&nbsp;"
					+((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getEvnt() 
					+ "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getDescription() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getService() + "&nbsp;</td>");
			} else {
					out.print("<tr bgcolor=\"AAAAFF\"><td>" 
					+ ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getCompId() 
					+ "&nbsp;" 
					+ ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getErrCd() 
					+ "&nbsp;"
					+((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getEvnt() 
					+ "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getDescription() + "&nbsp;</td>");
					out.print("<td>" + ((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getService() + "&nbsp;</td>");
			}%>
            <td><input type="checkbox" value="<%=((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getId()%>" name="keygroup<%=((TroubleTicketKeyGroupXRefBean)vKeyGroup.get(i)).getId()%>" UNCHECKED></td>
            </tr>
            <%}%>
            <tr>
                <td colspan="4" align="center"><input type="submit" value="Delete Checked" name="DELETEkeygroup"></td>
            </tr>
            </form>
            <%
    //if all the dropdowns can be populated from the database do this...
    if (vGb.size() > 0  && vKb.size() > 0 ){
    if (vKeyGroup.size() % 2 == 0) {%>
            <tr>
                <% } else {%>
            <tr bgcolor="AAAAFF">
                <%}%>
	            <form method="POST" action="AdminController">
	            <input type="hidden" name="CMD" value="TROUBLE_TICKET">
                <td><SELECT name="key">
                    <%for (int i = 0; i < vKb.size(); i++){%>
                    <option value="<%=((TroubleTicketKeyBean)vKb.get(i)).getID()%>"><%=((TroubleTicketKeyBean)vKb.get(i)).getCompId()%> <%=((TroubleTicketKeyBean)vKb.get(i)).getErrCode()%> <%=((TroubleTicketKeyBean)vKb.get(i)).getEvent()%></option>
                    <%}%>
                </SELECT></td>
                <td><SELECT name="group">
                    <%for (int i = 0; i < vGb.size(); i++){%>
                    <option value="<%=((TroubleTicketGroupBean)vGb.get(i)).getID()%>"><%=((TroubleTicketGroupBean)vGb.get(i)).getDescription()%></option>
                    <%}%>
                </SELECT></td>
                <td><input name="svc" type="text" value=""></td>
                <td><input type="submit" value="add"></td>
                <input type="hidden" value="ADDkeygroup" name="ADDkeygroup">
                </form>
            </tr>
            <tr>
            <%}%>
                <th align="left" colspan="4"><i><strong>Associating keys and groups allows you to select whom will be notified of a specific condition</strong></i></th>
            </tr>
        </TABLE>
        </DIV>
        </TD>
    </TR>
</TABLE>
</BODY>
</HTML>
