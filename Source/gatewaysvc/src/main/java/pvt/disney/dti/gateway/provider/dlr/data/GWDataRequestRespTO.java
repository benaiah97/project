package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * This class represents the DataRequestResp portion of an eGalaxy XML.
 * 
 * @author lewit019,smoon
 * 
 */
public class GWDataRequestRespTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  /**
   * The Enum ItemKind.
   */
  public static enum ItemKind {
    
    /** The regular ticket. */
    REGULAR_TICKET,
    
    /** The pass. */
    PASS,
    
    /** The unknown. */
    UNKNOWN
  }

  /**
   * The Enum Status.
   */
  public static enum Status {
    
    /** The valid. */
    VALID,
    
    /** The voided. */
    VOIDED,
    
    /** The returned. */
    RETURNED,
    
    /** The replaced. */
    REPLACED,
    
    /** The purchaser. */
    PURCHASER,
    
    /** The expired. */
    EXPIRED,
    
    /** The upgraded. */
    UPGRADED,
    
    /** The reprinted. */
    REPRINTED,
    
    /** The blocked. */
    BLOCKED,
    
    /** The unissued. */
    UNISSUED,
    
    /** The unknown. */
    UNKNOWN
  }

  /** The item kind. */
  private Integer itemKind;

  /** The returnable. */
  private Boolean returnable;

  /** The status. */
  private Integer status;

  /** The visual ID. */
  private String visualID;

  /** The price. */
  private BigDecimal price;

  /** The tax. */
  private BigDecimal tax;

  /** The use count. */
  private Integer useCount;

  /** The date sold. */
  // Start Validity Date
  private GregorianCalendar dateSold;

  /** The ticket date. */
  private GregorianCalendar ticketDate;

  /** The start date time. */
  private GregorianCalendar startDateTime;

  /** The date opened. */
  private GregorianCalendar dateOpened;

  /** The expiration date. */
  // End Validity Date
  private GregorianCalendar expirationDate;

  /** The end date time. */
  private GregorianCalendar endDateTime;

  /** The valid until. */
  private GregorianCalendar validUntil;

  /** The locked out. */
  private Boolean lockedOut;

  /** The first name. */
  private String firstName;

  /** The last name. */
  private String lastName;

  /** The street 1. */
  private String street1;

  /** The street 2. */
  private String street2;

  /** The city. */
  private String city;

  /** The state. */
  private String state;

  /** The zip. */
  private String zip;

  /** The country code. */
  private String countryCode;

  /** The phone. */
  private String phone;

  /** The email. */
  private String email;

  /** The date used. */
  private GregorianCalendar dateUsed;

  /** The pass kind name. */
  private String passKindName;

  /** The lineage array. */
  private ArrayList<LineageRecord> lineageArray = new ArrayList<LineageRecord>();
  
  /** The upgrade PLU list. */
  private ArrayList<UpgradePLUList> upgradePLUList = new ArrayList<UpgradePLUList>();
  
  /** The picture. */
  private String picture;
  
  private ArrayList<Contact> contactList=new ArrayList<Contact>();

  /** The kind. */
  private String kind; // Added as of 2.11

  /** The remaining use. */
  private Integer remainingUse; // Added as of 2.14 JTL

  /** The gender. */
  private Integer gender; // As of 2.16.1, JTL - used on request.

  /** The gender resp string. */
  private String genderRespString; // As of 2.16.1, JTL - used on response (Galaxy type violation)

  /** The date of birth. */
  private GregorianCalendar dateOfBirth; // As of 2.16.1, JTL

  /** The renewable. */
  private Boolean renewable; // As of 2.16.1, JTL

  /**
   * The Class LineageRecord.
   *
   * @author lewit019
   */
  public class LineageRecord {

    /** The amount. */
    private BigDecimal amount;

    /** The status. */
    private Integer status;

    /** The visual ID. */
    private String visualID;

    /** The valid. */
    private Boolean valid;

    /** The expiration date. */
    private GregorianCalendar expirationDate;

    /**
     * Gets the expiration date.
     *
     * @return the expiration date
     */
    public GregorianCalendar getExpirationDate() {
      return expirationDate;
    }

    /**
     * Sets the expiration date.
     *
     * @param expirationDate the new expiration date
     */
    public void setExpirationDate(GregorianCalendar expirationDate) {
      this.expirationDate = expirationDate;
    }

    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public BigDecimal getAmount() {
      return amount;
    }

    /**
     * Sets the amount.
     *
     * @param amount the new amount
     */
    public void setAmount(BigDecimal amount) {
      this.amount = amount;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public Status getStatus() {

      if (status == null) return Status.UNKNOWN;

      int statusValue = status.intValue();

      return transformStatus(statusValue);
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(Integer status) {
      this.status = status;
    }

    /**
     * Gets the valid.
     *
     * @return the valid
     */
    public Boolean getValid() {
      return valid;
    }

    /**
     * Sets the valid.
     *
     * @param valid the new valid
     */
    public void setValid(Boolean valid) {
      this.valid = valid;
    }

    /**
     * Gets the visual ID.
     *
     * @return the visual ID
     */
    public String getVisualID() {
      return visualID;
    }

    /**
     * Sets the visual ID.
     *
     * @param visualID the new visual ID
     */
    public void setVisualID(String visualID) {
      this.visualID = visualID;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
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
  
  /**
   * The Class UpgradePLUList.
   */
  public class UpgradePLUList{
	  
	  /** The plu. */
  	private String PLU;
	  
	  /** The price. */
  	private BigDecimal price;
	  
	  /** The upgrade price. */
  	private BigDecimal upgradePrice;

	/**
	 * Gets the plu.
	 *
	 * @return the pLU
	 */
	public String getPLU() {
		return PLU;
	}

	/**
	 * Sets the plu.
	 *
	 * @param pLU the pLU to set
	 */
	public void setPLU(String pLU) {
		PLU = pLU;
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
	 * Sets the price.
	 *
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Gets the upgrade price.
	 *
	 * @return the upgradePrice
	 */
	public BigDecimal getUpgradePrice() {
		return upgradePrice;
	}

	/**
	 * Sets the upgrade price.
	 *
	 * @param upgradePrice the upgradePrice to set
	 */
	public void setUpgradePrice(BigDecimal upgradePrice) {
		this.upgradePrice = upgradePrice;
	}
	  
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
   * The Class Contact.
   */
  public class Contact{
	  
  	/** The first name. */
  	private String firstName;
	  
  	/** The middle name. */
  	private String middleName;
	  
  	/** The last name. */
  	private String lastName;
	  
  	/** The identification no. */
  	private String identificationNo;
	  
  	/** The street 1. */
  	private String street1;
	  
  	/** The street 2. */
  	private String street2;
	  
  	/** The street 3. */
  	private String street3;
	  
  	/** The city. */
  	private String city;
	  
  	/** The state. */
  	private String state;
	  
  	/** The zip. */
  	private String zip;
	  
  	/** The country code. */
  	private String countryCode;
	  
  	/** The phone. */
  	private String phone;
	  
  	/** The fax. */
  	private String fax;
	  
  	/** The cell. */
  	private String cell;
	  
  	/** The email. */
  	private String email;
	  
  	/** The external id. */
  	private String externalId;
	  
  	/** The contact GUID. */
  	private String contactGUID;
	  
  	/** The galaxy contact id. */
  	private String galaxyContactId;
	  
  	/** The job title. */
  	private String jobTitle;
	  
  	/** The primary. */
  	private String primary;
	  
  	/** The contact note. */
  	private String contactNote;
	  
  	/** The name title id. */
  	private String nameTitleId;
	  
  	/** The name suffix id. */
  	private String nameSuffixId;
	  
  	/** The total payment contracts. */
  	private String totalPaymentContracts;
	  
  	/** The allow mail. */
  	private String allowMail;
	  
  	/** The allow mailing. */
  	private String allowMailing;
	  
  	/** The dob. */
  	private String dob;
	  
  	/** The age group. */
  	private String ageGroup;
	  
  	/** The gender. */
  	private String gender;

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the identificationNo
	 */
	public String getIdentificationNo() {
		return identificationNo;
	}

	/**
	 * @param identificationNo the identificationNo to set
	 */
	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	/**
	 * @return the street1
	 */
	public String getStreet1() {
		return street1;
	}

	/**
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	/**
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}

	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	/**
	 * @return the street3
	 */
	public String getStreet3() {
		return street3;
	}

	/**
	 * @param street3 the street3 to set
	 */
	public void setStreet3(String street3) {
		this.street3 = street3;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the cell
	 */
	public String getCell() {
		return cell;
	}

	/**
	 * @param cell the cell to set
	 */
	public void setCell(String cell) {
		this.cell = cell;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return the contactGUID
	 */
	public String getContactGUID() {
		return contactGUID;
	}

	/**
	 * @param contactGUID the contactGUID to set
	 */
	public void setContactGUID(String contactGUID) {
		this.contactGUID = contactGUID;
	}

	/**
	 * @return the galaxyContactId
	 */
	public String getGalaxyContactId() {
		return galaxyContactId;
	}

	/**
	 * @param galaxyContactId the galaxyContactId to set
	 */
	public void setGalaxyContactId(String galaxyContactId) {
		this.galaxyContactId = galaxyContactId;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the primary
	 */
	public String getPrimary() {
		return primary;
	}

	/**
	 * @param primary the primary to set
	 */
	public void setPrimary(String primary) {
		this.primary = primary;
	}

	/**
	 * @return the contactNote
	 */
	public String getContactNote() {
		return contactNote;
	}

	/**
	 * @param contactNote the contactNote to set
	 */
	public void setContactNote(String contactNote) {
		this.contactNote = contactNote;
	}

	/**
	 * @return the nameTitleId
	 */
	public String getNameTitleId() {
		return nameTitleId;
	}

	/**
	 * @param nameTitleId the nameTitleId to set
	 */
	public void setNameTitleId(String nameTitleId) {
		this.nameTitleId = nameTitleId;
	}

	/**
	 * @return the nameSuffixId
	 */
	public String getNameSuffixId() {
		return nameSuffixId;
	}

	/**
	 * @param nameSuffixId the nameSuffixId to set
	 */
	public void setNameSuffixId(String nameSuffixId) {
		this.nameSuffixId = nameSuffixId;
	}

	/**
	 * @return the totalPaymentContracts
	 */
	public String getTotalPaymentContracts() {
		return totalPaymentContracts;
	}

	/**
	 * @param totalPaymentContracts the totalPaymentContracts to set
	 */
	public void setTotalPaymentContracts(String totalPaymentContracts) {
		this.totalPaymentContracts = totalPaymentContracts;
	}

	/**
	 * @return the allowMail
	 */
	public String getAllowMail() {
		return allowMail;
	}

	/**
	 * @param allowMail the allowMail to set
	 */
	public void setAllowMail(String allowMail) {
		this.allowMail = allowMail;
	}

	/**
	 * @return the allowMailing
	 */
	public String getAllowMailing() {
		return allowMailing;
	}

	/**
	 * @param allowMailing the allowMailing to set
	 */
	public void setAllowMailing(String allowMailing) {
		this.allowMailing = allowMailing;
	}

	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}

	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}

	/**
	 * @return the ageGroup
	 */
	public String getAgeGroup() {
		return ageGroup;
	}

	/**
	 * @param ageGroup the ageGroup to set
	 */
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + ", middleName=" + middleName
				+ ", lastName=" + lastName + ", identificationNo="
				+ identificationNo + ", street1=" + street1 + ", street2="
				+ street2 + ", street3=" + street3 + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", countryCode="
				+ countryCode + ", phone=" + phone + ", fax=" + fax + ", cell="
				+ cell + ", email=" + email + ", externalId=" + externalId
				+ ", contactGUID=" + contactGUID + ", galaxyContactId="
				+ galaxyContactId + ", jobTitle=" + jobTitle + ", primary="
				+ primary + ", contactNote=" + contactNote + ", nameTitleId="
				+ nameTitleId + ", nameSuffixId=" + nameSuffixId
				+ ", totalPaymentContracts=" + totalPaymentContracts
				+ ", allowMail=" + allowMail + ", allowMailing=" + allowMailing
				+ ", dob=" + dob + ", ageGroup=" + ageGroup + ", gender="
				+ gender + "]";
	}
	  
	  
  }
  

  /**
   * Gets the date opened.
   *
   * @return the dateOpened
   */
  public GregorianCalendar getDateOpened() {
    return dateOpened;
  }

  /**
   * Gets the date sold.
   *
   * @return the dateSold
   */
  public GregorianCalendar getDateSold() {
    return dateSold;
  }

  /**
   * Gets the end date time.
   *
   * @return the endDateTime
   */
  public GregorianCalendar getEndDateTime() {
    return endDateTime;
  }

  /**
   * Gets the expiration date.
   *
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
   * Gets the locked out.
   *
   * @return the lockedOut
   */
  public Boolean getLockedOut() {
    return lockedOut;
  }

  /**
   * Gets the returnable.
   *
   * @return the returnable
   */
  public Boolean getReturnable() {
    return returnable;
  }

  /**
   * Gets the start date time.
   *
   * @return the startDateTime
   */
  public GregorianCalendar getStartDateTime() {
    return startDateTime;
  }

  /**
   * Gets the ticket date.
   *
   * @return the ticketDate
   */
  public GregorianCalendar getTicketDate() {
    return ticketDate;
  }

  /**
   * Gets the valid until.
   *
   * @return the validUntil
   */
  public GregorianCalendar getValidUntil() {
    return validUntil;
  }

  /**
   * Sets the date opened.
   *
   * @param dateOpened            the dateOpened to set
   */
  public void setDateOpened(GregorianCalendar dateOpened) {
    this.dateOpened = dateOpened;
  }

  /**
   * Sets the date sold.
   *
   * @param dateSold            the dateSold to set
   */
  public void setDateSold(GregorianCalendar dateSold) {
    this.dateSold = dateSold;
  }

  /**
   * Sets the end date time.
   *
   * @param endDateTime            the endDateTime to set
   */
  public void setEndDateTime(GregorianCalendar endDateTime) {
    this.endDateTime = endDateTime;
  }

  /**
   * Sets the expiration date.
   *
   * @param expirationDate            the expirationDate to set
   */
  public void setExpirationDate(GregorianCalendar expirationDate) {
    this.expirationDate = expirationDate;
  }

  /**
   * Sets the item kind.
   *
   * @param itemKind            the itemKind to set
   */
  public void setItemKind(Integer itemKind) {
    this.itemKind = itemKind;
  }

  /**
   * Sets the locked out.
   *
   * @param lockedOut            the lockedOut to set
   */
  public void setLockedOut(Boolean lockedOut) {
    this.lockedOut = lockedOut;
  }

  /**
   * Sets the returnable.
   *
   * @param returnable            the returnable to set
   */
  public void setReturnable(Boolean returnable) {
    this.returnable = returnable;
  }

  /**
   * Sets the start date time.
   *
   * @param startDateTime            the startDateTime to set
   */
  public void setStartDateTime(GregorianCalendar startDateTime) {
    this.startDateTime = startDateTime;
  }

  /**
   * Sets the ticket date.
   *
   * @param ticketDate            the ticketDate to set
   */
  public void setTicketDate(GregorianCalendar ticketDate) {
    this.ticketDate = ticketDate;
  }

  /**
   * Sets the valid until.
   *
   * @param validUntil            the validUntil to set
   */
  public void setValidUntil(GregorianCalendar validUntil) {
    this.validUntil = validUntil;
  }

  /**
   * Transform status.
   *
   * @param statusValue the status value
   * @return the status
   */
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
   * Sets the status.
   *
   * @param status            the status to set
   */
  public void setStatus(Integer status) {
    this.status = status;
  }

  /**
   * Gets the visual ID.
   *
   * @return the visualID
   */
  public String getVisualID() {
    return visualID;
  }

  /**
   * Sets the visual ID.
   *
   * @param visualID            the visualID to set
   */
  public void setVisualID(String visualID) {
    this.visualID = visualID;
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
   * Gets the tax.
   *
   * @return the tax
   */
  public BigDecimal getTax() {
    return tax;
  }

  /**
   * Sets the price.
   *
   * @param price            the price to set
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
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
   * Gets the use count.
   *
   * @return the useCount
   */
  public Integer getUseCount() {
    return useCount;
  }

  /**
   * Sets the use count.
   *
   * @param useCount            the useCount to set
   */
  public void setUseCount(Integer useCount) {
    this.useCount = useCount;
  }

  /**
   * Gets the city.
   *
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the city.
   *
   * @param city the new city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Gets the country code.
   *
   * @return the country code
   */
  public String getCountryCode() {
    return countryCode;
  }

  /**
   * Sets the country code.
   *
   * @param countryCode the new country code
   */
  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  /**
   * Gets the date used.
   *
   * @return the date used
   */
  public GregorianCalendar getDateUsed() {
    return dateUsed;
  }

  /**
   * Sets the date used.
   *
   * @param dateUsed the new date used
   */
  public void setDateUsed(GregorianCalendar dateUsed) {
    this.dateUsed = dateUsed;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email.
   *
   * @param email the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name.
   *
   * @param firstName the new first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name.
   *
   * @param lastName the new last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the pass kind name.
   *
   * @return the pass kind name
   */
  public String getPassKindName() {
    return passKindName;
  }

  /**
   * Sets the pass kind name.
   *
   * @param passKindName the new pass kind name
   */
  public void setPassKindName(String passKindName) {
    this.passKindName = passKindName;
  }

  /**
   * Gets the phone.
   *
   * @return the phone
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Sets the phone.
   *
   * @param phone the new phone
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Gets the state.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the state.
   *
   * @param state the new state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Gets the street 1.
   *
   * @return the street 1
   */
  public String getStreet1() {
    return street1;
  }

  /**
   * Sets the street 1.
   *
   * @param street1 the new street 1
   */
  public void setStreet1(String street1) {
    this.street1 = street1;
  }

  /**
   * Gets the street 2.
   *
   * @return the street 2
   */
  public String getStreet2() {
    return street2;
  }

  /**
   * Sets the street 2.
   *
   * @param street2 the new street 2
   */
  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  /**
   * Gets the zip.
   *
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * Sets the zip.
   *
   * @param zip the new zip
   */
  public void setZip(String zip) {
    this.zip = zip;
  }

  /**
   * Gets the lineage array.
   *
   * @return the lineage array
   */
  public ArrayList<LineageRecord> getLineageArray() {
    return lineageArray;
  }

  /**
   * Sets the lineage array.
   *
   * @param lineageArray the new lineage array
   */
  public void setLineageArray(ArrayList<LineageRecord> lineageArray) {
    this.lineageArray = lineageArray;
  }

  /**
   * Adds the lineage record.
   *
   * @param inRecord the in record
   */
  public void addLineageRecord(LineageRecord inRecord) {
    lineageArray.add(inRecord);
  }
  
  

  /**
   * Gets the upgrade PLU list.
   *
   * @return the upgradePLUList
   */
public ArrayList<UpgradePLUList> getUpgradePLUList() {
	return upgradePLUList;
}

/**
 * Sets the upgrade PLU list.
 *
 * @param upgradePLUList the upgradePLUList to set
 */
public void setUpgradePLUList(ArrayList<UpgradePLUList> upgradePLUList) {
	this.upgradePLUList = upgradePLUList;
}

/**
 * Adds the upgrade PLU list.
 *
 * @param inRecord the in record
 */
public void addUpgradePLUList(UpgradePLUList inRecord) {
	upgradePLUList.add(inRecord);
  }


/**
 * @return the contact
 */
public ArrayList<Contact> getContact() {
	return contactList;
}

/**
 * @param contact the contact to set
 */
public void addContact(ArrayList<Contact> contact) {
	this.contactList = contact;
}


public void addContactList(Contact contact){
	contactList.add(contact);
}
/**
 * Gets the kind.
 *
 * @return the kind
 */
  public String getKind() {
    return kind;
  }

  /**
   * Sets the kind.
   *
   * @param kind            the kind to set
   */
  public void setKind(String kind) {
    this.kind = kind;
  }

  /**
   * Gets the remaining use.
   *
   * @return the remainingUse
   */
  public Integer getRemainingUse() {
    return remainingUse;
  }

  /**
   * Sets the remaining use.
   *
   * @param remainingUse            the remainingUse to set
   */
  public void setRemainingUse(Integer remainingUse) {
    this.remainingUse = remainingUse;
  }

  /**
   * Gets the gender.
   *
   * @return the gender
   */
  public Integer getGender() {
    return gender;
  }

  /**
   * Sets the gender.
   *
   * @param gender            the gender to set
   */
  public void setGender(Integer gender) {
    this.gender = gender;
  }

  /**
   * Gets the date of birth.
   *
   * @return the dateOfBirth
   */
  public GregorianCalendar getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Sets the date of birth.
   *
   * @param dateOfBirth            the dateOfBirth to set
   */
  public void setDateOfBirth(GregorianCalendar dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Gets the gender resp string.
   *
   * @return the genderRespString
   */
  public String getGenderRespString() {
    return genderRespString;
  }

  /**
   * Sets the gender resp string.
   *
   * @param genderRespString            the genderRespString to set
   */
  public void setGenderRespString(String genderRespString) {
    this.genderRespString = genderRespString;
  }

  /**
   * Gets the renewable.
   *
   * @return the renewable
   */
  public Boolean getRenewable() {
    return renewable;
  }

  /**
   * Sets the renewable.
   *
   * @param renewable            the renewable to set
   */
  public void setRenewable(Boolean renewable) {
    this.renewable = renewable;
  }

}
