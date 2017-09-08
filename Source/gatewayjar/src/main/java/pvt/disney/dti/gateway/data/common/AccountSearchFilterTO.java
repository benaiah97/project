package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountSearchFilterTO.
 */
public class AccountSearchFilterTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9374628384L;
	
	/** The existing media id. */
	private String existingMediaId;
	
	/** The account id. */
	private String accountId;
	
	/** The ticket search mode. */
	private String ticketSearchMode;

	/**
	 * Gets the existing media id.
	 *
	 * @return the existing media id
	 */
	public String getExistingMediaId() {
		return existingMediaId;
	}

	/**
	 * Sets the existing media id.
	 *
	 * @param existingMediaId the new existing media id
	 */
	public void setExistingMediaId(String existingMediaId) {
		this.existingMediaId = existingMediaId;
	}

	/**
	 * Gets the account id.
	 *
	 * @return the account id
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Sets the account id.
	 *
	 * @param accountId the new account id
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * Gets the ticket search mode.
	 *
	 * @return the ticket search mode
	 */
	public String getTicketSearchMode() {
		return ticketSearchMode;
	}

	/**
	 * Sets the ticket search mode.
	 *
	 * @param ticketSearchMode the new ticket search mode
	 */
	public void setTicketSearchMode(String ticketSearchMode) {
		this.ticketSearchMode = ticketSearchMode;
	}
	

}
