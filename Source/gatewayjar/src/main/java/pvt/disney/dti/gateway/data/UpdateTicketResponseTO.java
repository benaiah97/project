package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the request parameters of an Update Ticket Response.
 * 
 * @author lewit019
 * 
 */
public class UpdateTicketResponseTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** An array list of tickets. */
	protected ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return tktList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.tktList = ticketList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void addTicket(TicketTO aTicket) {
		tktList.add(aTicket);
	}

}
