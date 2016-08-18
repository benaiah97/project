<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="com.disney.util.*" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Jdbc Main</TITLE>
<!--jsp:useBean id="DataSourceNames" class="java.util.Enumeration" scope="request"/-->
<jsp:useBean id="TableNameList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="ViewNameList" class="java.util.ArrayList" scope="request"/>
<jsp:useBean id="CurrentDataSourceName" class="java.lang.String" scope="request"/>
<jsp:useBean id="CurrentTableName" class="java.lang.String" scope="request"/>
<jsp:useBean id="ThrowableObject" class="java.lang.Throwable" scope="request"/>
<jsp:useBean id="TableResultBean" class="com.disney.admin.jdbc.ResultBean" scope="request"/>
<jsp:useBean id="QueryResultBean" class="com.disney.admin.jdbc.ResultBean" scope="request"/>
<jsp:useBean id="QueryString" class="java.lang.String" scope="request"/>
<jsp:useBean id="QueryId" class="java.lang.String" scope="request"/>
<jsp:useBean id="showViews" class="java.lang.String" scope="request"/>
<jsp:useBean id="showSystem" class="java.lang.String" scope="request"/>

	<SCRIPT language="JavaScript">
		function onLoad(){
			<%	// Set focus only if a DataSource	
			if (CurrentDataSourceName.length() > 0)
			{
			%>
				document.JdbcDSForm.QUERY_STRING.focus();
			<% } %>
		}

		function openDataSource(dsName)
		{
			document.JdbcDSForm.DATA_SOURCE_NAME.value = dsName;
			document.JdbcDSForm.ACTION.value = "OPEN";
			document.JdbcDSForm.submit();
		}
		
		function exploreTable(tName)
		{
			document.JdbcDSForm.TABLE_NAME.value = tName;
			document.JdbcDSForm.ACTION.value = "EXPLORE_TABLE";
			document.JdbcDSForm.submit();
		}
		
		function sendQuerySessions()
		{
			// Requires grant on V$_SESSION by DBA group. Show current connection/lock information.
			var queryString="select machine, username, logon_time, last_call_et, row_wait_obj#, row_wait_row#, sid, serial#, audsid, lockwait, status, osuser, terminal, program from v$session where machine not in ('wdwsun18', 'wdwsun76') order by machine, username";
			//alert(queryString);
			document.JdbcDSForm.QUERY_STRING.value = queryString;
			document.JdbcDSForm.ACTION.value='QUERY';
		}
	</SCRIPT>

</HEAD>
<BODY onload="onLoad()">

	<CENTER><H2>JDBC Data Sources</H2></CENTER>

	<FORM name="JdbcDSForm" method="post">
	<INPUT TYPE="hidden" NAME="DATA_SOURCE_NAME" value="<%=CurrentDataSourceName%>"/>
	<INPUT TYPE="hidden" NAME="TABLE_NAME" value="<%=CurrentTableName%>"/>
	<INPUT TYPE="hidden" NAME="CMD" value="JDBC"/>
	
	<H4>Configured Data Sources</H4>
		<UL>
	<%	// List all found Data Sources
	java.util.Enumeration dataSourceNames = (java.util.Enumeration)request.getAttribute("DataSourceNames");
	try
	{
		String actionStr = "REFRESH";
		if (dataSourceNames != null)
		{
			while (dataSourceNames.hasMoreElements())
			{
				String dsName = (String) dataSourceNames.nextElement();
		%>
				<LI><A href="javascript:openDataSource('<%=dsName%>')"><%=dsName%></A></LI>
		<%		
			}
		}
	%>
		</UL>
	<!-- showViews=<%=showViews %> -->
	<!-- showSystem=<%=showSystem %> -->
	<INPUT type="checkbox" name="SHOW_VIEWS" <% out.print((showViews!=null&&showViews.length()>0)?"checked=\""+showViews+"\"":""); %>/>Show Views
	<INPUT type="checkbox" name="SHOW_SYSTEM" <% out.print((showSystem!=null&&showSystem.length()>0)?"checked=\""+showSystem+"\"":""); %>/>Show System Tables
	
	<%	// Display Tables in DB	
		if (CurrentDataSourceName.length() > 0)
		{
			actionStr = "QUERY";
	%>
	<H4>Tables Found for: <%=CurrentDataSourceName%></H4>
	<TABLE><TR><TD>
	<%
			
			java.util.Iterator i = TableNameList.iterator();
			while (i.hasNext())
			{
				String tableName = (String) i.next();
	%>
			<A href="javascript:exploreTable('<%=tableName%>')"><%=tableName%></A> , 
	<%		
			}
	%>		
	</TD></TR></TABLE>
	<%	// Display Views in DB	
		if (ViewNameList != null && ViewNameList.size() > 0)
		{
	%>
	<BR/>
	<H4>Views Found for: <%=CurrentDataSourceName%></H4>
	<TABLE><TR><TD>
	<%
			
			i = ViewNameList.iterator();
			while (i.hasNext())
			{
				String viewName = (String) i.next();
	%>
			<A href="javascript:exploreTable('<%=viewName%>')"><%=viewName%></A> , 
	<%		
			}
		}
	%>		
	</TD></TR></TABLE>
	
	
	<%		// Display Column List Result
			java.util.List tableColumnNameList = TableResultBean.getColumnNameList();
			java.util.List tableRowList = TableResultBean.getRowList();
			if ( (tableColumnNameList != null) && (tableRowList != null) )
			{
	%>
		<BR/><BR/>
		
		<H4>Column List for: <%=CurrentTableName %></H4>
		<TABLE border="1" title="Query Result:">
			<TR>
				<TH>&nbsp;</TH>
	<%
				java.util.Iterator cols = tableColumnNameList.iterator();
				while (cols.hasNext())
				{
	%>
				<TH><%=(String)cols.next()%></TH>
	<%
				} 
	%>
			</TR>
	<%
				int count = 0;
				java.util.Iterator rows = tableRowList.iterator();
				while (rows.hasNext())
				{
	%>
			<TR>
				<TD><%=count++%></TD>
	<%
					java.util.List thisRow = (java.util.List) rows.next();
					if (thisRow != null)
					{
						java.util.Iterator row = thisRow.iterator();
						while (row.hasNext())
						{
	%>
				<TD><%=(String)row.next()%></TD>
	<%
						}
	%>
			</TR>
	<%
					}
				} 
	%>
		</TABLE>
	<%
			}
			// Don't display 'null' when no returned query string
			if (QueryString==null) {
				QueryString="";
			}
	%>
	<BR/><BR/>
		<H4>Query String:</H4>
		<INPUT name="QUERY_STRING" type="text" size="150" maxlength="5000" value="<%=QueryString%>"/><BR/>
		<INPUT type="submit" name="SEND_QUERY" value="Send Query" onclick="document.JdbcDSForm.ACTION.value='QUERY'"/>
		<INPUT type="submit" name="SEND_QUERY1" value="Sessions?" onclick="sendQuerySessions()"/>
		<!-- Sample query strings:
			Session info: select machine, username, logon_time, last_call_et, row_wait_obj#, row_wait_row#, sid, serial#, audsid, lockwait, status, osuser, terminal, program from v$session where machine!='wdwsun18' order by machine
			Locks: 
			Blocking / waiting locks: 
		-->
	<%		// Display Query Result
			java.util.List columnNameList = QueryResultBean.getColumnNameList();
			java.util.List rowList = QueryResultBean.getRowList();
			if ( (columnNameList != null) && (rowList != null) )
			{
	%>
		<BR/><BR/>
		
		<H4>
			<A href="AdminController?CMD=JDBC&ACTION=SAVE_AS&QUERY_ID=<%=QueryId%>">
				<IMG src="images/saveAs.gif" border="0"/>
			</A>Query Result: <%=QueryResultBean.getRowCount() %> Records
		</H4>
		<TABLE border="1" title="Query Result:">
			<TR>
				<TH>&nbsp;</TH>
	<%
				java.util.Iterator cols = columnNameList.iterator();
				while (cols.hasNext())
				{
	%>
				<TH><%=(String)cols.next()%></TH>
	<%
				} 
	%>
			</TR>
	<%
				int count = 0;
				java.util.Iterator rows = rowList.iterator();
				while (rows.hasNext())
				{
	%>
			<TR>
				<TD><%=count++%></TD>
	<%
					java.util.List thisRow = (java.util.List) rows.next();
					if (thisRow != null)
					{
						java.util.Iterator row = thisRow.iterator();
						while (row.hasNext())
						{
	%>
				<TD><%=(String)row.next()%></TD>
	<%
						}
	%>
			</TR>
	<%
					}
				} 
	%>
		</TABLE>
	<%
			}
		}
	%>
	<INPUT TYPE="hidden" NAME="ACTION" value="<%=actionStr%>"/>

	</FORM>

	<%
		String throwString = ThrowableObject.toString();
		String throwTrace = PrintStack.getTraceString(ThrowableObject);
		if (! ((throwString.indexOf("java.lang.Throwable") >= 0) && (throwString.length() < 25)) )
		{
	%>
	<H4>Exception Occurred during last operation:</H4>
	<FONT color="red">
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
	<H4>Exception Occurred during display:</H4>
	<FONT color="red">
		<B>Error: <%=throwString%></B><BR/>
		<I>Stack Trace: <%=throwTrace%></I><BR/>
	</FONT>
	<BR/>
	<%
	}
	%>
	
</BODY>
</HTML>
