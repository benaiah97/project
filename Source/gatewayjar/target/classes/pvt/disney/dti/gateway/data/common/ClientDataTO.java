package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * This class contains the data found in the client section of DTI's XML.
 * 
 * @author lewit019
 * 
 */
public class ClientDataTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** The enumeration of possible transaction types in DTI. */
	public enum PaymentMethod {
		CREDITCARD,
		DISNEYGIFTCARD,
		ORGANIZATIONALCHECK,
		CASHIERSCHECK,
		MONEYORDER,
		WIRETRANSFER,
		PAYATPICKUP,
		MASTERACCOUNT,
		SAPJOBNUMBER,
		TICKETEXCHANGE,
		UNDEFINED
	};

	/** The numerical value the provider system has assigned the client. */
	private String clientId;

	/** The type of client as assigned by the provider system. */
	private String clientType;

	/** The category of the client, as assigned by the provider system. */
	private String clientCategory;

	/** The client's language. */
	private String demoLanguage;

	/** Billing info of the client. */
	private DemographicsTO billingInfo;

	/** Shipping info of the client. */
	private DemographicsTO shippingInfo;

	/** Delivery info of the client. */
	private DemographicsTO deliveryInfo;

	/** Client payment method. */
	private PaymentMethod clientPaymentMethod = PaymentMethod.UNDEFINED;

	/** Client fulfillment method. */
	private String clientFulfillmentMethod;

	/** Client delivery instructions. */
	private String clientDeliveryInstructions;

	/** Client Group Validity Valid Start */
	private GregorianCalendar groupValidityValidStart;

	/** Client Group Validity Valid End */
	private GregorianCalendar groupValidityValidEnd;

	/** Client Sales Contact Protal ID who pushed the transaction through to DTI (from BOLT or who set up the store, originally */
	private String clientSalesContact;

	/** Time Requirement driven by the shipping product present on the order and supplied in full */
	private String timeRequirement;

	/**
	 * @return the billingInfo
	 */
	public DemographicsTO getBillingInfo() {
		return billingInfo;
	}

	/**
	 * @return the clientCategory
	 */
	public String getClientCategory() {
		return clientCategory;
	}

	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @return the demoLanguage
	 */
	public String getDemoLanguage() {
		return demoLanguage;
	}

	/**
	 * @return the shippingInfo
	 */
	public DemographicsTO getShippingInfo() {
		return shippingInfo;
	}

	/**
	 * @param billingInfo
	 *            the billingInfo to set
	 */
	public void setBillingInfo(DemographicsTO billingInfo) {
		this.billingInfo = billingInfo;
	}

	/**
	 * @param clientCategory
	 *            the clientCategory to set
	 */
	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}

	/**
	 * @param clientId
	 *            the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	/**
	 * @param clientType
	 *            the clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/**
	 * @param demoLanguage
	 *            the demoLanguage to set
	 */
	public void setDemoLanguage(String demoLanguage) {
		this.demoLanguage = demoLanguage;
	}

	/**
	 * @param shippingInfo
	 *            the shippingInfo to set
	 */
	public void setShippingInfo(DemographicsTO shippingInfo) {
		this.shippingInfo = shippingInfo;
	}

	public String toString() {
		String to = "ClientDataTO: clientId=" + clientId + " clientType=" + clientType;
		return to;
	}

	/**
	 * Gets the delivery info.
	 * 
	 * @return delivery info
	 */
	public DemographicsTO getDeliveryInfo() {
		return deliveryInfo;
	}

	/**
	 * Sets the delivery info
	 * 
	 * @param deliveryInfo
	 */
	public void setDeliveryInfo(DemographicsTO deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	/**
	 * Gets the client payment method
	 * 
	 * @return the client payment method
	 */
	public PaymentMethod getClientPaymentMethod() {
		return clientPaymentMethod;
	}

	/**
	 * Sets the client payment method
	 * 
	 * @param clientPaymentMethod
	 */
	public void setClientPaymentMethod(PaymentMethod clientPaymentMethod) {
		this.clientPaymentMethod = clientPaymentMethod;
	}

	/**
	 * Gets the client fulfillment method.
	 * 
	 * @return the client fulfillment method.
	 */
	public String getClientFulfillmentMethod() {
		return clientFulfillmentMethod;
	}

	/**
	 * Sets the client fulfillment method.
	 * 
	 * @param clientFulfillmentMethod
	 */
	public void setClientFulfillmentMethod(String clientFulfillmentMethod) {
		this.clientFulfillmentMethod = clientFulfillmentMethod;
	}

	/**
	 * Gets the client delivery instructions.
	 * 
	 * @return the client delivery instructions.
	 */
	public String getClientDeliveryInstructions() {
		return clientDeliveryInstructions;
	}

	/**
	 * Sets the client delivery instructions
	 * 
	 * @param clientDeliveryInstructions
	 */
	public void setClientDeliveryInstructions(String clientDeliveryInstructions) {
		this.clientDeliveryInstructions = clientDeliveryInstructions;
	}

	/**
	 * Gets the group validity valid end dates.
	 * 
	 * @return the group validity valid end dates.
	 */
	public GregorianCalendar getGroupValidityValidEnd() {
		return groupValidityValidEnd;
	}

	/**
	 * Sets the group validity valid end date.
	 * 
	 * @param groupValidityValidEnd
	 */
	public void setGroupValidityValidEnd(GregorianCalendar groupValidityValidEnd) {
		this.groupValidityValidEnd = groupValidityValidEnd;
	}

	/**
	 * Gets the group validity valid start date.
	 * 
	 * @return the group validity valid start date.
	 */
	public GregorianCalendar getGroupValidityValidStart() {
		return groupValidityValidStart;
	}

	/**
	 * Sets the group validity valid start date.
	 * 
	 * @param groupValidityValidStart
	 */
	public void setGroupValidityValidStart(
			GregorianCalendar groupValidityValidStart) {
		this.groupValidityValidStart = groupValidityValidStart;
	}

	/**
	 * Client Sales Contact Protal ID who pushed the transaction through to DTI (from BOLT or who set up the store, originally
	 * 
	 * @return the clientSalesContact
	 */
	public String getClientSalesContact() {
		return clientSalesContact;
	}

	/**
	 * Client Sales Contact Protal ID who pushed the transaction through to DTI (from BOLT or who set up the store, originally
	 * 
	 * @param clientSalesContact
	 *            the clientSalesContact to set
	 */
	public void setClientSalesContact(String clientSalesContact) {
		this.clientSalesContact = clientSalesContact;
	}

	/**
	 * Time Requirement driven by the shipping product present on the order and supplied in full
	 * 
	 * @return the timeRequirement
	 */
	public String getTimeRequirement() {
		return timeRequirement;
	}

	/**
	 * Time Requirement driven by the shipping product present on the order and supplied in full
	 * 
	 * @param timeRequirement
	 *            the timeRequirement to set
	 */
	public void setTimeRequirement(String timeRequirement) {
		this.timeRequirement = timeRequirement;
	}

}
