package pvt.disney.dti.gateway.rules.dlr;

import org.dom4j.DocumentException;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;

/**
 * The purpose of this class is to look beyond the top level status code in an egalaxy response and determine a more specific error to return. If we base error responses just on status code, we could return an inappropriate error. For
 * examle, a QueryTicket that returns a 1300 Error (QueryTicket request error) would map to a DTI invalid ticket error when what might be hidden deeper inside a QueryTicketErrors element is a egalaxy 999 error, which indicates a critical
 * database, memory, or other failure in egalxy itself. This should map to an 871 error, otherwise in this scenario valid AP passes could come back as invalid.
 * 
 * @author smoon
 * 
 */
public class DLRErrorRules {

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(DLRErrorRules.class.getCanonicalName());

	private static final String CRITICAL_ERROR = "-1";

	/**
	 * Examines the transaction type and Passes the xmlResponse and status to the appropriate internal helper method for the transaction type (QueryTicket, VoidTicket, etc) and determines the appropriate error code to use for the
	 * transaction.
	 * 
	 * @param dtiTxn
	 * @param statusCode
	 * @return
	 */
	public static String processStatusError(TransactionType requestType,
			String statusCode, String xmlResponse) {
		// NOTE: After refactoring we don't really need the whole dtiTxn, maybe
		// consider passing in something smaller to determine transaction type,
		// like the request type itself?
		String errorCode = null;

		switch (requestType) {

		case QUERYTICKET:
			errorCode = processQueryTicketErrorStatus(xmlResponse, statusCode);
			break;

		case UPGRADEALPHA:
			errorCode = processUpgradeAlphaErrorStatus(xmlResponse, statusCode);
			break;

		case VOIDTICKET:
			errorCode = processVoidTicketErrorStatus(xmlResponse, statusCode);
			break;

		case RESERVATION:
			errorCode = processReservationErrorStatus(xmlResponse, statusCode);
			break;

		case QUERYRESERVATION:
			errorCode = processQueryReservationErrorStatus(xmlResponse,
					statusCode);
			break;
			
		case RENEWENTITLEMENT:
      errorCode = processRenewEntitlementErrorStatus(xmlResponse,
          statusCode);
      
		case QUERYELIGIBLEPRODUCTS:
	      errorCode = processQueryEligibleProductErrorStatus(xmlResponse,
	          statusCode);
      break;			
		}
		return errorCode;
	}

	/**
	 * Internal Helper method to parse query ticket response for detailed errors Protected to allow calls from test cases...looks for these types of special errors:
	 * 
	 * <pre>
	 * <QueryTicketErrors>
	 *  	<Errors>
	 *  		<Error>
	 * 				<ErrorCode>999</ErrorCode>
	 * 					<ErrorText>An error occurred while processing the request: An attempt
	 *  to set the value of the TGTSPicture.ImageData property in the
	 *  [SelectLatestByVisualID] section of the script file TGTSPicturesSQL
	 *  failed due to the following reason: Access violation at address
	 *  0042DC1C in module 'eGalaxy.exe'. Read of address
	 *  F85589FD</ErrorText>
	 *  		</Error>
	 *  	</Errors>
	 *  	<DataRequestErrors />
	 *  </QueryTicketErrors>
	 * 		
	 *  or
	 * 		
	 *  <QueryTicketErrors>
	 *  	<DataRequestErrors>
	 *  		<DataRequestError>
	 *  		<ErrorCode>1302</ErrorCode>
	 *  		<ErrorText>The customer of this ticket is restricted by the
	 *  source</ErrorText>
	 *  		</DataRequestError>
	 *  	</DataRequestErrors>
	 * </QueryTicketErrors>
	 * </pre>
	 * 
	 * @param statusCode
	 * @return
	 */
	protected static String processQueryTicketErrorStatus(String xmlResponse,
			String statusCode) {
		// the errorCode we will hand back after checking for special errors
		// if no special errors found, it is the statusCode
		String errorCode = null;
		// the error text for an errorCode (if found)...grabbed for logging
		String errorText = null;

		// response document to check for errors
		org.dom4j.Document respDoc = null;

		// parse the response into the doc
		try {
			respDoc = org.dom4j.DocumentHelper.parseText(xmlResponse);
		}
		catch (DocumentException e) {
			// if we can't parse it something is very wrong with the eGalaxy
			// response
			logger.sendEvent(
					"Forced return of errorCode" + CRITICAL_ERROR + ": Unable to parse xmlResponse" + xmlResponse,
					EventType.DEBUG, new DLRErrorRules());
			errorCode = CRITICAL_ERROR;
		}

		// if all went well we can start hunting for errors
		if (respDoc != null) {

			// log for <QueryTicketErrors>
			org.dom4j.Node queryTicketErrors = respDoc
					.selectSingleNode("//QueryTicketErrors");
			// if we find it, log it
			if (queryTicketErrors != null) {
				logger.sendEvent(
						"QueryTicket Errors element:" + queryTicketErrors
								.asXML(), EventType.DEBUG, new DLRErrorRules());
			}

			// and try to grab //QueryTicketErrors/ERror/Error/ErrorCode
			org.dom4j.Node queryTickerErrorCode = queryTicketErrors
					.selectSingleNode("//Errors/Error/ErrorCode");

			// if we found one....
			if (queryTickerErrorCode != null && queryTickerErrorCode.getText() != null && queryTickerErrorCode
					.getText().length() > 0) {

				// grab the details for the error
				errorCode = queryTickerErrorCode.getText();
				errorText = queryTicketErrors.selectSingleNode(
						"//Errors/Error/ErrorText").getText();

				// and log it
				logger.sendEvent(
						"DLRErrorRules.processQueryTicketErrorStatus() found a QueryTicketError: " + errorCode + ":" + errorText,
						EventType.DEBUG, new DLRErrorRules());
			}
			// if we didn't find QueryTicketErrors/Errors/Error/ErrorCode we need to
			// check for
			// QueryTicketErrors/DataRequestErrors
			else if (queryTicketErrors
					.selectSingleNode("//DataRequestErrors/DataRequestError/ErrorCode") != null) {

				// if we have a DataRequestError, grab its details
				errorCode = queryTicketErrors.selectSingleNode(
						"//DataRequestErrors/DataRequestError/ErrorCode")
						.getText();
				errorText = queryTicketErrors.selectSingleNode(
						"//DataRequestErrors/DataRequestError/ErrorText")
						.getText();

				// and log it
				logger.sendEvent(
						"DLRErrorRules.processQueryTicketErrorStatus() found a DataRequestError: " + errorCode + ":" + errorText,
						EventType.DEBUG, new DLRErrorRules());

			}
			// otherwise, no special error code found so we just set the errorCode to
			// the
			// incoming statusCode
			else {
				errorCode = statusCode;
			}
		}
		// now we just hand back the error code and our work is done
		return errorCode;
	}
	
	/**
	 * Process query eligible product error status.
	 *
	 * @param xmlResponse the xml response
	 * @param statusCode the status code
	 * @return the string
	 */
	protected static String processQueryEligibleProductErrorStatus(String xmlResponse,
			String statusCode){
		String errorCode = null;
		errorCode=statusCode;
		return errorCode;
	}

	/**
	 * Internal Helper method to parse orders response for detailed errors Protected to allow calls from test cases
	 * 
	 * @param xmlResponse
	 * @param statusCode
	 * @return
	 */
	protected static String processReservationErrorStatus(String xmlResponse,
			String statusCode) {
		String errorCode = null;
		// TO DO until code to peek deeper into eGalaxy OrdersError/OrderError
		// element(s) is done,
		// we will just set the errorCode to the statusCode
		errorCode = statusCode;
		return errorCode;
	}

	/**
	 * Internal Helper method to ticket activation (upgrade alpha) response for detailed errors Protected to allow calls from test cases
	 * 
	 * @param xmlResponse
	 * @param statusCode
	 * @return
	 */
	protected static String processUpgradeAlphaErrorStatus(String xmlResponse,
			String statusCode) {
		String errorCode = null;
		// TO DO until code to peek deeper into eGalaxy TicketActivationErrors
		// element is done,
		// we will just set the errorCode to the statusCode
		errorCode = statusCode;
		return errorCode;
	}

	/**
	 * Internal Helper method to parse ticket activation (void ticket) response for detailed errors Protected to allow calls from test cases
	 * 
	 * @param xmlResponse
	 * @param statusCode
	 * @return
	 */
	protected static String processVoidTicketErrorStatus(String xmlResponse,
			String statusCode) {
		String errorCode = null;
		// TO DO until code to peek deeper into eGalaxy VoidTicketErrors element
		// is done,
		// we will just set the errorCode to the statusCode
		errorCode = statusCode;
		return errorCode;
	}

	/**
	 * Internal Helper method to parse ticket activation (void ticket) response for detailed errors Protected to allow calls from test cases
	 * 
	 * @param xmlResponse
	 * @param statusCode
	 * @return
	 */
	protected static String processQueryReservationErrorStatus(
			String xmlResponse, String statusCode) {
		String errorCode = null;
		// TO DO until code to peek deeper into eGalaxy VoidTicketErrors element
		// is done,
		// we will just set the errorCode to the statusCode
		errorCode = statusCode;
		return errorCode;
	}
	
	 /**
   * Internal Helper method to parse ticket activation (renew entitlement) response for detailed errors Protected to allow calls from test cases
   * 
   * @param xmlResponse
   * @param statusCode
   * @return
   */
  protected static String processRenewEntitlementErrorStatus(
      String xmlResponse, String statusCode) {
    String errorCode = null;
    // TO DO until code to peek deeper into eGalaxy VoidTicketErrors element
    // is done,
    // we will just set the errorCode to the statusCode
    errorCode = statusCode;
    return errorCode;
  }

}
