package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents the ticket seller entity in DTI.
 * 
 * @author lewit019
 * 
 */
public class EntityTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** The enumeration of default payment types. */
	public enum DefaultPaymentType {
		UNDEFINED,
		CASH,
		CREDITCARD,
		VOUCHER,
		JOBNUMBER,
		CHECK,
		DISNEYDOLLARS,
		MRCHTKTSLS,
		ORIGINAL,
		INVOICE,
		WODNYTKTSLS
	};

	// Non-nullable fields
	/** Entity ID of the entity in the database. */
	private int entityId;

	/** Entity ID of the Master Agreement Code holder in the database. */
	private int macEntityId;

	/** If the entity is active. */
	private boolean active;

	/** Date when the entity may begin selling. */
	private Date startValidDate;

	/** Date when the entity may no longer sell. */
	private Date endValidDate;

	/** The alphanumeric code which identifies this entity. */
	private String entityCode;

	/** If the entity is allowed to price mismatch. */
	private boolean priceMismatchAllowed;

	/** If the IATA number is required. */
	private boolean iataRequired;

	/** If payment is required for this entity. */
	private boolean pymtRequired;

	/** If the ticket shell (track 2, generally) is required for this entity. */
	private boolean ticketShellRequired;

	/** The ticket seller location of this entity. */
	private String tsLocation;

	/** The ticket seller master agreement code of this entity. */
	private String tsMac;

	// Nullable fields
	/** Time the entity may begin selling. */
	private String startValidTime;

	/** Time the entity may no longer sell. */
	private String endValidTime;

	/** The customer ID of this entity. */
	private String customerId;

	/** The eCommerce value of this entity. */
	private String eCommerceValue;

	/** Default payment data (voucher or holding account) used for this entity. */
	private String defPymtData;

	/** If this entity is allowed to sell tickets with a validity date. */
	private boolean validityDateProductAllowed;

	/** Default IATA for this entity. */
	private String defIata;

	/** The default payment type for this entity. (3 for voucher) */
	private int defPymtId = 0;

	/** The default sales rep ID for this entity. */
	private Long defSalesRepId;

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @return the defIata
	 */
	public String getDefIata() {
		return defIata;
	}

	/**
	 * @return the defPymtData
	 */
	public String getDefPymtData() {
		return defPymtData;
	}

	/**
	 * @return the endValidDate
	 */
	public Date getEndValidDate() {
		return endValidDate;
	}

	/**
	 * @return the endValidTime
	 */
	public String getEndValidTime() {
		return endValidTime;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @return the startValidDate
	 */
	public Date getStartValidDate() {
		return startValidDate;
	}

	/**
	 * @return the startValidTime
	 */
	public String getStartValidTime() {
		return startValidTime;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @param defIata
	 *            the defIata to set
	 */
	public void setDefIata(String defIata) {
		this.defIata = defIata;
	}

	/**
	 * @param defPymtData
	 *            the defPymtData to set
	 */
	public void setDefPymtData(String defPymtData) {
		this.defPymtData = defPymtData;
	}

	/**
	 * @param endValidDate
	 *            the endValidDate to set
	 */
	public void setEndValidDate(Date endValidDate) {
		this.endValidDate = endValidDate;
	}

	/**
	 * @param endValidTime
	 *            the endValidTime to set
	 */
	public void setEndValidTime(String endValidTime) {
		this.endValidTime = endValidTime;
	}

	/**
	 * @param entityCode
	 *            the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @param startValidDate
	 *            the startValidDate to set
	 */
	public void setStartValidDate(Date startValidDate) {
		this.startValidDate = startValidDate;
	}

	/**
	 * @param startValidTime
	 *            the startValidTime to set
	 */
	public void setStartValidTime(String startValidTime) {
		this.startValidTime = startValidTime;
	}

	/**
	 * @return the tsLocation
	 */
	public String getTsLocation() {
		return tsLocation;
	}

	/**
	 * @return the tsMac
	 */
	public String getTsMac() {
		return tsMac;
	}

	/**
	 * @param tsLocation
	 *            the tsLocation to set
	 */
	public void setTsLocation(String tsLocation) {
		this.tsLocation = tsLocation;
	}

	/**
	 * @param tsMac
	 *            the tsMac to set
	 */
	public void setTsMac(String tsMac) {
		this.tsMac = tsMac;
	}

	/**
	 * @return the defPymtId
	 */
	public long getDefPymtId() {
		return defPymtId;
	}

	/**
	 * @return the enumerated version of the default payment type.
	 */
	public DefaultPaymentType getDefPymtIdEnum() {

		DefaultPaymentType defPymtTypeEnum;

		switch (defPymtId) {

		case 1:
			defPymtTypeEnum = DefaultPaymentType.CASH;
			break;

		case 2:
			defPymtTypeEnum = DefaultPaymentType.CREDITCARD;
			break;

		case 3:
			defPymtTypeEnum = DefaultPaymentType.VOUCHER;
			break;

		case 4:
			defPymtTypeEnum = DefaultPaymentType.JOBNUMBER;
			break;

		case 5:
			defPymtTypeEnum = DefaultPaymentType.CHECK;
			break;

		case 6:
			defPymtTypeEnum = DefaultPaymentType.DISNEYDOLLARS;
			break;

		case 7:
			defPymtTypeEnum = DefaultPaymentType.MRCHTKTSLS;
			break;

		case 8:
			defPymtTypeEnum = DefaultPaymentType.ORIGINAL;
			break;

		case 9:
			defPymtTypeEnum = DefaultPaymentType.INVOICE;
			break;

		case 10:
			defPymtTypeEnum = DefaultPaymentType.WODNYTKTSLS;
			break;

		default:
			defPymtTypeEnum = DefaultPaymentType.UNDEFINED;

		}

		return defPymtTypeEnum;
	}

	/**
	 * @return the defSalesRepId
	 */
	public Long getDefSalesRepId() {
		return defSalesRepId;
	}

	/**
	 * @return the entityId
	 */
	public int getEntityId() {
		return entityId;
	}

	/**
	 * @param defPymtId
	 *            the defPymtId to set
	 */
	public void setDefPymtId(int defPymtIdIn) {
		this.defPymtId = defPymtIdIn;
	}

	/**
	 * @param defSalesRepId
	 *            the defSalesRepId to set
	 */
	public void setDefSalesRepId(Long defSalesRepId) {
		this.defSalesRepId = defSalesRepId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @return the ticketShellRequired
	 */
	public boolean isTicketShellRequired() {
		return ticketShellRequired;
	}

	/**
	 * @return the iataRequired
	 */
	public boolean isIataRequired() {
		return iataRequired;
	}

	/**
	 * @return the priceMismatchAllowed
	 */
	public boolean isPriceMismatchAllowed() {
		return priceMismatchAllowed;
	}

	/**
	 * @return the pymtRequired
	 */
	public boolean isPymtRequired() {
		return pymtRequired;
	}

	/**
	 * @return the validityDateProductAllowed
	 */
	public boolean isValidityDateProductAllowed() {
		return validityDateProductAllowed;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @param ticketShellRequired
	 *            the ticketShellRequired to set
	 */
	public void setTicketShellRequired(boolean checkTicketShell) {
		this.ticketShellRequired = checkTicketShell;
	}

	/**
	 * @param iataRequired
	 *            the iataRequired to set
	 */
	public void setIataRequired(boolean iataRequired) {
		this.iataRequired = iataRequired;
	}

	/**
	 * @param priceMismatchAllowed
	 *            the priceMismatchAllowed to set
	 */
	public void setPriceMismatchAllowed(boolean priceMismatchAllowed) {
		this.priceMismatchAllowed = priceMismatchAllowed;
	}

	/**
	 * @param pymtRequired
	 *            the pymtRequired to set
	 */
	public void setPymtRequired(boolean pymtRequired) {
		this.pymtRequired = pymtRequired;
	}

	/**
	 * @param validityDateProductAllowed
	 *            the validityDateProductAllowed to set
	 */
	public void setValidityDateProductAllowed(boolean validityDatesRequired) {
		this.validityDateProductAllowed = validityDatesRequired;
	}

	/**
	 * @return the eCommerceValue
	 */
	public String getECommerceValue() {
		return eCommerceValue;
	}

	/**
	 * @param commerceValue
	 *            the eCommerceValue to set
	 */
	public void setECommerceValue(String commerceValue) {
		eCommerceValue = commerceValue;
	}

	/**
	 * 
	 * @return the entity master agreement code identifier
	 */
	public int getMacEntityId() {
		return macEntityId;
	}

	/**
	 * Sets the entity master agreement code identifier
	 * 
	 * @param macEntityId
	 *            the entity master agreement code identifier to set
	 */
	public void setMacEntityId(int macEntityId) {
		this.macEntityId = macEntityId;
	}

}
