package pvt.disney.dti.gateway.util.quote;

import java.io.Serializable;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.data.common.EntityTO;

/**
 * Draft version of the Product Quote Request Transfer Object.
 * @author lewit019
 *
 */
public class ProductQuoteReqTO implements Serializable {

   /** Serial Version UID */
   private static final long serialVersionUID = 9129231995L;
   
   /** Product Code - sourced from the TicketTO. */
   private String prodCode; 
   
   /** The ticket validity start date - sourced from the TicketTO. (arrivalDate) */
   private GregorianCalendar tktValidityValidStart;

   /** The ticket validity end date - sourced from the TicketTO. (endArrivalDate?) */
   private GregorianCalendar tktValidityValidEnd;
   
   /** The dayCount - sourced from DBProductTO. (length of stay) */
   private int dayCount;
   
   /** The sale entity, sourced from the DTI Transaction (salesChannelId) */
   private EntityTO salesEntity;

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
    * @return the dayCount
    */
   public int getDayCount() {
      return dayCount;
   }

   /**
    * @param dayCount the dayCount to set
    */
   public void setDayCount(int dayCount) {
      this.dayCount = dayCount;
   }

   /**
    * @return the salesEntity
    */
   public EntityTO getSalesEntity() {
      return salesEntity;
   }

   /**
    * @param salesEntity the salesEntity to set
    */
   public void setSalesEntity(EntityTO salesEntity) {
      this.salesEntity = salesEntity;
   }   
}
