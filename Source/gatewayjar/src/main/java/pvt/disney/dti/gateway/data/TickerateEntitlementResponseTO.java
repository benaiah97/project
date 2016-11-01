package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * DTI Data Transfer Object for MultiEntitlementAccount - CreateEntitlement (Response)
 * 
 * 2.16.1
 * 
 * @author BIEST001
 * 
 */
public class TickerateEntitlementResponseTO extends CommandBodyTO implements Serializable {

	/**
	 * Auto-generated Serial Number
	 */
	private static final long serialVersionUID = 8723576692121016294L;

	// *************************************
	// NEW ACCOUNT

	private String accountId = null;

	private ArrayList<TicketTO> tickets = new ArrayList<TicketTO>();

	private ArrayList<NewMediaDataTO> mediaData = new ArrayList<NewMediaDataTO>();

	private Integer ticketsOnAccount = 0;

	private Integer mediaOnAccount = 0;

	public String getAccountId() {
		return accountId;
	}

	// *************************************
	// DATA ATTRIBUTES/PROPERTIES - GETS/SETS

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ArrayList<TicketTO> getTickets() {
		return tickets;
	}

	public void setTickets(ArrayList<TicketTO> tickets) {
		this.tickets = tickets;
	}

	public ArrayList<NewMediaDataTO> getMediaData() {
		return mediaData;
	}

	public void setMediaData(ArrayList<NewMediaDataTO> mediaData) {
		this.mediaData = mediaData;
	}

	public Integer getTicketsOnAccount() {
		return ticketsOnAccount;
	}

	public void setTicketsOnAccount(Integer ticketsOnAccount) {
		this.ticketsOnAccount = ticketsOnAccount;
	}

	public Integer getMediaOnAccount() {
		return mediaOnAccount;
	}

	public void setMediaOnAccount(Integer mediaOnAccount) {
		this.mediaOnAccount = mediaOnAccount;
	}

}