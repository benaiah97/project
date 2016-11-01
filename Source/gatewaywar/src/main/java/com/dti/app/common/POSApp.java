package com.dti.app.common;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/** This servlet powers the DTI Application Tester.
 * 
 * @author unknown
 * @version %version: %
 * 
 */
public class POSApp extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  /** The standard core logging mechanism. */
  private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

  /**
   * Calls doService.
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws javax.servlet.ServletException, java.io.IOException {

    doService(req, res);

  }

  /**
   * Calls doService.
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws javax.servlet.ServletException, java.io.IOException {

    doService(req, res);

  }

  /** Provides a way to create the test XML used by the DTI Application tester.
   * 
   * @param req
   * @param res
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  public void doService(HttpServletRequest req, HttpServletResponse res)
    throws javax.servlet.ServletException, java.io.IOException {

    eventLogger.sendEvent("Entering doService(,)", EventType.DEBUG, this);

    /* Properties variable to store properties */
  	ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
  	Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
    
    // for integration should be set to https to test ssl
    // final String DTI_APP_LOCATION = "http://localhost:8081/DTIApp/servlet/com.dti.app.common.DTIApp";
    // for unit test it will just hit the app
    String dtiAppLocation = req.getParameter("host");
    String fileName = req.getParameter("file");
    String directory = req.getParameter("folder");
    String payload = null;
    payload = req.getParameter("payload");

    String tempXml = PropertyHelper.readPropsValue(PropertyName.XML_FILE, props, null);

    String table = null;
    table = req.getParameter("table");
    URLConnection conn = null;
    XMLSerializer ser = null;
    Document docOut = null;
    Document docIn = null;

    try {
      if (table == null) {
        URL url = new URL(dtiAppLocation);
        eventLogger.sendEvent(
          "About to Instantiate XML on POS Side from File: " + fileName,
          EventType.DEBUG,
          this);
        docOut =
          org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(
            directory + fileName);
        eventLogger.sendEvent(
          "Done Instantiating XML on POS Side from File: " + fileName,
          EventType.DEBUG,
          this);
        conn = (URLConnection)url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        ser = new XMLSerializer(out, new OutputFormat("xml", "UTF-8", false));
        ser.serialize(docOut);
        out.close();
        docIn =
          org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(
            new InputSource(conn.getInputStream()));
        ser = new XMLSerializer(res.getOutputStream(), new OutputFormat("xml", "UTF-8", false));
        ser.serialize(docIn);

        //Document to stdout 
      } 
      
//      else {
//        String SQL = null;
//        if (table.equals("inboundTS")) {
//          SQL = "SELECT XML_DOC FROM INBOUND_TS_LOG WHERE TS_TRANSID = '" + payload + "'";
//        } else if (table.equals("inboundTP")) {
//          SQL =
//            "SELECT INBOUND_TP_LOG.XML_DOC FROM INBOUND_TP_LOG, INBOUND_TS_LOG WHERE INBOUND_TP_LOG.INBOUND_TS_ID = INBOUND_TS_LOG.TRANS_ID AND INBOUND_TS_LOG.TS_TRANSID = '"
//              + payload
//              + "'";
//        } else if (table.equals("outboundTS")) {
//          SQL =
//            "SELECT OUTBOUND_TS_LOG.XML_DOC FROM OUTBOUND_TS_LOG, INBOUND_TP_LOG, INBOUND_TS_LOG WHERE OUTBOUND_TS_LOG.INBOUND_TS_ID = INBOUND_TP_LOG.INBOUND_TS_ID AND INBOUND_TP_LOG.INBOUND_TS_ID = INBOUND_TS_LOG.TRANS_ID AND OUTBOUND_TS_LOG.ERR_RETURN_CODE IS NULL AND INBOUND_TS_LOG.TS_TRANSID = '"
//              + payload
//              + "'";
//        } else if (table.equals("outboundTP")) {
//          SQL =
//            "SELECT OUTBOUND_TP_LOG.XML_DOC FROM OUTBOUND_TP_LOG, INBOUND_TP_LOG, INBOUND_TS_LOG WHERE OUTBOUND_TP_LOG.INBOUND_TP_ID = INBOUND_TP_LOG.TRANS_ID AND INBOUND_TP_LOG.INBOUND_TS_ID = INBOUND_TS_LOG.TRANS_ID AND INBOUND_TS_LOG.TS_TRANSID = '"
//              + payload
//              + "'";
//        }

//        DBTransaction dbt = new DBTransaction();
//        dbt.setSqlStatement(SQL);
//        dbt.invoke();
//        String s = null;
//        if (dbt.getResponseData() != null) {
//          if (dbt.getResponseData().size() > 0) {
//            s = (String)dbt.getResponseData().get("0");
//          }
//        }

//        File f = null;
//        if (s != null) {
//          try {
//            PrintWriter out = new PrintWriter(new FileWriter(tempXml));
//            out.println(s);
//            out.close();
//            eventLogger.sendEvent("Got File Writer", EventType.DEBUG, this);
//            f = new File(tempXml);
//            eventLogger.sendEvent("Got File, about to convert to XML", EventType.DEBUG, this);
//            docIn =
//              org.apache.xerces.jaxp.DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(f);
//            eventLogger.sendEvent("Converted to XML", EventType.DEBUG, this);
//            ser = new XMLSerializer(res.getOutputStream(), new OutputFormat("xml", "UTF-8", false));
//            ser.serialize(docIn);
//
//          } catch (Exception e) {
//            eventLogger.sendEvent("EXCEPTION converting XML: " + e.toString(), EventType.WARN, this);
//          }
//
//        }
//      }

    } catch (SAXParseException se) {
      eventLogger.sendEvent("EXCEPTION in incoming XML: " + se.toString(), EventType.WARN, this);
    } catch (Exception e) {
      eventLogger.sendEvent("EXCEPTION processing XML: " + e.toString(), EventType.WARN, this);
    }

    return;
  }

}
