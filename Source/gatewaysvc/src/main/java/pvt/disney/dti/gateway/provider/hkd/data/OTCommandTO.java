package pvt.disney.dti.gateway.provider.hkd.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.hkd.data.common.OTErrorTO;

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
    MANAGERESERVATION,
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

  /** The value to be used in the no namespace schema location of the XML. */
  private String noNamespaceSchemaLocation;

  /** The Omni Ticket Header Transfer Object. */
  private OTHeaderTO headerTO;

  /** The Omni Ticket Manage Reservation Transfer Object. */
  private OTManageReservationTO manageReservationTO;

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

  /**
   * @return The Omni Ticket Error Transfer Object for this object
   */
  public OTErrorTO getErrorTO() {

    OTErrorTO otErrorTO = null;

    switch (txnType) {

    case MANAGERESERVATION:
      otErrorTO = manageReservationTO.getError();
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

    case MANAGERESERVATION:
      if (manageReservationTO == null) hasBody = false;
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

}
