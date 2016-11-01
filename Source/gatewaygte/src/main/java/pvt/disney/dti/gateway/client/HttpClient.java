package pvt.disney.dti.gateway.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/** This class is responsible for sending the request to the HTTPS target.
 * @author lewit019
 * @version 4
 */
@SuppressWarnings("deprecation")
public class HttpClient {

  /** The error code for an IO Exception while sending to https server. */
  public final static String IO_ERR_CODE = new String("WALC-001");

  /** The error text for an IO Exception while sending to https server. */
  public final static String IO_ERR_TEXT = new String(
      "Client IOException while sending to https server.");

  EventLogger eventLogger;

  /**
   * Constructor for HTTPController. Gets the logger for the class.
   *  
   */
  public HttpClient() {
    eventLogger = EventLogger.getLogger(this.getClass());
  }

  /**
   * This method sends the request XML passed in via the InputStream to the
   * target server identified in the WALC.properties.
   * 
   * @param in -
   *            an instance of java.io.InputStream containing the XML to be
   *            sent to the target server.
   * @return an instance of org.w3c.dom.Document containing the either the
   *         response or an error XML.
   */
  public Document processRequest(InputStream in) {

    Document docResponse = null;
    Document docRequest = null;
    XMLSerializer ser = null;
    URLConnection conn = null;
    String messageIdString = null;
    //    System.setProperty("javax.net.debug", "all");
    //    System.setProperty("java.security.debug", "all");

    try {

      eventLogger.sendEvent(
          "About to Initialize DTICLIENT.properties file ...",
          EventType.DEBUG, this);

	    ResourceBundle rb = ResourceLoader.getResourceBundle("DTICLIENT");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
      
      eventLogger.sendEvent(
          "Successfully Initialized DTICLIENT.properties file ...",
          EventType.DEBUG, this);

      // create the document request
      eventLogger.sendEvent("About to build docRequest ...",
          EventType.DEBUG, this);
      DocumentBuilderFactory docFactory = DocumentBuilderFactoryImpl
          .newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      docRequest = docBuilder.parse(in);
      eventLogger.sendEvent("Successfully initialized docRequest ...",
          EventType.DEBUG, this);

      URL url = new URL(PropertyHelper.readPropsValue(PropertyHelper
          .readPropsValue("ACTIVECONN", props, null), props, null));

      //Open Connection to eGalaxy
      conn = (URLConnection) url.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);

      eventLogger.sendEvent("Connection established to URL: "
          + conn.getURL().toString(), EventType.DEBUG, this);

      //Sending Request to eGalaxy
      OutputStream out = conn.getOutputStream();
      ser = new XMLSerializer(out,
          new OutputFormat("xml", "UTF-8", false));
      ser.serialize(docRequest);
      out.close();

      // MASK OUT <Endorsement>ZZZZZZZZZZZZZZZZ</Endorsement>
      //Used for Debugging
      String dOut = DTIFormatter.getDocumentToString(docRequest);
      String xmlMasked = PCIControl.overwritePciDataInXML(dOut);
      eventLogger.sendEvent("Document being sent TO http server: "
          + xmlMasked, EventType.DEBUG, this);

      //Getting response from eGalaxy
      InputStream response = conn.getInputStream();

      // Create the document response
      docFactory = DocumentBuilderFactoryImpl.newInstance();
      docBuilder = docFactory.newDocumentBuilder();
      docResponse = docBuilder.parse(response);

    } catch (SAXParseException se) {
      eventLogger.sendEvent("Error in incoming xml: " + se.toString(),
          EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION,
          ErrorCode.XML_PARSE_EXCEPTION, se, this);
    } catch (IOException ioe) {
      // Event Log something
      eventLogger.sendEvent("IOException sending xml: " + ioe.toString(),
          EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION,
          ErrorCode.MESSAGING_EXCEPTION, ioe, this);
      // return error XML
      docResponse = HttpClient.generateErrorXML(IO_ERR_CODE,
          IO_ERR_TEXT, messageIdString);
    } catch (Exception e) {
      eventLogger.sendEvent("Error:" + e.toString(), EventType.EXCEPTION,
          this);
      eventLogger.sendException(EventType.EXCEPTION,
          ErrorCode.APPLICATION_EXCEPTION, e, this);
    }
    eventLogger
        .sendEvent("Exiting processRequest()", EventType.DEBUG, this);
    return docResponse;
  }

  /**
   * This method generates an error XML document containing specific error
   * information.
   * 
   * @param errorCode
   *            The string which represents the error code.
   * @param errorMessage
   *            The string which represents the explanation text of the error.
   * @param messageId
   *            The numeric identifier of the orginal request message.
   * @return Document containing error XML. //
   */
  protected static Document generateErrorXML(String errorCode,
      String errorMessage, String messageId) {

    Document docResponse = null;

    // Format the current time.
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentTime_1 = new Date();
    String dateString = formatter.format(currentTime_1);

    String ts = new String("<?xml version=\"1.0\"?>");
    ts = ts.concat("<Envelope>");
    ts = ts.concat("<Header>");
    ts = ts.concat("<MessageID>" + messageId + "</MessageID>");
    ts = ts.concat("<MessageType>ActivateTicketResponse</MessageType>");
    ts = ts.concat("<SessionID>1</SessionID>");
    ts = ts.concat("<SourceID>SWY</SourceID>");
    ts = ts.concat("<TimeStamp>" + dateString + "</TimeStamp>");
    ts = ts.concat("</Header>");
    ts = ts.concat("<Body>");
    ts = ts.concat("<Status>");
    ts = ts.concat("<StatusCode>1400</StatusCode>");
    ts = ts
        .concat("<StatusText>Ticket activation request error</StatusText>");
    ts = ts.concat("</Status>");
    ts = ts.concat("<TicketActivationErrors>");
    ts = ts.concat("<TicketActivationError>");
    ts = ts.concat("<VisualID/>");
    ts = ts.concat("<StatusCode>" + errorCode + "</StatusCode>");
    ts = ts.concat("<StatusText>" + errorMessage + "</StatusText>");
    ts = ts.concat("</TicketActivationError>");
    ts = ts.concat("</TicketActivationErrors>");
    ts = ts.concat("</Body>");
    ts = ts.concat("</Envelope>");

    ByteArrayInputStream bais = new ByteArrayInputStream(ts.getBytes());
    DocumentBuilderFactory docFactory = DocumentBuilderFactoryImpl
        .newInstance();

    try {
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      docResponse = docBuilder.parse(bais);
    } catch (SAXException sae) {
      // Log something helpful
      System.err.println("SAXException creating error response: "
          + sae.toString());
    } catch (ParserConfigurationException pce) {
      // Log something helpful
      System.err
          .println("ParserConfigurationException creating error response: "
              + pce.toString());
    } catch (IOException ioe) {
      // Log something helpful
      System.err.println("IOException creating error response: "
          + ioe.toString());
    } catch (Exception e) {
      // Log something helpful
      System.err.println("Exception creating error response: "
          + e.toString());
    }

    return docResponse;
  }

}
