package pvt.disney.dti.gateway.rules.race.vo;

import java.util.Calendar;

/**
 * The Class Step1VO.
 * A Value Object that holds results of algorithm calculations for Step One
 * 
 * @author moons012
 */
public class Step1VO extends ParentStepVO {

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
	public Step1VO() {
		super();
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
	public void setResultMultipliedDateTimeArray(int[][] multipliedDateTimeArray) {
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step1VO: { ");
		buff.append(System.lineSeparator());
		buff.append("calendar=");
		buff.append(calendar.getTime());
		buff.append(System.lineSeparator());
		buff.append("dateArray:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(dateArray));
		buff.append(System.lineSeparator());
		buff.append("timeArray:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(timeArray));
		buff.append(System.lineSeparator());
		buff.append("resultMultipliedDateTimeArray:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(resultMultipliedDateTimeArray));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
