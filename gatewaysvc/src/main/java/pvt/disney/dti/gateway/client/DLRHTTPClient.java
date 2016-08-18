package pvt.disney.dti.gateway.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

// New with 2.11
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.LSOutput;
// End of new with 2.11



import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.UtilityXML;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;

/**
 * This class contains the logic required to communicate with DLR's eGalaxy system via HTTP/S.
 * 
 * @author lewit019, moons012
 * 
 */
public class DLRHTTPClient {

	/**
	 * shared core event logger.
	 */
	private EventLogger logger;

	/**
	 * Properties file
	 */
	private Properties props;

	/**
	 * initialized once during first instantiation
	 */
	private static String ACTIVE_CONNECTION = "";

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

		AbstractInitializer abstrInit = AbstractInitializer.getInitializer();
		props = abstrInit.getProps("DLRCLIENT.properties");
		
		//this may be overwritten, depending on transaction type
		READ_TIMEOUT_STRING = PropertyHelper.readPropsValue("READ_TIMEOUT_MILLIS", props, null);

		// initialize only once
		if (!INITIALIZED) {

			logger.sendEvent("About to Initialize DLRCLIENT.properties file ...",EventType.DEBUG, this);
			logger.sendEvent("Successfully Initialized DLRCLIENT.properties file ...", EventType.DEBUG, this);
			ACTIVE_CONNECTION = PropertyHelper.readPropsValue("ACTIVECONN",	props, null);
			URL_STRING = PropertyHelper.readPropsValue(ACTIVE_CONNECTION,props, null);
			CONNECT_TIMEOUT_STRING = PropertyHelper.readPropsValue("CONNECT_TIMEOUT_MILLIS", props, null);

			INITIALIZED = true;

		}
		else {
			logger.sendEvent("DLRHTTPClient already INITIALIZED ...",
					EventType.DEBUG, this);
		}
	}

	/**
	 * This method sends the request XML passed in via the InputStream to the target server identified in the WALC.properties.
	 * 
	 */
	public String sendRequest(DTITransactionTO dtiTxn, String xmlRequest) throws DTIException {

		InputStream in = UtilityXML.getStringToInputStream(xmlRequest);
		Document docResponse = null;
		Document docRequest = null;
		URLConnection conn = null;
		String messageIdString = null;
		String xmlResponse = null;

		try {

			// create the document request
			logger.sendEvent("About to build docRequest ...", EventType.DEBUG,
					this);
			DocumentBuilderFactory docFactory = DocumentBuilderFactoryImpl.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			docRequest = docBuilder.parse(in);
			logger.sendEvent("Successfully INITIALIZED docRequest ...",	EventType.DEBUG, this);

			// Capture In-bound Reference Number
			logger.sendEvent("About to capture In-bound Reference Number ...",	EventType.DEBUG, this);
			NodeList nl2 = docRequest.getElementsByTagName("MessageID");
			Node tagNode = nl2.item(0);
			Node valNode = tagNode.getFirstChild();
			messageIdString = valNode.getNodeValue();
			logger.sendEvent("Message ID STRING is " + messageIdString,
					EventType.DEBUG, this);

			URL url = new URL(URL_STRING);

			logger.sendEvent("About to send message to DLR provider system.",
					EventType.INFO, this);

			// Open Connection to eGalaxy
			conn = (URLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Set read and connect time outs.
			conn.setConnectTimeout(Integer.parseInt(CONNECT_TIMEOUT_STRING));

			switch (dtiTxn.getTransactionType()) {
			case RENEWENTITLEMENT:
				READ_TIMEOUT_STRING = PropertyHelper.readPropsValue("READ_TIMEOUT_MILLIS.RENEWAL", props, null);
				break;
			default:
				break;
			}
			conn.setReadTimeout(Integer.parseInt(READ_TIMEOUT_STRING));

			logger.sendEvent("Connection established to URL: " + conn.getURL()
					.toString(), EventType.DEBUG, this);

			// Sending Request to eGalaxy
			conn.setRequestProperty("Content-Type", "xml; charset=utf-8");
			OutputStream out = conn.getOutputStream();
			// NOTE: "DOM does not require implementations to be thread safe.
			// If you need to access the DOM from multiple threads, you are required
			// to add the appropriate locks to your application code." - From xerces2-j/faq-dom.html

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
			logger.sendEvent(
					"Document being sent TO DLR http server: " + xmlMasked,
					EventType.DEBUG, this);

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

		}
		catch (SocketTimeoutException ste) {
			logger.sendEvent(
					"Socket Timeout going to HTTP client: " + ste.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TRANSACTION_TIME_OUT,
					"Transaction timed out going to provider.");
		}
		catch (SAXParseException se) {
			logger.sendEvent("Error in incoming xml: " + se.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Unable to parse the response from the provider.");
		}
		catch (IOException ioe) {
			// Event Log something
			logger.sendEvent("IOException sending xml: " + ioe.toString(),
					EventType.EXCEPTION, this);
			throw new DTIException(DLRHTTPClient.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Unable to complete communication with the provider.");
		}
		catch (Exception e) {
			logger.sendEvent("Error:" + e.toString(), EventType.EXCEPTION, this);
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
				if (line == null) eof = true;
				else sb.append(line);
			}

			buff.close();
			file.close();

		}
		catch (IOException ioe) {
			throw ioe;
		}

		String xml = sb.toString();

		return xml;

	}
}
