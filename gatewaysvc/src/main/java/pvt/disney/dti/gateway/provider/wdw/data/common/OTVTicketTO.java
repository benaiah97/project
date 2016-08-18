package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * The Class OTVTicketTO represents a void ticket transfer object.
 * 
 * @author lewit019
 */
public class OTVTicketTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The item. */
	private BigInteger item;

	/** The ticket search mode. */
	private OTTicketTO ticketSearchMode;

	/** The ticket note. */
	private String ticketNote;

	/** The void code. */
	private Integer voidCode;

	/** The item status. */
	private Integer itemStatus;

	/** The provider ticket type. */
	private BigInteger providerTicketType;

	/** Tke tkt price. */
	private BigDecimal price;

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public BigInteger getItem() {
		return item;
	}

	/**
	 * Gets the ticket note.
	 * 
	 * @return the ticketNote
	 */
	public String getTicketNote() {
		return ticketNote;
	}

	/**
	 * Gets the ticket search mode. (equivalent to getTicket()).
	 * 
	 * @return the ticketSearchMode
	 */
	public OTTicketTO getTicketSearchMode() {
		return ticketSearchMode;
	}

	/**
	 * Gets the void code.
	 * 
	 * @return the voidCode
	 */
	public Integer getVoidCode() {
		return voidCode;
	}

	/**
	 * Sets the item.
	 * 
	 * @param item
	 *            the item to set
	 */
	public void setItem(BigInteger item) {
		this.item = item;
	}

	/**
	 * Sets the ticket note.
	 * 
	 * @param ticketNote
	 *            the ticketNote to set
	 */
	public void setTicketNote(String ticketNote) {
		this.ticketNote = ticketNote;
	}

	/**
	 * Sets the ticket search mode (equivalent to setTicket()).
	 * 
	 * @param ticketSearchMode
	 *            the ticketSearchMode to set
	 */
	public void setTicketSearchMode(OTTicketTO ticketSearchMode) {
		this.ticketSearchMode = ticketSearchMode;
	}

	/**
	 * Sets the void code.
	 * 
	 * @param voidCode
	 *            the voidCode to set
	 */
	public void setVoidCode(Integer voidCode) {
		this.voidCode = voidCode;
	}

	/**
	 * @return the item status
	 */
	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * Equivalent to setTicketSearchMode
	 * 
	 * @param otTicket
	 */
	public void setTicket(OTTicketTO otTicket) {
		ticketSearchMode = otTicket;
	}

	/**
	 * Equivalent to getTicketSearchMode
	 * 
	 * @return
	 */
	public OTTicketTO getTicket() {
		return ticketSearchMode;
	}

	/**
	 * @return the provider ticket type
	 */
	public BigInteger getProviderTicketType() {
		return providerTicketType;
	}

	/**
	 * @param providerTicketType
	 *            the ticket provider type to set
	 */
	public void setProviderTicketType(BigInteger providerTicketType) {
		this.providerTicketType = providerTicketType;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
