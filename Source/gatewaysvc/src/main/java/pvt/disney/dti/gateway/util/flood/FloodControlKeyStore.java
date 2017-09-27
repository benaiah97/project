package pvt.disney.dti.gateway.util.flood;

import java.util.Date;

/**
 * This interface defines the required methods for a flood control key store. The key store used to manage outstanding keys considered for flood control can vary, and this level of abstraction allows the storage implentation to change at
 * will.
 * 
 * @author lewit019
 * 
 */
public interface FloodControlKeyStore {

	/**
	 * Enumerates the different types of flood control key stores that are possible.
	 */
	public enum Type {
		MEMORY,
		DATABASE,
		SERVERREPLICATED,
		UNDEFINED
	};

	/**
	 * Retrieves all key occurrences of a certain object key. For example, if the key has occurred three times, then an array of three Dates will be returned. As a reminder, Dates are actually milliseconds since a predetermined point, so
	 * the Date class is serving as a more a Time class in a real-time transaction scenario.
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @return an array of all of the occurrences of that key presently in memory.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	Date[] retrieveKeyOccurrences(Object key) throws KeyStoreException;

	/**
	 * Retrieves a count of all key occurrences of a certain object key.
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @return a count of all key occurrences
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	int countKeyOccurrences(Object key) throws KeyStoreException;

	/**
	 * Store a new object key in the keystore and mark its first occurrence. Note: If storeNewKey is called when there is already a matching key in the keyStore, then the existing occurrences of the prior key will be added to the newly
	 * created key's history.
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	void storeNewKey(Object key) throws KeyStoreException;

	/**
	 * Stores a new occurrence of an already existing key. Note: If addKeyOccurrence is called when there is no matching key in the keyStore, then the keyStore will default to a normal storeNewKey behavior. This handles situations where an
	 * existing key ages out just before a new key occurrence arrives.
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	void addKeyOccurrence(Object key) throws KeyStoreException;

	/**
	 * Marks a key as "blocked" (i.e. it's crossed the line). Note: If addBlockingKeyOccurrence is called when there is no matching key in the keyStore, then the keyStore will default to a normal storeNewKey (which, of course, is
	 * non-blocked). This handles situations where a block qualifying key ages out just before a new key occurrence arrives.
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	void addBlockingKeyOccurrence(Object key) throws KeyStoreException;

	/**
	 * Traverses the keystore removing occurrences older than the date provided. If a key has no more occurrences left on its record, then the key is also removed from the keystore.
	 * 
	 * @param aDate
	 *            the date/time by which a judgement will be made to remove values from the keystore. Note that the date is exclusively evaluated, not inclusively evaluated, so transactions with this exact date/time would not be removed.
	 * @return The numerical count of occurrences removed.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	int refreshKeyStore(Date aDate) throws KeyStoreException;

	/**
	 * Removes occurrences older than the date provided on the exact key specified. If a key has no more occurrences left on its record, then the key is also removed from the keystore.
	 * 
	 * @param aDate
	 *            the date/time by which a judgement will be made to remove values
	 * @param key
	 *            an object that uniquely identifies a transaction under consideration for flood control.
	 * @return The numerical count of occurrences removed.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	int clearOccurrencesOlderThan(Date aDate, Object key) throws KeyStoreException;

	/**
	 * Resets the keystore to its initial state. This is intended to zero out the memory or database usage to its absolute minimum (delete from keystore_table all records, etc.).
	 * 
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	void resetKeyStore() throws KeyStoreException;

	/**
	 * Provides the numerical count of the keys presently stored in the keystore.
	 * 
	 * @return the numerical count of the keys presently stored in the keystore.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	int getNumberOfKeysStored() throws KeyStoreException;

	/**
	 * Returns a status on a particular key, answering the question: Is this a blocked key?
	 * 
	 * @param key
	 *            an object that uniquely identifies a transaction.
	 * @return true if the key is blocked, false if not.
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	boolean isBlockedKey(Object key) throws KeyStoreException;

	/**
	 * Removes all keys that were blocked before the specified date. Note that the date is exclusively evaluated, not inclusively evaluated, so transactions with this exact date/time would not be removed.
	 * 
	 * @param aDate
	 *            the date/time by which a judgement will be made to remove values
	 * @throws KeyStoreException
	 *             for any mechanical error experienced while performing this operation.
	 */
	void removeBlockedKeysOlderThan(Date aDate) throws KeyStoreException;

}
