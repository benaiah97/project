package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * @author lewit019
 * The External Transaction Identifier is a value that can be passed by the seller
 * to provide a private/encrypted means to identify whom is picking up the reservation.
 * @since 2.16.2
 */
public class ExtTxnIdentifierTO implements Serializable, Cloneable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;
  
  /** The transaction identifier. */
  private String txnIdentifier;
  
  /** Is this SHA-1 Encrypted? */
  private Boolean isSHA1Encrypted;

  /**
   * @return the txnIdentifier
   */
  public String getTxnIdentifier() {
    return txnIdentifier;
  }

  /**
   * @param txnIdentifier the txnIdentifier to set
   */
  public void setTxnIdentifier(String txnIdentifier) {
    this.txnIdentifier = txnIdentifier;
  }

  /**
   * @return the isSHA1Encrypted
   */
  public Boolean getIsSHA1Encrypted() {
    return isSHA1Encrypted;
  }

  /**
   * @param isSHA1Encrypted the isSHA1Encrypted to set
   */
  public void setIsSHA1Encrypted(Boolean isSHA1Encrypted) {
    this.isSHA1Encrypted = isSHA1Encrypted;
  }
  
}
