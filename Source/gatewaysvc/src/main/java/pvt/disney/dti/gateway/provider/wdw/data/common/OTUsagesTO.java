package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.text.ParseException;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;

/**
 * The Class OTUsagesTO represents a transfer object containing the Omni Ticket Usages information.
 * 
 * @author lewit019
 */
public class OTUsagesTO implements Serializable {

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
	private GregorianCalendar date;

	/** The time. */
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
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the date to set.
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	
	/**
	 * Allows the caller to set the validity end date as a string, with the
	 * format yy-MM-dd (such as 09-01-31).
	 * 
	 * @param validityEndDate
	 *           the string value of the validity end date to be set.
	 * @throws ParseException
	 *            if the string cannot be parsed.
	 */
	public void setDate(String date) throws ParseException {
		
		this.date = OTCommandTO.convertOmniYYDate(date);

		return;
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
