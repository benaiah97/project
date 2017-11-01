package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;
import java.util.List;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.PropertyTO;

/**
 * 
 * This DAO class is responsible for database lookups such as for application,
 * environment and section.
 * Flood Block will need to pass in the value of “0” for tpoId. 
 * That value will be used for property values that are not campus specific.
 * @author GANDV005
 *
 */
public class PropertyKey {

   /** Object instance used for logging. */
   private static final PropertyKey THISINSTANCE = new PropertyKey();

   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(PropertyKey.class.getCanonicalName());

   /** Constant representing the get properties query. */
   private static final String GET_PROPERTY = "GET_PROPERTY";

   @SuppressWarnings("unchecked")
   public static List<PropertyTO> getProperties(String application, Integer tpoId , String environment, String section)
            throws DTIException {

      logger.sendEvent("Entering PropertyKey.getProperties() with parameters: " + application + ", " + tpoId + ", " + environment + ", " + section, EventType.DEBUG, THISINSTANCE);

      List<PropertyTO> result = new ArrayList<PropertyTO>();

      // Replaces "?"
      Object[] values = { application, tpoId, environment, section };

      try {

         // Prepare query
         logger.sendEvent("About to getInstance from DAOHelper. ", EventType.DEBUG, THISINSTANCE);
         DAOHelper helper = DAOHelper.getInstance(GET_PROPERTY);

         // Run the SQL
         logger.sendEvent("About to processQuery:  GET_PROPERTY ", EventType.DEBUG, THISINSTANCE);
         result = (ArrayList<PropertyTO>) helper.processQuery(values);

         // Debug
         logger.sendEvent("getProperties found properties ", EventType.DEBUG, THISINSTANCE, result, null);

      } catch (Exception e) {
         logger.sendEvent("Exception executing getProperties: " + e.toString(), EventType.WARN, THISINSTANCE);
         throw new DTIException(PropertyKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
                  "Exception executing getProperties", e);
      }

      return result;
   }

}