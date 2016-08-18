package pvt.disney.dti.gateway.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pvt.disney.dti.gateway.data.common.TicketTO;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which handles the queries for managing the electronic entitlements process.
 * 
 * @author LEWIT019
 * 
 */
public class ElectronicEntitlementKey {

	// Constants
	/** The object used for logging. */
	private static final ElectronicEntitlementKey THISOBJ = new ElectronicEntitlementKey();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger("pvt.disney.dti.gateway.dao.ElectronicEntitlementKey");

	/** Constant representing insert activated entitlement query. */
	private static final String INS_ACTV_ENTTL = "INS_ACTV_ENTTL";

	/** Constant representing get entitlement visual Id. */
	private static final String GET_ENTTL_VSUALID = "GET_ENTTL_VSUALID";

	/** Constant representing upgrade type. */
	private static final String UPGRADE_TYPE_NAME = "UPGRADE";

	/** Constant representing void type. */
	private static final String VOID_TYPE_NAME = "VOID";

	/** Constant representing void type. */
	private static final String READY_STATUS_STRING = "READY";

	/**
	 * Constructor for ArchiveKey
	 */
	private ElectronicEntitlementKey() {
		super();
	}

	/**
	 * This inserts entitlements into the DTI.ACVT_ENTTL table where another process is responsible for ensuring they are associated properly (RFID to Ticket).
	 * 
	 * @param transIdITS
	 *            The in-bound ticket seller primary table key value.
	 * @param tranType
	 *            The type of transaction.
	 * @param aTicket
	 *            The ticket entitlement.
	 * @param payloadID
	 *            The payload ID for the transaction.
	 * @param entityId
	 *            The entity that sold it.
	 * @param ticketNbr
	 *            The ordinal ticket number (1st, 2nd, 3rd, etc.)
	 */
	private static final void queueEntitlementsForProcessing(
			Integer transIdITS, String tranType, TicketTO aTicket,
			String payloadID, int entityId) {

		// Validate all values...
		// Processing Status Type Code (READY)
		String statusTypeCode = READY_STATUS_STRING;

		// Media Visual ID (blank)
		String mediaVisualID = "NONE";

		// Transaction Attempt Count (0)
		Integer tranAttmptCnt = new Integer("0");

		// Create actv_enttl_id
		int tktItem = aTicket.getTktItem().intValue();
		String actvIdString = transIdITS.toString() + Integer.toString(tktItem);
		Integer actvId = null;
		try {
			actvId = new Integer(actvIdString);
		}
		catch (NumberFormatException nfe) {
			logger.sendEvent(
					"Unable to assemble actv_enttl_id: " + nfe.toString(),
					EventType.EXCEPTION, THISOBJ);
		}

		// Convert DssnNumber to Integer
		Integer dssnNumberObj = Integer.parseInt(aTicket.getDssnNumber());

		// Convert EntityId to Integer
		Integer entityIdObj = Integer.valueOf(entityId);

		// Date
		Date dssnDateObj = aTicket.getDssnDate().getTime();

		logger.sendEvent("Inserting into ACTV_ENTTL", EventType.DEBUG, THISOBJ);
		// Insert into ACTV_ENTTL
		try {
			Object[] values = { actvId,
					tranType,
					dssnDateObj,
					aTicket.getDssnSite(),
					aTicket.getDssnStation(),
					dssnNumberObj,
					payloadID,
					entityIdObj,
					statusTypeCode,
					mediaVisualID,
					tranAttmptCnt };
			DAOHelper helper = DAOHelper.getInstance(INS_ACTV_ENTTL);
			logger.sendEvent("About to processInsert:  INS_ACTV_ENTTL",
					EventType.DEBUG, THISOBJ);
			helper.processInsert(values);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Unable to insert a duplicate row in ACTV_ENTTL for payloadID " + payloadID,
							EventType.WARN, null);
				}
				else {
					logger.sendEvent("Unable to insert ACTV_ENTTL: " + sqle,
							EventType.EXCEPTION, THISOBJ);
				}

			}
			else {
				logger.sendEvent("Unable to insert ACTV_ENTTL: " + we,
						EventType.EXCEPTION, THISOBJ);
			}

		}
		catch (Exception e) {
			logger.sendEvent("Unable to insert ACTV_ENTTL: : " + e.toString(),
					EventType.EXCEPTION, THISOBJ);
			e.printStackTrace();
		}

		return;
	}

	/**
	 * This inserts a voided ticket into the queue for processing. (shorthand method).
	 * 
	 * @param inboundTSID
	 *            The in-bound ticket seller primary table key value.
	 * @param aTicket
	 *            The ticket entitlement.
	 * @param payloadID
	 *            The payload Id used during the sale.
	 * @param entityId
	 *            The entity who sold (or in this case returned) the ticket.
	 */
	public static final void insertVoidedEntitlement(Integer inboundTSID,
			TicketTO aTicket, String payloadID, int entityId) {

		logger.sendEvent("Entering insertVoidedEntitlements()",
				EventType.DEBUG, THISOBJ);

		// Transaction Type Name
		String tranType = VOID_TYPE_NAME;

		queueEntitlementsForProcessing(inboundTSID, tranType, aTicket,
				payloadID, entityId);

		return;
	}

	/**
	 * This inserts an upgraded ticket into the queue for processing. (Shorthand method).
	 * 
	 * @param inboundTSID
	 *            The in-bound ticket seller primary table key value.
	 * @param aTicket
	 *            The ticket entitlement.
	 * @param payloadID
	 *            The payload Id used during the sale.
	 * @param entityId
	 *            The entity who sold (or in this case returned) the ticket.
	 */
	public static final void insertUpgradedEntitlement(Integer inboundTSID,
			TicketTO aTicket, String payloadID, int entityId) {

		logger.sendEvent("Entering insertUpgradedEntitlements()",
				EventType.DEBUG, THISOBJ);

		// Transaction Type Name
		String tranType = UPGRADE_TYPE_NAME;

		queueEntitlementsForProcessing(inboundTSID, tranType, aTicket,
				payloadID, entityId);

		return;

	}

	/**
	 * Method to find the VisualId of an entitlement. This query is being written as to "fail quietly" without throwing an exception, just logging warnings. Code calling this should be able to manage the lack of VisualId when null is
	 * returned/ As of 2.15 JTL
	 * 
	 * @param aTicket
	 *            A ticket object with DSSN provided.
	 * @return null if visual ID not found or error, else visual ID of the entitlement
	 */
	public static final String queryEntitlementVisualId(TicketTO aTicket) {

		String result = null;

		logger.sendEvent("Entering queryEntitlementVisualId()",
				EventType.DEBUG, THISOBJ);

		// Retrieve and validate the parameters
		if (aTicket == null) {
			logger.sendEvent(
					"Invalid parameters sent to queryEntitlementVisualId - no TicketTO",
					EventType.WARN, THISOBJ);
			return null;
		}

		String tktDSSNDate = aTicket.getDssnDateString();
		logger.sendEvent(
				"queryEntitlementVisualId date string is " + tktDSSNDate,
				EventType.WARN, THISOBJ);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dssnDate = null;
		try {
			dssnDate = sdf.parse(tktDSSNDate);
		}
		catch (ParseException pe) {
			logger.sendEvent(
					"Invalid date returned by ATSto queryEntitlementVisualId. " + pe
							.toString(), EventType.WARN, THISOBJ);
			return null;
		}

		Timestamp dssnSQLDate = null;
		dssnSQLDate = new Timestamp(dssnDate.getTime());
		String tktDSSNSite = aTicket.getDssnSite();
		String tktDSSNStation = aTicket.getDssnStation();
		Integer tktDSSNNumber = new Integer(aTicket.getDssnNumber());

		try {
			// Retrieve and validate the parameters
			Object[] values = { dssnSQLDate,
					tktDSSNSite,
					tktDSSNStation,
					tktDSSNNumber };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISOBJ);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTTL_VSUALID);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENTTL_VSUALID",
					EventType.DEBUG, THISOBJ);
			result = (String) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing queryEntitlementVisualId: " + e
							.toString(), EventType.WARN, THISOBJ);
		}

		if (result != null) {
			logger.sendEvent(
					"queryEntitlementVisualId found VisualId:  " + result,
					EventType.DEBUG, THISOBJ, result, null);
		}

		return result;
	}

}
