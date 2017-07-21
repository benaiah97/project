package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * This class represents the DataRequestResp portion of an eGalaxy XML.
 * 
 * @author lewit019,smoon
 * 
 */
public class GWDataRequestRespTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  public static enum ItemKind {
    REGULAR_TICKET,
    PASS,
    UNKNOWN
  }

  public static enum Status {
    VALID,
    VOIDED,
    RETURNED,
    REPLACED,
    PURCHASER,
    EXPIRED,
    UPGRADED,
    REPRINTED,
    BLOCKED,
    UNISSUED,
    UNKNOWN
  }

  private Integer itemKind;

  private Boolean returnable;

  private Integer status;

  private String visualID;

  private BigDecimal price;

  private BigDecimal tax;

  private Integer useCount;

  // Start Validity Date
  private GregorianCalendar dateSold;

  private GregorianCalendar ticketDate;

  private GregorianCalendar startDateTime;

  private GregorianCalendar dateOpened;

  // End Validity Date
  private GregorianCalendar expirationDate;

  private GregorianCalendar endDateTime;

  private GregorianCalendar validUntil;

  private Boolean lockedOut;

  private String firstName;

  private String lastName;

  private String street1;

  private String street2;

  private String city;

  private String state;

  private String zip;

  private String countryCode;

  private String phone;

  private String email;

  private GregorianCalendar dateUsed;

  private String passKindName;

  private ArrayList<LineageRecord> lineageArray = new ArrayList<LineageRecord>();
  
  private ArrayList<UpgradePLUList> upgradePLUList = new ArrayList<UpgradePLUList>(); // Added as of 2.17.3, AR

  private String kind; // Added as of 2.11

  private Integer remainingUse; // Added as of 2.14 JTL

  private Integer gender; // As of 2.16.1, JTL - used on request.

  private String genderRespString; // As of 2.16.1, JTL - used on response (Galaxy type violation)

  private GregorianCalendar dateOfBirth; // As of 2.16.1, JTL

  private Boolean renewable; // As of 2.16.1, JTL

  /**
   * 
   * @author lewit019
   * 
   */
  public class LineageRecord {

    private BigDecimal amount;

    private Integer status;

    private String visualID;

    private Boolean valid;

    private GregorianCalendar expirationDate;

    public GregorianCalendar getExpirationDate() {
      return expirationDate;
    }

    public void setExpirationDate(GregorianCalendar expirationDate) {
      this.expirationDate = expirationDate;
    }

    public BigDecimal getAmount() {
      return amount;
    }

    public void setAmount(BigDecimal amount) {
      this.amount = amount;
    }

    public Status getStatus() {

      if (status == null) return Status.UNKNOWN;

      int statusValue = status.intValue();

      return transformStatus(statusValue);
    }

    public void setStatus(Integer status) {
      this.status = status;
    }

    public Boolean getValid() {
      return valid;
    }

    public void setValid(Boolean valid) {
      this.valid = valid;
    }

    public String getVisualID() {
      return visualID;
    }

    public void setVisualID(String visualID) {
      this.visualID = visualID;
    }

    public String toString() {

      StringBuffer sb = new StringBuffer("LineageRecord: ");
      sb.append(" VisualID: " + visualID);
      sb.append(" Status: " + status);
      sb.append(" Amount: " + amount);
      sb.append(" Valid: " + valid);
      sb.append(" Expiration Date: " + expirationDate);
      String resp = "Amount Status VisualId Valid ExpirationDate";

      return resp;
    }

  } // End class LineageRecord
  
  public class UpgradePLUList{
	  
	  private String PLU;
	  
	  private BigDecimal price;
	  
	  private BigDecimal upgradePrice;

	/**
	 * @return the pLU
	 */
	public String getPLU() {
		return PLU;
	}

	/**
	 * @param pLU the pLU to set
	 */
	public void setPLU(String pLU) {
		PLU = pLU;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the upgradePrice
	 */
	public BigDecimal getUpgradePrice() {
		return upgradePrice;
	}

	/**
	 * @param upgradePrice the upgradePrice to set
	 */
	public void setUpgradePrice(BigDecimal upgradePrice) {
		this.upgradePrice = upgradePrice;
	}
	  
	public String toString() {

	      StringBuffer sb = new StringBuffer("UpgradePluList: ");
	      sb.append(" PLU: " + PLU);
	      sb.append(" Price: " + price);
	      sb.append(" Upgrade Price: " + upgradePrice);
	     
	      String resp = sb.toString();

	      return resp;
	    }
	  
	  
  }

  /**
   * @return the dateOpened
   */
  public GregorianCalendar getDateOpened() {
    return dateOpened;
  }

  /**
   * @return the dateSold
   */
  public GregorianCalendar getDateSold() {
    return dateSold;
  }

  /**
   * @return the endDateTime
   */
  public GregorianCalendar getEndDateTime() {
    return endDateTime;
  }

  /**
   * @return the expirationDate
   */
  public GregorianCalendar getExpirationDate() {
    return expirationDate;
  }

  /**
   * Note: This method maps the actual values (1 & 2) to the enumerations.
   * 
   * @return the itemKind
   */
  public ItemKind getItemKind() {

    if (itemKind == null) return ItemKind.UNKNOWN;

    if (itemKind.intValue() == 1) {
      return ItemKind.REGULAR_TICKET;
    }

    if (itemKind.intValue() == 2) {
      return ItemKind.PASS;
    }

    return ItemKind.UNKNOWN;
  }

  /**
   * @return the lockedOut
   */
  public Boolean getLockedOut() {
    return lockedOut;
  }

  /**
   * @return the returnable
   */
  public Boolean getReturnable() {
    return returnable;
  }

  /**
   * @return the startDateTime
   */
  public GregorianCalendar getStartDateTime() {
    return startDateTime;
  }

  /**
   * @return the ticketDate
   */
  public GregorianCalendar getTicketDate() {
    return ticketDate;
  }

  /**
   * @return the validUntil
   */
  public GregorianCalendar getValidUntil() {
    return validUntil;
  }

  /**
   * @param dateOpened
   *            the dateOpened to set
   */
  public void setDateOpened(GregorianCalendar dateOpened) {
    this.dateOpened = dateOpened;
  }

  /**
   * @param dateSold
   *            the dateSold to set
   */
  public void setDateSold(GregorianCalendar dateSold) {
    this.dateSold = dateSold;
  }

  /**
   * @param endDateTime
   *            the endDateTime to set
   */
  public void setEndDateTime(GregorianCalendar endDateTime) {
    this.endDateTime = endDateTime;
  }

  /**
   * @param expirationDate
   *            the expirationDate to set
   */
  public void setExpirationDate(GregorianCalendar expirationDate) {
    this.expirationDate = expirationDate;
  }

  /**
   * @param itemKind
   *            the itemKind to set
   */
  public void setItemKind(Integer itemKind) {
    this.itemKind = itemKind;
  }

  /**
   * @param lockedOut
   *            the lockedOut to set
   */
  public void setLockedOut(Boolean lockedOut) {
    this.lockedOut = lockedOut;
  }

  /**
   * @param returnable
   *            the returnable to set
   */
  public void setReturnable(Boolean returnable) {
    this.returnable = returnable;
  }

  /**
   * @param startDateTime
   *            the startDateTime to set
   */
  public void setStartDateTime(GregorianCalendar startDateTime) {
    this.startDateTime = startDateTime;
  }

  /**
   * @param ticketDate
   *            the ticketDate to set
   */
  public void setTicketDate(GregorianCalendar ticketDate) {
    this.ticketDate = ticketDate;
  }

  /**
   * @param validUntil
   *            the validUntil to set
   */
  public void setValidUntil(GregorianCalendar validUntil) {
    this.validUntil = validUntil;
  }

  public Status transformStatus(int statusValue) {
    switch (statusValue) {

    case 0:
      return Status.VALID;
    case 1:
      return Status.VOIDED;
    case 2:
      return Status.RETURNED;
    case 3:
      return Status.REPLACED;
    case 4:
      return Status.PURCHASER;
    case 5:
      return Status.EXPIRED;
    case 6:
      return Status.UPGRADED;
    case 7:
      return Status.REPRINTED;
    case 8:
      return Status.BLOCKED;
    case 9:
      return Status.UNISSUED;
    default:
      return Status.UNKNOWN;
    }

  }

  /**
   * Note: This method maps the actual values to the enumerations.
   * 
   * @return the status
   */
  public Status getStatus() {

    if (status == null) return Status.UNKNOWN;

    int statusValue = status.intValue();

    return transformStatus(statusValue);

  }

  /**
   * @param status
   *            the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * @return the visualID
   */
  public String getVisualID() {
    return visualID;
  }

  /**
   * @param visualID
   *            the visualID to set
   */
  public void setVisualID(String visualID) {
    this.visualID = visualID;
  }

  /**
   * @return the price
   */
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * @return the tax
   */
  public BigDecimal getTax() {
    return tax;
  }

  /**
   * @param price
   *            the price to set
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /**
   * @param tax
   *            the tax to set
   */
  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  /**
   * @return the useCount
   */
  public Integer getUseCount() {
    return useCount;
  }

  /**
   * @param useCount
   *            the useCount to set
   */
  public void setUseCount(Integer useCount) {
    this.useCount = useCount;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public GregorianCalendar getDateUsed() {
    return dateUsed;
  }

  public void setDateUsed(GregorianCalendar dateUsed) {
    this.dateUsed = dateUsed;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassKindName() {
    return passKindName;
  }

  public void setPassKindName(String passKindName) {
    this.passKindName = passKindName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getStreet1() {
    return street1;
  }

  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public ArrayList<LineageRecord> getLineageArray() {
    return lineageArray;
  }

  public void setLineageArray(ArrayList<LineageRecord> lineageArray) {
    this.lineageArray = lineageArray;
  }

  public void addLineageRecord(LineageRecord inRecord) {
    lineageArray.add(inRecord);
  }
  
  

  /**
 * @return the upgradePLUList
 */
public ArrayList<UpgradePLUList> getUpgradePLUList() {
	return upgradePLUList;
}

/**
 * @param upgradePLUList the upgradePLUList to set
 */
public void setUpgradePLUList(ArrayList<UpgradePLUList> upgradePLUList) {
	this.upgradePLUList = upgradePLUList;
}

public void addUpgradePLUList(UpgradePLUList inRecord) {
	upgradePLUList.add(inRecord);
  }

/**
   * @return the kind
   */
  public String getKind() {
    return kind;
  }

  /**
   * @param kind
   *            the kind to set
   */
  public void setKind(String kind) {
    this.kind = kind;
  }

  /**
   * @return the remainingUse
   */
  public Integer getRemainingUse() {
    return remainingUse;
  }

  /**
   * @param remainingUse
   *            the remainingUse to set
   */
  public void setRemainingUse(Integer remainingUse) {
    this.remainingUse = remainingUse;
  }

  /**
   * @return the gender
   */
  public Integer getGender() {
    return gender;
  }

  /**
   * @param gender
   *            the gender to set
   */
  public void setGender(Integer gender) {
    this.gender = gender;
  }

  /**
   * @return the dateOfBirth
   */
  public GregorianCalendar getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * @param dateOfBirth
   *            the dateOfBirth to set
   */
  public void setDateOfBirth(GregorianCalendar dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * @return the genderRespString
   */
  public String getGenderRespString() {
    return genderRespString;
  }

  /**
   * @param genderRespString
   *            the genderRespString to set
   */
  public void setGenderRespString(String genderRespString) {
    this.genderRespString = genderRespString;
  }

  /**
   * @return the renewable
   */
  public Boolean getRenewable() {
    return renewable;
  }

  /**
   * @param renewable
   *            the renewable to set
   */
  public void setRenewable(Boolean renewable) {
    this.renewable = renewable;
  }

}
