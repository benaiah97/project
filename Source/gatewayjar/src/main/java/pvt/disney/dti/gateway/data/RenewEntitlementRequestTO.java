package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the request parameters of a Renew Entitlement Request.
 * 
 * @author lewit019
 * @since 2.16.1
 */
public class RenewEntitlementRequestTO extends CommandBodyTO implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 9129231995L;

  /** An array list of tickets (or products, in this case). */
  private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

  /** Eligibility */
  private String eligibilityGroup;
  private String eligibilityMember;

  /** Payment */
  private ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

  /** Reservation */
  private ReservationTO reservation;

  /** ClientData */
  private ClientDataTO clientData;

  /**
   * Identifies this as an installment reservation request (one which includes installment payments. This flag is set when the installment payment amount is validated. {@link ProductRules.validateInstallmentDownpayment() }
   */
  private boolean isInstallmentRenewRequest = false;

  /**
   * Identifies the amount of the down-payment. This value is set when the installment payment amount is verified. It is used to populate values in the out-bound transaction to eGalaxy. {@link ProductRules.validateInstallmentDownpayment()
   * }
   */
  private BigDecimal installmentDownpayment = null;

  /**
   * The total order amount (as summarized by the business rules). {@link BusinessRules.applyReservationRules() }
   */
  private BigDecimal totalOrderAmount;

  /**
   * @return the tktList
   */
  public ArrayList<TicketTO> getTktList() {
    return tktList;
  }

  /**
   * @param tktList
   *            the tktList to set
   */
  public void setTktList(ArrayList<TicketTO> tktList) {
    this.tktList = tktList;
  }

  /**
   * @return the eligibilityGroup
   */
  public String getEligibilityGroup() {
    return eligibilityGroup;
  }

  /**
   * @param eligibilityGroup
   *            the eligibilityGroup to set
   */
  public void setEligibilityGroup(String eligibilityGroup) {
    this.eligibilityGroup = eligibilityGroup;
  }

  /**
   * @return the eligibilityMember
   */
  public String getEligibilityMember() {
    return eligibilityMember;
  }

  /**
   * @param eligibilityMember
   *            the eligibilityMember to set
   */
  public void setEligibilityMember(String eligibilityMember) {
    this.eligibilityMember = eligibilityMember;
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
   * @return the reservation
   */
  public ReservationTO getReservation() {
    return reservation;
  }

  /**
   * @param reservation
   *            the reservation to set
   */
  public void setReservation(ReservationTO reservation) {
    this.reservation = reservation;
  }

  /**
   * @return the clientData
   */
  public ClientDataTO getClientData() {
    return clientData;
  }

  /**
   * @param clientData
   *            the clientData to set
   */
  public void setClientData(ClientDataTO clientData) {
    this.clientData = clientData;
  }

  /**
   * @return the totalOrderAmount
   */
  public BigDecimal getTotalOrderAmount() {
    return totalOrderAmount;
  }

  /**
   * @param totalOrderAmount
   *            the totalOrderAmount to set
   */
  public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
    this.totalOrderAmount = totalOrderAmount;
  }

  /**
   * @return the isInstallmentRenewRequest
   */
  public boolean isInstallmentRenewRequest() {
    return isInstallmentRenewRequest;
  }

  /**
   * @param isInstallmentRenewRequest
   *            the isInstallmentResRequest to set
   */
  public void setInstallmentRenewRequest(boolean isInstallmentRenewRequest) {
    this.isInstallmentRenewRequest = isInstallmentRenewRequest;
  }

  /**
   * @return the installmentDownpayment
   */
  public BigDecimal getInstallmentDownpayment() {
    return installmentDownpayment;
  }

  /**
   * @param installmentDownpayment
   *            the installmentDownpayment to set
   */
  public void setInstallmentDownpayment(BigDecimal installmentDownpayment) {
    this.installmentDownpayment = installmentDownpayment;
  }

}
