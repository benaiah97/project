package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.ResourceBundle;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.ArchiveKey;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.data.common.EntityTO.DefaultPaymentType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInstallmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.util.PropertyHelper;

/**
 * Class WDWBusinessRules implements the WDW provider specific business rules.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class WDWBusinessRules {

  /** The maximum value of a shell. */
  public static final int SHELLMAXVALUE = 999999;

  /** The constant indicating that a manager has authorized a transaction. */
  public static final String MANAGER = "MGR";

  /** The constant indicating that a system has authorized a transaction. */
  public static final String SYSTEM = "SYS";

  /** The expected length of an OT mag track (73). */
  public static final int MAG1_LENGTH = 73;

  /** The midpoint character expected in an OT mag track. (F) */
  public static final char MIDPOINT_CHAR = 'F';

  /** The index of the midpoint character. */
  public static final int MIDPOINT_CHAR_INDEX = 37;

  /** The standard length for an OT ticket numeric ID (TCOD). (17) */
  public static final int TKTNID_LENGTH = 17;

  /** The standard length for an alphanumeric OT bar code. (17) */
  public static final int BARCODE_LENGTH_ALPHANUM = 17;

  /** The standard length for a numeric OT bar code. (20) */
  public static final int BARCODE_LENGTH_NUMERIC = 20;

  /** The standard length for an Electronic Account. (17) */
  public static final int ACCOUNT_ID_LENGTH = 20;

  /** The standard length for a Media Id. (32) */
  public static final int MEDIA_ID_LENGTH = 32;

  /** The standard length for a Manufacturer's Id. (20) */
  public static final int MFR_ID_LENGTH = 20;

  /** The standard length for a Visual Id. (32) */
  public static final int VISUAL_ID_LENGTH = 32;

  /** Constant indicating the XML Scheme Instance. */
  final static String XML_SCHEMA_INSTANCE = "http://www.w3.org/2001/XMLSchema-instance";

  /** Constant indicating the CAVV Format of Hex (Binary = 1 is not used). */
  private final static String CAVVFORMAT_HEX = "2";

  /** Constant indicating the Disney Vacation Club. */
  final static String DVC_STRING = "DVC";

  /** Properties variable to store properties from AbstractInitializer. */
  private static Properties props = getProperties();

  /** Constant value for attribute insert. */
  private final static String INSERTCMD = "Insert";

  /** Constant value for cmd operator attribute key. */
  private final static Integer ATTRKEY_CMDOPERATOR = new Integer(4);

  /** Constant value for cmd actor attribute key. */
  private final static Integer ATTRKEY_CMDACTOR = new Integer(5);

  /** Constant value for cmd device attribute key. */
  private final static Integer ATTRKEY_CMDDEVICE = new Integer(3);

  /** Constant value for cmd invoice attribute key. */
  private final static Integer ATTRKEY_CMDINVOICE = new Integer(2);

  /** Constant value for tsmac tslocation attribute key. */
  private final static Integer ATTRKEY_TSMACTSLOC = new Integer(1);

  /** Constant value for attribute setting of free input. */
  private final static Integer ATTRTYPE_FREEINPUT = new Integer(4);

  /** Constant value for attribute setting of Not Editable. */
  private final static String NOTEDITABLE = "NotEditable";

  /** Constant value for DSSN Station min length */
  private final static int DSSN_STATION_MIN_LENGTH = 3;

  /** Constant value for DSSN Station max length */
  private final static int DSSN_STATION_MAX_LENGTH = 6;

  /** Constant value for IAGO prefix. */
  private final static String IAGO_PREFIX_VALUE = "999";

  /** Constant value for maximum description length. */
  private final static int DESC_MAX_LENGTH = 25;

  /** Constant indicating no provider error. */
  private final static String OT_NO_ERROR = "No error description.";

  private final static String XBANDID = "XBANDID";
  private final static String GXP_LINK_ID = "GXP_LINK_ID";
  private final static String XBAND_EXTERNAL_NUMBER = "XBAND_EXTERNAL_NUMBER";
  private final static String SWID = "SWID";
  private final static String GUID = "GUID";
  private final static String XBMS_LINK_ID = "XBMS_LINK_ID";
  private final static String XPASSID = "XPASSID";
  private final static String TRANSACTIONAL_GUEST_ID = "TRANSACTIONAL_GUEST_ID";
  private final static String ADMISSION_LINK_ID = "ADMISSION_LINK_ID";
  private final static String PAYMENT_LINK_ID = "PAYMENT_LINK_ID";
  private final static String MEDIA_LINK_ID = "MEDIA_LINK_ID";
  private final static String XID = "XID";
  private final static String DME_LINK_ID = "DME_LINK_ID";
  private final static String SECURE_ID = "SECURE_ID";
  private final static String TXN_GUID = "TXN_GUID";

  /** Indicates YES/true to solicit opt in (2.10) */
  public final static String YES = "YES";
  /** Indicates NO/false to solicit opt in (2.10) */
  public final static String NO = "NO";
  /** Indicates the gender is unspecified. */
  private final static String UNSPECIFIED_GENDER_DEFAULT = " ";

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
   * Provider-centric rules that are also transaction centric are to added in
   * their correct WDWTransactionNamed class. This method is primarily for
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
   * This method changes from the DTITransaction to the WDW provider's request
   * string format.
   * 
   * @param dtiTxn
   *          The dtiTxn object containing the request.
   * @return The WDW provider's request format.
   * @throws DTIException
   *           if called. Currently, the WDW transformation is not supported.
   */
  public static String changeToWDWProviderFormat(DTITransactionTO dtiTxn) throws DTIException {

    String xmlRequest = null;
    TransactionType requestType = dtiTxn.getTransactionType();

    switch (requestType) {

    case QUERYTICKET:
      xmlRequest = WDWQueryTicketRules.transformRequest(dtiTxn);
      break;

    case UPGRADEALPHA:
      xmlRequest = WDWUpgradeAlphaRules.transformRequest(dtiTxn);
      break;

    case VOIDTICKET:
      xmlRequest = WDWVoidTicketRules.transformRequest(dtiTxn);
      break;

    case RESERVATION:
      xmlRequest = WDWReservationRules.transformRequest(dtiTxn);
      break;

    case UPDATETICKET:
      xmlRequest = WDWUpdateTicketRules.transformRequest(dtiTxn);
      break;

    case UPDATETRANSACTION:
      xmlRequest = WDWUpdateTransactionRules.transformRequest(dtiTxn);
      break;

    case CREATETICKET:
      xmlRequest = WDWCreateTicketRules.transformRequest(dtiTxn);
      break;

    case UPGRADEENTITLEMENT: // 2.10
      xmlRequest = WDWUpgradeEntitlementRules.transformRequest(dtiTxn);
      break;

    case ASSOCIATEMEDIATOACCOUNT: // 2.16.1 BIEST001
      xmlRequest = WDWAssociateMediaToAccountRules.transformRequest(dtiTxn);
      break;

    case TICKERATEENTITLEMENT: // 2.16.1 BIEST001
      xmlRequest = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
      break;

    case RENEWENTITLEMENT: // As of 2.16.1, JTL
      xmlRequest = WDWRenewEntitlementRules.transformRequest(dtiTxn);
      break;

    case QUERYRESERVATION: // As of 2.16.1 BIEST001
      xmlRequest = WDWQueryReservationRules.transformRequest(dtiTxn);
      break;
      
    case VOIDRESERVATION: // As of 2.16.3, JTL
      xmlRequest = WDWVoidReservationRules.transformRequest(dtiTxn);
      break;  
      
    case QUERYELIGIBLEPRODUCTS: // As a part of AP Upgrade Service
        xmlRequest = WDWQueryEligibleProductsRules.transformRequest(dtiTxn);
        break;  

    default:
      throw new DTIException(TransformRules.class, DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Invalid WDW transaction type sent to DTI Gateway.  Unsupported.");
    }

    return xmlRequest;
  }

  /**
   * Parses the xmlResponse string from the WDW provider into the
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
  public static DTITransactionTO changeWDWProviderFormatToDti(DTITransactionTO dtiTxn, String xmlResponse)
      throws DTIException {

    transformResponse(dtiTxn, xmlResponse);

    return dtiTxn;
  }

  /**
   * Enforces the following rules: <BR>
   * 1. If the TicketIdType is MAG, then it must be 73 characters long with an
   * 'F' in position 37. <BR>
   * 2. If the TicketIdType is DSSN, then all four components must be filled out
   * and the DSSN Station must be of a valid length. <BR>
   * 3. If the TicketIdType is TKTNID, then it must be 17 characters long. <BR>
   * 4. If the TicketIdType is BARCODE, then it must be 20 characters long
   * (new). <BR>
   * 5. There may only be one TicketIdType per in-bound ticket.
   * 
   * @param aTktList
   *          ArrayList of tickets.
   * @throws DTIException
   *           for any failure of validation
   */
  static void validateInBoundWDWTickets(ArrayList<TicketTO> aTktList) throws DTIException {

    for /* each */(TicketTO aTicketTO : /* in */aTktList) {

      // Only one TktId on this ticket?
      if (aTicketTO.getTicketTypes().size() != 1)
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
            "In-bound WDW txn with <> 1 TktId: " + aTicketTO.getTicketTypes().size());

      // Mag Track checks
      if (aTicketTO.getTicketTypes().get(0) == TicketTO.TicketIdType.MAG_ID) {

        if (aTicketTO.getMagTrack1().length() != MAG1_LENGTH)
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid Mag length: " + aTicketTO.getMagTrack1().length());

        char midChar = Character.toUpperCase(aTicketTO.getMagTrack1().charAt(MIDPOINT_CHAR_INDEX));
        if (midChar != MIDPOINT_CHAR)
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid midpoint char: " + midChar);

      }

      // DSSN
      if (aTicketTO.getTicketTypes().get(0) == TicketTO.TicketIdType.DSSN_ID) {

        if ((aTicketTO.getDssnDate() == null) || (aTicketTO.getDssnSite() == null)
            || (aTicketTO.getDssnStation() == null) || (aTicketTO.getDssnNumber() == null))
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid DSSN.");

        if ((aTicketTO.getDssnSite().length() == 0) || (aTicketTO.getDssnStation().length() == 0)
            || (aTicketTO.getDssnNumber().length() == 0))
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid DSSN.");

        if ((aTicketTO.getDssnStation().length() == DSSN_STATION_MIN_LENGTH)
            || (aTicketTO.getDssnStation().length() == DSSN_STATION_MAX_LENGTH)) {
        } else {
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "For DSSN transactions, station must be either 3 or 6 digits long.  Request had length of "
                  + aTicketTO.getDssnStation().length() + ".");
        }

      }

      // TKTNID
      if (aTicketTO.getTicketTypes().get(0) == TicketTO.TicketIdType.TKTNID_ID) {

        if (aTicketTO.getTktNID().length() != TKTNID_LENGTH)
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid TktNID length: " + aTicketTO.getTktNID().length());
      }

      // BARCODE
      if (aTicketTO.getTicketTypes().get(0) == TicketTO.TicketIdType.BARCODE_ID) {
        if ((aTicketTO.getBarCode().length() != BARCODE_LENGTH_NUMERIC)
            && (aTicketTO.getBarCode().length() != BARCODE_LENGTH_ALPHANUM))
          throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
              "In-bound WDW txn with invalid Barcode length: " + aTicketTO.getBarCode().length());
      }

    }

  }

  /**
   * Validate ticket shell active.<BR>
   * Enforces the following rules:<BR>
   * 1. The ticket shell must be active.
   * 
   * @param orderShells
   *          the order shell numbers
   * @param activeShells
   *          the active shell numbers
   * 
   * @throws DTIException
   *           the DTI exception
   */
  static void validateTicketShellActive(HashSet<Integer> orderShells, ArrayList<Integer> activeShells)
      throws DTIException {

    if (orderShells.size() == 0)
      return;

    if ((activeShells == null) || (activeShells.size() == 0))
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_SHELL_NUMBER,
          "No shells on ticket order are known to the database.");

    for /* each */(Integer anOrderShell : /* in */orderShells) {

      if (activeShells.contains(anOrderShell))
        continue;
      else
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_SHELL_NUMBER, "Shell "
            + anOrderShell.toString() + " is not known or is not active in the database.");
    }

    return;

  }

  /**
   * Validate ticket shell to product.<BR>
   * Enforces the following rules:<BR>
   * 1. Ticket shells provided must be associated to the product being sold. *
   * 
   * @param prodShellsXRef
   *          the prod shells x ref
   * @param aTktListTO
   *          the a tkt list to
   * 
   * @throws DTIException
   *           the DTI exception
   */
  static void validateTicketShellToProduct(ArrayList<TicketTO> aTktListTO,
      HashMap<String, ArrayList<Integer>> prodShellsXRef) throws DTIException {

    for /* each */(TicketTO aTicketTO : /* in */aTktListTO) {

      String productCode = aTicketTO.getProdCode();
      String shellString = getShellStringFromTicket(aTicketTO);
      if (shellString == null)
        continue;
      Integer shellNbr = new Integer(shellString);

      ArrayList<Integer> validShells = prodShellsXRef.get(productCode);
      if ((validShells == null) || (!validShells.contains(shellNbr)))
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_SHELL_FOR_PRODUCT, "Shell "
            + shellNbr.toString() + " not associated with product " + productCode + " in the database.");
    }

    return;

  }

  /**
   * Validate ticket order shells by enforcing the following rules:<BR>
   * 1. If ticket contains a shell number, it must be valid and numeric.
   * 
   * @param aTktListTO
   *          the a tkt list to
   * 
   * @return The valid shell numbers.
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  static HashSet<Integer> validateTicketOrderShells(ArrayList<TicketTO> aTktListTO) throws DTIException {

    HashSet<Integer> aSet = new HashSet<Integer>();

    for /* each */(TicketTO aTicketTO : /* in */aTktListTO) {

      String shellString = getShellStringFromTicket(aTicketTO);

      if (shellString == null)
        continue;

      int shellNbr;
      try {
        shellNbr = Integer.parseInt(shellString);
      } catch (NumberFormatException nfe) {
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_SHELL_NUMBER, "Ticket item "
            + aTicketTO.getTktItem().intValue() + " Mag Track 2 contains non-numeric shell number: " + shellString);
      }

      if ((shellNbr <= 0) || (shellNbr > SHELLMAXVALUE))
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.INVALID_SHELL_NUMBER, "Ticket item "
            + aTicketTO.getTktItem().intValue() + " Mag Track 2 contains invalid shell number: " + shellString);

      if (shellString.length() != 0)
        aSet.add(new Integer(shellString));
    }

    return aSet;

  }

  /**
   * Gets the shell string from the ticket object.
   * 
   * @param aTicketTO
   *          the DTI Ticket object
   * @return the shell string
   */
  private static String getShellStringFromTicket(TicketTO aTicketTO) {

    String shellString = null;

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();
    if (ticketTypes.contains(TicketTO.TicketIdType.MAG_ID)) {

      if (aTicketTO.getMagTrack2() != null) {

        String magTrack2 = aTicketTO.getMagTrack2();
        if (magTrack2.contains("=")) {

          int indexOf = magTrack2.indexOf('=');
          shellString = magTrack2.substring(0, indexOf);

        }
      }
    }

    return shellString;

  }

  /**
   * Validates the void ticket actor to make sure it satisfies one of the two
   * required values.<BR>
   * (MGR or SYS).
   * 
   * @param cmdHeader
   *          the DTI command header transfer object.
   * @throws DTIException
   *           if the rule is violated.
   */
  static void validateVoidTicketActor(CommandHeaderTO cmdHeader) throws DTIException {

    String cmdActor = cmdHeader.getCmdActor();

    if (cmdActor == null)
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.ACTOR_NOT_AUTHORIZED,
          "No CmdActor tag provided where required.");

    if ((cmdActor.compareTo(WDWBusinessRules.MANAGER) != 0) && (cmdActor.compareTo(WDWBusinessRules.SYSTEM) != 0))
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.ACTOR_NOT_AUTHORIZED, "CmdActor tag of " + cmdActor
          + " not authorized for void (MGR or SYS only)");

    return;
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
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Operating Area not set up in DTI database.");
    }
    hdr.setOperatingArea(anAttributeTO.getAttrValue());

    // User ID
    anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.USER);
    if (anAttributeTO == null) {
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "User ID not set up in DTI database.");
    }
    String userIdString = anAttributeTO.getAttrValue();
    Integer userId = null;
    try {
      userId = Integer.decode(userIdString);
    } catch (NumberFormatException nfe) {
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "User ID not a parsable integer: "
          + userIdString);
    }
    hdr.setUserId(userId);

    // Password
    anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.PASS);
    if (anAttributeTO == null) {
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Password not set up in DTI database.");
    }
    String passwordString = anAttributeTO.getAttrValue();
    Integer password = null;
    try {
      password = Integer.decode(passwordString);
    } catch (NumberFormatException nfe) {
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_DATA_ERROR, "Password not a parsable integer: "
          + passwordString);
    }
    hdr.setPassword(password);
    return hdr;
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
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR,
          "Internal Error:  Cannot get properties values.");

    String siteNumberString = PropertyHelper.readPropsValue(PropertyName.ATS_SITE_NUMBER, props, null);
    Integer siteNumber;

    if (siteNumberString == null)
      throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR, "Internal Error:  No "
          + PropertyName.ATS_SITE_NUMBER + " in properties file.");
    else {
      try {
        siteNumber = Integer.decode(siteNumberString);
      } catch (NumberFormatException nfe) {
        throw new DTIException(WDWBusinessRules.class, DTIErrorCode.DTI_CONFIG_ERROR, "Internal Error:  "
            + PropertyName.ATS_SITE_NUMBER + " in properties file is not numeric.");
      }
    }

    return siteNumber;

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
   * Sets the in-transaction attributes on the Omni Ticket transfer object based
   * on data provided in the DTI transfer objects.
   * 
   * @param dtiTxn
   *          The DTI Transaction object.
   * @param inTxnAttrList
   *          The list of Omni Ticket In-Transaction Attribute Transfer Objects.
   */
  static void setOTInTransactionAttributes(DTITransactionTO dtiTxn, ArrayList<OTInTransactionAttributeTO> inTxnAttrList) {

    CommandHeaderTO dtiCmdHdr = dtiTxn.getRequest().getCommandHeader();
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    String cmdActor = dtiCmdHdr.getCmdActor();

    if ((cmdActor == null) || (cmdActor.length() == 0) || (cmdActor.compareTo(MANAGER) == 0)) {

      // CmdOperator
      String cmdOperator = dtiRequest.getCommandHeader().getCmdOperator();

      OTInTransactionAttributeTO inTxnAttr = new OTInTransactionAttributeTO();
      inTxnAttr.setAttributeCmd(INSERTCMD);
      inTxnAttr.setAttributeKey(ATTRKEY_CMDOPERATOR);
      inTxnAttr.setAttributeValue(cmdOperator);
      inTxnAttr.setAttributeType(ATTRTYPE_FREEINPUT);
      inTxnAttr.setAttributeFlag(NOTEDITABLE);
      inTxnAttrList.add(inTxnAttr);

      // CmdActor (only for "MGR")
      if ((cmdActor != null) && (cmdActor.compareTo(MANAGER) == 0)) {
        inTxnAttr = new OTInTransactionAttributeTO();
        inTxnAttr.setAttributeCmd(INSERTCMD);
        inTxnAttr.setAttributeKey(ATTRKEY_CMDACTOR);
        inTxnAttr.setAttributeValue(cmdActor);
        inTxnAttr.setAttributeType(ATTRTYPE_FREEINPUT);
        inTxnAttr.setAttributeFlag(NOTEDITABLE);
        inTxnAttrList.add(inTxnAttr);
      }

      // CmdDevice
      String cmdDevice = dtiCmdHdr.getCmdDevice();
      inTxnAttr = new OTInTransactionAttributeTO();
      inTxnAttr.setAttributeCmd(INSERTCMD);
      inTxnAttr.setAttributeKey(ATTRKEY_CMDDEVICE);
      inTxnAttr.setAttributeValue(cmdDevice);
      inTxnAttr.setAttributeType(ATTRTYPE_FREEINPUT);
      inTxnAttr.setAttributeFlag(NOTEDITABLE);
      inTxnAttrList.add(inTxnAttr);

      // CmdInvoice
      String cmdInvoice = dtiCmdHdr.getCmdInvoice();
      inTxnAttr = new OTInTransactionAttributeTO();
      inTxnAttr.setAttributeCmd(INSERTCMD);
      inTxnAttr.setAttributeKey(ATTRKEY_CMDINVOICE);
      inTxnAttr.setAttributeValue(cmdInvoice);
      inTxnAttr.setAttributeType(ATTRTYPE_FREEINPUT);
      inTxnAttr.setAttributeFlag(NOTEDITABLE);
      inTxnAttrList.add(inTxnAttr);

      // TSMAC.TSLOC
      String tsMac = dtiRequest.getPayloadHeader().getTktSeller().getTsMac();
      String tsLocation = dtiRequest.getPayloadHeader().getTktSeller().getTsLocation();
      inTxnAttr = new OTInTransactionAttributeTO();
      inTxnAttr.setAttributeCmd(INSERTCMD);
      inTxnAttr.setAttributeKey(ATTRKEY_TSMACTSLOC);
      inTxnAttr.setAttributeValue(tsMac + "." + tsLocation);
      inTxnAttr.setAttributeType(ATTRTYPE_FREEINPUT);
      inTxnAttr.setAttributeFlag(NOTEDITABLE);
      inTxnAttrList.add(inTxnAttr);

      // Prod Price (omitted)
      /*
       * Note, while code was in the old gateway to populate an in-transaction
       * attribute for <Product Price>, having spaces in the tag name is
       * actually not legal in XML. While WMB would not error on this type of
       * mistake, the Java version will not compile if it is attempted.
       * Therefore, mapping product price to attribute key 6 with a value
       * comprised of the product code (again, unknown where this would come
       * from) and a colon (":") plus the prod price has been omitted as code
       * that never functioned. JTL
       */

      return;
    }
  }

  /**
   * Sets the Omni Ticket transfer object based upon the DTI ticket and ticket
   * type provided.
   * 
   * @param dtiTicket
   *          The DTI Ticket transfer object.
   * @param dtiTicketType
   *          The ticket type (indicating which ticket identity was used).
   * @return The Omni Ticket Ticket Transfer Object.
   */
  static OTTicketTO setOTTicketTO(TicketTO dtiTicket, TicketIdType dtiTicketType) {
    OTTicketTO otTicket = new OTTicketTO();
    switch (dtiTicketType) {

    case DSSN_ID:
      otTicket.setTDssn(dtiTicket.getDssnDate(), dtiTicket.getDssnSite(), dtiTicket.getDssnStation(),
          dtiTicket.getDssnNumber());
      break;
    case TKTNID_ID:
      otTicket.setTCOD(dtiTicket.getTktNID());
      break;
    case BARCODE_ID:
      otTicket.setBarCode(dtiTicket.getBarCode());
      break;
    case MAG_ID:
      otTicket.setMagTrack(dtiTicket.getMagTrack1());
      break;
    case EXTERNAL_ID:
      otTicket.setExternalTicketCode(dtiTicket.getExternal());
      break;
    }
    return otTicket;
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

    OTCommandTO otCmdTO = OTCommandXML.getTO(xmlResponse);

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

      String payloadId = null;
      OTTransactionType requestType = otCmdTO.getTxnType();
      TransactionType dtiTransType = dtiTxn.getTransactionType();

      switch (requestType) {

      case QUERYTICKET:
    	  
    	  switch(dtiTransType){
    	  //When the transaction type is QUERYTICKET
    	  case QUERYTICKET: 	  
    		WDWQueryTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
    	        break;
    	     // added as a part of AP Upgrade Service
    	     // TODO this may not be necessary as we may not need to even return the response back to the client
    	  case QUERYELIGIBLEPRODUCTS:
    		WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        	  break;
		
    	  default:
			
			break;
    	  }
    	  break;

      case VOIDTICKET:

        WDWVoidTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        payloadId = dtiTxn.getRequest().getPayloadHeader().getPayloadID();
        ArchiveKey.deleteVoidTicketRequest(payloadId);
        break;

      case UPGRADETICKET:

        payloadId = dtiTxn.getRequest().getPayloadHeader().getPayloadID();
        ArchiveKey.deleteUpgradeAlphaRequest(payloadId);
        break;

      default:
        break;

      }

    } else {

      // If the provider had no error, transform the response.
      OTTransactionType requestType = otCmdTO.getTxnType();
      TransactionType dtiTransType = dtiTxn.getTransactionType();

      switch (requestType) {
      // TODO potential changes of style from switch/case to if/else or vice versa (see how UPGRADETICKET is done)
      case QUERYTICKET:
    	  
    	  switch(dtiTransType){
    	  
    	  case QUERYTICKET: 	  
    		  WDWQueryTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
    	        break;
    	  
    	  case QUERYELIGIBLEPRODUCTS:
    		// added as a part of AP Upgrade Service
        	  WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        	  break;
		
    	  default:
			break;
    	  }
    	  break;

      case UPGRADETICKET:

        /*
         * With 2.10, the complexity here increases slightly as a single
         * response type from ATS can be translated into two different commands
         * with DTI. The answer here is NOT to create "faux" ATS return
         * transaction types (which don't exist) but rather to condition
         * translation of the response based upon the transaction currently
         * understood by the DTI Transaction Object. It's the ultimate arbiter
         * of what the in-bound command context was supposed to be.
         */
        if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.UPGRADEALPHA) {
          WDWUpgradeAlphaRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          // Always update upgrade alpha responses in reporting
          // tables.
          ArchiveKey.updateUpgradeAlphaResponse(dtiTxn);

        } else { // Upgrade Entitlement
          WDWUpgradeEntitlementRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        }
        break;

      case VOIDTICKET:
        WDWVoidTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        // Always update upgrade alpha responses in reporting tables.
        ArchiveKey.updateVoidTicketResponse(dtiTxn);
        break;

      case MANAGERESERVATION: // As of 2.16.1 BIEST001

        switch (dtiTransType) {

        case RESERVATION:
          WDWReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;

        case QUERYRESERVATION:
          WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;
          
        case VOIDRESERVATION:
          WDWVoidReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;          

        default:
          break;

        }

        break;

      case UPDATETICKET:
        WDWUpdateTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        break;

      case UPDATETRANSACTION:
        WDWUpdateTransactionRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        break;

      case CREATETRANSACTION:
        WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        break;

      case MULTIENTITLEMENTACCOUNT:

        // 2.16.1 BIEST001
        // The MultiEntitlementAccount signature is quite diverse in the number
        // of functions
        // that it controls in the RFID space. It provides the ability to Create
        // Entitlements (legacy and electronic),
        // Transfer Entitlements from one account to another, Order Entitlements
        // so that certain tickets have usage applied first,
        // Create Electronic Accounts w/ or w/out media and entitlements on
        // them, Query Account, Merge the contents of one account
        // to a different account, Change the Status of an account, Associate
        // Media to an Account, Deassociate Media from an Account,
        // and Change the Status of Media.

        // The response comes back identical for all of these things...but we
        // need to know what function was originally called

        switch (dtiTransType) {
        case ASSOCIATEMEDIATOACCOUNT:
          WDWAssociateMediaToAccountRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;
        case TICKERATEENTITLEMENT:
          WDWTickerateEntitlementRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
          break;
        default:
          break;
        }

        break;

      case RENEWTICKET: // As of 2.16.1, JTL
        WDWRenewEntitlementRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
        break;

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
  static void setDTIPaymentList(ArrayList<PaymentTO> dtiPmtList, ArrayList<OTPaymentTO> otPmtList) {

    if ((otPmtList != null) && (otPmtList.size() > 0)) {

      long counter = 0;
      for /* each */(OTPaymentTO otPaymentTO : /* in */otPmtList) {
        
        if (otPaymentTO.getPayType() == OTPaymentTO.PaymentType.VOUCHER) {
          continue; // Nothing to do, here.
        }

        // Installment (as of 2.15, JTL)
        if (otPaymentTO.getPayType() == OTPaymentTO.PaymentType.INSTALLMENT) {
          PaymentTO dtiPmtTO = new PaymentTO();
          InstallmentTO installTO = new InstallmentTO();
          OTInstallmentTO OTinstall = otPaymentTO.getInstallment();

          if (OTinstall.getContractId() != null) {
            installTO.setContractId(OTinstall.getContractId());
            dtiPmtTO.setInstallment(installTO);
            dtiPmtList.add(dtiPmtTO);
          } // Else do not add this to the response; it will cause NPEs.
        }

        if (otPaymentTO.getPayType() == OTPaymentTO.PaymentType.CREDITCARD) {

          PaymentTO dtiPmtTO = new PaymentTO();
          
          dtiPmtTO.setPayItem(BigInteger.valueOf(++counter));
          
          // Add payment amount to query reservation payments 
          dtiPmtTO.setPayAmount(otPaymentTO.getPayAmount());

          OTCreditCardTO otCreditCardTO = otPaymentTO.getCreditCard();

          if (otCreditCardTO.isGiftCardIndicator()) { // Gift Card

            GiftCardTO dtiGiftCardTO = new GiftCardTO();
            dtiGiftCardTO.setGcAuthCode(otCreditCardTO.getAuthErrorCode());
            if (otCreditCardTO.getAuthNumber() != null)
              dtiGiftCardTO.setGcAuthNumber(otCreditCardTO.getAuthNumber());

            // AuthSystemResponse, as specified in the old gateway
            // is not present in either the NeXML spec or in their normal
            // responses. (omitted)
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
      OTFieldTO aField = new OTFieldTO(OTFieldTO.WDW_TKTDEMO_LASTFIRST, lastFirstString);
      otDemoData.addOTField(aField);

      // Date of Birth MM/DD/YY
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
      String dobString = sdf.format(aDtiTicket.getDateOfBirth().getTime());
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_DTE_OF_BIRTH, dobString));

      // Gender
      if (aDtiTicket.getGenderType() == DemographicsTO.GenderType.UNSPECIFIED) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_GENDER, UNSPECIFIED_GENDER_DEFAULT));
      } else {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_GENDER, DTIFormatter.websafe(aDtiTicket.getGender()
            .toUpperCase())));
      }

      // Address 1
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_ADDRESS_ONE, DTIFormatter.websafe(aDtiTicket.getAddr1()
          .toUpperCase())));

      // Address 2 (optional)
      if (aDtiTicket.getAddr2() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_ADDRESS_TWO, DTIFormatter.websafe(aDtiTicket.getAddr2()
            .toUpperCase())));
      }

      // City
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_CITY, DTIFormatter.websafe(aDtiTicket.getCity()
          .toUpperCase())));

      // State (optional)
      if (aDtiTicket.getState() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_STATE, DTIFormatter.websafe(aDtiTicket.getState()
            .toUpperCase())));
      }

      // ZIP
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_ZIP, DTIFormatter.websafe(aDtiTicket.getZip())));

      // Country
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_COUNTRY, DTIFormatter.websafe(aDtiTicket.getCountry()
          .toUpperCase())));

      // Telephone
      otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_PHONE, DTIFormatter.websafe(aDtiTicket.getTelephone()
          .toUpperCase())));

      // Email (optional)
      if (aDtiTicket.getEmail() != null) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_EMAIL, DTIFormatter.websafe(aDtiTicket.getEmail()
            .toUpperCase())));
      }

      // OptInSolicit (2.10)
      if (aDtiTicket.getOptInSolicit().booleanValue() == true) {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_OPTINSOLICIT, WDWBusinessRules.YES));
      } else {
        otDemoData.addOTField(new OTFieldTO(OTFieldTO.WDW_TKTDEMO_OPTINSOLICIT, WDWBusinessRules.NO));
      }

      otDemoInfo.addOTDemographicData(otDemoData);

    }
    return;
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
    OTFieldTO aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_FIRSTNAME, firstNameString);
    otDemoList.add(aField);

    // MiddleName (opt)
    if (installDemoTO.getMiddleName() != null) {
      String middleNameString = installDemoTO.getMiddleName();
      aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_MIDLNAME, middleNameString);
      otDemoList.add(aField);
    }

    // LastName
    String lastNameString = installDemoTO.getLastName();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_LASTNAME, lastNameString);
    otDemoList.add(aField);

    // DateOfBirth MM/DD/YY (opt)
    if (installDemoTO.getDateOfBirth() != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
      String dobString = sdf.format(installDemoTO.getDateOfBirth().getTime());
      aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_DOB, dobString);
      otDemoList.add(aField);
    }

    // Addr1
    String addr1String = installDemoTO.getAddr1();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_ADDR1, addr1String);
    otDemoList.add(aField);

    // Addr2 (opt)
    if (installDemoTO.getAddr2() != null) {
      String addr2String = installDemoTO.getAddr2();
      aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_ADDR2, addr2String);
      otDemoList.add(aField);
    }

    // City
    String cityString = installDemoTO.getCity();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_CITY, cityString);
    otDemoList.add(aField);

    // State
    String stateString = installDemoTO.getState();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_STATE, stateString);
    otDemoList.add(aField);

    // ZIP
    String zipString = installDemoTO.getZip();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_ZIP, zipString);
    otDemoList.add(aField);

    // Country
    String countryString = installDemoTO.getCountry();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_COUNTRY, countryString);
    otDemoList.add(aField);

    // Telephone
    String telephoneString = installDemoTO.getTelephone();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_TELEPHONE, telephoneString);
    otDemoList.add(aField);

    // AltTelephone (opt)
    if (installDemoTO.getAltTelephone() != null) {
      String altPhoneString = installDemoTO.getAltTelephone();
      aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_ALTPHONE, altPhoneString);
      otDemoList.add(aField);
    }

    // Email
    String emailString = installDemoTO.getEmail();
    aField = new OTFieldTO(OTFieldTO.WDW_INSTDEMO_EMAIL, emailString);
    otDemoList.add(aField);

    return;
  }

  /**
   * Get the Omni Ticket Byte value for the External Reference Type given to DTI
   * in a Reservation Request. If it is an invalid type, return NULL.
   * 
   * @param dtiType
   * @return
   */
  public static Byte transformEntitlementExternalReferenceType(String dtiType) {
    Byte OTByte = null;
    if (XBANDID.equals(dtiType)) {
      OTByte = new Byte("1");
    } else if (GXP_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("2");
    } else if (XBAND_EXTERNAL_NUMBER.equals(dtiType)) {
      OTByte = new Byte("3");
    } else if (SWID.equals(dtiType)) {
      OTByte = new Byte("4");
    } else if (GUID.equals(dtiType)) {
      OTByte = new Byte("5");
    } else if (XBMS_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("6");
    } else if (XPASSID.equals(dtiType)) {
      OTByte = new Byte("7");
    } else if (TRANSACTIONAL_GUEST_ID.equals(dtiType)) {
      OTByte = new Byte("8");
    } else if (ADMISSION_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("9");
    } else if (PAYMENT_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("10");
    } else if (MEDIA_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("11");
    } else if (XID.equals(dtiType)) {
      OTByte = new Byte("12");
    } else if (DME_LINK_ID.equals(dtiType)) {
      OTByte = new Byte("13");
    } else if (SECURE_ID.equals(dtiType)) {
      OTByte = new Byte("14");
    } else if (TXN_GUID.equals(dtiType)) {
      OTByte = new Byte("15");
    } else {
      // Invalid External Reference Account Type
      OTByte = null;
    }
    return OTByte;
  }

}