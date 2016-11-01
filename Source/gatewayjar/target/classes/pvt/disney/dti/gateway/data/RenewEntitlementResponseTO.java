package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;

/**
 * This class represents the response parameters of a Renew Entitlement Request.
 * 
 * @author lewit019
 * @since 2.16.1
 */
public class RenewEntitlementResponseTO extends CommandBodyTO implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 9129231995L;

  /** An array list of tickets. */
  protected ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();

  /** An array list of products. */
  protected ArrayList<ProductTO> productList = new ArrayList<ProductTO>();

  /** An array of payments. */
  protected ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();

  /** Renewal:ContactId */
  protected String contractId;

  /** Renewal:TktTransaction */
  protected TicketTransactionTO tktTran;

  /** ClientData */
  protected ClientDataTO clientData;

  /**
   * @return the ticketList
   */
  public ArrayList<TicketTO> getTicketList() {
    return ticketList;
  }

  /**
   * @param ticketList
   *            the ticketList to set
   */
  public void setTicketList(ArrayList<TicketTO> ticketList) {
    this.ticketList = ticketList;
  }

  /**
   * @return the productList
   */
  public ArrayList<ProductTO> getProductList() {
    return productList;
  }

  /**
   * @param productList
   *            the productList to set
   */
  public void setProductList(ArrayList<ProductTO> productList) {
    this.productList = productList;
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

  /**
   * @return the tktTran
   */
  public TicketTransactionTO getTktTran() {
    return tktTran;
  }

  /**
   * @param tktTran
   *            the tktTran to set
   */
  public void setTktTran(TicketTransactionTO tktTran) {
    this.tktTran = tktTran;
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
   * @param aPayment
   *            the PaymentTO to add
   */
  public void addPayment(PaymentTO aPayment) {
    paymentList.add(aPayment);
  }

  /**
   * @param tktList
   *            the tktList to add
   */
  public void addTicket(TicketTO aTicket) {
    ticketList.add(aTicket);
  }

}
