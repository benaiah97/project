package pvt.disney.dti.gateway.rules.hkd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
//import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
//import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.dao.TransidRescodeKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.AttributeTO.CmdAttrCodeType;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.ShowTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TransidRescodeTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicInfo;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTExternalTransactionIDTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTReservationDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTShowDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.provider.hkd.xml.HkdOTCommandXML;
import pvt.disney.dti.gateway.rules.PaymentRules;
import pvt.disney.dti.gateway.rules.ProductRules;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.rules.race.utility.AlgorithmUtility;
import pvt.disney.dti.gateway.util.DTIFormatter;

/**
 * This class is responsible for three major functions for WDW reservations:<BR>
 * 1. Defining the business rules specific to WDW reservations.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019, moon012
 * @since 2.16.3
 * 
 */
public class HKDReservationRules {

  private static final Class<HKDReservationRules> THISOBJECT = HKDReservationRules.class;

  /** Request type header constant. */
  private final static String REQUEST_TYPE_MANAGE = "Manage";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_MANAGERES = "ManageReservation";

  /** Constant indicating all tags should be created. */
  private final static String ALL_TAGS = "All";

  /** Constant indicating the Manage Reservation XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "ManageReservationRequest.xsd";

  /**
   * Constant indicating the create variant of the Manage Reservation command.
   */
  private final static String COMMAND_TYPE = "Create";

  /** Constant indicating the item type of ticket. */
  private final static String ITEM_TYPE = "T";

  private final static String MANUALMAILORDER = "ManualMailOrder";

  private final static String PRESALE = "Presale";

  /** Constant indicating that in-bound client number was provided. */
  private final static Integer NO_CLIENT_PROVIDED = new Integer(0);

  /** Constant representing the default member ID. */
  private final static Integer DEFAULT_MEMBERID = new Integer(0);

  /** Constant representing the first field number. */
  private final static Integer FIELD_NUMBER_ONE = new Integer(1);

  /** Constant representing the ATS carriage return. */
  private final static String ATSCR = "^";

  /**
   * Constant representing the maximum number of line items allowed on a
   * reservation.
   */
  private final static int MAX_RES_LINE_ITEMS = 20;

  /** Constant representing the appropriate value for a major client type. */
  private final static String MAJOR_CLIENT_TYPE = "Major";

  /** Constant representing the appropriate value for a major client type. */
  private final static String PRIVATE_CLIENT_TYPE = "Private";

  /** The maximum number of note details (20). */
  public final static int MAX_NUMBER_OF_NOTE_DETAILS = 20;

  /** The maximum note detail line length (50). */
  public final static int MAX_NOTE_DETAIL_LINE_LENGTH = 50;

  private static final EventLogger logger = EventLogger.getLogger(HKDReservationRules.class.getCanonicalName());

  //private static int atsMaxEncodeAllCnt = 200;

  /**
   * Pull in any rule values from the properties file.
   * 
   * @param props
   */
  public static void initHKDReservationRules(Properties props) {

//    String encodeCountString = null;
//    encodeCountString = PropertyHelper.readPropsValue(PropertyName.ATS_MAX_ENCODE_ALL_COUNT, props, null);
//
//    if (encodeCountString != null) {
//
//      try {
//        atsMaxEncodeAllCnt = Integer.parseInt(encodeCountString);
//      } catch (NumberFormatException nfe) {
//        logger.sendEvent("Unable to convert ATS.MaxEncodeAllCount property to a number.  Taking default.",
//            EventType.WARN, THISOBJECT);
//      }
//    }

    return;
  }

  /**
   * 
   * @param dtiTxn
   * @throws DTIException
   */
  public static void applyHKDReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;
    ArrayList<TicketTO> tktListTO = dtiResReq.getTktList();

    // Validate that the client ID is numeric and correct.
    validateHKDClientId(dtiResReq);

    // Ensure TP_Lookups have been populated in the database
    validateAndSaveTPLookups(dtiTxn, dtiResReq);

    // Validate that a reservation doesn't have more than 20 line items
    validateMaxResLineItems(dtiResReq);

    // Validate that major clients do not send billing or shipping
    // demographics
    validateMajorClientDemo(dtiResReq);

    // Validate that if other ticket demographics have been provided, phone has
    // been provided, as well.
    ProductRules.validateHkdTicketDemo(tktListTO);

    // RULE: Validate that if the "installment" type of payment is present,
    ArrayList<TPLookupTO> tpLookups = dtiTxn.getTpLookupTOList();
    PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);

    // RULE: assignResCode
    String resCode = assignResCode(dtiTxn, dtiResReq);
    dtiResReq.getReservation().setResCode(resCode);

    return;
  }

  /**
   * Assign a reservation code based on reservation code rules. If it is not a
   * rework, and the attribute is not set to not use race rescode generation,
   * then create a new rescode and insert it into the the transaction id rescode
   * xref table.
   * 
   * TODO: What if we do find it/can't insert? If I can know which index failed,
   * then that determines remediation. If I can't... (1) getTransidRescodeFromDB
   * (if not null, then it was already created) (2) getTransidRescodeFromDB (if
   * null, then recreate (x# of times).
   * 
   * @param dtiTxn
   *          the dti txn
   * @param dtiResReq
   *          the dti res req
   * @return the string
   * @throws DTIException
   *           the DTI exception
   */
  private static String assignResCode(DTITransactionTO dtiTxn, ReservationRequestTO dtiResReq) throws DTIException {
    String resCode = null;

    String payloadId = dtiTxn.getRequest().getPayloadHeader().getPayloadID();
    HashMap<CmdAttrCodeType, AttributeTO> attribMap = dtiTxn.getAttributeTOMap();
	
    boolean isRework = dtiTxn.isRework();
    //no check for RACE override for HKDL needed at this time
    
    // if this is a re-work, a reservation code should exist
    if (isRework) {
      // Get the reservation code array (there should only be one)
    	TransidRescodeTO rescodeTO = TransidRescodeKey.getTransidRescodeFromDB(payloadId);
    	
    	// Contingency assignment (should be infrequent that res isn't found in DB for rework)
    	if (rescodeTO == null) {
        AttributeTO resPrefixAttr = attribMap.get(CmdAttrCodeType.SELLER_RES_PREFIX);
        String sellerResPrefix = resPrefixAttr.getAttrValue();
        // generate the rescode and insert it into the database payload/rescode
        // ref table
        resCode = AlgorithmUtility.generateResCode(sellerResPrefix);
        TransidRescodeKey.insertTransIdRescode(dtiTxn.getTransIdITS(), payloadId, resCode);
    	} else {
    	  resCode = rescodeTO.getRescode();
    	}
    	
    } else { // it is not a rework so generate rescode from RACE utility
    	AttributeTO resPrefixAttr = attribMap.get(CmdAttrCodeType.SELLER_RES_PREFIX);
    	String sellerResPrefix = resPrefixAttr.getAttrValue();
    	// generate the rescode and insert it into the database payload/rescode
    	// ref table
    	resCode = AlgorithmUtility.generateResCode(sellerResPrefix);
    	TransidRescodeKey.insertTransIdRescode(dtiTxn.getTransIdITS(), payloadId, resCode);
    }
    return resCode;
  }

  /**
   * Transform the DTITransactionTO value object to the provider value objects
   * and then pass those to XML Marshaling routines to create an XML string.
   * 
   * @param dtiTxn
   *          the DTI Transaction object.
   * @return the XML string version of the provider request.
   * @throws DTIException
   *           when any transformation error is encountered.
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlString = null;
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;

    // === Command Level ===
    HkdOTCommandTO atsCommand = new HkdOTCommandTO(HkdOTCommandTO.OTTransactionType.MANAGERESERVATION);
    atsCommand.setXmlSchemaInstance(HKDBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    HkdOTHeaderTO hdr = HKDBusinessRules.transformOTHeader(dtiTxn, REQUEST_TYPE_MANAGE, REQUEST_SUBTYPE_MANAGERES);
    atsCommand.setHeaderTO(hdr);

    // === Manage Reservation Level ===
    HkdOTManageReservationTO otManageRes = new HkdOTManageReservationTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    tagList.add(ALL_TAGS);
    otManageRes.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();
    AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otManageRes.setSiteNumber(HKDBusinessRules.getSiteNumberProperty());
    } else {
      otManageRes.setSiteNumber(Integer.parseInt(anAttributeTO.getAttrValue()));
    }

    // CommandType
    otManageRes.setCommandType(COMMAND_TYPE);

    // ReservationCode
    if (dtiResReq.getReservation() != null) {
      ReservationTO resTO = dtiResReq.getReservation();
      if (resTO.getResCode() != null) {
        otManageRes.setReservationCode(resTO.getResCode());
      }
    }

    // Product
    ArrayList<TicketTO> dtiTktList = dtiResReq.getTktList();
    HashMap<String, DBProductTO> dtiPdtMap = dtiTxn.getDbProdMap();
    createOTProducts(otManageRes, dtiTktList, dtiPdtMap);

    // Payment Processing
    // There are three alternatives: default payment, no payment, and
    // payment.
    ArrayList<HkdOTPaymentTO> otPaymentList = otManageRes.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiResReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();
    HKDBusinessRules.createOTPaymentList(otPaymentList, dtiPayList, entityTO);

    // SellerId
    Long dtiSalesId = entityTO.getDefSalesRepId();
    if (dtiSalesId != null) {
      otManageRes.setSellerId(new Long(dtiSalesId.longValue()));
    }

    // Default is true;
    ReservationTO dtiRes = dtiResReq.getReservation();
    HkdOTReservationDataTO otRes = new HkdOTReservationDataTO();

    if ((PRESALE.compareTo(dtiRes.getResSalesType()) == 0)
        || (MANUALMAILORDER.compareTo(dtiRes.getResSalesType()) == 0)) {
      otRes.setPrinted(new Boolean(false));
      otRes.setValidated(new Boolean(false));
    } else {

      if (otPaymentList.size() == 0) { // no payment cannot be printed or
        // validated
        otRes.setPrinted(new Boolean(false));
        otRes.setValidated(new Boolean(false));
      } else {
        otRes.setPrinted(new Boolean(false));
        otRes.setValidated(new Boolean(false));
      }

    }

    otManageRes.setReservationData(otRes);

    // TP Lookups
    HashMap<TPLookupTO.TPLookupType, TPLookupTO> tpLookupMap = dtiTxn.getTpLookupTOMap();
    try {
      TPLookupTO aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.SALES_TYPE);
      if (aTPLookupTO == null) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "TPLookup for SalesType is missing in the database.");
      }
      otRes.setSalesType(Integer.parseInt(aTPLookupTO.getLookupValue()));

      aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.PICKUP_AREA);
      if (aTPLookupTO == null) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "TPLookup for ResPickupArea is missing in the database.");
      }

      otRes.setResPickupArea(Integer.parseInt(aTPLookupTO.getLookupValue()));

      aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.PICKUP_TYPE);
      if (aTPLookupTO == null) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "TPLookup for ResPickupType is missing in the database.");
      }
      otRes.setResPickupType(Integer.parseInt(aTPLookupTO.getLookupValue()));

    } catch (NumberFormatException nfe) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup value SalesType, PickupArea, or PickupType was not a valid integer in the database.");
    }
    otRes.setResPickupDate(dtiRes.getResPickupDate());

    // AssociationInfo
    if (dtiResReq.getEligibilityGroup() != null) {
      HkdOTAssociationInfoTO otAssocInfo = setAssociationInfo(dtiResReq);
      otManageRes.setAssociationInfo(otAssocInfo);
    }

    // IATA Number
    if (dtiResReq.getAgency() != null) {
      AgencyTO dtiAgencyTO = dtiResReq.getAgency();
      if (dtiAgencyTO.getIATA() != null) {
        otManageRes.setIATA(dtiAgencyTO.getIATA());
      }
    }

    // TaxExemptCode
    if (dtiResReq.getTaxExemptCode() != null) {
      otManageRes.setTaxExemptCode(dtiResReq.getTaxExemptCode());
    }

    // ClientData
    ClientDataTO dtiClientData = dtiResReq.getClientData();
    HkdOTClientDataTO otClientData = new HkdOTClientDataTO();
    if (dtiClientData.getClientId() == null) {
      otClientData.setClientUniqueId(NO_CLIENT_PROVIDED);
    } else {
      Integer clientNumber = null;
      try { // NOTE: Cannot use Integer.decode() here, as some values are
        // prefixed with zeros and
        // will be interpreted by that routine as "octal" values.
        int clientNum = Integer.parseInt(dtiClientData.getClientId());
        clientNumber = new Integer(clientNum);
      } catch (NumberFormatException nfe) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "ClientId is non-numeric value: " + dtiClientData.getClientId());
      }
      otClientData.setClientUniqueId(clientNumber);
    }
    TPLookupTO aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.CLIENT_TYPE);

    if (aTPLookupTO == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup for ClientType is missing in the database.");
    }
    otClientData.setClientType(aTPLookupTO.getLookupValue());
    otClientData.setClientCategory(dtiClientData.getClientCategory());

    aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.LANGUAGE);
    try {
      if (aTPLookupTO == null) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "TPLookup for Language is missing in the database.");
      }
      int intValue = Integer.parseInt(aTPLookupTO.getLookupValue());
      otClientData.setClientLanguage(new Integer(intValue));
    } catch (NumberFormatException nfe) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup value ClientLanguage was not a valid integer in the database: " + aTPLookupTO.getLookupValue());
    }

    // ClientData Demographics Fields
    ArrayList<HkdOTFieldTO> otFieldList = otClientData.getDemographicData();
    DemographicsTO dtiBillInfo = dtiClientData.getBillingInfo();
    DemographicsTO dtiShipInfo = dtiClientData.getShippingInfo();
    AgencyTO agencyTO = dtiResReq.getAgency();
    createOtDemographics(otFieldList, dtiBillInfo, dtiShipInfo, agencyTO);
    otManageRes.setClientData(otClientData);

    // ReservationRequest.Note
    ArrayList<String> noteDetailsArray = setClientDeliveryNoteDetails(dtiTxn);
    if (noteDetailsArray != null && noteDetailsArray.size() > 0) {
      // have Delivery notes, use it
      otManageRes.setNoteDetailsArray(noteDetailsArray);
    } else {
      // No Delivery Specific information
      if (dtiResReq.getNoteList().size() > 0) {
        // If note, process
        ArrayList<String> dtiNoteList = dtiResReq.getNoteList();

        boolean firstNote = true;
        StringBuffer noteBuffer = new StringBuffer();
        for /* each */(String resString : /* in */dtiNoteList) {
          if (!firstNote)
            noteBuffer.append(ATSCR);
          firstNote = false;
          noteBuffer.append(resString);
        }
        otManageRes.setReservationNote(noteBuffer.toString());
      }
    }

    // Set Transaction note
    otManageRes.setTransactionNote(dtiRequest.getPayloadHeader().getPayloadID());

    // Set ShowData (optional) (as of 2.16.3, JTL)
    if (dtiResReq.getShow() != null) {
      HkdOTShowDataTO otShowTO = new HkdOTShowDataTO();
      ShowTO showTO = dtiResReq.getShow();

      if (showTO.getShowGroup() != null) {
        otShowTO.setGroupCode(showTO.getShowGroup());
      }

      if (showTO.getShowPerformance() != null) {
        otShowTO.setPerformanceId(showTO.getShowPerformance());
      }

      if (showTO.getShowQuota() != null) {
        otShowTO.setQuota(showTO.getShowQuota());
      }

      otManageRes.setShowData(otShowTO);

    }

    // Set ExternalTransactionID (optional)
    if (dtiResReq.getExtTxnIdentifier() != null) {

      ExtTxnIdentifierTO dtiExtId = dtiResReq.getExtTxnIdentifier();

      HkdOTExternalTransactionIDTO otExtTxnId = new HkdOTExternalTransactionIDTO();
      otExtTxnId.setId(dtiExtId.getTxnIdentifier());
      otExtTxnId.setAlreadyEncrypted(dtiExtId.getIsSHA1Encrypted());

      otManageRes.setExternalTransactionID(otExtTxnId);
    }

    // Generate Event - (not applicable for HKDL)

    // Set the manage reservation TO on the command
    atsCommand.setManageReservationTO(otManageRes);

    // Get the XML String
    xmlString = HkdOTCommandXML.getXML(atsCommand);

    return xmlString;

  }

  /**
   * @param otManageRes
   * @param dtiTktList
   * @param dtiPdtMap
   * @return
   */
  private static boolean createOTProducts(HkdOTManageReservationTO otManageRes, ArrayList<TicketTO> dtiTktList,
      HashMap<String, DBProductTO> dtiPdtMap) {

    boolean productsAssignAccounts = false; // Always false for HKDL

    for /* each */(TicketTO aDtiTicket : /* in */dtiTktList) {

      HkdOTProductTO otProduct = new HkdOTProductTO();

      // Item
      otProduct.setItem(aDtiTicket.getTktItem());

      // ItemType
      otProduct.setItemType(ITEM_TYPE);

      // ItemNumCode
      DBProductTO dtiPdt = dtiPdtMap.get(aDtiTicket.getProdCode());
      logger.sendEvent(
          "transformRequest: got product " + dtiPdt.getPdtCode() + " is consumable?" + dtiPdt.isConsumable(),
          EventType.DEBUG, THISOBJECT);
      BigInteger tktNbr = dtiPdt.getMappedProviderTktNbr();
      otProduct.setItemNumCode(tktNbr);

      // Quantity
      otProduct.setQuantity(aDtiTicket.getProdQty());

      // ProdDemoData
      ArrayList<DemographicsTO> tktDemoList = aDtiTicket.getTicketDemoList();
      if (tktDemoList.size() > 0) {
        HkdOTDemographicInfo otDemoInfo = new HkdOTDemographicInfo();
        HKDBusinessRules.transformTicketDemoData(tktDemoList, otDemoInfo);
        otProduct.setDemographicInfo(otDemoInfo);
      }

      // StartDate
      boolean startValidityDateSet = false;
      boolean endValidityDateSet = false;
      if (dtiPdt.isValidityDateInfoRequired() && aDtiTicket.getTktValidityValidStart() != null) {
        otProduct.setValidity_StartDate(aDtiTicket.getTktValidityValidStart());
        startValidityDateSet = true;
      }

      // EndDate
      if (dtiPdt.isValidityDateInfoRequired() && aDtiTicket.getTktValidityValidEnd() != null) {
        otProduct.setValidity_EndDate(aDtiTicket.getTktValidityValidEnd());
        endValidityDateSet = true;
      }

      // Validity Start Date Validation/Auto-correction
      // Validity Start Date should be provided in all instances when end is,
      // but there is no
      // XSD rule mandating the relationship. The choices are to error or
      // default, and ticketing
      // is asking for a default to today's date for the start date if the end
      // has been specified.
      if (endValidityDateSet && !startValidityDateSet) {
        GregorianCalendar gc = new GregorianCalendar();
        otProduct.setValidity_StartDate(gc);
      }

      // Price
      otProduct.setPrice(aDtiTicket.getProdPrice());

      // Group Code (Optional, as of 2.17.1, JTL)
      if (aDtiTicket.getShowGroup() != null) {
        otProduct.setGroupCode(aDtiTicket.getShowGroup());
      }

      // Put product in the list.
      otManageRes.getProductList().add(otProduct);

    } // Product Loop
    return productsAssignAccounts;
  }

  /**
   * Sets the association info on the request, based on input from the DTI
   * reservation request.
   * 
   * @param dtiResReq
   *          the DTI Reservation Request Transfer Object
   * @return the Omni Ticket Association Info Transfer Object
   * @throws DTIException
   *           if unable to get the EligibilityAssocId
   */
  private static HkdOTAssociationInfoTO setAssociationInfo(ReservationRequestTO dtiResReq) throws DTIException {
    HkdOTAssociationInfoTO otAssocInfo = new HkdOTAssociationInfoTO();

    String eligGroup = dtiResReq.getEligibilityGroup();
    String eligMember = dtiResReq.getEligibilityMember();

    Integer associationId = EligibilityKey.getEligibilityAssocId(eligGroup);
    otAssocInfo.setAssociationId(associationId);

    boolean isMemberNumeric = true;
    Integer memberInteger = null;
    try {
      memberInteger = Integer.decode(eligMember);
    } catch (NumberFormatException nfe) {
      isMemberNumeric = false;
    }

    if (eligGroup.compareTo(HKDBusinessRules.DVC_STRING) == 0) {
      otAssocInfo.setMemberField(eligMember);
    } else if (isMemberNumeric == false) {
      otAssocInfo.setMemberId(DEFAULT_MEMBERID);
      otAssocInfo.setAllowMemberCreation(true);

      // Note: This is hard-coded to field number one because field number
      // one is the ONLY field ATS has enabled to search for existing
      // associations and add them dynamically.
      HkdOTFieldTO otField = new HkdOTFieldTO(FIELD_NUMBER_ONE, eligMember);
      otAssocInfo.getDemographicData().add(otField);

    } else {
      otAssocInfo.setMemberId(memberInteger);
    }
    return otAssocInfo;
  }

  /**
   * Create the OT Demographics in the OT field list.
   * 
   * @param otFieldList
   *          The field list of demographic elements and values.
   * @param dtiBillInfo
   *          The DTI billing info DemographicsTO.
   * @param dtiShipInfo
   *          The DTI shipping info DemographicsTO.
   * @param agencyTO
   *          The DTI travel agency information.
   */
  private static void createOtDemographics(ArrayList<HkdOTFieldTO> otFieldList, DemographicsTO dtiBillInfo,
      DemographicsTO dtiShipInfo, AgencyTO agencyTO) {

    // Billing Info
    if (dtiBillInfo != null) {

      // Bill name
      if (dtiBillInfo.getName() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_NAME, DTIFormatter.websafe(dtiBillInfo.getName())));
      }

      // Bill Last Name
      if (dtiBillInfo.getLastName() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_LASTNAME, DTIFormatter.websafe(dtiBillInfo
            .getLastName())));
      }

      // Bill First Name (Chinese)
      if (dtiBillInfo.getFirstNameChinese() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_FIRSTNAME_CH, DTIFormatter.websafe(dtiBillInfo
            .getFirstNameChinese())));
      }

      // Bill First Name (English)
      if (dtiBillInfo.getFirstName() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_FIRSTNAME_ENG, DTIFormatter.websafe(dtiBillInfo
            .getFirstName())));
      }

      // Bill Address 1
      // if (dtiBillInfo.getAddr1() != null) otFieldList.add(new HkdOTFieldTO(
      // HkdOTFieldTO.HKD_CLNT_BILL_ADDR1, DTIFormatter.websafe(dtiBillInfo
      // .getAddr1())));

      // Bill Address 2
      // if (dtiBillInfo.getAddr2() != null) otFieldList.add(new HkdOTFieldTO(
      // HkdOTFieldTO.HKD_CLNT_BILL_ADDR2, DTIFormatter.websafe(dtiBillInfo
      // .getAddr2())));

      // Bill City
      // if (dtiBillInfo.getCity() != null) otFieldList.add(new HkdOTFieldTO(
      // HkdOTFieldTO.HKD_CLNT_BILL_CITY, DTIFormatter.websafe(dtiBillInfo
      // .getCity())));

      // Bill State
      if (dtiBillInfo.getState() != null) {
        otFieldList
            .add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_STATE, DTIFormatter.websafe(dtiBillInfo.getState())));
      }

      // Bill ZIP
      // if (dtiBillInfo.getZip() != null) otFieldList.add(new HkdOTFieldTO(
      // HkdOTFieldTO.HKD_CLNT_BILL_ZIP, DTIFormatter.websafe(dtiBillInfo
      // .getZip())));

      // Bill Country
      if (dtiBillInfo.getCountry() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_COUNTRY, DTIFormatter.websafe(dtiBillInfo
            .getCountry())));
      }

      // Bill Telephone
      if (dtiBillInfo.getTelephone() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_TELEPHONE, DTIFormatter.websafe(dtiBillInfo
            .getTelephone())));
      }

      // Bill E-mail
      if (dtiBillInfo.getEmail() != null) {
        otFieldList
            .add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_BILL_EMAIL, DTIFormatter.websafe(dtiBillInfo.getEmail())));
      }

      // Bill SellerResNbr (also known as HKDL's TRANSACTION NO.)
      if (dtiBillInfo.getSellerResNbr() != null) {
        otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_TRANSACTION_NO, DTIFormatter.websafe(dtiBillInfo
            .getSellerResNbr())));
      }

    }

    // Shipping Info
    if (dtiShipInfo != null) {

      // Ship Name
      if (dtiShipInfo.getName() != null) {
         otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_NAME,
             DTIFormatter.websafe(dtiShipInfo.getName())));
      }

      // Ship Note
      if (dtiShipInfo.getNote() != null) {
         otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_NOTE,
             DTIFormatter.websafe(dtiShipInfo.getNote())));
      }
     
      // Ship Last Name
      // if (dtiShipInfo.getLastName() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_LASTNAME,
      // DTIFormatter.websafe(dtiShipInfo.getLastName())));
      // }

      // Ship First Name (English)
      // if (dtiShipInfo.getFirstName() != null) otFieldList
      // .add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_FIRSTNAME_ENG,
      // DTIFormatter.websafe(dtiShipInfo.getFirstName())));

      // Ship Address 1
      // if (dtiShipInfo.getAddr1() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_ADDR1,
      // DTIFormatter.websafe(dtiShipInfo.getAddr1())));
      // }

      // Ship Address 2
      // if (dtiShipInfo.getAddr2() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_ADDR2,
      // DTIFormatter.websafe(dtiShipInfo.getAddr2())));
      // }

      // Ship City
      // if (dtiShipInfo.getCity() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_CITY,
      // DTIFormatter.websafe(dtiShipInfo.getCity())));
      // }

      // Ship State
      // if (dtiShipInfo.getState() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_STATE,
      // DTIFormatter.websafe(dtiShipInfo.getState())));
      // }

      // Ship ZIP (doesn't exist for HKD)

      // Ship Country
      // if (dtiShipInfo.getCountry() != null) {
      // otFieldList.add(new HkdOTFieldTO(HkdOTFieldTO.HKD_CLNT_SHIP_COUNTRY,
      // DTIFormatter.websafe(dtiShipInfo.getCountry())));
      // }

      // Ship Telephone
      // if (dtiShipInfo.getTelephone() != null) {
      // otFieldList.add(new HkdOTFieldTO(+HkdOTFieldTO.HKD_CLNT_SHIP_TELEPHONE,
      // DTIFormatter.websafe(dtiShipInfo.getTelephone())));
      // }

      // Ship Agent (doesn't exist for HKD)

    }

    return;
  }

  /**
   * Format a note with the appropriate information needed for fulfillment when
   * handling private and major clients. If no special delivery fields supplied,
   * then return an empty ArrayList
   * 
   * @param dtiTxn
   *          the DTITransactionTO for this transaction.
   * @return formatted note string
   */
  private static ArrayList<String> setClientDeliveryNoteDetails(DTITransactionTO dtiTxn) {

    String noteString = TransformRules.setFulfillmentNoteDetails(dtiTxn);

    ArrayList<String> noteDetailList = new ArrayList<String>();

    if (noteString != null) {

      // Carve the note up into "Note Details".
      StringBuffer sb = new StringBuffer(noteString);
      double lineCount = (float) sb.length() / (float) MAX_NOTE_DETAIL_LINE_LENGTH;
      int numberOfLines = (int) Math.ceil(lineCount);

      if (numberOfLines > MAX_NUMBER_OF_NOTE_DETAILS) {
        numberOfLines = MAX_NUMBER_OF_NOTE_DETAILS;
      }

      for (int i = 0; i < numberOfLines; i++) {

        int startIndex = i * MAX_NOTE_DETAIL_LINE_LENGTH;
        int endIndex = (i + 1) * MAX_NOTE_DETAIL_LINE_LENGTH;

        if (endIndex <= sb.length()) {
          String subString = sb.substring(startIndex, endIndex);
          noteDetailList.add(subString);
        } else {
          String subString = sb.substring(startIndex);
          noteDetailList.add(subString);
        }

      }

    }

    return noteDetailList;
  }

  /**
   * Validate that a reservation doesn't have more than 20 line items
   * 
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateMaxResLineItems(ReservationRequestTO dtiResReq) throws DTIException {

    ArrayList<TicketTO> tktProdList = dtiResReq.getTktList();
    if (tktProdList.size() > MAX_RES_LINE_ITEMS) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation exceeds the maximum number of line items (" + MAX_RES_LINE_ITEMS + "). " + tktProdList.size()
              + " items were attempted.");
    }
  }

  /**
   * 
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateHKDClientId(ReservationRequestTO dtiResReq) throws DTIException {

    int clientId = -1;
    String clientIdString = null;

    // Validate that ClientId is numeric
    if (dtiResReq.getClientData() != null) {
      ClientDataTO clientTO = dtiResReq.getClientData();
      if (clientTO != null) {

        clientIdString = clientTO.getClientId();
        if (clientIdString != null && clientIdString.length() > 0) {

          try {
            clientId = Integer.parseInt(clientIdString);
          } catch (NumberFormatException nfe) {
            throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
                "ClientID must be numeric (is " + clientId + ").");
          }
        }
      }
    } else {

      // 2013.06.25 - MWH - ClientData should exists before we determine
      // ClientType (see below), otherwise throws NPE
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT, "ClientData must exists.");

    }

    // Validate that seller isn't attempting to create a new major client
    String clientType = dtiResReq.getClientData().getClientType();

    if (clientType.compareToIgnoreCase(MAJOR_CLIENT_TYPE) == 0) {

      if (clientIdString == null) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not omit a clientID.");
      }

      if (clientId == 0) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not specify a clientID of zero.");
      }
    }
    if (clientType.compareToIgnoreCase(PRIVATE_CLIENT_TYPE) == 0) {
      if (clientIdString != null && clientIdString.length() > 0 && clientId != 0) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Private may not specify a clientID.");
      }
    }

    return;
  }

  /**
   * Ensure TP_Lookups have been populated in the database
   * 
   * @param dtiTxn
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateAndSaveTPLookups(DTITransactionTO dtiTxn, ReservationRequestTO dtiResReq)
      throws DTIException {

    String tpiCode = dtiTxn.getTpiCode();
    TransactionType txnType = dtiTxn.getTransactionType();
    ReservationTO dtiResTO = dtiResReq.getReservation();
    ClientDataTO dtiCliDataTO = dtiResReq.getClientData();
    String language = dtiCliDataTO.getDemoLanguage();
    String clientType = dtiCliDataTO.getClientType();

    if (language == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
          "ClientData DemoLanguage cannot be null.");
    }
    if (clientType == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
          "ClientData ClientType cannot be null.");
    }

    // 2013.06.25 - MWH - Added check if Reservation tag is null or not
    if (dtiResTO == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT, "Reservation cannot be null.");
    }

    String resPickupArea = dtiResTO.getResPickupArea();
    String resSalesType = dtiResTO.getResSalesType();

    if (resPickupArea == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation ResPickupArea cannot be null.");
    }
    if (resSalesType == null) {
      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation ResSalesType cannot be null.");
    }

    ArrayList<TPLookupTO> tpLookups = LookupKey.getTPCommandLookup(tpiCode, txnType, language, clientType,
        resPickupArea, resSalesType);

    if (tpLookups.size() < 7) { // Should see seven items back.

      String missingValue = "all";

      HashSet<TPLookupTO.TPLookupType> hashSet = new HashSet<TPLookupTO.TPLookupType>();
      for /* each */(TPLookupTO aLookup : /* in */tpLookups) {
        hashSet.add(aLookup.getLookupType());
      }

      if (!hashSet.contains(TPLookupTO.TPLookupType.CLIENT_TYPE)) {
        missingValue = "ClientType - not mapped for " + clientType;
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.PICKUP_TYPE)) {
        missingValue = "PickupType";
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.SALES_TYPE)) {
        missingValue = "SalesType - not mapped for " + resSalesType;
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.LANGUAGE)) {
        missingValue = "Language - not mapped for " + language;
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.MAX_LIMIT)) {
        missingValue = "MaxLimit";
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.PICKUP_AREA)) {
        missingValue = "PickupArea - not mapped for " + resPickupArea;
      } else if (!hashSet.contains(TPLookupTO.TPLookupType.INSTALLMENT)) {
        missingValue = "Installment";
      }

      throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_AREA,
          "TPCommandLookup did not find all values (missing translation for " + missingValue + ")");
    }
    dtiTxn.setTpLookupTOList(tpLookups);
  }

  /**
   * Validate that major clients do not send demographics
   * 
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateMajorClientDemo(ReservationRequestTO dtiResReq) throws DTIException {

    String clientType = dtiResReq.getClientData().getClientType();

    if (clientType.compareToIgnoreCase(MAJOR_CLIENT_TYPE) == 0) {

      ClientDataTO clientDataTO = dtiResReq.getClientData();
      if ((clientDataTO.getBillingInfo() != null) || (clientDataTO.getShippingInfo() != null)) {
        throw new DTIException(HKDReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not specify billing or shipping demographics.");
      }

    }
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
  static void transformResponseBody(DTITransactionTO dtiTxn, HkdOTCommandTO otCmdTO, DTIResponseTO dtiRespTO)
      throws DTIException {

    ReservationResponseTO dtiResRespTO = new ReservationResponseTO();
    HkdOTManageReservationTO otMngResTO = otCmdTO.getManageReservationTO();
    dtiRespTO.setCommandBody(dtiResRespTO);

    // Price mismatch warning (Commented out as of 2.16.3, JTL)
    // HKDL plans to make extensive use of price mismatch, and the
    // warnings, likewise, would be excessive.
    // if (dtiTxn.isPriceMismatch()) {
    // DTIErrorTO mismatchWarn = ErrorKey
    // .getErrorDetail(DTIErrorCode.PRICE_MISMATCH_WARNING);
    // dtiRespTO.setDtiError(mismatchWarn);
    // }

    // ResponseType
    dtiResRespTO.setResponseType(otMngResTO.getCommandType());

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
    ArrayList<HkdOTTicketInfoTO> otTicketList = otMngResTO.getTicketInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for /* each */(HkdOTTicketInfoTO otTicketInfo : /* in */otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        HkdOTTicketTO otTicketTO = otTicketInfo.getTicket();

        dtiTicketTO.setTktItem(otTicketInfo.getItem());

        GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
        String site = otTicketTO.getTdssnSite();
        String station = otTicketTO.getTdssnStation();
        String number = otTicketTO.getTdssnTicketId();
        dtiTicketTO.setDssn(dssnDate, site, station, number);

        dtiTicketTO.setTktNID(otTicketTO.getTCOD());
        dtiTicketTO.setBarCode(otTicketTO.getBarCode());
        dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
        dtiTicketTO.setTktTax(otTicketInfo.getTax());

        if (otTicketInfo.getValidityStartDate() != null) {
          dtiTicketTO.setTktValidityValidStart(otTicketInfo.getValidityStartDate());
        }

        if (otTicketInfo.getValidityEndDate() != null) {
          dtiTicketTO.setTktValidityValidEnd(otTicketInfo.getValidityEndDate());
        }

        dtiTktList.add(dtiTicketTO);
      }
    }

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. Not supported in the XSD (RE: JTL
    // 09/15/2008)
    ArrayList<PaymentTO> dtiPmtList = dtiResRespTO.getPaymentList();
    ArrayList<HkdOTPaymentTO> otPmtList = otMngResTO.getPaymentInfoList();
    HKDBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

    // Receipt
    Long entityId = new Long(dtiTxn.getEntityTO().getEntityId());
    String receiptMessage = EntityKey.getEntityReceipt(entityId);
    if (receiptMessage != null) {
      dtiResRespTO.setReceiptMessage(receiptMessage);
    }

    // Reservation
    String resCode = otMngResTO.getReservationCode();
    if (resCode != null) {
      ReservationTO dtiReservationTO = new ReservationTO();
      dtiReservationTO.setResCode(resCode);

      // Contract ID (lifted from payment section)
      for /* each */(PaymentTO aPaymentTO : /* in */dtiPmtList) {
        if (aPaymentTO.getInstallment() != null) {
          if (aPaymentTO.getInstallment().getContractId() != null) {
            String contractId = aPaymentTO.getInstallment().getContractId();
            dtiReservationTO.setContractId(contractId);
            break; // Leave this loop when the condition is 1st satisfied.
          }
        }
      }

      dtiResRespTO.setReservation(dtiReservationTO);
    }

    // ClientData
    ClientDataTO dtiClientDataTO = new ClientDataTO();
    HkdOTClientDataTO otClientDataTO = otMngResTO.getClientData();
    if (otClientDataTO != null) {
      dtiClientDataTO.setClientId(otClientDataTO.getClientUniqueId().toString());
      dtiResRespTO.setClientData(dtiClientDataTO);
    }

    return;
  }

}
