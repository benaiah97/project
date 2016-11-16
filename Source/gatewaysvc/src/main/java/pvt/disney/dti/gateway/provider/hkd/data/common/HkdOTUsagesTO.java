package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * The Class OTUsagesTO represents a transfer object containing the Omni Ticket Usages information.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTUsagesTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The item. */
	private Integer item;

	/** The usage type. */
	private Integer usageType;

	/** The site number. */
	private Integer siteNumber;

	/** The gate. */
	private String gate;

	/** The date time. */
	private String date;

	private String time;

	/**
	 * Gets the gate.
	 * 
	 * @return the gate
	 */
	public String getGate() {
		return gate;
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public Integer getItem() {
		return item;
	}

	/**
	 * Gets the site number.
	 * 
	 * @return the siteNumber
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Gets the usage type.
	 * 
	 * @return the usageType
	 */
	public Integer getUsageType() {
		return usageType;
	}

	/**
	 * Sets the gate.
	 * 
	 * @param gateIn
	 *            the gate in
	 */
	public void setGate(String gateIn) {
		this.gate = gateIn;
	}

	/**
	 * Sets the item.
	 * 
	 * @param itemIn
	 *            the item in
	 */
	public void setItem(Integer itemIn) {
		this.item = itemIn;
	}

	/**
	 * Sets the site number.
	 * 
	 * @param siteNumberIn
	 *            the site number in
	 */
	public void setSiteNumber(Integer siteNumberIn) {
		this.siteNumber = siteNumberIn;
	}

	/**
	 * Sets the usage type.
	 * 
	 * @param usageTypeIn
	 *            the usage type in
	 */
	public void setUsageType(Integer usageTypeIn) {
		this.usageType = usageTypeIn;
	}

	/**
	 * 
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the date to set.
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 
	 * @return the time.
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 * 
	 * @param time
	 *            the time to set.
	 */
	public void setTime(String time) {
		this.time = time;
	}

}
