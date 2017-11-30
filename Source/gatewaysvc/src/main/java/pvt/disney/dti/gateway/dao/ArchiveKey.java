package pvt.disney.dti.gateway.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.rules.ContentRules;
import pvt.disney.dti.gateway.rules.TicketRules;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is the DAO key for the database archiving of upgrade alphas and void tickets.
 * 
 * @author mansor7 lewit019
 * 
 */
public class ArchiveKey {

	// Constants
	/** The object used for logging. */
	private static final ArchiveKey THISOBJ = new ArchiveKey();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger("pvt.disney.dti.gateway.dao.ArchiveKey");

	/** Constant representing insert upgrade alpha request query. */
	private static final String INSERT_UPGRADE_ALPHA_REQUEST = "INSERT_UPGRADE_ALPHA_REQUEST";

	/** Constant representing insert upgrade alpha response query. */
	private static final String INSERT_UPGRADE_ALPHA_RESPONSE = "INSERT_UPGRADE_ALPHA_RESPONSE";

	/** Constant representing update upgrade alpha response query. */
	private static final String UPDATE_UPGRADE_ALPHA_RESPONSE = "UPDATE_UPGRADE_ALPHA_RESPONSE";

	/** Constant representing insert void ticket request query. */
	private static final String INSERT_VOID_TICKET_REQUEST = "INSERT_VOID_TICKET_REQUEST";

	/** Constant representing insert void ticket response query. */
	private static final String INSERT_VOID_TICKET_RESPONSE = "INSERT_VOID_TICKET_RESPONSE";

	/** Constant representing update void ticket response query. */
	private static final String UPDATE_VOID_TICKET_RESPONSE = "UPDATE_VOID_TICKET_RESPONSE";

	/** Constant representing delete request query. */
	private static final String DELETE_REQUEST = "DELETE_REQUEST";

	/** Constant representing delete response query. */
	private static final String DELETE_RESPONSE = "DELETE_RESPONSE";

	/** Constant representing check for logged transaction query. */
	private static final String CHECK_FOR_LOGGED_TXN = "CHECK_FOR_LOGGED_TXN";

	/** Constant representing get product ID by provider ticket number query. */
	private static final String GET_PDTID_BY_PROV_TKT_NUM = "GET_PDTID_BY_PROV_TKT_NUM";

	/** Constant representing upgrade type. */
	private static final String UPGRADE_TYPE_NAME = "UPGRADE";

	/** Constant representing void type. */
	private static final String VOID_TYPE_NAME = "VOID";

	/**
	 * Constructor for ArchiveKey
	 */
	private ArchiveKey() {
		super();
	}

	/**
	 * insertUpgradeAlphaRequest method used database call to insert data in UpgradeAlphaRequest transfer object
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object.
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 * 
	 */
	public static final void insertUpgradeAlphaRequest(DTITransactionTO dtiTxn) throws DTIException {

		logger.sendEvent("Entering insertUpgradeAlphaRequest()",
				EventType.DEBUG, THISOBJ);

		DTIRequestTO dtiRqstTO = dtiTxn.getRequest();
		PayloadHeaderTO payHdrTO = dtiRqstTO.getPayloadHeader();
		CommandHeaderTO cmdHdrTO = dtiRqstTO.getCommandHeader();
		CommandBodyTO cmdBodyTO = dtiRqstTO.getCommandBody();
		UpgradeAlphaRequestTO uAReqTO = (UpgradeAlphaRequestTO) cmdBodyTO;

		int counter = 0;

		// Extract all the values for the INSERT statement
		String cmdOpIdVl = cmdHdrTO.getCmdOperator();
		Date createDts = new Date();
		GregorianCalendar cmdDateTimeCal = cmdHdrTO.getCmdDateTime();
		Date slDts = cmdDateTimeCal.getTime();
		String slTypNm = UPGRADE_TYPE_NAME;
		Integer translatedTktSlrMstrAgrId = null;
		Integer translatedTktSlrLocId = null;

		// Convert CreateDts into java.sql.Timestamp for it to insert date and time
		// correctly
		Timestamp createDateTimeStamp = new Timestamp(createDts.getTime());

		// Convert slDts into java.sql.Timestamp for it to insert date and time
		// correctly
		Timestamp salesDateTimeStamp = new Timestamp(slDts.getTime());

		translatedTktSlrMstrAgrId = dtiTxn.getEntityTO().getMacEntityId();
		translatedTktSlrLocId = dtiTxn.getEntityTO().getEntityId();
		String tktSlrTxnId = payHdrTO.getPayloadID();
		ArrayList<TicketTO> ticketList = uAReqTO.getTktList();

		logger.sendEvent("Inserting into DTI_RCONCL_REQ_DLY_HIST",
				EventType.DEBUG, THISOBJ);
		// Insert into DTI_RCONCL_REQ_DLY_HIST
		try {
			// Loop through the price list for updates
			for /* each */(TicketTO aTicket : /* in */ticketList) {
				Long tktOrdSeqNb = new Long(aTicket.getTktItem().longValue());
				Object[] values = { tktSlrTxnId,
						tktOrdSeqNb,
						slTypNm,
						salesDateTimeStamp,
						translatedTktSlrMstrAgrId,
						translatedTktSlrLocId,
						cmdOpIdVl,
						createDateTimeStamp };
				DAOHelper helper = DAOHelper
						.getInstance(INSERT_UPGRADE_ALPHA_REQUEST);
				logger.sendEvent(
						"About to processInsert:  INSERT_UPGRADE_ALPHA_REQUEST",
						EventType.DEBUG, THISOBJ);
				counter += helper.processInsert(values);
			}

			logger.sendEvent(
					"INSERT_UPGRADE_ALPHA_REQUEST inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Unable to insert a duplicate row in DTI_RCONCL_REQ_DLY_HIST for payloadID " + tktSlrTxnId,
							EventType.WARN, null);
				}
				else {
					logger.sendEvent(
							"Unable to insert DTI_RCONCL_REQ_DLY_HIST: " + sqle,
							EventType.WARN, THISOBJ);
				}

			}
			else {
				logger.sendEvent(
						"Unable to insert DTI_RCONCL_REQ_DLY_HIST: " + we,
						EventType.WARN, THISOBJ);
			}

		}
		catch (Exception e) {
			logger.sendEvent(
					"Unable to insert DTI_RCONCL_REQ_DLY_HIST: : " + e
							.toString(), EventType.WARN, THISOBJ);
			e.printStackTrace();
		}

		logger.sendEvent("Inserting into DTI_RCONCL_RESP_DLY_HIST",
				EventType.DEBUG, THISOBJ);
		counter = 0;
		HashMap<String, DBProductTO> dbProdMap = dtiTxn.getDbProdMap();

		// Insert into DTI_RCONCL_RESP_DLY_HIST
		BigInteger dtiProdId = null;
		try {
			// Loop through the ticker list for insert
			for /* each */(TicketTO aTicket : /* in */ticketList) {

				BigInteger tktOrdSeqNb = aTicket.getTktItem();

				DBProductTO dbProd = dbProdMap.get(aTicket.getProdCode());
				dtiProdId = dbProd.getPdtid();

				// DAO cannot handle BigInteger
				Long tktOrdSeqNbLong = new Long(tktOrdSeqNb.longValue());
				Long dtiProdIdLong = new Long(dtiProdId.longValue());

				Object[] values = { tktSlrTxnId,
						tktOrdSeqNbLong,
						slTypNm,
						dtiProdIdLong,
						createDateTimeStamp };
				DAOHelper helper = DAOHelper
						.getInstance(INSERT_UPGRADE_ALPHA_RESPONSE);

				logger.sendEvent(
						"About to processInsert:  INSERT_UPGRADE_ALPHA_RESPONSE",
						EventType.DEBUG, THISOBJ);

				counter += helper.processInsert(values);
			}

			logger.sendEvent(
					"INSERT_UPGRADE_ALPHA_RESPONSE inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Unable to insert a duplicate row in DTI_RCONCL_RESP_DLY_HIST for payloadID " + tktSlrTxnId,
							EventType.WARN, null);
				}
				else {
					logger.sendEvent(
							"Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + sqle,
							EventType.WARN, THISOBJ);
				}

			}
			else {
				logger.sendEvent(
						"Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + we,
						EventType.WARN, THISOBJ);
			}

		}
		catch (Exception e) {
			logger.sendEvent(
					"Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + e
							.toString(), EventType.WARN, THISOBJ);
			e.printStackTrace();
		}

		return;
	}

	/**
	 * updateUpgradeAlphaResponse method used database call to update data in UpgradeAlphaResponse transfer object
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object.
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 * 
	 */
	public static final void updateUpgradeAlphaResponse(DTITransactionTO dtiTxn) throws DTIException {

		logger.sendEvent("Entering updateUpgradeAlphaResponse()",
				EventType.DEBUG, THISOBJ);

		DTIRequestTO dtiRqstTO = dtiTxn.getRequest();
		DTIResponseTO dtiRespTO = dtiTxn.getResponse();
		PayloadHeaderTO payHdrTO = dtiRqstTO.getPayloadHeader();
		UpgradeAlphaResponseTO uARespTO = (UpgradeAlphaResponseTO) dtiRespTO
				.getCommandBody();

		int counter = 0;
		String tktSlrTxnId = payHdrTO.getPayloadID();

		DTIErrorTO dtiError = dtiRespTO.getDtiError();
		if ((dtiError != null) && (dtiError.getErrorCode().compareTo(
				new BigInteger("0")) != 0)) {

			// Delete the request records...
			deleteUpgradeAlphaRequest(tktSlrTxnId);

		}

		String txnNmrcId = "";
		ArrayList<TicketTO> respTktList = uARespTO.getTktList();
		if (respTktList.size() > 0) {
			TicketTO aTicket = respTktList.get(0);
			TicketTransactionTO tktTxnTO = aTicket.getTktTran();
			if (tktTxnTO != null) txnNmrcId = tktTxnTO.getTranNID();
		}

		Date updateDts = new Date();
		// Convert updateDts into java.sql.Timestamp for it to insert date and time
		// correctly
		Timestamp updateDateTimeStamp = new Timestamp(updateDts.getTime());

		ArrayList<TicketTO> ticketList = uARespTO.getTktList();
		try {
			// Loop through the ticket list for updates
			// for (int i = 0; i < ticketList.size(); i++) {
			for /* each */(TicketTO aTicket : /* in */ticketList) {

				String tktOrderSeqNum = aTicket.getTktItem().toString();

				String tktDt = aTicket.getDssnDateString();
				String tktSiteCd = aTicket.getDssnSite();
				String tktStnId = aTicket.getDssnStation();
				String tktNb = aTicket.getDssnNumber();
				String tktNmrcIdVl = aTicket.getTktNID();

				/*
				 * If the Ticket Seller Transaction Id is not null then check if the record matching that id exist in the database or not.
				 */
				if (tktSlrTxnId != null) {
					logger.sendEvent(
							"Checking for the existence of the record to update",
							EventType.DEBUG, THISOBJ);

					/*
					 * If the record does not exist matching the id, then throw an exception with RECORD_DOES_NOT_EXIST_IN_DATABASE. Since this is an update and we can perform an update without the record existing, so we will put this
					 * message back to the queue
					 */
					if (!doesRecordExistInDatabase(tktSlrTxnId, tktOrderSeqNum)) {
						logger.sendEvent(
								"Record does not exist in the database. Update will fail.",
								EventType.WARN, THISOBJ);
						return;
					}

					/*
					 * If the Ticket Seller Transaction Id is null then throw an exception with a NULL_VALUE_PARAMETER error. Since we can't process this request, we will move to the next one in the queue
					 */
				}
				else {
					logger.sendEvent(
							"TktSlrTxnId is null. Cannot find the record to update",
							EventType.EXCEPTION, THISOBJ);
					throw new DTIException(AttributeKey.class,
							DTIErrorCode.FAILED_DB_OPERATION_SVC,
							"Upgrade Alpha Response cannot be logged because no payload ID could be found.");
				}
				Object[] values = { tktDt,
						tktSiteCd,
						tktStnId,
						tktNb,
						tktNmrcIdVl,
						txnNmrcId,
						updateDateTimeStamp,
						tktSlrTxnId,
						tktOrderSeqNum };
				DAOHelper helper = DAOHelper
						.getInstance(UPDATE_UPGRADE_ALPHA_RESPONSE);
				logger.sendEvent(
						"About to processUpdate:  UPDATE_UPGRADE_ALPHA_RESPONSE",
						EventType.DEBUG, THISOBJ);
				counter += helper.processUpdate(values);
			}
			logger.sendEvent(
					"UPDATE_UPGRADE_ALPHA_RESPONSE inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing updateUpgradeAlphaResponse: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing updateUpgradeAlphaResponse", e);
		}
		return;
	}

	/**
	 * insertVoidTicketRequest method used database call to insert data in VoidTicketRequest transfer object
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object.
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 * 
	 */
	public static final void insertVoidTicketRequest(DTITransactionTO dtiTxn) throws DTIException {

		logger.sendEvent("Entering insertVoidTicketRequest()", EventType.DEBUG,
				THISOBJ);

		DTIRequestTO dtiRqstTO = dtiTxn.getRequest();
		PayloadHeaderTO payHdrTO = dtiRqstTO.getPayloadHeader();
		CommandHeaderTO cmdHdrTO = dtiRqstTO.getCommandHeader();
		CommandBodyTO cmdBodyTO = dtiRqstTO.getCommandBody();
		VoidTicketRequestTO voidTktReqTO = (VoidTicketRequestTO) cmdBodyTO;
		String tktSlrTxnId = payHdrTO.getPayloadID();

		int counter = 0;
		try {

			// Extract all the values for the INSERT statement
			String cmdOpIdVl = cmdHdrTO.getCmdOperator();
			Date createDts = new Date();
			GregorianCalendar cmdDateTimeCal = cmdHdrTO.getCmdDateTime();
			Date slDts = cmdDateTimeCal.getTime();
			String slTypNm = VOID_TYPE_NAME;
			Long translatedTktSlrMstrAgrId = null;
			Long translatedTktSlrLocId = null;

			// Convert CreateDts into java.sql.Timestamp for it to insert date and
			// time correctly
			Timestamp createDateTimeStamp = new Timestamp(createDts.getTime());

			// Convert slDts into java.sql.Timestamp for it to insert date and time
			// correctly
			Timestamp salesDateTimeStamp = new Timestamp(slDts.getTime());

			translatedTktSlrMstrAgrId = Long.valueOf(dtiTxn.getEntityTO()
					.getMacEntityId());
			translatedTktSlrLocId = Long.valueOf(dtiTxn.getEntityTO()
					.getEntityId());
			ArrayList<TicketTO> ticketList = voidTktReqTO.getTktList();
			TicketTO aTicket = ticketList.get(0);
			Long tktOrdSeqNb = new Long(aTicket.getTktItem().longValue());

			Object[] values = { tktSlrTxnId,
					tktOrdSeqNb,
					slTypNm,
					salesDateTimeStamp,
					translatedTktSlrMstrAgrId,
					translatedTktSlrLocId,
					cmdOpIdVl,
					createDateTimeStamp };

			DAOHelper helper = DAOHelper
					.getInstance(INSERT_VOID_TICKET_REQUEST);
			logger.sendEvent(
					"About to processInsert:  INSERT_VOID_TICKET_REQUEST",
					EventType.DEBUG, THISOBJ);
			counter += helper.processInsert(values);
			logger.sendEvent(
					"INSERT_VOID_TICKET_REQUEST inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Unable to insert a duplicate row in DTI_RCONCL_REQ_DLY_HIST for payloadID " + tktSlrTxnId,
							EventType.WARN, null);
				}
				else {
					logger.sendEvent(
							"Unable to insert DTI_RCONCL_REQ_DLY_HIST: " + sqle,
							EventType.WARN, THISOBJ);
				}

			}
			else {
				logger.sendEvent(
						"Unable to insert DTI_RCONCL_REQ_DLY_HIST: " + we,
						EventType.WARN, THISOBJ);
			}

		}
		catch (Exception e) {
			logger.sendEvent("Unable to insert DTI_RCONCL_REQ_DLY_HIST: " + e,
					EventType.WARN, THISOBJ);
		}

		counter = 0;
		try {
			// Extract all the values for the INSERT statement
			String slTypNm = VOID_TYPE_NAME;
			Date createDts = new Date();
			ArrayList<TicketTO> ticketList = voidTktReqTO.getTktList();
			TicketTO aTicket = ticketList.get(0);
			BigInteger tktOrdSeqNb = aTicket.getTktItem();

			// Convert CreateDts into java.sql.Timestamp for it to insert date and
			// time correctly
			Timestamp createDateTimeStamp = new Timestamp(createDts.getTime());

			// DAO cannot handle BigInteger
			Long tktOrdSeqNbLong = new Long(tktOrdSeqNb.longValue());

			Object[] values = { tktSlrTxnId,
					tktOrdSeqNbLong,
					slTypNm,
					createDateTimeStamp };

			DAOHelper helper = DAOHelper
					.getInstance(INSERT_VOID_TICKET_RESPONSE);

			// Run the SQL
			logger.sendEvent(
					"About to processUpdate:  INSERT_VOID_TICKET_RESPONSE",
					EventType.DEBUG, THISOBJ);

			counter += helper.processInsert(values);

			logger.sendEvent(
					"INSERT_VOID_TICKET_RESPONSE inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Unable to insert a duplicate row in DTI_RCONCL_RESP_DLY_HIST for payloadID " + tktSlrTxnId,
							EventType.WARN, null);
				}
				else {
					logger.sendEvent(
							"Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + sqle,
							EventType.WARN, THISOBJ);
				}

			}
			else {
				logger.sendEvent(
						"Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + we,
						EventType.WARN, THISOBJ);
			}

		}
		catch (Exception e) {
			logger.sendEvent("Unable to insert DTI_RCONCL_RESP_DLY_HIST: " + e,
					EventType.WARN, THISOBJ);
		}

		return;
	}

	/**
	 * updateVoidTicketResponse method used database call to update data in VoidTicketResponse transfer object
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object.
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 * 
	 */
	public static final void updateVoidTicketResponse(DTITransactionTO dtiTxn) throws DTIException {

		logger.sendEvent("Entering updateVoidTicketResponse()",
				EventType.DEBUG, THISOBJ);

		DTIRequestTO dtiRqstTO = dtiTxn.getRequest();
		DTIResponseTO dtiRespTO = dtiTxn.getResponse();
		PayloadHeaderTO payHdrTO = dtiRqstTO.getPayloadHeader();
		VoidTicketResponseTO voidTktRespTO = (VoidTicketResponseTO) dtiRespTO
				.getCommandBody();

		int counter = 0;
		String tktSlrTxnId = payHdrTO.getPayloadID();

		DTIErrorTO dtiError = dtiRespTO.getDtiError();
		if ((dtiError != null) && (dtiError.getErrorCode().compareTo(
				new BigInteger("0")) != 0)) {

			// Delete the request records...
			deleteVoidTicketRequest(tktSlrTxnId);

		}

		try {

			// Extract all the values for the update statement
			String txnNmrcId = "";
			TicketTO aTicket = null;
			ArrayList<TicketTO> respTktList = voidTktRespTO.getTktList();
			if (respTktList.size() > 0) {
				aTicket = respTktList.get(0);
				TicketTransactionTO tktTxnTO = aTicket.getTktTran();
				if (tktTxnTO != null) txnNmrcId = tktTxnTO.getTranNID();
			}

			Date updateDts = new Date();

			String tktOrderSeqNum = aTicket.getTktItem().toString();
			String tktDt = aTicket.getDssnDateString();
			String tktSiteCd = aTicket.getDssnSite();
			String tktStnId = aTicket.getDssnStation();
			String tktNb = aTicket.getDssnNumber();
			String tktNmrcIdVl = aTicket.getTktNID();

			// Convert updateDts into java.sql.Timestamp for it to insert date and
			// time correctly
			Timestamp updateDateTimeStamp = new Timestamp(updateDts.getTime());

			// Get the provider ticket type.
			BigInteger providerTicketType = aTicket.getProviderTicketType();

			/*
			 * If the providerTicketType is 0 then we will log an info message saying that shell wasn't upgraded and will skip this message and delete all the matching VoidTicketRequest
			 */
			if (providerTicketType.intValue() == 0) {
				logger.sendEvent(
						"Void returned with no provider ticket type." + " Deleting the corresponding VoidTicketRequest.",
						EventType.WARN, THISOBJ);
				// Delete the request records...
				deleteVoidTicketRequest(tktSlrTxnId);
				return;
			}

			/*
			 * If the voided ticket has no price, do not log. This will remove voids of unupgraded tickets and comp tickets from the log. (JTL 09/14/2009 - New Functionality) Validated with Perla Lagares in sales.
			 */
			if (aTicket.getTktPrice().compareTo(new BigDecimal("0.00")) == 0) {
				deleteVoidTicketRequest(tktSlrTxnId);
			}

			// Get the DTI product ID
			BigInteger dtiProdId = null;
			String productOwner = null;
			if (ContentRules.isTargetWDW(payHdrTO.getTarget())) {
				productOwner = TicketRules.WDW_TICKET_OWNER;
			}
			else {
				if (ContentRules.isTargetDLR(payHdrTO.getTarget())) {
					productOwner = TicketRules.DLR_TICKET_OWNER;
				}
				else {
					// shouldn't happen check
					logger.sendEvent(
							"Unknown target! Unable to set Owner for target:" + payHdrTO
									.getTarget() + ".", EventType.FATAL,
							THISOBJ);
				}
			}
			if ((providerTicketType != null) && (providerTicketType.intValue() != 0)) {
				dtiProdId = ArchiveKey.getProductIdByProviderTktNumber(
						providerTicketType, productOwner);
			}

			/*
			 * If the DTI Product Id is not returned from the query then we will log a warning saying 
			 * that the DTI product ID is not found and we will be unable to update the database
			 */
			if ((dtiProdId == null) || (dtiProdId.intValue() == 0)) {
				logger.sendEvent(
						"DTI Product ID does not exist in the DTI Database for " + providerTicketType
								.toString() + ".  Unable to update database.",
						EventType.WARN, THISOBJ);
				// Delete the request records...
				deleteVoidTicketRequest(tktSlrTxnId);
				return;
			}

			/*
			 * If the Ticket Seller Transaction Id is not null then check if the record matching that id exist in the database or not.
			 */
			if (tktSlrTxnId != null) {
				logger.sendEvent(
						"Checking for the existence of the record to update",
						EventType.DEBUG, THISOBJ);
				/*
				 * If the record does not exist matching the id, then throw an exception with 
				 * RECORD_ DOES_NOT_EXIST_IN_DATABASE. Since this is an update and we can perform an update
				 *  without the record existing, so we will put this message
				 * back to the queue
				 */
				if (!doesRecordExistInDatabase(tktSlrTxnId, tktOrderSeqNum)) {
					logger.sendEvent(
							"Record does not exist in the database. Update will fail.",
							EventType.WARN, THISOBJ);
					return;
				}

			}
			else {
				logger.sendEvent(
						"TktSlrTxnId is null. Cannot find the record to update.",
						EventType.EXCEPTION, THISOBJ);
				return;
			}

			// DAO cannot handle BigInteger
			Long dtiProdIdLong = new Long(dtiProdId.longValue());

			Object[] values = { dtiProdIdLong,
					tktDt,
					tktSiteCd,
					tktStnId,
					tktNb,
					tktNmrcIdVl,
					txnNmrcId,
					updateDateTimeStamp,
					tktSlrTxnId,
					tktOrderSeqNum };

			DAOHelper helper = DAOHelper
					.getInstance(UPDATE_VOID_TICKET_RESPONSE);

			// Run the SQL
			logger.sendEvent(
					"About to processUpdate:  UPDATE_VOID_TICKET_RESPONSE",
					EventType.DEBUG, THISOBJ);

			counter += helper.processUpdate(values);

			logger.sendEvent(
					"UPDATE_VOID_TICKET_RESPONSE inserted " + counter + " rows.",
					EventType.DEBUG, THISOBJ);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing UPDATE_VOID_TICKET_RESPONSE: " + e
							.toString(), EventType.WARN, THISOBJ);
			e.printStackTrace();
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing UPDATE_VOID_TICKET_RESPONSE", e);

		}
		return;
	}

	/**
	 * doesRecordExistInDatabase method uses database call to if the record matching TktSlrTxnId existin in the database or not
	 * 
	 * @param tktSlrTxnId
	 *            PayloadID
	 * @param tktOrderSequenceNumber
	 *            sequence number of ticket in order
	 * @return boolean true if order is present
	 * @throws DTIException
	 *             for any database or process error.
	 */
	private static final boolean doesRecordExistInDatabase(String tktSlrTxnId,
			String tktOrderSeqNum) throws DTIException {

		logger.sendEvent("Entering isRecordExistInDatabase() method",
				EventType.DEBUG, THISOBJ);

		boolean exist = false;
		try {

			/*
			 * If the Ticket Seller Transaction Id is null then throw an exception with NULL_VALUE_PARAMETER error
			 */
			if ((tktSlrTxnId == null) || (tktOrderSeqNum == null)) {
				throw new DTIException(ArchiveKey.class,
						DTIErrorCode.UNABLE_TO_LOG_TRANSACTION_DB,
						"Insufficient parameters to execute doesRecordExistInDatabase.");
			}

			// Prepare query
			Object[] values = { tktSlrTxnId, tktOrderSeqNum };
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISOBJ);
			DAOHelper helper = DAOHelper.getInstance(CHECK_FOR_LOGGED_TXN);

			// Run the SQL
			logger.sendEvent("About to processQuery:  CHECK_FOR_LOGGED_TXN",
					EventType.DEBUG, THISOBJ);
			Integer count = (Integer) helper.processQuery(values);
			if (count != null) {
				if (count.intValue() > 0) {
					exist = true;
				}
			}
			// Debug
			logger.sendEvent(
					"DEBUG - Retrieved results for doesRecordExistInDatabase",
					EventType.DEBUG, THISOBJ);
		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing doesRecordExistInDatabase: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing doesRecordExistInDatabase", e);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}

		return exist;
	}

	/**
	 * getProductIdByReferenceNumber method uses database call to get Product Id by using Ref Num
	 * 
	 * @param refNum
	 *            the provider ticket identifier
	 * @return Integer product id value
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 */
	@SuppressWarnings("unchecked")
	private static final BigInteger getProductIdByProviderTktNumber(
			BigInteger refNum, String productOwner) throws DTIException {

		logger.sendEvent("Entering getProductIdByProviderTktNumber()",
				EventType.DEBUG, THISOBJ);
		ArrayList<BigInteger> results = null;
		BigInteger productId = null;

		try {
			// If the Reference Number is null then throw an exception with
			// NULL_VALUE_PARAMETER error
			if (refNum == null || productOwner == null) {
				throw new DTIException(ArchiveKey.class,
						DTIErrorCode.UNABLE_TO_LOG_TRANSACTION_DB,
						"Insufficient parameters to execute getProductIdByProviderTktNumber.");
			}

			Long refNumLong = new Long(refNum.longValue());

			// Prepare query
			Object[] values = { refNumLong, productOwner };
			logger.sendEvent(
					"About to getInstance from DAOHelper w/ productOwner:" + productOwner,
					EventType.DEBUG, THISOBJ);
			DAOHelper helper = DAOHelper.getInstance(GET_PDTID_BY_PROV_TKT_NUM);

			// Run the SQL
			logger.sendEvent(
					"About to processQuery:  GET_PDTID_BY_PROV_TKT_NUM",
					EventType.DEBUG, THISOBJ);
			results = (ArrayList<BigInteger>) helper.processQuery(values);

			if (results.size() == 0) {
				productId = results.get(0);
			}
			else if (results.size() > 0) {
				productId = results.get(0);
				logger.sendEvent(
						"Multiple product ID's located for ticket code " + refNum
								.toString(), EventType.WARN, THISOBJ,
						productId, null);
			}

			// Debug
			logger.sendEvent(
					"DEBUG - Retrieved results for getProductIdByReferenceNumber",
					EventType.DEBUG, THISOBJ, productId, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing GET_PDTID_BY_PROV_TKT_NUM: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing GET_PDTID_BY_PROV_TKT_NUM", e);
		}

		return productId;
	}

	/**
	 * deleteUpgradeAlphaRequest method used database call to delete data matching tktSlrTxnId
	 * 
	 * @param tktSlrTxnId
	 * @throws ArchiveException
	 * 
	 */
	public static final void deleteUpgradeAlphaRequest(String tktSlrTxnId) throws DTIException {

		logger.sendEvent("Entering deleteUpgradeAlphaRequest()",
				EventType.DEBUG, THISOBJ);

		int counter = 0;

		try {
			Object[] values = { tktSlrTxnId };
			DAOHelper helper = DAOHelper.getInstance(DELETE_RESPONSE);
			// Run the SQL
			logger.sendEvent(
					"About to processDelete:  DELETE_UPGRADE_ALPHA_RESPONSE",
					EventType.DEBUG, THISOBJ);
			counter += helper.processDelete(values);
			logger.sendEvent(
					" Successfully deleted" + counter + " row(s) for Upgrade Alpha Response.",
					EventType.DEBUG, THISOBJ);
		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing DELETE_UPGRADE_ALPHA_RESPONSE: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing DELETE_UPGRADE_ALPHA_RESPONSE", e);
		}

		counter = 0;

		try {
			Object[] values = { tktSlrTxnId };
			DAOHelper helper = DAOHelper.getInstance(DELETE_REQUEST);
			// Run the SQL
			logger.sendEvent(
					"About to processDelete:  DELETE_UPGRADE_ALPHA_REQUEST",
					EventType.DEBUG, THISOBJ);
			counter += helper.processDelete(values);
			logger.sendEvent(
					"Successfully deleted" + counter + " row(s) for Upgrade Alpha Request.",
					EventType.DEBUG, THISOBJ);
		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing DELETE_UPGRADE_ALPHA_REQUEST: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing DELETE_UPGRADE_ALPHA_REQUEST", e);
		}

		return;
	}

	/**
	 * deleteVoidTicketRequest method used database call to delete data matching tktSlrTxnId
	 * 
	 * @param tktSlrTxnId
	 *            the payload ID value to be deleted.
	 * @throws DTIException
	 *             should any error occur logging the transaction.
	 * 
	 */
	public static final void deleteVoidTicketRequest(String tktSlrTxnId) throws DTIException {

		logger.sendEvent("Entering deleteVoidTicketRequest()", EventType.DEBUG,
				THISOBJ);

		int counter = 0;

		try {
			Object[] values = { tktSlrTxnId };
			DAOHelper helper = DAOHelper.getInstance(DELETE_RESPONSE);
			// Run the SQL
			logger.sendEvent(
					"About to processDelete:  DELETE_VOID_TICKET_REESPONSE",
					EventType.DEBUG, THISOBJ);
			counter += helper.processDelete(values);
			logger.sendEvent(
					"Successfully deleted" + counter + " row(s) for Void Ticket Response.",
					EventType.DEBUG, THISOBJ);
		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing DELETE_VOID_TICKET_RESPONSE: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing DELETE_VOID_TICKET_RESPONSE", e);
		}

		counter = 0;

		try {
			Object[] values = { tktSlrTxnId };
			DAOHelper helper = DAOHelper.getInstance(DELETE_REQUEST);
			// Run the SQL
			logger.sendEvent(
					"About to processDelete:  DELETE_VOID_TICKET_REQUEST",
					EventType.DEBUG, THISOBJ);
			counter += helper.processDelete(values);
			logger.sendEvent(
					"Successfully deleted " + counter + " row(s) for Void Ticket Request.",
					EventType.DEBUG, THISOBJ);
		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing DELETE_VOID_TICKET_REQUEST: " + e
							.toString(), EventType.WARN, THISOBJ);
			throw new DTIException(ArchiveKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing DELETE_VOID_TICKET_REQUEST", e);
		}

		return;
	}

}
