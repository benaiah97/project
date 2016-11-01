package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class represents a product in the DTI.
 * 
 * @author lewit019
 * 
 */
public class ProductTO implements Serializable {

	/** Serial Version UID */
	public final static long serialVersionUID = 9129231995L;

	/** The product item. */
	private BigInteger prodItem;

	/** The product code. */
	private String prodCode;

	/** The product quantity. */
	private BigInteger prodQty;

	/** The product parts. */
	private String prodParts;

	/** The product price. */
	private BigDecimal prodPrice;

	/** The product tax (1). */
	private BigDecimal prodTax1;

	/** The product tax (2). */
	private BigDecimal prodTax2;

	/** The product description. */
	private String prodDescription;

	/** The product receipt message. */
	private String prodReceiptMsg;

	/**
	 * @return the prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * @return the prodDescription
	 */
	public String getProdDescription() {
		return prodDescription;
	}

	/**
	 * @return the prodItem
	 */
	public BigInteger getProdItem() {
		return prodItem;
	}

	/**
	 * @return the prodParts
	 */
	public String getProdParts() {
		return prodParts;
	}

	/**
	 * @return the prodPrice
	 */
	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	/**
	 * @return the prodQty
	 */
	public BigInteger getProdQty() {
		return prodQty;
	}

	/**
	 * @return the prodReceiptMsg
	 */
	public String getProdReceiptMsg() {
		return prodReceiptMsg;
	}

	/**
	 * @return the prodTax1
	 */
	public BigDecimal getProdTax1() {
		return prodTax1;
	}

	/**
	 * @return the prodTax2
	 */
	public BigDecimal getProdTax2() {
		return prodTax2;
	}

	/**
	 * @param prodCode
	 *            the prodCode to set
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * @param prodDescription
	 *            the prodDescription to set
	 */
	public void setProdDescription(String prodDescription) {
		this.prodDescription = prodDescription;
	}

	/**
	 * @param prodItem
	 *            the prodItem to set
	 */
	public void setProdItem(BigInteger prodItem) {
		this.prodItem = prodItem;
	}

	/**
	 * @param prodParts
	 *            the prodParts to set
	 */
	public void setProdParts(String prodParts) {
		this.prodParts = prodParts;
	}

	/**
	 * @param prodPrice
	 *            the prodPrice to set
	 */
	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	/**
	 * @param prodQty
	 *            the prodQty to set
	 */
	public void setProdQty(BigInteger prodQty) {
		this.prodQty = prodQty;
	}

	/**
	 * @param prodReceiptMsg
	 *            the prodReceiptMsg to set
	 */
	public void setProdReceiptMsg(String prodReceiptMsg) {
		this.prodReceiptMsg = prodReceiptMsg;
	}

	/**
	 * @param prodTax1
	 *            the prodTax1 to set
	 */
	public void setProdTax1(BigDecimal prodTax1) {
		this.prodTax1 = prodTax1;
	}

	/**
	 * @param prodTax2
	 *            the prodTax2 to set
	 */
	public void setProdTax2(BigDecimal prodTax2) {
		this.prodTax2 = prodTax2;
	}

}
