package pvt.disney.dti.gateway.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LogKey;
import pvt.disney.dti.gateway.dao.LogTransaction;
import pvt.disney.dti.gateway.dao.SequenceKey;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.service.DTIService;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.DTITracker;
import pvt.disney.dti.gateway.util.DateTime;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;
import pvt.disney.dti.gateway.util.flood.KeyBlockException;
import pvt.disney.dti.gateway.util.flood.KeySuppressException;

import com.disney.context.BaseContext;
import com.disney.context.ContextManager;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/**
 * The DTI Controller is the class where all gates (HTTP, Web-Services) drop
 * their XML for processing.
 * 
 * @author lewit019 and others
 * @since 2.16.3
 * 
 */
public class DTIController {

  // CONSTANTS
  /** The standard core logging mechanism. */
  private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

  // Rework text Added for 2.16.3, JTL
  private final static String REWORK_TEXT = "Rework of ";
  
  private DTIFloodControl floodControl = null;

  /** Properties variable to store properties from AbstractInitializer. */
  private Properties props = null;

  private DateTime dt = null;

  /**
   * The ticket broker currently in use. Defaults to DTIUK if properties can't
   * be read.
   */
  private static String tktBroker = "DTIUK";

  // Boolean controlling flood blocking being active
  private static final boolean FLOODCONTROLACTIVE = true;

  // Boolean controlling flood blocking denying those who flood
  private static final boolean FLOODCONTROLDENY = true;

  /**
   * A string of comma separated values indicating TSMAC's that are not
   * permitted on this end-point.
   */
  private static String tsMacExcludeList = "";

  /**
   * Constructor for DTIApp
   */
  public DTIController() {
    super();

    // Get properties manager.
    try {
      ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
      props = ResourceLoader.convertResourceBundleToProperties(rb);
    } catch (Throwable e) {
      eventLogger.sendEvent("THROWABLE initing props: " + e.toString(), EventType.FATAL, this);
    }

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (Exception e) {
      eventLogger.sendEvent("Unable to init flood control:  " + e.toString(), EventType.WARN, this);
    }

    tktBroker = PropertyHelper.readPropsValue(PropertyName.POS_TKT_BROKER, props, null);

    /** Forbid certain sellers from reaching this end-point. */
    tsMacExcludeList = PropertyHelper.readPropsValue(PropertyName.TSMAC_EXCLUSION, props, null);

    return;
  }

  /**
   * It gets the XML from the in-bound message, performs some basic validation, logs the request, and determines if an answer from this XML request is already on file (rework). Then, it determines the correct path for the request, returns
   * the response string to the caller.
   * 
   * Note: There is a PCI control in this method to obliterate the credit card information prior to logging the request.
   * 
   * @param inStream
   *            the XML request
   * @return An XML Document response
   * @throws IOException
   *             in case of communications issues.
   */
  @SuppressWarnings("rawtypes")
  public Document processRequest(InputStream inStream) throws IOException {

    eventLogger.sendEvent("Entering processRequest(InputStream) ",
        EventType.DEBUG, this);

    String strInputFile = null;
    Document docIn = null;
    Document docOut = null;
    String payloadId = null;
    String tsMac = null;
    String tsLocation = null;
    boolean blockedTransaction = false;
    XMLSerializer strser = null;
    //DBTransaction dbTran = null;
    String target = null;
    Integer transIdITS = null;
    Integer tpRefNumber = null;
    String maskedXMLRequest = null;

    try {

      // get the XML from the in-bound message
      try {

        docIn = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
            .newInstance().newDocumentBuilder()
            .parse(new InputSource(inStream));

      }
      catch (Exception e) {
        // If we fail here, there's nothing to log, simply return bad XML.
        eventLogger.sendEvent("Error in incoming XML: " + e,
            EventType.WARN, this);
        DTIException dtie = new DTIException(
            "Error in XML from ticket seller.",
            getClass(),
            3,
            DTIErrorCode.INVALID_MSG_ELEMENT.getErrorCode(),
            "XML is not well formed", null);
        docOut = buildErrorXMLForPOS((DTIException) dtie, payloadId);
        return docOut;
      }
      finally {
        inStream.close();
      }

      // Create a string version of the request.
      StringWriter strwOut = new StringWriter();
      if (strwOut != null && docIn != null) {
        strser = new XMLSerializer(strwOut, null);
        strser.serialize(docIn);
        strInputFile = strwOut.toString();
        strwOut.close();
      }

      // PCI Control - DO NOT REMOVE!!!
      maskedXMLRequest = PCIControl.overwritePciDataInXML(strInputFile);
      eventLogger.sendEvent("Ticket seller request is: " + maskedXMLRequest,
          EventType.INFO, this);

      // get the payLoad ID, entity Name, StoreId, and other key fields
      // from the XML
      if (docIn != null) {
        eventLogger.sendEvent("Calling parseDoc() to extract key fields from XML.",
            EventType.DEBUG, this);
        HashMap parsedDoc = TiXMLHandler.parseDoc(docIn);
        payloadId = (String) parsedDoc.get(TiXMLHandler.PAYLOAD_ID);
        tsMac = (String) parsedDoc.get(TiXMLHandler.TS_MAC);
        tsLocation = (String) parsedDoc.get(TiXMLHandler.TS_LOCATION);
        target = (String) parsedDoc.get(TiXMLHandler.TS_ENVIRONMENT);

        // Required edit (new as of 2.16.3, JTL)
        // Ensure that they payload ID is present and is between the 
        // standard 12 and 20 digits, numeric.
        if ((payloadId == null) || 
            (payloadId.length() < 12) || 
            (payloadId.length() > 20) ||
            (Pattern.matches("[a-zA-Z]+", payloadId))) {
          
          throw new DTIException(DTIController.class,
              DTIErrorCode.INVALID_MSG_CONTENT,
              "PayloadID was of invalid length or composition.");
          
        }
        
        // Using core logging, establish a way to track this
        // transaction by payloadID
        DTITracker dtitr = new DTITracker();
        dtitr.setPayloadId(payloadId);
        BaseContext bctx = new BaseContext(dtitr);
        ContextManager.getInstance().storeContext(bctx);

        eventLogger.sendEvent("Key fields extracted from inbound XML.",
            EventType.DEBUG, this);

        // FLOOD CONTROL BEGINS ********************************************
        // Flood control works by deriving a key from components of the
        // transaction (payloadID is not included). If the key has occurred
        // more than is permissible, it's blocked for a period of time. The
        // first time it's blocked, a KeyBlockException is generated.
        // On subsequent attempts, it's returned by way of a
        // KeySuppressException.
        // Flood control will remove the block after a pre-configured period of
        // time.
        if (floodControl != null) {

          try {

            floodControl.setFloodControlActive(FLOODCONTROLACTIVE);

            floodControl.evaluateTransaction(parsedDoc);

          }
          catch (KeyBlockException kbe) {

            if (FLOODCONTROLDENY) {
              blockedTransaction = true;
            }
            else {
              eventLogger
                  .sendEvent(
                      "FLOOD CONTROL - THIS TRANSACTION WOULD HAVE BEEN BLOCKED.",
                      EventType.INFO, this);
            }

          }
          catch (KeySuppressException kse) {

            if (FLOODCONTROLDENY) {
              return null; // This forces an HTTP error.
            }
            else {
              eventLogger
                  .sendEvent(
                      "FLOOD CONTROL - THIS TRANSACTION WOULD HAVE BEEN SUPPRESSED.",
                      EventType.INFO, this);
            }
          }
          catch (Exception e) {
            eventLogger.sendEvent(
                "FLOOD CONTROL FAILED: " + e.toString(),
                EventType.EXCEPTION, this);
          }

        }
        // FLOOD CONTROL ENDS **********************************************

        // *******************
        // Record the request.
        // *******************
        // Obtain required key values for the database insert.
        try {
          transIdITS = SequenceKey.getITSTransId();
          tpRefNumber = SequenceKey.getTpRefNum();
        }
        catch (DTIException dtie) {
          eventLogger.sendEvent("EXCEPTION getting sequence numbers: " + dtie
                  .toString(), EventType.FATAL, this);
          throw dtie;
        }

        boolean isFirstAttempt = true;

        eventLogger.sendEvent("Attempting to record request.", EventType.DEBUG, this);

        // Get the entity ID.
        EntityTO entityTO = null;
        try {
          entityTO = EntityKey.getEntity(tsMac, tsLocation);
        }
        catch (DTIException dtie) {
          eventLogger.sendEvent("EXCEPTION getting entity lookup: " + dtie
                  .toString(), EventType.EXCEPTION, this);
          throw dtie;
        }

        try {
          isFirstAttempt = LogKey.insertITSLog(payloadId, entityTO, transIdITS, maskedXMLRequest);

          // Replace the transId with the one from the original attempt.
          if (!isFirstAttempt) {
            transIdITS = LogKey.getITSLogTransId(payloadId);
          }

          // Insert into DTI's consolidated log table.
          // If it fails, log & keep going. The key business processes do not
          // rely on this table.
          try {
            LogKey.insertDTITransLog(tpRefNumber, payloadId,entityTO, target, tktBroker, transIdITS,
                isFirstAttempt);
          }
          catch (DTIException dtie) {
            eventLogger
                .sendEvent("Unable to log ITS info into consolidated table:  " + dtie
                        .toString(), EventType.WARN,this);
          }

        }
        catch (DTIException dtie) {
          eventLogger.sendEvent("EXCEPTION writing to INBOUND_TS_LOG: " + dtie
                  .toString(), EventType.WARN, this);
          throw dtie;
        }

        eventLogger.sendEvent("Request recorded (or was dupe).", EventType.DEBUG, this);

        if (blockedTransaction) {
          DTIErrorCode dtiErrorCode = DTIErrorCode.TRANSACTION_FLOOD_BLOCKED;
          DTIException dtie = new DTIException(getClass(),
              dtiErrorCode, "Flood-blocked transaction");

          throw dtie;
        }

        // Handle rework.
        if (isFirstAttempt) { // it's not a duplicate
          eventLogger
              .sendEvent(
                  "Determined it's a new request.  Sending through DTI.",
                  EventType.INFO, this);
        }
        else { // it was a duplicate 
          
          // return the completed XML file if one exists.
          LogTransaction logTxn = new LogTransaction();
          String xmlString = logTxn.selectOTSLog(payloadId);
          if (xmlString != null) {
            docOut = getDocFromXMLString(xmlString,payloadId);
            addReworkText(docOut, payloadId);
            eventLogger.sendEvent("Rework response ready for ticket seller.", EventType.INFO, this);            
            return docOut;
          } else {
            eventLogger
                .sendEvent(
                    "No rework response found for duplicate.  Sending through DTI.",
                    EventType.INFO, this);
            // Replace "new" ITSLogTransId with one logged prior.
            transIdITS = LogKey.getITSLogTransId(payloadId);
          }
        }

        // *** SUBMIT THE REQUEST ***
        String xmlResponseMasked;
        String xmlRqstString = getDocumentToString(docIn);
        boolean isRework = !isFirstAttempt;
        String xmlResponse = DTIService.submitRequest(xmlRqstString,
            entityTO, transIdITS, tpRefNumber, isRework);

        // PCI Control - DO NOT REMOVE!!!
        xmlResponseMasked = PCIControl
            .overwritePciDataInXML(xmlResponse);

        eventLogger.sendEvent(
            "About to create XML Document from response string.",
            EventType.DEBUG, this);

        try {
          docOut = DocumentBuilderFactoryImpl
              .newInstance()
              .newDocumentBuilder()
              .parse(new InputSource(DTIFormatter
                  .getStringToInputStream(xmlResponseMasked)));
        }
        catch (Exception e) {
          throw new DTIException(
              "doPost()...error in XML from DTI",
              getClass(),
              tpRefNumber,
              DTIErrorCode.INVALID_MSG_ELEMENT.getErrorCode(),
              e.getMessage(), e);
        }

        eventLogger.sendEvent(
            "XML response to seller: " + xmlResponseMasked,
            EventType.INFO, this);

      }

    }
    catch (Exception ee) {

      eventLogger.sendEvent(
          "EXCEPTION processing request " + ee.toString(),
          EventType.EXCEPTION, this);
      ee.printStackTrace();

      if (!(ee instanceof DTIException)) {
        ee = new DTIException("processRequest", getClass(), 3,
            DTIErrorCode.UNABLE_TO_COMMUNICATE.getErrorCode(),
            ee.getMessage(), ee);
      }

      if (ee instanceof DTIException) {
        // check for error, generate XML for POS and to Error Queue, log to DB
        // and to log queue and return
        DTIException dtie = (DTIException) ee;
        docOut = buildErrorXMLForPOS(dtie, payloadId);
        Integer transIdOTS = null;

        try {
          transIdOTS = SequenceKey.getOTSTransId();
          logOutbound(getDocumentToString(docOut), dtie, payloadId,transIdOTS);
        }
        catch (DTIException ef) {
          ef.printStackTrace();
          docOut = buildErrorXMLForPOS((DTIException) ef, payloadId);
        }

        // Allow very permissive exception handling here to ensure seller
        // gets a response. Logging to the DTI TRANS LOG is OPTIONAL!
        try {
          DTIErrorCode dtiErrorCode = dtie.getDtiErrorCode();
          // Update in DTI Common Trans Log
          if (tpRefNumber != null) {

            TransactionType requestType = DTIService
                .findRequestType(maskedXMLRequest);

            LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTS,
                dtiErrorCode.getErrorCode(),
                dtiErrorCode.getErrorName(), null, null,
                new java.util.Date(), requestType, "Unknown");
          }

        }
        catch (Exception e) {
          e.printStackTrace();
        }
        catch (Throwable t) {
          t.printStackTrace();
        }

      }
    }
    finally {
      // Get rid of logging context (prevents an object leak).
      ContextManager.getInstance().removeContext();
    }

    return docOut;
  }

  /**
   * Builds the XML to be sent to POS when an error occurs
   * 
   * @return org.w3c.Document
   */
  private Document buildErrorXMLForPOS(DTIException e, String newPayloadId) {

    eventLogger.sendEvent("Entering buildErrorXMLForPOS(,)", EventType.DEBUG, this);

    // build the Document that goes back to POS
    // to be consistent we first ensure the insert
    // was performed then write the file to disk
    Document returnData = null;
    StringBuffer sb = null;
    dt = new DateTime();

    try {
      // Retrieve error info from DB
      eventLogger.sendEvent("Building error XML for ticket seller because: " + e + " Using PayloadID: " + newPayloadId,
          EventType.WARN, this);
      DTIErrorTO dtiErrorTO = ErrorKey.getErrorDetail(e.getDtiErrorCode());
      eventLogger.sendEvent("Getting error text from database for ERROR: " + e.getCode(), EventType.INFO, this);

      if (dtiErrorTO != null) {
        eventLogger.sendEvent("Got error data from DB...populating XML", EventType.DEBUG, this);
        sb = new StringBuffer(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transmission><Payload><PayloadHeader><PayloadID>"
                + newPayloadId
                + "</PayloadID><TSPayloadID>"
                + newPayloadId
                + "</TSPayloadID><Target>"
                + getProperty(PropertyName.POS_TARGET)
                + "</Target><Version>1.0</Version><Comm><Protocol>IP</Protocol><Method>Network</Method></Comm><TransmitDate>"
                + dt.getYear() + "-" + dt.getMonth() + "-" + dt.getDay() + "</TransmitDate><TransmitTime>"
                + dt.getHour() + ":" + dt.getMinute() + ":" + dt.getSecond() + "." + dt.getMilli().substring(0, 2)
                + "</TransmitTime><TktBroker>" + getProperty(PropertyName.POS_TKT_BROKER)
                + "</TktBroker><CommandCount>0</CommandCount><PayloadError><HdrErrorCode>"
                + (String) dtiErrorTO.getErrorCode().toString() + "</HdrErrorCode><HdrErrorType>"
                + (String) dtiErrorTO.getErrorType() + "</HdrErrorType><HdrErrorClass>"
                + (String) dtiErrorTO.getErrorClass() + "</HdrErrorClass><HdrErrorText>"
                + (String) dtiErrorTO.getErrorText()
                + "</HdrErrorText></PayloadError></PayloadHeader></Payload></Transmission>");

        // check to see if it is supposed to be sent back to POS
        try {
          returnData = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder()
              .parse(new InputSource(DTIFormatter.getStringToInputStream(sb.toString())));
        } catch (Exception ex) {
          eventLogger.sendEvent("EXCEPTION instantiating response XML: " + ex.toString(), EventType.WARN, this);
        }

        eventLogger.sendEvent("Error XML for ticket seller is: " + sb.toString(), EventType.INFO, this);
      }
    } catch (Exception ee) {
      eventLogger.sendEvent("EXCEPTION when retrieveing error data: " + ee.toString(), EventType.WARN, this);
      try {
        sb = new StringBuffer(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transmission><Payload><PayloadHeader><PayloadID>"
                + newPayloadId
                + "</PayloadID><TSPayloadID>"
                + newPayloadId
                + "</TSPayloadID><Target>"
                + getProperty(PropertyName.POS_TARGET)
                + "</Target><Version>1.0</Version><Comm><Protocol>IP</Protocol><Method>Network</Method></Comm><TransmitDate>"
                + dt.getYear() + "-" + dt.getMonth() + "-" + dt.getDay() + "</TransmitDate><TransmitTime>"
                + dt.getHour() + ":" + dt.getMinute() + ":" + dt.getSecond() + "." + dt.getMilli().substring(0, 2)
                + "</TransmitTime><TktBroker>" + getProperty(PropertyName.POS_TKT_BROKER)
                + "</TktBroker><CommandCount>0</CommandCount><PayloadError><HdrErrorCode>"
                + DTIErrorCode.FAILED_DB_OPERATION_GATE.getErrorCode() + "</HdrErrorCode><HdrErrorType>" + "Critical"
                + "</HdrErrorType><HdrErrorClass>" + "Database" + "</HdrErrorClass><HdrErrorText>"
                + DTIErrorCode.FAILED_DB_OPERATION_GATE.getErrorName()
                + "</HdrErrorText></PayloadError></PayloadHeader></Payload></Transmission>");

        // check to see if it is supposed to be sent back to POS
        try {
          returnData = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder()
              .parse(new InputSource(DTIFormatter.getStringToInputStream(sb.toString())));
        } catch (Exception es) {
          eventLogger.sendEvent("EXCEPTION instantiating error XML for ticket seller: " + es.toString(),
              EventType.WARN, this);
        }
      } catch (Exception es) {
        eventLogger.sendEvent("EXCEPTION building error XML for ticket seller: " + es.toString(), EventType.WARN, this);
      }
    }

    return returnData;
  }

  /**
   * Logs an error to the out-bound error log.
   * 
   * @param xml
   *            The XML error being returned.
   * @param e
   *            The DTIException encountered.
   * @param payloadId
   * @throws DTIException
   */
  private void logOutbound(String xml, DTIException e, String payloadId,
      Integer otsLogKey) throws DTIException {

    eventLogger.sendEvent("Entering logOutbound(,,)", EventType.DEBUG, this);

    Integer transIdITS = LogKey.getITSLogTransId(payloadId);
    // Since 2.9 - JTL
    // If there is no inbound_transid, then don't put the output in the database
    // table (as we haven't put the request in there, either).
    if (transIdITS == null) {
      return;
    }

    // Get Error code
    String errorCode = null;
    if (e != null) {
      if (e.getCode().length() > 2) {
        errorCode = e.getCode();
      }
    }

    LogKey.insertOTSLogError(otsLogKey,  payloadId,  xml,  errorCode, transIdITS);
    
    return;
  }

  /**
   * Converts an XML document into String format by using the XMLSerializer.
   * 
   * @param doc
   * @return String version of the XML document.
   */
  private String getDocumentToString(Document doc) {

    eventLogger.sendEvent("Entering getDocumentToString(Document)",
        EventType.DEBUG, this);

    // given a string write to file and instantiate
    String returnData = null;
    StringWriter sw = new StringWriter();
    XMLSerializer ser = null;
    if (sw != null && doc != null) {
      try {
        ser = new XMLSerializer(sw, null);
        ser.serialize(doc);
        sw.flush();
        sw.close();
      }
      catch (Exception e) {
        eventLogger
            .sendEvent(
                "EXCEPTION converting response Document to String: " + e
                    .toString(), EventType.WARN, this);
      }
    }
    returnData = sw.getBuffer().toString();
    return returnData;
  }

  /**
   * Adds the rework text to the response message.
   * 
   * @param doc
   * @param newPayloadId
   */
  private void addReworkText(Document doc, String newPayloadId) {
    // add the rework text to the message 
    Element payloadNote = doc.createElement(TiXMLHandler.PAYLOAD_NOTE);
    Text payloadNoteText = doc.createTextNode(REWORK_TEXT + newPayloadId);
    payloadNote.appendChild(payloadNoteText);
    NodeList nl = doc.getElementsByTagName(TiXMLHandler.PAYLOAD_HEADER);
    Node n = nl.item(0);
    n.appendChild(payloadNote);

  }


   /**
   * Retrieves XML response for a rework that is complete.
   * 
   * @param dbTransaction
   *            A connection object.
   * @param newPayloadId
   * @return An XML document.
   * @throws DTIException
   *             for any problem creating response XML.
   */
  private Document getDocFromXMLString(String completeXML, String payloadId) throws DTIException {

    eventLogger.sendEvent("Entering getDocFromResultSet(,)",EventType.DEBUG, this);

    Document returnDoc = null;
    eventLogger.sendEvent(
        "Retrieved original response for rework: " + completeXML, EventType.INFO, this);
    eventLogger.sendEvent(
            "About to Instantiate XML from OutBound TS LOG for payloadId: " + payloadId,
            EventType.DEBUG, this);
    
    try {
      InputStream istr = DTIFormatter.getStringToInputStream(completeXML);
      InputSource isrc = new InputSource(istr);
      returnDoc = DocumentBuilderFactoryImpl.newInstance()
          .newDocumentBuilder().parse(isrc);

      eventLogger
          .sendEvent(
              "Finished Instantiation XML from OutBound TS LOG for payloadId: " + payloadId,
              EventType.DEBUG, this);

    } catch (Exception e) {
      eventLogger
          .sendEvent(
              "EXCEPTION creating XML from OutBound TS LOG for payloadId " + payloadId + ":  " + e
                  .toString(), EventType.WARN, this);
      eventLogger.sendEvent("EXCEPTION instantiating XML was: " + e.toString(),
          EventType.WARN, this);
      throw new DTIException("EXCEPTION in XML from OUTBOUND_TS_LOG", getClass(), 3,
          DTIErrorCode.INVALID_MSG_ELEMENT.getErrorCode(), "XML is not Well Formed", null);
    }

    if (returnDoc == null) {
      throw new DTIException(
          "EXCPETION in XML from OUTBOUND_TS_LOG (returnDoc is null)",
          getClass(), 3, DTIErrorCode.INVALID_MSG_ELEMENT.getErrorCode(),
          "XML is not Well Formed", null);
    }
    
    return returnDoc;
  }
  
  
  /**
   * Returns properties loaded at runtime. Normally, you wouldn't incur the penalty of a second stack call here, but the properties are scattered throughout the XML. In those difficult areas, this call allows for cleaner code.
   * 
   * @param key
   *            java.lang.String
   */
  private String getProperty(String key) {

    String value = PropertyHelper.readPropsValue(key, props, null);
    return value;

  }
}
