package pvt.disney.dti.gateway.provider.hkd.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for HkdOTHeaderXML
 */
public class HkdOTHeaderXMLTestCase extends CommonTestUtils {

	/**
	 * JUnit for addHeaderElement
	 */
	@Test
	public void testAddHeaderElement() {
		Document document = DocumentHelper.createDocument();
		Element commandStanza = document.addElement("");
		HkdOTHeaderTO otHdrTO = new HkdOTHeaderTO();
		otHdrTO.setReferenceNumber("123456");
		otHdrTO.setRequestType("123456");
		otHdrTO.setRequestSubType("123");
		otHdrTO.setOperatingArea("123");
		otHdrTO.setUserId(12345);
		otHdrTO.setPassword(123456);
		/* Scenario :: 1 Passing all required parameter */
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 ReferenceNumber as null expected exception is
		 * OTHeaderTO doesn't have a ReferenceNumber. Can't render XML.
		 */
		otHdrTO.setReferenceNumber(null);
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a ReferenceNumber.  Can't render XML.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 RequestType as null expected exception is OTHeaderTO
		 * doesn't have a RequestType. Can't render XML.
		 */
		otHdrTO.setRequestType(null);
		otHdrTO.setReferenceNumber("123456");
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a RequestType.  Can't render XML.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 RequestSubType as null expected exception is OTHeaderTO
		 * doesn't have a RequestSubType. Can't render XML.
		 */
		otHdrTO.setRequestSubType(null);
		otHdrTO.setRequestType("123456");

		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a RequestSubType.  Can't render XML.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 5 OperatingArea as null expected exception is OTHeaderTO
		 * doesn't have a OperatingArea. Can't render XML.
		 */
		otHdrTO.setOperatingArea(null);
		otHdrTO.setRequestSubType("123");
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a OperatingArea.  Can't render XML.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 6 UserId as null expected exception is OTHeaderTO doesn't
		 * have a UserId. Can't render XML.
		 */
		otHdrTO.setUserId(null);
		otHdrTO.setOperatingArea("123");
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a UserId.  Can't render XML.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 7 Password as null expected exception is OTHeaderTO
		 * doesn't have a Password. Can't render XML.
		 */
		otHdrTO.setPassword(null);
		otHdrTO.setUserId(123456);
		try {
			HkdOTHeaderXML.addHeaderElement(otHdrTO, commandStanza);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"OTHeaderTO doesn't have a Password.  Can't render XML.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for GetTO
	 * @throws DTIException
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	@Test
	public void testGetTO() throws URISyntaxException,
			FileNotFoundException, DocumentException {
		String xmlResponse = "";
		Document document = null;
		try {
			URL url = this.getClass().getResource(hkdXMLURls);
			File file = null;
			file = new File(url.toURI());
			InputStream ls = new FileInputStream(file);
			xmlResponse = DTITestUtilities.getXMLFromFile(ls);
			document = DocumentHelper.parseText(xmlResponse);
			Node commandStanza = document.getRootElement();
			Node manageResNode = commandStanza.selectSingleNode("Header");
			/* Scenario ::1 passing Header node */
			HkdOTHeaderXML.getTO(manageResNode);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		Document documentCreate = DocumentHelper.createDocument();
		Element rootCreate = documentCreate.addElement("Command");
		rootCreate.addElement("referenceNumber");
		try {
			HkdOTHeaderXML.getTO(rootCreate);
		} catch (DTIException dtie) {
			Assert.assertEquals("Ticket provider returned XML without a Header,ReferenceNumber element.",dtie.getLogMessage());
		}
	}
}
