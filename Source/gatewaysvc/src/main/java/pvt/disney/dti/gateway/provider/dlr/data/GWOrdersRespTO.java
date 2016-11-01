package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * This class represents the Orders portion of an eGalaxy response (corresponds to DTI's Reservation response.
 * 
 * @author lewit019
 * 
 */
public class GWOrdersRespTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private String orderID;

	private String galaxyOrderID;

	private String paymentContractID; // As of 2.16.1, JTL

	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return orderID;
	}

	/**
	 * @param orderID
	 *            the orderID to set
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the galaxyOrderID
	 */
	public String getGalaxyOrderID() {
		return galaxyOrderID;
	}

	/**
	 * @param galaxyOrderID
	 *            the galaxyOrderID to set
	 */
	public void setGalaxyOrderID(String galaxyOrderID) {
		this.galaxyOrderID = galaxyOrderID;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the authCode
	 */
	public String getAuthCode() {
		return authCode;
	}

	/**
	 * @param authCode
	 *            the authCode to set
	 */
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	private int status;

	private String authCode;

	private ArrayList<TicketRecord> ticketArray = new ArrayList<TicketRecord>();

	/**
	 * 
	 * @author lewit019
	 * 
	 */
	public class TicketRecord {

		private String visualID;
		private String plu;
		private String description;
		private BigDecimal price;

		/**
		 * @return the price
		 */
		public BigDecimal getPrice() {
			return price;
		}

		/**
		 * @param price
		 *            the price to set
		 */
		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		/**
		 * @return the visualID
		 */
		public String getVisualID() {
			return visualID;
		}

		/**
		 * @param visualID
		 *            the visualID to set
		 */
		public void setVisualID(String visualID) {
			this.visualID = visualID;
		}

		/**
		 * @return the plu
		 */
		public String getPlu() {
			return plu;
		}

		/**
		 * @param plu
		 *            the plu to set
		 */
		public void setPlu(String plu) {
			this.plu = plu;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @param description
		 *            the description to set
		 */
		public void setDescription(String description) {
			this.description = description;
		}

	}

	/**
	 * @return the ticketArray
	 */
	public ArrayList<TicketRecord> getTicketArray() {
		return ticketArray;
	}

	/**
	 * @param ticketArray
	 *            the ticketArray to set
	 */
	public void setTicketArray(ArrayList<TicketRecord> ticketArray) {
		this.ticketArray = ticketArray;
	}

	public void addTicketRecord(TicketRecord inRecord) {
		ticketArray.add(inRecord);
	}

	/**
	 * @return the paymentContractID
	 */
	public String getPaymentContractID() {
		return paymentContractID;
	}

	/**
	 * @param paymentContractID
	 *            the paymentContractID to set
	 */
	public void setPaymentContractID(String paymentContractID) {
		this.paymentContractID = paymentContractID;
	}

}
