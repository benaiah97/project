package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
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
   public static void validateExternallyPricedProducts(DTITransactionTO dtiTxn, ArrayList<TicketTO> tktListTO)
            throws DTIException {
      eventLogger.sendEvent("Entering validateExternallyPricedProducts() :", EventType.DEBUG,
               WDWExternalPriceRules.class);

      ArrayList<DBProductTO> dbProdList = dtiTxn.getDbProdList();

      // EntityTO to send in quote service request
      EntityTO entityTO = dtiTxn.getEntityTO();

      // Put DB products in a HashMap for quick access
      HashMap<String, DBProductTO> dbProdMap = new HashMap<String, DBProductTO>();

      for /* each */ (DBProductTO aDBProduct : /* in */dbProdList) {
         dbProdMap.put(aDBProduct.getPdtCode(), aDBProduct);
      }

      setExtrnlPrcdFlagAndDayCountToTicketTO(tktListTO, dbProdMap);
      validateExternalPricing(tktListTO, entityTO);
      eventLogger.sendEvent("Exiting validateExternallyPricedProducts() :", EventType.DEBUG,
               WDWExternalPriceRules.class);
      return;
   }

   /**
    * Sets the extrnl prcd flag and day count to ticket TO.
    *
    * @param tktListTO
    *           the tkt list TO
    * @param dbProdMap
    *           the db prod map
    * @throws DTIException
    *            the DTI exception
    */
   private static void setExtrnlPrcdFlagAndDayCountToTicketTO(ArrayList<TicketTO> tktListTO,
            HashMap<String, DBProductTO> dbProdMap) throws DTIException {

      for /* each */ (TicketTO ticketTO : /* in */tktListTO) {

         String ticketPdtCode = ticketTO.getProdCode();
         DBProductTO dbProduct = dbProdMap.get(ticketPdtCode);

         if (ticketTO.getProdCode() != null) {
            ticketTO.setExtrnlPrcd(dbProduct.getExtrnlPrcd());
            ticketTO.setDayCount(dbProduct.getDayCount());
         }
      }
      return;
   }

   /**
    * Validate external pricing.
    *
    * @param tktListTO
    *           the tkt list TO
    * @param entityTO
    *           the entity TO
    * @throws DTIException
    *            the DTI exception
    */
   private static void validateExternalPricing(ArrayList<TicketTO> tktListTO, EntityTO entityTO) throws DTIException {

      eventLogger.sendEvent("Entering validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      ArrayList<TicketTO> quoteServiceTicketToList = createQuoteServiceTicketToList(tktListTO);

      if ((quoteServiceTicketToList != null) && (!quoteServiceTicketToList.isEmpty())) {
         tktListTO.removeAll(quoteServiceTicketToList);
         // Call to Quote service util class
         quoteUtilCall(entityTO, quoteServiceTicketToList);
         // validate price
         tktListTO.addAll(quoteServiceTicketToList);
      }

      eventLogger.sendEvent("Exiting validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Quote util call.
    *
    * @param entityTO
    *           the entity TO
    * @param quoteServiceTicketToList
    *           the quote service ticket to list
    * @throws DTIException
    *            the DTI exception
    */
   private static void quoteUtilCall(EntityTO entityTO, ArrayList<TicketTO> quoteServiceTicketToList)
            throws DTIException {

      eventLogger.sendEvent("Entering quoteUtilCall() :", EventType.DEBUG, WDWExternalPriceRules.class);

      QuoteUtil quoteUtil = new QuoteUtilImpl();
      Collection<ProductQuoteResTO> productQuoteResTOList = null;
      Collection<ProductQuoteReqTO> productQuoteReqTOList = getProductQuoteReqTO(entityTO, quoteServiceTicketToList);
      try {
         productQuoteResTOList = quoteUtil.quoteProducts(productQuoteReqTOList);
      } catch (Exception ex) {
         eventLogger.sendEvent("Unable to verify pricing with Quote Service : " + ex.toString(), EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
                  "Unable to verify pricing with Quote Service.");
      }

      if ((productQuoteResTOList != null) && (!productQuoteResTOList.isEmpty())) {
         getUpdatedTokenTicketList(quoteServiceTicketToList, productQuoteResTOList);
      }

      eventLogger.sendEvent("Exiting quoteUtilCall() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return;
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

      eventLogger.sendEvent("Entering createQuoteServiceTicketToList() :", EventType.DEBUG,
               WDWExternalPriceRules.class);

      // Forming ArrayList containing external priced flag true
      ArrayList<TicketTO> quoteServiceTicketToList = new ArrayList<>();
      for /* each */ (TicketTO ticketTO : /* in */tktListTOList) {
         if ((!ticketTO.getExtrnlPrcd().isEmpty()) && (ticketTO.getExtrnlPrcd().equals("T"))) {
            validateStartAndEndDates(ticketTO, quoteServiceTicketToList);
         }
      }

      eventLogger.sendEvent("Exiting createQuoteServiceTicketToList() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return quoteServiceTicketToList;
   }

   /**
    * Gets the updated token ticket list.
    *
    * @param tktListTO
    *           the tkt list TO list
    * @param quoteServiceCollection
    *           the quote service collection
    * @return the updated token ticket list
    * @throws DTIException
    *            the DTI exception
    */
   private static void getUpdatedTokenTicketList(ArrayList<TicketTO> tktListTO,
            Collection<ProductQuoteResTO> quoteServiceCollection) throws DTIException {

      eventLogger.sendEvent("Entering getUpdatedTokenTicketList() :", EventType.DEBUG, WDWExternalPriceRules.class);

      // loop through ticketTO list and productQuoteResTO to validate response
      // and update ticketTO list with prod code token
      for (TicketTO ticketTO : tktListTO) {
         for (ProductQuoteResTO productQuoteResTO : quoteServiceCollection) {
            if (ticketTO.getProdCode().equalsIgnoreCase(productQuoteResTO.getProdCode())) {
               validateQuoteServiceResp(ticketTO, productQuoteResTO);
            }
         }
      }

      eventLogger.sendEvent("Exiting getUpdatedTokenTicketList() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return;
   }

   
   /**
    * Validate quote service resp.
    *
    * @param ticketTO
    *           the ticket TO
    * @param productQuoteResTO
    *           the product quote res TO
    * @throws DTIException
    *            the DTI exception
    */
   private static void validateQuoteServiceResp(TicketTO ticketTO, ProductQuoteResTO productQuoteResTO)
            throws DTIException {

      eventLogger.sendEvent("Entering validateQuoteServiceResp() :", EventType.DEBUG, WDWExternalPriceRules.class);
      
      GregorianCalendar quoteRespStartDate = productQuoteResTO.getTktValidityValidStart();
      GregorianCalendar quoteRespEndDate = productQuoteResTO.getTktValidityValidEnd();
      GregorianCalendar validStartDate = ticketTO.getTktValidityValidStart();
      GregorianCalendar validEndDate = ticketTO.getTktValidityValidEnd();

      // Validate response validStartDate and validEndEnd
      if ((quoteRespStartDate != null) && (quoteRespEndDate != null) && (!validStartDate.equals(quoteRespStartDate))
               || (!validEndDate.equals(quoteRespEndDate))) {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " did not have ticket validity dates as required.",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " did not have ticket validity dates as required.");
      }
      
      BigDecimal netPrice = productQuoteResTO.getNetPrice();
      BigDecimal prodPrice = ticketTO.getProdPrice();

      // Validate product price
      if ((netPrice != null) && (!prodPrice.equals(netPrice))) {
         eventLogger.sendEvent("Product " + ticketTO.getProdCode() + " (at "
                  + productQuoteResTO.getNetPrice().toString() + ") can't be sold off price (sale at "
                  + ticketTO.getProdPrice().toString() + " was attempted.)", EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_PRICE,
                  "Product " + ticketTO.getProdCode() + " (at " + productQuoteResTO.getNetPrice().toString()
                           + ") can't be sold off price (sale at " + ticketTO.getProdPrice().toString()
                           + " was attempted.)");
      }

      // set prod price quote token
      ticketTO.setProdPriceQuoteToken(productQuoteResTO.getQuoteToken());

      eventLogger.sendEvent("Exiting validateQuoteServiceResp() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return;
   }

   /**
    * Gets the product quote req TO.
    *
    * @param entityTO
    *           the entity TO
    * @param tktListTO
    *           the tkt list TO list
    * @return the product quote req TO
    */
   private static Collection<ProductQuoteReqTO> getProductQuoteReqTO(EntityTO entityTO, ArrayList<TicketTO> tktListTO) {

      eventLogger.sendEvent("Entering getProductQuoteReqTO() :", EventType.DEBUG, WDWExternalPriceRules.class);

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

      eventLogger.sendEvent("Exiting getProductQuoteReqTO() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return productQuoteReqTOList;
   }

   /**
    * Validate start and end dates.
    *
    * @param ticketTO
    *           the ticket TO
    * @param quoteServiceTicketToList
    *           the quote service ticket to list
    * @throws DTIException
    *            the DTI exception
    */
   private static void validateStartAndEndDates(TicketTO ticketTO, ArrayList<TicketTO> quoteServiceTicketToList)
            throws DTIException {
      
      GregorianCalendar currentDate = new GregorianCalendar();

      GregorianCalendar validStartDate = ticketTO.getTktValidityValidStart();
      GregorianCalendar validEndDate = ticketTO.getTktValidityValidEnd();
      if ((validStartDate != null) && (validEndDate != null) && (validStartDate.before(validEndDate))
               && (currentDate.before(validStartDate)) && (currentDate.before(validEndDate))) {
         quoteServiceTicketToList.add(ticketTO);
      } else {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " did not have ticket validity dates as required.",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " did not have ticket validity dates as required.");
      }
   }
}
