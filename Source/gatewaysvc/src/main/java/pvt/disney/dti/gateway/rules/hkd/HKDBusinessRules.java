package pvt.disney.dti.gateway.rules.hkd;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;
import pvt.disney.dti.gateway.data.common.EntityTO.DefaultPaymentType;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInstallmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.rules.hkd.HKDBusinessRules;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * 
 * @author lewit019
 *
 */
public class HKDBusinessRules {

  /** Constant indicating the XML Scheme Instance. */
  final static String XML_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";
  
  /** Constant value for IAGO prefix. */
  private final static String IAGO_PREFIX_VALUE = "999";
  
  /** Constant indicating the CAVV Format of Hex (Binary = 1 is not used). */
  private final static String CAVVFORMAT_HEX = "2";
  
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
  
  /**
   * Gets the properties for this application found in "dtiApp.properties"
   * 
   * @return a populated Properties object.
   */
  private static Properties getProperties() {

    AbstractInitializer abstrInitl = null;
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
   * TODO
   */
  public static DTITransactionTO changeHKDProviderFormatToDti(
      DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {
    return null;
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
  static OTHeaderTO transformOTHeader(DTITransactionTO dtiTxn, String requestType, String requestSubType)
      throws DTIException {

    OTHeaderTO hdr;
    hdr = new OTHeaderTO();

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
  static void createOTPaymentList(ArrayList<OTPaymentTO> otPaymentList, ArrayList<PaymentTO> dtiPayList,
      EntityTO entityTO) {

    OTPaymentTO otPaymentTO = null;

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
  private static OTPaymentTO transformOTVoucherRequest(PaymentTO dtiPayment) {

    OTPaymentTO otPaymentTO = new OTPaymentTO();
    VoucherTO dtiVoucherTO = dtiPayment.getVoucher();
    String voucherNumber = dtiVoucherTO.getMainCode();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(OTPaymentTO.PaymentType.VOUCHER);

    // PayAmount (if provided)
    if (dtiPayment.getPayAmount() != null) {
      otPaymentTO.setPayAmount(dtiPayment.getPayAmount());
    }

    // MasterCode for Voucher
    OTVoucherTO otVoucherTO = new OTVoucherTO();
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
  private static OTPaymentTO transformOTPaymentRequest(PaymentTO dtiPaymentTO, EntityTO entityTO) {

    OTPaymentTO otPaymentTO = null;

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
  private static OTPaymentTO transformOTCreditCardRequest(PaymentTO dtiPayment, EntityTO entityTO) {

    OTPaymentTO otPaymentTO = new OTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(OTPaymentTO.PaymentType.CREDITCARD);

    // PayAmount
    otPaymentTO.setPayAmount(dtiPayment.getPayAmount());

    // Payment Details
    OTCreditCardTO otCreditCard = new OTCreditCardTO();
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
  private static OTPaymentTO transformOTGiftCardRequest(PaymentTO dtiPayment) {

    OTPaymentTO otPaymentTO = new OTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // PayType
    otPaymentTO.setPayType(OTPaymentTO.PaymentType.CREDITCARD);

    // PayAmount
    otPaymentTO.setPayAmount(dtiPayment.getPayAmount());

    // Payment Details
    OTCreditCardTO otCreditCard = new OTCreditCardTO();
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
  private static OTPaymentTO transformOTInstallmentRequest(PaymentTO dtiPayment) {

    OTPaymentTO otPaymentTO = new OTPaymentTO();

    // PayItem
    otPaymentTO.setPayItem(dtiPayment.getPayItem());

    // Installment Credit Card
    OTInstallmentTO otInstallmentTO = new OTInstallmentTO();
    InstallmentTO dtiInstallTO = dtiPayment.getInstallment();
    
    // Determine the correct Contract Alpha Code (as of 2.16.2, JTL)
    if (dtiInstallTO.isForRenewal()) {
      otInstallmentTO.setContractAlphaCode(OTInstallmentTO.CONTRACTALPHARENEWAL);
    } else {
      otInstallmentTO.setContractAlphaCode(OTInstallmentTO.CONTRACTALPHAPURCHASE);
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

    // InstallmentDemoData
    ArrayList<OTFieldTO> otDemoList = otInstallmentTO.getDemographicData();
    InstallmentDemographicsTO installDemoTO = dtiInstallTO.getInstllDemo();
    transformInstallmentDemoData(installDemoTO, otDemoList);

    // PayAmount (if provided)
    if (dtiPayment.getPayAmount() != null) {
      otPaymentTO.setPayAmount(dtiPayment.getPayAmount());
    }

    otPaymentTO.setInstallment(otInstallmentTO);

    return otPaymentTO;
  }

  /**
   * Sets the installment demographics objects.
   * 
   * @since 2.15
   * @param tktDemoList
   * @param otDemoInfo
   */
  public static void transformInstallmentDemoData(InstallmentDemographicsTO installDemoTO,
      ArrayList<OTFieldTO> otDemoList) {

    // FirstName
    String firstNameString = installDemoTO.getFirstName();
    OTFieldTO aField = new OTFieldTO(OTFieldTO.INST_DEMO_FIRSTNAME, firstNameString);
    otDemoList.add(aField);

    // MiddleName (opt)
    if (installDemoTO.getMiddleName() != null) {
      String middleNameString = installDemoTO.getMiddleName();
      aField = new OTFieldTO(OTFieldTO.INST_DEMO_MIDLNAME, middleNameString);
      otDemoList.add(aField);
    }

    // LastName
    String lastNameString = installDemoTO.getLastName();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_LASTNAME, lastNameString);
    otDemoList.add(aField);

    // DateOfBirth MM/DD/YY (opt)
    if (installDemoTO.getDateOfBirth() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
      String dobString = sdf.format(installDemoTO.getDateOfBirth().getTime());
      aField = new OTFieldTO(OTFieldTO.INST_DEMO_DOB, dobString);
      otDemoList.add(aField);
    }

    // Addr1
    String addr1String = installDemoTO.getAddr1();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_ADDR1, addr1String);
    otDemoList.add(aField);

    // Addr2 (opt)
    if (installDemoTO.getAddr2() != null) {
      String addr2String = installDemoTO.getAddr2();
      aField = new OTFieldTO(OTFieldTO.INST_DEMO_ADDR2, addr2String);
      otDemoList.add(aField);
    }

    // City
    String cityString = installDemoTO.getCity();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_CITY, cityString);
    otDemoList.add(aField);

    // State
    String stateString = installDemoTO.getState();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_STATE, stateString);
    otDemoList.add(aField);

    // ZIP
    String zipString = installDemoTO.getZip();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_ZIP, zipString);
    otDemoList.add(aField);

    // Country
    String countryString = installDemoTO.getCountry();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_COUNTRY, countryString);
    otDemoList.add(aField);

    // Telephone
    String telephoneString = installDemoTO.getTelephone();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_TELEPHONE, telephoneString);
    otDemoList.add(aField);

    // AltTelephone (opt)
    if (installDemoTO.getAltTelephone() != null) {
      String altPhoneString = installDemoTO.getAltTelephone();
      aField = new OTFieldTO(OTFieldTO.INST_DEMO_ALTPHONE, altPhoneString);
      otDemoList.add(aField);
    }

    // Email
    String emailString = installDemoTO.getEmail();
    aField = new OTFieldTO(OTFieldTO.INST_DEMO_EMAIL, emailString);
    otDemoList.add(aField);

    return;
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
  public static void transformTicketDemoData(ArrayList<DemographicsTO> tktDemoList, OTDemographicInfo otDemoInfo) {

    for /* each */(DemographicsTO aDtiTicket : /* in */tktDemoList) {

      OTDemographicData otDemoData = new OTDemographicData();

      // Last/First
      String lastFirstString = DTIFormatter.websafe(aDtiTicket.getLastName().toUpperCase()) + "/"
          + DTIFormatter.websafe(aDtiTicket.getFirstName().toUpperCase());
      OTFieldTO aField = new OTFieldTO(OTFieldTO.TKT_DEMO_LASTFIRST, lastFirstString);
      otDemoData.addOTField(aField);

      // Date of Birth MM/DD/YY
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
      String dobString = sdf.format(aDtiTicket.getDateOfBirth().getTime());
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_DATE_OF_BIRTH, dobString));

      // Gender
      if (aDtiTicket.getGenderType() == DemographicsTO.GenderType.UNSPECIFIED) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_GENDER, UNSPECIFIED_GENDER_DEFAULT));
      } else {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_GENDER, DTIFormatter.websafe(aDtiTicket.getGender()
            .toUpperCase())));
      }

      // Address 1
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_ADDRESS_ONE, DTIFormatter.websafe(aDtiTicket.getAddr1()
          .toUpperCase())));

      // Address 2 (optional)
      if (aDtiTicket.getAddr2() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_ADDRESS_TWO, DTIFormatter.websafe(aDtiTicket.getAddr2()
            .toUpperCase())));
      }

      // City
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_CITY, DTIFormatter.websafe(aDtiTicket.getCity()
          .toUpperCase())));

      // State (optional)
      if (aDtiTicket.getState() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_STATE, DTIFormatter.websafe(aDtiTicket.getState()
            .toUpperCase())));
      }

      // ZIP
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_ZIP, DTIFormatter.websafe(aDtiTicket.getZip())));

      // Country
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_COUNTRY, DTIFormatter.websafe(aDtiTicket.getCountry()
          .toUpperCase())));

      // Telephone
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_TELEPHONE, DTIFormatter.websafe(aDtiTicket.getTelephone()
          .toUpperCase())));

      // Email (optional)
      if (aDtiTicket.getEmail() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_EMAIL, DTIFormatter.websafe(aDtiTicket.getEmail()
            .toUpperCase())));
      }

      // OptInSolicit (2.10)
      if (aDtiTicket.getOptInSolicit().booleanValue() == true) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_OPTINSOLICIT, HKDBusinessRules.YES));
      } else {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.TKT_DEMO_OPTINSOLICIT, HKDBusinessRules.NO));
      }

      otDemoInfo.addOTDemographicData(otDemoData);

    }
    return;
  }

  
 }
