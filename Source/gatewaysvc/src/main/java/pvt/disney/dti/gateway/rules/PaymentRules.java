package pvt.disney.dti.gateway.rules;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.rules.dlr.DLRReservationRules;
import pvt.disney.dti.gateway.rules.wdw.WDWReservationRules;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * The class PaymentRules contains the business rules for payment validation.
 * 
 * @author lewit019
 * 
 */
public class PaymentRules {

  private static final int WDW_MAX_NUMBER_OF_CC_OR_GC = 2;
  private static final int WDW_MAX_NUMBER_OF_PAYMENTS = 3;
  private static final int DLR_MAX_NUMBER_OF_CC_OR_GC = 5;
  private static final int DLR_MAX_NUMBER_OF_PAYMENTS = 5;
  private static final int HKD_MAX_NUMBER_OF_CC_OR_GC = 2;
  private static final int HKD_MAX_NUMBER_OF_PAYMENTS = 3;
  
  /** The maximum CVV length (8). */
  public final static int MAX_WDW_CVV_LENGTH = 8;

  /** The minimum field length (standard of 1). */
  public final static int MIN_FIELD_LENGTH = 1;

  /** The maximum AVS street length (30). */
  public final static int MAX_WDW_AVS_STREET_LENGTH = 30;

  /** The maximum AVS ZIP length (10). */
  public final static int MAX_WDW_AVS_ZIP_LENGTH = 10;

  /** The required expiration field length (4). */
  public final static int WDW_REQUIRED_EXPIRATION_LENGTH = 4;

  private static final int MAX_MANUAL_CCGC_LENGTH = 19;
  private static final int MIN_MANUAL_CCGC_LENGTH = 12;

  private static final int MAXCARDHOLDERNAME = 25;

  /**
   * Validate payments on order. <BR>
   * Enforces the following rules: <BR>
   * 
   * RULE: If a payment section is provided, payments must match up to the cost
   * of items purchased. <BR>
   * RULE: Validate that all credit cards are between (inclusive) 12 and 19
   * digits in length. <BR>
   * RULE: Ensure that gift card numbers are being received and transmitted as
   * numeric only (as of 2.12). <BR>
   * 
   * @param tktListTO
   *          the tkt transfer object list
   * @param payListTO
   *          the payment transfer object list
   * @param entityTO
   *          the entity transfer object
   * @return Total Order Cost
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static BigDecimal validatePaymentsOnOrder(ArrayList<TicketTO> tktListTO, ArrayList<PaymentTO> payListTO,
      EntityTO entityTO) throws DTIException {

    // Determine the total order cost from the products
    BigDecimal totalProductCost = new BigDecimal("0.00");
    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      if (aTicketTO.getTicketTypes().size() > 0) {
        totalProductCost = totalProductCost.add(aTicketTO.getProdPrice());
      } else {
        BigDecimal prodCost = aTicketTO.getProdPrice();
        BigInteger prodQty = aTicketTO.getProdQty();
        BigDecimal prodTotal = prodCost.multiply(new BigDecimal(prodQty));
        totalProductCost = totalProductCost.add(prodTotal);
      }

    }

    // Any default voucher to cover the cost when no other payment is present?
    if ((payListTO == null) || (payListTO.size() == 0)) {

      // This is an "unpaid" reservation. If a voucher is present in the
      // database, it will be added to the order at translation. If not, the
      // order will be considered to be "unpaid" and processed/flagged
      // accordingly.

    } else {

      // Determine the total payments made from the payments list
      BigDecimal totalPaymentsMade = new BigDecimal("0.00");

      for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

        if ((aPaymentTO.getPayAmount() != null) && (aPaymentTO.getPayAmount().floatValue() > 0.0)) {
          totalPaymentsMade = totalPaymentsMade.add(aPaymentTO.getPayAmount());
        }

        // Validate the length of a credit card, if present.
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD) {

          CreditCardTO aCreditCard = aPaymentTO.getCreditCard();
          if (aCreditCard.getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {

            if ((aCreditCard.getCcNbr().length() > MAX_MANUAL_CCGC_LENGTH)
                || (aCreditCard.getCcNbr().length() < MIN_MANUAL_CCGC_LENGTH)) {

              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                  "Invalid credit card account number length on PayItem " + aPaymentTO.getPayItem().toString()
                      + " expected to be between " + MIN_MANUAL_CCGC_LENGTH + " and " + MAX_MANUAL_CCGC_LENGTH
                      + ", but length was " + aCreditCard.getCcNbr().length() + ".");
            }
          }

        } // If credit card

        // Validate the length of a gift card, if present, and that it is
        // numeric.
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.GIFTCARD) {

          GiftCardTO aGiftCard = aPaymentTO.getGiftCard();
          if (aGiftCard.getGcManualOrSwipe() == GiftCardTO.GiftCardType.GCMANUAL) {

            if ((aGiftCard.getGcNbr().length() > MAX_MANUAL_CCGC_LENGTH)
                || (aGiftCard.getGcNbr().length() < MIN_MANUAL_CCGC_LENGTH)) {

              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                  "Invalid gift card account number length on PayItem " + aPaymentTO.getPayItem().toString()
                      + " expected to be between " + MIN_MANUAL_CCGC_LENGTH + " and " + MAX_MANUAL_CCGC_LENGTH
                      + ", but length was " + aGiftCard.getGcNbr().length() + ".");
            }
          }

          if (!CustomDataTypeConverter.isNumeric(aGiftCard.getGcNbr())) {
            throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                "Invalid gift card account number (non-numeric characters detected) on PayItem "
                    + aPaymentTO.getPayItem().toString() + ".");
          }

        } // If gift card

        // Validate the lengths of installment credit cards, manual (as of 2.15,
        // JTL)
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.INSTALLMENT) {
          InstallmentTO installTO = aPaymentTO.getInstallment();

          InstallmentCreditCardTO aCreditCard = installTO.getCreditCard();
          if (installTO.getCreditCard().getCcManualOrSwipe() == CreditCardType.CCMANUAL) {

            if ((aCreditCard.getCcNbr().length() > MAX_MANUAL_CCGC_LENGTH)
                || (aCreditCard.getCcNbr().length() < MIN_MANUAL_CCGC_LENGTH)) {

              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                  "Invalid credit card account number length on PayItem " + aPaymentTO.getPayItem().toString()
                      + " expected to be between " + MIN_MANUAL_CCGC_LENGTH + " and " + MAX_MANUAL_CCGC_LENGTH
                      + ", but length was " + aCreditCard.getCcNbr().length() + ".");
            }

          }

        } // If installment

        // Validate that credit cards do not have a zero payment amount.
        // Version 2.2.2 change added per request of Joe Roberts by JTL 12/17
        // Since 2.9, moved to cover gift and credit cards.
        BigDecimal payment = aPaymentTO.getPayAmount();
        if (payment != null) {
          if (payment.compareTo(new BigDecimal("0.0")) == 0) {
            throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
                "Credit or gift card payment amount of zero is not allowed.");
          }
        }

      } // End Payment Loop

      // If payments and product cost don't match, throw exception.
      if (totalProductCost.compareTo(totalPaymentsMade) != 0) {
        throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT, "Product cost total of "
            + totalProductCost.toString() + " did not equal total payments made of " + totalPaymentsMade.toString());
      }
    }

    return (totalProductCost);

  }

  /**
   * Validate payments are present. <BR>
   * Enforces the following rules: <BR>
   * 
   * 1. Ensures that a payment is present or a default payment is specified.
   * 
   * @param payListTO
   *          the payment transfer object list
   * @param entityTO
   *          the entity transfer object
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validatePaymentsPresent(ArrayList<PaymentTO> payListTO, EntityTO entityTO) throws DTIException {

    if (entityTO.getDefPymtData() != null) {
      return;
    }

    if ((payListTO == null) || (payListTO.size() == 0)) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "On this transaction type, a payment type is required, either default or expressly specified.");
    }

    return;
  }

  /**
   * Validate payments on order. <BR>
   * Enforces the following rules: <BR>
   * 
   * 1. If a payment section is provided, payments must match up to the cost of
   * upgrades purchased. 2. Validate that all credit cards are between
   * (inclusive) 12 and 19 digits in length.
   * 
   * @param tktListTO
   *          the tkt transfer object list
   * @param payListTO
   *          the payment transfer object list
   * @param entityTO
   *          the entity transfer object
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validatePaymentsOnUpgrade(ArrayList<TicketTO> tktListTO, ArrayList<PaymentTO> payListTO,
      EntityTO entityTO) throws DTIException {

    // Determine the total order cost from the upgrade price
    BigDecimal totalProductCost = new BigDecimal("0.00");

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {
      totalProductCost = totalProductCost.add(aTicketTO.getUpgrdPrice());
    }

    // Any default voucher to cover the cost when no other payment is present?
    if ((payListTO == null) || (payListTO.size() == 0)) {

      // Assume it's a zero amount upgrade, therefore the totalProductCost
      // should be 0.0
      if (totalProductCost.floatValue() > 0.0) {
        throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE, "Product cost total of "
            + totalProductCost.toString() + " did not equal total payments made of 0.0.");
      }

    } else {

      // Determine the total payments made from the payments list
      BigDecimal totalPaymentsMade = new BigDecimal("0.00");
      for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

        if ((aPaymentTO.getPayAmount() != null) && (aPaymentTO.getPayAmount().floatValue() > 0.0)) {
          totalPaymentsMade = totalPaymentsMade.add(aPaymentTO.getPayAmount());
        }

        // Validate the length of a credit card, if present.
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD) {

          CreditCardTO aCreditCard = aPaymentTO.getCreditCard();
          if (aCreditCard.getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {

            if ((aCreditCard.getCcNbr().length() > MAX_MANUAL_CCGC_LENGTH)
                || (aCreditCard.getCcNbr().length() < MIN_MANUAL_CCGC_LENGTH)) {

              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                  "Invalid credit card account number length on PayItem " + aPaymentTO.getPayItem().toString()
                      + " expected to be between " + MIN_MANUAL_CCGC_LENGTH + " and " + MAX_MANUAL_CCGC_LENGTH
                      + ", but length was " + aCreditCard.getCcNbr().length() + ".");
            }
          }
        }

        // Validate the length of a gift card, if present.
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.GIFTCARD) {

          GiftCardTO aGiftCard = aPaymentTO.getGiftCard();
          if (aGiftCard.getGcManualOrSwipe() == GiftCardTO.GiftCardType.GCMANUAL) {

            if ((aGiftCard.getGcNbr().length() > MAX_MANUAL_CCGC_LENGTH)
                || (aGiftCard.getGcNbr().length() < MIN_MANUAL_CCGC_LENGTH)) {

              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                  "Invalid gift card account number length on PayItem " + aPaymentTO.getPayItem().toString()
                      + " expected to be between " + MIN_MANUAL_CCGC_LENGTH + " and " + MAX_MANUAL_CCGC_LENGTH
                      + ", but length was " + aGiftCard.getGcNbr().length() + ".");
            }
          }
          // (as of 2.12).
          if (!CustomDataTypeConverter.isNumeric(aGiftCard.getGcNbr())) {
            throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
                "Invalid gift card account number (non-numeric characters detected) on PayItem "
                    + aPaymentTO.getPayItem().toString() + ".");
          }
        }

        // Validate that credit cards do not have a zero payment amount.
        // Version 2.2.2 change added per request of Joe Roberts by JTL 12/17
        // Since 2.9, moved to cover gift and credit cards.
        BigDecimal payment = aPaymentTO.getPayAmount();
        if (payment != null) {
          if (payment.compareTo(new BigDecimal("0.0")) == 0) {
            throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
                "Credit or gift card payment amount of zero is not allowed.");
          }
        }

      } // End Payment Loop

      // If payments and product cost don't match, throw exception.
      if (totalProductCost.compareTo(totalPaymentsMade) != 0) {
        throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT, "Product cost total of "
            + totalProductCost.toString() + " did not equal total payments made of " + totalPaymentsMade.toString());
      }
    }

    return;

  }

  /**
   * Validate payment composition. Limits are placed upon the number of gift
   * cards or credit cards that can be used in combination, but the number of
   * vouchers is not limited beyond the maximum number of payments. Validates
   * the maximum number of payments allowed. <BR>
   * Enforces the following rules: <BR>
   * 1. Does the number and types of payments exceed provider limitations.<BR>
   * 2. Does the order include illegal payment types for a provider. <BR>
   * 3. Does a pre-auth credit card have all required fields? <BR>
   * 
   * @param payListTO
   *          the payment transfer object list
   * @param tpiCode
   *          the ticket provider interface code
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validatePaymentComposition(ArrayList<PaymentTO> payListTO, String tpiCode) throws DTIException {

    if (payListTO == null) {
      return;
    }

    int numberOfCCGC = 0;
    int numberOfPayments = 0;

    int maxNumberOfCCGC = 0;
    int maxNumberOfPayments = 0;

    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) {
      maxNumberOfCCGC = PaymentRules.DLR_MAX_NUMBER_OF_CC_OR_GC;
      maxNumberOfPayments = PaymentRules.DLR_MAX_NUMBER_OF_PAYMENTS;
    } else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      maxNumberOfCCGC = PaymentRules.WDW_MAX_NUMBER_OF_CC_OR_GC;
      maxNumberOfPayments = PaymentRules.WDW_MAX_NUMBER_OF_PAYMENTS;
    } else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_HKD) == 0) {
      maxNumberOfCCGC = PaymentRules.HKD_MAX_NUMBER_OF_CC_OR_GC ;
      maxNumberOfPayments = PaymentRules.HKD_MAX_NUMBER_OF_PAYMENTS ;
    }

    // Evaluate the number of payments
    if ((payListTO != null) && (payListTO.size() > 0)) {
      numberOfPayments = payListTO.size();

      if (numberOfPayments > maxNumberOfPayments) {
        throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_COUNT, "Number of payments provided "
            + numberOfPayments + " exceeded " + tpiCode + " maximum of " + maxNumberOfPayments);
      }

    }

    // Evaluate the composition of the payments
    for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

      if ((aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD)
          || (aPaymentTO.getPayType() == PaymentTO.PaymentType.GIFTCARD)) {
        numberOfCCGC++;

        if (aPaymentTO.getPayAmount() == null) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
              "Gift Cards and Credit Cards must have a payment amount (PayItem: " + aPaymentTO.getPayItem().intValue()
                  + ").");
        }
      }

      /**
       * RULE: If credit card payment is pre-auth, make sure it has all required
       * fields. (as of 2.16.2, JTL)
       */
      if (aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD) {

        CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();
        if ((aCreditCardTO.getPreApprovedCC() != null) && (aCreditCardTO.getPreApprovedCC().booleanValue())) {

          if (aCreditCardTO.getCcAuthCode() == null) {
            throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                "The pre-approved credit card is missing its authorization code.");
          }

          if (aCreditCardTO.getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {
            if (aCreditCardTO.getCcSha1() == null) {
              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "The pre-approved credit card is missing its SHA-1 credit card hash code.");
            }
            if (aCreditCardTO.getCcSubCode() == null) {
              throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "The pre-approved credit card is missing its Credit Card Sub code.");
            }

          }
        }
      }
    }

    if (numberOfCCGC > maxNumberOfCCGC) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Number of credit and gift card payments provided " + numberOfCCGC + " exceeded " + tpiCode + " maximum of "
              + maxNumberOfCCGC);
    }

    // Are any of the payment types being passed to DLR a voucher?
    if ((tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) && (payListTO.size() > 0)) {
      for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {
        if (aPaymentTO.getPayType() == PaymentTO.PaymentType.VOUCHER) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
              "DLR reservation attempted with the voucher payment type.  Not permitted.");
        }
      }
    }
    return;
  }

  /**
   * Determine tax exempt status.
   * 
   * @param taxExemptCode
   *          the tax exempt code
   * 
   * @return true, if successful
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static boolean determineTaxExemptStatus(String taxExemptCode) throws DTIException {

    boolean isTaxExempt = false;

    // Determine Tax Exempt Status
    if ((taxExemptCode != null) && (taxExemptCode.length() > 0)) {
      isTaxExempt = true;
    }

    return isTaxExempt;

  }

  /**
   * TO DO: JUNIT Ensures that transactions returning funds have a form of
   * payment (either default or explicitly provided) to return funds to.
   * Enforces the following rules: <BR>
   * 1. Does the transaction have a form of payment to return funds to.
   * 
   * @param payListTO
   *          An array list of Payment Transfer Objects.
   * @param entityTO
   *          The entity transfer object
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validateReturnFormOfPayment(ArrayList<PaymentTO> payListTO, EntityTO entityTO) throws DTIException {

    if (payListTO.size() > 0) {
      return;
    }

    if (entityTO.getDefPymtData() == null) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Void transaction has no return form of payment, explicit or default.");
    }

    return;
  }

  /**
   * Validate that the credit card fields have appropriate sizes.<BR>
   * Validated by this rule are...<BR>
   * AVS Street and Zip<BR>
   * CVV<BR>
   * Credit Card Expiration
   * 
   * @param pmtList
   * @throws DTIException
   * @since 2.2
   */
  public static void validateWDWCreditCardSizes(ArrayList<PaymentTO> pmtList) throws DTIException {

    if ((pmtList != null) && (pmtList.size() > 0)) {

      for /* each */(PaymentTO aPayment : /* in */pmtList) {

        if (aPayment.getCreditCard() != null) {

          CreditCardTO aCreditCard = aPayment.getCreditCard();

          // Validate CCV
          if (aCreditCard.getCcVV() != null) {

            String cvv = aCreditCard.getCcVV();

            if (cvv.length() < MIN_FIELD_LENGTH) {
              // 2011-12-15: CUS; Special case, as the documentation doesn't dictate a minimum CCVV
              // length then a blank CCVV XML tag is technically legal.
              if (cvv.length() == 0) {
                aCreditCard.setCcVV(null);
              } else {
                throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                    "Credit card CVV length less than " + MIN_FIELD_LENGTH + " not allowed.  Length " + cvv.length()
                        + " attempted.");
              }
            }

            if (cvv.length() > MAX_WDW_CVV_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card CVV length of " + cvv.length() + " is not allowed.  Maximum is " + MAX_WDW_CVV_LENGTH
                      + ".");
            }
          }

          // Validate AVS Street
          if (aCreditCard.getCcStreet() != null) {

            String avsStreet = aCreditCard.getCcStreet();

            if (avsStreet.length() < MIN_FIELD_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card AVS Street length of zero is not allowed.");
            }

            if (avsStreet.length() > MAX_WDW_AVS_STREET_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card AVS Street length of " + avsStreet.length() + " is not allowed.  Maximum is "
                      + MAX_WDW_AVS_STREET_LENGTH + ".");
            }

          }

          // Validate AVS ZIP Code
          if (aCreditCard.getCcZipCode() != null) {

            String avsZip = aCreditCard.getCcZipCode();

            if (avsZip.length() < MIN_FIELD_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card AVS ZIP length of zero is not allowed.");
            }

            if (avsZip.length() > MAX_WDW_AVS_ZIP_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card AVS ZIP length of " + avsZip.length() + " is not allowed.  Maximum is "
                      + MAX_WDW_AVS_ZIP_LENGTH + ".");
            }

          }

          // Validate Credit Card Expiration
          if (aCreditCard.getCcExpiration() != null) {

            String ccExpiration = aCreditCard.getCcExpiration();

            if (ccExpiration.length() != WDW_REQUIRED_EXPIRATION_LENGTH) {
              throw new DTIException(WDWReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Credit card expiration length of " + ccExpiration.length() + " is not allowed.  Required length is "
                      + WDW_REQUIRED_EXPIRATION_LENGTH + ".");
            }
          }
        }
      }
    }

    return;
  }

  /**
   * RULE: Validate that if the "installment" type of payment is present, the
   * down-payment matches the required amount. (As of 2.16.1, JTL) RULE: If
   * Installment Payment type is present, only one additional form of payment is
   * allowed and it must be the same credit card as installment. (as of 2.16.1
   * JTL)
   * 
   * @param dtiTxn
   * @param tpLookups
   * @throws DTIException
   */
  public static void validateResInstallDownpayment(DTITransactionTO dtiTxn, ArrayList<TPLookupTO> tpLookups)
      throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;

    // RULE: Validate that if the "installment" type of payment is present,
    // the down-payment matches the required amount. (As of 2.16.1, JTL)
    // Although other rules allow only one other form of payment
    // this rule doesn't enforce that clause and can adapt to
    // multiple FOPs on the down payment.
    ArrayList<PaymentTO> paymentList = dtiResReq.getPaymentList();

    BigDecimal installmentDownpayment = new BigDecimal("0.00");
    String installmentCreditCard = null;
    String downpaymentCreditCard = null;
    int downpaymentCreditCardCount = 0;

    for (/* each */PaymentTO aPaymentTO : /* in */paymentList) {

      if (aPaymentTO.getPayType() == PaymentTO.PaymentType.INSTALLMENT) {
        dtiResReq.setInstallmentResRequest(true);

        if (installmentCreditCard != null) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
              "Only one installment credit card permitted on an installment transaction.");
        }

        if (aPaymentTO.getInstallment().getCreditCard().getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {
          installmentCreditCard = aPaymentTO.getInstallment().getCreditCard().getCcNbr();
        } else {
          installmentCreditCard = aPaymentTO.getInstallment().getCreditCard().getCcTrack1();
        }

        // RULE: On installment payments, CCName cannot exceed 25 characters (As
        // of 2.16.2, JTL)
        if (aPaymentTO.getInstallment().getCreditCard().getCcName().length() > MAXCARDHOLDERNAME) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
              "Installment Credit Card CC Name exceeded maximum character length of " + MAXCARDHOLDERNAME
                  + " (length of what was sent was " + aPaymentTO.getInstallment().getCreditCard().getCcName().length()
                  + ").");
        }

      } else if (aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD) {

        downpaymentCreditCardCount++;

        if (aPaymentTO.getCreditCard().getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {
          downpaymentCreditCard = aPaymentTO.getCreditCard().getCcNbr();
        } else {
          downpaymentCreditCard = aPaymentTO.getCreditCard().getCcTrack1();
        }

        installmentDownpayment = aPaymentTO.getPayAmount();

      }

    }

    if (dtiResReq.isInstallmentResRequest() == false) {
      return;
    }

    if (downpaymentCreditCardCount > 1) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment must be accompanied by only one additional credit card.");
    }

    if (installmentCreditCard == null) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment transaction missing installment credit card.");
    }
    if (downpaymentCreditCard == null) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment transaction missing downpayment credit card.");
    }
    if (!downpaymentCreditCard.equalsIgnoreCase(installmentCreditCard)) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment must be the SAME credit card as the other credit card on the purchase.");
    }

    // Calculate how much of a down payment should have been provided.
    BigDecimal singleStandardDownpayment = null;
    for (/* each */TPLookupTO aTPLookup : /* in */tpLookups) {
      if ((aTPLookup.getLookupDesc().compareToIgnoreCase("Downpayment") == 0)
          && (aTPLookup.getLookupType() == TPLookupTO.TPLookupType.INSTALLMENT)) {

        try {
          singleStandardDownpayment = new BigDecimal(aTPLookup.getLookupValue());
        } catch (NumberFormatException nfe) {
          throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
              "PaymentRules.validateInstallmentDownpayment attempted to read downpayment from DB, and it wasn't a number:"
                  + aTPLookup.getLookupValue());
        }
      }
    }
    if (singleStandardDownpayment == null) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
          "PaymentRules.validateInstallmentDownpayment Installmnt Downpayment TP_LOOKUP for provider not populated.");
    }

    // Determine the quantity of non-shipping product on the order
    HashMap<String, DBProductTO> productCodeMap = dtiTxn.getDbProdMap();
    ArrayList<TicketTO> tktList = dtiResReq.getTktList();
    int quantity = 0;

    for (/* each */TicketTO aTicketTO : /* in */tktList) {
      String pdtCode = aTicketTO.getProdCode();
      String dayClass = productCodeMap.get(pdtCode).getDayClass();
      if (dayClass.compareToIgnoreCase("SHIP") != 0) {
        quantity = quantity + aTicketTO.getProdQty().intValue();
      }
    }

    // Determine how much down payment should be provided.
    BigDecimal stdPayTotal = singleStandardDownpayment.multiply(new BigDecimal(quantity));

    // Compare expected down payment to actual down payment.
    if (stdPayTotal.compareTo(installmentDownpayment) != 0) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
          "The total downpayment expected of " + stdPayTotal + " does not equal what was on the reservation: "
              + installmentDownpayment);
    }

    // Preserve down-payment amount for later use in the out-bound transaction
    // to eGalaxy.
    dtiResReq.setInstallmentDownpayment(installmentDownpayment);

    return;
  }

  /**
   * RULE: Validate that if the "installment" type of payment is present, the
   * down-payment matches the required amount. (As of 2.16.1, JTL)
   * 
   * @param dtiTxn
   * @param tpLookups
   * @throws DTIException
   */
  public static void validateRenewEntInstallDownpayment(DTITransactionTO dtiTxn, ArrayList<TPLookupTO> tpLookups)
      throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    RenewEntitlementRequestTO dtiRenewEntReq = (RenewEntitlementRequestTO) dtiCmdBody;

    // RULE: Validate that if the "installment" type of payment is present,
    // the down-payment matches the required amount. (As of 2.16.1, JTL)
    // Although other rules allow only one other form of payment
    // this rule doesn't enforce that clause and can adapt to
    // multiple FOPs on the down payment.
    ArrayList<PaymentTO> paymentList = dtiRenewEntReq.getPaymentList();

    BigDecimal installmentDownpayment = new BigDecimal("0.00");
    for (/* each */PaymentTO aPaymentTO : /* in */paymentList) {

      if (aPaymentTO.getPayType() == PaymentTO.PaymentType.INSTALLMENT) {
        dtiRenewEntReq.setInstallmentRenewRequest(true);
      } else {
        if (aPaymentTO.getPayAmount() != null) { // i.e. voucher payment
          installmentDownpayment = installmentDownpayment.add(aPaymentTO.getPayAmount());
        }
      }

    }

    if (dtiRenewEntReq.isInstallmentRenewRequest() == false) {
      return;
    }

    BigDecimal singleStandardDownpayment = null;

    for (/* each */TPLookupTO aTPLookup : /* in */tpLookups) {
      if ((aTPLookup.getLookupDesc().compareToIgnoreCase("Downpayment") == 0)
          && (aTPLookup.getLookupType() == TPLookupTO.TPLookupType.INSTALLMENT)) {

        try {
          singleStandardDownpayment = new BigDecimal(aTPLookup.getLookupValue());
        } catch (NumberFormatException nfe) {
          throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
              "PaymentRules.validateInstallmentDownpayment attempted to read downpayment from DB, and it wasn't a number:"
                  + aTPLookup.getLookupValue());
        }
      }
    }
    if (singleStandardDownpayment == null) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
          "PaymentRules.validateInstallmentDownpayment Installmnt Downpayment TP_LOOKUP for provider not populated.");
    }

    // Determine the quantity of non-shipping product on the order
    HashMap<String, DBProductTO> productCodeMap = dtiTxn.getDbProdMap();
    ArrayList<TicketTO> tktList = dtiRenewEntReq.getTktList();
    int quantity = 0;

    for (/* each */TicketTO aTicketTO : /* in */tktList) {
      String pdtCode = aTicketTO.getProdCode();
      String dayClass = productCodeMap.get(pdtCode).getDayClass();
      if (dayClass.compareToIgnoreCase("SHIP") != 0) {
        quantity = quantity + aTicketTO.getProdQty().intValue();
      }
    }

    BigDecimal stdPayTotal = singleStandardDownpayment.multiply(new BigDecimal(quantity));

    if (stdPayTotal.compareTo(installmentDownpayment) != 0) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
          "The total downpayment expected of " + stdPayTotal + " does not equal what was on the reservation: "
              + installmentDownpayment);
    }

    // Preserve down-payment amount for later use in the out-bound transaction
    // to eGalaxy.
    dtiRenewEntReq.setInstallmentDownpayment(installmentDownpayment);

    return;
  }

  /**
   * RULE: Validate that if the "installment" type of payment is present, the
   * down-payment matches the required amount. (As of 2.16.1, JTL) RULE: If
   * Installment Payment type is present, only one additional form of payment is
   * allowed and it must be the same credit card as installment. (as of 2.16.1
   * JTL)
   * 
   * @param dtiTxn
   * @param tpLookups
   * @throws DTIException
   */
  public static void validateUpgrdEntInstallDownpayment(DTITransactionTO dtiTxn, ArrayList<TPLookupTO> tpLookups)
      throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    UpgradeEntitlementRequestTO dtiUpgrdEnt = (UpgradeEntitlementRequestTO) dtiCmdBody;

    // RULE: Validate that if the "installment" type of payment is present,
    // the down-payment matches the required amount. (As of 2.16.1, JTL)
    // Although other rules allow only one other form of payment
    // this rule doesn't enforce that clause and can adapt to
    // multiple FOPs on the down payment.
    ArrayList<PaymentTO> paymentList = dtiUpgrdEnt.getPaymentList();

    BigDecimal installmentDownpayment = new BigDecimal("0.00");
    String installmentCreditCard = null;
    String downpaymentCreditCard = null;
    int downpaymentCreditCardCount = 0;

    for (/* each */PaymentTO aPaymentTO : /* in */paymentList) {

      if (aPaymentTO.getPayType() == PaymentTO.PaymentType.INSTALLMENT) {
        dtiUpgrdEnt.setInstallmentRequest(true);

        if (installmentCreditCard != null) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
              "Only one installment credit card permitted on an installment transaction.");
        }

        if (aPaymentTO.getInstallment().getCreditCard().getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {
          installmentCreditCard = aPaymentTO.getInstallment().getCreditCard().getCcNbr();
        } else {
          installmentCreditCard = aPaymentTO.getInstallment().getCreditCard().getCcTrack1();
        }

        // RULE: On installment payments, CCName cannot exceed 25 characters (As
        // of 2.16.2, JTL)
        if (aPaymentTO.getInstallment().getCreditCard().getCcName().length() > MAXCARDHOLDERNAME) {
          throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
              "Installment Credit Card CC Name exceeded maximum character length of " + MAXCARDHOLDERNAME
                  + " (length of what was sent was " + aPaymentTO.getInstallment().getCreditCard().getCcName().length()
                  + ").");
        }

      } else if (aPaymentTO.getPayType() == PaymentTO.PaymentType.CREDITCARD) {

        downpaymentCreditCardCount++;

        if (aPaymentTO.getCreditCard().getCcManualOrSwipe() == CreditCardTO.CreditCardType.CCMANUAL) {
          downpaymentCreditCard = aPaymentTO.getCreditCard().getCcNbr();
        } else {
          downpaymentCreditCard = aPaymentTO.getCreditCard().getCcTrack1();
        }

        installmentDownpayment = aPaymentTO.getPayAmount();

      }

    }

    if (dtiUpgrdEnt.isInstallmentRequest() == false) {
      return;
    }

    if (downpaymentCreditCardCount > 1) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment must be accompanied by only one additional credit card.");
    }

    if (installmentCreditCard == null) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment transaction missing installment credit card.");
    }
    if (downpaymentCreditCard == null) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment transaction missing downpayment credit card.");
    }
    if (!downpaymentCreditCard.equalsIgnoreCase(installmentCreditCard)) {
      throw new DTIException(PaymentRules.class, DTIErrorCode.INVALID_PAYMENT_TYPE,
          "Installment must be the SAME credit card as the other credit card on the purchase.");
    }

    // Calculate how much of a down payment should have been provided.
    BigDecimal singleStandardDownpayment = null;
    for (/* each */TPLookupTO aTPLookup : /* in */tpLookups) {
      if ((aTPLookup.getLookupDesc().compareToIgnoreCase("Downpayment") == 0)
          && (aTPLookup.getLookupType() == TPLookupTO.TPLookupType.INSTALLMENT)) {

        try {
          singleStandardDownpayment = new BigDecimal(aTPLookup.getLookupValue());
        } catch (NumberFormatException nfe) {
          throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
              "PaymentRules.validateInstallmentDownpayment attempted to read downpayment from DB, and it wasn't a number:"
                  + aTPLookup.getLookupValue());
        }
      }
    }
    if (singleStandardDownpayment == null) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
          "PaymentRules.validateInstallmentDownpayment Installmnt Downpayment TP_LOOKUP for provider not populated.");
    }

    // Determine the quantity of non-shipping product on the order
    HashMap<String, DBProductTO> productCodeMap = dtiTxn.getDbProdMap();
    ArrayList<TicketTO> tktList = dtiUpgrdEnt.getTicketList();
    int quantity = 0;

    for (/* each */TicketTO aTicketTO : /* in */tktList) {
      String pdtCode = aTicketTO.getProdCode();
      String dayClass = productCodeMap.get(pdtCode).getDayClass();
      if (dayClass.compareToIgnoreCase("SHIP") != 0) {
        quantity = quantity + aTicketTO.getProdQty().intValue();
      }
    }

    // Determine how much down payment should be provided.
    BigDecimal stdPayTotal = singleStandardDownpayment.multiply(new BigDecimal(quantity));

    // Compare expected down payment to actual down payment.
    if (stdPayTotal.compareTo(installmentDownpayment) != 0) {
      throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_PAYMENT_AMOUNT,
          "The total downpayment expected of " + stdPayTotal + " does not equal what was on the upgrade entitlement: "
              + installmentDownpayment);
    }

    // Preserve down-payment amount for later use in the out-bound transaction
    // to eGalaxy.
    dtiUpgrdEnt.setInstallmentDownpayment(installmentDownpayment);

    return;
  }
  
}
