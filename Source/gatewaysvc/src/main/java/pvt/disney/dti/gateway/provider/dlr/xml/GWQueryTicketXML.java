package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.logging.EventLogger;

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
      
      
      Element queryTicketElement = bodyElement.addElement("QueryTicket");
      Element queryStanza = queryTicketElement.addElement("Query");
      Element dataRequestStanza = queryTicketElement.addElement("DataRequest");

      GWTicketXML.addQueryTicketElement(qtReqTO, bodyElement, dataRequestStanza, queryStanza, queryTicketElement);

      String visualID = qtReqTO.getVisualID();

      // Provide pass renewal information.
      if (qtReqTO.isIncludeRenewalAttributes()) {
         queryStanza.addElement("PassRenewUpgradeMode").addText("1");
      }
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

      if (qtReqTO.isIncludePassAttributes()) {
         qtTag.add("DateUsed");
         qtTag.add("PassKindName");
         GWTicketXML.addElement("Field", qtTag, dataRequestStanza);

         if (visualID.length() == DLR_TEMP_ENTITLEMENT_LENGTH) {
            Element lineageStanza = dataRequestStanza.addElement("LineageRecords");
            List<String> lineageList = new ArrayList<>();
            lineageList.addAll(Arrays.asList("Amount", "Valid", "Status", "VisualID", "ExpirationDate"));
            GWTicketXML.addElement("Field", lineageList, lineageStanza);
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
   public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {
      GWTicketXML.setRespBodyTO(gwBodyTO, bodyElement);

   }

   /**
    * Extracts the lineage info (this structure is complex).
    * 
    * @param dataRespTO
    * @param i
    * @param linReqResp
    * @throws DTIException
    * 
    *            <LineageRequestResponse> <LineageRecords> <LineageRecord>
    *            <Amount>199</Amount> <ExpirationDate>2012-08-24
    *            00:00:00</ExpirationDate> <Status>7</Status> <Valid>NO</Valid>
    *            <VisualID>2937555200149073829</VisualID> </LineageRecord>
    * 
    */
   @SuppressWarnings("rawtypes")
   public static void extractLineageInfo(GWDataRequestRespTO dataRespTO, Iterator<org.dom4j.Element> i,
            Element lineageRequestResponse) throws DTIException {

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
}