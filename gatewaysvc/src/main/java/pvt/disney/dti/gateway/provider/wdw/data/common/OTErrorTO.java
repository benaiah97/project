package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;

/**
 * This class is a transfer object which represents an Omni Ticket error.
 * 
 * @author lewit019
 */
public class OTErrorTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The error code. */
	private Integer errorCode;

	/** The error short description. */
	private String errorShortDescription;

	/** The error description. */
	private String errorDescription;

	/**
	 * Gets the error code.
	 * 
	 * @return the errorCode
	 */
	public Integer getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets the error description.
	 * 
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Gets the error short description.
	 * 
	 * @return the errorShortDescription
	 */
	public String getErrorShortDescription() {
		return errorShortDescription;
	}

	/**
	 * Sets the error code.
	 * 
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Sets the error description.
	 * 
	 * @param errorDescription
	 *            the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Sets the error short description.
	 * 
	 * @param errorShortDescription
	 *            the errorShortDescription to set
	 */
	public void setErrorShortDescription(String errorShortDescription) {
		this.errorShortDescription = errorShortDescription;
	}
}
