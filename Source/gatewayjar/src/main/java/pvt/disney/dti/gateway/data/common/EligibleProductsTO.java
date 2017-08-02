package pvt.disney.dti.gateway.data.common;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

/***
 * Entity class to store the queried eligible products information 
 * 
 * @author GANDV005
 *
 */
public class EligibleProductsTO {

	/** product code */
	protected Object prodCode;

	/** product price */
	protected String prodPrice;

	/** actual product tax */
	protected BigDecimal prodTax;

	/** upgraded price */
	protected String upgrdPrice;

	/** upgraded tax */
	protected String upgrdTax;

	/** validity */
	protected XMLGregorianCalendar validEnd;

	/**
	 * Gets the value of the prodCode property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public Object getProdCode() {
		return prodCode;
	}

	/**
	 * Sets the value of the prodCode property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setProdCode(Object value) {
		this.prodCode = value;
	}

	/**
	 * Gets the value of the prodPrice property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getProdPrice() {
		return prodPrice;
	}

	/**
	 * Sets the value of the prodPrice property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setProdPrice(String value) {
		this.prodPrice = value;
	}

	/**
	 * Gets the value of the prodTax property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getProdTax() {
		return prodTax;
	}

	/**
	 * Sets the value of the prodTax property.
	 * 
	 * @param value
	 *            allowed object is {@link BigDecimal }
	 * 
	 */
	public void setProdTax(BigDecimal value) {
		this.prodTax = value;
	}

	/**
	 * Gets the value of the upgrdPrice property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUpgrdPrice() {
		return upgrdPrice;
	}

	/**
	 * Sets the value of the upgrdPrice property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUpgrdPrice(String value) {
		this.upgrdPrice = value;
	}

	/**
	 * Gets the value of the upgrdTax property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUpgrdTax() {
		return upgrdTax;
	}

	/**
	 * Sets the value of the upgrdTax property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUpgrdTax(String value) {
		this.upgrdTax = value;
	}

	/**
	 * Gets the value of the validEnd property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getValidEnd() {
		return validEnd;
	}

	/**
	 * Sets the value of the validEnd property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setValidEnd(XMLGregorianCalendar value) {
		this.validEnd = value;
	}

}