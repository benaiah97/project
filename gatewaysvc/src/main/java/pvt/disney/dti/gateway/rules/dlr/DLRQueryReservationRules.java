package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigInteger;
import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryReservationResponseTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderRespTO.TicketRecord;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * 
 * @author lewit019
 * 
 */
public class DLRQueryReservationRules implements TransformConstants {

	public static final String ENTITLEMENTPROD = "EntitlementProduct";

	/**
	 * Transforms the request in the DTI transfer object into a valid eGalaxy request string.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object
	 * @return eGalaxy request string
	 * @throws DTIException
	 *             should a problem with conversion occur
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
				GWEnvelopeTO.GWTransactionType.QUERYORDER);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWQueryOrderRqstTO qryOrdReqTO = new GWQueryOrderRqstTO();

		QueryReservationRequestTO qryResReq = (QueryReservationRequestTO) dtiTxn
				.getRequest().getCommandBody();

		// Set the Order ID
		qryOrdReqTO.setOrderID(qryResReq.getResCode());

		// Set the body
		bodyTO.setQueryOrderRqstTO(qryOrdReqTO);

		// Set the source ID to the TS MAC
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller()
				.getTsMac();
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader()
				.getPayloadID());

		// Set the time stamp to the GMT date/time now.
		headerTO.setTimeStamp(DateTimeRules.getGMTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_QRY_ORD_MSG_TYPE);

		xmlRequest = GWEnvelopeXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * Transforms the eGalaxy XML response string into response objects within the DTI transfer object.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object without any response information from eGalaxy.
	 * @param xmlResponse
	 *            the eGalaxy XML response string
	 * @return the DTI transfer object that has response information from eGalaxy.
	 * @throws DTIException
	 *             should any issues during conversion occur
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn,
			String xmlResponse) throws DTIException {

		// Parse the string into the Gateway Transfer Object!!
		GWEnvelopeTO gwRespTO = GWEnvelopeXML.getTO(xmlResponse);

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules
				.createRespPayloadHdr(dtiTxn);
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);
		QueryReservationRequestTO dtiReqTO = (QueryReservationRequestTO) dtiTxn
				.getRequest().getCommandBody();

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
			// Note that on successes, this eGalaxy command doesn't return any status
			// items
			gwStatusTO = new GWStatusTO();
			gwStatusTO.setStatusCode("0");
			gwStatusTO.setStatusText("No error.  Command Successful.");
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
		// Copy the ticket identity and default the TktStatus voidable to No
		if (statusCode != 0) {
			// we need to parse beyond just the status code so we can determine if
			// we need to throw based on an errorCode in the tickets clause rather
			// than just on the statusCode...this will keep us from returning
			// an invalid ticket error because we have a status code of 1300 when
			// down farther in the QueryTicketsElement we might have a 999 error,
			// which indicates that the error actually occurred because of a critical
			// failure inside of eGalaxy itself.

			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,
					xmlResponse);
			return dtiTxn;
		}

		// If the provider had no error, transform the response.
		GWQueryOrderRespTO gwQryOrdResp = gwBodyTO.getQueryOrderRespTO();
		QueryReservationResponseTO qryResRespTO = new QueryReservationResponseTO();

		// Set the response type.
		// In future, this may be multiple "styles" of response, but for right now,
		// it's only "EntitlementProduct".
		qryResRespTO.setResponseType(ENTITLEMENTPROD);

		// Get the original reservation identifier
		String eGalResCode = dtiReqTO.getResCode();
		ReservationTO resTO = new ReservationTO();
		resTO.setResCode(eGalResCode);
		qryResRespTO.setReservation(resTO);

		// Get the ticket array
		ArrayList<TicketRecord> gwTicketArray = gwQryOrdResp.getTicketArray();
		if (gwTicketArray.size() > 0) {
			ArrayList<TicketTO> dtiTicketArray = qryResRespTO.getTicketList();
			int dtiTktItem = 1;

			for /* each */(TicketRecord aGwTicket : /* in */gwTicketArray) {
				TicketTO dtiTicket = new TicketTO();
				dtiTicket.setTktItem(new BigInteger(Integer
						.toString(dtiTktItem)));
				dtiTicket.setBarCode(aGwTicket.getVisualID());

				// If efficiency becomes a problem, then this query could be
				// turned into a single query run for all the components returning, but
				// for right now, given the limited use, this query is called once
				// per visual ID/PLU, even if the PLU's are repeated.
				// JTL 04/24/2012
				String dtiProduct = ProductKey
						.getProductFromTicketType(aGwTicket.getPlu());

				if (dtiProduct == null) {
					dtiTicket.setProdCode(aGwTicket.getPlu());
				}
				else {
					dtiTicket.setProdCode(dtiProduct);
				}
				dtiTicketArray.add(dtiTicket);
				dtiTktItem++;
			}
		}

		dtiRespTO.setCommandBody(qryResRespTO);

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * If errors are detected in the eGalaxy response, transform that response string using the appropriate error response values for DTI.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object without any response information from eGalaxy.
	 * @param dtiRespTO
	 * @param statusString
	 * @param xmlResponse
	 *            the eGalaxy XML response string
	 * @return the DTI transfer object with error information based on the xmlResponse
	 * @throws DTIException
	 */
	static DTITransactionTO transformError(DTITransactionTO dtiTxn,
			DTIResponseTO dtiRespTO, String statusString, String xmlResponse) throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(
				dtiTxn.getTransactionType(), statusString, xmlResponse);

		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);

		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.COMMAND) {

			if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {
				ReservationRequestTO resReq = (ReservationRequestTO) dtiTxn
						.getRequest().getCommandBody();
				ReservationResponseTO resResp = new ReservationResponseTO();

				TicketTO dtiTktTO = new TicketTO();

				// Ticket Item
				dtiTktTO.setTktItem(new BigInteger(ITEM_1));

				// Ticket Identity
				if (resReq.getTktList().size() != 0) {
					TicketTO dtiTktTOReq = resReq.getTktList().get(0);
					if (dtiTktTOReq != null) dtiTktTO.setExternal(dtiTktTOReq
							.getExternal());
				}

				// Add the ticket to the response
				resResp.addTicket(dtiTktTO);

				dtiRespTO.setCommandBody(resReq);
			}
		}

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

}
