package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class has the responsibility for marshaling and unmarshlling the DLR
 * Query Ticket XML.
 * 
 * @author lewit019
 * 
 */
public class GWQueryTicketXML {
   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(GWQueryTicketXML.class.getCanonicalName());

   private static final int DLR_TEMP_ENTITLEMENT_LENGTH = 19;
   
   private static final String COMMA_STRING = ",";
   private static final String EMPTY_STRING = "";

   /**
    * Adds the query ticket element.
    * 
    * @param qtReqTO
    *           the Query Ticket request transfer object.
    * @param bodyElement
    *           the body element which needs the query ticket section added.
    * @throws DTIException
    *            should any marshaling error occur.
    */
   public static void addQueryTicketElement(GWQueryTicketRqstTO qtReqTO, Element bodyElement) throws DTIException {

      logger.sendEvent("GWQueryTicketXML addQueryTicketElement", EventType.INFO, logger);

      Element queryTicketElement = bodyElement.addElement("QueryTicket");
      Element queryStanza = queryTicketElement.addElement("Query");
      Element dataRequestStanza = queryTicketElement.addElement("DataRequest");

      addCommonElement(qtReqTO, bodyElement, dataRequestStanza, queryStanza, queryTicketElement);

      String visualID = qtReqTO.getVisualID();
      // to add the QEP tag in request
      if ((qtReqTO.getDtiTxnType() != null) && (qtReqTO.getDtiTxnType().compareTo(TransactionType.QUERYELIGPRODUCTS)) == 0) {

         List<String> queyEligibleTag = new ArrayList<>();
         queyEligibleTag.addAll(Arrays.asList("DOB", "Gender", "UpgradePLUList", "PLU"));

         addElement("Field", queyEligibleTag, dataRequestStanza);
         // list of uses tag
         List<String> usesTag = new ArrayList<>();
         Element usageElement = dataRequestStanza.addElement("UsageRecords");
         usesTag.addAll((Arrays.asList("UseTime", "UseNo")));

         addElement("Field", usesTag, usageElement);

      } else {
         // to add the Query ticket tag in request
         // Provide pass renewal information.
         if (qtReqTO.isIncludeRenewalAttributes()) {
            queryStanza.addElement("PassRenewUpgradeMode").addText("1");
         }
         // list of query ticket tag
         List<String> qtTag = new ArrayList<>();
         if ((qtReqTO.isIncludeTktDemographics()) || (qtReqTO.isIncludeRenewalAttributes())) {

            qtTag.addAll(Arrays.asList("FirstName", "LastName", "Street1", "Street2", "City", "State", "ZIP",
                     "CountryCode", "Phone", "Email"));
         }

         // If "renewal" is indicated, then ask for DOB & Gender (As of 2.16.1,JTL)
         if (qtReqTO.isIncludeRenewalAttributes()) {
            qtTag.add("DOB");
            qtTag.add("Gender");
            qtTag.add("Renewable");
         }
         // if IncludePassAttributes is true add the DateUsed and PassKindName to field element
         if (qtReqTO.isIncludePassAttributes()) {
            qtTag.add("DateUsed");
            qtTag.add("PassKindName");
            addElement("Field", qtTag, dataRequestStanza);
            if (visualID.length() == DLR_TEMP_ENTITLEMENT_LENGTH) {
               Element lineageStanza = dataRequestStanza.addElement("LineageRecords");
               List<String> lineageList = new ArrayList<>();
               lineageList.addAll(Arrays.asList("Amount", "Valid", "Status", "VisualID", "ExpirationDate"));

               addElement("Field", lineageList, lineageStanza);
            }
         }

      }
      return;
   }

   /**
    * Sets the response body transfer object with the parsed data from the body
    * element.
    * 
    * @param gwBodyTO
    *           The body transfer object which needs to have values set.
    * @param bodyElement
    *           the body element from the response.
    * @throws DTIException
    *            should any unmarshalling exception occur.
    */
   @SuppressWarnings("unchecked")
   public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {
      
      logger.sendEvent("GWQueryTicketXML setRespBodyTO", EventType.INFO, logger);
      for (Iterator<org.dom4j.Element> i = bodyElement.elementIterator(); i.hasNext();) {
         Element element = i.next();

         if (element.getName().compareTo("Status") == 0) {
            GWStatusTO statusTO = setStatusTO(element);
            gwBodyTO.setStatusTO(statusTO);
         }

         if (element.getName().compareTo("QueryTicketResponse") == 0) {
            GWQueryTicketRespTO respTO = setRespTO(element);
            gwBodyTO.setQueryTicketResponseTO(respTO);
         }

         if (element.getName().compareTo("QueryTicketErrors") == 0) {
            GWQueryTicketErrorsTO errorsTO = setErrorsTO(element);
            gwBodyTO.setQueryTicketErrorsTO(errorsTO);
         }

      }

      if (gwBodyTO.getStatusTO() == null) {
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned QueryTicketResponse without Status clause.");
      }

      if ((gwBodyTO.getQueryTicketErrorsTO() == null) && (gwBodyTO.getQueryTicketResponseTO() == null)) {
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned QueryTicketResponse without Error or Response clauses.");
      }

      return;

   }

   /**
    * Extracts the lineage info (this structure is complex).
    *
    * <LineageRequestResponse> <LineageRecords> <LineageRecord>
    * <Amount>199</Amount> <ExpirationDate>2012-08-24
    * 00:00:00</ExpirationDate> <Status>7</Status> <Valid>NO</Valid>
    * <VisualID>2937555200149073829</VisualID> </LineageRecord>
    * @param dataRespTO the data resp TO
    * @param i the i
    * @param lineageRequestResponse the lineage request response
    * @throws DTIException the DTI exception
    */
   @SuppressWarnings("rawtypes")
   public static void extractLineageInfo(GWDataRequestRespTO dataRespTO, Iterator<org.dom4j.Element> i,
            Element lineageRequestResponse) throws DTIException {

      logger.sendEvent("GWQueryTicketXML extractLineageInfo", EventType.INFO, logger);
      // LineageRequestResponse
      Node linReqResp = lineageRequestResponse;

      // LineageRecords
      Node linRecords = linReqResp.selectSingleNode("LineageRecords");
      if (linRecords == null) {
         return;
      }

      List linRecordList = linRecords.selectNodes("LineageRecord");

      for (int index = 0; index < linRecordList.size(); index++) {

         Node linRecord = (Node) linRecordList.get(index);

         // Create the inner class
         GWDataRequestRespTO.LineageRecord lineageRecordTO = dataRespTO.new LineageRecord();

         // Amount
         Node amountNode = linRecord.selectSingleNode("Amount");
         if (amountNode != null) {
            String inText = amountNode.getText();
            if (inText.contains("."))
               lineageRecordTO.setAmount(new BigDecimal(inText));
            else
               lineageRecordTO.setAmount(new BigDecimal(inText + ".00"));
         }

         // Expiration Date
         Node expireNode = linRecord.selectSingleNode("ExpirationDate");
         if (expireNode != null) {
            String inText = expireNode.getText();
            if ((inText != null) && (inText != "")) {
               GregorianCalendar expDate = UtilityXML.getGCalFromEGalaxyDate(inText);
               if (expDate == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML LineageRecord has unparsable ExpirationDate: " + inText);
               }
               lineageRecordTO.setExpirationDate(expDate);
            }
         }

         // Status
         Node statusNode = linRecord.selectSingleNode("Status");
         if (statusNode != null) {
            String inText = statusNode.getText();
            try {
               lineageRecordTO.setStatus(Integer.decode(inText));
            } catch (NumberFormatException nfe) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML LineageRecord has non-numeric Status.");
            }
         }

         // Valid
         Node validNode = linRecord.selectSingleNode("Valid");
         if (validNode != null) {
            String validString = validNode.getText();
            if (validString.equalsIgnoreCase("YES"))
               lineageRecordTO.setValid(Boolean.valueOf(true));
            else if (validString.equalsIgnoreCase("NO"))
               lineageRecordTO.setValid(Boolean.valueOf(false));
            else {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML LineageRecord has invalid value for Valid.");
            }
         }

         // VisualID
         Node visualIDNode = linRecord.selectSingleNode("VisualID");
         if (visualIDNode != null) {
            String inText = visualIDNode.getText();
            lineageRecordTO.setVisualID(inText);
         }
         // Save it to the array
         dataRespTO.addLineageRecord(lineageRecordTO);

      }
   }
   /**
    * Extract upgrade PLU info.
    * 
    * @param dataRespTO
    *           the data resp TO
    * @param i
    *           the i
    * @param upGradePLUResponse
    *           the up grade PLU response
    * @throws DTIException
    *            the DTI exception
    */
   @SuppressWarnings("rawtypes")
   public static void extractUpgradePLUInfo(GWDataRequestRespTO dataRespTO, Iterator<org.dom4j.Element> i,
            Element upGradePLUResponse) throws DTIException {

      logger.sendEvent("GWQueryEligibleProductsTicketXML extractUpgradePLUInfo", EventType.INFO, logger);
      // UpGradePLU Response
      Node upGradeLineResp = upGradePLUResponse;

      List linRecordList = upGradeLineResp.selectNodes("Item");

      for (int index = 0; index < linRecordList.size(); index++) {

         Node linRecord = (Node) linRecordList.get(index);

         // Create the inner class
         GWDataRequestRespTO.UpgradePLU upgradePLU = dataRespTO.new UpgradePLU();

         // PLU
         Node pluNode = linRecord.selectSingleNode("PLU");
         if (pluNode != null) {

            upgradePLU.setPLU(pluNode.getText());
         }

         // Price
         Node priceNode = linRecord.selectSingleNode("Price");
         if (priceNode != null) {
            String priceText = priceNode.getText();
            if (priceText != null) {
               priceText = priceText.replace(COMMA_STRING, EMPTY_STRING);
               upgradePLU.setPrice(new BigDecimal(priceText));
            }
         }

         // Upgrade Price
         Node upgdPriceNode = linRecord.selectSingleNode("UpgradePrice");
         if (upgdPriceNode != null) {
            String upgdPriceText = upgdPriceNode.getText();

            // if comma is present in the string (for values over 1000), remove
            // the comma
            if (upgdPriceText != null) {
               upgdPriceText = upgdPriceText.replace(COMMA_STRING, EMPTY_STRING);
               upgradePLU.setUpgradePrice(new BigDecimal(upgdPriceText));
            }
         }

         // Adding Pay Plan information
         if (upGradeLineResp.getName().compareTo("PaymentPlans") == 0) {
            dataRespTO.setPayPlan("YES");
            extractPayplanInfo(upgradePLU, upGradeLineResp);
         }

         // Save it to the array
         dataRespTO.addUpgradePLUList(upgradePLU);
      }
   }

   /**
    * Extract payplan info for the pay plan tag.
    * 
    * @param upgradePLUNode
    *           the upgrade PLU node
    * @param payPlanLineResponse
    *           the pay plan line response
    * @throws DTIException
    *            the DTI exception
    */
   @SuppressWarnings("rawtypes")
   private static void extractPayplanInfo(GWDataRequestRespTO.UpgradePLU upgradePLUNode, Node payPlanLineResponse)
            throws DTIException {

      logger.sendEvent("GWQueryEligibleProductsTicketXML extractPayplanInfo", EventType.INFO, logger);
      // Payment Plan response
      Node payPlanLineResp = payPlanLineResponse;

      List linRecordList = payPlanLineResp.selectNodes("PaymentPlan");

      for (int index = 0; index < linRecordList.size(); index++) {

         Node linRecord = (Node) linRecordList.get(index);

         // Create the inner class 

         GWDataRequestRespTO.UpgradePLU.PaymentPlan paymentPlan = upgradePLUNode.new PaymentPlan();

         // PayplanId
         Node payPlanId = linRecord.selectSingleNode("PaymentPlanID");
         if (payPlanId != null) {
            paymentPlan.setPlanId(payPlanId.getText());

         }

         // Description
         Node payPlanDesc = linRecord.selectSingleNode("Description");
         if (payPlanDesc != null) {
            paymentPlan.setPlanId(payPlanDesc.getText());
         }

         // Name
         Node payPlanName = linRecord.selectSingleNode("Name");
         if (payPlanName != null) {
            paymentPlan.setPlanId(payPlanName.getText());
         }

         upgradePLUNode.addPaymentPlans(paymentPlan);

      }

   }

   /**
    * Extract Usage records Info.
    * 
    * @param dataRespTO
    *           the data resp TO
    * @param i
    *           the i
    * @param usageRecordsResponse
    *           the usage records
    * @throws DTIException
    *            the DTI exception
    */
   @SuppressWarnings("rawtypes")
   public static void extractUsageRecordsInfo(GWDataRequestRespTO dataRespTO, Iterator<org.dom4j.Element> i,
            Element usageRecordsResponse) throws DTIException {
      
      logger.sendEvent("GWQueryEligibleProductsTicketXML extractUsageRecordsInfo", EventType.INFO, logger);
      // Usage Record Response
      Node usageLineResponse = usageRecordsResponse;

      Node linRecords = usageLineResponse.selectSingleNode("UsageRecords");
      if (linRecords == null) {
         return;
      }

      List linRecordList = linRecords.selectNodes("UsageRecord");

      for (int index = 0; index < linRecordList.size(); index++) {

         Node linRecord = (Node) linRecordList.get(index);

         // Create the inner class
         GWDataRequestRespTO.UsageRecord usageRecord = dataRespTO.new UsageRecord();

         // Use
         Node useNoNode = linRecord.selectSingleNode("UseNo");
         if (useNoNode != null) {
            int inText = Integer.valueOf(useNoNode.getText());
            usageRecord.setUseNo(inText);
         }

         // Use Time
         Node useTimeNode = linRecord.selectSingleNode("UseTime");
         if (useTimeNode != null) {
            String useTime = useTimeNode.getText();
            if ((useTime != null) && (useTime.length() > 0)) {
               GregorianCalendar useDateTime = UtilityXML.getGCalFromEGalaxyDate(useTime);
               if (useDateTime == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable StartDateTime: " + useTime);
               }
               usageRecord.setUseTime(useDateTime);
            }
         }
         dataRespTO.addUsageRecords(usageRecord);
      }

   }
   
   
   /**
    * Adds the query ticket element.
    *
    * @param qtReqTO the qt req TO
    * @param bodyElement the body element
    * @param dataRequestStanza the data request stanza
    * @param queryStanza the query stanza
    * @param queryTicketElement the query ticket element
    * @throws DTIException the DTI exception
    */
   public static void addCommonElement(GWQueryTicketRqstTO qtReqTO, Element bodyElement,
            Element dataRequestStanza, Element queryStanza, Element queryTicketElement) throws DTIException {

      String visualID = qtReqTO.getVisualID();
      if (visualID == null) {
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "QueryTicket request did not have a visual ID specified.");
      }
      queryStanza.addElement("VisualID").addText(visualID);

      List<String> commonTag = new ArrayList<>();
      // list of common tags 
      commonTag.addAll(Arrays.asList("ItemKind", "Returnable", "Status", "DateSold", "TicketDate", "StartDateTime",
               "DateOpened", "ExpirationDate", "EndDateTime", "ValidUntil", "LockedOut", "VisualID", "Price",
               "UseCount", "Tax", "RemainingUse", "Kind"));
      // add the common tag text of data request to field element
      addElement("Field", commonTag, dataRequestStanza);
   }

   /**
    * Adds the tag text of request to passed element.
    * 
    * @param element
    *           the element
    * @param text
    *           the text
    * @param requestStanza
    *           the request stanza
    */
   public static void addElement(String element, List<String> text, Element requestStanza) {
      for (String object : text) {
         requestStanza.addElement(element).addText(object);
      }

   }
   
   /**
    * Marshals the gateway status transfer object.
    * 
    * @param statusElement
    *           the response supplied by gateway.
    * @return the Gateway Status Transfer Object.
    * @throws DTIException
    *            should any unmarshalling error occur.
    */
   @SuppressWarnings("unchecked")
   private static GWStatusTO setStatusTO(Element statusElement) throws DTIException {

      GWStatusTO gwStatusTO = new GWStatusTO();

      for (Iterator<org.dom4j.Element> i = statusElement.elementIterator(); i.hasNext();) {
         Element element = i.next();

         if (element.getName().compareTo("StatusCode") == 0) {
            gwStatusTO.setStatusCode(element.getText());
         }

         if (element.getName().compareTo("StatusText") == 0) {
            gwStatusTO.setStatusText(element.getText());
         }

      }

      if (gwStatusTO.getStatusCode() == null)
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned Status clause missing a StatusCode.");

      if (gwStatusTO.getStatusText() == null)
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned Status clause missing a StatusText.");

      return gwStatusTO;
   }

   /**
    * Unmarshall the query response element into the gateway query ticket
    * response transfer object.
    * 
    * @param qryRespElement
    *           the query ticket response element provided in the parsed
    *           response.
    * @return the gateway query ticket response transfer object.
    * @throws DTIException
    *            should any unmarshalling problem occur.
    */
   @SuppressWarnings("unchecked")
   private static GWQueryTicketRespTO setRespTO(Element qryRespElement) throws DTIException {

      GWQueryTicketRespTO qtRespTO = new GWQueryTicketRespTO();

      Element dataRespElement = null;

      for (Iterator<org.dom4j.Element> i = qryRespElement.elementIterator(); i.hasNext();) {
         Element element = i.next();

         if (element.getName().compareTo("DataRequestResponse") == 0) {
            dataRespElement = element;
            break;
         }

      }

      GWDataRequestRespTO dataRespTO = new GWDataRequestRespTO();

      if (dataRespElement == null)
         throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned QueryTicketResponse without a DataRequestResponse clause.");

      for (Iterator<org.dom4j.Element> i = dataRespElement.elementIterator(); i.hasNext();) {
         Element element = i.next();

         Integer itemKind = 0;

         // Integer itemKind
         if (element.getName().compareTo("ItemKind") == 0) {
            String itemKindString = element.getText();
            try {
               itemKind = new Integer(itemKindString);
               dataRespTO.setItemKind(itemKind);
            } catch (NumberFormatException nfe) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has non-numeric ItemKind.");
            }
         }

         // Boolean returnable
         if (element.getName().compareTo("Returnable") == 0) {
            String returnableString = element.getText();
            if (returnableString.equalsIgnoreCase("YES"))
               dataRespTO.setReturnable(Boolean.valueOf(true));
            else if (returnableString.equalsIgnoreCase("NO"))
               dataRespTO.setReturnable(Boolean.valueOf(false));
            else
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has invalid value for Returnable.");
         }

         // Boolean renewable (As of 2.16.1, JTL)
         if (element.getName().compareTo("Renewable") == 0) {
            String renewableString = element.getText();
            if (renewableString.equalsIgnoreCase("YES"))
               dataRespTO.setRenewable(Boolean.valueOf(true));
            else if (renewableString.equalsIgnoreCase("NO"))
               dataRespTO.setRenewable(Boolean.valueOf(false));
            else
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has invalid value for Renewable.");
         }

         // Integer status
         if (element.getName().compareTo("Status") == 0) {
            String statusString = element.getText();
            try {
               Integer status = new Integer(statusString);
               dataRespTO.setStatus(status);
            } catch (NumberFormatException nfe) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has non-numeric Status.");
            }
         }

         // GregorianCalendar dateSold
         if (element.getName().compareTo("DateSold") == 0) {
            String dateSoldString = element.getText();
            GregorianCalendar dateSold = UtilityXML.getGCalFromEGalaxyDate(dateSoldString);
            if (dateSold == null) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has unparsable DateSold: " + dateSoldString);
            }
            dataRespTO.setDateSold(dateSold);
         }

         // GregorianCalendar ticketDate
         if (element.getName().compareTo("TicketDate") == 0) {
            String ticketDateString = element.getText();
            if ((ticketDateString != null) && (ticketDateString.length() > 0)) {
               GregorianCalendar ticketDate = UtilityXML.getGCalFromEGalaxyDate(ticketDateString);
               if (ticketDate == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable TicketDate: " + ticketDateString);
               }
               dataRespTO.setTicketDate(ticketDate);
            }
         }

         // GregorianCalendar startDateTime
         if (element.getName().compareTo("StartDateTime") == 0) {
            String startDateTimeString = element.getText();

            if ((startDateTimeString != null) && (startDateTimeString.length() > 0)) {
               GregorianCalendar startDateTime = UtilityXML.getGCalFromEGalaxyDate(startDateTimeString);
               if (startDateTime == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable StartDateTime: " + startDateTimeString);
               }
               dataRespTO.setStartDateTime(startDateTime);
            }
         }

         // GregorianCalendar dateOpened
         if (element.getName().compareTo("DateOpened") == 0) {
            String dateOpenedString = element.getText();
            GregorianCalendar dateOpened = UtilityXML.getGCalFromEGalaxyDate(dateOpenedString);
            if (dateOpened == null) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has unparsable DateOpened: " + dateOpenedString);
            }
            dataRespTO.setDateOpened(dateOpened);
         }

         // GregorianCalendar expirationDate;
         if (element.getName().compareTo("ExpirationDate") == 0) {
            String expirationDateString = element.getText();
            GregorianCalendar expirationDate = UtilityXML.getGCalFromEGalaxyDate(expirationDateString);
            if (expirationDate != null) {
               dataRespTO.setExpirationDate(expirationDate);
            }

         }

         // GregorianCalendar endDateTime
         // Since 2.9 - Modified to prevent empty tags from causing failures.
         if (element.getName().compareTo("EndDateTime") == 0) {
            String endDateTimeString = element.getText();

            if ((endDateTimeString != null) && (endDateTimeString.length() > 0)) {

               GregorianCalendar endDateTime = UtilityXML.getGCalFromEGalaxyDate(endDateTimeString);
               if (endDateTime == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable EndDateTime: " + endDateTimeString);
               }
               dataRespTO.setEndDateTime(endDateTime);
            }

         }

         // GregorianCalendar validUntil
         if (element.getName().compareTo("ValidUntil") == 0) {
            String validUntilString = element.getText();
            GregorianCalendar validUntil = UtilityXML.getGCalFromEGalaxyDate(validUntilString);
            if (validUntil == null) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has unparsable ValidUntil: " + validUntilString);
            }
            dataRespTO.setValidUntil(validUntil);
         }

         // Boolean lockedOut
         if (element.getName().compareTo("LockedOut") == 0) {
            String lockedOutString = element.getText();
            if (lockedOutString.equalsIgnoreCase("YES"))
               dataRespTO.setLockedOut(Boolean.valueOf(true));
            else if (lockedOutString.equalsIgnoreCase("NO"))
               dataRespTO.setLockedOut(Boolean.valueOf(false));
            else
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has invalid value for LockedOut.");
         }

         // String visualID
         if (element.getName().compareTo("VisualID") == 0) {
            String visualID = element.getText();
            dataRespTO.setVisualID(visualID);
         }

         // Use Count
         if (element.getName().compareTo("UseCount") == 0) {
            String useCountString = element.getText();
            try {
               Integer useCount = new Integer(useCountString);
               dataRespTO.setUseCount(useCount);
            } catch (NumberFormatException nfe) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has non-numeric UseCount.");
            }
         }

         // BigDecimal price
         if (element.getName().compareTo("Price") == 0) {
            String priceString = element.getText();
            if (priceString.contains("."))
               dataRespTO.setPrice(new BigDecimal(priceString));
            else
               dataRespTO.setPrice(new BigDecimal(priceString + ".00"));
         }

         // BigDecimal tax
         if (element.getName().compareTo("Tax") == 0) {
            String taxString = element.getText();
            if (taxString.contains("."))
               dataRespTO.setTax(new BigDecimal(taxString));
            else
               dataRespTO.setTax(new BigDecimal(taxString + ".00"));
         }

         // String First Name
         if (element.getName().compareTo("FirstName") == 0) {
            String firstName = element.getText();
            dataRespTO.setFirstName(firstName);
         }

         // String Last Name
         if (element.getName().compareTo("LastName") == 0) {
            String lastName = element.getText();
            dataRespTO.setLastName(lastName);
         }

         // String Street1
         if (element.getName().compareTo("Street1") == 0) {
            String street1 = element.getText();
            dataRespTO.setStreet1(street1);
         }

         // String Street2
         if (element.getName().compareTo("Street2") == 0) {
            String street2 = element.getText();
            dataRespTO.setStreet2(street2);
         }

         // String City
         if (element.getName().compareTo("City") == 0) {
            String city = element.getText();
            dataRespTO.setCity(city);
         }

         // String State
         if (element.getName().compareTo("State") == 0) {
            String state = element.getText();
            dataRespTO.setState(state);
         }

         // String ZIP
         if (element.getName().compareTo("ZIP") == 0) {
            String zip = element.getText();
            dataRespTO.setZip(zip);
         }

         // String CountryCode
         if (element.getName().compareTo("CountryCode") == 0) {
            String countryCode = element.getText();
            dataRespTO.setCountryCode(countryCode);
         }

         // String Phone
         if (element.getName().compareTo("Phone") == 0) {
            String phone = element.getText();
            dataRespTO.setPhone(phone);
         }

         // String Email
         if (element.getName().compareTo("Email") == 0) {
            String email = element.getText();
            dataRespTO.setEmail(email);
         }

         // Gregorian Calendar DOB (Date of Birth) As of 2.16.1, JTL
         if (element.getName().equalsIgnoreCase("DOB")) {
            String dobString = element.getText();
            if ((dobString != null) && (dobString != "")) {
               GregorianCalendar dateOfBirth = UtilityXML.getGCalFromEGalaxyDate(dobString);
               if (dateOfBirth == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable DateUsed: " + dobString);
               }
               dataRespTO.setDateOfBirth(dateOfBirth);
            }
         }

         // Gender (As of 2.16.1, JTL)
         if (element.getName().equalsIgnoreCase("Gender")) {
            String genderString = element.getText();
            dataRespTO.setGenderRespString(genderString);
         }

         // GregorianCalendar DateUsed
         if (element.getName().compareTo("DateUsed") == 0) {
            String dateUsedString = element.getText();
            if ((dateUsedString != null) && (dateUsedString != "")) {
               GregorianCalendar dateUsed = UtilityXML.getGCalFromEGalaxyDate(dateUsedString);
               if (dateUsed == null) {
                  throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                           "Response GW XML DataRequestResp has unparsable DateUsed: " + dateUsedString);
               }
               dataRespTO.setDateUsed(dateUsed);
            }
         }

         // String Kind (as of 2.11)
         if (element.getName().compareTo("Kind") == 0) {
            String kind = element.getText();
            dataRespTO.setKind(kind);
         }

         // Integer Remaining Use (as of 2.14)
         if (element.getName().compareTo("RemainingUse") == 0) {
            String useString = element.getText();
            if ((useString != null) && (useString.equalsIgnoreCase("Unlimited"))) {
               useString = "999";
            }
            try {
               Integer remainingUse = new Integer(useString);
               dataRespTO.setRemainingUse(remainingUse);
            } catch (NumberFormatException nfe) {
               throw new DTIException(GWQueryTicketXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                        "Response GW XML DataRequestResp has non-numeric RemainingUse.");
            }
         }

         // String PassKindName
         if (element.getName().compareTo("PassKindName") == 0) {
            String passKindName = element.getText();
            dataRespTO.setPassKindName(passKindName);
         }
         // QT
         // Compound Tags (LineageRecords)
         if (element.getName().compareTo("LineageRequestResponse") == 0) {
            extractLineageInfo(dataRespTO, i, element);
         }

         // QT

         // PM17616 - For item kind 1 end date time is not required from
         // eGalaxy.
         if ((itemKind == 1) && (dataRespTO.getEndDateTime() == null)) {
            GregorianCalendar endDateTime = UtilityXML.getGCalFromEGalaxyDate("2031-12-31 00:00:00");
            dataRespTO.setEndDateTime(endDateTime);
         }

         DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         Date dateobj = new Date();
         String strFormatedDate = df.format(dateobj);

         if ((itemKind == 1) && (dataRespTO.getStartDateTime() == null)) {
            GregorianCalendar startDateTime = UtilityXML.getGCalFromEGalaxyDate(strFormatedDate);
            dataRespTO.setStartDateTime(startDateTime);
         }

         if ((itemKind == 1) && (dataRespTO.getTicketDate() == null)) {
            GregorianCalendar ticketDate = UtilityXML.getGCalFromEGalaxyDate(strFormatedDate);
            dataRespTO.setTicketDate(ticketDate);
         }

         // String PLU - this is PLU for the current guest ticket
         if (element.getName().compareTo("PLU") == 0) {
            String plu = element.getText();
            dataRespTO.addPluList(plu);
            dataRespTO.setPlu(plu);

         }

         // Adding new Tag UpgradePLUList - this is the list of PLUs (AP) that
         // this ticket can be upgraded to
         if (element.getName().compareTo("UpgradePLUList") == 0) {

            extractUpgradePLUInfo(dataRespTO, i, element);
         }

         // Adding new Usage details
         if (element.getName().compareTo("UsageRequestResponse") == 0) {
            extractUsageRecordsInfo(dataRespTO, i, element);
         }
         
         
         
      }

      qtRespTO.setDataRespTO(dataRespTO);

      return qtRespTO;
   }

   /**
    * Unmarshall the errors element into the gateway query ticket errors
    * transfer object.
    * 
    * @param errorsElement
    *           as provided by the parsed eGalaxy response.
    * @return into the gateway query ticket errors transfer object.
    * @throws DTIException
    *            should any unmarshalling problem occurs.
    */
   private static GWQueryTicketErrorsTO setErrorsTO(Element errorsElement) throws DTIException {

      GWQueryTicketErrorsTO errorsTO = new GWQueryTicketErrorsTO();

      // the errorCode we will hand back after checking for special errors
      // if no special errors found, it is the statusCode
      String errorCode = null;
      // the error text for an errorCode (if found)...grabbed for logging
      String errorText = null;

      // response document to check for errors

      // if all went well we can start hunting for errors
      if (errorsElement != null) {
         logger.sendEvent("QueryTicket Errors element:" + errorsElement.asXML(), EventType.DEBUG, logger);

         // and try to grab //QueryTicketErrors/ERror/Error/ErrorCode
         org.dom4j.Node queryTickerErrorCode = errorsElement.selectSingleNode("//Errors/Error/ErrorCode");

         // if we found one....
         if (queryTickerErrorCode != null && queryTickerErrorCode.getText() != null
                  && queryTickerErrorCode.getText().length() > 0) {
            errorCode = queryTickerErrorCode.getText();
            errorText = errorsElement.selectSingleNode("//Errors/Error/ErrorText").getText();

            logger.sendEvent("GWTicketXML.setErrorsTO() found a QueryTicketError: " + errorCode + ":" + errorText,
                     EventType.DEBUG, logger);
         }
         // if we didnt find QueryTicketErrors/Errors/Error/ErrorCode we need to check for
         // QueryTicketErrors/DataRequestErrors
         else if (errorsElement.selectSingleNode("//DataRequestErrors/DataRequestError/ErrorCode") != null) {
            // if we have a DataRequestError, grab its details
            errorCode = errorsElement.selectSingleNode("//DataRequestErrors/DataRequestError/ErrorCode").getText();
            errorText = errorsElement.selectSingleNode("//DataRequestErrors/DataRequestError/ErrorText").getText();
            logger.sendEvent("GWTicketXML.setErrorTO() found a DataRequestError: " + errorCode + ":" + errorText,
                     EventType.DEBUG, logger);
         }

      }

      // check for bad xml construct
      if (errorCode == null) {
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned QueryTicketError,Errors,Error clause without ErrorCode clause.");
      }

      // check for bad xml construct
      if (errorText == null) {
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned QueryTicketError,Errors,Error clause without ErrorText clause.");
      }
      errorsTO.setErrorCode(errorCode);
      errorsTO.setErrorText(errorText);
      return errorsTO;

   }
  
   
}