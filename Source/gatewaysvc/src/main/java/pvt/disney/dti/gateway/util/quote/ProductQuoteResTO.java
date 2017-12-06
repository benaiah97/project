package pvt.disney.dti.gateway.util.quote;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * Draft version of the Product Quote Response Transfer Object.
 * @author lewit019
 *
 */
public class ProductQuoteResTO implements Serializable {

   /** Serial Version UID */
   private static final long serialVersionUID = 9129231995L;
   
   /** Product Code - sourced from the TicketTO. */
   private String prodCode; 
     
   /** The ticket validity start date - sourced from the TicketTO. (arrivalDate) */
   private GregorianCalendar tktValidityValidStart;

   /** The ticket validity end date - sourced from the TicketTO. (endArrivalDate?) */
   private GregorianCalendar tktValidityValidEnd;
   
   /** Quote token */
   private String quoteToken; 
   
   /** Printed price of the product. */
   private BigDecimal gatePrice;

   /** Printed price of the product. */
   private BigDecimal gateTax;
   
   /** Printed price of the product. */
   private BigDecimal netPrice;

   /** Printed price of the product. */
   private BigDecimal netTax;
   


   /**
    * @return the quoteToken
    */
   public String getQuoteToken() {
      return quoteToken;
   }

   /**
    * @param quoteToken the quoteToken to set
    */
   public void setQuoteToken(String quoteToken) {
      this.quoteToken = quoteToken;
   }

   /**
    * @return the gatePrice
    */
   public BigDecimal getGatePrice() {
      return gatePrice;
   }

   /**
    * @param gatePrice the gatePrice to set
    */
   public void setGatePrice(BigDecimal gatePrice) {
      this.gatePrice = gatePrice;
   }

   /**
    * @return the gateTax
    */
   public BigDecimal getGateTax() {
      return gateTax;
   }

   /**
    * @param gateTax the gateTax to set
    */
   public void setGateTax(BigDecimal gateTax) {
      this.gateTax = gateTax;
   }

   /**
    * @return the netPrice
    */
   public BigDecimal getNetPrice() {
      return netPrice;
   }

   /**
    * @param netPrice the netPrice to set
    */
   public void setNetPrice(BigDecimal netPrice) {
      this.netPrice = netPrice;
   }

   /**
    * @return the netTax
    */
   public BigDecimal getNetTax() {
      return netTax;
   }

   /**
    * @param netTax the netTax to set
    */
   public void setNetTax(BigDecimal netTax) {
      this.netTax = netTax;
   }

   /**
    * @return the tktValidityValidStart
    */
   public GregorianCalendar getTktValidityValidStart() {
      return tktValidityValidStart;
   }

   /**
    * @param tktValidityValidStart the tktValidityValidStart to set
    */
   public void setTktValidityValidStart(GregorianCalendar tktValidityValidStart) {
      this.tktValidityValidStart = tktValidityValidStart;
   }

   /**
    * @return the tktValidityValidEnd
    */
   public GregorianCalendar getTktValidityValidEnd() {
      return tktValidityValidEnd;
   }

   /**
    * @param tktValidityValidEnd the tktValidityValidEnd to set
    */
   public void setTktValidityValidEnd(GregorianCalendar tktValidityValidEnd) {
      this.tktValidityValidEnd = tktValidityValidEnd;
   }

   /**
    * @return the prodCode
    */
   public String getProdCode() {
      return prodCode;
   }

   /**
    * @param prodCode the prodCode to set
    */
   public void setProdCode(String prodCode) {
      this.prodCode = prodCode;
   }
      
}
