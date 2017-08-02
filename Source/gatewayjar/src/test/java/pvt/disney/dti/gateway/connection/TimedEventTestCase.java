package pvt.disney.dti.gateway.connection;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author MISHP012 JUnit for TimedEvent
 */
public class TimedEventTestCase {
	TimedEvent timedEvent = null;
	TransactionId transactionId = null;

	/**
	 * TestCase for TimedEventsettermethods
	 */
	@Before
	public void setUp() {
		timedEvent = new TimedEvent();
		timedEvent.setElapsedTime(2200);
		timedEvent.setStartTime(1000);
		transactionId = new TransactionId("masterMsgType", "componentId",
				"eventType", "currentMsgType", "errorCode");
		timedEvent.setTransactionId(transactionId);

	}

	@After
	public void tearDown() {
		timedEvent = null;
	}

	/**
	 * TestCase for TimedEventgettermethods
	 */
	@Test
	public void testTimedEvent() {
		assertNotNull(timedEvent);
		assertEquals(timedEvent.getElapsedTime(), 2200);
		assertEquals(timedEvent.getStartTime(), 1000);
		assertEquals(timedEvent.getTransactionId(), transactionId);
		assertEquals(timedEvent.getType(), "eventType");
		assertEquals(timedEvent.getComponentId(), "componentId");
	}

	/**
	 * TestCase for toString
	 */
	@Test
	public void testToString() {
		assertNotNull(timedEvent.toString());
	}
}
