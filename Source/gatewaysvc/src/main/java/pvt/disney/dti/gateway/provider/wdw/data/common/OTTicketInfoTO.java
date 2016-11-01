package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;

/**
 * The Class OTTicketInfoTO is used as a transfer object representing the Omni Ticket Ticket Info.
 * 
 * @author lewit019
 * 
 */
public class OTTicketInfoTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  /** The constant value which means true in Omni Ticket responses ("1"). */
  private final static String TRUESTRINGVALUE = "1";

  /** The ticket search mode (ticket identifier). */
  private OTTicketTO ticketSearchMode;

  /** The item status. */
  private Integer itemStatus;

  /** The ticket type. */
  private BigInteger ticketType;

  /** The ticket price. */
  private BigDecimal price;

  /** The ticket tax. */
  private BigDecimal tax;

  /** The value remaining on this ticket. */
  private BigDecimal remainingValue;

  /** The void code. */
  private Integer voidCode;

  /** The valid start date. */
  private GregorianCalendar validityStartDate;

  /** The valid end date. */
  private GregorianCalendar validityEndDate;

  /** The array list of ticket flag integers. */
  private ArrayList<Integer> ticketFlagList = new ArrayList<Integer>();

  /** The list of usages that this ticket has recorded. */
  private ArrayList<OTUsagesTO> usagesList = new ArrayList<OTUsagesTO>();

  /** The full ticket identify information. */
  private OTTicketTO ticket;

  /** The ticket attribute. */
  private Integer ticketAttribute;

  /** The ticket note. */
  private String ticketNote;

  /** The biometric level value. */
  private Integer biometricLevel;

  /** The Omni Ticket Transaction DSSN Transfer Object. */
  private OTTransactionDSSNTO transactionDSSN;

  /** The transaction code. */
  private String transactionCOD;

  /** The group quantity. */
  private Integer groupQuantity;

  /** The ZIP code. */
  private String zipCode;

  // Search fields
  /** The item number. */
  private BigInteger item;

  /** Is the ticket tax exempt? */
  private Boolean taxExempt;

  /** Is the ticket already used? */
  private Boolean alreadyused;

  /** is Entitlement electronic eligible ? */
  private Boolean denyMultiEntitlementFunctions;

  /** Account Id belonging to the entitlement (As of 2.16.1 BIEST001; 05/09/2016) */
  private String accountId;

  /** ticket response demographics (as of 2.16.1) */
  private OTDemographicData seasonPassDemo;

  /**
   * Gets the item.
   * 
   * @return the item
   */
  public BigInteger getItem() {
    return item;
  }

  /**
   * Gets the ticket search mode.
   * 
   * @return the ticketSearchMode
   */
  public OTTicketTO getTicketSearchMode() {
    return ticketSearchMode;
  }

  /**
   * Sets the item.
   * 
   * @param item
   *            the item to set
   */
  public void setItem(BigInteger item) {
    this.item = item;
  }

  /**
   * Sets the ticket search mode.
   * 
   * @param ticketSearchMode
   *            the ticketSearchMode to set
   */
  public void setTicketSearchMode(OTTicketTO ticketSearchMode) {
    this.ticketSearchMode = ticketSearchMode;
  }

  /**
   * @return the alreadyused
   */
  public Boolean getAlreadyused() {
    return alreadyused;
  }

  /**
   * @return the biometricLevel
   */
  public Integer getBiometricLevel() {
    return biometricLevel;
  }

  /**
   * @return the groupQuantity
   */
  public Integer getGroupQuantity() {
    return groupQuantity;
  }

  /**
   * @return the itemStatus
   */
  public Integer getItemStatus() {
    return itemStatus;
  }

  /**
   * @return the price
   */
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * @return the remainingValue
   */
  public BigDecimal getRemainingValue() {
    return remainingValue;
  }

  /**
   * @return the tax
   */
  public BigDecimal getTax() {
    return tax;
  }

  /**
   * @return the taxExempt
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * @return the ticketAttribute
   */
  public Integer getTicketAttribute() {
    return ticketAttribute;
  }

  /**
   * @return the ticketFlagList
   */
  public ArrayList<Integer> getTicketFlagList() {
    return ticketFlagList;
  }

  /**
   * @return the ticketNote
   */
  public String getTicketNote() {
    return ticketNote;
  }

  /**
   * @return the ticketType
   */
  public BigInteger getTicketType() {
    return ticketType;
  }

  /**
   * @return the transactionCOD
   */
  public String getTransactionCOD() {
    return transactionCOD;
  }

  /**
   * @return the transactionDSSN
   */
  public OTTransactionDSSNTO getTransactionDSSN() {
    return transactionDSSN;
  }

  /**
   * @return the usagesList
   */
  public ArrayList<OTUsagesTO> getUsagesList() {
    return usagesList;
  }

  /**
   * @return the validityEndDate
   */
  public GregorianCalendar getValidityEndDate() {
    return validityEndDate;
  }

  /**
   * @return the validityStartDate
   */
  public GregorianCalendar getValidityStartDate() {
    return validityStartDate;
  }

  /**
   * @return the voidCode
   */
  public Integer getVoidCode() {
    return voidCode;
  }

  /**
   * @param alreadyused
   *            the alreadyused to set
   */
  public void setAlreadyused(Boolean alreadyused) {
    this.alreadyused = alreadyused;
  }

  /**
   * @param biometricLevel
   *            the biometricLevel to set
   */
  public void setBiometricLevel(Integer biometricLevel) {
    this.biometricLevel = biometricLevel;
  }

  /**
   * @param groupQuantity
   *            the groupQuantity to set
   */
  public void setGroupQuantity(Integer groupQuantity) {
    this.groupQuantity = groupQuantity;
  }

  /**
   * @param itemStatus
   *            the itemStatus to set
   */
  public void setItemStatus(Integer itemStatus) {
    this.itemStatus = itemStatus;
  }

  /**
   * @param price
   *            the price to set
   */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  /**
   * @param remainingValue
   *            the remainingValue to set
   */
  public void setRemainingValue(BigDecimal remainingValue) {
    this.remainingValue = remainingValue;
  }

  /**
   * @param tax
   *            the tax to set
   */
  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  /**
   * @param taxExempt
   *            the taxExempt to set
   */
  public void setTaxExempt(Boolean taxExempt) {
    this.taxExempt = taxExempt;
  }

  /**
   * @param ticketAttribute
   *            the ticketAttribute to set
   */
  public void setTicketAttribute(Integer ticketAttribute) {
    this.ticketAttribute = ticketAttribute;
  }

  /**
   * @param ticketFlagList
   *            the ticketFlagList to set
   */
  public void setTicketFlagList(ArrayList<Integer> ticketFlagList) {
    this.ticketFlagList = ticketFlagList;
  }

  /**
   * @param ticketNote
   *            the ticketNote to set
   */
  public void setTicketNote(String ticketNote) {
    this.ticketNote = ticketNote;
  }

  /**
   * @param ticketType
   *            the ticketType to set
   */
  public void setTicketType(BigInteger ticketType) {
    this.ticketType = ticketType;
  }

  /**
   * @param transactionCOD
   *            the transactionCOD to set
   */
  public void setTransactionCOD(String transactionCOD) {
    this.transactionCOD = transactionCOD;
  }

  /**
   * @param transactionDSSN
   *            the transactionDSSN to set
   */
  public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
    this.transactionDSSN = transactionDSSN;
  }

  /**
   * @param usagesList
   *            the usagesList to set
   */
  public void setUsagesList(ArrayList<OTUsagesTO> usagesList) {
    this.usagesList = usagesList;
  }

  /**
   * @param validityEndDate
   *            the validityEndDate to set
   */
  public void setValidityEndDate(GregorianCalendar validityEndDate) {
    this.validityEndDate = validityEndDate;
  }

  /**
   * Allows the caller to set the validity end date as a string, with the format yy-MM-dd (such as 09-01-31).
   * 
   * @param validityEndDate
   *            the string value of the validity end date to be set.
   * @throws ParseException
   *             if the string cannot be parsed.
   */
  public void setValidityEndDate(String validityEndDate) throws ParseException {

    this.validityEndDate = OTCommandTO.convertOmniYYDate(validityEndDate);

    return;
  }

  /**
   * @param validityStartDate
   *            the validityStartDate to set
   */
  public void setValidityStartDate(GregorianCalendar validityStartDate) {
    this.validityStartDate = validityStartDate;
  }

  /**
   * Allows the caller to set the validity start date as a string, with the format yy-MM-dd (such as 09-01-31).
   * 
   * @param validityEndDate
   *            the string value of the validity start date to be set.
   * @throws ParseException
   *             if the string cannot be parsed.
   */
  public void setValidityStartDate(String validityStartDate) throws ParseException {

    this.validityStartDate = OTCommandTO
        .convertOmniYYDate(validityStartDate);

    return;
  }

  /**
   * @param voidCode
   *            the voidCode to set
   */
  public void setVoidCode(Integer voidCode) {
    this.voidCode = voidCode;
  }

  /**
   * @return the ticket
   */
  public OTTicketTO getTicket() {
    return ticket;
  }

  /**
   * @param ticket
   *            the ticket to set.
   */
  public void setTicket(OTTicketTO ticket) {
    this.ticket = ticket;
  }

  /**
   * Allows the caller to set the already used flag using the standard Omni string values.
   * 
   * @param flag
   *            the string version of the already used flag to set.
   */
  public void setAlreadyused(String flag) {
    if (flag.compareTo(TRUESTRINGVALUE) == 0) alreadyused = new Boolean(
        true);
    else alreadyused = new Boolean(false);
  }

  /**
   * 
   * @return the ZIP code.
   */
  public String getZipCode() {
    return zipCode;
  }

  /**
   * Sets the ZIP code.
   * 
   * @param zipCode
   *            the ZIP code to set.
   */
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  /**
   * Allows the caller to set the denyMultiEntitlementFunctions flag using the standard Omni string values.
   * 
   * @param flag
   *            the string version of the flag to set.
   */
  public void setDenyMultiEntitlementFunctions(String flag) {
    this.denyMultiEntitlementFunctions = new Boolean(
        flag.compareTo(TRUESTRINGVALUE) == 0);
  }

  /**
   * @param denyMultiEntitlementFunctions
   *            the denyMultiEntitlementFunctions to set
   */
  public void setDenyMultiEntitlementFunctions(
      Boolean denyMultiEntitlementFunctions) {
    this.denyMultiEntitlementFunctions = denyMultiEntitlementFunctions;
  }

  /**
   * @return the denyMultiEntitlementFunctions
   */
  public Boolean getDenyMultiEntitlementFunctions() {
    return denyMultiEntitlementFunctions;
  }

  /**
   * @return the accountId
   */
  public String getAccountId() {
    return accountId;
  }

  /**
   * @param accountId
   *            the accountId to set
   */
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  /**
   * @return the seasonPassDemo
   */
  public OTDemographicData getSeasonPassDemo() {
    return seasonPassDemo;
  }

  /**
   * @param seasonPassDemo
   *            the seasonPassDemo to set
   */
  public void setSeasonPassDemo(OTDemographicData seasonPassDemo) {
    this.seasonPassDemo = seasonPassDemo;
  }
}
