package pvt.disney.dti.gateway.util.flood;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** This class is used by the KeyMatchFloodControlVolumeTest class
 * as its test case it runs over and over again during a volume test.
 * 
 * @author lewit019
 * 
 */
public class FloodControlVolumeTestCase extends TestCase {

  private static int testNumber = 0;
  private static Properties props = new Properties();
  private static KeyMatchFloodControl floodControl;
  public static String keyFrequencyWindow = "6";
  public static String keyFrequencyLimit = "3";
  public static String keySuppressInterval = "9";

  /**
   * Constructor for the test case
   * @param name
   */
  public FloodControlVolumeTestCase(String name) {
    super(name);
    
    props = new Properties();

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/FloodControlVolumeTestCase.properties");
      props.load(inStream);
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load properties for test." + fnfe.toString());
    } catch (IOException ioe) {
      fail("Unable to load properties for test." + ioe.toString());
    }

    keyFrequencyWindow = props.getProperty("FloodControl.KeyFrequencyWindow");
    keyFrequencyLimit = props.getProperty("FloodControl.KeyFrequencyLimit");
    keySuppressInterval = props.getProperty("FloodControl.KeySuppressInterval");
  }

  /**
   * 
   */
  protected void setUp() {
  }

  protected void tearDown() {
  }

  /**
   * Primary test case used in volume testing.
   * @throws Exception
   */
  public void testKeyMatchFloodControl() throws Exception {

    try {
      floodControl = new TestImplementor(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }

    // **************************
    // NON-BLOCKED LIFECYCLE TEST
    // **************************

    String test = new String("Nonblocked" + getNewTestNumber());

    // Run three occurrences back-to-back (should be legal)
    // Relies on the KeyFrequencyLimit property above being set to 3.
    try {
      int numberOfAllowableOccurences = Integer.parseInt(keyFrequencyLimit);
      for (int i = 0; i < numberOfAllowableOccurences; i++) {
        floodControl.evaluateTransaction(test);
      }
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

    // Wait out the Key Frequency Window
    try {
      Thread.sleep((Integer.parseInt(keyFrequencyWindow) * 1000) + 50);
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

    // **********************
    // BLOCKED LIFECYCLE TEST
    // **********************
    test = new String("Blocked" + getNewTestNumber());

    // Run three occurrences back-to-back (should be legal)
    // Relies on the KeyFrequencyLimit property above being set to 3.
    try {
      int numberOfAllowableOccurences = Integer.parseInt(keyFrequencyLimit);
      for (int i = 0; i < numberOfAllowableOccurences; i++) {
        floodControl.evaluateTransaction(test);
      }
    } catch (Exception e) {
      fail("Unexpected exception: " + e.toString());
    }

    // This should be a blocking call
    try {
      floodControl.evaluateTransaction(test);
      fail("KeyBlockException 001 should have been generated for (" + test + ").");
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
      fail("KeySuppressException 001 should have been generated for (" + test + ").");
    } catch (KeySuppressException kse) {
      // This is the expected result.
    } catch (KeyBlockException kbe) {
      fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
    } catch (KeyStoreException kse) {
      fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
    }

    // "Almost" wait out the suppression interval. (KSI - 2 seconds)
    try {
      Thread.sleep((Integer.parseInt(keySuppressInterval) * 1000) - 2000);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }

    // This should be another supressing call
    try {
      floodControl.evaluateTransaction(test);
      fail("KeySuppressException 002 should have been generated for (" + test + ").");
    } catch (KeySuppressException kse) {
      // This is the expected result.
    } catch (KeyBlockException kbe) {
      fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
    } catch (KeyStoreException kde) {
      fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
    }

    // Wait 1 seconds to put you on the other side of the KSI.
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ie) {
      fail("Sleep thread was interrupted.");
    }

    boolean blockRemovalLate = false;

    // The next one should add normally (because the block aged out).
    try {
      floodControl.evaluateTransaction(test);
    } catch (KeySuppressException kse) {
      blockRemovalLate = true;
    } catch (Exception e) {
      fail("Unexpected exception after key block expiry: " + e.toString());
    }

    if (blockRemovalLate) {

      int numberOfSecondsLate = 0;
      for (int q = 0; q < 100; q++) {
        try {
          Thread.sleep(1000);
          numberOfSecondsLate++;
        } catch (InterruptedException ie) {
          fail("Sleep thread was interrupted.");
        }

        try {
          floodControl.evaluateTransaction(test);
          break;
        } catch (KeySuppressException kse) {

        } catch (Exception e) {
          fail("Unexpected exception after key block expiry: " + e.toString());
        }
      }

      if (numberOfSecondsLate > 60) {
        fail("Blocked key did not clear in a reasonable amount of time.");
      }

    }

  }

  /**
   * Creates and returns the test suite.
   * 
   * @return the test
   */
  public static Test suite() {
    return new TestSuite(FloodControlVolumeTestCase.class);
  }

  /**
   * @return the testNumber
   */
  private static synchronized int getNewTestNumber() {
    return testNumber++;
  }

  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }
}