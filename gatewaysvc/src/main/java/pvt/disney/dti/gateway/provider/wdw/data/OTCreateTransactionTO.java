package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTAccountDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;

/**
 * This class is a transfer object which represents the Omni ticket create transaction.
 * 
 * @author lewit019
 */
public class OTCreateTransactionTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The tagsList. */
	private ArrayList<String> tagsList = new ArrayList<String>();

	/** The site number. */
	private Integer siteNumber;

	/** Product Information. */
	private ArrayList<OTProductTO> productList = new ArrayList<OTProductTO>();

	/** Payment Information. */
	private ArrayList<OTPaymentTO> paymentInfoList = new ArrayList<OTPaymentTO>();

	/** The in txn attr. */
	private ArrayList<OTInTransactionAttributeTO> inTxnAttrList = new ArrayList<OTInTransactionAttributeTO>();

	/** TicketInfo. */
	private ArrayList<OTTicketInfoTO> tktInfoList = new ArrayList<OTTicketInfoTO>();

	/** TransactionDSSN. */
	private OTTransactionDSSNTO transactionDSSN;

	/** The transaction code. */
	private String transactionCOD;

	/** The association information for this order. */
	private OTAssociationInfoTO associationInfo;

	/** OT Error Info */
	private OTErrorTO error;

	/** The transaction note. (2.10) */
	private String transactionNote;

	/**
	 * Entitlement Account Creation Typology: SINGLEACCOUNT, PERTICKETACCOUNT, ENTITLEMENTACCOUNTID
	 */
	private String entitlementAccountCreationTypology;

	private ArrayList<OTAccountDataTO> accountsData = new ArrayList<OTAccountDataTO>();

	/** If transactions generates an event; xsd defaults to false */
	private Boolean generateEvent;

	/**
	 * 
	 * @return the error object.
	 */
	public OTErrorTO getError() {
		return error;
	}

	/**
	 * 
	 * @param error
	 *            object to set.
	 */
	public void setError(OTErrorTO error) {
		this.error = error;
	}

	/**
	 * 
	 * @return the in transction attribute transfer object array list.
	 */
	public ArrayList<OTInTransactionAttributeTO> getInTxnAttrList() {
		return inTxnAttrList;
	}

	/**
	 * Sets the in transaction attribute transfer object array list.
	 * 
	 * @param inTxnAttrList
	 *            the arraylist to set.
	 */
	public void setInTxnAttrList(
			ArrayList<OTInTransactionAttributeTO> inTxnAttrList) {
		this.inTxnAttrList = inTxnAttrList;
	}

	/**
	 * 
	 * @return the array list of Omni Ticket Payment Transfer Objects.
	 */
	public ArrayList<OTPaymentTO> getPaymentInfoList() {
		return paymentInfoList;
	}

	/**
	 * Sets the array list of Omni Ticket Payment Transfer Objects.
	 * 
	 * @param paymentInfoList
	 *            the array list to set.
	 */
	public void setPaymentInfoList(ArrayList<OTPaymentTO> paymentInfoList) {
		this.paymentInfoList = paymentInfoList;
	}

	/**
	 * 
	 * @return the array list of Omni Ticket Product Transfer Objects.
	 */
	public ArrayList<OTProductTO> getProductList() {
		return productList;
	}

	/**
	 * Sets the array list of Omni Ticket Product Transfer Objects.
	 * 
	 * @param productList
	 *            the array list to set.
	 */
	public void setProductList(ArrayList<OTProductTO> productList) {
		this.productList = productList;
	}

	/**
	 * 
	 * @return the site number.
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Sets the site number
	 * 
	 * @param siteNumber
	 *            the site number to set.
	 */
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	/**
	 * 
	 * @return the array list of tag strings.
	 */
	public ArrayList<String> getTagsList() {
		return tagsList;
	}

	/**
	 * Sets the array list of tag strings.
	 * 
	 * @param tagsList
	 *            the array list to set.
	 */
	public void setTagsList(ArrayList<String> tagsList) {
		this.tagsList = tagsList;
	}

	/**
	 * 
	 * @return the array list of Omni Ticket Ticket Info Transfer Objects.
	 */
	public ArrayList<OTTicketInfoTO> getTktInfoList() {
		return tktInfoList;
	}

	/**
	 * Sets the array list of Omni Ticket Ticket Info Transfer Objects.
	 * 
	 * @param tktInfoList
	 *            the array list to set.
	 */
	public void setTktInfoList(ArrayList<OTTicketInfoTO> tktInfoList) {
		this.tktInfoList = tktInfoList;
	}

	/**
	 * 
	 * @return the Omni Ticket Transaction DSSN Transfer Object.
	 */
	public OTTransactionDSSNTO getTransactionDSSN() {
		return transactionDSSN;
	}

	/**
	 * Sets the Omni Ticket Transaction DSSN Transfer Object.
	 * 
	 * @param transactionDSSN
	 *            the transaction DSSN to set.
	 */
	public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
		this.transactionDSSN = transactionDSSN;
	}

	public String getTransactionCOD() {
		return transactionCOD;
	}

	public void setTransactionCOD(String transactionCOD) {
		this.transactionCOD = transactionCOD;
	}

	/**
	 * @param entitlementAccountCreationTypology
	 *            the entitlementAccountCreationTypology to set
	 */
	public void setEntitlementAccountCreationTypology(
			String entitlementAccountCreationTypology) {
		this.entitlementAccountCreationTypology = entitlementAccountCreationTypology;
	}

	/**
	 * @return the entitlementAccountCreationTypology
	 */
	public String getEntitlementAccountCreationTypology() {
		return entitlementAccountCreationTypology;
	}

	/**
	 * @return the accountsData
	 */
	public ArrayList<OTAccountDataTO> getAccountsData() {
		return accountsData;
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

	/**
	 * @return the associationInfo
	 */
	public OTAssociationInfoTO getAssociationInfo() {
		return associationInfo;
	}

	/**
	 * @param associationInfo
	 *            the associationInfo to set
	 */
	public void setAssociationInfo(OTAssociationInfoTO associationInfo) {
		this.associationInfo = associationInfo;
	}

	/**
	 * @return the transactionNote
	 */
	public String getTransactionNote() {
		return transactionNote;
	}

	/**
	 * @param transactionNote
	 *            the transactionNote to set
	 */
	public void setTransactionNote(String transactionNote) {
		this.transactionNote = transactionNote;
	}

}
