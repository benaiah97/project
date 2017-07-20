package pvt.disney.dti.gateway.provider.dlr.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

public class GuestProductTO {

	/** The product code. */
	private String pdtCode;

	/** The TKT_NAME as tktSysId. */
	private String tktSysId;

	/** The database product ID. */
	private BigInteger pdtid;

	/** The Eligibility group ID. */
	private BigInteger eligGrpid;

	/** The product description. */
	private String pdtDesc;

	/** Tax value */
	private BigDecimal tax;

	/** Tax value 1. */
	private BigDecimal tax1;

	/** Tax value 2. */
	private BigDecimal tax2;

	/** Printed price of the product. */
	private BigDecimal printedPrice;

	/** Printed price of the product. */
	private BigDecimal unitPrice;

	/** If the product is sold out or not. */
	private boolean soldOut;

	/** The scalar value permitted in the mismatch. */
	private BigDecimal misMatchTol;

	/** The start sale date of the product. */
	private GregorianCalendar startSaleDate;

	/** The end sale date of the product. */
	private GregorianCalendar endSaleDate;

	/** The start validity date of the product. */
	private GregorianCalendar startValidDate;

	/** The end validity date of the product. */
	private GregorianCalendar endValidDate;

	/** Standard retail price of the product. */
	private BigDecimal standardRetailPrice;

	/** Day Class - class of the product (as of 2.12) */
	private String dayClass;

	/** Day Subclass - subclass of the product (as of 2.16.1, JTL) */
	private String daySubclass;

	/** The activeInd. */
	private String activeInd;

	/** The misMatchInd. */
	private String misMatchInd;

	/** The misMatchTolType. */
	private String misMatchTolType;

	/** The validitydatesInd. */
	private String validitydatesInd;

	/** The consumableInd. */
	private String consumableInd;

	/** The demographicInd. */
	private String demographicInd;

	/**
	 * @return activeInd
	 */
	public String getActiveInd() {
		return activeInd;
	}

	/**
	 * @param activeInd
	 */
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	/**
	 * @return misMatchInd
	 */
	public String getMisMatchInd() {
		return misMatchInd;
	}

	/**
	 * @param misMatchInd
	 */
	public void setMisMatchInd(String misMatchInd) {
		this.misMatchInd = misMatchInd;
	}

	/**
	 * @return misMatchTolType
	 */
	public String getMisMatchTolType() {
		return misMatchTolType;
	}

	/**
	 * @param misMatchTolType
	 */
	public void setMisMatchTolType(String misMatchTolType) {
		this.misMatchTolType = misMatchTolType;
	}

	/**
	 * @return validitydatesInd
	 */
	public String getValiditydatesInd() {
		return validitydatesInd;
	}

	/**
	 * @param validitydatesInd
	 */
	public void setValiditydatesInd(String validitydatesInd) {
		this.validitydatesInd = validitydatesInd;
	}

	/**
	 * @return consumableInd
	 */
	public String getConsumableInd() {
		return consumableInd;
	}

	/**
	 * @param consumableInd
	 */
	public void setConsumableInd(String consumableInd) {
		this.consumableInd = consumableInd;
	}

	/**
	 * @return demographicInd
	 */
	public String getDemographicInd() {
		return demographicInd;
	}

	/**
	 * @param demographicInd
	 */
	public void setDemographicInd(String demographicInd) {
		this.demographicInd = demographicInd;
	}

	/**
	 * @return pdtCode
	 */
	public String getPdtCode() {
		return pdtCode;
	}

	/**
	 * @return tktSysId
	 */
	public String getTktSysId() {
		return tktSysId;
	}

	/**
	 * @param tktSysId
	 */
	public void setTktSysId(String tktSysId) {
		this.tktSysId = tktSysId;
	}

	/**
	 * @param pdtCode
	 */
	public void setPdtCode(String pdtCode) {
		this.pdtCode = pdtCode;
	}

	/**
	 * @return pdtid
	 */
	public BigInteger getPdtid() {
		return pdtid;
	}

	/**
	 * @param pdtid
	 */
	public void setPdtid(BigInteger pdtid) {
		this.pdtid = pdtid;
	}

	/**
	 * @return eligGrpid
	 */
	public BigInteger getEligGrpid() {
		return eligGrpid;
	}

	/**
	 * @param eligGrpid
	 */
	public void setEligGrpid(BigInteger eligGrpid) {
		this.eligGrpid = eligGrpid;
	}

	/**
	 * @return pdtDesc
	 */
	public String getPdtDesc() {
		return pdtDesc;
	}

	/**
	 * @param pdtDesc
	 */
	public void setPdtDesc(String pdtDesc) {
		this.pdtDesc = pdtDesc;
	}

	/**
	 * @return tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @param tax
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * @return tax1
	 */
	public BigDecimal getTax1() {
		return tax1;
	}

	/**
	 * @param tax1
	 */
	public void setTax1(BigDecimal tax1) {
		this.tax1 = tax1;
	}

	/**
	 * @return tax2
	 */
	public BigDecimal getTax2() {
		return tax2;
	}

	/**
	 * @param tax2
	 */
	public void setTax2(BigDecimal tax2) {
		this.tax2 = tax2;
	}

	/**
	 * @return printedPrice
	 */
	public BigDecimal getPrintedPrice() {
		return printedPrice;
	}

	/**
	 * @param printedPrice
	 */
	public void setPrintedPrice(BigDecimal printedPrice) {
		this.printedPrice = printedPrice;
	}

	/**
	 * @return unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return soldOut
	 */
	public boolean isSoldOut() {
		return soldOut;
	}

	/**
	 * @param soldOut
	 */
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	/**
	 * @return misMatchTol
	 */
	public BigDecimal getMisMatchTol() {
		return misMatchTol;
	}

	/**
	 * @param misMatchTol
	 */
	public void setMisMatchTol(BigDecimal misMatchTol) {
		this.misMatchTol = misMatchTol;
	}

	/**
	 * @return startSaleDate
	 */
	public GregorianCalendar getStartSaleDate() {
		return startSaleDate;
	}

	/**
	 * @param startSaleDate
	 */
	public void setStartSaleDate(GregorianCalendar startSaleDate) {
		this.startSaleDate = startSaleDate;
	}

	/**
	 * @return endSaleDate
	 */
	public GregorianCalendar getEndSaleDate() {
		return endSaleDate;
	}

	/**
	 * @param endSaleDate
	 */
	public void setEndSaleDate(GregorianCalendar endSaleDate) {
		this.endSaleDate = endSaleDate;
	}

	/**
	 * @return startValidDate
	 */
	public GregorianCalendar getStartValidDate() {
		return startValidDate;
	}

	/**
	 * @param startValidDate
	 */
	public void setStartValidDate(GregorianCalendar startValidDate) {
		this.startValidDate = startValidDate;
	}

	/**
	 * @return endValidDate
	 */
	public GregorianCalendar getEndValidDate() {
		return endValidDate;
	}

	/**
	 * @param endValidDate
	 */
	public void setEndValidDate(GregorianCalendar endValidDate) {
		this.endValidDate = endValidDate;
	}

	/**
	 * @return standardRetailPrice
	 */
	public BigDecimal getStandardRetailPrice() {
		return standardRetailPrice;
	}

	/**
	 * @param standardRetailPrice
	 */
	public void setStandardRetailPrice(BigDecimal standardRetailPrice) {
		this.standardRetailPrice = standardRetailPrice;
	}

	/**
	 * @return dayClass
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * @param dayClass
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	/**
	 * @return daySubclass
	 */
	public String getDaySubclass() {
		return daySubclass;
	}

	/**
	 * @param daySubclass
	 */
	public void setDaySubclass(String daySubclass) {
		this.daySubclass = daySubclass;
	}

	@Override
	public String toString() {
		return "GuestProductTO [pdtCode=" + pdtCode + ", tktSysId=" + tktSysId
				+ ", pdtid=" + pdtid + ", eligGrpid=" + eligGrpid
				+ ", pdtDesc=" + pdtDesc + ", tax=" + tax + ", tax1=" + tax1
				+ ", tax2=" + tax2 + ", printedPrice=" + printedPrice
				+ ", unitPrice=" + unitPrice + ", soldOut=" + soldOut
				+ ", misMatchTol=" + misMatchTol + ", startSaleDate="
				+ startSaleDate + ", endSaleDate=" + endSaleDate
				+ ", startValidDate=" + startValidDate + ", endValidDate="
				+ endValidDate + ", standardRetailPrice=" + standardRetailPrice
				+ ", dayClass=" + dayClass + ", daySubclass=" + daySubclass
				+ ", activeInd=" + activeInd + ", misMatchInd=" + misMatchInd
				+ ", misMatchTolType=" + misMatchTolType
				+ ", validitydatesInd=" + validitydatesInd + ", consumableInd="
				+ consumableInd + ", demographicInd=" + demographicInd + "]";
	}
	
	

}