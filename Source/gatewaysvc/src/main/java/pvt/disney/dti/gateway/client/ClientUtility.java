package pvt.disney.dti.gateway.client;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VisitorSupport;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * 
 * @author lewit019
 * 
 */
public class ClientUtility {

	/**
	 * shared core event logger.
	 */
	private static EventLogger logger = EventLogger
			.getLogger(ClientUtility.class);

	private static ClientUtility instance = null;

	/**
	 * Remove the soap envelope from a response
	 * 
	 * @param response
	 * @return
	 */
	public static String convertResponse(String response) throws Exception {

		String convertedResp = null;

		// int cmdIdx = response.indexOf("Command");
		// int hdrIdx = response.indexOf("Header");
		// String testString = response.substring(cmdIdx,hdrIdx);
		// int tagStartIdx = testString.indexOf("<");
		// int colonIdx = testString.indexOf(":",tagStartIdx);
		// String prefix = testString.substring(tagStartIdx + 1,colonIdx);

		convertedResp = cleanupPrefixes(response, "tns");
		convertedResp = cleanupResNS(convertedResp);

		return convertedResp;
	}

	/**
	 * 
	 * @param xml
	 * @param prefix
	 * @return
	 */
	private static String cleanupPrefixes(String xml, String prefix) {
		CharSequence inputStr = xml;
		prefix = prefix.concat(":");
		String patternOpenStr = prefix;

		// Compile regular expression
		Pattern pattern = Pattern.compile(patternOpenStr);
		Matcher matcher = pattern.matcher(inputStr);

		// Replace all occurrences of pattern in input
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			// Get the match result
			String replaceStr = matcher.group();
			// convert prefix to nothing
			replaceStr = "";
			// Insert replacement
			matcher.appendReplacement(buf, replaceStr);
		}
		matcher.appendTail(buf);

		// Get result
		String result = buf.toString();

		return result;

	}

	/**
	 * 
	 * @param xml
	 * @return
	 */
	private static final String cleanupResNS(String xml) {
		CharSequence inputStr = xml;

		String patternOpenStr = "<Command [^>]*>";

		// Compile regular expression
		Pattern pattern = Pattern.compile(patternOpenStr);
		Matcher matcher = pattern.matcher(inputStr);

		// Replace all occurrences of pattern in input
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {

			// Get the match result
			String replaceStr = matcher.group();
			// Convert namespace
			replaceStr = "<Command>"; // : If required, replace with the
			// correct
			// Insert replacement
			matcher.appendReplacement(buf, replaceStr);
		}
		matcher.appendTail(buf);

		// Get result
		String result = buf.toString();

		return result;
	}

	/**
	 * Internal helper method to convert to IAGO XML web service format.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String convertRequest(String request) throws Exception {

		if (instance == null) {
			instance = new ClientUtility();
		}

		logger.sendEvent("IAGOSoapClient entering convertRequest() ...",
				EventType.DEBUG, instance);
		String convertedRequest = null;

		Document document = null;

		document = DocumentHelper.parseText(request);
		// Element root = document.getRootElement();

		String prefix = "iagoReq";
		String requestSubType = document.valueOf("//RequestSubType");

		String nsString = getReqNsStringFromType(requestSubType);

		Namespace oldNs = document.getRootElement().getNamespace();
		Namespace newNs = Namespace.get(prefix, nsString);
		VisitorSupport visitor = instance.new NamespaceChangingVisitor(oldNs,
				newNs);

		document.accept(visitor);

		document.getRootElement().addNamespace("iagoAns",
				getAnsNsStringFromType(requestSubType));

		convertedRequest = document.getRootElement().asXML();

		logger.sendEvent("IAGOSoapClient exiting convertRequest() ...",
				EventType.DEBUG, instance);

		return wrapInSOAP(convertedRequest);
	}

	/**
	 * Internal helper method to get the name space string for a IAGO XML message using the RequestSubType
	 * 
	 * @param requestSubType
	 * @return
	 */
	private static String getReqNsStringFromType(String requestSubType) {
		String nsString = "http://services.nexus.wdw.com/NexusTicketingService/types/";
		nsString = nsString + requestSubType + "Request";
		return nsString;
	}

	/**
	 * Internal helper method to get the name space string for a IAGO XML message using the RequestSubType
	 * 
	 * @param requestSubType
	 * @return
	 */
	private static String getAnsNsStringFromType(String requestSubType) {
		String nsString = "http://services.nexus.wdw.com/NexusTicketingService/types/";
		nsString = nsString + requestSubType + "Answer";
		return nsString;
	}

	/**
	 * Adds the SOAP header and footer.
	 * 
	 * @param request
	 * @return
	 */
	private static String wrapInSOAP(String request) {

		String soapRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" + "<soapenv:Header/> <soapenv:Body>" + request + "</soapenv:Body> </soapenv:Envelope>";

		return soapRequest;
	}

	/**
	 * Removes the soap header and footer
	 * 
	 * @param response
	 * @return
	 */
	public static String unwrapFromSOAP(String response) {

		// From <soapenv:Body> to </soapenv:Body>

		// Name space?
		int envelope = response.indexOf(":Envelope");
		int firstTagLt = response.substring(0, envelope).lastIndexOf("<");
		String nameSpace = response.substring(firstTagLt + 1, envelope);

		// Location of Command opening tag?
		int bodyStart = response.indexOf("<" + nameSpace + ":Body>");
		int bodyStartLength = new String("<" + nameSpace + ":Body>").length();
		int commandStart = response.indexOf("<", bodyStart + bodyStartLength);

		// Location of Command Closing tag?
		int bodyEnd = response.indexOf("</" + nameSpace + ":Body>");
		int commandEnd = bodyEnd;

		String requestSubstring = response.substring(commandStart, commandEnd);

		return requestSubstring;

	}

	/**
	 * Internal helper class. Implements the visitor pattern to change the namespace for each element
	 * 
	 * @author smoon
	 * 
	 */
	public class NamespaceChangingVisitor extends VisitorSupport {
		private Namespace from;

		private Namespace to;

		public NamespaceChangingVisitor(Namespace from, Namespace to) {
			this.from = from;
			this.to = to;
		}

		public void visit(Element node) {
			Namespace ns = node.getNamespace();

			if (ns.getURI().equals(from.getURI())) {
				QName newQName = new QName(node.getName(), to);
				node.setQName(newQName);
			}

			@SuppressWarnings("rawtypes")
			ListIterator namespaces = node.additionalNamespaces()
					.listIterator();
			while (namespaces.hasNext()) {
				Namespace additionalNamespace = (Namespace) namespaces.next();
				if (additionalNamespace.getURI().equals(from.getURI())) {
					namespaces.remove();
				}
			}
		}
	}

}
