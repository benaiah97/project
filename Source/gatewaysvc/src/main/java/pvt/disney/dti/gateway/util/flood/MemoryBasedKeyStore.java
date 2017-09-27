package pvt.disney.dti.gateway.util.flood;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

/**
 * A memory-based implementation of a FloodControlKeyStore.
 * 
 * @author lewit019
 */
public class MemoryBasedKeyStore implements FloodControlKeyStore {

   /** The in-memory representation of the keystore. */
   private Hashtable<Object, KeyRecord> keyStore;
   /** The maximum number of concurrent keys. */
   private int maxConcurrentKeys = 0;
   /**
    * The over-allocation percentage of the keystore used during initialization.
    */
   private static final double DATA_STRUCTURE_SCALING_FACTOR = 1.5;
   /** The default size, if none is specified. */
   private static final int DEFAULT_DATA_STRUCTURE_SIZE = 250;
   /** The minimum hashtable size that can be specified. */
   private static final int MINIMUM_HASHTABLE_SIZE = 50;
   /** Object instance variable supporting the singleton pattern. */
   private static MemoryBasedKeyStore obj = null;
   /** Boolean to indicate if a thread is refreshing the keystore. */
   private boolean refreshingKeyStore = false;
   /** A boolean to indicate if a thread is clearing blocks. */
   private boolean clearingBlocks = false;

   /**
    * Gets the single instance of MemoryBasedKeyStore.
    * 
    * Note: Package level access here is intentional. (The absence of public,
    * private, or protected.) Callers are directed to use the
    * FloodControlKeyStoreFactory to create key stores.
    * 
    * @param maxConKeysthe max ConcurrentKeys keys
    * @return single instance of MemoryBasedKeyStore
    * @throws FloodControlInitException the flood control init exception
    */
   static MemoryBasedKeyStore getInstance(Integer maxConKeys) throws FloodControlInitException {
      if (obj == null) {
         obj = new MemoryBasedKeyStore(maxConKeys);
      }
      return obj;
   }

   /**
    * Note: Package level access here is intentional. (The absence of public,
    * private, or protected.) Callers are directed to use the
    * FloodControlKeyStoreFactory to create key stores.
    * 
    * @return a fully initialized keystore.
    * @throws FloodControlInitException
    *            for any mechanical fault that occurs during initialization.
    */
   static MemoryBasedKeyStore getInstance() throws FloodControlInitException {
      if (obj == null) {
         obj = new MemoryBasedKeyStore();
      }
      return obj;
   }

   /**
    * Private constructor for the MemoryBasedKeyStore, enforces use of the
    * Singleton pattern.
    * 
    * @param maxConKeys
    *           the max ConcurrentKeys keys
    * @throws FloodControlInitException
    *            the flood control init exception
    */
   private MemoryBasedKeyStore(Integer maxConKeys) throws FloodControlInitException {

      setMaxConcurrentKeys(maxConKeys);
      if (maxConcurrentKeys < MINIMUM_HASHTABLE_SIZE) {
         throw new FloodControlInitException("FloodControl.MaxConcurrentKeys property value below minimum of "
                  + MINIMUM_HASHTABLE_SIZE + ".");
      }
      try {
         initialize();
      } catch (KeyStoreException kse) {
         throw new FloodControlInitException("Unable to recreate flood control keyStore." + kse.toString());
      }

   }

   /**
    * Private constructor for the MemoryBasedKeyStore, enforces use of the
    * Singleton pattern.
    * 
    * @throws FloodControlInitException
    *            for any mechanical fault that occurs during initialization.
    */
   private MemoryBasedKeyStore() throws FloodControlInitException {
      try {
         maxConcurrentKeys = DEFAULT_DATA_STRUCTURE_SIZE;
         initialize();
      } catch (KeyStoreException kse) {
         throw new FloodControlInitException("Unable to recreate flood control keyStore." + kse.toString());
      }
   }

   /**
    * Creates (or resets) the memory keystore. In this case, the keystore is a
    * Hashtable.
    * 
    * @throws KeyStoreException
    *            for any mechanical error experienced while performing this
    *            operation.
    */
   private synchronized void initialize() throws KeyStoreException {
      Double scalingFactor = new Double(maxConcurrentKeys * DATA_STRUCTURE_SCALING_FACTOR);
      int keyStoreSize = scalingFactor.intValue();
      keyStore = new Hashtable<Object, KeyRecord>(keyStoreSize);
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#resetKeyStore()
    */
   public synchronized void resetKeyStore() throws KeyStoreException {
      initialize();
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#addKeyOccurrence(Object
    *      key)
    */
   public synchronized void addKeyOccurrence(Object key) throws KeyStoreException {
      addKeyOccurrence(key, false);
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#addBlockingKeyOccurrence(Object
    *      key)
    */
   public synchronized void addBlockingKeyOccurrence(Object key) throws KeyStoreException {
      addKeyOccurrence(key, true);
   }

   /**
    * This method is meant to be called internally by another method which has
    * already established a lock.
    * 
    * @param key
    *           an object that uniquely identifies a transaction under
    *           consideration for flood control.
    * @param isBlocked
    *           flag indicating if the key should now be blocked.
    * @throws KeyStoreException
    *            for any mechanical error experienced while performing this
    *            operation.
    */
   private void addKeyOccurrence(Object key, boolean isBlocked) throws KeyStoreException {
      KeyRecord aKeyRecord = keyStore.get(key);

      if (aKeyRecord == null) {
         storeNewKey(key);
         return;
      }

      if (isBlocked)
         aKeyRecord.setBlockedKey(isBlocked);

      Date rightNow = new Date();
      aKeyRecord.addOccurrence(rightNow);
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#retrieveKeyOccurrences(Object
    *      key)
    */
   public Date[] retrieveKeyOccurrences(Object key) throws KeyStoreException {

      KeyRecord aKeyRecord = keyStore.get(key);
      Date[] dateList = aKeyRecord.getOccurrences();

      if (dateList.length == 0)
         return new Date[0];

      return dateList;
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#storeNewKey(Object
    *      key)
    */
   public synchronized void storeNewKey(Object key) throws KeyStoreException {
      KeyRecord aNewKeyRecord = new KeyRecord();
      KeyRecord anOldKeyRecord = keyStore.put(key, aNewKeyRecord);

      try {

         // We "hope" all keys called here are new, but if there is heavy
         // traffic, that may not be true. Make an attempt to preserve old
         // occurrences if they exist. It's up to the user of the
         // MemoryBasedKeyStore
         // upon a later evaluation to determine if the blocking threshold has
         // been
         // passed.
         if (anOldKeyRecord == null)
            return;
         else {
            Date[] previous = anOldKeyRecord.getOccurrences();
            int count = previous.length;

            for (int i = 0; i < count; i++) {
               aNewKeyRecord.addOccurrence(previous[i]);
            }
         }

      } catch (IndexOutOfBoundsException ioobe) {
         ioobe.printStackTrace();
      }

   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#getNumberOfKeysStored()
    */
   public int getNumberOfKeysStored() throws KeyStoreException {
      return keyStore.size();
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#clearOccurrencesOlderThan(Date
    *      expireTime, Object key)
    */
   public int clearOccurrencesOlderThan(Date expireTime, Object key) throws KeyStoreException {

      KeyRecord aKeyRecord = keyStore.get(key);
      int numberCleared = 0;

      // If this record has just been removed by another thread, then exit.
      if (aKeyRecord == null)
         return 0;
      else
         numberCleared = aKeyRecord.getNumberOfOccurrences();

      // if this record is in a blocked state, then block method calls have to
      // remove the block.
      if (aKeyRecord.isBlockedKey()) {
         return 0;
      }

      try {
         numberCleared = aKeyRecord.clearOccurrencesOlderThan(expireTime, key);
      } catch (EmptyKeyException eke) {
         keyStore.remove(key);
      }

      return numberCleared;
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#refreshKeyStore(Date
    *      occurrenceExpireTime)
    */
   public int refreshKeyStore(Date occurrenceExpireTime) throws KeyStoreException {

      int howManyCleared = 0;

      if (setRefreshingKeyStore() == false)
         return 0;

      try {

         // The next line forces Java to make an independent copy of the keys.
         // That should permit this block to remain unsync'd but still safe.
         // See the Collection.toArray() method for more information.
         Object[] keyArray = keyStore.keySet().toArray();

         for (int i = 0; i < keyArray.length; i++) {
            howManyCleared = clearOccurrencesOlderThan(occurrenceExpireTime, keyArray[i]);
         }
      } finally {
         unsetRefreshingKeyStore();
      }

      return howManyCleared;

   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#countKeyOccurrences(Object
    *      key)
    */
   public int countKeyOccurrences(Object key) throws KeyStoreException {

      KeyRecord aKeyRecord = keyStore.get(key);

      if (aKeyRecord == null)
         return 0;

      int count = aKeyRecord.getNumberOfOccurrences();
      return count;
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#isBlockedKey(Object
    *      key)
    */
   public boolean isBlockedKey(Object key) throws KeyStoreException {
      KeyRecord aKeyRecord = keyStore.get(key);

      if (aKeyRecord == null)
         return false;
      else
         return aKeyRecord.isBlockedKey();
   }

   /**
    * @see pvt.disney.dti.gateway.util.flood.FloodControlKeyStore#removeBlockedKeysOlderThan(Date
    *      blockExpireTime)
    */
   public void removeBlockedKeysOlderThan(Date blockExpireTime) throws KeyStoreException {

      if (setClearingBlocks() == false) {
         return;
      }

      int blockedKeysFound = 0;
      int blockedKeysRemoved = 0;

      try {

         // The next line forces Java to make an independent copy of the keys.
         // That should permit this block to remain unsync'd but still safe.
         // See the Collection.toArray() method for more information.
         Object[] keyArray = keyStore.keySet().toArray();

         for (int i = 0; i < keyArray.length; i++) {

            KeyRecord aKeyRecord;

            synchronized (this) {
               aKeyRecord = keyStore.get(keyArray[i]);

               // If this record has just been removed by another thread, then
               // exit.
               if (aKeyRecord == null)
                  continue;

               // if this record is not in a blocked state, then move to the
               // next
               // record.
               if (!aKeyRecord.isBlockedKey()) {
                  continue;
               }
            } // End synchronized block

            blockedKeysFound++;
            Date lastDate = aKeyRecord.getBlockedAt();
            if (lastDate == null) {
               keyStore.remove(keyArray[i]);
               blockedKeysRemoved++;
            } else if (lastDate.getTime() < blockExpireTime.getTime()) {
               keyStore.remove(keyArray[i]);
               blockedKeysRemoved++;
            }

         }
      } finally {
         unsetClearingBlocks();
      }

   }

   /**
    * Returns the maximum number of concurrent keys permitted in this keystore.
    * 
    * @return the maxConcurrentKeys
    */
   public int getMaxConcurrentKeys() {
      return maxConcurrentKeys;
   }

   /**
    * @param maxConcurrentKeys
    *           the maxConcurrentKeys to set
    */
   public void setMaxConcurrentKeys(int maxConcurrentKeys) {
      this.maxConcurrentKeys = maxConcurrentKeys;
   }

   /**
    * Synchronized method to ensure a lock is released when refreshing the
    * keystore.
    */
   private synchronized void unsetRefreshingKeyStore() {
      refreshingKeyStore = false;
      return;
   }

   /**
    * Synchronized method to ensure atomic, non-parallel key store refreshes,
    * ensuring only one thread gets the lock.
    * 
    * @return
    */
   private synchronized boolean setRefreshingKeyStore() {
      if (refreshingKeyStore == true)
         return false;
      else {
         refreshingKeyStore = true;
         return true;
      }
   }

   /**
    * Synchronized method to ensure a lock is released when clearing blocked
    * keys.
    * 
    */
   private synchronized void unsetClearingBlocks() {
      clearingBlocks = false;
      return;
   }

   /**
    * Synchronized method to ensure atomic, non-parallel clearing of blocked
    * keys, ensuring only one thread gets the lock.
    */
   private synchronized boolean setClearingBlocks() {
      if (clearingBlocks == true)
         return false;
      else {
         clearingBlocks = true;
         return true;
      }
   }

   /**
    * Exception used only within the confines of the MemoryBasedKeyStore to show
    * that key is not empty of all occurrences.
    * 
    * @author lewit019
    * 
    */
   private class EmptyKeyException extends Exception {

      private static final long serialVersionUID = 6881848514563583450L;

      private EmptyKeyException() {
         super();
      }
   }

   /**
    * A class that encapsulates a key record within the confines of the
    * MemoryBasedKeyStore. This class is internal to ensure adequate
    * encapsulation and that visibility of the innerworkings of the
    * implementation remain opaque.
    * 
    * @author lewit019
    */
   private class KeyRecord {

      private ArrayList<Date> occurrences = new ArrayList<Date>();
      private boolean isBlocked = false;
      private Date blockedAt = null;

      /**
       * Constructor that creates each key record with with one occurrence
       * record already pre-populated.
       */
      public KeyRecord() {
         Date rightNow = new Date();
         occurrences.add(rightNow);
      }

      /**
       * Returns a boolean indicating the key is blocked.
       * 
       * @return the isBlocked
       */
      public synchronized boolean isBlockedKey() {
         return isBlocked;
      }

      /**
       * Sets the key to blocked if the parameter is set to true. If it is set
       * to true, it records the date/time it was blocked so other processes can
       * obtain that information.
       * 
       * @param isBlocked
       *           the isBlocked to set
       */
      public synchronized void setBlockedKey(boolean isBlockedIn) {
         isBlocked = isBlockedIn;
         if (isBlocked == true) {
            blockedAt = new Date();
         } else {
            blockedAt = null;
         }
      }

      /**
       * Returns the number of occurrences on this key record.
       * 
       * @return
       */
      public synchronized int getNumberOfOccurrences() {
         return occurrences.size();
      }

      /**
       * Returns an array of all occurrences on this key record.
       * 
       * @return the occurrences
       */
      public synchronized Date[] getOccurrences() {

         Date[] dateArray = new Date[occurrences.size()];

         for (int i = 0; i < occurrences.size(); i++) {
            dateArray[i] = occurrences.get(i);
         }
         return dateArray;
      }

      /**
       * Adds a new occurrence to this key record.
       * 
       * @param newOccurrence
       */
      public synchronized void addOccurrence(Date newOccurrence) {
         occurrences.add(newOccurrence);
      }

      /**
       * Clears occurrences on a key record older than the expire time
       * specified.
       * 
       * @param expireTime
       *           the date/time by which a judgement will be made to remove
       *           values
       * @param key
       *           an object that uniquely identifies a transaction under
       *           consideration for flood control.
       * @return the number of occurrences cleared.
       * @throws EmptyKeyException
       *            if no more occurrences exist on the key record.
       */
      public synchronized int clearOccurrencesOlderThan(Date expireTime, Object key) throws EmptyKeyException {

         boolean stop = false;
         int numberCleared = 0;
         int index = 0;

         // If any occurrence time is older (less) than the expireTime, age it
         // off.
         while (stop == false) {

            if (occurrences.size() == 0)
               break;

            if (occurrences.get(index).getTime() < expireTime.getTime()) {
               occurrences.remove(index);
               numberCleared++;
               if (occurrences.size() == 0)
                  stop = true;
            } else {
               index++;
               if (index == occurrences.size())
                  stop = true;
            }

         }

         // If there are no more recorded date occurrences, delete the record.
         if (occurrences.size() == 0) {
            throw new EmptyKeyException();
         }

         return numberCleared;
      }

      /**
       * Returns the time/date the record was blocked at.
       * 
       * @return the blockedAt
       */
      synchronized Date getBlockedAt() {
         return blockedAt;
      }

   }

}
