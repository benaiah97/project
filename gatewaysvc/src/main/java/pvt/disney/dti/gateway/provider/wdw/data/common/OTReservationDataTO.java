package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class is used as a transfer object representing Omni Ticket Reservation Data.
 * 
 * @author lewit019
 * 
 */
public class OTReservationDataTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** Constant value representing Omni Ticket true string value ("1"). */
	private final static String TRUESTRINGVALUE = "1";

	/** Is the reservation printed? */
	private Boolean printed;

	/** Is the reservation validated? */
	private Boolean validated;

	/** Is the reservation encoded? */
	private Boolean encoded;

	/** Has the reservation been paid? */
	private Boolean paid;

	/** The deposit amount. */
	private BigDecimal depositAmount;

	/** The sales type. */
	private Integer salesType;

	/** The reservation pick-up date. */
	private GregorianCalendar resPickupDate;

	/** The reservation pick-up area. */
	private Integer resPickupArea;

	/** The reservation pick-up type. */
	private Integer resPickupType;

	/** The reservation status. */
	private String resStatus;

	/**
	 * @return the printed
	 */
	public Boolean getPrinted() {
		return printed;
	}

	/**
	 * @return the resPickupArea
	 */
	public Integer getResPickupArea() {
		return resPickupArea;
	}

	/**
	 * @return the resPickupDate
	 */
	public GregorianCalendar getResPickupDate() {
		return resPickupDate;
	}

	/**
	 * @return the resPickupType
	 */
	public Integer getResPickupType() {
		return resPickupType;
	}

	/**
	 * @return the salesType
	 */
	public Integer getSalesType() {
		return salesType;
	}

	/**
	 * @return the validated
	 */
	public Boolean getValidated() {
		return validated;
	}

	/**
	 * @param printed
	 *            the printed to set
	 */
	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}

	/**
	 * Sets the printed boolean based on Nexus string values.
	 * 
	 * @param flag
	 *            nexus flag value.
	 */
	public void setPrinted(String flag) {
		if (flag.compareTo(TRUESTRINGVALUE) == 0) printed = true;
		else printed = false;
	}

	/**
	 * @param resPickupArea
	 *            the resPickupArea to set
	 */
	public void setResPickupArea(Integer resPickupArea) {
		this.resPickupArea = resPickupArea;
	}

	/**
	 * @param resPickupDate
	 *            the resPickupDate to set
	 */
	public void setResPickupDate(GregorianCalendar resPickupDate) {
		this.resPickupDate = resPickupDate;
	}

	/**
	 * Sets the reservation pick-up date based on an string formatted YY-MM-DD.
	 * 
	 * @param resPickupDateString
	 *            the reservation date string.
	 * @throws ParseException
	 *             should the date be unparsable.
	 */
	public void setResPickupDate(String resPickupDateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		Date tempDate = sdf.parse(resPickupDateString);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(tempDate);

		this.resPickupDate = gc;
	}

	/**
	 * @param resPickupType
	 *            the resPickupType to set
	 */
	public void setResPickupType(Integer resPickupType) {
		this.resPickupType = resPickupType;
	}

	/**
	 * @param salesType
	 *            the salesType to set
	 */
	public void setSalesType(Integer salesType) {
		this.salesType = salesType;
	}

	/**
	 * @param validated
	 *            the validated to set
	 */
	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	/**
	 * Sets the validated boolean based on Nexus string values.
	 * 
	 * @param flag
	 *            nexus flag value.
	 */
	public void setValidated(String flag) {
		if (flag.compareTo(TRUESTRINGVALUE) == 0) validated = true;
		else validated = false;
	}

	/**
	 * @return the depositAmount
	 */
	public BigDecimal getDepositAmount() {
		return depositAmount;
	}

	/**
	 * @return the encoded
	 */
	public Boolean getEncoded() {
		return encoded;
	}

	/**
	 * @return the paid
	 */
	public Boolean getPaid() {
		return paid;
	}

	/**
	 * @return the resStatus
	 */
	public String getResStatus() {
		return resStatus;
	}

	/**
	 * @param depositAmount
	 *            the depositAmount to set
	 */
	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}

	/**
	 * @param encoded
	 *            the encoded to set
	 */
	public void setEncoded(Boolean encoded) {
		this.encoded = encoded;
	}

	/**
	 * Sets the encoded boolean based on Nexus string values.
	 * 
	 * @param flag
	 *            nexus flag value.
	 */
	public void setEncoded(String flag) {
		if (flag.compareTo(TRUESTRINGVALUE) == 0) encoded = true;
		else encoded = false;
	}

	/**
	 * @param paid
	 *            the paid to set
	 */
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	/**
	 * Sets the paid boolean based on Nexus string values.
	 * 
	 * @param flag
	 *            nexus flag value.
	 */
	public void setPaid(String flag) {
		if (flag.compareTo(TRUESTRINGVALUE) == 0) paid = true;
		else paid = false;
	}

	/**
	 * @param resStatus
	 *            the resStatus to set
	 */
	public void setResStatus(String resStatus) {
		this.resStatus = resStatus;
	}

}
