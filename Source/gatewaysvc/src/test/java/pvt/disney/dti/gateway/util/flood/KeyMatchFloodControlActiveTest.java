package pvt.disney.dti.gateway.util.flood;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Class tests max concurrent keys, props, and blocked lifeCycle.
 * 
 * @author lewit019
 * 
 */
public class KeyMatchFloodControlActiveTest {

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

		DTIMockUtil.processMockprepareAndExecuteSql();
		floodControl = new TestImplementor();
		String application ="DtiGateWay";
		String environment ="Latest";
		Integer tpoId =0;
		floodControl.getRefreshPropertyFromDB(application,tpoId,environment);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Standard Blocked LifeCycle
	 * 
	 * @throws FloodControlInitException
	 * @throws KeyStoreException
	 */
	@Test
	public final void testBlockedLifecycle() throws FloodControlInitException,
			KeyStoreException {
		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");
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

		}

		/* This should be a blocking call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (KeySuppressException kse) {
		} catch (KeyBlockException kbe) {
			/* This is the expected result. */
		} catch (KeyDerivationException kde) {
		} catch (KeyStoreException kse) {
		}

		/* This should be a suppressing call */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);

		} catch (KeySuppressException kse) {
			/* This is the expected result. */
		} catch (KeyBlockException kbe) {
		} catch (KeyDerivationException kde) {
		} catch (KeyStoreException kse) {
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
		} catch (KeySuppressException kse) {
			/* This is the expected result. */
		} catch (KeyBlockException kbe) {

		} catch (KeyDerivationException kde) {
			Assert.fail("KeyDerivationException occurred, but should have been KeySuppressException instead.");
		} catch (KeyStoreException kde) {
			Assert.fail("KeyStoreException occurred, but should have been KeySuppressException instead.");
		}

		/* Wait 2 seconds to put you on the other side of the KSI. */
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
		MemoryBasedKeyStore keyStore = MemoryBasedKeyStore.getInstance();
		keyStore.resetKeyStore();
		/* The next one should add normally (because the block aged out). */
		try {
			floodControl.evaluateTransaction(floodMatchSignatureTO);
		} catch (Exception e) {
			Assert.fail("Unexpected exception after key block expiry: "
					+ e.toString());
		}
	}

	/**
	 * Tests the behavior of maximum concurrent keys.
	 * 
	 * @throws FloodControlInitException
	 * @throws KeyStoreException
	 */
	@Test
	public final void testMaxConcurrentKeys() throws FloodControlInitException,
			KeyStoreException {

		FloodMatchSignatureTO floodMatchSignatureTO = new FloodMatchSignatureTO();
		floodMatchSignatureTO.setSignature("signature");

		MemoryBasedKeyStore keyStore = MemoryBasedKeyStore.getInstance();
		keyStore.resetKeyStore();

		int numberOfKeysStored = 0;

		/* Run three occurrences back-to-back (should be legal) */
		/* Relies on the KeyFrequencyLimit property above being set to 3. */
		try {
			int numberOfAllowableOccurences = Integer
					.parseInt(KEYFREQUENCYLIMIT);

			for (int i = 0; i < numberOfAllowableOccurences; i++) {
				floodControl.evaluateTransaction(floodMatchSignatureTO);

			}

		} catch (Exception e) {
			Assert.fail("Unexpected exception: " + e.toString());
		}
		/* Sleep at least one second+. */
		try {
			Thread.sleep(1050);
		} catch (InterruptedException ie) {
			Assert.fail("Sleep thread was interrupted.");
		}
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

}
