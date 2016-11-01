package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;

/**
 * This class represents a voucher payment in Omni Ticket.
 * 
 * @author lewit019
 * 
 */
public class OTVoucherTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** The voucher master code. */
	protected String masterCode;

	/** The voucher unique code. */
	protected String uniqueCode;

	/**
	 * @return the masterCode
	 */
	public String getMasterCode() {
		return masterCode;
	}

	/**
	 * @return the uniqueCode
	 */
	public String getUniqueCode() {
		return uniqueCode;
	}

	/**
	 * @param masterCode
	 *            the masterCode to set
	 */
	public void setMasterCode(String mainCode) {
		this.masterCode = mainCode;
	}

	/**
	 * @param uniqueCode
	 *            the uniqueCode to set
	 */
	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

}
