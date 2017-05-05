package pvt.disney.dti.gateway.connection;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**@author MISHP012
 * Test class for StatsRecorder
 */
public class StatsRecorderTestCase {

	private static StatsRecorder statsRecorder;
	private static String name = "Hello";
	private static Calendar calendar;

	/**
	 * StatsRecorder and Calendar Objects
	 */
	@BeforeClass
	public static void beforeClass() {
		statsRecorder = StatsRecorder.getInstance();
		StatsRecorder.getInstance();
		calendar = Calendar.getInstance();
	}

	/**
	 * Test case for StatsRecorder postTransactionResult()
	 */
	@Test
	public void testPostTransactionResult() {
		statsRecorder.postTransactionResult(name, calendar, calendar);
	}

	/**
	 * Test case for StatsRecorder toPropList()
	 */
	@Test
	public void testToPropList() {
		Assert.assertNotNull(statsRecorder.toPropList());
	}

	/**
	 * Test case for StatsRecorder toWebMonPropList()
	 */
	@Test
	public void testToWebMonPropList() {
		Assert.assertNotNull(statsRecorder.toWebMonPropList());
		Assert.assertNotNull(statsRecorder.toWebMonPropList(true));
	}

	/**
	 * Test case for StatsRecorder cloneStats()
	 */
	@Test
	public void testCloneStats() {
		Assert.assertNotNull(statsRecorder.cloneStats());
	}

	/**
	 * Test case for StatsRecorder clearStats()
	 */
	@Test
	public void testClearStats() {
		Assert.assertNotNull(statsRecorder.clearStats());
	}

	/**
	 * Test case for StatsRecorder getStartUpTime()
	 */
	@Test
	public void testGetStartUpTime() {
		Assert.assertNotNull(statsRecorder.getStartUpTime());
	}

}