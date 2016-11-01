package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the values in an Upgrade Alpha Request.
 * 
 * @author lewit019
 * 
 */
public class UpgradeAlphaRequestTO extends CommandBodyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** An array list of tickets (or products, in this case). */
	private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

	/** Payment */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

	/** Eligibility Group */
	private String eligibilityGroup;

	/** Eligibility Member */
	private String eligibilityMember;

	/** ClientData (not supported by DTI) */

	/** SalesRep (not supported by DTI) */

	/** Agency (Not supported by DTI) */

	/**
	 * As of 2.15 JTL An optional value that determines if the Visual ID should be returned in the response (in addition to then normal ticket attributes). Defaults to False.
	 */
	private boolean includeVisualId = false;

	/**
	 * @return the eligibilityGroup
	 */
	public String getEligibilityGroup() {
		return eligibilityGroup;
	}

	/**
	 * @return the eligibilityMember
	 */
	public String getEligibilityMember() {
		return eligibilityMember;
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTktList() {
		return tktList;
	}

	/**
	 * @param eligibilityGroup
	 *            the eligibilityGroup to set
	 */
	public void setEligibilityGroup(String eligibilityGroup) {
		this.eligibilityGroup = eligibilityGroup;
	}

	/**
	 * @param eligibilityMember
	 *            the eligibilityMember to set
	 */
	public void setEligibilityMember(String eligibilityMember) {
		this.eligibilityMember = eligibilityMember;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTktList(ArrayList<TicketTO> ticketList) {
		this.tktList = ticketList;
	}

	/**
	 * @return the includeVisualId
	 */
	public boolean isIncludeVisualId() {
		return includeVisualId;
	}

	/**
	 * @param includeVisualId
	 *            the includeVisualId to set
	 */
	public void setIncludeVisualId(boolean includeVisualId) {
		this.includeVisualId = includeVisualId;
	}

}
