package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;

/**
 * 
 * This class contains all of the utlities necessary to test the Omni Ticket
 * transformations. These tests used the standard field values (below) in
 * concert with XML to ensure that field translations occur when and how they
 * are supposed to.
 * 
 * @author lewit019
 * 
 */
public class OTTestUtil {

  // Standard Field Values
  public final static String ALREADYUSED = "1";
  public final static String ASSOCIATIONID = "21";
  public final static String ATTRIBUTECMD = "Insert";
  public final static String ATTRIBUTEKEY = "4";
  public final static String ATTRIBUTEVALUE = "100002684";
  public final static String ATTRIBUTETYPE = "7";
  public final static String ATTRIBUTEFLAG = "NotEditable";

  public final static String AUTHERRCODE = "0";
  public final static String AUTHMESSAGE = "APPROVED            ";
  public final static String AUTHNUMBER = "09997Z    ";

  public final static String BARCODE = "77777777777777777777";
  public final static String BIOMETRICLEVEL = "0";

  public final static String CARDTYPE = "0";
  public final static String CLIENTCATEGORY = "WW ";
  public final static String CLIENTSUBTYPE = "64";
  public final static String CLIENTTYPE = "P";
  public final static String CLIENTUNIQUEID = "0";
  public final static String CMDTYPE = "Create";

  public final static String DATE = "09-06-28";
  public final static String DATETIME = "09-06-28 16:17";
  public final static String DEPOSITAMOUNT = "0.00";
  public final static String DESCRPTION = "6 DAY (1 THEME PARK PER DAY)   AGES(10+)";
  public final static String DENYUPGRADEONUPGRADE = "true";

  public final static String ENCODED = "1";
  public final static String ENDDATE = "30-12-31";
  public final static String ERRORCODE = "21";
  public final static String ERRORSHORTDESCRIPTION = "OP_FAIL_CC_PAYM_NOT_APP";
  public final static String ERRORDESCRIPTION = "CREDIT CARD PAYMENT NOT APPROVED";
  public final static String EXTERNALTKTCODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public final static String FIELDVALUE = "FIELDVALUE";
  public final static String FIELDTYPE = "28";

  public final static String GATE = "1";
  public final static String GROUPQUANTITY = "0";

  public final static String IATA = "382010293";
  public final static String ITEMALPHACODE = "FJ0AY";
  public final static String ITEMNUMCODE = "25834";
  public final static String ITEMONE = "1";
  public final static String ITEMSTATUS = "0";
  public final static String ITEMTWO = "2";
  public final static String ITEMTYPE = "T";

  public final static String LAYOUT = "41";
  public final static String LASTUSAGEDATE = "30-12-13";

  public final static String MAGCODE = " FFFFFFFFFFFFFFFFFFFFFFFF ";
  public final static String MEMBERID = "3744439";
  public final static String MASTERCODE = "4444";

  public final static String NONVALUEDQTY = "0";

  public final static String OPERATINGAREA = "WDWIT";

  public final static String QUANTITY = "3";

  public final static String PAID = "1";
  public final static String PASSWORD = "11111";
  public final static String PAYAMOUNT = "835.24";
  public final static String PAYAVSZIPCODE = "31093";
  public final static String PAYAVSSTREET = "964 OAKTREE LN.";
  public final static String PAYCARDNUM = "XXXXXXXXXXXX1234";
  public final static String PAYCARDEXP = "1015";
  public final static String PAYMENTTYPECCARD = "2";
  public final static String PAYMENTTYPEVOUCHER = "6";
  public final static String PRICE = "239.63";
  public final static String PRINTED = "1";
  public final static String PRINTEDPRICE = "239.63";

  public final static String RESERVATIONID = "1231231";
  public final static String RESERVATIONNOTE = "This is a reservation note";
  public final static String REFERENCENUMBER = " 12345678";
  public final static String RESCODE = "RESCODE123";
  public final static String REMAININGVALUE = "350.48";
  public final static String REQUESTNUMBER = "7654321";
  public final static String REQRESPICKUPDATE = "2009-06-15";
  public final static String REQRESPICKUPTYPE = "001";
  public final static String REQCLIENTLANGUAGE = "003";
  public final static String REQSUBTYPE_CREATETXN = "CreateTransaction";
  public final static String REQSUBTYPE_MANAGERES = "ManageReservation";
  public final static String REQSUBTYPE_QUERYTICKET = "QueryTicket";
  public final static String REQSUBTYPE_UPDATETICKET = "UpdateTicket";
  public final static String REQSUBTYPE_UPGRADETICKET = "UpgradeTicket";
  public final static String REQSUBTYPE_UPDATETXN = "UpdateTransaction";
  public final static String REQSUBTYPE_VOIDTICKET = "VoidTicket";
  public final static String REQTYPE_CREATE = "Create";
  public final static String REQTYPE_MANAGE = "Manage";
  public final static String REQTYPE_QUERY = "Query";
  public final static String REQTYPE_UPDATE = "Update";
  public final static String REQTYPE_UPGRADE = "Upgrade";
  public final static String REQTYPE_VOID = "Void";
  public final static String RESPICKUPAREA = "98";
  public final static String RESPICKUPTYPE = "1";
  public final static String RESSTATUS = "C";
  public final static String RRN = "666666666666";
  public final static String RSPCLIENTLANGUAGE = "3";
  public final static String RSPRESPICKUPDATE = "09-06-15";

  public final static String SALESTYPE = "3";
  public final static String SELLERID = "351";
  public final static String SESSIONID = "7777777";
  public final static String SITENUMBER = "93";
  public final static String STARTDATE = "09-06-15";

  public final static String TAGS_ALL = "All";
  public final static String TAGS_TICKETINFOVOID = "TicketInfoVoid";
  public final static String TAGS_TICKETINFO = "TicketInfo";
  public final static String TAGS_TRANSACTIONDSSN = "TransactionDSSN";
  public final static String TAGS_TRANSACTIONCOD = "TransactionCOD";
  public final static String TAGS_PAYMENTINFO = "PaymentInfo";
  public final static String TAGS_UPGRADETICKETINFO = "UpgradeTicketInfo";
  public final static String TAGS_UPDATETICKETINFO = "UpdateTicketInfo";
  public final static String TAX = "14.63";
  public final static String TAXEXEMPT = "0";
  public final static String TCOD = "99999999999999999";
  public final static String TDSSNDATE_CCYY = "2009-06-15";
  public final static String TDSSNDATE_YY = "09-06-15";
  public final static String TDSSNSITE = "WDW";
  public final static String TDSSNSTATION = "CAS999";
  public final static String TDSSNTICKETID = "303";
  public final static String TICKETFLAGNUMBER = "0";
  public final static String TICKETATTRIBUTE = "0";
  public final static String TICKETNOTE = "I AM A TICKET NOTE";
  public final static String TICKETTYPE = "25834";
  public final static String TICKETNAME = "6D 1P             AD";
  public final static String TIME = "16:17";
  public final static String TOTALAMT = "935.24";
  public final static String TOTALTAX = "56.24";
  public final static String TRANDSSNSITE = "WDW";
  public final static String TRANDSSNSTATION = "CAS999";
  public final static String TRANDSSNDATE = "09-06-15";
  public final static String TRANDSSNTXNID = "123";
  public final static String TRANCOD = "99999999999999999";
  public final static String TRANNOTE = "I AM A TRANSACTION NOTE";
  public final static String TRANTIME = "12:48";
  public final static String TRUE = "true";

  public final static String UNIQUECODE = "1234";
  public final static String USERID = "2222";
  public final static String USAGETYPE = "4";

  public final static String VALIDATED = "1";
  public final static String VALUEDQTY = "10";
  public final static String VOIDCODE = "0";

  public final static String ZIPCODE = "32832";

  /**
   * Creates the Omni Ticket Request Header
   * 
   * @param requestType
   *          The request type
   * @param requestSubType
   *          The request sub-type
   * @return a fully generated Omni Ticket Header request Transfer Object.
   */
  public static OTHeaderTO createOTReqHeader(String requestType, String requestSubType) {

    OTHeaderTO otHdrTO = new OTHeaderTO();

    otHdrTO.setReferenceNumber(REFERENCENUMBER);
    otHdrTO.setRequestType(requestType);
    otHdrTO.setRequestSubType(requestSubType);
    otHdrTO.setOperatingArea(OPERATINGAREA);
    otHdrTO.setUserId(Integer.parseInt(USERID));
    otHdrTO.setPassword(Integer.parseInt(PASSWORD));

    return otHdrTO;
  }

  /**
   * Validates the Omni Ticket response Header Transfer Object.
   * 
   * @param otHdr
   *          the Omni Ticket response Header Transfer Object.
   * @param requestType
   *          the request type
   * @param requestSubType
   *          the request sub-type
   * @throws Exception
   *           for any validation errors.
   */
  public static void validateOTRspHeader(OTHeaderTO otHdr, String requestType, String requestSubType)
      throws Exception {

    if (REFERENCENUMBER.compareTo(otHdr.getReferenceNumber()) != 0)
      throw new Exception("Invalid OTHeaderTO.ReferenceNumber");

    Integer reqNum = Integer.decode(REQUESTNUMBER);
    if (reqNum.compareTo(otHdr.getRequestNumber()) != 0)
      throw new Exception("Invalid OTHeaderTO.RequestNumber");

    Integer sessionId = Integer.decode(SESSIONID);
    if (sessionId.compareTo(otHdr.getSessionId()) != 0)
      throw new Exception("Invalid OTHeaderTO.SessionId.");

    if (requestType.compareTo(otHdr.getRequestType()) != 0)
      throw new Exception("Invalid OTHeaderTO.RequestType");

    if (requestSubType.compareTo(otHdr.getRequestSubType()) != 0)
      throw new Exception("Invalid OTHeaderTO.RequestSubType");

    Integer valuedQty = Integer.decode(VALUEDQTY);
    if (valuedQty.compareTo(otHdr.getValueMsgValuedQty()) != 0)
      throw new Exception("Invalid OTHeaderTO.ValueMsg.ValuedQty");

    Integer nonValuedQty = Integer.decode(NONVALUEDQTY);
    if (nonValuedQty.compareTo(otHdr.getValueMsgNonValuedQty()) != 0)
      throw new Exception("Invalid OTHeaderTO.ValueMsg.NonValuedQty");

    return;
  }

  /**
   * Validates the Omni Ticket Error Transfer Object.
   * 
   * @param otErr
   *          the Omni Ticket Error Transfer Object.
   * @throws Exceptionn
   *           for any validation error.
   */
  public static void validateOTRspError(OTErrorTO otErr) throws Exception {

    Integer errorCode = Integer.decode(ERRORCODE);
    if (errorCode.compareTo(otErr.getErrorCode()) != 0)
      throw new Exception("Invalid OTErrorTO.ErrorCode");

    if (ERRORSHORTDESCRIPTION.compareTo(otErr.getErrorShortDescription()) != 0)
      throw new Exception("Invalid OTErrorTO.ErrorShortDescription");

    if (ERRORDESCRIPTION.compareTo(otErr.getErrorDescription()) != 0)
      throw new Exception("Invalid OTErrorTO.ErrorDescription");

    return;
  }

  /**
   * Converts yy-MM-dd to a GregorianCalendar format, used in the transfer
   * objects.
   * 
   * @param dateString
   *          the properly formatted date string
   * @return the date as a GregorianCalendar object
   * @throws Exception
   *           for a non-parsable date string.
   */
  public static GregorianCalendar convertYYMMDDtoGC(String dateString) throws Exception {

    GregorianCalendar gcCal = (GregorianCalendar) GregorianCalendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    Date aDate = null;
    try {
      aDate = sdf.parse(dateString);
    } catch (Exception e) {
      throw new Exception("Couldn't parse date string " + dateString + ": " + e.toString());
    }
    gcCal.setTime(aDate);

    return gcCal;
  }

  /**
   * Integer validating version of a series of overloaded methods that are used
   * to validate response XML messages.
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      Integer providedValue) throws Exception {

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    Integer validValue = Integer.decode(expectedValue);
    if (validValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * BigInteger validating version of a series of overloaded methods that are
   * used to validate response XML messages.
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      BigInteger providedValue) throws Exception {

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    BigInteger validValue = new BigInteger(expectedValue);
    if (validValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * BigDecimal validating version of a series of overloaded methods that are
   * used to validate response XML messages.
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      BigDecimal providedValue) throws Exception {

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    BigDecimal validValue = new BigDecimal(expectedValue);
    if (validValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * String validating version of a series of overloaded methods that are used
   * to validate response XML messages.
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      String providedValue) throws Exception {

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    if (expectedValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * Boolean validating version of a series of overloaded methods that are used
   * to validate response XML messages.
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      Boolean providedValue) throws Exception {

    if (expectedValue.compareTo("1") == 0)
      expectedValue = "true";
    else
      expectedValue = "false";

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    Boolean validValue = new Boolean(expectedValue);
    if (validValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * GregorianCalendar validating version of a series of overloaded methods that
   * are used to validate response XML messages (Takes only YY-MM-DD).
   * 
   * @param parentStanza
   *          used for formatting the exception text on a failure, the parent
   *          stanza containing the tag being evaluated.
   * @param childStanza
   *          used for formatting the exception text on a failure, the stanza
   *          being evaluated.
   * @param expectedValue
   *          the expected value (always string).
   * @param providedValue
   *          the value provided in the response
   * @throws Exception
   *           for any validation error (missing or invalid).
   */
  public static void validateValue(String parentStanza, String childStanza, String expectedValue,
      GregorianCalendar providedValue) throws Exception {

    if (providedValue == null)
      throw new Exception(parentStanza + " object had no " + childStanza);
    GregorianCalendar validValue = OTTestUtil.convertYYMMDDtoGC(expectedValue);
    if (validValue.compareTo(providedValue) != 0)
      throw new Exception(parentStanza + " object had invalid " + childStanza);

    return;
  }

  /**
   * Validates a transaction DSSN.
   * 
   * @param txnDssn
   *          the DSSN object to be evaluated
   * @throws Exception
   *           for any validation failures.
   */
  public static void validateTranDSSN(OTTransactionDSSNTO txnDssn) throws Exception {

    // TransactionDSSN
    if (txnDssn == null) {
      throw new Exception("Object OTTicketInfoTO has a null TransactionDssn.");
    }

    // Site
    OTTestUtil.validateValue("OTTransactionDSSNTO", "Site", OTTestUtil.TRANDSSNSITE, txnDssn
        .getSite());

    // Station
    OTTestUtil.validateValue("OTTransactionDSSNTO", "Station", OTTestUtil.TRANDSSNSTATION, txnDssn
        .getStation());

    // Date
    OTTestUtil.validateValue("OTTransactionDSSNTO", "Date", OTTestUtil.TRANDSSNDATE, txnDssn
        .getDate());

    // TransactionId
    OTTestUtil.validateValue("OTTransactionDSSNTO", "TransactionId", OTTestUtil.TRANDSSNTXNID,
        txnDssn.getTransactionId());

    return;

  }

  /**
   * Validates a standard payment list
   * 
   * @param paymentList
   *          the list of Omni Ticket Payment Transfer Objects being evaluated
   * @throws Exception
   *           for any validation failures.
   */
  public static void validatePaymentList(ArrayList<OTPaymentTO> paymentList) throws Exception {

    if ((paymentList == null) || (paymentList.size() == 0))
      throw new Exception("Transaction object had no or empty PaymentInfo list.");

    if (paymentList.size() != 2)
      throw new Exception("PaymentInfo list did not have 2 members, as expected.");

    String payNodeName = "OTPaymentTO";

    int itemNum = 0;

    // Payment 1: Credit Card
    OTPaymentTO aPayment = paymentList.get(0);

    // Item
    BigInteger item = aPayment.getPayItem();
    if (item == null)
      throw new Exception("OTPaymentTO object had no Item");
    BigInteger validItem = BigInteger.valueOf(++itemNum);
    if (validItem.compareTo(item) != 0)
      throw new Exception("OTPaymentTO object had invalid Item");

    // PaymentType
    OTTestUtil.validateValue(payNodeName, "PaymentType", OTTestUtil.PAYMENTTYPECCARD, aPayment
        .getPayTypeInt());

    // PaymentAmount
    OTTestUtil.validateValue(payNodeName, "PaymentAmount", OTTestUtil.PAYAMOUNT, aPayment
        .getPayAmount());

    // Credit Card
    OTCreditCardTO ccard = aPayment.getCreditCard();
    if (ccard == null)
      throw new Exception("OTPaymentTO object had no CreditCard");
    String ccNodeName = "CreditCard";

    // CreditCard.CardType
    OTTestUtil.validateValue(ccNodeName, "CardType", OTTestUtil.CARDTYPE, ccard
        .getCardTypeInteger());

    // CreditCard.AuthErrorCode
    OTTestUtil.validateValue(ccNodeName, "AuthErrorCode", OTTestUtil.AUTHERRCODE, ccard
        .getAuthErrorCode());

    // CreditCard.AuthNumber
    OTTestUtil
        .validateValue(ccNodeName, "AuthNumber", OTTestUtil.AUTHNUMBER, ccard.getAuthNumber());

    // CreditCard.AuthMessage
    OTTestUtil.validateValue(ccNodeName, "AuthMessage", OTTestUtil.AUTHMESSAGE, ccard
        .getAuthMessage());

    // CreditCard.CCNumber
    OTTestUtil.validateValue(ccNodeName, "CCNumber", OTTestUtil.PAYCARDNUM, ccard.getCcNumber());

    // CreditCard.RRN (ignored)

    // Payment 2: Voucher
    aPayment = paymentList.get(1);

    // Item
    item = aPayment.getPayItem();
    if (item == null)
      throw new Exception("OTPaymentTO object had no Item");
    validItem = BigInteger.valueOf(++itemNum);
    if (validItem.compareTo(item) != 0)
      throw new Exception("OTPaymentTO object had invalid Item");

    // PaymentType
    OTTestUtil.validateValue(payNodeName, "PaymentType", OTTestUtil.PAYMENTTYPEVOUCHER, aPayment
        .getPayTypeInt());

    // PaymentAmount
    OTTestUtil.validateValue(payNodeName, "PaymentAmount", OTTestUtil.PAYAMOUNT, aPayment
        .getPayAmount());

    // Voucher MasterCode
    OTVoucherTO aVoucher = aPayment.getVoucher();
    if (aVoucher == null) {
      throw new Exception(payNodeName + " object has null Voucher.");
    }
    OTTestUtil.validateValue(payNodeName, "Voucher.MasterCode", OTTestUtil.MASTERCODE, aVoucher
        .getMasterCode());

    // Voucher UniqueCode
    OTTestUtil.validateValue(payNodeName, "Voucher.UniqueCode", OTTestUtil.UNIQUECODE, aVoucher
        .getUniqueCode());

    return;
  }

  /**
   * Validates a ticket on the response.
   * 
   * @param aTicket
   *          an Omni Ticket Ticket Transfer Object.
   * @throws Exception
   *           for any validation failures.
   */
  public static void validateTicket(OTTicketTO aTktTO) throws Exception {

    if (aTktTO == null)
      throw new Exception("Transaction object had no Ticket object.");

    // TDSSN Date
    OTTestUtil.validateValue("OTTicketTO", "TDSSN.Date", OTTestUtil.TDSSNDATE_YY, aTktTO
        .getTdssnDate());

    // TDSSN Site
    OTTestUtil.validateValue("OTTicketTO", "TDSSN.Site", OTTestUtil.TDSSNSITE, aTktTO
        .getTdssnSite());

    // TDSSN Station
    OTTestUtil.validateValue("OTTicketTO", "TDSSN.Station", OTTestUtil.TDSSNSTATION, aTktTO
        .getTdssnStation());

    // TDSSN TicketId
    OTTestUtil.validateValue("OTTicketTO", "TDSSN.TicketId", OTTestUtil.TDSSNTICKETID, aTktTO
        .getTdssnTicketId());

    // TCOD
    OTTestUtil.validateValue("OTTicketTO", "TCOD", OTTestUtil.TCOD, aTktTO.getTCOD());

    // BarCode
    OTTestUtil.validateValue("OTTicketTO", "BarCode", OTTestUtil.BARCODE, aTktTO.getBarCode());

    // MagCode
    OTTestUtil.validateValue("OTTicketTO", "MagCode", OTTestUtil.MAGCODE, aTktTO.getMagTrack());

    // ExternalTicketCode
    OTTestUtil.validateValue("OTTicketTO", "ExternalTicketCode", OTTestUtil.EXTERNALTKTCODE, aTktTO
        .getExternalTicketCode());

    return;

  }

  /**
   * Validates the ticket flag list on the response
   * 
   * @param flagList
   *          The list of integer ticket values.
   * @throws Exception
   *           for any validation failures.
   */
  public static void validateTicketFlagList(ArrayList<Integer> flagList) throws Exception {


    // TicketFlag
    if ((flagList == null) || (flagList.size() < 1)) {
      throw new Exception("Object OTTicketInfoTO had an empty or null TicketFlag list.");
    }

    Integer aFlag = flagList.get(0);

    // TicketFlagNumber
    OTTestUtil.validateValue("OTTicketInfoTO", "TicketFlagNumber", OTTestUtil.TICKETFLAGNUMBER,
        aFlag);

    return;
  }

}
