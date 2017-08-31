package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.TicketSearchModeTO;

/**
 * The Class DemographicsInfoTO.
 */
public class DemographicsInfoTO implements Serializable {

	/**  Serial Version UID. */
	  private static final long serialVersionUID = 8403710484L;
	  
	/** The unique code. Req and Resp */
	private int uniqueCode;
	
	/** The ticket search mode TO. */
	private TicketSearchModeTO ticketSearchModeTO;
	
	/** The account search filter TO. */
	private AccountSearchFilterTO accountSearchFilterTO;
	
	/** The match demographic data TO. */
	private MatchDemographicDataTO matchDemographicDataTO;
	
	/** The demographic data TO. */
	private DemographicDataTO demographicDataTO;
	
	/** The item status. */
	/* Reponse element */
	private int itemStatus;
	
	/** The demographic type. */
	private String demographicType;
	
	/** The client category. */
	private String clientCategory;
	
	/** The client language. */
	private String clientLanguage;
	
	/** The association id. */
	private String associationId;

	/**
	 * Gets the unique code.
	 *
	 * @return the unique code
	 */
	public int getUniqueCode() {
		return uniqueCode;
	}

	/**
	 * Sets the unique code.
	 *
	 * @param uniqueCode the new unique code
	 */
	public void setUniqueCode(int uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	/**
	 * Gets the ticket search mode TO.
	 *
	 * @return the ticket search mode TO
	 */
	public TicketSearchModeTO getTicketSearchModeTO() {
		return ticketSearchModeTO;
	}

	/**
	 * Sets the ticket search mode TO.
	 *
	 * @param ticketSearchModeTO the new ticket search mode TO
	 */
	public void setTicketSearchModeTO(TicketSearchModeTO ticketSearchModeTO) {
		this.ticketSearchModeTO = ticketSearchModeTO;
	}

	/**
	 * Gets the account search filter TO.
	 *
	 * @return the account search filter TO
	 */
	public AccountSearchFilterTO getAccountSearchFilterTO() {
		return accountSearchFilterTO;
	}

	/**
	 * Sets the account search filter TO.
	 *
	 * @param accountSearchFilterTO the new account search filter TO
	 */
	public void setAccountSearchFilterTO(AccountSearchFilterTO accountSearchFilterTO) {
		this.accountSearchFilterTO = accountSearchFilterTO;
	}

	/**
	 * Gets the match demographic data TO.
	 *
	 * @return the match demographic data TO
	 */
	public MatchDemographicDataTO getMatchDemographicDataTO() {
		return matchDemographicDataTO;
	}

	/**
	 * Sets the match demographic data TO.
	 *
	 * @param matchDemographicDataTO the new match demographic data TO
	 */
	public void setMatchDemographicDataTO(MatchDemographicDataTO matchDemographicDataTO) {
		this.matchDemographicDataTO = matchDemographicDataTO;
	}

	/**
	 * Gets the demographic data TO.
	 *
	 * @return the demographic data TO
	 */
	public DemographicDataTO getDemographicDataTO() {
		return demographicDataTO;
	}

	/**
	 * Sets the demographic data TO.
	 *
	 * @param demographicDataTO the new demographic data TO
	 */
	public void setDemographicDataTO(DemographicDataTO demographicDataTO) {
		this.demographicDataTO = demographicDataTO;
	}
	
	/* response element accessors */

	/**
	 * Gets the item status.
	 *
	 * @return the item status
	 */
	public int getItemStatus() {
		return itemStatus;
	}

	/**
	 * Sets the item status.
	 *
	 * @param itemStatus the new item status
	 */
	public void setItemStatus(int itemStatus) {
		this.itemStatus = itemStatus;
	}

	/**
	 * Gets the demographic type.
	 *
	 * @return the demographic type
	 */
	public String getDemographicType() {
		return demographicType;
	}

	/**
	 * Sets the demographic type.
	 *
	 * @param demographicType the new demographic type
	 */
	public void setDemographicType(String demographicType) {
		this.demographicType = demographicType;
	}

	/**
	 * Gets the client category.
	 *
	 * @return the client category
	 */
	public String getClientCategory() {
		return clientCategory;
	}

	/**
	 * Sets the client category.
	 *
	 * @param clientCategory the new client category
	 */
	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}

	/**
	 * Gets the client language.
	 *
	 * @return the client language
	 */
	public String getClientLanguage() {
		return clientLanguage;
	}

	/**
	 * Sets the client language.
	 *
	 * @param clientLanguage the new client language
	 */
	public void setClientLanguage(String clientLanguage) {
		this.clientLanguage = clientLanguage;
	}

	/**
	 * Gets the association id.
	 *
	 * @return the association id
	 */
	public String getAssociationId() {
		return associationId;
	}

	/**
	 * Sets the association id.
	 *
	 * @param associationId the new association id
	 */
	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}
	
}
