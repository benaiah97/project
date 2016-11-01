package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * This class represents the a single element of the result set of a payment lookup.
 * 
 * @author lewit019
 */
public class PaymentLookupTO implements Serializable {

	/** Standard serial version UID. */
	private static final long serialVersionUID = -5387496705423442264L;

	/** The lookup value. */
	private String lookupValue;

	/** The payment code. */
	private String pymtCode;

	/**
	 * @return the lookupValue
	 */
	public String getLookupValue() {
		return lookupValue;
	}

	/**
	 * @return the pymtCode
	 */
	public String getPymtCode() {
		return pymtCode;
	}

	/**
	 * @param lookupValue
	 *            the lookupValue to set
	 */
	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}

	/**
	 * @param pymtCode
	 *            the pymtCode to set
	 */
	public void setPymtCode(String pymtCode) {
		this.pymtCode = pymtCode;
	}

}
