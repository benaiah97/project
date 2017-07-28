package pvt.disney.dti.gateway.rules.hkd;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.ShowTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.rules.wdw.WDWReservationRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for HKDReservationRules.
 *
 * @author ARORT002
 */
public class HKDReservationRulesTestCase extends CommonTestUtils {

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
		String xmlString = null;
		ReservationRequestTO dtiResReq = new ReservationRequestTO();
		ClientDataTO dtiClientData = new ClientDataTO();
		DemographicsTO dtiBillInfo = new DemographicsTO();
		DemographicsTO dtiShipInfo = new DemographicsTO();
		dtiShipInfo.setFirstNameChinese("ABCD");
		dtiBillInfo.setFirstName("ASBC");
		dtiBillInfo.setFirstNameChinese("ASDAD");
		dtiBillInfo.setLastName("ASADA");
		dtiBillInfo.setName("Asad");
		dtiBillInfo.setState("ASAS");
		dtiBillInfo.setCountry("ASASS");
		dtiBillInfo.setEmail("acb@gmail.com");
		dtiBillInfo.setTelephone("12121212");
		dtiBillInfo.setSellerResNbr("12345");
		dtiClientData.setBillingInfo(dtiBillInfo);
		dtiClientData.setShippingInfo(dtiShipInfo);
		dtiClientData.setClientId("123");
		dtiClientData.setClientDeliveryInstructions("ABC");
		dtiClientData.setClientFulfillmentMethod("ABC");
		dtiClientData.setTimeRequirement("ABC");
		DemographicsTO dlvAddr = new DemographicsTO();
		dtiClientData.setDeliveryInfo(dlvAddr);
		dlvAddr.setFirstName("ABCD");
		dlvAddr.setLastName("DVFG");
		dlvAddr.setName("ACVF");
		dlvAddr.setAddr1("ACDFA");
		dlvAddr.setAddr2("ADASA");
		dlvAddr.setCity("ASAS");
		dlvAddr.setState("ASAS");
		dlvAddr.setZip("ASA");
		dlvAddr.setCountry("ASAS");
		dlvAddr.setTelephone("121313131313");
		dlvAddr.setEmail("abc@gmail.com");
		
		dtiResReq.setClientData(dtiClientData);
		dtiClientData.setGroupValidityValidStart(new GregorianCalendar());
		dtiClientData.setGroupValidityValidEnd(new GregorianCalendar());
		AgencyTO dtiAgencyTO = new AgencyTO();
		dtiAgencyTO.setIATA("ABC");
		dtiResReq.setAgency(dtiAgencyTO);
		dtiResReq.setEligibilityMember("ABC");
		dtiResReq.setTaxExemptCode("ABC");
		dtiResReq.setEligibilityGroup("DEF");
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("abc");
		reservation.setResSalesType("ManualMailOrder");
		dtiResReq.setReservation(reservation);
		
		ArrayList<TicketTO> dtiTktList = new ArrayList<TicketTO>();
		TicketTO ticketTO = new TicketTO();
		ticketTO.setTktItem(BigInteger.valueOf(1));
		ticketTO.setProdCode("ABC");
		ticketTO.setProdQty(BigInteger.valueOf(1));
		
		ArrayList<DemographicsTO> tktDemoList = new ArrayList<DemographicsTO>();
		DemographicsTO demographicsTO = new DemographicsTO();
		
		demographicsTO.setLastName("ASD");
		demographicsTO.setFirstName("ASD");
		demographicsTO.setGender(GenderType.MALE);
		demographicsTO.setEmail("abc@gmail.com");
		demographicsTO.setDateOfBirth(new GregorianCalendar());
		demographicsTO.setCellPhone("1234567891");
		demographicsTO.setTelephone("1234567891");
		demographicsTO.setSellerRef("asf");
		tktDemoList.add(demographicsTO);
		ticketTO.setTicketDemoList(tktDemoList);
		dtiTktList.add(ticketTO);
		dtiResReq.setTktList(dtiTktList);
		ExtTxnIdentifierTO extTxnIdentifier = new ExtTxnIdentifierTO();
		extTxnIdentifier.setTxnIdentifier("ABC");
		extTxnIdentifier.setIsSHA1Encrypted(true);
		dtiResReq.setExtTxnIdentifier(extTxnIdentifier);
		
		ShowTO show = new ShowTO();
		show.setShowGroup("ABC");
		show.setShowPerformance("ASDF");
		show.setShowQuota("ASD");
		dtiResReq.setShow(show);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		
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
		
		ArrayList<DBProductTO> dbProductTOList = new ArrayList<DBProductTO>();
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setPdtCode("ABC");
		dbProductTO.setMappedProviderTktNbr(BigInteger.valueOf(123));
		dbProductTO.setValidityDateInfoRequired(true);

		dbProductTOList.add(dbProductTO);
		dtiTxn.setDbProdList(dbProductTOList);
		DTIMockUtil.mockGetEligibilityAssocId();
		try {
			xmlString = HKDReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		ReservationTO reservationTO = new ReservationTO();
		reservationTO.setResCode("abc");
		reservationTO.setResSalesType("abc");
		dtiResReq.setReservation(reservationTO);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		
		try {
			xmlString = HKDReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test case for transformResponseBody.
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		HkdOTManageReservationTO otMngResTO = new HkdOTManageReservationTO();
		HkdOTTicketInfoTO otTicketInfo = new HkdOTTicketInfoTO();
		otTicketInfo.setItem(BigInteger.valueOf(1));
		HkdOTTicketTO otTicketTO = new HkdOTTicketTO();
		otTicketTO.setBarCode("ABC");
		otTicketInfo.setTicket(otTicketTO);
		otTicketInfo.setValidityStartDate(new GregorianCalendar());
		otTicketInfo.setValidityEndDate(new GregorianCalendar());
		otMngResTO.getTicketInfoList().add(otTicketInfo);

		ArrayList<HkdOTProductTO> otProductList = new ArrayList<HkdOTProductTO>();
		HkdOTProductTO hkdOTProductTO = new HkdOTProductTO();
		hkdOTProductTO.setItem(BigInteger.valueOf(1));
		hkdOTProductTO.setItemAlphaCode("123");
		hkdOTProductTO.setQuantity(BigInteger.valueOf(1));
		hkdOTProductTO.setPrice(BigDecimal.valueOf(1));
		hkdOTProductTO.setTax(BigDecimal.valueOf(1));
		hkdOTProductTO.setDescription("asasas");
		otProductList.add(hkdOTProductTO);
		otMngResTO.setProductList(otProductList);

		ArrayList<HkdOTPaymentTO> otPmtList = new ArrayList<HkdOTPaymentTO>();
		HkdOTPaymentTO hkdOTPaymentTO = new HkdOTPaymentTO();
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.INSTALLMENT);
		HkdOTInstallmentTO installment = new HkdOTInstallmentTO();
		installment.setContractId("123");
		hkdOTPaymentTO.setInstallment(installment);
		otPmtList.add(hkdOTPaymentTO);
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setReservationCode("ABC");
		HkdOTClientDataTO otClientDataTO = new HkdOTClientDataTO();
		otClientDataTO.setClientUniqueId(123);
		otMngResTO.setClientData(otClientDataTO);
		HkdOTCommandTO otCmdTO = new HkdOTCommandTO(
				pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO.OTTransactionType.MANAGERESERVATION);
		otCmdTO.setManageReservationTO(otMngResTO);

		DTIMockUtil.processMockprepareAndExecuteSql();

		DTIResponseTO dtiRespTO = new DTIResponseTO();
		try {
			HKDReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test apply hkd query reservation rules.
	 */
	//@Test TODO Somesh mocking issue to be fixed after merge
	public void testApplyHKDQueryReservationRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		ReservationRequestTO queryResReqTO = new ReservationRequestTO();
		ClientDataTO clientTO = new ClientDataTO();
		clientTO.setClientId("123");
		clientTO.setClientType("Major");
		clientTO.setDemoLanguage("ABD");
		
		queryResReqTO.setClientData(clientTO);
		ReservationTO dtiResTO = new ReservationTO();
		dtiResTO.setResPickupArea("ASD");
		dtiResTO.setResSalesType("ASD");
		queryResReqTO.setReservation(dtiResTO);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);

		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		DTIMockUtil.mockGetTransidRescodeFromDB();
		dtiTxn.setRework(true);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		ReservationTO dtiResvtTO = queryResReqTO.getReservation();
		dtiResvtTO.setResPickupArea(null);
		queryResReqTO.setReservation(dtiResvtTO);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		
		ReservationRequestTO quryResReqTO = new ReservationRequestTO();
		ClientDataTO clientDataTO = new ClientDataTO();
		clientDataTO.setClientId("123");
		clientDataTO.setClientType("Major");
		clientDataTO.setDemoLanguage("ABD");
		
		quryResReqTO.setClientData(clientDataTO);
		ArrayList<TicketTO> tktProdList = new ArrayList<TicketTO>();
		for(int i =0;i<25;i++){
			TicketTO ticketTO = new TicketTO();
			tktProdList.add(ticketTO);
		}
		quryResReqTO.setTktList(tktProdList);
		ReservationTO dtiReTO = new ReservationTO();
		dtiReTO.setResPickupArea("ASD");
		dtiReTO.setResSalesType("ASD");
		quryResReqTO.setReservation(dtiReTO);
		dtiTxn.getRequest().setCommandBody(quryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ReservationTO dtiResvtionTO = queryResReqTO.getReservation();
		dtiResvtionTO.setResPickupArea("ASD");
		dtiResvtionTO.setResSalesType(null);
		queryResReqTO.setReservation(dtiResvtionTO);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clientObj = queryResReqTO.getClientData();
		clientObj.setDemoLanguage(null);
		queryResReqTO.setClientData(clientObj);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clientObjct = queryResReqTO.getClientData();
		clientObjct.setDemoLanguage("ASD");
		queryResReqTO.setClientData(clientObjct);
		queryResReqTO.setReservation(null);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clientDataTo = new ClientDataTO();
		clientDataTo.setClientId("0");
		clientDataTo.setClientType("Major");
		queryResReqTO.setClientData(clientDataTo);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clintDataTo = new ClientDataTO();
		clintDataTo.setClientId("123");
		clintDataTo.setClientType("Major");
		DemographicsTO billingInfo = new DemographicsTO();
		clintDataTo.setBillingInfo(billingInfo);
		DemographicsTO shippingInfo = new DemographicsTO();
		clintDataTo.setShippingInfo(shippingInfo);
		clintDataTo.setDemoLanguage("ABC");
		ReservationTO dtiRsTO = new ReservationTO();
		dtiRsTO.setResPickupArea("ASD");
		dtiRsTO.setResSalesType("ASD");
		queryResReqTO.setClientData(clintDataTo);
		queryResReqTO.setReservation(dtiRsTO);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		DTIMockUtil.mockGetTPCommandLookup();
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clientDtaTo = new ClientDataTO();
		clientDtaTo.setClientId("12");
		clientDtaTo.setClientType("Private");
		queryResReqTO.setClientData(clientDtaTo);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		ClientDataTO clientDatTo = new ClientDataTO();
		clientDatTo.setClientId(null);
		clientDatTo.setClientType("Major");
		queryResReqTO.setClientData(clientDatTo);
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		
		ReservationRequestTO queryResReq = new ReservationRequestTO();
		ClientDataTO client = new ClientDataTO();
		client.setClientId("ABC");
		client.setClientType("Major");
		queryResReq.setClientData(client);
		client.setDemoLanguage("ABD");
		client.setClientType("asd");
		ReservationTO dtiResvTO = new ReservationTO();
		dtiResvTO.setResPickupArea("ASD");
		dtiResvTO.setResSalesType("ASD");
		queryResReq.setReservation(dtiResvTO);
		dtiTxn.getRequest().setCommandBody(queryResReq);

		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		DTIMockUtil.mockGetTransidRescodeFromDB();
		dtiTxn.setRework(true);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		
		queryResReq.setClientData(null);
		dtiTxn.getRequest().setCommandBody(queryResReq);
		try {
			HKDReservationRules.applyHKDReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
		}
		

	}
	
	@Test
	public void testInitHKDReservationRules(){
		Properties prop = new Properties();
		HKDReservationRules.initHKDReservationRules(prop);
	}

}
