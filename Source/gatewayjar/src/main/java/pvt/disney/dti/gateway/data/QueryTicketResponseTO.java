package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the response to a query ticket request.
 * 
 * @author lewit019
 * 
 */
public class QueryTicketResponseTO extends CommandBodyTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/**
	 * An array list to hold the ticket response. Note that there is generally only one, but that the object allows for more since the XSD supports it.
	 */
	private ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void addTicket(TicketTO aTicket) {
		ticketList.add(aTicket);
	}

}
