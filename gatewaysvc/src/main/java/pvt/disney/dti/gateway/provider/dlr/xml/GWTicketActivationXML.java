package pvt.disney.dti.gateway.provider.dlr.xml;

import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketTO;
import pvt.disney.dti.gateway.rules.TransformConstants;

/**
 * The Class GWTicketActivationXML.
 */
public class GWTicketActivationXML {

	/**
	 * Adds the ticket activation element.
	 * 
	 * @param qtReqTO
	 *            the tkt act req to
	 * @param bodyElement
	 *            the body element
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static void addTicketActivationElement(
			GWTicketActivationRqstTO tktActReqTO, Element bodyElement) throws DTIException {

		Element ticketActivationElement = bodyElement
				.addElement("TicketActivation");

		if (tktActReqTO.getCommand() != null & tktActReqTO.getCommand()
				.length() > 0) {
			ticketActivationElement.addElement("Command").addText(
					tktActReqTO.getCommand());
		}
		String customerID = tktActReqTO.getCustomerID();
		if (customerID == null) {
			throw new DTIException(GWBodyXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"Ticket Activation Request did not have a customer ID specified.");
		}

		ticketActivationElement.addElement("CustomerID").addText(customerID);

		if (tktActReqTO.getTicketList().size() == 0) {
			throw new DTIException(GWBodyXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"Ticket Activation Request did not include any tickets.");
		}

		// BEGIN ITERATION THROUGH TICKETS
		Iterator<GWTicketTO> ticketIter = tktActReqTO.getTicketList()
				.iterator();
		while (ticketIter.hasNext()) {
			GWTicketTO dlrTicket = ticketIter.next();
			// check for visual id before trying to add it
			if (dlrTicket.getVisualID() == null) {
				throw new DTIException(
						GWBodyXML.class,
						DTIErrorCode.INVALID_MSG_CONTENT,
						"A ticket in the ActivateTicket Cancel (VoidTicket) request did not have an (external) visualID specified.");
			}
			else {
				org.dom4j.Element tickets = DocumentHelper
						.createElement("Tickets");
				org.dom4j.Element ticket = DocumentHelper
						.createElement("Ticket");

				// add the visual id, this is needed for both activates and cancel commands
				ticket.addElement("VisualID").addText(dlrTicket.getVisualID());

				// BEGIN TICKET LOGIC FOR ACTIVATE (UPGRADE ALPHA)
				// check if this is an activate, if so we need to add additional fields to activate
				// a ticket (i.e. upgrade alpha)

				if (tktActReqTO.getCommand().equals(
						TransformConstants.GW_COMMAND_ACTIVATE)) {
					// check for itemCode before trying to add it
					if (dlrTicket.getItemCode() == null) {
						throw new DTIException(
								GWBodyXML.class,
								DTIErrorCode.INVALID_MSG_CONTENT,
								"A ticket in the ActivateTicket Activate (UpgradeAlpha) request did not have an itemCode specified.");
					}
					else {

						ticket.addElement("ItemCode").addText(
								dlrTicket.getItemCode());
					}
					// check for price before trying to add it
					if (dlrTicket.getPrice() == null) {
						throw new DTIException(
								GWBodyXML.class,
								DTIErrorCode.INVALID_MSG_CONTENT,
								"A ticket in the ActivateTicket Activate (UpgradeAlpha) request did not have a price specified.");

					}
					else {
						ticket.addElement("Price")
								.addText(dlrTicket.getPrice());
					}
				}

				// add the ticket to the tickets clause
				tickets.add(ticket);
				// add the tickets clause to the ticketActivationClause
				ticketActivationElement.add(tickets);

			}// END TICKET LOGIC FOR ACTIVATE (UPGRADE ALPHA)

		} // END ITERATION THROUGH TICKETS

		// CHECK AGAIN IF WE ARE AN ACTIVATE (UPGRADE ALPHA) COMMAND
		// add the payments info for the activate (upgrade alpha)
		// create the payments element
		if (tktActReqTO.getCommand().equals(
				TransformConstants.GW_COMMAND_ACTIVATE)) {
			if (tktActReqTO.getPaymentList().size() == 0) {
				throw new DTIException(GWBodyXML.class,
						DTIErrorCode.INVALID_MSG_CONTENT,
						"Ticket Activation Request did inlclude any payments.");
			}

			org.dom4j.Element payments = DocumentHelper
					.createElement("Payments");
			// BEGIN ITERATION THROUGH Payments
			Iterator<GWPaymentTO> paymentIter = tktActReqTO.getPaymentList()
					.iterator();

			while (paymentIter.hasNext()) {
				GWPaymentTO payment = paymentIter.next();

				// check that needed fields are present, this is ugly refactor a field validation method
				if (payment.getPaymentCode() == null || payment
						.getPaymentCode().length() == 0 || payment
						.getDescription() == null || payment.getDescription()
						.length() == 0 || payment.getAmount() == null || payment
						.getAmount().length() == 0) {
					throw new DTIException(
							GWBodyXML.class,
							DTIErrorCode.INVALID_MSG_CONTENT,
							"Ticket Activation Request missing required PaymentCode, Description, or Amount");
				}
				// create the payment element
				Element paymentElement = DocumentHelper
						.createElement("Payment");
				paymentElement.addElement("PaymentCode").addText(
						payment.getPaymentCode());
				paymentElement.addElement("Description").addText(
						payment.getDescription());
				paymentElement.addElement("Amount")
						.addText(payment.getAmount());
				// add the element to the payments element clause
				payments.add(paymentElement);
			}
			bodyElement.add(payments);
		}
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
	private static GWStatusTO setStatusTO(Element statusElement) throws DTIException {

		GWStatusTO gwStatusTO = new GWStatusTO();

		// get the status fields and set them in the status to
		org.dom4j.XPath statusCodeSelector = DocumentHelper
				.createXPath("//Status/StatusCode");
		String statusCode = statusCodeSelector.selectSingleNode(statusElement)
				.getText();

		org.dom4j.XPath statusTextSelector = DocumentHelper
				.createXPath("//Status/StatusText");
		String statusText = statusTextSelector.selectSingleNode(statusElement)
				.getText();

		gwStatusTO.setStatusCode(statusCode);
		gwStatusTO.setStatusText(statusText);

		if (gwStatusTO.getStatusCode() == null) throw new DTIException(
				GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned Status clause missing a StatusCode.");

		if (gwStatusTO.getStatusText() == null) throw new DTIException(
				GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned Status clause missing a StatusText.");

		return gwStatusTO;
	}

	/**
	 * Sets the resp body to.
	 * 
	 * @param gwBodyTO
	 *            the gw body to
	 * @param bodyElement
	 *            the body element
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {

		String element = bodyElement.asXML();
		// get the status fields and set them in the status to
		org.dom4j.XPath statusSelector = DocumentHelper.createXPath("./Status");
		Node statusNode = statusSelector.selectSingleNode(bodyElement);
		if (statusNode != null) {
			GWStatusTO statusTO = setStatusTO(bodyElement);
			gwBodyTO.setStatusTO(statusTO);
		}

		// get the error fields and set them in the error to

		org.dom4j.XPath errorSelector = DocumentHelper
				.createXPath("//TicketActivationErrors");
		Node ErrorNode = errorSelector.selectSingleNode(bodyElement);

		if (ErrorNode != null) {
			GWTicketActivationErrorsTO errorsTo = setErrorsTO(bodyElement);
			gwBodyTO.setTicketActivationtErrorsTO(errorsTo);

		}

		if (gwBodyTO.getStatusTO() == null) {
			throw new DTIException(GWBodyXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned TicketActivationResponse without a Status clause.");
		}

		if (gwBodyTO.getStatusTO().getStatusCode().equals("0")) {
			GWTicketActivationRespTO respTo = new GWTicketActivationRespTO();
			gwBodyTO.setTicketActivationResponseTO(respTo);
		}

		if ((gwBodyTO.getTicketActivationResponseTO()) == null && (gwBodyTO
				.getTicketActivationtErrorsTO() == null)) {
			throw new DTIException(
					GWBodyXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned TicketActivationResponse without a Error or Response clauses.");
		}
	}

	/**
	 * Unmarshall the errors element into the gateway ticket activation errors transfer object.
	 * 
	 * @param errorsElement
	 *            as provided by the parsed eGalaxy response.
	 * @return the gateway ticket activations errors transfer object.
	 * @throws DTIException
	 *             should any unmarshalling problem occurs.
	 */
	private static GWTicketActivationErrorsTO setErrorsTO(Element bodyElement) throws DTIException {
		GWTicketActivationErrorsTO errorsTO = new GWTicketActivationErrorsTO();

		// should have body incoming
		// first set status code and text as "master top level error"
		org.dom4j.XPath topLevelCodeSelector = DocumentHelper
				.createXPath("//StatusCode");
		String topLevelCode = topLevelCodeSelector
				.selectSingleNode(bodyElement).getText();

		org.dom4j.XPath topLevelTextSelector = DocumentHelper
				.createXPath("//StatusText");
		String topLevelText = topLevelTextSelector
				.selectSingleNode(bodyElement).getText();

		errorsTO.setErrorCode(topLevelCode);
		errorsTO.setErrorText(topLevelText);

		// now get TicketActivationErrors (can contain multiple TicketActivationError elements)
		// lookat at each TicketActivationErrors.TicketActivationError
		// TicketActivationError.VisialID
		// TicketActivationError.StatusCode
		// TicketActivationError.StatusText
		org.dom4j.XPath ticketErrorSelector = DocumentHelper
				.createXPath("//TicketActivationError");
		List<org.dom4j.Element> ticketErrorList = ticketErrorSelector
				.selectNodes(bodyElement);

		Iterator<org.dom4j.Element> ticketErrorIter = ticketErrorList
				.iterator();

		while (ticketErrorIter.hasNext()) {
			org.dom4j.Element ticketError = ticketErrorIter.next();
			org.dom4j.XPath ticketIdSelector = DocumentHelper
					.createXPath("//VisualID");
			String ticketId = ticketIdSelector.selectSingleNode(ticketError)
					.getText();
			if (ticketId == null) {
				ticketId = "";
			}

			org.dom4j.XPath ticketStatusCodeSelector = DocumentHelper
					.createXPath("//StatusCode");
			String ticketStatusCode = ticketStatusCodeSelector
					.selectSingleNode(ticketError).getText();

			org.dom4j.XPath ticketStatusTextSelector = DocumentHelper
					.createXPath("//StatusText");
			String ticketStatusText = ticketStatusTextSelector
					.selectSingleNode(ticketError).getText();

			// null visual ID
			errorsTO.addError(ticketId, ticketStatusCode, ticketStatusText);
		}

		if (errorsTO.getErrorCode() == null) {
			throw new DTIException(
					GWHeaderTO.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned TicketActivationErrors.TicketActivationError.Error clause without StatusCode clause.");
		}

		if (errorsTO.getErrorText() == null) {
			throw new DTIException(
					GWHeaderTO.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned TicketActivationErrors.TicketActivationError.Error clause without StatisText clause.");
		}

		return errorsTO;
	}

	/**
	 * Unmarshall the ticket activation response element into the gateway ticket activation response transfer object.
	 * 
	 * @param aRespTO
	 *            the void ticket response element provided in the parsed response.
	 * @return the gateway ticket activation response transfer object.
	 * @throws DTIException
	 *             should any unmarshalling problem occur.
	 */
	static GWTicketActivationRespTO setRespTO(Element aRespTO) throws DTIException {
		GWTicketActivationRespTO tktActRespTO = new GWTicketActivationRespTO();
		return tktActRespTO;
	}
}
