package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTMultiEntitlementAccountTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMultiEntitlementAccountTO.CommandType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket MultiEntitlementAccount Command.
 * 
 * Please note that all of its methods are either package or private level access.
 * 
 * 2.16.1
 * 
 * @author biest001
 */
public class OTMultiEntitlementAccountXML {

	/** The prefix of a CAS station, sometimes missing in requests. */
	public final static String CASPREFIX = "CAS";

	private static EventLogger logger = EventLogger
			.getLogger(OTQueryTicketXML.class);

	/**
	 * Adds the transaction body element of a MultiEntitlementAccount request to the XML of the command stanza.
	 * 
	 * @param otMultiEntitlementAccountTO
	 *            The Omni Ticket OTMultiEntitlementAccountTO Transfer Object.
	 * @param commandStanza
	 *            The XML command stanza.
	 */
	static void addTxnBodyElement(
			OTMultiEntitlementAccountTO otMultiEntitlementAccountTO,
			Element commandStanza) {

		// create the transaction body based on what NGE Command has been requested
		CommandType cmdType = otMultiEntitlementAccountTO.getCommandType();
		switch (cmdType) {
		case ManageMedia:
			manageMediaTxnBodyElement(otMultiEntitlementAccountTO,
					commandStanza);
			break;
		case CreateEntitlement:
			createEntitlementTxnBodyElement(otMultiEntitlementAccountTO,
					commandStanza);
			break;
		default:
			break;
		}

	}

	/**
	 * Adds the transaction body element of an AssociateMediaToAccount request to the XML of the command stanza.
	 * 
	 * @param otAssociateMediaToAccountTO
	 *            The Omni Ticket MultiEntitlementAccount - ManageMedia Transfer Object.
	 * @param commandStanza
	 *            The XML command stanza.
	 */

	static void manageMediaTxnBodyElement(
			OTMultiEntitlementAccountTO otAssociateMediaToAccountTO,
			Element commandStanza) {

		// ****************************
		// MultiEntitlementAccount
		Element multiEntitlementAccountStanza = commandStanza
				.addElement("MultiEntitlementAccount");

		// WorkRules
		ArrayList<String> tagList = otAssociateMediaToAccountTO.getTagsList();
		OTCommandXML.addWorkRules(multiEntitlementAccountStanza, tagList);

		// SiteNumber
		multiEntitlementAccountStanza.addElement("SiteNumber").addText(
				otAssociateMediaToAccountTO.getSiteNumber().toString());

		// CommandType (ManageMedia)
		multiEntitlementAccountStanza.addElement("CommandType").addText(
				otAssociateMediaToAccountTO.getCommandType().ManageMedia
						.toString());

		// GenerateEvent
		multiEntitlementAccountStanza.addElement("GenerateEvent").addText(
				"true");

		// ****************************
		// ManageMedia
		Element manageMediaStanza = multiEntitlementAccountStanza
				.addElement(otAssociateMediaToAccountTO.getCommandType().ManageMedia
						.toString());

		// ManageMedia - SubCommand
		manageMediaStanza.addElement("SubCommandType").addText(
				otAssociateMediaToAccountTO.getSubCommandType().Associate
						.toString());

		// ****************************
		// Associate
		Element associateStanza = manageMediaStanza
				.addElement(otAssociateMediaToAccountTO.getSubCommandType().Associate
						.toString());

		// ****************************
		// Existing Account Search Filter

		Element searchFilterStanza = associateStanza.addElement("SearchFilter");

		if ((otAssociateMediaToAccountTO.getExistingMediaId() != null) && (otAssociateMediaToAccountTO
				.getExistingMediaId().length() > 0)) {
			searchFilterStanza.addElement("ExistingMediaId").addText(
					otAssociateMediaToAccountTO.getExistingMediaId());
		}
		else if ((otAssociateMediaToAccountTO.getAccountId() != null) && (otAssociateMediaToAccountTO
				.getAccountId().length() > 0)) {
			searchFilterStanza.addElement("AccountId").addText(
					otAssociateMediaToAccountTO.getAccountId());
		}
		else if (otAssociateMediaToAccountTO.getTicketInfo() != null) {

			Element ticketSearchModeStanza = searchFilterStanza
					.addElement("TicketSearchMode");

			// The JAXB parsing routine for AssociateMediaToAccount set the property for
			// "Account Search by Ticket" using "setTicket"
			// and not "setTicketSearchMode". This is important to note.
			OTTicketInfoTO ticketInfo = otAssociateMediaToAccountTO
					.getTicketInfo();
			OTTicketTO ticket = ticketInfo.getTicket();

			if ((ticket.getBarCode() != null) && (ticket.getBarCode().length() > 0)) {
				ticketSearchModeStanza.addElement("BarCode").addText(
						ticket.getBarCode());
			}
			else if ((ticket.getMagTrack() != null) && (ticket.getMagTrack()
					.length() > 0)) {
				ticketSearchModeStanza.addElement("MagCode").addText(
						ticket.getMagTrack());
			}
			else if ((ticket.getTCOD() != null) && (ticket.getTCOD().length() > 0)) {
				ticketSearchModeStanza.addElement("TCOD").addText(
						ticket.getTCOD());
			}
			else if ((ticket.getExternalTicketCode() != null) && (ticket
					.getExternalTicketCode().length() > 0)) {
				ticketSearchModeStanza.addElement("ExternalTicketCode")
						.addText(ticket.getExternalTicketCode());
			}
			else if ((ticket.getTdssnSite() != null) && (ticket.getTdssnSite()
					.length() > 0)) {

				// DSSN
				Element tdssnStanza = ticketSearchModeStanza
						.addElement("TDSSN");

				tdssnStanza.addElement("Site").addText(ticket.getTdssnSite());
				tdssnStanza.addElement("Station").addText(
						ticket.getTdssnStation());

				GregorianCalendar tdssnDateCal = ticket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnStanza.addElement("Date").addText(tdssnDate);

				tdssnStanza.addElement("TicketId").addText(
						ticket.getTdssnTicketId());
			}

		}

		// ****************************
		// Media Data

		ArrayList<OTMediaDataTO> mediaData = otAssociateMediaToAccountTO
				.getMediaData();

		for (OTMediaDataTO media : mediaData) {

			Element mediaDataStanza = associateStanza.addElement("MediaData");

			mediaDataStanza.addElement("MediaId").addText(media.getMediaId());
			mediaDataStanza.addElement("MfrId").addText(media.getMfrId());
			mediaDataStanza.addElement("VisualId").addText(media.getVisualId());

		}

		return;
	}

	/**
	 * Adds the transaction body element of a TickerateEntitlement request to the XML of the command stanza.
	 * 
	 * @param otTickerateEntitlementTO
	 *            The Omni Ticket MultiEntitlementAccount - CreateEntitlement Transfer Object.
	 * @param commandStanza
	 *            The XML command stanza.
	 */

	static void createEntitlementTxnBodyElement(
			OTMultiEntitlementAccountTO otTickerateEntitlementTO,
			Element commandStanza) {

		// ****************************
		// MultiEntitlementAccount
		Element multiEntitlementAccountStanza = commandStanza
				.addElement("MultiEntitlementAccount");

		// WorkRules
		ArrayList<String> tagList = otTickerateEntitlementTO.getTagsList();
		OTCommandXML.addWorkRules(multiEntitlementAccountStanza, tagList);

		// SiteNumber
		multiEntitlementAccountStanza.addElement("SiteNumber").addText(
				otTickerateEntitlementTO.getSiteNumber().toString());

		// CommandType (CreateEntitlement)
		multiEntitlementAccountStanza.addElement("CommandType").addText(
				otTickerateEntitlementTO.getCommandType().CreateEntitlement
						.toString());

		// GenerateEvent
		multiEntitlementAccountStanza.addElement("GenerateEvent").addText(
				"true");

		// ****************************
		// CreateEntitlement
		Element createEntitlementStanza = multiEntitlementAccountStanza
				.addElement(otTickerateEntitlementTO.getCommandType().CreateEntitlement
						.toString());

		// ****************************
		// Ticket Info

		ArrayList<OTTicketInfoTO> ticketList = otTickerateEntitlementTO
				.getTicketInfoList();

		for (OTTicketInfoTO ticketInfo : ticketList) {

			Element ticketInfoStanza = createEntitlementStanza
					.addElement("TicketInfo");
			Element ticketSearchModeStanza = ticketInfoStanza
					.addElement("TicketSearchMode");

			OTTicketTO ticket = ticketInfo.getTicket();

			if ((ticket.getBarCode() != null) && (ticket.getBarCode().length() > 0)) {
				ticketSearchModeStanza.addElement("BarCode").addText(
						ticket.getBarCode());
			}
			else if ((ticket.getMagTrack() != null) && (ticket.getMagTrack()
					.length() > 0)) {
				ticketSearchModeStanza.addElement("MagCode").addText(
						ticket.getMagTrack());
			}
			else if ((ticket.getTCOD() != null) && (ticket.getTCOD().length() > 0)) {
				ticketSearchModeStanza.addElement("TCOD").addText(
						ticket.getTCOD());
			}
			else if ((ticket.getExternalTicketCode() != null) && (ticket
					.getExternalTicketCode().length() > 0)) {
				ticketSearchModeStanza.addElement("ExternalTicketCode")
						.addText(ticket.getExternalTicketCode());
			}
			else if ((ticket.getTdssnSite() != null) && (ticket.getTdssnSite()
					.length() > 0)) {

				// DSSN
				Element tdssnStanza = ticketSearchModeStanza
						.addElement("TDSSN");

				tdssnStanza.addElement("Site").addText(ticket.getTdssnSite());
				tdssnStanza.addElement("Station").addText(
						ticket.getTdssnStation());

				GregorianCalendar tdssnDateCal = ticket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnStanza.addElement("Date").addText(tdssnDate);

				tdssnStanza.addElement("TicketId").addText(
						ticket.getTdssnTicketId());
			}

		}

		// ****************************
		// Existing Account Search Filter

		Boolean searchAccountFound = false;
		Element searchFilterStanza = createEntitlementStanza
				.addElement("SearchFilter");

		if ((otTickerateEntitlementTO.getExistingMediaId() != null) && (otTickerateEntitlementTO
				.getExistingMediaId().length() > 0)) {
			searchAccountFound = true;
			searchFilterStanza.addElement("ExistingMediaId").addText(
					otTickerateEntitlementTO.getExistingMediaId());
		}
		else if ((otTickerateEntitlementTO.getAccountId() != null) && (otTickerateEntitlementTO
				.getAccountId().length() > 0)) {
			searchAccountFound = true;
			searchFilterStanza.addElement("AccountId").addText(
					otTickerateEntitlementTO.getAccountId());
		}
		else if (otTickerateEntitlementTO.getTicketInfo() != null) {

			searchAccountFound = true;

			Boolean ticketFound = false;
			Element ticketSearchModeStanza = searchFilterStanza
					.addElement("TicketSearchMode");

			OTTicketInfoTO ticketInfo = otTickerateEntitlementTO
					.getTicketInfo();

			// The JAXB parsing routine for TickerateEntitlement set the property for
			// "Account Search by Ticket" using "setTicket"
			// and not "setTicketSearchMode". This is important to note.
			OTTicketTO ticket = ticketInfo.getTicket();

			if ((ticket.getBarCode() != null) && (ticket.getBarCode().length() > 0)) {
				ticketFound = true;
				ticketSearchModeStanza.addElement("BarCode").addText(
						ticket.getBarCode());
			}
			else if ((ticket.getMagTrack() != null) && (ticket.getMagTrack()
					.length() > 0)) {
				ticketFound = true;
				ticketSearchModeStanza.addElement("MagCode").addText(
						ticket.getMagTrack());
			}
			else if ((ticket.getTCOD() != null) && (ticket.getTCOD().length() > 0)) {
				ticketFound = true;
				ticketSearchModeStanza.addElement("TCOD").addText(
						ticket.getTCOD());
			}
			else if ((ticket.getExternalTicketCode() != null) && (ticket
					.getExternalTicketCode().length() > 0)) {
				ticketFound = true;
				ticketSearchModeStanza.addElement("ExternalTicketCode")
						.addText(ticket.getExternalTicketCode());
			}
			else if ((ticket.getTdssnSite() != null) && (ticket.getTdssnSite()
					.length() > 0)) {

				ticketFound = true;

				// DSSN
				Element tdssnStanza = ticketSearchModeStanza
						.addElement("TDSSN");

				tdssnStanza.addElement("Site").addText(ticket.getTdssnSite());
				tdssnStanza.addElement("Station").addText(
						ticket.getTdssnStation());

				GregorianCalendar tdssnDateCal = ticket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnStanza.addElement("Date").addText(tdssnDate);

				tdssnStanza.addElement("TicketId").addText(
						ticket.getTdssnTicketId());
			}

			if (!ticketFound) {
				searchAccountFound = false;
				searchFilterStanza.remove(ticketSearchModeStanza);
			}

		}

		// If there wasn't an Account Search requested, remove the SearchFilter node
		if (!searchAccountFound) {
			createEntitlementStanza.remove(searchFilterStanza);
		}

		// ****************************
		// Media Data

		ArrayList<OTMediaDataTO> mediaData = otTickerateEntitlementTO
				.getMediaData();

		for (OTMediaDataTO media : mediaData) {

			Element mediaDataStanza = createEntitlementStanza
					.addElement("MediaData");

			mediaDataStanza.addElement("MediaId").addText(media.getMediaId());
			mediaDataStanza.addElement("MfrId").addText(media.getMfrId());
			mediaDataStanza.addElement("VisualId").addText(media.getVisualId());

		}

		// ****************************
		// ExternalReference

		if ((otTickerateEntitlementTO.getAccountExternalReference() != null) && (otTickerateEntitlementTO
				.getAccountExternalReference().length() > 0)) {

			Element externalReferenceStanza = createEntitlementStanza
					.addElement("ExternalReference");

			// External Reference Value
			externalReferenceStanza.addElement("AccountExternalReference")
					.addText(
							otTickerateEntitlementTO
									.getAccountExternalReference());
			// External Reference Type
			if ((otTickerateEntitlementTO.getAccountExternalReferenceType() != null) && (otTickerateEntitlementTO
					.getAccountExternalReferenceType().toString().length() > 0)) {
				externalReferenceStanza.addElement(
						"AccountExternalReferenceType").addText(
						otTickerateEntitlementTO
								.getAccountExternalReferenceType().toString());
			}
		}

		return;
	}

	/**
	 * Gets the transfer object for MultiEntitlementAccount based on the parsed XML passed in.
	 * 
	 * @param multiEntitlementAccountNode
	 *            the parsed XML containing MultiEntitlementAccount
	 * @return The Omni Ticket MultiEntitlementAccount Transfer Object.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	@SuppressWarnings("unchecked")
	static OTMultiEntitlementAccountTO getTO(Node multiEntitlementAccountNode) throws DTIException {

		OTMultiEntitlementAccountTO multiEntitlementAccountTO = new OTMultiEntitlementAccountTO();

		// Set error object
		OTErrorTO otErrorTO = OTCommandXML
				.getErrorTO(multiEntitlementAccountNode);
		multiEntitlementAccountTO.setError(otErrorTO);

		// Account Id
		Node accountId = multiEntitlementAccountNode
				.selectSingleNode("AccountId");
		if (accountId != null) {
			multiEntitlementAccountTO.setAccountId(accountId.getText());
		}

		// TicketDetails
		List<Node> ticketInfoNodeList = multiEntitlementAccountNode
				.selectNodes("TicketDetails");
		setOTTicketInfoTOList(ticketInfoNodeList,
				multiEntitlementAccountTO.getTicketInfoList());

		// MediaData
		List<Node> mediaDataNodeList = multiEntitlementAccountNode
				.selectNodes("MediaData");
		setOTMediaDataTOList(mediaDataNodeList,
				multiEntitlementAccountTO.getMediaData());

		// TicketsOnAccount
		Node ticketsOnAccount = multiEntitlementAccountNode
				.selectSingleNode("TicketsOnAccount");
		if (ticketsOnAccount != null) {
			multiEntitlementAccountTO.setTicketsOnAccount(Integer
					.parseInt(ticketsOnAccount.getText()));
		}

		// MediaOnAccount
		Node mediaOnAccount = multiEntitlementAccountNode
				.selectSingleNode("MediaOnAccount");
		if (mediaOnAccount != null) {
			multiEntitlementAccountTO.setMediaOnAccount(Integer
					.parseInt(mediaOnAccount.getText()));
		}

		return multiEntitlementAccountTO;
	}

	/**
	 * Sets the ticket info list transfer objects based upon the parsed XML that's passed in.
	 * 
	 * @param ticketInfoNodeList
	 *            the parsed XML section containing the ticket info list.
	 * @param otTktList
	 *            The list of Omni Ticket Ticket Info Transfer Objects.
	 * @throws DTIException
	 *             for any missing required fields or other parsing errors.
	 */
	private static void setOTTicketInfoTOList(List<Node> ticketInfoNodeList,
			ArrayList<OTTicketInfoTO> otTktList) throws DTIException {

		List<Node> ticketList = (List<Node>) ticketInfoNodeList;

		// NGE signatures do not include the Ticket Item Id...we must create it
		Integer tktCtr = 0;

		for (Node aNode : ticketList) {

			tktCtr++;
			OTTicketInfoTO otTktInfoTO = new OTTicketInfoTO();

			// Item
			otTktInfoTO.setItem(new BigInteger(tktCtr.toString()));

			// Ticket
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode != null) {
				otTktInfoTO.setTicket(OTCommandXML.setOTTicketTO(ticketNode));
			}

			otTktList.add(otTktInfoTO);
		}

		return;
	}

	/**
	 * This function loops through the MediaData XML nodes, and assigns the child node values to the respective Ticket Provider's MediaData properties. The resulting object is added to a list.
	 * 
	 * @param mediaDataNodeList
	 *            - MediaData XML Nodes
	 * @param otMediaData
	 *            - list of ticket provider MediaData objects
	 * @throws DTIException
	 *             - for any missing required fields or other parsing errors.
	 * 
	 * @author biest001
	 */
	private static void setOTMediaDataTOList(List<Node> mediaDataNodeList,
			ArrayList<OTMediaDataTO> otMediaData) throws DTIException {

		List<Node> mediaList = (List<Node>) mediaDataNodeList;

		for (Node aNode : mediaList) {

			OTMediaDataTO otMedia = new OTMediaDataTO();

			// Media Id
			Node mediaIdNode = aNode.selectSingleNode("MediaId");
			if (mediaIdNode != null) {
				otMedia.setMediaId(mediaIdNode.getText());
			}

			// Mfr Id
			Node mfrIdNode = aNode.selectSingleNode("MfrId");
			if (mfrIdNode != null) {
				otMedia.setMfrId(mfrIdNode.getText());
			}

			// Visual Id
			Node visualIdNode = aNode.selectSingleNode("VisualId");
			if (visualIdNode != null) {
				otMedia.setVisualId(visualIdNode.getText());
			}

			otMediaData.add(otMedia);
		}

		return;
	}
}