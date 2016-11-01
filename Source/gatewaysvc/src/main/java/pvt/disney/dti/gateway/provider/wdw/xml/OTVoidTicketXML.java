package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTVoidTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Void Ticket Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 */
public class OTVoidTicketXML {

	/**
	 * Adds the transaction body element for void ticket to the command stanza.
	 * 
	 * @param otVoidTktTO
	 *            The Omni Ticket Void Ticket Transfer Object
	 * @param commandStanza
	 *            the command stanza which needs the void ticket section.
	 */
	static void addTxnBodyElement(OTVoidTicketTO otVoidTktTO,
			Element commandStanza) {

		// VoidTicket
		Element voidTktStanza = commandStanza.addElement("VoidTicket");

		// WorkRules
		ArrayList<String> tagList = otVoidTktTO.getTagsList();
		OTCommandXML.addWorkRules(voidTktStanza, tagList);

		// SiteNumber
		voidTktStanza.addElement("SiteNumber").addText(
				otVoidTktTO.getSiteNumber().toString());

		// TicketInfo
		ArrayList<OTVTicketTO> tktList = otVoidTktTO.getVTicketList();
		for /* each */(OTVTicketTO otVTicket : /* in */tktList) {

			Element vTicketStanza = voidTktStanza.addElement("VTicket");

			// Item
			vTicketStanza.addElement("Item").addText(
					otVTicket.getItem().toString());

			// TicketSearchMode
			Element tktSrchModeStanza = vTicketStanza
					.addElement("TicketSearchMode");

			OTTicketTO otTicket = otVTicket.getTicketSearchMode();
			ArrayList<TicketIDType> otTicketIdTypeList = otTicket
					.getTktIdentityTypes();
			TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

			switch (otTicketIdType) {

			case MAGCODE:
				tktSrchModeStanza.addElement("MagCode").addText(
						otTicket.getMagTrack());
				break;

			case TDSSN:
				Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");
				tdssnElement.addElement("Site")
						.setText(otTicket.getTdssnSite());
				tdssnElement.addElement("Station").setText(
						otTicket.getTdssnStation());
				GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnElement.addElement("Date").setText(tdssnDate);
				tdssnElement.addElement("TicketId").setText(
						otTicket.getTdssnTicketId());
				break;

			case TCOD:
				tktSrchModeStanza.addElement("TCOD")
						.addText(otTicket.getTCOD());
				break;

			case BARCODE:
				tktSrchModeStanza.addElement("BarCode").addText(
						otTicket.getBarCode());
				break;

			case EXTERNALTICKETCODE:
				tktSrchModeStanza.addElement("ExternalTicketCode").addText(
						otTicket.getExternalTicketCode());
				break;
			}

			// TicketNote (ignored)
			// VoidCode (ignored)
			// DeleteDemographic (ignored)
		}

		// PaymentInfo
		OTCommandXML.addPaymentInfoStanza(voidTktStanza,
				otVoidTktTO.getPaymentInfoList());

		// TransactionNode
		if ((otVoidTktTO.getTransactionNote() != null) && (otVoidTktTO
				.getTransactionNote().length() > 0)) voidTktStanza.addElement(
				"TransactionNote").setText(otVoidTktTO.getTransactionNote());

		// TaxExemptCode (ignored)
		// ZipCode (ignored)

		// InTransactionAttibute
		ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otVoidTktTO
				.getInTxnAttrList();

		OTCommandXML.addInTransactionAttributeStanzas(voidTktStanza,
				inTxnAttrList);

		// Generate Event
		if (otVoidTktTO.getGenerateEvent() != null) {
			voidTktStanza.addElement("GenerateEvent").addText(
					otVoidTktTO.getGenerateEvent().toString());
		}

		return;
	}

	/**
	 * When provided with the parsed XML, creates the transfer object for the void ticket.
	 * 
	 * @param voidTktNode
	 *            the parsed XML containing the void ticket stanzas.
	 * @return The Omni Ticket Void Ticket Transfer Object
	 * @throws DTIException
	 *             for any missing fields or parsing errors.
	 */
	@SuppressWarnings("unchecked")
	static OTVoidTicketTO getTO(Node voidTktNode) throws DTIException {

		OTVoidTicketTO otVoidTktTO = new OTVoidTicketTO();

		// Set error object
		OTErrorTO otErrorTO = OTCommandXML.getErrorTO(voidTktNode);
		otVoidTktTO.setError(otErrorTO);

		// TicketInfoVoid List
		List<Node> ticketInfoVoidNodeList = voidTktNode
				.selectNodes("TicketInfoVoid");
		if ((ticketInfoVoidNodeList != null) && (ticketInfoVoidNodeList.size() > 0)) setOTTicketInfoVoidTOList(
				otVoidTktTO.getVTicketList(), ticketInfoVoidNodeList);

		// PaymentInfo (ignored)

		// TransactionDSSN
		Node tranDssnNode = voidTktNode.selectSingleNode("TransactionDSSN");
		if (tranDssnNode != null) otVoidTktTO.setTransactionDSSN(OTCommandXML
				.setOTTransactionDSSNTO(tranDssnNode));

		// TransactionCOD
		Node tranCODNode = voidTktNode.selectSingleNode("TransactionCOD");
		if (tranCODNode != null) otVoidTktTO.setTransactionCOD(tranCODNode
				.getText());

		// TotalAmount (ignored)
		// TotalTax (ignored)
		// TaxExempt (ignored)

		return otVoidTktTO;
	}

	/**
	 * Sets the list of transfer objects for ticket info void when provided with the parsed XML.
	 * 
	 * @param otTktVoidList
	 *            The list of Omni Ticket Ticket Info Void Transfer Objects.
	 * @param ticketInfoVoidNodeList
	 *            the parsed XML containing the ticket info void stanzas.
	 * @throws DTIException
	 *             for any missing required fields or other parsing issues.
	 */
	private static void setOTTicketInfoVoidTOList(
			ArrayList<OTVTicketTO> otTktVoidList,
			List<Node> ticketInfoVoidNodeList) throws DTIException {

		List<Node> ticketList = ticketInfoVoidNodeList;

		for /* each */(Node aNode : /* in */ticketList) {

			OTVTicketTO otTktInfoTO = new OTVTicketTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(OTVoidTicketXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,Item element.");
			else {
				String inText = itemNode.getText();
				otTktInfoTO.setItem(new BigInteger(inText));
			}

			// ItemStatus
			Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
			if (itemStatusNode == null) throw new DTIException(
					OTVoidTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,ItemStatus element.");
			else {
				String inText = itemStatusNode.getText();
				otTktInfoTO.setItemStatus(new Integer(inText));
			}

			// TicketType (not displayed, but used for logging)
			Node ticketTypeNode = aNode.selectSingleNode("TicketType");
			if (ticketTypeNode == null) {
				otTktInfoTO.setProviderTicketType(new BigInteger("0"));
			}
			else {
				String inText = ticketTypeNode.getText();
				otTktInfoTO.setProviderTicketType(new BigInteger(inText));
			}

			// Price (not displayed, but used for logging)
			Node priceNode = aNode.selectSingleNode("Price");
			if (priceNode == null) {
				otTktInfoTO.setPrice(new BigDecimal("0.00"));
			}
			else {
				String inText = priceNode.getText();
				otTktInfoTO.setPrice(new BigDecimal(inText));
			}

			// Tax (ignored)
			// RemainingValue (ignored)
			// VoidCode (ignored)

			// Ticket (min 1 version, may be any)
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode == null) throw new DTIException(
					OTVoidTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfoVoid,Ticket element.");
			otTktInfoTO.setTicket(setOTTicketTO(ticketNode));

			// Add the ticket to the list.
			otTktVoidList.add(otTktInfoTO);

		}

		return;
	}

	/**
	 * A void only version of the unmarshalling required because the void response permissions are much more permissive than other commands. Sets the transfer object based upon the parsed XML provided.
	 * 
	 * @param ticketNode
	 *            the parsed XML section containing the ticket stanzas.
	 * @return The Omni Ticket Ticket Transfer Objects.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	private static OTTicketTO setOTTicketTO(Node ticketNode) throws DTIException {

		OTTicketTO otTicket = new OTTicketTO();

		// TDSSN
		Node tdssnNode = ticketNode.selectSingleNode("TDSSN");
		if (tdssnNode != null) {

			// Site, Station, Date, TicketId
			Node siteNode = tdssnNode.selectSingleNode("Site");
			Node stationNode = tdssnNode.selectSingleNode("Station");
			Node dateNode = tdssnNode.selectSingleNode("Date");
			Node ticketIdNode = tdssnNode.selectSingleNode("TicketId");
			if ((siteNode == null) || (stationNode == null) || (dateNode == null) || (ticketIdNode == null)) throw new DTIException(
					OTVoidTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML with incomplete TDSSN element.");

			String dateString = dateNode.getText();
			String siteString = siteNode.getText();
			String stationString = stationNode.getText();
			String ticketIdString = ticketIdNode.getText();

			try {
				otTicket.setTDssn(dateString, siteString, stationString,
						ticketIdString);
			}
			catch (ParseException pe) {
				throw new DTIException(OTVoidTicketXML.class,
						DTIErrorCode.TP_INTERFACE_FAILURE,
						"Ticket provider returned XML with invalid TDSSN element.");
			}
		}

		// TCOD
		Node tcodNode = ticketNode.selectSingleNode("TCOD");
		if (tcodNode != null) otTicket.setTCOD(tcodNode.getText());

		// BarCode
		Node barCodeNode = ticketNode.selectSingleNode("BarCode");
		if (barCodeNode != null) otTicket.setBarCode(barCodeNode.getText());

		// MagCode
		Node magCodeNode = ticketNode.selectSingleNode("MagCode");
		if (magCodeNode != null) otTicket.setMagTrack(magCodeNode.getText());

		// ExternalTicketCode
		Node externalNode = ticketNode.selectSingleNode("ExternalTicketCode");
		if (externalNode != null) otTicket.setExternalTicketCode(externalNode
				.getText());

		return otTicket;
	}
}