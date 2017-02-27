package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.HashMap;

import mockit.Deencapsulation;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * This is the DAO class which handles the queries for entity and ticket
 * attributes.
 * 
 * @author lewit019
 * 
 */
public class TestAttributeKey {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testGetCommandCodeString() {
		DTITransactionTO associatemediatoaccount = new DTITransactionTO(
				TransactionType.ASSOCIATEMEDIATOACCOUNT);
		String commandCode = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString",
				new Object[] { associatemediatoaccount });
		assertEquals("AssocMediaToAccount", commandCode);

		DTITransactionTO createticket = new DTITransactionTO(
				TransactionType.CREATETICKET);
		String createticketString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { createticket });
		assertEquals("CreateTicket", createticketString);

		DTITransactionTO queryreservation = new DTITransactionTO(
				TransactionType.QUERYRESERVATION);
		String queryreservationString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { queryreservation });
		assertEquals("QueryReservation", queryreservationString);

		DTITransactionTO queryticket = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		String queryticketString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { queryticket });
		assertEquals("QueryTicket", queryticketString);

		DTITransactionTO renewentitlement = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		String renewentitlementString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { renewentitlement });
		assertEquals("RenewEntitlement", renewentitlementString);

		DTITransactionTO reservation = new DTITransactionTO(
				TransactionType.RESERVATION);
		String reservationString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { reservation });
		assertEquals("Reservation", reservationString);

		DTITransactionTO tickerateentitlement = new DTITransactionTO(
				TransactionType.TICKERATEENTITLEMENT);
		String tickerateentitlementString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { tickerateentitlement });
		assertEquals("TickerateEntitlement", tickerateentitlementString);

		DTITransactionTO updateticket = new DTITransactionTO(
				TransactionType.UPDATETICKET);
		String updateticketString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { updateticket });
		assertEquals("UpdateTicket", updateticketString);

		DTITransactionTO updatetransaction = new DTITransactionTO(
				TransactionType.UPDATETRANSACTION);
		String updatetransactionString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { updatetransaction });
		assertEquals("UpdateTransaction", updatetransactionString);

		DTITransactionTO upgradealpha = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		String upgradealphaString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { upgradealpha });
		assertEquals("UpgradeAlpha", upgradealphaString);

		DTITransactionTO upgradeentitlement = new DTITransactionTO(
				TransactionType.UPGRADEENTITLEMENT);
		String upgradeentitlementString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { upgradeentitlement });
		assertEquals("UpgradeEntitlement", upgradeentitlementString);

		DTITransactionTO voidreservation = new DTITransactionTO(
				TransactionType.VOIDRESERVATION);
		String voidreservationString = Deencapsulation.invoke(
				AttributeKey.class, "getCommandCodeString",
				new Object[] { voidreservation });
		assertEquals("VoidReservation", voidreservationString);

		DTITransactionTO voidticket = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		String voidticketString = Deencapsulation.invoke(AttributeKey.class,
				"getCommandCodeString", new Object[] { voidticket });
		assertEquals("VoidTicket", voidticketString);

	}

	@Test
	public void testGetWDWTicketAttributes() throws DTIException {

		// EasyMock.replay(rs);
		try {
			AttributeKey.getWDWTicketAttributes(null);
		} catch (DTIException dtie) {
			assertEquals("No ticketNumber provided to getWDWTicketAttributes",
					dtie.getLogMessage());
		}

		try {

			AttributeKey.getWDWTicketAttributes(new BigInteger("3"));

		} catch (DTIException dtie) {
			assertEquals("Exception executing getWDWTicketAttributes",
					dtie.getLogMessage());
		}

		DTIMockUtil.mockTicketAttribute();

		DBTicketAttributes dbTicketAttributes = AttributeKey
				.getWDWTicketAttributes(new BigInteger("3"));
		if (dbTicketAttributes == null) {
			fail("DBTicketAttributes can not be null");
		}

	}

	@Test
	public void testGetEntAttribtues() {
		DTITransactionTO dtiTxn = null;

		String tpiCode = "NEX01";
		long entityId = 1;
		String actor = "1";
		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId, actor);
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_ENTITY) {
				fail("Insufficient parameters to execute getEntAttribtues. with Error code INVALID_ENTITY");
			}
		}
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId, actor);
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.FAILED_DB_OPERATION_SVC) {
				fail("Exception executing getEntAttributes expected");
			}

		}

		DTIMockUtil.mockAttributeKey();
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
		try {
			attributeMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
					entityId, actor);
		} catch (DTIException dtie) {
			fail("Exception executing getEntAttributes expected");
		}
		if (attributeMap == null) {
			fail("Attribute value is not retrieved");
		}

		// /for calling getEntAttribtues getEntAttribtues( DTITransactionTO
		// dtiTxn, String tpiCode, long entityId)
		testGetEntAttributes();

	}

	private void testGetEntAttributes() {
		DTITransactionTO dtiTxn = null;

		String tpiCode = "NEX01";
		long entityId = 1;

		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId);
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_ENTITY) {
				fail("Insufficient parameters to execute getEntAttribtues. with Error code INVALID_ENTITY");
			}
		}
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		try {
			attributeMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
					entityId);
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.FAILED_DB_OPERATION_SVC) {
				fail("Exception executing getEntAttributes expected");
			}

		}

		if (attributeMap == null) {
			fail("Attribute value is not retrieved");
		}
	}

}
