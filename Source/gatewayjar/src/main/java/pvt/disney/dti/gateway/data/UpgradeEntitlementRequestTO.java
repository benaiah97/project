package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * 
 * @author HUFFM017
 * 
 */
public class UpgradeEntitlementRequestTO extends CommandBodyTO implements Serializable {

	/** Standard serial Version UID */
	private static final long serialVersionUID = -5390027434972940288L;

	/** An array list of tickets (or products, in this case). */
	protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

	/** Payment */
	private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();
	
	/**
	 * Audit Notation (as of 2.12). When the transaction note in ATS needs to be used or a reason for a failure recorded,
	 */
	private String auditNotation;
	
  /**
   * Identifies this as an installment reservation request (one which includes installment payments. This flag is set when the installment payment amount is validated. {@link ProductRules.validateInstallmentDownpayment() }
   */
  private boolean isInstallmentRequest = false;

  /**
   * Identifies the amount of the down-payment. This value is set when the installment payment amount is verified. It is used to populate values in the out-bound transaction to eGalaxy. {@link ProductRules.validateInstallmentDownpayment()
   * }
   */
  private BigDecimal installmentDownpayment = null;

	/* -------------------------------------------------------------------------------- */

	/**
	 * @return the tktList
	 */
	public ArrayList<TicketTO> getTicketList() {
		return ticketList;
	}

	/**
	 * @param tktList
	 *            the tktList to set
	 */
	public void setTicketList(ArrayList<TicketTO> ticketList) {
		this.ticketList = ticketList;
	}

	/**
	 * @return the paymentList
	 */
	public ArrayList<PaymentTO> getPaymentList() {
		return paymentList;
	}

	/**
	 * @param paymentList
	 *            the paymentList to set
	 */
	public void setPaymentList(ArrayList<PaymentTO> paymentList) {
		this.paymentList = paymentList;
	}

	/**
	 * @return the auditNotation
	 */
	public String getAuditNotation() {
		return auditNotation;
	}

	/**
	 * @param auditNotation
	 *            the auditNotation to set
	 */
	public void setAuditNotation(String auditNotation) {
		this.auditNotation = auditNotation;
	}

  /**
   * @return the isInstallmentRequest
   */
  public boolean isInstallmentRequest() {
    return isInstallmentRequest;
  }

  /**
   * @param isInstallmentRequest the isInstallmentRequest to set
   */
  public void setInstallmentRequest(boolean isInstallmentRequest) {
    this.isInstallmentRequest = isInstallmentRequest;
  }

  /**
   * @return the installmentDownpayment
   */
  public BigDecimal getInstallmentDownpayment() {
    return installmentDownpayment;
  }

  /**
   * @param installmentDownpayment the installmentDownpayment to set
   */
  public void setInstallmentDownpayment(BigDecimal installmentDownpayment) {
    this.installmentDownpayment = installmentDownpayment;
  }

}
