package pvt.disney.dti.gateway.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;

/**
 * The purpose of this class is to contain all of the measures needed to ensure compliance with the Payment Card Industry standards. Please do not change anything in this class without filling out the internal change log and documenting the
 * running the test scenarios. This class may be reviewed by IT Security and Compliance. <BR>
 * 
 * Change Log <BR>
 * 1. Version 1.0 - Todd Lewis - Initial Development - 6/17/2009 <BR>
 * 2. Version 1.1 - Todd Lewis - Clean-up and documentation - 7/2/2009 <BR>
 * 3. Version 1.2 - Todd Lewis - Import clean-up - 8/4/2009 <BR>
 * 4. Version 1.3 - Todd Lewis - Handle situations with null or empty or short CC strings. <BR>
 * 5. Version 1.4 - Todd Lewis - Handle situations with SHA-1 hashing - 12/29/2016 <BR>
 * 
 * @author lewit019
 * @version 1.3
 * 
 */
public class PCIControl {

	/** The style of overwrite required for compliance. */
	public static enum OverwriteStyle {
		CREDITCARD,
		GIFTCARD,
		FULL
	}

	// ArrayList that holds all of the fields that need to be obliterated for PCI
	// purposes.
	private static ArrayList<PCIControl> tagList = new ArrayList<PCIControl>();

	// XML Fields that require PCI remediation...
	/** CCNbr is obliterated by overwriting the first 12 characters. */
	public final static PCIControl CCNBR = new PCIControl("CCNbr",
			OverwriteStyle.CREDITCARD);

	/** CCVV is obliterated. */
	public final static PCIControl CCVV = new PCIControl("CCVV",
			OverwriteStyle.FULL);

	/** GCNbr is partially obliterated by overwriting the last 4 characters. */
	public final static PCIControl GCNBR = new PCIControl("GCNbr",
			OverwriteStyle.GIFTCARD);

	/** GCNumber is partially obliterated by overwriting the last 4 characters. */
	public final static PCIControl GCNUMBER = new PCIControl("GCNumber",
			OverwriteStyle.GIFTCARD);

	/**
	 * Endorsement is fully obliterated. Full because Omni puts gift cards and credit cards in the same field.
	 */
	public final static PCIControl ENDORSEMENT = new PCIControl("Endorsement",
			OverwriteStyle.FULL);

	/** CCTrack1 is fully obliterated. */
	public final static PCIControl CCTRACK1 = new PCIControl("CCTrack1",
			OverwriteStyle.FULL);

	/** CCTrack2 is fully obliterated. */
	public final static PCIControl CCTRACK2 = new PCIControl("CCTrack2",
			OverwriteStyle.FULL);

	/** GCTrack1 is fully obliterated. */
	public final static PCIControl GCTRACK1 = new PCIControl("GCTrack1",
			OverwriteStyle.FULL);

	/** GCTrack2 is fully obliterated. */
	public final static PCIControl GCTRACK2 = new PCIControl("GCTrack2",
			OverwriteStyle.FULL);

	/** CVN is fully obliterated. */
	public final static PCIControl CVN = new PCIControl("CVN",
			OverwriteStyle.FULL);

	/** Track1 is fully obliterated. */
	public final static PCIControl TRACK1 = new PCIControl("Track1",
			OverwriteStyle.FULL);

	/** Track2 is fully obliterated. */
	public final static PCIControl TRACK2 = new PCIControl("Track2",
			OverwriteStyle.FULL);

	/** CVV is fully obliterated. */
	public final static PCIControl CVV = new PCIControl("CVV",
			OverwriteStyle.FULL);

	/**
	 * CardNumber is fully obliterated. Full because Omni puts gift cards and credit cards in the same field.
	 */
	public final static PCIControl CARDNUMBER = new PCIControl("CardNumber",
			OverwriteStyle.FULL);

	/**
	 * CCNumber is obliterated by overwriting the first 12 characters.
	 */
	public final static PCIControl CCNUMBER = new PCIControl("CCNumber",
			OverwriteStyle.CREDITCARD);

	/** CAVVValue is fully obliterated. */
	public final static PCIControl CAVVVALUE = new PCIControl("CAVVValue",
			OverwriteStyle.FULL);

	/** CCSHA-1 is fully obliterated to prevent any possible discovery of the PAN. (as of 2.16.3, JTL) */
  public final static PCIControl CCSHA1 = new PCIControl("CCSHA-1", OverwriteStyle.FULL);

  /** SHA-1 is fully obliterated to prevent any possible discovery of the PAN. (as of 2.16.3, JTL) */
  public final static PCIControl SHA1 = new PCIControl("SHA-1", OverwriteStyle.FULL);
	
	/** Overwritten digits are replaced with "Z" */
	public final static String REPLACECHAR = "Z";

	/**
	 * Constant representing the minimum size considered for conditional masking (12)
	 */
	private final static int MIN_CONDITIONAL_SIZE = 12;

	// ErrorCode Object Attributes
	private String fieldName;

	private PCIControl.OverwriteStyle overwriteStyle;

	/**
	 * Constructor for this PCIControl
	 */
	public PCIControl(String fieldNameIn,
			PCIControl.OverwriteStyle overwriteStyleIn) {

		fieldName = fieldNameIn;
		overwriteStyle = overwriteStyleIn;
		tagList.add(this);

		return;
	}

	/**
	 * PCI Control: Do not modify unless IT Security and Compliance has been advised of the impact of any changes. This method overwrites the PCI data in an XML as specified by the constants in this class.
	 * 
	 * @param inXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String overwritePciDataInXML(String inXml) throws DTIException {

		String outXml = null;

		Document document = null;

		try {
			document = DocumentHelper.parseText(inXml);
		}
		catch (DocumentException de) {
			throw new DTIException(PCIControl.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"Unable to parse XML: " + de.toString());
		}

		String xmlEncoding = document.getXMLEncoding();
		boolean hadEncoding = false;
		if (xmlEncoding != null) {
			hadEncoding = true;
		}

		for /* each */(PCIControl aControl : /* in */tagList) {

			// 1. Select the tag(s)
			List<Node> nodeList = (List<Node>) document
					.selectNodes("//" + aControl.getFieldName());
			if ((nodeList == null) || (nodeList.size() == 0)) {
			  continue;
			}

			// 2. Extract the text value, modify it
			for /* each */(Node aNode : /* in */nodeList) {

				StringBuffer dirtyStringBuf = new StringBuffer(aNode.getText());
				StringBuffer cleanStringBuf;
				String overwriteString;
				int length = dirtyStringBuf.length();

				// Don't replace an empty field
				if (length == 0) {
					continue;
				}

				// Replace all in an under-sized field.
				if (length <= MIN_CONDITIONAL_SIZE) {

					overwriteString = createOverwriteString(length);
					cleanStringBuf = new StringBuffer(overwriteString);

				}
				else if (aControl.getOverwriteStyle() == OverwriteStyle.CREDITCARD) {

					overwriteString = createOverwriteString(length - 4);
					cleanStringBuf = dirtyStringBuf.replace(0, length - 4,
							overwriteString);

				}
				else if (aControl.getOverwriteStyle() == OverwriteStyle.GIFTCARD) {

					overwriteString = createOverwriteString(4);
					cleanStringBuf = dirtyStringBuf.replace(length - 4, length,
							overwriteString);

				}
				else {

					overwriteString = createOverwriteString(length);
					cleanStringBuf = new StringBuffer(overwriteString);

				}

				aNode.setText(cleanStringBuf.toString());

			} // node list

		} // PCI control list

		if (!hadEncoding) {

			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			outformat.setOmitEncoding(true);

			try {
				XMLWriter writer = new XMLWriter(outStream, outformat);
				writer.write(document);
				writer.flush();
				outXml = outStream.toString();
			}
			catch (Exception e) {
				throw new DTIException(
						PCIControl.class,
						DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"PCIControl unable to recreate XML without encoding: " + e
								.toString());
			}

		}
		else {
			outXml = document.asXML();
		}

		return outXml;
	}

	/**
	 * Generates an overwrite string of a certain length.
	 * 
	 * @param length
	 *            the length of overwrite string required.
	 * @return the overwrite string.
	 */
	private static String createOverwriteString(int length) {

		StringBuffer overwriteString = new StringBuffer();

		for (int i = 0; i < length; i++) {
			overwriteString.append(REPLACECHAR);
		}

		return overwriteString.toString();

	}

	/**
	 * @return the field name of this PCI control.
	 */
	private String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the overwrite style of this PCI control.
	 */
	private PCIControl.OverwriteStyle getOverwriteStyle() {
		return overwriteStyle;
	}

}
