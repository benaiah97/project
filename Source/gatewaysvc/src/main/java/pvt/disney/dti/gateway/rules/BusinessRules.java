package pvt.disney.dti.gateway.rules;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.ValueConstants;
import pvt.disney.dti.gateway.dao.AttributeKey;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.dao.PaymentKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountRequestTO;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementRequestTO;
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.rules.dlr.DLRBusinessRules;
import pvt.disney.dti.gateway.rules.dlr.DLRQueryTicketRules;
import pvt.disney.dti.gateway.rules.dlr.DLRRenewEntitlementRules;
import pvt.disney.dti.gateway.rules.dlr.DLRReservationRules;
import pvt.disney.dti.gateway.rules.dlr.DLRUpgradeAlphaRules;
import pvt.disney.dti.gateway.rules.dlr.DLRVoidTicketRules;
import pvt.disney.dti.gateway.rules.hkd.HKDBusinessRules;
import pvt.disney.dti.gateway.rules.hkd.HKDQueryReservationRules;
import pvt.disney.dti.gateway.rules.hkd.HKDReservationRules;
import pvt.disney.dti.gateway.rules.wdw.WDWAssociateMediaToAccountRules;
import pvt.disney.dti.gateway.rules.wdw.WDWBusinessRules;
import pvt.disney.dti.gateway.rules.wdw.WDWCreateTicketRules;
import pvt.disney.dti.gateway.rules.wdw.WDWQueryTicketRules;
import pvt.disney.dti.gateway.rules.wdw.WDWRenewEntitlementRules;
import pvt.disney.dti.gateway.rules.wdw.WDWReservationRules;
import pvt.disney.dti.gateway.rules.wdw.WDWUpdateTicketRules;
import pvt.disney.dti.gateway.rules.wdw.WDWUpdateTransactionRules;
import pvt.disney.dti.gateway.rules.wdw.WDWUpgradeAlphaRules;
import pvt.disney.dti.gateway.rules.wdw.WDWUpgradeEntitlementRules;
import pvt.disney.dti.gateway.rules.wdw.WDWVoidTicketRules;

/**
 * This class has the responsibility of running all rules (such as pricing, permission to sell a ticket, batch hours, permission to run a transactions, etc.) at both the general and specific transaction level.
 * 
 * @author lewit019
 * @version 1
 * @since 2.16.3
 */
public abstract class BusinessRules {

  /** Serial Version ID for Business Rules */
  static final long serialVersionUID = 3206093459760846163L;
  
  /** The standard core logging mechanism. */
  private static EventLogger logger = EventLogger.getLogger(BusinessRules.class);

  public final static String CREATETAG = "Create";

  /** This holds the contingency rules object. */
  private static CalmRules calmRules = null;
  
  /**
   * Pull in any rule values from the properties file.
   * 
   * @param props
   */
  public static void initBusinessRules(Properties props) throws DTIException {

    WDWReservationRules.initWDWReservationRules(props);
    
    /** Initialize the Contingency Action Logic Module. */
    calmRules = CalmRules.getInstance(props);

    return;
  }

  /**
   * Has the sole responsibility of applying business rules.
   * 
   * @param dtiTxn
   *            The dtiTxn object containing the request.
   * @return The DTITransactionTO object with any alterations the business rules might have made.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  public static DTITransactionTO applyBusinessRules(DTITransactionTO dtiTxn) throws DTICalmException, DTIException {

    // Apply rules common for all transactions, all providers
    dtiTxn = applyCommonRules(dtiTxn);

    // Apply rules specific to the individual transaction
    TransactionType requestType = dtiTxn.getTransactionType();
    switch (requestType) {

    case QUERYTICKET:
      applyQueryTicketRules(dtiTxn);
      break;

    case UPGRADEALPHA:
      applyUpgradeAlphaRules(dtiTxn);
      break;

    case VOIDTICKET:
      applyVoidTicketRules(dtiTxn);
      break;

    case RESERVATION:
      applyReservationRules(dtiTxn);
      break;

    case CREATETICKET:
      applyCreateTicketRules(dtiTxn);
      break;

    case UPDATETICKET:
      applyUpdateTicketRules(dtiTxn);
      break;

    case UPDATETRANSACTION:
      applyUpdateTransactionRules(dtiTxn);
      break;

    case QUERYRESERVATION:
      applyQueryReservationRules(dtiTxn);
      break;

    case UPGRADEENTITLEMENT:
      applyUpgradeEntitlementRules(dtiTxn);
      break;

    case ASSOCIATEMEDIATOACCOUNT:
      applyAssociateMediaToAccountRules(dtiTxn);
      break;

    case TICKERATEENTITLEMENT:
      applyTickerateEntitlementRules(dtiTxn);
      break;

    case RENEWENTITLEMENT: // As of 2.16.1, JTL
      applyRenewEntitlementRules(dtiTxn);
      break;
      
    case VOIDRESERVATION: // As of 2.16.3, JTL
      applyVoidReservationRules(dtiTxn);
      break;   
    case QUERYELIGIBLEPRODUCTS: // As a part of AP Upgrade
        //applyVoidReservationRules(dtiTxn);
        break;   

    default:
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Transaction not supported in Java version of DTI Gateway.");
      // implicit break;
    }

    // Validate entity date
    // Rule moved here from applyCommonRules() as a part of 2.16.3, JTL
    // This places CALM and normal batch down-times after the
    // business rules have already been run.
    EntityTO entityTO = dtiTxn.getEntityTO();
    Date startDate = entityTO.getStartValidDate();
    Date endDate = entityTO.getEndValidDate();
    String startTime = entityTO.getStartValidTime();
    String endTime = entityTO.getEndValidTime();

    boolean withinDate = DateTimeRules.isNowWithinDate(startDate, endDate);
    boolean withinTime = DateTimeRules.isNowWithinGMTTime(startTime,
        endTime);

    if ((!withinDate) || (!withinTime)) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.INVALID_SALES_DATE_TIME,
          "Request attempted outside of configured time windows.");
    }
    
    // Check Contingency Actions Logic Module (CALM)
    // Moved here from DTIService.makeServiceRequest in 2.16.3, JTL
    calmRules.checkContingencyActionsLogicModule(dtiTxn);
    logger.sendEvent("Checked Contingency Actions Logic Module (CALM).",
        EventType.DEBUG, null);
    
    
    return dtiTxn;
  }

  /**
   * Applies the rules common to all transactions. Validates payload and command header, the ticket seller entity, the entity attributes, the payment information, and the entity sales date/time.
   * 
   * @param dtiTxn
   *            The dtiTxn object containing the request.
   * @return The DTITransactionTO object with any alterations the business rules might have made.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  private static DTITransactionTO applyCommonRules(DTITransactionTO dtiTxn) throws DTIException {

    // Validate that the request passed is properly constructed
    ContentRules.validateDTIRequestStructure(dtiTxn);

    // Ensure there are no missing payload header fields (common)
    PayloadHeaderTO payHeaderTO = dtiTxn.getRequest().getPayloadHeader();
    ContentRules.validatePayHdrFields(payHeaderTO);

    // Ensure there are no missing command header fields (common)
    CommandHeaderTO cmdHeaderTO = dtiTxn.getRequest().getCommandHeader();
    ContentRules.validateCmdHdrFields(cmdHeaderTO);

    // Validate provider and environment (note: adds values to "dtiTxn" object)
    dtiTxn = ContentRules.validateProviderTarget(dtiTxn);

    // Validate that the entity is active
    EntityTO entityTO = dtiTxn.getEntityTO();
    if (entityTO.isActive() != true) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.ENTITY_NOT_ACTIVE,
          "Entity tsmac " + entityTO.getTsMac() + " tslocation " + entityTO
              .getTsLocation() + " is not active in the DTI database.");
    }

    // Validate attributes (If this seller is allowed to run this transaction)
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap = null;
    String tpiCode = dtiTxn.getTpiCode();
    long entityId = entityTO.getEntityId();
    if (dtiTxn.getRequest().getCommandHeader().getCmdActor() == null) {
      attributeTOMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
          entityId);
    }
    else {
      String actor = dtiTxn.getRequest().getCommandHeader().getCmdActor();
      attributeTOMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
          entityId, actor);
    }
    if ((attributeTOMap != null) && (attributeTOMap.size() != 0)) {
      dtiTxn.setAttributeTOMap(attributeTOMap);
    }
    else {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Client requesting transaction did not have tags required by attributes.");
    }

    // Validate payment information
    long paymentId = entityTO.getDefPymtId();
    if (paymentId != 0) {
      ArrayList<PaymentLookupTO> paymentLookupTOList = PaymentKey
          .getPaymentLookup(tpiCode, paymentId);
      dtiTxn.setPaymentLookupTOList(paymentLookupTOList);
    }

    // Validate entity date
    Date startDate = entityTO.getStartValidDate();
    Date endDate = entityTO.getEndValidDate();
    String startTime = entityTO.getStartValidTime();
    String endTime = entityTO.getEndValidTime();

    boolean withinDate = DateTimeRules.isNowWithinDate(startDate, endDate);
    boolean withinTime = DateTimeRules.isNowWithinGMTTime(startTime,
        endTime);

    if ((!withinDate) || (!withinTime)) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.INVALID_SALES_DATE_TIME,
          "Request attempted outside of configured time windows.");
    }

    // Apply only provider-centric, not transaction-centric rules
    if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) { 
      dtiTxn = DLRBusinessRules.applyBusinessRules(dtiTxn); 
    }
    else if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) { 
      dtiTxn = WDWBusinessRules.applyBusinessRules(dtiTxn);
    } 
    else if (tpiCode.equals(DTITransactionTO.TPI_CODE_HKD)) {
      dtiTxn = HKDBusinessRules.applyBusinessRules(dtiTxn);
    }

    return dtiTxn;

  }

  /**
   * Applies rules specific to query ticket transactions. Makes no changes to the dtiTxn object. Calls provider-specific rules after applying the common rules.<BR>
   * 1. Enforces the rule
   * 
   * @param dtiTxn
   *            The dtiTxn object containing the request.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  public static void applyQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a QueryTicketRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != QueryTicketRequestTO.class) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-query class passed to validate.");
    }

    // RULE: Is there only one ticket on a Query Ticket, as expected?
    QueryTicketRequestTO queryReq = (QueryTicketRequestTO) commandBody;
    ArrayList<TicketTO> aTktList = queryReq.getTktList();
    TicketRules.validateOnlyOneTicketOnRequest(aTktList);

    // Apply Transaction-centric, Provider-centric rules
    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) DLRQueryTicketRules
        .applyDLRQueryTicketRules(dtiTxn);
    else if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) WDWQueryTicketRules
        .applyWDWQueryTicketRules(dtiTxn);

    return;

  }

  /**
   * Apply the upgrade alpha business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyUpgradeAlphaRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a UpgradeAlphaTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != UpgradeAlphaRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-upgrade alpha request class passed to applyUpgradeAlphaRules.");
    }

    UpgradeAlphaRequestTO uaReqTO = (UpgradeAlphaRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // Get database information for all products on the order
    ArrayList<TicketTO> tktListTO = uaReqTO.getTktList();
    ArrayList<DBProductTO> dbProdList = ProductKey
        .getOrderProducts(tktListTO);

    // RULE: Are all products on order known to the database?
    // RULE: Are all products on the order active?
    // RULE: Are any of the products on the order sold out?
    // RULE: Can all of the products on the order be sold today?
    ProductRules.validateProductsAreSellable(tktListTO, dbProdList);

    // RULE: Is the entity allowed to sell all products on the order?
    EntityTO entityTO = dtiTxn.getEntityTO();
    ArrayList<BigInteger> allowedPdtIdList = new ArrayList<BigInteger>();
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbProdList));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbProdList));
    ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
        allowedPdtIdList);

    // RULE: Are the products being sold at an acceptable price?
    // NOTE: This rule can modify the tktListTO members to indicate price
    // mismatch.
    ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);

    // RULE: Are the number of products/tickets on the order within tolerance?
    TicketRules.validateMaxEightTicketsOnRequest(tktListTO);

    // RULE: Is special eligibility required to sell these products?
    if (EligibilityRules.eligibilityRequired(dbProdList)) {
      String eligGrpCode = uaReqTO.getEligibilityGroup();
      String eligMemberID = uaReqTO.getEligibilityMember();
      Boolean eligFlag = EligibilityKey.getOrderEligibility(dbProdList,
          eligGrpCode, eligMemberID);
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode,
          eligMemberID);
    }

    // RULE: Are the mapped ticket types valid and active?
    // NOTE: The getProductTicketTypes query modifies the dbProdList members to
    // include ticket mappings.
    dbProdList = ProductKey.getProductTicketTypes(dbProdList);
    ProductRules.validateMappedProviderTicketTypes(dbProdList);

    // RULE: Do the various payments, if provided, cover the products on the
    // order?
    ArrayList<PaymentTO> payListTO = uaReqTO.getPaymentList();
    PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);

    // RULE: Are payments and payment types acceptable?
    String tpiCode = dtiTxn.getTpiCode();
    PaymentRules.validatePaymentComposition(payListTO, tpiCode);

    // Save the dbProduct list to the request for use in later translation.
    dtiTxn.setDbProdList(dbProdList);

    // Apply any rules unique to one provider.
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) DLRUpgradeAlphaRules
        .applyDLRUpgradeAlphaRules(dtiTxn);
    else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) WDWUpgradeAlphaRules
        .applyWDWUpgradeAlphaRules(dtiTxn);

    return;
  }

  /**
   * Apply void ticket rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             the DTI exception
   */
  public static void applyVoidTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a VoidTicketRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != VoidTicketRequestTO.class) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-void ticket class passed to applyVoidTicketRules.");
    }

    // RULE: Is there only one ticket on a Void Ticket, as expected?
    VoidTicketRequestTO voidReq = (VoidTicketRequestTO) commandBody;
    ArrayList<TicketTO> aTktList = voidReq.getTktList();
    TicketRules.validateOnlyOneTicketOnRequest(aTktList);

    // Apply any rules unique to one provider.
    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWVoidTicketRules.applyWDWVoidTicketRules(dtiTxn);
    }
    else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) {
      DLRVoidTicketRules.applyDLRVoidTicketRules(dtiTxn);
    }

    return;
  }

  /**
   * Apply reservation business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a ReservationRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != ReservationRequestTO.class) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-reservation class passed to applyReservationRules.");
    }

    // RULE: Is the RequestType "Create", as expected?
    ReservationRequestTO resReqTO = (ReservationRequestTO) dtiTxn
        .getRequest().getCommandBody();
    if ((resReqTO.getRequestType() == null) || (resReqTO.getRequestType()
        .length() == 0)) { 
      throw new DTIException(BusinessRules.class, DTIErrorCode.INVALID_MSG_ELEMENT,
        "No RequestType on reservation.");
    }
    if (resReqTO.getRequestType().compareTo(CREATETAG) != 0) { 
      throw new DTIException(
        BusinessRules.class, DTIErrorCode.INVALID_MSG_ELEMENT,
        "Invalid RequestType on reservation:" + resReqTO
            .getRequestType());
    }

    // Get database information for all products on the order
    ArrayList<TicketTO> tktListTO = resReqTO.getTktList();
    ArrayList<DBProductTO> dbProdList = ProductKey
        .getOrderProducts(tktListTO);

    // RULE: Are the correct Ticket Assignment Product quantities provided
    TicketRules.validateTicketAssignment(tktListTO);

    // RULE: Are all products on order known to the database?
    // RULE: Are all products on the order active?
    // RULE: Are any of the products on the order sold out?
    // RULE: Can all of the products on the order be sold today?
    ProductRules.validateProductsAreSellable(tktListTO, dbProdList);

    // RULE: Do all products have a quantity of greater than zero?
    ProductRules.validateNonZeroProductQuantity(tktListTO);

    // RULE: Is the entity allowed to sell all products on the order?
    EntityTO entityTO = dtiTxn.getEntityTO();
    ArrayList<BigInteger> allowedPdtIdList = new ArrayList<BigInteger>();
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbProdList));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbProdList));
    ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
        allowedPdtIdList);

    // RULE: Are the products being sold at an acceptable price?
    String taxExemptCode = resReqTO.getTaxExemptCode();
    boolean isTaxExempt = PaymentRules
        .determineTaxExemptStatus(taxExemptCode);
    dtiTxn.setPriceMismatch(ProductRules.validateProductPrice(entityTO,
        tktListTO, dbProdList, isTaxExempt));

    // RULE: Are the products required to have demographics? (As of 2.9)
    ProductRules.validateProductsHaveDemographics(tktListTO, dbProdList);

    // RULE: Is special eligibility required to sell these products?
    if (EligibilityRules.eligibilityRequired(dbProdList)) {
      String eligGrpCode = resReqTO.getEligibilityGroup();
      String eligMemberID = resReqTO.getEligibilityMember();
      Boolean eligFlag = EligibilityKey.getOrderEligibility(dbProdList,
          eligGrpCode, eligMemberID);
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode,
          eligMemberID);
    }

    // RULE: Are validity dates required?
    TicketRules
        .validateTicketValidityDates(tktListTO, entityTO, dbProdList);

    // RULE: Are the mapped ticket types valid and active?
    // NOTE: The getProductTicketTypes query modifies the dbProdList members to
    // include ticket mappings.
    dbProdList = ProductKey.getProductTicketTypes(dbProdList);
    ProductRules.validateMappedProviderTicketTypes(dbProdList);

    // RULE: Do the various payments, if provided, cover the products on the
    // order?
    ArrayList<PaymentTO> payListTO = resReqTO.getPaymentList();
    BigDecimal totalOrderAmount = PaymentRules.validatePaymentsOnOrder(
        tktListTO, payListTO, entityTO);
    resReqTO.setTotalOrderAmount(totalOrderAmount);

    // RULE: Are payments and payment types acceptable?
    String tpiCode = dtiTxn.getTpiCode();
    PaymentRules.validatePaymentComposition(payListTO, tpiCode);

    // Save the dbProduct list to the request for use in later translation.
    dtiTxn.setDbProdList(dbProdList);

    // Apply any rules unique to one provider.
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) {
      DLRReservationRules.applyDLRReservationRules(dtiTxn);
    } else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) { 
      WDWReservationRules.applyWDWReservationRules(dtiTxn);
    } else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_HKD) == 0) {
      HKDReservationRules.applyHKDReservationRules(dtiTxn);
    }

    // RULE: Are the number of products/tickets on the order within tolerance?
    // NOTE: This rule requires provider specific queries to have run before
    // processing to supply the "MaxLimit" lookup.
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> entityAttributes = dtiTxn
        .getAttributeTOMap();
    ArrayList<TPLookupTO> tpLookups = dtiTxn.getTpLookupTOList();
    ReservationTO resTO = resReqTO.getReservation();
    TicketRules.validateReservationTicketCount(tktListTO, tpLookups,
        entityAttributes, resTO);

    return;
  }

  /**
   * Apply create ticket business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyCreateTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    String tpiCode = dtiTxn.getTpiCode();

    // RULE: Is this a CreateTicketRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != CreateTicketRequestTO.class) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-createticket class passed to applyCreateTicketRules.");
    }

    // RULE: Is the RequestType is "Create", as expected?
    CreateTicketRequestTO createTktTO = (CreateTicketRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // Get database information for all products on the order
    ArrayList<TicketTO> tktListTO = createTktTO.getTktList();
    ArrayList<DBProductTO> dbProdList = ProductKey
        .getOrderProducts(tktListTO);

    // RULE: Are the correct Ticket Assignment Product quantities provided
    TicketRules.validateTicketAssignment(tktListTO);

    // RULE: Are all products on order known to the database?
    // RULE: Are all products on the order active?
    // RULE: Are any of the products on the order sold out?
    // RULE: Can all of the products on the order be sold today?
    ProductRules.validateProductsAreSellable(tktListTO, dbProdList);

    // RULE: Do all products have a quantity of greater than zero?
    ProductRules.validateNonZeroProductQuantity(tktListTO);

    // RULE: Is the entity allowed to sell all products on the order?
    EntityTO entityTO = dtiTxn.getEntityTO();
    ArrayList<BigInteger> allowedPdtIdList = new ArrayList<BigInteger>();
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbProdList));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbProdList));
    ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
        allowedPdtIdList);

    // RULE: Are the products being sold at an acceptable price?
    boolean isTaxExempt = false;
    dtiTxn.setPriceMismatch(ProductRules.validateProductPrice(entityTO,
        tktListTO, dbProdList, isTaxExempt));

    // RULE: Are the products required to have demographics? (As of 2.9)
    ProductRules.validateProductsHaveDemographics(tktListTO, dbProdList);

    // RULE: Is special eligibility required to sell these products?
    if (EligibilityRules.eligibilityRequired(dbProdList)) {
      String eligGrpCode = createTktTO.getEligibilityGroup();
      String eligMemberID = createTktTO.getEligibilityMember();
      Boolean eligFlag = EligibilityKey.getOrderEligibility(dbProdList,
          eligGrpCode, eligMemberID);
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode,
          eligMemberID);
    }

    // RULE: Are validity dates required?
    TicketRules
        .validateTicketValidityDates(tktListTO, entityTO, dbProdList);

    // RULE: Are the mapped ticket types valid and active?
    // NOTE: This rule modifies the dbProdList members to include ticket
    // mappings.
    dbProdList = ProductKey.getProductTicketTypes(dbProdList);
    ProductRules.validateMappedProviderTicketTypes(dbProdList);

    // RULE: Is a payment, of some kind, provided?
    // As of 2.11
    ArrayList<PaymentTO> payListTO = createTktTO.getPaymentList();
    PaymentRules.validatePaymentsPresent(payListTO, entityTO);

    // RULE: Do the various payments, if provided, cover the products on the
    // order?
    PaymentRules.validatePaymentsOnOrder(tktListTO, payListTO, entityTO);

    // RULE: Are payments and payment types acceptable?
    PaymentRules.validatePaymentComposition(payListTO, tpiCode);

    // RULE: Are the minimum fields for all tickets present?
    // Removed as of the 2.9 release; checks no longer applicable to general
    // sales. JTL
    // ContentRules.validateCreateTicketFields(tktListTO);

    // Save the dbProduct list to the request for use in later translation.
    dtiTxn.setDbProdList(dbProdList);

    // Get the default TP Lookups
    ArrayList<TPLookupTO> tpLookups = null;
    tpLookups = LookupKey.getTPCommandLookup(tpiCode,
        dtiTxn.getTransactionType());
    dtiTxn.setTpLookupTOList(tpLookups);

    // Apply any rules unique to one provider.
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWCreateTicketRules.applyWDWCreateTicketRules(dtiTxn); // as of 2.10
    }

    // RULE: Are the number of products/tickets on the order within tolerance?
    // NOTE: This rule requires provider specific queries to have run before
    // processing
    // to supply the "MaxLimit" lookup.
    TicketRules.validateCreateTicketCount(tktListTO, tpLookups);

    return;
  }

  /**
   * Apply update ticket business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyUpdateTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a UpdateTicketRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != UpdateTicketRequestTO.class) {
      throw new DTIException(BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-update ticket class passed to applyUpdateTicketRules.");
    }

    // Apply any rules unique to one provider.
    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWUpdateTicketRules.applyWDWUpdateTicketRules(dtiTxn);
    }

    return;
  }

  /**
   * Apply update transaction business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyUpdateTransactionRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a UpdateTransactionRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != UpdateTransactionRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-update transaction class passed to applyUpdateTransactionRules.");
    }

    // Apply any rules unique to one provider.
    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWUpdateTransactionRules.applyWDWUpdateTransactionRules(dtiTxn);
    }

    return;
  }

  /**
   * Apply query reservation business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyQueryReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a QueryReservationRequest TO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != QueryReservationRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-query reservation transaction class passed to applyQueryReservationRules.");
    }

    // RULE: Is this type of reservation identifier supported for a query?
    QueryReservationRequestTO queryResReqTO = (QueryReservationRequestTO) dtiTxn
        .getRequest().getCommandBody();
    if ((queryResReqTO.getExternalResCode() != null) || (queryResReqTO
        .getResNumber() != null)) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Query Reservation attempted with illegal reservation identifier type.  Must be ResCode.");
    }

    // Apply any rules unique to one provider.
    String tpiCode = dtiTxn.getTpiCode();
    
    // The only rule, at present, is the payload ID for res code swap
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_HKD) == 0) {
      HKDQueryReservationRules.applyHKDQueryReservationRules(dtiTxn);
    }

    return;
  }

  /**
   * Apply query reservation business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyVoidReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a VoidReservationRequest TO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != VoidReservationRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-void reservation transaction class passed to applyVoidReservationRules.");
    }

    // RULE: Is this type of reservation identifier supported for a query?
    VoidReservationRequestTO queryResReqTO = (VoidReservationRequestTO) dtiTxn
        .getRequest().getCommandBody();
    if ((queryResReqTO.getExternalResCode() != null) ||
        (queryResReqTO.getResNumber() != null)) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Void Reservation attempted with illegal reservation identifier type.  Must be ResCode.");
    }

    // Apply any rules unique to one provider.
    // None at this time.

    return;
  }  
  
  /**
   * Apply upgrade entitlement business rules.
   * 
   * 2.10
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  private static void applyUpgradeEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a UpgradeEntitlementRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != UpgradeEntitlementRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-upgrade entitlement class passed to applyUpgradeEntitlementRules.");
    }

    // RULE: Is the RequestType is "UpgradeEntitlement", as expected?
    UpgradeEntitlementRequestTO upgrdEntReqTO = (UpgradeEntitlementRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // Get database information for all products and tickets on the order
    ArrayList<TicketTO> tktListTO = upgrdEntReqTO.getTicketList();
    ArrayList<PaymentTO> payListTO = upgrdEntReqTO.getPaymentList();
    ArrayList<BigInteger> allowedPdtIdList = new ArrayList<BigInteger>();

    String tpiCode = dtiTxn.getTpiCode();
    EntityTO entityTO = dtiTxn.getEntityTO();

    // ////////
    // Deals with the "TO" product
    // ////////

    ArrayList<DBProductTO> dbProdList = ProductKey.getOrderProducts(
        tktListTO, ValueConstants.TYPE_CODE_SELL);
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbProdList, ValueConstants.TYPE_CODE_SELL));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbProdList, ValueConstants.TYPE_CODE_SELL));

    // RULE: Is the entity allowed to sell all products on the order?
    ProductRules.validateProductsAreSellable(tktListTO, dbProdList,
        ValueConstants.TYPE_CODE_SELL);
    ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
        allowedPdtIdList);

    // RULE: Are the products required to have demographics? (As of 2.15, JTL)
    ProductRules.validateProductsHaveDemographics(tktListTO, dbProdList);

    // Save the dbProduct list to the request for use in later translation.
    dtiTxn.setDbProdList(dbProdList);

    // ////////
    // Deals with the "FROM" product
    // ////////

    // Clear out the ArrayLists
    allowedPdtIdList.clear();

    ArrayList<DBProductTO> dbUpgrdProdList = ProductKey.getOrderProducts(
        tktListTO, ValueConstants.TYPE_CODE_UPGRADE);
    ProductRules.validateUpgradeProducts(dbUpgrdProdList); // This is essentially an "is null" check.
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbUpgrdProdList, ValueConstants.TYPE_CODE_UPGRADE));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbUpgrdProdList, ValueConstants.TYPE_CODE_UPGRADE));

    // RULE: Is the entity allowed to upgrade all products on the order?
    ProductRules.validateEntityCanUpgradeProducts(entityTO,
        dbUpgrdProdList, allowedPdtIdList);

    // RULE: Are the number of products/tickets on the order within tolerance?
    TicketRules.validateMaxEightTicketsOnRequest(tktListTO);

    // RULE: Are the products being sold at an acceptable price?
    // NOTE: This rule can modify the tktListTO members to indicate price
    // mismatch.
    ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);

    // RULE: Are the "FROM" prices either the Unit or Standard Retail Price (as of 2.12)
    ProductRules.validateUpgradeProductPrice(entityTO, tktListTO,
        dbUpgrdProdList);

    // RULE: Are the mapped ticket types valid and active?
    // NOTE: This rule modifies the dbProdList members to include ticket
    // mappings.
    dbProdList = ProductKey.getProductTicketTypes(dbProdList);
    ProductRules.validateMappedProviderTicketTypes(dbProdList);

    // RULE: Do the various payments, cover the products on upgrade?
    PaymentRules.validatePaymentsOnUpgrade(tktListTO, payListTO, entityTO);

    // RULE: Are payments and payment types acceptable?
    PaymentRules.validatePaymentComposition(payListTO, tpiCode);

    // Apply any rules unique to one provider.
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWUpgradeEntitlementRules.applyWDWUpgradeEntitlementRules(dtiTxn);
    }

    return;
  }

  private static void applyAssociateMediaToAccountRules(
      DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this an AssociateMediaToAccountRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != AssociateMediaToAccountRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-AssociateMedia class passed to applyAssociateMediaToAccountRules.");
    }

    AssociateMediaToAccountRequestTO associateMediaReq = (AssociateMediaToAccountRequestTO) commandBody;

    // Media Data Rules
    ArrayList<NewMediaDataTO> aMediaList = associateMediaReq.getMediaData();

    // RULE: Is there at least one media on the request?
    MediaRules.validateMinOneMediaOnRequest(aMediaList);

    // RULE: Is there no more than 250 media on the request?
    MediaRules.validateMax250MediaOnRequest(aMediaList);

    // Apply any rules unique to one provider.
    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) {
      WDWAssociateMediaToAccountRules
          .applyWDWAssociateMediaToAccountRules(dtiTxn);
    }
    return;
  }

  private static void applyTickerateEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a TickerateEntitlementRequestTO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != TickerateEntitlementRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-TickerateEntitlement class passed to applyTickerateEntitlementRules.");
    }

    TickerateEntitlementRequestTO tickerateEntitlementReq = (TickerateEntitlementRequestTO) commandBody;

    // Ticket Rules
    ArrayList<TicketTO> aTicketList = tickerateEntitlementReq.getTickets();

    // RULE: Is there at least one ticket on the request?
    TicketRules.validateMinOneTicketOnRequest(aTicketList);

    // Media Data Rules
    ArrayList<NewMediaDataTO> aMediaList = tickerateEntitlementReq
        .getMediaData();

    if ((aMediaList != null) && (aMediaList.size() > 0)) {
      // RULE: Is there no more than 250 media on the request?
      MediaRules.validateMax250MediaOnRequest(aMediaList);
    }

    return;
  }

  /**
   * Apply renew entitlement business rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void applyRenewEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

    // RULE: Is this a RenewEntitlementRequest TO?
    CommandBodyTO commandBody = dtiTxn.getRequest().getCommandBody();
    if (commandBody.getClass() != RenewEntitlementRequestTO.class) {
      throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.DTI_PROCESS_ERROR,
          "Internal Error:  Non-renew entitlement transaction class passed to applyRenewEntitlementRules.");
    }

    // RULE: Is the RequestType is "RenewEntitlement", as expected?
    RenewEntitlementRequestTO renewEntReqTO = (RenewEntitlementRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // Get database information for all products and tickets on the order
    ArrayList<TicketTO> tktListTO = renewEntReqTO.getTktList();
    ArrayList<DBProductTO> dbProdList = ProductKey
        .getOrderProducts(tktListTO);

    // RULE: Are all products on order known to the database?
    // RULE: Are all products on the order active?
    // RULE: Are any of the products on the order sold out?
    // RULE: Can all of the products on the order be sold today?
    ProductRules.validateProductsAreSellable(tktListTO, dbProdList);

    // RULE: Is the entity allowed to sell all products on the order?
    EntityTO entityTO = dtiTxn.getEntityTO();
    ArrayList<BigInteger> allowedPdtIdList = new ArrayList<BigInteger>();
    allowedPdtIdList.addAll(EntityKey.getEntityProducts(entityTO,
        dbProdList));
    allowedPdtIdList.addAll(EntityKey.getEntityProductGroups(entityTO,
        dbProdList));
    ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
        allowedPdtIdList);

    // RULE: Are the products being sold at an acceptable price?
    boolean isTaxExempt = false; // For renewals, tax exempt status is not supported.
    dtiTxn.setPriceMismatch(ProductRules.validateProductPrice(entityTO,
        tktListTO, dbProdList, isTaxExempt));

    // RULE: Are the products required to have demographics? (As of 2.9)
    ProductRules.validateProductsHaveDemographics(tktListTO, dbProdList);

    // RULE: Is special eligibility required to sell these products?
    if (EligibilityRules.eligibilityRequired(dbProdList)) {
      String eligGrpCode = renewEntReqTO.getEligibilityGroup();
      String eligMemberID = renewEntReqTO.getEligibilityMember();
      Boolean eligFlag = EligibilityKey.getOrderEligibility(dbProdList,
          eligGrpCode, eligMemberID);
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode,
          eligMemberID);
    }

    // RULE: Are validity dates required?
    TicketRules
        .validateTicketValidityDates(tktListTO, entityTO, dbProdList);

    // RULE: Are the mapped ticket types valid and active?
    // NOTE: The getProductTicketTypes query modifies the dbProdList members to
    // include ticket mappings.
    dbProdList = ProductKey.getProductTicketTypes(dbProdList);
    ProductRules.validateMappedProviderTicketTypes(dbProdList);

    // RULE: Do the various payments, if provided, cover the products on the
    // order?
    ArrayList<PaymentTO> payListTO = renewEntReqTO.getPaymentList();
    BigDecimal totalOrderAmount = PaymentRules.validatePaymentsOnOrder(
        tktListTO, payListTO, entityTO);
    renewEntReqTO.setTotalOrderAmount(totalOrderAmount);

    // RULE: Are payments and payment types acceptable?
    String tpiCode = dtiTxn.getTpiCode();
    PaymentRules.validatePaymentComposition(payListTO, tpiCode);

    // Save the dbProduct list to the request for use in later translation.
    dtiTxn.setDbProdList(dbProdList);

    // Apply any rules unique to one provider.
    if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_DLR) == 0) DLRRenewEntitlementRules
        .applyDLRRenewEntitlementRules(dtiTxn);
    else if (tpiCode.compareTo(DTITransactionTO.TPI_CODE_WDW) == 0) WDWRenewEntitlementRules
        .applyWDWRenewEntitlementRules(dtiTxn);

    return;
  }

}
