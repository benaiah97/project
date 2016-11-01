package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.dom4j.Element;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.util.UtilityXML;

/**
 * This class is responsible for marshalling and unmarshalling the DLR header XML.
 * 
 * @author lewit019
 * 
 */
public class GWHeaderXML {

	/**
	 * Process the request from DTI to the provider.
	 * 
	 * @param gwEnvTO
	 *            The gateway header transfer object.
	 * @param envelopeElement
	 *            The envelope element which requires a header to be added.
	 * @throws DTIException
	 *             should any marshalling error occur.
	 */
	public static void addHeaderElement(GWHeaderTO gwHdrTO,
			Element envelopeElement) throws DTIException {

		// Header
		Element headerElement = envelopeElement.addElement("Header");

		// SourceID
		if (gwHdrTO.getSourceID() == null) {
			throw new DTIException(GWHeaderXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"GWHeaderTO doesn't have a sourceID.  Can't render XML.");
		}
		else {
			headerElement.addElement("SourceID").addText(gwHdrTO.getSourceID());
		}

		// MessageID
		if (gwHdrTO.getMessageID() == null) {
			throw new DTIException(GWHeaderXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"GWHeaderTO doesn't have a messageID.  Can't render XML.");
		}
		else {
			headerElement.addElement("MessageID").addText(
					gwHdrTO.getMessageID().toString());
		}

		// TimeStamp
		if (gwHdrTO.getTimeStamp() == null) {
			throw new DTIException(GWHeaderXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"GWHeaderTO doesn't have a timeStamp.  Can't render XML.");
		}
		else {
			GregorianCalendar gCal = gwHdrTO.getTimeStamp();
			String timeStampString = UtilityXML.getEGalaxyDateFromGCal(gCal);
			headerElement.addElement("TimeStamp").addText(timeStampString);

		}

		// MessageType
		if (gwHdrTO.getMessageType() == null) {
			throw new DTIException(GWHeaderXML.class,
					DTIErrorCode.INVALID_MSG_CONTENT,
					"GWHeaderTO doesn't have a messageType.  Can't render XML.");
		}
		else {
			headerElement.addElement("MessageType").addText(
					gwHdrTO.getMessageType());
		}

		// EchoData (optional)
		if (gwHdrTO.getEchoData() != null) {
			headerElement.addElement("EchoData").addText(gwHdrTO.getEchoData());
		}

		return;
	}

	/**
	 * Process the response from the provider, back to DTI
	 * 
	 * @param header
	 *            The header element from the response XML.
	 * @return the gateway header transfer object
	 * @throws DTIException
	 *             should any unmarshalling errors occur.
	 */
	@SuppressWarnings("unchecked")
	public static GWHeaderTO getTO(Element header) throws DTIException {

		GWHeaderTO gwHdrTO = new GWHeaderTO();

		Iterator<org.dom4j.Element> i = header.elementIterator();

		for (; i.hasNext();) {
			Element element = i.next();

			if (element.getName().compareTo("SourceID") == 0) {
				gwHdrTO.setSourceID(element.getText());
			}

			if (element.getName().compareTo("MessageID") == 0) {
				String inText = element.getText();
				BigInteger inMsgID = new BigInteger(inText);
				gwHdrTO.setMessageID(inMsgID);
			}

			if (element.getName().compareTo("MessageType") == 0) {
				gwHdrTO.setMessageType(element.getText());
			}

			if (element.getName().compareTo("TimeStamp") == 0) {

				String timeStampString = element.getText();
				GregorianCalendar gCal = UtilityXML
						.getGCalFromEGalaxyDate(timeStampString);

				if (gCal == null) {
					throw new DTIException(
							GWHeaderXML.class,
							DTIErrorCode.TP_INTERFACE_FAILURE,
							"Ticket provider returned unparsable TimeStamp: " + timeStampString);
				}

				gwHdrTO.setTimeStamp(gCal);
			}

		}

		// Validate that the provider return all expected fields.
		if (gwHdrTO.getSourceID() == null) throw new DTIException(
				GWHeaderXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,SourceID element.");
		if (gwHdrTO.getMessageID() == null) throw new DTIException(
				GWHeaderXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,MessageID element.");
		if (gwHdrTO.getMessageType() == null) throw new DTIException(
				GWHeaderXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,MessageType element.");
		if (gwHdrTO.getTimeStamp() == null) throw new DTIException(
				GWHeaderXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a Header,TimeStamp element.");

		return gwHdrTO;
	}

}
