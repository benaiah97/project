package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;

/**
 * This class represents the request parameters of an Update Transaction Request.
 * 
 * @author lewit019
 * 
 */
public class UpdateTransactionRequestTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** Provider Transaction */
	protected TicketTransactionTO transaction;

	/** Ticket */
	protected TicketTO ticket;

	/** Sales Rep */
	protected String salesRep;

	/** Agency */
	protected AgencyTO agency;

	/**
	 * @return the agency
	 */
	public AgencyTO getAgency() {
		return agency;
	}

	/**
	 * @return the salesRep
	 */
	public String getSalesRep() {
		return salesRep;
	}

	/**
	 * @return the ticket
	 */
	public TicketTO getTicket() {
		return ticket;
	}

	/**
	 * @return the transaction
	 */
	public TicketTransactionTO getTransaction() {
		return transaction;
	}

	/**
	 * @param agency
	 *            the agency to set
	 */
	public void setAgency(AgencyTO agency) {
		this.agency = agency;
	}

	/**
	 * @param salesRep
	 *            the salesRep to set
	 */
	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}

	/**
	 * @param ticket
	 *            the Ticket to set
	 */
	public void setTicket(TicketTO ticket) {
		this.ticket = ticket;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(TicketTransactionTO transaction) {
		this.transaction = transaction;
	}

}
