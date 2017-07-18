package pvt.disney.dti.gateway.rules.dlr;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * Contains the DLR specific business rules.
 * 
 * @author smoon
 */
public class DLRBusinessRules {

  // NOTE: WMB does not enforce these, so these may go away
  /** The maximum length for an egalaxy visual id (40) */
  public static final int MAX_VISUALID_LENGTH = 40;
  /** The maximum length for an egalaxy item code(20) */
  public static final int MAX_ITEMCODE_LENGTH = 20;

  /**
   * For provider-centric rules only that are NOT transaction-centric. Provider-centric rules that are also transaction centric are to added in their correct DLRTransactionNamed class. This method is primarily for handling anything in the
   * payload header or the command header that has a provider-centric rule.
   * 
   * @param dtiTxn
   *            the dti txn
   * 
   * @return the DTI transaction transfer object
   * 
   * @throws DTIException
   *             should any rule fail.
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
   */
  public static String changeToDLRProviderFormat(DTITransactionTO dtiTxn) throws DTIException {

    String xmlRequest = null;
    TransactionType requestType = dtiTxn.getTransactionType();

    switch (requestType) {

    case QUERYTICKET:
      xmlRequest = DLRQueryTicketRules.transformRequest(dtiTxn);
      break;

    case UPGRADEALPHA:
      xmlRequest = DLRUpgradeAlphaRules.transformRequest(dtiTxn);
      break;

    case VOIDTICKET:
      xmlRequest = DLRVoidTicketRules.transformRequest(dtiTxn);
      break;

    case RESERVATION:
      xmlRequest = DLRReservationRules.transformRequest(dtiTxn);
      break;

    case QUERYRESERVATION:
      xmlRequest = DLRQueryReservationRules.transformRequest(dtiTxn);
      break;

    case RENEWENTITLEMENT: // As of 2.16.1, JTL
      xmlRequest = DLRRenewEntitlementRules.transformRequest(dtiTxn);
      break;

    default:
      throw new DTIException(TransformRules.class,
          DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Invalid DLR transaction type sent to DTI Gateway.  Unsupported.");
    }

    return xmlRequest;
  }

  /**
   * Parses the xmlResponse string from the DLR provider into the DTITransactionTO object.
   * 
   * @param dtiTxn
   *            The dtiTxn object for this transaction.
   * @param xmlResponse
   *            The response string returned by the DLR provider.
   * @return The dtiTxn object enhanced with the response.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  public static DTITransactionTO changeDLRProviderFormatToDti(
      DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {

    TransactionType requestType = dtiTxn.getTransactionType();

    switch (requestType) {

    case QUERYTICKET:
      dtiTxn = DLRQueryTicketRules.transformResponse(dtiTxn, xmlResponse);
      break;

    case UPGRADEALPHA:
      dtiTxn = DLRUpgradeAlphaRules
          .transformResponse(dtiTxn, xmlResponse);
      break;

    case VOIDTICKET:
      dtiTxn = DLRVoidTicketRules.transformResponse(dtiTxn, xmlResponse);
      break;

    case RESERVATION:
      dtiTxn = DLRReservationRules.transformResponse(dtiTxn, xmlResponse);
      break;

    case QUERYRESERVATION:
      dtiTxn = DLRQueryReservationRules.transformResponse(dtiTxn,
          xmlResponse);
      break;

    case RENEWENTITLEMENT:
      dtiTxn = DLRRenewEntitlementRules.transformResponse(dtiTxn,
          xmlResponse);
      break;

    default:
      throw new DTIException(
          TransformRules.class,
          DTIErrorCode.COMMAND_NOT_AUTHORIZED,
          "Invalid DLR transaction type sent to Java version of DTI Gateway.  Unsupported.");
    }

    return dtiTxn;
  }

  /**
   * Validates in-bound DLR tickets
   * 
   * @param aTktList
   * @throws DTIException
   */
  static void validateInBoundDLRTickets(ArrayList<TicketTO> aTktList) throws DTIException {

    for (TicketTO aTicketTO : aTktList) {

      // Only one TktId on this ticket?
      if (aTicketTO.getTicketTypes().size() != 1) throw new DTIException(
          DLRBusinessRules.class, DTIErrorCode.INVALID_TICKET_ID,
          "In-bound dLR txn with <> 1 TktId: " + aTicketTO
              .getTicketTypes().size());

      // External Check
      if (aTicketTO.getTicketTypes().get(0) != TicketTO.TicketIdType.EXTERNAL_ID) {
        throw new DTIException(
            DLRBusinessRules.class,
            DTIErrorCode.INVALID_TICKET_ID,
            "In-bound dLR txn with an invalid TicketIdType: " + aTicketTO
                .getTicketTypes().get(0));
      }
      else {
        if (aTicketTO.getExternal().length() > MAX_VISUALID_LENGTH) throw new DTIException(
            DLRBusinessRules.class,
            DTIErrorCode.INVALID_TICKET_ID,
            "In-bound DLR txn with invalid External (VisualID) length: " + aTicketTO
                .getExternal().length());
      }

    }

  }

}
