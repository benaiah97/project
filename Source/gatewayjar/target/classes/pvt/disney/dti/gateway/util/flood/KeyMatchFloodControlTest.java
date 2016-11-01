package pvt.disney.dti.gateway.util.flood;

import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Full Junit test class for KeyMatchFloodControl functionality (not related to volume testing).
 * @author lewit019
 *
 */
public class KeyMatchFloodControlTest {

  private static int testNumber = 0;
  
  private static KeyMatchFloodControl floodControl;
  public static final String KEYFREQUENCYWINDOW = "6";
  public static final String KEYFREQUENCYLIMIT = "3";
  public static final String KEYSUPPRESSINTERVAL = "9";
  public static final String MAXCONCURRENTKEYS = "60";
  public static final String CACHEREFRESHINTERVAL = "2";

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
   * Tests property editing.
   *
   */
  @Test
  public final void testKeyMatchFloodControlProperties() {

    Properties props = new Properties();
    // The properties are not tested; they default to a value without error.
    props.setProperty("FloodControl.KeyStoreType", "InMemory");
    props.setProperty("FloodControl.Active", "true");
    props.setProperty("FloodControl.KeyBlockException", "true");

    // Test numeric parameter constraints
    try {
      props.setProperty("FloodControl.KeyFrequencyWindow", "notanumber");
      floodControl = new TestImplementor(props);
      fail("Did not throw exception on bad KeyFrequencyWindow.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.KeyFrequencyLimit", "notanumber");
      floodControl = new TestImplementor(props);
      fail("Did not throw exception on bad KeyFrequencyLimit.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.KeySuppressInterval", "notanumber");
      floodControl = new TestImplementor(props);
      fail("Did not throw exception on bad KeySuppressInterval.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.MaxConcurrentKeys", "notanumber");
      floodControl = new TestImplementor(props);
      fail("Did not throw exception on bad MaxConcurrentKeys.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.CacheRefreshInterval", "notanumber");
      floodControl = new TestImplementor(props);
      fail("Did not throw exception on bad CacheRefreshInterval.");
    } catch (FloodControlInitException fcie) {
    }

    //
    props.setProperty("FloodControl.KeyFrequencyWindow", KEYFREQUENCYWINDOW); // seconds
    props.setProperty("FloodControl.KeyFrequencyLimit", KEYFREQUENCYLIMIT);
    props.setProperty("FloodControl.KeySuppressInterval", KEYSUPPRESSINTERVAL); // seconds
    props.setProperty("FloodControl.MaxConcurrentKeys", MAXCONCURRENTKEYS);
    props.setProperty("FloodControl.CacheRefreshInterval", CACHEREFRESHINTERVAL); // seconds

    try {
      floodControl = new TestImplementor(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }

    return;

  }

  /** 
   * Tests key derivation.
   *
   */
  @Test
  public final void testDeriveKey() {

    String newKey;
    String txnIn = new String("key");

    try {
      newKey = (String) floodControl.deriveKey(txnIn);
      if (!newKey.equals(txnIn))
        fail("Key passed in doesn't match what was expected.");
    } catch (KeyDerivationException kde) {
      fail("Valid key caused KeyDerivationException");
    }

    try {
      newKey = (String) floodControl.deriveKey(new String(""));
      fail("Invalid key caused no KeyDerivationException");
    } catch (KeyDerivationException kde) {
    }

  }

  /**
   * Tests the non-blocked lifecycle.
   */
  @Test
  public final void testNonBlockedLifecycle() {

    int testNumber = getNewTestNumber();
    String test = new String("Test" + testNumber);

    // Run three occurrences back-to-back (should be legal)
    // Relies on the KeyFrequencyLimit property above being set to 3.
    try {
      int numberOfAllowableOccurences = Integer.parseInt(KEYFREQUENCYLIMIT);
      for (int i = 0; i < numberOfAllowableOccurences; i++) {
        floodControl.evaluateTransaction(test);
      }
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

    // Wait out the Key Frequency Window
    try {
      Thread.sleep((Integer.parseInt(KEYFREQUENCYWINDOW) * 1000) + 50);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }

    // This should be a valid call, all three of the previous occurrences should
    // have aged out.
    try {
      floodControl.evaluateTransaction(test);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

  }

  /**
   * Test the standard blocked lifecycle
   *
   */
  @Test
  public final void testBlockedLifecycle() {

    int testNumber = getNewTestNumber();
    String test = new String("Test" + testNumber);

    // Run three occurrences back-to-back (should be legal)
    // Relies on the KeyFrequencyLimit property above being set to 3.
    try {
      int numberOfAllowableOccurences = Integer.parseInt(KEYFREQUENCYLIMIT);
      for (int i = 0; i < numberOfAllowableOccurences; i++) {
        floodControl.evaluateTransaction(test);
      }
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

    // This should be a blocking call
    try {
      floodControl.evaluateTransaction(test);
      fail("KeyBlockException should have been generated.");
    } catch (KeySuppressException kse) {
      fail("KeySuppressException occurred, but should have been KeyBlockException instead.");
    } catch (KeyBlockException kbe) {
      // This is the expected result.
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException occurred, but should have been KeyBlockException instead.");
    } catch (KeyStoreException kse) {
      fail("KeyStoreException occurred, but should have been KeyBlockException instead.");
    }

    // This should be a supressing call
    try {
      floodControl.evaluateTransaction(test);
      fail("KeySuppressException should have been generated.");
    } catch (KeySuppressException kse) {
      // This is the expected result.
    } catch (KeyBlockException kbe) {
      fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
    } catch (KeyStoreException kse) {
      fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
    }

    // "Almost" wait out the suppression interval. (KSI - 1 second)
    try {
      Thread.sleep((Integer.parseInt(KEYSUPPRESSINTERVAL) * 1000) - 1000);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }

    // This should be another supressing call
    try {
      floodControl.evaluateTransaction(test);
      fail("KeySuppressException should have been generated.");
    } catch (KeySuppressException kse) {
      // This is the expected result.
    } catch (KeyBlockException kbe) {
      fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
    } catch (KeyStoreException kde) {
      fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
    }

    // Wait 2 seconds to put you on the other side of the KSI.
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }

    // The next one should add normally (because the block aged out).
    try {
      floodControl.evaluateTransaction(test);
    } catch (Exception e) {
      fail("Unexpected exception after key block expiry: " + e.toString());
    }

  }

  /**
   * Test max concurrent keys.
   */
  @Test
  public final void testMaxConcurrentKeys() {
    
    int testNumber = getNewTestNumber();
    String test = new String("Test" + testNumber);
    
    int counter = 0;
    int numberOfKeysStored = 0;
    
    // Run three occurrences back-to-back (should be legal)
    // Relies on the KeyFrequencyLimit property above being set to 3.
    try {
      int maximumNumberOfKeys = Integer.parseInt(MAXCONCURRENTKEYS);
      int numberOfAllowableOccurences = Integer.parseInt(KEYFREQUENCYLIMIT);
      for (counter = 0; counter < maximumNumberOfKeys; counter++) {
        String newKey = test + counter;
        for (int i = 0; i < numberOfAllowableOccurences; i++) {
          floodControl.evaluateTransaction(newKey);
        }
      }
      
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

    // Sleep at least one second+.
    try {
      Thread.sleep(1050);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }   
    
    // The next one should add normally.
    try {
      floodControl.evaluateTransaction(test);
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }
   
    try {
       numberOfKeysStored = floodControl.getNumberOfKeysStored();
       if (numberOfKeysStored != 1) {
         fail("KeyStore did not clear when passing max concurrent keys.");
       }
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());      
    }

  }

  /**
   * @return the testNumber
   */
  private static synchronized int getNewTestNumber() {
    return testNumber++;
  }

}
