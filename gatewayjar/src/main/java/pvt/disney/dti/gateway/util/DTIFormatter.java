package pvt.disney.dti.gateway.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.constants.TagConstants;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * General format and conversion utility. Methods and attributes in this class are used to format String and convert datatypes and should be static.
 * 
 * JTL - There was a huge amount of this class that DTI doesn't use, has been more than adequately covered in the Java class library itself, or is too easy to implement to merit another method call. Those routines have been cleaned up.
 * 1/20/05.
 * 
 * Also, since this class contains only static variables and methods, there's no reason to instantiate it other than the need to pass in an object when logging. To that effect, when logging is attempted, Formatter.getInstance() is called.
 * It follows the Singleton pattern with the exception that getInstance is ALSO private.
 * 
 * @author Andy Anon
 * @since 6/21/01 11:58:22 AM
 * @version %version: 2 %
 */
public class DTIFormatter {

	/** The standard core logging mechanism. */
	private static EventLogger eventLogger = EventLogger.getLogger("Formatter");

	public static final char FALSE_VALUE_1_F = 'F';
	public static final char FALSE_VALUE_2_f = 'f';
	public static final char FALSE_VALUE_3_N = 'N';
	public static final char FALSE_VALUE_4_n = 'n';
	public static final char FALSE_VALUE_5_0 = '0';
	public static final char TRUE_VALUE_1_T = 'T';
	public static final char TRUE_VALUE_2_t = 't';
	public static final char TRUE_VALUE_3_Y = 'Y';
	public static final char TRUE_VALUE_4_y = 'y';
	public static final char TRUE_VALUE_5_1 = '1';

	private static Object thisInstance = null;

	/**
	 * The standard constructor.
	 * 
	 */
	private DTIFormatter() {
		super();
	}

	/**
	 * Singleton pattern required to get object reference for logging.
	 * 
	 * @return thisInstance
	 */
	private static Object getInstance() {

		if (thisInstance == null) {
			thisInstance = new DTIFormatter();
			return thisInstance;
		}
		else {
			return thisInstance;
		}

	}

	/**
	 * <p>
	 * Return boolean primitive, based on given character.
	 * <p>
	 * <code>true</code>: T,t,Y, y, 1<br>
	 * <code>false</code>: anything else
	 * 
	 * @return boolean
	 * @param b
	 *            java.lang.String
	 */
	public static boolean toBoolean(char b) {

		switch (b) {
		case TRUE_VALUE_1_T:
		case TRUE_VALUE_2_t:
		case TRUE_VALUE_3_Y:
		case TRUE_VALUE_4_y:
		case TRUE_VALUE_5_1:
			return true;
		case FALSE_VALUE_1_F:
		case FALSE_VALUE_2_f:
		case FALSE_VALUE_3_N:
		case FALSE_VALUE_4_n:
		case FALSE_VALUE_5_0:

		default:
			return false;
		}

	}

	/**
	 * Return boolean primitive, based on first character in String;
	 * 
	 * @return boolean
	 * @param b
	 *            java.lang.String
	 */
	public static boolean toBoolean(String b) {
		if ((b == null) || (b.length() == 0)) return false;
		return toBoolean(b.charAt(0));
	}

	/**
	 * String -> int
	 * 
	 * @return int
	 * @param s
	 *            java.lang.String
	 */
	public static int toInt(String s) {
		if (s == null) return 0;
		if (s.equals("")) return 0;
		return Integer.parseInt(s);
	}

	/**
	 * long -> String
	 * 
	 * @return String
	 * @param data
	 *            long
	 */
	public static String toString(long data) {
		return Long.toString(data);
	}

	/**
	 * Returns date in reversed format yyyy/MM/dd where the input format was MM/dd/yyyy and converts the '/' to '-'. Final result yyyy-MM-dd
	 * 
	 * @return java.lang.String
	 * @param date
	 *            java.lang.String
	 */
	public static String dateForDB(String date) {

		// declarations
		StringBuffer resultString = null;

		if (date == null) {
			return date;
		}
		else {
			String year = date.substring(0, date.indexOf("-"));
			String month = date.substring(year.length() + 1,
					date.indexOf("-", year.length() + 1));
			String day = date.substring(date.lastIndexOf("-") + 1,
					date.indexOf(" ", date.lastIndexOf(" ")));
			String time = date.substring(date.lastIndexOf(" "), date.length());
			resultString = new StringBuffer(
					month + "/" + day + "/" + year + " " + time);
		}
		return resultString.toString();

	}

	/**
	 * <p>
	 * Takes a <code>File<code> and returns it as a <code>String</code> formatted <br>
	 * This is used for testing only.
	 * 
	 * @return String
	 * @param f
	 *            java.io.File
	 */
	public static String getFileToString(File f) {
		// given a file return the string value minus returns and spaces

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			int nextLine = 0;
			while (nextLine > -1) {
				String line = in.readLine();
				// remove tabs and returns
				sb.append(line);
				if (nextLine == 0) {
					sb.append("<");
				}
				nextLine = in.read();

			}
		}
		catch (Exception e) {
			eventLogger.sendEvent(
					"Error when converting File to String because: " + e
							.toString(), EventType.DEBUG, DTIFormatter
							.getInstance());
		}
		return sb.toString();
	}

	/**
	 * Takes a Document and returns it as a formatted String.
	 * 
	 * @return java.lang.String
	 * @param doc
	 *            An XML Document.
	 */
	public static String getDocumentToString(Document doc) {
		// given a string write to file and instatiate
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
										.toString(), EventType.DEBUG,
								DTIFormatter.getInstance());
			}
		}
		returnData = sw.getBuffer().toString();
		return returnData;
	}

	/**
	 * Takes a String and returns it as an InputStream.
	 * 
	 * @return InputStream
	 * @param str
	 *            java.lang.String
	 */
	public static InputStream getStringToInputStream(String str) {
		ByteArrayInputStream istream = null;

		byte[] recv = str.getBytes();
		istream = new ByteArrayInputStream(recv);

		return istream;
	}

	/**
	 * Takes an InputStream and returns it as a String.
	 * 
	 * @return InputStream
	 * @param str
	 *            java.lang.String
	 */
	public static String getInputStreamToString(InputStream str) {

		StringBuffer stringBuff = new StringBuffer();
		boolean eof = false;

		BufferedReader buffReader = new BufferedReader(new InputStreamReader(
				str));

		try {

			while (!eof) {
				String readLine = buffReader.readLine();
				if (readLine == null) {
					eof = true;
				}
				else {
					stringBuff.append(readLine);
				}
			}

		}
		catch (IOException ioe) {
			eventLogger
					.sendEvent(
							"DTIFormatter:  IOException in getInputStreamToString: " + ioe
									.toString(), EventType.DEBUG, DTIFormatter
									.getInstance());
		}

		return stringBuff.toString();
	}

	/**
	 * Replaces non-websafe characters with +.
	 * 
	 * @param inString
	 * @return String that has been websafed.
	 * 
	 */
	public static String websafe(String inString) {

		if (inString == null) {
			return null;
		}
		if (inString.length() == 0) {
			return inString;
		}
		else {
			return inString.replaceAll("[!-&/<>\\\\\t\n\r\f]", "+");
		}
	}

	/**
	 * Replaces non-websafe characters with + and forces them to upper case.
	 * 
	 * @param inString
	 * @return String that has been websafed.
	 * 
	 */
	public static String websafeToUpper(String inString) {

		if (inString == null) {
			return null;
		}
		if (inString.length() == 0) {
			return inString;
		}
		else {
			return websafe(inString).toUpperCase();
		}
	}

	/**
	 * 
	 * @param dtiTxn
	 */
	public static void formatDefaultDTIResponseHeaders(DTITransactionTO dtiTxn,
			DTIResponseTO dtiResp) {

		// Create a spare payload header
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date today = new Date();
		String sparePayloadId = TagConstants.INTERNAL_PAYLOAD_PREFIX + sdf
				.format(today);

		// Build Payload Header
		// ====================
		PayloadHeaderTO payHdrIn = dtiTxn.getRequest().getPayloadHeader();
		PayloadHeaderTO payHdrOut = new PayloadHeaderTO();

		// PayloadId
		if (dtiTxn.getTpRefNum() != null) {
			payHdrOut.setPayloadID(dtiTxn.getTpRefNum().toString());
		}
		else {
			if ((payHdrIn.getPayloadID() != null) && (payHdrIn.getPayloadID()
					.length() > 3)) {
				StringBuffer strBuf = new StringBuffer(payHdrIn.getPayloadID());
				strBuf.replace(0, 2, TagConstants.INTERNAL_PAYLOAD_PREFIX);
				payHdrOut.setPayloadID(strBuf.toString());
			}
			else {
				payHdrOut.setPayloadID(sparePayloadId);
			}
		}

		// TspayloadId
		if (payHdrIn.getPayloadID() != null) {
			payHdrOut.setTsPayloadID(payHdrIn.getPayloadID());
		}
		else {
			payHdrOut.setTsPayloadID(sparePayloadId);
		}

		// Target
		if (payHdrIn.getTarget() != null) {
			payHdrOut.setTarget(payHdrIn.getTarget());
		}
		else {
			payHdrOut.setTarget(TagConstants.UNKNOWN_TARGET);
		}

		// Version
		if (payHdrIn.getVersion() != null) {
			payHdrOut.setVersion(payHdrIn.getVersion());
		}
		else {
			payHdrOut.setVersion(TagConstants.VERSION_1_0);
		}

		// Comm Protocol
		if (payHdrIn.getCommProtocol() != null) {
			payHdrOut.setCommProtocol(payHdrIn.getCommProtocol());
		}
		else {
			payHdrOut.setCommProtocol(TagConstants.INTERNET_PROTOCOL);
		}

		// Comm Method
		if (payHdrIn.getCommMethod() != null) {
			payHdrOut.setCommMethod(payHdrIn.getCommMethod());
		}
		else {
			payHdrOut.setCommMethod(TagConstants.COMM_METHOD_NETWORK);
		}

		// Transmit Date Time
		payHdrOut.setTransmitDateTime(new GregorianCalendar());

		// Ticket Broker
		payHdrOut.setTktBroker(dtiTxn.getTktBroker());

		// Command Count
		payHdrOut.setCommandCount(new BigInteger("1"));

		dtiResp.setPayloadHeader(payHdrOut);

		// Build Command Header
		// ====================
		CommandHeaderTO cmdHdrIn = dtiTxn.getRequest().getCommandHeader();
		CommandHeaderTO cmdHdrOut = new CommandHeaderTO();

		// CmdItem
		if (cmdHdrIn.getCmdItem() != null) {
			cmdHdrOut.setCmdItem(cmdHdrIn.getCmdItem());
		}
		else {
			cmdHdrOut.setCmdItem(new BigInteger(TagConstants.CMD_ITEM_1));
		}

		// CmdDuration
		cmdHdrOut.setCmdDuration(new BigDecimal(TagConstants.ZERO_DURATION));

		// Cmd Date Time
		cmdHdrOut.setCmdDateTime(new GregorianCalendar());

		dtiResp.setCommandHeader(cmdHdrOut);

		return;
	}

}