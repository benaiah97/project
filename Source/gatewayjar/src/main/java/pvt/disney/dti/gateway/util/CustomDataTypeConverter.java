package pvt.disney.dti.gateway.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * Title: CustomDataTypeConverter
 * </p>
 * <p>
 * Description: The is a helper Utility used by the jaxb custom binding. The bindings addresses the shortcoming of the DTI schemas (not accepting valid date/time formats as defined by the schema.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: WDPRO
 * </p>
 * 
 * @author SMoon
 * @version 1.0
 */
public class CustomDataTypeConverter {

	/**
	 * Default Constructor.
	 */
	public CustomDataTypeConverter() {
	}

	/**
	 * Custom method to allow jaxb classes to omit GMT offset from dates.
	 * 
	 * @param cal
	 *            Calendar
	 * @return String <TktDate>2005-10-11</TktDate> <TransmitDate>2006-05-17</TransmitDate> <TransmitTime>19:13:55.99</TransmitTime>
	 * 
	 */
	public static String printCalToDTIDateFormat(Calendar cal) {
		// Note: format: YYYY-MM-DD
		// Note: month must be two digit
		String dtiDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		dtiDate = dateFormat.format(CustomDataTypeConverter
				.convertCalendarToDate(cal));

		return dtiDate;
	}

	/**
	 * Custom method to allow jaxb classes to omit GMT offset from dates.
	 * 
	 * @param cal
	 *            Calendar
	 * @return String 10/11/2005
	 * 
	 */
	public static String printCalToStandardDateFormat(Calendar cal) {
		// Note: format: MM/dd/yyyy
		// Note: month must be two digit
		String dtiDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		dtiDate = dateFormat.format(CustomDataTypeConverter
				.convertCalendarToDate(cal));

		return dtiDate;
	}

	/**
	 * Custom method to allow jaxb classes to omit GMT offset from times.
	 * 
	 * @param cal
	 *            Calendar
	 * @return String
	 */
	public static String printCalToDTITimeFormat(Calendar cal) {
		// Note: format HH:MM:SS.ss
		// we'll just round it, ss as 00
		// cal.get(Calendar.MILLISECOND)
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if (minute.length() == 1) {
			minute = "0" + minute;
		}

		if (hour.length() == 1) {
			hour = "0" + hour;
		}

		if (second.length() == 1) {
			second = "0" + second;
		}

		String dtiTime = hour + ":" + minute + ":" + second + "." + "00";
		return dtiTime;
	}

	/**
	 * Custom method to allow jaxb classes to omit GMT offset from dates.
	 * 
	 * @param cal
	 *            Calendar
	 * @return String <TktDate>05-10-11</TktDate> <TransmitDate>06-05-17</TransmitDate>
	 * 
	 */
	public static String printCalToNexusDateFormat(Calendar cal) {
		// Note: format: YYYY-MM-DD
		// Note: month must be two digit
		String dtiDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

		dtiDate = dateFormat.format(CustomDataTypeConverter
				.convertCalendarToDate(cal));

		return dtiDate;
	}

	/**
	 * TO DO: This method needs to do something to provide the conversion.
	 * 
	 * @param cal
	 * @return
	 */
	public static Date convertCalendarToDate(Calendar pCalendar,
			boolean pUseYMDOnly) {
		if (pCalendar == null) {
			return null;
		}
		Calendar aCal;
		if (pUseYMDOnly) {
			int date = pCalendar.get(Calendar.DATE);
			int month = pCalendar.get(Calendar.MONTH);
			int year = pCalendar.get(Calendar.YEAR);
			aCal = new GregorianCalendar(year, month, date);
		}
		else {
			aCal = pCalendar;
		}
		return aCal.getTime();

	}

	public static Date convertCalendarToDate(Calendar pCalendar) {
		return convertCalendarToDate(pCalendar, true);
	}

	/**
	 * parseISODate method parses a date string into a Date object with the format dd-MM-yy if this is not the format which is being passed in then we will use yyyy-MM-dd
	 * 
	 * @param dateStr
	 * @return Date
	 */
	public static Calendar parseYYMMDDDate(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd");
		Calendar cal = Calendar.getInstance();
		ParsePosition pos = new ParsePosition(0);
		Date newDate = null;

		try {
			newDate = formatter.parse(dateStr.trim(), pos);

			// Validate new date
			if (newDate == null) {
				// Date was null, so try again with a different format pattern
				formatter = new SimpleDateFormat("yyyy-MM-dd");
				newDate = formatter.parse(dateStr.trim(), pos);
			}

			/*
			 * Don't create an exception if the date is null. This function is called even if the field is not present. Let the JAXB object reject the XML if the field is required. if (newDate == null) { Exception e = new
			 * Exception("XML Parse parseISODate Null Exception: " + dateStr); e.fillInStackTrace(); // Log exception but don't throw it evl.sendException(EventType.DEBUG, ErrorCode.XML_PARSE_EXCEPTION, e, thisClass); }
			 */
		}
		catch (Throwable t) {
		}

		cal.setTime(newDate);
		return cal;
	}

	/**
	 * 
	 * @param dateStr
	 * @return
	 */
	public static GregorianCalendar parseYYYYMMDDDate(String dateStr) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date newDate = null;
		Calendar cal = Calendar.getInstance();
		;
		newDate = formatter.parse(dateStr.trim(), pos);
		cal.setTime(newDate);

		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTime(newDate);

		return gCal;
	}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param dateStr
	 * @return
	 */
	public static GregorianCalendar parseDateTimeStamp(String dateStr) {

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		Date newDate = null;
		try {
			newDate = formatter.parse(dateStr);
		}
		catch (ParseException pe) {
			return null;
		}

		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTime(newDate);

		return gCal;
	}

	/**
	 * Determines if a string is numeric.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
