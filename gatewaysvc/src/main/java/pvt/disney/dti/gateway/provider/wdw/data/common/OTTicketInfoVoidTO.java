package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * This class represents a transfer object containing an Omni Ticket Ticket Info Void.
 * 
 * @author lewit019
 */
public class OTTicketInfoVoidTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The item. */
	private Integer item;

	/** The item status. */
	private Integer itemStatus;

	/** The Ticket Type. */
	private Integer ticketType;

	/** The price. */
	private BigDecimal price;

	/** The tax. */
	private BigDecimal tax;

	/** The remaining value. */
	private BigDecimal remainingValue;

	/** The void code. */
	private Integer voidCode;

	/** The ticket. */
	private OTTicketTO ticket;

	/** The last usage date. */
	private GregorianCalendar lastUsageDate;

	/** The tax exempt. */
	private Boolean taxExempt;

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public Integer getItem() {
		return item;
	}

	/**
	 * Gets the item status.
	 * 
	 * @return the itemStatus
	 */
	public Integer getItemStatus() {
		return itemStatus;
	}

	/**
	 * Gets the last usage date.
	 * 
	 * @return the lastUsageDate
	 */
	public GregorianCalendar getLastUsageDate() {
		return lastUsageDate;
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Gets the remaining value.
	 * 
	 * @return the remainingValue
	 */
	public BigDecimal getRemainingValue() {
		return remainingValue;
	}

	/**
	 * Gets the tax.
	 * 
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * Gets the tax exempt.
	 * 
	 * @return the taxExempt
	 */
	public Boolean getTaxExempt() {
		return taxExempt;
	}

	/**
	 * Gets the ticket.
	 * 
	 * @return the ticket
	 */
	public OTTicketTO getTicket() {
		return ticket;
	}

	/**
	 * Gets the ticket type.
	 * 
	 * @return the ticketType
	 */
	public Integer getTicketType() {
		return ticketType;
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
	public void setItem(Integer item) {
		this.item = item;
	}

	/**
	 * Sets the item status.
	 * 
	 * @param itemStatus
	 *            the itemStatus to set
	 */
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * Sets the last usage date.
	 * 
	 * @param lastUsageDate
	 *            the lastUsageDate to set
	 */
	public void setLastUsageDate(GregorianCalendar lastUsageDate) {
		this.lastUsageDate = lastUsageDate;
	}

	/**
	 * Sets the price.
	 * 
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Sets the remaining value.
	 * 
	 * @param remainingValue
	 *            the remainingValue to set
	 */
	public void setRemainingValue(BigDecimal remainingValue) {
		this.remainingValue = remainingValue;
	}

	/**
	 * Sets the tax.
	 * 
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * Sets the tax exempt.
	 * 
	 * @param taxExempt
	 *            the taxExempt to set
	 */
	public void setTaxExempt(Boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	/**
	 * Sets the ticket.
	 * 
	 * @param ticket
	 *            the ticket to set
	 */
	public void setTicket(OTTicketTO ticket) {
		this.ticket = ticket;
	}

	/**
	 * Sets the ticket type.
	 * 
	 * @param ticketType
	 *            the ticketType to set
	 */
	public void setTicketType(Integer ticketType) {
		this.ticketType = ticketType;
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

}
