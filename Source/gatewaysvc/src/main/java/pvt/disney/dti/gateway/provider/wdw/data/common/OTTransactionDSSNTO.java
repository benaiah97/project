package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.text.ParseException;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;

/**
 * This class represents an Omni Ticket TransactionDSSN transfer object.
 * 
 * @author lewit019
 */
public class OTTransactionDSSNTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The site. */
	private String site;

	/** The station. */
	private String station;

	/** The date. */
	private GregorianCalendar date;

	/** The transaction id. */
	private Integer transactionId;

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * Gets the site.
	 * 
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * Gets the station.
	 * 
	 * @return the station
	 */
	public String getStation() {
		return station;
	}

	/**
	 * Gets the transaction id.
	 * 
	 * @return the transactionId
	 */
	public Integer getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	/**
	 * Sets the DSSN for a transaction using a string-based version of the date. The format used is yy-MM-dd (for example, 09-01-31).
	 * 
	 * @param dateIn
	 *            the date in
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	public void setDate(String dateIn) throws ParseException {

		this.date = OTCommandTO.convertOmniYYDate(dateIn);

		return;
	}

	/**
	 * Sets the site.
	 * 
	 * @param site
	 *            the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * Sets the station.
	 * 
	 * @param station
	 *            the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}

	/**
	 * Sets the transaction id.
	 * 
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

}
