<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*,com.disney.util.*,com.disney.xml.*,com.disney.admin.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Jdbc Log Viewer</TITLE>
<!--jsp:useBean id="dataSourceNames" class="java.util.Enumeration" scope="request"/-->
<jsp:useBean id="currentDataSourceName" class="java.lang.String" scope="request"/>
<jsp:useBean id="jdbcBean" class="com.disney.admin.logging.JdbcLogBean" scope="request"/>
<jsp:useBean id="throwable" class="java.lang.Throwable" scope="request"/>
<jsp:useBean id="eventList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="resultBean" class="com.disney.admin.jdbc.ResultBean" scope="request"/>
<jsp:useBean id="queryId" class="java.lang.String" scope="request"/>
 
	<SCRIPT language="JavaScript">
		function openDataSource(dsName)
		{
			document.LogForm.DATA_SOURCE_NAME.value = dsName;
			document.LogForm.ACTION.value = "OPEN";
			document.LogForm.submit();
		}
		function refresh()
		{
			document.LogForm.ACTION.value = "REFRESH";
			document.LogForm.submit();
		}
		function onLoad(){
			<%	// Set focus only if a DataSource	
			if (currentDataSourceName.length() > 0)
			{
			%>
				document.LogForm.AFTER.focus();
			<% } %>
		}
		
		function changeDropDown(selectBox, otherBox)
		{
			if (selectBox.value == 'OTHER')
			{
				otherBox.style.visibility = 'visible';
			}
			else
			{
				otherBox.style.visibility = 'hidden';
			}
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
<BODY onload="onLoad()">

	<CENTER><H2>Jdbc Log Viewer</H2></CENTER>

	<FORM name="LogForm" method="post">
	<INPUT TYPE="hidden" NAME="DATA_SOURCE_NAME" value="<%=currentDataSourceName%>"/>
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	<INPUT TYPE="hidden" NAME="CMD" value="JDBC_LOG"/>
	
	<H4>Configured Data Sources</H4>
		<UL>
	<%	// List all found Data Sources
	java.util.Enumeration dataSourceNames = (java.util.Enumeration)request.getAttribute("dataSourceNames");
	try
	{
		while (dataSourceNames.hasMoreElements())
		{
			String dsName = (String) dataSourceNames.nextElement();
	%>
			<LI><A href="javascript:openDataSource('<%=dsName%>')"><%=dsName%></A></LI>
	<%		
		}
	%>
		</UL>
	<BR/>

	<%	// Display Tables in DB	
		if (currentDataSourceName.length() > 0)
		{
		
			String afterStr = "";
			if (jdbcBean.getAfter() != null)
			{
				afterStr = Converter.printISODate(jdbcBean.getAfter());
			} else {
				afterStr = Converter.printISODate(new java.util.Date(System.currentTimeMillis() - 120000));
			}

			String beforeStr = "";
			if (jdbcBean.getBefore() != null)
			{
				beforeStr = Converter.printISODate(jdbcBean.getBefore());
			}
			
			List list = null;
			java.util.Iterator i = null;
	%>
	<H4>Connected to EVENT Table in: <%=currentDataSourceName%></H4>
		<B>Total number of Events Found in last <%=jdbcBean.getQueryTime()%> minutes:</B> <%=jdbcBean.getNumRows()%><BR/>
		<BR/>

		<TABLE width="80%" align="center" border="1">
			<TR>
				<TD>
					<H5>
						<A HREF="javascript:refresh()">
							<IMG src="images/refresh.gif" height="15" border="0"/>
						</A> 
						Search for EVENTS
					</H5>
					
					<TABLE border="0" width="80%">
						<TR>
							<TD>
								<B>WebSphere Node:</B>
							</TD>
							<TD>
								<SELECT name="WAS_NODE" onchange="javascript:changeDropDown(document.LogForm.WAS_NODE, document.LogForm.WAS_NODE_OTHER)">
									<OPTION value="">ALL</OPTION>
	<%
			String otherVisibility = "hidden";
			list = jdbcBean.getWasNodeList();
			if (list != null) 
			{
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getWasNode())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getWasNode()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getWasNode() == null || jdbcBean.getWasNode().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="WAS_NODE_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getWasNode()%>"/>
								<BR/>
							</TD>
						</TR>
						
						<TR>
							<TD>
								<B>WebSphere App Server:</B>
							</TD>
							<TD>
								<SELECT name="WAS_APP" onchange="javascript:changeDropDown(document.LogForm.WAS_APP, document.LogForm.WAS_APP_OTHER)">
									<OPTION value="">ALL</OPTION>
	<%
			list = jdbcBean.getWasAppList();
			if (list != null) 
			{
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getWasApp())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getWasApp()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getWasApp() == null || jdbcBean.getWasApp().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="WAS_APP_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getWasApp()%>"/>
								<BR/>
							</TD>
						</TR>
						
						<TR>
							<TD>
								<B>Event Type:</B>
							</TD>
							<TD>
								<SELECT name="EVENT_TYPE" onchange="javascript:changeDropDown(document.LogForm.EVENT_TYPE, document.LogForm.EVENT_TYPE_OTHER)">
									<OPTION value="">Choose Event Type..</OPTION>
	<%
			list = jdbcBean.getEventTypeList();
			if (list != null) {
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getEventType())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getEventType()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getEventType() == null || jdbcBean.getEventType().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="EVENT_TYPE_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getEventType()%>"/>
								<BR/>
							</TD>
						</TR>
						
						<TR>
							<TD>
								<B>Error Code:</B>
							</TD>
							<TD>
								<SELECT name="ERROR_CODE" onchange="javascript:changeDropDown(document.LogForm.ERROR_CODE, document.LogForm.ERROR_CODE_OTHER)">
									<OPTION value="">Choose Error Code..</OPTION>
	<%
			list = jdbcBean.getErrorCodeList();
			if (list != null) 
			{
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getErrorCode())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getErrorCode()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getErrorCode() == null || jdbcBean.getErrorCode().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="ERROR_CODE_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getErrorCode()%>"/>
								<BR/>
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Component Id:</B>
							</TD>
							<TD>
								<SELECT name="COMPONENT_ID" onchange="javascript:changeDropDown(document.LogForm.COMPONENT_ID, document.LogForm.COMPONENT_ID_OTHER)">
									<OPTION value="">Choose Component Id..</OPTION>
	<%
			list = jdbcBean.getComponentList();
			if (list != null) {
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getComponentId())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getComponentId()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getComponentId() == null || jdbcBean.getComponentId().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="COMPONENT_ID_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getComponentId()%>"/>
								<BR/>
							</TD>
						</TR>

						<TR>
							<TD>	
								<B>Business Process Id:</B>
							</TD>
							<TD>
								<SELECT name="BP_ID" onchange="javascript:changeDropDown(document.LogForm.BP_ID, document.LogForm.BP_ID_OTHER)">
									<OPTION value="">Choose BP Id..</OPTION>
	<%
			list = jdbcBean.getBpIdList();
			if (list != null) {
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getBpId())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getBpId()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getBpId() == null || jdbcBean.getBpId().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="BP_ID_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getBpId()%>"/>
								<BR/>
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Business Step:</B>
							</TD>
							<TD>
								<SELECT name="BP_STEP" onchange="javascript:changeDropDown(document.LogForm.BP_STEP, document.LogForm.BP_STEP_OTHER)">
									<OPTION value="">Choose BP Step..</OPTION>
	<%
			list = jdbcBean.getBpStepList();
			if (list != null) {
				i = list.iterator();
				while (i.hasNext())
				{
					String nextVal = (String)i.next();
	%>
									<OPTION value="<%=nextVal%>" <%=(nextVal.equalsIgnoreCase(jdbcBean.getBpStep())?"selected":"")%>><%=nextVal%></OPTION>
	<%
				}
				otherVisibility = (list.contains(jdbcBean.getBpStep()))?"hidden":"visible";
				otherVisibility = (jdbcBean.getBpStep() == null || jdbcBean.getBpStep().trim().length() == 0)?"hidden":otherVisibility;
			}
	%>
									<OPTION value="OTHER" <%=(otherVisibility.equals("visible")?"selected":"")%>>Other...</OPTION>
								</SELECT>
								<INPUT name="BP_STEP_OTHER" type="text" style="visibility=<%=otherVisibility%>" value="<%=jdbcBean.getBpStep()%>"/>
								<BR/>
							</TD>
						</TR>
						
						<TR>
							<TD>
								<B>Session Id:</B><BR>
								<I>(or ConversationId)</I>
							</TD>
							<TD>
								<INPUT name="SESSION_ID" value="<%=jdbcBean.getSessionId()%>" type="text" size="30"/>
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Message Id:</B>
							</TD>
							<TD>
								<INPUT name="MESSAGE_ID" value="<%=jdbcBean.getMessageId()%>" type="text" size="30"/>
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Created After:</B>
							</TD>
							<TD>
								<INPUT name="AFTER" value="<%=afterStr%>" type="text" size="30"/><BR/>
								In the Form: <font color="blue"><%=Converter.printISODateMS(new java.util.Date())%></font>
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Created Before:</B>
							</TD>
							<TD>
								<INPUT name="BEFORE" value="<%=beforeStr%>" type="text" size="30"/><BR/>
								In the Form: <font color="blue"><%=Converter.printISODateMS(new java.util.Date())%></font>
							</TD>
						</TR>

						<TR>
							<TD>
								<INPUT type="submit" name="SUBMIT_QUERY" onclick="document.LogForm.ACTION.value='SEARCH'"/>
							</TD>
							<TD>
							</TD>
						</TR>
					</TABLE>
					
				</TD>
			</TR>
		</TABLE>
	<%
		}
	%>
	<!-- <% out.println("System.currentTimeMillis=" + System.currentTimeMillis()); %> -->

	</FORM>

	<!-- ********************  RESULTS SECTION  *************************  -->

	<!-- USING RESULTS BEAN --->

	<%
		java.util.List columnNameList = resultBean.getColumnNameList();
		java.util.List rowList = resultBean.getRowList();
		if ( (columnNameList != null) && (rowList != null) )
		{
	%>
	
	<H4>
		<A href="AdminController?CMD=JDBC&ACTION=SAVE_AS&QUERY_ID=<%=queryId%>"><IMG src="images/saveAs.gif" border="0"/></A>Search Results:
	</H4>
	<TABLE width="90%" align="center" border="1">
		<TR>
			<TH bgcolor="#cccccc"><B>Creation Date</B></TH>
			<TH bgcolor="#cccccc"><B>Event<BR/>Type</B></TH>
			<TH bgcolor="#cccccc"><B>Error<BR/>Code</B></TH>
			<TH bgcolor="#cccccc"><B>Component<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>BP<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>BP<BR/>Step</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Name</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Type</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Action</B></TH>
			<TH bgcolor="#cccccc"><B>Conversation<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>Message<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>Message Timestamp</B></TH>
			<TH bgcolor="#cccccc"><B>From</B></TH>
			<TH bgcolor="#cccccc"><B>Log<BR/>Level</B></TH>
		</TR>
	<%
			int convIdCol = columnNameList.indexOf("CONVERSATION_ID");
			int messageIdCol = columnNameList.indexOf("MESSAGE_ID");
			int logLevelCol = columnNameList.indexOf("LOG_LEVEL");
			int eventTypeCol = columnNameList.indexOf("TYPE");
			int errorCodeCol = columnNameList.indexOf("CODE");
			int creationTimeCol = columnNameList.indexOf("CREATION_TIME");
			int msgTimeCol = columnNameList.indexOf("MSG_TIMESTAMP");
			int compIdCol = columnNameList.indexOf("COMPONENT_ID");
			int bpIdCol = columnNameList.indexOf("BP_ID");
			int bpStepCol = columnNameList.indexOf("BP_STEP");
			int svcNameCol = columnNameList.indexOf("SERVICE_NAME");
			int svcTypeCol = columnNameList.indexOf("SERVICE_TYPE");
			int svcActionCol = columnNameList.indexOf("SERVICE_ACTION");
			int fromCol = columnNameList.indexOf("FROM_PARTY");
			int nodeCol = columnNameList.indexOf("WAS_NODE");
			int appCol = columnNameList.indexOf("WAS_APP");
			
			int whereCol = columnNameList.indexOf("CALLER");
			int messageCol = columnNameList.indexOf("MESSAGE");
			int stackTraceCol = columnNameList.indexOf("STACK_TRACE");

			java.util.Iterator rows = rowList.iterator();
			boolean green = true;
			String colorStr = "";
			String redStr = "";
			int eventId = 0;
			while (rows.hasNext())
			{
				eventId++;
				java.util.List thisRow = (java.util.List) rows.next();
				colorStr = "#99ccff";
				
				Date d = new Date(Long.parseLong((String)thisRow.get(creationTimeCol)));
				
				if (eventId % 20 == 0)
				{
					// Print the column headers every 20 records (easier to read)
	%>
		<TR>
			<TH bgcolor="#cccccc"><B>Creation Date</B></TH>
			<TH bgcolor="#cccccc"><B>Event<BR/>Type</B></TH>
			<TH bgcolor="#cccccc"><B>Error<BR/>Code</B></TH>
			<TH bgcolor="#cccccc"><B>Component<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>BP<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>BP<BR/>Step</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Name</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Type</B></TH>
			<TH bgcolor="#cccccc"><B>Service<BR/>Action</B></TH>
			<TH bgcolor="#cccccc"><B>Conversation<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>Message<BR/>Id</B></TH>
			<TH bgcolor="#cccccc"><B>Message Timestamp</B></TH>
			<TH bgcolor="#cccccc"><B>From</B></TH>
			<TH bgcolor="#cccccc"><B>Log<BR/>Level</B></TH>
		</TR>
	<%
				}
	%>
				<!-- <% out.println("Date in millis=" + (String)thisRow.get(creationTimeCol)); %> -->
		<TR>
			<TD bgcolor="<%=colorStr%>" nowrap><%=Converter.printISOTimeMSOnly(d)%></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)thisRow.get(eventTypeCol) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)(thisRow.get(errorCodeCol)==null || thisRow.get(errorCodeCol).equals("null") ? "" : thisRow.get(errorCodeCol)) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)thisRow.get(compIdCol) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)thisRow.get(bpIdCol) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)thisRow.get(bpStepCol) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)(thisRow.get(svcNameCol)==null || thisRow.get(svcNameCol).equals("null") ? "" : thisRow.get(svcNameCol)) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)(thisRow.get(svcTypeCol)==null || thisRow.get(svcTypeCol).equals("null") ? "" : thisRow.get(svcTypeCol)) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)(thisRow.get(svcActionCol)==null || thisRow.get(svcActionCol).equals("null") ? "" : thisRow.get(svcActionCol)) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)(thisRow.get(convIdCol)==null || thisRow.get(convIdCol).equals("null") ? "" : thisRow.get(convIdCol)) %></TD>
			<TD bgcolor="<%=colorStr%>" nowrap><%=(String)(thisRow.get(messageIdCol)==null || thisRow.get(messageIdCol).equals("null") ? "" : thisRow.get(messageIdCol)) %></TD>
                         <% String msgTime = (String)(thisRow.get(msgTimeCol));
                            if (msgTime==null || msgTime.equals("null")) msgTime="";
                         %>
			<TD bgcolor="<%=colorStr%>" nowrap><%=msgTime %></TD>
			<TD bgcolor="<%=colorStr%>" nowrap><%=(String)(thisRow.get(fromCol)==null || thisRow.get(fromCol).equals("null") ? "" : thisRow.get(fromCol)) %></TD>
			<TD bgcolor="<%=colorStr%>"><%=(String)thisRow.get(logLevelCol) %></TD>
		</TR>
		<% 					colorStr = "#ffffff"; %>
		<TR>
			<TD bgcolor="<%=colorStr%>" colspan="9"><B>Caller: </B><%=(String)thisRow.get(whereCol)%></TD>
			<TD bgcolor="<%=colorStr%>" colspan="2" valign="top" nowrap><B>Node: </B><%=(String)thisRow.get(nodeCol)%></TD>
			<TD bgcolor="<%=colorStr%>" colspan="3" valign="top" nowrap><B>App Svr: </B><%=(String)thisRow.get(appCol)%></TD>
		</TR>
		<%		String msgString = (String)thisRow.get(messageCol);
				if (msgString.indexOf("RROR")>-1 || msgString.indexOf("xception")>-1 || msgString.indexOf("XCEPTION")>-1 || msgString.indexOf("rror:")>-1 || msgString.indexOf("not found")>-1) {
					redStr = "<font color='red'>";
				} else {
					redStr = "";
				}

				int pageSize = 10000;
				XmlWriter writer = XmlWriter.getInstance();
				writer.leaf("root", msgString);
				String escaped = writer.toXml(true);
				String[] pages = HtmlConverter.separate(escaped, pageSize); 
%>
		<TR>
			<TD bgcolor="<%=colorStr%>" colspan="14"><B>Message: </B><%=redStr %><%=pages[0]%><BR/>
<%
				for (int pageId = 1; pageId < pages.length; pageId++)
				{
					String divName = "div" + eventId + "-" + pageId;
%>
						<A HREF="javascript:toggle('<%=divName%>')" alt="Page <%=pageId%>"><B>(<%=pageId%>)</B></A>
						<DIV style="display:none" id="<%=divName%>">
						<%=pages[pageId]%>
						</DIV>
<%					
				}
%>
			</TD>
		</TR>
		<%
				String stack = (String)thisRow.get(stackTraceCol);
				if (stack != null && !stack.equals("") && !stack.equals("null"))
				{
		%>
		<TR>
			<TD bgcolor="<%=colorStr%>" colspan="14"><B>Stack Trace: </B><font color="red"><%=stack%></font></TD>
		</TR>
	<%
				}
			}
	%>
	</TABLE>

	<%
		}
	%>

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
