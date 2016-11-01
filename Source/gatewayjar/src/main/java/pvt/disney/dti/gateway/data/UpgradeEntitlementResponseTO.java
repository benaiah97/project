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
public class UpgradeEntitlementResponseTO extends CommandBodyTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = 8443163917070515607L;

	/** An array list of tickets. */
	protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/** An array list of payments. */
	protected ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/* -------------------------------------------------------------------------------- */

	/**
	 * @return the ticketList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param ticketList
	 *            the ticketList to set
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
	 * @param dtiTkt
	 *            the dtiTkt to add
	 */
	public void addTicket(TicketTO dtiTkt) {
		ticketList.add(dtiTkt);

	}

}
