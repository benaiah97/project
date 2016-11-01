<%@ page contentType="text/xml"%>
<%
	String xmlField = request.getParameter("ACTION");
	String xmlData = request.getParameter(xmlField);
%>
<%=xmlData%>
