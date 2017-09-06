package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 *This class represents the request parameters of a Eligible Products Response.
 *
 */
public class QueryEligibilityProductsResponseTO extends CommandBodyTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 91292311005L;
	/**
	 * An array list to hold the ticket response. Note that there is generally
	 * only one, but that the object allows for more since the XSD supports it.
	 */
	private ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();
	private String saleType;

	/**
	 * @return the ticketList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param ticketList
	 *           the ticketList to set
	 */
	public void add(TicketTO ticketTO) {
		this.ticketList.add(ticketTO);
	}

	/**
	 * @return the saleType
	 */
	public String getSaleType() {
		return saleType;
	}

	/**
	 * @param saleType
	 *           the saleType to set
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

}
