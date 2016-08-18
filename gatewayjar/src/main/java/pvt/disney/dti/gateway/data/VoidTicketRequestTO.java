package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the values in an Upgrade Alpha Request.
 * 
 * @author lewit019
 * 
 */
public class VoidTicketRequestTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** An array list of tickets. */
	protected ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/** Payment */
	protected ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTktList() {
		return tktList;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTktList(ArrayList<TicketTO> ticketList) {
		this.tktList = ticketList;
	}

}
