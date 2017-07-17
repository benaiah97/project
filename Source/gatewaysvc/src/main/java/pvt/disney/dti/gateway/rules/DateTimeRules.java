package pvt.disney.dti.gateway.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.StringTokenizer;

/**
 * This class contains the date and time based rules used in DTI.
 * 
 * @author lewit019
 * 
 */
public class DateTimeRules {

	/**
	 * Gets the current GMT Time.
	 * 
	 * @return GMT date set to "now".
	 */
	private static Date getGMTNow() {

		// Starting in 2007, most of the United States and Canada observe DST from
		// the second Sunday in March to the first Sunday in November, almost
		// two-thirds of the year.
		Date todayLocal = new Date();

		SimpleTimeZone stz = new SimpleTimeZone(18000000, "America/New_York",
				Calendar.MARCH, 8, -Calendar.SUNDAY, 7200000,
				Calendar.NOVEMBER, 1, Calendar.SUNDAY, 7200000, 3600000);

		Date todayGMT;
		if (stz.inDaylightTime(todayLocal)) {
			todayGMT = new Date(todayLocal.getTime() + 14400000L); // + 4 hours
		}
		else {
			todayGMT = new Date(todayLocal.getTime() + 18000000L); // + 5 hours
		}

		return todayGMT;
	}

 /**
   * Gets the current Pacific Time.
   * @return GMT date set to "now".
   */
  public static Date getPTNow() {

    // Starting in 2007, most of the United States and Canada observe DST from
    // the second Sunday in March to the first Sunday in November, almost
    // two-thirds of the year.
    Date todayLocal = new Date();

    Date todayPT = new Date(todayLocal.getTime() - 10800000L); // -3 hours

    return todayPT;
  }
	
	/**
	 * Determines if now is within the GMT time.
	 * 
	 * @param startTime
	 *            Start time
	 * @param endTime
	 *            End time
	 * @return True if so, false if not.
	 */
	public static boolean isNowWithinGMTTime(String startTime, String endTime) {

		if ((startTime == null) || (endTime == null)) {
			return true;
		}

		// Get the GMT date/time
		Date todayGMT = DateTimeRules.getGMTNow();

		// Validate entity time
		StringTokenizer strTknStart = new StringTokenizer(startTime, ":");
		StringTokenizer strTknEnd = new StringTokenizer(endTime, ":");

		String startTimeHour = strTknStart.nextToken();
		String startTimeMinute = strTknStart.nextToken();
		int startTimeInt = Integer.parseInt(startTimeHour + startTimeMinute);

		String endTimeHour = strTknEnd.nextToken();
		String endTimeMinute = strTknEnd.nextToken();
		int endTimeInt = Integer.parseInt(endTimeHour + endTimeMinute);

		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		String curTimeGMT = sdf.format(todayGMT);
		int curTimeInt = Integer.parseInt(curTimeGMT);

		if (startTimeInt < endTimeInt) {
			if ((curTimeInt > endTimeInt) || (curTimeInt < startTimeInt)) {
			  return false;
			}	else { 
			  return true;
			}
		}
		else {
			if ((endTimeInt < curTimeInt) && (startTimeInt > curTimeInt)) { 
			  return false;
			} else { 
			  return true;
			}
		}

	}

	/**
	 * Determines if now is within the date.
	 * 
	 * @param startTime
	 *            Start time as a Date
	 * @param endTime
	 *            End time as a Date
	 * @return True if so, false if not.
	 */
	public static boolean isNowWithinDate(Date startDate, Date endDate) {

		Date todayLocal = new Date();

		if (startDate == null) {
			startDate = todayLocal;
		}

		if (endDate == null) {
			if (todayLocal.compareTo(startDate) < 0) return false;
			else return true;
		}

		// Validate date
		if ((todayLocal.compareTo(startDate) < 0) || (todayLocal
				.compareTo(endDate) > 0)) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Determines if now is within the date.
	 * 
	 * @param startDateString
	 *            the start date string
	 * @param endDateString
	 *            the end date string
	 * 
	 * @return true if so, false if not
	 */
	public static boolean isNowWithinDate(String startDateString,
			String endDateString) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate;
		Date endDate;

		try {
			startDate = sdf.parse(startDateString);
			endDate = sdf.parse(endDateString);

			return isNowWithinDate(startDate, endDate);

		}
		catch (ParseException pe) {
			return false;
		}
	}

	/**
	 * Gets the current GMT Date.
	 * 
	 * @return A GregorianDate version of the GMT Date Time.
	 */
	public static GregorianCalendar getGMTDateNow() {

		Date gmtDateTime = DateTimeRules.getGMTNow();
		GregorianCalendar gCal = new GregorianCalendar();
		gCal.setTime(gmtDateTime);

		return gCal;
	}
	
  /**
   * Gets the current GMT Date.
   * 
   * @return A GregorianDate version of the GMT Date Time.
   */
  public static GregorianCalendar getPTDateNow() {

    Date ptDateTime = DateTimeRules.getPTNow();
    GregorianCalendar gCal = new GregorianCalendar();
    gCal.setTime(ptDateTime);

    return gCal;
  }
	
}
