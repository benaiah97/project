package pvt.disney.dti.gateway.provider.dlr.data;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Gateway Order Transfer Object
 * 
 * @author lewit019
 * 
 */
public class GWOrderTO {

	/** order node */
	private String orderNote;
	/** order id */
	private String orderID;
	/** customer id */
	private String customerID;
	/** order date */
	private String orderDate;
	/** order status */
	private String orderStatus;
	/** Sales Program (as of 2.16.1, JTL) */
	private String salesProgram;
	/** order total */
	private BigDecimal orderTotal;

	/** Payment contracts (as of 2.16.1, JTL) */
	private ArrayList<GWPaymentContractTO> paymntContractList = new ArrayList<GWPaymentContractTO>();

	/** order contact */
	private GWOrderContactTO orderContact;
	/** order ship to contact */
	private GWOrderContactTO shipToContact;
	/** collection of order line elements in an order */
	private ArrayList<GWOrderLineTO> orderLineList = new ArrayList<GWOrderLineTO>();
	/** shipping delivery method */
	private String shipDeliveryMethod; // 2
	/** shipping delivery details */
	private String shipDeliveryDetails;// US MAIL
	/** group visit date */
	private String groupVisitDate;
	/** group visit reference */
	private String groupVisitReference;
	/** order reference field */
	private String orderReference;
	/** group visit description */
	private String groupVisitDescription;

	/**
	 * @return the orderReference
	 */
	public String getOrderReference() {
		return orderReference;
	}

	/**
	 * @param orderReference
	 *            the orderReference to set
	 */
	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	/**
	 * @return the groupVisitDate
	 */
	public String getGroupVisitDate() {
		return groupVisitDate;
	}

	/**
	 * @param groupVisitDate
	 *            the groupVisitDate to set
	 */
	public void setGroupVisitDate(String groupVisitDate) {
		this.groupVisitDate = groupVisitDate;
	}

	/**
	 * @return the orderNote
	 */
	public String getOrderNote() {
		return orderNote;
	}

	/**
	 * @param orderNote
	 *            the orderNote to set
	 */
	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return orderID;
	}

	/**
	 * @param orderID
	 *            the orderID to set
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @param customerID
	 *            the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate
	 *            the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the orderContact
	 */
	public GWOrderContactTO getOrderContact() {
		return orderContact;
	}

	/**
	 * @param orderContact
	 *            the orderContact to set
	 */
	public void setOrderContact(GWOrderContactTO orderContact) {
		this.orderContact = orderContact;
	}

	/**
	 * @return the shipToContact
	 */
	public GWOrderContactTO getShipToContact() {
		return shipToContact;
	}

	/**
	 * @param shipToContact
	 *            the shipToContact to set
	 */
	public void setShipToContact(GWOrderContactTO shipToCotact) {
		this.shipToContact = shipToCotact;
	}

	/**
	 * @return the orderLineList
	 */
	public ArrayList<GWOrderLineTO> getOrderLineList() {
		return orderLineList;
	}

	/**
	 * Convenience method for adding an order line to to the order line list.
	 * 
	 * @param orderTO
	 */
	public void addOrderLine(GWOrderLineTO orderTO) {
		orderLineList.add(orderTO);
	}

	/**
	 * @param orderLineList
	 *            the orderLineList to set
	 */
	public void setOrderLineList(ArrayList<GWOrderLineTO> orderLineList) {
		this.orderLineList = orderLineList;
	}

	/**
	 * @return the shipDeliveryMethod
	 */
	public String getShipDeliveryMethod() {
		return shipDeliveryMethod;
	}

	/**
	 * @param shipDeliveryMethod
	 *            the shipDeliveryMethod to set
	 */
	public void setShipDeliveryMethod(String shipDeliveryMethod) {
		this.shipDeliveryMethod = shipDeliveryMethod;
	}

	/**
	 * @return the shipDeliveryDetails
	 */
	public String getShipDeliveryDetails() {
		return shipDeliveryDetails;
	}

	/**
	 * @param shipDeliveryDetails
	 *            the shipDeliveryDetails to set
	 */
	public void setShipDeliveryDetails(String shipDeliveryDetails) {
		this.shipDeliveryDetails = shipDeliveryDetails;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	/**
	 * 
	 * @param orderTotal
	 */
	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}

	/**
	 * @return the groupVisitReference
	 */
	public String getGroupVisitReference() {
		return groupVisitReference;
	}

	/**
	 * @param groupVisitReference
	 *            the groupVisitReference to set
	 */
	public void setGroupVisitReference(String groupVisitReference) {
		this.groupVisitReference = groupVisitReference;
	}

	/**
	 * @return the groupVisitDescription
	 */
	public String getGroupVisitDescription() {
		return groupVisitDescription;
	}

	/**
	 * @param groupVisitDescription
	 *            the groupVisitDescription to set
	 */
	public void setGroupVisitDescription(String groupVisitDescription) {
		this.groupVisitDescription = groupVisitDescription;
	}

	/**
	 * @return the salesProgram
	 */
	public String getSalesProgram() {
		return salesProgram;
	}

	/**
	 * @param salesProgram
	 *            the salesProgram to set
	 */
	public void setSalesProgram(String salesProgram) {
		this.salesProgram = salesProgram;
	}

	/**
	 * @return the paymntContractList
	 */
	public ArrayList<GWPaymentContractTO> getPaymntContractList() {
		return paymntContractList;
	}

	/**
	 * @param paymntContractList
	 *            the paymntContractList to set
	 */
	public void setPaymntContractList(
			ArrayList<GWPaymentContractTO> paymntContractList) {
		this.paymntContractList = paymntContractList;
	}

	/**
	 * 
	 * @param aContract
	 */
	public void addPaymentContract(GWPaymentContractTO aContract) {
		this.paymntContractList.add(aContract);
	}

}
