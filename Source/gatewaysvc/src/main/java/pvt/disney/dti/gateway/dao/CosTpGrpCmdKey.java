package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBQueryBuilder;
import pvt.disney.dti.gateway.data.common.CosTpGrpCmdTO;

public class CosTpGrpCmdKey {
	
	// Constants
		/** The class instance used for logging. */
		private static final CosTpGrpCmdKey THISINSTANCE = new CosTpGrpCmdKey();

		/** The event logger. */
		private static final EventLogger logger = EventLogger.getLogger(LookupKey.class.getCanonicalName());

		/**
		 * The constant value representing the get the class of service group lookup
		 * query by provider (i.e. WDW or DLR)
		 */
		private static final String  GET_COS_TP_GROUP_COMMAND = "GET_COS_TP_GROUP_COMMAND";

		/**
		 * This returns clas of service groups for a provider"
		 * 
		 * @param tpiCode
		 * @param txnType
		 * @return
		 * @throws DTIException
		 */
		public static final ArrayList<CosTpGrpCmdTO> getTpCosGrpCmd(String provider) throws DTIException {

			ArrayList<CosTpGrpCmdTO> result = new ArrayList<CosTpGrpCmdTO>();

			logger.sendEvent("Entering getTsCosGrps()", EventType.DEBUG, THISINSTANCE);

			// Replaces "?"
			Object[] values = { provider };

			try {
				// Prepare query
				logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
				DAOHelper helper = DAOHelper.getInstance(GET_COS_TP_GROUP_COMMAND);

				// Run the SQL
				logger.sendEvent("About to processQuery:  GET_COS_TP_GROUP_COMMAND", EventType.DEBUG, THISINSTANCE);
				result = (ArrayList<CosTpGrpCmdTO>) helper.processQuery(values);

				// Debug
				logger.sendEvent("getTpCosGrpCmd found provider class of service groups for commands.", EventType.DEBUG, THISINSTANCE, result, null);

			} catch (Exception e) {
				logger.sendEvent("Exception executing getTpCosGrpCmd: " + e.toString(), EventType.WARN, THISINSTANCE);
				throw new DTIException(CosTpGrpCmdKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
						"Exception executing getCosGrps", e);
			}

			return result;

		}

}
