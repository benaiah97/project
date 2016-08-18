package pvt.disney.dti.gateway.dao;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.TransactionSequences;
import pvt.disney.dti.gateway.data.DTITransactionTO;

import com.disney.jdbc.dao.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * DTI relies on a number of system assigned keys or sequence numbers for logging. This DAO class handles getting new key values.
 * 
 * @author lewit019
 * 
 */
public class SequenceKey {

	// Constants
	/** Object instance used for logging. */
	private static final SequenceKey THISINSTANCE = new SequenceKey();

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(SequenceKey.class.getCanonicalName());

	/**
	 * Constant representing the get ticket provider reference sequence number query.
	 */
	private static final String GET_TPREF_SEQ = "GET_TPREF_SEQ";

	/**
	 * Constant representing the get inbound ticket seller log sequence number query.
	 */
	private static final String GET_ITSLOG_SEQ = "GET_ITSLOG_SEQ";

	/**
	 * Constant representing the get inbound ticket provider log sequence number query .
	 */
	private static final String GET_ITPLOG_SEQ = "GET_ITPLOG_SEQ";

	/**
	 * Constant representing the get outbound ticket provider sequence number query.
	 */
	private static final String GET_OTPLOG_SEQ = "GET_OTPLOG_SEQ";

	/**
	 * Constant representing the get outbound ticket seller sequence number query.
	 */
	private static final String GET_OTSLOG_SEQ = "GET_OTSLOG_SEQ";

	/** Constant representing the get dti trans sequence number query. */
	private static final String GET_DTITRANS_SEQ = "GET_DTITRANS_SEQ";

	/**
	 * Constructor for SequenceKey
	 */
	private SequenceKey() {
		super();
	}

	/**
	 * @return a Long value for a Ticket Provider Reference Number
	 * @throws DTIException
	 *             on a DB or DAO error
	 */
	public static final Integer getTpRefNum() throws DTIException {

		logger.sendEvent("Entering getTpRefNum()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		try {

			// Retrieve and validate the parameters

			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_TPREF_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_TPREF_NUM",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getTpRefNum retreived TP ref num:  " + result.toString(),
					EventType.DEBUG, THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getTpRefNum: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getTpRefNum", e);
		}

		return result;
	}

	/**
	 * @return The sequence number for use in the INBOUND_TS_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	public static final Integer getITSTransId() throws DTIException {

		logger.sendEvent("Entering getITSTransId()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		try {

			// Retrieve and validate the parameters

			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ITSLOG_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ITSLOG_SEQ",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getITSTransId retreived sequence number:  " + result
							.toString(), EventType.DEBUG, THISINSTANCE, result,
					null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getITSTransId: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getITSTransId", e);
		}

		return result;
	}

	/**
	 * @return The sequence number for use in the INBOUND_TP_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	public static final Integer getITPTransId() throws DTIException {

		logger.sendEvent("Entering getITPTransId()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		try {

			// Retrieve and validate the parameters

			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ITPLOG_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ITPLOG_SEQ",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getITPTransId retreived sequence number:  " + result
							.toString(), EventType.DEBUG, THISINSTANCE, result,
					null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getITPTransId: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getITPTransId", e);
		}

		return result;
	}

	/**
	 * @return The sequence number for use in the OUTBOUND_TP_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	public static final Integer getOTPTransId() throws DTIException {

		logger.sendEvent("Entering getOTPTransId()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		try {

			// Retrieve and validate the parameters

			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_OTPLOG_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_OTPLOG_SEQ",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getOTPTransId retreived sequence number:  " + result
							.toString(), EventType.DEBUG, THISINSTANCE, result,
					null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getOTPTransId: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getOTPTransId", e);
		}

		return result;
	}

	/**
	 * @return The sequence number for use in the OUTBOUND_TS_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	public static final Integer getOTSTransId() throws DTIException {

		logger.sendEvent("Entering getOTSTransId()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		try {

			// Retrieve and validate the parameters

			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_OTSLOG_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_OTSLOG_SEQ",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getOTSTransId retreived sequence number:  " + result
							.toString(), EventType.DEBUG, THISINSTANCE, result,
					null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getOTSTransId: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getOTSTransId", e);
		}

		return result;
	}

	/**
	 * ))(( JTL - Try this query: select inbound_tp_seqno.nextval ITPSEQ, outbound_tp_seqno.nextval OTPSEQ, outbound_ts_seqno.nextval OTSSEQ from dual
	 * 
	 * @return The sequence numbers for use in the INBOUND_TP_LOG, OUTBOUND_TS_LOG, and OUTBOUND_TP_LOG table.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	public static final void setItpOtpOtsSequenceNumbers(DTITransactionTO dtiTxn) throws DTIException {

		logger.sendEvent("Entering setItpOtpOtsSequenceNumbers()",
				EventType.DEBUG, THISINSTANCE);

		TransactionSequences result = null;

		try {

			// Retrieve and validate the parameters
			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_DTITRANS_SEQ);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_DTITRANS_SEQ",
					EventType.DEBUG, THISINSTANCE);
			result = (TransactionSequences) helper.processQuery(values);

			// Debug
			logger.sendEvent(
					"getOTPTransId retreived sequence number:  " + result
							.toString(), EventType.DEBUG, THISINSTANCE, result,
					null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing setItpOtpOtsSequenceNumbers: " + e
							.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(SequenceKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing setItpOtpOtsSequenceNumbers", e);
		}

		dtiTxn.setTransIdITP(result.getTransIdITP());
		dtiTxn.setTransIdOTP(result.getTransIdOTP());
		dtiTxn.setTransIdOTS(result.getTransIdOTS());

		return;
	}

}
