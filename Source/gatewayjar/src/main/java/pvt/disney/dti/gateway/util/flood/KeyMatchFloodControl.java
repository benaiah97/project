package pvt.disney.dti.gateway.util.flood;

import java.util.Date;
import java.util.Properties;

/**
 * This class utilizes a "key match" method for flood control. Simply stated, a key is an object of any type that will be compared against new transactions to see if there is a match. All keys are stored in a key store (type defined by
 * configuration). This class must not only control the rules of flood control, it must also make sure that, periodically or continually (configurable) that the key store is cleaned up.
 * 
 * @author lewit019
 * 
 */
public abstract class KeyMatchFloodControl {

	// Configuration values
	/** Is flood control active. If not, it will simply return. Set via method. */
	private boolean floodControlActive = true;
	/** The window of time in which key occurrences are subject to evaluation. Property. */
	private int keyFrequencyWindow = 0;
	/** The number of times in which a key can re-occur without being subject to blocking or suppression. Property. */
	private int keyFrequencyLimit = 0;
	/** Determines if a key block exception will be used the first time a key exceeds its limit of occurrences. Property. */
	private boolean keyBlockException = false;
	/** The amount of time (in seconds) that a flood controlled key will be suppressed. Property. */
	private int keySuppressInterval = 0;
	/** The maximum number of concurrent keys allowed to exist at one time. Property. */
	private int maxConcurrentKeys = 0;
	/** The period of time, in seconds, that the key store will be refreshed (for clean-up). Property. */
	private int cacheRefreshInterval = 0;
	/** Date/Time of the last cache (key store) refresh. */
	private static Date lastCacheRefresh = new Date();
	/** Date/Time of the last task refresh. */
	private static Date lastTaskRefresh = new Date();
	/** Properties used to control KeyMatchFloodControl. */
	protected static Properties props;
	/** The flood control key store. */
	private FloodControlKeyStore keyStore;

	/**
	 * The constructor for KeyMatchFloodControl. Sets and validates properties.
	 * 
	 * @param props
	 *            Properties used to initialize KeyMatchFloodControl.
	 * @throws FloodControlInitException
	 *             for any mechanical error experienced while performing this operation.
	 */
	public KeyMatchFloodControl(Properties propsIn)
			throws FloodControlInitException {

		KeyMatchFloodControl.setProperties(propsIn);

		String keyStoreString = props.getProperty("FloodControl.KeyStoreType",
				"InMemory");
		if (keyStoreString.compareTo("InMemory") == 0) {
			keyStore = FloodControlKeyStoreFactory.getInstance(props,
					FloodControlKeyStore.Type.MEMORY);
		}
		else {
			keyStore = MemoryBasedKeyStore.getInstance(props); // It's the only one
			// available.
		}

		// integer keyFrequencyWindow
		String keyFrequencyWindowString = props.getProperty(
				"FloodControl.KeyFrequencyWindow", "180");
		try {
			setKeyFrequencyWindow(Integer.parseInt(keyFrequencyWindowString));
		}
		catch (NumberFormatException nfe) {
			throw new FloodControlInitException(
					"FloodControl.KeyFrequencyWindow property value non-integer.");
		}

		// integer keyFrequencyLimit
		String keyFrequencyLimitString = props.getProperty(
				"FloodControl.KeyFrequencyLimit", "5");
		try {
			setKeyFrequencyLimit(Integer.parseInt(keyFrequencyLimitString));
		}
		catch (NumberFormatException nfe) {
			throw new FloodControlInitException(
					"FloodControl.KeyFrequencyLimit property value non-integer.");
		}

		// boolean keyBlockException
		String keyBlockExceptionString = props.getProperty(
				"FloodControl.KeyBlockException", "true");
		setKeyBlockException(Boolean.parseBoolean(keyBlockExceptionString));

		// integer keySuppressInterval
		String keySuppressIntervalString = props.getProperty(
				"FloodControl.KeySuppressInterval", "1800");
		try {
			setKeySuppressInterval(Integer.parseInt(keySuppressIntervalString));
		}
		catch (NumberFormatException nfe) {
			throw new FloodControlInitException(
					"FloodControl.KeySuppressInterval property value non-integer.");
		}

		// integer maxConcurrentKeys
		String maxConcurrentKeysString = props.getProperty(
				"FloodControl.MaxConcurrentKeys", "250");
		try {
			setMaxConcurrentKeys(Integer.parseInt(maxConcurrentKeysString));
		}
		catch (NumberFormatException nfe) {
			throw new FloodControlInitException(
					"FloodControl.MaxConcurrentKeys property value non-integer.");
		}

		// integer cacheRefreshInterval
		String cacheRefreshIntervalString = props.getProperty(
				"FloodControl.CacheRefreshInterval", "30");
		try {
			setCacheRefreshInterval(Integer
					.parseInt(cacheRefreshIntervalString));
		}
		catch (NumberFormatException nfe) {
			throw new FloodControlInitException(
					"FloodControl.CacheRefreshInterval property value non-integer.");
		}

	}

	/**
	 * Abstract method intended to be used by whatever application is implementing flood control. The implementation of this method is delegated to the application so the individual application can choose how it develops keys from
	 * transaction data.
	 * 
	 * @param txn
	 *            some type of transaction object (XML, String, class, etc.).
	 * @return a unique transaction key derived from the object passed in.
	 * @throws KeyDerivationException
	 *             should there be a problem or inability to derive the key from the transaction.
	 */
	public abstract Object deriveKey(Object txn) throws KeyDerivationException;

	/**
	 * The primary method used to evaluate a transaction and determine if it has crossed flood control boundaries.
	 * 
	 * @param txn
	 *            some type of transaction object (XML, String, class, etc.).
	 * @throws KeyBlockException
	 *             if the transaction is now crossing flood control boundaries. (optional, configurable)
	 * @throws KeySuppressException
	 *             if the transaction has crossed flood control boundaries.
	 * @throws KeyDerivationException
	 *             should there be a problem or inability to derive the key from the transaction.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	public void evaluateTransaction(Object txn) throws KeyBlockException,
			KeySuppressException,
			KeyDerivationException,
			KeyStoreException {

		Object key = null;
		// Do math to determine the "Date" in seconds items older than should be
		// obliterated.
		Date rightNow = new Date();
		long onceUponATime = rightNow.getTime() - (keyFrequencyWindow * 1000);
		Date trailingWindowEdge = new Date(onceUponATime);

		// Perform the required, periodic updates of the key store such as
		// aging off blocked keys, aging off occurrences, etc.
		updateKeyStore(rightNow, trailingWindowEdge);

		// If flood control is not active, exit.
		if (floodControlActive == false) return;

		// If implementor provides a null transaction, then exit.
		if (txn == null) return;

		// Obtain the key from the transaction. If null, exit.
		key = deriveKey(txn);
		if (key == null) return;

		// If this is an already blocked key, throw KeySuppressException.
		if (keyStore.isBlockedKey(key)) throw new KeySuppressException();

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
		}
		else if (keyOccurrences > keyFrequencyLimit) {
			keyStore.addBlockingKeyOccurrence(key);
			if (this.keyBlockException) {
				throw new KeyBlockException();
			}
			else {
				throw new KeySuppressException();
			}

		}

		return;
	}

	/**
	 * Evaluates blocked keys and the number of keys stored to determine if action is required (such as removing a blocked key or resetting the key store because it's reached its maximum number of concurrent keys). It also refreshes the key
	 * store for general clean-up.
	 * 
	 * @param rightNow
	 *            a "snapshot" time, intended to be the time considered current. This isn't developed by the method because "now" could change by milliseconds as code is executed.
	 * @param trailingWindowEdge
	 *            value used to indicate the time when key occurrences will no longer be considered in the window for flood control consideration.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	private void updateKeyStore(Date rightNow, Date trailingWindowEdge) throws KeyStoreException {

		// Do the following tasks no more than one per second (less if volume
		// is slower than one transaction per second).
		long elapsed = rightNow.getTime() - lastTaskRefresh.getTime();
		if (elapsed >= 1000) {

			lastTaskRefresh = rightNow;

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

		// Refreshes the cache for general clean-up
		// If cacheRefreshInterval is zero, refresh the cache. Otherwise, only do
		// so if it's time.
		if (this.cacheRefreshInterval == 0) {
			keyStore.refreshKeyStore(trailingWindowEdge);
		}
		else {

			long suggestedRefresh = rightNow.getTime() - (cacheRefreshInterval * 1000);
			long lastRefresh = getLastCacheRefresh().getTime();
			if (lastRefresh <= suggestedRefresh) {
				keyStore.refreshKeyStore(trailingWindowEdge);
				setLastCacheRefresh(rightNow);
			}
		}

		return;
	}

	/**
	 * @return the cacheRefreshInterval
	 */
	public int getCacheRefreshInterval() {
		return cacheRefreshInterval;
	}

	/**
	 * @return the floodControlActive
	 */
	public boolean isFloodControlActive() {
		return floodControlActive;
	}

	/**
	 * @return the keyBlockException
	 */
	public boolean isKeyBlockException() {
		return keyBlockException;
	}

	/**
	 * @return the keyFrequencyLimit
	 */
	public synchronized int getKeyFrequencyLimit() {
		return keyFrequencyLimit;
	}

	/**
	 * @return the keyFrequencyWindow
	 */
	public int getKeyFrequencyWindow() {
		return keyFrequencyWindow;
	}

	/**
	 * @return the keySuppressInterval
	 */
	public int getKeySuppressInterval() {
		return keySuppressInterval;
	}

	/**
	 * @return the maxConcurrentKeys
	 */
	public int getMaxConcurrentKeys() {
		return maxConcurrentKeys;
	}

	/**
	 * @param cacheRefreshInterval
	 *            the cacheRefreshInterval to set
	 */
	public void setCacheRefreshInterval(int cacheRefreshIntervalIn) {
		this.cacheRefreshInterval = cacheRefreshIntervalIn;
	}

	/**
	 * @param floodControlActiveNextState
	 *            the floodControlActive next state.
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
	 * @param keyBlockException
	 *            the keyBlockException to set
	 */
	public void setKeyBlockException(boolean keyBlockExceptionIn) {
		this.keyBlockException = keyBlockExceptionIn;
	}

	/**
	 * @param keyFrequencyLimit
	 *            the keyFrequencyLimit to set
	 */
	public synchronized void setKeyFrequencyLimit(int keyFrequencyLimitIn) {
		keyFrequencyLimit = keyFrequencyLimitIn;
	}

	/**
	 * @param keyFrequencyWindow
	 *            the keyFrequencyWindow to set
	 */
	public void setKeyFrequencyWindow(int keyFrequencyWindowIn) {
		this.keyFrequencyWindow = keyFrequencyWindowIn;
	}

	/**
	 * @param keySuppressInterval
	 *            the keySuppressInterval to set
	 */
	public void setKeySuppressInterval(int keySuppressIntervalIn) {
		this.keySuppressInterval = keySuppressIntervalIn;
	}

	/**
	 * @param maxConcurrentKeys
	 *            the maxConcurrentKeys to set
	 */
	public void setMaxConcurrentKeys(int maxConcurrentKeysIn) {
		this.maxConcurrentKeys = maxConcurrentKeysIn;
	}

	/**
	 * @return the lastCacheRefresh
	 */
	public synchronized Date getLastCacheRefresh() {
		return new Date(lastCacheRefresh.getTime());
	}

	/**
	 * @param lastCacheRefresh
	 *            the lastCacheRefresh to set
	 */
	public static synchronized void setLastCacheRefresh(Date lastCacheRefreshIn) {
		lastCacheRefresh = new Date(lastCacheRefreshIn.getTime());
	}

	/**
	 * 
	 * @return the number of keys in the key store.
	 * @throws KeyStoreException
	 */
	public int getNumberOfKeysStored() throws KeyStoreException {
		return keyStore.getNumberOfKeysStored();
	}

	/**
	 * 
	 * @param propsIn
	 *            the properties to set
	 */
	private static synchronized void setProperties(Properties propsIn) {
		props = propsIn;
	}

}
