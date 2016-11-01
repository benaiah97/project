package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Value/Transfer class encapsulates the values in a create ticket request.
 * 
 * @author lewit019
 * 
 */
public class CreateTicketRequestTO extends CommandBodyTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = 9129227490L;

	/** An array list of tickets (or products, in this case). */
	protected ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/** Payment */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/** Eligibility */
	private String eligibilityGroup;
	private String eligibilityMember;

	/**
	 * Optional Default Entitlement Account per ticket or per order. If Default account is filled, no specified accounts will be accepted
	 */
	private String defaultAccount;

	/**
	 * Specified accounts, if defaultAccount is not filled, specific accounts can be listed.
	 */
	private ArrayList<SpecifiedAccountTO> specifiedAccounts = new ArrayList<SpecifiedAccountTO>();

	/**
	 * Audit Notation (as of 2.12). When the transaction note in ATS needs to be used or a reason for a failure recorded,
	 */
	private String auditNotation;

	/**
	 * @return the eligibilityGroup
	 */
	public String getEligibilityGroup() {
		return eligibilityGroup;
	}

	/**
	 * @return the eligibilityMember
	 */
	public String getEligibilityMember() {
		return eligibilityMember;
	}

	/**
	 * @param eligibilityGroup
	 *            the eligibilityGroup to set
	 */
	public void setEligibilityGroup(String eligibilityGroup) {
		this.eligibilityGroup = eligibilityGroup;
	}

	/**
	 * @param eligibilityMember
	 *            the eligibilityMember to set
	 */
	public void setEligibilityMember(String eligibilityMember) {
		this.eligibilityMember = eligibilityMember;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTktList() {
		return tktList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.tktList = ticketList;
	}

	/**
	 * @param defaultAccount
	 *            the defaultAccount to set
	 */
	public void setDefaultAccount(String defaultAccount) {
		this.defaultAccount = defaultAccount;
	}

	/**
	 * @return the defaultAccount
	 */
	public String getDefaultAccount() {
		return defaultAccount;
	}

	/**
	 * @return the specifiedAccounts
	 */
	public ArrayList<SpecifiedAccountTO> getSpecifiedAccounts() {
		return specifiedAccounts;
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @return the auditNotation
	 */
	public String getAuditNotation() {
		return auditNotation;
	}

	/**
	 * @param auditNotation
	 *            the auditNotation to set
	 */
	public void setAuditNotation(String auditNotation) {
		this.auditNotation = auditNotation;
	}

}
