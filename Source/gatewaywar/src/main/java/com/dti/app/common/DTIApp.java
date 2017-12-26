package com.dti.app.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.controller.DTIController;
import pvt.disney.dti.gateway.service.DTIService;
import pvt.disney.dti.gateway.util.ResourceLoader;
import pvt.disney.dti.gateway.util.flood.FloodControlInitException;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * The DTIApp class implements the servlet that receives commands from a ticket
 * seller and returns responses to them.
 * 
 * @author IBM
 * @since 2.16.3
 */
public class DTIApp extends HttpServlet {

  /** Serial Version UID */
  private static final long serialVersionUID = 1L;

  /** The standard core logging mechanism. */
  private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

  /** Properties variable to store properties */
  private ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
  private Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

  /**
   * Constructor for DTIApp.
   */
  public DTIApp() {
    super();
  }

  /**
   * doGet calls doPost
   * 
   * @throws ServletException
   *           , IOException
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    PrintWriter outPrint = null;
    String responseString = null;
    outPrint = res.getWriter();

    eventLogger.sendEvent("doGet() called by: " + req.getRemoteHost(), EventType.INFO, this);

    responseString = DTIService.generateGenericError(DTIErrorCode.INVALID_ACCESS_METHOD, null);

    res.setContentType("text/xml;charset=UTF-8");

    outPrint.println(responseString);

    return;
  }

  /**
   * Performs the processing for the DTI App
   * 
   * @param req
   *          HttpServletRequest
   * @param res
   *          HttpServletResponse
   * @return DBTransaction
   * @throws ServletException
   *           , IOException
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    Document docOut = null;
    XMLSerializer ser = null;

    eventLogger.sendEvent("Entering doPost(req,res) ", EventType.DEBUG, this);

      try {
         // get the XML from POS
         eventLogger.sendEvent("About to Instantiate DTIController on DTIApp side", EventType.DEBUG, this);
         try {

            eventLogger.sendEvent("Received ticket seller request from https.", EventType.INFO, this);

            DTIController controller = new DTIController();
            docOut = controller.processRequest(req.getInputStream());

            // Flood blocking response logic
            if (docOut == null) {
               res.sendError(HttpServletResponse.SC_GONE,
                        "If you believe you have reached this page in error, please contact DTI Support.");
               return;
            }

            req.getInputStream().close();

            // return to the POS with a confirmation message
            try {
               ser = new XMLSerializer(res.getOutputStream(), new OutputFormat("xml", "UTF-8", false));
               ser.serialize(docOut);
               res.getOutputStream().close();
            } catch (Exception e) {
               String errorCode = DTIErrorCode.UNABLE_TO_COMMUNICATE.getErrorCode();
               throw new DTIException("doPost()...error writing XML to POS", getClass(), 3, errorCode, e.getMessage(),
                        e);
            } finally {
            }
         } catch (Exception e) {

            if ((e instanceof FloodControlInitException)) {
               // return to the POS generic error message after getting the
               // FloodControlInitException
               // during the instantiation of DTIController
               PrintWriter outPrint = null;
               String responseString = null;
               outPrint = res.getWriter();
               responseString = DTIService.generateGenericError(DTIErrorCode.DTI_CONFIG_ERROR, null);
               res.setContentType("text/xml;charset=UTF-8");
               outPrint.println(responseString);

            } else {
               String errorCode = DTIErrorCode.INVALID_MSG_ELEMENT.getErrorCode();
               eventLogger.sendEvent("Error in request: " + e.toString(), EventType.WARN, this);
               throw new DTIException("doPost()...error in XML from POS", getClass(), 3, errorCode,
                        "XML is not well Formed", null);
            }
         }
      } catch (Exception ee) {
         String errorCode = DTIErrorCode.UNABLE_TO_COMMUNICATE.getErrorCode();
         if (!(ee instanceof DTIException)) {
            ee = new DTIException("doPost()", getClass(), 3, errorCode, ee.getMessage(), ee);
         }
      }

    eventLogger.sendEvent("Returning response to https ticket seller.  Servlet complete.", EventType.INFO, this);
  }
}
