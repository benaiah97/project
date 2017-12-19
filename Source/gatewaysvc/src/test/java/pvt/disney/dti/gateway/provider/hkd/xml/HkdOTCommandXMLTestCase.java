package pvt.disney.dti.gateway.provider.hkd.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTCreditCardTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTErrorTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTVoucherTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for HkdOTCommandXML
 */
public class HkdOTCommandXMLTestCase extends CommonTestUtils {

	/**
	 * JUnit for getXML
	 */
	@Test
	public void testGetXML() {
		HkdOTCommandTO otCommandTO = new HkdOTCommandTO(
				HkdOTCommandTO.OTTransactionType.MANAGERESERVATION);
		HkdOTHeaderTO headerTO = new HkdOTHeaderTO();
		headerTO.setReferenceNumber("123456");
		headerTO.setRequestNumber(1231235);
		headerTO.setRequestType("Create");
		headerTO.setRequestSubType("ManageReservation");
		headerTO.setSessionId(123);
		headerTO.setUserId(12);
		headerTO.setPassword(1111);
		headerTO.setOperatingArea("12");
		otCommandTO.setHeaderTO(headerTO);
		HkdOTManageReservationTO manageReservationTO = new HkdOTManageReservationTO();
		ArrayList<String> tags = new ArrayList<>();
		tags.add("Tag");
		manageReservationTO.setTagsList(tags);
		manageReservationTO.setSiteNumber(123);
		manageReservationTO.setCommandType("CommandType");
		otCommandTO.setManageReservationTO(manageReservationTO);
		/*scenario : 1 Transaction type is MANAGERESERVATION */ 
		String xml = null;
		try {
			xml = HkdOTCommandXML.getXML(otCommandTO);
			Assert.assertNotNull(xml);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/*scenario : 2 Transaction type is UNDEFINED */ 
		otCommandTO = new HkdOTCommandTO(
				HkdOTCommandTO.OTTransactionType.UNDEFINED);
		otCommandTO.setHeaderTO(headerTO);
		otCommandTO.setManageReservationTO(manageReservationTO);
		try {
			xml = HkdOTCommandXML.getXML(otCommandTO);
			Assert.assertNotNull(xml);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Internal Error:  Unknown enumeration on OTCommandTO class.",
					dtie.getLogMessage());
		}

	}

	/**
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 *             JUnit for getTO
	 */
	@Test
	public void testGetTO() throws FileNotFoundException, URISyntaxException {
		String xmlResponse = null;
		HkdOTCommandTO hkdOTCommandTO = null;
		URL url = this.getClass().getResource(hkdXMLURls);
		File file = null;
		try {
			file = new File(url.toURI());
			InputStream ls = new FileInputStream(file);
			xmlResponse = DTITestUtilities.getXMLFromFile(ls);
			/*scenario : 1 Passing xml response */ 
			hkdOTCommandTO = HkdOTCommandXML.getTO(xmlResponse);
			Assert.assertNotNull(hkdOTCommandTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for addPaymentInfoStanza
	 */
	@Test
	public void testAddPaymentInfoStanza() {
		Document document = DocumentHelper.createDocument();
		Element txnStanza = document.addElement("Command");
		ArrayList<HkdOTPaymentTO> payList = new ArrayList<>();
		gethkdOTPaymentTO(payList);
		/*scenario : 1 CreditCardEntryType.MANUAL */ 
		HkdOTCommandXML.addPaymentInfoStanza(txnStanza, payList);
		/*scenario : 2 CreditCardEntryType.SWIPED */ 
		for (HkdOTPaymentTO hkdOTPaymentTO : payList) {
			hkdOTPaymentTO.getCreditCard().setTrack1("ccTrack1");
			hkdOTPaymentTO.getInstallment().setCardNumber("123654");
		}
		HkdOTCommandXML.addPaymentInfoStanza(txnStanza, payList);
	}

	/**
	 * JUnit for addInTransactionAttributeStanzas
	 */
	@Test
	public void testAddInTransactionAttributeStanzas() {
		Document document = DocumentHelper.createDocument();
		Element tktStanza = document.addElement("Command");
		HkdOTInTransactionAttributeTO inTransAttributeTO = new HkdOTInTransactionAttributeTO();
		inTransAttributeTO.setAttributeCmd("attributeCmd");
		inTransAttributeTO.setAttributeFlag("attributeFlag");
		inTransAttributeTO.setAttributeKey(12);
		inTransAttributeTO.setAttributeType(555);
		inTransAttributeTO.setAttributeValue("attributeValue");
		/*scenario : 1 passing HkdOTInTransactionAttributeTOL */ 
		ArrayList<HkdOTInTransactionAttributeTO> inTxnAttrList = new ArrayList<>();
		inTxnAttrList.add(inTransAttributeTO);
		HkdOTCommandXML.addInTransactionAttributeStanzas(tktStanza,
				inTxnAttrList);

	}

	/**
	 * JUnit for getErrorTO
	 * 
	 * @throws DocumentException
	 * @throws FileNotFoundException
	 * @throws URISyntaxException
	 */
	@Test
	public void testGetErrorTO() throws DocumentException,
			FileNotFoundException, URISyntaxException {
		String xmlResponse = null;
		Document document = null;
		HkdOTErrorTO hkdOTErrorTO = null;
		try {
			URL url = this.getClass().getResource(hkdXMLURls);
			File file = null;
			file = new File(url.toURI());
			InputStream ls = new FileInputStream(file);
			xmlResponse = DTITestUtilities.getXMLFromFile(ls);
			document = DocumentHelper.parseText(xmlResponse);
			Node commandStanza = document.getRootElement();
			Node headerNode = commandStanza
					.selectSingleNode("ManageReservation");
			/*scenario : 1 passing ManageReservation node */ 
			hkdOTErrorTO = HkdOTCommandXML.getErrorTO(headerNode);
			Assert.assertNotNull(hkdOTErrorTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for setOTTicketInfoValidity
	 * 
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 */
	@Test
	public void testSetOTTicketInfoValidity() throws URISyntaxException,
			FileNotFoundException, DocumentException {
		String xmlResponse = null;
		Document document = null;
		try {
			URL url = this.getClass().getResource(hkdXMLURls);
			File file = null;
			file = new File(url.toURI());
			InputStream ls = new FileInputStream(file);
			xmlResponse = DTITestUtilities.getXMLFromFile(ls);
			document = DocumentHelper.parseText(xmlResponse);
			Node commandStanza = document.getRootElement();
			Node headerNode = commandStanza
					.selectSingleNode("ManageReservation");
			HkdOTTicketInfoTO otTktTO = new HkdOTTicketInfoTO();
			/*scenario : 1 passing ManageReservation node */ 
			HkdOTCommandXML.setOTTicketInfoValidity(headerNode, otTktTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}
	/**
	 * JUnit for setOTErrorTO 
	 */
	@Test
	public void testSetOTErrorTO() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Command");
		root.addElement("ErrorCode");
		Node commandStanza = document.getRootElement();
		Node errorNode = commandStanza.selectSingleNode("ErrorCode");
		/*scenario : 1 passing ErrorCode node */ 
		try {
			HkdOTCommandXML.setOTErrorTO(errorNode);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket provider returned XML without a Error,ErrorCode element.",
					dtie.getLogMessage());
		}
	}
	/**
	 * JUnit for setOTTicketTO 
	 */
	@Test
	public void testSetOTTicketTO() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("Command");
		root.addElement("TDSSN");
		/*Scenario :: 1 Element value are null */ 
		try {
			HkdOTCommandXML.setOTTicketTO(root);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket provider returned XML with incomplete TDSSN element.",
					dtie.getLogMessage());
		}	
	}
	/**
	 * @param payList
	 */
	private static void gethkdOTPaymentTO(ArrayList<HkdOTPaymentTO> payList) {
		HkdOTPaymentTO hkdOTPaymentTO = new HkdOTPaymentTO();
		HkdOTInstallmentTO installment = new HkdOTInstallmentTO();
		installment.setCardNumber("123456789");
		installment.setCardExpDate("10-10-10");
		installment.setCardHolderName("Disney");
		installment.setContractAlphaCode(2);
		installment.setTrack1("Track1");
		installment.setTrack2("Track2");
		HkdOTVoucherTO voucher = new HkdOTVoucherTO();
		voucher.setMasterCode("123456");
		voucher.setUniqueCode("");
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.CREDITCARD);
		hkdOTPaymentTO.setPayItem(new BigInteger("3"));
		hkdOTPaymentTO.setPayAmount(new BigDecimal("3"));
		hkdOTPaymentTO.setVoucher(voucher);
		HkdOTCreditCardTO creditCard = new HkdOTCreditCardTO();
		creditCard.setTrack1("123456");
		creditCard.setTrack2("2356");
		creditCard.setPosTerminalId("1236");
		creditCard.setExternalDeviceSerialId("123564");
		creditCard.setCardNumber("22");
		creditCard.setSha1("23");
		creditCard.setSubCode("SubCode");
		creditCard.setCardExpDate("10-10-10");
		creditCard.setCVV("1112");
		creditCard.setAvs_AvsStreet("AvsStreet");
		creditCard.setAvs_AvsZipCode("123456");
		creditCard.setCAVVFormat("12");
		creditCard.setCAVVValue("1234");
		creditCard.setECommerceValue("234");
		creditCard.setIsPreApproved(true);
		creditCard.setAuthCode("234");
		hkdOTPaymentTO.setCreditCard(creditCard);
		payList.add(hkdOTPaymentTO);
		hkdOTPaymentTO.setInstallment(installment);
		payList.add(hkdOTPaymentTO);
	}
}