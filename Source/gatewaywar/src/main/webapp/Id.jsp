<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page import="com.disney.util.AbstractInitializer"%>
<%@ page import="com.disney.util.PropertyHelper"%>
<%@ page import="com.dti.app.util.PropertyName"%>
<%@ page import="java.util.Properties"%>
<%@ page import="pvt.disney.dti.gateway.util.ResourceLoader" %>
<%@ page import="java.util.ResourceBundle" %>

<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>ID JSP</TITLE>
<%!public String getProperty(String key) {
	
  	ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

    String value = PropertyHelper.readPropsValue(key, props, null);
    
	  return value;
}
%>
</HEAD>
<BODY>
	<TABLE>
		<TBODY>
			<TR>
				<TD width="75"><B>Module:</B></TD>
				<TD width="175"><%=getProperty(PropertyName.BUILD_MODULE)%></TD>
			</TR>
			<TR>
				<TD width="75"><B>Date:</B></TD>
				<TD width="175"><%=getProperty(PropertyName.BUILD_DATE)%></TD>
			</TR>
			<TR>
				<TD width="75"><B>Revision:</B></TD>
				<TD width="175"><%=getProperty(PropertyName.BUILD_REVISION)%></TD>
			</TR>
		</TBODY>
	</TABLE>
</BODY>
</HTML>
