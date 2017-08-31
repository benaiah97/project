package pvt.disney.dti.gateway.rules.hkd;

import java.util.Properties;

import com.disney.logging.EventLogger;

//import com.disney.logging.EventLogger;
//import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;

import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpdateDemographicsRequestTO;
import pvt.disney.dti.gateway.data.UpdateDemographicsResponseTO;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;


import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;

public class HKDUpdateDemographicsRules {

	 /** The Constant THISOBJECT. */
	  private static final Class<HKDUpdateDemographicsRules> THISOBJECT = HKDUpdateDemographicsRules.class;

	  /** The Constant logger. */
	  private static final EventLogger logger = EventLogger.getLogger(HKDUpdateDemographicsRules.class.getCanonicalName());
	
	  /**
	   * Pull in any rule values from the properties file, later from DB
	   *
	   * @param props the props
	   */
	  public static void initHKDUpdateDemographicsRules(Properties props) {
	    return;
	  }
	  
	public static void applyHKDUpdateDemographicRules(DTITransactionTO dtiTxn) throws DTIException {
		  DTIRequestTO dtiRequest = dtiTxn.getRequest();
		  CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		    
		// TODO return null;
	}

	/**
	 * Transform the DTITransactionTO value object to the provider value objects
	 * and then pass those to XML Marshaling routines to create an XML string.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object.
	 * @return the XML string version of the provider request.
	 * @throws DTIException
	 *             when any transformation error is encountered.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
		 String xmlString = null;
		 DTIRequestTO dtiRequest = dtiTxn.getRequest();
		 CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		 UpdateDemographicsRequestTO dtiResReq = (UpdateDemographicsRequestTO) dtiCmdBody;
		 //TODO
		 return null;
	}
	
	  /**
	   * Transforms a reservation response string from the HKD provider and updates
	   * the DTITransactionTO object with the response information.
	   *
	   * @param dtiTxn          The transaction object for this request.
	   * @param otCmdTO the ot cmd TO
	   * @param dtiRespTO the dti resp TO
	   * @return The DTITransactionTO object, enriched with the response
	   *         information.
	   * @throws DTIException           for any error. Contains enough detail to formulate an error
	   *           response to the seller.
	   */
	  static void transformResponseBody(DTITransactionTO dtiTxn, HkdOTCommandTO otCmdTO, DTIResponseTO dtiRespTO)
	      throws DTIException {
		//TODO  
	 
	  }
	
}
