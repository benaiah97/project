package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.CreateTicketResponseTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAccountDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.ElectronicEntitlementRules;
import pvt.disney.dti.gateway.rules.ProductRules;

/**
 * This class is responsible for three major functions for WDW create tickets:<BR>
 * 1. Defining the business rules specific to WDW create tickets.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class WDWCreateTicketRules {

  private static final Class<WDWCreateTicketRules> THISOBJECT = WDWCreateTicketRules.class;

  /** Request type header constant. */
  private final static String REQUEST_TYPE_CREATE = "Create";

  /** Request subtype header constant. */
  private final static String REQUEST_SUBTYPE_CREATE_TXN = "CreateTransaction";

  /** Constant indicating all tags should be created. */
  // private final static String[] CT_TAGS = { "TicketInfo", "TransactionCOD",
  // "TransactionDSSN" };

  private final static String[] CT_TAGS = { "All" }; // As of 2.9

  /** Constant indicating the Create Transaction XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "CreateTransactionRequest.xsd";

  /** The Alpha code for the WDW ticket provider. */
  private final static String WDW_PROVIDER = "WDW";

  /** Entitlement Account typology */
  private final static String SINGLE_ACCOUNT = "SINGLEACCOUNT";

  /** Entitlement Account typology */
  private final static String PER_TICKET_ACCOUNT = "PERTICKETACCOUNT";

  /** Entitlement Account typology */
  private final static String ENTITLEMENT_ACCOUNT_ID = "ENTITLEMENTACCOUNTID";

  /** Constant representing the default member ID. */
  private final static Integer DEFAULT_MEMBERID = new Integer(0);

  /** Constant representing the first field number. */
  private final static Integer FIELD_NUMBER_ONE = new Integer(1);

  public static final String ACCOUNT_PER_TICKET = "AccountPerTicket";
  public static final String ACCOUNT_PER_ORDER = "AccountPerOrder";

  private static final EventLogger logger = EventLogger
      .getLogger(WDWCreateTicketRules.class.getCanonicalName());

  /**
   * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshalling routines to create an XML string.
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
    CreateTicketRequestTO dtiCreateTktReq = (CreateTicketRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.CREATETRANSACTION);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_CREATE, REQUEST_SUBTYPE_CREATE_TXN);
    atsCommand.setHeaderTO(hdr);

    // === Create Transaction Level ===
    OTCreateTransactionTO otCrtTxnTO = new OTCreateTransactionTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    for (int i = 0; i < CT_TAGS.length; i++) {
      tagList.add(CT_TAGS[i]);
    }
    otCrtTxnTO.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();
    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otCrtTxnTO.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    }
    else {
      otCrtTxnTO.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }
    // Product
    ArrayList<OTProductTO> otProdList = otCrtTxnTO.getProductList();
    ArrayList<TicketTO> dtiTktList = dtiCreateTktReq.getTktList();
    HashMap<String, DBProductTO> dbProdMap = dtiTxn.getDbProdMap();
    boolean productsAssignAccounts = createOTProducts(otProdList,
        dtiTktList, dbProdMap);

    // Entitlement Account
    if (ACCOUNT_PER_ORDER.equals(dtiCreateTktReq.getDefaultAccount())) {

      otCrtTxnTO.setEntitlementAccountCreationTypology(SINGLE_ACCOUNT);

    }
    else if (ACCOUNT_PER_TICKET.equals(dtiCreateTktReq.getDefaultAccount())) {

      otCrtTxnTO
          .setEntitlementAccountCreationTypology(PER_TICKET_ACCOUNT);

    }
    else {

      if (dtiCreateTktReq.getSpecifiedAccounts() != null && dtiCreateTktReq
          .getSpecifiedAccounts().size() > 0) {

        // ENTITLEMENTACCOUNTID always requires a tag to be present in
        // the product node Per Andrea Martinotti 10/2/2012
        if (productsAssignAccounts) {

          otCrtTxnTO
              .setEntitlementAccountCreationTypology(ENTITLEMENT_ACCOUNT_ID);

        }
        else {

          otCrtTxnTO
              .setEntitlementAccountCreationTypology(SINGLE_ACCOUNT);

        }

        transformSpecifiedAccount(dtiCreateTktReq,
            otCrtTxnTO.getAccountsData());

      }
    }

    // Payment as of 2.9
    // There are three alternatives: default payment, no payment, and
    // payment.
    ArrayList<OTPaymentTO> otPaymentList = otCrtTxnTO.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiCreateTktReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();
    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
        entityTO);

    // InTransactionAttributes
    if ((otCrtTxnTO.getInTxnAttrList() != null) && (otCrtTxnTO
        .getInTxnAttrList().size() > 0)) {
      ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otCrtTxnTO
          .getInTxnAttrList();
      WDWBusinessRules
          .setOTInTransactionAttributes(dtiTxn, inTxnAttrList);
    }

    // AssociationInfo
    if (dtiCreateTktReq.getEligibilityGroup() != null) {
      OTAssociationInfoTO otAssocInfo = setAssociationInfo(dtiCreateTktReq);
      otCrtTxnTO.setAssociationInfo(otAssocInfo);
    }

    // Generate Event - create ticket affects entitlements and thus should
    // generate an event.
    otCrtTxnTO.setGenerateEvent(true);

    // Transaction Note (payload ID as of 2.10)
    // As of 2.12, override with Audit Notification if available.
    if (dtiCreateTktReq.getAuditNotation() == null) {
      otCrtTxnTO.setTransactionNote(dtiRequest.getPayloadHeader()
          .getPayloadID());
    }
    else {
      otCrtTxnTO.setTransactionNote(dtiCreateTktReq.getAuditNotation());
    }

    // Set the Create Transaction TO on the command
    atsCommand.setCreateTransactionTO(otCrtTxnTO);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
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
      CreateTicketRequestTO dtiResReq) throws DTIException {
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
   * Creates the Omni Ticket product list from the DTI Product List and Database Product Map provided.
   * 
   * @param otProdList
   *            The Omni Ticket Product List.
   * @param dtiTktList
   *            The DTI Request Ticket List.
   * @param dbProdMap
   *            The DTI Database Product Map.
   */
  private static boolean createOTProducts(ArrayList<OTProductTO> otProdList,
      ArrayList<TicketTO> dtiTktList,
      HashMap<String, DBProductTO> dbProdMap) {

    boolean productsAssignAccounts = false;

    for /* each */(TicketTO aDtiTicket : /* in */dtiTktList) {

      OTProductTO otProduct = new OTProductTO();

      // Item
      otProduct.setItem(aDtiTicket.getTktItem());

      // TicketType (Note: Varies from Reservation - inconsistent OT interface)
      DBProductTO dbProduct = dbProdMap.get(aDtiTicket.getProdCode());
      BigInteger ticketType = dbProduct.getMappedProviderTktNbr();
      otProduct.setTicketType(ticketType);

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

      // Validity Dates
      // StartDate
      if (dbProduct.isValidityDateInfoRequired() && aDtiTicket
          .getTktValidityValidStart() != null) {
        otProduct.setValidity_StartDate(aDtiTicket
            .getTktValidityValidStart());
      }

      // EndDate
      if (dbProduct.isValidityDateInfoRequired() && aDtiTicket
          .getTktValidityValidEnd() != null) {
        otProduct.setValidity_EndDate(aDtiTicket
            .getTktValidityValidEnd());
      }
      
      // Price quote token for variably priced products
      if((null != aDtiTicket.getExtrnlPrcd()) && (aDtiTicket.getExtrnlPrcd().equals("T"))) {
         otProduct.setProdPriceToken(aDtiTicket.getProdPriceQuoteToken());   
      }

      // Price (as of 2.9)
      if (aDtiTicket.getProdPrice() != null) {
        otProduct.setPrice(aDtiTicket.getProdPrice());
      }

      // EntitlementAccountId, list of id, one for each non-consumable
      // ticket of this product tied to that account
      if ((aDtiTicket.getTicketAssignmets().size() > 0) && (dbProduct
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

      // TicketNote
      if (aDtiTicket.getTktNote() != null) {
        otProduct.setTicketNote(aDtiTicket.getTktNote());
      }

      // TicketAttribute
      if (aDtiTicket.getTktShell() != null) {
        otProduct.setTicketAttribute(new BigInteger(aDtiTicket
            .getTktShell()));
      }

      // Add to List
      otProdList.add(otProduct);

    }

    return productsAssignAccounts;
  }

  /**
   * Transforms a create ticket response string from the WDW provider and updates the DTITransactionTO object with the response information.
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

    CreateTicketResponseTO dtiCrtTktResp = new CreateTicketResponseTO();
    dtiRespTO.setCommandBody(dtiCrtTktResp);
    OTCreateTransactionTO otCrtTxnTO = otCmdTO.getCreateTransactionTO();

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiCrtTktResp.getTicketList();
    ArrayList<OTTicketInfoTO> otTicketList = otCrtTxnTO.getTktInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for /* each */(OTTicketInfoTO otTicketInfo : /* in */otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        OTTicketTO otTicketTO = otTicketInfo.getTicket();

        dtiTicketTO.setTktItem(otTicketInfo.getItem());

        dtiTicketTO.setMag(otTicketTO.getMagTrack());
        dtiTicketTO.setBarCode(otTicketTO.getBarCode());
        GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
        String site = otTicketTO.getTdssnSite();
        String station = otTicketTO.getTdssnStation();
        String number = otTicketTO.getTdssnTicketId();
        dtiTicketTO.setDssn(dssnDate, site, station, number);
        dtiTicketTO.setTktNID(otTicketTO.getTCOD());

        // TDSSN Provider etc.
        TicketTransactionTO dtiTktTranTO = new TicketTransactionTO();
        dtiTktTranTO.setTktProvider(WDW_PROVIDER);
        dtiTktTranTO.setDssnDate(otCrtTxnTO.getTransactionDSSN()
            .getDate());
        dtiTktTranTO.setDssnSite(otCrtTxnTO.getTransactionDSSN()
            .getSite());
        dtiTktTranTO.setDssnStation(otCrtTxnTO.getTransactionDSSN()
            .getStation());
        dtiTktTranTO.setDssnNumber(otCrtTxnTO.getTransactionDSSN()
            .getTransactionId().toString());
        dtiTicketTO.setTktTran(dtiTktTranTO);

        dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
        dtiTicketTO.setTktTax(otTicketInfo.getTax());

        if (otTicketInfo.getValidityStartDate() != null) dtiTicketTO
            .setTktValidityValidStart(otTicketInfo
                .getValidityStartDate());

        if (otTicketInfo.getValidityEndDate() != null) dtiTicketTO
            .setTktValidityValidEnd(otTicketInfo
                .getValidityEndDate());

        if (otTicketInfo.getTicketNote() != null) {
          dtiTicketTO.setTktNote(otTicketInfo.getTicketNote());
        }

        dtiTktList.add(dtiTicketTO);
      }
    }

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. Not supported in the XSD (RE: JTL
    // 09/15/2008)
    ArrayList<PaymentTO> dtiPmtList = dtiCrtTktResp.getPaymentList();
    ArrayList<OTPaymentTO> otPmtList = otCrtTxnTO.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

    return;
  }

  /**
   * @since 2.9
   * @param dtiResReq
   * @param otAccounts
   * @throws DTIException
   */
  private static void transformSpecifiedAccount(
      CreateTicketRequestTO dtiResReq,
      ArrayList<OTAccountDataTO> otAccounts) throws DTIException {

    logger.sendEvent(
        "WDWCreateTicketRules: Entering transformSpecifiedAccount():",
        EventType.DEBUG, THISOBJECT);

    /* Loop through the specified accounts, either new or existing. */
    for (SpecifiedAccountTO dtiAccount : dtiResReq.getSpecifiedAccounts()) {

      OTAccountDataTO otAccountData = new OTAccountDataTO();
      otAccountData.setEntitlementAccountId(dtiAccount.getAccountItem());

      if ((dtiAccount.getNewExternalReferenceType() != null) && (dtiAccount
          .getNewExternalReferenceValue() != null)) {

        logger.sendEvent(
            "WDWCreateTicketRules: search by External Reference type",
            EventType.DEBUG, WDWCreateTicketRules.class);

        // New Account with external reference
        Byte otType = WDWBusinessRules
            .transformEntitlementExternalReferenceType(dtiAccount
                .getNewExternalReferenceType());

        if (otType == null) {
          // ERROR shouldn't happen
          throw new DTIException(WDWCreateTicketRules.class,
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
        // search for existing account
        logger.sendEvent(
            "WDWCreateTicketRules: Adding Existing account",
            EventType.DEBUG, THISOBJECT);

        if (dtiAccount.getExistingMediaId() != null) {

          otAccountData.setSearchExistingMediaId(dtiAccount
              .getExistingMediaId());
          logger.sendEvent(
              "Just added existing media ID: " + dtiAccount
                  .getExistingMediaId() + " to otAccountData.",
              EventType.DEBUG, THISOBJECT);

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
              "WDWCreateTicketRules: Got existingAccountId:" + dtiAccount
                  .getExistingAccountId(), EventType.DEBUG,
              THISOBJECT);
          otAccountData.setSearchAccountId(dtiAccount
              .getExistingAccountId());
        }
      }

      // JTL moved out of if/else as is unrelated. 10-02-2013
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

    } // For loop

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
   * Implements the following rules:<BR>
   * 1. Specified accounts must be valid.
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @throws DTIException
   *             for any rules violation.
   * @version 2.10
   */
  public static void applyWDWCreateTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    CreateTicketRequestTO crtTktReq = (CreateTicketRequestTO) dtiCmdBody;

    // Validate the electronic entitlement account section, if present (as of 2.10)
    if (crtTktReq.getSpecifiedAccounts() != null) {
      ElectronicEntitlementRules.validateSpecifiedAccounts(crtTktReq
          .getSpecifiedAccounts());
    }

    ArrayList<TicketTO> tktListTO = crtTktReq.getTktList();

    // Validate that if other ticket demographics have been provided, phone has been provided, as well.
    // As of 2.16.1 APMP JTL
    ProductRules.validateWdwTicketDemo(tktListTO);
    
    // validate variably priced product
    WDWExternalPriceRules.validateDeltaProducts(dtiTxn, tktListTO);

    return;
  }

}
