package pvt.disney.dti.gateway.util.flood;

import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Class tests max concurrent keys, props, and blocked lifecycle.
 * 
 * @author lewit019
 * 
 */
public class KeyMatchFloodControlActiveTest {

  private static int testNumber = 0;
  private static Properties props = null;
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
   * Test key match flood control properties.
   */
  @Test
  public final void testKeyMatchFloodControlProperties() {

    props = new Properties();
    // The properties are not tested; they default to a value without error.
    props.setProperty("FloodControl.KeyStoreType", "InMemory");
    props.setProperty("FloodControl.Active", "true");
    props.setProperty("FloodControl.KeyBlockException", "true");

    // Test numeric parameter constraints
    try {
      props.setProperty("FloodControl.KeyFrequencyWindow", "notanumber");
      floodControl = new TestImplementor(props);
      Assert.fail("Did not throw exception on bad KeyFrequencyWindow.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.KeyFrequencyLimit", "notanumber");
      floodControl = new TestImplementor(props);
      Assert.fail("Did not throw exception on bad KeyFrequencyLimit.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.KeySuppressInterval", "notanumber");
      floodControl = new TestImplementor(props);
      Assert.fail("Did not throw exception on bad KeySuppressInterval.");
    } catch (FloodControlInitException fcie) {
    }

    try {
      props.setProperty("FloodControl.MaxConcurrentKeys", "notanumber");
      floodControl = new TestImplementor(props);
      Assert.fail("Did not throw exception on bad MaxConcurrentKeys.");
    } catch (FloodControlInitException fcie) {
    }
    try {
      props.setProperty("FloodControl.CacheRefreshInterval", "notanumber");
      floodControl = new TestImplementor(props);
      Assert.fail("Did not throw exception on bad CacheRefreshInterval.");
    } catch (FloodControlInitException fcie) {
    }
    props.setProperty("FloodControl.KeyFrequencyWindow", KEYFREQUENCYWINDOW); // seconds
    props.setProperty("FloodControl.KeyFrequencyLimit", KEYFREQUENCYLIMIT);
    props.setProperty("FloodControl.KeySuppressInterval", KEYSUPPRESSINTERVAL); // seconds
    props.setProperty("FloodControl.MaxConcurrentKeys", MAXCONCURRENTKEYS);
    props.setProperty("FloodControl.CacheRefreshInterval", CACHEREFRESHINTERVAL); // seconds
    try {
      floodControl = new TestImplementor(props);
    } catch (FloodControlInitException fcie) {
    	Assert.fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
  }

  /**
   * Standard Blocked Lifecycle
   */
  @Test
  public final void testBlockedLifecycle() {

    int testNumber = getNewTestNumber();
    String test = new String("Test" + testNumber);

    /* Run three occurrences back-to-back (should be legal)
     Relies on the KeyFrequencyLimit property above being set to 3.*/
    try {
      int numberOfAllowableOccurences = Integer.parseInt(KEYFREQUENCYLIMIT);
      for (int i = 0; i < numberOfAllowableOccurences; i++) {
        floodControl.evaluateTransaction(test);
      }
    } catch (Exception e) {

    }

    /*This should be a blocking call*/
    try {
      floodControl.evaluateTransaction(test);
      Assert.fail("KeyBlockException should have been generated.");
    } catch (KeySuppressException kse) {
    } catch (KeyBlockException kbe) {
      /*This is the expected result.*/
    } catch (KeyDerivationException kde) {
    } catch (KeyStoreException kse) {
    }

    /*This should be a supressing call*/
    try {
      floodControl.evaluateTransaction(test);
      Assert.fail("KeySuppressException should have been generated.");
    } catch (KeySuppressException kse) {
      /*This is the expected result.*/
    } catch (KeyBlockException kbe) {
    } catch (KeyDerivationException kde) {
    } catch (KeyStoreException kse) {
    }

   /* "Almost" wait out the suppression interval. (KSI - 1 second)*/
    try {
      Thread.sleep((Integer.parseInt(KEYSUPPRESSINTERVAL) * 1000) - 1000);
    } catch (InterruptedException ie) {
    	Assert.fail("Sleep thread was interrupted.");
    }

  /*This should be another supressing call*/
    try {
      floodControl.evaluateTransaction(test);
      Assert.fail("KeySuppressException should have been generated.");
    } catch (KeySuppressException kse) {
      /*This is the expected result.*/
    } catch (KeyBlockException kbe) {
    	Assert.fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
    } catch (KeyDerivationException kde) {
    	Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
    } catch (KeyStoreException kde) {
    	Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
    }
    props.setProperty("FloodControl.Active", "false");

    /* Wait 2 seconds to put you on the other side of the KSI.*/
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ie) {
    	Assert.fail("Sleep thread was interrupted.");
    }

    /* The next one should add normally (because the block aged out).*/
    try {
      floodControl.evaluateTransaction(test);
    } catch (Exception e) {
    	Assert.fail("Unexpected exception after key block expiry: " + e.toString());
    }
  }

  /**
   * Tests the behavior of maximum concurrent keys.
   */
  @Test
  public final void testMaxConcurrentKeys() {

    int testNumber = getNewTestNumber();
    String test = new String("Test" + testNumber);

    int counter = 0;
    int numberOfKeysStored = 0;

   /*Run three occurrences back-to-back (should be legal)*/
    /*Relies on the KeyFrequencyLimit property above being set to 3.*/
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
    	Assert.fail("Unexpected exception: " + e.toString());
    }
   /* Sleep at least one second+.*/
    try {
      Thread.sleep(1050);
    } catch (InterruptedException ie) {
    	Assert.fail("Sleep thread was interrupted.");
    }
   /*The next one should add normally.*/
    try {
      floodControl.evaluateTransaction(test);
    } catch (Exception e) {
    	Assert.fail("Unexpected exception: " + e.toString());
    }
    try {
      numberOfKeysStored = floodControl.getNumberOfKeysStored();
      if (numberOfKeysStored != 1) {
    	  Assert.fail("KeyStore did not clear when passing max concurrent keys.");
      }
    } catch (Exception e) {
    	Assert.fail("Unexpected exception: " + e.toString());
    }
  }

  /**
   * @return the testNumber
   */
  private static synchronized int getNewTestNumber() {
    return testNumber++;
  }

}
