package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;
import java.util.List;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.BarricadeTO;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;


/**
 * The Class BarricadeKey.
 * @author MISHP012
 *
 */
public class BarricadeKey {
   
   /** The class instance used for logging. */
   private static final BarricadeKey THISINSTANCE = new BarricadeKey();
   
   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(LogKey.class.getCanonicalName());
   
   /** The Constant GET_BARRICADE_QUERY. */
   private static final String GET_BARRICADE= "GET_BARRICADE";
   
   /**
    * Constructor for BarricadeKey.
    */
   private BarricadeKey() {
     super();
   }
   
   /**
    * Gets the barricade lookup.
    *
    * @param cosgrpId the cosgrp id
    * @return the barricade lookup
    * @throws DTIException the DTI exception
    */
   @SuppressWarnings("unchecked")
   public static final List<BarricadeTO> getBarricadeLookup(Integer cosgrpId,String ownerId) throws DTIException {

      ArrayList<BarricadeTO> result = null;

      // Retrieve and validate the parameters
      if ((cosgrpId == null || ownerId == null)) {
         throw new DTIException(BarricadeKey.class, DTIErrorCode.DTI_DATA_ERROR, "Class of service error; cosgrpId: "
                  + cosgrpId + ", OwnerId: " + ownerId);
      }

      Object[] values = {cosgrpId,ownerId};

      try {
         // Prepare query
         logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
         DAOHelper helper = DAOHelper.getInstance(GET_BARRICADE);

         // Run the SQL
         logger.sendEvent("About to processQuery:  GET_BARRICADE", EventType.DEBUG, THISINSTANCE);
         result = (ArrayList<BarricadeTO>) helper.processQuery(values);

         // Debug
         logger.sendEvent("getBarricadeLookup successful.", EventType.DEBUG, THISINSTANCE, result, null);

      } catch (Exception e) {
         logger.sendEvent("Exception executing getBarricadeLookup: " + e.toString(), EventType.WARN, THISINSTANCE);
         throw new DTIException(BarricadeKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC, "Exception executing getBarricadeLookup", e);
      }
      return result;
   }
}
