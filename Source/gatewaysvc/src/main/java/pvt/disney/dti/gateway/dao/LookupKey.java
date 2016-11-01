package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which handles getting the payment lookup from the database.
 * 
 * @author lewit019
 * 
 */
public class LookupKey {

  // Constants
  /** The class instance used for logging. */
  private static final LookupKey THISINSTANCE = new LookupKey();

  /** The event logger. */
  private static final EventLogger logger = EventLogger
      .getLogger(LookupKey.class.getCanonicalName());

  /** The constant value representing the get payment lookup query. */
  private static final String GET_PMT_LOOKUP = "GET_PMT_LOOKUP";

  /**
   * The constant value representing the get ticket provider command lookup query.
   */
  private static final String GET_TP_CMD_LOOKUP = "GET_TP_CMD_LOOKUP";

  /**
   * The constant value representing the get gateway eGalaxy ticket provider lookup
   */
  private static final String GET_GW_TP_CMD_LOOKUP = "GET_GW_TP_CMD_LOOKUP";

  /** The constant value representing the get simple TP lookup (key value) query */
  private static final String GET_SIMPLE_TPLOOKUP = "GET_SIMPLE_TPLOOKUP";

  /** The constant value representing the get void code query. */
  private static final String GET_VOID_CODE = "GET_VOID_CODE";

  /** The constant value representing a reservation command code. */
  private static final String CMD_CODE_RESERVATION = "Reservation";

  /** The constant value representing a void ticket command code. */
  private static final String CMD_CODE_VOIDTICKET = "VoidTicket";

  /** The constant value representing a query ticket command code. */
  private static final String CMD_CODE_QUERYTICKET = "QueryTicket";

  /** The constant value representing a upgrade alpha command code. */
  private static final String CMD_CODE_UPGRADEALPHA = "UpgradeAlpha";

  /** The constant value representing a upgrade entitlement command code. */
  private static final String CMD_CODE_UPGRADEENTITLEMENT = "UpgradeEntitlement";

  /** The constant value representing a update transaction command code. */
  private static final String CMD_CODE_UPDATETRANSACTION = "UpdateTransaction";

  /** The constant value representing a update ticket command code. */
  private static final String CMD_CODE_UPDATETICKET = "UpdateTicket";

  /** The constant value representing a create ticket command code. */
  private static final String CMD_CODE_CREATETICKET = "CreateTicket";

  /** The constant value representing a create ticket command code. */
  private static final String CMD_CODE_RENEWENTITLELMENT = "RenewEntitlement";

  /** The constant value representing an associate media to account command code. */
  private static final String CMD_ASSOCIATE_MEDIA_TO_ACCOUNT = "AssocMediaToAccount";

  /** The constant value representing a tickerate entitlement command code. */
  private static final String CMD_TICKERATE_ENTITLEMENT = "TickerateEntitlement";

  /** The constant indicating a pre-sale sales type. */
  private static final String RES_SALES_TYPE_PRESALE = "Presale";

  /**
   * The constant indicating a ManualMailOrder sales type. Treated like a Pre-sale
   */
  private static final String RES_SALES_TYPE_MANUALMAILORDER = "ManualMailOrder";

  /**
   * The constant indicating an ElectronicEntitlement sales type. Treated like mail order.
   */
  private static final String RES_SALES_TYPE_ELECTRONICENTL = "ElectronicEntitlement";

  /** The constant indicating a pickup type of MailOrder. */
  private static final String RES_PICKUP_TYPE_MAILORDER = "MailOrder";

  /** The constant indicating a pickup type of WillCall. */
  private static final String RES_PICKUP_TYPE_WILLCALL = "WillCall";

  /** The constant indicating a pickup type of Electronic Entitlement. */
  private static final String RES_PICKUP_TYPE_ELECTRONICENTL = "ElectronicEntitlement";

  /**
   * Constructor for LookupKey
   */
  private LookupKey() {
    super();
  }

  /**
   * Returns an array list of PaymentLookup information.
   * 
   * @param commandCode
   *            The command used in this transaction.
   * @param ticketProviderId
   *            The ticket provider Id used in this transaction.
   * @param entityId
   *            The entity ID used in this transaction.
   * @param actor
   *            The actor passed in the transaction.
   * @return An array of payment lookup information.
   * @throws DTIException
   *             on a DB or DAO error.
   */
  @SuppressWarnings("unchecked")
  public static final ArrayList<PaymentLookupTO> getPaymentLookup(
      String commandCode, String ticketProviderId, long entityId,
      String actor) throws DTIException {

    logger.sendEvent("Entering getPaymentLookup()", EventType.DEBUG,
        THISINSTANCE);

    ArrayList<PaymentLookupTO> result = null;

    // Retrieve and validate the parameters
    if ((commandCode == null) || (ticketProviderId == null) || (actor == null) || (entityId <= 0)) {
      throw new DTIException(LookupKey.class,
          DTIErrorCode.INVALID_ENTITY,
          "Insufficient parameters to execute getPaymentLookup.");
    }

    // Retrieve and validate the parameters
    Long entityIdLong = Long.valueOf(entityId);
    Object[] values = { commandCode, ticketProviderId, actor, entityIdLong };

    try {

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_PMT_LOOKUP);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_PMT_LOOKUP",
          EventType.DEBUG, THISINSTANCE);
      result = (ArrayList<PaymentLookupTO>) helper.processQuery(values);

      // Debug
      logger.sendEvent("getPaymentLookup with Actor found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getPaymentLookup: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(LookupKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getPaymentLookup", e);
    }

    return result;
  }

  /**
   * This returns only the "MaxLimit" or default TP Lookups.
   * 
   * @since 2.9
   * @param tpiCode
   * @param txnType
   * @return
   * @throws DTIException
   */
  public static final ArrayList<TPLookupTO> getTPCommandLookup(
      String tpiCode, DTITransactionTO.TransactionType txnType) throws DTIException {

    return getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
        "NONE");

  }

  /**
   * Returns an array of TP Command Lookup values
   * 
   * @param tpiCode
   *            The ticket provider interface code.
   * @param txnType
   *            The transaction type being attempted.
   * @return an array of TP Command Lookup values
   * @throws DTIException
   *             on a DB or DAO error.
   */
  @SuppressWarnings("unchecked")
  public static final ArrayList<TPLookupTO> getTPCommandLookup(
      String tpiCode, DTITransactionTO.TransactionType txnType,
      String language, String clientType, String resPickupArea,
      String resSalesType) throws DTIException {

    logger.sendEvent("Entering getTPCommandLookup()", EventType.DEBUG,
        THISINSTANCE);

    ArrayList<TPLookupTO> result = null;

    // Retrieve and validate the parameters
    if ((tpiCode == null) || (txnType == null)) {
      throw new DTIException(LookupKey.class,
          DTIErrorCode.INVALID_ENTITY,
          "Insufficient parameters to execute getTPCommandLookup.");
    }

    String commandCode = null;

    switch (txnType) {

    case QUERYTICKET:
      commandCode = CMD_CODE_QUERYTICKET;
      break;
    case VOIDTICKET:
      commandCode = CMD_CODE_VOIDTICKET;
      break;
    case UPGRADEALPHA:
      commandCode = CMD_CODE_UPGRADEALPHA;
      break;
    case UPGRADEENTITLEMENT: // 2.10
      commandCode = CMD_CODE_UPGRADEENTITLEMENT;
      break;
    case RESERVATION:
      commandCode = CMD_CODE_RESERVATION;
      break;
    case UPDATETICKET:
      commandCode = CMD_CODE_UPDATETICKET;
      break;
    case UPDATETRANSACTION:
      commandCode = CMD_CODE_UPDATETRANSACTION;
      break;
    case CREATETICKET:
      commandCode = CMD_CODE_CREATETICKET;
      break;
    case RENEWENTITLEMENT:
      commandCode = CMD_CODE_RENEWENTITLELMENT;
      break;

    case ASSOCIATEMEDIATOACCOUNT:
      commandCode = CMD_ASSOCIATE_MEDIA_TO_ACCOUNT;
      break;

    case TICKERATEENTITLEMENT:
      commandCode = CMD_TICKERATE_ENTITLEMENT;
      break;

    default:
      break;

    }

    // This conditional logic and hard-coding was in the old gateway,
    // and is mimicked here for compatibility.
    String pickupType;
    if (RES_SALES_TYPE_MANUALMAILORDER.compareToIgnoreCase(resSalesType) == 0 || resSalesType
        .compareToIgnoreCase(RES_SALES_TYPE_PRESALE) == 0) {
      pickupType = RES_PICKUP_TYPE_WILLCALL;
    }
    else if (RES_SALES_TYPE_ELECTRONICENTL
        .compareToIgnoreCase(resSalesType) == 0) {
      pickupType = RES_PICKUP_TYPE_ELECTRONICENTL;
    }
    else {
      pickupType = RES_PICKUP_TYPE_MAILORDER;
    }

    // Retrieve and validate the parameters
    Object[] values = { tpiCode,
        commandCode,
        language,
        clientType,
        resSalesType,
        pickupType,
        resPickupArea };

    try {

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_TP_CMD_LOOKUP);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_TP_CMD_LOOKUP",
          EventType.DEBUG, THISINSTANCE);
      result = (ArrayList<TPLookupTO>) helper.processQuery(values);

      // Debug
      logger.sendEvent("getTPCommandLookup with Actor found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getTPCommandLookup: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(LookupKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getPaymentLookup", e);
    }

    return result;
  }

  /**
   * Returns an array of TP Command Lookup values
   * 
   * @param tpiCode
   *            The ticket provider interface code.
   * @param txnType
   *            The transaction type being attempted.
   * @return an array of TP Command Lookup values
   * @throws DTIException
   *             on a DB or DAO error.
   */
  public static final String getSimpleTPLookup(Integer ticketProviderId,
      String lookup_key, String lookup_type) throws DTIException {

    logger.sendEvent("Entering getSimpleTPLookup()", EventType.DEBUG,
        THISINSTANCE);

    String result = null;

    // Retrieve and validate the parameters
    if ((lookup_key == null) || (lookup_type == null)) {
      throw new DTIException(LookupKey.class,
          DTIErrorCode.UNDEFINED_FAILURE,
          "Insufficient parameters to execute getSimpleTPLookup.");
    }

    // Retrieve and validate the parameters
    Object[] values = { lookup_type, lookup_key, ticketProviderId };

    try {

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_SIMPLE_TPLOOKUP);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_SIMPLE_TPLOOKUP",
          EventType.DEBUG, THISINSTANCE);
      result = (String) helper.processQuery(values);

      // Debug
      logger.sendEvent("getSimpleTPLookup found results",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getSimpleTPLookup: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(LookupKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getSimpleTPLookup", e);
    }

    return result;
  }

  /**
   * Returns an array list of PaymentLookup information.
   * 
   * @param commandCode
   *            The command used in this transaction.
   * @param ticketProviderId
   *            The ticket provider Id used in this transaction.
   * @param entityId
   *            The entity ID used in this transaction.
   * @param actor
   *            The actor passed in the transaction.
   * @return An array of payment lookup information.
   * @throws DTIException
   *             on a DB or DAO error.
   */
  public static final Integer getVoidLookup(String statusValue,
      String ticketProviderId) throws DTIException {

    logger.sendEvent("Entering getVoidLookup()", EventType.DEBUG,
        THISINSTANCE);

    Integer result = null;

    // Retrieve and validate the parameters
    if ((statusValue == null) || (ticketProviderId == null)) {
      throw new DTIException(LookupKey.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Insufficient parameters to execute getVoidLookup.");
    }

    // Retrieve and validate the parameters
    Object[] values = { statusValue, ticketProviderId };

    try {

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_VOID_CODE);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_VOID_CODE",
          EventType.DEBUG, THISINSTANCE);
      result = (Integer) helper.processQuery(values);

      // Debug
      logger.sendEvent("getVoidLookup found results.", EventType.DEBUG,
          THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getVoidLookup: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(LookupKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getVoidLookup", e);
    }

    return result;
  }

  /**
   * 
   * Returns an array of gateway eGalaxy TP Command Lookup values
   * 
   * @param tpiCode
   *            The ticket provider interface code.
   * @param txnType
   *            The transaction type being attempted.
   * @return an array of TP Command Lookup values
   * @throws DTIException
   *             on a DB or DAO error.
   */
  @SuppressWarnings("unchecked")
  public static final ArrayList<TPLookupTO> getGWTPCommandLookup(
      String tpiCode, DTITransactionTO.TransactionType txnType,
      String shipMethod, String shipDetail) throws DTIException {

    logger.sendEvent("Entering getGWTPCommandLookup()", EventType.DEBUG,
        THISINSTANCE);

    ArrayList<TPLookupTO> result = null;

    // Retrieve and validate the parameters
    if ((tpiCode == null) || (txnType == null) || (shipMethod == null) || (shipDetail == null)) {
      throw new DTIException(LookupKey.class,
          DTIErrorCode.INVALID_ENTITY,
          "Insufficient parameters to execute getGWTPCommandLookup.");
    }

    String commandCode = null;

    switch (txnType) {

    case QUERYTICKET:
      commandCode = CMD_CODE_QUERYTICKET;
      break;
    case VOIDTICKET:
      commandCode = CMD_CODE_VOIDTICKET;
      break;
    case UPGRADEALPHA:
      commandCode = CMD_CODE_UPGRADEALPHA;
      break;
    case UPGRADEENTITLEMENT: // 2.10
      commandCode = CMD_CODE_UPGRADEENTITLEMENT;
      break;
    case RESERVATION:
      commandCode = CMD_CODE_RESERVATION;
      break;
    case UPDATETICKET:
      commandCode = CMD_CODE_UPDATETICKET;
      break;
    case UPDATETRANSACTION:
      commandCode = CMD_CODE_UPDATETRANSACTION;
      break;
    case CREATETICKET:
      commandCode = CMD_CODE_CREATETICKET;
      break;
    case RENEWENTITLEMENT: // as of 2.16.1, JTL
      commandCode = CMD_CODE_RENEWENTITLELMENT;
      break;

    case ASSOCIATEMEDIATOACCOUNT:
      commandCode = CMD_ASSOCIATE_MEDIA_TO_ACCOUNT;
      break;

    case TICKERATEENTITLEMENT:
      commandCode = CMD_TICKERATE_ENTITLEMENT;
      break;

    default:
      break;

    }

    // Retrieve and validate the parameters
    Object[] values = { tpiCode, commandCode, shipMethod, shipDetail };

    try {

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_GW_TP_CMD_LOOKUP);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_GW_TP_CMD_LOOKUP",
          EventType.DEBUG, THISINSTANCE);
      result = (ArrayList<TPLookupTO>) helper.processQuery(values);

      // Debug
      logger.sendEvent(
          "getGWTPCommandLookup with Actor found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getGWTPCommandLookup: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(LookupKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getPaymentLookup", e);
    }

    return result;
  }

}
