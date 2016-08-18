package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigDecimal;
import java.util.Iterator;

import org.dom4j.Element;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryOrderRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * 
 * @author lewit019
 * 
 */
public class GWQueryOrderXML {

	/** The standard core logging mechanism. */
	private static EventLogger logger = EventLogger.getLogger(GWOrderXML.class);
	private static final GWQueryOrderXML logInstance = new GWQueryOrderXML();

	public static void addQueryOrderElement(GWQueryOrderRqstTO orderReqTO,
			Element bodyElement) throws DTIException {

		Element queryTicketElement = bodyElement.addElement("QueryOrder");

		String orderID = orderReqTO.getOrderID();
		if (orderID == null) throw new DTIException(GWBodyXML.class,
				DTIErrorCode.INVALID_MSG_CONTENT,
				"QueryOrder request did not have an order ID specified.");

		Element queryStanza = queryTicketElement.addElement("Query");
		queryStanza.addElement("OrderID").addText(orderID);

		Element dataRequestStanza = queryTicketElement
				.addElement("DataRequest");
		dataRequestStanza.addElement("Field").addText("Tickets");

		return;

	}

	/**
	 * 
	 * @param gwBodyTO
	 * @param bodyElement
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {

		//
		for (Iterator<org.dom4j.Element> i = bodyElement.elementIterator(); i
				.hasNext();) {
			Element element = i.next();

			if (element.getName().compareTo("Status") == 0) {
				GWStatusTO statusTO = setStatusTO(element);
				gwBodyTO.setStatusTO(statusTO);
			}

			if (element.getName().compareTo("QueryOrderResponse") == 0) {
				GWQueryOrderRespTO respTO = setRespTO(element);
				gwBodyTO.setQueryOrderRespTO(respTO);
			}

			if (element.getName().compareTo("QueryTicketErrors") == 0) {
				GWQueryOrderErrorsTO errorsTO = setErrorsTO(element);
				gwBodyTO.setQueryOrderErrorsTO(errorsTO);
			}

		}

	}

	/**
	 * @param queryOrderResponseElement
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	private static GWQueryOrderRespTO setRespTO(
			Element queryOrderResponseElement) throws DTIException {

		GWQueryOrderRespTO qtRespTO = new GWQueryOrderRespTO();

		if (queryOrderResponseElement == null) throw new DTIException(
				GWBodyXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned QueryTicketResponse without a Query Order Response clause.");

		for (Iterator<org.dom4j.Element> ordRespIter = queryOrderResponseElement
				.elementIterator(); ordRespIter.hasNext();) {
			Element queryOrderResponseChildElement = ordRespIter.next();

			// Tickets
			if (queryOrderResponseChildElement.getName().compareTo("Tickets") == 0) {
				Element ticketsElement = queryOrderResponseChildElement;

				// Iterate through tickets

				for (Iterator<org.dom4j.Element> ticketsListIter = ticketsElement
						.elementIterator(); ticketsListIter.hasNext();) {
					Element ticketElement = ticketsListIter.next();

					if (ticketElement.getName().compareTo("Ticket") == 0) {

						GWQueryOrderRespTO.TicketRecord gwTicketTO = qtRespTO.new TicketRecord();

						for (Iterator<org.dom4j.Element> ticketIter = ticketElement
								.elementIterator(); ticketIter.hasNext();) {
							Element ticketChildElement = ticketIter.next();

							// Visual ID
							if (ticketChildElement.getName().compareTo(
									"VisualID") == 0) {
								String visualID = ticketChildElement.getText();
								gwTicketTO.setVisualID(visualID);
							}

							// PLU
							if (ticketChildElement.getName().compareTo("PLU") == 0) {
								String plu = ticketChildElement.getText();
								gwTicketTO.setPlu(plu);
							}

							// Description
							if (ticketChildElement.getName().compareTo(
									"Description") == 0) {
								String description = ticketChildElement
										.getText();
								gwTicketTO.setDescription(description);
							}

							// Price
							if (ticketChildElement.getName().compareTo("Price") == 0) {
								String priceString = ticketChildElement
										.getText();
								if (priceString.contains(".")) gwTicketTO
										.setPrice(new BigDecimal(priceString));
								else gwTicketTO.setPrice(new BigDecimal(
										priceString + ".00"));
							}

						}

						qtRespTO.addTicketRecord(gwTicketTO);

					}
				}
			}
		}
		return qtRespTO;
	}

	/**
	 * Marshalls the gateway status transfer object.
	 * 
	 * @param statusElement
	 *            the response supplied by gateway.
	 * @return the Gateway Status Transfer Object.
	 * @throws DTIException
	 *             should any unmarshalling error occur.
	 */
	@SuppressWarnings("unchecked")
	private static GWStatusTO setStatusTO(Element statusElement) throws DTIException {

		GWStatusTO gwStatusTO = new GWStatusTO();

		for (Iterator<org.dom4j.Element> i = statusElement.elementIterator(); i
				.hasNext();) {
			Element element = i.next();

			if (element.getName().compareTo("StatusCode") == 0) {
				gwStatusTO.setStatusCode(element.getText());
			}

			if (element.getName().compareTo("StatusText") == 0) {
				gwStatusTO.setStatusText(element.getText());
			}

		}

		if (gwStatusTO.getStatusCode() == null) throw new DTIException(
				GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned Status clause missing a StatusCode.");

		if (gwStatusTO.getStatusText() == null) throw new DTIException(
				GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned Status clause missing a StatusText.");

		return gwStatusTO;
	}

	/**
	 * Unmarshall the errors element into the gateway query ticket errors transfer object.
	 * 
	 * @param errorsElement
	 *            as provided by the parsed eGalaxy response.
	 * @return into the gateway query ticket errors transfer object.
	 * @throws DTIException
	 *             should any unmarshalling problem occurs.
	 */
	private static GWQueryOrderErrorsTO setErrorsTO(Element errorsElement) throws DTIException {

		GWQueryOrderErrorsTO errorsTO = new GWQueryOrderErrorsTO();

		// the errorCode we will hand back after checking for special errors
		// if no special errors found, it is the statusCode
		String errorCode = null;
		// the error text for an errorCode (if found)...grabbed for logging
		String errorText = null;

		// response document to check for errors

		// if all went well we can start hunting for errors
		if (errorsElement != null) {
			logger.sendEvent(
					"QueryOrder Errors element:" + errorsElement.asXML(),
					EventType.DEBUG, logInstance);

			// and try to grab //QueryOrderErrors/Errors/Error/ErrorCode
			org.dom4j.Node queryOrderErrorCode = errorsElement
					.selectSingleNode("//Errors/Error/ErrorCode");

			// if we found one....
			if (queryOrderErrorCode != null && queryOrderErrorCode.getText() != null && queryOrderErrorCode
					.getText().length() > 0) {

				// grab the details for the error
				errorCode = queryOrderErrorCode.getText();

				// System.err.println("seeking errorText:");
				errorText = errorsElement.selectSingleNode(
						"//Errors/Error/ErrorText").getText();
				// and log it
				logger.sendEvent(
						"GWQueryOrderXML.setErrorsTO() found a QueryOrderError: " + errorCode + ":" + errorText,
						EventType.DEBUG, logInstance);
			}
			// if we didn't find QueryOrderErrors/Errors/Error/ErrorCode we need to
			// check for QueryOrderErrors/DataRequestErrors
			else if (errorsElement
					.selectSingleNode("//DataRequestErrors/DataRequestError/ErrorCode") != null) {
				// if we have a DataRequestError, grab its details
				errorCode = errorsElement.selectSingleNode(
						"//DataRequestErrors/DataRequestError/ErrorCode")
						.getText();
				errorText = errorsElement.selectSingleNode(
						"//DataRequestErrors/DataRequestError/ErrorText")
						.getText();
				// and log it
				logger.sendEvent(
						"GWQueryOrderXML.setErrorTO() found a DataRequestError: " + errorCode + ":" + errorText,
						EventType.DEBUG, logInstance);
			}

		}

		// check for bad XML construct
		if (errorCode == null) {
			throw new DTIException(
					GWHeaderTO.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned QueryTicketError,Errors,Error clause without ErrorCode clause.");
		}

		// check for bad XML construct
		if (errorText == null) {
			throw new DTIException(
					GWHeaderTO.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned QueryTicketError,Errors,Error clause without ErrorText clause.");
		}

		errorsTO.setErrorCode(errorCode);
		errorsTO.setErrorText(errorText);

		return errorsTO;

	}

}
