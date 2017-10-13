package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.dao.data.GuestProductTO;
import pvt.disney.dti.gateway.dao.data.UpgradeCatalogTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.PayPlanEligibilityStatusType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.data.common.TicketTO.UpgradeEligibilityStatusType;
import pvt.disney.dti.gateway.data.common.UpgrdPathSeqTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.ProductRules;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for three major functions for WDW Query Eligible
 * Product :<BR>
 * 1. Defining the business rules specific to WDW query Eligible Products.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * 
 */
public class WDWQueryEligibleProductsRules {

   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(WDWQueryEligibleProductsRules.class
            .getCanonicalName());

   /** Object instance used for logging. */
   private static final WDWQueryEligibleProductsRules THISINSTANCE = new WDWQueryEligibleProductsRules();

   /** Request type header constant. */
   private final static String REQUEST_TYPE_QUERY = "Query";

   /** Request subtype header constant. */
   private final static String REQUEST_SUBTYPE_QUERYTICKET = "QueryTicket";

   /** Constant indicating all tags should be created. */
   private final static String ALL_TAGS = "All";

   /** Constant indicating the Query Eligible Products XSD. */
   private final static String NO_NAMESPACE_SCHEMA_LOCATION = "query_eligible_products_request.xsd";

   /** Constant text for ITEM ONE (1). */
   private final static String ITEMONE = "1";

   /** The Constant WDW_TPS_CODE. */
   private final static String WDW_TPS_CODE = "WDW-ATS";

   /** The paylan. */
   private static final String PAYPLAN = "PAYPLAN";

   /** The paylan. */
   private static final String BIOMETRIC = "BIOMET";

   /** YES */
   private static final String YES = "YES";

   /** NO */
   private static final String NO = "NO";

   /** The Constant NUMBER_FOR_DAYCOUNT. */
   private static final int NUMBER_FOR_DAYCOUNT = 24 * 60 * 60 * 1000;

   /**
    * Transform the DTITransactionTO value object to the provider value objects
    * and then pass those to XML Marshaling routines to create an XML string.
    * 
    * @param dtiTxn
    *           the DTI Transaction object.
    * @return the XML string version of the provider request.
    * @throws DTIException
    *            when any transformation error is encountered.
    */
   static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
      String xmlString = null;
      DTIRequestTO dtiRequest = dtiTxn.getRequest();
      CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
      QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiCmdBody;

      // Command Level
      OTCommandTO atsCommand = new OTCommandTO(OTCommandTO.OTTransactionType.QUERYTICKET);
      atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
      atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

      // Header Level
      OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn, REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERYTICKET);
      atsCommand.setHeaderTO(hdr);

      // Query Ticket Level
      OTQueryTicketTO otQryTkt = new OTQueryTicketTO();

      // Tags
      ArrayList<String> tagList = otQryTkt.getTagsList();

      // Adding ALL_Tag in case of Query Eligible
      tagList.add(ALL_TAGS);

      // SiteNumber
      HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();
      AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
      if (anAttributeTO == null) {
         otQryTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
      } else {
         otQryTkt.setSiteNumber(Integer.parseInt(anAttributeTO.getAttrValue()));
      }

      // Ticket Information
      ArrayList<TicketTO> dtiTicketList = dtiReq.getTktList();
      TicketTO dtiTicket = dtiTicketList.get(0);

      // putting ticket into ticket info list
      // ArrayList<OTTicketInfoTO> otTicketList = otQryTkt.getTicketInfoList();

      OTTicketTO otTicket = new OTTicketTO();
      OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

      otTicketInfo.setItem(new BigInteger(ITEMONE));

      ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();
      TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

      switch (dtiTicketType) {
      case DSSN_ID:
         otTicket.setTDssn(dtiTicket.getDssnDate(), dtiTicket.getDssnSite(), dtiTicket.getDssnStation(),
                  dtiTicket.getDssnNumber());
         break;
      case TKTNID_ID:
         otTicket.setTCOD(dtiTicket.getTktNID());
         break;
      case BARCODE_ID:
         otTicket.setBarCode(dtiTicket.getBarCode());
         break;
      case MAG_ID:
         otTicket.setMagTrack(dtiTicket.getMagTrack1());
         break;
      case EXTERNAL_ID:
         otTicket.setExternalTicketCode(dtiTicket.getExternal());
         break;
      }

      otTicketInfo.setTicketSearchMode(otTicket);

      otQryTkt.getTicketInfoList().add(otTicketInfo);

      // Set the Query Ticket TO on the command
      atsCommand.setQueryTicketTO(otQryTkt);

      // Get the XML String
      xmlString = OTCommandXML.getXML(atsCommand);

      return xmlString;
   }

   /**
    * Transforms a QueryEligibleResponse response string from the WDW provider
    * and updates the DTITransactionTO object with the response information.
    * 
    * @param dtiTxn
    *           The transaction object for this request.
    * @param otCmdTO
    *           the ot cmd TO
    * @param dtiRespTO
    *           the dti resp TO
    * @return The DTITransactionTO object, enriched with the response
    *         information.
    * @throws DTIException
    *            for any error. Contains enough detail to formulate an error
    *            response to the seller.
    */
   @SuppressWarnings("unused")
   static void transformResponseBody(DTITransactionTO dtiTxn, OTCommandTO otCmdTO, DTIResponseTO dtiRespTO)
            throws DTIException {

      // Adding QueryEligibleProductsRequestTO to look if this can be used
      QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiTxn.getRequest().getCommandBody();

      QueryEligibilityProductsResponseTO dtiResRespTO = new QueryEligibilityProductsResponseTO();
      OTQueryTicketTO otQryTicketTO = otCmdTO.getQueryTicketTO();

      // Setting the command body
      dtiRespTO.setCommandBody(dtiResRespTO);

      // global Upgrade Catalog product
      UpgradeCatalogTO globalUpgradeProduct = null;

      // new and final List of Upgrade able Product after filter
      ArrayList<DBProductTO> newProductcatalogList = null;

      // ResponseType
      ArrayList<TicketTO> ticketListTo = dtiResRespTO.getTicketList();

      // Query Ticket InfoInlist
      ArrayList<OTTicketInfoTO> otTicketList = otQryTicketTO.getTicketInfoList();

      // Error Handling
      if (dtiRespTO.getDtiError() != null) {
         TicketTO dtiTicketTO = new TicketTO();
         for (TicketTO aTicketTO : dtiReq.getTktList()) {
            dtiResRespTO.add(aTicketTO);
         }
         return;
      }

      // Iterate for each of the ticketInfo
      if ((otTicketList != null) && (otTicketList.size() > 0)) {
         for (OTTicketInfoTO otTicketInfo : otTicketList) {
            TicketTO dtiTicketTO = new TicketTO();
            ArrayList<BigInteger> tktNbr = new ArrayList<BigInteger>();

            if (otTicketInfo.getTicketType() == null) {
               logger.sendEvent("Ticket type not found ", EventType.EXCEPTION, THISINSTANCE);
               throw new DTIException(WDWQueryEligibleProductsRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                        "Provider returned no ticket on a successful query ticket.");
            } else {
               tktNbr.add(otTicketInfo.getTicketType());
            }

            // To get the list of DB products for the guest
            GuestProductTO guestproductTO = getGuestProductDetails(otTicketInfo, dtiTicketTO, tktNbr);

            // if no guest Type information is found then ticket is INELIGIBLE
            // and transaction stops
            if (guestproductTO.getDbproductTO() == null) {

               logger.sendEvent("DB product of guestproductTO not found. ", EventType.DEBUG, THISINSTANCE);
               dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);

               // populating the ticket Information
               setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
               dtiResRespTO.add(dtiTicketTO);
               return;
            }

            // Putting a check to avoid multiple calls in case of multiple
            // tickets, need to call getAPUpgradeCatalog once
            if (globalUpgradeProduct == null) {
               globalUpgradeProduct = ProductKey.getAPUpgradeCatalog(dtiTxn.getEntityTO(), WDW_TPS_CODE);

               if ((globalUpgradeProduct == null) || (globalUpgradeProduct.getProductListCount() == 0)) {
                  dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);

                  // populating the ticket Information
                  setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
                  dtiResRespTO.add(dtiTicketTO);
                  return;
               }
            }

            // Validation of guest product detail against specified criteria
            if (!validateGuestProductHasValue(otTicketInfo, guestproductTO.getDbproductTO())) {

               dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);

               // populating the ticket Information
               setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
               dtiResRespTO.add(dtiTicketTO);
               return;

            }

            // Pay Plan Eligibility
            if (validateGuestProductPayPlanEligible(otTicketInfo, guestproductTO.getDbproductTO())) {
               dtiTicketTO.setPayPlanEligibilityStatus(PayPlanEligibilityStatusType.YES);
            }

            // Look for list sellable product list (product catalog) from step 2
            // and refine the list
            if ((globalUpgradeProduct != null) && (globalUpgradeProduct.getProductListCount() > 0)) {

               logger.sendEvent("Original list of sellable products. " + globalUpgradeProduct.toString(),
                        EventType.DEBUG, THISINSTANCE);

               logger.sendEvent("Original no sellable products. " + globalUpgradeProduct.getProductListCount(),
                        EventType.DEBUG, THISINSTANCE);

               // Validate the list of the day sub classes for AP Upgrades for
               // Upgrade Path Id obtained in method getGuestProductDetails

               // path id from guest product to
               BigInteger upgradePathId = guestproductTO.getDbproductTO().getUpgrdPathId();

               // Day Sub Class List
               ArrayList<String> daySubClassList = new ArrayList<String>();

               // Retrieve subclasses list only if Upgrade Path id is not null
               if (upgradePathId != null) {

                  Integer upgradePathIdInt = upgradePathId.intValue();
                  ArrayList<UpgrdPathSeqTO> subClassObjList = ProductKey.getSubClassesForPathId(upgradePathIdInt);

                  if (subClassObjList != null) {
                     for (/* each */UpgrdPathSeqTO seqTO : /* in */subClassObjList) {
                        daySubClassList.add(seqTO.getDaySubclass());
                     }
                  }
               }

               logger.sendEvent("Day Sub Class List. " + daySubClassList, EventType.DEBUG, THISINSTANCE);

               // Match the day sub class list with list of sellable products
               globalUpgradeProduct.keepDaySubclasses(daySubClassList);

               logger.sendEvent("List obtained after filtering the sellable products with Day Sub Class List. ["
                        + globalUpgradeProduct.toString() + "]", EventType.DEBUG, THISINSTANCE);

               if (globalUpgradeProduct.getProductListCount() > 0) {
                  // Validates the site details obtained in the WDW response and
                  // compares with site list of day subclasses list of each of
                  // the sellable products
                  ArrayList<String> usageSiteList = new ArrayList<String>();

                  if ((guestproductTO.getOtTicketInfo().getUsagesList() != null)
                           && (guestproductTO.getOtTicketInfo().getUsagesList().size() > 0)) {

                     for (/* each */OTUsagesTO usagesTO : /* in */guestproductTO.getOtTicketInfo().getUsagesList()) {

                        // if site No is present in the usages
                        if (usagesTO.getSiteNumber() != null) {
                           usageSiteList.add(String.valueOf(usagesTO.getSiteNumber()));
                        }
                     }
                  }

                  logger.sendEvent("List of site fetched used by the guest. " + usageSiteList, EventType.DEBUG,
                           THISINSTANCE);

                  if (usageSiteList.size() > 0) {

                     // Rule to disqualify the day sub class based upon
                     // disqualification rules for usage sites
                     ProductRules.disqualifyProduct(usageSiteList, globalUpgradeProduct);
                  }

                  logger.sendEvent("List obtained after filtering sellable products with site List. "
                           + globalUpgradeProduct.toString(), EventType.DEBUG, THISINSTANCE);

                  if (globalUpgradeProduct.getProductListCount() > 0) {

                     // Standard Retail Price
                     BigDecimal standardRetailPrice = guestproductTO.getDbproductTO().getStandardRetailPrice();

                     if ((standardRetailPrice != null) && (standardRetailPrice.compareTo(BigDecimal.ZERO) > 0)) {

                        // Add products from sellable products whose unit price
                        // is greater then standard retail Price
                        globalUpgradeProduct.removeProductsLowerThan(guestproductTO.getDbproductTO()
                                 .getStandardRetailPrice());

                        logger.sendEvent(
                                 "List of Sellable Products Obtained after removing products whose unit price is less than "
                                          + guestproductTO.getDbproductTO().getStandardRetailPrice() + ": "
                                          + globalUpgradeProduct.toString(), EventType.DEBUG, THISINSTANCE);
                     }

                     logger.sendEvent("Final list of sellable products obtained. " + globalUpgradeProduct.toString(),
                              EventType.DEBUG, THISINSTANCE);

                     // Step 5 : Setting up the detail in Ticket TO
                     setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, globalUpgradeProduct.getProductList());
                     dtiResRespTO.add(dtiTicketTO);

                  } else {
                     logger.sendEvent("No sellable product found in new product list.", EventType.DEBUG, THISINSTANCE);
                     dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);
                     // populating the ticket Information
                     setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
                     dtiResRespTO.add(dtiTicketTO);
                     return;
                  }

               } else {
                  logger.sendEvent("No sellable product found in new product list. ", EventType.DEBUG, THISINSTANCE);
                  dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);

                  // populating the ticket Information
                  setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
                  dtiResRespTO.add(dtiTicketTO);
                  return;
               }

               if (globalUpgradeProduct.getProductListCount() == 0) {

                  logger.sendEvent("No sellable product found in new product list. ", EventType.DEBUG, THISINSTANCE);
                  dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);
                  // populating the ticket Information
                  setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO, null);
                  dtiResRespTO.add(dtiTicketTO);
                  return;

               }
            } else {
               logger.sendEvent("No sellable product found. ", EventType.DEBUG, THISINSTANCE);
               dtiTicketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);

            }

         }
      }
   }

   /**
    * Sets the guest product details.
    * 
    * @param otTicketInfo
    *           the ot ticket info
    * @param dtiTktTO
    *           the TicketTO
    * @param tktNbr
    *           the ticket Number
    * @return the guest product TO
    * @throws DTIException
    *            the DTI exception
    */
   public static GuestProductTO getGuestProductDetails(OTTicketInfoTO otTicketInfo, TicketTO dtiTktTO,
            ArrayList<BigInteger> tktNbr) throws DTIException {

      // This method is used for processing step 1
      GuestProductTO guestProductTO = new GuestProductTO();

      // Setting up GWDataRequestRespTO in case of WDW
      guestProductTO.setOtTicketInfo(otTicketInfo);

      // Retrieving the product details for GuestProduct
      ArrayList<DBProductTO> dbProductTO = ProductKey.getProductsfromTktNbr(tktNbr);

      if ((dbProductTO != null) && (dbProductTO.size() > 0)) {
         logger.sendEvent("Product details found as. " + guestProductTO.toString(), EventType.DEBUG, THISINSTANCE);

         guestProductTO.setDbproductTO(dbProductTO.get(0));

      }
      // Return the GuestProductTO
      return guestProductTO;
   }

   /**
    * Validate guest product value.
    *
    * @param otTicketInfo
    *           the ot ticket info
    * @param productTO
    *           the product TO
    * @return true, if successful
    * @throws DTIException
    *            the DTI exception
    */
   public static boolean validateGuestProductHasValue(OTTicketInfoTO otTicketInfo, DBProductTO productTO)
            throws DTIException {

      // firstuseDate
      GregorianCalendar firstuseDate = null;

      // Getting Ticket details

      // Void Code
      Integer voidCode = otTicketInfo.getVoidCode();

      // End Date
      GregorianCalendar endDate = otTicketInfo.getValidityEndDate();

      // Usages
      ArrayList<OTUsagesTO> usagesTOs = otTicketInfo.getUsagesList();

      // Biometric Template
      String biometricTemplate = otTicketInfo.getBiometricTemplate();

      // isResident
      boolean isResident = productTO.isResidentInd();

      // dayCount
      Integer dayCount = productTO.getDayCount();

      // Remaining Value
      BigDecimal remainingEntitlement = otTicketInfo.getRemainingValue();

      // Getting the usages details
      if ((usagesTOs != null) && (usagesTOs.size() > 0)) {
         for (OTUsagesTO otUsagesTO : usagesTOs) {
            if (otUsagesTO.getItem() == 1) {
               firstuseDate = otUsagesTO.getDate();
               break;
            }
         }
      }
      try {
         if (firstuseDate == null) {

            // Usages must have at least one entry
            logger.sendEvent("Usages must have one entry :Validation Failed", EventType.DEBUG, THISINSTANCE);
            return false;
         }

         // Rule 1 : UpgrdPathId is 0
         BigInteger upGrdPath = productTO.getUpgrdPathId();
         if ((upGrdPath == null) || (upGrdPath.intValue() == 0)) {

            logger.sendEvent("UpgrdPathId value is :" + upGrdPath + " :Validation Failed", EventType.DEBUG,
                     THISINSTANCE);
            return false;
         }

         // Rule 2 : VoidCode in the ATS Query Ticket response greater than 0 or
         // less than or equal to 100
         if ((voidCode != null) && ((voidCode > 0) && (voidCode <= 100))) {
            logger.sendEvent("Void Code value is :" + voidCode + " :Validation Failed", EventType.DEBUG, THISINSTANCE);

            return false;
         }

         // Rule 3 : Biometric Template must have one entry
         if (biometricTemplate == null) {

            logger.sendEvent("biometricTemplate is : +" + biometricTemplate + " :Validation Failed", EventType.DEBUG,
                     THISINSTANCE);

            return false;
         }

         // Rule 4 : ResidentInd is N and Validity EndDate less than today
         if ((endDate != null) && (isResident == false) && (getDayDifference(endDate) > 0)) {

            logger.sendEvent("Resident Ind is: " + isResident + "and Validity EndDate is :" + endDate
                     + " :Validation Failed", EventType.DEBUG, THISINSTANCE);

            return false;
         }

         // Rule 5 : resident flag is 'N' and all entitlements are used.
         if ((isResident == false) && (remainingEntitlement != BigDecimal.ZERO)) {

            logger.sendEvent("ResidentInd is :" + isResident + "remainingEntitlement is : " + remainingEntitlement
                     + " :Validation Failed.", EventType.DEBUG, THISINSTANCE);
            return false;
         }

         // Rule 6 : ResidentInd is Y and DayCount = 1 and the first use date is
         // older than 14 days
         if ((firstuseDate != null) && (isResident == true) && (dayCount == 1) && (getDayDifference(firstuseDate) > 14)) {

            logger.sendEvent("ResidentInd is :" + isResident + "Day count is :" + dayCount + "and first use date "
                     + ":" + firstuseDate + " :Validation Failed.", EventType.DEBUG, THISINSTANCE);
            return false;
         }

         // Rule 7 : resident flag is 'Y' and DayCount > 1, and the first use
         // date is older than six months (185 days).
         if ((firstuseDate != null) && (isResident == true) && (dayCount > 1) && (getDayDifference(firstuseDate) > 185)) {

            logger.sendEvent("ResidentInd is :" + isResident + "first use date is :" + firstuseDate
                     + " and DayCount is: " + dayCount + " :Validation Failed.", EventType.DEBUG, THISINSTANCE);
            return false;
         }

      } catch (Exception e) {

         logger.sendEvent("Exception Occured while doing validation in validateGuestProductHasValue :" + e.toString(),
                  EventType.EXCEPTION, THISINSTANCE);
         throw new DTIException(WDWQueryEligibleProductsRules.class, DTIErrorCode.TP_CRITICAL_FAILURE,
                  "Exception Occured while doing validation in  validateGuestProductHasValue");
      }

      return true;
   }

   /**
    * Validate guest product pay plan eligible.
    *
    * @param otTicketInfo
    *           the ot ticket info
    * @param productTO
    *           the product TO
    * @return true, if successful
    * @throws DTIException
    *            the DTI exception
    */
   public static boolean validateGuestProductPayPlanEligible(OTTicketInfoTO otTicketInfo, DBProductTO productTO)
            throws DTIException {

      // isResident
      boolean isResident = productTO.isResidentInd();

      GregorianCalendar firstuseDate = null;

      // Usages
      ArrayList<OTUsagesTO> usagesTOs = otTicketInfo.getUsagesList();

      if ((usagesTOs == null) || (usagesTOs.size() == 0)) {
         return false;
      }

      firstuseDate = usagesTOs.get(0).getDate();

      // Rule 7
      if ((isResident != true) || ((isResident == true) && (getDayDifference(firstuseDate) > 14))) {
         return false;
      }

      return true;

   }

   /**
    * To get the day difference for the GregorianCalendar argument.
    * 
    * @param calendar
    *           the calendar
    * @return the day difference
    */
   private static long getDayDifference(GregorianCalendar calendar) {
      long difference = (new Date().getTime() - calendar.getTime().getTime()) / NUMBER_FOR_DAYCOUNT;
      return difference;
   }

   /**
    * Sets the query eligible response command.
    * 
    * @param guestProductTO
    *           the guest product TO
    * @param dtiTicketTO
    *           the dti ticket TO
    * @param upgradedProductTOList
    *           the upgraded product TO list
    * @throws DTIException
    *            the DTI exception
    */
   private static void setQueryEligibleResponseCommand(GuestProductTO guestProductTO, TicketTO dtiTicketTO,
            ArrayList<DBProductTO> upgradedProductTOList) throws DTIException {

      // Omni ticket Info <TicketInfo >fetched from the Query Ticket response
      OTTicketInfoTO otTicketInfo = guestProductTO.getOtTicketInfo();

      // dbProductTO from GuestProductTO
      DBProductTO dbProductTO = guestProductTO.getDbproductTO();

      if (dbProductTO != null) {
         // Prod Code
         dtiTicketTO.setProdCode(dbProductTO.getPdtCode());

         // ProdGuestType
         if (dbProductTO.getGuestType() != null) {
            dtiTicketTO.setGuestType(dbProductTO.getGuestType());
         }

         // Product Price
         if (dbProductTO.getStandardRetailPrice() != null) {
            dtiTicketTO.setTktPrice(dbProductTO.getStandardRetailPrice());
         }

         // Product Tax
         dtiTicketTO.setTktTax(dbProductTO.getStandardRetailTax());
      }

      // Ticket from TicketInfo <TicketInfo>
      OTTicketTO otTicketTO = null;
      if (otTicketInfo.getTicket() != null) {
         otTicketTO = otTicketInfo.getTicket();
      }

      // Tkt Item
      dtiTicketTO.setTktItem(otTicketInfo.getItem());

      // Ticket Demo
      DemographicsTO dtiDemoTO = new DemographicsTO();
      ArrayList<OTFieldTO> otFieldList = new ArrayList<OTFieldTO>();
      if ((otTicketInfo != null) && (otTicketInfo.getSeasonPassDemo() != null)) {
         otFieldList = otTicketInfo.getSeasonPassDemo().getDemoDataList();

         logger.sendEvent("DemoDataList found with size:" + otFieldList.size(), EventType.DEBUG, THISINSTANCE);
      }

      for (/* each */OTFieldTO anOTField : /* in */otFieldList) {

         // FirstName
         // LastName
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_LASTFIRST)) {
            String lastFirstString = anOTField.getFieldValue();
            if (lastFirstString.contains("/")) {

               String lastNameString = lastFirstString.substring(0, lastFirstString.indexOf("/"));

               String firstNameString = lastFirstString.substring(lastFirstString.indexOf("/") + 1);

               if ((lastNameString != null) && (lastNameString.length() != 0)) {
                  dtiDemoTO.setLastName(lastNameString);
               }

               if ((firstNameString != null) && (lastNameString.length() != 0)) {
                  dtiDemoTO.setFirstName(firstNameString);
               }
            }
         }

         // Addr1
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ADDRESS_ONE)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setAddr1(anOTField.getFieldValue());
            }
         }

         // Addr2
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ADDRESS_TWO)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setAddr2(anOTField.getFieldValue());
            }
         }

         // City
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_CITY)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setCity(anOTField.getFieldValue());
            }
         }

         // State
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_STATE)) {
            if (anOTField.getFieldValue() != null) {
               String stateString = anOTField.getFieldValue();
               dtiDemoTO.setState(stateString.substring(0, 2));
            }
         }

         // ZIP
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ZIP)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setZip(anOTField.getFieldValue());
            }
         }

         // Country
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_COUNTRY)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setCountry(anOTField.getFieldValue());
            }
         }

         // Telephone
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_PHONE)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setTelephone(anOTField.getFieldValue());
            }
         }

         // Email
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_EMAIL)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setEmail(anOTField.getFieldValue());
            }
         }

         // Gender - Not to be included in the response unless "ALL" was
         // specified
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_GENDER)) {
            if (anOTField.getFieldValue() != null) {
               dtiDemoTO.setGender(anOTField.getFieldValue());
            }
         }

         // DOB
         if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_DTE_OF_BIRTH)) {
            if (anOTField.getFieldValue() != null) {
               String dobString = anOTField.getFieldValue();
               try {
                  dtiDemoTO.setDateOfBirth(OTCommandTO.convertTicketDOB(dobString));
               } catch (ParseException pe) {

                  logger.sendEvent("Date formate is not of proper type ", EventType.EXCEPTION, THISINSTANCE);
                  throw new DTIException(WDWQueryEligibleProductsRules.class, DTIErrorCode.TP_CRITICAL_FAILURE,
                           "Ticket provider returned XML with an invalid ticket demographic date of birth: "
                                    + pe.toString());
               }
            }
         }
      }

      dtiTicketTO.addTicketDemographic(dtiDemoTO);

      // Ticket Validity
      if (otTicketInfo.getValidityStartDate() != null) {
         dtiTicketTO.setTktValidityValidStart(otTicketInfo.getValidityStartDate());
      }
      if (otTicketInfo.getValidityEndDate() != null) {
         dtiTicketTO.setTktValidityValidEnd(otTicketInfo.getValidityEndDate());
      }

      // Ticket Status BIOMETRIC and PAYPLAN
      ArrayList<TktStatusTO> tktStatusList = dtiTicketTO.getTktStatusList();

      TktStatusTO tktStatus = dtiTicketTO.new TktStatusTO();

      // Biometric
      tktStatus.setStatusItem(BIOMETRIC);
      if ((otTicketInfo.getBiometricTemplate() != null)
               && (otTicketInfo.getBiometricTemplate().compareToIgnoreCase(YES) == 0)) {
         tktStatus.setStatusValue(YES);
      } else {
         tktStatus.setStatusValue(NO);
      }

      // Pay plan
      tktStatus.setStatusItem(PAYPLAN);
      if (dtiTicketTO.getPayPlanEligibilityStatus() == PayPlanEligibilityStatusType.YES) {
         tktStatus.setStatusValue(YES);
      } else {
         tktStatus.setStatusValue(NO);
      }

      // Add the tktStatus to ticket
      tktStatusList.add(tktStatus);

      GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
      String site = otTicketTO.getTdssnSite();
      String station = otTicketTO.getTdssnStation();
      String number = otTicketTO.getTdssnTicketId();

      if ((StringUtils.isNotBlank(site)) && (StringUtils.isNotBlank(station)) && (StringUtils.isNotBlank(number))) {
         dtiTicketTO.setDssn(dssnDate, site, station, number);
      }
      if (StringUtils.isNotBlank(otTicketTO.getMagTrack())) {
         dtiTicketTO.setMag(otTicketTO.getMagTrack());
      }
      if (StringUtils.isNotBlank(otTicketTO.getBarCode())) {
         dtiTicketTO.setBarCode(otTicketTO.getBarCode());
      }
      if (StringUtils.isNotBlank(otTicketTO.getTCOD())) {
         dtiTicketTO.setTktNID(otTicketTO.getTCOD());
      }
      if (StringUtils.isNotBlank(otTicketTO.getExternalTicketCode())) {
         dtiTicketTO.setExternal(otTicketTO.getExternalTicketCode());
      }

      // if result type is INELIGIBLE or NOPRODUCTS no need for checking or
      // displaying eligible productList
      if ((dtiTicketTO.getUpgradeEligibilityStatus() == UpgradeEligibilityStatusType.INELIGIBLE)
               || (dtiTicketTO.getUpgradeEligibilityStatus() == UpgradeEligibilityStatusType.NOPRODUCTS)) {
         return;
      }

      // Usages Information : first usage date

      // FirstDate String
      GregorianCalendar firstUseDate = null;

      if ((guestProductTO.getOtTicketInfo().getUsagesList() != null)
               && (guestProductTO.getOtTicketInfo().getUsagesList().size() > 0)) {

         for (OTUsagesTO otUsagesTO : guestProductTO.getOtTicketInfo().getUsagesList()) {
            if (otUsagesTO.getItem() == 1) {
               firstUseDate = otUsagesTO.getDate();
               break;
            }
         }
      }

      logger.sendEvent("upgradedProductTOList found :", EventType.DEBUG, THISINSTANCE);

      // Eligible product
      if (upgradedProductTOList != null) {
         for (/* each */DBProductTO productTO : /* in */upgradedProductTOList) {
            EligibleProductsTO eligibleProductsTO = new EligibleProductsTO();

            // set the product code
            eligibleProductsTO.setProdCode(productTO.getPdtCode());

            // set the actual product price
            BigDecimal prodPrice = productTO.getUnitPrice();

            eligibleProductsTO.setProdPrice(prodPrice.toString());

            // set the actual product tax
            BigDecimal prodTax = productTO.getTax();

            eligibleProductsTO.setProdTax(prodTax);

            // set the upgraded product price
            BigDecimal prodUpgradePrice = prodPrice.subtract(guestProductTO.getDbproductTO().getStandardRetailPrice());

            eligibleProductsTO.setUpgrdPrice(prodUpgradePrice.toString());

            // set the upgraded product tax
            BigDecimal prodUpgrdTax = prodTax.subtract(guestProductTO.getDbproductTO().getStandardRetailTax());
            eligibleProductsTO.setUpgrdTax(prodUpgrdTax.toString());

            // set the valid end
            try {
               Integer dayCount = productTO.getDayCount();

               Calendar calendar = Calendar.getInstance();
               if (firstUseDate != null) {
                  calendar.setTime(firstUseDate.getTime());
                  calendar.add(Calendar.DAY_OF_MONTH, dayCount);
                  GregorianCalendar gc = new GregorianCalendar();

                  gc.setTime(calendar.getTime());

                  eligibleProductsTO.setValidEnd(gc);

               }
            } catch (Exception e) {

               logger.sendEvent("Exception caught while parsing the first Usage Date " + e.toString(),
                        EventType.EXCEPTION, THISINSTANCE);
               throw new DTIException(WDWQueryEligibleProductsRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                        "Provider responded with an invalid usage Date.");
            }
            // add the eligible product element to the list
            dtiTicketTO.addEligibleProducts(eligibleProductsTO);
         }
      }

      return;
   }

   /**
    * If a type of transaction has a specific number of provider centric rules,
    * implement them here, but if there are a very limited set of rules, mostly
    * common to both providers, implement in the BusinessRules in the parent
    * package.<BR>
    * Currently implements the following rules: <BR>
    * RULE: Validate that ticket presented is of valid format.
    * 
    * @param dtiTxn
    *           the transaction object for this request
    * @throws DTIException
    *            for any rules error.
    */
   public static void applyWDWQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {
      QueryEligibleProductsRequestTO reqTO = (QueryEligibleProductsRequestTO) dtiTxn.getRequest().getCommandBody();
      ArrayList<TicketTO> aTktList = reqTO.getTktList();

      // Validate that ticket presented is of valid format.
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
   }

}