package pvt.disney.dti.gateway.util;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author MISHP012
 *JUnit for DTITracker
 */
public class DTITrackerTestCase {

	private static DTITracker dtiTracker;

	private String message = "hello";

	/**
	 * Instantiates the DTITracker Utility
	 */
	@BeforeClass
	public static void beforeClass() {
		dtiTracker = new DTITracker();
	}

	/**
	 * Test DTITracker setPayloadId()
	 */
	@Test
	public void testSetPayloadId() {
		dtiTracker.setPayloadId(null);
		dtiTracker.setPayloadId(message);
	}

	/**
	 * Test DTITracker getPayloadId()
	 */
	@Test
	public void testGetPayloadId() {
		Assert.assertNotNull(dtiTracker.getPayloadId());
	}

}