<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META http-equiv="Content-Style-Type" content="text/css">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Socket Connection Pools</TITLE>

	<SCRIPT language="JavaScript">
		function lookupPool(poolName)
		{
			document.SocketForm.POOL.value = poolName;
			document.SocketForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>
	<CENTER><H2>Socket Connection Pools</H2></CENTER>

	<FORM name="SocketForm" method="post" target="SocketConPoolInfo">
	<INPUT name="ACTION" type="hidden" value="REFRESH"/>
	<INPUT name="POOL" type="hidden" value="HUH?"/>
	<INPUT name="CMD" type="hidden" value="SOCKET_CON_POOL"/>
	<TABLE width="100%" height="90%" border="0">
	<TR>
		<TD width="25%" valign="top">
			<UL>
<%			
			com.disney.connection.socket.SocketConnectionManagerFactory.refreshDictionary();
			java.util.Iterator names = com.disney.connection.socket.SocketConnectionManagerFactory.getManagers().iterator();
			while (names.hasNext())
			{
				String name = (String)names.next();
%>
				<LI><A href="javascript:lookupPool('<%=name%>')"><%=name%></A></LI>
<%
			}
%>
			</UL>
		</TD>
		<TD width="75%">
			<IFRAME name="SocketConPoolInfo" width="100%" height="100%">
			</IFRAME>
		</TD>
	</TR>
	</TABLE>
	
	</FORM>
	
</BODY>
</HTML>
