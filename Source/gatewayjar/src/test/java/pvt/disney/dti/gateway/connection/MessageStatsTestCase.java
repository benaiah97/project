package pvt.disney.dti.gateway.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import com.disney.logging.audit.Event;

/**
 * @author MISHP012 JUnit for MessageStats
 */
public class MessageStatsTestCase {
	MessageStats messageStats = new MessageStats();
	TransactionId transId = null;

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		messageStats.setConversationId("conversationId");
		messageStats.setMessageId("messageId");
		messageStats.setServiceType("serviceType");
		messageStats.setAppName("appName");
		messageStats.setNode("node");
		;
	}

	/**
	 * TestCase for addTimedEvent
	 */
	@Test
	public void testAddTimedEvent() {
		transId = new TransactionId("masterMsgType", "componentId",
				"eventType", "currentMsgType", "errorCode");
		long startTime = 10000;
		long elapsedTime = 20000;
		messageStats.addTimedEvent(transId, startTime, elapsedTime);
	}

	/**
	 * TestCase for messageStatsgettermethods
	 */
	@Test
	public void testMessageStats() {
		assertNotNull(messageStats);
		assertEquals(messageStats.getConversationId(), "conversationId");
		assertEquals(messageStats.getMessageId(), "messageId");
		assertEquals(messageStats.getAppName(), "appName");
		assertEquals(messageStats.getNode(), "node");
		assertEquals(messageStats.getServiceType(), "serviceType");
	}

	/**
	 * TestCase for toString
	 */
	@Test
	public void testToString() {
		messageStats.toString();
	}

	/**
	 * TestCase for getTimedEvents
	 */
	@Test
	public void testGetTimedEvents() {
		messageStats.getTimedEvents();
	}
	/**
	 * TestCase for addEvent
	 */
	@Test
	public void testAddEvent() {
		messageStats.addEvent(new Event());
	}

	/**
	 * TestCase for getEvent
	 */
	@Test
	public void testGetEvent() {
		messageStats.getEvents();
	}
}
