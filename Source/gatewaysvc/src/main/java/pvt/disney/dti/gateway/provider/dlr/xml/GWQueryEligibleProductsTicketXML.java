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
 * @author MISHP012
 *
 */
public class GWQueryEligibleProductsTicketXML {

   /** Event logger. */
   private static final EventLogger logger = EventLogger.getLogger(GWQueryEligibleProductsTicketXML.class.getCanonicalName());
   
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
      
      Element queryTicketElement = bodyElement.addElement("QueryTicket");
      Element queryStanza = queryTicketElement.addElement("Query");;
      Element dataRequestStanza  = queryTicketElement.addElement("DataRequest");;
      GWTicketXML.addQueryTicketElement(qtReqTO, bodyElement, dataRequestStanza, queryStanza,queryTicketElement);
      List<String> qepTag = new ArrayList<>();
      // Demographics Data
      if (qtReqTO.isIncludeTktDemographics()) {
       
         qepTag.addAll(Arrays.asList("FirstName", "LastName", "Street1", "Street2", "City", "State", "ZIP",
                  "CountryCode", "Phone", "Email"));

         
      }
      qepTag.addAll(Arrays.asList("DOB","Gender","UpgradePLUList","PLU"));
   
      GWTicketXML.addElement("Field", qepTag, dataRequestStanza);
      
      Element usageElement = dataRequestStanza.addElement("UsageRecords");
      usageElement.addElement("Field").addText("UseTime");
      usageElement.addElement("Field").addText("UseNo");

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

      GWTicketXML.setRespBodyTO(gwBodyTO, bodyElement);

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

}
