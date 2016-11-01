package pvt.disney.dti.gateway.service.dtixml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This class contains utility methods that are used to convert XML values back and forth between regular data types.
 * 
 * @author lewit019
 * 
 */
public class UtilXML {

	/**
	 * Converts a Gregorian Calendar date into an XML Gregorian Calendar.
	 * 
	 * @param gCal
	 *            the Gregorian Calendar to be converted.
	 * @return the XML Gregorian Calendar value.
	 * @throws JAXBException
	 *             should there be a problem instantiating Datatype Factory.
	 */
	public static XMLGregorianCalendar convertToXML(GregorianCalendar gCal) throws JAXBException {

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

		return xCalDate;
	}

	/**
	 * Converts an XML Gregorian Calendar date into a Gregorian Calendar.
	 * 
	 * @param xCal
	 *            the XML Gregorian Calendar value.
	 * @return the Gregorian Calendar
	 */
	public static GregorianCalendar convertFromXML(XMLGregorianCalendar xCal) {

		return new GregorianCalendar(xCal.getEonAndYear().intValue(),
				xCal.getMonth() - 1, xCal.getDay());
	}

	/**
	 * Converts an XMLGregorianCalendar date and time string to a GregorianCalendar.
	 * 
	 * @param tXCalDate
	 *            XMLGregorianCalendar
	 * @param timeString
	 *            String with time formatted HH:mm:ss.S to .SSS
	 * @return GregorianCalendar
	 * @throws JAXBException
	 *             if the time value cannot be parsed.
	 */
	public static GregorianCalendar formatXMLDateTime(
			XMLGregorianCalendar tXCalDate, String timeString) throws JAXBException {

		// JTL Deployment Hack - go back and make pretty...
		SimpleDateFormat sdf = null;

		if (timeString.indexOf(".") == -1) {
			sdf = new SimpleDateFormat("HH:mm:ss");
		}
		else {
			sdf = new SimpleDateFormat("HH:mm:ss.S");
		}

		Date tempDate = null;

		try {
			tempDate = sdf.parse(timeString);
		}
		catch (ParseException pe) {
			throw new JAXBException(
					"Unable to parse Transmit or CommandTime as passed by seller.");
		}

		GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
				.getInstance();
		gCal.setTime(tempDate);

		GregorianCalendar tempCalendar = new GregorianCalendar(tXCalDate
				.getEonAndYear().intValue(), tXCalDate.getMonth() - 1,
				tXCalDate.getDay(), gCal.get(Calendar.HOUR_OF_DAY),
				gCal.get(Calendar.MINUTE), gCal.get(Calendar.SECOND));

		tempCalendar.set(Calendar.MILLISECOND, gCal.get(Calendar.MILLISECOND));

		return tempCalendar;
	}

}
