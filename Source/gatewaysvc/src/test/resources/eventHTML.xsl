<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" indent="yes" omit-xml-declaration="yes" />
	<xsl:template match="/">
		<H4>Search Results:</H4>
		<B>Matching Logs found: </B>
		<xsl:value-of select="Results/@matches" />
		<BR />
		<B>Search Time: </B>
		<xsl:value-of select="Results/@time" />
		milliseconds
		<BR />
		<table width="20%" align="center" border="1">
			<xsl:apply-templates />
		</table>
	</xsl:template>

	<xsl:template match="Results">
		<xsl:for-each select="Event">
			<xsl:sort select="@creationDate" />
			<xsl:if test="((position()-1) mod 10) = 0">
				<tr>
					<th bgcolor="#cccccc">
						<b>Creation Date</b>
					</th>
					<th bgcolor="#cccccc">
						<b>
							Event
							<br />
							Type
						</b>
					</th>
					<th bgcolor="#cccccc">
						<b>
							Error
							<br />
							Code
						</b>
					</th>
					<th bgcolor="#cccccc">
						<b>
							Log
							<br />
							Level
						</b>
					</th>
				</tr>
			</xsl:if>

			<tr bgcolor="#99ccff">
				<td nowrap="">
					<xsl:value-of select="@creationDate" />
				</td>
				<td>
					<xsl:value-of select="@type" />
				</td>
				<td>
					<xsl:value-of select="@code" />
				</td>
				<td>
					<xsl:value-of select="@logLevel" />
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="1">
					<b>Caller: </b>
					<xsl:value-of select="@where" />
				</td>
				<td colspan="1">
					<b>Node: </b>
					<xsl:value-of select="TraceInfo/@nodeName" />
				</td>
				<td colspan="1">
					<b>App Server: </b>
					<xsl:value-of select="TraceInfo/@appName" />
				</td>
			</tr>
			<tr bgcolor="#ffffff">
				<td colspan="3">
					<b>Message: </b>
					<xsl:value-of select="Message" />
				</td>
			</tr>
			<xsl:if test="StackTrace">
				<tr bgcolor="#ffffff">
					<td colspan="3">
						<font color="red">
							<b>Stack Trace: </b>
							<xsl:value-of select="StackTrace" />
						</font>
					</td>
				</tr>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
