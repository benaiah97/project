package pvt.disney.dti.gateway.service.dtixml;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.response.xsd.PayloadHeader.PayloadError;

/**
 * This class is responsible for transformations between the DTI TiXML and the internal objects used by the DTI Application. This class manages the PayloadHeader clause.
 * 
 * @author lewit019
 * 
 */
public abstract class PayloadHeaderXML {

	/** The length of a response payload ID (9). */
	private static final int RESP_PAYLOAD_ID_LENGTH = 9;

	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param payloadHeader
	 *            The JAXB version of the object.
	 * @return The DTI Application version of the Payload Header.
	 * @throws JAXBException
	 *             should any parsing errors occur.
	 */
	public static PayloadHeaderTO getTO(
			pvt.disney.dti.gateway.request.xsd.PayloadHeader payloadHeader) throws JAXBException {

		PayloadHeaderTO payloadHeaderTO = new PayloadHeaderTO();

		payloadHeaderTO.setPayloadID(payloadHeader.getPayloadID());
		payloadHeaderTO.setTarget(payloadHeader.getTarget());
		payloadHeaderTO.setVersion(payloadHeader.getVersion());

		// TransmitDateTime
		XMLGregorianCalendar tXCalDate = payloadHeader.getTransmitDate();
		String timeString = payloadHeader.getTransmitTime();
		GregorianCalendar tempCalendar = UtilXML.formatXMLDateTime(tXCalDate,
				timeString);
		payloadHeaderTO.setTransmitDateTime(tempCalendar);

		// Version and other fields
		payloadHeaderTO.setVersion(payloadHeader.getVersion());
		payloadHeaderTO.setCommMethod(payloadHeader.getComm().getMethod());
		payloadHeaderTO.setCommProtocol(payloadHeader.getComm().getProtocol());

		TktSellerTO seller = new TktSellerTO();
		seller.setTsLocation(payloadHeader.getTktSeller().getTSLocation());
		seller.setTsMac(payloadHeader.getTktSeller().getTSMAC());
		seller.setTsSecurity(payloadHeader.getTktSeller().getTSSecurity());
		seller.setTsSystem(payloadHeader.getTktSeller().getTSSystem());
		//check for hkdl only - only set the sellerid (which is optional) if it is present in the request
		if (payloadHeader.getTktSeller().getSellerId()!=null && payloadHeader.getTktSeller().getSellerId().length() > 0) {
			seller.setSellerId(payloadHeader.getTktSeller().getSellerId());
		}
		payloadHeaderTO.setTktSeller(seller);

		payloadHeaderTO.setCommandCount(payloadHeader.getCommandCount());

		return payloadHeaderTO;

	}

	/**
	 * Returns the JAXB version of the payload header when given the DTI Application version.
	 * 
	 * @param payloadHeaderTO
	 *            The DTI Application Version of the payload header.
	 * @return The JAXB version of the payload header.
	 * @throws JAXBException
	 *             should any parsing errors occur.
	 */
	public static pvt.disney.dti.gateway.response.xsd.PayloadHeader getJaxb(
			PayloadHeaderTO payloadHeaderTO, DTIErrorTO errorTO) throws JAXBException {

		pvt.disney.dti.gateway.response.xsd.PayloadHeader payloadHeader = new pvt.disney.dti.gateway.response.xsd.PayloadHeader();

		/**
		 * Pad response payload header to nine, right justified, left spaces to match old gateway. JTL 09/29/2009
		 */
		String unpadded = payloadHeaderTO.getPayloadID();
		int padSpaces = RESP_PAYLOAD_ID_LENGTH - unpadded.length();
		StringBuffer padString = new StringBuffer("");
		for (int i = 0; i < padSpaces; i++) {
			padString.append(" ");
		}
		payloadHeader.setPayloadID(padString + unpadded);

		payloadHeader.setTSPayloadID(payloadHeaderTO.getTsPayloadID());
		payloadHeader.setTarget(payloadHeaderTO.getTarget());
		payloadHeader.setVersion(payloadHeaderTO.getVersion());

		pvt.disney.dti.gateway.response.xsd.PayloadHeader.Comm comm = new pvt.disney.dti.gateway.response.xsd.PayloadHeader.Comm();
		comm.setProtocol(payloadHeaderTO.getCommProtocol());
		comm.setMethod(payloadHeaderTO.getCommMethod());
		payloadHeader.setComm(comm);

		GregorianCalendar gCal = payloadHeaderTO.getTransmitDateTime();

		DatatypeFactory aFactory = null;
		try {
			aFactory = DatatypeFactory.newInstance();
		}
		catch (DatatypeConfigurationException dce) {
			throw new JAXBException("Cannot instantiate datatype factory.");
		}

		XMLGregorianCalendar xCalDate = aFactory.newXMLGregorianCalendarDate(
				gCal.get(GregorianCalendar.YEAR),
				gCal.get(GregorianCalendar.MONTH) + 1,
				gCal.get(GregorianCalendar.DAY_OF_MONTH),
				DatatypeConstants.FIELD_UNDEFINED);

		payloadHeader.setTransmitDate(xCalDate);

		// Because of ticket seller limitations, the time format must be HH:mm:ss.SS, but .SSS is
		// always generated. The string is therefore clipped at the end to make it acceptible.
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		String tempCalendarString = sdf.format(gCal.getTime());
		tempCalendarString = tempCalendarString.substring(0,
				(tempCalendarString.length() - 1));

		payloadHeader.setTransmitTime(tempCalendarString);

		payloadHeader.setTktBroker(payloadHeaderTO.getTktBroker());
		payloadHeader.setCommandCount(payloadHeaderTO.getCommandCount());

		if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.PAYLOAD)) {
			PayloadError error = new PayloadError();
			error.setHdrErrorCode(errorTO.getErrorCode());
			error.setHdrErrorType(errorTO.getErrorType());
			error.setHdrErrorClass(errorTO.getErrorClass());
			error.setHdrErrorText(errorTO.getErrorText());
			payloadHeader.setPayloadError(error);
		}

		return payloadHeader;
	}

}
