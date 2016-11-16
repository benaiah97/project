package pvt.disney.dti.gateway.controller;

import java.util.HashMap;
import java.util.Properties;

import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.util.flood.FloodControlInitException;
import pvt.disney.dti.gateway.util.flood.KeyDerivationException;
import pvt.disney.dti.gateway.util.flood.KeyMatchFloodControl;

/**
 * Singleton class whose primary responsibility is to implement the abstract
 * deriveKey() method of KeyMatchFloodControl.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class DTIFloodControl extends KeyMatchFloodControl {

  private final static String CREATETICKETSTRING       = "CT"; // CMD 1
  private final static String UPGRADEALPHASTRING       = "UA"; // CMD 2
  private final static String UPDATETICKETSTRING       = "UT"; // CMD 3
  private final static String UPDATETXNSTRING          = "UX"; // CMD 4  
  private final static String VOIDTICKETSTRING         = "VT"; // CMD 5
  private final static String QUERYTICKETSTRING        = "QT"; // CMD 6
  //private final static String ECHOSTRING               = "EC"; // CMD 7
  //private final static String QUERYTXNSTRING           = "QX"; // CMD 8  
  private final static String RESERVATIONSTRING        = "RR"; // CMD 9
  private final static String QUERYRESERVATIONSTRING   = "QR"; // CMD 10
  private final static String UPGRADEENTITLEMENTSTRING = "UE"; // CMD 11
  private final static String RENEWENTTLSTRING         = "RE"; // CMD 12
  private final static String ASSOCMEDIA2ACCTSTRING    = "AM"; // CMD 13
  private final static String TICKERATEENTTLSTRING     = "TE"; // CMD 14
  private final static String VOIDRESERVATIONSTRING    = "VR"; // CMD 15
  

  /** Object holder for the singleton pattern. */
  static DTIFloodControl obj = null;

  /**
   * Private constructor for the singleton pattern.
   * 
   * @param props
   *          Flood control parameters. Property
   *          "DtiApp.FloodControlExceptionTsLoc" is required in addition to the
   *          properties values defined in KeyMatchFloodControl.
   * @see pvt.disney.dti.gateway.util.flood.KeyMatchFloodControl
   * @throws FloodControlInitException
   */
  private DTIFloodControl(Properties props) throws FloodControlInitException {
    super(props);
  }

  /**
   * Used to get the singleton instance of the DTIFloodControl. If one doesn't
   * exist, creates one.
   * 
   * @param props
   *          Flood control parameters. Property
   *          "DtiApp.FloodControlExceptionTsLoc" is required in addition to the
   *          properties values defined in KeyMatchFloodControl.
   * @return an instance of DTIFloodControl
   * @throws FloodControlInitException
   */
  public static DTIFloodControl getInstance(Properties props) throws FloodControlInitException {

    if (obj == null) {
      obj = new DTIFloodControl(props);
    }

    return obj;
  }

  // @Override
  /**
   * Composes a key out of a two character transaction type string + tsMac +
   * tsLocation plus either all product codes on the order (reservation or
   * upgrade) or the tickets on the request (in the case of query or void
   * ticket).
   * 
   * @param txn
   *          A HashMap containing the TransactionType, TSMAC, TSLocation,
   *          Products, and/or Tickets.
   * @return an object which uniquely identifies this transaction to flood
   *         control.
   * @throws KeyDerivationException
   *           should it be passed something from which it can't derive a key.
   * 
   */
  @SuppressWarnings("rawtypes")
  public Object deriveKey(Object txn) throws KeyDerivationException {

    HashMap txnMap = null;

    // Ensure the object passed in is correct.
    try {
      txnMap = (HashMap) txn;
    } catch (ClassCastException cce) {
      throw new KeyDerivationException("Cannot cast txn to HashMap for flood control. "
          + cce.toString());
    }

    StringBuffer key = new StringBuffer();

    // Determine if this is one of the transactions to be bypassed by
    // flood control. If so, return null.
    TransactionType txnType = (TransactionType) txnMap.get(TiXMLHandler.TXN_TYPE);

    if ((txnType == TransactionType.CREATETICKET) || (txnType == TransactionType.UPDATETICKET)
        || (txnType == TransactionType.UPDATETRANSACTION) || txnType == TransactionType.UNDEFINED) {
      return null; // effectively excludes this transaction from flood control
    }

    // Get the key component values standard to every transaction type
    String txnTypeString = null;

    switch (txnType) {
    case CREATETICKET: 
      txnTypeString = CREATETICKETSTRING;
      break;
    case UPGRADEALPHA:
      txnTypeString = UPGRADEALPHASTRING;
      break;
    case UPDATETICKET:
      txnTypeString = UPDATETICKETSTRING;
      break;
    case UPDATETRANSACTION:
      txnTypeString = UPDATETXNSTRING;
      break;
    case VOIDTICKET:
      txnTypeString = VOIDTICKETSTRING;
      break;
    case QUERYTICKET:
      txnTypeString = QUERYTICKETSTRING;
      break;
    case RESERVATION:
      txnTypeString = RESERVATIONSTRING;
      break;
    case QUERYRESERVATION:
      txnTypeString = QUERYRESERVATIONSTRING;
      break;
    case UPGRADEENTITLEMENT:  // 2.10
      txnTypeString = UPGRADEENTITLEMENTSTRING;
      break;
    case RENEWENTITLEMENT:
      txnTypeString = RENEWENTTLSTRING;
      break;
    case ASSOCIATEMEDIATOACCOUNT:
      txnTypeString = ASSOCMEDIA2ACCTSTRING;
      break;
    case TICKERATEENTITLEMENT:
      txnTypeString = TICKERATEENTTLSTRING;
      break;
    case VOIDRESERVATION:
      txnTypeString = VOIDRESERVATIONSTRING;
      break;
    default:
      return null; // Shouldn't be able to get here, but still fail gracefully.
    }

    // Get tsMac and tsLocation
    String tsMac = (String) txnMap.get(TiXMLHandler.TS_MAC);
    String tsLocation = (String) txnMap.get(TiXMLHandler.TS_LOCATION);
    if ((tsMac == null) || (tsLocation == null)) {
      return null;
    }

    // Determine if the tsLocation is on the exception list. If so, return null.
    String exceptionTsLocs = (String) props.get(PropertyName.FLOOD_CONTROL_EXCEPTION_TSLOC);
    String[] exceptions = exceptionTsLocs.split(",");
    for (int i = 0; i < exceptions.length; i++) {
      if (tsLocation.equals(exceptions[i].trim()))
        return null;
    }

    // Assemble the common portion of the key.
    key.append(txnTypeString + tsMac + tsLocation);

    // If upgrade or reservation, gets all the products listed on the order
    if ((txnType == TransactionType.UPGRADEALPHA) || 
        (txnType == TransactionType.RESERVATION)  ||
        (txnType == TransactionType.CREATETICKET) ||
        (txnType == TransactionType.UPGRADEENTITLEMENT) ||
        (txnType == TransactionType.RENEWENTITLEMENT)) {

      String value = (String) txnMap.get(TiXMLHandler.PRODUCTS);
      if (value == null) {
        return null;
      }
      key.append(value);
    }

    // If void or query, get all tickets listed.
    if ((txnType == TransactionType.VOIDTICKET) || 
        (txnType == TransactionType.QUERYTICKET) ||
        (txnType == TransactionType.TICKERATEENTITLEMENT)) {
      String value = (String) txnMap.get(TiXMLHandler.TICKETS);
      if (value == null) {
        return null;
      }
      key.append(value);
    }
    
    // If query reservation, get all of the reservation codes listed.
    if ((txnType == TransactionType.QUERYRESERVATION) ||
        (txnType == TransactionType.VOIDRESERVATION)) {
      String value = (String) txnMap.get(TiXMLHandler.RESCODES);
      if (value == null) {
        return null;
      }
      key.append(value);
    }
    
    // If associate media, get all of the media IDs listed.
    if ((txnType == TransactionType.ASSOCIATEMEDIATOACCOUNT)) {
      String value = (String) txnMap.get(TiXMLHandler.VISUALMEDIAIDS);
      if (value == null) {
        return null;
      }
      key.append(value);
    }

    return key.toString();
  }

}
