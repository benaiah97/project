package pvt.disney.dti.gateway.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import pvt.disney.dti.gateway.client.ProviderClient;
import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LogKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.rules.ElectronicEntitlementRules;
import pvt.disney.dti.gateway.rules.TransformRules;
import pvt.disney.dti.gateway.service.dtixml.TransmissionRespXML;
import pvt.disney.dti.gateway.service.dtixml.TransmissionRqstXML;
import pvt.disney.dti.gateway.util.DTITracker;
import pvt.disney.dti.gateway.util.ResourceLoader;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.context.BaseContext;
import com.disney.context.ContextManager;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;

/**
 * This class is the primary driver for transactions in the Java version of the DTI Gateway. Note: Since all class variables are just for configuration, then the class can operate as a singleton.
 * 
 * @author lewit019
 * @version 1
 * @since 2.16.3
 */
public class DTIService {

  /** The standard core logging mechanism. */
  private EventLogger logger = EventLogger.getLogger(this.getClass());

  /**
   * The ticket broker currently in use. Defaults to DTIUK if properties can't be read.
   */
  private static String tktBroker = "DTIUK";

  /** Properties variable to store properties from AbstractInitializer. */
  private Properties props = null;

  /** This a singleton class */
  private static DTIService dtiServ = null;

  /**
   * Private constructor that reads the properties
   */
  private DTIService() throws DTIException {

    super();

    // Get properties manager.
    try {
	    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
	    props = ResourceLoader.convertResourceBundleToProperties(rb);
    }
    catch (Throwable e) {
      System.err
          .println("ERROR *** Unable to initialize DTIService object.  Properties issue! ***");
      throw new DTIException();
    }

    tktBroker = getBrokerName();

    /** Initiate Business Rules */
    BusinessRules.initBusinessRules(props);

    return;
  }

  /**
   * Implements the singleton pattern. If needed, method creates the instance of
   * DTIService. Calls the service "makeServiceRequest" method.
   * 
   * @param request
   * @return XML response of type string.
   */
  public static String submitRequest(String request, EntityTO entityTO, Integer transIdITS, Integer tpRefNumber,
      boolean isRework) {

    String response = null;

    if (dtiServ == null) {
      try {
        dtiServ = new DTIService();
      } catch (DTIException dtie) {
        return DTIService.generateGenericError(DTIErrorCode.DTI_PROCESS_ERROR, request);
      }
    }

    response = dtiServ.makeServiceRequest(request, entityTO, transIdITS, tpRefNumber, isRework);

    return response;

  }

  /**
   * The primary driver method used by the DTIService. It takes the string
   * version of the XML request, creates a domain object (dtiTxn), and then
   * passes through business rules, transformations, and logging to complete the
   * transaction.
   * 
   * @param requestXML
   *          The XML request in string format.
   * @return XML response of type string.
   */
  private String makeServiceRequest(String requestXML, EntityTO entityTO, Integer transIdITS, Integer tpRefNumber,
      boolean isRework) {

    String responseXML;
    DTITransactionTO dtiTxn;

    logger.sendEvent("Entering makeServiceRequest().", EventType.DEBUG, this);

    if (requestXML == null) {
      return generateGenericError(DTIErrorCode.INVALID_MSG_ELEMENT, requestXML);
    }

    // Using core logging, establish a way to track this
    // transaction by payloadID
    String payloadId = findTag(requestXML, "PayloadID");
    if (payloadId != null) {
      DTITracker dtitr = new DTITracker();
      dtitr.setPayloadId(payloadId);
      BaseContext bctx = new BaseContext(dtitr);
      ContextManager.getInstance().storeContext(bctx);
    } else {
      return DTIService.generateGenericError(DTIErrorCode.INVALID_MSG_ELEMENT, requestXML);
    }

    //
    // Determine the type of request.
    //
    TransactionType requestType = findRequestType(requestXML);
    if (requestType == TransactionType.UNDEFINED) {
      logger.sendEvent("Unable to determine request type from XML.", EventType.WARN, this);
      ContextManager.getInstance().removeContext();
      return generateGenericError(DTIErrorCode.INVALID_COMMAND, requestXML);
    }
    logger.sendEvent("DTIService determined type of request is " + requestType, EventType.INFO, this);

    //
    // Unmarshal the request
    // Creates a dtiTxn object that knows its broker and its own request
    // type.
    //
    try {

      dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType, tktBroker);
      if (dtiTxn == null)
        throw new DTIException("Null request transfer object returned from parser.");

      dtiTxn.setEntityTO(entityTO);
      dtiTxn.setTransIdITS(transIdITS);

      // Set TP reference number
      dtiTxn.setTpRefNum(tpRefNumber);
      dtiTxn.setRework(isRework); // As of 2.16.3 (JTL)
      logger.sendEvent("Request unmarshalled.", EventType.DEBUG, this);
    } catch (JAXBException je) {
      logger.sendEvent("Exception parsing request xml:  " + je.toString(), EventType.WARN, this);
      je.printStackTrace();
      return generateGenericError(DTIErrorCode.INVALID_MSG_CONTENT, requestXML);
    } catch (DTIException qe) {
      logger.sendEvent("Exception parsing request xml:  " + qe.toString(), EventType.WARN, this);
      ContextManager.getInstance().removeContext();
      return generateGenericError(DTIErrorCode.INVALID_MSG_ELEMENT, requestXML);
    }

    //
    // Process the request through rules to the provider
    //
    try {

      // Apply the business rules
      BusinessRules.applyBusinessRules(dtiTxn);
      logger.sendEvent("Applied business rules.", EventType.DEBUG, this);

      // Check Contingency Actions Logic Module (CALM) moved to BusinessRules
      // as of 2.16.3 (JTL) so outages and raising the wall both have business
      // rules run beforehand.

      // Apply the transform rules
      String xmlRequest = TransformRules.changeToProviderFormat(dtiTxn);
      logger.sendEvent("Applied transform rules (DTI to Provider).", EventType.INFO, this);

      // Acquire all log table key values
      LogKey.getLogTableKeys(dtiTxn);

      // Log the In-bound TP Message
      LogKey.insertITPLog(dtiTxn, xmlRequest);
      logger.sendEvent("Logged inbound TP message.", EventType.INFO, this);

      // Log the Product Detail
      ProductKey.insertProductDetail(dtiTxn);
      logger.sendEvent("Logged the product detail.", EventType.DEBUG, this);

      // Send to Provider
      String xmlResponse = ProviderClient.sendRequestToProvider(dtiTxn, xmlRequest);
      logger.sendEvent("Sent message to provider.", EventType.DEBUG, this);

      // Log the Out-bound TP Message
      LogKey.insertOTPLog(dtiTxn, xmlResponse);
      logger.sendEvent("Logged outbound TP message.", EventType.INFO, this);

      // Parse Response into DTITransactionTO object
      dtiTxn = TransformRules.changeToDtiFormat(dtiTxn, xmlResponse);
      logger.sendEvent("Applied transform rules (Provider to DTI).", EventType.INFO, this);

    } catch (DTIException dtie) {

      logger.sendEvent("DTI ERROR CODE: " + dtie.getDtiErrorCode().getErrorCode() + " - " + dtie.getLogMessage(),
          EventType.WARN, this);
      logger.sendEvent("DTI application class: " + dtie.getClassName().toString(), EventType.WARN, this);

      // Format error transfer object response
      DTIErrorTO errorTO;
      if (dtie.getDtiErrorCode() != null) {
        errorTO = ErrorKey.getErrorDetail(dtie.getDtiErrorCode());
      } else {
        errorTO = ErrorKey.getErrorDetail(DTIErrorCode.UNDEFINED_FAILURE);
      }
      dtiTxn.setResponse(DTIErrorCode.populateDTIErrorResponse(errorTO, dtiTxn));

    } catch (DTICalmException dtice) {

      dtiTxn = dtice.getDtiTxn();
      logger.sendEvent("Warning: CALM Override for provider " + dtiTxn.getTpiCode(), EventType.WARN, this);

    } catch (Exception e) {
      e.printStackTrace();
      logger.sendEvent("Exception: " + e.toString(), EventType.WARN, this);
      logger.sendException(EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
      DTIErrorTO errorTO;
      errorTO = ErrorKey.getErrorDetail(DTIErrorCode.UNDEFINED_FAILURE);
      dtiTxn.setResponse(DTIErrorCode.populateDTIErrorResponse(errorTO, dtiTxn));
    }

    //
    // Records sales for conversion to electronic entitlements. (CR180)
    //
    ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);

    //
    // Marshal the response.
    //
    try {
      responseXML = TransmissionRespXML.getXML(dtiTxn);
      if (responseXML == null)
        throw new DTIException(DTIService.class, DTIErrorCode.UNDEFINED_FAILURE,
            "Unable to marshall XML from the objects passed as response");

      logger.sendEvent("Marshalled the response.", EventType.DEBUG, this);

      //
      // Log the Output Ticket Seller XML
      //
      LogKey.insertOTSLog(dtiTxn, responseXML);
      logger.sendEvent("Logged outbound TS message.", EventType.INFO, this);

    } catch (JAXBException je) {

      logger.sendEvent("Exception parsing resp object:  " + je.toString(), EventType.EXCEPTION, this);
      logger.sendEvent("Error code was: " + je.getErrorCode() + ":  " + je.getLocalizedMessage(), EventType.EXCEPTION,
          this);
      je.printStackTrace();
      return generateGenericError(DTIErrorCode.TP_INTERFACE_FAILURE, requestXML);

    } catch (DTIException qe) {
      logger.sendEvent("Exception retrieving resp xml:  " + qe.toString(), EventType.EXCEPTION, this);
      return generateGenericError(DTIErrorCode.TP_INTERFACE_FAILURE, requestXML);

    } finally {
      // Get rid of logging context (prevents an object leak).
      ContextManager.getInstance().removeContext();
    }

    return responseXML;
  }

  /**
   * A simple routine to locate the value of a unique XML tag in an XML string.
   * returns empty string if tag is empty.
   * 
   * @param xml
   *          All of the XML to be searched as one string.
   * @param tagName
   *          Name of the tag being searched. Don't include greater than, less
   *          than, or slash.
   * @return the String value of the desired tag (empty string is returned for
   *         an empty XML tag or null if not present).
   */
  private String findTag(String xml, String tagName) {

    // Locate the tag in the string.
    int startErrorTag = xml.indexOf("<" + tagName + ">");
    int endErrorTag = xml.indexOf("</" + tagName + ">");

    if ((startErrorTag == -1) || (endErrorTag == -1)) {
      if (xml.indexOf("<" + tagName + "/>") != -1)
        return "";
      else
        return null;
    }

    startErrorTag += tagName.length() + 2; // Move pointer to start of the value.

    if (startErrorTag == endErrorTag) {
      return "";
    }

    String tagString = xml.substring(startErrorTag, endErrorTag);

    return tagString;
  }

  /**
   * This pulls the request type from the request XML (if it exists) and returns
   * a constant value indicating which type of request was found.
   * 
   * @param xmlRequest
   * @return TransactionType an enumeration identifying the transaction type.
   */
  public static TransactionType findRequestType(String xmlRequest) {

    int indexOfXMLTag = xmlRequest.indexOf("</CommandHeader>");
    int indexOfKeyTag = xmlRequest.indexOf("<", indexOfXMLTag + 1);
    int indexOfKeyEndGthan = xmlRequest.indexOf(">", indexOfKeyTag);

    String compare = xmlRequest.substring(indexOfKeyTag + 1, indexOfKeyEndGthan).trim();

    if (compare.equals("QueryTicketRequest")) {
      return TransactionType.QUERYTICKET;
    }
    if (compare.equals("UpgradeAlphaRequest")) {
      return TransactionType.UPGRADEALPHA;
    }
    if (compare.equals("VoidTicketRequest")) {
      return TransactionType.VOIDTICKET;
    }
    if (compare.equals("ReservationRequest")) {
      return TransactionType.RESERVATION;
    }
    if (compare.equals("CreateTicketRequest")) {
      return TransactionType.CREATETICKET;
    }
    if (compare.equals("UpdateTicketRequest")) {
      return TransactionType.UPDATETICKET;
    }
    if (compare.equals("UpdateTransactionRequest")) {
      return TransactionType.UPDATETRANSACTION;
    }
    if (compare.equals("QueryReservationRequest")) {
      return TransactionType.QUERYRESERVATION;
    }
    if (compare.equals("UpgradeEntitlementRequest")) { // 2.10
      return TransactionType.UPGRADEENTITLEMENT;
    }
    if (compare.equals("AssociateMediaToAccountRequest")) { // 2.16.1 BIEST001
      return TransactionType.ASSOCIATEMEDIATOACCOUNT;
    }
    if (compare.equals("TickerateEntitlementRequest")) { // 2.16.1 BIEST001
      return TransactionType.TICKERATEENTITLEMENT;
    }
    if (compare.equals("RenewEntitlementRequest")) { // as of 2.16.1 JTL
      return TransactionType.RENEWENTITLEMENT;
    }
    if (compare.equals("VoidReservationRequest")) { // as of 2.16.3, JTL
      return TransactionType.VOIDRESERVATION;
    }

    return TransactionType.UNDEFINED;
  }

  /**
   * This method returns an XML response when there is insufficient information
   * available in the request to determine what the request type is or parse the
   * request appropriately.
   * 
   * @return
   */
  public static String generateGenericError(DTIErrorCode dtiError, String requestXML) {

    DTIErrorTO errorTO = ErrorKey.getErrorDetail(dtiError);
    Date today = new Date();

    if (requestXML == null) {
      requestXML = "";
    }

    // Create DTI payload ID
    SimpleDateFormat pdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
    String payloadSuffix = pdf.format(today);
    String payloadID = "999" + payloadSuffix;

    // Get TSPayloadID if available
    String tsPayloadID = UtilityXML.getTagData(requestXML, "PayloadID");
    if ((tsPayloadID == null) || (tsPayloadID.length() == 0)) {
      tsPayloadID = payloadID;
    }

    // Get Target, if available
    String target = UtilityXML.getTagData(requestXML, "Target");
    if ((target == null) || (target.length() == 0)) {
      target = "Unknown";
    }

    StringBuffer resp = new StringBuffer();
    resp.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    resp.append("<Transmission><Payload><PayloadHeader><PayloadID>" + payloadID + "</PayloadID>");
    resp.append("<TSPayloadID>" + tsPayloadID + "</TSPayloadID>");
    resp.append("<Target>" + target + "</Target><Version>1.0</Version>");
    resp.append("<Comm><Protocol>IP</Protocol><Method>Network</Method></Comm><TransmitDate>");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String dateString = sdf.format(today);

    resp.append(dateString);
    resp.append("</TransmitDate><TransmitTime>");

    // Because of ticket seller limitations, the time format must be
    // HH:mm:ss.SS, but .SSS is
    // always generated. The string is therefore clipped at the end to make
    // it
    // acceptable.
    sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    String tempTimeString = sdf.format(today);
    String timeString = tempTimeString.substring(0, (tempTimeString.length() - 1));

    resp.append(timeString);
    resp.append("</TransmitTime><TktBroker>" + tktBroker + "</TktBroker><CommandCount>0</CommandCount>");

    resp.append("<PayloadError><HdrErrorCode>" + errorTO.getErrorCode().toString() + "</HdrErrorCode>");
    resp.append("<HdrErrorType>" + errorTO.getErrorClass() + "</HdrErrorType>");
    resp.append("<HdrErrorClass>" + errorTO.getErrorType() + "</HdrErrorClass>");
    resp.append("<HdrErrorText>" + errorTO.getErrorText() + "</HdrErrorText></PayloadError>");
    resp.append("</PayloadHeader></Payload></Transmission>");

    String response = resp.toString();

    return response;
  }

  /**
   * Get the broker name from the environment. 
   * @return
   */
  private String getBrokerName()  {
      Map<String, String> env = System.getenv();
      
      String computerName = "DTIUNK";
      
      if (env.containsKey("COMPUTERNAME")) {
        computerName = env.get("COMPUTERNAME");
      } else if (env.containsKey("HOSTNAME")) {
        computerName = env.get("HOSTNAME");
      } else {
        return computerName;
      }
      
      char[] nameArray = computerName.toCharArray();
      StringBuffer suffixArray = new StringBuffer();
      boolean foundDigit = false;
        
      // Get only the numeric portion of the value...
      for (/*each*/ char aChar: /*in*/ nameArray) {
          
          if (Character.isDigit(aChar)) {
            foundDigit = true;  
          }
          
          if (foundDigit == true) {
            suffixArray.append(Character.toUpperCase(aChar));
          }
        
      }

      computerName = "DTI" + suffixArray.toString();

      return computerName;
  }
  
}
