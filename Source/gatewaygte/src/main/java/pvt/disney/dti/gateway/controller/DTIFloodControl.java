package pvt.disney.dti.gateway.controller;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.util.flood.KeyDerivationException;
import pvt.disney.dti.gateway.util.flood.KeyMatchFloodControl;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * Singleton class whose primary responsibility is to implement the abstract
 * deriveKey() method of KeyMatchFloodControl.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class DTIFloodControl extends KeyMatchFloodControl {

   /** The standard core logging mechanism. */
  private static EventLogger eventLogger = EventLogger.getLogger(DTIFloodControl.class);
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
  private final static String QUERYELIGPRODUCTSSTRING    = "QP"; // CMD 16

   // When were the flood properties read.
   private static long propertiesNextRead = new Date().getTime();

   private static int propertyRefreshInterval = 0;

  
   /** Object holder for the singleton pattern. */
   static DTIFloodControl floodControlInstance = null;

   /**
    * Private constructor for the singleton pattern. Instantiates a new DTI
    * flood control.
    */
   private DTIFloodControl() {
      super();
   }


   /**
    * Used to get the singleton instance of the DTIFloodControl. If instance
    * doesn't exist, creates one, if instance already exists, check whether the
    * instance needs to be refreshed
    * Flood Block will need to pass in the value of “0” for tpoId. 
    * That value will be used for property values that are not campus specific.
    * 
    * @param application the application
    * @param tpoId the tpo id
    * @param environment the environment
    * @return single instance of DTIFloodControl
    */
   public static DTIFloodControl getInstance(String application, Integer tpoId, String environment) {

      eventLogger.sendEvent("Execution of DTIFloodControl.getInstance(application, environment) by "
               + Thread.currentThread().getId() + ": " + Thread.currentThread().getName(), EventType.DEBUG, DTIFloodControl.class);

      // Synchronize object creation - if check and object creation both need to be part of the same block
      synchronized (DTIFloodControl.class) {
            if (floodControlInstance == null) {

               // Create singleton instance
               floodControlInstance = new DTIFloodControl();

               // To refresh/overlap the flood block properties from DB.
               floodControlInstance.getRefreshPropertyFromDB(application, tpoId, environment);

               // Set time for next DB refresh
               setNextRefreshTime();

               eventLogger.sendEvent("DTIFloodControl instance created by thread: " + Thread.currentThread().getId()
                     + ": " + Thread.currentThread().getName(), EventType.DEBUG, DTIFloodControl.class);

               return floodControlInstance;
            }
      } // End of synchronized block

      // Synchronize check for whether refresh is required and updating the variable to the next refresh time.
      synchronized (DTIFloodControl.class) {
         
         // Check whether current time is greater than next read time stamp,
         // if it is - update refresh time stamp and exit synchronization block in order to avoid other threads from waiting on the actual DB
         // Property file refresh
         if (new Date().getTime() >= propertiesNextRead) {

              // set time for next DB refresh
              setNextRefreshTime();

              eventLogger.sendEvent("DTIFloodControl determined that refresh is required by thread: "
                     + Thread.currentThread().getId() + ": " + Thread.currentThread().getName(), EventType.DEBUG,
                     DTIFloodControl.class);
         } else {
            return floodControlInstance;
         }
      } // End of synchronized block

      // We can now refresh the DB properties by this thread and not having to worry about other threads waiting
      floodControlInstance.getRefreshPropertyFromDB(application, tpoId, environment);

      eventLogger.sendEvent("DTIFloodControl DB Property refresh completed by thread: "
               + Thread.currentThread().getId() + ": " + Thread.currentThread().getName(), EventType.DEBUG, DTIFloodControl.class);

      return floodControlInstance;
   }

 
  
   /**
    * This method compares the current time to last read value. If the
    * difference is more than or equal to the propertyRefreshInterval then it
    * returns true, indicating the refresh on flood block properties is
    * required.
    * 
    * Sets the next refresh time.
    */
   private static void setNextRefreshTime() {
      
      // Calculating the interval based on the value from DB.
      propertyRefreshInterval = floodControlInstance.getFloodBlockPropRefresh() * 1000;

      // Initializing the propertiesNextRead value.
      propertiesNextRead = new Date().getTime() + propertyRefreshInterval;
   }

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

      FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();

      if ((txnType == TransactionType.UPDATETICKET) || (txnType == TransactionType.UPDATETRANSACTION)
               || txnType == TransactionType.UNDEFINED) {
      return null; // effectively excludes this transaction from flood control
    }

      /*
       * Get the key component values standard to every transaction type &
       * Setting the TransactionType in the floodMatchSignatureTo
       */
      String txnTypeString = null;

      switch (txnType) {

      // CREATETICKET
      case CREATETICKET:
         floodMatchSignatureTO.setTransactionType(TransactionType.CREATETICKET);
         txnTypeString = CREATETICKETSTRING;
         break;

      // UPGRADEALPHA
      case UPGRADEALPHA:
         floodMatchSignatureTO.setTransactionType(TransactionType.UPGRADEALPHA);
         txnTypeString = UPGRADEALPHASTRING;
         break;

      // UPDATETICKET
      case UPDATETICKET:
         floodMatchSignatureTO.setTransactionType(TransactionType.UPDATETICKET);
         txnTypeString = UPDATETICKETSTRING;
         break;

      // UPDATETRANSACTION
      case UPDATETRANSACTION:
         floodMatchSignatureTO.setTransactionType(TransactionType.UPDATETRANSACTION);
         txnTypeString = UPDATETXNSTRING;
         break;

      // VOIDTICKET
      case VOIDTICKET:
         floodMatchSignatureTO.setTransactionType(TransactionType.VOIDTICKET);
         txnTypeString = VOIDTICKETSTRING;
         break;

      // QUERYTICKET
      case QUERYTICKET:
         floodMatchSignatureTO.setTransactionType(TransactionType.QUERYTICKET);
         txnTypeString = QUERYTICKETSTRING;
         break;

      // RESERVATION
      case RESERVATION:
         floodMatchSignatureTO.setTransactionType(TransactionType.RESERVATION);
         txnTypeString = RESERVATIONSTRING;
         break;

      // QUERYRESERVATION
      case QUERYRESERVATION:
         floodMatchSignatureTO.setTransactionType(TransactionType.QUERYRESERVATION);
         txnTypeString = QUERYRESERVATIONSTRING;
         break;

      // UPGRADEENTITLEMENT
      case UPGRADEENTITLEMENT: // 2.10
         floodMatchSignatureTO.setTransactionType(TransactionType.UPGRADEENTITLEMENT);
         txnTypeString = UPGRADEENTITLEMENTSTRING;
         break;

      // RENEWENTITLEMENT
      case RENEWENTITLEMENT:
         floodMatchSignatureTO.setTransactionType(TransactionType.RENEWENTITLEMENT);
         txnTypeString = RENEWENTTLSTRING;
         break;

      // ASSOCIATEMEDIATOACCOUNT
      case ASSOCIATEMEDIATOACCOUNT:
         floodMatchSignatureTO.setTransactionType(TransactionType.ASSOCIATEMEDIATOACCOUNT);
         txnTypeString = ASSOCMEDIA2ACCTSTRING;
         break;

      // TICKERATEENTITLEMENT
      case TICKERATEENTITLEMENT:
         floodMatchSignatureTO.setTransactionType(TransactionType.TICKERATEENTITLEMENT);
         txnTypeString = TICKERATEENTTLSTRING;
         break;

      // VOIDRESERVATION
      case VOIDRESERVATION:
         floodMatchSignatureTO.setTransactionType(TransactionType.VOIDRESERVATION);
         txnTypeString = VOIDRESERVATIONSTRING;
         break;

      // QUERYELIGPRODUCTS
      case QUERYELIGPRODUCTS:
         floodMatchSignatureTO.setTransactionType(TransactionType.QUERYELIGPRODUCTS);
         txnTypeString = QUERYELIGPRODUCTSSTRING;
         break;
   	
    default:
      return null; // Shouldn't be able to get here, but still fail gracefully.
    }

      // tsMac
      String tsMac = (String) txnMap.get(TiXMLHandler.TS_MAC);
      
      // tsLocation
      String tsLocation = (String) txnMap.get(TiXMLHandler.TS_LOCATION);
      
      // target
      String target = (String) txnMap.get(TiXMLHandler.TS_ENVIRONMENT);

      // if any of the entity Null , exit
      if ((tsMac == null) || (tsLocation == null) || (target == null)) {
         return null;
      }

      // Determine if the tsLocation is on the exception list. If so, return null.
      String exceptionTsLocs = this.getFloodControlExceptionTsLoc();
    String[] exceptions = exceptionTsLocs.split(",");
    for (int i = 0; i < exceptions.length; i++) {
      if (tsLocation.equals(exceptions[i].trim()))
        return null;
    }

      // Appending the common portion of txn in the key.
      key.append(txnTypeString + target + tsMac + tsLocation);

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

      if ((txnType == TransactionType.RESERVATION) || (txnType == TransactionType.UPGRADEENTITLEMENT)
               || (txnType == TransactionType.RENEWENTITLEMENT)) {
         
         // FirstName
         String billFirstName = (String) txnMap.get(TiXMLHandler.FIRSTNAME);
         
         // LastName
         String billLastName = (String) txnMap.get(TiXMLHandler.LASTNAME);
         
         // if the Guest Demo is not Null, Appending the firstName and lastName in the key.
         if ((StringUtils.isNotBlank(billFirstName)) && (StringUtils.isNotBlank(billLastName))) {
            
            key.append(billFirstName);
            key.append(billLastName);
         }
      }

    // If void or query, get all tickets listed.
    if ((txnType == TransactionType.VOIDTICKET) || 
        (txnType == TransactionType.QUERYTICKET) ||
        (txnType == TransactionType.QUERYELIGPRODUCTS) ||
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

      // Setting the ProviderType of the txn in the FloodSignatureTO
      floodMatchSignatureTO.setProviderType(target);
      
      // Setting the Signature of the txn in the FloodSignatureTO
      floodMatchSignatureTO.setSignature(key.toString());

      return floodMatchSignatureTO;
   }

}
