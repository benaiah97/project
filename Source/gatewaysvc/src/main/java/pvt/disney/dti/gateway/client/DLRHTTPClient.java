package pvt.disney.dti.gateway.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
// End of new with 2.11
// New with 2.11
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXParseException;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.CosGrpKey;
import pvt.disney.dti.gateway.dao.CosTpGrpCmdKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CosGrpTO;
import pvt.disney.dti.gateway.data.common.CosTpGrpCmdTO;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/**
 * This class contains the logic required to communicate with DLR's eGalaxy
 * system via HTTP/S.
 * 
 * @author lewit019, moons012
 * 
 */
public class DLRHTTPClient {

	/**
	 * shared core event logger.
	 */
	private static EventLogger logger;

	/**
	 * Properties file
	 */
	private Properties props;
	
	/** The cos endpoints next read time for refreshing from the database. */
	protected static long cosEndpointsNextRead = new Date().getTime();

	/**
	 * The cos refresh interval - drives when the cos endpoints are refreshed from
	 * the dti database.
	 */
	private static int cosRefreshIntervalMillis = 60000;

	/**
	 * URI end-points for DLR web service INITIALIZED during first instantiation
	 * and refreshed at read intervals
	 */
	private static Hashtable<String, String> ENDPOINTS = new Hashtable<String, String>();
	/**
	 * initialized once during first instantiation
	 */
	private static String ACTIVE_CONNECTION = "";

	//TODO REMOVE WHEN WE SWTICH OVER TO COS ENDPOINTS
	/**
	 * initialized once during first instantiation
	 */
	private static String URL_STRING = "";

	/**
	 * initialized once during first instantiation
	 */
	private static String CONNECT_TIMEOUT_STRING = "";

	/**
	 * initialized once during first instantiation
	 */
	private static String READ_TIMEOUT_STRING = "";
	private static Integer READ_TIMEOUT_STANDARD;
	private static String READ_TIMEOUT_RENEWAL_STRING = "";
	private static Integer READ_TIMEOUT_RENEWAL;

	/**
	 * used to check initialized status
	 */
	private static boolean INITIALIZED = false;

	/**
	 * Constructor for HTTPController. Gets the logger for the class.
	 * 
	 */
	public DLRHTTPClient() {
		logger = EventLogger.getLogger(this.getClass());

		// initialize only once
		if (!INITIALIZED) {

			logger.sendEvent(
					"About to Initialize DLRCLIENT.properties file ...",
					EventType.DEBUG, this);
			
			ResourceBundle rb = ResourceLoader.getResourceBundle("DLRCLIENT");
			props = ResourceLoader.convertResourceBundleToProperties(rb);

			logger.sendEvent(
					"Successfully Initialized DLRCLIENT.properties file ...",
					EventType.DEBUG, this);
			ACTIVE_CONNECTION = PropertyHelper.readPropsValue("ACTIVECONN",
					props, null);
			
			//TODO: REOMVE WHEN WE SWITCH TO ENDPOINTS
			URL_STRING = PropertyHelper.readPropsValue(ACTIVE_CONNECTION,
					props, null);
			//TODO uncomment when we turn on COS
			//ENDPOINTS = DLRHTTPClient.loadClassOfServiceEndpoints();
				
			CONNECT_TIMEOUT_STRING = PropertyHelper.readPropsValue(
					"CONNECT_TIMEOUT_MILLIS", props, null);

			READ_TIMEOUT_RENEWAL_STRING = PropertyHelper.readPropsValue(
					"READ_TIMEOUT_MILLIS.RENEWAL", props, null);
			if (READ_TIMEOUT_RENEWAL_STRING == null) {
				READ_TIMEOUT_RENEWAL = new Integer(60000);
			} else {
				try {
					READ_TIMEOUT_RENEWAL = Integer
							.parseInt(READ_TIMEOUT_RENEWAL_STRING);
				} catch (NumberFormatException nfe) {
					logger.sendEvent(
							"Unable to parse READ_TIMEOUT_MILLIS.RENEWAL, default to 60...",
							EventType.EXCEPTION, this);
					READ_TIMEOUT_RENEWAL = new Integer(60000);
				}
			}

			READ_TIMEOUT_STRING = PropertyHelper.readPropsValue(
					"READ_TIMEOUT_MILLIS", props, null);
			if (READ_TIMEOUT_STRING == null) {
				READ_TIMEOUT_STANDARD = new Integer(35000);
			} else {
				try {
					READ_TIMEOUT_STANDARD = Integer
							.parseInt(READ_TIMEOUT_STRING);
				} catch (NumberFormatException nfe) {
					logger.sendEvent(
							"Unable to parse READ_TIMEOUT_MILLIS, default to 35...",
							EventType.EXCEPTION, this);
					READ_TIMEOUT_STANDARD = new Integer(35000);
				}
			}

			INITIALIZED = true;

		} else {
			logger.sendEvent("DLRHTTPClient already INITIALIZED ...",
					EventType.DEBUG, this);
		}
	}

	/**
	 * This method sends the request XML passed in via the InputStream to the
	 * target server identified in the WALC.properties.
	 * 
	 */
	public String sendRequest(DTITransactionTO dtiTxn, String xmlRequest)
			throws DTIException {
		
		InputStream in = UtilityXML.getStringToInputStream(xmlRequest);
		Document docResponse = null;
		Document docRequest = null;
		URLConnection conn = null;
		String messageIdString = null;
		String xmlResponse = null;

		try {
			//TODO uncomment when we push COS
			//checkForCosRefresh();
			
			// create the document request
			logger.sendEvent("About to build docRequest ...", EventType.DEBUG,
					this);
			DocumentBuilderFactory docFactory = DocumentBuilderFactoryImpl
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			docRequest = docBuilder.parse(in);
			logger.sendEvent("Successfully INITIALIZED docRequest ...",
					EventType.DEBUG, this);

			// Capture In-bound Reference Number
			logger.sendEvent("About to capture In-bound Reference Number ...",
					EventType.DEBUG, this);
			NodeList nl2 = docRequest.getElementsByTagName("MessageID");
			Node tagNode = nl2.item(0);
			Node valNode = tagNode.getFirstChild();
			messageIdString = valNode.getNodeValue();
			logger.sendEvent("Message ID STRING is " + messageIdString,
					EventType.DEBUG, this);
			//TODO uncomment this to switch to cos endpoints
			//URL url = new URL(ENDPOINTS.get( dtiTxn.getTransactionType() ));
			//TODO remove url_string when we switch to COS
			URL url = new URL(URL_STRING);
			
			logger.sendEvent("About to send message to DLR provider system.",
					EventType.INFO, this);

			// Open Connection to eGalaxy
			conn = (URLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set read and connect time outs.
			conn.setConnectTimeout(Integer.parseInt(CONNECT_TIMEOUT_STRING));

			Integer readTimeoutValue;
			switch (dtiTxn.getTransactionType()) { // Updated logic as of
													// 2.16.2, JTL
			case RENEWENTITLEMENT:
				readTimeoutValue = READ_TIMEOUT_RENEWAL;
				break;
			default:
				readTimeoutValue = READ_TIMEOUT_STANDARD;
				break;
			}

			logger.sendEvent("Read timeout will be: " + readTimeoutValue,
					EventType.DEBUG, this);

			conn.setReadTimeout(readTimeoutValue);

			logger.sendEvent("Connection established to URL: "
					+ conn.getURL().toString(), EventType.DEBUG, this);

			// Sending Request to eGalaxy
			conn.setRequestProperty("Content-Type", "xml; charset=utf-8");
			OutputStream out = conn.getOutputStream();
			// NOTE: "DOM does not require implementations to be thread safe.
			// If you need to access the DOM from multiple threads, you are
			// required
			// to add the appropriate locks to your application code." - From
			// xerces2-j/faq-dom.html

			DOMImplementationRegistry registry = DOMImplementationRegistry
					.newInstance();
			DOMImplementationLS impl = (DOMImplementationLS) registry
					.getDOMImplementation("LS");
			LSSerializer writer = impl.createLSSerializer();
			LSOutput output = impl.createLSOutput();
			output.setByteStream(out);
			writer.write(docRequest, output);
			out.close();

			// MASK OUT <Endorsement>ZZZZZZZZZZZZZZZZ</Endorsement>
			// Used for Debugging
			String xmlMasked = PCIControl.overwritePciDataInXML(xmlRequest);
			logger.sendEvent("Document being sent TO DLR http server: "
					+ xmlMasked, EventType.DEBUG, this);

			// Getting response from eGalaxy
			InputStream response = conn.getInputStream();

			// Create the document response
			docFactory = DocumentBuilderFactoryImpl.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			docResponse = docBuilder.parse(response);
			xmlResponse = UtilityXML.getDocumentToString(docResponse);
			xmlMasked = PCIControl.overwritePciDataInXML(xmlResponse);

			logger.sendEvent(
					"Just received response from DLR provider system.",
					EventType.INFO, this);

		} catch (SocketTimeoutException ste) {
			logger.sendEvent(
					"Socket Timeout going to HTTP client: " + ste.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TRANSACTION_TIME_OUT,
					"Transaction timed out going to provider.");
		} catch (SAXParseException se) {
			logger.sendEvent("Error in incoming xml: " + se.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Unable to parse the response from the provider.");
		} catch (IOException ioe) {
			// Event Log something
			logger.sendEvent("IOException sending xml: " + ioe.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Unable to complete communication with the provider.");
		} catch (Exception e) {
			logger.sendEvent("Error: " + e.toString(), EventType.EXCEPTION,
					this);
			e.printStackTrace();
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.UNDEFINED_FAILURE,
					"Unable to complete communication with the provider.");
		}

		logger.sendEvent("Exiting processRequest()", EventType.DEBUG, this);

		return xmlResponse;
	}

	/**
	 * Test Utility Method (presently not used)
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String getXMLfromFile(String fileName) throws IOException {

		StringBuffer sb = new StringBuffer();

		try {

			FileReader file = new FileReader(fileName);
			BufferedReader buff = new BufferedReader(file);
			boolean eof = false;
			while (!eof) {
				String line = buff.readLine();
				if (line == null)
					eof = true;
				else
					sb.append(line);
			}

			buff.close();
			file.close();

		} catch (IOException ioe) {
			throw ioe;
		}

		String xml = sb.toString();

		return xml;

	}
	
	/**
	 * Read class of service endpoints helper.
	 *
	 * @return the hashtable
	 * @throws DTIException 
	 */
	/**
	 * Read class of service endpoints helper.
	 *
	 * @return the hashtable
	 */
	protected static Hashtable<String, String> loadClassOfServiceEndpoints() {
		Hashtable<String, String> endpoints = new Hashtable<String, String>();

	    // Get the cos grp	    
		ArrayList<CosTpGrpCmdTO> cosList = new ArrayList<CosTpGrpCmdTO>();
	    try {
	    		cosList = CosTpGrpCmdKey.getTpCosGrpCmd("DLR");
	    		for (CosTpGrpCmdTO cosTpGrp : cosList) {
	    	        endpoints.put(cosTpGrp.getCmdcode().toUpperCase() ,cosTpGrp.getEndpointurl());
	    	        logger.sendEvent("DLRHTTPClient added" + cosTpGrp.getCmdcode().toUpperCase() + " with " + cosTpGrp.getEndpointurl(), EventType.INFO, null );
	    	    }
		} catch (Exception e) {
			logger.sendEvent(
					"Unable to load WDW Class of Service End"
					+ "points...",
					EventType.EXCEPTION, DLRHTTPClient.class);
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
	private  void checkForCosRefresh() throws DTIException {
		if (new Date().getTime() >= cosEndpointsNextRead) {
			//set the time for next database refresh
			String threadInfo = Thread.currentThread().getId() + ": " + Thread.currentThread().getName();
			logger.sendEvent("DLRHTTPClient Class of service determined that endpoint refresh is required by thread: " + threadInfo, EventType.DEBUG, this);
			DLRHTTPClient.loadClassOfServiceEndpoints();
		}
	}
}
