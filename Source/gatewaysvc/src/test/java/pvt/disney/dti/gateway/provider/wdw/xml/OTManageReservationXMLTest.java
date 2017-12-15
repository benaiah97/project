package pvt.disney.dti.gateway.provider.wdw.xml;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.transform.impl.AddDelegateTransformer;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.InvalidXPathException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.XPath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.*;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Manage Reservation XML translations.
 * 
 * @author AGARS017
 * 
 */

/**
 * Sets the up before class.
 * 
 * @throws Exception
 *             the exception
 */

public class OTManageReservationXMLTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the ability of the system to create XML from a properly constructed
	 * transfer object.
	 */

	@Test
	public void testAddTxnBodyElement() {

		OTManageReservationTO otMngResTO = new OTManageReservationTO();
		Element commandStanza = null;
		Document document = DocumentHelper.createDocument();

		try {

			//ManageReservation
			commandStanza = document.addElement("Command");

			//WorkRules

			ArrayList<String> tagList = otMngResTO.getTagsList();

			// SiteNumber
			otMngResTO.setSiteNumber(10);
			otMngResTO.getSiteNumber().toString();

			//CommandType

			otMngResTO.setCommandType("abc");

			// CommandType
			otMngResTO.setCommandType("abc");

			//ReservationIdentifier

			otMngResTO.setReservationCode("RCode");

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otMngResTO.setReservationCode(null);
			otMngResTO.setReservationUniqueId("RUId");
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			otMngResTO.setReservationUniqueId(null);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			// Product 
			ArrayList<OTProductTO> arrOtProductTOs = new ArrayList<OTProductTO>();
			OTProductTO otProductTO = new OTProductTO();
			otProductTO.setProdPriceToken("ProdPriceQuoteToken");
			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			otProductTO.setItem(new BigInteger("2"));
			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			// ItemType
			otProductTO.setItemType("itemType");

			// ItemIdentifier
			otProductTO.setItemAlphaCode("22");
			otProductTO.setItemNumCode(new BigInteger("2"));
			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			// Quantity
			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);
			otProductTO.setQuantity(new BigInteger("100"));

			// Validity
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			GregorianCalendar gregorianCalendar1 = new GregorianCalendar();

			otProductTO.setValidity_StartDate(gregorianCalendar);
			otProductTO.setValidity_EndDate(gregorianCalendar1);

			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otProductTO.setValidity_StartDate(null);
			otProductTO.setValidity_EndDate(gregorianCalendar1);

			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otProductTO.setValidity_StartDate(gregorianCalendar);
			otProductTO.setValidity_EndDate(null);

			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otProductTO.setValidity_StartDate(null);
			otProductTO.setValidity_EndDate(null);

			arrOtProductTOs.add(otProductTO);
			otMngResTO.setProductList(arrOtProductTOs);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			otProductTO.setValidity_StartDate(gregorianCalendar);
			otProductTO.setValidity_EndDate(gregorianCalendar1);

			otProductTO.setPrice(new BigDecimal("1000.00"));

			// demographic data
			OTDemographicData otDemographicData = new OTDemographicData();
			ArrayList<OTDemographicData> arrDemographicDatas = new ArrayList<OTDemographicData>();
			arrDemographicDatas.add(otDemographicData);
			OTDemographicInfo otDemographicInfo = new OTDemographicInfo();
			otDemographicInfo.setDemoDataList(arrDemographicDatas);
			otProductTO.setDemographicInfo(otDemographicInfo);

			// ArrayList<String> arrString =new ArrayList<String>();
			// otProductTO.setEntitlementAccountId(arrString);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otProductTO.setItemNumCode(new BigInteger("2"));

			otMngResTO.setReservationCode("rcode");
			otMngResTO.setReservationUniqueId("rid");
			/**************** PaymentInfo **************************************************************************/
			otMngResTO.getPaymentInfoList();

			// SellerId*********
			otMngResTO.setSellerId((long) (2));
			/**************** AssociationInfo *************************************************************************/

			OTAssociationInfoTO oTAssociationInfoTO = new OTAssociationInfoTO();
			otMngResTO.setAssociationInfo(oTAssociationInfoTO);
			oTAssociationInfoTO.setAssociationId(2);
			oTAssociationInfoTO.setMemberId(10);
			/**************** MemberInfo *************************************************************************/

			oTAssociationInfoTO.setMemberId(10);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			oTAssociationInfoTO.setMemberId(null);
			oTAssociationInfoTO.setMemberField("MField");
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			oTAssociationInfoTO.setMemberId(20);
			/**************** AllowMemberCreation *************************************************************************/

			oTAssociationInfoTO.setAllowMemberCreation(true);

			/**************** DemographicData:: can't cover due to absent of setter *************************************************************************/

			ArrayList<OTFieldTO> arrOtFieldTo = new ArrayList<OTFieldTO>();

			OTFieldTO otFieldTO = new OTFieldTO(1, "newtest");

			arrOtFieldTo.add(otFieldTO);
			oTAssociationInfoTO.getDemographicData().add(otFieldTO);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			/*
			 * ZipCode (omitted)
			 */

			/**************** IATA *************************************************************************/
			otMngResTO.setIATA("iaia");

			/**************** TransactionNote (2.10) *************************************************************************/
			otMngResTO.setTransactionNote("TransactionNote");
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			/**************** TaxExemptCode *************************************************************************/

			otMngResTO.setTaxExemptCode("taxExemptCode");

			/*
			 * InTransactionAttribute (omitted)
			 */

			/********************* ReservationNote ********************************************************************/
			otMngResTO.setReservationNote("aaaaaa");

			/********************* Note Details ********************************************************************/
			ArrayList<String> noteDetailsArray = new ArrayList<String>();
			noteDetailsArray.add("testvalue");

			otMngResTO.setNoteDetailsArray(noteDetailsArray);

			/***************** ReservationData ***********************/
			// when otMngResTO = null
			otMngResTO.setReservationData(null);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			// when otMngResTO != null
			OTReservationDataTO otReservationDataTO = new OTReservationDataTO();
			otMngResTO.setReservationData(otReservationDataTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			// Printed
			otReservationDataTO.setPrinted(true);
			// Encoded
			otReservationDataTO.setEncoded(true);
			// Validated
			otReservationDataTO.setValidated(true);
			// Deposit (omitted)
			// SalesType
			otReservationDataTO.setSalesType(1);
			// ResPickupDate
			GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
			otReservationDataTO.setResPickupDate(gregorianCalendar2);
			// ResPickupArea
			otReservationDataTO.setResPickupArea(2);
			// ResPickupType
			otReservationDataTO.setResPickupType(1);
			// Batch (omitted)
			// BatchInfo (omitted)
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			/*********************************** ClientData *******************************/

			OTClientDataTO clientDataTO = new OTClientDataTO();
			otMngResTO.setClientData(clientDataTO);
			clientDataTO.setClientUniqueId(20);
			clientDataTO.setClientType("TYPE");
			clientDataTO.setClientSubtype(1);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			clientDataTO.setClientSubtype(null);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			clientDataTO.setClientCategory("catogry");
			clientDataTO.setClientLanguage(1);
			ArrayList<OTFieldTO> arrdemographicData = new ArrayList<OTFieldTO>();
			clientDataTO.setDemographicData(arrdemographicData);
			arrdemographicData.add(otFieldTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			clientDataTO.setDemographicData(null);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			clientDataTO.setDemographicData(arrdemographicData);
			// BillTo (currently in WDW XSD)
			// RegistrantInfo (currently in WDW XSD)

			// ShowData (omitted)
			// VoidToOriginalMeansOfPaym (as of 2.16.3, JTL)

			otMngResTO.setVoidToOrigPayment(false);

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			otMngResTO.setVoidToOrigPayment(true);

			// ExplodeComplementary (currently in WDW XSD)
			// AllowVoidUsedTickets (currently in WDW XSD)

			/*************************** Entitlement Account *******************************/

			otMngResTO.setEntitlementAccountCreationTypology("TOPOLOGY");

			OTAccountDataTO oTAccountDataTO = new OTAccountDataTO();
			otMngResTO.getAccountsData().add(oTAccountDataTO);

			oTAccountDataTO.setEntitlementAccountId(new BigInteger("2"));

			oTAccountDataTO.setAccountExternalReferenceType(new Byte("2"));

			oTAccountDataTO
					.setAccountExternalReference("accountExternalReference");

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			oTAccountDataTO.setAccountExternalReference(null);
			oTAccountDataTO.setSearchExistingMediaId("searchExistingMediaId");
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			oTAccountDataTO.setSearchExistingMediaId(null);
			oTAccountDataTO.setSearchAccountId("searchAccountId");
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			// oTAccountDataTO.setSearchExistingMediaId(null);
			oTAccountDataTO.setSearchAccountId(null);
			OTTicketTO oTTicketTO = new OTTicketTO();
			oTTicketTO.setBarCode("barCodeIn");
			oTAccountDataTO.setTicketSearchMode(oTTicketTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			oTTicketTO.setBarCode(null);
			oTTicketTO.setExternalTicketCode("externalTicketCodeIn");
			oTAccountDataTO.setTicketSearchMode(oTTicketTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			oTTicketTO.setExternalTicketCode(null);
			oTTicketTO.setMagTrack("magTrackIn");
			oTAccountDataTO.setTicketSearchMode(oTTicketTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			oTTicketTO.setMagTrack(null);
			oTTicketTO.setTCOD("tcodIn");
			oTAccountDataTO.setTicketSearchMode(oTTicketTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			oTTicketTO.setTCOD(null);
			oTTicketTO.setTDssn("17-12-12", "site", "station", "number");
			oTAccountDataTO.setTicketSearchMode(oTTicketTO);
			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			oTAccountDataTO
					.setAccountExternalReference("accountExternalReference");

			
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
			OTDemographicData oTDemographicData = new OTDemographicData();
			oTAccountDataTO.setDemoData(oTDemographicData);
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

			OTMediaDataTO oTMediaDataTO = new OTMediaDataTO();
			ArrayList<OTMediaDataTO> arrOTMediaDataTO = new ArrayList<OTMediaDataTO>();
			arrOTMediaDataTO.add(oTMediaDataTO);
			oTAccountDataTO.setMediaDataList(arrOTMediaDataTO);
			oTMediaDataTO.setMediaId("mediaId");
			oTMediaDataTO.setMfrId("mfrId");
			oTMediaDataTO.setVisualId("visualId");
			OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

         // GenerateEvent (Currently in WDW XSD)
         otMngResTO.setGenerateEvent(true);
         OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

         // External Transaction ID (optional as of 2.16.2, JTL)
         OTExternalTransactionIDTO otExternalTransactionIDTO = new OTExternalTransactionIDTO();
         otMngResTO.setExternalTransactionID(otExternalTransactionIDTO);
         OTExternalTransactionIDTO otExtTxnIdTO = new OTExternalTransactionIDTO();
         otExtTxnIdTO.setId("ID");
         otExtTxnIdTO.setAlreadyEncrypted(true);
         otMngResTO.setExternalTransactionID(otExtTxnIdTO);

         OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
         otProductTO.setItemNumCode(null);
         OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);

		} catch (Exception e) {

		}

	}

	/*
	 * @Test public final void testGetTO() throws DocumentException,
	 * DTIException { String fileName = new String(
	 * "./src/test/resources/xml/config/Reservation/test.xml"); Document
	 * document = DocumentHelper.parseText(fileName); Node node =
	 * document.getRootElement(); Node headerNode =
	 * node.selectSingleNode("Error");
	 * 
	 * OTManageReservationXML.getTO(headerNode); }
	 */
	/*
	 * @Test public void testGetToS(){ OTManageReservationTO otMngResTO = new
	 * OTManageReservationTO(); Node manageResNode=null;
	 */

	//@Test
	public final void testGetTO1() throws IOException {

		// ***** Read the test file. *****
		String baselineXML = "";
		String fileName = new String(
				"./src/test/resources/xml/config/Reservation/Reservation_01_Rsp_WDW.xml");
		OTCommandTO otCommandTO = null;
		// Scenario 1 :: Invalid XML
		try {
			otCommandTO = OTCommandXML.getTO(baselineXML);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.TP_INTERFACE_FAILURE);
		}

		// Scenario 2 :: when Command is not preset
		String fileNameTest1 = new String(
				"./src/test/resources/xml/config/Reservation/Reservation_02_Test.xml");
		try {
			baselineXML = DTITestUtilities.getXMLfromFile(fileNameTest1);
			otCommandTO = OTCommandXML.getTO(baselineXML);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.TP_INTERFACE_FAILURE);

		} catch (Exception e) {
		}

		// Scenario 4:: when header is not preset
		try {
			String fileNameTest2 = new String(
					"./src/test/resources/xml/config/Reservation/Reservation_01_Test.xml");
			baselineXML = DTITestUtilities.getXMLfromFile(fileNameTest2);
			otCommandTO = OTCommandXML.getTO(baselineXML);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.TP_INTERFACE_FAILURE);

		} 

		// Scenario 4:: Success Scenario
		try {
			baselineXML = DTITestUtilities.getXMLfromFile(fileName);
			otCommandTO = OTCommandXML.getTO(baselineXML);
		} catch (DTIException dtie) {
			Assert.fail("DTI Exception getting transfer object from XML string: "
					+ dtie.toString());
		} catch (Exception e) {
			Assert.fail("Unable to read test file: " + fileName);
		}

		try {
			validateManageReservationRspTO(otCommandTO);
		} catch (Exception e) {
			Assert.fail("Failure in validating command header: " + e.toString());
		}

		try {
			OTCommandXML.getTO(baselineXML);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.TP_INTERFACE_FAILURE);
		}
		return;

	}

	private void validateManageReservationRspTO(OTCommandTO otCommandTO)
			throws Exception {

		// Header
		OTHeaderTO otHdr = otCommandTO.getHeaderTO();
		if (otHdr == null)
			throw new Exception("OTCommandTO object had no OTHeaderTO object.");
		OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_MANAGE,
				OTTestUtil.REQSUBTYPE_MANAGERES);

		// ManageReservation
		OTManageReservationTO otMngRes = otCommandTO.getManageReservationTO();
		if (otMngRes == null)
			throw new Exception(
					"OTCommandTO object had no OTManageReservationTO object.");

		// Error
		OTErrorTO otErr = otMngRes.getError();
		if (otErr == null)
			throw new Exception(
					"OTManageReservationTO object had no OTErrorTO object.");
		OTTestUtil.validateOTRspError(otErr);

		String mngResNode = "OTManageReservationTO";

		// CommandType
		OTTestUtil.validateValue(mngResNode, "CommandType", OTTestUtil.CMDTYPE,
				otMngRes.getCommandType());

		// TicketInfo
		ArrayList<OTTicketInfoTO> ticketInfoList = otMngRes.getTicketInfoList();
		if ((ticketInfoList == null) || (ticketInfoList.size() == 0))
			throw new Exception(
					"OTManageReservationTO object had no or empty TicketInfo list.");
		validateTicketInfoList(ticketInfoList);

		// ProductInfo
		ArrayList<OTProductTO> productList = otMngRes.getProductList();
		if ((productList == null) || (productList.size() == 0))
			throw new Exception(
					"OTManageReservationTO object had no or empty ProductInfo list.");
		validateProductList(productList);

		// PaymentInfo
		ArrayList<OTPaymentTO> paymentList = otMngRes.getPaymentInfoList();
		OTTestUtil.validatePaymentList(paymentList);

		// TransactionDSSN
		OTTestUtil.validateTranDSSN(otMngRes.getTransactionDSSN());

		// TransactionCOD
		OTTestUtil.validateValue(mngResNode, "TransactionCOD",
				OTTestUtil.TRANCOD, otMngRes.getTransactionCOD());

		// TransactionTime
		OTTestUtil.validateValue(mngResNode, "TransactionTime",
				OTTestUtil.TRANTIME, otMngRes.getTransactionTime());

		// TotalAmount
		OTTestUtil.validateValue(mngResNode, "TotalAmount",
				OTTestUtil.TOTALAMT, otMngRes.getTotalAmount());

		// TotalTax
		OTTestUtil.validateValue(mngResNode, "TotalTax", OTTestUtil.TOTALTAX,
				otMngRes.getTotalTax());

		// TaxExempt
		OTTestUtil.validateValue(mngResNode, "TaxExempt", OTTestUtil.TAXEXEMPT,
				otMngRes.getTaxExempt());

		// ReservationCode

		OTTestUtil.validateValue(mngResNode, "ReservationCode",
				OTTestUtil.RESCODE, otMngRes.getReservationCode());

		// ReservationId
		OTTestUtil.validateValue(mngResNode, "ReservationId",
				OTTestUtil.RESERVATIONID, otMngRes.getReservationId());

		// ReservationData
		OTReservationDataTO otResData = otMngRes.getReservationData();
		if (otResData == null)
			throw new Exception(
					"OTManageReservationTO object had no ReservationData object.");
		validateReservationData(otResData);

		// ClientData
		OTClientDataTO otClientData = otMngRes.getClientData();
		if (otClientData == null)
			throw new Exception(
					"OTManageReservationTO object had no ClientData object.");
		validateClientData(otClientData);

		return;
	}

	private static void validateClientData(OTClientDataTO otClientData)
			throws Exception {

		String nodeName = "ClientData";

		// ClientUniqueId
		OTTestUtil.validateValue(nodeName, "ClientUniqueId",
				OTTestUtil.CLIENTUNIQUEID, otClientData.getClientUniqueId());

		// ClientType
		OTTestUtil.validateValue(nodeName, "ClientType", OTTestUtil.CLIENTTYPE,
				otClientData.getClientType());

		// ClientSubtype
		OTTestUtil.validateValue(nodeName, "ClientSubtype",
				OTTestUtil.CLIENTSUBTYPE, otClientData.getClientSubtype());

		// ClientCategory
		OTTestUtil.validateValue(nodeName, "ClientCategory",
				OTTestUtil.CLIENTCATEGORY, otClientData.getClientCategory());

		// ClientLanguage
		OTTestUtil.validateValue(nodeName, "ClientLanguage",
				OTTestUtil.RSPCLIENTLANGUAGE, otClientData.getClientLanguage());

		// DemographicData
		ArrayList<OTFieldTO> demoList = otClientData.getDemographicData();
		if ((demoList == null) || (demoList.size() == 0))
			throw new Exception(
					"ClientData object had no or empty DemographicData list.");
		validateDemoList(demoList);

		return;
	}

	private static void validateDemoList(ArrayList<OTFieldTO> demoList)
			throws Exception {

		if (demoList.size() != 2)
			throw new Exception(
					"DemographicData list did not have 2 members, as expected.");

		String nodeName = "DemographicData.Field";

		int itemNum = 0;

		for /* each */(OTFieldTO aField : /* in */demoList) {

			// Item
			Integer item = aField.getFieldIndex();
			if (item == null)
				throw new Exception("DemographicData.Field object had no Item");
			Integer validItem = Integer.valueOf(++itemNum);
			if (validItem.compareTo(item) != 0)
				throw new Exception(
						"DemographicData.Field object had invalid Item");

			// FieldType
			OTTestUtil.validateValue(nodeName, "FieldType",
					OTTestUtil.FIELDTYPE, aField.getFeildType());

			// FieldValue
			OTTestUtil.validateValue(nodeName, "FieldValue",
					OTTestUtil.FIELDVALUE, aField.getFieldValue());
		}

		return;
	}

	private static void validateReservationData(OTReservationDataTO otResData)
			throws Exception {

		String nodeName = "ReservationData";

		// Paid
		OTTestUtil.validateValue(nodeName, "Paid", OTTestUtil.PAID,
				otResData.getPaid());

		// Printed
		OTTestUtil.validateValue(nodeName, "Printed", OTTestUtil.PRINTED,
				otResData.getPrinted());

		// Encoded
		OTTestUtil.validateValue(nodeName, "Encoded", OTTestUtil.ENCODED,
				otResData.getEncoded());

		// Validated
		OTTestUtil.validateValue(nodeName, "Validated", OTTestUtil.VALIDATED,
				otResData.getValidated());

		// DepositAmount
		OTTestUtil.validateValue(nodeName, "DepositAmount",
				OTTestUtil.DEPOSITAMOUNT, otResData.getDepositAmount());

		// SalesType
		OTTestUtil.validateValue(nodeName, "SalesType", OTTestUtil.SALESTYPE,
				otResData.getSalesType());

		// ResStatus
		OTTestUtil.validateValue(nodeName, "ResStatus", OTTestUtil.RESSTATUS,
				otResData.getResStatus());

		// ResPickupDate
		OTTestUtil.validateValue(nodeName, "ResPickupDate",
				OTTestUtil.RSPRESPICKUPDATE, otResData.getResPickupDate());

		// ResPickupArea
		OTTestUtil.validateValue(nodeName, "ResPickupArea",
				OTTestUtil.RESPICKUPAREA, otResData.getResPickupArea());

		// ResPickupType
		OTTestUtil.validateValue(nodeName, "ResPickupType",
				OTTestUtil.RESPICKUPTYPE, otResData.getResPickupType());

		return;
	}

	private static void validateProductList(ArrayList<OTProductTO> productList)
			throws Exception {

		if (productList.size() != 2)
			throw new Exception(
					"ProductInfo list did not have 2 members, as expected.");

		String nodeName = "OTProductTO";

		int itemNum = 0;

		for /* each */(OTProductTO aProduct : /* in */productList) {

			// Item
			BigInteger item = aProduct.getItem();
			if (item == null)
				throw new Exception("OTProductTO object had no Item");
			BigInteger validItem = BigInteger.valueOf(++itemNum);
			if (validItem.compareTo(item) != 0)
				throw new Exception("OTProductTO object had invalid Item");

			// TicketType
			OTTestUtil.validateValue(nodeName, "TicketType",
					OTTestUtil.TICKETTYPE, aProduct.getTicketType());

			// Price
			OTTestUtil.validateValue(nodeName, "Price", OTTestUtil.PRICE,
					aProduct.getPrice());

			// Qty
			OTTestUtil.validateValue(nodeName, "Qty", OTTestUtil.QUANTITY,
					aProduct.getQuantity());

			// Tax
			OTTestUtil.validateValue(nodeName, "Tax", OTTestUtil.TAX,
					aProduct.getTax());

			// TicketName
			OTTestUtil.validateValue(nodeName, "TicketName",
					OTTestUtil.TICKETNAME, aProduct.getTicketName());

			// Description
			OTTestUtil.validateValue(nodeName, "Description",
					OTTestUtil.DESCRPTION, aProduct.getDescription());

		}

		return;
	}

	private static void validateTicketInfoList(
			ArrayList<OTTicketInfoTO> ticketInfoList) throws Exception {

		String nodeName = "OTTicketInfoTO";

		if (ticketInfoList.size() != 2)
			throw new Exception(
					"OTTicketInfoTO list did not have 2 members, as expected.");

		int itemNum = 0;

		for /* each */(OTTicketInfoTO aTicketInfo : /* in */ticketInfoList) {

			// Item
			BigInteger item = aTicketInfo.getItem();
			if (item == null)
				throw new Exception("OTTicketInfoTO object had no Item");
			BigInteger validItem = BigInteger.valueOf(++itemNum);
			if (validItem.compareTo(item) != 0)
				throw new Exception("OTTicketInfoTO object had invalid Item");

			// ItemStatus
			OTTestUtil.validateValue("OTTicketInfoTO", "ItemStatus",
					OTTestUtil.ITEMSTATUS, aTicketInfo.getItemStatus());

			// ItemType (ignored)
			// ItemAlphaCode (ignored)
			// ItemNumCode (ignored)
			// TicketName (ignored)
			// Description (ignored)
			// PrintedPrice (ignored)
			// TicketFlag (ignored)
			// TicketAttribute (ignored)
			// TktNote (ignored)
			// BiometricLevel (ignored)
			// ShowData (ignored)
			// SeatData (ignored)
			// Layout (ignored)

			// Price
			OTTestUtil.validateValue(nodeName, "Price", OTTestUtil.PRICE,
					aTicketInfo.getPrice());

			// Tax
			OTTestUtil.validateValue(nodeName, "Tax", OTTestUtil.TAX,
					aTicketInfo.getTax());

			// Ticket (obj)
			OTTestUtil.validateTicket(aTicketInfo.getTicket());

			// Validity StartDate
			OTTestUtil.validateValue(nodeName, "Validity.StartDate",
					OTTestUtil.STARTDATE, aTicketInfo.getValidityStartDate());

			// Validity EndDate
			OTTestUtil.validateValue(nodeName, "Validity.EndDate",
					OTTestUtil.ENDDATE, aTicketInfo.getValidityEndDate());

		} // TicketInfo Loop

		return;
	}

}
