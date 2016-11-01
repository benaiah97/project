package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * This class represents the reservation information of a DTI order.
 * 
 * @author lewit019
 */
public class ReservationTO implements Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = 9129231995L;

	/** The res code. */
	private String resCode;

	/** ContractId. (As of 2.15, JTL) */
	private String contractId;

	/** The res number. */
	private String resNumber;

	/** The res create date. */
	private GregorianCalendar resCreateDate;

	/** The res pickup date. */
	private GregorianCalendar resPickupDate;

	/** The res pickup area. */
	private String resPickupArea;

	/** The res sales type. */
	private String resSalesType;

	/**
	 * Gets the res code.
	 * 
	 * @return the resCode
	 */
	public String getResCode() {
		return resCode;
	}

	/**
	 * Gets the res create date.
	 * 
	 * @return the resCreateDate
	 */
	public GregorianCalendar getResCreateDate() {
		return resCreateDate;
	}

	/**
	 * Gets the res number.
	 * 
	 * @return the resNumber
	 */
	public String getResNumber() {
		return resNumber;
	}

	/**
	 * Gets the res pickup area.
	 * 
	 * @return the resPickupArea
	 */
	public String getResPickupArea() {
		return resPickupArea;
	}

	/**
	 * Gets the res pickup date.
	 * 
	 * @return the resPickupDate
	 */
	public GregorianCalendar getResPickupDate() {
		return resPickupDate;
	}

	/**
	 * Gets the res sales type.
	 * 
	 * @return the resSalesType
	 */
	public String getResSalesType() {
		return resSalesType;
	}

	/**
	 * Sets the res code.
	 * 
	 * @param resCode
	 *            the resCode to set
	 */
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	/**
	 * Sets the res create date.
	 * 
	 * @param resCreateDate
	 *            the resCreateDate to set
	 */
	public void setResCreateDate(GregorianCalendar resCreateDate) {
		this.resCreateDate = resCreateDate;
	}

	/**
	 * Sets the res number.
	 * 
	 * @param resNumber
	 *            the resNumber to set
	 */
	public void setResNumber(String resNumber) {
		this.resNumber = resNumber;
	}

	/**
	 * Sets the res pickup area.
	 * 
	 * @param resPickupArea
	 *            the resPickupArea to set
	 */
	public void setResPickupArea(String resPickupArea) {
		this.resPickupArea = resPickupArea;
	}

	/**
	 * Sets the res pickup date.
	 * 
	 * @param resPickupDate
	 *            the resPickupDate to set
	 */
	public void setResPickupDate(GregorianCalendar resPickupDate) {
		this.resPickupDate = resPickupDate;
	}

	/**
	 * Sets the res sales type.
	 * 
	 * @param resSalesType
	 *            the resSalesType to set
	 */
	public void setResSalesType(String resSalesType) {
		this.resSalesType = resSalesType;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId
	 *            the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

}
