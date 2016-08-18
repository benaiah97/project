package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the ticket activation request portion of an eGalaxy XML. This is used for void ticket requests for dlr and for upgrade alphas for dlr.
 * 
 * @author moons012
 * 
 */
public class GWTicketActivationRqstTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/**
	 * Needed for voids and for upgrade alphas. eGalaxy customer group identifier
	 */
	private String customerID;

	/**
	 * List of tickets objects to activate or to cancel in a ticket activation message (upgrade alpha or void)
	 */
	private ArrayList<GWTicketTO> ticketList = new ArrayList<GWTicketTO>();

	/**
	 * list of payments for the payment clause if this is an activate (i.e. upgrade alpha) vs a cancel (void ticket)
	 */
	private ArrayList<GWPaymentTO> paymentList = new ArrayList<GWPaymentTO>();

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public ArrayList<GWPaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * Accessor
	 * 
	 * @param paymentList
	 */
	public void setPaymentList(ArrayList<GWPaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * Convenience method to add a payment to the list of payment in the payments clause.
	 * 
	 * @param payment
	 */
	public void addPayment(GWPaymentTO payment) {
		paymentList.add(payment);
	}

	/**
	 * Helper method to add a ticket to the list of tickets to cancel (void) or activate (upgrade alpha)
	 * 
	 * @param ticket
	 */
	public void addTicket(GWTicketTO ticket) {
		ticketList.add(ticket);
	}

	/**
	 * Helper method to add a ticket to the list of tickets to cancel (void)
	 * 
	 * @param ticket
	 */
	public void addTicket(String visualID) {
		ticketList.add(new GWTicketTO(visualID));
	}

	/**
	 * Helper method to add a ticket to the list of tickets to cancel (void) or activate (upgrade alpha)
	 * 
	 * @param ticket
	 */
	public void addTicket(String visualID, String itemCode, String price) {
		ticketList.add(new GWTicketTO(visualID, itemCode, price));
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public ArrayList<GWTicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * Accessor
	 * 
	 * @param ticketList
	 */
	public void setTicketList(ArrayList<GWTicketTO> ticketList) {
		this.ticketList = ticketList;
	}

	/**
	 * "Cancel" for voids, "Activate" for upgrade alphas.
	 */
	private String command;

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Accessor
	 * 
	 * @param command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * Accessor
	 * 
	 * @param customerID
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
}
