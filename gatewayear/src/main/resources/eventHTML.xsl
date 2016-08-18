<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" indent="yes" omit-xml-declaration="yes"/>
	<xsl:template match="/">
            <P>
			<H4>Search Results:</H4>
			<B>Matching Logs found: </B><xsl:value-of select="Results/@matches"/><BR/>
			<B>Search Time: </B><xsl:value-of select="Results/@time"/> milliseconds<BR/>
            </P>
			<table align="center" border="1">
				<xsl:apply-templates/>
			</table>
	</xsl:template>
	
	<xsl:template match="Results">
		<xsl:for-each select="Event">
			<xsl:sort select="@creationDate"/>
            <tr bgcolor="#ffffff">
              <td width="600">
              <font color="blue"><b>TYPE:</b></font>
                   <font color="white">.....</font><xsl:value-of select="@type"/><BR/>

              <xsl:if test="@code">
                  <font color="red"><B>ERROR CODE:</B></font>
                       <font color="white">.....</font><xsl:value-of select="@code"/><BR/>
              </xsl:if>

              <font color="blue"><B>WHEN:</B></font>
                   <font color="white">.....</font><xsl:value-of select="@creationDate"/><BR/>

              <xsl:if test="MessageInfo/@messageId">
                  <font color="blue"><B>TRANSACTION_INFO:</B></font>
                       <font color="white">.....</font> <xsl:value-of select="MessageInfo/@messageId"/><BR/>
              </xsl:if>

              <font color="blue"><B>MESSAGE:</B></font>
                   <font color="white">.....</font> <xsl:value-of select="Message"/><BR/>

              <font color="blue"><B>TRACE INFO:</B></font>
                   <font color="white">.....</font>componentId=<xsl:value-of select="TraceInfo/@componentId"/>
                   bpId=<xsl:value-of select="TraceInfo/@bpId"/>
                   bpStep=<xsl:value-of select="TraceInfo/@bpStep"/>
                   node=<xsl:value-of select="TraceInfo/@nodeName"/>
                   appName=<xsl:value-of select="TraceInfo/@appName"/><BR/>

			  <xsl:if test="StackTrace">
		          <font color="red"><b>STACK TRACE: </b><xsl:value-of select="StackTrace"/></font>
			  </xsl:if>
              <BR/>
              </td>
            </tr>

		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
