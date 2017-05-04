package pvt.disney.dti.gateway.data.common;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

/**
 * @author MISHP012 JUnit for PayloadHeaderTO
 */
public class PayloadHeaderTOTestCase {
	PayloadHeaderTO payloadHeaderTO = new PayloadHeaderTO();
	GregorianCalendar transmitDateTime = new GregorianCalendar();
	TktSellerTO tktSellerTO = new TktSellerTO();
	EntityTO dbEntity = new EntityTO();
	BigInteger bigi = new BigInteger("1");

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		payloadHeaderTO.setCommandCount(bigi);
		payloadHeaderTO.setCommMethod("commmethod");
		payloadHeaderTO.setCommProtocol("commProtocol");
		payloadHeaderTO.setDbEntity(dbEntity);
		payloadHeaderTO.setPayloadID("payloadId");
		payloadHeaderTO.setTarget("target");
		payloadHeaderTO.setTktBroker("tktBroker");
		payloadHeaderTO.setTktSeller(tktSellerTO);
		payloadHeaderTO.setTransmitDateTime(transmitDateTime);
		payloadHeaderTO.setTsPayloadID("tsPayloadID");
		payloadHeaderTO.setVersion("version");
	}

	/**
	 * TestCase for PayloadHeaderTOGetterMethods
	 */
	@Test
	public void testPayloadHeaderTO() {
		assertEquals(payloadHeaderTO.getCommandCount(), bigi);
		assertEquals(payloadHeaderTO.getCommMethod(), "commmethod");
		assertEquals(payloadHeaderTO.getCommProtocol(), "commProtocol");
		assertEquals(payloadHeaderTO.getDbEntity(), dbEntity);
		assertEquals(payloadHeaderTO.getPayloadID(), "payloadId");
		assertEquals(payloadHeaderTO.getTarget(), "target");
		assertEquals(payloadHeaderTO.getTktBroker(), "tktBroker");
		assertEquals(payloadHeaderTO.getTktSeller(), tktSellerTO);
		assertEquals(payloadHeaderTO.getTransmitDateTime(), transmitDateTime);
		assertEquals(payloadHeaderTO.getTsPayloadID(), "tsPayloadID");
		assertEquals(payloadHeaderTO.getVersion(), "version");

	}

	/**
	 * TestCase for getCopy
	 */
	@Test
	public void testGetCopy() {

		payloadHeaderTO.getCopy();

	}

	/**
	 * TestCase for toString
	 */
	@Test
	public void testToString() {
		payloadHeaderTO.toString();
	}

}
