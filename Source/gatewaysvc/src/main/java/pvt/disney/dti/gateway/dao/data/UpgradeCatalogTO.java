package pvt.disney.dti.gateway.dao.data;

import java.math.BigDecimal;

public class UpgradeCatalogTO {
	
	/** The product code. */
	private String pdtCode;
	
	/** Printed price of the product. */
	private BigDecimal unitPrice;
	
	/**  Tax value. */
	private BigDecimal tax;
	
	/** Day Class - class of the product (as of 2.12) */
	private String dayClass;

	/** Day Subclass - subclass of the product (as of 2.16.1, JTL) */
	private String daySubclass;
	
	/** The dayCount. */
	private String dayCount;
	
	/** The plu. */
	private String plu;

	/**
	 * @return the pdtCode
	 */
	public String getPdtCode() {
		return pdtCode;
	}

	/**
	 * @param pdtCode the pdtCode to set
	 */
	public void setPdtCode(String pdtCode) {
		this.pdtCode = pdtCode;
	}

	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * @return the dayClass
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * @param dayClass the dayClass to set
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	/**
	 * @return the daySubclass
	 */
	public String getDaySubclass() {
		return daySubclass;
	}

	/**
	 * @param daySubclass the daySubclass to set
	 */
	public void setDaySubclass(String daySubclass) {
		this.daySubclass = daySubclass;
	}

	/**
	 * @return the dayCount
	 */
	public String getDayCount() {
		return dayCount;
	}

	/**
	 * @param dayCount the dayCount to set
	 */
	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}

	/**
	 * @return the plu
	 */
	public String getPlu() {
		return plu;
	}

	/**
	 * @param plu the plu to set
	 */
	public void setPlu(String plu) {
		this.plu = plu;
	}
	
	

}
