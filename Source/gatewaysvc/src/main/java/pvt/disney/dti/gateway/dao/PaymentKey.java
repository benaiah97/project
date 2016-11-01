package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This DAO class handles getting the payment lookup information.
 * 
 * @author lewit019
 * 
 */
public class PaymentKey {

	// Constants
	/** Class instance used for logging. */
	private static final PaymentKey THISINSTANCE = new PaymentKey();

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(PaymentKey.class.getCanonicalName());

	/** Constant representing the get payment lookup query. */
	private static final String GET_PMT_LOOKUP = "GET_PMT_LOOKUP";

	/**
	 * Constructor for PaymentKey
	 */
	private PaymentKey() {
		super();
	}

	/**
	 * Returns an array list of PaymentLookupTO's.
	 * 
	 * @param tpiCode
	 *            The ticket provider interface code.
	 * @param paymentId
	 *            The numerical index to the payment Id.
	 * @return an array list of PaymentLookupTO's.
	 * @throws DTIException
	 *             on a DB or DAO error.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<PaymentLookupTO> getPaymentLookup(
			String tpiCode, long paymentId) throws DTIException {

		logger.sendEvent(
				"Entering getPaymentLookup() for " + tpiCode + ":" + paymentId,
				EventType.DEBUG, THISINSTANCE);

		ArrayList<PaymentLookupTO> result = null;

		// Retrieve and validate the parameters
		if ((tpiCode == null) || (paymentId <= 0)) {
			throw new DTIException(PaymentKey.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Insufficient parameters to execute getPaymentLookup");
		}

		// Retrieve and validate the parameters
		Long paymentIdLong = Long.valueOf(paymentId);
		Object[] values = { tpiCode, paymentIdLong };

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PMT_LOOKUP);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PMT_LOOKUP",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<PaymentLookupTO>) helper.processQuery(values);

			// Debug
			logger.sendEvent("getPaymentLookup successful. ", EventType.DEBUG,
					THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getPaymentLookup: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(PaymentKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getPaymentLookup", e);
		}

		return result;
	}

}
