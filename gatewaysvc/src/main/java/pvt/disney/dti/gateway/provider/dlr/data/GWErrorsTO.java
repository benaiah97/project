/**
 * 
 */
package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

/**
 * This is the parent class for all Gateway Error Transfer objects.
 * 
 * @author smoon
 */
public class GWErrorsTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The error code. */
	private String errorCode;

	/** The error text. */
	private String errorText;

	/**
	 * Gets the error code.
	 * 
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the error code.
	 * 
	 * @param errorCode
	 *            the new error code
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Gets the error text.
	 * 
	 * @return the error text
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * Sets the error text.
	 * 
	 * @param errorText
	 *            the new error text
	 */
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
}
