package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * The Class OTUpgradeTicketInfoTO represents a transfer object containing the upgrade ticket info fields for Omni Ticket.
 * 
 * @author lewit019
 */
public class OTUpgradeTicketInfoTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	// Outbound fields
	/** The item. */
	private BigInteger item;

	/** The ticket search mode. */
	private OTTicketTO ticketSearchMode;

	/** The ticket type. */
	private BigInteger ticketType;

	/** The price. */
	private BigDecimal price;

	/** The price override. */
	private BigDecimal priceOverride;

	/** The validity start date. */
	private GregorianCalendar validityStartDate;

	/** The validity end date. */
	private GregorianCalendar validityEndDate;

	/** The ticket note. */
	private String ticketNote;

	/** The Demographics. (As of 2.15, JTL) */
	private OTDemographicInfo demographicInfo;

	// In-bound fields
	/** The item status. */
	private Integer itemStatus;

	/** The remaining value. */
	private BigDecimal remainingValue;

	/** The void code. */
	private Integer voidCode;

	/** The tax. */
	private BigDecimal tax;

	/** The ticket. */
	private OTTicketTO ticket;

	/** The ticket flag list. */
	private ArrayList<Integer> ticketFlagList = new ArrayList<Integer>();
	
	/** The prod price token. */
	private String prodPriceToken;
	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public BigInteger getItem() {
		return item;
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
	 * Gets the ticket search mode.
	 * 
	 * @return the ticketSearchMode
	 */
	public OTTicketTO getTicketSearchMode() {
		return ticketSearchMode;
	}

	/**
	 * Gets the ticket type.
	 * 
	 * @return the ticketType
	 */
	public BigInteger getTicketType() {
		return ticketType;
	}

	/**
	 * Gets the validity end date.
	 * 
	 * @return the validityEndDate
	 */
	public GregorianCalendar getValidityEndDate() {
		return validityEndDate;
	}

	/**
	 * Gets the validity start date.
	 * 
	 * @return the validityStartDate
	 */
	public GregorianCalendar getValidityStartDate() {
		return validityStartDate;
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
	 * Sets the price.
	 * 
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Sets the ticket search mode.
	 * 
	 * @param ticketSearchMode
	 *            the ticketSearchMode to set
	 */
	public void setTicketSearchMode(OTTicketTO ticketSearchMode) {
		this.ticketSearchMode = ticketSearchMode;
	}

	/**
	 * Sets the ticket type.
	 * 
	 * @param ticketType
	 *            the ticketType to set
	 */
	public void setTicketType(BigInteger ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * Sets the validity end date.
	 * 
	 * @param validityEndDate
	 *            the validityEndDate to set
	 */
	public void setValidityEndDate(GregorianCalendar validityEndDate) {
		this.validityEndDate = validityEndDate;
	}

	/**
	 * Sets the validity start date.
	 * 
	 * @param validityStartDate
	 *            the validityStartDate to set
	 */
	public void setValidityStartDate(GregorianCalendar validityStartDate) {
		this.validityStartDate = validityStartDate;
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
	 * Sets the ticket note.
	 * 
	 * @param ticketNote
	 *            the ticketNote to set
	 */
	public void setTicketNote(String ticketNote) {
		this.ticketNote = ticketNote;
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
	 * Gets the ticket.
	 * 
	 * @return the ticket
	 */
	public OTTicketTO getTicket() {
		return ticket;
	}

	/**
	 * Gets the ticket flag list.
	 * 
	 * @return the ticketFlagList
	 */
	public ArrayList<Integer> getTicketFlagList() {
		return ticketFlagList;
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
	 * Sets the item status.
	 * 
	 * @param itemStatus
	 *            the itemStatus to set
	 */
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
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
	 * Sets the ticket.
	 * 
	 * @param ticket
	 *            the ticket to set
	 */
	public void setTicket(OTTicketTO ticket) {
		this.ticket = ticket;
	}

	/**
	 * Sets the ticket flag list.
	 * 
	 * @param ticketFlagList
	 *            the ticketFlagList to set
	 */
	public void setTicketFlagList(ArrayList<Integer> ticketFlagList) {
		this.ticketFlagList = ticketFlagList;
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
	 * @return the price override
	 */
	public BigDecimal getPriceOverride() {
		return priceOverride;
	}

	/**
	 * @param priceOverride
	 *            the price override to set
	 */
	public void setPriceOverride(BigDecimal priceOverride) {
		this.priceOverride = priceOverride;
	}

	/**
	 * @return the demographicInfo
	 */
	public OTDemographicInfo getDemographicInfo() {
		return demographicInfo;
	}

	/**
	 * @param demographicInfo
	 *            the demographicInfo to set
	 */
	public void setDemographicInfo(OTDemographicInfo demographicInfo) {
		this.demographicInfo = demographicInfo;
	}

   /**
    * @return the prodPriceToken
    */
   public String getProdPriceToken() {
      return prodPriceToken;
   }

   /**
    * @param prodPriceToken
    *           the prodPriceToken to set
    */
   public void setProdPriceToken(String prodPriceToken) {
      this.prodPriceToken = prodPriceToken;
   }



}
