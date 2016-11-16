package pvt.disney.dti.gateway.provider.hkd.xml;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;

/**
 * This class is responsible for transforming a parsed Omni Ticket XML header into a transfer object, and back again. All methods are intentionally limited to package visibility.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTHeaderXML {

	/**
	 * Adds the header element to the command XML stanza based on the transfer object provided.
	 * 
	 * @param otHdrTO
	 *            The Omni Ticket Header Transfer Object.
	 * @param commandStanza
	 *            The command stanza which needs the header added.
	 * @throws DTIException
	 *             for any missing fields.
	 */
	static void addHeaderElement(HkdOTHeaderTO otHdrTO, Element commandStanza) throws DTIException {

		Element headerElement = commandStanza.addElement("Header");

		// ReferenceNumber
		if (otHdrTO.getReferenceNumber() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a ReferenceNumber.  Can't render XML.");
		else headerElement.addElement("ReferenceNumber").addText(
				otHdrTO.getReferenceNumber());

		// RequestType
		if (otHdrTO.getRequestType() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a RequestType.  Can't render XML.");
		else headerElement.addElement("RequestType").addText(
				otHdrTO.getRequestType());

		// RequestSubType
		if (otHdrTO.getRequestSubType() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a RequestSubType.  Can't render XML.");
		else headerElement.addElement("RequestSubType").addText(
				otHdrTO.getRequestSubType());

		// OperatingArea
		if (otHdrTO.getOperatingArea() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a OperatingArea.  Can't render XML.");
		else headerElement.addElement("OperatingArea").addText(
				otHdrTO.getOperatingArea());

		// UserId
		if (otHdrTO.getUserId() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a UserId.  Can't render XML.");
		else headerElement.addElement("UserId").addText(
				otHdrTO.getUserId().toString());

		// Password
		if (otHdrTO.getPassword() == null) throw new DTIException(
				HkdOTHeaderXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
				"OTHeaderTO doesn't have a Password.  Can't render XML.");
		else headerElement.addElement("Password").addText(
				otHdrTO.getPassword().toString());

		return;
	}

	/**
	 * Gets the header transfer object based on the parsed XML provided.
	 * 
	 * @param headerNode
	 *            the parsed XML section containing the header.
	 * @return The Omni Ticket Header Transfer Object.
	 * @throws DTIException
	 *             for any missing required fields or for other parsing errors.
	 */
	static HkdOTHeaderTO getTO(Node headerNode) throws DTIException {

	  HkdOTHeaderTO otHdrTO = new HkdOTHeaderTO();

		// ReferenceNumber
		Node referenceNumber = headerNode.selectSingleNode("ReferenceNumber");

		if (referenceNumber == null) {

			throw new DTIException(HkdOTHeaderXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a Header,ReferenceNumber element.");

		}
		else {

			String prefixedRefNum = referenceNumber.getText();
			String refNum = null;
			try {
				refNum = prefixedRefNum.substring(3);
			}
			catch (IndexOutOfBoundsException ioobe) {
				throw new DTIException(HkdOTHeaderXML.class,
						DTIErrorCode.MESSAGE_PARAMETER_INVALID,
						"Ticket provider returned a reference number that was less than three digits!");
			}
			otHdrTO.setReferenceNumber(refNum);

		}

		// RequestNumber
		Node requestNumber = headerNode.selectSingleNode("RequestNumber");
		if (requestNumber == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,RequestNumber element.");
		else {
			String inText = requestNumber.getText();
			otHdrTO.setRequestNumber(new Integer(inText));
		}

		// SessionId
		Node sessionId = headerNode.selectSingleNode("SessionId");
		if (sessionId == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,SessionId element.");
		else {
			String inText = sessionId.getText();
			otHdrTO.setSessionId(new Integer(inText));
		}

		// RequestType
		Node requestType = headerNode.selectSingleNode("RequestType");
		if (requestType == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,RequestType element.");
		else otHdrTO.setRequestType(requestType.getText());

		// RequestSubType
		Node requestSubType = headerNode.selectSingleNode("RequestSubType");
		if (requestSubType == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,RequestSubType element.");
		else otHdrTO.setRequestSubType(requestSubType.getText());

		// ValueMsg
		Node valueMsg = headerNode.selectSingleNode("ValueMsg");
		if (valueMsg == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,ValueMsg element.");

		// ValuedQty
		Node valuedQty = valueMsg.selectSingleNode("ValuedQty");
		if (valuedQty == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,ValueMsg,ValuedQty element.");
		else {
			String inText = valuedQty.getText();
			otHdrTO.setValueMsgValuedQty(new Integer(inText));
		}

		// NonValuedQty
		Node nonValuedQty = valueMsg.selectSingleNode("NonValuedQty");
		if (nonValuedQty == null) throw new DTIException(HkdOTHeaderXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,ValueMsg,NonValuedQty element.");
		else {
			String inText = nonValuedQty.getText();
			otHdrTO.setValueMsgNonValuedQty(new Integer(inText));
		}

		return otHdrTO;
	}

}
