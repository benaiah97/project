package pvt.disney.dti.gateway.util.flood;

import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.common.FloodControlPropertyTO;
import pvt.disney.dti.gateway.data.common.PropertyTO;
import pvt.disney.dti.gateway.util.flood.FloodControlInitException;
import pvt.disney.dti.gateway.util.flood.FloodControlKeyStore;
import pvt.disney.dti.gateway.util.flood.FloodControlKeyStoreFactory;
import pvt.disney.dti.gateway.util.flood.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.util.flood.KeyBlockException;
import pvt.disney.dti.gateway.util.flood.KeyDerivationException;
import pvt.disney.dti.gateway.util.flood.KeySuppressException;
import pvt.disney.dti.gateway.util.flood.MemoryBasedKeyStore;

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
	

   // Configuration values
   /** Is flood control active. If not, it will simply return. Set via method. */
   private boolean floodControlActive = true;
   /**
    * The window of time in which key occurrences are subject to evaluation.
    * Property.
    */
   private int keyFrequencyWindow = 0;
   /**
    * The number of times in which a key can re-occur without being subject to
    * blocking or suppression. Property.
    */
   private int keyFrequencyLimit = 0;
   /**
    * Determines if a key block exception will be used the first time a key
    * exceeds its limit of occurrences. Property.
    */
   private boolean keyBlockException = false;
   /**
    * The amount of time (in seconds) that a flood controlled key will be
    * suppressed. Property.
    */
   private int keySuppressInterval = 0;
   /**
    * The maximum number of concurrent keys allowed to exist at one time.
    * Property.
    */
   private int maxConcurrentKeys = 0;
   /**
    * The period of time, in seconds, that the key store will be reFreshed (for
    * clean-up). Property.
    */
   private int cacheReFreshInterval = 0;
   /** Date/Time of the last cache (key store) reFresh. */
   private static Date lastCacheReFresh = new Date();
   /** Date/Time of the last task reFresh. */
   private static Date lastTaskReFresh = new Date();
   /** Properties used to control KeyMatchFloodControl. */
   protected static Properties props;
   /** The flood control key store. */
   private FloodControlKeyStore keyStore;

   /** The Constant TESTWDW. */
   private final static String TESTWDW = "Test-WDW";
   
   /** The Constant PRODWDW. */
   private final static String PRODWDW = "prod-WDW";
   
   /** The Constant WDW. */
   private final static String TEST = "Test";
   
   /** The Constant WDW. */
   private final static String PROD = "Prod";
   
   /** The Constant TESTDLR. */
   private final static String TESTDLR = "Test-DLR";
   
   /** The Constant PRODDLR. */
   private final static String PRODDLR = "prod-DLR";
   
   /** The Constant TESTHKD. */
   private final static String TESTHKD = "Test-HKD";
   
   /** The Constant PRODHKD. */
   private final static String PRODHKD = "prod-HKD";
   
	/** The Constant KEYS. */
	private final static String KEYS[] = { "FloodControl.KeyStoreType",
			"FloodControl.KeyBlockException",
			"FloodControl.KeyFrequencyWindow",
			"FloodControl.KeyFrequencyLimit",
			"FloodControl.KeySuppressInterval",
			"FloodControl.MaxConcurrentKeys",
			"FloodControl.CacheReFreshInterval", "KEY_FREQUENCY_LIMIT_WDW_CT",
			"KEY_FREQUENCY_LIMIT_WDW_UA", "KEY_FREQUENCY_LIMIT_WDW_UT",
			"KEY_FREQUENCY_LIMIT_WDW_UX", "KEY_FREQUENCY_LIMIT_WDW_VT",
			"KEY_FREQUENCY_LIMIT_WDW_QT", "KEY_FREQUENCY_LIMIT_WDW_RR",
			"KEY_FREQUENCY_LIMIT_WDW_QR", "KEY_FREQUENCY_LIMIT_WDW_UE",
			"KEY_FREQUENCY_LIMIT_WDW_RE", "KEY_FREQUENCY_LIMIT_WDW_AM",
			"KEY_FREQUENCY_LIMIT_WDW_TE", "KEY_FREQUENCY_LIMIT_WDW_VR",
			"KEY_FREQUENCY_LIMIT_DLR_CT", "KEY_FREQUENCY_LIMIT_DLR_UA",
			"KEY_FREQUENCY_LIMIT_DLR_UT", "KEY_FREQUENCY_LIMIT_DLR_UX",
			"KEY_FREQUENCY_LIMIT_DLR_VT", "KEY_FREQUENCY_LIMIT_DLR_QT",
			"KEY_FREQUENCY_LIMIT_DLR_RR", "KEY_FREQUENCY_LIMIT_DLR_QR",
			"KEY_FREQUENCY_LIMIT_DLR_UE", "KEY_FREQUENCY_LIMIT_DLR_RE",
			"KEY_FREQUENCY_LIMIT_DLR_AM", "KEY_FREQUENCY_LIMIT_DLR_TE",
			"KEY_FREQUENCY_LIMIT_DLR_VR" };
   
   /** The Constant FLOODBLOCK. */
   private final static String FLOODBLOCK = "FB";
   
   /** The Key store type. */
   private String KeyStoreType ;
   
   /** The flood control property TO. */
   private FloodControlPropertyTO floodControlPropertyTO = new FloodControlPropertyTO();
   
   /**
    * The constructor for KeyMatchFloodControl. Sets and validates properties.
    *
    * @param application the application
    * @param environment the environment
    * @throws FloodControlInitException for any mechanical error experienced while performing this
    *            operation.
    * @throws DTIException the DTI exception
    */
	public KeyMatchFloodControl(String application, String environment) throws FloodControlInitException, DTIException
	{
		
		floodControlPropperty(application, environment);
   }
	
	public  void floodControlPropperty(String application, String environment) throws FloodControlInitException, DTIException
 {
		List<PropertyTO> propertyList = null;
		List<String> KeyList = new ArrayList<String>();
		List<String> KeyListFromDB = new ArrayList<String>();
		try {
			propertyList = PropertyKey.getProperties(application, environment,
					FLOODBLOCK);

			List<String> propertyConstantList = Arrays.asList(KEYS);

			if ((propertyList != null) && !propertyList.isEmpty()) {
				for (PropertyTO propertyTO : propertyList) {

					String propertSetKey = propertyTO.getPropSetKey();

					String propertySetValue = propertyTO.getPropSetValue();

					if (StringUtils.isEmpty(propertSetKey)
							&& StringUtils.isEmpty(propertySetValue)) {
						throw new FloodControlInitException(
								"Prperties are not defined properly.");
					}

					if (propertSetKey.equals("FloodControl.KeyStoreType")) {
						{
							try {
								setKeyStoreType(propertySetValue);
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.KeyStoreType property value non-integer.");
							}
						}

						// boolean keyBlockException
						if (propertSetKey
								.equals("FloodControl.KeyBlockException")) {
							try {
								setKeyBlockException(Boolean
										.parseBoolean(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.KeyBlockException property value non-integer.");
							}
							continue;
						}

						// integer keyFrequencyWindow
						if (propertSetKey
								.equals("FloodControl.KeyFrequencyWindow")) {
							try {
								setKeyFrequencyWindow(Integer
										.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.KeyFrequencyWindow property value non-integer.");
							}
							continue;
						}

						// integer keyFrequencyLimit
						if (propertSetKey
								.equals("FloodControl.KeyFrequencyLimit")) {
							try {
								setKeyFrequencyLimit(Integer
										.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.KeyFrequencyLimit property value non-integer.");
							}
						}

						// integer keySuppressInterval
						if (propertSetKey
								.equals("FloodControl.KeySuppressInterval")) {
							try {
								setKeySuppressInterval(Integer
										.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.KeySuppressInterval property value non-integer.");
							}
							continue;
						}

						// integer maxConcurrentKeys
						if (propertSetKey
								.equals("FloodControl.MaxConcurrentKeys")) {
							try {
								setMaxConcurrentKeys(Integer
										.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.MaxConcurrentKeys property value non-integer.");
							}
							continue;
						}
						// integer cacheReFreshInterval
						if (propertSetKey
								.equals("FloodControl.CacheReFreshInterval")) {
							try {
								setCacheReFreshInterval(Integer
										.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"FloodControl.CacheReFreshInterval property value non-integer.");
							}
							continue;
						}

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_QT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWQT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_QT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_CT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWCT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_CT property value non-integer.");
							}
							continue;
						}

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_UA")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUA(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_UA  property value non-integer.");
							}
							continue;
						}

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_UT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_UT property value non-integer.");
							}
							continue;
						}

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_UX")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUX(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_UT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_VT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWVT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_VT property value non-integer.");
							}
							continue;
						}

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_RR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWRR(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_RR property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_QR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWQR(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_QR property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_UE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_UE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_RE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWRE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_RE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_AM")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWAM(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_AM property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_TE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWTE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_TE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_WDW_VR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWVR(Integer
												.parseInt(propertySetValue));
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_WDW_VR property value non-integer.");
							}
							continue;
						}

						// /

						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_CT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWCT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_CT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_UA")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUA(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_UA property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_UT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_UT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_UX")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWUX(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_UX property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_VT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitWDWVT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_VT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_QT")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRQT(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_QT property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_RR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRRR(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_RR property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_QR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRQR(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_QR property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_UE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRUE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_UE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_RE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRRE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_RE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_AM")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRAM(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_AM property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_TE")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRTE(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_TE property value non-integer.");
							}
							continue;
						}
						if (propertSetKey.equals("KEY_FREQUENCY_LIMIT_DLR_VR")) {
							try {
								floodControlPropertyTO
										.setKeyFrequencyLimitDLRVR(Integer
												.parseInt(propertySetValue));
								KeyListFromDB.add(propertSetKey);
							} catch (NumberFormatException nfe) {
								throw new FloodControlInitException(
										"KEY_FREQUENCY_LIMIT_DLR_VR property value non-integer.");
							}
							continue;
						}
					}

					propertyConstantList.removeAll(KeyListFromDB);

					if (propertyConstantList.size() > 0) {
						throw new FloodControlInitException("Properties "+ propertyConstantList + " are not provided.");
					}

					// if KeyStoretype is InMemory
					if (getKeyStoreType().equals("InMemory")) {
						keyStore = FloodControlKeyStoreFactory.getInstance(getMaxConcurrentKeys(),FloodControlKeyStore.Type.MEMORY);
					} else {
						keyStore = MemoryBasedKeyStore.getInstance(getMaxConcurrentKeys()); // It's the only one available.
					}
				}
			}
		} catch (DTIException e) {
			throw new DTIException(KeyMatchFloodControl.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProperties", e);

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
      // Do math to determine the "Date" in seconds items older than should be
      // obliterated.
      Date rightNow = new Date();
      long onceUponATime = rightNow.getTime() - (keyFrequencyWindow * 1000);
      Date trailingWindowEdge = new Date(onceUponATime);
      FloodMatchSignatureTO floodMatchSignatureTO=new FloodMatchSignatureTO();
      // Perform the required, periodic updates of the key store such as
      // aging off blocked keys, aging off occurrences, etc.
      updateKeyStore(rightNow, trailingWindowEdge);

      // If flood control is not active, exit.
      if (floodControlActive == false)
         return;

      // If implementor provides a null transaction, then exit.
      if (txn == null)
         return;

      // Obtain the key from the transaction. If null, exit.
      floodMatchSignatureTO = (FloodMatchSignatureTO) deriveKey(txn);
      if (floodMatchSignatureTO == null)
         return;
       key=floodMatchSignatureTO.getSignature();
      // TODO :: Somesh
      // checking the key substring If Provider will WDW
      if (TESTWDW.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PRODWDW.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || TEST.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PROD.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())) {
         switch (floodMatchSignatureTO.getTransactionType()) {
         case CREATETICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyfrequencyLimitWDWCT();
            break;
         case UPGRADEALPHA:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWUA();
            break;
         case UPDATETICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWUT();
            break;
         case UPDATETRANSACTION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWUT();
            break;
         case VOIDTICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWVT();
            break;
         case QUERYTICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWQT();
            break;
         case RESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWRR();
            break;
         case QUERYRESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWQR();
            break;
         case UPGRADEENTITLEMENT: // 2.10
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWUE();
            break;
         case RENEWENTITLEMENT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWRE();
            break;
         case ASSOCIATEMEDIATOACCOUNT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWAM();
            break;
         case TICKERATEENTITLEMENT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWTE();
            break;
         case VOIDRESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitWDWVR();
            break;
            //TODO:: Somesh:: QEP
         default:
            return; // Shouldn't be able to get here, but still fail
                        // gracefully.
         }
      }
 
      // If Provider will DLR
      else if (TESTDLR.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())
               || PRODDLR.equalsIgnoreCase(floodMatchSignatureTO.getProviderType())) {

         switch (floodMatchSignatureTO.getTransactionType()) {
         case CREATETICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRCT();
            break;
         case UPGRADEALPHA:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRUA();
            break;
         case UPDATETICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRUT();
            break;
         case UPDATETRANSACTION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRUT();
            break;
         case VOIDTICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRVT();
            break;
         case QUERYTICKET:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRQT();
            break;
         case RESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRRR();
            break;
         case QUERYRESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRQR();
            break;
         case UPGRADEENTITLEMENT: // 2.10
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRUE();
            break;
         case RENEWENTITLEMENT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRRE();
            break;
         case ASSOCIATEMEDIATOACCOUNT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRAM();
            break;
         case TICKERATEENTITLEMENT:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRTE();
            break;
         case VOIDRESERVATION:
            keyFrequencyLimit=floodControlPropertyTO.getKeyFrequencyLimitDLRVR();
            break;
            //TODO:: Somesh:: QEP
         default:
            return; // Shouldn't be able to get here, but still fail
                        // gracefully.
         }
      }

     /* // If Provider will HKDL
      else if (TESTHKD.equalsIgnoreCase(floodControllerVO.getProviderType())
               || PRODHKD.equalsIgnoreCase(floodControllerVO.getProviderType())) {

         switch (floodControllerVO.getTransactionType()) {
         case CREATETICKET:
            floodControlPropertyTO.getKeyFrequencyLimitHkdCT();
            break;
         case UPGRADEALPHA:
            floodControlPropertyTO.getKeyFrequencyLimitHkdUA();
            break;
         case UPDATETICKET:
            floodControlPropertyTO.getKeyFrequencyLimitHkdUT();
            break;
         case UPDATETRANSACTION:
            floodControlPropertyTO.getKeyFrequencyLimitHkdUT();
            break;
         case VOIDTICKET:
            floodControlPropertyTO.getKeyFrequencyLimitHkdVT();
            break;
         case QUERYTICKET:
            floodControlPropertyTO.getKeyFrequencyLimitHkdQT();
            break;
         case RESERVATION:
            floodControlPropertyTO.getKeyFrequencyLimitHkdRR();
            break;
         case QUERYRESERVATION:
            floodControlPropertyTO.getKeyFrequencyLimitHkdQR();
            break;
         case UPGRADEENTITLEMENT: // 2.10
            floodControlPropertyTO.getKeyFrequencyLimitHkdUE();
            break;
         case RENEWENTITLEMENT:
            floodControlPropertyTO.getKeyFrequencyLimitHkdRE();
            break;
         case ASSOCIATEMEDIATOACCOUNT:
            floodControlPropertyTO.getKeyFrequencyLimitHkdAM();
            break;
         case TICKERATEENTITLEMENT:
            floodControlPropertyTO.getKeyFrequencyLimitHkdTE();
            break;
         case VOIDRESERVATION:
            floodControlPropertyTO.getKeyFrequencyLimitVR();
            break;
         default:
            return; // Shouldn't be able to get here, but still fail
                        // gracefully.
         }
      }*/
   
      // If this is an already blocked key, throw KeySuppressException.
      if (keyStore.isBlockedKey(key))
         throw new KeySuppressException();

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
      if (this.cacheReFreshInterval == 0) {
         keyStore.refreshKeyStore(trailingWindowEdge);
      } else {

         long suggestedReFresh = rightNow.getTime() - (cacheReFreshInterval * 1000);
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
      return cacheReFreshInterval;
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
   public synchronized int getKeyFrequencyLimit() {
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
    * @param cacheReFreshIntervalIn the new cache reFresh interval
    */
   public void setCacheReFreshInterval(int cacheReFreshIntervalIn) {
      this.cacheReFreshInterval = cacheReFreshIntervalIn;
   }

   /**
    * Sets the flood control active.
    *
    * @param floodControlActiveNextState           the floodControlActive next state.
    * @throws KeyStoreException the key store exception
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
    * @param keyBlockExceptionIn the new key block exception
    */
   public void setKeyBlockException(boolean keyBlockExceptionIn) {
      this.keyBlockException = keyBlockExceptionIn;
   }

   /**
    * Sets the key Frequency limit.
    *
    * @param keyFrequencyLimitIn the new key Frequency limit
    */
   public synchronized void setKeyFrequencyLimit(int keyFrequencyLimitIn) {
      keyFrequencyLimit = keyFrequencyLimitIn;
   }

   /**
    * Sets the key Frequency window.
    *
    * @param keyFrequencyWindowIn the new key Frequency window
    */
   public void setKeyFrequencyWindow(int keyFrequencyWindowIn) {
      this.keyFrequencyWindow = keyFrequencyWindowIn;
   }

   /**
    * Sets the key suppress interval.
    *
    * @param keySuppressIntervalIn the new key suppress interval
    */
   public void setKeySuppressInterval(int keySuppressIntervalIn) {
      this.keySuppressInterval = keySuppressIntervalIn;
   }

   /**
    * Sets the max concurrent keys.
    *
    * @param maxConcurrentKeysIn the new max concurrent keys
    */
   public void setMaxConcurrentKeys(int maxConcurrentKeysIn) {
      this.maxConcurrentKeys = maxConcurrentKeysIn;
   }

   /**
    * Gets the last cache reFresh.
    *
    * @return the lastCacheReFresh
    */
   public synchronized Date getLastCacheReFresh() {
      return new Date(lastCacheReFresh.getTime());
   }

   /**
    * Sets the last cache reFresh.
    *
    * @param lastCacheReFreshIn the new last cache reFresh
    */
   public static synchronized void setLastCacheReFresh(Date lastCacheReFreshIn) {
      lastCacheReFresh = new Date(lastCacheReFreshIn.getTime());
   }

   /**
    * Gets the number of keys stored.
    *
    * @return the number of keys in the key store.
    * @throws KeyStoreException the key store exception
    */
   public int getNumberOfKeysStored() throws KeyStoreException {
      return keyStore.getNumberOfKeysStored();
   }

   /**
    * Sets the properties.
    *
    * @param propsIn           the properties to set
    */
   private static synchronized void setProperties(Properties propsIn) {
      props = propsIn;
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
	 * @param keyStoreType the new key store type
	 */
	public void setKeyStoreType(String keyStoreType) {
		KeyStoreType = keyStoreType;
	}

	
	
}
