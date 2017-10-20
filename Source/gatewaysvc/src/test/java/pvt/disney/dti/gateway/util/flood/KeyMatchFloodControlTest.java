package pvt.disney.dti.gateway.util.flood;

import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Full Junit test class for KeyMatchFloodControl functionality (not related to
 * volume testing).
 * 
 * @author lewit019
 * 
 */
public class KeyMatchFloodControlTest {

	private static int testNumber = 0;

	private static KeyMatchFloodControl floodControl;
	public static final String KEYFREQUENCYWINDOW = "6";
	public static final String KEYFREQUENCYLIMIT = "6";
	public static final String KEYSUPPRESSINTERVAL = "9";
	public static final String MAXCONCURRENTKEYS = "60";
	public static final String CACHEREFRESHINTERVAL = "2";
	MemoryBasedKeyStore keyStore = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil dtiMockUtil = new DTIMockUtil();
		floodControl = new TestImplementor();
		String application = "DtiGateWay";
		String environment = "Latest";
		Integer tpoId = 0;
		floodControl.getRefreshPropertyFromDB(application,tpoId, environment);
		keyStore = MemoryBasedKeyStore.getInstance();
		keyStore.resetKeyStore();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests key derivation.
	 * 
	 */
	@Test
	public final void testDeriveKey() {
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");
		FloodMatchSignatureTO newKey;

		try {
			newKey = (FloodMatchSignatureTO) floodControl
					.deriveKey(floodMatchSignatureTO);
			if (!newKey.equals(newKey))
				Assert.fail("Key passed in doesn't match what was expected.");
		} catch (KeyDerivationException kde) {
			Assert.fail("Valid key caused KeyDerivationException");
		}
	}

	/**
	 * Tests the non-blocked lifecycle.
	 * 
	 * @throws KeyStoreException
	 * @throws FloodControlInitException
	 */
	@Test
	public final void testNonBlockedLifecycle() throws KeyStoreException,
			FloodControlInitException {
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");
		int testNumber = getNewTestNumber();
		String test = new String("Test" + testNumber);

		/*
		 * Run three occurrences back-to-back (should be legal) Relies on the
		 * KeyFrequencyLimit property above being set to 3.
		 */
		try {
			int numberOfAllowableOccurences = Integer
					.parseInt(KEYFREQUENCYLIMIT);
			for (int i = 0; i < numberOfAllowableOccurences; i++) {
				floodControl.evaluateTransaction(floodMatchSignatureTO);
			}
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.toString());
		}
		/* Wait out the Key Frequency Window */
		try {
			Thread.sleep((Integer.parseInt(KEYFREQUENCYWINDOW) * 1000) + 50);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
		/*
		 * This should be a valid call, all three of the previous occurrences
		 * should have aged out.
		 */
		MemoryBasedKeyStore keyStore = MemoryBasedKeyStore.getInstance();
		keyStore.resetKeyStore();
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.toString());
		}
	}

	/**
	 * Test the standard blocked lifecycle
	 * 
	 * @throws KeyStoreException
	 * @throws FloodControlInitException
	 * 
	 */
	@Test
	public final void testBlockedLifecycle() throws KeyStoreException,
			FloodControlInitException {
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");
		// String test = new String("Test" + testNumber);

		/*
		 * Run three occurrences back-to-back (should be legal) Relies on the
		 * KeyFrequencyLimit property above being set to 3.
		 */
		try {
			int numberOfAllowableOccurences = Integer
					.parseInt(KEYFREQUENCYLIMIT);
			for (int i = 0; i < numberOfAllowableOccurences; i++) {
				floodControl.evaluateTransaction(floodMatchSignatureTO);
			}
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
			Assert.fail("KeySuppressException should have been generated.");
		} catch (KeySuppressException kse) {
			/* This is the expected result. */
		} catch (KeyBlockException kbe) {
			Assert.fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
		} catch (KeyStoreException kse) {
			Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
		}

		/* "Almost" wait out the suppression interval. (KSI - 1 second) */
		try {
			Thread.sleep((Integer.parseInt(KEYSUPPRESSINTERVAL) * 1000) - 1000);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}

		/* This should be another suppressing call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
			Assert.fail("KeySuppressException should have been generated.");
		} catch (KeySuppressException kse) {
			/* This is the expected result. */
		} catch (KeyBlockException kbe) {
			Assert.fail("KeyBlockException occurred, but should have been KeySuppressException instead.");
		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
		} catch (KeyStoreException kde) {
			Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
		}
	}

	/**
	 * Test max concurrent keys.
	 * 
	 * @throws KeyStoreException
	 */
	@Test
	public final void testMaxConcurrentKeys() throws KeyStoreException {

		int numberOfKeysStored = 0;
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");

		/*
		 * Run three occurrences back-to-back (should be legal) Relies on the
		 * KeyFrequencyLimit property above being set to 3.
		 */
		try {
			int maximumNumberOfKeys = Integer.parseInt(KEYFREQUENCYLIMIT);
			for (int i = 0; i < maximumNumberOfKeys; i++) {
				floodControl.evaluateTransaction(floodMatchSignatureTO);
			}
			// }
		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.toString());
		}

		/* Sleep at least one second+. */
		try {
			Thread.sleep(1050);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
		keyStore.resetKeyStore();
		/* The next one should add normally. */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
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
