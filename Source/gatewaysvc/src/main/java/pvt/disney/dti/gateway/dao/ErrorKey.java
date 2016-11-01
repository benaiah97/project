package pvt.disney.dti.gateway.dao;

import java.math.BigInteger;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class that handles retrieving error information from the database.
 * 
 * @author lewit019
 * 
 */
public class ErrorKey {

	// Constants
	/** Object instance used for logging. */
	private static final ErrorKey THISINSTANCE = new ErrorKey();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(ErrorKey.class.getCanonicalName());

	/** Constant value representing the get error detail query. */
	private static final String GET_ERROR_DETAIL = "GET_ERROR_DETAIL";

	/** Constant value representing the get tp error map query. */
	private static final String GET_TPERROR_MAP = "GET_TPERROR_MAP";

	/**
	 * Constructor for ErrorKey
	 */
	private ErrorKey() {
		super();
	}

	/**
	 * Returns a fully-created DTIErrorTO object when provided a standard DTIErrorCode
	 * 
	 * @param dtiErrCde
	 *            the DTI Error Code object.
	 * @return a fully-created DTIErrorTO object when provided a standard DTIErrorCode
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final DTIErrorTO getErrorDetail(DTIErrorCode dtiErrCde) {

		logger.sendEvent(
				"Entering getErrorDetail() with dtiErrCode " + dtiErrCde
						.getErrorCode(), EventType.INFO, THISINSTANCE);

		DTIErrorTO result = null;

		// Retrieve and validate the parameters
		if ((dtiErrCde == null) || (dtiErrCde.getErrorCode() == null)) {
			logger.sendEvent(
					"Method getErrorDetail called with null error code.",
					EventType.EXCEPTION, THISINSTANCE);
			result = new DTIErrorTO(new BigInteger("360"), "Database",
					"Failed DB Operation", "Critical");
			return result;
		}

		String errorCode = dtiErrCde.getErrorCode();
		Object[] values = { errorCode };

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ERROR_DETAIL);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ERROR_DETAIL",
					EventType.DEBUG, THISINSTANCE);
			result = (DTIErrorTO) helper.processQuery(values);

			// Debug
			logger.sendEvent("getErrorDetail successful.", EventType.DEBUG,
					THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getErrorDetail: " + e.toString(),
					EventType.EXCEPTION, THISINSTANCE);

			result = new DTIErrorTO(new BigInteger("360"), "Database",
					"Failed DB Operation", "Critical");

		}

		if (result == null) {
			logger.sendEvent(
					"No database error mapping found for errorCode: " + errorCode,
					EventType.EXCEPTION, THISINSTANCE);
			result = new DTIErrorTO(new BigInteger("360"), "Database",
					"Failed DB Operation", "Critical");
		}

		return result;
	}

	/**
	 * Returns a fully-created DTIErrorTO object when provided a tpErrorCode string
	 * 
	 * @param tpErrorCode
	 *            the ticket provider error code.
	 * @return a fully-created DTIErrorTO object when provided a tpErrorCode string
	 * @throws DTIException
	 *             DTIException on a DB or DAO problem.
	 */
	public static final DTIErrorTO getTPErrorMap(String tpErrorCode) throws DTIException {
		logger.sendEvent("Entering getTPErrorMap()", EventType.DEBUG,
				THISINSTANCE);
		logger.sendEvent("tpErrorCode provided was: " + tpErrorCode,
				EventType.DEBUG, THISINSTANCE);

		DTIErrorTO result = null;

		// Retrieve and validate the parameters
		Object[] values = { tpErrorCode };

		if (tpErrorCode == null) {
			logger.sendEvent(
					"TP Error Code of null was passed in to getTPErrorMap.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(ErrorKey.class,
					DTIErrorCode.UNDEFINED_FAILURE,
					"TP Error Code of null was passed into getTPErrorMap.");
		}

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_TPERROR_MAP);
			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_TPERROR_MAP",
					EventType.DEBUG, THISINSTANCE);
			result = (DTIErrorTO) helper.processQuery(values);

			// Debug
			logger.sendEvent("getErrorDetail successful.", EventType.DEBUG,
					THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getTPErrorMap: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ErrorKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getTPErrorMap", e);
		}

		if (result == null) {
			logger.sendEvent(
					"TP Error Code of " + tpErrorCode + " is not mapped in database.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(
					ErrorKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"TP Error Code of " + tpErrorCode + " is not mapped in database.");
		}
		else {
			logger.sendEvent(
					"Exiting getTPErrorMap with DTIErrorTO of " + result
							.toString(), EventType.DEBUG, THISINSTANCE);
		}

		return result;
	}

}
