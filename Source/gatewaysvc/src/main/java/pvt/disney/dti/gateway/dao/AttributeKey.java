package pvt.disney.dti.gateway.dao;

import java.math.BigInteger;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which handles the queries for entity and ticket attributes.
 * 
 * @author lewit019
 * 
 */
public class AttributeKey {

  // Constants
  /** Object instance used for logging. */
  private static final AttributeKey THISINSTANCE = new AttributeKey();

  /** Event logger. */
  private static final EventLogger logger = EventLogger
      .getLogger(AttributeKey.class.getCanonicalName());

  /** Constant value for get entity attributes with actor query. */
  private static final String GET_ENT_ATTR_ACTOR = "GET_ENT_ATTR_ACTOR";

  /** Constant value for get entity attributes with no actor query. */
  private static final String GET_ENT_ATTR_NOACTOR = "GET_ENT_ATTR_NOACTOR";

  /** Constant value for get Walt Disney World ticket attributes. */
  private static final String GET_WDWTKT_ATTR = "GET_WDWTKT_ATTR";

  // Key Strings required for this query.
  /** Constant value indicating query ticket XML text. */
  private static final String QT_DBSTRING = "QueryTicket";

  /** Constant value indicating upgrade alpha XML text. */
  private static final String UA_DBSTRING = "UpgradeAlpha";

  /** Constant value indicating void ticket XML text. */
  private static final String VT_DBSTRING = "VoidTicket";

  /** Constant value indicating reservation XML text. */
  private static final String RES_DBSTRING = "Reservation";

  /** Constant value indicating create ticket XML text. */
  private static final String CT_DBSTRING = "CreateTicket";

  /** Constant value indicating update ticket XML text. */
  private static final String UTKT_DBSTRING = "UpdateTicket";

  /** Constant value indicating update transaction XML text. */
  private static final String UTXN_DBSTRING = "UpdateTransaction";

  /** Constant value indicating upgrade entitlement XML text. */
  private static final String UENT_DBSTRING = "UpgradeEntitlement";

  /** Constant value indicating renew entitlement XML text. */
  private static final String RENT_DBSTRING = "RenewEntitlement";

  /** Constant value indicating query reservation XML text. */
  private static final String QR_DBSTRING = "QueryReservation";
  
  /** Constant value indicating associate media XML text. */
  private static final String AM_DBSTRING = "AssocMediaToAccount"; // 2.16.1 BIEST001 (Charles defined this as AssocMediaToAccount in database)

  /** Constant value indicating tickerate entitlement XML text. */
  private static final String TE_DBSTRING = "TickerateEntitlement"; // 2.16.1 BIEST001
  
  /** Constant value indicating void reservation XML text. */
  private static final String VR_DBSTRING = "VoidReservation"; // 2.16.3, JTL

  /**
   * Constructor for AttributeKey
   */
  private AttributeKey() {
    super();
  }

  /**
   * Gets the entity attributes, when an actor is provided.
   * 
   * @param dtiTxn
   *            the DTI Transaction Transfer Object
   * @param tpiCode
   *            Ticket Provider Indicator code
   * @param entityId
   *            the numerical key value of the entity in the database
   * @param actor
   *            the string for the actor (MGR or SYS for example).
   * @return The array list of command attributes
   * @throws DTIException
   *             on a DB or DAO problem.
   */
  @SuppressWarnings("unchecked")
  public static final HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> getEntAttribtues(
      DTITransactionTO dtiTxn, String tpiCode, long entityId, String actor) throws DTIException {

    logger.sendEvent("Entering getEntAttribtues()", EventType.DEBUG,
        THISINSTANCE);

    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> result = null;

    // Retrieve and validate the parameters
    if ((dtiTxn == null) || (tpiCode == null) || (actor == null) || (entityId <= 0)) {
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.INVALID_ENTITY,
          "Insufficient parameters to execute getEntAttribtues.");
    }

    try {

      String commandCode = getCommandCodeString(dtiTxn);

      // Retrieve and validate the parameters
      Long entityIdLong = Long.valueOf(entityId);
      Object[] values = { commandCode, tpiCode, actor, entityIdLong };

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_ENT_ATTR_ACTOR);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_ENT_ATTR_ACTOR",
          EventType.DEBUG, THISINSTANCE);
      result = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) helper
          .processQuery(values);

      // Debug
      logger.sendEvent("getEntAttribtues with Actor found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getEntAttribtues: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getEntAttributes", e);
    }

    return result;
  }

  /**
   * Gets the entity attributes when no actor is provided.
   * 
   * @param dtiTxn
   *            the DTI Transaction Transfer Object
   * @param tpiCode
   *            Ticket Provider Indicator code
   * @param entityId
   *            the numerical key value of the entity in the database
   * @return The array list of command attributes
   * @throws DTIException
   *             on a DB or DAO problem.
   */
  @SuppressWarnings("unchecked")
  public static final HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> getEntAttribtues(
      DTITransactionTO dtiTxn, String tpiCode, long entityId) throws DTIException {

    logger.sendEvent("Entering getEntAttribtues()", EventType.DEBUG,
        THISINSTANCE);

    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> result = null;

    // Retrieve and validate the parameters
    if ((dtiTxn == null) || (tpiCode == null) || (entityId <= 0)) {
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.INVALID_ENTITY,
          "Insufficient parameters to execute getEntAttribtues.");
    }

    try {

      String commandCode = getCommandCodeString(dtiTxn);

      // Retrieve and validate the parameters
      Long entityIdLong = new Long(entityId);
      Object[] values = { commandCode, tpiCode, entityIdLong };

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_ENT_ATTR_NOACTOR);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_ENT_ATTR_NOACTOR",
          EventType.DEBUG, THISINSTANCE);
      result = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) helper
          .processQuery(values);

      // Debug
      logger.sendEvent(
          "getEntAttribtues with no Actor found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getEntAttribtues: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getEntAttributes", e);
    }

    return result;
  }

  /**
   * Determines the correct DBString to use for a particular query.
   * 
   * @param dtiTxn
   *            The DTI Transaction Transfer Object
   * @return the command code string
   */
  private static final String getCommandCodeString(DTITransactionTO dtiTxn) {

    String commandCode = null;
    TransactionType requestType = dtiTxn.getTransactionType();

    switch (requestType) {

    case QUERYTICKET:
      commandCode = QT_DBSTRING;
      break;

    case UPGRADEALPHA:
      commandCode = UA_DBSTRING;
      break;

    case VOIDTICKET:
      commandCode = VT_DBSTRING;
      break;

    case RESERVATION:
      commandCode = RES_DBSTRING;
      break;

    case CREATETICKET:
      commandCode = CT_DBSTRING;
      break;

    case UPDATETICKET:
      commandCode = UTKT_DBSTRING;
      break;

    case UPDATETRANSACTION:
      commandCode = UTXN_DBSTRING;
      break;

    case QUERYRESERVATION:
      commandCode = QR_DBSTRING;
      break;

    case UPGRADEENTITLEMENT: // 2.10
      commandCode = UENT_DBSTRING;
      break;

    case ASSOCIATEMEDIATOACCOUNT: // 2.16.1 BIEST001
      commandCode = AM_DBSTRING;
      break;

    case TICKERATEENTITLEMENT: // 2.16.1 BIEST001
      commandCode = TE_DBSTRING;
      break;

    case RENEWENTITLEMENT: // as of 2.16.1, JTL
      commandCode = RENT_DBSTRING;
      break;
      
    case VOIDRESERVATION: // as of 2.16.3, JTL
      commandCode = VR_DBSTRING;
      break; 
    case QUERYELIGIBLEPRODUCTS: // TODO As a part of new AP Upgrade Service. Need to add entry for queryEligibleProducts in DB
      commandCode = QT_DBSTRING; // right now keeping the entry for queryTicket
     break;

    default:
      commandCode = new String("");

    }

    return commandCode;
  }

  /**
   * Gets the Walt Disney World Ticket Attributes.
   * 
   * @param ticketNumber
   *            the ticket identifier
   * @return the ticket attributes from the database.
   * @throws DTIException
   *             on any DB or process failure.
   */
  public static final DBTicketAttributes getWDWTicketAttributes(
      BigInteger ticketNumber) throws DTIException {

    logger.sendEvent("Entering getWDWTicketAttributes()", EventType.DEBUG,
        THISINSTANCE);

    DBTicketAttributes result = null;

    // Retrieve and validate the parameters
    if (ticketNumber == null) {
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "No ticketNumber provided to getWDWTicketAttributes");
    }

    try {

      // Retrieve and validate the parameters
      Long aLongNumber = Long.valueOf(ticketNumber.longValue());
      Object[] values = { aLongNumber };

      // Prepare query
      logger.sendEvent("About to getInstance from DAOHelper",
          EventType.DEBUG, THISINSTANCE);
      DAOHelper helper = DAOHelper.getInstance(GET_WDWTKT_ATTR);

      // Run the SQL
      logger.sendEvent("About to processQuery:  GET_WDWTKT_ATTR",
          EventType.DEBUG, THISINSTANCE);
      result = (DBTicketAttributes) helper.processQuery(values);

      // Debug
      logger.sendEvent("getWDWTicketAttributes found attributes.",
          EventType.DEBUG, THISINSTANCE, result, null);

    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing getWDWTicketAttributes: " + e
              .toString(), EventType.WARN, THISINSTANCE);
      throw new DTIException(AttributeKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing getWDWTicketAttributes", e);
    }

    return result;
  }

}
