package pvt.disney.dti.gateway.rules.hkd;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;
import pvt.disney.dti.gateway.data.common.EntityTO.DefaultPaymentType;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTCreditCardTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicData;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicInfo;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTVoucherTO;
import pvt.disney.dti.gateway.provider.hkd.xml.HkdOTCommandXML;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.rules.hkd.HKDBusinessRules;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HKDBusinessRules {

  /** Constant indicating the XML Scheme Instance. */
  final static String XML_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";
  
  /** Constant value for IAGO prefix. */
  private final static String IAGO_PREFIX_VALUE = "999";
  
  /** Constant indicating the CAVV Format of Hex (Binary = 1 is not used). */
  private final static String CAVVFORMAT_HEX = "2";

  /** Constant value for maximum description length. */
  private final static int DESC_MAX_LENGTH = 25;
  
  /** Properties variable to store properties from AbstractInitializer. */
  private static Properties props = getProperties();
  
  /** Constant indicating the Disney Vacation Club. */
  final static String DVC_STRING = "DVC";
  
  /** Indicates the gender is unspecified. */
  private final static String UNSPECIFIED_GENDER_DEFAULT = " ";

  /** Indicates YES/true to solicit opt in (2.10) */
  public final static String YES = "YES";
  
  /** Indicates NO/false to solicit opt in (2.10) */
  public final static String NO = "NO";
  
  /** Constant indicating no provider error. */
  private final static String OT_NO_ERROR = "No error description.";
  
  /**
   * Gets the properties for this application found in "dtiApp.properties"
   * 
   * @return a populated Properties object.
   */
  private static Properties getProperties() {

    Properties properties = null;

    // Get properties manager.
    try {

	    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
	    properties = ResourceLoader.convertResourceBundleToProperties(rb);

    } catch (Throwable e) {
      System.err.println("ERROR *** Unable to initialize DTIService object.  Properties issue! ***" + e.toString());
    }

    return properties;
  }
  /**
   * For provider-centric rules only that are NOT transaction-centric.
   * This method is primarily for
   * handling anything in the payload header or the command header that has a
   * provider-centric rule.
   * 
   * @param dtiTxn
   *          the dti txn
   * 
   * @return the DTI transaction transfer object
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static DTITransactionTO applyBusinessRules(DTITransactionTO dtiTxn) throws DTIException {
    return dtiTxn;
  }
  
  /**
   * This method changes from the DTITransaction to the DLR provider's request string format.
   * 
   * @param dtiTxn
   *            The dtiTxn object containing the request.
   * @return The DLR provider's request format.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   * 
   */
  public static String changeToHKDProviderFormat(DTITransactionTO dtiTxn) throws DTIException {
    String xmlRequest = null;
    
    TransactionType requestType = dtiTxn.getTransactionType();

    switch (requestType) {

    case RESERVATION:
      xmlRequest = HKDReservationRules.transformRequest(dtiTxn);
      break;

    case QUERYRESERVATION: 
      xmlRequest = HKDQueryReservationRules.transformRequest(dtiTxn);
      break;

    default:
      throw new DTIException(TransformRules.class, DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Invalid HKD transaction type sent to DTI Gateway.  Unsupported.");
    }

    return xmlRequest;
  }
  
  /**
   * Parses the xmlResponse string from the HKD provider into the
   * DTITransactionTO object.
   * 
   * @param dtiTxn
   *          The dtiTxn object for this transaction.
   * @param xmlResponse
   *          The response string returned by the DLR provider.
   * @return The dtiTxn object enhanced with the response.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  public static DTITransactionTO changeHKDProviderFormatToDti(
      DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {
    
    transformResponse(dtiTxn, xmlResponse);

    return dtiTxn;
  }
  
  /**
   * Transforms the Omni Ticket Header using the dtiTxn, the request type, and
   * the request sub type.
   * 
   * @param dtiTxn
   *          the DTITransactionTO for this interaction.
   * @param requestType
   *          The text value for the request type.
   * @param requestSubType
   *          The text value for the request sub-type.
   * @return a properly populated OTHeaderTO object.
   * @throws DTIException
   *           for any transformation error.
   */
  static HkdOTHeaderTO transformOTHeader(DTITransactionTO dtiTxn, String requestType, String requestSubType)
      throws DTIException {

    HkdOTHeaderTO hdr;
    hdr = new HkdOTHeaderTO();

    String referenceNumber = IAGO_PREFIX_VALUE + dtiTxn.getTpRefNum().toString();
    hdr.setReferenceNumber(referenceNumber);

    hdr.setRequestType(requestType);
    hdr.setRequestSubType(requestSubType);

    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();

    // Operating Area
    AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.OP_AREA);
    if (anAttributeTO == null) {
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Operating Area not set up in DTI database.");
    }
    hdr.setOperatingArea(anAttributeTO.getAttrValue());

    // User ID
    anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.USER);
    if (anAttributeTO == null) {
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "User ID not set up in DTI database.");
    }
    String userIdString = anAttributeTO.getAttrValue();
    Integer userId = null;
    try {
      userId = Integer.decode(userIdString);
    } catch (NumberFormatException nfe) {
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "User ID not a parsable integer: "
          + userIdString);
    }
    hdr.setUserId(userId);

    // Password
    anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.PASS);
    if (anAttributeTO == null) {
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Password not set up in DTI database.");
    }
    String passwordString = anAttributeTO.getAttrValue();
    Integer password = null;
    try {
      password = Integer.decode(passwordString);
    } catch (NumberFormatException nfe) {
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "Password not a parsable integer: "
          + passwordString);
    }
    hdr.setPassword(password);
    return hdr;
  }
  
  /**
   * Populates the OT Payment List based on the DTI payment list provided.
   * 
   * @param otPaymentList
   *          ArrayList of the OT Payments
   * @param dtiPayList
   *          ArrayList of DTI payments
   * @param entityTO
   *          The entity transfer object.
   */
  static void createOTPaymentList(ArrayList<HkdOTPaymentTO> otPaymentList, ArrayList<PaymentTO> dtiPayList,
      EntityTO entityTO) {

    HkdOTPaymentTO otPaymentTO = null;

    if ((dtiPayList == null) || (dtiPayList.size() == 0)) {

      String defPymtData = null;

      if ((entityTO.getDefPymtIdEnum() == DefaultPaymentType.VOUCHER) && (entityTO.getDefPymtData() != null)) {
        defPymtData = entityTO.getDefPymtData();

        VoucherTO dtiVoucherTO = new VoucherTO();
        dtiVoucherTO.setMainCode(defPymtData);
        PaymentTO dtiPaymentTO = new PaymentTO();
        // There will only be one default payment.
        dtiPaymentTO.setPayItem(new BigInteger("1"));
        dtiPaymentTO.setVoucher(dtiVoucherTO);
        otPaymentTO = transformOTVoucherRequest(dtiPaymentTO);
        otPaymentList.add(otPaymentTO);

      } else {

        // do nothing (generate no payment clause)

      }
    } else {

      for /* each */(PaymentTO dtiPaymentTO : /* in */dtiPayList) {

        // create payment based on input
        otPaymentTO = transformOTPaymentRequest(dtiPaymentTO, entityTO);
        otPaymentList.add(otPaymentTO);
      }
    }

    return;
  }

  /**
   * Transforms a DTI payment to an Omni Ticket Payment transfer object for
   * vouchers.
   * 
   * @param dtiPayment
   *          containing a voucher
   * @return a populated OTPaymentTO object populated for voucher.
   */
  private static HkdOTPaymentTO transformOTVoucherRequest(PaymentTO dtiPayment) {

    HkdOTPaymentTO otPaymentTO = new HkdOTPaymentTO();
    VoucherTO dtiVoucherTO = dtiPayment.getVoucher();
    String voucherNumber = dtiVoucherTO.getMainCode();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.VOUCHER);

    // PayAmount (if provided)
    if (dtiPayment.getPayAmount() != null) {
      otPaymentTO.setPayAmount(dtiPayment.getPayAmount());
    }

    // MasterCode for Voucher
    HkdOTVoucherTO otVoucherTO = new HkdOTVoucherTO();
    otVoucherTO.setMasterCode(voucherNumber);

    // UniqueCode for Voucher (if provided)
    if (dtiVoucherTO.getUniqueCode() != null) {
      otVoucherTO.setUniqueCode(dtiVoucherTO.getUniqueCode());
    }

    otPaymentTO.setVoucher(otVoucherTO);

    return otPaymentTO;
  }
  
  /**
   * Transforms the DTI payment and entity transfer objects into an OT Payment
   * object. Supports credit card, gift card, or voucher.
   * 
   * @param dtiPaymentTO
   *          The DTI payment transfer object.
   * @param entityTO
   *          The DTI entity transfer object.
   * @return a populated OT Payment transfer object.
   */
  private static HkdOTPaymentTO transformOTPaymentRequest(PaymentTO dtiPaymentTO, EntityTO entityTO) {

    HkdOTPaymentTO otPaymentTO = null;

    PaymentTO.PaymentType dtiPayType = dtiPaymentTO.getPayType();

    switch (dtiPayType) {

    case CREDITCARD:
      otPaymentTO = transformOTCreditCardRequest(dtiPaymentTO, entityTO);
      break;

    case GIFTCARD:
      otPaymentTO = transformOTGiftCardRequest(dtiPaymentTO);
      break;

    case VOUCHER:
      otPaymentTO = transformOTVoucherRequest(dtiPaymentTO);
      break;

    case INSTALLMENT: // As of 2.15, JTL
      otPaymentTO = transformOTInstallmentRequest(dtiPaymentTO);

    default:
      break;

    }

    return otPaymentTO;
  }

  /**
   * Transforms a DTI credit card payment into an Omni Ticket payment object.
   * 
   * @param dtiPaymentTO
   *          The DTI payment transfer object.
   * @param entityTO
   *          The DTI entity transfer object.
   * @return a populated OT Payment transfer object.
   */
  private static HkdOTPaymentTO transformOTCreditCardRequest(PaymentTO dtiPayment, EntityTO entityTO) {

    HkdOTPaymentTO otPaymentTO = new HkdOTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.CREDITCARD);

    // PayAmount
    otPaymentTO.setPayAmount(dtiPayment.getPayAmount());

    // Payment Details
    HkdOTCreditCardTO otCreditCard = new HkdOTCreditCardTO();
    CreditCardTO dtiCreditCard = dtiPayment.getCreditCard();

    CreditCardTO.CreditCardType entryType = dtiCreditCard.getCcManualOrSwipe();

    if (entryType == CreditCardTO.CreditCardType.CCSWIPE) {
      otCreditCard.setTrack1(dtiCreditCard.getCcTrack1());
      otCreditCard.setTrack2(dtiCreditCard.getCcTrack2());

      if (dtiCreditCard.getPosTermID() != null) { // As of 2.12
        otCreditCard.setPosTerminalId(dtiCreditCard.getPosTermID());
      }

      if (dtiCreditCard.getExtnlDevSerial() != null) { // As of 2.12
        otCreditCard.setExternalDeviceSerialId(dtiCreditCard.getExtnlDevSerial());
      }

    } else { // Must be "Manual"

      otCreditCard.setCardNumber(dtiCreditCard.getCcNbr());

      // Omni Ticket requires expiration date in reverse order (MMYY to
      // YYMM)
      String dtiExpiration = dtiCreditCard.getCcExpiration();
      String otExpiration = dtiExpiration.substring(2) + dtiExpiration.substring(0, 2);
      otCreditCard.setCardExpDate(otExpiration);

      // the Gateway doesn't have to send a CVV XML element to IAGO, but
      // if it does, it must have a value
      if (dtiCreditCard.getCcVV() != null && dtiCreditCard.getCcVV().length() > 0) {
        otCreditCard.setCVV(dtiCreditCard.getCcVV());
      }

      // AVS Street and ZIP
      otCreditCard.setAvs_AvsStreet(dtiCreditCard.getCcStreet());
      otCreditCard.setAvs_AvsZipCode(dtiCreditCard.getCcZipCode());

      // CVV
      if (dtiCreditCard.getCcCAVV() != null) {
        otCreditCard.setCAVVFormat(CAVVFORMAT_HEX);
        otCreditCard.setCAVVValue(dtiCreditCard.getCcCAVV());
      }

      // PreApproved (optional) (as of 2.16.2, JTL)
      if (dtiCreditCard.getPreApprovedCC() != null) {
        otCreditCard.setIsPreApproved(dtiCreditCard.getPreApprovedCC());
      }

      // AuthCode (optional) (as of 2.16.2, JTL)
      if (dtiCreditCard.getCcAuthCode() != null) {
        otCreditCard.setAuthCode(dtiCreditCard.getCcAuthCode());
      }

      if (dtiCreditCard.getCcSha1() != null) { // As of 2.16.2, JTL
        otCreditCard.setSha1(dtiCreditCard.getCcSha1());
      }

      if (dtiCreditCard.getCcSubCode() != null) { // As of 2.16.2, JTL
        otCreditCard.setSubCode(dtiCreditCard.getCcSubCode());
      }

      if (dtiCreditCard.getCcEcommerce() != null) {
        otCreditCard.setECommerceValue(dtiCreditCard.getCcEcommerce());
      } else {
        if (entityTO.getECommerceValue() != null) {
          otCreditCard.setECommerceValue(entityTO.getECommerceValue());
        }
      }

    }

    otCreditCard.setGiftCardIndicator(false);
    otCreditCard.setWireless(dtiCreditCard.isWireless());

    otPaymentTO.setCreditCard(otCreditCard);

    return otPaymentTO;
  }

  /**
   * Transforms a DTI gift card payment into an Omni Ticket payment object.
   * 
   * @param dtiPaymentTO
   *          The DTI payment transfer object.
   * @return a populated OT Payment transfer object.
   */
  private static HkdOTPaymentTO transformOTGiftCardRequest(PaymentTO dtiPayment) {

    HkdOTPaymentTO otPaymentTO = new HkdOTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.CREDITCARD);

    // PayAmount
    otPaymentTO.setPayAmount(dtiPayment.getPayAmount());

    // Payment Details
    HkdOTCreditCardTO otCreditCard = new HkdOTCreditCardTO();
    GiftCardTO dtiGiftCard = dtiPayment.getGiftCard();
    GiftCardTO.GiftCardType entryType = dtiGiftCard.getGcManualOrSwipe();

    if (entryType == GiftCardTO.GiftCardType.GCSWIPE) {

      if (dtiGiftCard.getGcTrack1() != null)
        otCreditCard.setTrack1(dtiGiftCard.getGcTrack1());
      otCreditCard.setTrack2(dtiGiftCard.getGcTrack2());

    } else {
      otCreditCard.setCardNumber(dtiGiftCard.getGcNbr());
    }

    otCreditCard.setGiftCardIndicator(true);

    otPaymentTO.setCreditCard(otCreditCard);

    return otPaymentTO;
  }
  
  /**
   * Transforms a DTI payment to an Omni Ticket Payment transfer object for
   * installment payment.
   * 
   * @param dtiPayment
   *          containing a voucher
   * @return a populated OTPaymentTO object populated for voucher.
   */
  private static HkdOTPaymentTO transformOTInstallmentRequest(PaymentTO dtiPayment) {

    HkdOTPaymentTO otPaymentTO = new HkdOTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // Installment Credit Card
    HkdOTInstallmentTO otInstallmentTO = new HkdOTInstallmentTO();
    InstallmentTO dtiInstallTO = dtiPayment.getInstallment();
    
    // Determine the correct Contract Alpha Code (as of 2.16.2, JTL)
    if (dtiInstallTO.isForRenewal()) {
      otInstallmentTO.setContractAlphaCode(HkdOTInstallmentTO.CONTRACTALPHARENEWAL);
    } else {
      otInstallmentTO.setContractAlphaCode(HkdOTInstallmentTO.CONTRACTALPHAPURCHASE);
    }

    CreditCardType cardType = dtiInstallTO.getCreditCard().getCcManualOrSwipe();
    InstallmentCreditCardTO creditCardTO = dtiInstallTO.getCreditCard();

    // Transform Credit Card Information
    if (cardType == CreditCardType.CCMANUAL) {
      otInstallmentTO.setCardNumber(creditCardTO.getCcNbr());

      // Omni Ticket requires expiration date in reverse order (MMYY to
      // YYMM)
      String dtiExpiration = creditCardTO.getCcExpiration();
      String otExpiration = dtiExpiration.substring(2) + dtiExpiration.substring(0, 2);
      otInstallmentTO.setCardExpDate(otExpiration);

      otInstallmentTO.setCardHolderName(creditCardTO.getCcName());
    } else {
      otInstallmentTO.setTrack1(creditCardTO.getCcTrack1());
      otInstallmentTO.setTrack2(creditCardTO.getCcTrack2());
    }

    // InstallmentDemoData (not used for HKDL)

    // PayAmount (if provided)
    if (dtiPayment.getPayAmount() != null) {
      otPaymentTO.setPayAmount(dtiPayment.getPayAmount());
    }

    otPaymentTO.setInstallment(otInstallmentTO);

    return otPaymentTO;
  }

  /**
   * Get the site number property used in OT transactions.
   * 
   * @return the numerical site number
   * @throws DTIException
   *           should the property not be properly specified in configuration.
   */
  static Integer getSiteNumberProperty() throws DTIException {

    if (props == null)
      props = getProperties();
    if (props == null)
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR,
          "Internal Error:  Cannot get properties values.");

    String siteNumberString = PropertyHelper.readPropsValue(PropertyName.ATS_SITE_NUMBER, props, null);
    Integer siteNumber;

    if (siteNumberString == null)
      throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR, "Internal Error:  No "
          + PropertyName.ATS_SITE_NUMBER + " in properties file.");
    else {
      try {
        siteNumber = Integer.decode(siteNumberString);
      } catch (NumberFormatException nfe) {
        throw new DTIException(HKDBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR, "Internal Error:  "
            + PropertyName.ATS_SITE_NUMBER + " in properties file is not numeric.");
      }
    }

    return siteNumber;

  }
  
  /**
   * Sets the ticket demographics objects.
   * 
   * @since 2.9
   * @param tktDemoList
   * @param otDemoInfo
   */
  public static void transformTicketDemoData(ArrayList<DemographicsTO> tktDemoList, HkdOTDemographicInfo otDemoInfo) {

    for /* each */(DemographicsTO aDtiTicket : /* in */tktDemoList) {

      HkdOTDemographicData otDemoData = new HkdOTDemographicData();

      // Last Name
      String lastNameString = DTIFormatter.websafe(aDtiTicket.getLastName().toUpperCase());
      HkdOTFieldTO aField = new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_LASTNAME, lastNameString);
      otDemoData.addOTField(aField);
      
      // First Name
      String firstNameString = DTIFormatter.websafe(DTIFormatter.websafe(aDtiTicket.getFirstName().toUpperCase()));
      aField = new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_FIRSTNAME, firstNameString);
      otDemoData.addOTField(aField);      

      // Gender
      if (aDtiTicket.getGenderType() == DemographicsTO.GenderType.UNSPECIFIED) {
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_GENDER, UNSPECIFIED_GENDER_DEFAULT));
      } else {
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_GENDER, DTIFormatter.websafe(aDtiTicket.getGender()
            .toUpperCase())));
      }
      
      // Email (optional)
      if (aDtiTicket.getEmail() != null) {
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_EMAIL, DTIFormatter.websafe(aDtiTicket.getEmail()
            .toUpperCase())));
      }

      // Address 1
//      otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_ADDRESS_ONE, DTIFormatter.websafe(aDtiTicket.getAddr1()
//          .toUpperCase())));

      // Address 2 (optional)
//      if (aDtiTicket.getAddr2() != null) {
//        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_ADDRESS_TWO, DTIFormatter.websafe(aDtiTicket.getAddr2()
//            .toUpperCase())));
//      }
      
      // Date of Birth (Optional)
      if (aDtiTicket.getDateOfBirth() != null) {
        
        // Date of Birth DD
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String dobString = sdf.format(aDtiTicket.getDateOfBirth().getTime());
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_DOB_DD, dobString));

        // Date of Birth MM
        sdf = new SimpleDateFormat("MM");
        dobString = sdf.format(aDtiTicket.getDateOfBirth().getTime());
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_DOB_MM, dobString));

        // Date of Birth YYYY
        sdf = new SimpleDateFormat("yyyy");
        dobString = sdf.format(aDtiTicket.getDateOfBirth().getTime());
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_DOB_YYYY, dobString));
      }
      
      // Telephone (Mobile) 
      if (aDtiTicket.getCellPhone() != null) {
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_MOBILEPHONE, DTIFormatter.websafe(aDtiTicket.getCellPhone())));
      }

      // City
//      otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_CITY, DTIFormatter.websafe(aDtiTicket.getCity()
//          .toUpperCase())));

      // State (optional)
//      if (aDtiTicket.getState() != null) {
//        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_STATE, DTIFormatter.websafe(aDtiTicket.getState()
//            .toUpperCase())));
//      }

      // ZIP
//      otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_ZIP, DTIFormatter.websafe(aDtiTicket.getZip())));

      // Country (not used for HKDL)

      // Telephone (HOME)
      if (aDtiTicket.getTelephone() != null) {
    	  otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_HOMEPHONE, DTIFormatter.websafe(aDtiTicket.getTelephone()
          .toUpperCase())));
      }
      // OptInSolicit (2.10)
//      if (aDtiTicket.getOptInSolicit().booleanValue() == true) {
//        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_OKAYFORNEWS, HKDBusinessRules.YES));
//      } else {
//        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_OKAYFORNEWS, HKDBusinessRules.NO));
//      }
      
      // Vendor Reference 
      if (aDtiTicket.getSellerRef() != null) {
        otDemoData.addOTField(new HkdOTFieldTO(HkdOTFieldTO.HKD_TKTDEMO_SELLERREF, 
            DTIFormatter.websafe(aDtiTicket.getSellerRef())));
      }
      
      otDemoInfo.addOTDemographicData(otDemoData);

    }
    return;
  }

  /**
   * Transforms a reservation response string from the WDW provider and updates
   * the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *          The transaction object for this request.
   * @param xmlResponse
   *          The WDW provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response
   *         information.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  private static DTIResponseTO transformResponse(DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {

    HkdOTCommandTO otCmdTO = HkdOTCommandXML.getTO(xmlResponse);

    DTIResponseTO dtiRespTO = new DTIResponseTO();
    dtiTxn.setResponse(dtiRespTO);

    // Set up the Payload and Command Header Responses.
    PayloadHeaderTO payloadHdrTO = TransformRules.createRespPayloadHdr(dtiTxn);
    CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);

    dtiRespTO.setPayloadHeader(payloadHdrTO);
    dtiRespTO.setCommandHeader(commandHdrTO);

    // Check for blatant error
    if (otCmdTO == null)
      throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_FAILURE,
          "Internal Error:  Omni XML translated into a response with null Command object.");
    if (otCmdTO.getHeaderTO() == null)
      throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_FAILURE,
          "Internal Error:  Omni XML translated into a response with null Header object.");
    if (otCmdTO.hasBodyObject() == false)
      throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_FAILURE,
          "Internal Error:  Omni XML translated into a response with null body object.");
    if (otCmdTO.getErrorTO() == null)
      throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_FAILURE,
          "Internal Error:  Omni XML translated into a response with null Error object.");

    String otRefNumString = otCmdTO.getHeaderTO().getReferenceNumber();
    // Validate numeric reference number string
    Integer.parseInt(otRefNumString.trim());

    // Extract error code
    Integer otErrorCode = otCmdTO.getErrorTO().getErrorCode();
    dtiRespTO.setProviderErrCode(otErrorCode.toString());
    if (otCmdTO.getErrorTO().getErrorDescription() != null) {
      String errorString = otCmdTO.getErrorTO().getErrorDescription();
      if (errorString.length() > DESC_MAX_LENGTH) {
        errorString = errorString.substring(0, DESC_MAX_LENGTH);
      }
      dtiRespTO.setProviderErrName(errorString);
    } else {
      dtiRespTO.setProviderErrName(OT_NO_ERROR);
    }

    // If the provider had an error, map it and generate the response.
    // Copy the ticket identity and default the TktStatus Voidable to No
    // NOTE: Post error processing is only valid in the cases below - do not add.
    if (otErrorCode.intValue() != 0) {

      DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(otErrorCode.toString());

      if (dtiErrorTO == null)
        throw new DTIException(TransformRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
            "Internal Error:  Provider error code " + otErrorCode.toString()
                + " does has have a translation in TP_ERROR table.");

      DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

      /**
       * Some transactions process the body portion translations or some
       * contingency actions even if there is an error.
       */
      // Placeholder for QueryTicket, VoidTicket, UpgradeTicket
      // when implemented for HKDL

    } else {

      // If the provider had no error, transform the response.
      OTTransactionType requestType = otCmdTO.getTxnType();
      TransactionType dtiTransType = dtiTxn.getTransactionType();

      switch (requestType) {

      case MANAGERESERVATION: // As of 2.16.1 BIEST001

        switch (dtiTransType) {

        case RESERVATION:
          HKDReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;

        case QUERYRESERVATION:
          HKDQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;

        default:
          break;

        }

        break;

      // ---------------------------------------------------------

      default:
        throw new DTIException(TransformRules.class, DTIErrorCode.COMMAND_NOT_AUTHORIZED,
            "Invalid WDW transaction response sent to DTI Gateway.  Unsupported.");
      }

    }

    return dtiRespTO;
  }

  /**
   * Sets the DTI payment list transfer object list based upon the transfer
   * objects created from the provider response.
   * 
   * @param dtiPmtList
   *          The list of DTI payment transfer objects.
   * @param otPmtList
   *          The list of Omni Ticket Payment Transfer Objects.
   */
  static void setDTIPaymentList(ArrayList<PaymentTO> dtiPmtList, ArrayList<HkdOTPaymentTO> otPmtList) {

    if ((otPmtList != null) && (otPmtList.size() > 0)) {

      long counter = 0;
      for /* each */(HkdOTPaymentTO otPaymentTO : /* in */otPmtList) {

        if (otPaymentTO.getPayType() == HkdOTPaymentTO.PaymentType.VOUCHER) {
          continue; // Nothing to do, here.
        }

        // Installment (as of 2.15, JTL)
        if (otPaymentTO.getPayType() == HkdOTPaymentTO.PaymentType.INSTALLMENT) {
          PaymentTO dtiPmtTO = new PaymentTO();
          InstallmentTO installTO = new InstallmentTO();
          HkdOTInstallmentTO OTinstall = otPaymentTO.getInstallment();

          if (OTinstall.getContractId() != null) {
            installTO.setContractId(OTinstall.getContractId());
            dtiPmtTO.setInstallment(installTO);
            dtiPmtList.add(dtiPmtTO);
          } // Else do not add this to the response; it will cause NPEs.
        }

        if (otPaymentTO.getPayType() == HkdOTPaymentTO.PaymentType.CREDITCARD) {

          PaymentTO dtiPmtTO = new PaymentTO();
          dtiPmtTO.setPayItem(BigInteger.valueOf(++counter));

          HkdOTCreditCardTO otCreditCardTO = otPaymentTO.getCreditCard();
          if (otCreditCardTO.isGiftCardIndicator()) { // Gift Card

            GiftCardTO dtiGiftCardTO = new GiftCardTO();
            dtiGiftCardTO.setGcAuthCode(otCreditCardTO.getAuthErrorCode());
            if (otCreditCardTO.getAuthNumber() != null)
              dtiGiftCardTO.setGcAuthNumber(otCreditCardTO.getAuthNumber());

            // AuthSystemResponse, as specified in the old gateway
            // is
            // not
            // present in either the NeXML spec or in their normal
            // responses.
            // (omitted)
            // dtiGiftCardTO.setGcAuthSysResponse(gcAuthSysResponse)

            if (otCreditCardTO.getCcNumber() != null)
              dtiGiftCardTO.setGcNumber(otCreditCardTO.getCcNumber());
            if (otCreditCardTO.getRemainingBalance() != null)
              dtiGiftCardTO.setGcRemainingBalance(otCreditCardTO.getRemainingBalance());
            if (otCreditCardTO.getPromoExpDate() != null)
              dtiGiftCardTO.setGcPromoExpDate(otCreditCardTO.getPromoExpDate());

            dtiPmtTO.setGiftCard(dtiGiftCardTO);
          } else { // Credit Card
            CreditCardTO dtiCredCardTO = new CreditCardTO();
            dtiCredCardTO.setCcAuthCode(otCreditCardTO.getAuthErrorCode());

            if (otCreditCardTO.getAuthNumber() != null)
              dtiCredCardTO.setCcAuthNumber(otCreditCardTO.getAuthNumber());

            // AuthSystemResponse, as specified in the old gateway
            // is not
            // present in either the NeXML spec or in their normal
            // responses. (omitted)
            // dtiCredCardTO.setCcAuthSysResponse(ccAuthSysResponse);

            if (otCreditCardTO.getCcNumber() != null)
              dtiCredCardTO.setCcNumber(otCreditCardTO.getCcNumber());

            dtiPmtTO.setCreditCard(dtiCredCardTO);

          }

          dtiPmtList.add(dtiPmtTO);
        }

      }

    }
    return;
  }
  
 }
