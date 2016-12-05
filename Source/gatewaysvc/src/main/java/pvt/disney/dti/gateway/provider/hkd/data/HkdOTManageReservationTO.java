package pvt.disney.dti.gateway.provider.hkd.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAccountDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTErrorTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTExternalTransactionIDTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTReservationDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTShowDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTransactionDSSNTO;

/**
 * This class is a transfer object which represents the Omni ticket manage reservation.
 * 
 * @author lewit019
 */
public class HkdOTManageReservationTO implements Serializable {

  /** Standard serial version UID. */
  public final static long serialVersionUID = 9129231995L;

  // Out-bound fields
  private ArrayList<String> tagsList = new ArrayList<String>();

  /** The Omni Ticket site number. */
  private Integer siteNumber;

  /** The reservation unique identifier. */
  private String reservationUniqueId;

  /** The array list of Omni Ticket Product Transfer Objects. */
  private ArrayList<HkdOTProductTO> productList = new ArrayList<HkdOTProductTO>();

  /** The numerical identifier of the seller. */
  private Long sellerId;

  /** The International Air Transport Association number (travel agent number. */
  private String iATA;

  /**
   * The tax exempt code to be recorded on an order for not for profit sellers.
   */
  private String taxExemptCode;

  // In-bound fields
  /** The numerical reservation identifier. */
  private Integer reservationId;

  /** The flag indicating a tax exempt sale. */
  private Boolean taxExempt;

  /** The total tax on an order. */
  private BigDecimal totalTax;

  /** The total amount on an order. */
  private BigDecimal totalAmount;

  /** The transaction time. */
  private String transactionTime;

  /** The transaction code. */
  private String transactionCOD;

  /** The Omni Ticket Transaction DSSN (Date, site, station, number). */
  private HkdOTTransactionDSSNTO transactionDSSN;

  /** The Omni Ticket error in the response. */
  private HkdOTErrorTO error;

  /** The list of ticket info objects. */
  private ArrayList<HkdOTTicketInfoTO> ticketInfoList = new ArrayList<HkdOTTicketInfoTO>();

  /** The association information for this order. */
  private HkdOTAssociationInfoTO associationInfo;

  // Shared fields
  /** The command type. */
  private String commandType;

  /** The reservation code. */
  private String reservationCode;

  /** The reservation note. */
  private String reservationNote;

  /** Note Details. */
  private ArrayList<String> noteDetailsArray = new ArrayList<String>();

  /** The Omni Ticket Reservation Data Transfer Object. */
  private HkdOTReservationDataTO reservationData;

  /** The Omni Ticket Client Data Transfer Object. */
  private HkdOTClientDataTO clientData;

  /** The list of payment info transfer objects. */
  private ArrayList<HkdOTPaymentTO> paymentInfoList = new ArrayList<HkdOTPaymentTO>();

  /**
   * Entitlement Account Creation Typology: SINGLEACCOUNT, PERTICKETACCOUNT, ENTITLEMENTACCOUNTID
   */
  private String entitlementAccountCreationTypology;

  private ArrayList<HkdOTAccountDataTO> accountsData = new ArrayList<HkdOTAccountDataTO>();

  /** If transactions generates an event; XSD defaults to false */
  private Boolean generateEvent;

  /** The transaction note. (2.10) */
  private String transactionNote;
  
  /** The External Transaction ID */
  private HkdOTExternalTransactionIDTO externalTransactionID;
  
  /** Show Data (as of 2.16.3, JTL) */
  private HkdOTShowDataTO showData;

  /**
   * @return the clientData
   */
  public HkdOTClientDataTO getClientData() {
    return clientData;
  }

  /**
   * @return the commandType
   */
  public String getCommandType() {
    return commandType;
  }

  /**
   * @return the iATA
   */
  public String getIATA() {
    return iATA;
  }

  /**
   * @return the paymentInfoList
   */
  public ArrayList<HkdOTPaymentTO> getPaymentInfoList() {
    return paymentInfoList;
  }

  /**
   * @return the productList
   */
  public ArrayList<HkdOTProductTO> getProductList() {
    return productList;
  }

  /**
   * @return the reservationCode
   */
  public String getReservationCode() {
    return reservationCode;
  }

  /**
   * @return the reservationData
   */
  public HkdOTReservationDataTO getReservationData() {
    return reservationData;
  }

  /**
   * @return the reservationId
   */
  public Integer getReservationId() {
    return reservationId;
  }

  /**
   * @return the reservationNote
   */
  public String getReservationNote() {
    return reservationNote;
  }

  /**
   * @return the reservationUniqueId
   */
  public String getReservationUniqueId() {
    return reservationUniqueId;
  }

  /**
   * @return the sellerId
   */
  public Long getSellerId() {
    return sellerId;
  }

  /**
   * @return the siteNumber
   */
  public Integer getSiteNumber() {
    return siteNumber;
  }

  /**
   * @return the tagsList
   */
  public ArrayList<String> getTagsList() {
    return tagsList;
  }

  /**
   * @return the taxExempt
   */
  public Boolean getTaxExempt() {
    return taxExempt;
  }

  /**
   * @return the transactionCOD
   */
  public String getTransactionCOD() {
    return transactionCOD;
  }

  /**
   * @return the transactionTime
   */
  public String getTransactionTime() {
    return transactionTime;
  }

  /**
   * @param clientData
   *            the clientData to set
   */
  public void setClientData(HkdOTClientDataTO clientData) {
    this.clientData = clientData;
  }

  /**
   * @param commandType
   *            the commandType to set
   */
  public void setCommandType(String commandType) {
    this.commandType = commandType;
  }

  /**
   * @param iata
   *            the iATA to set
   */
  public void setIATA(String iata) {
    iATA = iata;
  }

  /**
   * @param paymentInfoList
   *            the paymentInfoList to set
   */
  public void setPaymentInfoList(ArrayList<HkdOTPaymentTO> paymentInfo) {
    this.paymentInfoList = paymentInfo;
  }

  /**
   * @param productList
   *            the productList to set
   */
  public void setProductList(ArrayList<HkdOTProductTO> products) {
    this.productList = products;
  }

  /**
   * @param reservationCode
   *            the reservationCode to set
   */
  public void setReservationCode(String reservationCode) {
    this.reservationCode = reservationCode;
  }

  /**
   * @param reservationData
   *            the reservationData to set
   */
  public void setReservationData(HkdOTReservationDataTO reservationData) {
    this.reservationData = reservationData;
  }

  /**
   * @param reservationId
   *            the reservationId to set
   */
  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  /**
   * @param reservationNote
   *            the reservationNote to set
   */
  public void setReservationNote(String reservationNote) {
    this.reservationNote = reservationNote;
  }

  /**
   * @param reservationUniqueId
   *            the reservationUniqueId to set
   */
  public void setReservationUniqueId(String reservationUniqueId) {
    this.reservationUniqueId = reservationUniqueId;
  }

  /**
   * @param sellerId
   *            the sellerId to set
   */
  public void setSellerId(Long sellerId) {
    this.sellerId = sellerId;
  }

  /**
   * @param siteNumber
   *            the siteNumber to set
   */
  public void setSiteNumber(Integer siteNumber) {
    this.siteNumber = siteNumber;
  }

  /**
   * @param tagsList
   *            the tagsList to set
   */
  public void setTagsList(ArrayList<String> tags) {
    this.tagsList = tags;
  }

  /**
   * @param taxExempt
   *            the taxExempt to set
   */
  public void setTaxExempt(Boolean taxExempt) {
    this.taxExempt = taxExempt;
  }

  /**
   * @param transactionCOD
   *            the transactionCOD to set
   */
  public void setTransactionCOD(String transactionCOD) {
    this.transactionCOD = transactionCOD;
  }

  /**
   * @return the error
   */
  public HkdOTErrorTO getError() {
    return error;
  }

  /**
   * @param error
   *            the error to set
   */
  public void setError(HkdOTErrorTO error) {
    this.error = error;
  }

  /**
   * @return the taxExemptCode
   */
  public String getTaxExemptCode() {
    return taxExemptCode;
  }

  /**
   * @param taxExemptCode
   *            the taxExemptCode to set
   */
  public void setTaxExemptCode(String taxExemptCode) {
    this.taxExemptCode = taxExemptCode;
  }

  /**
   * @return the ArrayList of Omni Ticket Ticket Info Transfer Objects
   */
  public ArrayList<HkdOTTicketInfoTO> getTicketInfoList() {
    return ticketInfoList;
  }

  /**
   * @return the Omni Ticket Transaction DSSN Transfer Object
   */
  public HkdOTTransactionDSSNTO getTransactionDSSN() {
    return transactionDSSN;
  }

  /**
   * @param transactionDSSN
   *            the Omni Ticket Transaction DSSN Transfer Object to set
   */
  public void setTransactionDSSN(HkdOTTransactionDSSNTO transactionDSSN) {
    this.transactionDSSN = transactionDSSN;
  }

  /**
   * @param transactionTime
   *            The string version of the transaction time to set
   */
  public void setTransactionTime(String transactionTime) {
    this.transactionTime = transactionTime;
  }

  /**
   * @return the total amount value for this object
   */
  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  /**
   * @param totalAmount
   *            the total amount value to set
   */
  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  /**
   * @return the total tax value for this object.
   */
  public BigDecimal getTotalTax() {
    return totalTax;
  }

  /**
   * @param totalTax
   *            the total tax amount value to set
   */
  public void setTotalTax(BigDecimal totalTax) {
    this.totalTax = totalTax;
  }

  /**
   * Gets the association info.
   * @return
   */
  public HkdOTAssociationInfoTO getAssociationInfo() {
    return associationInfo;
  }

  /**
   * Sets the association info
   * @param associationInfo
   */
  public void setAssociationInfo(HkdOTAssociationInfoTO associationInfo) {
    this.associationInfo = associationInfo;
  }

  /**
   * Gets the note details array
   * @return
   */
  public ArrayList<String> getNoteDetailsArray() {
    return noteDetailsArray;
  }

  /**
   * Set note details array
   * @param noteDetailsArray
   */
  public void setNoteDetailsArray(ArrayList<String> noteDetailsArray) {
    this.noteDetailsArray = noteDetailsArray;
  }

  /**
   * @param entitlementAccountCreationTypology
   *            the entitlementAccountCreationTypology to set
   */
  public void setEntitlementAccountCreationTypology(
      String entitlementAccountCreationTypology) {
    this.entitlementAccountCreationTypology = entitlementAccountCreationTypology;
  }

  /**
   * @return the entitlementAccountCreationTypology
   */
  public String getEntitlementAccountCreationTypology() {
    return entitlementAccountCreationTypology;
  }

  /**
   * @return the accountsData
   */
  public ArrayList<HkdOTAccountDataTO> getAccountsData() {
    return accountsData;
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

  /**
   * @return the transactionNote
   */
  public String getTransactionNote() {
    return transactionNote;
  }

  /**
   * @param transactionNote
   *            the transactionNote to set
   */
  public void setTransactionNote(String transactionNote) {
    this.transactionNote = transactionNote;
  }

  /**
   * @return the externalTransactionID
   */
  public HkdOTExternalTransactionIDTO getExternalTransactionID() {
    return externalTransactionID;
  }

  /**
   * @param externalTransactionID the externalTransactionID to set
   */
  public void setExternalTransactionID(HkdOTExternalTransactionIDTO externalTransactionID) {
    this.externalTransactionID = externalTransactionID;
  }

  /**
   * @return the showData
   */
  public HkdOTShowDataTO getShowData() {
    return showData;
  }

  /**
   * @param showData the showData to set
   */
  public void setShowData(HkdOTShowDataTO showData) {
    this.showData = showData;
  }

}
