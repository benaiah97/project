package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which is used to get product and seller eligibility information.
 * 
 * @author lewit019
 * 
 */
public class EligibilityKey {

	// Constants
	/** Object instance used for logging. */
	private static final EligibilityKey THISINSTANCE = new EligibilityKey();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(EligibilityKey.class.getCanonicalName());

	/** The constant value representing the get product eligibility query. */
	private static final String GET_PROD_ELIG = "GET_PROD_ELIG";

	/** The constant value representing the get wdw association info. */
	private static final String GET_WDW_ASSN_INFO = "GET_WDW_ASSN_INFO";

	/**
	 * Constructor for EligibilityKey
	 */
	private EligibilityKey() {
		super();
	}

	/**
	 * Returns the order eligibility status.
	 * 
	 * @param dbProdList
	 *            the list of products in the database.
	 * @param eligGroupCode
	 *            the eligibility group code
	 * @param eligMemberID
	 *            the eligibility member ID
	 * @throws DTIException
	 *             on a DB or DAO problem or if it's unable to complete the query to a true or false result.
	 * @return true if products were found requiring eligibility and the lookup showed active
	 * @return false if products were found requiring eligibility and the lookup showed inactive
	 * @throws DTIException
	 *             if the SQL returns null (meaning invalid elig set-up in DB) or no products are found requiring eligiblity.
	 */
	public static final Boolean getOrderEligibility(
			ArrayList<DBProductTO> dbProdList, String eligGrpCode,
			String eligMemberID) throws DTIException {

		logger.sendEvent("Entering getOrderEligibility()", EventType.DEBUG,
				THISINSTANCE);

		Boolean result = null;

		// Retrieve and validate the parameters
		if ((dbProdList == null) || (eligGrpCode == null) || (eligMemberID == null)) {
			throw new DTIException(EligibilityKey.class,
					DTIErrorCode.INVALID_ELIGIBILITY_NBR,
					"Insufficient parameters to execute getOrderEligibility.");
		}

		String prodCode = null;
		String eligGrpID = null;

		// Loop through and find one product that requires eligibility.
		for /* each */(DBProductTO aProduct : /* in */dbProdList) {

			if (aProduct.getEligGrpid() == null) continue;
			else {
				prodCode = aProduct.getPdtCode();
				eligGrpID = aProduct.getEligGrpid().toString();
			}
		}

		if (prodCode == null) throw new DTIException(
				EligibilityKey.class,
				DTIErrorCode.INVALID_ELIGIBILITY_NBR,
				"getOrderEligibility called with dbProdList containing no products needing eligibility.");

		// Cut down the elig group code to 10 character string because that's all
		// the database
		// supports. Extra characters are ignored, so BIG_WELLS_FARGO and
		// BIG_WELLS_FOSTER are equal.
		if (eligGrpCode.length() > 10) {
			eligGrpCode = eligGrpCode.substring(0, 10);
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { prodCode, eligGrpID, eligGrpCode, eligMemberID };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PROD_ELIG);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PROD_ELIG",
					EventType.DEBUG, THISINSTANCE);
			result = (Boolean) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getOrderEligibility: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(EligibilityKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getOrderEligibility", e);
		}

		if (result == null) throw new DTIException(
				EligibilityKey.class,
				DTIErrorCode.INVALID_ELIGIBILITY_NBR,
				"getOrderEligibility could not find valid elig set-up on product " + prodCode + " and elig group code of " + eligGrpCode);

		return result;
	}

	/**
	 * Gets the eligibility association ID based on the group code.
	 * 
	 * @param eligGrpCode
	 *            group code, as provided in the database. NOTE: all elig group codes are cut down to the first 10 characters before the query is run, since the database column length is only 10.
	 * @return the integer value of the association identifier.
	 * @throws DTIException
	 *             for any database or process related failures.
	 */
	public static final Integer getEligibilityAssocId(String eligGrpCode) throws DTIException {

		logger.sendEvent("Entering getEligibilityAssocId()", EventType.DEBUG,
				THISINSTANCE);

		Integer result = null;

		// Retrieve and validate the parameters
		if (eligGrpCode == null) {
			throw new DTIException(EligibilityKey.class,
					DTIErrorCode.INVALID_ELIGIBILITY_NBR,
					"Insufficient parameters to execute getEligibilityAssocId.");
		}

		// Cut down the elig group code to 10 character string because that's all
		// the database
		// supports. Extra characters are ignored, so BIG_WELLS_FARGO and
		// BIG_WELLS_FOSTER are equal.
		if (eligGrpCode.length() > 10) {
			eligGrpCode = eligGrpCode.substring(0, 10);
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { eligGrpCode };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_WDW_ASSN_INFO);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_WDW_ASSN_INFO",
					EventType.DEBUG, THISINSTANCE);
			result = (Integer) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEligibilityAssocId: " + e
							.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(EligibilityKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEligibilityAssocId", e);
		}

		if (result == null) throw new DTIException(
				EligibilityKey.class,
				DTIErrorCode.INVALID_ELIGIBILITY_NBR,
				"getEligibilityAssocId could not find valid elig group " + eligGrpCode);

		return result;
	}

}
