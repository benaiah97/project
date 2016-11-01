package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

/**
 * This class represents the transfer object version of the Gateway Payment Contract.
 * 
 * @author lewit019
 * @since 2.16.1
 */
public class GWPaymentContractTO implements Serializable {

	/** Default serial version ID */
	private static final long serialVersionUID = 1L;

	// Input Fields
	private String recurrenceType;

	private Integer dayOfMonth;

	private Integer interval;

	private String startDate;

	private String endDate;

	private String downPaymentAmount;

	private String paymentPlanID;

	private Boolean renewContract;

	private String paymentContractStatusID;

	private String contactMethod;

	// Output fields
	private String paymentContractID;

	/**
	 * @return the recurrenceType
	 */
	public String getRecurrenceType() {
		return recurrenceType;
	}

	/**
	 * @param recurrenceType
	 *            the recurrenceType to set
	 */
	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	/**
	 * @return the dayOfMonth
	 */
	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	/**
	 * @param dayOfMonth
	 *            the dayOfMonth to set
	 */
	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * @return the interval
	 */
	public Integer getInterval() {
		return interval;
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the paymentPlanID
	 */
	public String getPaymentPlanID() {
		return paymentPlanID;
	}

	/**
	 * @param paymentPlanID
	 *            the paymentPlanID to set
	 */
	public void setPaymentPlanID(String paymentPlanID) {
		this.paymentPlanID = paymentPlanID;
	}

	/**
	 * @return the renewContract
	 */
	public Boolean getRenewContract() {
		return renewContract;
	}

	/**
	 * @param renewContract
	 *            the renewContract to set
	 */
	public void setRenewContract(Boolean renewContract) {
		this.renewContract = renewContract;
	}

	/**
	 * @return the paymentContractStatusID
	 */
	public String getPaymentContractStatusID() {
		return paymentContractStatusID;
	}

	/**
	 * @param paymentContractStatusID
	 *            the paymentContractStatusID to set
	 */
	public void setPaymentContractStatusID(String paymentContractStatusID) {
		this.paymentContractStatusID = paymentContractStatusID;
	}

	/**
	 * @return the contactMethod
	 */
	public String getContactMethod() {
		return contactMethod;
	}

	/**
	 * @param contactMethod
	 *            the contactMethod to set
	 */
	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}

	/**
	 * @return the downPaymentAmount
	 */
	public String getDownPaymentAmount() {
		return downPaymentAmount;
	}

	/**
	 * @param downPaymentAmount
	 *            the downPaymentAmount to set
	 */
	public void setDownPaymentAmount(String downPaymentAmount) {
		this.downPaymentAmount = downPaymentAmount;
	}

	/**
	 * @return the paymentContractID
	 */
	public String getPaymentContractID() {
		return paymentContractID;
	}

	/**
	 * @param paymentContractID
	 *            the paymentContractID to set
	 */
	public void setPaymentContractID(String paymentContractID) {
		this.paymentContractID = paymentContractID;
	}

}
