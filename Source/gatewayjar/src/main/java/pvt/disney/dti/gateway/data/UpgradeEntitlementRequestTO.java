package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * 
 * @author HUFFM017
 * 
 */
public class UpgradeEntitlementRequestTO extends CommandBodyTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = -5390027434972940288L;

	/** An array list of tickets (or products, in this case). */
	protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/** Payment */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/**
	 * Audit Notation (as of 2.12). When the transaction note in ATS needs to be used or a reason for a failure recorded,
	 */
	private String auditNotation;

	/* -------------------------------------------------------------------------------- */

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
	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.ticketList = ticketList;
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @return the auditNotation
	 */
	public String getAuditNotation() {
		return auditNotation;
	}

	/**
	 * @param auditNotation
	 *            the auditNotation to set
	 */
	public void setAuditNotation(String auditNotation) {
		this.auditNotation = auditNotation;
	}

}
