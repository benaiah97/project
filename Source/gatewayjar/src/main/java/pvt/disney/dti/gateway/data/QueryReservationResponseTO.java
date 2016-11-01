package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

public class QueryReservationResponseTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = -6690211930867903798L;

	/** Response Type */
	protected String responseType;

	/** An array list of tickets. */
	protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/** An array list of products. */
	protected ArrayList<ProductTO> productList = new ArrayList<ProductTO>();

	/** An array of payments. */
	protected ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/** Receipt Message */
	protected String receiptMessage;

	/** Reservation */
	protected ReservationTO reservation;

	/** ClientData */
	protected ClientDataTO clientData;

	/**
	 * @return the clientData
	 */
	public ClientDataTO getClientData() {
		return clientData;
	}

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
	 * @return the reservation
	 */
	public ReservationTO getReservation() {
		return reservation;
	}

	/**
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param clientData
	 *            the clientData to set
	 */
	public void setClientData(ClientDataTO clientData) {
		this.clientData = clientData;
	}

	/**
	 * @param receiptMessage
	 *            the receiptMessage to set
	 */
	public void setReceiptMessage(String receiptMessage) {
		this.receiptMessage = receiptMessage;
	}

	/**
	 * @param reservation
	 *            the reservation to set
	 */
	public void setReservation(ReservationTO reservation) {
		this.reservation = reservation;
	}

	/**
	 * @param responseType
	 *            the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * @param tktList
	 *            the tktList to add
	 */
	public void addTicket(TicketTO aTicket) {
		ticketList.add(aTicket);
	}

	/**
	 * @param aPayment
	 *            the PaymentTO to add
	 */
	public void addPayment(PaymentTO aPayment) {
		paymentList.add(aPayment);
	}

	/**
	 * @param aProduct
	 *            the ProductTO to add
	 */
	public void addProduct(ProductTO aProduct) {
		productList.add(aProduct);
	}

	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.ticketList = ticketList;
	}

	public void setProductList(ArrayList<ProductTO> productList) {
		this.productList = productList;
	}

	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

}
