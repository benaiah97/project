package pvt.disney.dti.gateway.constants;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.data.AssociateMediaToAccountResponseTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementResponseTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.util.DTIFormatter;

/**
 * This class contains the DTIErrorCode values, and, upon loading, creates a HashMap which is used assign the appropriate scope (Payload, Command, Ticket) based upon the error type. This is critical to DTI, since it's errors don't always
 * appear at the same level in the XML.
 * 
 * @author lewit019
 * 
 */
public class DTIErrorCode implements Serializable {

  /** The scope of the error. */
  public static enum ErrorScope {
    PAYLOAD,
    COMMAND,
    TICKET,
    MEDIA
  }

  /** Standard serialVersionUID. */
  final static long serialVersionUID = 9129231995L;

  /** A hashmap containing all errors, allowing a codeToScope lookup. */
  private static HashMap<String, ErrorScope> codeToScope = new HashMap<String, ErrorScope>();

  // DTIErrorCode Codes
  public final static DTIErrorCode INVALID_MSG_ELEMENT = new DTIErrorCode(
      "300", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Msg Element");
  public final static DTIErrorCode INVALID_MSG_CONTENT = new DTIErrorCode(
      "301", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Msg Content");
  public final static DTIErrorCode INVALID_PAYLOAD_HDR = new DTIErrorCode(
      "302", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Payload Hdr");
  public final static DTIErrorCode INVALID_COMMAND_HDR = new DTIErrorCode(
      "303", DTIErrorCode.ErrorScope.COMMAND, "Invalid Command Hdr");
  public final static DTIErrorCode INVALID_ACCESS_METHOD = new DTIErrorCode(
      "305", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Access Method");
  public final static DTIErrorCode INVALID_GATEWAY_ENDPOINT = new DTIErrorCode(
      "306", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Gateway Endpoint"); // 2.11
  public final static DTIErrorCode UNABLE_TO_COMMUNICATE = new DTIErrorCode(
      "307", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Communicate");
  public final static DTIErrorCode UNABLE_TO_LOG_TRANSACTION_DB = new DTIErrorCode(
      "308", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Log Transaction");
  public final static DTIErrorCode UNABLE_TO_LOG_TRANSACTION_TB = new DTIErrorCode(
      "309", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Log Transaction");
  public final static DTIErrorCode TRANSACTION_TIME_OUT = new DTIErrorCode(
      "310", DTIErrorCode.ErrorScope.PAYLOAD, "Transaction Time-Out");
  public final static DTIErrorCode UNABLE_TO_CONNECT = new DTIErrorCode(
      "311", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Connect");
  public final static DTIErrorCode UNABLE_TO_WRITE = new DTIErrorCode("312",
      DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Write");
  public final static DTIErrorCode UNABLE_TO_READ = new DTIErrorCode("313",
      DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Read");
  public final static DTIErrorCode UNABLE_TO_DISCONNECT = new DTIErrorCode(
      "314", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Disconnect");
  public final static DTIErrorCode INVALID_SELLER_ENTITY = new DTIErrorCode(
      "350", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Entity");
  public final static DTIErrorCode INVALID_TARGET_VERSION = new DTIErrorCode(
      "352", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Target/Version");
  public final static DTIErrorCode FAILED_DB_OPERATION_GATE = new DTIErrorCode(
      "360", DTIErrorCode.ErrorScope.PAYLOAD, "Failed DB Operation");
  public final static DTIErrorCode PRICE_MISMATCH_WARNING = new DTIErrorCode(
      "401", DTIErrorCode.ErrorScope.COMMAND, "Price Mismatch-Warning");
  public final static DTIErrorCode UNABLE_TO_UPDATE_TICKET = new DTIErrorCode(
      "410", DTIErrorCode.ErrorScope.PAYLOAD, "Unable to Update Ticket");
  public final static DTIErrorCode TICKET_TYPE_LOAD_ERROR = new DTIErrorCode(
      "497", DTIErrorCode.ErrorScope.PAYLOAD, "Ticket Type Load Error");
  public final static DTIErrorCode ELIG_MEMBER_LOAD_ERROR = new DTIErrorCode(
      "498", DTIErrorCode.ErrorScope.PAYLOAD, "Elig Member Load Error");
  public final static DTIErrorCode UNDEFINED_WARNING = new DTIErrorCode(
      "499", DTIErrorCode.ErrorScope.PAYLOAD, "Undefined Warning");
  public final static DTIErrorCode INVALID_PRODUCT_CODE = new DTIErrorCode(
      "500", DTIErrorCode.ErrorScope.COMMAND, "Invalid Product Code");
  public final static DTIErrorCode PRODUCT_EXPIRED = new DTIErrorCode("501",
      DTIErrorCode.ErrorScope.COMMAND, "Product Expired");
  public final static DTIErrorCode PRODUCT_SOLD_OUT = new DTIErrorCode("502",
      DTIErrorCode.ErrorScope.COMMAND, "Product Sold Out");
  public final static DTIErrorCode PRODUCT_UNAVAILABLE = new DTIErrorCode(
      "503", DTIErrorCode.ErrorScope.COMMAND, "Product Unavailable");
  public final static DTIErrorCode INVALID_SHELL_FOR_PRODUCT = new DTIErrorCode(
      "504", DTIErrorCode.ErrorScope.COMMAND, "Invalid Shell for Product");
  public final static DTIErrorCode PRODUCT_NOT_PERMITTED = new DTIErrorCode(
      "505", DTIErrorCode.ErrorScope.COMMAND, "Product Not Permitted");
  public final static DTIErrorCode INVALID_ELIGIBILITY_NBR = new DTIErrorCode(
      "506", DTIErrorCode.ErrorScope.COMMAND, "Invalid Eligibility Nbr");
  public final static DTIErrorCode INACTIVE_ELIGIBILITY_NBR = new DTIErrorCode(
      "507", DTIErrorCode.ErrorScope.COMMAND, "Inactive Eligibility Nbr");
  public final static DTIErrorCode INVALID_AREA = new DTIErrorCode("508",
      DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Area");
  public final static DTIErrorCode INVALID_PRODUCT_QTY = new DTIErrorCode(
      "509", DTIErrorCode.ErrorScope.COMMAND, "Invalid Product Qty");
  public final static DTIErrorCode INVALID_PRODUCT_UPGRADE_PATH = new DTIErrorCode(
      "510", DTIErrorCode.ErrorScope.COMMAND, "Invalid Pdt Upgrade Path"); // 2.10
  public final static DTIErrorCode PRICE_MISMATCH_ERROR_HI = new DTIErrorCode(
      "512", DTIErrorCode.ErrorScope.COMMAND, "Price Mismatch-Error:Hi");
  public final static DTIErrorCode PRICE_MISMATCH_ERROR_LO = new DTIErrorCode(
      "513", DTIErrorCode.ErrorScope.COMMAND, "Price Mismatch-Error:Lo");
  public final static DTIErrorCode INVALID_TICKET_ID = new DTIErrorCode(
      "514", DTIErrorCode.ErrorScope.COMMAND, "Invalid Ticket ID");
  public final static DTIErrorCode INACTIVE_TICKET_TYPE = new DTIErrorCode(
      "515", DTIErrorCode.ErrorScope.COMMAND, "Inactive Ticket Type");
  public final static DTIErrorCode MISSING_TICKET_TYPE = new DTIErrorCode(
      "516", DTIErrorCode.ErrorScope.COMMAND, "Missing Ticket Type");
  public final static DTIErrorCode INVALID_PAYMENT_AMOUNT = new DTIErrorCode(
      "520", DTIErrorCode.ErrorScope.COMMAND, "Invalid Payment Amount");
  public final static DTIErrorCode INVALID_PAYMENT_TYPE = new DTIErrorCode(
      "521", DTIErrorCode.ErrorScope.COMMAND, "Invalid Payment Type");
  public final static DTIErrorCode INVALID_PAYMENT_COUNT = new DTIErrorCode(
      "522", DTIErrorCode.ErrorScope.COMMAND, "Invalid Payment Count");
  public final static DTIErrorCode INVALID_MEDIA_COUNT = new DTIErrorCode(
      "530", DTIErrorCode.ErrorScope.MEDIA, "Invalid Media Count"); // 2.16.1 BIEST001
  public final static DTIErrorCode MEDIA_ALREADY_ASSOCIATED = new DTIErrorCode(
      "531", DTIErrorCode.ErrorScope.MEDIA,
      "Media associated to different account."); // 2.16.1 BIEST001 (Nexus error code 504)
  public final static DTIErrorCode TICKET_ALREADY_ELECTRONIC = new DTIErrorCode(
      "532", DTIErrorCode.ErrorScope.TICKET,
      "Ticket is already electronic.  Cannot be tickerated."); // 2.16.1 (BIEST001) (Nexus error code 505)
  public final static DTIErrorCode INVALID_ENTITY = new DTIErrorCode("550",
      DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Entity");
  public final static DTIErrorCode INVALID_COMMAND_UNSUPPORTED = new DTIErrorCode(
      "551", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Command");
  public final static DTIErrorCode COMMAND_NOT_AUTHORIZED = new DTIErrorCode(
      "552", DTIErrorCode.ErrorScope.PAYLOAD, "Command Not Authorized");
  public final static DTIErrorCode ENTITY_NOT_ACTIVE = new DTIErrorCode(
      "553", DTIErrorCode.ErrorScope.PAYLOAD, "Entity Not Active");
  public final static DTIErrorCode INVALID_SALES_DATE_TIME = new DTIErrorCode(
      "554", DTIErrorCode.ErrorScope.PAYLOAD, "Invalid Sales Date/Time");
  public final static DTIErrorCode INVALID_SHELL_NUMBER = new DTIErrorCode(
      "555", DTIErrorCode.ErrorScope.COMMAND, "Invalid Shell Number");
  public final static DTIErrorCode INVALID_TICKET_PROVIDER = new DTIErrorCode(
      "556", DTIErrorCode.ErrorScope.COMMAND, "Invalid Ticket Provider");
  public final static DTIErrorCode INVALID_COMMAND_COUNT = new DTIErrorCode(
      "558", DTIErrorCode.ErrorScope.COMMAND, "Invalid Command Count");
  public final static DTIErrorCode INVALID_TICKET_COUNT = new DTIErrorCode(
      "559", DTIErrorCode.ErrorScope.COMMAND, "Invalid Ticket Count");
  public final static DTIErrorCode FAILED_DB_OPERATION_SVC = new DTIErrorCode(
      "560", DTIErrorCode.ErrorScope.COMMAND, "Failed DB Operation");
  public final static DTIErrorCode ACTOR_NOT_AUTHORIZED = new DTIErrorCode(
      "561", DTIErrorCode.ErrorScope.COMMAND, "Actor Not Authorized");
  public final static DTIErrorCode INVALID_PRICE = new DTIErrorCode("562",
      DTIErrorCode.ErrorScope.COMMAND, "Invalid Price");
  public final static DTIErrorCode INVALID_VALIDITY_DATES = new DTIErrorCode(
      "563", DTIErrorCode.ErrorScope.COMMAND, "Invalid Validity Dates");
  public final static DTIErrorCode INVALID_TICKET_DEMOGRAPHICS = new DTIErrorCode(
      "564", DTIErrorCode.ErrorScope.COMMAND,
      "Invalid Ticket Demographics"); // As of 2.9
  public final static DTIErrorCode TRANSACTION_FLOOD_BLOCKED = new DTIErrorCode(
      "565", DTIErrorCode.ErrorScope.PAYLOAD, "Transaction Flood Block");
  public final static DTIErrorCode INVALID_SPECIFIED_ACCT = new DTIErrorCode(
      "570", DTIErrorCode.ErrorScope.COMMAND, "Invalid Specified Account"); // As of 2.10
  public final static DTIErrorCode DTI_CONFIG_ERROR = new DTIErrorCode("590",
      DTIErrorCode.ErrorScope.COMMAND, "DTI Config Error");
  public final static DTIErrorCode DTI_DATA_ERROR = new DTIErrorCode("591",
      DTIErrorCode.ErrorScope.COMMAND, "DTI Data Error");
  public final static DTIErrorCode DTI_PROCESS_ERROR = new DTIErrorCode(
      "592", DTIErrorCode.ErrorScope.COMMAND, "DTI Process Error");
  public final static DTIErrorCode UNDEFINED_CRITICAL_ERROR = new DTIErrorCode(
      "799", DTIErrorCode.ErrorScope.COMMAND, "Undefined Critical Error");
  public final static DTIErrorCode MESSAGE_PARAMETER_INVALID = new DTIErrorCode(
      "800", DTIErrorCode.ErrorScope.COMMAND, "Message Parameter Invalid");
  public final static DTIErrorCode MESSAGE_TIME_OUT = new DTIErrorCode("801",
      DTIErrorCode.ErrorScope.COMMAND, "Message Time-Out");
  public final static DTIErrorCode OPERATION_CANNOT_COMPLETE = new DTIErrorCode(
      "805", DTIErrorCode.ErrorScope.COMMAND, "Operation Cannot Complete");
  public final static DTIErrorCode MODULE_NOT_AVAILABLE = new DTIErrorCode(
      "806", DTIErrorCode.ErrorScope.COMMAND, "Module Not Available");
  public final static DTIErrorCode PRODUCT_INVALID = new DTIErrorCode("810",
      DTIErrorCode.ErrorScope.COMMAND, "Product Invalid");
  public final static DTIErrorCode ORDER_INVALID = new DTIErrorCode("811",
      DTIErrorCode.ErrorScope.COMMAND, "Order Invalid");
  public final static DTIErrorCode EVENT_INVALID = new DTIErrorCode("812",
      DTIErrorCode.ErrorScope.COMMAND, "Event Invalid");
  public final static DTIErrorCode SOURCE_INVALID = new DTIErrorCode("813",
      DTIErrorCode.ErrorScope.COMMAND, "Source Invalid");
  public final static DTIErrorCode DUPLICATE_ORDER = new DTIErrorCode("815",
      DTIErrorCode.ErrorScope.COMMAND, "Duplicate Order");
  public final static DTIErrorCode PAYMENT_AMOUNT_INVALID = new DTIErrorCode(
      "820", DTIErrorCode.ErrorScope.COMMAND, "Payment Amount Invalid");
  public final static DTIErrorCode PAYMENT_TYPE_INVALID = new DTIErrorCode(
      "821", DTIErrorCode.ErrorScope.COMMAND, "Payment Type Invalid");
  public final static DTIErrorCode VOUCHER_INVALID = new DTIErrorCode("822",
      DTIErrorCode.ErrorScope.COMMAND, "Voucher Invalid");
  public final static DTIErrorCode VOUCHER_EXPIRED = new DTIErrorCode("823",
      DTIErrorCode.ErrorScope.COMMAND, "Voucher Expired");
  public final static DTIErrorCode MASTER_VOUCHER_EXPIRED = new DTIErrorCode(
      "824", DTIErrorCode.ErrorScope.COMMAND, "Master Voucher Expired");
  public final static DTIErrorCode MASTER_VOUCHER_INVALID = new DTIErrorCode(
      "825", DTIErrorCode.ErrorScope.COMMAND, "Master Voucher Invalid");
  public final static DTIErrorCode CREDIT_CARD_EXPIRED = new DTIErrorCode(
      "826", DTIErrorCode.ErrorScope.COMMAND, "Credit Card Expired");
  public final static DTIErrorCode CREDIT_CARD_INVALID = new DTIErrorCode(
      "827", DTIErrorCode.ErrorScope.COMMAND, "Credit Card Invalid");
  public final static DTIErrorCode CREDIT_CARD_DENIED = new DTIErrorCode(
      "828", DTIErrorCode.ErrorScope.COMMAND, "Credit Card Denied");
  public final static DTIErrorCode CALL_FOR_APPROVAL = new DTIErrorCode(
      "829", DTIErrorCode.ErrorScope.COMMAND, "Call for Approval");
  public final static DTIErrorCode AUTHORIZATION_OFFLINE = new DTIErrorCode(
      "830", DTIErrorCode.ErrorScope.COMMAND, "Authorization Offline");
  public final static DTIErrorCode AUTHORIZATION_TIME_OUT = new DTIErrorCode(
      "831", DTIErrorCode.ErrorScope.COMMAND, "Authorization Time-Out");
  public final static DTIErrorCode PAYMENT_NOT_APPROVED = new DTIErrorCode(
      "832", DTIErrorCode.ErrorScope.COMMAND, "Payment not approved");
  public final static DTIErrorCode TRANSACTION_INVALID = new DTIErrorCode(
      "840", DTIErrorCode.ErrorScope.COMMAND, "Transaction Invalid");
  public final static DTIErrorCode CANNOT_VOID_TRANSACTION = new DTIErrorCode(
      "841", DTIErrorCode.ErrorScope.COMMAND, "Cannot Void Transaction");
  public final static DTIErrorCode INVALID_ACCOUNT = new DTIErrorCode("842",
      DTIErrorCode.ErrorScope.COMMAND, "One or more accounts invalid.");
  public final static DTIErrorCode INVALID_ACCOUNT_CREATION = new DTIErrorCode(
      "843", DTIErrorCode.ErrorScope.COMMAND,
      "Invalid account creation topology.");
  public final static DTIErrorCode INVALID_MIXED_ENTITLEMENTS = new DTIErrorCode(
      "844", DTIErrorCode.ErrorScope.COMMAND,
      "Invalid mixed entitlements."); // 2.10
  public final static DTIErrorCode TICKET_INVALID = new DTIErrorCode("850",
      DTIErrorCode.ErrorScope.TICKET, "Ticket Invalid");
  public final static DTIErrorCode TICKET_NOT_UPGRADEABLE = new DTIErrorCode(
      "851", DTIErrorCode.ErrorScope.COMMAND, "Ticket Not Upgradeable");
  public final static DTIErrorCode TICKET_ALREADY_USED = new DTIErrorCode(
      "852", DTIErrorCode.ErrorScope.TICKET, "Ticket Already Used");
  public final static DTIErrorCode TICKET_ALREADY_VOIDED = new DTIErrorCode(
      "853", DTIErrorCode.ErrorScope.TICKET, "Ticket Already Voided");
  public final static DTIErrorCode TICKET_ALREADY_REFUNDED = new DTIErrorCode(
      "854", DTIErrorCode.ErrorScope.TICKET, "Ticket Already Refunded");
  public final static DTIErrorCode TICKET_ALREADY_ISSUED = new DTIErrorCode(
      "855", DTIErrorCode.ErrorScope.TICKET, "Ticket Already Issued");
  public final static DTIErrorCode FAILED_DB_OPERATION = new DTIErrorCode(
      "860", DTIErrorCode.ErrorScope.COMMAND, "Failed DB Operation");
  public final static DTIErrorCode INVALID_TKT_DEMO_PROVIDER = new DTIErrorCode(
      "864", DTIErrorCode.ErrorScope.COMMAND,
      "Invalid Ticket Demographics"); // As of 2.9
  public final static DTIErrorCode TP_CRITICAL_FAILURE = new DTIErrorCode(
      "870", DTIErrorCode.ErrorScope.COMMAND, "TP Critical Failure");
  public final static DTIErrorCode TP_INTERFACE_FAILURE = new DTIErrorCode(
      "871", DTIErrorCode.ErrorScope.COMMAND, "TP Interface Failure");
  public final static DTIErrorCode SCHEDULER_201_ERROR = new DTIErrorCode(
      "872", DTIErrorCode.ErrorScope.COMMAND, "201 Error");
  public final static DTIErrorCode TP_CONNECT_FAILURE = new DTIErrorCode(
      "880", DTIErrorCode.ErrorScope.COMMAND, "TP Connect Failure");
  public final static DTIErrorCode INVALID_COMMAND = new DTIErrorCode("881",
      DTIErrorCode.ErrorScope.COMMAND, "Invalid Command");
  public final static DTIErrorCode INVALID_RESERVATION_NUMBER = new DTIErrorCode(
      "882", DTIErrorCode.ErrorScope.COMMAND, "Invalid Reservation Num");
  public final static DTIErrorCode CANNOT_FIND_RESERVATION = new DTIErrorCode(
      "883", DTIErrorCode.ErrorScope.COMMAND, "Cannot find Reservation");
  public final static DTIErrorCode PARTIAL_FAILURE = new DTIErrorCode("898",
      DTIErrorCode.ErrorScope.COMMAND, "Partial Failure");
  public final static DTIErrorCode UNDEFINED_FAILURE = new DTIErrorCode(
      "899", DTIErrorCode.ErrorScope.COMMAND, "Undefined Failure");

  // ErrorCode Object Attributes
  private String errorCode;
  private DTIErrorCode.ErrorScope errorLevel;
  private String errorName;

  /**
   * Constructor for a standard error
   */
  public DTIErrorCode(String errorCodeIn,
      DTIErrorCode.ErrorScope errorLevelIn, String errorNameIn) {
    errorCode = errorCodeIn;
    errorLevel = errorLevelIn;
    errorName = errorNameIn;
    codeToScope.put(errorCodeIn, errorLevelIn);
  }

  /**
   * @return the errorLevel
   */
  public DTIErrorCode.ErrorScope getErrorScope() {
    return errorLevel;
  }

  /**
   * Defaults to payload scope if none is present in the HashMap created at class loading.
   * 
   * @param dtiErrorCode
   * @return
   */
  public static DTIErrorCode.ErrorScope getScopeFromCode(String dtiErrorCode) {

    ErrorScope errScpe = codeToScope.get(dtiErrorCode);

    if (errScpe == null) {
      return ErrorScope.PAYLOAD;
    }
    else {

      return errScpe;
    }
  }

  /**
   * @return the errorCode
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * @param errorLevel
   *            the errorLevel to set
   */
  public void setErrorScope(DTIErrorCode.ErrorScope errorLevel) {
    this.errorLevel = errorLevel;
  }

  /**
   * @param errorCode
   *            the errorCode to set
   */
  public void setErrorCode(String errorNumber) {
    this.errorCode = errorNumber;
  }

  /*
   * NOTE: Special functionality Push a copy of the error to the correct level, creating the ticket level if required. Build all fields required for the response from the request.
   * 
   * @param dtiErrorIn the dtiError to set
   * 
   * @param dtiTxn the DTITransactionTO used to reference key values
   * 
   * @param dtiResp the DTIResponseTO that needs to be populated
   */
  public static DTIResponseTO populateDTIErrorResponse(DTIErrorTO dtiErrorIn,
      DTITransactionTO dtiTxn, DTIResponseTO dtiResp) {

    dtiResp.setDtiError(dtiErrorIn);
    DTIFormatter.formatDefaultDTIResponseHeaders(dtiTxn, dtiResp);

    // Build Ticket Portion (if applicable)
    // ====================================
    if (dtiErrorIn.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

      if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

        QueryTicketResponseTO qtRespTO = new QueryTicketResponseTO();

        TicketTO ticketTO = new TicketTO();
        ticketTO.setTktItem(new BigInteger(TagConstants.TKT_ITEM_1));

        dtiTxn.getRequest().getCommandBody();

        qtRespTO.addTicket(ticketTO);

        dtiResp.setCommandBody(qtRespTO);

      }
      else if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.VOIDTICKET) {

        VoidTicketResponseTO vtRespTO = new VoidTicketResponseTO();

        TicketTO ticketTO = new TicketTO();
        ticketTO.setTktItem(new BigInteger(TagConstants.TKT_ITEM_1));

        dtiTxn.getRequest().getCommandBody();

        vtRespTO.addTicket(ticketTO);

        dtiResp.setCommandBody(vtRespTO);

      }
    }

    return dtiResp;
  }

  /**
   * NOTE: Special functionality Push a copy of the error to the correct level, creating the ticket level if required. Build all fields required for the response from the request.
   * 
   * @param dtiErrorIn
   *            the dtiError to set
   * @param dtiTxn
   *            the DTITransactionTO used to reference key values
   */
  public static DTIResponseTO populateDTIErrorResponse(DTIErrorTO dtiErrorIn,
      DTITransactionTO dtiTxn) {

    DTIResponseTO dtiResp = new DTIResponseTO();

    return populateDTIErrorResponse(dtiErrorIn, dtiTxn, dtiResp);

  }

  public String getErrorName() {
    return errorName;
  }

  public void setErrorName(String errorName) {
    this.errorName = errorName;
  }

}
