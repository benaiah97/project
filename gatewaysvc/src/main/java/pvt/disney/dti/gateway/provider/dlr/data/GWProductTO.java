package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * 
 * @author lewit019
 * 
 */
public class GWProductTO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	/** The enumeration of possible transaction types in DTI. */
	public enum ProductType {
		TICKET,
		FEE,
		UNDEFINED
	};

	private ProductType type = ProductType.UNDEFINED;

	/** Unique identifier for DLR ticket. */
	private String visualID;

	private int serialNo;

	private String plu;

	private String itemName;

	private String itemDescription;

	private String itemUserCode;

	private String accessCode;

	private BigDecimal price;

	private String orderLineID;

	private BigDecimal taxAmount;

	private BigDecimal discountAmount;

	private GregorianCalendar dateSold;

	private String eventID;

	private BigDecimal netPrice;

	private BigDecimal externalPrice;

	private BigDecimal retailPrice;

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public String getVisualID() {
		return visualID;
	}

	public void setVisualID(String visualID) {
		this.visualID = visualID;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getPlu() {
		return plu;
	}

	public void setPlu(String plu) {
		this.plu = plu;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemUserCode() {
		return itemUserCode;
	}

	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getOrderLineID() {
		return orderLineID;
	}

	public void setOrderLineID(String orderLineID) {
		this.orderLineID = orderLineID;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public GregorianCalendar getDateSold() {
		return dateSold;
	}

	public void setDateSold(GregorianCalendar dateSold) {
		this.dateSold = dateSold;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public BigDecimal getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(BigDecimal netPrice) {
		this.netPrice = netPrice;
	}

	public BigDecimal getExternalPrice() {
		return externalPrice;
	}

	public void setExternalPrice(BigDecimal externalPrice) {
		this.externalPrice = externalPrice;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

}
