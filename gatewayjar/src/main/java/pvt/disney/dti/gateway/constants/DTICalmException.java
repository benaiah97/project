package pvt.disney.dti.gateway.constants;

import pvt.disney.dti.gateway.data.DTITransactionTO;

/**
 * 
 * @author lewit019
 * 
 */
public class DTICalmException extends Exception {

	/** Serial Version UID */
	private static final long serialVersionUID = 90210L;

	/** DTI Transaction containing both request and response. */
	private DTITransactionTO dtiTxn = null;

	/**
	 * Initializes a WDWException using an error code message.
	 * 
	 * @param messageKey
	 *            A string Message
	 */
	public DTICalmException(DTITransactionTO dtiTxnIn) {
		dtiTxn = dtiTxnIn;
		return;
	}

	public DTITransactionTO getDtiTxn() {
		return dtiTxn;
	}

}
