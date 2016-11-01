package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * DTI Data Transfer Object for MultiEntitlementAccount - CreateEntitlement (Request)
 * 
 * 2.16.1
 * 
 * @author BIEST001
 * 
 */
public class TickerateEntitlementRequestTO extends CommandBodyTO implements Serializable {

	/**
	 * Auto-generated Serial Number
	 */
	private static final long serialVersionUID = -4562423997806008890L;

	// *************************************
	// TICKETS TO MAKE ELECTRONIC

	private ArrayList<TicketTO> tickets = null;

	// *************************************
	// NEW ACCOUNT

	private String newExternalReferenceType = null;
	private String newExternalReferenceValue = null;

	// *************************************
	// EXISTING ACCOUNT - SEARCH CRITERIA

	// Existing Media ID from a magic band
	private String existingMediaId = null;

	// MyMagic Account Id in base ticketing system (aka ATS Link Id)
	private String accountId = null;

	// entitlement data to use for the search
	private TicketTO ticket = new TicketTO();

	// *************************************
	// MEDIA TO BE ASSOCIATED

	// array of mediaData
	private ArrayList<NewMediaDataTO> mediaData = null;

	// *************************************
	// DATA ATTRIBUTES/PROPERTIES - GETS/SETS

	public ArrayList<TicketTO> getTickets() {
		return tickets;
	}

	public void setTickets(ArrayList<TicketTO> tickets) {
		this.tickets = tickets;
	}

	public String getNewExternalReferenceType() {
		return newExternalReferenceType;
	}

	public void setNewExternalReferenceType(String newExternalReferenceType) {
		this.newExternalReferenceType = newExternalReferenceType;
	}

	public String getNewExternalReferenceValue() {
		return newExternalReferenceValue;
	}

	public void setNewExternalReferenceValue(String newExternalReferenceValue) {
		this.newExternalReferenceValue = newExternalReferenceValue;
	}

	public String getExistingMediaId() {
		return existingMediaId;
	}

	public void setExistingMediaId(String existingMediaId) {
		this.existingMediaId = existingMediaId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public TicketTO getTicket() {
		return ticket;
	}

	public void setTicket(TicketTO ticket) {
		this.ticket = ticket;
	}

	public ArrayList<NewMediaDataTO> getMediaData() {
		return mediaData;
	}

	public void setMediaData(ArrayList<NewMediaDataTO> mediaData) {
		this.mediaData = mediaData;
	}

}