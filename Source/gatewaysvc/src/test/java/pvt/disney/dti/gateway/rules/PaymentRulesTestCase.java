package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO.GiftCardType;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO.TPLookupType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

// TODO: Auto-generated Javadoc
/**
 * Tests the payment rules.
 * 
 * @author lewit019
 *
 */
public class PaymentRulesTestCase extends CommonBusinessTest {
	private static final int MIN_MANUAL_CCGC_LENGTH = 12;
	private static final int MAX_MANUAL_CCGC_LENGTH = 19;
	private static final int MAXCARDHOLDERNAME = 25;
	/** The maximum CVV length (8). */
	public final static int MAX_WDW_CVV_LENGTH = 8;
	/** The maximum AVS street length (30). */
	public final static int MAX_WDW_AVS_STREET_LENGTH = 30;
	/** The maximum AVS ZIP length (10). */
	public final static int MAX_WDW_AVS_ZIP_LENGTH = 10;
	/** The required expiration field length (4). */
	public final static int WDW_REQUIRED_EXPIRATION_LENGTH = 4;

	/**
	 * Test validate payments on order.
	 */
	@Test
	public final void testValidatePaymentsOnOrder() {
		BigDecimal totalProductCost = new BigDecimal("0.00");
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = null;
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		tktListTO.add(aTicketTO);

		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");
		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1000.00"));
		aPayment01.setCreditCard(creditCard);

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("410.82"));
		aPayment02.setCreditCard(creditCard);
		PaymentTO aPayment03 = new PaymentTO();
		aPayment03.setPayType(PaymentTO.PaymentType.VOUCHER);
		aPayment03.setPayAmount(new BigDecimal("0.01"));
		aPayment03.setCreditCard(creditCard);
		PaymentTO aPayment04 = new PaymentTO();
		aPayment04.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment04.setPayAmount(new BigDecimal("0.01"));
		aPayment04.setCreditCard(creditCard);
		PaymentTO aPayment05 = new PaymentTO();
		aPayment05.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment05.setPayAmount(new BigDecimal("0.00"));
		aPayment05.setCreditCard(creditCard);
		/* Scenario ::1: No payments, no voucher */
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception on Scenario :: 1: No payments, no voucher: "
					+ dtie.toString());
		}

		/* Scenario :: 2: Empty payments, no voucher */
		payListTO = new ArrayList<PaymentTO>();
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception:  Empty payments, no voucher: "
					+ dtie.toString());
		}

		/* Scenario :: 3: Payments less than products */
		payListTO.add(aPayment01);
		payListTO.add(aPayment02);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception on Test 3:  Payments less than products.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}

		/* Scenario :: 4: Payments equal to products */
		payListTO.add(aPayment03);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception on Scenario :: 4:  Payments equal to products: "
					+ dtie.toString());
		}

		/* Scenario :: 5: Payments greater than products */
		payListTO.add(aPayment04);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception on Scenario :: 5:  Payments greater than products.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}

		/* Scenario :: 6: Payments amount = 0 */
		payListTO.add(aPayment05);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception on Scenario :: 6:  Payments amount = 0");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}

		assertEquals(payListTO.size() > 0, true);
		assertEquals(tktListTO.size() > 0, true);
		for (TicketTO aTicket : tktListTO) {
			totalProductCost = totalProductCost.add(aTicket.getProdPrice());
		}
		assertEquals(totalProductCost, new BigDecimal("130.11"));

	}

	/**
	 * Test validate payment composition.
	 */
	@Test
	public final void testValidatePaymentComposition() {

		String tpiCode = DTITransactionTO.TPI_CODE_WDW;
		ArrayList<PaymentTO> payListTO = null;

		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");
		creditCard.setPreApprovedCC(true);
		creditCard.setCcAuthCode("ccAuthCode");
		creditCard.setCcSubCode("ccSubCode");
		
		
		CreditCardTO creditCard01 = new CreditCardTO();
		creditCard01.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard01.setCcNbr("123456789256");
		creditCard01.setPreApprovedCC(true);
		/*Removing authCode for cover the exception*/
		//creditCard01.setCcAuthCode("ccAuthCode");
		creditCard01.setCcSubCode("ccSubCode");

		CreditCardTO creditCard02 = new CreditCardTO();
		creditCard02.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard02.setCcNbr("123456789256");
		
		PaymentTO aCreditCard = new PaymentTO();
		aCreditCard.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aCreditCard.setPayAmount(new BigDecimal("50.00"));
		aCreditCard.setCreditCard(creditCard);

		PaymentTO aGiftCard = new PaymentTO();
		aGiftCard.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aGiftCard.setPayAmount(new BigDecimal("50.00"));
		aGiftCard.setCreditCard(creditCard);

		PaymentTO aVoucher = new PaymentTO();
		aVoucher.setPayType(PaymentTO.PaymentType.VOUCHER);
		aVoucher.setPayAmount(new BigDecimal("50.00"));
		
		PaymentTO aCreditCard01 = new PaymentTO();
		aCreditCard01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aCreditCard01.setPayItem(new BigInteger("1"));
		aCreditCard01.setCreditCard(creditCard);
		
		PaymentTO aCreditCard02 = new PaymentTO();
		aCreditCard02.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aCreditCard02.setPayItem(new BigInteger("1"));
		aCreditCard02.setPayAmount(new BigDecimal("50.00"));
		aCreditCard02.setCreditCard(creditCard01);
		
		PaymentTO aCreditCard03 = new PaymentTO();
		aCreditCard03.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aCreditCard03.setPayItem(new BigInteger("1"));
		aCreditCard03.setPayAmount(new BigDecimal("50.00"));
		aCreditCard03.setCreditCard(creditCard02);

		/*Scenario :: 1: No paylist.*/
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception: No paylist. "
					+ dtie.toString());
		}

		/*Scenario :: 2: WDW one credit card*/
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Expected Result",
					"The pre-approved credit card is missing its SHA-1 credit card hash code.",
					dtie.getLogMessage());// ///
		}

		/*Scenario :: 3: WDW two credit cards*/
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Expected Result",
					"The pre-approved credit card is missing its SHA-1 credit card hash code.",
					dtie.getLogMessage());
		}

		/*Scenario :: 4: WDW one gift card, one credit card*/
		payListTO.clear();
		payListTO.add(aGiftCard);
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Expected Result",
					"The pre-approved credit card is missing its SHA-1 credit card hash code.",
					dtie.getLogMessage());
		}

		/*Scenario :: 5: WDW one gift card, two credit cards*/
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
			fail("Expected exception on Test 5: WDW one gift card, two credit cards.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_TYPE)
				Assert.assertEquals(
						"Expected Result",
						"The pre-approved credit card is missing its SHA-1 credit card hash code.",
						dtie.getLogMessage());
		}

		/*Scenario :: 6: WDW two credit cards & voucher*/
		payListTO.clear();
		payListTO.add(aCreditCard);
		payListTO.add(aCreditCard);
		payListTO.add(aVoucher);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Expected Result",
					"The pre-approved credit card is missing its SHA-1 credit card hash code.",
					dtie.getLogMessage());
			// fail("Unexpected exception on Test 6: WDW two credit cards & voucher. "
			// + dtie.toString());
		}

		/*Scenario :: 7: WDW four credit cards*/
		payListTO.clear();
		payListTO.add(aCreditCard);
		payListTO.add(aCreditCard);
		payListTO.add(aCreditCard);
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
			fail("Expected exception on Test 7: WDW four credit cards.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_TYPE)

				Assert.assertEquals(
						"Expected Result",
						"Number of payments provided 4 exceeded NEX01 maximum of 3",
						dtie.getLogMessage());
			// fail("Expected error PAYMENT_TYPE_INVALID on Test 7: WDW four credit cards.");
		}

		/*Scenario :: 8: DLR five credit cards*/
		tpiCode = DTITransactionTO.TPI_CODE_DLR;
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Expected Result",
					"The pre-approved credit card is missing its SHA-1 credit card hash code.",
					dtie.getLogMessage());
		}

		/*Scenario :: 9: DLR six credit cards*/
		payListTO.add(aCreditCard);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
			fail("Expected exception on Test 9: DLR six credit cards.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_TYPE)
				Assert.assertEquals(
						"Expected Result",
						"Number of payments provided 6 exceeded DLR01 maximum of 5",
						dtie.getLogMessage());
			// fail("Expected error PAYMENT_TYPE_INVALID on Test 9: DLR six credit cards.");
		}
		/*Scenario :: 10: Passing Voucher*/
		payListTO.clear();
		payListTO.add(aVoucher);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
			Assert.fail("Expected exception: DLR reservation attempted with the voucher payment type.  Not permitted.");
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PAYMENT_TYPE,
					dtie.getDtiErrorCode());
			assertEquals(
					"DLR reservation attempted with the voucher payment type.  Not permitted.",
					dtie.getLogMessage());
			
		}
		/*Scenario :: 11: DLR five credit cards*/
		tpiCode = DTITransactionTO.TPI_CODE_HKD;
		payListTO.clear();
		payListTO.add(aVoucher);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PAYMENT_AMOUNT,
					dtie.getDtiErrorCode());
			assertEquals(
					"Gift Cards and Credit Cards must have a payment amount (PayItem: "
							+ aCreditCard01.getPayItem().intValue() + ").",
					dtie.getLogMessage());
		}
		/*Scenario :: 12: DLR five credit cards*/
		payListTO.clear();
		payListTO.add(aCreditCard02);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT,
					dtie.getDtiErrorCode());
			assertEquals(
					"The pre-approved credit card is missing its authorization code.",
					dtie.getLogMessage());
		}
		/*Scenario :: 13: When Passing 3 Gift Card*/
		payListTO.clear();
		payListTO.add(aCreditCard03);
		payListTO.add(aCreditCard03);
		payListTO.add(aCreditCard03);
		try {
			PaymentRules.validatePaymentComposition(payListTO, tpiCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PAYMENT_TYPE,
					dtie.getDtiErrorCode());
			assertEquals(
					"Number of credit and gift card payments provided 3 exceeded HKD01 maximum of 2",
					dtie.getLogMessage());
		}


		
	}

	/**
	 * Test validate payments on order.
	 */
	@Test
	public final void testValidatePaymentsOnOrderLargeSums() {

		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("359.99"));
		aTicketTO.setProdQty(new BigInteger("998"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("359.99"));
		aTicketTO.setProdQty(new BigInteger("998"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("359.99"));
		aTicketTO.setProdQty(new BigInteger("998"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("1"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("5.00"));
		tktListTO.add(aTicketTO);

		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");
		// creditCard.setPreApprovedCC(true);
		// creditCard.setCcAuthCode("ccAuthCode");
		// creditCard.setCcSubCode("ccSubCode");

		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1077815.06"));
		aPayment01.setCreditCard(creditCard);
		payListTO.add(aPayment01);

		// Test 1: Large sums
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception :  Large sums: "
					+ dtie.toString());
		}

		return;
	}

	/**
	 * Test validate payments on order min credit card length.
	 */
	@Test
	public final void testValidatePaymentsOnOrderMinCreditCardLength() {
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		tktListTO.add(aTicketTO);

		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("12345678925");
		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1000.00"));
		aPayment01.setCreditCard(creditCard);
		aPayment01.setPayItem(new BigInteger("1"));

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("410.82"));
		aPayment02.setCreditCard(creditCard);
		aPayment02.setPayItem(new BigInteger("2"));

		payListTO.add(aPayment01);
		payListTO.add(aPayment02);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception:  Credit Card Length");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
		}
		return;
	}

	/**
	 * Test validate payments on order min credit card length for gift card.
	 */
	@Test
	public final void testValidatePaymentsOnOrderMinCreditCardLengthForGiftCard() {
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		tktListTO.add(aTicketTO);

		GiftCardTO aGiftCard = new GiftCardTO();
		aGiftCard.setGcNbr("12345678925");

		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1000.00"));
		aPayment01.setPayItem(new BigInteger("1"));
		aPayment01.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment01.setGiftCard(aGiftCard);

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("410.82"));
		aPayment02.setGiftCard(aGiftCard);
		aPayment02.setPayItem(new BigInteger("2"));
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);

		payListTO.add(aPayment01);
		payListTO.add(aPayment02);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception :  Gift Card Length");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
		}
		return;
	}

	/**
	 * Test validate payments on order non numeic gift card.
	 */
	@Test
	public final void testValidatePaymentsOnOrderNonNumeicGiftCard() {
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		tktListTO.add(aTicketTO);

		GiftCardTO aGiftCard = new GiftCardTO();
		aGiftCard.setGcNbr("ABCDEFGHIJKLM");

		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1000.00"));
		aPayment01.setPayItem(new BigInteger("1"));
		aPayment01.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment01.setGiftCard(aGiftCard);

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("410.82"));
		aPayment02.setGiftCard(aGiftCard);
		aPayment02.setPayItem(new BigInteger("2"));
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);

		payListTO.add(aPayment01);
		payListTO.add(aPayment02);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception :  Gift Card Length");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
		}
		return;
	}

	/**
	 * Test validate payments on order for installment.
	 */
	@Test
	public final void testValidatePaymentsOnOrderForInstallment() {
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		tktListTO.add(aTicketTO);

		InstallmentCreditCardTO aCreditCard = new InstallmentCreditCardTO();
		aCreditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		aCreditCard.setCcNbr("12345678912");

		InstallmentTO installTO = new InstallmentTO();
		installTO.setCreditCard(aCreditCard);

		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("1000.00"));
		aPayment01.setPayItem(new BigInteger("1"));
		aPayment01.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		aPayment01.setInstallment(installTO);

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("410.82"));
		aPayment02.setPayItem(new BigInteger("2"));
		aPayment02.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		aPayment02.setInstallment(installTO);

		payListTO.add(aPayment01);
		payListTO.add(aPayment02);
		try {
			PaymentRules
					.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
			Assert.fail("Expected exception :  Installment");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
		}
		return;
	}

	/**
	 * Test validate payments present null pay list.
	 */
	@Test
	public void testValidatePaymentsPresentNullPayList() {
		ArrayList<PaymentTO> payListTO = null;
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");
		try {
			PaymentRules.validatePaymentsPresent(payListTO, entityTO);
			Assert.fail("Expected exception:  ValidatePaymentsPresent");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "On this transaction type, a payment type is required, either default or expressly specified.";
			assertEquals(errorMsg, dtie.getLogMessage());
		}
		return;
	}

	/**
	 * Test validate payments present def pyment details.
	 */
	@Test
	public void testValidatePaymentsPresentDefPymentDetails() {
		ArrayList<PaymentTO> payListTO = null;
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");
		entityTO.setDefPymtData("defPymtData");
		try {
			PaymentRules.validatePaymentsPresent(payListTO, entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "On this transaction type, a payment type is required, either default or expressly specified.";
			assertEquals(errorMsg, dtie.getLogMessage());
		}
		return;
	}

	/**
	 * Test validate payments present null pay list.
	 */
	@Test
	public void testValidateReturnFormOfPaymentNullDefPymntDetail() {
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");
		try {
			PaymentRules.validateReturnFormOfPayment(payListTO, entityTO);
			Assert.fail("Expected exception:  ValidateReturnFormOfPaymentNullDefPymntDetail");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "Void transaction has no return form of payment, explicit or default.";
			assertEquals(errorMsg, dtie.getLogMessage());
		}
		return;
	}

	/**
	 * Test validate payments present null pay list.
	 */
	@Test
	public void testValidateReturnFormOfPayment() {
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		PaymentTO paymentTO = new PaymentTO();
		payListTO.add(paymentTO);
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");
		try {
			PaymentRules.validateReturnFormOfPayment(payListTO, entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "Void transaction has no return form of payment, explicit or default.";
			assertEquals(errorMsg, dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for ValidatePaymentsOnUpgrade
	 */
	@Test
	public void testValidatePaymentsOnUpgrade() {
		BigDecimal totalProductCost = new BigDecimal("0.00");
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<PaymentTO> payListTO = null;
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityCode("EntityCode");

		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("IMABARCODE");
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setProdPrice(new BigDecimal("50.01"));
		aTicketTO.setUpgrdPrice(new BigDecimal("25.01"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("17"));
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setProdPrice(new BigDecimal("79.99"));
		aTicketTO.setUpgrdPrice(new BigDecimal("70.98"));
		tktListTO.add(aTicketTO);

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		aTicketTO.setProdCode("CCC03");
		aTicketTO.setProdPrice(new BigDecimal("0.11"));
		aTicketTO.setUpgrdPrice(new BigDecimal("0.07"));
		tktListTO.add(aTicketTO);

		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");

		CreditCardTO creditCard1 = new CreditCardTO();
		creditCard1.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard1.setCcNbr("12345678925");

		GiftCardTO aGiftCard = new GiftCardTO();
		aGiftCard.setGcNbr("12345678925");

		GiftCardTO aGiftCard1 = new GiftCardTO();
		aGiftCard1.setGcNbr("a23456789256");

		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("25.01"));
		aPayment01.setCreditCard(creditCard);

		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment02.setPayAmount(new BigDecimal("70.98"));
		aPayment02.setCreditCard(creditCard);
		PaymentTO aPayment03 = new PaymentTO();
		aPayment03.setPayType(PaymentTO.PaymentType.VOUCHER);
		aPayment03.setPayAmount(new BigDecimal("0.07"));
		aPayment03.setCreditCard(creditCard);
		PaymentTO aPayment04 = new PaymentTO();
		aPayment04.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment04.setPayAmount(new BigDecimal("0.01"));
		aPayment04.setPayItem(new BigInteger("1"));
		aPayment04.setCreditCard(creditCard1);
		PaymentTO aPayment05 = new PaymentTO();
		aPayment05.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment05.setPayAmount(new BigDecimal("0.00"));
		aPayment05.setCreditCard(creditCard);

		PaymentTO aPayment06 = new PaymentTO();
		aPayment06.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment06.setPayAmount(new BigDecimal("70.98"));
		aPayment06.setPayItem(new BigInteger("1"));
		aPayment06.setGiftCard(aGiftCard);

		PaymentTO aPayment07 = new PaymentTO();
		aPayment07.setPayType(PaymentTO.PaymentType.GIFTCARD);
		aPayment07.setPayAmount(new BigDecimal("70.98"));
		aPayment07.setPayItem(new BigInteger("1"));
		aPayment07.setGiftCard(aGiftCard1);
		/* Scenario ::1: No payments, no voucher */
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
		}
		/* Scenario ::2: When payment is less then the product */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment01);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}
		/* Scenario ::3: When payment is equal to the product */
		payListTO.add(aPayment02);
		payListTO.add(aPayment03);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception ::" + dtie.getLogMessage());
		}
		/* Scenario :: 4 when credit card length is less than 11 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment04);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorString = "Invalid credit card account number length on PayItem "
					+ aPayment04.getPayItem().toString()
					+ " expected to be between "
					+ MIN_MANUAL_CCGC_LENGTH
					+ " and "
					+ MAX_MANUAL_CCGC_LENGTH
					+ ", but length was "
					+ aPayment04.getCreditCard().getCcNbr().length() + ".";
			assertEquals(dtie.getLogMessage(), errorString);
		}
		/* Scenario :: 5 when gift card length is less than 11 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment06);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorString = "Invalid gift card account number length on PayItem "
					+ aPayment06.getPayItem().toString()
					+ " expected to be between "
					+ MIN_MANUAL_CCGC_LENGTH
					+ " and "
					+ MAX_MANUAL_CCGC_LENGTH
					+ ", but length was "
					+ aPayment06.getGiftCard().getGcNbr().length() + ".";
			assertEquals(dtie.getLogMessage(), errorString);
		}
		/* Scenario :: 5 when gift card is non numeric */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment07);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorString = "Invalid gift card account number (non-numeric characters detected) on PayItem "
					+ aPayment07.getPayItem().toString() + ".";
			assertEquals(dtie.getLogMessage(), errorString);
		}
		/* Scenario :: 6 when card payment is of 0 amount */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment05);
		try {
			PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO,
					entityTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
			String errorString = "Credit or gift card payment amount of zero is not allowed.";
			assertEquals(dtie.getLogMessage(), errorString);
		}
	}

	/**
	 * Test case for ValidateRenewEntInstallDownpayment
	 */
	@Test
	public void testValidateRenewEntInstallDownpayment() {
		/* Creating object for CreditCardTO */
		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");
		/* Creating another object for CreditCardTO */
		CreditCardTO creditCard1 = new CreditCardTO();
		creditCard1.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard1.setCcNbr("12345678925");
		/* Creating object for GiftCardTO */
		GiftCardTO aGiftCard = new GiftCardTO();
		aGiftCard.setGcNbr("12345678925");
		/*Object for InstallmentTO*/
		InstallmentTO installment = new InstallmentTO();
		ArrayList<PaymentTO> payListTO = null;
		ArrayList<DBProductTO> prodList = null;
		/*Payment Detail*/
		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("25.01"));
		aPayment01.setCreditCard(creditCard);
		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		aPayment02.setPayAmount(new BigDecimal("70.98"));
		aPayment02.setInstallment(installment);
		PaymentTO aPayment03 = new PaymentTO();
		aPayment03.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment03.setPayAmount(new BigDecimal("1"));
		aPayment03.setCreditCard(creditCard);
		/*Creating DTITransactionTO object of type RENEWENTITLEMENT*/
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		/*Creating common Request applicable to all */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		ArrayList<TPLookupTO> tpLookups = new ArrayList<TPLookupTO>();
		/* Scenario :: 1 * when payment pay type is not INSTALLMENT */
		RenewEntitlementRequestTO renewEntReqTO = new RenewEntitlementRequestTO();
		renewEntReqTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		renewEntReqTO.setEligibilityGroup("1");
		renewEntReqTO.setEligibilityMember("1");
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment01);
		renewEntReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		try {
			PaymentRules.validateRenewEntInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception:  Empty payments, no voucher: "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 2 * when payment pay type is INSTALLMENT and lookupValue
		 * is non numeric
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment02);
		renewEntReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		TPLookupTO tpLookupTO = new TPLookupTO();
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		tpLookupTO.setLookupValue("one");
		tpLookupTO.setLookupDesc("Downpayment");
		tpLookups = new ArrayList<TPLookupTO>();
		tpLookupTO.setCmdCode("1");
		tpLookupTO.setCmdId(new BigInteger("1"));
		tpLookups.add(tpLookupTO);
		try {
			PaymentRules.validateRenewEntInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			String errorString = "PaymentRules.validateInstallmentDownpayment attempted to read downpayment from DB, and it wasn't a number:"
					+ tpLookupTO.getLookupValue();
			assertEquals(dtie.getLogMessage(), errorString);
		}
		/*
		 * Scenario :: 3 * when payment pay type is INSTALLMENT and lookupValue
		 * is not null and is numeric
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment02);
		renewEntReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		tpLookupTO.setLookupValue("1");
		tpLookups.add(tpLookupTO);	
		prodList = DTIMockUtil.fetchDBOrderList();
		dtiTxn.setDbProdList(prodList);
		try {
			PaymentRules.validateRenewEntInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}
		/*
		 * Scenario :: 4 * when payment pay type is INSTALLMENT and CREDIT CARD
		 * , request contains both INSTALLMENT and CREDIT CARD
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment02);
		payListTO.add(aPayment03);
		renewEntReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		try {
			PaymentRules.validateRenewEntInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for ValidateResInstallDownpayment
	 */
	@Test
	public void testValidateResInstallDownpayment() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RESERVATION);
		/* creating common request applicable to all */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		ArrayList<PaymentTO> payListTO = null;
		/* Initialize TPLookupTO */
		ArrayList<TPLookupTO> tpLookups = new ArrayList<TPLookupTO>();
		/* Initialize InstallmentTO :: creating object for InstallmentTO */
		InstallmentTO installment = new InstallmentTO();
		InstallmentCreditCardTO increditCard = new InstallmentCreditCardTO();
		increditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		increditCard.setCcNbr("123456789256");
		increditCard.setCcName("Installment Credit card Test");
		installment.setCreditCard(increditCard);
		/* Initialize CreditCardTO :: creating object for CreditCardTO */
		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("12345678925");
		/* Another Object for CreditCardTO making it equal to the installment */
		CreditCardTO creditCard01 = new CreditCardTO();
		creditCard01.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard01.setCcNbr("123456789256");
		/* Initialize InstallmentTO :: creating object for InstallmentTO */
		InstallmentTO installment01 = new InstallmentTO();
		InstallmentCreditCardTO increditCard01 = new InstallmentCreditCardTO();
		increditCard01.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		increditCard01.setCcNbr("123456789256");
		increditCard01.setCcName("Installment Credit card");
		installment01.setCreditCard(increditCard01);
		/* Payment Detail */
		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("25.01"));
		aPayment01.setCreditCard(creditCard);
		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		aPayment02.setPayAmount(new BigDecimal("70.98"));
		aPayment02.setInstallment(installment);
		PaymentTO aPayment03 = new PaymentTO();
		aPayment03.setPayType(PaymentTO.PaymentType.INSTALLMENT);
		aPayment03.setPayAmount(new BigDecimal("78.98"));
		aPayment03.setInstallment(installment01);
		PaymentTO aPayment04 = new PaymentTO();
		aPayment04.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment04.setPayAmount(new BigDecimal("25.01"));
		aPayment04.setCreditCard(creditCard01);
		PaymentTO aPayment05 = new PaymentTO();
		aPayment05.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment05.setPayAmount(new BigDecimal("1"));
		aPayment05.setCreditCard(creditCard01);
		/*
		 * Scenario :: 1 * when payment pay type is not INSTALLMENT and length
		 * is more then 25
		 */
		ReservationRequestTO resReqTO = new ReservationRequestTO();
		getReservationRequest(resReqTO);
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment02);
		resReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Installment Credit Card CC Name exceeded maximum character length of "
					+ MAXCARDHOLDERNAME
					+ " (length of what was sent was "
					+ aPayment02.getInstallment().getCreditCard().getCcName()
							.length() + ").";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/* Scenario :: 2 * when payment pay type is Credit Card only */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment01);
		resReqTO.setPaymentList(payListTO);
		resReqTO.setInstallmentResRequest(true);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "Installment transaction missing installment credit card.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 3 * when payment pay type is not Credit Card and is
		 * Installment
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment03);
		resReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "Installment transaction missing downpayment credit card.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 4 * passing both INSTALLMENT and CREDIT CARD as pay type
		 * , but both card having different ccnbr
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment01);
		payListTO.add(aPayment03);
		resReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_TYPE);
			String errorMsg = "Installment must be the SAME credit card as the other credit card on the purchase.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/* Scenario :: 5 * when TPLookupTO size is 0 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment03);
		payListTO.add(aPayment04);
		resReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			String errorMsg = "PaymentRules.validateInstallmentDownpayment Installmnt Downpayment TP_LOOKUP for provider not populated.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 6 * Number format Exception is expected when lookupValue
		 * is set as empty
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment03);
		payListTO.add(aPayment04);
		resReqTO.setPaymentList(payListTO);
		TPLookupTO tpLookupTO = new TPLookupTO();
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		tpLookupTO.setLookupValue("");
		tpLookupTO.setLookupDesc("Downpayment");
		tpLookups = new ArrayList<TPLookupTO>();
		tpLookupTO.setCmdCode("");
		tpLookupTO.setCmdId(new BigInteger("1"));
		tpLookups.add(tpLookupTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			String errorMsg = "PaymentRules.validateInstallmentDownpayment attempted to read downpayment from DB, and it wasn't a number:"
					+ tpLookupTO.getLookupValue();
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 7 * When payment of the installment pay amount is not
		 * matching with sum of the Credit Card
		 */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment03);
		payListTO.add(aPayment04);
		resReqTO.setPaymentList(payListTO);
		tpLookupTO.setLookupValue("1");
		tpLookups.add(tpLookupTO);
		ArrayList<DBProductTO> prodList = null;
		/* fetching the value for DBOrder List */
		prodList = DTIMockUtil.fetchDBOrderList();
		dtiTxn.setDbProdList(prodList);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYMENT_AMOUNT);
		}
		/* Scenario ::8 * *Success */
		payListTO = new ArrayList<PaymentTO>();
		payListTO.add(aPayment03);
		payListTO.add(aPayment05);
		resReqTO.setPaymentList(payListTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for method ValidateWDWCreditCardSizes
	 */
	@Test
	public void testValidateWDWCreditCardSizes() {
		ArrayList<PaymentTO> payListTO = null;
		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard.setCcNbr("123456789256");
		creditCard.setCcVV("");
		PaymentTO aPayment01 = new PaymentTO();
		aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment01.setPayAmount(new BigDecimal("25.01"));
		aPayment01.setCreditCard(creditCard);

		CreditCardTO creditCard01 = new CreditCardTO();
		creditCard01.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard01.setCcNbr("123456789256");
		creditCard01.setCcVV("123456789");
		PaymentTO aPayment02 = new PaymentTO();
		aPayment02.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment02.setPayAmount(new BigDecimal("25.01"));
		aPayment02.setCreditCard(creditCard01);

		CreditCardTO creditCard02 = new CreditCardTO();
		creditCard02.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard02.setCcNbr("123456789256");
		creditCard02.setCcVV("12345678");
		creditCard02.setCcStreet("");
		PaymentTO aPayment03 = new PaymentTO();
		aPayment03.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment03.setPayAmount(new BigDecimal("25.01"));
		aPayment03.setCreditCard(creditCard02);

		CreditCardTO creditCard03 = new CreditCardTO();
		creditCard03.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard03.setCcNbr("123456789256");
		creditCard03.setCcVV("12345678");
		creditCard03.setCcStreet("01 XXX Abraham Linkon Road XXXX ");
		PaymentTO aPayment04 = new PaymentTO();
		aPayment04.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment04.setPayAmount(new BigDecimal("25.01"));
		aPayment04.setCreditCard(creditCard03);

		CreditCardTO creditCard04 = new CreditCardTO();
		creditCard04.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard04.setCcNbr("123456789256");
		creditCard04.setCcVV("12345678");
		creditCard04.setCcStreet("01 Abraham Linkon Road");
		creditCard04.setCcZipCode("");
		PaymentTO aPayment05 = new PaymentTO();
		aPayment05.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment05.setPayAmount(new BigDecimal("25.01"));
		aPayment05.setCreditCard(creditCard04);

		CreditCardTO creditCard05 = new CreditCardTO();
		creditCard05.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard05.setCcNbr("123456789256");
		creditCard05.setCcVV("12345678");
		creditCard05.setCcStreet("01 Abraham Linkon Road");
		creditCard05.setCcZipCode("012345678912");
		PaymentTO aPayment06 = new PaymentTO();
		aPayment06.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment06.setPayAmount(new BigDecimal("25.01"));
		aPayment06.setCreditCard(creditCard05);

		CreditCardTO creditCard06 = new CreditCardTO();
		creditCard06.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard06.setCcNbr("123456789256");
		creditCard06.setCcVV("12345678");
		creditCard06.setCcStreet("01 Abraham Linkon Road");
		creditCard06.setCcZipCode("012345");
		creditCard06.setCcExpiration("231");
		PaymentTO aPayment07 = new PaymentTO();
		aPayment07.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment07.setPayAmount(new BigDecimal("25.01"));
		aPayment07.setCreditCard(creditCard06);

		CreditCardTO creditCard07 = new CreditCardTO();
		creditCard07.setCcManualOrSwipe(CreditCardType.CCMANUAL);
		creditCard07.setCcNbr("123456789256");
		creditCard07.setCcVV("12345678");
		creditCard07.setCcStreet("01 Abraham Linkon Road");
		creditCard07.setCcZipCode("012345");
		creditCard07.setCcExpiration("2301");
		PaymentTO aPayment08 = new PaymentTO();
		aPayment08.setPayType(PaymentTO.PaymentType.CREDITCARD);
		aPayment08.setPayAmount(new BigDecimal("25.01"));
		aPayment08.setCreditCard(creditCard07);

		/* Scenario :: 1 When CVV passed is empty */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment01);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* Scenario :: 2 When CVV length is greater than 8 */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment02);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card CVV length of "
					+ aPayment02.getCreditCard().getCcVV().length()
					+ " is not allowed.  Maximum is " + MAX_WDW_CVV_LENGTH
					+ ".";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/* Scenario :: 3 When Credit Card contains empty street */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment03);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card AVS Street length of zero is not allowed.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 4 When Credit Card contains street of more than 30
		 * character
		 */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment04);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card AVS Street length of "
					+ aPayment04.getCreditCard().getCcStreet().length()
					+ " is not allowed.  Maximum is "
					+ MAX_WDW_AVS_STREET_LENGTH + ".";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/* Scenario :: 5 When Credit Card contains empty zip Code */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment05);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card AVS ZIP length of zero is not allowed.";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 6 When Credit Card contains zip Code of more than 10
		 * character
		 */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment06);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card AVS ZIP length of "
					+ aPayment06.getCreditCard().getCcZipCode().length()
					+ " is not allowed.  Maximum is " + MAX_WDW_AVS_ZIP_LENGTH
					+ ".";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/*
		 * Scenario :: 7 When Credit Card contains CC Expiration of less than 4
		 * character
		 */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment07);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			String errorMsg = "Credit card expiration length of "
					+ aPayment07.getCreditCard().getCcExpiration().length()
					+ " is not allowed.  Required length is "
					+ WDW_REQUIRED_EXPIRATION_LENGTH + ".";
			assertEquals(dtie.getLogMessage(), errorMsg);
		}
		/* Scenario :: 8 Valid Scenario */
		try {
			payListTO = new ArrayList<PaymentTO>();
			payListTO.add(aPayment08);
			PaymentRules.validateWDWCreditCardSizes(payListTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}

	}

}
