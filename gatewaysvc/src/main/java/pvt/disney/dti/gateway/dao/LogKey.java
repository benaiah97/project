package pvt.disney.dti.gateway.dao;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.ITPLogEntry;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.util.PCIControl;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which handles getting the log information required by DTI to process a transaction. Note that, because of CLOB issues, not all queries can be rendered using the DAO. Those are kept in the LogTransaction class.
 * 
 * @see pvt.disney.dti.gateway.dao.LogTransaction
 * @author lewit019
 * 
 */
public class LogKey {

	// Constants
	/** Object instance used for logging. */
	private static final LogKey THISINSTANCE = new LogKey();

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(LogKey.class.getCanonicalName());

	/** Constant value representing the get inbound ticket provider log query. */
	private static final String GET_ITP_LOG = "GET_ITP_LOG";

	/** Constant value representing the get inbound ticket seller log query. */
	private static final String GET_ITS_LOG = "GET_ITS_LOG";

	/** Constant value representing the input ticket seller log insert statement. */
	private static final String INSERT_ITS_LOG = "INSERT_ITS_LOG";

	/** Constant value representing the input ticket provider log insert statement. */
	private static final String INSERT_ITP_LOG = "INSERT_ITP_LOG";

	/** Constant value representing the insert DTI transaction log statement. */
	private static final String INSERT_DTI_LOG = "INSERT_DTI_LOG";

	/** Constant value for the output ticket provider log insert statement. */
	private static final String INSERT_OTP_LOG = "INSERT_OTP_LOG";

	/** Constant value representing the update DTI log ITP statement. */
	private static final String UPD_DTI_LOG_ITP = "UPD_DTI_LOG_ITP";

	/** Constant value representing the update DTI log OTP statement. */
	private static final String UPD_DTI_LOG_OTP = "UPD_DTI_LOG_OTP";

	/** Constant value representing the update DTI log OTS statement. */
	private static final String UPD_DTI_LOG_OTS = "UPD_DTI_LOG_OTS";

	/** Constant value representing the output ticket seller log insert statement. */
	private static final String INSERT_OTS_LOG_DTI_ERR = "INSERT_OTS_LOG_DTI_ERR";

	/** Constant value representing the output ticket seller log insert statement. */
	private static final String INSERT_OTS_LOG_NOERR = "INSERT_OTS_LOG_NOERR";

	/** Constant value representing the output ticket seller log insert statement. */
	private static final String INSERT_OTS_LOG_PRV_ERR = "INSERT_OTS_LOG_PRV_ERR";

	/** Constant value representing the output ticket seller log insert statement. */
	private static final String INSERT_OTS_LOG_PRV_FAIL = "INSERT_OTS_LOG_PRV_FAIL";

	/** Constant value limiting how large an XML can be logged w/o using CLOBS. */
	private static final int MAX_XML_SIZE = 3000;

	// Note: these values are also found in the database, but never change.
	private static final Integer CMD_CREATE_TICKET = new Integer(1);
	private static final Integer CMD_UPGRADE_ALPHA = new Integer(2);
	private static final Integer CMD_UPDATE_TICKET = new Integer(3);
	private static final Integer CMD_UPDATE_TRANSACTION = new Integer(4);
	private static final Integer CMD_VOID_TICKET = new Integer(5);
	private static final Integer CMD_QUERY_TICKET = new Integer(6);
	// private static final Integer CMD_ECHO = new Integer(7);
	// private static final Integer CMD_QUERY_TRANSACTION = new Integer(8);
	private static final Integer CMD_RESERVATION = new Integer(9);
	private static final Integer CMD_QUERY_RESERVATION = new Integer(10);
	private static final Integer CMD_UPGRADE_ENTITLEMENT = new Integer(11); // 2.10
	private static final Integer CMD_RENEW_ENTITLEMENT = new Integer(12); // as of 2.16.1, JTL
	private static final Integer CMD_ASSOCIATE_MEDIA_TO_ACCOUNT = new Integer(
			13); // 2.16.1 BIEST001
	private static final Integer CMD_TICKERATE_ENTITLEMENT = new Integer(14); // 2.16.1 BIEST001

	/**
	 * Constructor for LogKey
	 */
	private LogKey() {
		super();
	}

	/**
	 * Returns the ITPLogEntry filled with appropriate values when provided the primary key of the INBOUND_TP_LOG tables.
	 * 
	 * @param tpTransId
	 *            The primary key value in the INBOUND_TP_LOG table.
	 * @return ITPLogEntry filled with appropriate values.
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	// @SuppressWarnings("unchecked")
	public static final ITPLogEntry getITPLogEntry(long tpTransId) throws DTIException {

		logger.sendEvent("Entering getITPLogEntry()", EventType.DEBUG,
				THISINSTANCE);

		ITPLogEntry result = null;

		// Retrieve and validate the parameters
		if ((tpTransId <= 0)) {
			throw new DTIException(LogKey.class,
					DTIErrorCode.UNDEFINED_FAILURE,
					"Invalid tpTransId passed to getITPLogEntry");
		}

		// Retrieve and validate the parameters
		Long tpTransIdLong = Long.valueOf(tpTransId);
		Object[] values = { tpTransIdLong };

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ITP_LOG);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ITP_LOG",
					EventType.DEBUG, THISINSTANCE);
			result = (ITPLogEntry) helper.processQuery(values);

			// Debug
			logger.sendEvent("getITPLogEntry successful.", EventType.DEBUG,
					THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getITPLogEntry: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getITPLogEntry", e);
		}

		return result;
	}

	/**
	 * Returns the primary key field value of a row in the INBOUND_TS_LOG table when provided with a value payloadId.
	 * 
	 * @param payloadId
	 *            The client-chosen unique identifier of a DTI transaction.
	 * @return Long value of the primary key of the INBOUND_TS_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	// @SuppressWarnings("unchecked")
	public static final Integer getITSLogTransId(String payloadId) throws DTIException {

		logger.sendEvent("Entering getITSLogEntry()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		// Retrieve and validate the parameters
		if ((payloadId == null)) {
			throw new DTIException(LogKey.class,
					DTIErrorCode.UNDEFINED_FAILURE,
					"Null payloadId passed to getITSLogTransId");
		}

		// Retrieve and validate the parameters
		Object[] values = { payloadId };

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ITS_LOG);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ITS_LOG",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent("getITSLogTransId successful.", EventType.DEBUG,
					THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getITSLogTransId: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getITSLogTransId", e);
		}

		return result;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static boolean insertITSLog(String payloadId, EntityTO entityTO,
			Integer transIdITS, String inXMLString) throws DTIException {

		logger.sendEvent("Entering insertITSLog() via DAO", EventType.DEBUG,
				THISINSTANCE);

		String pciSafeXMLString = new String("");
		int entityId = entityTO.getEntityId();

		if (inXMLString.length() > MAX_XML_SIZE) {
			LogTransaction logTxn = new LogTransaction();
			return logTxn.insertITSLog(payloadId, entityId, inXMLString,
					transIdITS);
		}

		try {
			// PCI Control: DO NOT REMOVE
			pciSafeXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			// Prepare values for insert
			Integer entityIdInt = new Integer(entityId);

			Object[] values = { transIdITS,
					payloadId,
					pciSafeXMLString,
					entityIdInt };

			DAOHelper helper = DAOHelper.getInstance(INSERT_ITS_LOG);
			helper.processInsert(values);

			logger.sendEvent("INSERT_ITS_LOG inserted row.", EventType.DEBUG,
					THISINSTANCE);

		}
		catch (WrappedException we) {

			if (we.getWrappedException() instanceof SQLException) {

				SQLException sqle = (SQLException) we.getWrappedException();
				logger.sendEvent(
						"LogKey:  SQLException error code is " + sqle
								.getErrorCode(), EventType.INFO, THISINSTANCE);

				if (sqle.getErrorCode() == 1) {
					logger.sendEvent(
							"Recognized a duplicate request, did not insert to INBOUND_TS_LOG.",
							EventType.INFO, THISINSTANCE);
					return false; // I was NOT able to insert this record (duplicate)
				}
				else {
					logger.sendEvent(
							"SQL Exception executing insertITSLog: " + sqle,
							EventType.WARN, THISINSTANCE);
					throw new DTIException(LogKey.class,
							DTIErrorCode.FAILED_DB_OPERATION_SVC,
							"Exception executing insertITSLog", we);
				}
			}
			else {
				logger.sendEvent(
						"Exception executing insertITSLog: " + we.toString(),
						EventType.WARN, THISINSTANCE);
				throw new DTIException(LogKey.class,
						DTIErrorCode.FAILED_DB_OPERATION_SVC,
						"Exception executing insertITSLog", we);
			}

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertITSLog: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertITSLog", e);
		}

		return true;
	}

	/**
	 * Get the log table keys needed for inserting records into the Inbound Ticket Provider, Outbound Ticket Provider, and Outbound Ticket Seller logs.
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	public static void getLogTableKeys(DTITransactionTO dtiTxn) throws DTIException {

		// Enrich the request with values required for logging.
		Integer transIdITP = SequenceKey.getITPTransId();
		Integer transIdOTP = SequenceKey.getOTPTransId();
		Integer transIdOTS = SequenceKey.getOTSTransId();
		dtiTxn.setTransIdITP(transIdITP);
		dtiTxn.setTransIdOTP(transIdOTP);
		dtiTxn.setTransIdOTS(transIdOTS);

		return;
	}

	/**
	 * Inserts values into the ITP Log.
	 * 
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void insertITPLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		logger.sendEvent("Entering insertITPLog() via DAO", EventType.DEBUG,
				THISINSTANCE);

		String pciSafeXMLString = new String("");

		// Update the consolidated table.
		Integer tpRefNumber = dtiTxn.getTpRefNum();
		Integer transIdITP = dtiTxn.getTransIdITP();
		Date trans_date = dtiTxn.getRequest().getPayloadHeader()
				.getTransmitDateTime().getTime();
		Integer cmdid = getCommandID(dtiTxn);
		String cmdInvoice = dtiTxn.getRequest().getCommandHeader()
				.getCmdInvoice();
		LogKey.updateDTITransLogITP(tpRefNumber, trans_date, cmdid, cmdInvoice,
				transIdITP);

		// Divert to slower CLOB logging routine if size of request dictates.
		if (inXMLString.length() > MAX_XML_SIZE) {
			LogTransaction logTxn = new LogTransaction();
			logTxn.insertITPLog(dtiTxn, inXMLString);
			return;
		}

		try {
			// PCI Control: DO NOT REMOVE
			pciSafeXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			logger.sendEvent("LogKey:insertITPLog(): " + pciSafeXMLString,
					EventType.DEBUG, THISINSTANCE);

			// Prepare values for insert
			String tpRefNum = dtiTxn.getTpRefNum().toString();
			String errorCode;
			if (dtiTxn.isPriceMismatch()) errorCode = DTIErrorCode.PRICE_MISMATCH_WARNING
					.getErrorCode();
			else errorCode = " ";

			String payloadId = dtiTxn.getRequest().getPayloadHeader()
					.getPayloadID();
			String target = dtiTxn.getRequest().getPayloadHeader().getTarget();
			String xmlVersion = dtiTxn.getRequest().getPayloadHeader()
					.getVersion();
			String commProtocol = dtiTxn.getRequest().getPayloadHeader()
					.getCommProtocol();
			String commMethod = dtiTxn.getRequest().getPayloadHeader()
					.getCommMethod();
			Integer transIdITS = dtiTxn.getTransIdITS();

			// Order of values: TRANS_ID, TP_TRANSID, ERR_RETURN_CODE, PAYLOAD_ID,
			// TARG_SYS,
			// XML_VERSION, COMM_PROTOCOL, COMM_METHOD, XML_DOC, INBOUND_TS_ID)
			Object[] values = { transIdITP,
					tpRefNum,
					errorCode,
					payloadId,
					target,
					xmlVersion,
					commProtocol,
					commMethod,
					pciSafeXMLString,
					transIdITS };

			DAOHelper helper = DAOHelper.getInstance(INSERT_ITP_LOG);
			helper.processInsert(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertITPLog: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertITPLog", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void insertOTPLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		logger.sendEvent("Entering insertOTPLog() via DAO", EventType.DEBUG,
				THISINSTANCE);

		String pciSafeXMLString = new String("");

		// Enrich the request with the log key, and then
		Integer otpLogTransId = null;
		if (dtiTxn.getTransIdOTP() == null) {
			otpLogTransId = SequenceKey.getOTPTransId();
			dtiTxn.setTransIdOTP(otpLogTransId);
		}
		else {
			otpLogTransId = dtiTxn.getTransIdOTP();
		}

		// Update the consolidated table.
		Integer tpRefNumber = dtiTxn.getTpRefNum();
		LogKey.updateDTITransLogOTP(tpRefNumber, otpLogTransId);

		// Divert to slower CLOB logging routine if size of request dictates.
		if (inXMLString.length() > MAX_XML_SIZE) {
			LogTransaction logTxn = new LogTransaction();
			logTxn.insertOTPLog(dtiTxn, inXMLString);
			return;
		}

		try {
			// START PCI Control: DO NOT REMOVE
			pciSafeXMLString = PCIControl.overwritePciDataInXML(inXMLString);
			// END PCI Control: DO NOT REMOVE

			// Prepare values for insert
			Integer transIdOTP = dtiTxn.getTransIdOTP();
			Integer tpRefNum = dtiTxn.getTpRefNum();
			Integer inboundTPId = dtiTxn.getTransIdITP();

			// Order of values: (TRANS_ID, TP_TRANSID, XML_DOC, INBOUND_TP_ID)
			Object[] values = { transIdOTP,
					tpRefNum,
					pciSafeXMLString,
					inboundTPId };

			DAOHelper helper = DAOHelper.getInstance(INSERT_OTP_LOG);
			helper.processInsert(values);

			// JTL: Required to avoid an FK constraint violation later.
			dtiTxn.setLoggedOTP(true);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertOTPLog: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertOTPLog", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void insertOTSLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		logger.sendEvent("Entering insertOTSLog() via DAO", EventType.DEBUG,
				THISINSTANCE);

		String pciSafeXMLString = new String("");

		// Ensure that we have an OTS log number.
		Integer transIdOTS = null;
		if (dtiTxn.getTransIdOTS() == null) {
			transIdOTS = SequenceKey.getOTSTransId();
			dtiTxn.setTransIdOTS(transIdOTS);
		}
		else {
			transIdOTS = dtiTxn.getTransIdOTS();
		}

		// Update the consolidated table.
		Integer tpRefNumber = dtiTxn.getTpRefNum();
		Date trans_date = dtiTxn.getRequest().getPayloadHeader()
				.getTransmitDateTime().getTime();
		Integer cmdid = getCommandID(dtiTxn);
		String cmdInvoice = dtiTxn.getRequest().getCommandHeader()
				.getCmdInvoice();
		DTIErrorTO dtiErrorTO = dtiTxn.getResponse().getDtiError();
		String errReturnCode = "0";
		String errName = "No error.";
		if (dtiErrorTO != null) {
			errReturnCode = dtiTxn.getResponse().getDtiError().getErrorCode()
					.toString();
			errName = dtiTxn.getResponse().getDtiError().getErrorText();
		}
		String providerErrCode = dtiTxn.getResponse().getProviderErrCode();
		String providerErrName = dtiTxn.getResponse().getProviderErrName();

		LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTS, errReturnCode,
				errName, providerErrCode, providerErrName, trans_date, cmdid,
				cmdInvoice);

		Integer transIdITP = dtiTxn.getTransIdITP();
		Integer transIdOTP = dtiTxn.getTransIdOTP();
		DTIErrorTO dtiError = dtiTxn.getResponse().getDtiError();

		// With the addition of CALM functionality, it's now possible for the DTI
		// answer transactions on its own without the provider, which means no error
		// is present, but the ITP and OTP log files are not populated. The
		// LogTransaction.insertOTSLog builds its XML dynamically, and while more
		// expensive, can adequately handle this situation. If, bypass is true, then
		// we bypass the quicker DB access method in favor of the one which will
		// handle this one-off situation.
		boolean bypassDAOLogging = false;
		if ((transIdITP == null) && (transIdOTP == null) && dtiError == null) {
			bypassDAOLogging = true;
		}

		if ((inXMLString.length() > MAX_XML_SIZE) || (bypassDAOLogging)) {
			LogTransaction logTxn = new LogTransaction();
			logTxn.insertOTSLog(dtiTxn, inXMLString);
			return;
		}

		try {

			// PCI Control: DO NOT REMOVE
			logger.sendEvent("Executing PCIControls", EventType.DEBUG,
					THISINSTANCE);
			pciSafeXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			// Prepare values for insert
			String tsPayloadId = dtiTxn.getResponse().getPayloadHeader()
					.getTsPayloadID();
			Integer transIdITS = dtiTxn.getTransIdITS();

			if (transIdITS == null) {
				logger.sendEvent("LogKey:  transIdITS was NULL!?!?",
						EventType.WARN, THISINSTANCE);
			}

			// Is an error present?
			if (dtiError != null) {
				logger.sendEvent("DTIError present", EventType.DEBUG,
						THISINSTANCE);

				// If ITP and OTP are both null, then DTI must be responsible.
				if ((transIdITP == null) && (transIdOTP == null)) {
					logger.sendEvent("IPT and OTP null", EventType.DEBUG,
							THISINSTANCE);
					String errorCode = dtiTxn.getResponse().getDtiError()
							.getErrorCode().toString();
					Object[] values = { transIdOTS,
							tsPayloadId,
							errorCode,
							transIdITS,
							pciSafeXMLString };
					DAOHelper helper = DAOHelper
							.getInstance(INSERT_OTS_LOG_DTI_ERR);
					logger.sendEvent(
							"1) Values to be inserted: [tsPayloadId:" + tsPayloadId + "] " + "[tsPayloadId:" + tsPayloadId + "] " + "[errorCode:" + errorCode + "] " + "[transIdITS:" + transIdITS + "] " + "[pciSafeXMLString:" + pciSafeXMLString + "] ",
							EventType.DEBUG, THISINSTANCE);
					helper.processInsert(values);

				}
				else {

					logger.sendEvent("Provider Error", EventType.DEBUG,
							THISINSTANCE);
					// It's a provider error (but cover missing log files, just in case,
					// with zeroes).
					String errorCode = dtiTxn.getResponse().getDtiError()
							.getErrorCode().toString();

					if (dtiTxn.isLoggedOTP() == false) {

						Object[] values = { transIdOTS,
								tsPayloadId,
								errorCode,
								pciSafeXMLString,
								transIdITS,
								transIdITP };
						DAOHelper helper = DAOHelper
								.getInstance(INSERT_OTS_LOG_PRV_FAIL);
						logger.sendEvent(
								"transIdOTP: helper about to process inserts",
								EventType.DEBUG, THISINSTANCE);
						logger.sendEvent(
								"2) Values to be inserted: [tsPayloadId:" + tsPayloadId + "] " + "[tsPayloadId:" + tsPayloadId + "] " + "[errorCode:" + errorCode + "] " + "[transIdITS:" + transIdITS + "] " + "[transIdITP:" + transIdITP + "] " + "[pciSafeXMLString:" + pciSafeXMLString + "] ",
								EventType.DEBUG, THISINSTANCE);
						helper.processInsert(values);

					}
					else {

						Object[] values = { transIdOTS,
								tsPayloadId,
								errorCode,
								pciSafeXMLString,
								transIdITS,
								transIdITP,
								transIdOTP };
						DAOHelper helper = DAOHelper
								.getInstance(INSERT_OTS_LOG_PRV_ERR);
						logger.sendEvent("helper about to process inserts",
								EventType.DEBUG, THISINSTANCE);
						logger.sendEvent(
								"3) Values to be inserted: [tsPayloadId:" + tsPayloadId + "] " + "[tsPayloadId:" + tsPayloadId + "] " + "[errorCode:" + errorCode + "] " + "[transIdITS:" + transIdITS + "] " + "[transIdITP:" + transIdITP + "] " + "[pciSafeXMLString:" + pciSafeXMLString + "] ",
								EventType.DEBUG, THISINSTANCE);

						helper.processInsert(values);
					}
				}
			}
			else { // no error present

				if (transIdITP == null) {
					throw new DTIException(LogKey.class,
							DTIErrorCode.UNDEFINED_FAILURE,
							"Exception executing insertOTSLog:  ITP log value was missing.");
				}

				if (transIdOTP == null) {
					throw new DTIException(LogKey.class,
							DTIErrorCode.UNDEFINED_FAILURE,
							"Exception executing insertOTSLog:  OTP log value was missing.");
				}

				Object[] values = { transIdOTS,
						tsPayloadId,
						pciSafeXMLString,
						transIdITS,
						transIdITP,
						transIdOTP };
				logger.sendEvent(
						"4) Values to be inserted: [tsPayloadId:" + tsPayloadId + "] " + "[tsPayloadId:" + tsPayloadId + "] " + "[transIdITS:" + transIdITS + "] " + "[transIdITP:" + transIdITP + "] " + "[transIdOTP:" + transIdOTP + "] " + "[pciSafeXMLString:" + pciSafeXMLString + "] ",
						EventType.DEBUG, THISINSTANCE);
				DAOHelper helper = DAOHelper.getInstance(INSERT_OTS_LOG_NOERR);
				logger.sendEvent(
						"No error present: helper about to process inserts",
						EventType.DEBUG, THISINSTANCE);

				helper.processInsert(values);

			}

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertOTSLog: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertOTSLog", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void insertDTITransLog(Integer tpRefNumber, String payloadId,
			EntityTO entityTO, String target, String broker,
			Integer transIdITS, boolean isFirstAttempt) throws DTIException {

		logger.sendEvent("Entering insertDTITransLog()", EventType.DEBUG,
				THISINSTANCE);

		try {

			String rework = null;

			// Prepare values for insert
			if (isFirstAttempt) {
				rework = "F";
			}
			else {
				rework = "T";
			}
			Integer tsLocation = entityTO.getEntityId();
			Integer tsMac = entityTO.getMacEntityId();

			// Order of values: Integer tpRefNumber, String payloadId,
			// EntityTO entityTO, String target, String broker, Integer transIdITS,
			// boolean isFirstAttempt)
			Object[] values = { tpRefNumber,
					payloadId,
					tsLocation,
					tsMac,
					target,
					broker,
					transIdITS,
					rework };

			DAOHelper helper = DAOHelper.getInstance(INSERT_DTI_LOG);
			helper.processInsert(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertDTITransLog: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertDTITransLog", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void updateDTITransLogITP(Integer tpRefNumber,
			Date trans_date, Integer cmdid, String cmdInvoice,
			Integer transIdITP) throws DTIException {

		logger.sendEvent("Entering updateDTITransLogITP()", EventType.DEBUG,
				THISINSTANCE);

		try {

			// Convert slDts into java.sql.Timestamp for it to insert date and time
			// correctly
			Timestamp transDateTimeStamp = new Timestamp(trans_date.getTime());

			Object[] values = { transDateTimeStamp,
					cmdid,
					cmdInvoice,
					transIdITP,
					tpRefNumber };

			DAOHelper helper = DAOHelper.getInstance(UPD_DTI_LOG_ITP);
			helper.processUpdate(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing updateDTITransLogITP: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing updateDTITransLogITP", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void updateDTITransLogOTP(Integer tpRefNumber,
			Integer transIdOTP) throws DTIException {

		logger.sendEvent("Entering updateDTITransLogOTP()", EventType.DEBUG,
				THISINSTANCE);

		try {

			Object[] values = { transIdOTP, tpRefNumber };

			DAOHelper helper = DAOHelper.getInstance(UPD_DTI_LOG_OTP);
			helper.processUpdate(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing updateDTITransLogOTP: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing updateDTITransLogOTP", e);
		}

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void updateDTITransLogOTS(Integer tpRefNumber,
			Integer transIdOTS, String errReturnCode, String errName,
			String providerErrCode, String providerErrName, Date trans_date,
			TransactionType txnType, String cmdInvoice) throws DTIException {

		logger.sendEvent("Entering updateDTITransLogOTS()", EventType.DEBUG,
				THISINSTANCE);

		Integer cmdid = getCommandID(txnType);

		updateDTITransLogOTS(tpRefNumber, transIdOTS, errReturnCode, errName,
				providerErrCode, providerErrName, trans_date, cmdid, cmdInvoice);

		return;
	}

	/**
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static void updateDTITransLogOTS(Integer tpRefNumber,
			Integer transIdOTS, String errReturnCode, String errName,
			String providerErrCode, String providerErrName, Date trans_date,
			Integer cmdid, String cmdInvoice) throws DTIException {

		logger.sendEvent("Entering updateDTITransLogOTS()", EventType.DEBUG,
				THISINSTANCE);

		try {

			if (providerErrCode == null) {
				providerErrCode = "None";
			}

			if (providerErrName == null) {
				providerErrName = "None available";
			}

			Object[] values = { transIdOTS,
					errReturnCode,
					errName,
					providerErrCode,
					providerErrName,
					trans_date,
					cmdid,
					cmdInvoice,
					tpRefNumber };

			DAOHelper helper = DAOHelper.getInstance(UPD_DTI_LOG_OTS);
			helper.processUpdate(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing updateDTITransLogOTS: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(LogKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing updateDTITransLogOTS", e);
		}

		return;
	}

	/**
	 * Determines the correct DBString to use for a particular query.
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object
	 * @return the command code string
	 */
	private static Integer getCommandID(DTITransactionTO dtiTxn) {

		TransactionType requestType = dtiTxn.getTransactionType();

		return getCommandID(requestType);

	}

	/**
	 * Determines the correct DBString to use for a particular query.
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Transfer Object
	 * @return the command code string
	 */
	private static Integer getCommandID(TransactionType requestType) {

		Integer commandCode = null;

		switch (requestType) {

		case QUERYTICKET:
			commandCode = CMD_QUERY_TICKET;
			break;

		case UPGRADEALPHA:
			commandCode = CMD_UPGRADE_ALPHA;
			break;

		case VOIDTICKET:
			commandCode = CMD_VOID_TICKET;
			break;

		case RESERVATION:
			commandCode = CMD_RESERVATION;
			break;

		case CREATETICKET:
			commandCode = CMD_CREATE_TICKET;
			break;

		case UPDATETICKET:
			commandCode = CMD_UPDATE_TICKET;
			break;

		case UPDATETRANSACTION:
			commandCode = CMD_UPDATE_TRANSACTION;
			break;

		case QUERYRESERVATION:
			commandCode = CMD_QUERY_RESERVATION;
			break;

		case UPGRADEENTITLEMENT: // 2.10
			commandCode = CMD_UPGRADE_ENTITLEMENT;
			break;

		case RENEWENTITLEMENT: // as of 2.16.1, JTL
			commandCode = CMD_RENEW_ENTITLEMENT;
			break;

		case ASSOCIATEMEDIATOACCOUNT: // 2.16.1 BIEST001
			commandCode = CMD_ASSOCIATE_MEDIA_TO_ACCOUNT;
			break;

		case TICKERATEENTITLEMENT: // 2.16.1 BIEST001
			commandCode = CMD_TICKERATE_ENTITLEMENT;
			break;

		case UNDEFINED:
			commandCode = 0;

		}

		return commandCode;
	}

}
