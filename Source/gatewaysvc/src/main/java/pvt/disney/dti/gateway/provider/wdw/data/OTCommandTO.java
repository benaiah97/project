package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;

/**
 * This class is the Omni Ticket Command transfer object.
 * 
 * @author lewit019
 * 
 */
public class OTCommandTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  /** Enumeration of the different types of Omni Ticket transactions. */
  public enum OTTransactionType {
    CREATETRANSACTION,
    MANAGERESERVATION,
    QUERYTICKET,
    UPDATETICKET,
    UPDATETRANSACTION,
    VOIDTICKET,
    UPGRADETICKET,
    RENEWTICKET,
    MULTIENTITLEMENTACCOUNT,
    UNDEFINED
  };

  /** The transaction type of this Omni Ticket transaction. */
  private OTTransactionType txnType = OTTransactionType.UNDEFINED;

  /**
   * The constant value of the year (1980) that selects which century Omni's 2 digit dates are placed in.
   */
  public final static int OMNI_CENTURY_YEAR = 1980;

  /** The value to be used in the schema instance section of the XML. */
  private String xmlSchemaInstance;

  /** The value to be used int he no namespace schema location of the XML. */
  private String noNamespaceSchemaLocation;

  /** The Omni Ticket Header Transfer Object. */
  private OTHeaderTO headerTO;

  /** The Omni Ticket Void Ticket Transfer Object. */
  private OTVoidTicketTO voidTicketTO;

  /** The Omni Ticket Query Ticket Transfer Object. */
  private OTQueryTicketTO queryTicketTO;

  /** The Omni Ticket Upgrade Ticket Transfer Object. */
  private OTUpgradeTicketTO upgradeTicketTO;

  /** The Omni Ticket Manage Reservation Transfer Object. */
  private OTManageReservationTO manageReservationTO;

  /** The Omni Ticket Update Ticket Transfer Object. */
  private OTUpdateTicketTO updateTicketTO;

  /** The Omni Ticket Create Transaction Transfer Object. */
  private OTCreateTransactionTO createTransactionTO;

  /** The Omni Ticket Update Transactions Transfer Object. */
  private OTUpdateTransactionTO updateTransactionTO;

  /** The Omni Ticket MultiEntitlementAccount transfer Object */
  private OTMultiEntitlementAccountTO multiEntitlementAccountTO;

  /** The Omni Ticket Update Transactions Transfer Object. */
  private OTRenewTicketTO renewTicketTO;

  /**
   * Instantiates a new Omni Ticket Command Transfer Object.
   * 
   * @param txnTypeIn
   *            the transaction type
   */
  public OTCommandTO(OTTransactionType txnTypeIn) {
    txnType = txnTypeIn;
  }

  /**
   * @return the createTransactionTO
   */
  public OTCreateTransactionTO getCreateTransactionTO() {
    return createTransactionTO;
  }

  /**
   * @return the headerTO
   */
  public OTHeaderTO getHeaderTO() {
    return headerTO;
  }

  /**
   * @return the manageReservationTO
   */
  public OTManageReservationTO getManageReservationTO() {
    return manageReservationTO;
  }

  /**
   * @return the queryTicketTO
   */
  public OTQueryTicketTO getQueryTicketTO() {
    return queryTicketTO;
  }

  /**
   * @return the updateTicketTO
   */
  public OTUpdateTicketTO getUpdateTicketTO() {
    return updateTicketTO;
  }

  /**
   * @return the updateTransactionTO
   */
  public OTUpdateTransactionTO getUpdateTransactionTO() {
    return updateTransactionTO;
  }

  /**
   * @return the upgradeTicketTO
   */
  public OTUpgradeTicketTO getUpgradeTicketTO() {
    return upgradeTicketTO;
  }

  /**
   * @return the voidTicketTO
   */
  public OTVoidTicketTO getVoidTicketTO() {
    return voidTicketTO;
  }

  /**
   * @param createTransactionTO
   *            the createTransactionTO to set
   */
  public void setCreateTransactionTO(OTCreateTransactionTO createTransactionTO) {
    this.createTransactionTO = createTransactionTO;
  }

  /**
   * @param headerTO
   *            the headerTO to set
   */
  public void setHeaderTO(OTHeaderTO headerTO) {
    this.headerTO = headerTO;
  }

  /**
   * @param manageReservationTO
   *            the manageReservationTO to set
   */
  public void setManageReservationTO(OTManageReservationTO manageReservationTO) {
    this.manageReservationTO = manageReservationTO;
  }

  /**
   * @param queryTicketTO
   *            the queryTicketTO to set
   */
  public void setQueryTicketTO(OTQueryTicketTO queryTicketTO) {
    this.queryTicketTO = queryTicketTO;
  }

  /**
   * @param updateTicketTO
   *            the updateTicketTO to set
   */
  public void setUpdateTicketTO(OTUpdateTicketTO updateTicketTO) {
    this.updateTicketTO = updateTicketTO;
  }

  /**
   * @param updateTransactionTO
   *            the updateTransactionTO to set
   */
  public void setUpdateTransactionTO(OTUpdateTransactionTO updateTransactionTO) {
    this.updateTransactionTO = updateTransactionTO;
  }

  /**
   * @param upgradeTicketTO
   *            the upgradeTicketTO to set
   */
  public void setUpgradeTicketTO(OTUpgradeTicketTO upgradeTicketTO) {
    this.upgradeTicketTO = upgradeTicketTO;
  }

  /**
   * @param voidTicketTO
   *            the voidTicketTO to set
   */
  public void setVoidTicketTO(OTVoidTicketTO voidTicketTO) {
    this.voidTicketTO = voidTicketTO;
  }

  /**
   * @return the txnType
   */
  public OTTransactionType getTxnType() {
    return txnType;
  }

  /**
   * @param txnType
   *            the txnType to set
   */
  public void setTxnType(OTTransactionType txnType) {
    this.txnType = txnType;
  }

  /**
   * @return the no name space schema location string.
   */
  public String getNoNamespaceSchemaLocation() {
    return noNamespaceSchemaLocation;
  }

  /**
   * 
   * @param noNamespaceSchemeLocation
   *            string to set for this object
   */
  public void setNoNamespaceSchemaLocation(String noNamespaceSchemeLocation) {
    this.noNamespaceSchemaLocation = noNamespaceSchemeLocation;
  }

  /**
   * 
   * @return the XML schema instance string value of this object.
   */
  public String getXmlSchemaInstance() {
    return xmlSchemaInstance;
  }

  /**
   * @param xml
   *            schema instance string to set for this object.
   */
  public void setXmlSchemaInstance(String xmlSchemaInstance) {
    this.xmlSchemaInstance = xmlSchemaInstance;
  }

  public OTMultiEntitlementAccountTO getMultiEntitlementAccountTO() {
    return multiEntitlementAccountTO;
  }

  public void setMultiEntitlementAccountTO(
      OTMultiEntitlementAccountTO multiEntitlementAccountTO) {
    this.multiEntitlementAccountTO = multiEntitlementAccountTO;
  }

  /**
   * @return The Omni Ticket Error Transfer Object for this object
   */
  public OTErrorTO getErrorTO() {

    OTErrorTO otErrorTO = null;

    switch (txnType) {

    case CREATETRANSACTION:
      otErrorTO = createTransactionTO.getError();
      break;
    case MANAGERESERVATION:
      otErrorTO = manageReservationTO.getError();
      break;
    case QUERYTICKET:
      otErrorTO = queryTicketTO.getError();
      break;
    case UPDATETRANSACTION:
      otErrorTO = updateTransactionTO.getError();
      break;
    case UPDATETICKET:
      otErrorTO = updateTicketTO.getError();
      break;
    case UPGRADETICKET:
      otErrorTO = upgradeTicketTO.getError();
      break;
    case VOIDTICKET:
      otErrorTO = voidTicketTO.getError();
      break;
    case MULTIENTITLEMENTACCOUNT:
      otErrorTO = multiEntitlementAccountTO.getError();
      break;

    case RENEWTICKET:
      otErrorTO = renewTicketTO.getError();
      break;

    default:
      break;
    }
    return otErrorTO;
  }

  /**
   * Determines if the OTCommandTO object has the correct body object assigned for its type.
   * 
   * @return true if the object has the right type of body associated with it.
   */
  public boolean hasBodyObject() {

    boolean hasBody = false;

    switch (txnType) {

    case CREATETRANSACTION:
      if (createTransactionTO == null) hasBody = false;
      else hasBody = true;
      break;
    case MANAGERESERVATION:
      if (manageReservationTO == null) hasBody = false;
      else hasBody = true;
      break;
    case QUERYTICKET:
      if (queryTicketTO == null) hasBody = false;
      else hasBody = true;
      break;
    case UPDATETRANSACTION:
      if (updateTransactionTO == null) hasBody = false;
      else hasBody = true;
      break;
    case UPDATETICKET:
      if (updateTicketTO == null) hasBody = false;
      else hasBody = true;
      break;
    case UPGRADETICKET:
      if (upgradeTicketTO == null) hasBody = false;
      else hasBody = true;
      break;
    case VOIDTICKET:
      if (voidTicketTO == null) hasBody = false;
      else hasBody = true;
      break;
    case MULTIENTITLEMENTACCOUNT:
      if (multiEntitlementAccountTO == null) {
        hasBody = false;
      }
      else {
        hasBody = true;
      }
      break;

    case RENEWTICKET:
      if (renewTicketTO == null) hasBody = false;
      else hasBody = true;
      break;

    default:
      hasBody = false;
      break;

    }

    return hasBody;

  }

  /**
   * Converts an Omni date (in the format of "yy-MM-dd") into a GregorianCalendar. Makes a correction to the parsed year based on the "OMNI_CENTURY_YEAR".
   * 
   * @param stringDate
   *            the String version of the date.
   * @return GregorianCalendar version of the date.
   */
  public static GregorianCalendar convertOmniYYDate(String stringDate) throws ParseException {

    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    Date tempDate = sdf.parse(stringDate);

    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(tempDate);

    /*
    if (gc.get(Calendar.YEAR) < OTCommandTO.OMNI_CENTURY_YEAR) {
      gc.set(Calendar.YEAR, gc.get(Calendar.YEAR) + 100);
    }
    */
    
    return gc;

  }

  /**
   * Converts an Omni date (in the format of "yy-MM-dd") into a GregorianCalendar. Makes a correction to the parsed year based on the "OMNI_CENTURY_YEAR".
   * 
   * @param stringDate
   *            the String version of the date.
   * @return GregorianCalendar version of the date.
   */
  public static GregorianCalendar convertTicketDOB(String stringDate) throws ParseException {

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    Date tempDate = sdf.parse(stringDate);

    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(tempDate);

    /*
    if (gc.get(Calendar.YEAR) < OTCommandTO.OMNI_CENTURY_YEAR) {
      gc.set(Calendar.YEAR, gc.get(Calendar.YEAR) + 100);
    }
    */

    return gc;

  }

  /**
   * @return the renewTicketTO
   */
  public OTRenewTicketTO getRenewTicketTO() {
    return renewTicketTO;
  }

  /**
   * @param renewTicketTO
   *            the renewTicketTO to set
   */
  public void setRenewTicketTO(OTRenewTicketTO renewTicketTO) {
    this.renewTicketTO = renewTicketTO;
  }

}
