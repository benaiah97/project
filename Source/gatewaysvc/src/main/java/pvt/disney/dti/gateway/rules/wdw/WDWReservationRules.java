package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.dao.TransidRescodeKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TransidRescodeTO;
import pvt.disney.dti.gateway.data.common.AttributeTO.CmdAttrCodeType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAccountDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTExternalTransactionIDTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTReservationDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.ElectronicEntitlementRules;
import pvt.disney.dti.gateway.rules.PaymentRules;
import pvt.disney.dti.gateway.rules.ProductRules;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.rules.race.utility.WDWAlgorithmUtility;
import pvt.disney.dti.gateway.service.dtixml.ReservationXML;
import pvt.disney.dti.gateway.util.DTIFormatter;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/**
 * This class is responsible for three major functions for WDW reservations:<BR>
 * 1. Defining the business rules specific to WDW reservations.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class WDWReservationRules {

  private static final Class<WDWReservationRules> THISOBJECT = WDWReservationRules.class;

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
   * Constant representing the maximum number of line items allowed on a reservation.
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

  /** Entitlement Account type */
  private final static String SINGLE_ACCOUNT = "SINGLEACCOUNT";

  /** Entitlement Account type */
  private final static String PER_TICKET_ACCOUNT = "PERTICKETACCOUNT";

  /** Entitlement Account type */
  private final static String ENTITLEMENT_ACCOUNT_ID = "ENTITLEMENTACCOUNTID";

  private static final EventLogger logger = EventLogger
      .getLogger(WDWReservationRules.class.getCanonicalName());

  private static int atsMaxEncodeAllCnt = 200;

  /**
   * Pull in any rule values from the properties file.
   * 
   * @param props
   */
  public static void initWDWReservationRules(Properties props) {

    String encodeCountString = null;
    encodeCountString = PropertyHelper.readPropsValue(
        PropertyName.ATS_MAX_ENCODE_ALL_COUNT, props, null);

    if (encodeCountString != null) {

      try {
        atsMaxEncodeAllCnt = Integer.parseInt(encodeCountString);
      }
      catch (NumberFormatException nfe) {
        logger.sendEvent(
            "Unable to convert ATS.MaxEncodeAllCount property to a number.  Taking default.",
            EventType.WARN, THISOBJECT);
      }
    }

    return;
  }

  /**
   * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshaling routines to create an XML string.
   * 
   * @param dtiTxn
   *            the DTI Transaction object.
   * @return the XML string version of the provider request.
   * @throws DTIException
   *             when any transformation error is encountered.
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlString = null;
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.MANAGERESERVATION);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_MANAGE, REQUEST_SUBTYPE_MANAGERES);
    atsCommand.setHeaderTO(hdr);

    // === Manage Reservation Level ===
    OTManageReservationTO otManageRes = new OTManageReservationTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    tagList.add(ALL_TAGS);
    otManageRes.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();
    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otManageRes.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    }
    else {
      otManageRes.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }

    // CommandType
    otManageRes.setCommandType(COMMAND_TYPE);

    // ReservationCode
    if (dtiResReq.getReservation() != null) {
      ReservationTO resTO = dtiResReq.getReservation();
      if (resTO.getResCode() != null) otManageRes
          .setReservationCode(resTO.getResCode());
    }

    // Product
    ArrayList<TicketTO> dtiTktList = dtiResReq.getTktList();
    HashMap<String, DBProductTO> dtiPdtMap = dtiTxn.getDbProdMap();
    boolean productsAssignAccounts = false;
    productsAssignAccounts = createOTProducts(otManageRes, dtiTktList,
        dtiPdtMap);

    // Entitlement Account
    boolean electronicEnabled = false;
    if (ReservationXML.ACCOUNT_PER_ORDER.equals(dtiResReq
        .getDefaultAccount())) {

      otManageRes.setEntitlementAccountCreationTypology(SINGLE_ACCOUNT);
      electronicEnabled = true;

    }
    else if (ReservationXML.ACCOUNT_PER_TICKET.equals(dtiResReq
        .getDefaultAccount())) {

      otManageRes
          .setEntitlementAccountCreationTypology(PER_TICKET_ACCOUNT);
      electronicEnabled = true;

    }
    else {

      if (dtiResReq.getSpecifiedAccounts() != null && dtiResReq
          .getSpecifiedAccounts().size() > 0) {

        // ENTITLEMENTACCOUNTID always requires a tag to be present in
        // the product node Per Andrea Martinotti 10/2/2012
        if (productsAssignAccounts) {

          otManageRes
              .setEntitlementAccountCreationTypology(ENTITLEMENT_ACCOUNT_ID);

        }
        else {

          otManageRes
              .setEntitlementAccountCreationTypology(SINGLE_ACCOUNT);

        }

        transformSpecifiedAccount(dtiResReq,
            otManageRes.getAccountsData());
        electronicEnabled = true;

      }
    }

    // Payment Processing
    // There are three alternatives: default payment, no payment, and
    // payment.
    ArrayList<OTPaymentTO> otPaymentList = otManageRes.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiResReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();
    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
        entityTO);

    // SellerId
    Long dtiSalesId = entityTO.getDefSalesRepId();
    if (dtiSalesId != null) {
      otManageRes.setSellerId(new Long(dtiSalesId.longValue()));
    }

    // Default is true;
    ReservationTO dtiRes = dtiResReq.getReservation();
    OTReservationDataTO otRes = new OTReservationDataTO();

    if ((PRESALE.compareTo(dtiRes.getResSalesType()) == 0) || (MANUALMAILORDER
        .compareTo(dtiRes.getResSalesType()) == 0)) {
      otRes.setPrinted(new Boolean(false));
      otRes.setValidated(new Boolean(false));
    }
    else {

      if (otPaymentList.size() == 0) { // no payment cannot be printed or
        // validated
        otRes.setPrinted(new Boolean(false));
        otRes.setValidated(new Boolean(false));
      }
      else {
        otRes.setPrinted(new Boolean(true));
        otRes.setValidated(new Boolean(true));
      }

    }

    // ******************************************************************
    // Encode Everything Override (as of 2.11)
    // For 2.11, DTI Gateway is to encode and electronically entitle all
    // orders that are paid and are under a set quantity of tickets.
    boolean executeOverride = true;

    // Add up all of the tickets, either actual or those listed as quantity.
    ArrayList<TicketTO> tktList = dtiResReq.getTktList();
    int numberOfTickets = 0;
    for /* each */(TicketTO aTicketTO : /* in */tktList) {
      numberOfTickets += aTicketTO.getProdQty().intValue();
    }

    // Is the rule active (atsMaxEncodeAllCnt > 0)
    if (atsMaxEncodeAllCnt <= 0) {
      executeOverride = false;
      logger.sendEvent(
          "Encode everything overriden because rule is turned off.",
          EventType.DEBUG, THISOBJECT);
    }

    // Is the rule active for this seller?
    anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.DEFAULT_ELECT_ENC);
    if (anAttributeTO != null) {
      if (anAttributeTO.getAttrValue().compareToIgnoreCase("T") == 0) {
        executeOverride = false;
        logger.sendEvent(
            "Encode everything overriden because seller override is set.",
            EventType.DEBUG, THISOBJECT);
      }
    }

    // Is the order paid?
    if (otPaymentList.size() == 0) {
      executeOverride = false;
      logger.sendEvent(
          "Encode everything overriden because order is not paid.",
          EventType.DEBUG, THISOBJECT);
    }

    // Does the rule exceed atsMaxEncodeAllCnt?
    if (numberOfTickets > atsMaxEncodeAllCnt) {
      executeOverride = false;
      logger.sendEvent(
          "Encode everything overriden because number of tickets (" + numberOfTickets + ") is greater than " + atsMaxEncodeAllCnt + ".",
          EventType.DEBUG, THISOBJECT);
    }
    else {
      logger.sendEvent(
          "Number of Tickets (" + numberOfTickets + ") is less than or equal to " + "ATSMaxEncodeAllCnt (" + atsMaxEncodeAllCnt + ")",
          EventType.DEBUG, THISOBJECT);
    }

    if (executeOverride) {
      otRes.setPrinted(new Boolean(true));
      otRes.setValidated(new Boolean(true));

      // Has an electronic account already been specified?
      if (!electronicEnabled) {
        otManageRes
            .setEntitlementAccountCreationTypology(PER_TICKET_ACCOUNT);
      }

    }

    // End of Encode Everything Override
    // ******************************************************************

    otManageRes.setReservationData(otRes);

    // TP Lookups
    HashMap<TPLookupTO.TPLookupType, TPLookupTO> tpLookupMap = dtiTxn
        .getTpLookupTOMap();
    try {
      TPLookupTO aTPLookupTO = tpLookupMap
          .get(TPLookupTO.TPLookupType.SALES_TYPE);
      if (aTPLookupTO == null) throw new DTIException(
          WDWReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup for SalesType is missing in the database.");
      otRes.setSalesType(Integer.parseInt(aTPLookupTO.getLookupValue()));

      aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.PICKUP_AREA);
      if (aTPLookupTO == null) throw new DTIException(
          WDWReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup for ResPickupArea is missing in the database.");
      otRes.setResPickupArea(Integer.parseInt(aTPLookupTO
          .getLookupValue()));

      aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.PICKUP_TYPE);
      if (aTPLookupTO == null) throw new DTIException(
          WDWReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup for ResPickupType is missing in the database.");
      otRes.setResPickupType(Integer.parseInt(aTPLookupTO
          .getLookupValue()));

    }
    catch (NumberFormatException nfe) {
      throw new DTIException(
          WDWReservationRules.class,
          DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup value SalesType, PickupArea, or PickupType was not a valid integer in the database.");
    }
    otRes.setResPickupDate(dtiRes.getResPickupDate());

    // AssociationInfo
    if (dtiResReq.getEligibilityGroup() != null) {
      OTAssociationInfoTO otAssocInfo = setAssociationInfo(dtiResReq);
      otManageRes.setAssociationInfo(otAssocInfo);
    }

    // IATA Number
    if (dtiResReq.getAgency() != null) {
      AgencyTO dtiAgencyTO = dtiResReq.getAgency();
      if (dtiAgencyTO.getIATA() != null) otManageRes.setIATA(dtiAgencyTO
          .getIATA());
    }

    // TaxExemptCode
    if (dtiResReq.getTaxExemptCode() != null) {
      otManageRes.setTaxExemptCode(dtiResReq.getTaxExemptCode());
    }

    // ClientData
    ClientDataTO dtiClientData = dtiResReq.getClientData();
    OTClientDataTO otClientData = new OTClientDataTO();
    if (dtiClientData.getClientId() == null) otClientData
        .setClientUniqueId(NO_CLIENT_PROVIDED);
    else {
      Integer clientNumber = null;
      try { // NOTE: Cannot use Integer.decode() here, as some values are
        // prefixed with zeros and
        // will be interpreted by that routine as "octal" values.
        int clientNum = Integer.parseInt(dtiClientData.getClientId());
        clientNumber = new Integer(clientNum);
      }
      catch (NumberFormatException nfe) {
        throw new DTIException(WDWReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "ClientId is non-numeric value: " + dtiClientData
                .getClientId());
      }
      otClientData.setClientUniqueId(clientNumber);
    }
    TPLookupTO aTPLookupTO = tpLookupMap
        .get(TPLookupTO.TPLookupType.CLIENT_TYPE);

    if (aTPLookupTO == null) throw new DTIException(
        WDWReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
        "TPLookup for ClientType is missing in the database.");
    otClientData.setClientType(aTPLookupTO.getLookupValue());
    otClientData.setClientCategory(dtiClientData.getClientCategory() + " ");

    aTPLookupTO = tpLookupMap.get(TPLookupTO.TPLookupType.LANGUAGE);
    try {
      if (aTPLookupTO == null) throw new DTIException(
          WDWReservationRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup for Language is missing in the database.");
      int intValue = Integer.parseInt(aTPLookupTO.getLookupValue());
      otClientData.setClientLanguage(new Integer(intValue));
    }
    catch (NumberFormatException nfe) {
      throw new DTIException(
          WDWReservationRules.class,
          DTIErrorCode.DTI_DATA_ERROR,
          "TPLookup value ClientLanguage was not a valid integer in the database: " + aTPLookupTO
              .getLookupValue());
    }

    // ClientData Demographics Fields
    ArrayList<OTFieldTO> otFieldList = otClientData.getDemographicData();
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
    }
    else {
      // No Delivery Specific information
      if (dtiResReq.getNoteList().size() > 0) {
        // If note, process
        ArrayList<String> dtiNoteList = dtiResReq.getNoteList();

        boolean firstNote = true;
        StringBuffer noteBuffer = new StringBuffer();
        for /* each */(String resString : /* in */dtiNoteList) {
          if (!firstNote) noteBuffer.append(ATSCR);
          firstNote = false;
          noteBuffer.append(resString);
        }
        otManageRes.setReservationNote(noteBuffer.toString());
      }
    }

    // Set Transaction note (2.10)
    otManageRes.setTransactionNote(dtiRequest.getPayloadHeader()
        .getPayloadID());
	
    // Set ExternalTransactionID (optional) (as of 2.16.2, JTL)
    if (dtiResReq.getExtTxnIdentifier() != null) {
      
      ExtTxnIdentifierTO dtiExtId = dtiResReq.getExtTxnIdentifier(); 
      
      OTExternalTransactionIDTO otExtTxnId = new OTExternalTransactionIDTO();
      otExtTxnId.setId(dtiExtId.getTxnIdentifier());
      otExtTxnId.setAlreadyEncrypted(dtiExtId.getIsSHA1Encrypted());
      
      otManageRes.setExternalTransactionID(otExtTxnId);
    }

    // Generate Event - Reservations affect entitlements and thus should
    // generate an event.
    otManageRes.setGenerateEvent(true);

    // Set the manage reservation TO on the command
    atsCommand.setManageReservationTO(otManageRes);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;

  }

  /**
   * @since 2.9
   * @param otManageRes
   * @param dtiTktList
   * @param dtiPdtMap
   * @return
   */
  private static boolean createOTProducts(OTManageReservationTO otManageRes,
      ArrayList<TicketTO> dtiTktList,
      HashMap<String, DBProductTO> dtiPdtMap) {

    boolean productsAssignAccounts = false;

    for /* each */(TicketTO aDtiTicket : /* in */dtiTktList) {

      OTProductTO otProduct = new OTProductTO();

      // Item
      otProduct.setItem(aDtiTicket.getTktItem());

      // ItemType
      otProduct.setItemType(ITEM_TYPE);

      // ItemNumCode
      DBProductTO dtiPdt = dtiPdtMap.get(aDtiTicket.getProdCode());
      logger.sendEvent(
          "transformRequest: got product " + dtiPdt.getPdtCode() + " is consumable?" + dtiPdt
              .isConsumable(), EventType.DEBUG, THISOBJECT);
      BigInteger tktNbr = dtiPdt.getMappedProviderTktNbr();
      otProduct.setItemNumCode(tktNbr);

      // Quantity
      otProduct.setQuantity(aDtiTicket.getProdQty());

      // ProdDemoData (as of 2.9)
      ArrayList<DemographicsTO> tktDemoList = aDtiTicket
          .getTicketDemoList();
      if (tktDemoList.size() > 0) {
        OTDemographicInfo otDemoInfo = new OTDemographicInfo();
        WDWBusinessRules.transformTicketDemoData(tktDemoList,
            otDemoInfo);
        otProduct.setDemographicInfo(otDemoInfo);
      }

      // StartDate
      boolean startValidityDateSet = false;
      boolean endValidityDateSet = false;
      if (dtiPdt.isValidityDateInfoRequired() && aDtiTicket
          .getTktValidityValidStart() != null) {
        otProduct.setValidity_StartDate(aDtiTicket
            .getTktValidityValidStart());
        startValidityDateSet = true;
      }

      // EndDate
      if (dtiPdt.isValidityDateInfoRequired() && aDtiTicket
          .getTktValidityValidEnd() != null) {
        otProduct.setValidity_EndDate(aDtiTicket
            .getTktValidityValidEnd());
        endValidityDateSet = true;
      }

      // Validity Start Date Validation/Auto-correction (as of 2.11)
      // Validity Start Date should be provided in all instances when end is, but there is no
      // XSD rule mandating the relationship. The choices are to error or default, and ticketing
      // is asking for a default to today's date for the start date if the end has been specified.
      if (endValidityDateSet && !startValidityDateSet) {
        GregorianCalendar gc = new GregorianCalendar();
        otProduct.setValidity_StartDate(gc);
      }

      // Price
      otProduct.setPrice(aDtiTicket.getProdPrice());
      
      //ProdPriceToken
      if((null != aDtiTicket.getProdPriceQuoteToken()) && (aDtiTicket.getExtrnlPrcd().equalsIgnoreCase("F"))) {
    	  otProduct.setProdPriceToken(aDtiTicket.getProdPriceQuoteToken());  
      }

      // EntitlementAccountId, list of id, one for each non-consumable
      // ticket of this product tied to that account
      if ((aDtiTicket.getTicketAssignmets().size() > 0) && (dtiPdt
          .isConsumable() == false)) {

        productsAssignAccounts = true;

        for (/* each */TktAssignmentTO ticket : /* in */aDtiTicket
            .getTicketAssignmets()) {
          int qty = ticket.getProdQty().intValue();
          for (int i = 0; i < qty; i++) {
            otProduct.getEntitlementAccountId().add(
                ticket.getAccountItem().toString());
          }
        }
      }

      // Put product in the list.
      otManageRes.getProductList().add(otProduct);

    } // Product Loop
    return productsAssignAccounts;
  }

  /**
   * Sets the association info on the request, based on input from the DTI reservation request.
   * 
   * @param dtiResReq
   *            the DTI Reservation Request Transfer Object
   * @return the Omni Ticket Association Info Transfer Object
   * @throws DTIException
   *             if unable to get the EligibilityAssocId
   */
  private static OTAssociationInfoTO setAssociationInfo(
      ReservationRequestTO dtiResReq) throws DTIException {
    OTAssociationInfoTO otAssocInfo = new OTAssociationInfoTO();

    String eligGroup = dtiResReq.getEligibilityGroup();
    String eligMember = dtiResReq.getEligibilityMember();

    Integer associationId = EligibilityKey.getEligibilityAssocId(eligGroup);
    otAssocInfo.setAssociationId(associationId);

    boolean isMemberNumeric = true;
    Integer memberInteger = null;
    try {
      memberInteger = Integer.decode(eligMember);
    }
    catch (NumberFormatException nfe) {
      isMemberNumeric = false;
    }

    if (eligGroup.compareTo(WDWBusinessRules.DVC_STRING) == 0) {
      otAssocInfo.setMemberField(eligMember);
    }
    else if (isMemberNumeric == false) {
      otAssocInfo.setMemberId(DEFAULT_MEMBERID);
      otAssocInfo.setAllowMemberCreation(true);

      // Note: This is hard-coded to field number one because field number
      // one is the ONLY field ATS has enabled to search for existing
      // associations and add them dynamically.
      OTFieldTO otField = new OTFieldTO(FIELD_NUMBER_ONE, eligMember);
      otAssocInfo.getDemographicData().add(otField);

    }
    else {
      otAssocInfo.setMemberId(memberInteger);
    }
    return otAssocInfo;
  }

  /**
   * Create the OT Demographics in the OT field list.
   * 
   * @param otFieldList
   *            The field list of demographic elements and values.
   * @param dtiBillInfo
   *            The DTI billing info DemographicsTO.
   * @param dtiShipInfo
   *            The DTI shipping info DemographicsTO.
   * @param agencyTO
   *            The DTI travel agency information.
   */
  private static void createOtDemographics(ArrayList<OTFieldTO> otFieldList,
      DemographicsTO dtiBillInfo, DemographicsTO dtiShipInfo,
      AgencyTO agencyTO) {

    // Billing Info
    if (dtiBillInfo != null) {

      // Bill name
      if (dtiBillInfo.getName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_NAME, 
            DTIFormatter.websafe(dtiBillInfo.getName())));
      }

      // Bill Last Name
      if (dtiBillInfo.getLastName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_LASTNAME,
              DTIFormatter.websafe(dtiBillInfo.getLastName())));
      }

      // Bill First Name
      if (dtiBillInfo.getFirstName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_FIRSTNAME,
              DTIFormatter.websafe(dtiBillInfo.getFirstName())));
      }

      // Bill Address 1
      if (dtiBillInfo.getAddr1() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_ADDR1, 
            DTIFormatter.websafe(dtiBillInfo.getAddr1())));
      }

      // Bill Address 2
      if (dtiBillInfo.getAddr2() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_ADDR2, 
            DTIFormatter.websafe(dtiBillInfo.getAddr2())));
      }

      // Bill City
      if (dtiBillInfo.getCity() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_CITY, 
            DTIFormatter.websafe(dtiBillInfo.getCity())));
      }

      // Bill State
      if (dtiBillInfo.getState() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_STATE, 
            DTIFormatter.websafe(dtiBillInfo.getState())));
      }

      // Bill ZIP
      if (dtiBillInfo.getZip() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_ZIP, 
            DTIFormatter.websafe(dtiBillInfo.getZip())));
      }

      // Bill Country
      if (dtiBillInfo.getCountry() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_COUNTRY, 
            DTIFormatter.websafe(dtiBillInfo.getCountry())));
      }

      // Bill Telephone
      if (dtiBillInfo.getTelephone() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_TELEPHONE,
              DTIFormatter.websafe(dtiBillInfo.getTelephone())));
      }

      // Bill E-mail
      if (dtiBillInfo.getEmail() != null) {
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_EMAIL, 
            DTIFormatter.websafe(dtiBillInfo.getEmail())));
    }

      // Bill SellerResNbr
      if (dtiBillInfo.getSellerResNbr() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_BILL_SLR_RES_NBR,
              DTIFormatter.websafe(dtiBillInfo.getSellerResNbr())));
      }
    }

    // Shipping Info
    if (dtiShipInfo != null) {

      // Ship name
      if (dtiShipInfo.getName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_NAME, 
            DTIFormatter.websafe(dtiShipInfo.getName())));
      }

      // Ship Last Name
      if (dtiShipInfo.getLastName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_LASTNAME,
              DTIFormatter.websafe(dtiShipInfo.getLastName())));
      }

      // Ship First Name
      if (dtiShipInfo.getFirstName() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_FIRSTNAME,
              DTIFormatter.websafe(dtiShipInfo.getFirstName())));
      }

      // Ship Address 1
      if (dtiShipInfo.getAddr1() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_ADDR1, 
            DTIFormatter.websafe(dtiShipInfo.getAddr1())));
      }

      // Ship Address 2
      if (dtiShipInfo.getAddr2() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_ADDR2, 
            DTIFormatter.websafe(dtiShipInfo.getAddr2())));
      }

      // Ship City
      if (dtiShipInfo.getCity() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_CITY, 
            DTIFormatter.websafe(dtiShipInfo.getCity())));
      }

      // Ship State
      if (dtiShipInfo.getState() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_STATE, 
            DTIFormatter.websafe(dtiShipInfo.getState())));
      }

      // Ship ZIP
      if (dtiShipInfo.getZip() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_ZIP, 
            DTIFormatter.websafe(dtiShipInfo.getZip())));
      }

      // Ship Country
      if (dtiShipInfo.getCountry() != null) { 
        otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_COUNTRY, 
            DTIFormatter.websafe(dtiShipInfo.getCountry())));
      }

      // Ship Telephone
      if (dtiShipInfo.getTelephone() != null) { 
        otFieldList.add(new OTFieldTO(+OTFieldTO.WDW_CLNT_SHIP_TELEPHONE,
              DTIFormatter.websafe(dtiShipInfo.getTelephone())));
      }

      // Ship Agent
      if (agencyTO != null) {
        if (agencyTO.getAgent() != null) {
          otFieldList.add(new OTFieldTO(OTFieldTO.WDW_CLNT_SHIP_AGENT,
              DTIFormatter.websafe(agencyTO.getAgent())));
        }
      }
    }

    return;
  }

  /**
   * Transforms a reservation response string from the WDW provider and updates the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @param xmlResponse
   *            The WDW provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response information.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  static void transformResponseBody(DTITransactionTO dtiTxn,
      OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

    ReservationResponseTO dtiResRespTO = new ReservationResponseTO();
    OTManageReservationTO otMngResTO = otCmdTO.getManageReservationTO();
    dtiRespTO.setCommandBody(dtiResRespTO);

    // Price mismatch warning
    if (dtiTxn.isPriceMismatch()) {
      DTIErrorTO mismatchWarn = ErrorKey
          .getErrorDetail(DTIErrorCode.PRICE_MISMATCH_WARNING);
      dtiRespTO.setDtiError(mismatchWarn);
    }

    // ResponseType
    dtiResRespTO.setResponseType(otMngResTO.getCommandType());

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
    ArrayList<OTTicketInfoTO> otTicketList = otMngResTO.getTicketInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for /* each */(OTTicketInfoTO otTicketInfo : /* in */otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        OTTicketTO otTicketTO = otTicketInfo.getTicket();

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
          dtiTicketTO.setTktValidityValidStart(otTicketInfo
                .getValidityStartDate());
        }

        if (otTicketInfo.getValidityEndDate() != null) { 
          dtiTicketTO.setTktValidityValidEnd(otTicketInfo
                .getValidityEndDate());
        }

        dtiTktList.add(dtiTicketTO);
      }
    }

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. Not supported in the XSD (RE: JTL
    // 09/15/2008)
    ArrayList<PaymentTO> dtiPmtList = dtiResRespTO.getPaymentList();
    ArrayList<OTPaymentTO> otPmtList = otMngResTO.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

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

      // Contract ID (lifted from payment section) (As of 2.15, JTL)
      for /* each */(PaymentTO aPaymentTO : /* in */dtiPmtList) {
        if (aPaymentTO.getInstallment() != null) {
          if (aPaymentTO.getInstallment().getContractId() != null) {
            String contractId = aPaymentTO.getInstallment()
                .getContractId();
            dtiReservationTO.setContractId(contractId);
            break; // Leave this loop when the condition is 1st satisfied.
          }
        }
      }

      dtiResRespTO.setReservation(dtiReservationTO);
    }

    // ClientData
    ClientDataTO dtiClientDataTO = new ClientDataTO();
    OTClientDataTO otClientDataTO = otMngResTO.getClientData();
    if (otClientDataTO != null) {
      dtiClientDataTO.setClientId(otClientDataTO.getClientUniqueId()
          .toString());
      dtiResRespTO.setClientData(dtiClientDataTO);
    }

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to 

both providers, implement in the BusinessRules in the parent package.<BR>
   * Implements the following rules:<BR>
   * 1. For OMNI, ClientId must be numeric. 2. Validate max number of reservation line items. 3. Validate major client demographics. 4. Validate maximum sizes on 

CVV & AVS data, if present. RULE: Validate that if the "installment" type of
   * payment is present,
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @throws DTIException
   *             for any rules violation.
   */
  public static void applyWDWReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;

    // RULE: Is there one shipping product on the order? (as of 2.12)
    // Moved to both WDW and DLR as a part of 2.16.3, JTL
    ArrayList<DBProductTO>dbProdList = dtiTxn.getDbProdList();
    ArrayList<TicketTO> tktListTO = dtiResReq.getTktList();
    ProductRules.validateOneShipProduct(tktListTO, dbProdList);
   
    // Validate that the client ID is numeric and correct.
    validateWDWClientId(dtiResReq);

    // Ensure TP_Lookups have been populated in the database
    validateAndSaveTPLookups(dtiTxn, dtiResReq);

    // Validate that a reservation doesn't have more than 20 line items
    validateMaxResLineItems(dtiResReq);

    // Validate that major clients do not send billing or shipping
    // demographics
    validateMajorClientDemo(dtiResReq);

    // Validate that the credit card fields have appropriate sizes. (as of 2.2)
    ArrayList<PaymentTO> pmtList = dtiResReq.getPaymentList();
    PaymentRules.validateWDWCreditCardSizes(pmtList);

    // Validate the electronic entitlement account section, if present (as of 2.10)
    if (dtiResReq.getSpecifiedAccounts() != null) {
      ElectronicEntitlementRules.validateSpecifiedAccounts(dtiResReq
          .getSpecifiedAccounts());
    }

    // Validate that if other ticket demographics have been provided, phone has been provided, as well.
    // As of 2.16.1 APMP JTL
    ProductRules.validateWdwTicketDemo(tktListTO);
    
    //Checking Delta Product & ProdPriceQuoteToken
    WDWExternalPriceRules.validateDeltaProducts(tktListTO);

    // RULE: Validate that if the "installment" type of payment is present,
    ArrayList<TPLookupTO> tpLookups = dtiTxn.getTpLookupTOList();
    PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);

    //RULE: If the reservation code wasn't supplied, attempt to assign one. (2.17.2 - JTL)
    if (dtiResReq.getReservation().getResCode() == null) {
       String resCode = assignResCode(dtiTxn,dtiResReq);
       dtiResReq.getReservation().setResCode(resCode);
    }
    
    return ;
  }
  
  /**
   * Assign a reservation code based on reservation code rules.
   * If it is not a rework, and the attribute is not set to override
   *  the use of race rescode generation, then create a new rescode and insert it
   * into the the transaction id rescode xref table. Otherwise rescode is not generated
   * and is null.
   *
   * @param dtiTxn the dti txn
   * @param dtiResReq the dti res req
   * @throws DTIException the DTI exception
   */
  private static String assignResCode(DTITransactionTO dtiTxn,
      ReservationRequestTO dtiResReq) throws DTIException {
    
    String resCode = null;
    String candidateResCode = null;

    HashMap<CmdAttrCodeType, AttributeTO> attribMap = dtiTxn.getAttributeTOMap();
    AttributeTO resOverrideAttr = attribMap.get(CmdAttrCodeType.RACE_RES_OVERRIDE);
    
    if (resOverrideAttr != null) {
      return resCode; // null
    }
    
    String payloadId = dtiTxn.getRequest().getPayloadHeader().getPayloadID();
    boolean isRework = dtiTxn.isRework();
    
    // If this is a rework, there is likely a res code already logged.
    // Attempt to get it.
    if (isRework) {
      TransidRescodeTO rescodeTO = TransidRescodeKey.getTransidRescodeFromDB(payloadId);
      if (rescodeTO != null) {
         resCode = rescodeTO.getRescode();
      }
    } 

    // If this isn't rework, then it is likely you need to create a res code.
    // Attempt to do so.
    if (!isRework) {     
       candidateResCode = WDWAlgorithmUtility.generateResCode(payloadId); 
       resCode = TransidRescodeKey.insertTransIdRescode(dtiTxn.getTransIdITS(), payloadId, candidateResCode);
    } 
    
    // If you get here with no resCode, it's likely that there wasn't one 
    // when you were expecting it or a res code duplicate was found.
    // Try 10 times to create.  If you can, great.  If not, error.
    if (resCode == null) {
      
      logger.sendEvent("Initial reservation code generation failure.  Reattempting for payload ID: " + payloadId,EventType.DEBUG, THISOBJECT);
    
      int counter = 0;
      while ((resCode == null) && (counter < 10)) {
        candidateResCode = WDWAlgorithmUtility.generateResCode(payloadId); 
        resCode = TransidRescodeKey.insertTransIdRescode(dtiTxn.getTransIdITS(), payloadId, candidateResCode);
        counter++;
      }
      
      // After 10 times, you weren't able to create a res code and store it. Error out.
      if (resCode == null) {
        throw new DTIException(WDWReservationRules.class, DTIErrorCode.CANNOT_GEN_RESCODE,
            "Cannot generate reservation code after ten attempts.");
      }
    }

    return resCode;
  }

  /**
   * Creates the reservation code by attempting multiple times if there is an insert problem.
   * @param dtiTxn
   * @param resCode
   * @param payloadId
   * @return
   * @throws DTIException
   */
  protected static String createReservationCode(DTITransactionTO dtiTxn, String resCode, String payloadId)
      throws DTIException {
    
    boolean inserted = false;
     int attemptCount = 0;
     
     while (inserted == false) {
       
       resCode = WDWAlgorithmUtility.generateResCode(payloadId);
       try {
         TransidRescodeKey.insertTransIdRescode(dtiTxn.getTransIdITS(), payloadId, resCode);
         inserted = true;
       } catch (Exception e) {
         attemptCount++;
         logger.sendEvent(
             "WDW RACE_RES_OVERRIDE failed to create an insert a valid res code.  Attempt: " + attemptCount, EventType.WARN, THISOBJECT);
         if (attemptCount >= 10) {
           logger.sendEvent(
               "WDW RACE_RES_OVERRIDE failed to create an insert a valid res code after 10 attempts.", EventType.WARN, THISOBJECT);
           throw e;
         }
       }
     }
     
    return resCode;
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
      if ((clientDataTO.getBillingInfo() != null) || (clientDataTO
          .getShippingInfo() != null)) {
        throw new DTIException(
            WDWReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not specify billing or shipping demographics.");
      }

    }
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
      throw new DTIException(
          WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation exceeds the maximum number of line items (" + MAX_RES_LINE_ITEMS + "). " + tktProdList
              .size() + " items were attempted.");
    }
  }

  /**
   * Ensure TP_Lookups have been populated in the database
   * 
   * @param dtiTxn
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateAndSaveTPLookups(DTITransactionTO dtiTxn,
      ReservationRequestTO dtiResReq) throws DTIException {

    String tpiCode = dtiTxn.getTpiCode();
    TransactionType txnType = dtiTxn.getTransactionType();
    ReservationTO dtiResTO = dtiResReq.getReservation();
    ClientDataTO dtiCliDataTO = dtiResReq.getClientData();
    String language = dtiCliDataTO.getDemoLanguage();
    String clientType = dtiCliDataTO.getClientType();

    if (language == null) {
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "ClientData DemoLanguage cannot be null.");
    }
    if (clientType == null) {
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "ClientData ClientType cannot be null.");
    }

    // 2013.06.25 - MWH - Added check if Reservation tag is null or not
    if (dtiResTO == null) {
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation cannot be null.");
    }

    String resPickupArea = dtiResTO.getResPickupArea();
    String resSalesType = dtiResTO.getResSalesType();

    if (resPickupArea == null) {
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation ResPickupArea cannot be null.");
    }
    if (resSalesType == null) {
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation ResSalesType cannot be null.");
    }

    ArrayList<TPLookupTO> tpLookups = LookupKey.getTPCommandLookup(tpiCode,
        txnType, language, clientType, resPickupArea, resSalesType);

    if (tpLookups.size() < 7) { // Should see seven items back.

      String missingValue = "all";

      HashSet<TPLookupTO.TPLookupType> hashSet = new HashSet<TPLookupTO.TPLookupType>();
      for /* each */(TPLookupTO aLookup : /* in */tpLookups) {
        hashSet.add(aLookup.getLookupType());
      }

      if (!hashSet.contains(TPLookupTO.TPLookupType.CLIENT_TYPE)) {
        missingValue = "ClientType - not mapped for " + clientType;
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.PICKUP_TYPE)) {
        missingValue = "PickupType";
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.SALES_TYPE)) {
        missingValue = "SalesType - not mapped for " + resSalesType;
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.LANGUAGE)) {
        missingValue = "Language - not mapped for " + language;
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.MAX_LIMIT)) {
        missingValue = "MaxLimit";
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.PICKUP_AREA)) {
        missingValue = "PickupArea - not mapped for " + resPickupArea;
      }
      else if (!hashSet.contains(TPLookupTO.TPLookupType.INSTALLMENT)) {
        missingValue = "Installment";
      }

      throw new DTIException(
          WDWReservationRules.class,
          DTIErrorCode.INVALID_AREA,
          "TPCommandLookup did not find all values (missing translation for " + missingValue + ")");
    }
    dtiTxn.setTpLookupTOList(tpLookups);
  }

  /**
   * Validates the WDW Client ID
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateWDWClientId(ReservationRequestTO dtiResReq) throws DTIException {

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
          }
          catch (NumberFormatException nfe) {
            throw new DTIException(
                WDWReservationRules.class,
                DTIErrorCode.INVALID_MSG_CONTENT,
                "ClientID must be numeric (is " + clientId + ").");
          }
        }
      }
    }
    else {

      // 2013.06.25 - MWH - ClientData should exists before we determine
      // ClientType (see below), otherwise throws NPE
      throw new DTIException(WDWReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT, "ClientData must exists.");

    }

    // Validate that seller isn't attempting to create a new major client
    String clientType = dtiResReq.getClientData().getClientType();

    if (clientType.compareToIgnoreCase(MAJOR_CLIENT_TYPE) == 0) {

      if (clientIdString == null) {
        throw new DTIException(WDWReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not omit a clientID.");
      }

      if (clientId == 0) {
        throw new DTIException(WDWReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Major may not specify a clientID of zero.");
      }
    }
    if (clientType.compareToIgnoreCase(PRIVATE_CLIENT_TYPE) == 0) {
      if (clientIdString != null && clientIdString.length() > 0 && clientId != 0) {
        throw new DTIException(WDWReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "Reservations with a client type of Private may not specify a clientID.");
      }
    }

    return;
  }

  /**
   * Format a note with the appropriate information needed for fulfillment when handling private and major clients. If no special delivery fields supplied, then 

return an empty ArrayList
   * 
   * @param dtiTxn
   *            the DTITransactionTO for this transaction.
   * @return formatted note string
   */
  private static ArrayList<String> setClientDeliveryNoteDetails(
      DTITransactionTO dtiTxn) {

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
        }
        else {
          String subString = sb.substring(startIndex);
          noteDetailList.add(subString);
        }

      }

    }

    return noteDetailList;
  }

  /**
   * @since 2.9
   * @param dtiResReq
   * @param otAccounts
   * @throws DTIException
   */
  private static void transformSpecifiedAccount(
      ReservationRequestTO dtiResReq,
      ArrayList<OTAccountDataTO> otAccounts) throws DTIException {

    for (SpecifiedAccountTO dtiAccount : dtiResReq.getSpecifiedAccounts()) {

      OTAccountDataTO otAccountData = new OTAccountDataTO();

      // Account Item -> EntitlementAccountId
      otAccountData.setEntitlementAccountId(dtiAccount.getAccountItem());

      // New Account -> ExternalReference
      if (dtiAccount.getNewExternalReferenceType() != null && dtiAccount
          .getNewExternalReferenceValue() != null) {
        logger.sendEvent(
            "WDWReservationRules: search by External Referenct type",
            EventType.DEBUG, WDWReservationRules.class);

        // New Account with external reference
        Byte otType = WDWBusinessRules
            .transformEntitlementExternalReferenceType(dtiAccount
                .getNewExternalReferenceType());

        if (otType == null) {
          // ERROR shouldn't happen
          throw new DTIException(WDWReservationRules.class,
              DTIErrorCode.INVALID_MSG_CONTENT,
              "Invalid ExternalReferenceType submited.");
        }
        else {
          otAccountData.setAccountExternalReferenceType(otType);
        }

        otAccountData.setAccountExternalReference(dtiAccount
            .getNewExternalReferenceValue());

      }
      else {

        // Existing Account -> Search Filter
        logger.sendEvent(
            "WDWReservationRules: Search for existing account existingAccountId:" + dtiAccount
                .getExistingAccountId(), EventType.DEBUG,
            THISOBJECT);

        if (dtiAccount.getExistingMediaId() != null) {

          otAccountData.setSearchExistingMediaId(dtiAccount
              .getExistingMediaId());

        }
        else if (dtiAccount.getExistingTktID() != null) {

          OTTicketTO otTicket = new OTTicketTO();
          TicketIdTO dtiTicket = dtiAccount.getExistingTktID();
          ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket
              .getTicketTypes();
          TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

          switch (dtiTicketType) {
          case DSSN_ID:
            otTicket.setTDssn(dtiTicket.getDssnDate(),
                dtiTicket.getDssnSite(),
                dtiTicket.getDssnStation(),
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

          otAccountData.setTicketSearchMode(otTicket);

        }
        else if (dtiAccount.getExistingAccountId() != null) {
          logger.sendEvent(
              "WDWReservationRules: Got existingAccountId:" + dtiAccount
                  .getExistingAccountId(), EventType.DEBUG,
              THISOBJECT);
          otAccountData.setSearchAccountId(dtiAccount
              .getExistingAccountId());
        }
      }

      // Account Demo Data -> DemographicData
      if (dtiAccount.getAccountDemo() != null) {

        DemographicsTO dtiAcctDemo = dtiAccount.getAccountDemo();

        OTDemographicData otDemoData = new OTDemographicData();

        ArrayList<OTFieldTO> otFieldList = new ArrayList<OTFieldTO>();

        // Title
        if (dtiAcctDemo.getTitle() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_TITLE,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getTitle())));
        }

        // FirstName
        if (dtiAcctDemo.getFirstName() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_FIRSTNAME,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getFirstName())));
        }

        // MiddleName
        if (dtiAcctDemo.getMiddleName() != null) {
          otFieldList.add(new OTFieldTO(
              OTFieldTO.WDW_ACCTDEMO_MIDDLENAME,
              DTIFormatter.websafeToUpper(dtiAcctDemo
                  .getMiddleName())));
        }

        // LastName
        if (dtiAcctDemo.getLastName() != null) {
          otFieldList.add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_LASTNAME,
              DTIFormatter.websafeToUpper(dtiAcctDemo
                  .getLastName())));
        }

        // Suffix
        if (dtiAcctDemo.getSuffix() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_SUFFIX,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getSuffix())));
        }

        // DateOfBirth
        if (dtiAcctDemo.getDateOfBirth() != null) {
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
          String dobString = sdf.format(dtiAcctDemo.getDateOfBirth()
              .getTime());
          otFieldList.add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_DOB,
              dobString));
        }

        // Addr1
        if (dtiAcctDemo.getAddr1() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_ADDRESS1,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getAddr1())));
        }

        // Addr2
        if (dtiAcctDemo.getAddr2() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_ADDRESS2,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getAddr2())));
        }

        // City
        if (dtiAcctDemo.getCity() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_CITY,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getCity())));
        }

        // State
        if (dtiAcctDemo.getState() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_STATE,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getState())));
        }

        // ZIP
        if (dtiAcctDemo.getZip() != null) {
          otFieldList.add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_ZIP,
              DTIFormatter.websafeToUpper(dtiAcctDemo.getZip())));
        }

        // Country
        if (dtiAcctDemo.getCountry() != null) {
          otFieldList.add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_COUNTRY,
              DTIFormatter.websafeToUpper(dtiAcctDemo
                  .getCountry())));
        }

        // Email
        if (dtiAcctDemo.getEmail() != null) {
          otFieldList
              .add(new OTFieldTO(OTFieldTO.WDW_ACCTDEMO_EMAIL,
                  DTIFormatter.websafeToUpper(dtiAcctDemo
                      .getEmail())));
        }

        // OptInSolicit
        if (dtiAcctDemo.getOptInSolicit() != null) {
          if (dtiAcctDemo.getOptInSolicit().booleanValue() == true) {
            otFieldList.add(new OTFieldTO(
                OTFieldTO.WDW_ACCTDEMO_OKTOMAIL,
                WDWBusinessRules.YES));
          }
          else {
            otFieldList.add(new OTFieldTO(
                OTFieldTO.WDW_ACCTDEMO_OKTOMAIL,
                WDWBusinessRules.NO));
          }
        }

        // Set Demo Data List
        otDemoData.setDemoDataList(otFieldList);

        // Set Demo Data
        otAccountData.setDemoData(otDemoData);

      }

      // NewMediaData -> MediaData
      if (dtiAccount.getNewMediaDataList() != null) {

        // 2013.06.12 - MWH - logic for NewMediaData
        ArrayList<NewMediaDataTO> aMediaList = dtiAccount
            .getNewMediaDataList();

        for (NewMediaDataTO mediaItem : aMediaList) {

          OTMediaDataTO otMediaData = new OTMediaDataTO();

          otMediaData.setMediaId(mediaItem.getMediaId());
          otMediaData.setMfrId(mediaItem.getMfrId());
          otMediaData.setVisualId(mediaItem.getVisualId());

          otAccountData.addMediaData(otMediaData);

        }

      }

      otAccounts.add(otAccountData);

    }

    return;

  }

}
