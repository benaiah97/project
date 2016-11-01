package pvt.disney.dti.gateway.common;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.util.PropertyHelper;
//import com.disney.logging.EventLogger;
//import com.disney.logging.audit.EventType;

/**
 * The TiXMLHandler class performs XML validations and other XML- related
 * processes of behalf of the DTI.
 * 
 * Program to count the number of elements using only the localName component of
 * the element, in an XML document. Name space names are ignored for simplicity.
 * This example also shows one way to turn on validation and how to use a SAX
 * ErrorHandler.
 * 
 * Notes: DefaultHandler is a SAX helper class that implements the SAX
 * ContentHandler interface by providing no-operation methods. This class overrides
 * some of the methods by extending DefaultHandler. This program turns on
 * name space processing and uses SAX2 interfaces to process XML documents which
 * may or may not be using name spaces.
 * 
 * Update 2002-04-18: Added code that shows how to use JAXP 1.2 features to
 * support W3C XML Schema validation. See the JAXP 1.2 maintenance review
 * specification for more information on these features.
 * 
 * @author Edwin Goei
 * @version %version: 2 %
 * 
 */
public class TiXMLHandler extends DefaultHandler {

  public static final String UPGRADE_ALPHA      = "UpgradeAlpha           ";
  public static final String VOID_TICKET        = "VoidTicket             ";
  public static final String QUERY_TICKET       = "QueryTicket            ";
  public static final String RESERVATION        = "Reservation            ";
  public static final String QUERY_RESERVATION  = "QueryReservation       ";
  public static final String CREATEALPHA        = "CreateAlpha            ";
  public static final String UPDATETICKET       = "UpdateTicket           ";
  public static final String UPDATETRANSACTION  = "UpdateTransaction      ";
  public static final String UPGRADEENTITLEMENT = "UpgradeEntitlement     "; // 2.10
  public static final String UPGRADE_ALPHA_REQUEST = "UpgradeAlphaRequest";
  public static final String VOID_TICKET_REQUEST = "VoidTicketRequest";
  public static final String QUERY_TICKET_REQUEST = "QueryTicketRequest";
  public static final String RESERVATION_REQUEST = "ReservationRequest";
  public static final String CREATE_ALPHA_REQUEST = "CreateTicketRequest";
  public static final String UPDATE_TICKET_REQUEST = "UpdateTicketRequest";
  public static final String QUERY_RESERVATION_REQUEST = "QueryReservationRequest";
  public static final String UPDATE_TRANSACTION_REQUEST = "UpdateTransactionRequest";
  public static final String UPGRADE_ENTITLEMENT_REQUEST = "UpgradeEntitlementRequest"; // 2.10
  public static final String PAYLOAD_ID = "PayloadID";
  public static final String TS_MAC = "TSMAC";
  public static final String TS_LOCATION = "TSLocation";
  public static final String TS_VERSION = "Version";
  public static final String TS_ENVIRONMENT = "Target";
  public static final String PAYLOAD_NOTE = "PayloadNote";
  public static final String PAYLOAD_HEADER = "PayloadHeader";
  public static final String ACTION = "Action";
  public static final String TXN_TYPE = "TransactionType";
  public static final String PRODCODE = "ProdCode";
  public static final String RESCODE = "ResCode";
  public static final String MAGTRACK1 = "MagTrack1";
  public static final String BARCODE = "Barcode";
  public static final String TKTDATE = "TktDate";
  public static final String TKTSITE = "TktSite";
  public static final String TKTSTATION = "TktStation";
  public static final String TKTNBR = "TktNbr";
  public static final String TKTNID = "TktNID";
  public static final String EXTERNAL = "External";
  public static final String PRODUCTS = "Products";
  public static final String RESCODES = "ResCodes";
  public static final String TICKETS = "Tickets";

  /** The standard core logging mechanism. */
  //private EventLogger eventLogger = EventLogger.getLogger(this.getClass());
  /** A Hash table with tag names as keys and Integers as values */
  private Hashtable<String, Integer> tags;
  private boolean xsdValidate = true;
  private String inputFile;

  public TiXMLHandler(String xmlFile) {
    inputFile = xmlFile;
  }

  // TO DO: Does this replace the same code in the controller?
  public static Document parseXML(InputStream inStream) throws Exception {

    Document xmlDoc;

    xmlDoc = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder()
        .parse(new InputSource(inStream));

    return xmlDoc;
  }

  /**
   * Parser calls this once at the beginning of a document.
   * 
   */
  public void startDocument() throws SAXException {
    tags = new Hashtable<String, Integer>();
  }

  /**
   * Parser calls this for each element in a document.
   * 
   */
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
      throws SAXException {
    String key = localName;
    Object value = tags.get(key);
    if (value == null) {
      // Add a new entry
      tags.put(key, new Integer(1));
    } else {
      // Get the current count and increment it
      int count = ((Integer) value).intValue();
      count++;
      tags.put(key, new Integer(count));
    }
  }

  /**
   * Parser calls this once after parsing a document
   * 
   */
  public void endDocument() throws SAXException {
  } // endDocument

  /**
   * Parses against the XSD (if configured) and for checking it is well-formed.
   * 
   * @throws DTIException
   */
  public void validate() throws DTIException {

    //eventLogger.sendEvent("Entering validate()", EventType.DEBUG, this);

    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

    try {

      // Create a JAXP SAXParserFactory and configure it
      SAXParserFactory spf = SAXParserFactory.newInstance();

      // Set namespaceAware to true to get a parser that corresponds to
      // the default SAX2 name space feature setting. This is necessary
      // because the default value from JAXP 1.0 was defined to be false.
      spf.setNamespaceAware(true);
      // Validation part 1: set whether validation is on
      spf.setValidating(xsdValidate);
      // Create a JAXP SAXParser
      SAXParser saxParser = spf.newSAXParser();

      // Validation part 2b: Set the schema source, if any. See the JAXP
      // 1.2 maintenance update specification for more complex usages of
      // this feature.

      // Get the encapsulated SAX XMLReader
      XMLReader xmlReader = saxParser.getXMLReader();

      // Added feature set for WS 5.1 upgrade ------ JTL
      xmlReader.setFeature("http://apache.org/xml/features/validation/schema", true);
      // End ------ JTL

      // Set the ContentHandler of the XMLReader
      xmlReader.setContentHandler(new TiXMLHandler(inputFile));

      // Set an ErrorHandler before parsing
      xmlReader.setErrorHandler(new MyErrorHandler(System.err));

      // Tell the XMLReader to parse the XML document
      InputStream inStream = DTIFormatter.getStringToInputStream(inputFile);
      InputSource inSource = new InputSource(inStream);
      xmlReader.parse(inSource);

    } catch (Exception e) {
      throw new DTIException("validate()", this.getClass(), 3, PropertyHelper.readPropsValue(
          PropertyName.ERROR_CODE_VALIDATION_VALID, props, null), e.getMessage(), e);
    }
  }

  /**
   * Extracts key data values from the inbound XML.
   * 
   * @param doc
   * @return a HashMap of key/value pairs.
   */
  public static HashMap<String, Object> parseDoc(Document doc) {

    TransactionType txnType;
    ArrayList<String> valueList;
    HashMap<String, Object> returnData = new HashMap<String, Object>();

    // Get the PayloadId
    valueList = getValueByTagName(doc, PAYLOAD_ID);
    if (valueList.size() != 0)
      returnData.put(PAYLOAD_ID, valueList.get(0));

    // Get the TS_MAC
    valueList = getValueByTagName(doc, TS_MAC);
    if (valueList.size() != 0)
      returnData.put(TS_MAC, valueList.get(0));

    // Get the TS_LOCATION
    valueList = getValueByTagName(doc, TS_LOCATION);
    if (valueList.size() != 0)
      returnData.put(TS_LOCATION, valueList.get(0));

    // Get the TS_ENVIRONMENT
    valueList = getValueByTagName(doc, TS_ENVIRONMENT);
    if (valueList.size() != 0)
      returnData.put(TS_ENVIRONMENT, valueList.get(0));

    // Get the TS_VERSION
    valueList = getValueByTagName(doc, TS_VERSION);
    if (valueList.size() != 0)
      returnData.put(TS_VERSION, valueList.get(0));

    // set the action and transaction type (which for now, are the same thing).
    // For legacy WMB and logging, action must be a string of length 20.
    // In future, DTI should transition to enumerated values (tag keys included)
    // either leaving the strings behind or putting them close to where logging
    // happens.
    txnType = TransactionType.UNDEFINED;

    if (doc.getElementsByTagName(UPGRADE_ALPHA_REQUEST) != null) {
      if (doc.getElementsByTagName(UPGRADE_ALPHA_REQUEST).getLength() > 0) {
        returnData.put(ACTION, UPGRADE_ALPHA);
        txnType = TransactionType.UPGRADEALPHA;
      }
    }
    if (doc.getElementsByTagName(VOID_TICKET_REQUEST) != null) {
      if (doc.getElementsByTagName(VOID_TICKET_REQUEST).getLength() > 0) {
        returnData.put(ACTION, VOID_TICKET);
        txnType = TransactionType.VOIDTICKET;
      }
    }
    if (doc.getElementsByTagName(QUERY_TICKET_REQUEST) != null) {
      if (doc.getElementsByTagName(QUERY_TICKET_REQUEST).getLength() > 0) {
        returnData.put(ACTION, QUERY_TICKET);
        txnType = TransactionType.QUERYTICKET;
      }
    }
    if (doc.getElementsByTagName(RESERVATION_REQUEST) != null) {
      if (doc.getElementsByTagName(RESERVATION_REQUEST).getLength() > 0) {
        returnData.put(ACTION, RESERVATION);
        txnType = TransactionType.RESERVATION;
      }
    }
    if (doc.getElementsByTagName(CREATE_ALPHA_REQUEST) != null) {
      if (doc.getElementsByTagName(CREATE_ALPHA_REQUEST).getLength() > 0) {
        returnData.put(ACTION, CREATEALPHA);
        txnType = TransactionType.CREATETICKET;
      }
    }
    if (doc.getElementsByTagName(UPDATE_TICKET_REQUEST) != null) {
      if (doc.getElementsByTagName(UPDATE_TICKET_REQUEST).getLength() > 0) {
        returnData.put(ACTION, UPDATETICKET);
        txnType = TransactionType.UPDATETICKET;
      }
    }
    if (doc.getElementsByTagName(UPDATE_TRANSACTION_REQUEST) != null) {
      if (doc.getElementsByTagName(UPDATE_TRANSACTION_REQUEST).getLength() > 0) {
        returnData.put(ACTION, UPDATETRANSACTION);
        txnType = TransactionType.UPDATETRANSACTION;
      }
    } 
    if (doc.getElementsByTagName(QUERY_RESERVATION_REQUEST) != null) {
      if (doc.getElementsByTagName(QUERY_RESERVATION_REQUEST).getLength() > 0) {
        returnData.put(ACTION, QUERY_RESERVATION);
        txnType = TransactionType.QUERYRESERVATION;
      }
    }
    if (doc.getElementsByTagName(UPGRADE_ENTITLEMENT_REQUEST) != null) { // 2.10
        if (doc.getElementsByTagName(UPGRADE_ENTITLEMENT_REQUEST).getLength() > 0) {
          returnData.put(ACTION, UPGRADEENTITLEMENT);
          txnType = TransactionType.UPGRADEENTITLEMENT;
        }
      }
    

    returnData.put(TXN_TYPE, txnType);

    // Get the products listed on the order
    if ((txnType == TransactionType.UPGRADEALPHA) || (txnType == TransactionType.RESERVATION)) {

      StringBuffer products = new StringBuffer();

      // Get all the PRODUCTS
      valueList = getValueByTagName(doc, PRODCODE);
      if (valueList.size() != 0) {
        for (int h = 0; h < valueList.size(); h++) {
          products.append(valueList.get(h));
        }
      }

      returnData.put(PRODUCTS, products.toString());

    }

    // This nesting prioritizes the most frequently used ticket versions
    // first for speed of processing.  Note, that this also relies on having one
    // and only one ticket per void ticket and query ticket.
    // In order, MAG Stripe, DSSN, External, Bar code, and NID
    if ((txnType == TransactionType.VOIDTICKET) || (txnType == TransactionType.QUERYTICKET)) {

      // MAGTRACK
      ArrayList<String> magTrack = getValueByTagName(doc, MAGTRACK1);
      if (magTrack.size() > 0) {
        returnData.put(TICKETS, magTrack.get(0));
      } else {

        // DSSN
        ArrayList<String> tktDate = getValueByTagName(doc, TKTDATE);
        if (tktDate.size() > 0) {
          ArrayList<String> tktSite = getValueByTagName(doc, TKTSITE);
          ArrayList<String> tktStation = getValueByTagName(doc, TKTSTATION);
          ArrayList<String> tktNumber = getValueByTagName(doc, TKTNBR);

          StringBuffer aDssnTicket = new StringBuffer(tktDate.get(0));
          if (tktSite.size() > 0)
            aDssnTicket.append(tktSite.get(0));
          if (tktStation.size() > 0)
            aDssnTicket.append(tktStation.get(0));
          if (tktNumber.size() > 0)
            aDssnTicket.append(tktNumber.get(0));

          returnData.put(TICKETS, aDssnTicket.toString());
        } else {

          // EXTERNALTKTCODE
          ArrayList<String> external = getValueByTagName(doc, EXTERNAL);
          if (external.size() > 0)
            returnData.put(TICKETS, external.get(0));
          else {

            // BARCODE
            ArrayList<String> barCode = getValueByTagName(doc, BARCODE);
            if (barCode.size() > 0)
              returnData.put(TICKETS, barCode.get(0));
            else {

              // NID
              ArrayList<String> tktnid = getValueByTagName(doc, TKTNID);
              if (tktnid.size() > 0)
                returnData.put(TICKETS, tktnid.get(0));
            }
          }
        }
      }
    }
    
    // Get the reservation codes listed on the query reservation 
    // Should be 1, but this allows for expansion. 
    if (txnType == TransactionType.QUERYRESERVATION) {

      StringBuffer rescodes = new StringBuffer();

      // Get all the PRODUCTS
      valueList = getValueByTagName(doc, RESCODE);
      if (valueList.size() != 0) {
        for (int h = 0; h < valueList.size(); h++) {
          rescodes.append(valueList.get(h));
        }
      }

      returnData.put(RESCODES, rescodes.toString());

    }

    return returnData;
  }

  /**
   * 
   * @param doc
   * @param tagName
   * @return
   */
  private static ArrayList<String> getValueByTagName(Document doc, String tagName) {
    NodeList nl;
    ArrayList<String> list = new ArrayList<String>();

    nl = doc.getElementsByTagName(tagName);
    for (int h = 0; h < nl.getLength(); h++) {
      Node n = nl.item(h);
      if (n != null) {
        n = n.getFirstChild();
      }
      if (n != null) {
        list.add(n.getNodeValue());
      }
    }

    return list;

  }

}

/**
 * Error handler to report errors and warnings
 * 
 */
class MyErrorHandler implements ErrorHandler {

  /** Error handler output goes here */
  private PrintStream out;

  /**
   * 
   * @param out
   */
  MyErrorHandler(PrintStream out) {
    this.out = out;
  }

  /**
   * Returns a string describing parse exception details
   */
  private String getParseExceptionInfo(SAXParseException spe) {
    String systemId = spe.getSystemId();
    if (systemId == null) {
      systemId = "null";
    }
    String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
    return info;
  }

  /**
   * Standard SAX ErrorHandler method. See SAX documentation for more info.
   */
  public void warning(SAXParseException spe) throws SAXException {
    out.println("Warning: " + getParseExceptionInfo(spe));
  }

  /**
   * Standard SAX ErrorHandler method. See SAX documentation for more info.
   */
  public void error(SAXParseException spe) throws SAXException {
    String message = "Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);

  }

  /**
   * Standard SAX ErrorHandler method. See SAX documentation for more info.
   */
  public void fatalError(SAXParseException spe) throws SAXException {
    String message = "Fatal Error: " + getParseExceptionInfo(spe);
    throw new SAXException(message);
  }

}
