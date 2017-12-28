package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;

/**
 * This class represents the Envelope portion of an eGalaxy XML.
 * 
 * @author lewit019
 * 
 */
public class GWEnvelopeTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	public enum GWTransactionType {
		QUERYTICKET,
		TICKETACTIVATION,
		ORDERS,
		QUERYORDER,
		UNDEFINED
	};

	/** The type of transaction this represents. */
	private GWTransactionType txnType = GWTransactionType.UNDEFINED;

	private GWHeaderTO headerTO = new GWHeaderTO();
	private GWBodyTO bodyTO = new GWBodyTO();
	
	/** The dti txn type. */
	private TransactionType dtiTxnType ;

	/**
	 * 
	 * @param txnTypeIn
	 */
	public GWEnvelopeTO(GWTransactionType txnTypeIn) {

		txnType = txnTypeIn;

	}

	/**
	 * @return the bodyTO
	 */
	public GWBodyTO getBodyTO() {
		return bodyTO;
	}

	/**
	 * @return the headerTO
	 */
	public GWHeaderTO getHeaderTO() {
		return headerTO;
	}

	/**
	 * @param bodyTO
	 *            the bodyTO to set
	 */
	public void setBodyTO(GWBodyTO bodyTO) {
		this.bodyTO = bodyTO;
	}

	/**
	 * @param headerTO
	 *            the headerTO to set
	 */
	public void setHeaderTO(GWHeaderTO headerTO) {
		this.headerTO = headerTO;
	}

	/**
	 * @return the txnType
	 */
	public GWTransactionType getTxnType() {
		return txnType;
	}

	/**
	 * @param txnType
	 *            the txnType to set
	 */
	public void setTxnType(GWTransactionType txnType) {
		this.txnType = txnType;
	}

   
   /**
    * Gets the dti txn type.
    *
    * @return the dti txn type
    */
   public TransactionType getDtiTxnType() {
      return dtiTxnType;
   }

   /**
    * Sets the dti txn type.
    *
    * @param dtiTxnType the dtiTxnType to set
    */
   public void setDtiTxnType(TransactionType dtiTxnType) {
      this.dtiTxnType = dtiTxnType;
   }
	
}
