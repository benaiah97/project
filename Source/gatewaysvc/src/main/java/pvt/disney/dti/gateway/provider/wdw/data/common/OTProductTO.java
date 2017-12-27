package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class is used as a transfer object for an Omni Ticket Product.
 * 
 * @author lewit019
 */
public class OTProductTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The item. */
	private BigInteger item;

	/** The item type. */
	private String itemType;

	/** The item item num code. */
	public BigInteger itemNumCode;

	/** The item item alpha code. */
	private String itemAlphaCode;

	/** The quantity. */
	private BigInteger quantity;

	/** The validity_ start date. */
	private GregorianCalendar validity_StartDate;

	/** The validity_ end date. */
	private GregorianCalendar validity_EndDate;

	/** The price. */
	private BigDecimal price;

	/** The ticket type. */
	private BigInteger ticketType;

	/** The tax. */
	private BigDecimal tax;

	/** The ticket name of the product. */
	private String ticketName;

	/** The description of the product. */
	private String description;

	/** The ticket attribute. */
	private BigInteger ticketAttribute;

	/** The ticket note. */
	private String ticketNote;

	/** The Entitlement Account ID. */
	private ArrayList<String> entitlementAccountId = new ArrayList<String>();

	/** The Demographics. */
	private OTDemographicInfo demographicInfo;
	
	/** The prod price token. */
	private String prodPriceToken;
	
	/**
    * @return the prodPriceToken
    */
   public String getProdPriceToken() {
      return prodPriceToken;
   }

   /**
    * @param prodPriceToken the prodPriceToken to set
    */
   public void setProdPriceToken(String prodPriceToken) {
      this.prodPriceToken = prodPriceToken;
   }

   /**
	 * 
	 * @return the ticket attribute value.
	 */
	public BigInteger getTicketAttribute() {
		return ticketAttribute;
	}

	/**
	 * Sets the ticket attribute value.
	 * 
	 * @param ticketAttribute
	 *            the value to set.
	 */
	public void setTicketAttribute(BigInteger ticketAttribute) {
		this.ticketAttribute = ticketAttribute;
	}

	/**
	 * Gets the item.
	 * 
	 * @return the item
	 */
	public BigInteger getItem() {
		return item;
	}

	/**
	 * Gets the item id_ item alpha code.
	 * 
	 * @return the itemAlphaCode
	 */
	public String getItemAlphaCode() {
		return itemAlphaCode;
	}

	/**
	 * Gets the item id_ item num code.
	 * 
	 * @return the itemNumCode
	 */
	public BigInteger getItemNumCode() {
		return itemNumCode;
	}

	/**
	 * Gets the item type.
	 * 
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
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
	 * Gets the quantity.
	 * 
	 * @return the quantity
	 */
	public BigInteger getQuantity() {
		return quantity;
	}

	/**
	 * Gets the validity_ end date.
	 * 
	 * @return the validity_EndDate
	 */
	public GregorianCalendar getValidity_EndDate() {
		return validity_EndDate;
	}

	/**
	 * Gets the validity_ start date.
	 * 
	 * @return the validity_StartDate
	 */
	public GregorianCalendar getValidity_StartDate() {
		return validity_StartDate;
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
	 * Sets the item id_ item alpha code.
	 * 
	 * @param itemAlphaCode
	 *            the itemAlphaCode to set
	 */
	public void setItemAlphaCode(String itemId_ItemAlphaCode) {
		this.itemAlphaCode = itemId_ItemAlphaCode;
	}

	/**
	 * Sets the item id_ item num code.
	 * 
	 * @param itemNumCode
	 *            the itemNumCode to set
	 */
	public void setItemNumCode(BigInteger itemId_ItemNumCode) {
		this.itemNumCode = itemId_ItemNumCode;
	}

	/**
	 * Sets the item type.
	 * 
	 * @param itemType
	 *            the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	 * Sets the quantity.
	 * 
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	/**
	 * Sets the validity_ end date.
	 * 
	 * @param validity_EndDate
	 *            the validity_EndDate to set
	 */
	public void setValidity_EndDate(GregorianCalendar validity_EndDate) {
		this.validity_EndDate = validity_EndDate;
	}

	/**
	 * Sets the validity_ start date.
	 * 
	 * @param validity_StartDate
	 *            the validity_StartDate to set
	 */
	public void setValidity_StartDate(GregorianCalendar validity_StartDate) {
		this.validity_StartDate = validity_StartDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @return the ticketName
	 */
	public String getTicketName() {
		return ticketName;
	}

	/**
	 * @return the ticketType
	 */
	public BigInteger getTicketType() {
		return ticketType;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * @param ticketName
	 *            the ticketName to set
	 */
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	/**
	 * @param ticketType
	 *            the ticketType to set
	 */
	public void setTicketType(BigInteger ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * 
	 * @return the ticket note.
	 */
	public String getTicketNote() {
		return ticketNote;
	}

	/**
	 * Sets the ticket note.
	 * 
	 * @param ticketNote
	 *            the ticket note to set.
	 */
	public void setTicketNote(String ticketNote) {
		this.ticketNote = ticketNote;
	}

	/**
	 * @return the entitlementAccountId
	 */
	public ArrayList<String> getEntitlementAccountId() {
		return entitlementAccountId;
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

}
