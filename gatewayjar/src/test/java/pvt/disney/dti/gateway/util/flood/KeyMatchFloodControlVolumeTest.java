package pvt.disney.dti.gateway.util.flood;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.clarkware.junitperf.LoadTest;

/**
 * This class utilizes the FloodControlVolumeTestCase to perform 
 * a volume test of Flood Control.
 * @author Todd lewis
 */
public class KeyMatchFloodControlVolumeTest extends TestCase {

  public static final long toleranceInMillis = 100;

  public KeyMatchFloodControlVolumeTest(String name) {
    super(name);
  }

  protected void setUp() {
  }

  protected void tearDown() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();

    suite.addTest(makeFloodControlTest());

    return suite;
  }

  /**
   * Creates the test.
   * @return
   */
  protected static Test makeFloodControlTest() {
	//TODO THIS IS A HACK, it should be 500 / 200 for a full blown test
	//SHAN will create filters and a property file so that by default it
	//runs by the values below unless a full blown test targeted is specified when running Maven
    int users = 5;
    int iterations = 2;

    Test testCase = new FloodControlVolumeTestCase("testKeyMatchFloodControl");

    Test loadTest = new LoadTest(testCase, users, iterations);

    return loadTest;
  }

  /** Runs the test. 
   * 
   * @param args
   */
  public static void main(String args[]) {
    junit.textui.TestRunner.run(suite());
  }
}
