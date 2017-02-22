package pvt.disney.dti.gateway.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;

public class DTITestUtilities {

	/**
	 * Gets the XML from a file.
	 * 
	 * @param fileName
	 *            the file name
	 * 
	 * @return the XML from file
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getXMLfromFile(String fileName) throws IOException {

		StringBuffer sb = new StringBuffer();

		try {

			File f=new File(fileName);
			f.getAbsoluteFile();
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
	 * A simple routine to locate the value of a unique XML tag in an XML
	 * string. returns empty string if tag is empty.
	 * 
	 * @param xml
	 *            All of the XML to be searched as one string.
	 * @param tagName
	 *            Name of the tag being searched. Don't include greater than,
	 *            less than, or slash.
	 * @return the String value of the desired tag (empty string is returned for
	 *         an empty XML tag or null if not present).
	 */
	public static String findTag(String xml, String tagName) {

		// Locate the tag in the string.
		int startErrorTag = xml.indexOf("<" + tagName + ">");
		int endErrorTag = xml.indexOf("</" + tagName + ">");

		if ((startErrorTag == -1) || (endErrorTag == -1)) {
			if (xml.indexOf("<" + tagName + "/>") != -1)
				return "";
			else
				return null;
		}

		// Move pointer to start of the value.
		startErrorTag += tagName.length() + 2;

		if (startErrorTag == endErrorTag) {
			return "";
		}

		String tagString = xml.substring(startErrorTag, endErrorTag);

		return tagString;
	}

	/**
	 * Compare XML.
	 * 
	 * @param newXML
	 *            the new XML
	 * @param baselineXML
	 *            the baseline XML
	 * @param sectionName
	 *            the section name
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public static void compareXML(String baselineXML, String newXML,
			String sectionName) throws Exception {

		String trimBase = clearXMLWhiteSpace(baselineXML);
		String trimNew = clearXMLWhiteSpace(newXML);
		StringBuffer exceptionMessage = new StringBuffer();
		int compareLength = 0;

		if (trimBase.compareTo(trimNew) != 0) {

			if (trimBase.length() < trimNew.length())
				compareLength = trimBase.length();
			else
				compareLength = trimNew.length();

			for (int i = 0; i < compareLength; i++) {

				if (trimBase.charAt(i) != trimNew.charAt(i)) {

					exceptionMessage.append("Invalid XML generated in section "
							+ sectionName + ": ");
					exceptionMessage.append("\nBaseline XML Version:\n");
					String baseVersion = null;

					if (trimBase.length() < 60)
						baseVersion = trimBase;
					else if (((i - 30) < 0) && ((i + 31) < trimBase.length()))
						baseVersion = trimBase.subSequence(0, i + 30)
								.toString();
					else if (((i - 30) >= 0) && ((i + 31) > trimBase.length()))
						baseVersion = trimBase.subSequence(i - 30,
								trimBase.length() - 1).toString();
					else if (((i - 30) > 0) && ((i + 31) < trimBase.length()))
						baseVersion = trimBase.subSequence(i - 30, i + 30)
								.toString();

					exceptionMessage.append(baseVersion);

					exceptionMessage.append("\nNew XML Version:\n");

					String newVersion = null;
					if (trimNew.length() < 60)
						newVersion = trimNew;
					else if (((i - 30) < 0) && ((i + 31) < trimNew.length()))
						newVersion = trimNew.subSequence(0, i + 30).toString();
					else if (((i - 30) >= 0) && ((i + 31) > trimNew.length()))
						newVersion = trimNew.subSequence(i - 30,
								trimNew.length() - 1).toString();
					else if (((i - 30) > 0) && ((i + 31) < trimNew.length()))
						newVersion = trimNew.subSequence(i - 30, i + 30)
								.toString();

					exceptionMessage.append(newVersion);

					throw new Exception(exceptionMessage.toString());

				}

			}

		}

	}

	/**
	 * NOTE: This will remove trailing whitespace at the end of an XML value.
	 * Please use another method to validate those tags.
	 * 
	 * @param inXML
	 *            the in xml
	 * 
	 * @return the string
	 */
	private static String clearXMLWhiteSpace(String inXML) {

		// Remove all white space between tags
		String workString = inXML.trim();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < workString.length(); i++) {

			if (workString.charAt(i) == '<') {
				String tempString = sb.toString().trim();
				sb = new StringBuffer(tempString);
			}

			sb.append(workString.charAt(i));

		}

		String test = sb.toString();

		return test;
	}

	/**
	 * A simple routine to locate the value of a unique XML tag in an XML
	 * string. returns empty string if tag is empty.
	 * 
	 * @param xml
	 *            All of the XML to be searched as one string.
	 * @param tagName
	 *            Name of the tag being searched. Don't include greater than,
	 *            less than, or slash.
	 * @return the String value of the desired tag (empty string is returned for
	 *         an empty XML tag or null if not present).
	 */
	public static String getTagData(String xml, String tagName) {

		// Locate the error tag in the string.
		int startTag = xml.indexOf("<" + tagName + ">");
		int endTag = xml.indexOf("</" + tagName + ">");

		if ((startTag == -1) || (endTag == -1)) {

			if ((startTag == -1) && (endTag == -1)) {

				// Tag not present, or empty tag
				int emptyTag = xml.indexOf("<" + tagName + "/>");
				if (emptyTag != -1)
					return "";
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
	 * A simple routine to locate the value of a unique XML tag in an XML
	 * string. returns false if the tag isn't present; true if it is present.
	 * 
	 * @param xml
	 *            All of the XML to be searched as one string.
	 * @param tagName
	 *            Name of the tag being searched. Don't include greater than,
	 *            less than, or slash.
	 * @return the String value of the desired tag (empty string is returned for
	 *         an empty XML tag or null if not present).
	 */
	public static boolean isTagPresent(String xml, String tagName) {

		// Locate the error tag in the string.
		int startErrorTag = xml.indexOf("<" + tagName + ">");
		if (startErrorTag == -1)
			return false;
		else
			return true;
	}

	/**
	 * Helper method. Creates a dom4j document from a file containing xml and
	 * returns it to the caller.
	 * 
	 * @param aFile
	 * @return
	 * @throws DocumentException
	 */
	public static org.dom4j.Document getDom4jDocFromFile(File aFile)
			throws DocumentException {
		SAXReader xmlReader = new SAXReader();
		org.dom4j.Document doc = xmlReader.read(aFile);
		return doc;
	}

	/**
	 * Helper Method. Creates a dom4j document from the file represented by the
	 * passed in file name and returns it to the caller.
	 * 
	 * @param aFileName
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static org.dom4j.Document getDom4jDocFromFile(String aFileName)
			throws IOException, DocumentException {
		String xml = getXMLfromFile(aFileName);
		org.dom4j.Document document = DocumentHelper.parseText(xml);

		return document;

	}

}
