package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketTO;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.TicketRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * The Class DLRVoidTicketRules. This class provides the transformation from a DTI Void Ticket into a DLR eGalaxy Ticket Activation message used to void a DLR ticket.
 */
public class DLRVoidTicketRules implements TransformConstants {

	/**
	 * Transforms a DTI Void Ticket transaction into a eGalaxy DLR void request, which is a TicketActivationRequest (yes, to void you use an activate message, counter-intuitive as that may seem).
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
		// Set the Ticket ID (should be only one in the array, since we only support 1)
		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
				GWEnvelopeTO.GWTransactionType.TICKETACTIVATION);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWTicketActivationRqstTO tktActTicketReqTO = new GWTicketActivationRqstTO();
		// set the command for the ticket activations so we know if this is a
		// cancel (ie void) or activate (ie upgrade alpha)
		tktActTicketReqTO.setCommand(TransformConstants.GW_COMMAND_CANCEL);

		VoidTicketRequestTO voidReq = (VoidTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		ArrayList<TicketTO> ticketList = voidReq.getTktList();
		Iterator<TicketTO> ticketIter = ticketList.iterator();
		while (ticketIter.hasNext()) {
			TicketTO dtiTicket = ticketIter.next();
			GWTicketTO dlrTicket = new GWTicketTO(dtiTicket.getExternal());
			tktActTicketReqTO.addTicket(dlrTicket);
		}
		tktActTicketReqTO.setCustomerID(dtiTxn.getEntityTO().getCustomerId());
		bodyTO.setTicketActivationRqstTO(tktActTicketReqTO);

		// Set the source ID to the TS Mac
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller()
				.getTsMac();
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader()
				.getPayloadID());

    // Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
    headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_TKT_ACTIVATE_TYPE);

		envelopeTO.setHeaderTO(headerTO);
		envelopeTO.setBodyTO(bodyTO);
		xmlRequest = GWEnvelopeXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * Transforms an egal ticket activation response into a DTI void ticket response.
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

		// If the provider had no error, transform the response.
		TicketTO dtiTktTO = new TicketTO();
		// GWTicketActivationRespTO gwTktActRespTO = gwBodyTO.getTicketActivationResponseTO();

		// Set TktItem: Always only one.
		dtiTktTO.setTktItem(new BigInteger(ITEM_1));

		// Set ticket identifier (for DLR it's External)
		// egal does not return this on success (unlike ATS), so if we have
		// no errors, we assume what came in is what should go out, and we
		// grab it from the request object to set on the response
		VoidTicketRequestTO voidReq = (VoidTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		// we only support one at this time
		TicketTO tktto = voidReq.getTktList().get(0);

		dtiTktTO.setExternal(tktto.getExternal());
		VoidTicketResponseTO voidResp = new VoidTicketResponseTO();
		voidResp.addTicket(dtiTktTO);
		dtiRespTO.setCommandBody(voidResp);

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * Transforms a DLR Void Ticket error into a DTI Transaction Object
	 * 
	 * @param dtiTxn
	 *            The DTITransacationTO object representing this transaction.
	 * @param dtiRespTO
	 *            The parsed version of the received response.
	 * @param statusString
	 *            get to get the TP provider error information.
	 * @throws DTIException
	 *             Should the routine be unable to find the TP error lookup.
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

			VoidTicketRequestTO voidReq = (VoidTicketRequestTO) dtiTxn
					.getRequest().getCommandBody();
			VoidTicketResponseTO voidResp = new VoidTicketResponseTO();

			TicketTO dtiTktTO = new TicketTO();

			// Tkt Item
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			// Tkt Identity
			if (voidReq.getTktList().size() != 0) {
				TicketTO dtiTktTOReq = voidReq.getTktList().get(0);
				if (dtiTktTOReq != null) dtiTktTO.setExternal(dtiTktTOReq
						.getExternal());
			}

			// Add the ticket to the response
			voidResp.addTicket(dtiTktTO);

			dtiRespTO.setCommandBody(voidResp);

		}

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object
	 * @throws DTIException
	 *             if any rule fails.
	 */
	public static void applyDLRVoidTicketRules(DTITransactionTO dtiTxn) throws DTIException {

		VoidTicketRequestTO reqTO = (VoidTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();
		TicketTO aTicketTO = aTktList.get(0);

		TicketRules.validateExternalTktIdOnly(aTicketTO);

		return;
	}

}
