package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

/**
 * This class represents the Status portion of an eGalaxy XML.
 * 
 * @author lewit019, moons012
 * 
 */
public class GWStatusTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** status code for a message */
	private String statusCode;
	/** status text for a message */
	private String statusText;

	/**
	 * @return the statusCode
	 */
	public String getStatusCode() {
		return statusCode;
	}

	/**
	 * @return the statusText
	 */
	public String getStatusText() {
		return statusText;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @param statusText
	 *            the statusText to set
	 */
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

}
