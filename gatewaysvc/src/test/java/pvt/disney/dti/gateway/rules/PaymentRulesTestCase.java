package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Tests the payment rules.
 * 
 * @author lewit019
 *
 */
public class PaymentRulesTestCase {

  /**
   * Test validate payments on order.
   */
  @Test
  public final void testValidatePaymentsOnOrder() {

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

    PaymentTO aPayment01 = new PaymentTO();
    aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
    aPayment01.setPayAmount(new BigDecimal("1000.00"));

    PaymentTO aPayment02 = new PaymentTO();
    aPayment02.setPayType(PaymentTO.PaymentType.GIFTCARD);
    aPayment02.setPayAmount(new BigDecimal("410.82"));

    PaymentTO aPayment03 = new PaymentTO();
    aPayment03.setPayType(PaymentTO.PaymentType.VOUCHER);
    aPayment03.setPayAmount(new BigDecimal("0.01"));

    PaymentTO aPayment04 = new PaymentTO();
    aPayment04.setPayType(PaymentTO.PaymentType.CREDITCARD);
    aPayment04.setPayAmount(new BigDecimal("0.01"));

    // Test 1: No payments, no voucher
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1: No payments, no voucher: " + dtie.toString());
    }

    // Test 2: Empty payments, no voucher
    payListTO = new ArrayList<PaymentTO>();
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2:  Empty payments, no voucher: " + dtie.toString());
    }

    // Test 3: Payments less than products
    payListTO.add(aPayment01);
    payListTO.add(aPayment02);
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
      fail("Expected exception on Test 3:  Payments less than products.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_AMOUNT)
        fail("Expected error INVALID_PAYMENT_AMOUNT on Test 3:  Payments less than products.");
    }

    // Test 4: Payments equal to products
    payListTO.add(aPayment03);
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 4:  Payments equal to products: " + dtie.toString());
    }

    // Test 5: Payments greater than products
    payListTO.add(aPayment04);
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
      fail("Expected exception on Test 5:  Payments greater than products.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_AMOUNT)
        fail("Expected error INVALID_PAYMENT_AMOUNT on Test 5:  Payments greater than products.");
    }

    return;
  }

  /**
   * Test validate payment composition.
   */
  @Test
  public final void testValidatePaymentComposition() {

    String tpiCode = DTITransactionTO.TPI_CODE_WDW;
    ArrayList<PaymentTO> payListTO = null;

    PaymentTO aCreditCard = new PaymentTO();
    aCreditCard.setPayType(PaymentTO.PaymentType.CREDITCARD);
    aCreditCard.setPayAmount(new BigDecimal("50.00"));

    PaymentTO aGiftCard = new PaymentTO();
    aGiftCard.setPayType(PaymentTO.PaymentType.GIFTCARD);
    aGiftCard.setPayAmount(new BigDecimal("50.00"));

    PaymentTO aVoucher = new PaymentTO();
    aVoucher.setPayType(PaymentTO.PaymentType.VOUCHER);
    aVoucher.setPayAmount(new BigDecimal("50.00"));

    // Test 1: No paylist.
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1: No paylist. " + dtie.toString());
    }

    // Test 2: WDW one credit card
    payListTO = new ArrayList<PaymentTO>();
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2: WDW one credit card. " + dtie.toString());
    }

    // Test 3: WDW two credit cards
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 3: WDW two credit cards. " + dtie.toString());
    }

    // Test 4: WDW one gift card, one credit card
    payListTO.clear();
    payListTO.add(aGiftCard);
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 4: WDW one gift card, one credit card. " + dtie.toString());
    }

    // Test 5: WDW one gift card, two credit cards
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
      fail("Expected exception on Test 5: WDW one gift card, two credit cards.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_TYPE)
        fail("Expected error PAYMENT_TYPE_INVALID on Test 5: WDW one gift card, two credit cards.");
    }

    // Test 6: WDW two credit cards & voucher
    payListTO.clear();
    payListTO.add(aCreditCard);
    payListTO.add(aCreditCard);
    payListTO.add(aVoucher);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 6: WDW two credit cards & voucher. " + dtie.toString());
    }

    // Test 7: WDW four credit cards
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
        fail("Expected error PAYMENT_TYPE_INVALID on Test 7: WDW four credit cards.");
    }

    // Test 8: DLR five credit cards
    tpiCode = DTITransactionTO.TPI_CODE_DLR;
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 8: DLR five credit cards. " + dtie.toString());
    }

    // Test 9: DLR six credit cards
    payListTO.add(aCreditCard);
    try {
      PaymentRules.validatePaymentComposition(payListTO, tpiCode);
      fail("Expected exception on Test 9: DLR six credit cards.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PAYMENT_TYPE)
        fail("Expected error PAYMENT_TYPE_INVALID on Test 9: DLR six credit cards.");
    }

    return;
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

    PaymentTO aPayment01 = new PaymentTO();
    aPayment01.setPayType(PaymentTO.PaymentType.CREDITCARD);
    aPayment01.setPayAmount(new BigDecimal("1077815.06"));
    payListTO.add(aPayment01);
    
    // Test 1:  Large sums
    try {
      PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1:  Large sums: " + dtie.toString());
    }

    return;
  }
  
}
