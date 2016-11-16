package pvt.disney.dti.gateway.client;

import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;

/**
 * Provider client is an abstract wrapper around the provider-centric communication classes. It cannot be instantiated directly, because it can't do any do anything without the provider classes.
 * 
 * @author lewit019
 * @since 2.16.3
 * 
 */
public abstract class ProviderClient {

	/**
	 * Sends the request to some provider. The provider is determined by fields inside of the dtiTxn
	 * 
	 * @param dtiTxn
	 *            The DTITransactionTO that contains the request.
	 * @param xmlRequest
	 *            The XML as formatted for consumption of the target provider.
	 * @return the xmlResponse
	 * @throws DTIException
	 *             if there are any communications level errors.
	 */
	public static String sendRequestToProvider(DTITransactionTO dtiTxn,
			String xmlRequest) throws DTIException {

		String tpiCode = dtiTxn.getTpiCode();
		if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) {

			return sendRequestToWDWProvider(dtiTxn, xmlRequest);

		}
		else if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) {

			return sendRequestToDLRProvider(dtiTxn, xmlRequest);

		}
		else if (tpiCode.equals(DTITransactionTO.TPI_CODE_HKD)) {
		  
		  return sendRequestToHKDProvider(dtiTxn, xmlRequest);
		  
		}
		else {

			throw new DTIException(ProviderClient.class,
					DTIErrorCode.INVALID_TICKET_PROVIDER,
					"Invalid ticket provider sent to queryTicket() in Java version of DTI Gateway.");

		}

	}

	/**
	 * Sends the request to the DLR provider.
	 * 
	 * @param xmlRequest
	 *            The XML request formatted for consumption by the DLR provider.
	 * @return the XML Response
	 * @throws DTIException
	 *             if there are any communications level errors.
	 */
	public static String sendRequestToDLRProvider(DTITransactionTO dtiTxn, String xmlRequest) throws DTIException {

		String xmlResponse = null;

		// Send (via HTTP client)
		DLRHTTPClient dlrHttpClient = new DLRHTTPClient();
		xmlResponse = dlrHttpClient.sendRequest(dtiTxn, xmlRequest);

		return xmlResponse;

	}

	/**
	 * Sends the request to the WDW provider.
	 * 
	 * @param xmlRequest
	 *            The XML request formatted for consumption by the WDW provider.
	 * @return the XML Response
	 * @throws DTIException
	 *             if there are any communications level errors.
	 */
	public static String sendRequestToWDWProvider(DTITransactionTO dtiTxn, String xmlRequest) throws DTIException {

		String xmlResponse = null;

		IagoClient wdwIagoClient = new IagoClient();

		xmlResponse = wdwIagoClient.sendRequest(dtiTxn, xmlRequest);

		return xmlResponse;

	}
	
	 /**
   * Sends the request to the WDW provider.
   * 
   * @param xmlRequest
   *            The XML request formatted for consumption by the WDW provider.
   * @return the XML Response
   * @throws DTIException
   *             if there are any communications level errors.
   * TODO             
   *             
   */
  public static String sendRequestToHKDProvider(DTITransactionTO dtiTxn, String xmlRequest) throws DTIException {

    String xmlResponse = null;

    IagoClient wdwIagoClient = new IagoClient();

    xmlResponse = wdwIagoClient.sendRequest(dtiTxn, xmlRequest);

    return xmlResponse;

  }

}
