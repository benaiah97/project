package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * This class represents guest demographic information.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class DemographicsTO implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 9129231995L;

  public enum GenderType {
    MALE,
    FEMALE,
    UNSPECIFIED,
    NOTPRESENT
  }

  public final static String GENDER_MALE_VALUE = "M";

  public final static String GENDER_FEMALE_VALUE = "F";

  public final static String GENDER_UNSPECIFIED_VALUE = "U";

  /** Guest full name. */
  private String name;

  /** Guest last name. */
  private String lastName;

  /** Guest first name. */
  private String firstName;

  /** Guest first name (Chinese) as of 2.16.3, JTL. */
  private String firstNameChinese;
  
  /** Guest address line one. */
  private String addr1;

  /** Guest address line two. */
  private String addr2;

  /** Guest city. */
  private String city;

  /** Guest state. */
  private String state;

  /** Guest ZIP. */
  private String zip;

  /** Guest country. */
  private String country;

  /** Guest telephone number. */
  private String telephone;

  /** Guest e-mail. */
  private String email;

  /** Guest seller res nbr */
  private String sellerResNbr;

  // Below added for Ticket Level Demographics

  /** Date of Birth. */
  private GregorianCalendar dateOfBirth;

  /** Gender */
  private GenderType gender = GenderType.NOTPRESENT;

  /** OptInSolicit */
  private Boolean optInSolicit;

  // Below added for Account Level Demographics (2.12)

  /** Title */
  private String title;

  /** MiddleName */
  private String middleName;

  /** Suffix */
  private String suffix;

  /** AltTelephone (as of 2.15, JTL) */
  private String altTelephone;
  
  /** Cell Phone */
  private String cellPhone;
  
  /** Seller Ref */
  private String sellerRef;

  /**
   * 
   * @return the seller res nbr
   */
  public String getSellerResNbr() {
    return sellerResNbr;
  }

  /**
   * Sets the seller res nbr.
   * 
   * @param sellerResNbr
   */
  public void setSellerResNbr(String sellerResNbr) {
    this.sellerResNbr = sellerResNbr;
  }

  /**
   * @return the addr1
   */
  public String getAddr1() {
    return addr1;
  }

  /**
   * @return the addr2
   */
  public String getAddr2() {
    return addr2;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * @return the telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * @param addr1
   *            the addr1 to set
   */
  public void setAddr1(String addr1) {
    this.addr1 = addr1;
  }

  /**
   * @param addr2
   *            the addr2 to set
   */
  public void setAddr2(String addr2) {
    this.addr2 = addr2;
  }

  /**
   * @param city
   *            the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @param country
   *            the country to set
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * @param email
   *            the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @param firstName
   *            the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @param lastName
   *            the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @param name
   *            the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @param state
   *            the state to set
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * @param telephone
   *            the telephone to set
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  /**
   * @param zip
   *            the ZIP to set
   */
  public void setZip(String zip) {
    this.zip = zip;
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
   * @return the gender in DTI string format ("M", "F", "U", or NULL)
   */
  public String getGender() {
    if (this.gender == GenderType.FEMALE) {
      return GENDER_FEMALE_VALUE;
    }
    else if (this.gender == GenderType.MALE) {
      return GENDER_MALE_VALUE;
    }
    else if (this.gender == GenderType.UNSPECIFIED) {
      return GENDER_UNSPECIFIED_VALUE;
    }
    else {
      return null;
    }
  }

  /**
   * @return the gender by enumerated type.
   */
  public GenderType getGenderType() {
    return this.gender;
  }

  /**
   * @param gender
   *            the gender to set
   */
  public void setGender(String genderString) {

    if (genderString.trim().substring(0, 1).equalsIgnoreCase(GENDER_FEMALE_VALUE)) {
      this.gender = GenderType.FEMALE;
    }
    else if (genderString.trim().substring(0, 1).equalsIgnoreCase(GENDER_MALE_VALUE)) {
      this.gender = GenderType.MALE;
    }
    else if (genderString.trim().substring(0, 1).equalsIgnoreCase(GENDER_UNSPECIFIED_VALUE)) {
      this.gender = GenderType.UNSPECIFIED;
    }
    else this.gender = GenderType.NOTPRESENT;
  }

  /**
   * 
   * @param genderTypeIn
   *            the gender to set
   */
  public void setGender(GenderType genderTypeIn) {
    this.gender = genderTypeIn;
  }

  /**
   * @return the optInSolicit
   */
  public Boolean getOptInSolicit() {
    return optInSolicit;
  }

  /**
   * @param optInSolicit
   *            the optInSolicit to set
   */
  public void setOptInSolicit(Boolean optInSolicit) {
    this.optInSolicit = optInSolicit;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title
   *            the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the middleName
   */
  public String getMiddleName() {
    return middleName;
  }

  /**
   * @param middleName
   *            the middleName to set
   */
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * @return the suffix
   */
  public String getSuffix() {
    return suffix;
  }

  /**
   * @param suffix
   *            the suffix to set
   */
  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  /**
   * @return the altTelephone
   */
  public String getAltTelephone() {
    return altTelephone;
  }

  /**
   * @param altTelephone
   *            the altTelephone to set
   */
  public void setAltTelephone(String altTelephone) {
    this.altTelephone = altTelephone;
  }

  /**
   * @return the cellPhone
   */
  public String getCellPhone() {
    return cellPhone;
  }

  /**
   * @param cellPhone the cellPhone to set
   */
  public void setCellPhone(String cellPhone) {
    this.cellPhone = cellPhone;
  }

  /**
   * @return the sellerRef
   */
  public String getSellerRef() {
    return sellerRef;
  }

  /**
   * @param sellerRef the sellerRef to set
   */
  public void setSellerRef(String sellerRef) {
    this.sellerRef = sellerRef;
  }

  /**
   * @return the firstNameChinese
   */
  public String getFirstNameChinese() {
    return firstNameChinese;
  }

  /**
   * @param firstNameChinese the firstNameChinese to set
   */
  public void setFirstNameChinese(String firstNameChinese) {
    this.firstNameChinese = firstNameChinese;
  }

}
