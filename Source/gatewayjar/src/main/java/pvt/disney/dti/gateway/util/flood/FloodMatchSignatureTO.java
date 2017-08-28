package pvt.disney.dti.gateway.util.flood;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;

public class FloodMatchSignatureTO {

   
   private String providerType;
   private TransactionType transactionType;
   private String signature;
   
   public String getSignature() {
      return signature;
   }

   public void setSignature(String signature) {
      this.signature = signature;
   }

   public TransactionType getTransactionType() {
      return transactionType;
   }

   public void setTransactionType(TransactionType transactionType) {
      this.transactionType = transactionType;
   }

   public String getProviderType() {
      return providerType;
   }

   public void setProviderType(String providerType) {
      this.providerType = providerType;
   }

  
 
}
