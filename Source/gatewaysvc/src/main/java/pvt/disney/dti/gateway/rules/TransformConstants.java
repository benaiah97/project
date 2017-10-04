package pvt.disney.dti.gateway.rules;

/**
 * The Interface TransformConstants.
 * 
 * @author lewit019, moons012
 */
public interface TransformConstants {

  /** YES. */
  String YES = "YES";

  /** NO. */
  String NO = "NO";

  /** VOIDABLE. */
  String VOIDABLE = "Voidable";

  /** ACTIVE. */
  String ACTIVE = "Active";

  /** BLOCKED. */
  String BLOCKED = "Blocked";

  /** Constant for Redeemable" */
  String REDEEMABLE = "Redeemable";

  /** ANNUA l_ attr. */
  String ANNUAL_ATTR = "ANNUAL  ";

  /** Pass Type attribute */
  String PASS_KIND = "PassKind";

  /** Pass Renew attribute */
  String PASS_RENEW = "PassRenew";

  /** Parking db attribute */
  String PARKING = "PARKING";

  /** ITE m_1. */
  String ITEM_1 = "1";
  
  /** The picture. */
  String PICTURE="PICTURE";
  
  /** The paylan. */
  String PAYPLAN="PAYPLAN";

  // GW PREFIX INDICATES GATEWAY EGALAXY (DLR) CONSTANTS

  /** Command for GW, used to specify a TicketActivation is a void */
  String GW_COMMAND_CANCEL = "Cancel";

  /**
   * Command for GW , used to specify a TicketActivation is an activate (upgrade alpha)
   */
  String GW_COMMAND_ACTIVATE = "Activate";

  /** The GW_QRY_TKT_MSG_TYPE. */
  String GW_QRY_TKT_MSG_TYPE = "QueryTicket"; // to keep GWEnvelopTO happy

  /** The GW_QRY_ORD_MSG_TYPE. */
  String GW_QRY_ORD_MSG_TYPE = "QueryOrder"; // to keep GWEnvelopTO happy

  /** The GW_TKT_ACTIVATE_TYPE. */
  String GW_TKT_ACTIVATE_TYPE = "ActivateTicket";// to keep GWEnvelopTO happy

  /** The GW_TKT_ACTIVATE_MSG_TYPE. */
  String GW_TKT_ACTIVATE_MSG_TYPE = "ActivateTicketResponse";

  /** The GW_ORDERS_MESSAGE_TYPE. */
  String GW_ORDERS_MESSAGE_TYPE = "ORDERS";

  /** The GW_ORDERS_RESPONSE_MESSAGE_TYPE */
  String GW_ORDERS_RESPONSE_MESSAGE_TYPE = "SetOrderStatus";

  /** The GW_QUERY_ORDER_RESPONSE_MESSAGE_TYPE */
  String GW_QUERY_ORDER_RESPONSE_MESSAGE_TYPE = "QueryOrderResponse";

  /** GW_ORDERS_RESP_MESSAGE_TYPE */
  String GW_ORDERS_RESP_MESSAGE_TYPE = "OrdersResponse";

  /** GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE = "4" (as of 2.16.1, JTL) */
  String GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE = "4";

  /** GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL = "1" (as of 2.16.1, JTL) */
  Integer GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL = new Integer("1");

  /** GW_ORDERS_CONTRACT_RENEW_CONTRACT = "False" (as of 2.16.1, JTL) */
  Boolean GW_ORDERS_CONTRACT_RENEW_CONTRACT = new Boolean("False");
  
  /** GW_ORDERS_CONTRACT_UPGRADE_CONTRACT = "False" (as of 2.16.1, JTL) */
  Boolean GW_ORDERS_CONTRACT_UPGRADE_CONTRACT = new Boolean("True");

  /** GW_ORDERS_CONTRACT_STATUS_ID = "1" (as of 2.16.1, JTL) */
  String GW_ORDERS_CONTRACT_STATUS_ID = "1";

  /** GW_ORDERS_CONTRACT_CONTACT_METHOD = "0" (as of 2.16.1, JTL) */
  String GW_ORDERS_CONTRACT_CONTACT_METHOD = "0";

  /** California Resident */
  String CA_RES = "CA_RES";

  /** Southern California Resident */
  String SOCA_RES = "SOCA_RES";

  /** Gateway orders Sales Program Database Key (for TP Lookup) as of 2.16.1, JTL */
  String GW_ORDERS_SALESPROGRAM_DBKEY = "SalesProgram";

  /** GW_ORDERS_SOCAPURCHPLAN - Description Value for Southern California Purchase Plan */
  String GW_ORDERS_SOCAPURCHPLAN = "SOCAPurchPlan";

  /** GW_ORDERS_SOCARENEWPLAN - Description Value for Southern California Renewal Plan */
  String GW_ORDERS_SOCARENEWPLAN = "SOCARenewPlan";

  /** GW_ORDERS_CAPURCHPLAN - Description Value for California Purchase Plan */
  String GW_ORDERS_CAPURCHPLAN = "CAPurchPlan";

  /** GW_ORDERS_CARENEWPLAN - Description Value for California Renewal Plan */
  String GW_ORDERS_CARENEWPLAN = "CARenewPlan";

  /** The GW_QRY_TKT_RSP_MSG_TYPE. */
  String GW_QRY_TKT_RSP_MSG_TYPE = "QueryTicketResponse";

  /** The GW_QRY_TKT_RSP_MSG_TYPE. */
  String GW_VOID_TKT_RSP_MSG_TYPE = "VoidTicketResponse";

  /** gateway galaxy order line item type for ticket product */
  String GW_ORDERS_TICKET_ORDER_LINE_ITEM = "1";

  /** gateway galaxy order line item type for payments */
  String GW_ORDERS_PAYMENT_ORDER_LINE_ITEM = "2";

  /** gateway galaxy order line item type for ticket product */
  String GW_ORDERS_PASS_ORDER_LINE_ITEM = "8";
  
  /** gateway galaxy order line item type for ticket Upgrade TO DO */
  String GW_ORDERS_UPGRADE_ORDER_LINE_ITEM = "16";

  /** gateway galaxy order status field for unpaid orders, value 1 */
  String GW_ORDERS_UNPAID_ORDER_STATUS = "1";

  /** gateway galaxy order status field for paid orders, value 2 */
  String GW_ORDERS_PAID_ORDER_STATUS = "2";

  /** gateway galaxy order DLR eligibility group constant */
  String GW_ORDERS_DLR_ELIGIBILITY_GROUP = "DLR";

  /** gateway galaxy DLR meetings and conventions eligibility group */
  String GW_ORDERS_DLR_MTGS_CONV_ELIGIBILITY_GROUP = "DLR_MTGS_CONV";

  /** gateway galaxy DLR BOLT eligibility group */
  String GW_ORDERS_DLR_BOLT_GROUP = "BOLT";

  /** gateway galaxy PASS (as configured in DTI's Day_Subclass) */
  String GW_PASS = "PASS";
  
  // need to verify TO DO 
  /** gateway galaxy UPGRADE (ANNUAL PASS) */
  String GW_UPGRADE = "AP";

  /** gateway galaxy PASS (as configured in DTI's Day_Subclass) */
  String GW_RENEW = "RENEW";

  /** gateway galaxy DLR weddings eligibility group */
  String GW_ORDERS_DLR_WEDDINGS_ELIGIBILITY_GROUP = "DLR_WED";

  /** gateway galaxy order gift card order line item (payment) description */
  String GW_ORDERS_ORDER_LINE_ITEM_DESCRIPTION_GIFT_CARD = "GC";

  /** Standard Source ID from all transactions from WDPro */
  String GW_WDPRO = "WDPRONADLR";

  /** Source ID for all WDPro credit card transactions w/o CVV provided */
  String GW_WDPRO_NO_CVV = "WDPRONADLR2";

  /** Auto activation for renewals. */
  String GW_AUTO_ACTIVATION = "Automatic Activation";

  /** Ship delivery code for renewals. */
  String GW_SHIP_DELIVERY = "17";

  // ===============================================
  // OT PREFIX INDICATES OMNITICKET (WDW) CONSTANTS
  // ===============================================

  /** The OT_MANAGE_RES_TYPE. */
  String OT_MANAGE_RES_TYPE = "ManageReservation";

  /** The OT_UPGRADE_TICKET. */
  String OT_UPGRADE_TICKET = "UpgradeTicket";

  /** The OT_VOID_TICKET. */
  String OT_VOID_TICKET = "VoidTicket";

  /** The OT_QUERY_TICKET. */
  String OT_QUERY_TICKET = "QueryTicket";

  /** The OT_UPDATE_TICKET. */
  String OT_UPDATE_TICKET = "UpdateTicket";

  /** The OT_UPDATE_TRANSACTION. */
  String OT_UPDATE_TRANSACTION = "UpdateTransaction";

  /** The OT_CREATE_TRANSACTION. */
  String OT_CREATE_TRANSACTION = "CreateTransaction";

  /** The OT_UPGRADE_ENTITLEMENT. */
  String OT_UPGRADE_ENTITLEMENT = "UpgradeEntitlement"; // 2.10

  /** The OT_MULTI_ENTITLEMENT_ACCOUNT */
  String OT_MULTI_ENTITLEMENT_ACCOUNT = "MultiEntitlementAccount"; // 2.16.1 BIEST001 (used for all NGE Responses)

  /** The OT_UPGRADE_ENTITLEMENT. */
  String OT_RENEW_TICKET = "RenewTicket"; // as of 2.16.1, JTL

}
