package pvt.disney.dti.gateway.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import pvt.disney.dti.gateway.common.DBTransaction;
import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.LogKey;
import pvt.disney.dti.gateway.dao.SequenceKey;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.service.DTIService;
import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.DTITracker;
import pvt.disney.dti.gateway.util.DateTime;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.flood.KeyBlockException;
import pvt.disney.dti.gateway.util.flood.KeySuppressException;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import com.disney.context.BaseContext;
import com.disney.context.ContextManager;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;

/**
 * The DTI Controller is the class where all gates (HTTP, Web-Services) drop their XML for processing.
 * 
 * @author lewit019 and others
 * @version %version: 2 %
 * 
 */
public class DTIController {

	// CONSTANTS
	/** The standard core logging mechanism. */
	private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

	private DTIFloodControl floodControl = null;

	/** Core properties management initializer. */
	private AbstractInitializer abstrInit = null;

	/** Properties variable to store properties from AbstractInitializer. */
	private Properties props = null;

	// private boolean validate = false;
	private boolean enableFloodControlDeny = false;
	private DateTime dt = null;

	/**
	 * The ticket broker currently in use. Defaults to DTIUK if properties can't be read.
	 */
	private static String tktBroker = "DTIUK";

	/**
	 * A string of comma separated values indicating TSMAC's that are not permitted on this end-point.
	 */
	private static String tsMacExcludeList = "";

	/**
	 * Constructor for DTIApp
	 */
	public DTIController() {
		super();

		// Get properties manager.
		try {
			abstrInit = AbstractInitializer.getInitializer();
			props = abstrInit.getProps("dtiApp.properties");
		}
		catch (Throwable e) {
			eventLogger.sendEvent("THROWABLE initing props: " + e.toString(),
					EventType.FATAL, this);
		}

		try {
			floodControl = DTIFloodControl.getInstance(props);
		}
		catch (Exception e) {
			eventLogger.sendEvent(
					"Unable to init flood control:  " + e.toString(),
					EventType.WARN, this);
		}

		String propsValue = PropertyHelper.readPropsValue(
				PropertyName.ENABLE_FLOOD_CONTROL_DENY, props, null);
		if (propsValue.compareToIgnoreCase("true") == 0) {
			enableFloodControlDeny = true;
		}

		tktBroker = PropertyHelper.readPropsValue(PropertyName.POS_TKT_BROKER,
				props, null);

		/** Forbid certain sellers from reaching this end-point. */
		tsMacExcludeList = PropertyHelper.readPropsValue(
				PropertyName.TSMAC_EXCLUSION, props, null);

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
		DBTransaction dbTran = null;
		String target = null;
		String version = null;
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
						getProperty(PropertyName.ERROR_CODE_VALIDATION_WELL_FORMED),
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
			eventLogger.sendEvent(
					"Ticket seller request is: " + maskedXMLRequest,
					EventType.INFO, this);

			// get the payLoad ID, entity Name, StoreId, and other key fields
			// from the XML
			if (docIn != null) {
				eventLogger.sendEvent(
						"Calling parseDoc() to extract key fields from XML.",
						EventType.DEBUG, this);
				HashMap parsedDoc = TiXMLHandler.parseDoc(docIn);
				payloadId = (String) parsedDoc.get(TiXMLHandler.PAYLOAD_ID);
				tsMac = (String) parsedDoc.get(TiXMLHandler.TS_MAC);
				tsLocation = (String) parsedDoc.get(TiXMLHandler.TS_LOCATION);
				target = (String) parsedDoc.get(TiXMLHandler.TS_ENVIRONMENT);
				version = (String) parsedDoc.get(TiXMLHandler.TS_VERSION);

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

						// Reread property value.
						String propsValue = PropertyHelper.readPropsValue(
								PropertyName.FLOOD_CONTROL_ACTIVE, props, null);
						boolean floodControlActive = false;
						if (propsValue.compareToIgnoreCase("true") == 0) {
							floodControlActive = true;
						}
						floodControl.setFloodControlActive(floodControlActive);

						floodControl.evaluateTransaction(parsedDoc);

					}
					catch (KeyBlockException kbe) {

						if (enableFloodControlDeny) {
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

						if (enableFloodControlDeny) {
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
					eventLogger.sendEvent(
							"EXCEPTION getting sequence numbers: " + dtie
									.toString(), EventType.FATAL, this);
					throw dtie;
				}

				boolean isFirstAttempt = true;

				eventLogger.sendEvent("Attempting to record request.",
						EventType.DEBUG, this);

				// Get the entity ID.
				EntityTO entityTO = null;
				try {
					entityTO = EntityKey.getEntity(tsMac, tsLocation);
				}
				catch (DTIException dtie) {
					eventLogger.sendEvent(
							"EXCEPTION getting entity lookup: " + dtie
									.toString(), EventType.EXCEPTION, this);
					throw dtie;
				}

				try {
					isFirstAttempt = LogKey.insertITSLog(payloadId, entityTO,
							transIdITS, maskedXMLRequest);

					// Replace the transId with the one from the original attempt.
					if (!isFirstAttempt) {
						transIdITS = LogKey.getITSLogTransId(payloadId);
					}

					// Insert into DTI's consolidated log table.
					// If it fails, log & keep going. The key business processes do not
					// rely on this table.
					try {
						LogKey.insertDTITransLog(tpRefNumber, payloadId,
								entityTO, target, tktBroker, transIdITS,
								isFirstAttempt);
					}
					catch (DTIException dtie) {
						eventLogger
								.sendEvent(
										"Unable to log ITS info into consolidated table:  " + dtie
												.toString(), EventType.WARN,
										this);
					}

				}
				catch (DTIException dtie) {
					eventLogger.sendEvent(
							"EXCEPTION writing to INBOUND_TS_LOG: " + dtie
									.toString(), EventType.WARN, this);
					throw dtie;
				}

				eventLogger.sendEvent("Request recorded (or was dupe).",
						EventType.DEBUG, this);

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
					dbTran = new DBTransaction();
					dbTran.handleDuplicate(payloadId);
					if ((dbTran.getResponseData() != null) && (dbTran
							.getResponseData().size() > 0)) {
						docOut = getDocFromResultSet(dbTran, payloadId);
						dbTran = null;
						addReworkText(docOut, payloadId);
						eventLogger.sendEvent(
								"Rework response ready for ticket seller.",
								EventType.INFO, this);
						return docOut;
					}
					else {
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
				String xmlResponse = DTIService.submitRequest(xmlRqstString,
						entityTO, transIdITS, tpRefNumber);

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
							getProperty(PropertyName.ERROR_CODE_VALIDATION_WELL_FORMED),
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
						getProperty(PropertyName.ERROR_CODE_DTI),
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
					logOutbound(getDocumentToString(docOut), dtie, payloadId,
							transIdOTS);
					// TODO: JTL Log edit key fields failures should be logged in DTI trans log here.
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

		eventLogger.sendEvent("Entering buildErrorXMLForPOS(,)",
				EventType.DEBUG, this);

		// build the Document that goes back to POS
		// to be consistent we first ensure the insert
		// was performed then write the file to disk
		Document returnData = null;
		StringBuffer sb = null;
		DBTransaction dbTransaction = new DBTransaction();
		dt = new DateTime();

		try {
			// Retrieve error info from DB
			eventLogger
					.sendEvent(
							"Building error XML for ticket seller because: " + e + " Using PayloadID: " + newPayloadId,
							EventType.WARN, this);
			dbTransaction
					.setSqlStatement("SELECT ERR_NBR, CRITICALITY, ERR_CLASS, ERR_NAME FROM DTI_ERROR WHERE ERR_NBR ='" + e
							.getCode() + "'");
			eventLogger.sendEvent(
					"Getting error text from database for ERROR: " + e
							.getCode(), EventType.INFO, this);
			dbTransaction.invoke();

			if (dbTransaction.getResponseData().size() > 0) {
				eventLogger.sendEvent(
						"Got error data from DB...populating XML",
						EventType.DEBUG, this);
				sb = new StringBuffer(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transmission><Payload><PayloadHeader><PayloadID>" + newPayloadId + "</PayloadID><TSPayloadID>" + newPayloadId + "</TSPayloadID><Target>" + getProperty(PropertyName.POS_TARGET) + "</Target><Version>" + getProperty(PropertyName.POS_VERSION) + "</Version><Comm><Protocol>" + getProperty(PropertyName.POS_PROTOCOL) + "</Protocol><Method>" + getProperty(PropertyName.POS_METHOD) + "</Method></Comm><TransmitDate>" + dt
								.getYear() + "-" + dt.getMonth() + "-" + dt
								.getDay() + "</TransmitDate><TransmitTime>" + dt
								.getHour() + ":" + dt.getMinute() + ":" + dt
								.getSecond() + "." + dt.getMilli().substring(0,
								2) + "</TransmitTime><TktBroker>" + getProperty(PropertyName.POS_TKT_BROKER) + "</TktBroker><CommandCount>" + getProperty(PropertyName.POS_COMMAND_COUNT) + "</CommandCount><PayloadError><HdrErrorCode>" + (String) dbTransaction
								.getResponseData().get("0") + "</HdrErrorCode><HdrErrorType>" + (String) dbTransaction
								.getResponseData().get("1") + "</HdrErrorType><HdrErrorClass>" + (String) dbTransaction
								.getResponseData().get("2") + "</HdrErrorClass><HdrErrorText>" + (String) dbTransaction
								.getResponseData().get("3") + "</HdrErrorText></PayloadError></PayloadHeader></Payload></Transmission>");

				// check to see if it is supposed to be sent back to POS
				try {
					returnData = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
							.newInstance()
							.newDocumentBuilder()
							.parse(new InputSource(DTIFormatter
									.getStringToInputStream(sb.toString())));
				}
				catch (Exception ex) {
					eventLogger.sendEvent(
							"EXCEPTION instantiating response XML: " + ex
									.toString(), EventType.WARN, this);
				}

				eventLogger.sendEvent(
						"Error XML for ticket seller is: " + sb.toString(),
						EventType.INFO, this);
			}
		}
		catch (Exception ee) {
			eventLogger.sendEvent(
					"EXCEPTION when retrieveing error data: " + ee.toString(),
					EventType.WARN, this);
			try {
				sb = new StringBuffer(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><Transmission><Payload><PayloadHeader><PayloadID>" + newPayloadId + "</PayloadID><TSPayloadID>" + newPayloadId + "</TSPayloadID><Target>" + getProperty(PropertyName.POS_TARGET) + "</Target><Version>" + getProperty(PropertyName.POS_VERSION) + "</Version><Comm><Protocol>" + getProperty(PropertyName.POS_PROTOCOL) + "</Protocol><Method>" + getProperty(PropertyName.POS_METHOD) + "</Method></Comm><TransmitDate>" + dt
								.getYear() + "-" + dt.getMonth() + "-" + dt
								.getDay() + "</TransmitDate><TransmitTime>" + dt
								.getHour() + ":" + dt.getMinute() + ":" + dt
								.getSecond() + "." + dt.getMilli().substring(0,
								2) + "</TransmitTime><TktBroker>" + getProperty(PropertyName.POS_TKT_BROKER) + "</TktBroker><CommandCount>" + getProperty(PropertyName.POS_COMMAND_COUNT) + "</CommandCount><PayloadError><HdrErrorCode>" + getProperty(PropertyName.ERROR_POS_DEFAULT_CODE) + "</HdrErrorCode><HdrErrorType>" + getProperty(PropertyName.ERROR_POS_DEFAULT_TYPE) + "</HdrErrorType><HdrErrorClass>" + getProperty(PropertyName.ERROR_POS_DEFAULT_CLASS) + "</HdrErrorClass><HdrErrorText>" + getProperty(PropertyName.ERROR_POS_DEFAULT_TEXT) + "</HdrErrorText></PayloadError></PayloadHeader></Payload></Transmission>");

				// check to see if it is supposed to be sent back to POS
				try {
					returnData = org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
							.newInstance()
							.newDocumentBuilder()
							.parse(new InputSource(DTIFormatter
									.getStringToInputStream(sb.toString())));
				}
				catch (Exception es) {
					eventLogger
							.sendEvent(
									"EXCEPTION instantiating error XML for ticket seller: " + es
											.toString(), EventType.WARN, this);
				}
			}
			catch (Exception es) {
				eventLogger.sendEvent(
						"EXCEPTION building error XML for ticket seller: " + es
								.toString(), EventType.WARN, this);
			}
		}

		return returnData;
	}

	/**
	 * Logs an error to the outbound error log.
	 * 
	 * @param xml
	 *            The XML error being returned.
	 * @param e
	 *            The DTIException encountered.
	 * @param newPayloadId
	 * @throws DTIException
	 */
	private void logOutbound(String xml, DTIException e, String newPayloadId,
			Integer otsLogKey) throws DTIException {

		eventLogger
				.sendEvent("Entering logOutbound(,,)", EventType.DEBUG, this);

		int inbound_transid = 0;
		DBTransaction dbTransaction = new DBTransaction();

		try {
			dbTransaction
					.setSqlStatement("SELECT TRANS_ID FROM INBOUND_TS_LOG WHERE TS_TRANSID ='" + newPayloadId + "'");
			dbTransaction.invoke();
			if (dbTransaction.getResponseData().size() > 0) {
				inbound_transid = DTIFormatter.toInt((String) dbTransaction
						.getResponseData().get("0"));
			}
			String error = null;
			if (e != null) {
				if (e.getCode().length() > 2) {
					error = "'" + e.getCode() + "'";
				}
			}

			// Since 2.9 - JTL
			// If there is no inbound_transid, then don't put the output in the database
			// table (as we haven't put the request in there, either).
			if (inbound_transid <= 0) {
				return;
			}

			dbTransaction
					.setSqlStatement("SELECT TRANS_ID FROM OUTBOUND_TS_LOG WHERE TS_TRANSID ='" + newPayloadId + "' AND ERR_RETURN_CODE IS NULL");
			dbTransaction.invoke();

			if (dbTransaction.getResponseData().size() == 0) {
				dbTransaction
						.setSqlStatement("INSERT INTO OUTBOUND_TS_LOG (TRANS_ID, TRANS_DATE, TS_TRANSID, " + "ERR_RETURN_CODE, XML_DOC, INBOUND_TS_ID) VALUES (" + otsLogKey + ", SYSDATE, '" + newPayloadId + "'," + error + ",'" + xml + "'," + inbound_transid + ")");
				dbTransaction.invoke();
			}
		}
		catch (Exception ex) {
			if (e instanceof DTIException) {
				eventLogger.sendEvent(
						"EXCEPTION writing to OUTBOUND_TS_LOG: " + ex
								.toString(), EventType.WARN, this);
				throw (DTIException) ex;
			}
			else {
				eventLogger.sendEvent(
						"EXCEPTION invoking DBTransaction: " + ex.toString(),
						EventType.DEBUG, this);
				throw new DTIException("DBTransaction()", DBTransaction.class,
						3, getProperty(PropertyName.ERROR_OUTBOUND_LOG),
						"Exception in SQL Execution", ex);
			}
		}
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
		Text payloadNoteText = doc
				.createTextNode(getProperty(PropertyName.REWORK_TEXT) + " " + newPayloadId);
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
	private Document getDocFromResultSet(DBTransaction dbTransaction,
			String newPayloadId) throws DTIException {

		eventLogger.sendEvent("Entering getDocFromResultSet(,)",
				EventType.DEBUG, this);

		Document returnDoc = null;
		String completeXML = (String) dbTransaction.getResponseData().get("0");
		eventLogger.sendEvent(
				"Retrieved original response for rework: " + completeXML,
				EventType.INFO, this);
		eventLogger
				.sendEvent(
						"About to Instantiate XML from OutBound TS LOG for payloadId: " + newPayloadId,
						EventType.DEBUG, this);
		try {

			InputStream istr = DTIFormatter.getStringToInputStream(completeXML);
			InputSource isrc = new InputSource(istr);
			returnDoc = DocumentBuilderFactoryImpl.newInstance()
					.newDocumentBuilder().parse(isrc);

			eventLogger
					.sendEvent(
							"Finished Instantiation XML from OutBound TS LOG for payloadId: " + newPayloadId,
							EventType.DEBUG, this);

		}
		catch (Exception e) {
			eventLogger
					.sendEvent(
							"EXCEPTION creating XML from OutBound TS LOG for payloadId " + newPayloadId + ":  " + e
									.toString(), EventType.WARN, this);
			eventLogger.sendEvent(
					"EXCEPTION instantiating XML was: " + e.toString(),
					EventType.WARN, this);
			throw new DTIException(
					"EXCEPTION in XML from OUTBOUND_TS_LOG",
					getClass(),
					3,
					getProperty(PropertyName.ERROR_CODE_VALIDATION_WELL_FORMED),
					"XML is not Well Formed", null);
		}

		if (returnDoc == null) {
			throw new DTIException(
					"EXCPETION in XML from OUTBOUND_TS_LOG (returnDoc is null)",
					getClass(),
					3,
					getProperty(PropertyName.ERROR_CODE_VALIDATION_WELL_FORMED),
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
