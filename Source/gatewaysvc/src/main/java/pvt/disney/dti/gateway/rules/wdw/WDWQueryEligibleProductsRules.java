package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for three major functions for WDW query
 * reservation:<BR>
 * 1. Defining the business rules specific to WDW query Eligible Products.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * @author JTL
 * @since 2.16.3
 * 
 */
public class WDWQueryEligibleProductsRules {

	/** Object instance used for logging. */
	private static final WDWQueryEligibleProductsRules THISINSTANCE = new WDWQueryEligibleProductsRules();
	/** Request type header constant. */
	private final static String REQUEST_TYPE_QUERY = "Query";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_QUERYTICKET = "QueryTicket";

	/** Constant indicating all tags should be created. */
	private final static String ALL_TAGS = "All";

	/** Constant indicating the Manage Reservation XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "dtigatewayrequest.xsd";

	/** Constant indicating limited tags should be created. */
	private final static String[] QT_TAGS = { "Tax", "VoidCode", "TicketFlag",
			"TicketAttribute", "AlreadyUsed" };

	/**
	 * Tag exception -- This is hard-coded, but when TDS gets rid of their old
	 * system, we'll be able to remove this. JTL
	 */
	private final static String EXCEPTION_TSMAC = "TDSNA";

	/** Constant text for ITEM ONE (1). */
	private final static String ITEMONE = "1";

	@SuppressWarnings("unused")
	private static final EventLogger logger = EventLogger
			.getLogger(WDWQueryEligibleProductsRules.class.getCanonicalName());

	/**
	 * Transform the DTITransactionTO value object to the provider value objects
	 * and then pass those to XML Marshaling routines to create an XML string.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object.
	 * @return the XML string version of the provider request.
	 * @throws DTIException
	 *             when any transformation error is encountered.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
		String xmlString = null;
		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiCmdBody;

		// === Command Level ===
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.QUERYTICKET);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// === Header Level ===
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERYTICKET);
		atsCommand.setHeaderTO(hdr);

		// === Query Ticket Level ===
		OTQueryTicketTO otQryTkt = new OTQueryTicketTO();

		// Tags
		ArrayList<String> tagList = otQryTkt.getTagsList();

		// Adding ALL_Tag in case of Query Eligible
		tagList.add(ALL_TAGS);

		// SiteNumber
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otQryTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		} else {
			otQryTkt.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}
		// Ticket Information
		ArrayList<TicketTO> dtiTicketList = dtiReq.getTktList();

		TicketTO dtiTicket = dtiTicketList.get(0);

		// putting ticket into ticket info list
		ArrayList<OTTicketInfoTO> otTicketList = otQryTkt.getTicketInfoList();

		OTTicketTO otTicket = new OTTicketTO();

		OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

		otTicketInfo.setItem(new BigInteger(ITEMONE));

		ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();

		TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

		switch (dtiTicketType) {

		case DSSN_ID:
			otTicket.setTDssn(dtiTicket.getDssnDate(), dtiTicket.getDssnSite(),
					dtiTicket.getDssnStation(), dtiTicket.getDssnNumber());
			break;
		case TKTNID_ID:
			otTicket.setTCOD(dtiTicket.getTktNID());
			break;
		case BARCODE_ID:
			otTicket.setBarCode(dtiTicket.getBarCode());
			break;
		case MAG_ID:
			otTicket.setMagTrack(dtiTicket.getMagTrack1());
			break;
		case EXTERNAL_ID:
			otTicket.setExternalTicketCode(dtiTicket.getExternal());
			break;
		}

		otTicketInfo.setTicketSearchMode(otTicket);
		otTicketList.add(otTicketInfo);

		// Set the Query Ticket TO on the command
		atsCommand.setQueryTicketTO(otQryTkt);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;
	}

	/**
	 * Transforms a QueryEligibleResponse response string from the WDW provider
	 * and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param xmlResponse
	 *            The WDW provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response
	 *         information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error
	 *             response to the seller.
	 */
	@SuppressWarnings("unused")
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {
		// TODO space this out, need to change format/style, Todd 06-23-2017 JTL
		// will share the formatter with everyone

		// Adding QueryEligibleProductsRequestTO to look if this can be used
		QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiTxn
				.getRequest().getCommandBody();

		QueryEligibilityProductsResponseTO dtiResRespTO = new QueryEligibilityProductsResponseTO();

		OTQueryTicketTO otQryTicketTO = otCmdTO.getQueryTicketTO();

		dtiRespTO.setCommandBody(dtiResRespTO);
		ArrayList<BigInteger> tktNbr = new ArrayList<BigInteger>();

		// ResponseType
		ArrayList<TicketTO> ticketListTo = dtiResRespTO.getTicketList();

		// Query Ticket InfoInlist
		ArrayList<OTTicketInfoTO> otTicketList = otQryTicketTO
				.getTicketInfoList();

		for (OTTicketInfoTO otTicketInfoTO : otTicketList) {
			tktNbr.add(otTicketInfoTO.getTicketType());
		}

		if ((otTicketList != null) && (otTicketList.size() > 0)) {

			for (OTTicketInfoTO otTicketInfo : otTicketList) {

				ArrayList<DBProductTO> productList = ProductKey
						.getProductsTktNbr(tktNbr);

				boolean inElegibleProduct = validateInEligibleProducts(otQryTicketTO, productList);

				TicketTO dtiTicketTO = new TicketTO();
				TicketTO.TktStatusTO newStatus = dtiTicketTO.new TktStatusTO();

				// Status Item
				newStatus.setStatusItem(otTicketInfo.getItem().toString());

				// Ticket Status Value
				newStatus.setStatusValue(otTicketInfo.getItemStatus()
						.toString());

				OTTicketTO otTicketTO = otTicketInfo.getTicket();

				// Tkt Item
				dtiTicketTO.setTktItem(otTicketInfo.getItem());

				// Prod Code
				dtiTicketTO.setProdCode(otTicketInfo.getItem().toString());

				// Prod Price
				dtiTicketTO.setProdPrice(otTicketInfo.getPrice());

				// Ticket status
				dtiTicketTO.addTicketStatus(newStatus);

				GregorianCalendar dssnDate = otTicketTO.getTdssnDate();

				String site = otTicketTO.getTdssnSite();
				String station = otTicketTO.getTdssnStation();
				String number = otTicketTO.getTdssnTicketId();

				// DSSN
				if (site != null && site != "" && station != null
						&& station != "" && number != null && number != "") {
					dtiTicketTO.setDssn(dssnDate, site, station, number);
				}
				if (otTicketTO.getMagTrack() != null
						&& otTicketTO.getMagTrack() != "") {
					dtiTicketTO.setMag(otTicketTO.getMagTrack());
				}
				if (otTicketTO.getBarCode() != null
						&& otTicketTO.getBarCode() != "") {
					dtiTicketTO.setBarCode(otTicketTO.getBarCode());
				}
				if (otTicketTO.getTCOD() != null && otTicketTO.getTCOD() != "") {
					dtiTicketTO.setTktNID(otTicketTO.getTCOD());
				}
				if (otTicketTO.getExternalTicketCode() != null
						&& otTicketTO.getExternalTicketCode() != "") {
					dtiTicketTO.setExternal(otTicketTO.getExternalTicketCode());
				}

				dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
				dtiTicketTO.setTktTax(otTicketInfo.getTax());

				if (otTicketInfo.getValidityStartDate() != null) {
					dtiTicketTO.setTktValidityValidStart(otTicketInfo
							.getValidityStartDate());

				}
				if (otTicketInfo.getValidityEndDate() != null) {
					dtiTicketTO.setTktValidityValidEnd(otTicketInfo
							.getValidityEndDate());
				}

				dtiResRespTO.add(dtiTicketTO);
			}
		}
		return;
	}

	/**
	 * @param infoTO
	 * @param productTOs
	 * @return
	 * @throws DTIException
	 */
	private static boolean validateInEligibleProducts(OTQueryTicketTO infoTO,
			ArrayList<DBProductTO> productTOs) throws DTIException {
		logger.sendEvent("Entering validateInEligibleProducts", EventType.DEBUG,
				THISINSTANCE);
		boolean isInEligibleProductFlag = false;
		Date date = null;
		ArrayList<BigInteger> tktNbr = new ArrayList<BigInteger>();
		Integer voidCode = null;
		GregorianCalendar endDate = null;
		ArrayList<OTUsagesTO> usagesTOs = null;
		String biometricTemplet = null;
		/* Getting Ticket details */
		for (OTTicketInfoTO otTicketInfoTO : infoTO.getTicketInfoList()) {
			tktNbr.add(otTicketInfoTO.getTicketType());
			voidCode = otTicketInfoTO.getVoidCode();
			endDate = otTicketInfoTO.getValidityEndDate();
			usagesTOs = otTicketInfoTO.getUsagesList();
			biometricTemplet = otTicketInfoTO.getBiometricTemplate();
		}
		/* Getting the uses details */
		List<Date> useDates = new ArrayList<>();
		for (OTUsagesTO otUsagesTO : usagesTOs) {
			SimpleDateFormat fmt = new SimpleDateFormat("yy-MM-dd");
			try {
				date = fmt.parse(otUsagesTO.getDate());
			} catch (ParseException e) {
			}
			useDates.add(date);
		}
		/* First use date */
		Date fisrtUsesDate = Collections.min(useDates);
		long currentDateMiliSec = new Date().getTime();
		for (DBProductTO productTO : productTOs) {
			try {
				/* UpgrdPathId is 0 */
				if (productTO.getUpgrdPathId() != null
						&& productTO.getUpgrdPathId().intValue() == 0) {
					isInEligibleProductFlag = true;
				}
				/* ResidentInd is N and Validity EndDate less than today */
				if (endDate!=null && productTO.isResidentInd() == false
						&& (currentDateMiliSec - endDate.getTime().getTime()) / 86400000 > 0) {
					isInEligibleProductFlag = true;
				}
				/*
				 * ResidentInd is Y and DayCount = 1 and the first use date is
				 * older than 14 days
				 */
				if (fisrtUsesDate!= null && productTO.isResidentInd() == true
						&& Integer.parseInt(productTO.getDayCount()) == 1
						&& currentDateMiliSec - fisrtUsesDate.getTime()
								/ 86400000 > 14) {
					isInEligibleProductFlag = true;
				}
				/*
				 * resident flag is 'Y' and DayCount > 1, and the first use date
				 * is older than six months (185 days).
				 */
				if (fisrtUsesDate!= null && productTO.isResidentInd() == true
						&& Integer.parseInt(productTO.getDayCount()) > 1
						&& new Date().getTime() - fisrtUsesDate.getTime()
								/ 86400000 > 185) {
					isInEligibleProductFlag = true;
				}
			} catch (Exception e) {
				logger.sendEvent(
						"Exception executing validateInEligibleProducts: " + e.toString(),
						EventType.WARN, THISINSTANCE);
			}
		}
		/*
		 * VoidCode in the ATS Query Ticket response greater than 0 or less than
		 * or equal to 100
		 */
		if (voidCode > 0 || voidCode <= 100) {
			isInEligibleProductFlag = true;
		}
		/* Usages must have one entry and BiometricTemplate must have one entry */
		if (usagesTOs != null && usagesTOs.size() == 0 || biometricTemplet == null) {
			isInEligibleProductFlag = true;
		}
		return isInEligibleProductFlag;
	}
}
