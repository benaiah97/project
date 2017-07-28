package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketTO;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.TicketRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * The Class DLRUpgradeAlphaRules.
 */
public class DLRUpgradeAlphaRules implements TransformConstants {

	/**
	 * Once notable transformation...we disregard the passed in price which matches the DTI_PRODUCT.PRINTED_PRICE and replace it with UNIT_PRICE.
	 * 
	 * @param dtiTxn
	 *            the dti txn
	 * 
	 * @return the string
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
		// the string for transformation
		String xmlReqStr = null;

		// get the incoming dti request transfer object from the transaction
		DTIRequestTO dtiRequest = dtiTxn.getRequest();

		// get the command body from the request
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();

		// get the upgrade alpha clase from the command body
		UpgradeAlphaRequestTO dtiUpgrdAlphaReq = (UpgradeAlphaRequestTO) dtiCmdBody;

		// start creating the egalaxy gateway transfer objects
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
				GWEnvelopeTO.GWTransactionType.TICKETACTIVATION);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();

		// create the ticket activation request
		GWTicketActivationRqstTO tktActTicketTO = new GWTicketActivationRqstTO();

		// set the command for the ticket activations so we know if this is a
		// cancel (ie void) or activate (ie upgrade alpha)
		tktActTicketTO.setCommand(TransformConstants.GW_COMMAND_ACTIVATE);

		ArrayList<TicketTO> ticketList = dtiUpgrdAlphaReq.getTktList();
		Iterator<TicketTO> ticketIter = ticketList.iterator();
		while (ticketIter.hasNext()) {
			TicketTO dtiTicket = ticketIter.next();
			// set the needed values on the ticket
			GWTicketTO dlrTicket = new GWTicketTO(dtiTicket.getExternal());

			HashMap<String, DBProductTO> dtiProdMap = dtiTxn.getDbProdMap();
			DBProductTO dtiProduct = dtiProdMap.get(dtiTicket.getProdCode());
			dlrTicket.setItemCode(dtiProduct.getMappedProviderTktNbr()
					.toString());

			// throw out the dti_producted printed price and replace it with the unit price
			// this is a HACK to accomodate prior bad data setup deeds now
			// hardcoded into some POS systems.
			dlrTicket.setPrice(dtiProduct.getUnitPrice().toPlainString());
			// add the ticket to the parent ticket activation
			tktActTicketTO.addTicket(dlrTicket);
		}
		// set the customer id on the ticket activation
		tktActTicketTO.setCustomerID(dtiTxn.getEntityTO().getCustomerId());
		// set the ticket activation on the body
		bodyTO.setTicketActivationRqstTO(tktActTicketTO);

		// set the payment stuff and add it to the body
		// looks like only payment lookups were used in the legacy payment code
		// we just use payment lookup, but keeping here for now just in case
		// we want to support additional payment options down the road
		// Iterator<PaymentTO> payIter = dtiUpgrdAlphaReq.getPaymentList().iterator();
		// while (payIter.hasNext()) {
		// PaymentTO dtiPayTO = payIter.next();
		// create some sort of payment object and add it to the activation request
		// tktActTicketTO.addPayment(gwPayTo);
		// }

		// if we dont have payments in the incoming msg, we probably have a
		// payment lookup, for example, for valuelink upgrade alphas,
		// see src/tet/resources/xml/dti/UpgradeAlphaRequest-VLINK.xml
		Iterator<PaymentLookupTO> payLookIter = dtiTxn.getPaymentLookupTOList()
				.iterator();
		while (payLookIter.hasNext()) {
			PaymentLookupTO dtiPayLookupTO = payLookIter.next();
			// SET "OutputRoot"."XML"."Envelope"."Body"."Payments"."Payment"."PaymentCode" = Environment.Variables.PAYMENT.LOOKUP_VALUE;
			// SET "OutputRoot"."XML"."Envelope"."Body"."Payments"."Payment"."Description" = Environment.Variables.PAYMENT.PYMT_CODE;
			// counter-intuitive, paymentCode is lookup value, description is paymentCode...whee!
			String paymentCode = dtiPayLookupTO.getLookupValue();
			String description = dtiPayLookupTO.getPymtCode();
			GWPaymentTO gwPayTo = new GWPaymentTO(paymentCode, description,
					getTotalTicketPrice(tktActTicketTO));
			tktActTicketTO.addPayment(gwPayTo);
		}

		// Set the source ID to the TS Mac
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller()
				.getTsMac();
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

    // Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
    headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader()
				.getPayloadID());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_TKT_ACTIVATE_TYPE);
		envelopeTO.setHeaderTO(headerTO);
		envelopeTO.setBodyTO(bodyTO);
		xmlReqStr = GWEnvelopeXML.getXML(envelopeTO);

		return xmlReqStr;

	}

	/**
	 * Convenience method for calculating the total price of tickets in a ticket activation request
	 * 
	 * @param tktActTO
	 * @return String representation of the total ticket price
	 */
	private static String getTotalTicketPrice(GWTicketActivationRqstTO tktActTO) {
		return getTotalTicketPrice(tktActTO.getTicketList());
	}

	/**
	 * Convenience method for calculating the total price of tickets in a ticket activation request
	 * 
	 * @param tktList
	 * @return String representation of the total ticket price
	 */
	private static String getTotalTicketPrice(ArrayList<GWTicketTO> tktList) {
		BigDecimal totalPrice = new BigDecimal("0.00");
		Iterator<GWTicketTO> ticketIter = tktList.iterator();
		while (ticketIter.hasNext()) {
			GWTicketTO ticket = ticketIter.next();
			BigDecimal tktPrice = new BigDecimal(ticket.getPrice());

			totalPrice = totalPrice.add(tktPrice);
		}

		return totalPrice.toPlainString();
	}

	/**
	 * Transforms an egal ticket activation response into a DTI upgrade alpha ticket response.
	 * 
	 * 
	 * @param dtiTxn
	 *            the dti txn
	 * @param xmlResponse
	 *            the xml response
	 * 
	 * @return the DTI transaction to
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn,
			String xmlResponse) throws DTIException {
		// NOTE, here we almost certainly have more reuse on transformations
		GWEnvelopeTO gwRespTO = GWEnvelopeXML.getTO(xmlResponse);

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules
				.createRespPayloadHdr(dtiTxn);
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);

		dtiRespTO.setPayloadHeader(payloadHdrTO);
		dtiRespTO.setCommandHeader(commandHdrTO);

		// Search for blatant error first
		GWBodyTO gwBodyTO = gwRespTO.getBodyTO();
		if (gwBodyTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null body.");
		}

		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();

		if (gwStatusTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status.");
		}

		String statusString = gwStatusTO.getStatusCode();
		if (statusString == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status code.");
		}

		// Get the provider response status code
		int statusCode = -1;

		try {
			statusCode = Integer.parseInt(statusString);
			dtiRespTO.setProviderErrCode(statusString);
		}
		catch (NumberFormatException e) {
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Provider responded with a non-numeric status code.");
		}

		// If the provider had an error, map it and generate the response.
		// Copy the ticket identity and default the TktStatus Voidable to No
		if (statusCode != 0) {
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,
					xmlResponse);
			return dtiTxn;
		}

		// 2011-12-09; cus: removed as the var is never read. Delete after verify that it's ok.
		// GWTicketActivationRespTO gwTktActRespTO = gwBodyTO.getTicketActivationResponseTO();

		// If the provider had no error, transform the response.+
		UpgradeAlphaResponseTO uaResp = new UpgradeAlphaResponseTO();
		// ArrayList<TicketTO> dtiTktTOList = new ArrayList<TicketTO>();

		// Set ticket identifier (for DLR it's External) on the dti ticket list
		// egal does not return this on success (unlike ATS), so if we have
		// no errors, we assume what came in is what should go out, and we
		// grab it from the request object to set on the response
		UpgradeAlphaRequestTO uaReq = (UpgradeAlphaRequestTO) dtiTxn
				.getRequest().getCommandBody();

		ArrayList<TicketTO> tktReqList = uaReq.getTktList();
		Iterator<TicketTO> tktReqIter = tktReqList.iterator();

		while (tktReqIter.hasNext()) {
			TicketTO dtiTktTO = tktReqIter.next();
			uaResp.addTicket(dtiTktTO);
		}

		dtiRespTO.setCommandBody(uaResp);
		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * Transforms a DLR Ticket Activation (Upgrade Alpha) Ticket error into a DTI Transaction Object
	 * 
	 * @param dtiTxn
	 *            the dti txn
	 * @param dtiRespTO
	 *            the dti resp to
	 * @param statusString
	 *            the status string
	 * 
	 * @return the DTI transaction to
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	static DTITransactionTO transformError(DTITransactionTO dtiTxn,
			DTIResponseTO dtiRespTO, String statusString, String xmlResponse) throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(
				dtiTxn.getTransactionType(), statusString, xmlResponse);

		// DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(statusString);
		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);

		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

			UpgradeAlphaRequestTO uaReq = (UpgradeAlphaRequestTO) dtiTxn
					.getRequest().getCommandBody();
			UpgradeAlphaResponseTO uaResp = new UpgradeAlphaResponseTO();

			TicketTO dtiTktTO = new TicketTO();

			// Tkt Item
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			// Tkt Identity
			if (uaReq.getTktList() != null && uaReq.getTktList().size() != 0) {
				Iterator<TicketTO> tktListIter = uaReq.getTktList().iterator();
				while (tktListIter.hasNext()) {
					uaResp.addTicket(tktListIter.next());
				}
			}

			// Add the ticket to the response
			dtiRespTO.setCommandBody(uaResp);

		}

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.
	 * 
	 * @param dtiTxn
	 *            the dti txn
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static void applyDLRUpgradeAlphaRules(DTITransactionTO dtiTxn) throws DTIException {
		UpgradeAlphaRequestTO reqTO = (UpgradeAlphaRequestTO) dtiTxn
				.getRequest().getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();

		// TicketRules.validateMaxEightTicketsOnRequest(aTktList);
		// this is applied before we get here

		// NOTE: although dlr only gets 1 on a vlink request, we should support 8 alphas
		// so we iterate through rather than just taking the one and only
		// ticket on the list.
		Iterator<TicketTO> ticketIter = aTktList.iterator();
		while (ticketIter.hasNext()) {
			TicketRules.validateExternalTktIdOnly(ticketIter.next());
		}

	}

}
