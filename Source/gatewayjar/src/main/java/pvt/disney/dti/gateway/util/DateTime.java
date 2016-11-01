package pvt.disney.dti.gateway.util;

import java.util.Calendar;

/**
 * Class for managing date and time conversions in the application.
 * 
 * @author unknown
 * @version %version: %
 * 
 */
public class DateTime {

	private Calendar calendar = null;
	private String year = null;
	private String month = null;
	private String day = null;
	private String hour = null;
	private String minute = null;
	private String second = null;
	private String milli = null;
	private int millisecond = 0;

	/**
	 * Constructor for DTIApp
	 */
	public DateTime() {
		super();
		calendar = Calendar.getInstance();
	}

	/**
	 * Gets the day
	 * 
	 * @return Returns a String
	 */
	public String getDay() {
		if (day == null) day = (calendar.get(Calendar.DAY_OF_MONTH) < 10) ? "0" + calendar
				.get(Calendar.DAY_OF_MONTH) : DTIFormatter.toString(calendar
				.get(Calendar.DAY_OF_MONTH));
		return day;
	}

	/**
	 * Gets the hour
	 * 
	 * @return Returns a String
	 */
	public String getHour() {
		if (hour == null) hour = (calendar.get(Calendar.HOUR_OF_DAY) < 10) ? "0" + calendar
				.get(Calendar.HOUR_OF_DAY) : DTIFormatter.toString(calendar
				.get(Calendar.HOUR_OF_DAY));
		return hour;
	}

	/**
	 * Gets the millisecond
	 * 
	 * @return Returns a int
	 */
	private int getMillisecond() {
		millisecond = calendar.get(Calendar.MILLISECOND);
		return millisecond;
	}

	/**
	 * Gets the milli
	 * 
	 * @return Returns a String
	 */
	public String getMilli() {
		if (milli == null) {
			int millisecond = getMillisecond();
			if (millisecond < 10) {
				milli = "00" + millisecond;
			}
			else if (millisecond < 100) {
				milli = "0" + millisecond;
			}
			else {
				milli = DTIFormatter.toString(millisecond);
			}
		}
		return milli;
	}

	/**
	 * Gets the minute
	 * 
	 * @return Returns a String
	 */
	public String getMinute() {
		if (minute == null) minute = (calendar.get(Calendar.MINUTE) < 10) ? "0" + calendar
				.get(Calendar.MINUTE) : DTIFormatter.toString(calendar
				.get(Calendar.MINUTE));
		return minute;
	}

	/**
	 * Gets the month
	 * 
	 * @return Returns a String
	 */
	public String getMonth() {
		if (month == null) month = ((calendar.get(Calendar.MONTH) + 1) < 10) ? "0" + (calendar
				.get(Calendar.MONTH) + 1) : DTIFormatter.toString((calendar
				.get(Calendar.MONTH) + 1));
		return month;
	}

	/**
	 * Gets the second
	 * 
	 * @return Returns a String
	 */
	public String getSecond() {
		if (second == null) second = (calendar.get(Calendar.SECOND) < 10) ? "0" + calendar
				.get(Calendar.SECOND) : DTIFormatter.toString(calendar
				.get(Calendar.SECOND));
		return second;
	}

	/**
	 * Gets the year
	 * 
	 * @return Returns a String
	 */
	public String getYear() {
		if (year == null) year = DTIFormatter.toString(calendar
				.get(Calendar.YEAR));
		return year;
	}

}
