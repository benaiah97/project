package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountRequestTO;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountResponseTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
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
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMultiEntitlementAccountTO.SubCommandType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

/**
 * This class is responsible for three specific functions for WDW AssociateMediaToAccount:<BR>
 * 
 * 1. Defining the business rules specific to WDW Associate Media.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the ticket provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the ticket provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author biest001
 * @since 2.16.1
 * 
 */
public class WDWAssociateMediaToAccountRules {

	/** Request type header constant. */
	private final static String REQUEST_TYPE_ASSOCIATEMEDIATOACCOUNT = "MultiEntitlementAccount";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_ASSOCIATEMEDIATOACCOUNT = "MultiEntitlementAccount";

	/**
	 * Tag exception -- This is hard-coded, but when TDS gets rid of their old system, we'll be able to remove this. JTL
	 */
	private final static String EXCEPTION_TSMAC = "TDSNA";

	/** Constant indicating all tags should be created. */
	private final static String ALL_TAGS = "All";

	/** Constant indicating the "work rules" tags that should be created. */
	private final static String[] ASSOCIATEMEDIATOACCOUNT_TAGS = { "AccountId",
			"TicketDetails",
			"MediaData",
			"TicketsOnAccount",
			"MediaOnAccount" };

	/** Constant indicating the AssociateMediaToAccount XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "AssociateMediaToAccountRequest.xsd";

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

		AssociateMediaToAccountRequestTO dtiAssociateMedia = (AssociateMediaToAccountRequestTO) dtiCmdBody;

		// *********************************************
		// Command Level
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.MULTIENTITLEMENTACCOUNT);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// *********************************************
		// Header Level
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_ASSOCIATEMEDIATOACCOUNT,
				REQUEST_SUBTYPE_ASSOCIATEMEDIATOACCOUNT);
		atsCommand.setHeaderTO(hdr);

		// *********************************************
		// Request (AssociateMediaToAccount) Level
		OTMultiEntitlementAccountTO otAssociateMedia = new OTMultiEntitlementAccountTO();

		// *********************************************
		// Work Rules Tags
		ArrayList<String> tagList = otAssociateMedia.getTagsList();
		if (EXCEPTION_TSMAC.compareTo(payloadHdr.getTktSeller().getTsMac()) == 0) {
			for (int i = 0; i < ASSOCIATEMEDIATOACCOUNT_TAGS.length; i++) {
				tagList.add(ASSOCIATEMEDIATOACCOUNT_TAGS[i]);
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
			otAssociateMedia.setSiteNumber(WDWBusinessRules
					.getSiteNumberProperty());
		}
		else {
			otAssociateMedia.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		// *********************************************
		// Command Type
		otAssociateMedia.setCommandType(CommandType.ManageMedia);

		// *********************************************
		// Generate Event
		otAssociateMedia.setGenerateEvent(true);

		// *********************************************
		// SubCommand Type
		otAssociateMedia.setSubCommandType(SubCommandType.Associate);

		// *********************************************
		// Search Filter

		if (dtiAssociateMedia.getExistingMediaId() != null) {
			otAssociateMedia.setExistingMediaId(dtiAssociateMedia
					.getExistingMediaId());
		}
		else if (dtiAssociateMedia.getAccountId() != null) {
			otAssociateMedia.setAccountId(dtiAssociateMedia.getAccountId());
		}
		else if (dtiAssociateMedia.getTicket() != null) {

			OTTicketInfoTO ticketInfo = new OTTicketInfoTO();
			OTTicketTO ticketId = new OTTicketTO();

			TicketTO ticketSearchMode = dtiAssociateMedia.getTicket();

			if (ticketSearchMode.getMagTrack1() != null) {
				ticketId.setMagTrack(ticketSearchMode.getMagTrack1());
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
			otAssociateMedia.setTicketInfo(ticketInfo);
		}

		// *********************************************
		// Media Data
		ArrayList<OTMediaDataTO> otMediaData = new ArrayList<OTMediaDataTO>();

		for (NewMediaDataTO media : dtiAssociateMedia.getMediaData()) {
			OTMediaDataTO newMedia = new OTMediaDataTO();
			newMedia.setMediaId(media.getMediaId());
			newMedia.setMfrId(media.getMfrId());
			newMedia.setVisualId(media.getVisualId());
			otMediaData.add(newMedia);
		}

		otAssociateMedia.setMediaData(otMediaData);

		// Set the AssociateMediaToAccount TO on the command
		atsCommand.setMultiEntitlementAccountTO(otAssociateMedia);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;

	}

	/**
	 * Transforms an AssociateMediaToAccount response string from the WDW provider and updates the DTITransactionTO object with the response information.
	 * 
	 * @throws DTIException
	 *             - for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

		// The ticket provider response is identical for all of the NGE signatures (anything belonging to MultiEntitlementAccount).
		// This should probably have been a "common" routine that used reflection to interrogate the objects properties.

		// DTI response transfer object
		AssociateMediaToAccountResponseTO dtiAssociateMediaResp = new AssociateMediaToAccountResponseTO();

		dtiRespTO.setCommandBody(dtiAssociateMediaResp);

		// Ticket Provider AssociateMediaToAccount transfer object
		OTMultiEntitlementAccountTO otAssociateMediaToAccountTO = otCmdTO
				.getMultiEntitlementAccountTO();

		// Account Id?
		if (otAssociateMediaToAccountTO.getAccountId() != null) {
			dtiAssociateMediaResp.setAccountId(otAssociateMediaToAccountTO
					.getAccountId());
		}

		// Tickets?
		if ((otAssociateMediaToAccountTO.getTicketInfoList() != null) && (otAssociateMediaToAccountTO
				.getTicketInfoList().size() > 0)) {
			ArrayList<OTTicketInfoTO> otTktInfoList = otAssociateMediaToAccountTO
					.getTicketInfoList();

			Integer tktCtr = 0;
			for (OTTicketInfoTO otTktInfo : otTktInfoList) {

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

				dtiAssociateMediaResp.getTickets().add(dtiTkt);
			}
		}

		// MediaData?
		if ((otAssociateMediaToAccountTO.getMediaData() != null) && (otAssociateMediaToAccountTO
				.getMediaData().size() > 0)) {
			ArrayList<OTMediaDataTO> otMediaData = otAssociateMediaToAccountTO
					.getMediaData();

			for (OTMediaDataTO mediaData : otMediaData) {

				NewMediaDataTO dtiMediaData = new NewMediaDataTO();

				dtiMediaData.setMediaId(mediaData.getMediaId());
				dtiMediaData.setMfrId(mediaData.getMfrId());
				dtiMediaData.setVisualId(mediaData.getVisualId());

				dtiAssociateMediaResp.getMediaData().add(dtiMediaData);
			}
		}

		// TicketsOnAccount?
		if (otAssociateMediaToAccountTO.getTicketsOnAccount() != null) {
			dtiAssociateMediaResp
					.setTicketsOnAccount(otAssociateMediaToAccountTO
							.getTicketsOnAccount());
		}

		// MediaOnAccount?
		if (otAssociateMediaToAccountTO.getMediaOnAccount() != null) {
			dtiAssociateMediaResp.setMediaOnAccount(otAssociateMediaToAccountTO
					.getMediaOnAccount());
		}

		return;
	}

	/**
	 * Validate that the ticket included in the "Search for Account" filter is of the proper format.
	 * 
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	public static void applyWDWAssociateMediaToAccountRules(
			DTITransactionTO dtiTxn) throws DTIException {

		AssociateMediaToAccountRequestTO reqTO = (AssociateMediaToAccountRequestTO) dtiTxn
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
		return;

	}

}