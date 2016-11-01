package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

public class GWPaymentTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** payment code */
	private String paymentCode;
	/** description */

	private String description;

	/** amount */
	private String amount;

	/**
	 * Convenience constructor
	 * 
	 * @param paymentCode
	 * @param description
	 * @param amount
	 */
	public GWPaymentTO(String paymentCode, String description, String amount) {
		this.paymentCode = paymentCode;
		this.description = description;
		this.amount = amount;
	}

	/**
	 * Default constructor
	 */
	public GWPaymentTO() {

	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getPaymentCode() {
		return paymentCode;
	}

	/**
	 * Accessor
	 * 
	 * @param paymentCode
	 */
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Accessor
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * Accessor
	 * 
	 * @param amount
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

}
