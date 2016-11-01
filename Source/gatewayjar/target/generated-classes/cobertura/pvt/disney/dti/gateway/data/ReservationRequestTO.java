package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.ExtTxnIdentifierTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the request parameters of a Reservation Request.
 * 
 * @author lewit019
 * 
 */
public class ReservationRequestTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** Request Type */
	private String requestType;

	/** An array list of tickets (or products, in this case). */
	private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/** APPassInfo */
	private String aPPassInfo;

	/** Payment */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/** Eligibility */
	private String eligibilityGroup;
	private String eligibilityMember;

	/** Reservation */
	private ReservationTO reservation;

	/** ClientData */
	private ClientDataTO clientData;

	/** Agency */
	private AgencyTO agency;

	/** The tax exempt code. */
	private String taxExemptCode;

	/** Note */
	private ArrayList<String> noteList = new ArrayList<String>();

	/**
	 * Optional Default Entitlement Account per ticket or per order. If Default account is filled, no specified accounts will be accepted
	 */
	private String defaultAccount;

	/**
	 * Specified accounts, if defaultAccount is not filled, specific accounts can be listed.
	 */
	private ArrayList<SpecifiedAccountTO> specifiedAccounts = new ArrayList<SpecifiedAccountTO>();

	/**
	 * Identifies this as an installment reservation request (one which includes installment payments. This flag is set when the installment payment amount is validated. {@link ProductRules.validateInstallmentDownpayment() }
	 */
	private boolean isInstallmentResRequest = false;

	/**
	 * Identifies the amount of the down-payment. This value is set when the installment payment amount is verified. It is used to populate values in the out-bound transaction to eGalaxy. {@link ProductRules.validateInstallmentDownpayment()
	 * }
	 */
	private BigDecimal installmentDownpayment = null;

	/**
	 * The total order amount (as summarized by the business rules). {@link BusinessRules.applyReservationRules() }
	 */
	private BigDecimal totalOrderAmount;
	
	
	/**
	 * External transaction identifier (as of 2.16.2, JTL)
	 */
	private ExtTxnIdentifierTO extTxnIdentifier = null;

	/**
	 * @return the agency
	 */
	public AgencyTO getAgency() {
		return agency;
	}

	/**
	 * @return the aPPassInfo
	 */
	public String getAPPassInfo() {
		return aPPassInfo;
	}

	/**
	 * @return the clientData
	 */
	public ClientDataTO getClientData() {
		return clientData;
	}

	/**
	 * @return the noteList
	 */
	public ArrayList<String> getNoteList() {
		return noteList;
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @return the reservation
	 */
	public ReservationTO getReservation() {
		return reservation;
	}

	/**
	 * @param agency
	 *            the agency to set
	 */
	public void setAgency(AgencyTO agency) {
		this.agency = agency;
	}

	/**
	 * @param passInfo
	 *            the aPPassInfo to set
	 */
	public void setAPPassInfo(String passInfo) {
		aPPassInfo = passInfo;
	}

	/**
	 * @param clientData
	 *            the clientData to set
	 */
	public void setClientData(ClientDataTO clientData) {
		this.clientData = clientData;
	}

	/**
	 * @param noteList
	 *            the noteList to set
	 */
	public void setNoteList(ArrayList<String> noteList) {
		this.noteList = noteList;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @param reservation
	 *            the reservation to set
	 */
	public void setReservation(ReservationTO reservation) {
		this.reservation = reservation;
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
	public void setTktList(ArrayList<TicketTO> ticketList) {
		this.tktList = ticketList;
	}

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
	 * @return the taxExemptCode
	 */
	public String getTaxExemptCode() {
		return taxExemptCode;
	}

	/**
	 * @param taxExemptCode
	 *            the taxExemptCode to set
	 */
	public void setTaxExemptCode(String taxExemptCode) {
		this.taxExemptCode = taxExemptCode;
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
	 * @return the isInstallmentResRequest
	 */
	public boolean isInstallmentResRequest() {
		return isInstallmentResRequest;
	}

	/**
	 * @param isInstallmentResRequest
	 *            the isInstallmentResRequest to set
	 */
	public void setInstallmentResRequest(boolean isInstallmentResRequest) {
		this.isInstallmentResRequest = isInstallmentResRequest;
	}

	/**
	 * @return the totalOrderAmount
	 */
	public BigDecimal getTotalOrderAmount() {
		return totalOrderAmount;
	}

	/**
	 * @param totalOrderAmount
	 *            the totalOrderAmount to set
	 */
	public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
		this.totalOrderAmount = totalOrderAmount;
	}

	/**
	 * @return the installmentDownpayment
	 */
	public BigDecimal getInstallmentDownpayment() {
		return installmentDownpayment;
	}

	/**
	 * @param installmentDownpayment
	 *            the installmentDownpayment to set
	 */
	public void setInstallmentDownpayment(BigDecimal installmentDownpayment) {
		this.installmentDownpayment = installmentDownpayment;
	}

  /**
   * @return the extTxnIdentifier
   */
  public ExtTxnIdentifierTO getExtTxnIdentifier() {
    return extTxnIdentifier;
  }

  /**
   * @param extTxnIdentifier the extTxnIdentifier to set
   */
  public void setExtTxnIdentifier(ExtTxnIdentifierTO extTxnIdentifier) {
    this.extTxnIdentifier = extTxnIdentifier;
  }

}
