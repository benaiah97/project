package pvt.disney.dti.gateway.dao;

import java.util.ArrayList;
import java.util.HashSet;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBQueryBuilder;
import pvt.disney.dti.gateway.dao.data.DBUtil;
import pvt.disney.dti.gateway.data.common.TransidRescodeTO;

/**
 * The Class TransidRescodeKey.
 * 
 * @author MOONS012
 * @since 2.16.3
 */
public class TransidRescodeKey {

  /** Object instance used for logging. */
  private static final TransidRescodeKey THISINSTANCE = new TransidRescodeKey();
  
  /** Event logger. */
  private static final EventLogger logger = EventLogger
      .getLogger(TransidRescodeKey.class.getCanonicalName());
  
  /** Constant representing the get transide rescode. */
  private static final String GET_TRANSID_RESCODE = "GET_TRANSID_RESCODE";
  
  /** Constant representing the insert product detail query. */
  private static final String INS_TRANSID_RESCODE = "INS_TRANSID_RESCODE";

  /**
   * Gets the trans id res code.
   *
   * @param transid the transid
   * @return the trans id res code
   * @throws DTIException the DTI exception
   */
  public static final ArrayList<TransidRescodeTO> getTransIdResCode(String transid) throws DTIException {
    return getTransidRescodeFromDB(transid);    
  }
  
  @SuppressWarnings("unchecked")
  public static final ArrayList<TransidRescodeTO> getTransidRescodeFromDB(String transid) throws DTIException  {
     ArrayList<TransidRescodeTO> result = null; //new ArrayList<TransidRescodeTO>();
     
      logger.sendEvent("Entering getTransidRescodeFromDB()", EventType.DEBUG,
          THISINSTANCE);
      
      // validate the parameters
      if ((transid == null) || (transid.length() == 0)) {
        throw new DTIException(TransidRescodeKey.class,
            DTIErrorCode.UNDEFINED_FAILURE,
            "getTransidRescodeFromDB DB routine found a null or empty transid.");
      }

      // Create a set of transid for rescode lookup
      HashSet<String> transidSet = new HashSet<String>();
      transidSet.add(transid);
      
      //Create our query parameters
      Object[] queryParms = { DBUtil.createSQLInList(transidSet) };
      
      // Replaces "?"
      Object[] values = {};
      
      // Get instance of Query Builder (Replaces "%")
      DBQueryBuilder qBuilder = new DBQueryBuilder();
      
      try {

        // Prepare query
        logger.sendEvent("About to getInstance from DAOHelper",
            EventType.DEBUG, THISINSTANCE);
        DAOHelper helper = DAOHelper.getInstance(GET_TRANSID_RESCODE);

        // Run the SQL
        logger.sendEvent("About to processQuery:  GET_TRANSID_RESCODE",
            EventType.DEBUG, THISINSTANCE);
        result = (ArrayList<TransidRescodeTO>) helper.processQuery(values,
            queryParms, qBuilder);
        
        if (result.isEmpty()) {
          logger.sendEvent(
              "getTransidRescodeFromDB did not find results for '" + transid + "'",
              EventType.WARN, THISINSTANCE);
          throw new DTIException(TransidRescodeKey.class,
              DTIErrorCode.DTI_CANNOT_FIND_RESERVATION,
              "Exception executing getTransidRescodeFromDB");
        }

        // Debug
        logger.sendEvent("getTransidRescodeFromDB found rescode.",
            EventType.DEBUG, THISINSTANCE, result, null);

      }
      catch (Exception e) {
        logger.sendEvent(
            "Exception executing getTransidRescodeFromDB: " + e.toString(),
            EventType.WARN, THISINSTANCE);
        throw new DTIException(TransidRescodeKey.class,
            DTIErrorCode.FAILED_DB_OPERATION_SVC,
            "Exception executing getTransidRescodeFromDB", e);
      }
          
     return result;
  }
  
  /**
   * Insert entry into ts_transid_rescode_xref.
   *
   * @param dtiTxn the dti txn
   * @throws DTIException the DTI exception
   */
  public static final void insertTransIdRescode(String ts_transid, String rescode) throws DTIException {

    logger.sendEvent("Entering insertTransIdRescode()", EventType.DEBUG,
        THISINSTANCE);
    
    //validat input
    if (ts_transid == null || rescode == null || ts_transid.length() == 0 || rescode.length() == 0 ){
      throw new DTIException(TransidRescodeKey.class,
          DTIErrorCode.UNDEFINED_FAILURE,
          "insertTransIdRescode DB routine given null or empty parameters: .ts_transid:'" + ts_transid + "' , rescode:'" + rescode + "'.");
    }

    //values to insert in sql statement
    Object[] values = { ts_transid, rescode, };
    
    int counter = 0;
    
    //helper to process insert
    DAOHelper helper = DAOHelper.getInstance(INS_TRANSID_RESCODE);
    
    try { 
      counter += helper.processInsert(values);
      
    }
    catch (Exception e) {
      logger.sendEvent(
          "Exception executing insertTransIdRescode: " + e.toString(),
          EventType.WARN, THISINSTANCE);
      throw new DTIException(ProductKey.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "Exception executing insertTransIdRescode", e);
    }
    
    logger.sendEvent("INS_TRANSID_RESCODE inserted " + counter + " rows.",
        EventType.DEBUG, THISINSTANCE);
  }
}
