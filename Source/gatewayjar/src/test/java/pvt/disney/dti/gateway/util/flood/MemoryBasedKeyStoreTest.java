package pvt.disney.dti.gateway.util.flood;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the memory based keystore.
 * 
 * @author lewit019
 */
public class MemoryBasedKeyStoreTest {

  private static int testNumber = 0;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Tests properties.
   * 
   */
  @Test
  public final void testGetInstanceProperties() {
    Properties props = new Properties();
    /*Test non-numeric property*/
    props.setProperty("FloodControl.MaxConcurrentKeys", "Bob");
    MemoryBasedKeyStore keyStore = null;
    try {
      keyStore = MemoryBasedKeyStore.getInstance(props);
    } catch (FloodControlInitException fcie) {
    	 Assert.assertEquals("FloodControl.MaxConcurrentKeys property value non-integer.",fcie.getMessage());
    }
   /*Test sub-minimum property*/
    props.setProperty("FloodControl.MaxConcurrentKeys", "5");
    keyStore = null;
    try {
      keyStore = MemoryBasedKeyStore.getInstance(props);
    } catch (FloodControlInitException fcie) {
    	 Assert.assertEquals("FloodControl.MaxConcurrentKeys property value below minimum of 50.",fcie.getMessage());
    }
    props.setProperty("FloodControl.MaxConcurrentKeys", "200");
    keyStore = null;
    try {
      keyStore = MemoryBasedKeyStore.getInstance(props);
    } catch (FloodControlInitException fcie) {
    	Assert.fail("Exception on getInstance: " + fcie.toString());
    }
    if (keyStore == null) {
    	Assert.fail("Null instance returned");
    }

  }

  /**
   * Tests get instance.
   * 
   */
  @Test
  public final void testGetInstance() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();
    } catch (FloodControlInitException fcie) {
    	Assert.fail("Exception: " + fcie.toString());
    }

    if (keyStore == null) {
    	Assert.fail("Null instance returned on MemoryBasedKeyStore.getInstance()");
    }

  }

  /**
   * Tests keystore reset.
   */
  @Test
  public final void testResetKeyStore() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();

      /*Test initial storage*/
      String key = new String("testResetKeyStore" + getNewTestNumber());
      keyStore.storeNewKey(key);
      keyStore.addKeyOccurrence(key);
      keyStore.addKeyOccurrence(key);
      keyStore.addKeyOccurrence(key);
      int occurrenceCountBefore = keyStore.countKeyOccurrences(key);

      if (occurrenceCountBefore != 4) {
    	  Assert.fail("Adding occurrences to a KeyRecord resulted in an invalid count: "
            + occurrenceCountBefore + " (should be 4)");
      }

      keyStore.resetKeyStore();

      int occurrenceCountAfter = keyStore.countKeyOccurrences(key);

      if (occurrenceCountAfter != 0) {
    	  Assert.fail("Resetting keyStore resulted in an invalid occurrence count: " + occurrenceCountAfter
            + " (should be 0)");
      }

      int countRecords = keyStore.getNumberOfKeysStored();

      if (countRecords != 0) {
    	  Assert.fail("Resetting keyStore resulted in an invalid record count: " + countRecords
            + " (should be 0)");
      }

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * Tests the adding of key occurrences.
   */
  @Test
  public final void testAddKeyOccurrence() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();

     /*Test initial storage*/
      String key = new String("testAddKeyOccurrence" + getNewTestNumber());
      keyStore.addKeyOccurrence(key);
      int count = keyStore.countKeyOccurrences(key);
      if (count != 1)
    	  Assert.fail("Adding a missing key resulted in an invalid count: " + count + " (should be 1)");

      /*Test key overwrite*/
      keyStore.addKeyOccurrence(key);
      keyStore.addKeyOccurrence(key);
      keyStore.addKeyOccurrence(key);
      count = keyStore.countKeyOccurrences(key);
      if (count != 4)
    	  Assert.fail("Adding occurrences to a KeyRecord resulted in an invalid count: " + count
            + " (should be 4)");

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * Tests the adding of blocking key occurrences.
   */
  @Test
  public final void testAddBlockingKeyOccurrence() {
    MemoryBasedKeyStore keyStore = null;

    try {
     /*Test adding a "blocked" key with no instances*/
      keyStore = MemoryBasedKeyStore.getInstance();
      String key = new String("testAddBlockingKeyOccurrence" + getNewTestNumber());
      keyStore.addBlockingKeyOccurrence(key);

      boolean isBlockedKey = keyStore.isBlockedKey(key);
      if (isBlockedKey)
    	  Assert.fail("Adding blocked keys with no prior records should result in an unblocked 1st occurrence.");

     /*Test adding a "blocked" key with prior occurrences*/
      keyStore.addBlockingKeyOccurrence(key);
      isBlockedKey = keyStore.isBlockedKey(key);
      if (!isBlockedKey)
    	  Assert.fail("Adding blocked key with prior occurence should result in a blocked key.");

      int count = keyStore.countKeyOccurrences(key);
      if (count != 2) {
    	  Assert.fail("Adding a blocked key should result in another occurrence.");
      }

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }

  }

  /**
   * Test retrieve key occurrences.
   */
  @Test
  public final void testRetrieveKeyOccurrences() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();
      String key = new String("testRetrieveKeyOccurrences" + getNewTestNumber());

      for (int i = 0; i < 1000; i++) {
        keyStore.addKeyOccurrence(key);
        Thread.sleep(5);
      }

      Date[] occurs = keyStore.retrieveKeyOccurrences(key);

      if (occurs.length != 1000) {
    	  Assert.fail("Improper number of occurences of a key returned.");
      }

      Date firstDate = occurs[0];
      Date lastDate = occurs[999];

      if (firstDate.getTime() == lastDate.getTime()) {
    	  Assert.fail("All occurrences returned have same time.");
      }
    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }

  }

  /**
   * Test store new key.
   */
  @Test
  public final void testStoreNewKey() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();

      /*Test initial storage*/
      String key = new String("testStoreNewKey" + getNewTestNumber());
      keyStore.storeNewKey(key);
      int count = keyStore.countKeyOccurrences(key);
      if (count != 1)
    	  Assert.fail("Storing a new key resulted in an invalid count: " + count + " (should be 1)");

      /*Test key overwrite*/
      keyStore.addKeyOccurrence(key);
      keyStore.addKeyOccurrence(key);
      keyStore.storeNewKey(key);
      count = keyStore.countKeyOccurrences(key);
      if (count != 4)
    	  Assert.fail("Recovering an overwritten KeyRecord resulted in an invalid count: " + count
            + " (should be 4)");

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }

  }

  /**
   * Test get number of keys stored.
   */
  @Test
  public final void testGetNumberOfKeysStored() {

    MemoryBasedKeyStore keyStore = null;
    try {
      keyStore = MemoryBasedKeyStore.getInstance();
      keyStore.resetKeyStore();
      for (int i = 0; i < 10000; i++) {
        String key = new String("testGetNumberOfKeysStored" + getNewTestNumber() + "iter" + i);
        keyStore.storeNewKey(key);
      }
      int numberOfKeys = keyStore.getNumberOfKeysStored();
      keyStore.resetKeyStore();
      if (numberOfKeys != 10000) {
    	  Assert.fail("Number of keys stored did not match number inserted.");
      }
    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * Test clear occurrences older than.
   */
  @Test
  public final void testClearOccurrencesOlderThan() {
    MemoryBasedKeyStore keyStore = null;

    try {
      keyStore = MemoryBasedKeyStore.getInstance();
      keyStore.resetKeyStore();
      String key = new String("testClearOccurrencesOlderThan" + getNewTestNumber());

      for (int i = 0; i < 9; i++) {
        keyStore.addKeyOccurrence(key);
        Thread.sleep(5);
      }
      Date[] occurs = keyStore.retrieveKeyOccurrences(key);
      Date midPoint = occurs[4];
      keyStore.clearOccurrencesOlderThan(midPoint, key);
      int count = keyStore.countKeyOccurrences(key);
      if (count != 5) {
    	  Assert.fail("Number of occurences after midPoint age off (" + count
            + ") does not match expected of 5.");
      }
      Date rightNow = new Date();
      keyStore.clearOccurrencesOlderThan(rightNow, key);

      count = keyStore.countKeyOccurrences(key);
      if (count != 0) {
    	  Assert.fail("Number of occurences after total age off (" + count
            + ") does not match expected of 0.");
      }
      for (int i = 0; i < 9; i++) {
        keyStore.addKeyOccurrence(key);
        Thread.sleep(5);
      }
      keyStore.addBlockingKeyOccurrence(key);
      rightNow = new Date();
      int countBefore = keyStore.countKeyOccurrences(key);
      keyStore.clearOccurrencesOlderThan(rightNow, key);
      count = keyStore.countKeyOccurrences(key);
      if (count != countBefore) {
    	  Assert.fail("Method was allowed to delete occurrences of a blocked key.  Illegal operation for this method.");
      }
    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * Test refresh key store.
   */
  @Test
  public final void testRefreshKeyStore() {
    MemoryBasedKeyStore keyStore = null;

    String key = new String("testRefreshKeyStore" + getNewTestNumber());

    try {
      keyStore = MemoryBasedKeyStore.getInstance();
      keyStore.resetKeyStore();
      String key01 = new String(key + "01");
      String key02 = new String(key + "02");
      String key03 = new String(key + "03");

      for (int i = 0; i < 10; i++) {
        keyStore.addKeyOccurrence(key01);
        keyStore.addKeyOccurrence(key02);
        keyStore.addKeyOccurrence(key03);
        Thread.sleep(5);
      }

      Date midPoint = new Date();

      for (int i = 0; i < 10; i++) {
        keyStore.addKeyOccurrence(key01);
        keyStore.addKeyOccurrence(key02);
        keyStore.addKeyOccurrence(key03);
        Thread.sleep(5);
      }

      keyStore.refreshKeyStore(midPoint);
      Thread.sleep(5);

      int count01 = keyStore.countKeyOccurrences(key01);
      int count02 = keyStore.countKeyOccurrences(key02);
      int count03 = keyStore.countKeyOccurrences(key03);

      if ((count01 != 10) || (count02 != 10) || (count03 != 10)) {
    	  Assert.fail("Refreshing three keys resulted in incorrect number of remaining occurrences ("
            + count01 + ", " + count02 + ", " + count03 + ": should be 10).");
      }

      Date rightNow = new Date();
      keyStore.refreshKeyStore(rightNow);
      count01 = keyStore.countKeyOccurrences(key01);
      count02 = keyStore.countKeyOccurrences(key02);
      count03 = keyStore.countKeyOccurrences(key03);

      if ((count01 != 0) || (count02 != 0) || (count03 != 0)) {
    	  Assert.fail("Refreshing three keys resulted in incorrected removal of occurrences (" + count01
            + ", " + count02 + ", " + count03 + ": should be 0).");
      }

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }

  }

  /**
   * Test is blocked key.
   */
  @Test
  public final void testIsBlockedKey() {
    MemoryBasedKeyStore keyStore = null;

    try {
      /*Test adding a "blocked" key with no instances*/
      keyStore = MemoryBasedKeyStore.getInstance();
      keyStore.resetKeyStore();
      String key = new String("testIsBlockedKey" + getNewTestNumber());
      keyStore.storeNewKey(key);

      boolean isBlockedKey = keyStore.isBlockedKey(key);
      if (isBlockedKey)
    	  Assert.fail("Adding keys with no prior records should result in an unblocked 1st occurrence.");

      /*Test adding a "blocked" key with prior occurrences*/
      keyStore.addBlockingKeyOccurrence(key);
      isBlockedKey = keyStore.isBlockedKey(key);
      if (!isBlockedKey)
    	  Assert.fail("Adding blocked key with prior occurence should result in a blocked key.");

      isBlockedKey = keyStore.isBlockedKey(new String("Unknown Key"));
      if (isBlockedKey) {
        fail("Should not return true on an unknown key.");
      }

    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * Test remove blocked keys older than.
   */
  @Test
  public final void testRemoveBlockedKeysOlderThan() {
    MemoryBasedKeyStore keyStore = null;

    String key = new String("testRemoveBlockedKeysOlderThan" + getNewTestNumber());

    try {
      keyStore = MemoryBasedKeyStore.getInstance();
      keyStore.resetKeyStore();
      String key01 = new String(key + "01");
      String key02 = new String(key + "02");
      String key03 = new String(key + "03");

      keyStore.addKeyOccurrence(key01);
      keyStore.addKeyOccurrence(key01);
      keyStore.addKeyOccurrence(key01);
      keyStore.addBlockingKeyOccurrence(key01);
      Thread.sleep(5);

      Date midPoint01 = new Date();
      keyStore.addKeyOccurrence(key02);
      keyStore.addKeyOccurrence(key02);
      keyStore.addKeyOccurrence(key02);
      keyStore.addBlockingKeyOccurrence(key02);
      Thread.sleep(5);
      Date midPoint02 = new Date();
      keyStore.addKeyOccurrence(key03);
      keyStore.addKeyOccurrence(key03);
      keyStore.addKeyOccurrence(key03);
      keyStore.addBlockingKeyOccurrence(key03);
      Thread.sleep(5);

      keyStore.removeBlockedKeysOlderThan(midPoint01);

      if (keyStore.isBlockedKey(key01)) {
    	  Assert.fail("Blocked key older than time specified should have been removed (01).");
      }
      if ((!keyStore.isBlockedKey(key02)) || (!keyStore.isBlockedKey(key03))) {
    	  Assert.fail("Blocked keys newer than time specified should have remained blocked(02,03).");
      }

      keyStore.removeBlockedKeysOlderThan(midPoint02);

      if ((keyStore.isBlockedKey(key01) || (keyStore.isBlockedKey(key02)))) {
    	  Assert.fail("Blocked key older than time specified should have been removed. (01,02)");
      }

      if (!keyStore.isBlockedKey(key03)) {
    	  Assert.fail("Blocked keys newer than time specified should have remained blocked. (03)");
      }
      Date rightNow = new Date();
      keyStore.removeBlockedKeysOlderThan(rightNow);
      if (keyStore.isBlockedKey(key01) || keyStore.isBlockedKey(key02)
          || keyStore.isBlockedKey(key03)) {
    	  Assert.fail("Blocked key older than time specified should have been removed. (01,02,03)");
      }
    } catch (Exception e) {
    	Assert.fail("Exception: " + e.toString());
    }
  }

  /**
   * @return the testNumber
   */
  private static synchronized int getNewTestNumber() {
    return testNumber++;
  }

}
