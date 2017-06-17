package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

/**
 * This class represents the database version of the ticket products.
 * 
 * @author lewit019
 * 
 */
public class DBProductTO implements Serializable {

	/** Serial Version UID */
	public final static long serialVersionUID = 9129231995L;

	/** The enumeration of possible mismatch types in DTI. */
	public enum MismatchToleranceType {
		AMOUNT,
		PERCENT,
		UNDEFINED
	};

	/** The product code. */
	private String pdtCode;

	/** The database product ID. */
	private BigInteger pdtid;

	/** The product description. */
	private String pdtDesc;

	/** The Eligibility group ID. */
	private BigInteger eligGrpid;

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

	/** If the product is active or not. */
	private boolean active;

	/** If the product is sold out or not. */
	private boolean soldOut;

	/** If the product allows for price mismatch to occur. */
	private boolean priceMismatchAllowed;

	/** The tolerance type of mismatch configured. */
	private MismatchToleranceType misMatchTolType = MismatchToleranceType.UNDEFINED;

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

	/** If validity date info is required on this product. */
	private boolean validityDateInfoRequired;

	/** The provider ticket number mapped to this product. */
	private BigInteger mappedProviderTktNbr;

	/** The provider ticket name mapped to this product. */
	private String mappedProviderTktName;

	/** If the mapped provider ticket is active. */
	private boolean mappedProviderTktActive = false;

	/** Quantity of this product on the order. */
	private BigInteger quantity;

	/** If product is consumable */
	private boolean consumableInd;

	/** If demographics are required */
	private boolean demographicsInd;

	/** Day Class - class of the product (as of 2.12) */
	private String dayClass;

	/** Day Subclass - subclass of the product (as of 2.16.1, JTL) */
	private String daySubclass;

	/** Standard retail price of the product. */
	private BigDecimal standardRetailPrice;

	/**
	 * 
	 * @return
	 */
	public BigInteger getQuantity() {
		return quantity;
	}

	/**
	 * 
	 * @param quantity
	 */
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return the eligGrpid
	 */
	public BigInteger getEligGrpid() {
		return eligGrpid;
	}

	/**
	 * @return the endSaleDate
	 */
	public GregorianCalendar getEndSaleDate() {
		return endSaleDate;
	}

	/**
	 * @return the priceMismatchAllowed
	 */
	public boolean isPriceMismatchAllowed() {
		return priceMismatchAllowed;
	}

	/**
	 * @return the misMatchTol
	 */
	public BigDecimal getMisMatchTol() {
		return misMatchTol;
	}

	/**
	 * @return the misMatchTolType
	 */
	public MismatchToleranceType getMisMatchTolType() {
		return misMatchTolType;
	}

	/**
	 * @return the pdtCode
	 */
	public String getPdtCode() {
		return pdtCode;
	}

	/**
	 * @return the pdtid
	 */
	public BigInteger getPdtid() {
		return pdtid;
	}

	/**
	 * @return the printedPrice
	 */
	public BigDecimal getPrintedPrice() {
		return printedPrice;
	}

	/**
	 * @return the soldOut
	 */
	public boolean isSoldOut() {
		return soldOut;
	}

	/**
	 * @return the startSaleDate
	 */
	public GregorianCalendar getStartSaleDate() {
		return startSaleDate;
	}

	/**
	 * @return the tax1
	 */
	public BigDecimal getTax1() {
		return tax1;
	}

	/**
	 * @return the tax2
	 */
	public BigDecimal getTax2() {
		return tax2;
	}

	/**
	 * @return the validityDateInfoRequired
	 */
	public boolean isValidityDateInfoRequired() {
		return validityDateInfoRequired;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean activeInd) {
		this.active = activeInd;
	}

	/**
	 * @param eligGrpid
	 *            the eligGrpid to set
	 */
	public void setEligGrpid(BigInteger eligGrpid) {
		this.eligGrpid = eligGrpid;
	}

	/**
	 * @param endSaleDate
	 *            the endSaleDate to set
	 */
	public void setEndSaleDate(GregorianCalendar endSaleDate) {
		this.endSaleDate = endSaleDate;
	}

	/**
	 * @param priceMismatchAllowed
	 *            the priceMismatchAllowed to set
	 */
	public void setPriceMismatchAllowed(boolean mismatchInd) {
		System.out.println(pdtCode+ " **********************MISMATCH ALLOWED:"+ mismatchInd);
		this.priceMismatchAllowed = mismatchInd;
	}

	/**
	 * @param misMatchTol
	 *            the misMatchTol to set
	 */
	public void setMisMatchTol(BigDecimal misMatchTol) {
		System.out.println(pdtCode+ " **********************MISMATCH TOLERANCE:"+ misMatchTol);
		this.misMatchTol = misMatchTol;
	}

	/**
	 * @param misMatchTolType
	 *            the misMatchTolType to set
	 */
	public void setMisMatchTolType(MismatchToleranceType misMatchTolType) {
		System.out.println(pdtCode+ " **********************MISMATCH TOLERANCE Type:"+ misMatchTolType);
		this.misMatchTolType = misMatchTolType;
	}

	/**
	 * @param pdtCode
	 *            the pdtCode to set
	 */
	public void setPdtCode(String pdtCode) {
		this.pdtCode = pdtCode;
	}

	/**
	 * @param pdtid
	 *            the pdtid to set
	 */
	public void setPdtid(BigInteger pdtid) {
		this.pdtid = pdtid;
	}

	/**
	 * @param printedPrice
	 *            the printedPrice to set
	 */
	public void setPrintedPrice(BigDecimal printedPrice) {
		this.printedPrice = printedPrice;
	}

	/**
	 * @param soldOut
	 *            the soldOut to set
	 */
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	/**
	 * @param startSaleDate
	 *            the startSaleDate to set
	 */
	public void setStartSaleDate(GregorianCalendar startSaleDate) {
		this.startSaleDate = startSaleDate;
	}

	/**
	 * @param tax1
	 *            the tax1 to set
	 */
	public void setTax1(BigDecimal tax1) {
		this.tax1 = tax1;
	}

	/**
	 * @param tax2
	 *            the tax2 to set
	 */
	public void setTax2(BigDecimal tax2) {
		this.tax2 = tax2;
	}

	/**
	 * @param validityDateInfoRequired
	 *            the validityDateInfoRequired to set
	 */
	public void setValidityDateInfoRequired(boolean validityDatesInd) {
		this.validityDateInfoRequired = validityDatesInd;
	}

	/**
	 * @return the pdtDesc
	 */
	public String getPdtDesc() {
		return pdtDesc;
	}

	/**
	 * @param pdtDesc
	 *            the pdtDesc to set
	 */
	public void setPdtDesc(String pdtDesc) {
		this.pdtDesc = pdtDesc;
	}

	/**
	 * @return the mappedProviderTktActive
	 */
	public boolean isMappedProviderTktActive() {
		return mappedProviderTktActive;
	}

	/**
	 * @return the mappedProviderTktName
	 */
	public String getMappedProviderTktName() {
		return mappedProviderTktName;
	}

	/**
	 * @return the mappedProviderTktNbr
	 */
	public BigInteger getMappedProviderTktNbr() {
		return mappedProviderTktNbr;
	}

	/**
	 * @param mappedProviderTktActive
	 *            the mappedProviderTktActive to set
	 */
	public void setMappedProviderTktActive(boolean mappedProviderTktActive) {
		this.mappedProviderTktActive = mappedProviderTktActive;
	}

	/**
	 * @param mappedProviderTktName
	 *            the mappedProviderTktName to set
	 */
	public void setMappedProviderTktName(String mappedProviderTktName) {
		this.mappedProviderTktName = mappedProviderTktName;
	}

	/**
	 * @param mappedProviderTktNbr
	 *            the mappedProviderTktNbr to set
	 */
	public void setMappedProviderTktNbr(BigInteger mappedProviderTktNbr) {
		this.mappedProviderTktNbr = mappedProviderTktNbr;
	}

	/**
	 * @return the tax
	 */
	public BigDecimal getTax() {
		return tax;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * 
	 * @return the unit price
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * Sets the unit price.
	 * 
	 * @param unitPrice
	 *            the unit price to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * 
	 * @return the start valid date
	 */
	public GregorianCalendar getStartValidDate() {
		return startValidDate;
	}

	/**
	 * Sets the start valid date
	 * 
	 * @param startValidDate
	 *            the start valid date to set.
	 */
	public void setStartValidDate(GregorianCalendar startValidDate) {
		this.startValidDate = startValidDate;
	}

	/**
	 * 
	 * @return the end valid date
	 */
	public GregorianCalendar getEndValidDate() {
		return endValidDate;
	}

	/**
	 * Sets the end valid date.
	 * 
	 * @param endValidDate
	 *            the end valid date to set.
	 */
	public void setEndValidDate(GregorianCalendar endValidDate) {
		this.endValidDate = endValidDate;
	}

	/**
	 * @param consumable
	 *            the consumable to set
	 */
	public void setConsumable(boolean consumable) {
		this.consumableInd = consumable;
	}

	/**
	 * @return the consumable
	 */
	public boolean isConsumable() {
		return consumableInd;
	}

	/**
	 * @return the demographicsInd
	 */
	public boolean areDemographicsRequired() {
		return demographicsInd;
	}

	/**
	 * @param demographicsInd
	 *            the demographicsInd to set
	 */
	public void setDemographicsInd(boolean demographicsInd) {
		this.demographicsInd = demographicsInd;
	}

	/**
	 * @return the dayClass
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * @param dayClass
	 *            the dayClass to set
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	/**
	 * @return the standardRetailPrice
	 */
	public BigDecimal getStandardRetailPrice() {
		return standardRetailPrice;
	}

	/**
	 * @param standardRetailPrice
	 *            the standardRetailPrice to set
	 */
	public void setStandardRetailPrice(BigDecimal standardRetailPrice) {
		this.standardRetailPrice = standardRetailPrice;
	}

	/**
	 * @return the daySubclass
	 */
	public String getDaySubclass() {
		return daySubclass;
	}

	/**
	 * @param daySubclass
	 *            the daySubclass to set
	 */
	public void setDaySubclass(String daySubclass) {
		this.daySubclass = daySubclass;
	}

}
