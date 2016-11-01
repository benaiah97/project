package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementRequestTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTMultiEntitlementAccountTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMultiEntitlementAccountTO.CommandType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

/**
 * This class is responsible for three specific functions for WDW TickerateEntitlement:<BR>
 * 
 * 1. Defining the business rules specific to WDW Tickerate Entitlement.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the ticket provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the ticket provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author biest001
 * @since 2.16.1
 * 
 */
public class WDWTickerateEntitlementRules {

	/** Request type header constant. */
	private final static String REQUEST_TYPE_TICKERATEENTITLEMENT = "MultiEntitlementAccount";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_TICKERATEENTITLEMENT = "MultiEntitlementAccount";

	/**
	 * Tag exception -- This is hard-coded, but when TDS gets rid of their old system, we'll be able to remove this. JTL
	 */
	private final static String EXCEPTION_TSMAC = "TDSNA";

	/** Constant indicating all tags should be created. */
	private final static String ALL_TAGS = "All";

	/** Constant indicating the "work rules" tags that should be created. */
	private final static String[] TICKERATEENTITLEMENT_TAGS = { "AccountId",
			"TicketDetails",
			"MediaData",
			"TicketsOnAccount",
			"MediaOnAccount" };

	/** Constant indicating the TickerateEntitlement XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "TickerateEntitlementRequest.xsd";

	/**
	 * Transform the DTITransactionTO object to the ticket provider objects and then passes those to XML Marshaling routines to create an XML string.
	 * 
	 * @param dtiTxn
	 *            - the DTI Transaction object.
	 * @return the XML string version of the provider request.
	 * @throws DTIException
	 *             - when any transformation error is encountered.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlString = null;
		DTIRequestTO dtiRequest = dtiTxn.getRequest();

		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();

		TickerateEntitlementRequestTO dtiTickerateEntitlement = (TickerateEntitlementRequestTO) dtiCmdBody;

		// *********************************************
		// Command Level
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.MULTIENTITLEMENTACCOUNT);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// *********************************************
		// Header Level
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_TICKERATEENTITLEMENT,
				REQUEST_SUBTYPE_TICKERATEENTITLEMENT);
		atsCommand.setHeaderTO(hdr);

		// *********************************************
		// Request (TickerateEntitlement) Level
		OTMultiEntitlementAccountTO otTickerateEntitlement = new OTMultiEntitlementAccountTO();

		// *********************************************
		// Work Rules Tags
		ArrayList<String> tagList = otTickerateEntitlement.getTagsList();
		if (EXCEPTION_TSMAC.compareTo(payloadHdr.getTktSeller().getTsMac()) == 0) {
			for (int i = 0; i < TICKERATEENTITLEMENT_TAGS.length; i++) {
				tagList.add(TICKERATEENTITLEMENT_TAGS[i]);
			}
		}
		else {
			tagList.add(ALL_TAGS);
		}

		// *********************************************
		// Site Number
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otTickerateEntitlement.setSiteNumber(WDWBusinessRules
					.getSiteNumberProperty());
		}
		else {
			otTickerateEntitlement.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		// *********************************************
		// Command Type
		otTickerateEntitlement.setCommandType(CommandType.CreateEntitlement);

		// *********************************************
		// Generate Event
		otTickerateEntitlement.setGenerateEvent(true);

		// *********************************************
		// Ticket Info

		ArrayList<OTTicketInfoTO> otTicketList = new ArrayList<OTTicketInfoTO>();

		for (TicketTO ticket : dtiTickerateEntitlement.getTickets()) {

			// *********************************************
			// Search Filter for the entitlement(s) to tickerate

			OTTicketInfoTO ticketInfo = new OTTicketInfoTO();
			OTTicketTO ticketId = new OTTicketTO();

			// Ticket Item Id doesn't exist in the orchestration for NGE Signatures... I don't think this is needed
			// ticketInfo.setItem(ticket.getTktItem());

			if (ticket.getMagTrack1() != null) {
				ticketId.setMagTrack(ticket.getMagTrack1());
			}
			else if (ticket.getBarCode() != null) {
				ticketId.setBarCode(ticket.getBarCode());
			}
			else if (ticket.getTktNID() != null) {
				ticketId.setTCOD(ticket.getTktNID());
			}
			else if (ticket.getExternal() != null) {
				ticketId.setExternalTicketCode(ticket.getExternal());
			}
			else if (ticket.getDssnSite() != null) {

				ticketId.setTDssn(ticket.getDssnDate(), ticket.getDssnSite(),
						ticket.getDssnStation(), ticket.getDssnNumber());
			}

			ticketInfo.setTicket(ticketId);
			otTicketList.add(ticketInfo);

		}

		otTickerateEntitlement.setTicketInfoList(otTicketList);

		// *********************************************
		// Search Filter for the ACCOUNT

		if (dtiTickerateEntitlement.getExistingMediaId() != null) {
			otTickerateEntitlement.setExistingMediaId(dtiTickerateEntitlement
					.getExistingMediaId());
		}
		else if (dtiTickerateEntitlement.getAccountId() != null) {
			otTickerateEntitlement.setAccountId(dtiTickerateEntitlement
					.getAccountId());
		}
		else if (dtiTickerateEntitlement.getTicket() != null) {

			OTTicketInfoTO ticketInfo = new OTTicketInfoTO();
			OTTicketTO ticketId = new OTTicketTO();

			TicketTO ticketSearchMode = dtiTickerateEntitlement.getTicket();

			if (ticketSearchMode.getMagTrack1() != null) {
				ticketId.setBarCode(ticketSearchMode.getMagTrack1());
			}
			else if (ticketSearchMode.getBarCode() != null) {
				ticketId.setBarCode(ticketSearchMode.getBarCode());
			}
			else if (ticketSearchMode.getTktNID() != null) {
				ticketId.setTCOD(ticketSearchMode.getTktNID());
			}
			else if (ticketSearchMode.getExternal() != null) {
				ticketId.setExternalTicketCode(ticketSearchMode.getExternal());
			}
			else if (ticketSearchMode.getDssnSite() != null) {

				ticketId.setTDssn(ticketSearchMode.getDssnDate(),
						ticketSearchMode.getDssnSite(),
						ticketSearchMode.getDssnStation(),
						ticketSearchMode.getDssnNumber());
			}

			ticketInfo.setTicket(ticketId);
			otTickerateEntitlement.setTicketInfo(ticketInfo);
		}

		// *********************************************
		// Media Data
		ArrayList<OTMediaDataTO> otMediaData = new ArrayList<OTMediaDataTO>();

		for (NewMediaDataTO media : dtiTickerateEntitlement.getMediaData()) {
			OTMediaDataTO newMedia = new OTMediaDataTO();
			newMedia.setMediaId(media.getMediaId());
			newMedia.setMfrId(media.getMfrId());
			newMedia.setVisualId(media.getVisualId());
			otMediaData.add(newMedia);
		}

		otTickerateEntitlement.setMediaData(otMediaData);

		// Set the Query Ticket TO on the command
		atsCommand.setMultiEntitlementAccountTO(otTickerateEntitlement);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;

	}

	/**
	 * Transforms a TickerateEntitlement response string from the WDW provider and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            - The transaction object for this request.
	 * @param xmlResponse
	 *            - The WDW provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response information.
	 * @throws DTIException
	 *             - for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

		// The ticket provider response is identical for all of the NGE signatures (anything belonging to MultiEntitlementAccount).
		// This should probably have been a "common" routine that used reflection to interrogate the objects properties.

		// DTI response transfer object
		TickerateEntitlementResponseTO dtiTickerateEntitlementResp = new TickerateEntitlementResponseTO();

		dtiRespTO.setCommandBody(dtiTickerateEntitlementResp);

		// Ticket Provider TickerateEntitlement transfer object
		OTMultiEntitlementAccountTO otTickerateEntitlementTO = otCmdTO
				.getMultiEntitlementAccountTO();

		// Account Id?
		if (otTickerateEntitlementTO.getAccountId() != null) {
			dtiTickerateEntitlementResp.setAccountId(otTickerateEntitlementTO
					.getAccountId());
		}

		// Tickets?
		if ((otTickerateEntitlementTO.getTicketInfoList() != null) && (otTickerateEntitlementTO
				.getTicketInfoList().size() > 0)) {
			ArrayList<OTTicketInfoTO> otTktInfoList = otTickerateEntitlementTO
					.getTicketInfoList();

			Integer tktCtr = 0;
			for (OTTicketInfoTO otTktInfo : otTktInfoList) {

				tktCtr++;
				TicketTO dtiTkt = new TicketTO();

				// NGE signatures do not return a Ticket Item Id, so we will create this value

				// Item
				dtiTkt.setTktItem(new BigInteger(tktCtr.toString()));

				// TktId
				OTTicketTO otTkt = otTktInfo.getTicket();

				// TktId.DSSN
				if (otTkt.getTktIdentityTypes().contains(
						OTTicketTO.TicketIDType.TDSSN)) {
					dtiTkt.setDssn(otTkt.getTdssnDate(), otTkt.getTdssnSite(),
							otTkt.getTdssnStation(), otTkt.getTdssnTicketId());
				}

				// TktId.TktNID
				if (otTkt.getTktIdentityTypes().contains(
						OTTicketTO.TicketIDType.TCOD)) {
					dtiTkt.setTktNID(otTkt.getTCOD());
				}

				// TktId.BarCode
				if (otTkt.getTktIdentityTypes().contains(
						OTTicketTO.TicketIDType.BARCODE)) {
					dtiTkt.setBarCode(otTkt.getBarCode());
				}

				// TktId.Mag
				if (otTkt.getTktIdentityTypes().contains(
						OTTicketTO.TicketIDType.MAGCODE)) {
					dtiTkt.setMag(otTkt.getMagTrack());
				}

				// TktId.External
				if (otTkt.getTktIdentityTypes().contains(
						OTTicketTO.TicketIDType.EXTERNALTICKETCODE)) {
					dtiTkt.setExternal(otTkt.getExternalTicketCode());
				}

				dtiTickerateEntitlementResp.getTickets().add(dtiTkt);
			}
		}

		// MediaData?
		if ((otTickerateEntitlementTO.getMediaData() != null) && (otTickerateEntitlementTO
				.getMediaData().size() > 0)) {
			ArrayList<OTMediaDataTO> otMediaData = otTickerateEntitlementTO
					.getMediaData();

			for (OTMediaDataTO mediaData : otMediaData) {

				NewMediaDataTO dtiMediaData = new NewMediaDataTO();

				dtiMediaData.setMediaId(mediaData.getMediaId());
				dtiMediaData.setMfrId(mediaData.getMfrId());
				dtiMediaData.setVisualId(mediaData.getVisualId());

				dtiTickerateEntitlementResp.getMediaData().add(dtiMediaData);
			}
		}

		// TicketsOnAccount?
		if (otTickerateEntitlementTO.getTicketsOnAccount() != null) {
			dtiTickerateEntitlementResp
					.setTicketsOnAccount(otTickerateEntitlementTO
							.getTicketsOnAccount());
		}

		// MediaOnAccount?
		if (otTickerateEntitlementTO.getMediaOnAccount() != null) {
			dtiTickerateEntitlementResp
					.setMediaOnAccount(otTickerateEntitlementTO
							.getMediaOnAccount());
		}

		return;
	}

	/**
	 * (1) Validate that the ticket included in the "Search for Account" filter is of the proper format.
	 * 
	 * (2) Validate that the list of tickets to be included in the "tickeration" are of the proper format.
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	public static void applyWDWTickerateEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

		TickerateEntitlementRequestTO reqTO = (TickerateEntitlementRequestTO) dtiTxn
				.getRequest().getCommandBody();

		// *********************************************
		// validate the "search for account" ticket
		TicketTO searchTicket = reqTO.getTicket();

		// if the search was performed using a ticket...validate it
		if (searchTicket != null) {
			ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();
			ticketList.add(searchTicket);

			// Validate that the "ticket" is of valid format.
			WDWBusinessRules.validateInBoundWDWTickets(ticketList);
		}

		// *********************************************
		// validate the list of tickets being "tickerated"
		ArrayList<TicketTO> ticketList = reqTO.getTickets();

		// Validate that the "tickets" are of valid format.
		WDWBusinessRules.validateInBoundWDWTickets(ticketList);

		return;

	}

}