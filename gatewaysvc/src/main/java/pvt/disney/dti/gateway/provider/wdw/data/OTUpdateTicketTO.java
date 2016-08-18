package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;

/**
 * This class is a transfer object which represents the Omni Ticket update ticket.
 * 
 * @author lewit019
 * 
 */
public class OTUpdateTicketTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The tagsList. */
	private ArrayList<String> tagsList = new ArrayList<String>();

	/** The site number. */
	private Integer siteNumber;

	/** TicketInfo. */
	private ArrayList<OTTicketInfoTO> tktInfoList = new ArrayList<OTTicketInfoTO>();

	/** OT Error Info */
	private OTErrorTO error;

	/**
	 * 
	 * @return the error for this transaction.
	 */
	public OTErrorTO getError() {
		return error;
	}

	/**
	 * Sets the error for this transaction.
	 * 
	 * @param error
	 *            the error transfer object to set.
	 */
	public void setError(OTErrorTO error) {
		this.error = error;
	}

	/**
	 * 
	 * @return the site number.
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Sets the site number.
	 * 
	 * @param siteNumber
	 *            the site number to set.
	 */
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	/**
	 * @return the array list of tag strings.
	 */
	public ArrayList<String> getTagsList() {
		return tagsList;
	}

	/**
	 * Sets the array list of tag strings.
	 * 
	 * @param tagsList
	 *            the array list to set.
	 */
	public void setTagsList(ArrayList<String> tagsList) {
		this.tagsList = tagsList;
	}

	/**
	 * 
	 * @return the ticket info transfer object array list.
	 */
	public ArrayList<OTTicketInfoTO> getTktInfoList() {
		return tktInfoList;
	}

	/**
	 * Sets the ticket info transfer object array list.
	 * 
	 * @param tktInfoList
	 *            the array list to set.
	 */
	public void setTktInfoList(ArrayList<OTTicketInfoTO> tktInfoList) {
		this.tktInfoList = tktInfoList;
	}

}
