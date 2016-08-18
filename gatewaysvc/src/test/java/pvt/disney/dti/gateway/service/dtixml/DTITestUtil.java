package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * Series of utilities used by the DTI test cases.
 * 
 * @author lewit019
 * 
 */
public class DTITestUtil {

  public final static String AGENT = "Pete";

  public final static String APPASSINFO = "APPASSINFO";

  public final static String BILLADDR1 = "123 innovation way";

  public final static String BILLADDR2 = "apt a";

  public final static String BILLCITY = "lake buena vista";

  public final static String BILLCOUNTRY = "USA";

  public final static String BILLEMAIL = "mickey.mouse@disney.com";

  public final static String BILLFIRSTNAME = "mickey";

  public final static String BILLLASTNAME = "mouse";

  public final static String BILLNAME = "mickey mouse";

  public final static String BILLSTATE = "FL";

  public final static String BILLTELEPHONE = "4078285888";

  public final static String BILLZIP = "32832";

  public final static String CCAUTHCODE = "0";

  public final static String CCAUTHNUMBER = "44582B    ";

  public final static String CCAUTHSYSRESPONSE = "ICUNUROK";

  public final static String CCCAVV = "B23";

  public final static String CCECOMMERCE = "00";

  public final static String CCEXPIRATION = "1110";

  public final static String CCNAME = "mickey mouse";

  public final static String CCNBR = "4444333322221111";

  public final static String CCNUMBER = "4444333322221111";

  public final static String CCSTREET = "123 innovation way";

  public final static String CCTRACK1 = "CCTrackOneData";

  public final static String CCTRACK2 = "CCTrackTwoData";

  public final static String CCTYPE = "VI";

  public final static String CCVV = "A12";

  public final static String CCZIPCODE = "32832";

  public final static String CLIENTCATEGORY = "WW";

  public final static String CLIENTID = "6292288";

  public final static String CLIENTTYPE = "Private";

  public final static String CMDACTOR = "MGR";

  public final static String CMDATTRNAMEONE = "AttributeNameOne";

  public final static String CMDATTRNAMETWO = "AttributeNameTwo";

  public final static String CMDATTRVALUONE = "AttributeValueOne";

  public final static String CMDATTRVALUTWO = "AttributeValueTwo";

  public final static String CMDDATETIME = "2009-01-23 11:38:20.456";

  public final static String CMDDEVICE = "DEVICE";

  public final static String CMDDURATION = "0.00";

  public final static String CMDINVOICE = "942871739062978";

  public final static String CMDITEM = "1";

  public final static String CMDMARKET = "32832";

  public final static String CMDNOTE = "This is a command note.";

  public final static String CMDOPERATOR = "BOBSMITH";

  public final static String CMDTIMEOUT = "45";

  public final static String COMMANDCOUNT = "1";

  public final static String COMMANDHEADER = "CommandHeader";

  public final static String COMMMETHOD = "Network";

  public final static String COMMPROTOCOL = "IP";

  public final static String DEMOLANGUAGE = "en";

  public final static String ELIGGROUP = "EligibilityGroup";

  public final static String ELIGMEMBER = "EligibilityMember";

  public final static String ERRORCLASS = "Servelet";

  public final static String ERRORCODE = "799";

  public final static String ERRORTEXT = "Undefined Critical Error";

  public final static String ERRORTYPE = "Critical";

  public final static String GCAUTHCODE = "ANAUTHCODE";

  public final static String GCAUTHNUMBER = "44582B    ";

  public final static String GCAUTHSYSRESPONSE = "ICUNUROK";

  public final static String GCNBR = "1111222233334444";

  public final static String GCNUMBER = "1111222233334444";

  public final static String GCPROMOEXPDATE = "2009-12-31";
  
  public final static String GCSTARTDATE = "2009-12-31";

  public final static String GCREMAININGBALANCE = "1.23";

  public final static String GCTRACK1 = "GCTrackOneData";

  public final static String GCTRACK2 = "GCTrackTwoData";

  public final static String IATA = "A02131A912";

  public final static String MAINCODE = "VoucherMainCode";

  public final static String NOTE = "This is a reservation note.";

  public final static String PAYAMOUNT = "1000.01";

  public final static String PAYLOADHEADER = "PayloadHeader";

  public final static String PAYLOADID = "99999999999999999999";

  public final static String PRODCODE = "PRD11";

  public final static String PRODDESC = "This is a product description.";

  public final static String PRODPARTS = "1";

  public final static String PRODPRICE = "500.05";

  public final static String PRODQTY = "5";

  public final static String PRODRECEIPTMSG = "This is a product receipt message.";

  public final static String PRODTAX1 = "50.05";

  public final static String PRODTAX2 = "5.50";

  public final static String RECEIPTMESSAGE = "This is a receipt message.";

  public final static String REQUESTTYPE = "Create";

  public final static String RESCODE = "DOL1234567";
  
  public final static String RESNUMBER = "TSK123456";

  public final static String RESCREATEDATE = "2009-01-23";

  public final static String RESPICKUPAREA = "WillCall";

  public final static String RESPICKUPDATE = "2009-01-23";

  public final static String RESPONSETYPE = "Create";

  public final static String RESSALESTYPE = "Presale";

  public final static String SALESREP = "BillyBob";

  public final static String SHIPADDR1 = "321 contact street";

  public final static String SHIPADDR2 = "apt b";

  public final static String SHIPCITY = "warner robins";

  public final static String SHIPCOUNTRY = "USA";

  public final static String SHIPEMAIL = "ludwig.vondrake@disney.com";

  public final static String SHIPFIRSTNAME = "ludwig";

  public final static String SHIPLASTNAME = "vondrake";

  public final static String SHIPNAME = "ludwig vondrake";

  public final static String SHIPSTATE = "GA";

  public final static String SHIPTELEPHONE = "4789231995";

  public final static String SHIPZIP = "31093";

  public final static String STATUSITEM = "StatusItem";

  public final static String STATUSVALUE = "StatusValue";

  public final static String TARGET = "PROD-WDW";

  public final static String TAXEXEMPTCODE = "987654321ABC";

  public final static String TKTAGEGROUP = "A";

  public final static String TKTBARCODE = "33333333333333333333";

  public final static String TKTBROKER = "DTI01";

  public final static String TKTDSSNDATE = "2008-11-01";

  public final static String TKTDSSNNBR = "222";

  public final static String TKTDSSNSITE = "WMK";

  public final static String TKTDSSNSTATION = "111";

  public final static String TKTERRORCLASS = "Ticket";

  public final static String TKTERRORCODE = "853";

  public final static String TKTERRORTEXT = "Ticket Already Voided";

  public final static String TKTERRORTYPE = "Error";

  public final static String TKTEXTERNAL = "5555555555555555";

  public final static String TKTMAGTRACK1 = " BBBBBBBBBBBBBBBBBB ";

  public final static String TKTMAGTRACK2 = "22=222222";

  public final static String TKTMARKET = "32832";

  public final static String TKTMEDIATYPE = "S";

  public final static String TKTNID = "44444444444444444";

  public final static String TKTNOTE = "This is a ticket note.";

  public final static String TKTPASSCLASS = "PH";

  public final static String TKTPASSRENEW = "PassRenew";

  public final static String TKTPASSTYPE = "SEASONAL";

  public final static String TKTPRICE = "500.05";

  public final static String TKTPROVIDER = "WDW";

  public final static String TKTRESIDENT = "Resident";

  public final static String TKTSECURITYLEVEL = "1";

  public final static String TKTSHELL = "78";

  public final static String TKTTAX = "50.05";

  public final static String TKTTRANDATE = "2009-01-23";

  public final static String TKTTRANNBR = "98";

  public final static String TKTTRANNID = "77777777777777777";

  public final static String TKTTRANSITE = "WDW";

  public final static String TKTTRANSTATION = "CAS002";

  public final static String TKTVALIDEND = "2009-09-20";

  public final static String TKTVALIDSTART = "2008-09-20";

  public final static String TRANSMITDATETIME = "2009-01-23 11:38:20.123";

  public final static String TSLOCATION = "A352R";

  public final static String TSMAC = "TSMACTSMAC";

  public final static String TSPAYLOADID = " 8888888";

  public final static String TSSECURITY = "WaltDisney1";

  public final static String TSSYSTEM = "CC";

  public final static String UNIQUECODE = "VoucherUniqueCode";

  public final static String VERSION = "1.0";

  /**
   * Validates the PayloadHeaderTO created during the test for specific values.
   * Ensures that everything that was in the payload header made it correctly
   * into the objects for processing.
   * 
   * @param pHdrTO
   * @throws Exception
   */
  public static void validatePayloadHeaderReqTO(PayloadHeaderTO pHdrTO) throws Exception {

    // PayloadID
    if (pHdrTO.getPayloadID().compareTo(DTITestUtil.PAYLOADID) != 0)
      throw new Exception("Invalid PayloadID: " + pHdrTO.getPayloadID());

    // Target
    if (pHdrTO.getTarget().compareTo(DTITestUtil.TARGET) != 0)
      throw new Exception("Invalid Target: " + pHdrTO.getTarget());

    // Version
    if (pHdrTO.getVersion().compareTo(DTITestUtil.VERSION) != 0)
      throw new Exception("Invalid Version: " + pHdrTO.getVersion());

    // CommProtocol & CommMethod
    if (pHdrTO.getCommProtocol().compareTo(DTITestUtil.COMMPROTOCOL) != 0)
      throw new Exception("Invalid CommProtocol: " + pHdrTO.getCommProtocol());
    if (pHdrTO.getCommMethod().compareTo(DTITestUtil.COMMMETHOD) != 0)
      throw new Exception("Invalid CommMethod: " + pHdrTO.getCommMethod());

    // TransmitDate & TransmitTime
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date aDate = null;
    try {
      aDate = sdf.parse(DTITestUtil.TRANSMITDATETIME);
    } catch (Exception e) {
      System.out.println("Invalid TRANSMITDATETIME format in DLRTestUtil.");
    }
    GregorianCalendar transmitDateTime = (GregorianCalendar) GregorianCalendar.getInstance();
    transmitDateTime.setTime(aDate);

    if (transmitDateTime.compareTo(pHdrTO.getTransmitDateTime()) != 0)
      throw new Exception("Invalid TransmitDate/TransmitTime: "
          + sdf.format(pHdrTO.getTransmitDateTime().getTime()));

    // TktSeller
    TktSellerTO tktSellerTO = pHdrTO.getTktSeller();
    if (tktSellerTO.getTsMac().compareTo(DTITestUtil.TSMAC) != 0)
      throw new Exception("Invalid TktSellerTSMAC: " + tktSellerTO.getTsMac());
    if (tktSellerTO.getTsSystem().compareTo(DTITestUtil.TSSYSTEM) != 0)
      throw new Exception("Invalid TktSellerTSSystem: " + tktSellerTO.getTsSystem());
    if (tktSellerTO.getTsLocation().compareTo(DTITestUtil.TSLOCATION) != 0)
      throw new Exception("Invalid TktSellerTSLocation: " + tktSellerTO.getTsLocation());
    if (tktSellerTO.getTsSecurity().compareTo(DTITestUtil.TSSECURITY) != 0)
      throw new Exception("Invalid TktSellerTSSecurity: " + tktSellerTO.getTsSecurity());

    // CommandCount
    if (pHdrTO.getCommandCount().compareTo(new BigInteger(DTITestUtil.COMMANDCOUNT)) != 0)
      throw new Exception("Invalid CommandCount: " + pHdrTO.getCommandCount().toString());

    return;

  }

  /**
   * Validate command header req to.
   * 
   * @param cHdrTO
   *          the c hdr to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateCommandHeaderReqTO(CommandHeaderTO cHdrTO) throws Exception {

    // CmdItem
    if (cHdrTO.getCmdItem().compareTo(new BigInteger(DTITestUtil.CMDITEM)) != 0)
      throw new Exception("Invalid CmdItem: " + cHdrTO.getCmdItem().toString());

    // CmdTimeout
    if (cHdrTO.getCmdTimeout().compareTo(new BigInteger(DTITestUtil.CMDTIMEOUT)) != 0)
      throw new Exception("Invalid CmdTimeout: " + cHdrTO.getCmdTimeout().toString());

    // CmdDate & Time
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date aDate = null;
    try {
      aDate = sdf.parse(DTITestUtil.CMDDATETIME);
    } catch (Exception e) {
      System.out.println("Invalid CMDDATETIME format in DLRTestUtil.");
    }
    GregorianCalendar commandDateTime = (GregorianCalendar) GregorianCalendar.getInstance();
    commandDateTime.setTime(aDate);

    if (commandDateTime.compareTo(cHdrTO.getCmdDateTime()) != 0)
      throw new Exception("Invalid CmdDate/CmdTime: " + cHdrTO.getCmdDateTime().toString());

    // CmdInvoice
    if (cHdrTO.getCmdInvoice().compareTo(DTITestUtil.CMDINVOICE) != 0)
      throw new Exception("Invalid CmdInvoice: " + cHdrTO.getCmdInvoice());

    // CmdDevice
    if (cHdrTO.getCmdDevice().compareTo(DTITestUtil.CMDDEVICE) != 0)
      throw new Exception("Invalid CmdDevice: " + cHdrTO.getCmdInvoice());

    // CmdOperator
    if (cHdrTO.getCmdOperator().compareTo(DTITestUtil.CMDOPERATOR) != 0)
      throw new Exception("Invalid CmdOperator: " + cHdrTO.getCmdOperator());

    // CmdActor
    if (cHdrTO.getCmdActor().compareTo(DTITestUtil.CMDACTOR) != 0)
      throw new Exception("Invalid CmdActor: " + cHdrTO.getCmdActor());

    // CmdNote
    if (cHdrTO.getCmdNote().compareTo(DTITestUtil.CMDNOTE) != 0)
      throw new Exception("Invalid CmdNote: " + cHdrTO.getCmdNote());

    // CmdMarket
    if (cHdrTO.getCmdMarket().compareTo(DTITestUtil.CMDMARKET) != 0)
      throw new Exception("Invalid CmdMarket: " + cHdrTO.getCmdMarket());

    // CmdAttributes
    ArrayList<CommandHeaderTO.CmdAttributeTO> attribList = cHdrTO.getCmdAttributeList();
    if (attribList.size() != 2)
      throw new Exception("Invalid CmdAttributes.  List contains: " + attribList.size());
    for /* each */(CommandHeaderTO.CmdAttributeTO anAttribute : /* in */attribList) {

      if (anAttribute.getAttribName().compareTo(DTITestUtil.CMDATTRNAMEONE) == 0) {
        if (anAttribute.getAttribValue().compareTo(DTITestUtil.CMDATTRVALUONE) != 0)
          throw new Exception("Invalid CmdAttribute value: " + anAttribute.getAttribValue());
      } else if (anAttribute.getAttribName().compareTo(DTITestUtil.CMDATTRNAMETWO) == 0) {
        if (anAttribute.getAttribValue().compareTo(DTITestUtil.CMDATTRVALUTWO) != 0)
          throw new Exception("Invalid CmdAttribute value: " + anAttribute.getAttribValue());

      } else
        throw new Exception("Invalid CmdAttribute name: " + anAttribute.getAttribName());
    }

    return;
  }

  /**
   * Validate dssn tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateDSSNTktId(TicketTO aTicketTO) throws Exception {

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();

    boolean hasDssnTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.DSSN_ID)
        hasDssnTktId = true;
    }

    if (!hasDssnTktId)
      throw new Exception("Ticket does not have expected DSSN TktId.");

    GregorianCalendar gCalDssnDateTO = aTicketTO.getDssnDate();
    GregorianCalendar gCalDssnDateTst = CustomDataTypeConverter
        .parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    if (gCalDssnDateTO.compareTo(gCalDssnDateTst) != 0)
      throw new Exception("Invalid TktDssn.TktDate value: "
          + CustomDataTypeConverter.printCalToDTIDateFormat(gCalDssnDateTO));

    if (aTicketTO.getDssnSite().compareTo(DTITestUtil.TKTDSSNSITE) != 0)
      throw new Exception("Invalid TktDSSN.TktSite value: " + aTicketTO.getDssnSite());

    if (aTicketTO.getDssnStation().compareTo(DTITestUtil.TKTDSSNSTATION) != 0)
      throw new Exception("Invalid TktDSSN.TktStation value: " + aTicketTO.getDssnStation());

    if (aTicketTO.getDssnNumber().compareTo(DTITestUtil.TKTDSSNNBR) != 0)
      throw new Exception("Invalid TktDSSN.TktNbr value: " + aTicketTO.getDssnNumber());
    return;
  }

  /**
   * Validate mag tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateTwoMagTktId(TicketTO aTicketTO) throws Exception {

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();

    boolean hasMagTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.MAG_ID)
        hasMagTktId = true;
    }

    if (!hasMagTktId)
      throw new Exception("Ticket does not have expected Mag TktId.");

    if (aTicketTO.getMagTrack1().compareTo(DTITestUtil.TKTMAGTRACK1) != 0)
      throw new Exception("Invalid MagTrack1 Ticket Value: " + aTicketTO.getMagTrack1());

    if (aTicketTO.getMagTrack2().compareTo(DTITestUtil.TKTMAGTRACK2) != 0)
      throw new Exception("Invalid MagTrack2 Ticket Value: " + aTicketTO.getMagTrack2());
    return;
  }
  
  /**
   * Validate mag tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateOneMagTktId(TicketTO aTicketTO) throws Exception {

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();

    boolean hasMagTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.MAG_ID)
        hasMagTktId = true;
    }

    if (!hasMagTktId)
      throw new Exception("Ticket does not have expected Mag TktId.");

    if (aTicketTO.getMagTrack1().compareTo(DTITestUtil.TKTMAGTRACK1) != 0)
      throw new Exception("Invalid MagTrack1 Ticket Value: " + aTicketTO.getMagTrack1());

    return;
  }

  /**
   * Validate barcode tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateBarcodeTktId(TicketTO aTicketTO) throws Exception {
    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();

    boolean hasBarcodeTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.BARCODE_ID)
        hasBarcodeTktId = true;
    }

    if (!hasBarcodeTktId)
      throw new Exception("Ticket does not have expected Barcode TktId.");

    if (aTicketTO.getBarCode().compareTo(DTITestUtil.TKTBARCODE) != 0)
      throw new Exception("Invalid Barcode Ticket Value: " + aTicketTO.getBarCode());

    return;
  }

  /**
   * Validate tkt nid tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateTktNIDTktId(TicketTO aTicketTO) throws Exception {
    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();
    boolean hasTktNIDTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.TKTNID_ID)
        hasTktNIDTktId = true;
    }

    if (!hasTktNIDTktId)
      throw new Exception("Ticket does not have expected TktNID TktId.");

    if (aTicketTO.getTktNID().compareTo(DTITestUtil.TKTNID) != 0)
      throw new Exception("Invalid TktNID Ticket Value: " + aTicketTO.getTktNID());

    return;
  }

  /**
   * Validate external tkt id.
   * 
   * @param aTicketTO
   *          the a ticket to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateExternalTktId(TicketTO aTicketTO) throws Exception {

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();
    boolean hasExternalTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.EXTERNAL_ID)
        hasExternalTktId = true;
    }

    if (!hasExternalTktId)
      throw new Exception("Ticket does not have expected External TktId.");

    if (aTicketTO.getExternal().compareTo(DTITestUtil.TKTEXTERNAL) != 0)
      throw new Exception("Invalid External Ticket Value: " + aTicketTO.getExternal());

    return;
  }

  /**
   * Validate cc manual payment.
   * 
   * @param aPaymentTO
   *          the a payment to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateCCManualPayment(PaymentTO aPaymentTO) throws Exception {

    PaymentTO.PaymentType payType = aPaymentTO.getPayType();
    if (payType != PaymentTO.PaymentType.CREDITCARD)
      throw new Exception("Invalid paytype found while trying to validate CCManual");

    CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();

    CreditCardTO.CreditCardType ccType = aCreditCardTO.getCcManualOrSwipe();

    if (ccType != CreditCardTO.CreditCardType.CCMANUAL)
      throw new Exception("Invalid Credit Card.  CCManual was expected.");

    if (aCreditCardTO.getCcNbr().compareTo(DTITestUtil.CCNBR) != 0)
      throw new Exception("Invalid CCNbr: " + aCreditCardTO.getCcNbr());

    if (aCreditCardTO.getCcExpiration().compareTo(DTITestUtil.CCEXPIRATION) != 0)
      throw new Exception("Invalid CCExpiration: " + aCreditCardTO.getCcExpiration());

    if (aCreditCardTO.getCcVV().compareTo(DTITestUtil.CCVV) != 0)
      throw new Exception("Invalid CCVV: " + aCreditCardTO.getCcVV());

    if (aCreditCardTO.getCcName().compareTo(DTITestUtil.CCNAME) != 0)
      throw new Exception("Invalid CCName: " + aCreditCardTO.getCcName());

    if (aCreditCardTO.getCcStreet().compareTo(DTITestUtil.CCSTREET) != 0)
      throw new Exception("Invalid CCStreet: " + aCreditCardTO.getCcStreet());

    if (aCreditCardTO.getCcZipCode().compareTo(DTITestUtil.CCZIPCODE) != 0)
      throw new Exception("Invalid CCZipcode: " + aCreditCardTO.getCcZipCode());

    if (aCreditCardTO.getCcCAVV().compareTo(DTITestUtil.CCCAVV) != 0)
      throw new Exception("Invalid CCCAVV: " + aCreditCardTO.getCcCAVV());

    if (aCreditCardTO.getCcEcommerce().compareTo(DTITestUtil.CCECOMMERCE) != 0)
      throw new Exception("Invalid CCEcommerce: " + aCreditCardTO.getCcEcommerce());

    if (aPaymentTO.getPayAmount().compareTo(new BigDecimal(DTITestUtil.PAYAMOUNT)) != 0)
      throw new Exception("Invalid PayAmount: " + aPaymentTO.getPayAmount());

    return;
  }

  /**
   * Validate cc swipe payment.
   * 
   * @param aPaymentTO
   *          the a payment to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateCCSwipePayment(PaymentTO aPaymentTO) throws Exception {

    PaymentTO.PaymentType payType = aPaymentTO.getPayType();
    if (payType != PaymentTO.PaymentType.CREDITCARD)
      throw new Exception("Invalid paytype found while trying to validate CCManual");

    CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();

    CreditCardTO.CreditCardType ccType = aCreditCardTO.getCcManualOrSwipe();

    if (ccType != CreditCardTO.CreditCardType.CCSWIPE)
      throw new Exception("Invalid Credit Card.  CCSwipe was expected.");

    if (aCreditCardTO.getCcTrack1().compareTo(DTITestUtil.CCTRACK1) != 0)
      throw new Exception("Invalid CCTrack1: " + aCreditCardTO.getCcTrack1());

    if (aCreditCardTO.getCcTrack2().compareTo(DTITestUtil.CCTRACK2) != 0)
      throw new Exception("Invalid CCTrack2: " + aCreditCardTO.getCcTrack2());

    if (aCreditCardTO.getCcVV().compareTo(DTITestUtil.CCVV) != 0)
      throw new Exception("Invalid CCVV: " + aCreditCardTO.getCcVV());

    if (aPaymentTO.getPayAmount().compareTo(new BigDecimal(DTITestUtil.PAYAMOUNT)) != 0)
      throw new Exception("Invalid PayAmount: " + aPaymentTO.getPayAmount());

    return;
  }

  /**
   * Validate gc manual payment.
   * 
   * @param aPaymentTO
   *          the a payment to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateGCManualPayment(PaymentTO aPaymentTO) throws Exception {

    PaymentTO.PaymentType payType = aPaymentTO.getPayType();
    if (payType != PaymentTO.PaymentType.GIFTCARD)
      throw new Exception("Invalid paytype found while trying to validate Gift Card");

    GiftCardTO gc = aPaymentTO.getGiftCard();

    GiftCardTO.GiftCardType gcType = gc.getGcManualOrSwipe();

    if (gcType != GiftCardTO.GiftCardType.GCMANUAL)
      throw new Exception("Invalid Gift Card.  GCManual was expected.");

    if (gc.getGcNbr().compareTo(DTITestUtil.GCNBR) != 0)
      throw new Exception("Invalid GCManual.GCNbr: " + gc.getGcNbr());
    
    GregorianCalendar gCalGcStartDateTst = CustomDataTypeConverter
    .parseYYYYMMDDDate(DTITestUtil.GCSTARTDATE);
    if (gc.getGcStartDate().compareTo(gCalGcStartDateTst)!=0)
      throw new Exception("Invalid GCStartDate.");

    if (aPaymentTO.getPayAmount().compareTo(new BigDecimal(DTITestUtil.PAYAMOUNT)) != 0)
      throw new Exception("Invalid PayAmount: " + aPaymentTO.getPayAmount());

    return;
  }

  /**
   * Validate gc swipe payment.
   * 
   * @param aPaymentTO
   *          the a payment to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateGCSwipePayment(PaymentTO aPaymentTO) throws Exception {

    PaymentTO.PaymentType payType = aPaymentTO.getPayType();
    if (payType != PaymentTO.PaymentType.GIFTCARD)
      throw new Exception("Invalid paytype found while trying to validate Gift Card");

    GiftCardTO gc = aPaymentTO.getGiftCard();

    GiftCardTO.GiftCardType gcType = gc.getGcManualOrSwipe();

    if (gcType != GiftCardTO.GiftCardType.GCSWIPE)
      throw new Exception("Invalid Gift Card.  GCSwipe was expected.");

    if (gc.getGcTrack1().compareTo(DTITestUtil.GCTRACK1) != 0)
      throw new Exception("Invalid GCSwipe.GCTrack1: " + gc.getGcTrack1());

    if (gc.getGcTrack2().compareTo(DTITestUtil.GCTRACK2) != 0)
      throw new Exception("Invalid GCSwipe.GCTrack2: " + gc.getGcTrack2());

    if (aPaymentTO.getPayAmount().compareTo(new BigDecimal(DTITestUtil.PAYAMOUNT)) != 0)
      throw new Exception("Invalid PayAmount: " + aPaymentTO.getPayAmount());

    return;

  }

  /**
   * Validate voucher payment.
   * 
   * @param aPaymentTO
   *          the a payment to
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateVoucherPayment(PaymentTO aPaymentTO) throws Exception {

    PaymentTO.PaymentType payType = aPaymentTO.getPayType();
    if (payType != PaymentTO.PaymentType.VOUCHER)
      throw new Exception("Invalid paytype found while trying to validate Voucher");

    VoucherTO vp = aPaymentTO.getVoucher();
    if (vp.getMainCode().compareTo(DTITestUtil.MAINCODE) != 0)
      throw new Exception("Invalid Voucher.MainCode: " + vp.getMainCode());

    if (vp.getUniqueCode().compareTo(DTITestUtil.UNIQUECODE) != 0)
      throw new Exception("Invalid Voucher.UniqueCode: " + vp.getUniqueCode());

    if (aPaymentTO.getPayAmount().compareTo(new BigDecimal(DTITestUtil.PAYAMOUNT)) != 0)
      throw new Exception("Invalid PayAmount: " + aPaymentTO.getPayAmount());
  }

  /**
   * Creates the test pay header to.
   * 
   * @return the payload header to
   */
  public static PayloadHeaderTO createTestPayHeaderTO() {

    // Payload Header
    PayloadHeaderTO payHdrTO = new PayloadHeaderTO();
    payHdrTO.setPayloadID(DTITestUtil.PAYLOADID);
    payHdrTO.setTsPayloadID(DTITestUtil.TSPAYLOADID);
    payHdrTO.setTarget(DTITestUtil.TARGET);
    payHdrTO.setVersion(DTITestUtil.VERSION);
    payHdrTO.setCommProtocol(DTITestUtil.COMMPROTOCOL);
    payHdrTO.setCommMethod(DTITestUtil.COMMMETHOD);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date aDate = null;
    try {
      aDate = sdf.parse(DTITestUtil.TRANSMITDATETIME);
    } catch (Exception e) {
      System.out.println("Invalid TRANSMITDATETIME format in DLRTestUtil.");
    }

    GregorianCalendar transmitDateTime = (GregorianCalendar) GregorianCalendar.getInstance();
    transmitDateTime.setTime(aDate);
    payHdrTO.setTransmitDateTime(transmitDateTime);
    payHdrTO.setTktBroker(DTITestUtil.TKTBROKER);
    payHdrTO.setCommandCount(new BigInteger(DTITestUtil.COMMANDCOUNT));

    return payHdrTO;

  }

  /**
   * Creates the test cmd header to.
   * 
   * @return the command header to
   */
  public static CommandHeaderTO createTestCmdHeaderTO() {

    CommandHeaderTO cmdHdrTO = new CommandHeaderTO();
    cmdHdrTO.setCmdItem(new BigInteger(DTITestUtil.CMDITEM));
    cmdHdrTO.setCmdDuration(new BigDecimal(DTITestUtil.CMDDURATION));
    GregorianCalendar commandDateTime = (GregorianCalendar) GregorianCalendar.getInstance();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    Date aDate = null;
    try {
      aDate = sdf.parse(DTITestUtil.CMDDATETIME);
    } catch (Exception e) {
      System.out.println("Invalid CMDDATETIME format in DLRTestUtil.");
    }

    commandDateTime.setTime(aDate);

    cmdHdrTO.setCmdDateTime(commandDateTime);

    return cmdHdrTO;
  }

  /**
   * Validate xml payload header.
   * 
   * @param baselineXML
   *          the baseline xml
   * @param newXML
   *          the new xml
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateXMLPayloadHeader(String baselineXML, String newXML) throws Exception {
    String basePayHdr = DTITestUtilities.findTag(baselineXML, DTITestUtil.PAYLOADHEADER);
    String newPayHdr = DTITestUtilities.findTag(newXML, DTITestUtil.PAYLOADHEADER);

    DTITestUtilities.compareXML(basePayHdr, newPayHdr, DTITestUtil.PAYLOADHEADER);
  }

  /**
   * Validate xml headers.
   * 
   * @param baselineXML
   *          the baseline xml
   * @param newXML
   *          the new xml
   * 
   * @throws Exception
   *           the exception
   */
  public static void validateXMLHeaders(String baselineXML, String newXML) throws Exception {
    String basePayHdr = DTITestUtilities.findTag(baselineXML, DTITestUtil.PAYLOADHEADER);
    String newPayHdr = DTITestUtilities.findTag(newXML, DTITestUtil.PAYLOADHEADER);

    DTITestUtilities.compareXML(basePayHdr, newPayHdr, DTITestUtil.PAYLOADHEADER);

    String baseCmdHdr = DTITestUtilities.findTag(baselineXML, DTITestUtil.COMMANDHEADER);
    String newCmdHdr = DTITestUtilities.findTag(newXML, DTITestUtil.COMMANDHEADER);

    DTITestUtilities.compareXML(baseCmdHdr, newCmdHdr, DTITestUtil.COMMANDHEADER);

  }

}
