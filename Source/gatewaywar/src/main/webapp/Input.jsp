<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN">
<HTML>
<HEAD>
<%@ page import="com.disney.util.AbstractInitializer"%>
<%@ page import="com.disney.util.PropertyHelper"%>
<%@ page import="com.dti.app.util.PropertyName"%>
<%@ page import="java.util.Properties"%>
<%@ page import="pvt.disney.dti.gateway.util.ResourceLoader" %>
<%@ page import="java.util.ResourceBundle" %>

<%!public String getProperty(String key) {
	
  	ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

    String value = PropertyHelper.readPropsValue(key, props, null);
    
	  return value;
}
%>
<TITLE>DTICLAW - Production Https Adapter Test App</TITLE>
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<META name="GENERATOR" content="IBM WebSphere Studio">
</HEAD>
<BODY bgcolor="#b9b9ff">
	<H2>
		Https Adapter Test App<BR>
		<%=getProperty(PropertyName.BUILD_MODULE)%>-
		<%=getProperty(PropertyName.ERROR_POS_TARGET)%></H2>
	<FORM action="servlet/com.dti.app.common.POSApp" method="POST"
		target="bodyFrame">
		<TABLE>

			<TR>
				<TD colspan="3"><font color="red"><B>Please Enter a
							folder name as c:\\temp\\ for Windows /tmp/ for Solaris<BR>
							Please Enter a WebApp Path as http://server
							name/DTIApp/com.dti.app.common.DTIApp
					</B></font></TD>
			</TR>

			<TR>
				<TD><B>Folder Name: </B></TD>
				<TD><input type="text" name="folder" size="50"
					value="/var/opt/apps/RT/schemas/xml/"></TD>
				<TD></TD>
			</TR>
			<TR>
				<TD><B>URL: </B></TD>
				<TD><input type="text" name="host" size="50"
					value="https://<%=getProperty(PropertyName.DTI_DEPLOY_HOST)%>/DTIApp/servlet/com.dti.app.common.DTIApp">
				</TD>
				<TD></TD>
			</TR>
			<TR>
				<TD colspan="3"><input type="radio" name="file"
					value="UpgradeAlphaRequest.xml">UpgradeAlphaRequest.xml<BR>
					<input type="radio" name="file" value="VoidTicketRequest.xml">VoidTicketRequest.xml<BR>
					<input type="radio" name="file" value="QueryTicketRequest.xml">QueryTicketRequest.xml<BR>
					<input type="radio" name="file" value="ReservationRequest.xml">ReservationRequest.xml
				</TD>
			</TR>
			<TR>
				<TD><input type="submit"></TD>
				<TD>
					<!--a href="xml.html" target="headerFrame">XML Retriever</a-->
				</TD>
			</TR>
		</TABLE>
	</FORM>
</BODY>
</HTML>


