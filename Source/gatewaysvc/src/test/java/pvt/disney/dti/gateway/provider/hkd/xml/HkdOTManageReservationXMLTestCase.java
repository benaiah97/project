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
import java.util.GregorianCalendar;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAccountDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicData;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicInfo;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTExternalTransactionIDTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTMediaDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTReservationDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTShowDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit HkdOTManageReservationXML
 *
 */
public class HkdOTManageReservationXMLTestCase  extends CommonTestUtils{
	/**
	 * JUnit for addTxnBodyElement
	 */
	@Test
	public void testAddTxnBodyElement() {
		HkdOTManageReservationTO otMngResTO = new HkdOTManageReservationTO();
		getHkdOTManageReserve(otMngResTO);
		Document document = DocumentHelper.createDocument();
		Element commandStanza = document.addElement("Command");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		/* Scenario :: 1 BarCode as null */
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setBarCode(null);
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setTCOD("tcodIn");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		/* Scenario :: 2 TCOD as null */
		otMngResTO.getAccountsData().get(0).getTicketSearchMode().setTCOD(null);
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setMagTrack("magTrackIn");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		/* Scenario :: 3 MagTrack as null */
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setMagTrack(null);
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setExternalTicketCode("externalTicketCodeIn");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		/* Scenario :: 4 ExternalTicketCode as null */
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setExternalTicketCode(null);
		otMngResTO.getAccountsData().get(0).getTicketSearchMode()
				.setTDssn(new GregorianCalendar(), "site", "station", "22");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		otMngResTO.getAccountsData().get(0).setAccountExternalReference("ExternalReference");
		otMngResTO.getAccountsData().get(0).setAccountExternalReferenceType(new Byte("2"));
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
		/* Scenario :: 5 AccountExternalReferenceType as null */
		otMngResTO.getAccountsData().get(0).setAccountExternalReferenceType(null);
		otMngResTO.getAccountsData().get(0).setSearchExistingMediaId("searchExistingMediaId");
		HkdOTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
	}

	/**
	 * JUnit for getTO
	 * @throws DocumentException
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testGetTO() throws DocumentException, URISyntaxException,
			FileNotFoundException {
		String xmlResponse = null;
		Document document = null;
		HkdOTManageReservationTO manageReservationTO = null;
		try {
			URL url = this.getClass().getResource(hkdXMLURls);
			File file = null;
			file = new File(url.toURI());
			InputStream ls = new FileInputStream(file);
			xmlResponse = DTITestUtilities.getXMLFromFile(ls);
			document = DocumentHelper.parseText(xmlResponse);
			Node commandStanza = document.getRootElement();
			Node manageResNode = commandStanza
					.selectSingleNode("ManageReservation");
			/* Scenario :: 1 Passing  ManageReservation node */
			manageReservationTO=HkdOTManageReservationXML.getTO(manageResNode);
			Assert.assertNotNull(manageReservationTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception"+dtie.getLogMessage());
		}
	}

	/**
	 * @param otMngResTO
	 */
	private static void getHkdOTManageReserve(
			HkdOTManageReservationTO otMngResTO) {
		HkdOTAssociationInfoTO associationInfo = new HkdOTAssociationInfoTO();
		HkdOTDemographicInfo demographicInfo = new HkdOTDemographicInfo();
		HkdOTAccountDataTO accountDataTO = new HkdOTAccountDataTO();
		HkdOTDemographicData demoData = new HkdOTDemographicData();
		HkdOTMediaDataTO hkdOTMediaDataTO = new HkdOTMediaDataTO();
		HkdOTProductTO hkdOTProductTO = new HkdOTProductTO();
		HkdOTClientDataTO clientData = new HkdOTClientDataTO();
		HkdOTTicketTO hkdOTTicketTO = new HkdOTTicketTO();
		HkdOTReservationDataTO reservationData = new HkdOTReservationDataTO();
		reservationData.setPrinted(true);
		reservationData.setEncoded(true);
		reservationData.setValidated(true);
		reservationData.setSalesType(555);
		reservationData.setResPickupDate(new GregorianCalendar());
		reservationData.setResPickupArea(444);
		reservationData.setResPickupType(8888);
		HkdOTFieldTO fieldTO = new HkdOTFieldTO(55, "fieldValueIn");
		ArrayList<HkdOTFieldTO> demographicData = new ArrayList<>();
		demographicData.add(fieldTO);
		clientData.setClientUniqueId(22);
		clientData.setClientLanguage(222);
		clientData.setClientSubtype(66);
		clientData.setClientType("clientType");
		clientData.setClientCategory("clientCategory");
		clientData.setDemographicData(demographicData);
		ArrayList<HkdOTDemographicData> demoDataList = new ArrayList<>();
		demoDataList.add(demoData);
		hkdOTProductTO.setItem(new BigInteger("3"));
		hkdOTProductTO.setItemAlphaCode("itemId_ItemAlphaCode");
		hkdOTProductTO.setItemNumCode(new BigInteger("3"));
		hkdOTProductTO.setItemType("itemType");
		hkdOTProductTO.setQuantity(new BigInteger("555"));
		hkdOTProductTO.setValidity_StartDate(new GregorianCalendar());
		hkdOTProductTO.setValidity_EndDate(new GregorianCalendar());
		hkdOTProductTO.setPrice(new BigDecimal("2"));
		hkdOTProductTO.setGroupCode("groupCode");
		hkdOTProductTO.getEntitlementAccountId().add("ONE");
		demographicInfo.setDemoDataList(demoDataList);
		hkdOTProductTO.setDemographicInfo(demographicInfo);
		ArrayList<HkdOTProductTO> products = new ArrayList<>();
		products.add(hkdOTProductTO);
		hkdOTMediaDataTO.setMediaId("mediaId");
		hkdOTMediaDataTO.setMfrId("mfrId");
		hkdOTMediaDataTO.setVisualId("visualId");
		ArrayList<HkdOTMediaDataTO> mediaDataList = new ArrayList<>();
		mediaDataList.add(hkdOTMediaDataTO);
		HkdOTExternalTransactionIDTO externalTransactionID = new HkdOTExternalTransactionIDTO();
		externalTransactionID.setAlreadyEncrypted(true);
		externalTransactionID.setId("2");
		HkdOTShowDataTO showData = new HkdOTShowDataTO();
		showData.setGroupCode("groupCode");
		showData.setPerformanceId("performanceId");
		showData.setQuota("quota");
		ArrayList<String> noteDetailsArray = new ArrayList<>();
		noteDetailsArray.add("one");
		HkdOTFieldTO hkdOTFieldTO = new HkdOTFieldTO(22, "field");
		associationInfo.setAssociationId(22);
		associationInfo.setMemberId(2);
		associationInfo.setMemberField("55");
		associationInfo.setAllowMemberCreation(true);
		associationInfo.getDemographicData().add(hkdOTFieldTO);
		otMngResTO.setSiteNumber(123);
		otMngResTO.setCommandType("Create");
		otMngResTO.setReservationCode("123456");
		otMngResTO.setReservationUniqueId("2345");
		otMngResTO.setSellerId(new Long(2));
		otMngResTO.setIATA("iata");
		otMngResTO.setTransactionNote("222");
		otMngResTO.setTaxExemptCode("taxExemptCode");
		otMngResTO.setReservationNote("reservAt");
		otMngResTO.setNoteDetailsArray(noteDetailsArray);
		otMngResTO.setShowData(showData);
		otMngResTO.setGenerateEvent(true);
		otMngResTO.setExternalTransactionID(externalTransactionID);
		otMngResTO.setEntitlementAccountCreationTypology("ent");
		otMngResTO.setAssociationInfo(associationInfo);
		otMngResTO.setProductList(products);
		otMngResTO.setClientData(clientData);
		otMngResTO.setReservationData(reservationData);
		hkdOTTicketTO.setBarCode("A0001");
		accountDataTO.setEntitlementAccountId(new BigInteger("2"));
		accountDataTO.setTicketSearchMode(hkdOTTicketTO);
		accountDataTO.setDemoData(demoData);
		accountDataTO.setMediaDataList(mediaDataList);
		otMngResTO.getAccountsData().add(accountDataTO);
	}
}
