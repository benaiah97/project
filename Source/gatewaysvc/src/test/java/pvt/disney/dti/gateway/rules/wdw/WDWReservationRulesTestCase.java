package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.AttributeTO.CmdAttrCodeType;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInstallmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.service.dtixml.ReservationXML;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for WDWReservationRules.
 *
 * @author ARORT002
 */
public class WDWReservationRulesTestCase extends CommonTestUtils {

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
	}

	/**
	 * test case for transformRequest method.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		ReservationRequestTO reservationRequestTO = getReservationRequestTO();

		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		ArrayList<DBProductTO> dbProductTOList = new ArrayList<DBProductTO>();
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setPdtCode("ABC");
		
		dbProductTO.setMappedProviderTktNbr(BigInteger.valueOf(123));
		dbProductTO.setValidityDateInfoRequired(true);

		dbProductTOList.add(dbProductTO);
		dtiTxn.setDbProdList(dbProductTOList);
		String xmlString = null;

		TPLookupTO aTPLookupSalesTypeTO = new TPLookupTO();
		aTPLookupSalesTypeTO.setLookupValue("123");
		aTPLookupSalesTypeTO.setLookupType(TPLookupTO.TPLookupType.SALES_TYPE);

		TPLookupTO aTPLookupPickUpAreaTO = new TPLookupTO();
		aTPLookupPickUpAreaTO.setLookupValue("123");
		aTPLookupPickUpAreaTO
				.setLookupType(TPLookupTO.TPLookupType.PICKUP_AREA);

		TPLookupTO aTPLookupPickUpTypeTO = new TPLookupTO();
		aTPLookupPickUpTypeTO.setLookupValue("123");
		aTPLookupPickUpTypeTO
				.setLookupType(TPLookupTO.TPLookupType.PICKUP_TYPE);

		TPLookupTO aTPLookupClientTypeTO = new TPLookupTO();
		aTPLookupClientTypeTO.setLookupValue("123");
		aTPLookupClientTypeTO
				.setLookupType(TPLookupTO.TPLookupType.CLIENT_TYPE);

		TPLookupTO aTPLookupLanguageTO = new TPLookupTO();
		aTPLookupLanguageTO.setLookupValue("123");
		aTPLookupLanguageTO.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);

		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<TPLookupTO>();
		tpLookupTOListIn.add(aTPLookupSalesTypeTO);
		tpLookupTOListIn.add(aTPLookupPickUpAreaTO);
		tpLookupTOListIn.add(aTPLookupPickUpTypeTO);
		tpLookupTOListIn.add(aTPLookupClientTypeTO);
		tpLookupTOListIn.add(aTPLookupLanguageTO);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		DTIMockUtil.mockGetEligibilityAssocId();
		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		ReservationRequestTO reservationReqTO = getReservationRequestTO();
		reservationReqTO.setEligibilityMember("ABC");
		reservationReqTO.setEligibilityGroup("ABC");
		dtiTxn.getRequest().setCommandBody(reservationReqTO);
		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		ReservationRequestTO reservatonReqTO = getReservationRequestTO();
		reservatonReqTO.setEligibilityMember("123");
		reservatonReqTO.setEligibilityGroup("ABC");
		dtiTxn.getRequest().setCommandBody(reservatonReqTO);
		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		reservationRequestTO
				.setDefaultAccount(ReservationXML.ACCOUNT_PER_TICKET);
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("abc");
		reservation.setResSalesType("ManualMailOrder");
		reservationRequestTO.setReservation(reservation);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		reservationRequestTO.setDefaultAccount(null);
		ReservationTO reservationTo = new ReservationTO();
		reservationTo.setResCode("abc");
		reservationTo.setResSalesType("");
		reservationRequestTO.setReservation(reservationTo);
		SpecifiedAccountTO specifiedAccountTO = new SpecifiedAccountTO();
		specifiedAccountTO.setAccountItem(BigInteger.valueOf(1));
		specifiedAccountTO.setNewExternalReferenceType("XBANDID");
		specifiedAccountTO.setNewExternalReferenceValue("def");
		reservationRequestTO.getSpecifiedAccounts().add(specifiedAccountTO);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);

		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		reservationRequestTO.setDefaultAccount(null);
		SpecifiedAccountTO specifiedAccount = new SpecifiedAccountTO();
		specifiedAccount.setAccountItem(BigInteger.valueOf(1));
		specifiedAccount.setNewExternalReferenceType("XBANDID");
		specifiedAccount.setNewExternalReferenceValue("def");
		ArrayList<TicketTO> tickektList = getTktList(false);
		reservationRequestTO.setTktList(tickektList);
		reservationRequestTO.getSpecifiedAccounts().add(specifiedAccountTO);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);

		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		reservationRequestTO.setDefaultAccount(null);
		SpecifiedAccountTO specifiedAccunt = new SpecifiedAccountTO();
		specifiedAccunt.setAccountItem(BigInteger.valueOf(1));
		specifiedAccunt.setNewExternalReferenceType(null);
		specifiedAccunt.setNewExternalReferenceValue(null);
		specifiedAccunt.setExistingMediaId("ABC");
		ArrayList<TicketTO> ticktList = getTktList(false);
		reservationRequestTO.setTktList(ticktList);
		reservationRequestTO.getSpecifiedAccounts().add(specifiedAccunt);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);

		try {
			xmlString = WDWReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

//		 reservationRequestTO.setDefaultAccount(null);
//		 SpecifiedAccountTO specifedAccunt = new SpecifiedAccountTO();
//		 specifedAccunt.setAccountItem(BigInteger.valueOf(1));
//		 specifedAccunt.setNewExternalReferenceType(null);
//		 specifedAccunt.setNewExternalReferenceValue(null);
//		 TicketIdTO ticketIdTO = new TicketIdTO();
//		 ticketIdTO.getTicketTypes().add(TicketIdType.DSSN_ID);
//		 specifedAccunt.setExistingTktID(ticketIdTO);
//		 ArrayList<TicketTO> tiktList = getTktList(false);
//		 reservationRequestTO.setTktList(tiktList);
//		 reservationRequestTO.getSpecifiedAccounts().add(specifedAccunt);
//		 dtiTxn.getRequest().setCommandBody(reservationRequestTO);
//		 dtiTxn.setTpRefNum(1);
//		 HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = new
//		 HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();
//		 AttributeTO attributeTOOpArea = new AttributeTO();
//		 attributeTOOpArea.setAttrValue("ABC");
//		
//		 AttributeTO attributeTOUser = new AttributeTO();
//		 attributeTOUser.setAttrValue("123");
//		 AttributeTO attributeTOPass = new AttributeTO();
//		 attributeTOPass.setAttrValue("123");
//		 aMap.put(AttributeTO.CmdAttrCodeType.OP_AREA,attributeTOOpArea);
//		 aMap.put(AttributeTO.CmdAttrCodeType.USER,attributeTOUser);
//		 aMap.put(AttributeTO.CmdAttrCodeType.PASS,attributeTOPass);
//		 dtiTxn.setAttributeTOMap(aMap);
//		 try {
//		 xmlString = WDWReservationRules.transformRequest(dtiTxn);
//		 assertNotNull(xmlString);
//		 } catch (DTIException dtie) {
//		 Assert.fail("Unexepected Exception " + dtie.getLogMessage());
//		 }

	}

	/**
	 * Gets the reservation request to.
	 *
	 * @return the reservation request to
	 */
	private ReservationRequestTO getReservationRequestTO() {
		ReservationRequestTO reservationRequestTO = new ReservationRequestTO();
		reservationRequestTO
				.setDefaultAccount(ReservationXML.ACCOUNT_PER_ORDER);
		reservationRequestTO.setEligibilityGroup("DVC");
		reservationRequestTO.setEligibilityMember("123");
		ExtTxnIdentifierTO extTxnIdentifierTO = new ExtTxnIdentifierTO();
		extTxnIdentifierTO.setTxnIdentifier("asd");
		extTxnIdentifierTO.setIsSHA1Encrypted(true);
		reservationRequestTO.setExtTxnIdentifier(extTxnIdentifierTO);
		AgencyTO agencyTO = new AgencyTO();
		agencyTO.setIATA("IAIA");
		agencyTO.setAgent("asd");
		reservationRequestTO.setAgency(agencyTO);
		reservationRequestTO.setTaxExemptCode("ABC");

		ReservationTO reservationTO = new ReservationTO();
		reservationTO.setResCode("abc");
		reservationTO.setResSalesType("Presale");

		ArrayList<PaymentTO> dtiPayList = new ArrayList<PaymentTO>();
		PaymentTO paymentTO = new PaymentTO();

		CreditCardTO dtiCreditCard = new CreditCardTO();
		dtiCreditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCSWIPE);
		dtiCreditCard.setCcTrack1("123");
		dtiCreditCard.setCcTrack2("456");
		dtiCreditCard.setPosTermID("12");
		dtiCreditCard.setWireless(true);
		paymentTO.setPayType(PaymentType.CREDITCARD);
		paymentTO.setCreditCard(dtiCreditCard);
		paymentTO.setPayItem(BigInteger.valueOf(1));
		paymentTO.setPayAmount(BigDecimal.valueOf(2.0));
		dtiPayList.add(paymentTO);
		reservationRequestTO.setPaymentList(dtiPayList);

		reservationRequestTO.setReservation(reservationTO);

		ArrayList<TicketTO> tktList = getTktList(true);
		reservationRequestTO.setTktList(tktList);

		ClientDataTO clientData = new ClientDataTO();
		clientData.setClientId("123");
		DemographicsTO billingInfo = getDemographicsTO();
		DemographicsTO shippingInfo = getDemographicsTO();
		clientData.setBillingInfo(billingInfo);
		clientData.setShippingInfo(shippingInfo);
		clientData.setClientCategory("abc");
		reservationRequestTO.setClientData(clientData);
		return reservationRequestTO;
	}

	/**
	 * Gets the demographics to.
	 *
	 * @return the demographics to
	 */
	private DemographicsTO getDemographicsTO() {
		DemographicsTO billingInfo = new DemographicsTO();
		billingInfo.setName("ABC");
		billingInfo.setFirstName("ABC");
		billingInfo.setLastName("DEF");
		billingInfo.setAddr1("ABC");
		billingInfo.setAddr2("DEF");
		billingInfo.setCity("ABC");
		billingInfo.setState("ABC");
		billingInfo.setZip("ABC");
		billingInfo.setCountry("ABC");
		billingInfo.setTelephone("123456789");
		billingInfo.setEmail("acd@gmail.com");
		billingInfo.setSellerResNbr("1234567");
		return billingInfo;
	}

	/**
	 * Gets the tkt list.
	 *
	 * @param productsAssignAccounts
	 *            the products assign accounts
	 * @return the tkt list
	 */
	private ArrayList<TicketTO> getTktList(boolean productsAssignAccounts) {
		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		TicketTO ticketTO = new TicketTO();
		ticketTO.setTktItem(BigInteger.valueOf(1));
		ticketTO.setProdQty(BigInteger.valueOf(1));
		ticketTO.setProdCode("ABC");
		ticketTO.setProdPriceQuoteToken("prodPriceQuoteToken");
		ticketTO.setExtrnlPrcd("F");
		if (productsAssignAccounts) {
			ArrayList<TktAssignmentTO> ticketAssignmetList = new ArrayList<>();
			TicketTO ticket = new TicketTO();
			TicketTO.TktAssignmentTO ticketAssignmets = ticket.new TktAssignmentTO();
			ticketAssignmets.setProdQty(BigInteger.valueOf(1));
			ticketAssignmets.setAccountItem(BigInteger.valueOf(12));
			ticketAssignmetList.add(ticketAssignmets);
			ticketTO.setTicketAssignmets(ticketAssignmetList);
		}

		ticketTO.setTktValidityValidStart(new GregorianCalendar());
		ticketTO.setTktValidityValidEnd(new GregorianCalendar());
		ArrayList<DemographicsTO> tktDemoList = new ArrayList<DemographicsTO>();
		DemographicsTO demographicsTO = new DemographicsTO();
		demographicsTO.setFirstName("ABC");
		demographicsTO.setLastName("DEF");
		demographicsTO.setDateOfBirth(new GregorianCalendar());
		demographicsTO.setGender(GenderType.MALE);
		demographicsTO.setAddr1("ABC");
		demographicsTO.setAddr2("DEF");
		demographicsTO.setCity("ABC");
		demographicsTO.setState("DEF");
		demographicsTO.setZip("ABC");
		demographicsTO.setCountry("ADCF");
		demographicsTO.setEmail("abc@gmail.com");
		demographicsTO.setTelephone("123456789");
		demographicsTO.setOptInSolicit(true);
		tktDemoList.add(demographicsTO);
		ticketTO.setTicketDemoList(tktDemoList);
		tktList.add(ticketTO);
		return tktList;
	}

	/**
	 * Test case for transformResponseBody.
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.QUERYTICKET;

		OTManageReservationTO otMngResTO = new OTManageReservationTO();
		otMngResTO.setCommandType("commandType");
		otMngResTO.setReservationCode("ABC");
		OTClientDataTO otClientDataTO = new OTClientDataTO();
		otClientDataTO.setClientUniqueId(1);
		otMngResTO.setClientData(otClientDataTO);
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		otCmdTO.setManageReservationTO(otMngResTO);
		OTTicketInfoTO ottTcket = new OTTicketInfoTO();
		ottTcket.setItemNumCode(new BigInteger("1"));
		ottTcket.setValidityStartDate(new GregorianCalendar());
		ottTcket.setValidityEndDate(new GregorianCalendar());
		ottTcket.setTicket(getOTicket());
		otMngResTO.getTicketInfoList().add(ottTcket);

		ArrayList<OTPaymentTO> otPmtList = new ArrayList<OTPaymentTO>();
		OTPaymentTO otPaymentTO = new OTPaymentTO();
		otPaymentTO.setPayType(OTPaymentTO.PaymentType.INSTALLMENT);
		OTInstallmentTO otinstall = new OTInstallmentTO();
		otinstall.setContractId("123");
		otPaymentTO.setInstallment(otinstall);
		otPmtList.add(otPaymentTO);

		otMngResTO.setPaymentInfoList(otPmtList);

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		DTIMockUtil.mockGetProductCodeFromTktNbr();
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			WDWReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test apply wdw associate media to account rules.
	 */
	// @Test TODO Somesh mocking issue to be fixed after merge
	public void testApplyWDWReservationRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		ReservationRequestTO reqTO = getReservationRequestTo();

		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setDayClass("SHIP");
		dbProductTO.setPdtCode("SHIP");
		dbProdList.add(dbProductTO);
		dtiTxn.setDbProdList(dbProdList);
		dtiTxn.getRequest().setCommandBody(reqTO);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);

		HashMap<CmdAttrCodeType, AttributeTO> attribMap = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();
		dtiTxn.setAttributeTOMap(attribMap);
		dtiTxn.setRework(false);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		ReservationRequestTO reqstTO = getReservationRequestTo();
		ClientDataTO dtiCliDataTO = reqstTO.getClientData();
		dtiCliDataTO.setDemoLanguage(null);
		reqstTO.setClientData(dtiCliDataTO);
		dtiTxn.getRequest().setCommandBody(reqstTO);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO requestTO = getReservationRequestTo();
		requestTO.setReservation(null);
		dtiTxn.getRequest().setCommandBody(requestTO);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO request = getReservationRequestTo();
		ReservationTO reservationTO = request.getReservation();
		reservationTO.setResPickupArea(null);
		request.setReservation(reservationTO);
		dtiTxn.getRequest().setCommandBody(request);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO requestObj = getReservationRequestTo();
		ReservationTO reservationObj = requestObj.getReservation();
		reservationObj.setResSalesType(null);
		requestObj.setReservation(reservationObj);
		dtiTxn.getRequest().setCommandBody(requestObj);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO requestObject = getReservationRequestTo();
		dtiTxn.getRequest().setCommandBody(requestObject);
		DTIMockUtil.mockGetTPCommandLookupForException();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.INVALID_AREA);
		}

		ReservationRequestTO requestOb = getReservationRequestTo();
		ClientDataTO clientDataTO = requestOb.getClientData();
		clientDataTO.setClientId("ABC");
		requestOb.setClientData(clientDataTO);
		dtiTxn.getRequest().setCommandBody(requestOb);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO reqestOb = getReservationRequestTo();
		reqestOb.setClientData(null);
		dtiTxn.getRequest().setCommandBody(reqestOb);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO requestObjct = getReservationRequestTo();
		ClientDataTO clientDataObj = requestObjct.getClientData();
		clientDataObj.setClientId(null);
		requestObjct.setClientData(clientDataObj);
		dtiTxn.getRequest().setCommandBody(requestObjct);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO reqstObject = getReservationRequestTo();
		ClientDataTO clintDataObj = reqstObject.getClientData();
		clintDataObj.setClientId("0");
		reqstObject.setClientData(clintDataObj);
		dtiTxn.getRequest().setCommandBody(reqstObject);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO rqstObject = getReservationRequestTo();
		ClientDataTO clientdataObj = rqstObject.getClientData();
		clientdataObj.setClientType("Private");
		rqstObject.setClientData(clientdataObj);
		dtiTxn.getRequest().setCommandBody(rqstObject);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO resvObject = getReservationRequestTo();
		ClientDataTO clientObj = resvObject.getClientData();
		clientObj.setBillingInfo(new DemographicsTO());
		clientObj.setShippingInfo(new DemographicsTO());
		resvObject.setClientData(clientObj);
		dtiTxn.getRequest().setCommandBody(resvObject);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

		ReservationRequestTO resvationObject = getReservationRequestTo();
		ArrayList<TicketTO> ticketArrayList = new ArrayList<TicketTO>();

		for (int i = 0; i < 25; i++) {
			TicketTO ticketTO = new TicketTO();
			ticketTO.setProdCode("SHIP1");
			ticketArrayList.add(ticketTO);
		}
		resvationObject.setTktList(ticketArrayList);
		dtiTxn.getRequest().setCommandBody(resvationObject);
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		try {
			WDWReservationRules.applyWDWReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}

	}

	/**
	 * Gets the reservation request to.
	 *
	 * @return the reservation request to
	 */
	private ReservationRequestTO getReservationRequestTo() {
		ReservationRequestTO reqTO = new ReservationRequestTO();
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		TicketTO ticketTO = new TicketTO();
		ticketTO.setProdCode("SHIP");
		tktListTO.add(ticketTO);
		reqTO.setTktList(tktListTO);
		ClientDataTO clientData = new ClientDataTO();
		clientData.setClientId("123");
		clientData.setClientType("Major");
		clientData.setDemoLanguage("ABC");
		clientData.setClientType("Major");
		reqTO.setClientData(clientData);
		ReservationTO reservationTO = new ReservationTO();
		reservationTO.setResCode("abc");
		reservationTO.setResSalesType("Presale");
		reservationTO.setResPickupArea("ABC");

		reqTO.setReservation(reservationTO);
		return reqTO;
	}

}
