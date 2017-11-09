package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;
import java.util.List;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.CommandTO;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * The Class CommandKey.
 *
 * @author MISHP012
 */
public class CommandKey {
   

   /** Object instance used for logging. */
   private static final CommandKey THISINSTANCE = new CommandKey();

   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(CommandKey.class.getCanonicalName());

   /** Constant representing the get properties query. */
   private static final String GET_COMMAND = "GET_COMMAND";

   @SuppressWarnings("unchecked")
   public static List<String> getCommand(int cosgrpId)
            throws DTIException {

      logger.sendEvent("Entering CommandKey.getCommand() with cosgrpId: ", EventType.DEBUG, THISINSTANCE);

      List<String> result = new ArrayList<String>();

      // Replaces "?"
      Object[] values = {cosgrpId};

      try {

         // Prepare query
         logger.sendEvent("About to getInstance from DAOHelper. ", EventType.DEBUG, THISINSTANCE);
         DAOHelper helper = DAOHelper.getInstance(GET_COMMAND);

         // Run the SQL
         logger.sendEvent("About to processQuery:  GET_COMMAND ", EventType.DEBUG, THISINSTANCE);
         result = (List<String>) helper.processQuery(values);

         // Debug
         logger.sendEvent("getCommand found command ", EventType.DEBUG, THISINSTANCE, result, null);

      } catch (Exception e) {
         logger.sendEvent("Exception executing getCommand: " + e.toString(), EventType.WARN, THISINSTANCE);
         throw new DTIException(PropertyKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
                  "Exception executing getCommand", e);
      }

      return result;
   }


}
