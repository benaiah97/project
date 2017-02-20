package pvt.disney.dti.gateway.rules;

import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;

/**
 * The class TicketRules implements the various business rules regarding
 * tickets.
 * 
 * @author lewit019
 * 
 */
public class TicketRules {

  /** Constant value for "Presale" */
  private static final String PRESALE = "Presale";

  /** Constant value for ManualMailOrder, mimics Presale */
  private static final String MANUALMAILORDER = "ManualMailOrder";

  /** Constant value for the owner of WDW Ticket Types */
  public static final String WDW_TICKET_OWNER = "WDW";

  /** Constant value for the owner of DLR Ticket Types */
  public static final String DLR_TICKET_OWNER = "DLR";

  /**
   * Enforces the following rules: <BR>
   * 1. Only one TicketTO may be present in the ticket list.
   * 
   * @param aTktList
   *          The ticket transfer object list
   * @throws DTIException
   *           should there be more or less than one on the list.
   */
  public static void validateOnlyOneTicketOnRequest(ArrayList<TicketTO> aTktList) throws DTIException {

    if (aTktList.size() != 1) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction required to have at least 1 and no more than 1 had " + aTktList.size() + " tickets.");
    }

    return;

  }

  /**
   * Enforces the following rules: <BR>
   * 1. Up to eight tickets may be present in the ticket list.
   * 
   * @param aTktList
   *          the ticket list
   * 
   * @throws DTIException
   *           should there be more than eight tickets on a request.
   */
  public static void validateMaxEightTicketsOnRequest(ArrayList<TicketTO> aTktList) throws DTIException {

    if (aTktList.size() > 8) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction required to have at no more than 8 had " + aTktList.size() + " tickets.");
    }

    return;

  }

  /**
   * Enforces the following rules: <BR>
   * 1. At least 1 ticket must be present in the ticket list.
   * 
   * @param aTktList
   *          the ticket list
   * 
   * @throws DTIException
   *           should there be at least 1 ticket on a request.
   */
  public static void validateMinOneTicketOnRequest(ArrayList<TicketTO> aTktList) throws DTIException {

    if (aTktList.size() == 0) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction required to have at least 1 ticket.");
    }

    return;

  }

  /**
   * Enforces the following rules: <BR>
   * 1. Up to 250 tickets may be present in the ticket list.
   * 
   * @param aTktList
   *          the ticket list
   * 
   * @throws DTIException
   *           should there be no more than 250 tickets on a request.
   */
  public static void validateMax250TicketsOnRequest(ArrayList<TicketTO> aTktList) throws DTIException {

    if (aTktList.size() > 250) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction required to have at no more than 250 tickets; had " + aTktList.size() + " tickets.");
    }

    return;

  }

  /**
   * Reviews the reservation ticket count to ensure it's in agreement with the
   * following. <BR>
   * 1. Is the reservation ticket clause complete? <BR>
   * 2. Were there any tickets on the transactions? <BR>
   * 3. Determine if the seller is allowed to sell the number of tickets on the
   * order.
   * 
   * @param tktList
   *          the tkt list
   * @param tpLookupList
   *          the ticket provider lookup list
   * @param entityAttrMap
   *          the entity attribute list
   * @param reservationTO
   *          the reservation transfer object.
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validateReservationTicketCount(ArrayList<TicketTO> tktList, ArrayList<TPLookupTO> tpLookupList,
      HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> entityAttrMap, ReservationTO reservationTO) throws DTIException {

    int numberOfTickets = 0;

    // Add up all of the tickets, either actual or those listed as quantity.
    for /* each */(TicketTO aTicketTO : /* in */tktList) {
      if (aTicketTO.getProdQty() == null) {
        throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
            "Reservation transaction had a ticket clause with no quantity. Not allowed.");
      } else {
        numberOfTickets += aTicketTO.getProdQty().intValue();
      }
    }

    if (numberOfTickets == 0) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction has no tickets that could be counted. Not allowed.");
    }

    int maxTicketLimit = -1;
    for /* each */(TPLookupTO aTPLookupTO : /* in */tpLookupList) {

      if (aTPLookupTO.getLookupType() == TPLookupTO.TPLookupType.MAX_LIMIT) {

        try {
          maxTicketLimit = Integer.parseInt(aTPLookupTO.getLookupValue());
        } catch (NumberFormatException nfe) {
          throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
              "Ticket MaxLimit in DTI database is not a parseable integer: " + aTPLookupTO.getLookupValue());
        }

        break;
      }
    }

    if (maxTicketLimit <= -1) {
      throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Ticket MaxLimit not defined in DTI database as required for this transaction type.");
    }

    // Determine if there are exceptions specified.
    int prntTktCntMaxException = 0;
    int noPrntTktCntMaxException = 0;
    AttributeTO anAttributeTO = entityAttrMap.get(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);

    if (anAttributeTO != null) {
      try {
        prntTktCntMaxException = Integer.parseInt(anAttributeTO.getAttrValue());
      } catch (NumberFormatException nfe) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "Entity Attr PrntTktCntMax in DTI database is not a parseable integer: " + anAttributeTO.getAttrValue());
      }
    }

    anAttributeTO = entityAttrMap.get(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
    if (anAttributeTO != null) {
      try {
        noPrntTktCntMaxException = Integer.parseInt(anAttributeTO.getAttrValue());
      } catch (NumberFormatException nfe) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "Entity Attr NoPrntTktCntMax in DTI database is not a parseable integer: " + anAttributeTO.getAttrValue());
      }
    }

    // Validate the exceptions, if specified. They must be more than
    // MaxTicket
    if (prntTktCntMaxException > 0) {
      if (prntTktCntMaxException <= maxTicketLimit) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "Data set-up failure:  PrntTktCntMax is less or equal to MaxTicket, " + prntTktCntMaxException + " versus "
                + maxTicketLimit);
      }
    } else if (noPrntTktCntMaxException > 0) {
      if (noPrntTktCntMaxException <= maxTicketLimit) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "Data set-up failure:  NoPrntTktCntMax is less or equal to MaxTicket, " + noPrntTktCntMaxException
                + " versus " + maxTicketLimit);
      }
    }

    // Ensure the correct leveling of exceptions when both are specified
    if ((prntTktCntMaxException > 0) && (noPrntTktCntMaxException > 0)) {

      if (prntTktCntMaxException > noPrntTktCntMaxException) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
            "Data set-up failure:  PrntTktCntMax greater than NoPrntTktCntMax, " + prntTktCntMaxException + " versus "
                + noPrntTktCntMaxException);
      }
    }

    // Determine if the number of tickets is permitted.
    if (numberOfTickets > maxTicketLimit) {

      // If no overrides have been specified, the count is in error.
      if ((prntTktCntMaxException == 0) && (noPrntTktCntMaxException == 0)) {
        throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
            "Transaction exceeded max ticket count of " + maxTicketLimit + ": " + numberOfTickets);
      }

      // If only the printed ticket override has been specified, and the
      // number
      // exceeds it, then the count is in error.
      if ((prntTktCntMaxException > 0) && (noPrntTktCntMaxException == 0)) {
        if (numberOfTickets > prntTktCntMaxException) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
              "Transaction exceeded printed ticket exception count of " + prntTktCntMaxException + ": "
                  + numberOfTickets);
        }
      }

      // If only the non printed ticket override has been specified...
      if ((prntTktCntMaxException == 0) && (noPrntTktCntMaxException > 0)) {

        // If the number of tickets is higher than the limit, the count is in error.
        if (numberOfTickets > noPrntTktCntMaxException) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
              "Transaction exceeded non-printed ticket exception count of " + noPrntTktCntMaxException + ": "
                  + numberOfTickets);
        }

        // If the limit is between MaxTicket and Non-print, but does not have
        // "PRESALE" or ManualMailOrder
        // as the ResSalesType, then the ticket count is in error.
        if ((PRESALE.compareTo(reservationTO.getResSalesType()) != 0)
            && (MANUALMAILORDER.compareTo(reservationTO.getResSalesType()) != 0)) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
              "Transaction exceeded MaxTicket limit (" + maxTicketLimit + ") and failed to "
                  + "qualify for defined NoPrntTktCntMax exception: " + numberOfTickets);
        }
      }

      if ((prntTktCntMaxException > 0) && (noPrntTktCntMaxException > 0)) {

        if (numberOfTickets > noPrntTktCntMaxException) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
              "Transaction exceeded non-printed ticket exception count of " + noPrntTktCntMaxException + ": "
                  + numberOfTickets);
        }

        if ((numberOfTickets > prntTktCntMaxException) && (PRESALE.compareTo(reservationTO.getResSalesType()) != 0)
            && (MANUALMAILORDER.compareTo(reservationTO.getResSalesType()) != 0)) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
              "Transaction exceeded MaxTicket limit (" + maxTicketLimit + ") and failed to "
                  + "qualify for defined NoPrntTktCntMax exception: " + numberOfTickets);
        }

      }

    }

    return;
  }

  /**
   * Reviews the reservation ticket count to ensure it's in agreement with the
   * following. <BR>
   * 1. Is the reservation ticket clause complete? <BR>
   * 2. Were there any tickets on the transactions? <BR>
   * 3. Determine if the seller is allowed to sell the number of tickets on the
   * order.
   * 
   * @param tktList
   *          the tkt list
   * @param tpLookupList
   *          the ticket provider lookup list
   * @param entityAttrMap
   *          the entity attribute list
   * @param reservationTO
   *          the reservation transfer object.
   * 
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validateCreateTicketCount(ArrayList<TicketTO> tktList, ArrayList<TPLookupTO> tpLookupList)
      throws DTIException {

    int numberOfTickets = 0;

    // Add up all of the tickets, either actual or those listed as quantity.
    for /* each */(TicketTO aTicketTO : /* in */tktList) {
      if (aTicketTO.getProdQty() == null) {
        throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
            "Reservation transaction had a ticket clause with no quantity. Not allowed.");
      } else {
        numberOfTickets += aTicketTO.getProdQty().intValue();
      }
    }

    if (numberOfTickets == 0) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction has no tickets that could be counted. Not allowed.");
    }

    int maxTicketLimit = -1;
    for /* each */(TPLookupTO aTPLookupTO : /* in */tpLookupList) {

      if (aTPLookupTO.getLookupType() == TPLookupTO.TPLookupType.MAX_LIMIT) {

        try {
          maxTicketLimit = Integer.parseInt(aTPLookupTO.getLookupValue());
        } catch (NumberFormatException nfe) {
          throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
              "Ticket MaxLimit in DTI database is not a parseable integer: " + aTPLookupTO.getLookupValue());
        }

        break;
      }
    }

    if (maxTicketLimit <= -1) {
      throw new DTIException(TicketRules.class, DTIErrorCode.DTI_DATA_ERROR,
          "Ticket MaxLimit not defined in DTI database as required for this transaction type.");
    }

    // Determine if the number of tickets is permitted.
    if (numberOfTickets > maxTicketLimit) {
      throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_TICKET_COUNT,
          "Transaction exceeded max ticket count of " + maxTicketLimit + ": " + numberOfTickets);
    }

    return;
  }

  /**
   * Validate external ticket id only.<BR>
   * Enforces the following rules: <BR>
   * 1. Does the ticket include an external ticket ID and only an external
   * ticket ID.
   * 
   * @param aTicketTO
   *          a ticket to
   * 
   * @throws DTIException
   *           should any other ticket be provided than an external ticket ID.
   */
  public static void validateExternalTktIdOnly(TicketTO aTicketTO) throws DTIException {

    ArrayList<TicketTO.TicketIdType> tktTypes = aTicketTO.getTicketTypes();

    DTIErrorCode errCode = DTIErrorCode.INVALID_TICKET_ID;

    if (tktTypes.size() == 0) {
      throw new DTIException(TicketRules.class, errCode, "Ticket did not include any ticket ids.");
    }

    if (tktTypes.size() > 1) {
      throw new DTIException(TicketRules.class, errCode, "Ticket included more ticket types than external ID.");
    }

    if (tktTypes.get(0) != TicketTO.TicketIdType.EXTERNAL_ID) {
      throw new DTIException(TicketRules.class, errCode, "Ticket did not include external ticket id.");
    }

    if (aTicketTO.getExternal() == null) {
      throw new DTIException(TicketRules.class, errCode, "Ticket included empty external ticket id.");
    }

    if (aTicketTO.getExternal().compareTo("") == 0) {
      throw new DTIException(TicketRules.class, errCode, "Ticket included invalid external ticket id.");
    }

    return;

  }

  /**
   * Enforces validity Date rules. <BR>
   * Enforces the following rules: <BR>
   * 1. If the ticket product doesn't have validity date information and the
   * product says it should, then the order fails. <BR>
   * 2. If the order does have a validStart date, the product says it should
   * have validity dates but the entity is not allowed to send them, then the
   * order fails. <BR>
   * 3. (New) If the ticket product is not supposed to have validity date
   * information, and it does, the order fails. (As of 2.9 - Added 2/13/2013)
   * 
   * @param aTicketTO
   *          a ticket transfer object
   * @throws DTIException
   *           should any rule fail.
   */
  public static void validateTicketValidityDates(ArrayList<TicketTO> tktListTO, EntityTO anEntityTO,
      ArrayList<DBProductTO> dbProdList) throws DTIException {

    DTIErrorCode errCode = DTIErrorCode.INVALID_VALIDITY_DATES;

    HashMap<String, DBProductTO> dbProdMap = new HashMap<String, DBProductTO>();

    if (anEntityTO == null) {
      throw new DTIException(TicketRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
          "Did not receive entity information, as expected.");
    }

    // Put products in a HashMap to make them easily accessible
    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
      dbProdMap.put(aDBProduct.getPdtCode(), aDBProduct);
    }

    // Cycle through the tickets on the order enforcing the rules
    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      String tktPdtCode = aTicketTO.getProdCode();
      DBProductTO aDBProduct = dbProdMap.get(tktPdtCode);

      if (aDBProduct == null) {
        throw new DTIException(TicketRules.class, DTIErrorCode.DTI_PROCESS_ERROR, "Ticket item "
            + aTicketTO.getTktItem().toString() + " with product " + tktPdtCode
            + " does not have an entry, as expected, in the DB Product list.");
      }

      if (aDBProduct.isValidityDateInfoRequired() && (aTicketTO.getTktValidityValidStart() == null)) {
        throw new DTIException(TicketRules.class, errCode, "Ticket item " + aTicketTO.getTktItem().toString()
            + " with product " + tktPdtCode + " did not have ticket validity dates as required.");
      }

      if (aDBProduct.isValidityDateInfoRequired() && aTicketTO.getTktValidityValidStart() != null
          && !anEntityTO.isValidityDateProductAllowed()) {

        throw new DTIException(TicketRules.class, errCode, "Ticket item " + aTicketTO.getTktItem().toString()
            + " with product " + tktPdtCode + " had ticket validity dates.  Not allowed for entity "
            + anEntityTO.getTsLocation() + ".");

      }

      // New 2/13/2013 2.9 JTL
      if (!aDBProduct.isValidityDateInfoRequired()
          && ((aTicketTO.getTktValidityValidStart() != null) || (aTicketTO.getTktValidityValidEnd() != null))) {

        throw new DTIException(TicketRules.class, errCode, "Ticket item " + aTicketTO.getTktItem().toString()
            + " with product " + tktPdtCode + " had ticket validity dates.  Not allowed for product " + tktPdtCode
            + ".");
      }

    }

    return;

  }

  /**
   * Enforce Ticket Assignment Rules 1. A valid ( greater than zero) Product
   * Quantity is provided for each Ticket Assignment 2. The sum of all Ticket
   * Assignment product quantities equals the product quantity for that ticket
   * 
   * @param tickets
   * @throws DTIException
   */
  public static void validateTicketAssignment(ArrayList<TicketTO> tickets) throws DTIException {
    
    for (TicketTO ticket : tickets) {
      int assignedQuantity = 0;

      for (TktAssignmentTO assigned : ticket.getTicketAssignmets()) {
        if (assigned.getProdQty() == null || assigned.getProdQty().intValue() <= 0) {
          // invalid Product Quantity
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
              "Invalid Ticket Assignment Product Quantity provided.");
        } else {
          assignedQuantity += assigned.getProdQty().intValue();
        }
      }
      if (ticket.getProdQty() == null) {
        throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
            "validateTicketAssignment: Invalid Ticket Product Quantity provided.");
      } else {
        // If no EntitlementAccountId is provided, don't check as it
        // will be a default account
        if (assignedQuantity > 0 && ticket.getProdQty().intValue() != assignedQuantity) {
          throw new DTIException(TicketRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
              "validateTicketAssignment: Ticket Product Quantity must match the sum of Assigned Products.");
        }
      }
    }
  }

}
