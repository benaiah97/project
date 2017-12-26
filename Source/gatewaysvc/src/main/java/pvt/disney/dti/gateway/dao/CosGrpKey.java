package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBQueryBuilder;
import pvt.disney.dti.gateway.data.common.CosGrpTO;

/**
 * Class of Service Key
 * 
 * @author moons012
 *
 */
public class CosGrpKey {

	// Constants
	/** The class instance used for logging. */
	private static final CosGrpKey THISINSTANCE = new CosGrpKey();

	/** The event logger. */
	private static final EventLogger logger = EventLogger.getLogger(LookupKey.class.getCanonicalName());

	/**
	 * The constant value representing the get the class of service group lookup
	 * query by provider (i.e. WDW or DLR)
	 */
	private static final String GET_PROVIDER_COS_GRPS = "GET_PROVIDER_COS_GRPS";

	/**
	 * The constant value representing the get all class of service groups query
	 */
	private static final String GET_COS_GRP = "GET_COS_GRPS";
	
	/** The constant value to get the command group for a particular TXN */
	private static final String GET_PROVIDER_CMD_COS_GRP = "GET_PROVIDER_CMD_COS_GRP";

	/**
	 * This returns clas of service groups for a provider"
	 * 
	 * @param tpiCode
	 * @param txnType
	 * @return
	 * @throws DTIException
	 */
	public static final ArrayList<CosGrpTO> getTsCosGrps(String provider) throws DTIException {

		ArrayList<CosGrpTO> result = new ArrayList<CosGrpTO>();

		logger.sendEvent("Entering getTsCosGrps()", EventType.DEBUG, THISINSTANCE);

		// Retrieve and validate the parameters
		Object[] queryParms = { provider };

		// Replaces "?"
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {
			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PROVIDER_COS_GRPS);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PROVIDER_COS_GRP", EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<CosGrpTO>) helper.processQuery(values, queryParms, qBuilder);

			// Debug
			logger.sendEvent("getCosGrps found products.", EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent("Exception executing getCosGrps: " + e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(CosGrpKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getCosGrps", e);
		}

		return result;

	}
	
	/**
	 * This returns class of service groups for a provider command
	 * 
	 * @param tpiCode
	 * @param txnType
	 * @return
	 * @throws DTIException
	 */
	public static final CosGrpTO getTsCmdCosGrp(String provider, String cmd, String environment) throws DTIException {

		CosGrpTO result = new CosGrpTO();

		logger.sendEvent("Entering getTsCmdCosGrp()", EventType.DEBUG, THISINSTANCE);

		// Retrieve and validate the parameters
		Object[] values = { provider,cmd.toUpperCase(), environment };

		try {
			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PROVIDER_CMD_COS_GRP);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PROVIDER_CMD_COS_GRP", EventType.DEBUG, THISINSTANCE);
			result = (CosGrpTO) helper.processQuery(values);

			// Debug
			logger.sendEvent("getCosGrps found cos group.", EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent("Exception executing getCosGrps: " + e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(LookupKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getTsCmdCosGrp", e);
		}

		return result;

	}

}
