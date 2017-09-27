package pvt.disney.dti.gateway.data.common;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;

/**
 * The Class FloodMatchSignatureTO.
 * @author AGARS017  
 */

public class FloodMatchSignatureTO {

   
   /** The provider type. */
   private String providerType;
   
   /** The transaction type. */
   private TransactionType transactionType;
   
   /** The signature. */
   private String signature;
   
   /**
    * Gets the signature.
    *
    * @return the signature
    */
   public String getSignature() {
      return signature;
   }

   /**
    * Sets the signature.
    *
    * @param signature the new signature
    */
   public void setSignature(String signature) {
      this.signature = signature;
   }

   /**
    * Gets the transaction type.
    *
    * @return the transaction type
    */
   public TransactionType getTransactionType() {
      return transactionType;
   }

   /**
    * Sets the transaction type.
    *
    * @param transactionType the new transaction type
    */
   public void setTransactionType(TransactionType transactionType) {
      this.transactionType = transactionType;
   }

   /**
    * Gets the provider type.
    *
    * @return the provider type
    */
   public String getProviderType() {
      return providerType;
   }

   /**
    * Sets the provider type.
    *
    * @param providerType the new provider type
    */
   public void setProviderType(String providerType) {
      this.providerType = providerType;
   }

  
 
}
