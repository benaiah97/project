package pvt.disney.dti.gateway.rules.hkd;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO.TPLookupType;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTCreditCardTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicInfo;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for HKDBusinessRules
 */
public class HKDBusinessRulesTestCase extends CommonTestUtils {

	private final static String HKDXMLPATH = "/xml/hkd/";

	@Before
	public void setUp() {
		setMockProperty();
	}

	/**
	 * JUnit for changeToHKDProviderFormat
	 */
	@Test
	public void testChangeToHKDProviderFormat() {
		/* scenario :1 TransactionType is RESERVATION */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		ReservationRequestTO reservationRequestTO = new ReservationRequestTO();
		ReservationTO reservation = new ReservationTO();
		reservation.setResSalesType("ManualMailOrder");
		reservationRequestTO.setReservation(reservation);
		ClientDataTO clientData = new ClientDataTO();
		clientData.setClientId("2365");
		reservationRequestTO.setClientData(clientData);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		dtiTxn.setTpRefNum(222);
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap = new HashMap<>();
		AttributeTO attributeTO = new AttributeTO();
		attributeTO.setAttrValue("12354");
		attributeTOMap
				.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		TPLookupTO lookupTO = new TPLookupTO();
		lookupTO.setLookupType(TPLookupType.SALES_TYPE);
		lookupTO.setLookupValue("123654");
		TPLookupTO lookup = new TPLookupTO();
		lookup.setLookupType(TPLookupType.PICKUP_AREA);
		lookup.setLookupValue("123654");
		TPLookupTO lookuptype = new TPLookupTO();
		lookuptype.setLookupType(TPLookupType.PICKUP_TYPE);
		lookuptype.setLookupValue("123654");
		TPLookupTO lookupclient = new TPLookupTO();
		lookupclient.setLookupType(TPLookupType.CLIENT_TYPE);
		lookupclient.setLookupValue("123654");
		TPLookupTO lookupLangu = new TPLookupTO();
		lookupLangu.setLookupType(TPLookupType.LANGUAGE);
		lookupLangu.setLookupValue("123654");
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<>();
		tpLookupTOListIn.add(lookupTO);
		tpLookupTOListIn.add(lookup);
		tpLookupTOListIn.add(lookuptype);
		tpLookupTOListIn.add(lookupclient);
		tpLookupTOListIn.add(lookupLangu);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		String xmlRequest = null;
		try {
			xmlRequest = HKDBusinessRules.changeToHKDProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* scenario :2 TransactionType is QUERYRESERVATION */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYRESERVATION);
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO requestTO = new QueryReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(requestTO);
		dtiTxn.setTpRefNum(222);
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		attributeTOMap = new HashMap<>();
		attributeTO = new AttributeTO();
		attributeTO.setAttrValue("12354");
		attributeTOMap
				.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		xmlRequest = null;
		try {
			xmlRequest = HKDBusinessRules.changeToHKDProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* scenario :3 TransactionType is UNDEFINED */
		dtiTxn = new DTITransactionTO(TransactionType.UNDEFINED);
		try {
			HKDBusinessRules.changeToHKDProviderFormat(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Invalid HKD transaction type sent to DTI Gateway.  Unsupported.",
					dtie.getLogMessage());
		}
	}

	/**
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 *             JUnit for changeHKDProviderFormatToDti
	 */
	@Test
	public void testChangeHKDProviderFormatToDti() throws URISyntaxException,
			FileNotFoundException {
		/* scenario :1 TransactionType is RESERVATION */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RESERVATION);
		String xmlResponse = "";
		createCommonRequest(dtiTxn);
		DTITransactionTO dtiTransactionTO = null;
		/* scenario :2 TransactionType is QUERYTICKET */
		try {
			InputStream fileName = this.getClass().getResourceAsStream(
					HKDXMLPATH + "HKD_Reservation.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTxn.setTpRefNum(222);
			EntityTO entityTO = new EntityTO();
			entityTO.setEntityId(22);
			dtiTxn.setEntityTO(entityTO);
			DTIMockUtil.processMockprepareAndExecuteSql();
			dtiTransactionTO = HKDBusinessRules.changeHKDProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* scenario :3 TransactionType is QUERYRESERVATION */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYRESERVATION);
		createCommonRequest(dtiTxn);
		try {
			dtiTxn.setTpRefNum(222);
			EntityTO entityTO = new EntityTO();
			entityTO.setEntityId(22);
			dtiTxn.setEntityTO(entityTO);
			DTIRequestTO request = new DTIRequestTO();
			PayloadHeaderTO header = new PayloadHeaderTO();
			request.setPayloadHeader(header);
			DTIMockUtil.processMockprepareAndExecuteSql();
			dtiTransactionTO = null;
			dtiTransactionTO = HKDBusinessRules.changeHKDProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for createOTPaymentList
	 */
	@Test
	public void testCreateOTPaymentList() {
		ArrayList<HkdOTPaymentTO> otPaymentList = new ArrayList<>();
		ArrayList<PaymentTO> dtiPayList = new ArrayList<>();
		EntityTO entityTO = new EntityTO();
		entityTO.setDefPymtId(3);
		entityTO.setDefPymtData("defPymtData");
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		entityTO = new EntityTO();
		entityTO.setDefPymtId(2);
		PaymentTO paymentTO = new PaymentTO();
		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setPosTermID("22");
		creditCard.setExtnlDevSerial("123654");
		/* scenario :1 CreditCardType is CCSWIPE */
		creditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCSWIPE);
		paymentTO.setCreditCard(creditCard);
		paymentTO.setPayItem(new BigInteger("2"));
		/* scenario :2 PaymentType is CREDITCARD */
		paymentTO.setPayType(PaymentTO.PaymentType.CREDITCARD);
		dtiPayList.add(paymentTO);
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario :3 CreditCardType is CCMANUAL */
		creditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		creditCard.setCcExpiration("1010");
		creditCard.setCcVV("23645");
		creditCard.setCcCAVV("4569");
		creditCard.setPreApprovedCC(true);
		creditCard.setCcAuthCode("147852");
		creditCard.setCcSha1("236");
		creditCard.setCcSubCode("852");
		creditCard.setCcEcommerce("222");
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario :4 PaymentType is GIFTCARD */
		paymentTO.setPayType(PaymentTO.PaymentType.GIFTCARD);
		GiftCardTO giftCard = new GiftCardTO();
		giftCard.setGcTrack2("2585");
		giftCard.setGcTrack1("2585");
		paymentTO.setGiftCard(giftCard);
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario :5 PaymentType is VOUCHER */
		paymentTO.setPayType(PaymentTO.PaymentType.VOUCHER);
		VoucherTO voucher = new VoucherTO();
		voucher.setMainCode("mainCode");
		paymentTO.setVoucher(voucher);
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario :6 PaymentType is INSTALLMENT */
		paymentTO.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		InstallmentTO installment = new InstallmentTO();
		InstallmentCreditCardTO creditCardTO = new InstallmentCreditCardTO();
		creditCardTO.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		creditCardTO.setCcExpiration("1010");
		installment.setCreditCard(creditCardTO);
		paymentTO.setInstallment(installment);
		paymentTO.setPayAmount(new BigDecimal("55"));
		HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
	}

	/**
	 * JUnit for transformTicketDemoData
	 */
	@Test
	public void testTransformTicketDemoData() {
		ArrayList<DemographicsTO> tktDemoList = new ArrayList<>();
		tktDemoList.add(getBillingInfo());
		HkdOTDemographicInfo otDemoInfo = new HkdOTDemographicInfo();
		HKDBusinessRules.transformTicketDemoData(tktDemoList, otDemoInfo);
	}

	/**
	 * JUnit for getSiteNumberProperty
	 */
	@Test
	public void testGetSiteNumberProperty() {
		try {
			HKDBusinessRules.getSiteNumberProperty();
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformOTHeader
	 */
	@Test
	public void testTransformOTHeader() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		dtiTxn.setTpRefNum(22);
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap = new HashMap<>();
		AttributeTO attributeTO = new AttributeTO();
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, null);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		String requestType = "requestType";
		String requestSubType = "requestSubType";
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.assertEquals("Operating Area not set up in DTI database.",
					dtie.getLogMessage());
		}
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, null);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.assertEquals("User ID not set up in DTI database.",
					dtie.getLogMessage());
		}
		attributeTO = new AttributeTO();
		attributeTO.setAttrValue("attrValue");

		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO);

		dtiTxn.setAttributeTOMap(attributeTOMap);
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.assertEquals("User ID not a parsable integer: attrValue",
					dtie.getLogMessage());
		}
		attributeTO = new AttributeTO();
		attributeTO.setAttrValue("3");

		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, null);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.assertEquals("Password not set up in DTI database.",
					dtie.getLogMessage());
		}
		attributeTO = new AttributeTO();
		attributeTO.setAttrValue("2");
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		attributeTO = new AttributeTO();
		attributeTO.setAttrValue("attrValue");
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		try {
			HKDBusinessRules.transformOTHeader(dtiTxn, requestType,
					requestSubType);
		} catch (DTIException dtie) {
			Assert.assertEquals("Password not a parsable integer: attrValue",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for setDTIPaymentList
	 */
	@Test
	public void testSetDTIPaymentList() {
		ArrayList<PaymentTO> dtiPmtList = new ArrayList<>();
		ArrayList<HkdOTPaymentTO> otPmtList = new ArrayList<>();
		HkdOTPaymentTO hkdOTPaymentTO = new HkdOTPaymentTO();
		/* scenario :1 PaymentType is VOUCHER */
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.VOUCHER);
		otPmtList.add(hkdOTPaymentTO);
		HKDBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);
		/* scenario :2 PaymentType is INSTALLMENT */
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.INSTALLMENT);
		HkdOTInstallmentTO installment = new HkdOTInstallmentTO();
		installment.setContractId("258");
		hkdOTPaymentTO.setInstallment(installment);
		HKDBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);
		/* scenario :3 PaymentType is CREDITCARD */
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.CREDITCARD);
		HkdOTCreditCardTO creditCard = new HkdOTCreditCardTO();
		creditCard.setGiftCardIndicator(true);
		creditCard.setAuthNumber("147");
		creditCard.setCcNumber("55");
		creditCard.setRemainingBalance(new BigDecimal("22"));
		hkdOTPaymentTO.setCreditCard(creditCard);
		HKDBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);
	}
}
