package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.util.quote.ProductQuoteReqTO;
import pvt.disney.dti.gateway.util.quote.ProductQuoteResTO;
import pvt.disney.dti.gateway.util.quote.QuoteUtil;
import pvt.disney.dti.gateway.util.quote.QuoteUtilImpl;

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
    * @param dtiTxn
    *           the dti txn
    * @param tktListTO
    *           the tkt list TO list
    * @throws DTIException
    *            the DTI exception
    */
   public static void validateDeltaProducts(DTITransactionTO dtiTxn, ArrayList<TicketTO> tktListTO)
            throws DTIException {
      eventLogger.sendEvent("Entering validateVariablyPriced() :", EventType.DEBUG, WDWExternalPriceRules.class);
      setDayCountToTicketTO(dtiTxn, tktListTO);
      setExtrnlPrcdFlagToTicketTO(tktListTO);
      validateExternalPricing(dtiTxn, tktListTO);
      eventLogger.sendEvent("Exiting validateVariablyPriced() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Sets the extrnl prcd flag to ticket TO.
    *
    * @param tktListTOList
    *           the new extrnl prcd flag to ticket TO
    * @throws DTIException
    *            the DTI exception
    */
   private static void setExtrnlPrcdFlagToTicketTO(ArrayList<TicketTO> tktListTOList) throws DTIException {
      HashMap<String, String> result = ProductKey.getOrderVarPrcdProducts(tktListTOList);

      if ((null != result) && (!result.isEmpty())) {
         for /* each */ (TicketTO ticketTO : /* in */tktListTOList) {
            if (ticketTO.getProdCode() != null) {
               ticketTO.setExtrnlPrcd(result.get(ticketTO.getProdCode()));
            }
         }
      }
      return;
   }

   /**
    * Sets the day count to ticket TO.
    *
    * @param dtiTxn
    *           the dti txn
    * @param tktListTOList
    *           the tkt list TO list
    */
   private static void setDayCountToTicketTO(DTITransactionTO dtiTxn, ArrayList<TicketTO> tktListTOList) {
      ArrayList<DBProductTO> dbProdList = dtiTxn.getDbProdList();
      HashMap<String, Integer> dayCountMap = new HashMap<>();

      for (DBProductTO dBProductTO : dbProdList) {
         dayCountMap.put(dBProductTO.getPdtCode(), dBProductTO.getDayCount());
      }

      for (TicketTO ticketTO : tktListTOList) {
         if (dayCountMap.containsKey(ticketTO.getProdCode())) {
            ticketTO.setDayCount(dayCountMap.get(ticketTO.getProdCode()));
         }
      }
      return;
   }

   /**
    * Validate external pricing.
    *
    * @param dtiTxn
    *           the dti txn
    * @param tktListTOList
    *           the tkt list TO list
    * @throws DTIException
    *            the DTI exception
    */
   private static void validateExternalPricing(DTITransactionTO dtiTxn, ArrayList<TicketTO> tktListTOList)
            throws DTIException {

      eventLogger.sendEvent("Entering validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      ArrayList<TicketTO> quoteServiceTicketToList = createQuoteServiceTicketToList(tktListTOList);

      if ((null != quoteServiceTicketToList) && (!quoteServiceTicketToList.isEmpty())) {
         tktListTOList.removeAll(quoteServiceTicketToList);
         // Call to Quote service util class
         quoteUtilCall(dtiTxn, quoteServiceTicketToList);
         // validate price
         tktListTOList.addAll(quoteServiceTicketToList);
      }

      eventLogger.sendEvent("Exiting validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Quote util call.
    *
    * @param dtiTxn
    *           the dti txn
    * @param tktListTOList
    *           the tkt list TO list
    * @throws DTIException
    *            the DTI exception
    */
   private static void quoteUtilCall(DTITransactionTO dtiTxn, ArrayList<TicketTO> quoteServiceTicketToList)
            throws DTIException {
     
      final QuoteUtil quoteUtil = new QuoteUtilImpl();
      final EntityTO entityTO = dtiTxn.getEntityTO();
      Collection<ProductQuoteResTO> productQuoteResTOList = null;
      final Collection<ProductQuoteReqTO> productQuoteReqTOList = getProductQuoteReqTO(entityTO, quoteServiceTicketToList);
      try {
         productQuoteResTOList = quoteUtil.quoteProducts(productQuoteReqTOList);

      } catch (Exception ex) {
         eventLogger.sendEvent("Unable to verify pricing with Quote Service : " + ex.toString(), EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
                  "Unable to verify pricing with Quote Service.");
      }

      if(!productQuoteResTOList.isEmpty()){
         getUpdatedTokenTicketList(quoteServiceTicketToList, productQuoteResTOList);   
      }
   }

   /**
    * Creates the quote service ticket to list.
    *
    * @param tktListTOList
    *           the tkt list TO list
    * @return the array list
    * @throws DTIException
    *            the DTI exception
    */
   private static ArrayList<TicketTO> createQuoteServiceTicketToList(ArrayList<TicketTO> tktListTOList)
            throws DTIException {
      ArrayList<TicketTO> quoteServiceTicketToList = new ArrayList<>();
      for /* each */ (TicketTO ticketTO : /* in */tktListTOList) {
         if ((!ticketTO.getExtrnlPrcd().isEmpty()) && (ticketTO.getExtrnlPrcd().equals("T"))) {
            if (validateStartAndEndDates(ticketTO)) {
               quoteServiceTicketToList.add(ticketTO);
            } else {
               eventLogger.sendEvent("Invalid/empty validity start date and validity end date.", EventType.WARN,
                        WDWExternalPriceRules.class);
               throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                        "Invalid/empty validity start date and validity end date.");
            }
         }
      }
      return quoteServiceTicketToList;
   }

   /**
    * Gets the updated token ticket list.
    *
    * @param tktListTOList
    *           the tkt list TO list
    * @param quoteServiceCollection
    *           the quote service collection
    * @return the updated token ticket list
    * @throws DTIException
    */
   private static void getUpdatedTokenTicketList(ArrayList<TicketTO> tktListTOList,
            Collection<ProductQuoteResTO> quoteServiceCollection) throws DTIException {

      // loop through ticketTO list and productQuoteResTO to validate response
      // and update ticketTO list with prod code token
      for (TicketTO ticketTO : tktListTOList) {
         for (ProductQuoteResTO productQuoteResTO : quoteServiceCollection) {
            if (ticketTO.getProdCode().equalsIgnoreCase(productQuoteResTO.getProdCode())) {
               validateQuoteServiceResp(ticketTO, productQuoteResTO);
            }
         }
      }
      return;
   }

   private static void validateQuoteServiceResp(TicketTO ticketTO, ProductQuoteResTO productQuoteResTO)
            throws DTIException {

      // Validate response validStartDate and validEndEnd
      if (ticketTO.getTktValidityValidStart().equals(productQuoteResTO.getTktValidityValidStart())
               && ticketTO.getTktValidityValidEnd().equals(productQuoteResTO.getTktValidityValidStart())) {
         eventLogger.sendEvent("validity start date and validity end date are invaild.", EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "validity start date and validity end date are invaild.");
      }

      // Validate prod price
      
      // Ignoring this for now as not sure about the exact price of the product
      /*if ((null != productQuoteResTO.getNetPrice())
               && (!ticketTO.getProdPrice().equals(productQuoteResTO.getNetPrice()))) {
         eventLogger.sendEvent("Price Mismatch between quote service response and DTI request price.", EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_PRICE,
                  "Price Mismatch between quote service response and DTI request price.");
      }*/

      // set prod price quote tocken
      ticketTO.setProdPriceQuoteToken(productQuoteResTO.getQuoteToken());
   }

   /**
    * Gets the product quote req TO.
    *
    * @param dtiTxn
    *           the dti txn
    * @param tktListTO
    *           the tkt list TO list
    * @return the product quote req TO
    */
   private static Collection<ProductQuoteReqTO> getProductQuoteReqTO(EntityTO entityTO, ArrayList<TicketTO> tktListTO) {
      ArrayList<ProductQuoteReqTO> productQuoteReqTOList = new ArrayList<>();

      // build ProductQuoteReqTO list using tktListTO
      for (TicketTO ticketTo : tktListTO) {
         ProductQuoteReqTO productQuoteReqTO = new ProductQuoteReqTO();
         productQuoteReqTO.setProdCode(ticketTo.getProdCode());
         productQuoteReqTO.setTktValidityValidStart(ticketTo.getTktValidityValidStart());
         productQuoteReqTO.setTktValidityValidEnd(ticketTo.getTktValidityValidEnd());
         productQuoteReqTO.setDayCount(ticketTo.getDayCount());
         productQuoteReqTO.setSalesEntity(entityTO);
         productQuoteReqTOList.add(productQuoteReqTO);
      }
      return productQuoteReqTOList;
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
}
