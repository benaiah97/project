package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * This class represents the database version of the ticket products.
 * 
 * @author lewit019
 * 
 */
public class DBProductTO implements Serializable {

	/**  Serial Version UID. */
	public final static long serialVersionUID = 9129231995L;

	/** The enumeration of possible mismatch types in DTI. */
	public enum MismatchToleranceType {
	
		/** The amount. */
		AMOUNT,
		
		/** The percent. */
		PERCENT,
		
		/** The undefined. */
		UNDEFINED
	};
	
	/** The enumeration of guest types in DTI. */
	public enum GuestType {
		ADULT,
		CHILD,
		ANY,
		UNKNOWN
	};

	/** The product code. */
	private String pdtCode;

	/** The database product ID. */
	private BigInteger pdtid;

	/** The product description. */
	private String pdtDesc;

	/** The Eligibility group ID. */
	private BigInteger eligGrpid;

	/**  Tax value. */
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
	
	/** The tolerance type of mismatch configured. */
	private GuestType guestType = GuestType.UNKNOWN;

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

	/**  If product is consumable. */
	private boolean consumableInd;

	/**  If demographics are required. */
	private boolean demographicsInd;

	/** Day Class - class of the product (as of 2.12) */
	private String dayClass;

	/** Day Subclass - subclass of the product (as of 2.16.1, JTL) */
	private String daySubclass;

	/** Standard retail price of the product. */
	private BigDecimal standardRetailPrice;
	
	/**   ResidentInd. */
	private boolean residentInd;

	/** Standard retail tax of the product. */
	private BigDecimal standardRetailTax;
	
	/** The upgrd path id. */
	private BigInteger upgrdPathId;
	
	/** The dayCount. */
	private String dayCount;
	
	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public BigInteger getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(BigInteger quantity) {
		this.quantity = quantity;
	}

	/**
	 * Checks if is active.
	 *
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Gets the elig grpid.
	 *
	 * @return the eligGrpid
	 */
	public BigInteger getEligGrpid() {
		return eligGrpid;
	}

	/**
	 * Gets the end sale date.
	 *
	 * @return the endSaleDate
	 */
	public GregorianCalendar getEndSaleDate() {
		return endSaleDate;
	}

	/**
	 * Checks if is price mismatch allowed.
	 *
	 * @return the priceMismatchAllowed
	 */
	public boolean isPriceMismatchAllowed() {
		return priceMismatchAllowed;
	}

	/**
	 * Gets the mis match tol.
	 *
	 * @return the misMatchTol
	 */
	public BigDecimal getMisMatchTol() {
		return misMatchTol;
	}

	/**
	 * Gets the mis match tol type.
	 *
	 * @return the misMatchTolType
	 */
	public MismatchToleranceType getMisMatchTolType() {
		return misMatchTolType;
	}

	/**
	 * Gets the pdt code.
	 *
	 * @return the pdtCode
	 */
	public String getPdtCode() {
		return pdtCode;
	}

	/**
	 * Gets the pdtid.
	 *
	 * @return the pdtid
	 */
	public BigInteger getPdtid() {
		return pdtid;
	}

	/**
	 * Gets the printed price.
	 *
	 * @return the printedPrice
	 */
	public BigDecimal getPrintedPrice() {
		return printedPrice;
	}

	/**
	 * Checks if is sold out.
	 *
	 * @return the soldOut
	 */
	public boolean isSoldOut() {
		return soldOut;
	}

	/**
	 * Gets the start sale date.
	 *
	 * @return the startSaleDate
	 */
	public GregorianCalendar getStartSaleDate() {
		return startSaleDate;
	}

	/**
	 * Gets the tax 1.
	 *
	 * @return the tax1
	 */
	public BigDecimal getTax1() {
		return tax1;
	}

	/**
	 * Gets the tax 2.
	 *
	 * @return the tax2
	 */
	public BigDecimal getTax2() {
		return tax2;
	}

	/**
	 * Checks if is validity date info required.
	 *
	 * @return the validityDateInfoRequired
	 */
	public boolean isValidityDateInfoRequired() {
		return validityDateInfoRequired;
	}

	/**
	 * Sets the active.
	 *
	 * @param activeInd the new active
	 */
	public void setActive(boolean activeInd) {
		this.active = activeInd;
	}

	/**
	 * Sets the elig grpid.
	 *
	 * @param eligGrpid            the eligGrpid to set
	 */
	public void setEligGrpid(BigInteger eligGrpid) {
		this.eligGrpid = eligGrpid;
	}

	/**
	 * Sets the end sale date.
	 *
	 * @param endSaleDate            the endSaleDate to set
	 */
	public void setEndSaleDate(GregorianCalendar endSaleDate) {
		this.endSaleDate = endSaleDate;
	}

	/**
	 * Sets the price mismatch allowed.
	 *
	 * @param mismatchInd the new price mismatch allowed
	 */
	public void setPriceMismatchAllowed(boolean mismatchInd) {
		this.priceMismatchAllowed = mismatchInd;
	}

	/**
	 * Sets the mis match tol.
	 *
	 * @param misMatchTol            the misMatchTol to set
	 */
	public void setMisMatchTol(BigDecimal misMatchTol) {
		this.misMatchTol = misMatchTol;
	}

	/**
	 * Sets the mis match tol type.
	 *
	 * @param misMatchTolType            the misMatchTolType to set
	 */
	public void setMisMatchTolType(MismatchToleranceType misMatchTolType) {
		this.misMatchTolType = misMatchTolType;
	}

	/**
	 * Sets the pdt code.
	 *
	 * @param pdtCode            the pdtCode to set
	 */
	public void setPdtCode(String pdtCode) {
		this.pdtCode = pdtCode;
	}

	/**
	 * Sets the pdtid.
	 *
	 * @param pdtid            the pdtid to set
	 */
	public void setPdtid(BigInteger pdtid) {
		this.pdtid = pdtid;
	}

	/**
	 * Sets the printed price.
	 *
	 * @param printedPrice            the printedPrice to set
	 */
	public void setPrintedPrice(BigDecimal printedPrice) {
		this.printedPrice = printedPrice;
	}

	/**
	 * Sets the sold out.
	 *
	 * @param soldOut            the soldOut to set
	 */
	public void setSoldOut(boolean soldOut) {
		this.soldOut = soldOut;
	}

	/**
	 * Sets the start sale date.
	 *
	 * @param startSaleDate            the startSaleDate to set
	 */
	public void setStartSaleDate(GregorianCalendar startSaleDate) {
		this.startSaleDate = startSaleDate;
	}

	/**
	 * Sets the tax 1.
	 *
	 * @param tax1            the tax1 to set
	 */
	public void setTax1(BigDecimal tax1) {
		this.tax1 = tax1;
	}

	/**
	 * Sets the tax 2.
	 *
	 * @param tax2            the tax2 to set
	 */
	public void setTax2(BigDecimal tax2) {
		this.tax2 = tax2;
	}

	/**
	 * Sets the validity date info required.
	 *
	 * @param validityDatesInd the new validity date info required
	 */
	public void setValidityDateInfoRequired(boolean validityDatesInd) {
		this.validityDateInfoRequired = validityDatesInd;
	}

	/**
	 * Gets the pdt desc.
	 *
	 * @return the pdtDesc
	 */
	public String getPdtDesc() {
		return pdtDesc;
	}

	/**
	 * Sets the pdt desc.
	 *
	 * @param pdtDesc            the pdtDesc to set
	 */
	public void setPdtDesc(String pdtDesc) {
		this.pdtDesc = pdtDesc;
	}

	/**
	 * Checks if is mapped provider tkt active.
	 *
	 * @return the mappedProviderTktActive
	 */
	public boolean isMappedProviderTktActive() {
		return mappedProviderTktActive;
	}

	/**
	 * Gets the mapped provider tkt name.
	 *
	 * @return the mappedProviderTktName
	 */
	public String getMappedProviderTktName() {
		return mappedProviderTktName;
	}

	/**
	 * Gets the mapped provider tkt nbr.
	 *
	 * @return the mappedProviderTktNbr
	 */
	public BigInteger getMappedProviderTktNbr() {
		return mappedProviderTktNbr;
	}

	/**
	 * Sets the mapped provider tkt active.
	 *
	 * @param mappedProviderTktActive            the mappedProviderTktActive to set
	 */
	public void setMappedProviderTktActive(boolean mappedProviderTktActive) {
		this.mappedProviderTktActive = mappedProviderTktActive;
	}

	/**
	 * Sets the mapped provider tkt name.
	 *
	 * @param mappedProviderTktName            the mappedProviderTktName to set
	 */
	public void setMappedProviderTktName(String mappedProviderTktName) {
		this.mappedProviderTktName = mappedProviderTktName;
	}

	/**
	 * Sets the mapped provider tkt nbr.
	 *
	 * @param mappedProviderTktNbr            the mappedProviderTktNbr to set
	 */
	public void setMappedProviderTktNbr(BigInteger mappedProviderTktNbr) {
		this.mappedProviderTktNbr = mappedProviderTktNbr;
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
	 * Sets the tax.
	 *
	 * @param tax            the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * Gets the unit price.
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
	 * Gets the start valid date.
	 *
	 * @return the start valid date
	 */
	public GregorianCalendar getStartValidDate() {
		return startValidDate;
	}

	/**
	 * Sets the start valid date.
	 *
	 * @param startValidDate            the start valid date to set.
	 */
	public void setStartValidDate(GregorianCalendar startValidDate) {
		this.startValidDate = startValidDate;
	}

	/**
	 * Gets the end valid date.
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
	 * Sets the consumable.
	 *
	 * @param consumable            the consumable to set
	 */
	public void setConsumable(boolean consumable) {
		this.consumableInd = consumable;
	}

	/**
	 * Checks if is consumable.
	 *
	 * @return the consumable
	 */
	public boolean isConsumable() {
		return consumableInd;
	}

	/**
	 * Are demographics required.
	 *
	 * @return the demographicsInd
	 */
	public boolean areDemographicsRequired() {
		return demographicsInd;
	}

	/**
	 * Sets the demographics ind.
	 *
	 * @param demographicsInd            the demographicsInd to set
	 */
	public void setDemographicsInd(boolean demographicsInd) {
		this.demographicsInd = demographicsInd;
	}

	/**
	 * Gets the day class.
	 *
	 * @return the dayClass
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * Sets the day class.
	 *
	 * @param dayClass            the dayClass to set
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	/**
	 * Gets the standard retail price.
	 *
	 * @return the standardRetailPrice
	 */
	public BigDecimal getStandardRetailPrice() {
		return standardRetailPrice;
	}

	/**
	 * Sets the standard retail price.
	 *
	 * @param standardRetailPrice            the standardRetailPrice to set
	 */
	public void setStandardRetailPrice(BigDecimal standardRetailPrice) {
		this.standardRetailPrice = standardRetailPrice;
	}

	/**
	 * Gets the day subclass.
	 *
	 * @return the daySubclass
	 */
	public String getDaySubclass() {
		return daySubclass;
	}

	/**
	 * Sets the day subclass.
	 *
	 * @param daySubclass            the daySubclass to set
	 */
	public void setDaySubclass(String daySubclass) {
		this.daySubclass = daySubclass;
	}

	/**
	 * Checks if is resident ind.
	 *
	 * @return the residentInd
	 */
	public boolean isResidentInd() {
		return residentInd;
	}

	/**
	 * Sets the resident ind.
	 *
	 * @param residentInd the residentInd to set
	 */
	public void setResidentInd(boolean residentInd) {
		this.residentInd = residentInd;
	}

	/**
	 * Gets the standard retail tax.
	 *
	 * @return standardRetailTax
	 */
	public BigDecimal getStandardRetailTax() {
		return standardRetailTax;
	}

	/**
	 * Sets the standard retail tax.
	 *
	 * @param standardRetailTax the new standard retail tax
	 */
	public void setStandardRetailTax(BigDecimal standardRetailTax) {
		this.standardRetailTax = standardRetailTax;
	}

	/**
	 * Gets the upgrd path id.
	 *
	 * @return upgrdPathId
	 */
	public BigInteger getUpgrdPathId() {
		return upgrdPathId;
	}

	/**
	 * Sets the upgrd path id.
	 *
	 * @param upgrdPathId the new upgrd path id
	 */
	public void setUpgrdPathId(BigInteger upgrdPathId) {
		this.upgrdPathId = upgrdPathId;
	}
	/**
	 * @return dayCount
	 */
	public String getDayCount() {
		return dayCount;
	}

	/**
	 * @param dayCount
	 */
	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}
	
	/**
	 * @return the guestType
	 */
	public GuestType getGuestType() {
		return guestType;
	}

	/**
	 * @param guestType the guestType to set
	 */
	public void setGuestType(GuestType guestType) {
		this.guestType = guestType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DBProductTO [pdtCode=" + pdtCode + ", pdtid=" + pdtid
				+ ", pdtDesc=" + pdtDesc + ", eligGrpid=" + eligGrpid
				+ ", tax=" + tax + ", tax1=" + tax1 + ", tax2=" + tax2
				+ ", printedPrice=" + printedPrice + ", unitPrice=" + unitPrice
				+ ", active=" + active + ", soldOut=" + soldOut
				+ ", priceMismatchAllowed=" + priceMismatchAllowed
				+ ", misMatchTolType=" + misMatchTolType + ", misMatchTol="
				+ misMatchTol + ", startSaleDate=" + startSaleDate
				+ ", endSaleDate=" + endSaleDate + ", startValidDate="
				+ startValidDate + ", endValidDate=" + endValidDate
				+ ", validityDateInfoRequired=" + validityDateInfoRequired
				+ ", mappedProviderTktNbr=" + mappedProviderTktNbr
				+ ", mappedProviderTktName=" + mappedProviderTktName
				+ ", mappedProviderTktActive=" + mappedProviderTktActive
				+ ", quantity=" + quantity + ", consumableInd=" + consumableInd
				+ ", demographicsInd=" + demographicsInd + ", dayClass="
				+ dayClass + ", daySubclass=" + daySubclass
				+ ", standardRetailPrice=" + standardRetailPrice
				+ ", residentInd=" + residentInd + ", standardRetailTax="
				+ standardRetailTax + ", upgrdPathId=" + upgrdPathId
				+ ", dayCount=" + dayCount + ", guestType="+ guestType + "]";
	}
	
	
}
