package pvt.disney.dti.gateway.rules.hkd;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for HKDQueryReservationRules.
 *
 * @author ARORT002
 */
public class HKDQueryReservationRulesTestCase extends CommonTestUtils {

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
		QueryReservationRequestTO reservationRequestTO = new QueryReservationRequestTO();
		reservationRequestTO.setResCode("123");
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = HKDQueryReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		reservationRequestTO.setResCode(null);
		reservationRequestTO.setResNumber("456");
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		try {
			xmlString = HKDQueryReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Gets the reservation request to.
	 *
	 * @return the reservation request to
	 */
	private QueryReservationRequestTO getReservationRequestTO() {
		QueryReservationRequestTO reservationRequestTO = new QueryReservationRequestTO();
		ExtTxnIdentifierTO extTxnIdentifierTO = new ExtTxnIdentifierTO();
		extTxnIdentifierTO.setTxnIdentifier("asd");
		extTxnIdentifierTO.setIsSHA1Encrypted(true);
		AgencyTO agencyTO = new AgencyTO();
		agencyTO.setIATA("IAIA");
		agencyTO.setAgent("asd");

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

		ClientDataTO clientData = new ClientDataTO();
		clientData.setClientId("123");
		DemographicsTO billingInfo = getDemographicsTO();
		DemographicsTO shippingInfo = getDemographicsTO();
		clientData.setBillingInfo(billingInfo);
		clientData.setShippingInfo(shippingInfo);
		clientData.setClientCategory("abc");
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
			HKDQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test apply hkd query reservation rules.
	 */
	@Test
	public void testApplyHKDQueryReservationRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		QueryReservationRequestTO queryResReqTO = new QueryReservationRequestTO();
		queryResReqTO.setPayloadID("123");
		dtiTxn.getRequest().setCommandBody(queryResReqTO);

		DTIMockUtil.mockGetTransidRescodeFromDB();
		try {
			HKDQueryReservationRules.applyHKDQueryReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

}
