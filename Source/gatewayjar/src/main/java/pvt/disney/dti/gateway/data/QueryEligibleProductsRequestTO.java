package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the request parameters of a Query Eligible request.
 */
public class QueryEligibleProductsRequestTO extends CommandBodyTO implements Serializable{

	  /** Standard serial version UID. */
	  final static long serialVersionUID = 91292311005L;
	  /**
	   * An array list of tickets (although generally there should only be one, this matches the XML specification.
	   */
	  /**
	   * An array list of tickets (although generally there should only be one, this matches the XML specification.
	   */
	  private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTktList() {
		return tktList;
	}

	/**
	 * @param tktList the tktList to set
	 */
	public void add(TicketTO ticketTO) {
		this.tktList.add(ticketTO);
	}
		  
}
