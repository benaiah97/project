package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * This class represents a voucher payment in DTI.
 * 
 * @author lewit019
 * 
 */
public class VoucherTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** The voucher main code. */
	private String mainCode;

	/** The voucher unique code. */
	private String uniqueCode;

	/**
	 * @return the mainCode
	 */
	public String getMainCode() {
		return mainCode;
	}

	/**
	 * @return the uniqueCode
	 */
	public String getUniqueCode() {
		return uniqueCode;
	}

	/**
	 * @param mainCode
	 *            the mainCode to set
	 */
	public void setMainCode(String mainCode) {
		this.mainCode = mainCode;
	}

	/**
	 * @param uniqueCode
	 *            the uniqueCode to set
	 */
	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

}
