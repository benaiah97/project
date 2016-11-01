<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META http-equiv="Content-Style-Type" content="text/css">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>Jms Connection Pools</TITLE>

	<SCRIPT language="JavaScript">
		function sendAction(aAction)
		{
			document.JmsForm.ACTION.value = aAction;
			document.JmsForm.submit();
		}
	</SCRIPT>

</HEAD>
<BODY>
	<CENTER><H2>Jms Connection Pools</H2></CENTER>

	<FORM name="JmsForm" method="post" target="JmsConPoolInfo">
	<INPUT name="ACTION" type="hidden" value="REFRESH"/>
	<INPUT name="CMD" type="hidden" value="JMS_CON_POOL"/>
	<TABLE width="100%" height="90%" border="0">
	<TR>
		<TD width="25%" valign="top">
			<UL>
				<LI><A href="javascript:sendAction('CON_FACTORIES')">Connection Factory</A></LI>
				<LI><A href="javascript:sendAction('DESTINATIONS')">Destination</A></LI>
				<LI><A href="javascript:sendAction('QUEUE_CONNECTIONS')">Queue Connection</A></LI>
				<LI><A href="javascript:sendAction('BROWSERS')">Jms Browser</A></LI>
				<LI><A href="javascript:sendAction('READERS')">Jms Reader</A></LI>
				<LI><A href="javascript:sendAction('WRITERS')">Jms Writer</A></LI>
			</UL>
		</TD>
		<TD width="75%">
			<IFRAME name="JmsConPoolInfo" width="100%" height="100%">
			</IFRAME>
		</TD>
	</TR>
	</TABLE>
	
	</FORM>
	
</BODY>
</HTML>
