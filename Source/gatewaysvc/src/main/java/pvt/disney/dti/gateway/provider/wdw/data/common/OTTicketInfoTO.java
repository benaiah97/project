package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;

// TODO: Auto-generated Javadoc
/**
 * The Class OTTicketInfoTO is used as a transfer object representing the Omni
 * Ticket Ticket Info.
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

	/** The item alpha code (as of 2.16.3, JTL) */
	private String itemAlphaCode;

	/** The item num code (as of 2.16.3, JTL) */
	private BigInteger itemNumCode;

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

	/** Is the ticket tax exempt?. */
	private Boolean taxExempt;

	/** Is the ticket already used?. */
	private Boolean alreadyused;

	/** is Entitlement electronic eligible ?. */
	private Boolean denyMultiEntitlementFunctions;

	/**
	 * Account Id belonging to the entitlement (As of 2.16.1 BIEST001;
	 * 05/09/2016)
	 */
	private String accountId;

	/** ticket response demographics (as of 2.16.1) */
	private OTDemographicData seasonPassDemo;

	/** The biometric template. */
	private String biometricTemplate;

	/** The pay plan. */
	private String payPlan;

	/** The upgrade PLU list. */
	private ArrayList<OTUpgradePLU> upgradePLUList = new ArrayList<OTUpgradePLU>();

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
	 *           the item to set
	 */
	public void setItem(BigInteger item) {
		this.item = item;
	}

	/**
	 * Sets the ticket search mode.
	 * 
	 * @param ticketSearchMode
	 *           the ticketSearchMode to set
	 */
	public void setTicketSearchMode(OTTicketTO ticketSearchMode) {
		this.ticketSearchMode = ticketSearchMode;
	}

	/**
	 * Gets the alreadyused.
	 *
	 * @return the alreadyused
	 */
	public Boolean getAlreadyused() {
		return alreadyused;
	}

	/**
	 * Gets the biometric level.
	 *
	 * @return the biometricLevel
	 */
	public Integer getBiometricLevel() {
		return biometricLevel;
	}

	/**
	 * Gets the group quantity.
	 *
	 * @return the groupQuantity
	 */
	public Integer getGroupQuantity() {
		return groupQuantity;
	}

	/**
	 * Gets the item status.
	 *
	 * @return the itemStatus
	 */
	public Integer getItemStatus() {
		return itemStatus;
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
	 * Gets the remaining value.
	 *
	 * @return the remainingValue
	 */
	public BigDecimal getRemainingValue() {
		return remainingValue;
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
	 * Gets the tax exempt.
	 *
	 * @return the taxExempt
	 */
	public Boolean getTaxExempt() {
		return taxExempt;
	}

	/**
	 * Gets the ticket attribute.
	 *
	 * @return the ticketAttribute
	 */
	public Integer getTicketAttribute() {
		return ticketAttribute;
	}

	/**
	 * Gets the ticket flag list.
	 *
	 * @return the ticketFlagList
	 */
	public ArrayList<Integer> getTicketFlagList() {
		return ticketFlagList;
	}

	/**
	 * Gets the ticket note.
	 *
	 * @return the ticketNote
	 */
	public String getTicketNote() {
		return ticketNote;
	}

	/**
	 * Gets the ticket type.
	 *
	 * @return the ticketType
	 */
	public BigInteger getTicketType() {
		return ticketType;
	}

	/**
	 * Gets the transaction COD.
	 *
	 * @return the transactionCOD
	 */
	public String getTransactionCOD() {
		return transactionCOD;
	}

	/**
	 * Gets the transaction DSSN.
	 *
	 * @return the transactionDSSN
	 */
	public OTTransactionDSSNTO getTransactionDSSN() {
		return transactionDSSN;
	}

	/**
	 * Gets the usages list.
	 *
	 * @return the usagesList
	 */
	public ArrayList<OTUsagesTO> getUsagesList() {
		return usagesList;
	}

	/**
	 * Gets the validity end date.
	 *
	 * @return the validityEndDate
	 */
	public GregorianCalendar getValidityEndDate() {
		return validityEndDate;
	}

	/**
	 * Gets the validity start date.
	 *
	 * @return the validityStartDate
	 */
	public GregorianCalendar getValidityStartDate() {
		return validityStartDate;
	}

	/**
	 * Gets the void code.
	 *
	 * @return the voidCode
	 */
	public Integer getVoidCode() {
		return voidCode;
	}

	/**
	 * Sets the alreadyused.
	 *
	 * @param alreadyused
	 *           the alreadyused to set
	 */
	public void setAlreadyused(Boolean alreadyused) {
		this.alreadyused = alreadyused;
	}

	/**
	 * Sets the biometric level.
	 *
	 * @param biometricLevel
	 *           the biometricLevel to set
	 */
	public void setBiometricLevel(Integer biometricLevel) {
		this.biometricLevel = biometricLevel;
	}

	/**
	 * Sets the group quantity.
	 *
	 * @param groupQuantity
	 *           the groupQuantity to set
	 */
	public void setGroupQuantity(Integer groupQuantity) {
		this.groupQuantity = groupQuantity;
	}

	/**
	 * Sets the item status.
	 *
	 * @param itemStatus
	 *           the itemStatus to set
	 */
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * Sets the price.
	 *
	 * @param price
	 *           the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Sets the remaining value.
	 *
	 * @param remainingValue
	 *           the remainingValue to set
	 */
	public void setRemainingValue(BigDecimal remainingValue) {
		this.remainingValue = remainingValue;
	}

	/**
	 * Sets the tax.
	 *
	 * @param tax
	 *           the tax to set
	 */
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	/**
	 * Sets the tax exempt.
	 *
	 * @param taxExempt
	 *           the taxExempt to set
	 */
	public void setTaxExempt(Boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	/**
	 * Sets the ticket attribute.
	 *
	 * @param ticketAttribute
	 *           the ticketAttribute to set
	 */
	public void setTicketAttribute(Integer ticketAttribute) {
		this.ticketAttribute = ticketAttribute;
	}

	/**
	 * Sets the ticket flag list.
	 *
	 * @param ticketFlagList
	 *           the ticketFlagList to set
	 */
	public void setTicketFlagList(ArrayList<Integer> ticketFlagList) {
		this.ticketFlagList = ticketFlagList;
	}

	/**
	 * Sets the ticket note.
	 *
	 * @param ticketNote
	 *           the ticketNote to set
	 */
	public void setTicketNote(String ticketNote) {
		this.ticketNote = ticketNote;
	}

	/**
	 * Sets the ticket type.
	 *
	 * @param ticketType
	 *           the ticketType to set
	 */
	public void setTicketType(BigInteger ticketType) {
		this.ticketType = ticketType;
	}

	/**
	 * Sets the transaction COD.
	 *
	 * @param transactionCOD
	 *           the transactionCOD to set
	 */
	public void setTransactionCOD(String transactionCOD) {
		this.transactionCOD = transactionCOD;
	}

	/**
	 * Sets the transaction DSSN.
	 *
	 * @param transactionDSSN
	 *           the transactionDSSN to set
	 */
	public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
		this.transactionDSSN = transactionDSSN;
	}

	/**
	 * Sets the usages list.
	 *
	 * @param usagesList
	 *           the usagesList to set
	 */
	public void setUsagesList(ArrayList<OTUsagesTO> usagesList) {
		this.usagesList = usagesList;
	}

	/**
	 * Sets the validity end date.
	 *
	 * @param validityEndDate
	 *           the validityEndDate to set
	 */
	public void setValidityEndDate(GregorianCalendar validityEndDate) {
		this.validityEndDate = validityEndDate;
	}

	/**
	 * Allows the caller to set the validity end date as a string, with the
	 * format yy-MM-dd (such as 09-01-31).
	 * 
	 * @param validityEndDate
	 *           the string value of the validity end date to be set.
	 * @throws ParseException
	 *            if the string cannot be parsed.
	 */
	public void setValidityEndDate(String validityEndDate) throws ParseException {

		this.validityEndDate = OTCommandTO.convertOmniYYDate(validityEndDate);

		return;
	}

	/**
	 * Sets the validity start date.
	 *
	 * @param validityStartDate
	 *           the validityStartDate to set
	 */
	public void setValidityStartDate(GregorianCalendar validityStartDate) {
		this.validityStartDate = validityStartDate;
	}

	/**
	 * Allows the caller to set the validity start date as a string, with the
	 * format yy-MM-dd (such as 09-01-31).
	 *
	 * @param validityStartDate
	 *           the new validity start date
	 * @throws ParseException
	 *            if the string cannot be parsed.
	 */
	public void setValidityStartDate(String validityStartDate) throws ParseException {

		this.validityStartDate = OTCommandTO.convertOmniYYDate(validityStartDate);

		return;
	}

	/**
	 * Sets the void code.
	 *
	 * @param voidCode
	 *           the voidCode to set
	 */
	public void setVoidCode(Integer voidCode) {
		this.voidCode = voidCode;
	}

	/**
	 * Gets the ticket.
	 *
	 * @return the ticket
	 */
	public OTTicketTO getTicket() {
		return ticket;
	}

	/**
	 * Sets the ticket.
	 *
	 * @param ticket
	 *           the ticket to set.
	 */
	public void setTicket(OTTicketTO ticket) {
		this.ticket = ticket;
	}

	/**
	 * Allows the caller to set the already used flag using the standard Omni
	 * string values.
	 * 
	 * @param flag
	 *           the string version of the already used flag to set.
	 */
	public void setAlreadyused(String flag) {
		if (flag.compareTo(TRUESTRINGVALUE) == 0)
			alreadyused = new Boolean(true);
		else
			alreadyused = new Boolean(false);
	}

	/**
	 * Gets the zip code.
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
	 *           the ZIP code to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * Allows the caller to set the denyMultiEntitlementFunctions flag using the
	 * standard Omni string values.
	 * 
	 * @param flag
	 *           the string version of the flag to set.
	 */
	public void setDenyMultiEntitlementFunctions(String flag) {
		this.denyMultiEntitlementFunctions = new Boolean(flag.compareTo(TRUESTRINGVALUE) == 0);
	}

	/**
	 * Sets the deny multi entitlement functions.
	 *
	 * @param denyMultiEntitlementFunctions
	 *           the denyMultiEntitlementFunctions to set
	 */
	public void setDenyMultiEntitlementFunctions(Boolean denyMultiEntitlementFunctions) {
		this.denyMultiEntitlementFunctions = denyMultiEntitlementFunctions;
	}

	/**
	 * Gets the deny multi entitlement functions.
	 *
	 * @return the denyMultiEntitlementFunctions
	 */
	public Boolean getDenyMultiEntitlementFunctions() {
		return denyMultiEntitlementFunctions;
	}

	/**
	 * Gets the account id.
	 *
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Sets the account id.
	 *
	 * @param accountId
	 *           the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * Gets the season pass demo.
	 *
	 * @return the seasonPassDemo
	 */
	public OTDemographicData getSeasonPassDemo() {
		return seasonPassDemo;
	}

	/**
	 * Sets the season pass demo.
	 *
	 * @param seasonPassDemo
	 *           the seasonPassDemo to set
	 */
	public void setSeasonPassDemo(OTDemographicData seasonPassDemo) {
		this.seasonPassDemo = seasonPassDemo;
	}

	/**
	 * Gets the item num code.
	 *
	 * @return the itemNumCode
	 */
	public BigInteger getItemNumCode() {
		return itemNumCode;
	}

	/**
	 * Sets the item num code.
	 *
	 * @param itemNumCode
	 *           the itemNumCode to set
	 */
	public void setItemNumCode(BigInteger itemNumCode) {
		this.itemNumCode = itemNumCode;
	}

	/**
	 * Gets the item alpha code.
	 *
	 * @return the itemAlphaCode
	 */
	public String getItemAlphaCode() {
		return itemAlphaCode;
	}

	/**
	 * Sets the item alpha code.
	 *
	 * @param itemAlphaCode
	 *           the itemAlphaCode to set
	 */
	public void setItemAlphaCode(String itemAlphaCode) {
		this.itemAlphaCode = itemAlphaCode;
	}

	/**
	 * Gets the biometric template.
	 *
	 * @return biometricTemplate
	 */
	public String getBiometricTemplate() {
		return biometricTemplate;
	}

	/**
	 * Sets the biometric template.
	 *
	 * @param biometricTemplate
	 *           the new biometric template
	 */
	public void setBiometricTemplate(String biometricTemplate) {
		this.biometricTemplate = biometricTemplate;
	}

	/**
	 * Gets the pay plan.
	 *
	 * @return the payPlan
	 */
	public String getPayPlan() {
		return payPlan;
	}

	/**
	 * Sets the pay plan.
	 *
	 * @param payPlan
	 *           the payPlan to set
	 */
	public void setPayPlan(String payPlan) {
		this.payPlan = payPlan;
	}

	/**
	 * @return the upgradePLUList
	 */
	public ArrayList<OTUpgradePLU> getUpgradePLUList() {
		return upgradePLUList;
	}

	/**
	 * @param upgradePLUList
	 *           the upgradePLUList to set
	 */
	public void setUpgradePLUList(ArrayList<OTUpgradePLU> upgradePLUList) {
		this.upgradePLUList = upgradePLUList;
	}
	
	public void addUpgradePLUList(OTUpgradePLU oTUpgradePLU){
		upgradePLUList.add(oTUpgradePLU);
	}

}
