package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;

/**
 * This class is a transfer object which represents the Omni Ticket upgrade ticket.
 * 
 * @author lewit019
 */
public class OTUpgradeTicketTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	// Out-bound fields
	/** The tagsList. */
	private ArrayList<String> tagsList = new ArrayList<String>();

	/** The site number. */
	private Integer siteNumber;

	/** The upgrade ticket info list. */
	private ArrayList<OTUpgradeTicketInfoTO> upgradeTicketInfoList = new ArrayList<OTUpgradeTicketInfoTO>();

	/** The payment info. */
	private ArrayList<OTPaymentTO> paymentInfoList = new ArrayList<OTPaymentTO>();

	/** The seller id. */
	private BigInteger sellerId;

	/** The association id. */
	private Integer associationId;

	/** The member id. */
	private Integer memberId;

	/** The member field. */
	private String memberField;

	/** The i ata. */
	private String iATA;

	/** The transaction note. */
	private String transactionNote;

	/** The in txn attr. */
	private ArrayList<OTInTransactionAttributeTO> inTxnAttrList = new ArrayList<OTInTransactionAttributeTO>();

	/** The deny upgrade on upgrade. */
	private Boolean denyUpgradeOnUpgrade;

	// In-bound fields
	/** The error. */
	private OTErrorTO error;

	/** The transaction dssn. */
	private OTTransactionDSSNTO transactionDSSN;

	/** The transaction cod. */
	private String transactionCOD;

	/** The total amount. */
	private BigDecimal totalAmount;

	/** The total tax. */
	private BigDecimal totalTax;

	/** The tax exempt. */
	private Boolean taxExempt;

	/** If transactions generates an event; xsd defaults to false */
	private Boolean generateEvent;

	/**
	 * Gets the association id.
	 * 
	 * @return the associationId
	 */
	public Integer getAssociationId() {
		return associationId;
	}

	/**
	 * Gets the deny upgrade on upgrade.
	 * 
	 * @return the denyUpgradeOnUpgrade
	 */
	public Boolean getDenyUpgradeOnUpgrade() {
		return denyUpgradeOnUpgrade;
	}

	/**
	 * Gets the error.
	 * 
	 * @return the error
	 */
	public OTErrorTO getError() {
		return error;
	}

	/**
	 * Gets the iata.
	 * 
	 * @return the iATA
	 */
	public String getIATA() {
		return iATA;
	}

	/**
	 * Gets the in txn attr.
	 * 
	 * @return the inTxnAttrList
	 */
	public ArrayList<OTInTransactionAttributeTO> getInTxnAttrList() {
		return inTxnAttrList;
	}

	/**
	 * Gets the member field.
	 * 
	 * @return the memberField
	 */
	public String getMemberField() {
		return memberField;
	}

	/**
	 * Gets the member id.
	 * 
	 * @return the memberId
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * Gets the payment info.
	 * 
	 * @return the paymentInfoList
	 */
	public ArrayList<OTPaymentTO> getPaymentInfoList() {
		return paymentInfoList;
	}

	/**
	 * Gets the seller id.
	 * 
	 * @return the sellerId
	 */
	public BigInteger getSellerId() {
		return sellerId;
	}

	/**
	 * Gets the site number.
	 * 
	 * @return the siteNumber
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Gets the tagsList.
	 * 
	 * @return the tagsList
	 */
	public ArrayList<String> getTagsList() {
		return tagsList;
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
	 * Gets the total amount.
	 * 
	 * @return the totalAmount
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Gets the total tax.
	 * 
	 * @return the totalTax
	 */
	public BigDecimal getTotalTax() {
		return totalTax;
	}

	/**
	 * Gets the transaction cod.
	 * 
	 * @return the transactionCOD
	 */
	public String getTransactionCOD() {
		return transactionCOD;
	}

	/**
	 * Gets the transaction dssn.
	 * 
	 * @return the transactionDSSN
	 */
	public OTTransactionDSSNTO getTransactionDSSN() {
		return transactionDSSN;
	}

	/**
	 * Gets the transaction note.
	 * 
	 * @return the transactionNote
	 */
	public String getTransactionNote() {
		return transactionNote;
	}

	/**
	 * Gets the upgrade ticket info list.
	 * 
	 * @return the upgradeTicketInfoList
	 */
	public ArrayList<OTUpgradeTicketInfoTO> getUpgradeTicketInfoList() {
		return upgradeTicketInfoList;
	}

	/**
	 * Sets the association id.
	 * 
	 * @param associationId
	 *            the associationId to set
	 */
	public void setAssociationId(Integer associationId) {
		this.associationId = associationId;
	}

	/**
	 * Sets the deny upgrade on upgrade.
	 * 
	 * @param denyUpgradeOnUpgrade
	 *            the denyUpgradeOnUpgrade to set
	 */
	public void setDenyUpgradeOnUpgrade(Boolean denyUpgradeOnUpgrade) {
		this.denyUpgradeOnUpgrade = denyUpgradeOnUpgrade;
	}

	/**
	 * Sets the error.
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(OTErrorTO error) {
		this.error = error;
	}

	/**
	 * Sets the iata.
	 * 
	 * @param iata
	 *            the iATA to set
	 */
	public void setIATA(String iata) {
		iATA = iata;
	}

	/**
	 * Sets the in txn attr.
	 * 
	 * @param inTxnAttrList
	 *            the inTxnAttrList to set
	 */
	public void setInTxnAttrList(ArrayList<OTInTransactionAttributeTO> inTxnAttr) {
		this.inTxnAttrList = inTxnAttr;
	}

	/**
	 * Sets the member field.
	 * 
	 * @param memberField
	 *            the memberField to set
	 */
	public void setMemberField(String memberField) {
		this.memberField = memberField;
	}

	/**
	 * Sets the member id.
	 * 
	 * @param memberId
	 *            the memberId to set
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * Sets the payment info.
	 * 
	 * @param paymentInfoList
	 *            the paymentInfoList to set
	 */
	public void setPaymentInfoList(ArrayList<OTPaymentTO> paymentInfo) {
		this.paymentInfoList = paymentInfo;
	}

	/**
	 * Sets the seller id.
	 * 
	 * @param sellerId
	 *            the sellerId to set
	 */
	public void setSellerId(BigInteger sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * Sets the site number.
	 * 
	 * @param siteNumber
	 *            the siteNumber to set
	 */
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	/**
	 * Sets the tagsList.
	 * 
	 * @param tagsList
	 *            the tagsList to set
	 */
	public void setTagsList(ArrayList<String> tags) {
		this.tagsList = tags;
	}

	/**
	 * Sets the tax exempt.
	 * 
	 * @param taxExempt
	 *            the taxExempt to set
	 */
	public void setTaxExempt(Boolean taxExempt) {
		this.taxExempt = taxExempt;
	}

	/**
	 * Sets the total amount.
	 * 
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * Sets the total tax.
	 * 
	 * @param totalTax
	 *            the totalTax to set
	 */
	public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	/**
	 * Sets the transaction cod.
	 * 
	 * @param transactionCOD
	 *            the transactionCOD to set
	 */
	public void setTransactionCOD(String transactionCOD) {
		this.transactionCOD = transactionCOD;
	}

	/**
	 * Sets the transaction dssn.
	 * 
	 * @param transactionDSSN
	 *            the transactionDSSN to set
	 */
	public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
		this.transactionDSSN = transactionDSSN;
	}

	/**
	 * Sets the transaction note.
	 * 
	 * @param transactionNote
	 *            the transactionNote to set
	 */
	public void setTransactionNote(String transactionNote) {
		this.transactionNote = transactionNote;
	}

	/**
	 * Sets the upgrade ticket info list.
	 * 
	 * @param upgradeTicketInfoList
	 *            the upgradeTicketInfoList to set
	 */
	public void setUpgradeTicketInfoList(
			ArrayList<OTUpgradeTicketInfoTO> upgradeTicketInfoList) {
		this.upgradeTicketInfoList = upgradeTicketInfoList;
	}

	/**
	 * @param generateEvent
	 *            the generateEvent to set
	 */
	public void setGenerateEvent(Boolean generateEvent) {
		this.generateEvent = generateEvent;
	}

	/**
	 * @return the generateEvent
	 */
	public Boolean getGenerateEvent() {
		return generateEvent;
	}

}
