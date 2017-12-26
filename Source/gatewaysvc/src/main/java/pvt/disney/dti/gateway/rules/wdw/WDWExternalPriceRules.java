package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
    * Validate externally priced products.
    *
    * @param dtiTxn
    *           the dti txn
    * @param tktListTO
    *           the tkt list TO
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

         if (ticketPdtCode != null) {
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

      for /* each */ (TicketTO ticketTO : /* in */tktListTO) {
         if (ticketTO.getExtrnlPrcd().equals("T")) {
            validateStartAndEndDates(ticketTO);
         }
      }

      quoteUtilCall(entityTO, tktListTO);

      eventLogger.sendEvent("Exiting validateExternalPricing() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return;
   }

   /**
    * Quote util call.
    *
    * @param entityTO
    *           the entity TO
    * @param tktListTO
    *           the quote service ticket to list
    * @throws DTIException
    *            the DTI exception
    */
   private static void quoteUtilCall(EntityTO entityTO, ArrayList<TicketTO> tktListTO)
            throws DTIException {

      eventLogger.sendEvent("Entering quoteUtilCall() :", EventType.DEBUG, WDWExternalPriceRules.class);

      QuoteUtil quoteUtil = new QuoteUtilImpl();
      Collection<ProductQuoteResTO> productQuoteResTOList;
      Collection<ProductQuoteReqTO> productQuoteReqTOList = getProductQuoteReqTO(entityTO, tktListTO);
      productQuoteResTOList = quoteUtil.quoteProducts(productQuoteReqTOList);

      if ((productQuoteResTOList != null) && (!productQuoteResTOList.isEmpty())) {
         getUpdatedTicketTOList(tktListTO, productQuoteResTOList);
      }

      eventLogger.sendEvent("Exiting quoteUtilCall() :", EventType.DEBUG, WDWExternalPriceRules.class);

      return;
   }

   /**
    * Gets the updated ticket TO list.
    *
    * @param tktListTO
    *           the tkt list TO
    * @param quoteServiceCollection
    *           the quote service collection
    * @return the updated ticket TO list
    * @throws DTIException
    *            the DTI exception
    */
   private static void getUpdatedTicketTOList(ArrayList<TicketTO> tktListTO,
            Collection<ProductQuoteResTO> quoteServiceCollection) throws DTIException {

      eventLogger.sendEvent("Entering getUpdatedTicketTOList() :", EventType.DEBUG, WDWExternalPriceRules.class);

      // loop through ticketTO list and productQuoteResTO to validate response
      // and update ticketTO list with prod code token
      for (TicketTO ticketTO : tktListTO) {
         for (ProductQuoteResTO productQuoteResTO : quoteServiceCollection) {
            if (ticketTO.getProdCode().equalsIgnoreCase(productQuoteResTO.getProdCode())) {
               validateQuoteServiceResp(ticketTO, productQuoteResTO);
            }
         }
      }

      eventLogger.sendEvent("Exiting getUpdatedTicketTOList() :", EventType.DEBUG, WDWExternalPriceRules.class);

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
      if ((quoteRespStartDate == null) && (quoteRespEndDate == null)) {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " received empty start/end dates from quote service.",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " received empty start/end dates from quote service.");
      }

      if (!validStartDate.equals(quoteRespStartDate)) {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having start validity date : " + validStartDate
                           + " not equal to quote response start date : " + quoteRespStartDate + ".",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " having start validity date : " + validStartDate
                           + " not equal to quote response start date : " + quoteRespStartDate + ".");
      }

      if (!validEndDate.equals(quoteRespEndDate)) {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having end validity date : " + validStartDate
                           + " not equal to quote response end date : " + quoteRespStartDate + ".",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having end validity date : " + validEndDate
                           + " not equal to quote response end date : " + quoteRespEndDate + ".");
      }

      BigDecimal netPrice = productQuoteResTO.getNetPrice();
      BigDecimal prodPrice = ticketTO.getProdPrice();

      // Validate product price
      if ((netPrice != null) && (!prodPrice.equals(netPrice))) {
         eventLogger.sendEvent("Product " + ticketTO.getProdCode() + " (price: "
                  + productQuoteResTO.getNetPrice().toString() + ") can't be sold at this price (sale at "
                  + ticketTO.getProdPrice().toString() + " was attempted.)", EventType.WARN,
                  WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_PRICE,
                  "Product " + ticketTO.getProdCode() + " (price: " + productQuoteResTO.getNetPrice().toString()
                           + ") can't be sold at this price (sale at " + ticketTO.getProdPrice().toString()
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
         if (ticketTo.getExtrnlPrcd().equalsIgnoreCase("T")) {
            ProductQuoteReqTO productQuoteReqTO = new ProductQuoteReqTO();
            productQuoteReqTO.setProdCode(ticketTo.getProdCode());
            productQuoteReqTO.setTktValidityValidStart(ticketTo.getTktValidityValidStart());
            productQuoteReqTO.setTktValidityValidEnd(ticketTo.getTktValidityValidEnd());
            productQuoteReqTO.setDayCount(ticketTo.getDayCount());
            productQuoteReqTO.setSalesEntity(entityTO);
            productQuoteReqTOList.add(productQuoteReqTO);
         }
      }
      eventLogger.sendEvent("Exiting getProductQuoteReqTO() :", EventType.DEBUG, WDWExternalPriceRules.class);
      return productQuoteReqTOList;
   }

   /**
    * Validate start and end dates.
    *
    * @param ticketTO
    *           the ticket TO
    * @param quoteServiceTicketTOList
    *           the quote service ticket to list
    * @throws DTIException
    *            the DTI exception
    */
   private static void validateStartAndEndDates(TicketTO ticketTO) throws DTIException {

      GregorianCalendar curtDate = new GregorianCalendar();
      Date currentDate = setTimeToMidnight(curtDate.getTime());
      Date validStartDate;
      Date validEndDate;

      if (ticketTO.getTktValidityValidStart() != null) {
         validStartDate = ticketTO.getTktValidityValidStart().getTime();
      } else {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " have empty start ticket validity date.",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " have empty start ticket validity date.");
      }
      
      if (ticketTO.getTktValidityValidEnd() != null) {
         validEndDate = ticketTO.getTktValidityValidEnd().getTime();
      } else {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " have empty end ticket validity date.",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " have empty end ticket validity date.");
      }

      if (validStartDate.after(validEndDate)) {
         eventLogger.sendEvent(
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having start validity date : " + ticketTO.getTktValidityValidStart()
                           + " after end validity date : " + ticketTO.getTktValidityValidEnd() + ".",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having start validity date : " + ticketTO.getTktValidityValidStart()
                           + " after end validity date : " + ticketTO.getTktValidityValidEnd() + ".");
      }

      if ((!validStartDate.equals(currentDate)) && (validStartDate.before(currentDate))) {
         eventLogger.sendEvent("Ticket item " + ticketTO.getTktItem().toString() + " with product "
                  + ticketTO.getProdCode() + " is having start validity date : " + ticketTO.getTktValidityValidStart()
                  + " before current date : " + curtDate + ".", EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having start validity date : " + ticketTO.getTktValidityValidStart()
                           + " before current date : " + curtDate + ".");
      }

      if ((!validEndDate.equals(currentDate)) && (validEndDate.before(currentDate))) {
         eventLogger.sendEvent("Ticket item " + ticketTO.getTktItem().toString() + " with product "
                  + ticketTO.getProdCode() + " should have ticket end validity date : "
                  + ticketTO.getTktValidityValidEnd().getTime() + " before current date : " + curtDate + ".",
                  EventType.WARN, WDWExternalPriceRules.class);
         throw new DTIException(WDWExternalPriceRules.class, DTIErrorCode.INVALID_VALIDITY_DATES,
                  "Ticket item " + ticketTO.getTktItem().toString() + " with product " + ticketTO.getProdCode()
                           + " is having end validity date : " + ticketTO.getTktValidityValidEnd().getTime()
                           + " before current date : " + curtDate + ".");
      }
   }

   /**
    * Sets the time to midnight.
    *
    * @param date
    *           the date
    * @return the date
    */
   public static Date setTimeToMidnight(Date date) {
      Calendar calendar = Calendar.getInstance();

      calendar.setTime(date);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);

      return calendar.getTime();
   }
}
