package pvt.disney.dti.gateway.provider.dlr.data;

import java.util.ArrayList;

/**
 * Represents an individual OrderLine in a Order
 * 
 * @author smoon
 * 
 */
public class GWOrderLineTO {

	/** detail type */
	private String detailType;

	/** plu - detail type 1 and 8 */
	private String plu;
	/** description - detail type 1, 2, and 8; for type 2 contains card type */
	private String description;
	/** tax code - detail type 1 */
	private String taxCode;
	/** quantity - detail type 1 and 8 */
	private String qty;
	/** amount - detail type 1, 2, and 8 */
	private String amount;
	/** total - detail type 1, 2, 8 */
	private String total;
	/** payment Code - detail type 2 */
	private String paymentCode;
	/** payment date - detail type 2 */
	private String paymentDate;
	/** endorsement - detail type 2, contains card number */
	private String endorsement;
	/** billingZip - detail type 2 */
	private String billingZip;
	/** billingStreet - detail type 2, (as of 2.16.2, JTL) */
  private String billingStreet;
	/** expDate - detail type 2 */
	private String expDate;
	/** cvn - detail type 2 */
	private String cvn;
	/** PaymentPlanID - detail type 8 */
	private String paymentPlanID;
	
	/** pass members */
	private ArrayList<GWMemberDemographicsTO> memberList = new ArrayList<GWMemberDemographicsTO>();

	/** ticket date, used by meeting/convention tickets */
	private String ticketDate;

	/**
	 * @return the ticketDate
	 */
	public String getTicketDate() {
		return ticketDate;
	}

	/**
	 * @param ticketDate
	 *            the ticketDate to set
	 */
	public void setTicketDate(String ticketDate) {
		this.ticketDate = ticketDate;
	}

	/**
	 * @return the billingZip
	 */
	public String getBillingZip() {
		return billingZip;
	}

	/**
	 * @param billingZip
	 *            the billingZip to set
	 */
	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	/**
	 * @return the cvn
	 */
	public String getCvn() {
		return cvn;
	}

	/**
	 * @param cvn
	 *            the cvn to set
	 */
	public void setCvn(String cvn) {
		this.cvn = cvn;
	}

	/**
	 * @return the endorsement
	 */
	public String getEndorsement() {
		return endorsement;
	}

	/**
	 * @param endorsement
	 *            the endorsement to set
	 */
	public void setEndorsement(String endorsement) {
		this.endorsement = endorsement;
	}

	/**
	 * @return the expDate
	 */
	public String getExpDate() {
		return expDate;
	}

	/**
	 * @param expDate
	 *            the expDate to set
	 */
	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	/**
	 * @return the paymentCode
	 */
	public String getPaymentCode() {
		return paymentCode;
	}

	/**
	 * @param paymentCode
	 *            the paymentCode to set
	 */
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	/**
	 * @return the paymentDate
	 */
	public String getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate
	 *            the paymentDate to set
	 */
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the detailType
	 */
	public String getDetailType() {
		return detailType;
	}

	/**
	 * @param detailType
	 *            the detailType to set
	 */
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	/**
	 * @return the plu
	 */
	public String getPlu() {
		return plu;
	}

	/**
	 * @param plu
	 *            the plu to set
	 */
	public void setPlu(String plu) {
		this.plu = plu;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the taxCode
	 */
	public String getTaxCode() {
		return taxCode;
	}

	/**
	 * @param taxCode
	 *            the taxCode to set
	 */
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty
	 *            the qty to set
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}

	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the memberList
	 */
	public ArrayList<GWMemberDemographicsTO> getMemberList() {
		return memberList;
	}

	/**
	 * @param memberList
	 *            the memberList to set
	 */
	public void setMemberList(ArrayList<GWMemberDemographicsTO> memberList) {
		this.memberList = memberList;
	}

	/**
	 * 
	 * @param aMember
	 */
	public void addMember(GWMemberDemographicsTO aMember) {
		this.memberList.add(aMember);
	}

	/**
	 * @return the paymentPlanID
	 */
	public String getPaymentPlanID() {
		return paymentPlanID;
	}

	/**
	 * @param paymentPlanID
	 *            the paymentPlanID to set
	 */
	public void setPaymentPlanID(String paymentPlanID) {
		this.paymentPlanID = paymentPlanID;
	}

  /**
   * @return the billingStreet
   */
  public String getBillingStreet() {
    return billingStreet;
  }

  /**
   * @param billingStreet the billingStreet to set
   */
  public void setBillingStreet(String billingStreet) {
    this.billingStreet = billingStreet;
  }

}
