package pvt.disney.dti.gateway.rules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.*;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.dao.*;

public class ElectronicEntitlementRules {

	/** The object used for logging. */
	private static final ElectronicEntitlementRules THISOBJ = new ElectronicEntitlementRules();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger("pvt.disney.dti.gateway.dao.ElectronicEntitlementRules");

	public static final BigInteger PRICE_MISMATCH_WARN = new BigInteger(
			DTIErrorCode.PRICE_MISMATCH_WARNING.getErrorCode());

  public static final String SHELL78 = "78";
  public static final String SHELL79 = "79";
  public static final String SHELL11 = "11";  

  /**
   * Constructor for ElectronicEntitlementRules
   */
  private ElectronicEntitlementRules() {
    super();
  }

	/**
	 * Records sales for auto-promotion to electronic entitlements. 1. Don't save any products which are consumable. 2. Only save voids and upgrades, all other transaction types are excluded. 3. Insert only non-errant transactions
	 * (completed).
	 * 
	 */
	public static void recordShellEntitlements(DTITransactionTO dtiTxn) {

		// Insert only non-errant transactions.
		// If DTIError is present, the only allowable values is the the
		// price mismatch warning. If the error isn't a price mismatch warning,
		// then return. It's an in-line return, but it simplifies the code.
		DTIErrorTO dtiError = dtiTxn.getResponse().getDtiError();
		if (dtiError != null) {
			if (dtiError.getErrorCode() != null) {
				if (dtiError.getErrorCode().compareTo(PRICE_MISMATCH_WARN) != 0) {
					return;
				}
			}
		}

		/** Only process WDW. */
		if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.WDWNEXUS) {
			return;
		}

		// Gather database product values...
		HashMap<String, DBProductTO> dbProdMap = dtiTxn.getDbProdMap();

		// Activated entitlement ID (primary key)
		Integer itsNbr = dtiTxn.getTransIdITS();

		// Payload ID
		String payloadID = dtiTxn.getRequest().getPayloadHeader()
				.getPayloadID();

		// Entity ID
		int entityId = dtiTxn.getEntityTO().getEntityId();

		// Determine the transaction type
		if (dtiTxn.getTransactionType() == TransactionType.UPGRADEALPHA) {

			DTIRequestTO dtiRqstTO = dtiTxn.getRequest();
			CommandBodyTO cmdRqstBodyTO = dtiRqstTO.getCommandBody();
			UpgradeAlphaRequestTO uARqstTO = (UpgradeAlphaRequestTO) cmdRqstBodyTO;
			ArrayList<TicketTO> rqstTktList = uARqstTO.getTktList();

			DTIResponseTO dtiRespTO = dtiTxn.getResponse();
			CommandBodyTO cmdRespBodyTO = dtiRespTO.getCommandBody();
			UpgradeAlphaResponseTO uARespTO = (UpgradeAlphaResponseTO) cmdRespBodyTO;
			ArrayList<TicketTO> respTktList = uARespTO.getTktList();

			// If upgrade, then consumables have to be skipped.

			// For each ticket in the request, check request to see if the product is
			// consumable. If it is, then skip, else record its companion
			// DSSN in the response.
			for (/* each */TicketTO aRqstTicket : /* in */rqstTktList) {

        String prodCode = aRqstTicket.getProdCode();
        DBProductTO aProduct = dbProdMap.get(prodCode);
        
        // If a product is consumable, don't save it.
        if (aProduct.isConsumable()) {
          continue;
        }
        
        // If a product isn't an RFID shell, don't save it.
        if (aRqstTicket.getTktShell()!= null) {

          String tktShell = aRqstTicket.getTktShell();
         //TODO :: Somesh 
          ArrayList<String> rfidShellList = new ArrayList<String>();
          rfidShellList.add(SHELL78);
          rfidShellList.add(SHELL79);
          rfidShellList.add(SHELL11);
          
          if (rfidShellList.contains(tktShell)) {
            int tktItem = aRqstTicket.getTktItem().intValue();
            TicketTO aRespTicket = respTktList.get(tktItem - 1);

            ElectronicEntitlementKey.insertUpgradedEntitlement(itsNbr,aRespTicket, payloadID, entityId);
          } else {
            continue;
          }
        } 
      }
    } else if (dtiTxn.getTransactionType() == TransactionType.VOIDTICKET) {

			DTIResponseTO dtiRespTO = dtiTxn.getResponse();
			CommandBodyTO cmdRespBodyTO = dtiRespTO.getCommandBody();
			VoidTicketResponseTO vTRespTO = (VoidTicketResponseTO) cmdRespBodyTO;
			ArrayList<TicketTO> respTktList = vTRespTO.getTktList();

			// There are no qualifications on inserting voided tickets.
			TicketTO aTicket = null;
			if (respTktList.size() != 1) {
				logger.sendEvent(
						"Payload ID " + payloadID + " void did not have only one ticket.  It had " + respTktList
								.size(), EventType.EXCEPTION, THISOBJ);
			}
			else {
				aTicket = respTktList.get(0);
				ElectronicEntitlementKey.insertVoidedEntitlement(itsNbr,
						aTicket, payloadID, entityId);
			}

		}

		return;
	}

	/**
	 * Ensures that specified accounts denote at least one (and only one) existing or new account.
	 * 
	 * @version 2.10
	 */
	public static void validateSpecifiedAccounts(
			ArrayList<SpecifiedAccountTO> accountList) throws DTIException {

		for (/* each */SpecifiedAccountTO anAccount : /* in */accountList) {

			// If there is no existing account information...
			if ((anAccount.getExistingAccountId() == null) && (anAccount
					.getExistingMediaId() == null) && (anAccount
					.getExistingTktID() == null)) {

				// And no new account information... that's bad.
				if (anAccount.getNewExternalReferenceType() == null) {
					throw new DTIException(
							PaymentRules.class,
							DTIErrorCode.INVALID_SPECIFIED_ACCT,
							"Specified Account " + anAccount.getAccountItem()
									.intValue() + " had neither a new account or search criteria for an old account.  There must be, at least, one.");
				}
			}

			// If there is an existing account specified...
			if ((anAccount.getExistingAccountId() != null) || (anAccount
					.getExistingMediaId() != null) || (anAccount
					.getExistingTktID() != null)) {

				// An a new account specified, as well ... that's bad.
				if (anAccount.getNewExternalReferenceType() != null) {
					throw new DTIException(
							PaymentRules.class,
							DTIErrorCode.INVALID_SPECIFIED_ACCT,
							"Specified Account " + anAccount.getAccountItem()
									.intValue() + " had both a new account and search criteria for an old account.  There can be only one.");
				}
			}

		} // For Loop

	}

}
