package pvt.disney.dti.gateway.connection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author MISHP012 Junit for TransactionId
 */

public class TransactionIdTestCase {
	TransactionId transactionId = new TransactionId("masterMsgType",
			"componentId", "eventType", "currentMsgType", "errorCode");

	/**
	 * JUnit for parseTransactionId
	 */
	@Test
	public void testParseTransactionId() {
		String transId = "masterMsgType-componentId-eventType-temp_";
		TransactionId.parseTransactionId(transId);
		transactionId = new TransactionId(null, "componentId", "eventType",
				"currentMsgType", "errorCode");
		TransactionId.parseTransactionId(transId);
	}

	/**
	 * TestCase for toString
	 */
	@Test
	public void testToString() {
		transactionId.toString();
	}

	/**
	 * TestCase for toByteArray
	 */
	@Test
	public void testToByteArray() {
		transactionId.toByteArray();
		transactionId = new TransactionId("m", "c", "e", "c", "e");
		transactionId.toByteArray();
	}

	/**
	 * TestCase for TransactionIdGetterMethods
	 */
	@Test
	public void testTransactionId() {
		assertEquals(transactionId.getComponentId(), "componentId");
		assertEquals(transactionId.getCurrentMsgType(), "currentMsgType");
		assertEquals(transactionId.getErrorCode(), "errorCode");
		assertEquals(transactionId.getMasterMsgType(), "masterMsgType");
		assertEquals(transactionId.getEventType(), "eventType");
	}
}