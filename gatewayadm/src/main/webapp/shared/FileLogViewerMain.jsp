<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="" errorPage="JSPError.jsp" %>
<HTML>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="admin.css" rel="stylesheet" type="text/css">
<TITLE>File Log Viewer</TITLE>
<jsp:useBean id="throwable" class="java.lang.Throwable" scope="request"/>
<jsp:useBean id="regexp" class="java.lang.String" scope="request"/>
<jsp:useBean id="begin" class="java.lang.String" scope="request"/>
<jsp:useBean id="end" class="java.lang.String" scope="request"/>
<jsp:useBean id="results" class="java.lang.String" scope="request"/>
 
	<SCRIPT language="JavaScript">
	</SCRIPT>

</HEAD>
<BODY>

	<CENTER><H2>File Log Viewer</H2></CENTER>

	<FORM name="LogForm" method="post">
	<INPUT TYPE="hidden" NAME="ACTION" value="REFRESH"/>
	<INPUT TYPE="hidden" NAME="CMD" value="FILE_LOG"/>
	
	<%
		com.disney.util.AbstractInitializer init = com.disney.util.AbstractInitializer.getInitializer();
		java.util.Properties props = init.getProps("admin.properties");
		
		String logDirName = (String)request.getAttribute("logDirName");
		if (logDirName == null || logDirName.trim().length() == 0)
		{
			logDirName = props.getProperty("LOG_DIR_NAME", init.getBASE_DIR());
		}
		
	%>
		<TABLE width="90%" align="center" border="1">
			<TR>
				<TD>
					<H5>Search for Log Events:</H5>
					
					<TABLE border="1" width="100%">
						<TR>
							<TD>
								<B>Log File Directory:</B>
							</TD>
							<TD>
								<INPUT name="LOG_DIR_NAME" type="text" value="<%=logDirName%>"/>
							</TD>
							<TD rowspan="5">
								<UL>
									<LI>How do I search for the word 'AUDIT'?
										<UL><LI>Use: 'AUDIT'</LI></UL>
									</LI>
									<LI>What if I'm not sure of the case?
										<UL><LI>Use: '[Aa][Uu][Dd][Ii][Tt]'</LI></UL>
									<LI>What if only the inital case may be different, 'Audit' or 'audit'?
										<UL><LI>Use: 'Audit|audit' or '[Aa]udit'</LI></UL>
									<LI>What if I'm looking for only specific errors by code?
										<UL>
											<LI>Keep in mind that error codes are in the format 'abc####'</LI>
											<LI>For EAI use: 'eai[0-9]{4}'</LI>
										</UL>
									<LI>What if I'm working with a message or conversation ID and I don't have the entire string?
										<UL><LI>You can use any part of the ID, keeping in mind that the more specific you are the less results you'll get.</LI></UL>
								</UL> 
								
								For more information, visit:<BR/>
								<A href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html">
									JDK 1.4 Regular Expression Javadoc
								</A>
							</TD>
						</TR>
						
						<TR>
							<TD valign="top" colspan="2">
								<B>Regular Expression:</B><BR/>
								<TEXTAREA NAME='REGEXP' ROWS="5" COLS="50"><%=regexp%></TEXTAREA>
							</TD>
						</TR>
						
						<TR>
							<TD colspan="2">
								<B>Date Range:</B>
								(In the Form: <font color="blue"><%=com.disney.util.Converter.printISODate(new java.util.Date())%></font>)
							</TD>
						</TR>

						<TR>
							<TD>
								<B>Begin:</B><BR/>
								<INPUT name="BEGIN" type="text" value="<%=begin%>" size="25"/>
							</TD>
							<TD>
								<B>End:</B><BR/>
								<INPUT name="END" type="text" value="<%=end%>" size="25"/>
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

	</FORM>

	<!-- ********************  RESULTS SECTION  *************************  -->

	<%=results%>

	<%
		String throwString = throwable.toString();
		String throwTrace = com.disney.util.PrintStack.getTraceString(throwable);
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
	
</BODY>
</HTML>
