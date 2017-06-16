package pvt.disney.dti.gateway.provider.wdw.data;

import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;

/**
 * @author RASTA006
 *
 */
public class OTQueryEligibleProductsTo {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	// Out-bound fields
	/** The tagsList. */
	private ArrayList<String> tagsList = new ArrayList<String>();

	/** The site number. */
	private Integer siteNumber;
	
	/** The sale type. UPGRADE/RENEW */
	private String saletype;

	// In-bound fields
	private OTErrorTO error;

	// Shared fields
	/** The ticket info list. */
	private ArrayList<OTTicketInfoTO> ticketInfoList = new ArrayList<OTTicketInfoTO>();

	/**
	 * Gets the site number.
	 * 
	 * @return the siteNumber
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Gets the tagsList.
	 * 
	 * @return the tagsList
	 */
	public ArrayList<String> getTagsList() {
		return tagsList;
	}

	/**
	 * Gets the ticket info list.
	 * 
	 * @return the ticketInfoList
	 */
	public ArrayList<OTTicketInfoTO> getTicketInfoList() {
		return ticketInfoList;
	}

	/**
	 * Sets the site number.
	 * 
	 * @param siteNumber
	 *            the siteNumber to set
	 */
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	/**
	 * @return the error
	 */
	public OTErrorTO getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(OTErrorTO error) {
		this.error = error;
	}

	/**
	 * @return the saletype
	 */
	public String getSaletype() {
		return saletype;
	}

	/**
	 * @param saletype the saletype to set
	 */
	public void setSaletype(String saletype) {
		this.saletype = saletype;
	}
	
	

}
