package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GWQueryOrderRespTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private ArrayList<TicketRecord> ticketArray = new ArrayList<TicketRecord>();

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

}
