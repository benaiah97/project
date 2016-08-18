package pvt.disney.dti.gateway.provider.dlr.data;

import java.math.BigDecimal;

/**
 * 
 * @author lewit019
 * 
 */
public class GWTicketTO {

	/** Unique identifier for DLR ticket. */
	private String visualID;

	/** used for upgrade alpha a.k.a. TickcateActivation.Command==Activate */
	private String itemCode;

	/** used for upgrade alpha a.k.a. TickcateActivation.Command==Activate */
	private String price;

	private String plu;

	private String description;

	private String tax;

	private String status;

	private BigDecimal redeemedPoints;

	private String eventID;

	/**
	 * 
	 * @return
	 */
	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getRedeemedPoints() {
		return redeemedPoints;
	}

	public void setRedeemedPoints(BigDecimal redeemedPoints) {
		this.redeemedPoints = redeemedPoints;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	/**
	 * Constructor convenience.
	 * 
	 * @param visualID
	 */
	public GWTicketTO(String visualID) {
		this.visualID = visualID;
	}

	/**
	 * Constructor for convenience;
	 * 
	 * @param visualID
	 * @param itemCode
	 * @param price
	 */
	public GWTicketTO(String visualID, String itemCode, String price) {
		this.visualID = visualID;
		this.itemCode = itemCode;
		this.price = price;
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * Accessor
	 * 
	 * @param price
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * Accessor
	 * 
	 * @param itemCode
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
}
