package pvt.disney.dti.gateway.rules.race.vo;

import java.util.Calendar;

/**
 * The Class StepOneVO.
 * A Value Object that holds results of algorithm calculations for Step One
 * 
 * @author moons012
 * @since 2.16.3
 */
public class StepOneVO {

	/** The date array. */
	private int[][] dateArray;
	
	/** The time array. */
	private int[][] timeArray;
	
	/** The result - multiplied date time array. */
	private int[][] resultMultipliedDateTimeArray;
	
	/** The calendar. */
	private Calendar calendar;
	
	/**
	 * Instantiates a new step one value object.
	 */
	public StepOneVO() {
	}

	/**
	 * Gets the date array.
	 *
	 * @return the date array
	 */
	public int[][] getDateArray() {
		return dateArray;
	}

	/**
	 * Sets the date array.
	 *
	 * @param dateArray the new date array
	 */
	public void setDateArray(int[][] dateArray) {
		this.dateArray = dateArray;
	}

	/**
	 * Gets the time array.
	 *
	 * @return the time array
	 */
	public int[][] getTimeArray() {
		return timeArray;
	}

	/**
	 * Sets the time array.
	 *
	 * @param timeArray the new time array
	 */
	public void setTimeArray(int[][] timeArray) {
		this.timeArray = timeArray;
	}

	/**
	 * Gets the multiplied date time array.
	 *
	 * @return the multiplied date time array
	 */
	public int[][] getResultMultipliedDateTimeArray() {
		return resultMultipliedDateTimeArray;
	}

	/**
	 * Sets the multiplied date time array.
	 *
	 * @param multipliedDateTimeArray the new multiplied date time array
	 */
	public void setMultipliedDateTimeArray(int[][] multipliedDateTimeArray) {
		this.resultMultipliedDateTimeArray = multipliedDateTimeArray;
	}

	/**
	 * Gets the calendar.
	 *
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	/**
	 * Sets the calendar.
	 *
	 * @param calendar the new calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

}
