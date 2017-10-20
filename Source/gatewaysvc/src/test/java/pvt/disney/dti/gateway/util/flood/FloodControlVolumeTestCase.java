package pvt.disney.dti.gateway.util.flood;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.Assert;
import org.junit.Before;

import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * This class is used by the KeyMatchFloodControlVolumeTest class as its test
 * case it runs over and over again during a volume test.
 * 
 * @author lewit019
 * 
 */
public class FloodControlVolumeTestCase extends TestCase {

	private static KeyMatchFloodControl floodControl;
	private static MemoryBasedKeyStore keyStore = null;
	public static String keyFrequencyWindow = "6";
	public static String keyFrequencyLimit = "3";
	public static String keySuppressInterval = "9";

	/**
	 * Constructor for the test case
	 * 
	 * @param name
	 * @throws FloodControlInitException
	 */
	public FloodControlVolumeTestCase(String name) {
		super(name);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			floodControl = new TestImplementor();
			String application = "DtiGateWay";
			String environment = "Latest";
			Integer tpoId =0;
			floodControl.getRefreshPropertyFromDB(application,tpoId, environment);
		} catch (FloodControlInitException e) {
			Assert.fail("Unexpected Exception");
		}
	}

	/**
	 * @throws FloodControlInitException
	 * 
	 */
	@Before
	protected void setUp() throws FloodControlInitException {
		 keyStore = MemoryBasedKeyStore.getInstance();
	}

	protected void tearDown() {
	}

	/**
	 * Primary test case used in volume testing.
	 * 
	 * @throws FloodControlInitException
	 * @throws KeyStoreException
	 * @throws Exception
	 */
	public void testKeyMatchFloodControl() throws FloodControlInitException,
			KeyStoreException {
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");

		/*
		 * Run three occurrences back-to-back (should be legal) Relies on the
		 * KeyFrequencyLimit property above being set to 3.
		 */
		try {
			int numberOfAllowableOccurences = Integer
					.parseInt(keyFrequencyLimit);
			for (int i = 0; i < numberOfAllowableOccurences; i++) {
				floodControl.evaluateTransaction(floodMatchSignatureTO);
			}
		} catch (Exception e) {
			// Assert.fail("Unexpected exception: " + e.getMessage());
		}

		/* Wait out the Key Frequency Window */
		try {
			Thread.sleep((Integer.parseInt(keyFrequencyWindow) * 2000) + 50);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
		keyStore.resetKeyStore();
		/*
		 * This should be a valid call, all three of the previous occurrences
		 * should have aged out.
		 */
	
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.toString());
		}

		/* This should be a blocking call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);

		} catch (KeySuppressException kse) {

		} catch (KeyBlockException kbe) {
			/* This is the expected result. */
		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeyBlockException instead.");
		} catch (KeyStoreException kse) {
			Assert.fail("KeyStoreException occurred, but should have been KeyBlockException instead.");
		}

		/* This should be a suppressing call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (KeySuppressException kse) {
			// This is the expected result.
		} catch (KeyBlockException kbe) {
		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
		} catch (KeyStoreException kse) {
			Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
		}
		keyStore.resetKeyStore();
		
		 /*"Almost" wait out the suppression interval. (KSI - 2 seconds)*/ 
		try {
			Thread.sleep((Integer.parseInt(keySuppressInterval) * 1000) - 2000);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
		 

		/* This should be another suppressing call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (KeySuppressException kse) {
			/* This is the expected result. */
		} catch (KeyBlockException kbe) {
			/* This is the expected result. */
		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
		} catch (KeyStoreException kde) {
			Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
		}
		// keyStore.resetKeyStore();
		/* Wait 1 seconds to put you on the other side of the KSI. */
		/*
		 * try { Thread.sleep(2000); } catch (InterruptedException ie) {
		 * Assert.fail("Sleep thread was interrupted."); }
		 */
		floodControl.getCacheReFreshInterval();
		floodControl.isFloodControlActive();
		floodControl.isKeyBlockException();
		floodControl.getKeyFrequencyLimit();
		boolean blockRemovalLate = false;
		keyStore.resetKeyStore();
		/* The next one should add normally (because the block aged out). */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (KeySuppressException kse) {
			blockRemovalLate = true;
		} catch (Exception e) {
			Assert.fail("Unexpected exception after key block expiry: "
					+ e.toString());
		}

		if (blockRemovalLate) {

			int numberOfSecondsLate = 0;
			for (int q = 0; q < 100; q++) {
				try {
					Thread.sleep(1000);
					numberOfSecondsLate++;
				} catch (InterruptedException ie) {
					Assert.fail("Sleep thread was interrupted.");
				}
				try {
					floodControl.evaluateTransaction(floodMatchSignatureTO);
					break;
				} catch (KeySuppressException kse) {

				} catch (Exception e) {
					Assert.fail("Unexpected exception after key block expiry: "
							+ e.toString());
				}
			}
			if (numberOfSecondsLate > 60) {
				Assert.fail("Blocked key did not clear in a reasonable amount of time.");
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
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		junit.textui.TestRunner.run(suite());
	}
}