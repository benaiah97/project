package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;

/**
 * This class is a transfer object which represents the Omni Ticket renew ticket.
 * 
 * @author lewit019
 */
public class OTRenewTicketTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  // Out-bound fields
  /** The tagsList. */
  private ArrayList<String> tagsList = new ArrayList<String>();

  /** The site number. */
  private Integer siteNumber;

  /** The renew ticket info list. */
  private ArrayList<OTTicketInfoTO> renewTicketInfoList = new ArrayList<OTTicketInfoTO>();

  /** The payment info. */
  private ArrayList<OTPaymentTO> paymentInfoList = new ArrayList<OTPaymentTO>();

  /** The seller id. */
  private BigInteger sellerId;

  /** The transaction note. */
  private String transactionNote;

  /** The in txn attr. */
  private ArrayList<OTInTransactionAttributeTO> inTxnAttrList = new ArrayList<OTInTransactionAttributeTO>();

  // In-bound fields
  /** The error. */
  private OTErrorTO error;

  /** The transaction dssn. */
  private OTTransactionDSSNTO transactionDSSN;

  /** The transaction cod. */
  private String transactionCOD;

  /** The total amount. */
  private BigDecimal totalAmount;

  /** The total tax. */
  private BigDecimal totalTax;

  /** The tax exempt. */
  private Boolean taxExempt;

  /** If transactions generates an event; xsd defaults to false */
  private Boolean generateEvent;

  /**
   * Gets the error.
   * 
   * @return the error
   */
  public OTErrorTO getError() {
    return error;
  }

  /**
   * Gets the in txn attr.
   * 
   * @return the inTxnAttrList
   */
  public ArrayList<OTInTransactionAttributeTO> getInTxnAttrList() {
    return inTxnAttrList;
  }

  /**
   * Gets the payment info.
   * 
   * @return the paymentInfoList
   */
  public ArrayList<OTPaymentTO> getPaymentInfoList() {
    return paymentInfoList;
  }

  /**
   * Gets the seller id.
   * 
   * @return the sellerId
   */
  public BigInteger getSellerId() {
    return sellerId;
  }

  /**
   * Gets the site number.
   * 
   * @return the siteNumber
   */
  public Integer getSiteNumber() {
    return siteNumber;
  }

  /**
   * Gets the tagsList.
   * 
   * @return the tagsList
   */
  public ArrayList<String> getTagsList() {
    return tagsList;
  }

  /**
   * Gets the tax exempt.
   * 
   * @return the taxExempt
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * Gets the total amount.
   * 
   * @return the totalAmount
   */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  /**
   * Gets the total tax.
   * 
   * @return the totalTax
   */
  public BigDecimal getTotalTax() {
    return totalTax;
  }

  /**
   * Gets the transaction cod.
   * 
   * @return the transactionCOD
   */
  public String getTransactionCOD() {
    return transactionCOD;
  }

  /**
   * Gets the transaction dssn.
   * 
   * @return the transactionDSSN
   */
  public OTTransactionDSSNTO getTransactionDSSN() {
    return transactionDSSN;
  }

  /**
   * Gets the transaction note.
   * 
   * @return the transactionNote
   */
  public String getTransactionNote() {
    return transactionNote;
  }

  /**
   * Gets the renew ticket info list.
   * 
   * @return the renewTicketInfoList
   */
  public ArrayList<OTTicketInfoTO> getRenewTicketInfoList() {
    return renewTicketInfoList;
  }

  /**
   * Sets the error.
   * 
   * @param error
   *            the error to set
   */
  public void setError(OTErrorTO error) {
    this.error = error;
  }

  /**
   * Sets the in txn attr.
   * 
   * @param inTxnAttrList
   *            the inTxnAttrList to set
   */
  public void setInTxnAttrList(ArrayList<OTInTransactionAttributeTO> inTxnAttr) {
    this.inTxnAttrList = inTxnAttr;
  }

  /**
   * Sets the payment info.
   * 
   * @param paymentInfoList
   *            the paymentInfoList to set
   */
  public void setPaymentInfoList(ArrayList<OTPaymentTO> paymentInfo) {
    this.paymentInfoList = paymentInfo;
  }

  /**
   * Sets the seller id.
   * 
   * @param sellerId
   *            the sellerId to set
   */
  public void setSellerId(BigInteger sellerId) {
    this.sellerId = sellerId;
  }

  /**
   * Sets the site number.
   * 
   * @param siteNumber
   *            the siteNumber to set
   */
  public void setSiteNumber(Integer siteNumber) {
    this.siteNumber = siteNumber;
  }

  /**
   * Sets the tagsList.
   * 
   * @param tagsList
   *            the tagsList to set
   */
  public void setTagsList(ArrayList<String> tags) {
    this.tagsList = tags;
  }

  /**
   * Sets the tax exempt.
   * 
   * @param taxExempt
   *            the taxExempt to set
   */
  public void setTaxExempt(Boolean taxExempt) {
    this.taxExempt = taxExempt;
  }

  /**
   * Sets the total amount.
   * 
   * @param totalAmount
   *            the totalAmount to set
   */
  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  /**
   * Sets the total tax.
   * 
   * @param totalTax
   *            the totalTax to set
   */
  public void setTotalTax(BigDecimal totalTax) {
    this.totalTax = totalTax;
  }

  /**
   * Sets the transaction cod.
   * 
   * @param transactionCOD
   *            the transactionCOD to set
   */
  public void setTransactionCOD(String transactionCOD) {
    this.transactionCOD = transactionCOD;
  }

  /**
   * Sets the transaction dssn.
   * 
   * @param transactionDSSN
   *            the transactionDSSN to set
   */
  public void setTransactionDSSN(OTTransactionDSSNTO transactionDSSN) {
    this.transactionDSSN = transactionDSSN;
  }

  /**
   * Sets the transaction note.
   * 
   * @param transactionNote
   *            the transactionNote to set
   */
  public void setTransactionNote(String transactionNote) {
    this.transactionNote = transactionNote;
  }

  /**
   * Sets the renew ticket info list.
   * 
   * @param renewTicketInfoList
   *            the renewTicketInfoList to set
   */
  public void setRenewTicketInfoList(ArrayList<OTTicketInfoTO> ticketInfoList) {
    this.renewTicketInfoList = ticketInfoList;
  }

  /**
   * @param generateEvent
   *            the generateEvent to set
   */
  public void setGenerateEvent(Boolean generateEvent) {
    this.generateEvent = generateEvent;
  }

  /**
   * @return the generateEvent
   */
  public Boolean getGenerateEvent() {
    return generateEvent;
  }

}
