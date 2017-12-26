package pvt.disney.dti.gateway.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.CosTpGrpCmdKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CosTpGrpCmdTO;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;


/**
 * The Class WdwIagoClient.
 *
 * @author lewit019, moons012
 */
public class WdwIagoClient {

	/**
	 * shared core event logger.
	 */
	private static EventLogger logger;

	/** Properties file. */
	private Properties props;

	/** The cos endpoints next read time for refreshing from the database. */
	protected static long cosEndpointsNextRead = new Date().getTime();

	/**
	 * The cos refresh interval - drives when the cos endpoints are refreshed from
	 * the dti database.
	 */
	private static int cosRefreshIntervalMillis = 60000;

	//TODO: remove when we switch to cos endpoints
	/** endpoint for iago connection */
	//private static String ENDPOINT ="";
	/**
	 * URI end-points for IAGO web service INITIALIZED during first instantiation
	 * and refreshed at read intervals
	 */
	private static Hashtable<String, String> ENDPOINTS = new Hashtable<String, String>();

	/**
	 * socket timeout value for communicating with IAGO service INITIALIZED once
	 * during first instantiation.
	 */
	private static String READ_TIMEOUT_STRING = "";

	/** The read timeout standard. */
	private static Integer READ_TIMEOUT_STANDARD;

	/** The read timeout renewal string. */
	private static String READ_TIMEOUT_RENEWAL_STRING = "";

	/** The read timeout renewal. */
	private static Integer READ_TIMEOUT_RENEWAL;

	/**
	 * socket timeout value for communicating with IAGO service INITIALIZED once
	 * during first instantiation.
	 */
	private static int CONNECTION_TIMEOUT = 0;

	/** Checks property initialization. */
	private static boolean INITIALIZED = false;
	
	/**
	 *  used to get environment
	 */
	private static String environment = null;
	
	/**
	 * Constructor for IagoSoapClient. Gets the logger for the class and initializes
	 * connection properties (if not already INITIALIZED).
	 */
	public WdwIagoClient() {
		// create the logger
		logger = EventLogger.getLogger(this.getClass());

		if (!INITIALIZED) {
			// initialize IAGO properties
			logger.sendEvent("About to Initialize IAGO.properties file ...", EventType.DEBUG, this);

			ResourceBundle rb = ResourceLoader.getResourceBundle("IAGO");
			props = ResourceLoader.convertResourceBundleToProperties(rb);

			logger.sendEvent("Successfully Initialized IAGO.properties file ...", EventType.DEBUG, this);
			
			//TODO remove property based endpoint when we switch to cos
			//ENDPOINT = PropertyHelper.readPropsValue("iago.endpoint", props, null);
			//TODO uncomment this when we are ready to push cos
			ENDPOINTS = WdwIagoClient.loadClassOfServiceEndpoints();
			
			READ_TIMEOUT_RENEWAL_STRING = PropertyHelper.readPropsValue("iago.socketTimeout.renewal", props, null);
			if (READ_TIMEOUT_RENEWAL_STRING == null) {
				READ_TIMEOUT_RENEWAL = new Integer(60000);
			} else {
				try {
					READ_TIMEOUT_RENEWAL = Integer.parseInt(READ_TIMEOUT_RENEWAL_STRING);
				} catch (NumberFormatException nfe) {
					logger.sendEvent("Unable to parse iago.socketTimeout.renewal, default to 60...",
							EventType.EXCEPTION, this);
					READ_TIMEOUT_RENEWAL = new Integer(60000);
				}
			}

			READ_TIMEOUT_STRING = PropertyHelper.readPropsValue("iago.socketTimeout", props, null);
			if (READ_TIMEOUT_STRING == null) {
				READ_TIMEOUT_STANDARD = new Integer(35000);
			} else {
				try {
					READ_TIMEOUT_STANDARD = Integer.parseInt(READ_TIMEOUT_STRING);
				} catch (NumberFormatException nfe) {
					logger.sendEvent("Unable to parse iago.socketTimeout, default to 35...", EventType.EXCEPTION, this);
					READ_TIMEOUT_STANDARD = new Integer(35000);
				}
			}

			CONNECTION_TIMEOUT = Integer.parseInt(PropertyHelper.readPropsValue("iago.connectionTimeout", props, null));

			//TODO Uncomment this when COS goes live
			logger.sendEvent("WdwIago Class of service endpoints are: " + ENDPOINTS, EventType.DEBUG, this);

			logger.sendEvent("iago.socketTimeout is " + READ_TIMEOUT_STANDARD, EventType.DEBUG, this);
			logger.sendEvent("iago.socketTimeout.renewal is " + READ_TIMEOUT_RENEWAL, EventType.DEBUG, this);

			INITIALIZED = true;

		} else {
			logger.sendEvent("IAGO already INITIALIZED ...", EventType.DEBUG, this);
		}

	}

	/**
	 * Send a request that needs converted to IAGO XML.
	 *
	 * @param dtiTxn
	 *            the dti txn
	 * @param xmlRequest
	 *            the xml request
	 * @return the string
	 * @throws DTIException
	 *             the DTI exception
	 */
	public String sendRequest(DTITransactionTO dtiTxn, String xmlRequest) throws DTIException {
		return sendRequest(dtiTxn, xmlRequest, true);

	}

	/**
	 * This method sends the request XML passed in via the InputStream to the target
	 * server identified in the IAGO.properties.
	 * 
	 * If convert is true, is is converted to IAGOXML, otherwise it is just sent.
	 *
	 * @param dtiTxn
	 *            the dti txn
	 * @param xmlRequest
	 *            the xml request
	 * @param convert
	 *            the convert
	 * @return the string
	 * @throws DTIException
	 *             the DTI exception
	 */
	public String sendRequest(DTITransactionTO dtiTxn, String xmlRequest, boolean convert) throws DTIException {
		//TODO uncomment this when COS refresh goes live
		checkForCosRefresh();
		
		String campus = null;
		URLConnection conn = null;
		Document docResponse = null;
		Document docRequest = null;

		String xmlResponse = null;

		campus = dtiTxn.getRequest().getPayloadHeader().getTarget();
		// Convert to SOAP Format
		if (convert == true) {
			try {
				xmlRequest = ClientUtility.convertRequest(xmlRequest);
			} catch (Exception e1) {
				logger.sendEvent(("Unable to convert request to IAGO format: '"
						+ PCIControl.overwritePciDataInXML(xmlRequest) + "'"), EventType.EXCEPTION, this);
				e1.printStackTrace();
			}
		}

		try {

			// Convert to Document
			InputStream in = UtilityXML.getStringToInputStream(xmlRequest);
			logger.sendEvent("About to build docRequest ...", EventType.DEBUG, this);
			DocumentBuilderFactory docFactory = DocumentBuilderFactoryImpl.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			docRequest = docBuilder.parse(in);
			logger.sendEvent("Successfully INITIALIZED docRequest ...", EventType.DEBUG, this);

			//synchronize check for is the database is due for refresh
			synchronized (WdwIagoClient.class) {
				checkForCosRefresh();				
			}
			
			//TODO REMOVE WHEN WE SWITCH TO COS ENDPOINTS
			//URL url = new URL(ENDPOINT);
			// get correct class of service endpoint by transaction type
			URL url = new URL(ENDPOINTS.get(dtiTxn.getTransactionType().toString()));

			logger.sendEvent("About to send message to ATS '" + campus + "' provider system.", EventType.INFO, this);

			// Open Connection
			conn = (URLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set read and connect time outs.
			conn.setConnectTimeout(CONNECTION_TIMEOUT);

			int readTimeout = 30000;

			switch (dtiTxn.getTransactionType()) {
			case RENEWENTITLEMENT:
				readTimeout = READ_TIMEOUT_RENEWAL;
				break;
			default:
				readTimeout = READ_TIMEOUT_STANDARD;
				break;
			}
			conn.setReadTimeout(readTimeout);

			logger.sendEvent("Connection established to URL: " + conn.getURL().toString(), EventType.DEBUG, this);

			// Sending Request to IAGO
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			OutputStream out = conn.getOutputStream();
			// NOTE: "DOM does not require implementations to be thread safe.
			// If you need to access the DOM from multiple threads, you are required
			// to add the appropriate locks to your application code." - From
			// xerces2-j/faq-dom.html

			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
			LSSerializer writer = impl.createLSSerializer();
			LSOutput output = impl.createLSOutput();
			output.setByteStream(out);
			writer.write(docRequest, output);
			out.close();

			// MASK OUT <Endorsement>ZZZZZZZZZZZZZZZZ</Endorsement>
			// Used for Debugging
			String xmlMasked = PCIControl.overwritePciDataInXML(xmlRequest);
			logger.sendEvent("Document being sent TO WDW ticketing server: " + xmlMasked, EventType.DEBUG, this);

			// Getting response from IAGO
			InputStream response = conn.getInputStream();

			// Create the document response
			docFactory = DocumentBuilderFactoryImpl.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			docResponse = docBuilder.parse(response);
			xmlResponse = UtilityXML.getDocumentToString(docResponse);
			xmlMasked = PCIControl.overwritePciDataInXML(xmlResponse);

			logger.sendEvent("Just received response from the " + campus + " provider system.", EventType.INFO, this);
			logger.sendEvent("IAGO Raw Response:" + xmlMasked, EventType.DEBUG, this);

			// Check for SOAP fault
			String faultCode = UtilityXML.getTagData(xmlResponse, "faultcode");
			if (faultCode != null) {
				String faultString = UtilityXML.getTagData(xmlResponse, "faultstring");
				if (faultString != null) {
					logger.sendEvent("SOAP fault from IAGO: " + faultCode + " " + faultString, EventType.WARN, this);
					throw new DTIException(WdwIagoClient.class, DTIErrorCode.TP_INTERFACE_FAILURE,
							"SOAP fault from IAGO: " + faultCode + " " + faultString);
				} else {
					logger.sendEvent("SOAP fault from IAGO: " + faultCode, EventType.WARN, this);
					throw new DTIException(WdwIagoClient.class, DTIErrorCode.TP_INTERFACE_FAILURE,
							"SOAP fault from IAGO: " + faultCode);
				}
			}

			// Convert from SOAP format.
			xmlResponse = ClientUtility.unwrapFromSOAP(xmlResponse);
			if (convert) {
				logger.sendEvent("IAGOSoapClient entering convertResponse() ...", EventType.DEBUG, this);
				xmlResponse = ClientUtility.convertResponse(xmlResponse);
				logger.sendEvent(xmlResponse, EventType.DEBUG, WdwIagoClient.class);
			}

		} catch (DTIException dte) {
			logger.sendEvent("Caught DTIException " + dte.getDtiErrorCode().getErrorCode(), EventType.WARN, this);
			dte.printStackTrace();
			throw dte;
		} catch (Exception e) {

			logger.sendEvent("Fell through Caught: " + e + ":" + e.getMessage() + ":" + e.getCause(), EventType.WARN,
					this);
			e.printStackTrace();
			throw new DTIException(WdwIagoClient.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Unable to complete communication with the provider.");
		}

		return xmlResponse;
	}

	/**
	 * Read class of service endpoints helper.
	 *
	 * @return the hashtable
	 */
	protected static Hashtable<String, String> loadClassOfServiceEndpoints() {
		Hashtable<String, String> endpoints = new Hashtable<String, String>();

	    // Get the cos grp
		environment = System.getProperty("APP_ENV");
		ArrayList<CosTpGrpCmdTO> cosList = new ArrayList<CosTpGrpCmdTO>();
	    try {
	    		cosList = CosTpGrpCmdKey.getTpCosGrpCmd("WDW", environment);
	    		for (CosTpGrpCmdTO cosTpGrp : cosList) {
	    	        endpoints.put(cosTpGrp.getCmdcode().toUpperCase() ,cosTpGrp.getEndpointurl());
	    	        logger.sendEvent("WDWIAGOCLIENT added" + cosTpGrp.getCmdcode() + " with " + cosTpGrp.getEndpointurl(), EventType.INFO, null );
	    	    }
		} catch (Exception e) {
			logger.sendEvent(
					"Unable to load WDW Class of Service End"
					+ "points...",
					EventType.EXCEPTION, WdwIagoClient.class);
			e.printStackTrace();
		}
		return endpoints;
	}

	/**
	 * Helper. This method compares the current time to last read value. If the difference
	 * is more than or equal to the refresh interval then it resets the refresh time.
	 * 
	 * Sets the next refresh time.
	 */
	private synchronized static void nextRefreshTime() {
		// Initializing the next read value.
		cosEndpointsNextRead = new Date().getTime() + cosRefreshIntervalMillis;
	}

	/**
	 * Gets the cos refresh interval millis.
	 *
	 * @return the cos refresh interval millis
	 */
	public static int getCosRefreshIntervalMillis() {
		return cosRefreshIntervalMillis;
	}
	/**
	 * Check for cos refresh helper.
	 */
	private  void checkForCosRefresh() {
		if (new Date().getTime() >= cosEndpointsNextRead) {
			//set the time for next database refresh
			String threadInfo = Thread.currentThread().getId() + ": " + Thread.currentThread().getName();
			logger.sendEvent("WdwIago Class of service determined that endpoint refresh is required by thread: " + threadInfo, EventType.DEBUG, this);
			WdwIagoClient.loadClassOfServiceEndpoints();
		}
	}
    
	/**
	 * returns the static set of endpoints
	 * @return
	 */
	public static Hashtable<String, String> getENDPOINTS() {
		return ENDPOINTS;
	}

}
