package pvt.disney.dti.gateway.data;

import java.io.Serializable;

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
	   * An array list of tickets (although generally there should only be one, this matches the XML specification.
	   */
	  private TicketTO ticketTO = new TicketTO();
	  private String saleType;

	/**
	 * @return the ticketTO
	 */
	public TicketTO getTicketTO() {
		return ticketTO;
	}

	/**
	 * @param ticketTO the ticketTO to set
	 */
	public void setTicketTO(TicketTO ticketTO) {
		this.ticketTO = ticketTO;
	}

	/**
	 * @return the saleType
	 */
	public String getSaleType() {
		return saleType;
	}

	/**
	 * @param saleType the saleType to set
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	 
	 

	  
}
