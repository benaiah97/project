package pvt.disney.dti.gateway.util.flood;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.PropertyKey;
import pvt.disney.dti.gateway.data.common.FloodControlPropertyTO;
import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.data.common.PropertyTO;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

// TODO: Auto-generated Javadoc
/**
 * This class utilizes a "key match" method for flood control. Simply stated, a
 * key is an object of any type that will be compared against new transactions
 * to see if there is a match. All keys are stored in a key store (type defined
 * by configuration). This class must not only control the rules of flood
 * control, it must also make sure that, periodically or continually
 * (configurable) that the key store is cleaned up.
 * 
 * @author lewit019
 * 
 */
public abstract class KeyMatchFloodControl {

   /** The event logger. */
   private EventLogger eventLogger = EventLogger.getLogger(this.getClass());
   // Configuration values
   /** Is flood control active. If not, it will simply return. Set via method. */
   private volatile boolean floodControlActive = true;
   /**
    * The window of time in which key occurrences are subject to evaluation.
    * Property.
    */
   private volatile int keyFrequencyWindow = 60;
   /**
    * The number of times in which a key can re-occur without being subject to
    * blocking or suppression. Property.
    */
   private volatile int keyFrequencyLimit = 6;
   /**
    * Determines if a key block exception will be used the first time a key
    * exceeds its limit of occurrences. Property.
    */
   private volatile boolean keyBlockException = true;

   /** The key block exception string. */
   private volatile String keyBlockExceptionString = "true";
   /**
    * The amount of time (in seconds) that a flood controlled key will be
    * suppressed. Property.
    */
   private volatile int keySuppressInterval = 90;
   /**
    * The maximum number of concurrent keys allowed to exist at one time.
    * Property.
    */
   private volatile int maxConcurrentKeys = 1500;

   /** The window of time in which the flood block properties are refreshed. */
   private volatile int floodBlockPropRefresh = 600;

   /** The flood control exception ts loc. */
   private volatile String floodControlExceptionTsLoc = "VoidStore";

   /**
    * The period of time, in seconds, that the key store will be reFreshed (for
    * clean-up). Property.
    */
   private volatile int cacheRefreshInterval = 5;
   /** Date/Time of the last cache (key store) reFresh. */
   private static Date lastCacheReFresh = new Date();
   /** Date/Time of the last task reFresh. */
   private static Date lastTaskReFresh = new Date();
   /** Properties used to control KeyMatchFloodControl. */
   protected static Properties props;
   /** The flood control key store. */
   private FloodControlKeyStore keyStore;

   /** The Constant TESTWDW. */
   private static final  String TESTWDW = "Test-WDW";

   /** The Constant PRODWDW. */
   private static final  String PRODWDW = "prod-WDW";

   /** The Constant WDW. */
   private static final  String TEST = "Test";

   /** The Constant WDW. */
   private static final  String PROD = "Prod";

   /** The Constant TESTDLR. */
   private static final  String TESTDLR = "Test-DLR";

   /** The Constant PRODDLR. */
   private static final  String PRODDLR = "prod-DLR";

   /** The Constant TESTHKD. */
   private static final  String TESTHKD = "Test-HKD";

   /** The Constant PRODHKD. */
   private static final  String PRODHKD = "prod-HKD";

   /** The Constant FLOODBLOCK. */
   private static final  String FLOODBLOCK = "FLOOD_BLOCK";

   /** The Constant REQUIRED_PROPERTY_KEYS. */
   private static final  String REQUIRED_PROPERTY_KEYS[] = { "KEY_STORE_TYPE", "KEY_BLOCK_EXCEPTION",
            "KEY_FREQUENCY_WINDOW", "KEY_SUPPRESS_INTERVAL", "MAX_CONCURRENT_KEYS", "CACHE_REFRESH_INTERVAL",
            "FLOODBLOCK_PROP_REFRESH", "KEY_FREQUENCY_LIMIT_WDW_QT", "KEY_FREQUENCY_LIMIT_WDW_CT",
            "KEY_FREQUENCY_LIMIT_WDW_UA", "KEY_FREQUENCY_LIMIT_WDW_UT", "KEY_FREQUENCY_LIMIT_WDW_UX",
            "KEY_FREQUENCY_LIMIT_WDW_VT", "KEY_FREQUENCY_LIMIT_WDW_RR", "KEY_FREQUENCY_LIMIT_WDW_QR",
            "KEY_FREQUENCY_LIMIT_WDW_UE", "KEY_FREQUENCY_LIMIT_WDW_RE", "KEY_FREQUENCY_LIMIT_WDW_AM",
            "KEY_FREQUENCY_LIMIT_WDW_TE", "KEY_FREQUENCY_LIMIT_WDW_VR", "KEY_FREQUENCY_LIMIT_WDW_QP",
            "KEY_FREQUENCY_LIMIT_DLR_CT", "KEY_FREQUENCY_LIMIT_DLR_UA", "KEY_FREQUENCY_LIMIT_DLR_UT",
            "KEY_FREQUENCY_LIMIT_DLR_UX", "KEY_FREQUENCY_LIMIT_DLR_VT", "KEY_FREQUENCY_LIMIT_DLR_QT",
            "KEY_FREQUENCY_LIMIT_DLR_RR", "KEY_FREQUENCY_LIMIT_DLR_QR", "KEY_FREQUENCY_LIMIT_DLR_UE",
            "KEY_FREQUENCY_LIMIT_DLR_RE", "KEY_FREQUENCY_LIMIT_DLR_AM", "KEY_FREQUENCY_LIMIT_DLR_TE",
            "KEY_FREQUENCY_LIMIT_DLR_VR", "KEY_FREQUENCY_LIMIT_DLR_QP", "KEY_FREQUENCY_LIMIT_HKD_QT",
            "KEY_FREQUENCY_LIMIT_HKD_CT", "KEY_FREQUENCY_LIMIT_HKD_UA", "KEY_FREQUENCY_LIMIT_HKD_UT",
            "KEY_FREQUENCY_LIMIT_HKD_UX", "KEY_FREQUENCY_LIMIT_HKD_VT", "KEY_FREQUENCY_LIMIT_HKD_RR",
            "KEY_FREQUENCY_LIMIT_HKD_QR", "KEY_FREQUENCY_LIMIT_HKD_UE", "KEY_FREQUENCY_LIMIT_HKD_RE",
            "KEY_FREQUENCY_LIMIT_HKD_AM", "KEY_FREQUENCY_LIMIT_HKD_TE", "KEY_FREQUENCY_LIMIT_HKD_VR",
            "FLOOD_CONTROL_EXCEPTION_TSLOC" };

   /** The Key store type. */
   private volatile String KeyStoreType = "InMemory";

   /** The flood control property TO. */
   private FloodControlPropertyTO floodControlPropertyTO = new FloodControlPropertyTO();

   /** The keystoretype. */
   private static final  String KEY_STORE_TYPE = "KEY_STORE_TYPE";

   /** The keyblockexception. */
   private static final  String KEY_BLOCK_EXCEPTION = "KEY_BLOCK_EXCEPTION";

   /** The keyfrequencywindow. */
   private static final  String KEY_FREQUENCY_WINDOW = "KEY_FREQUENCY_WINDOW";

   /** The keysuppressinterval. */
   private static final  String KEY_SUPPRESS_INTERVAL = "KEY_SUPPRESS_INTERVAL";

   /** The maxconcurrentkeys. */
   private static final  String MAX_CONCURRENT_KEYS = "MAX_CONCURRENT_KEYS";

   /** The cacherefreshinterval. */
   private static final  String CACHE_REFRESH_INTERVAL = "CACHE_REFRESH_INTERVAL";

   /** The floodblock prop refresh. */
   private static final  String FLOODBLOCK_PROP_REFRESH = "FLOODBLOCK_PROP_REFRESH";

   /** The key frequency limit wdw qt. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_QT = "KEY_FREQUENCY_LIMIT_WDW_QT";

   /** The key frequency limit wdw ct. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_CT = "KEY_FREQUENCY_LIMIT_WDW_CT";

   /** The key frequency limit wdw ua. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_UA = "KEY_FREQUENCY_LIMIT_WDW_UA";

   /** The key frequency limit wdw ut. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_UT = "KEY_FREQUENCY_LIMIT_WDW_UT";

   /** The key frequency limit wdw ux. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_UX = "KEY_FREQUENCY_LIMIT_WDW_UX";

   /** The key frequency limit wdw vt. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_VT = "KEY_FREQUENCY_LIMIT_WDW_VT";

   /** The key frequency limit wdw rr. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_RR = "KEY_FREQUENCY_LIMIT_WDW_RR";

   /** The key frequency limit wdw qr. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_QR = "KEY_FREQUENCY_LIMIT_WDW_QR";

   /** The key frequency limit wdw ue. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_UE = "KEY_FREQUENCY_LIMIT_WDW_UE";

   /** The key frequency limit wdw re. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_RE = "KEY_FREQUENCY_LIMIT_WDW_RE";

   /** The key frequency limit wdw am. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_AM = "KEY_FREQUENCY_LIMIT_WDW_AM";

   /** The key frequency limit wdw te. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_TE = "KEY_FREQUENCY_LIMIT_WDW_TE";

   /** The key frequency limit wdw vr. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_VR = "KEY_FREQUENCY_LIMIT_WDW_VR";

   /** The key frequency limit wdw qp. */
   private static final  String KEY_FREQUENCY_LIMIT_WDW_QP = "KEY_FREQUENCY_LIMIT_WDW_QP";

   /** The key frequency limit dlr ct. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_CT = "KEY_FREQUENCY_LIMIT_DLR_CT";

   /** The key frequency limit dlr ua. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_UA = "KEY_FREQUENCY_LIMIT_DLR_UA";

   /** The key frequency limit dlr ut. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_UT = "KEY_FREQUENCY_LIMIT_DLR_UT";

   /** The key frequency limit dlr ux. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_UX = "KEY_FREQUENCY_LIMIT_DLR_UX";

   /** The key frequency limit dlr vt. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_VT = "KEY_FREQUENCY_LIMIT_DLR_VT";

   /** The key frequency limit dlr qt. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_QT = "KEY_FREQUENCY_LIMIT_DLR_QT";

   /** The key frequency limit dlr rr. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_RR = "KEY_FREQUENCY_LIMIT_DLR_RR";

   /** The key frequency limit dlr qr. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_QR = "KEY_FREQUENCY_LIMIT_DLR_QR";

   /** The key frequency limit dlr ue. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_UE = "KEY_FREQUENCY_LIMIT_DLR_UE";

   /** The key frequency limit dlr re. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_RE = "KEY_FREQUENCY_LIMIT_DLR_RE";

   /** The key frequency limit dlr am. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_AM = "KEY_FREQUENCY_LIMIT_DLR_AM";

   /** The key frequency limit dlr te. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_TE = "KEY_FREQUENCY_LIMIT_DLR_TE";

   /** The key frequency limit dlr vr. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_VR = "KEY_FREQUENCY_LIMIT_DLR_VR";

   /** The key frequency limit dlr qp. */
   private static final  String KEY_FREQUENCY_LIMIT_DLR_QP = "KEY_FREQUENCY_LIMIT_DLR_QP";

   /** The key frequency limit hkd qt. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_QT = "KEY_FREQUENCY_LIMIT_HKD_QT";

   /** The key frequency limit hkd ct. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_CT = "KEY_FREQUENCY_LIMIT_HKD_CT";

   /** The key frequency limit hkd ua. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_UA = "KEY_FREQUENCY_LIMIT_HKD_UA";

   /** The key frequency limit hkd ut. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_UT = "KEY_FREQUENCY_LIMIT_HKD_UT";

   /** The key frequency limit hkd ux. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_UX = "KEY_FREQUENCY_LIMIT_HKD_UX";

   /** The key frequency limit hkd vt. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_VT = "KEY_FREQUENCY_LIMIT_HKD_VT";

   /** The key frequency limit hkd rr. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_RR = "KEY_FREQUENCY_LIMIT_HKD_RR";

   /** The key frequency limit hkd qr. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_QR = "KEY_FREQUENCY_LIMIT_HKD_QR";

   /** The key frequency limit hkd ue. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_UE = "KEY_FREQUENCY_LIMIT_HKD_UE";

   /** The key frequency limit hkd re. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_RE = "KEY_FREQUENCY_LIMIT_HKD_RE";

   /** The key frequency limit hkd am. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_AM = "KEY_FREQUENCY_LIMIT_HKD_AM";

   /** The key frequency limit hkd te. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_TE = "KEY_FREQUENCY_LIMIT_HKD_TE";

   /** The key frequency limit hkd vr. */
   private static final  String KEY_FREQUENCY_LIMIT_HKD_VR = "KEY_FREQUENCY_LIMIT_HKD_VR";

   /** The flood control exception tsloc. */
   private static final  String FLOOD_CONTROL_EXCEPTION_TSLOC = "FLOOD_CONTROL_EXCEPTION_TSLOC";

   /**
    * Instantiates a new key match flood control.
    */
   public KeyMatchFloodControl() {

   }

   /**
    * Gets the refreshed property key, value from DB.
    * 
    * @param application
    *           the application
    * @param environment
    *           the environment
    * @return the refresh property from DB
    */
   public void getRefreshPropertyFromDB(String application, String environment) {

      try {

         // To get the propertyKey , propertyValue pair from database based on the application,environment and FLOOD_BLOCK
         List<PropertyTO> propertyList = PropertyKey.getProperties(application, environment, FLOODBLOCK);

         if ((propertyList != null) && propertyList.size() > 0) {

            eventLogger.sendEvent("List of flood Control Properties found in database of size: " + propertyList.size() + ".", EventType.DEBUG, this);

            for (PropertyTO propertyTO : propertyList) {

               // FloodPropertyKey
               String propertyKey = propertyTO.getPropSetKey();

               // FloodPropertyValue
               String propertyValue = propertyTO.getPropSetValue();

               floodControlPropertyTO.getRetrievedPropertyValuesfromDB().put(propertyKey, propertyValue);
            }

            // Setting up the property key Value Default from DB.
            setKeyPropertyValue();

         } else {
            eventLogger.sendEvent("Flood Control Properties are not defined in database.", EventType.WARN, this);
         }

      } catch (DTIException dtie) {

         eventLogger.sendEvent("Exception occured while fetching the property value from DB , inside method getRefreshPropertyFromDB "
                           + dtie.toString(), EventType.WARN, this);
      }
      
      // To get the keyStore
      getMemoryBasedKeyStore();
   }

   /**
    * Gets the memory based key store instance.
    * 
    * @return the memory based key store
    */
   private void getMemoryBasedKeyStore() {

      try {
         // If KeyStoretype is InMemory
         if (KeyStoreType.equals("InMemory")) {
            
            // If keyStore type is MEMORY , setting the maxConcurrentKeys to keyStore
            keyStore = FloodControlKeyStoreFactory.getInstance(maxConcurrentKeys, FloodControlKeyStore.Type.MEMORY);
         } else {
            keyStore = MemoryBasedKeyStore.getInstance(maxConcurrentKeys); // It's the only one available.
         }
      } catch (FloodControlInitException fcie) {

         eventLogger.sendEvent("Exception occured while setting the maxConcurrentKeys to keyStore." + fcie.toString(),
                  EventType.WARN, this);
      }
   }

   /**
    * Gets the numerical property value. 
    * It will check, if the retrieved value
    * from DB is numeric .
    * 
    * @param keyProperty
    *           the key property
    * @param propertyValFromDB
    *           the numeric property value
    * @return the numerical property value
    * @throws NumberFormatException
    *            the number format exception
    */
   private Integer getNumericalPropertyValue(String keyProperty, String propertyValFromDB) throws NumberFormatException {
      Integer propertyValue = null;

      if (StringUtils.isNumeric(propertyValFromDB)) {
         
         propertyValue = Integer.valueOf(propertyValFromDB);
      } else {
         eventLogger.sendEvent("For " + keyProperty + " value defined in db is " + propertyValFromDB+ ". ", EventType.WARN, this);
      }
      return propertyValue;
   }

 
   /**
    * Retrieve numeric value properties. Gets the numeric value from DB, if the
    * value is not found in DB it will set the default value and log the message.
    * 
    * @param keyProperty
    *           the key property
    * @param defaultKeyVal
    *           the default key value
    * @return the integer
    */
   private Integer retrieveNumericValueProperties(String keyProperty, Integer defaultKeyVal) {
     
      Integer propertyValue = getNumericalPropertyValue(keyProperty, floodControlPropertyTO.getRetrievedPropertyValuesfromDB().get(keyProperty));
      
      if (propertyValue == null) {
         propertyValue = defaultKeyVal;
         eventLogger.sendEvent("Key property: " + keyProperty + " is not found is DB, fetching a default Value for " + keyProperty + ": " + defaultKeyVal + ".", EventType.WARN, this);
      } else {
         eventLogger.sendEvent("Value for key property: " + keyProperty + " found is, " + propertyValue + " in DB.", EventType.INFO, this);
      }
      return propertyValue;
   }


   /**
    * Retrieve non numeric value properties. Gets the non numeric value from DB
    * , if the value is not found in DB it will set the default value and log
    * the message. if value found it will set the value .
    * 
    * @param keyProperty
    *           the key property
    * @param defaultKeyVal
    *           the default key value
    * @return the string
    */
   private String retrieveNonNumericValueProperties(String keyProperty, String defaultKeyVal) {

      String propertyVal = floodControlPropertyTO.getRetrievedPropertyValuesfromDB().get(keyProperty);

      if (StringUtils.isBlank(propertyVal)) {
         propertyVal = defaultKeyVal;
         eventLogger.sendEvent("Key property: " + keyProperty + " is not found is DB , fetching a default Value for "+ keyProperty + ": " + defaultKeyVal + ".", EventType.WARN, this);
      } else {
         eventLogger.sendEvent("Value for key property: " + keyProperty + " found is, " + propertyVal + " in DB.", EventType.DEBUG, this);
      }
      return propertyVal;
   }

   /**
    * To set the numeric and non-numeric values pulled from the DB to the flood
    * control instance variables or setting the default values if DB entry is
    * not present.
    */
   private void setKeyPropertyValue() {

      try {

         for (String propertyKey : REQUIRED_PROPERTY_KEYS) {

            switch (propertyKey) {

            // KEY STORE TYPE
            case KEY_STORE_TYPE:
               setKeyStoreType(retrieveNonNumericValueProperties(propertyKey, KeyStoreType));
               break;

            // KEY BLOCK EXCEPTION
            case KEY_BLOCK_EXCEPTION:
               setKeyBlockException(Boolean.parseBoolean(retrieveNonNumericValueProperties(propertyKey, keyBlockExceptionString)));
               break;

            // KEY FREQUENCY WINDOW
            case KEY_FREQUENCY_WINDOW:
               setKeyFrequencyWindow(retrieveNumericValueProperties(propertyKey, keyFrequencyWindow));
               break;

            // KEY SUPPRESS INTERVAL
            case KEY_SUPPRESS_INTERVAL:
               setKeySuppressInterval(retrieveNumericValueProperties(propertyKey, keySuppressInterval));
               break;

            // MAX CONCURRENT KEYS
            case MAX_CONCURRENT_KEYS:
               setMaxConcurrentKeys(retrieveNumericValueProperties(propertyKey, maxConcurrentKeys));
               break;

            // CACHE REFRESH INTERVAL
            case CACHE_REFRESH_INTERVAL:
               setCacheReFreshInterval(retrieveNumericValueProperties(propertyKey, cacheRefreshInterval));
               break;

            // FLOOD CONTROL EXCEPTION TS LOC
            case FLOOD_CONTROL_EXCEPTION_TSLOC:
               setFloodControlExceptionTsLoc(retrieveNonNumericValueProperties(propertyKey, floodControlExceptionTsLoc));
               break;

            // KEY FREQUENCY LIMIT WDW QT
            case KEY_FREQUENCY_LIMIT_WDW_QT:
               floodControlPropertyTO.setKeyFrequencyLimitWDWQT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWQT()));
               break;

            // KEY FREQUENCY LIMIT WDW CT
            case KEY_FREQUENCY_LIMIT_WDW_CT:
               floodControlPropertyTO.setKeyFrequencyLimitWDWCT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWCT()));
               break;

            // KEY FREQUENCY LIMIT WDW UA
            case KEY_FREQUENCY_LIMIT_WDW_UA:
               floodControlPropertyTO.setKeyFrequencyLimitWDWUA(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWUA()));
               break;

            // KEY FREQUENCY LIMIT WDW UT
            case KEY_FREQUENCY_LIMIT_WDW_UT:
               floodControlPropertyTO.setKeyFrequencyLimitWDWUT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWUT()));
               break;

            // KEY FREQUENCY LIMIT WDW UX
            case KEY_FREQUENCY_LIMIT_WDW_UX:
               floodControlPropertyTO.setKeyFrequencyLimitWDWUX(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWUX()));
               break;

            // KEY FREQUENCY LIMIT WDW VT
            case KEY_FREQUENCY_LIMIT_WDW_VT:
               floodControlPropertyTO.setKeyFrequencyLimitWDWVT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWVT()));
               break;

            // KEY FREQUENCY LIMIT WDW RR
            case KEY_FREQUENCY_LIMIT_WDW_RR:
               floodControlPropertyTO.setKeyFrequencyLimitWDWRR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWRR()));
               break;

            // KEY FREQUENCY LIMIT WDW QR
            case KEY_FREQUENCY_LIMIT_WDW_QR:
               floodControlPropertyTO.setKeyFrequencyLimitWDWQR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWQR()));
               break;

            // KEY FREQUENCY LIMIT WDW UE
            case KEY_FREQUENCY_LIMIT_WDW_UE:
               floodControlPropertyTO.setKeyFrequencyLimitWDWUE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWUE()));
               break;

            // KEY FREQUENCY LIMIT WDW RE
            case KEY_FREQUENCY_LIMIT_WDW_RE:
               floodControlPropertyTO.setKeyFrequencyLimitWDWRE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWRE()));
               break;

            // KEY FREQUENCY LIMIT WDW AM
            case KEY_FREQUENCY_LIMIT_WDW_AM:
               floodControlPropertyTO.setKeyFrequencyLimitWDWAM(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWAM()));
               break;

            // KEY FREQUENCY LIMIT WDW TE
            case KEY_FREQUENCY_LIMIT_WDW_TE:
               floodControlPropertyTO.setKeyFrequencyLimitWDWTE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWTE()));
               break;

            // KEY FREQUENCY LIMIT WDW VR
            case KEY_FREQUENCY_LIMIT_WDW_VR:
               floodControlPropertyTO.setKeyFrequencyLimitWDWVR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWVR()));
               break;

            // KEY FREQUENCY LIMIT DLR CT
            case KEY_FREQUENCY_LIMIT_DLR_CT:
               floodControlPropertyTO.setKeyFrequencyLimitDLRCT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRCT()));
               break;

            // KEY FREQUENCY LIMIT DLR UA
            case KEY_FREQUENCY_LIMIT_DLR_UA:
               floodControlPropertyTO.setKeyFrequencyLimitDLRUA(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRUA()));
               break;

            // KEY FREQUENCY LIMIT DLR UT
            case KEY_FREQUENCY_LIMIT_DLR_UT:
               floodControlPropertyTO.setKeyFrequencyLimitDLRUT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRUT()));
               break;

            // KEY FREQUENCY LIMIT DLR UX
            case KEY_FREQUENCY_LIMIT_DLR_UX:
               floodControlPropertyTO.setKeyFrequencyLimitDLRUX(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRUX()));
               break;

            // KEY FREQUENCY LIMIT DLR VT
            case KEY_FREQUENCY_LIMIT_DLR_VT:
               floodControlPropertyTO.setKeyFrequencyLimitDLRVT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRVT()));
               break;

            // KEY FREQUENCY LIMIT DLR QT
            case KEY_FREQUENCY_LIMIT_DLR_QT:
               floodControlPropertyTO.setKeyFrequencyLimitDLRQT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRQT()));
               break;

            // KEY FREQUENCY LIMIT DLR RR
            case KEY_FREQUENCY_LIMIT_DLR_RR:
               floodControlPropertyTO.setKeyFrequencyLimitDLRRR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRRR()));
               break;

            // KEY FREQUENCY LIMIT DLR QR
            case KEY_FREQUENCY_LIMIT_DLR_QR:
               floodControlPropertyTO.setKeyFrequencyLimitDLRQR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRQR()));
               break;

            // KEY FREQUENCY LIMIT DLR UE
            case KEY_FREQUENCY_LIMIT_DLR_UE:
               floodControlPropertyTO.setKeyFrequencyLimitDLRUE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRUE()));
               break;

            // KEY FREQUENCY LIMIT DLR RE
            case KEY_FREQUENCY_LIMIT_DLR_RE:
               floodControlPropertyTO.setKeyFrequencyLimitDLRRE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRRE()));
               break;

            // KEY FREQUENCY LIMIT DLR AM
            case KEY_FREQUENCY_LIMIT_DLR_AM:
               floodControlPropertyTO.setKeyFrequencyLimitDLRAM(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRAM()));
               break;

            // KEY FREQUENCY LIMIT DLR TE
            case KEY_FREQUENCY_LIMIT_DLR_TE:
               floodControlPropertyTO.setKeyFrequencyLimitDLRTE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRTE()));
               break;

            // KEY FREQUENCY LIMIT DLR VR
            case KEY_FREQUENCY_LIMIT_DLR_VR:
               floodControlPropertyTO.setKeyFrequencyLimitDLRVR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRVR()));
               break;

            // FLOODBLOCK PROP REFRESH
            case FLOODBLOCK_PROP_REFRESH:
               setFloodBlockPropRefresh(retrieveNumericValueProperties(propertyKey, floodBlockPropRefresh));
               break;

            // KEY FREQUENCY LIMIT HKD QT
            case KEY_FREQUENCY_LIMIT_HKD_QT:
               floodControlPropertyTO.setKeyFrequencyLimitHKDQT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDQT()));
               break;

            // KEY FREQUENCY LIMIT HKD CT
            case KEY_FREQUENCY_LIMIT_HKD_CT:
               floodControlPropertyTO.setKeyFrequencyLimitHKDCT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDCT()));
               break;

            // KEY FREQUENCY LIMIT HKD UA
            case KEY_FREQUENCY_LIMIT_HKD_UA:
               floodControlPropertyTO.setKeyFrequencyLimitHKDUA(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDUA()));
               break;

            // KEY FREQUENCY LIMIT HKD UT
            case KEY_FREQUENCY_LIMIT_HKD_UT:
               floodControlPropertyTO.setKeyFrequencyLimitHKDUT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDUT()));
               break;

            // KEY FREQUENCY LIMIT HKD UX
            case KEY_FREQUENCY_LIMIT_HKD_UX:
               floodControlPropertyTO.setKeyFrequencyLimitHKDUX(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDUX()));
               break;

            // KEY FREQUENCY LIMIT HKD VT
            case KEY_FREQUENCY_LIMIT_HKD_VT:
               floodControlPropertyTO.setKeyFrequencyLimitHKDVT(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDVT()));
               break;

            // KEY FREQUENCY LIMIT HKD RR
            case KEY_FREQUENCY_LIMIT_HKD_RR:
               floodControlPropertyTO.setKeyFrequencyLimitHKDRR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDRR()));
               break;

            // KEY FREQUENCY LIMIT HKD QR
            case KEY_FREQUENCY_LIMIT_HKD_QR:
               floodControlPropertyTO.setKeyFrequencyLimitHKDQR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDQR()));
               break;

            // KEY FREQUENCY LIMIT HKD UE
            case KEY_FREQUENCY_LIMIT_HKD_UE:
               floodControlPropertyTO.setKeyFrequencyLimitHKDUE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDUE()));
               break;

            // KEY FREQUENCY LIMIT HKD RE
            case KEY_FREQUENCY_LIMIT_HKD_RE:
               floodControlPropertyTO.setKeyFrequencyLimitHKDRE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDRE()));
               break;

            // KEY FREQUENCY LIMIT HKD AM
            case KEY_FREQUENCY_LIMIT_HKD_AM:
               floodControlPropertyTO.setKeyFrequencyLimitHKDAM(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDAM()));
               break;

            // KEY FREQUENCY LIMIT HKD TE
            case KEY_FREQUENCY_LIMIT_HKD_TE:
               floodControlPropertyTO.setKeyFrequencyLimitHKDTE(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDTE()));
               break;

            // KEY FREQUENCY LIMIT HKD VR
            case KEY_FREQUENCY_LIMIT_HKD_VR:
               floodControlPropertyTO.setKeyFrequencyLimitHKDVR(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitHKDVR()));
               break;

            // KEY FREQUENCY LIMIT WDW QP
            case KEY_FREQUENCY_LIMIT_WDW_QP:
               floodControlPropertyTO.setKeyFrequencyLimitWDWQP(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitWDWQP()));
               break;

            // KEY FREQUENCY LIMIT DLR QP
            case KEY_FREQUENCY_LIMIT_DLR_QP:
               floodControlPropertyTO.setKeyFrequencyLimitDLRQP(retrieveNumericValueProperties(propertyKey, floodControlPropertyTO.getKeyFrequencyLimitDLRQP()));
               break;
            }
         }

      } catch (Exception dtie) {

         eventLogger.sendEvent("Exception occured while setting up the value: " + dtie.toString(), EventType.WARN, this);
      }

   }

   /**
    * Abstract method intended to be used by whatever application is
    * implementing flood control. The implementation of this method is delegated
    * to the application so the individual application can choose how it
    * develops keys From transaction data.
    * 
    * @param txn
    *           some type of transaction object (XML, String, class, etc.).
    * @return a unique transaction key derived From the object passed in.
    * @throws KeyDerivationException
    *            should there be a problem or inability to derive the key From
    *            the transaction.
    */
   public abstract Object deriveKey(Object txn) throws KeyDerivationException;

   /**
    * The primary method used to evaluate a transaction and determine if it has
    * crossed flood control boundaries.
    * 
    * @param txn
    *           some type of transaction object (XML, String, class, etc.).
    * @throws KeyBlockException
    *            if the transaction is now crossing flood control boundaries.
    *            (optional, configurable)
    * @throws KeySuppressException
    *            if the transaction has crossed flood control boundaries.
    * @throws KeyDerivationException
    *            should there be a problem or inability to derive the key From
    *            the transaction.
    * @throws KeyStoreException
    *            for any mechanical error experienced while performing this
    *            operation.
    */
   public void evaluateTransaction(Object txn) throws KeyBlockException, KeySuppressException, KeyDerivationException,
            KeyStoreException {

      Object key = null;
      FloodMatchSignatureTO floodMatchSignatureTO = null;

      // Do math to determine the "Date" in seconds items older than should be
      // obliterated.
      Date rightNow = new Date();
      long onceUponATime = rightNow.getTime() - (keyFrequencyWindow * 1000);
      Date trailingWindowEdge = new Date(onceUponATime);

      // Perform the required, periodic updates of the key store such as
      // aging off blocked keys, aging off occurrences, etc.
      updateKeyStore(rightNow, trailingWindowEdge);

      // If flood control is not active, exit.
      if (floodControlActive == false)
      {
         return;
      }
      // If implementor provides a null transaction, then exit.
      if (txn == null)
      {
         return;
      }
      // Obtain the floodMatchSignatureTO From the transaction. If null, exit.
      floodMatchSignatureTO = (FloodMatchSignatureTO) deriveKey(txn);
      if (floodMatchSignatureTO == null) {
         return;
      }

      // Getting the keyFrequencyLimit for respective request
      keyFrequencyLimit = getFloodControlPropertyTO(floodMatchSignatureTO);

      // getting the key String from floodMatchSignatureTO
      key = floodMatchSignatureTO.getSignature();

      // If this is an already blocked key, throw KeySuppressException.
      if (keyStore.isBlockedKey(key))
      {
         throw new KeySuppressException();
      }
      // Get the number of recorded occurrences of this key
      int keyOccurrences = keyStore.countKeyOccurrences(key);

      // If this is a new key, store it, then exit.
      if (keyOccurrences == 0) {
         keyStore.storeNewKey(key);
         return;
      }

      // If this key should have been already suppressed (based on occurrences,
      // suppress
      if (keyOccurrences > keyFrequencyLimit) {
         keyStore.addBlockingKeyOccurrence(key);
         throw new KeySuppressException();
      }

      // If this key is not new, then update the cache based on time.
      keyStore.clearOccurrencesOlderThan(trailingWindowEdge, key);

      // This repeated call is intentional, to obtain the most accurate picture.
      keyOccurrences = keyStore.countKeyOccurrences(key) + 1;

      if (keyOccurrences <= keyFrequencyLimit) {
         keyStore.addKeyOccurrence(key);
         return;
      } else if (keyOccurrences > keyFrequencyLimit) {
         keyStore.addBlockingKeyOccurrence(key);
         if (this.keyBlockException) {
            throw new KeyBlockException();
         } else {
            throw new KeySuppressException();
         }

      }

      return;
   }

   /**
    * Evaluates blocked keys and the number of keys stored to determine if
    * action is required (such as removing a blocked key or resetting the key
    * store because it's reached its maximum number of concurrent keys). It also
    * reFreshes the key store for general clean-up.
    * 
    * @param rightNow
    *           a "snapshot" time, intended to be the time considered current.
    *           This isn't developed by the method because "now" could change by
    *           milliseconds as code is executed.
    * @param trailingWindowEdge
    *           value used to indicate the time when key occurrences will no
    *           longer be considered in the window for flood control
    *           consideration.
    * @throws KeyStoreException
    *            for any mechanical error experienced while performing this
    *            operation.
    */
   private void updateKeyStore(Date rightNow, Date trailingWindowEdge) throws KeyStoreException {

      // Do the following tasks no more than one per second (less if volume
      // is slower than one transaction per second).
      long elapsed = rightNow.getTime() - lastTaskReFresh.getTime();
      if (elapsed >= 1000) {

         lastTaskReFresh = rightNow;

         // Evaluates the blocked keys (no more than once per second).
         long onceUponATime = rightNow.getTime() - (keySuppressInterval * 1000);
         Date theTimeWhenBlocksExpire = new Date(onceUponATime);
         keyStore.removeBlockedKeysOlderThan(theTimeWhenBlocksExpire);

         // Evaluates the max concurrent keys (no more than once per second).
         int numberOfKeysStored = keyStore.getNumberOfKeysStored();
         if (numberOfKeysStored > maxConcurrentKeys) {
            keyStore.resetKeyStore();
         }

      }

      // ReFreshes the cache for general clean-up
      // If cacheReFreshInterval is zero, reFresh the cache. Otherwise, only do
      // so if it's time.
      if (this.cacheRefreshInterval == 0) {
         keyStore.refreshKeyStore(trailingWindowEdge);
      } else {

         long suggestedReFresh = rightNow.getTime() - (cacheRefreshInterval * 1000);
         long lastReFresh = getLastCacheReFresh().getTime();
         if (lastReFresh <= suggestedReFresh) {
            keyStore.refreshKeyStore(trailingWindowEdge);
            setLastCacheReFresh(rightNow);
         }
      }

      return;
   }

   /**
    * Gets the cache reFresh interval.
    * 
    * @return the cacheReFreshInterval
    */
   public int getCacheReFreshInterval() {
      return cacheRefreshInterval;
   }

   /**
    * Checks if is flood control active.
    * 
    * @return the floodControlActive
    */
   public boolean isFloodControlActive() {
      return floodControlActive;
   }

   /**
    * Checks if is key block exception.
    * 
    * @return the keyBlockException
    */
   public boolean isKeyBlockException() {
      return keyBlockException;
   }

   /**
    * Gets the key Frequency limit.
    * 
    * @return the keyFrequencyLimit
    */
   public int getKeyFrequencyLimit() {
      return keyFrequencyLimit;
   }

   /**
    * Gets the key Frequency window.
    * 
    * @return the keyFrequencyWindow
    */
   public int getKeyFrequencyWindow() {
      return keyFrequencyWindow;
   }

   /**
    * Gets the key suppress interval.
    * 
    * @return the keySuppressInterval
    */
   public int getKeySuppressInterval() {
      return keySuppressInterval;
   }

   /**
    * Gets the max concurrent keys.
    * 
    * @return the maxConcurrentKeys
    */
   public int getMaxConcurrentKeys() {
      return maxConcurrentKeys;
   }

   /**
    * Sets the cache reFresh interval.
    * 
    * @param cacheRefreshInterval
    *           the new cache re fresh interval
    */
   public void setCacheReFreshInterval(int cacheRefreshInterval) {
      this.cacheRefreshInterval = cacheRefreshInterval;
   }

   /**
    * Sets the flood control active.
    * 
    * @param floodControlActiveNextState
    *           the floodControlActive next state.
    * @throws KeyStoreException
    *            the key store exception
    */
   public void setFloodControlActive(boolean floodControlActiveNextState) throws KeyStoreException {

      if (floodControlActiveNextState != floodControlActive) {
         this.floodControlActive = floodControlActiveNextState;
         if (floodControlActiveNextState == false) {
            keyStore.resetKeyStore();
         }
      }

      return;
   }

   /**
    * Sets the key block exception.
    * 
    * @param keyBlockExceptionIn
    *           the new key block exception
    */
   public void setKeyBlockException(boolean keyBlockExceptionIn) {
      this.keyBlockException = keyBlockExceptionIn;
   }

   /**
    * Sets the key Frequency limit.
    * 
    * @param keyFrequencyLimitIn
    *           the new key Frequency limit
    */
   public void setKeyFrequencyLimit(int keyFrequencyLimitIn) {
      keyFrequencyLimit = keyFrequencyLimitIn;
   }

   /**
    * Sets the key Frequency window.
    * 
    * @param keyFrequencyWindowIn
    *           the new key Frequency window
    */
   public void setKeyFrequencyWindow(int keyFrequencyWindowIn) {
      this.keyFrequencyWindow = keyFrequencyWindowIn;
   }

   /**
    * Sets the key suppress interval.
    * 
    * @param keySuppressIntervalIn
    *           the new key suppress interval
    */
   public void setKeySuppressInterval(int keySuppressIntervalIn) {
      this.keySuppressInterval = keySuppressIntervalIn;
   }

   /**
    * Sets the max concurrent keys.
    * 
    * @param maxConcurrentKeysIn
    *           the new max concurrent keys
    */
   public void setMaxConcurrentKeys(int maxConcurrentKeysIn) {
      this.maxConcurrentKeys = maxConcurrentKeysIn;
   }

   /**
    * Gets the last cache reFresh.
    * 
    * @return the lastCacheReFresh
    */
   public Date getLastCacheReFresh() {
      return new Date(lastCacheReFresh.getTime());
   }

   /**
    * Sets the last cache reFresh.
    * 
    * @param lastCacheReFreshIn
    *           the new last cache reFresh
    */
   public static void setLastCacheReFresh(Date lastCacheReFreshIn) {
      lastCacheReFresh = new Date(lastCacheReFreshIn.getTime());
   }

   /**
    * Gets the number of keys stored.
    * 
    * @return the number of keys in the key store.
    * @throws KeyStoreException
    *            the key store exception
    */
   public int getNumberOfKeysStored() throws KeyStoreException {
      return keyStore.getNumberOfKeysStored();
   }

   /**
    * Gets the key store type.
    * 
    * @return the key store type
    */
   public String getKeyStoreType() {
      return KeyStoreType;
   }

   /**
    * Sets the key store type.
    * 
    * @param keyStoreType
    *           the new key store type
    */
   public  void setKeyStoreType(String keyStoreType) {
      KeyStoreType = keyStoreType;
   }

   /**
    * Gets the flood block prop refresh.
    * 
    * @return the floodBlockPropRefresh
    */
   public int getFloodBlockPropRefresh() {
      return floodBlockPropRefresh;
   }

   /**
    * Sets the flood block prop refresh.
    * 
    * @param floodBlockPropRefresh
    *           the floodBlockPropRefresh to set
    */
   public void setFloodBlockPropRefresh(int floodBlockPropRefresh) {
      this.floodBlockPropRefresh = floodBlockPropRefresh;
   }

   /**
    * Gets the flood control exception ts loc.
    * 
    * @return the flood control exception ts loc
    */
   public String getFloodControlExceptionTsLoc() {
      return floodControlExceptionTsLoc;
   }

   /**
    * Sets the flood control exception ts loc.
    * 
    * @param floodControlExceptionTsLoc
    *           the new flood control exception ts loc
    */
   public void setFloodControlExceptionTsLoc(String floodControlExceptionTsLoc) {
      this.floodControlExceptionTsLoc = floodControlExceptionTsLoc;
   }

   /**
    * Gets the flood control property TO. This method is used to fetch the
    * property value for each transaction from the DB.
    * 
    * @param floodMatchSignatureTO
    *           the flood match signature TO
    * @return the flood control property TO
    * @throws KeyDerivationException
    *            the key derivation exception
    */
   public int getFloodControlPropertyTO(FloodMatchSignatureTO floodMatchSignatureTO) throws KeyDerivationException {

      // If Provider Type is WDW
      if (TESTWDW.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PRODWDW.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || TEST.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PROD.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())) {

         switch (floodMatchSignatureTO.getTransactionType()) {

         // CREATETICKET
         case CREATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWCT();
            break;

         // UPGRADEALPHA
         case UPGRADEALPHA:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWUA();
            break;

         // UPDATETICKET
         case UPDATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWUT();
            break;

         // UPDATETRANSACTION
         case UPDATETRANSACTION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWUT();
            break;

         // VOIDTICKET
         case VOIDTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWVT();
            break;

         // QUERYTICKET
         case QUERYTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWQT();
            break;

         // RESERVATION
         case RESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWRR();
            break;

         // QUERYRESERVATION
         case QUERYRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWQR();
            break;

         // UPGRADEENTITLEMENT
         case UPGRADEENTITLEMENT: // 2.10
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWUE();
            break;

         // RENEWENTITLEMENT
         case RENEWENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWRE();
            break;

         // ASSOCIATEMEDIATOACCOUNT
         case ASSOCIATEMEDIATOACCOUNT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWAM();
            break;

         // TICKERATEENTITLEMENT
         case TICKERATEENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWTE();
            break;

         // VOIDRESERVATION
         case VOIDRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWVR();
            break;

         // QUERYELIGIBLEPRODUCTS
         case QUERYELIGIBLEPRODUCTS:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitWDWQP();
            break;

         default:
            floodControlPropertyTO.toString();
            return 0; // Shouldn't be able to get here, but still fail gracefully.
         }
      }
      
      // If ProviderType is DLR.
      else if (TESTDLR.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PRODDLR.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())) {

         switch (floodMatchSignatureTO.getTransactionType()) {

         // CREATETICKET
         case CREATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRCT();
            break;

         // UPGRADEALPHA
         case UPGRADEALPHA:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRUA();
            break;

         // UPDATETICKET
         case UPDATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRUT();
            break;

         // UPDATETRANSACTION
         case UPDATETRANSACTION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRUT();
            break;

         // VOIDTICKET
         case VOIDTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRVT();
            break;

         // QUERYTICKET
         case QUERYTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRQT();
            break;

         // RESERVATION
         case RESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRRR();
            break;

         // QUERYRESERVATION
         case QUERYRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRQR();
            break;

         // UPGRADEENTITLEMENT
         case UPGRADEENTITLEMENT: // 2.10
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRUE();
            break;

         // RENEWENTITLEMENT
         case RENEWENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRRE();
            break;

         // ASSOCIATEMEDIATOACCOUNT
         case ASSOCIATEMEDIATOACCOUNT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRAM();
            break;

         // TICKERATEENTITLEMENT
         case TICKERATEENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRTE();
            break;

         // VOIDRESERVATION
         case VOIDRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRVR();
            break;

         // QUERYELIGIBLEPRODUCTS
         case QUERYELIGIBLEPRODUCTS:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitDLRQP();
            break;

         default:
            floodControlPropertyTO.toString();
            return 0; // Shouldn't be able to get here, but still fail
                      // gracefully.
         }
      }
      // If ProviderType is HKD.
      else if (TESTHKD.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PRODHKD.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())) {

         switch (floodMatchSignatureTO.getTransactionType()) {

         // CREATETICKET
         case CREATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDCT();
            break;

         // UPGRADEALPHA
         case UPGRADEALPHA:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDUA();
            break;

         // UPDATETICKET
         case UPDATETICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDUT();
            break;

         // UPDATETRANSACTION
         case UPDATETRANSACTION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDUT();
            break;

         // VOIDTICKET
         case VOIDTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDVT();
            break;

         // QUERYTICKET
         case QUERYTICKET:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDQT();
            break;

         // RESERVATION
         case RESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDRR();
            break;

         // QUERYRESERVATION
         case QUERYRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDQR();
            break;

         // UPGRADEENTITLEMENT
         case UPGRADEENTITLEMENT: // 2.10
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDUE();
            break;

         // RENEWENTITLEMENT
         case RENEWENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDRE();
            break;

         // ASSOCIATEMEDIATOACCOUNT
         case ASSOCIATEMEDIATOACCOUNT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDAM();
            break;

         // TICKERATEENTITLEMENT
         case TICKERATEENTITLEMENT:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDTE();
            break;

         // VOIDRESERVATION
         case VOIDRESERVATION:
            keyFrequencyLimit = floodControlPropertyTO.getKeyFrequencyLimitHKDVR();
            break;

         default:
            floodControlPropertyTO.toString();
            return 0; // Shouldn't be able to get here, but still fail gracefully.
         }
      }
      return keyFrequencyLimit;
   }
}