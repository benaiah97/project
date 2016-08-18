package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;

/**
 * This class is a transfer object which represents the Omni ticket update transaction.
 * 
 * @author lewit019
 */
public class OTUpdateTransactionTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** TransactionDSSN */
	private OTTransactionDSSNTO transactionDSSN;

	/** TransactionCOD */
	private String transactionCOD;

	/** TransactionNote */
	private String transactionNote;

	/** Seller */
	private String seller;

	/** ZipCode */
	private String zipCode;

	/** IATA */
	private String iata;

	/** The in txn attr. */
	private ArrayList<OTInTransactionAttributeTO> inTxnAttrList = new ArrayList<OTInTransactionAttributeTO>();

	/** OT Error Info */
	private OTErrorTO error;

	/** Item */
	private BigInteger item;

	/** The site number. */
	private Integer siteNumber;

	/**
	 * 
	 * @return the site number
	 */
	public Integer getSiteNumber() {
		return siteNumber;
	}

	/**
	 * Sets the site number.
	 * 
	 * @param siteNumber
	 *            the site number to set.
	 */
	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	public String getIata() {
		return iata;
	}

	/**
	 * Sets the International Air Travel Association number.
	 * 
	 * @param iata
	 *            the iata to set.
	 */
	public void setIata(String iata) {
		this.iata = iata;
	}

	/**
	 * 
	 * @return the Omni Ticket In-Transaction Attribute Tranfer Object array list.
	 */
	public ArrayList<OTInTransactionAttributeTO> getInTxnAttrList() {
		return inTxnAttrList;
	}

	/**
	 * Sets the Omni Ticket In-Transaction Attribute Tranfer Object array list.
	 * 
	 * @param inTxnAttrList
	 *            the array list to set.
	 */
	public void setInTxnAttrList(
			ArrayList<OTInTransactionAttributeTO> inTxnAttrList) {
		this.inTxnAttrList = inTxnAttrList;
	}

	/**
	 * 
	 * @return the seller ID string.
	 */
	public String getSeller() {
		return seller;
	}

	/**
	 * Sets the seller ID string.
	 * 
	 * @param seller
	 *            the seller ID string to set.
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}

	/**
	 * 
	 * @return the transaction code.
	 */
	public String getTransactionCOD() {
		return transactionCOD;
	}

	/**
	 * Sets the transaction code.
	 * 
	 * @param transactionCOD
	 *            the transaction code to set.
	 */
	public void setTransactionCOD(String transactionCOD) {
		this.transactionCOD = transactionCOD;
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
	 *            the txn dssn to set
	 */
	public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
		this.transactionDSSN = transactionDSSN;
	}

	/**
	 * 
	 * @return the transaction note.
	 */
	public String getTransactionNote() {
		return transactionNote;
	}

	/**
	 * Sets the transaction note.
	 * 
	 * @param transactionNote
	 *            the transaction note to set.
	 */
	public void setTransactionNote(String transactionNote) {
		this.transactionNote = transactionNote;
	}

	/**
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
	 *            the ZIP code to set.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 
	 * @return the error object for this transaction.
	 */
	public OTErrorTO getError() {
		return error;
	}

	/**
	 * Sets the error object for this transaction.
	 * 
	 * @param error
	 *            the error to set.
	 */
	public void setError(OTErrorTO error) {
		this.error = error;
	}

	/**
	 * 
	 * @return the item number.
	 */
	public BigInteger getItem() {
		return item;
	}

	/**
	 * Sets the item number.
	 * 
	 * @param item
	 *            the item number to set.
	 */
	public void setItem(BigInteger item) {
		this.item = item;
	}

}
