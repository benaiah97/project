package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the values in an Upgrade Alpha Response.
 * 
 * @author lewit019
 * 
 */
public class UpgradeAlphaResponseTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** An array list of tickets. */
	private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/** An array list of products. */
	private ArrayList<ProductTO> productList = new ArrayList<ProductTO>();

	/** An array of payments. */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/** Receipt Message */
	private String receiptMessage;

	/** ClientData (not supported by DTI) */

	/** Transaction Numeric ID (not produced in response XML) */

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @return the productList
	 */
	public ArrayList<ProductTO> getProductList() {
		return productList;
	}

	/**
	 * @return the receiptMessage
	 */
	public String getReceiptMessage() {
		return receiptMessage;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTktList() {
		return tktList;
	}

	/**
	 * @param receiptMessage
	 *            the receiptMessage to set
	 */
	public void setReceiptMessage(String receiptMessage) {
		this.receiptMessage = receiptMessage;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void addTicket(TicketTO aTicket) {
		tktList.add(aTicket);
	}

	public void addPayment(PaymentTO aPayment) {
		paymentList.add(aPayment);
	}

	public void addProduct(ProductTO aProduct) {
		productList.add(aProduct);
	}

}
