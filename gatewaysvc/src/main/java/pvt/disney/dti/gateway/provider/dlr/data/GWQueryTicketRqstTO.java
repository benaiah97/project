package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Query Ticket portion of an eGalaxy XML.
 * 
 * @author lewit019
 * 
 */
public class GWQueryTicketRqstTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** Defines the ticket demographics request type. */
	private boolean includeTktDemographics = false;

	private boolean includePassAttributes = false;

	private boolean includeTicketRedeemability = false;

	private boolean includeRenewalAttributes = false;

	private String visualID;

	private ArrayList<String> dataRequest = new ArrayList<String>();

	/**
	 * 
	 * @param newRequest
	 */
	public void addDataRequest(String newRequest) {
		dataRequest.add(newRequest);
	}

	/**
	 * @return the dataRequest
	 */
	public ArrayList<String> getDataRequest() {
		return dataRequest;
	}

	/**
	 * @return the visualID
	 */
	public String getVisualID() {
		return visualID;
	}

	/**
	 * @param visualID
	 *            the visualID to set
	 */
	public void setVisualID(String visualID) {
		this.visualID = visualID;
	}

	public boolean isIncludePassAttributes() {
		return includePassAttributes;
	}

	public void setIncludePassAttributes(boolean includePassAttributes) {
		this.includePassAttributes = includePassAttributes;
	}

	/**
	 * @return the includeTicketRedeemability
	 */
	public boolean isIncludeTicketRedeemability() {
		return includeTicketRedeemability;
	}

	/**
	 * @param includeTicketRedeemability
	 *            the includeTicketRedeemability to set
	 */
	public void setIncludeTicketRedeemability(boolean includeTicketRedeemability) {
		this.includeTicketRedeemability = includeTicketRedeemability;
	}

	/**
	 * @return the includeTktDemographics
	 */
	public boolean isIncludeTktDemographics() {
		return includeTktDemographics;
	}

	/**
	 * @param includeTktDemographics
	 *            the includeTktDemographics to set
	 */
	public void setIncludeTktDemographics(boolean includeTktDemographics) {
		this.includeTktDemographics = includeTktDemographics;
	}

	/**
	 * @return the includeRenewalAttributes
	 */
	public boolean isIncludeRenewalAttributes() {
		return includeRenewalAttributes;
	}

	/**
	 * @param includeRenewalAttributes
	 *            the includeRenewalAttributes to set
	 */
	public void setIncludeRenewalAttributes(boolean includeRenewalAttributes) {
		this.includeRenewalAttributes = includeRenewalAttributes;
	}

}
