package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.rules.DateTimeRules;

/**
 * General format and conversion utility. Methods and attributes in this class are used to format String and convert datatypes and should be static.
 * 
 * @author Todd Lewis
 */
public abstract class UtilityXML {

	/**
	 * Takes a Document and returns it as a formatted String.
	 * 
	 * @return java.lang.String
	 * @param doc
	 *            An XML Document.
	 */
	public static String getDocumentToString(Document doc) throws Exception {
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
				throw e;
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
	 * Takes in a date string and returns a Gregorian Calendar. Date is expected to be in format yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 * @return GregorianCalendar
	 */
	public static GregorianCalendar getGCalFromEGalaxyDate(String dateString) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		GregorianCalendar aCal = new GregorianCalendar();

		try {
			Date aDate = dateFormat.parse(dateString);
			aCal.setTime(aDate);
		}
		catch (ParseException pe) {
			return null;
		}

		return aCal;
	}

	/**
	 * Returns an eGalaxy formatted date, when given a GregorianCalendar object yyyy-MM-dd HH:mm:ss.SS
	 * 
	 * @param timeStamp
	 * @return String date formatted yyyy-MM-dd HH:mm:ss.SS
	 */
	public static String getEGalaxyDateFromGCal(GregorianCalendar timeStamp) {

		Date now = timeStamp.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SS");

		String dataString = dateFormat.format(now);

		return dataString;
	}

	/**
	 * Returns an eGalaxy formatted date, when given a GregorianCalendar object yyyy-MM-dd
	 * 
	 * @param timeStamp
	 * @return String date formatted yyyy-MM-dd HH:mm:ss.SS
	 */
	public static String getEGalaxyDateFromGCalNoTime(
			GregorianCalendar timeStamp) {

		Date now = timeStamp.getTime();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String dataString = dateFormat.format(now);

		return dataString;
	}

  /**
   * Returns an eGalaxy formatted date for the current date/time.
   * 
   * Changing eGalaxy time to PT as of 2.17.2.1 (JTL)
   * @return
   */
	public static String getCurrentEgalaxyDate() {
		Date now = DateTimeRules.getPTNow();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SS");

		String dataString = dateFormat.format(now);

		return dataString;
	}

	/**
	 * A simple routine to locate the value of a unique XML tag in an XML string. returns empty string if tag is empty.
	 * 
	 * @param xml
	 *            All of the XML to be searched as one string.
	 * @param tagName
	 *            Name of the tag being searched. Don't include greater than, less than, or slash.
	 * @return the String value of the desired tag (empty string is returned for an empty XML tag or null if not present).
	 */
	public static String getTagData(String xml, String tagName) {

		// Locate the error tag in the string.
		int startTag = xml.indexOf("<" + tagName + ">");
		int endTag = xml.indexOf("</" + tagName + ">");

		if ((startTag == -1) || (endTag == -1)) {

			if ((startTag == -1) && (endTag == -1)) {

				// Tag not present, or empty tag
				int emptyTag = xml.indexOf("<" + tagName + "/>");
				if (emptyTag != -1) return "";
			}

			// Tags without partners
			return null;
		}

		startTag += tagName.length() + 2; // Move pointer to start of error
											// value.

		String tagString = xml.substring(startTag, endTag);

		return tagString;
	}

	/**
	 */
	public static ArrayList<String> getRecurringTagData(String xml,
			String tagName) {

		ArrayList<String> recurringTags = new ArrayList<String>();

		// Locate the first occurrence of the tag in the string.
		String searchString = xml;
		int startTag = searchString.indexOf("<" + tagName + ">");

		while (startTag != -1) {

			int endTag = searchString.indexOf("</" + tagName + ">");
			if ((startTag == -1) || (endTag == -1)) {

				if ((startTag == -1) && (endTag == -1)) {

					// Tag not present, or empty tag
					int emptyTag = searchString.indexOf("<" + tagName + "/>");
					if (emptyTag != -1) {
						String emptyTagString = "";
						recurringTags.add(emptyTagString);
						return recurringTags;
					}

				}

				// Tags without partners
				return recurringTags;
			}

			startTag += tagName.length() + 2; // Move pointer to start of tag
												// value.
			String tagString = searchString.substring(startTag, endTag);
			recurringTags.add(tagString);

			searchString = searchString
					.substring(endTag + tagName.length() + 3);
			startTag = searchString.indexOf("<" + tagName + ">");

		}

		return recurringTags;
	}

	/**
	 * A simple routine to locate the value of a unique XML tag in an XML string. returns false if the tag isn't present; true if it is present.
	 * 
	 * @param xml
	 *            All of the XML to be searched as one string.
	 * @param tagName
	 *            Name of the tag being searched. Don't include greater than, less than, or slash.
	 * @return the String value of the desired tag (empty string is returned for an empty XML tag or null if not present).
	 */
	public static boolean isTagPresent(String xml, String tagName) {

		// Locate the error tag in the string.
		int startErrorTag = xml.indexOf("<" + tagName + ">");
		if (startErrorTag == -1) return false;
		else return true;
	}

	/**
	 * 
	 * @return the current date in "2006-09-28" format.
	 */
	public static String getCurrentDTIDate() {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(now);

		return dateString;
	}

	/**
	 * 
	 * @return the current date in "04:59:53.000" format.
	 */
	public static String getCurrentDTITime() {

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		String dateString = sdf.format(now);

		return dateString;
	}

}