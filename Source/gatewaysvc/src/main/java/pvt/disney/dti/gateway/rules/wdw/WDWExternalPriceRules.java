package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.HashMap;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * The Class WDWExternalPriceRules.
 *
 * @author manjrs
 */
public class WDWExternalPriceRules {

   /** The standard core logging mechanism. */
   private static EventLogger eventLogger = EventLogger.getLogger(WDWExternalPriceRules.class);

   /**
    * Instantiates a new WDW external price rules.
    */
   private WDWExternalPriceRules() {
      // private default constructor
      // to avoid unwanted instance creation
   }

   /**
    * Validate variably priced.
    *
    * @param tktListTOList
    *           the tkt list TO list
    * @throws DTIException
    *            the DTI exception
    */
   public static void validateDeltaProducts(ArrayList<TicketTO> tktListTOList) throws DTIException {

      eventLogger.sendEvent("Entering validateVariablyPriced() :", EventType.DEBUG, WDWExternalPriceRules.class);
      HashMap<String, String> result = ProductKey.getOrderVarPrcdProducts(tktListTOList);

      if (!result.isEmpty() && result != null) {
         for /* each */ (TicketTO ticketTO : /* in */tktListTOList) {
            if(ticketTO.getProdCode()!=null){
               ticketTO.setExtrnlPrcd(result.get(ticketTO.getProdCode()));               
            }
         }
      } 
      validateExternalPricing(tktListTOList);
      eventLogger.sendEvent("Exiting validateVariablyPriced() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Validate external pricing.
    *
    * @param tktListTOList
    *           the tkt list TO list
    * @throws DTIException
    */
   private static void validateExternalPricing(ArrayList<TicketTO> tktListTOList) throws DTIException {

      eventLogger.sendEvent("Entering validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      ArrayList<TicketTO> qouteServiceList = new ArrayList<>();
      for /* each */ (TicketTO ticketTO : /* in */tktListTOList) {
         if ((!ticketTO.getExtrnlPrcd().isEmpty()) && (ticketTO.getExtrnlPrcd().equals("T"))) {
            if (validateStartAndEndDates(ticketTO)) {
               qouteServiceList.add(ticketTO);
            } else {
               eventLogger.sendEvent("Invalid/empty validity start date and validity end date.", EventType.WARN,
                        WDWExternalPriceRules.class);
               throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                        "Invalid/empty validity start date and validity end date.");
            }
         }
      }

      if((null != qouteServiceList) && (!qouteServiceList.isEmpty())){
         tktListTOList.removeAll(qouteServiceList);

         // Call to Quote service util class
         try {
            qouteServiceList = quoteServiceUtilClassCall(qouteServiceList);
         } catch (Exception ex) {
            eventLogger.sendEvent("Unable to verify pricing with Quote Service : " + ex.toString(), EventType.WARN,
                     WDWExternalPriceRules.class);
            throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
                     "Unable to verify pricing with Quote Service.");
         }

         // validate price
         for /* each */ (TicketTO ticketTO : /* in */qouteServiceList) {
            if ((null != ticketTO.getProdPrice()) && (!ticketTO.getProdPrice().equals(ticketTO.getQuotePrice()))) {
               eventLogger.sendEvent("Price Mismatch between quote service response and DTI request price.",
                        EventType.WARN, WDWExternalPriceRules.class);
               throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_PRICE,
                        "Price Mismatch between quote service response and DTI request price.");
            }
         }
         tktListTOList.addAll(qouteServiceList);
      }

      eventLogger.sendEvent("Exiting validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Validate start and end dates.
    *
    * @param ticketTO
    *           the ticket TO
    * @return true, if successful
    */
   private static boolean validateStartAndEndDates(TicketTO ticketTO) {
      return ((null != ticketTO.getTktValidityValidStart()) && (null != ticketTO.getTktValidityValidEnd())
               && (ticketTO.getTktValidityValidStart().before(ticketTO.getTktValidityValidEnd())));
   }

   /**
    * Quote service util class call.
    *
    * @param quoteServiceList
    *           the qoute service list
    * @return the array list
    */
   public static ArrayList<TicketTO> quoteServiceUtilClassCall(ArrayList<TicketTO> quoteServiceList) throws Exception {
      for /* each */ (TicketTO ticketTO : /* in */quoteServiceList) {
         ticketTO.setProdPriceQuoteToken("prodquotetokenfor" + ticketTO.getProdCode());
         ticketTO.setQuotePrice(ticketTO.getProdPrice());
      }
      return quoteServiceList;
   }
}
