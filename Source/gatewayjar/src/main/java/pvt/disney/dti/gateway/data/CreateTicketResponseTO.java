package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Value/Transfer class encapsulates the values in a create ticket response.
 * 
 * @author lewit019
 * 
 */
public class CreateTicketResponseTO extends CommandBodyTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = 9129227490L;

	/** An array list of tickets. */
	protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/** An array list of products. */
	protected ArrayList<ProductTO> productList = new ArrayList<ProductTO>();

	/** An array of payments. */
	protected ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/**
	 * @return the productList
	 */
	public ArrayList<ProductTO> getProductList() {
		return productList;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param productList
	 *            the productList to set
	 */
	public void setProductList(ArrayList<ProductTO> productList) {
		this.productList = productList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.ticketList = ticketList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void addTicket(TicketTO aTicket) {
		ticketList.add(aTicket);
	}

	/**
	 * 
	 * @param aProduct
	 *            the product to add
	 */
	public void addProduct(ProductTO aProduct) {
		productList.add(aProduct);
	}

	/**
	 * @param aPayment
	 *            the PaymentTO to add
	 */
	public void addPayment(PaymentTO aPayment) {
		paymentList.add(aPayment);
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

}
