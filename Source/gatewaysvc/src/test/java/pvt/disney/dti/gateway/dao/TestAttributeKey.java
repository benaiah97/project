package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;

import mockit.Deencapsulation;
import mockit.Mock;
import mockit.MockUp;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.dao.result.TicketAttributeResult;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This is the DAO class which handles the queries for entity and ticket
 * attributes.
 * 
 * @author lewit019
 * 
 */
public class TestAttributeKey {
	ResultSet rs = null;
	ResultSet attributeRs = null;
	ResultSet attributeRs1 = null;
	ResultSet attributeRs2 = null;

	@Before
	public void setUp() throws Exception {
		rs = PowerMock.createMock(ResultSet.class);
		attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);
		setResultSet(rs);
		setAttributeRS();

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

		EasyMock.replay(rs);
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

		new MockUp<DAOHelper>() {

			@Mock
			protected Object processQuery(Object[] values) {

				DBTicketAttributes dbTicketAttributes = null;
				ResultSetProcessor theProcessor = new TicketAttributeResult();
				try {
					theProcessor.processNextResultSet(rs);
					dbTicketAttributes = (DBTicketAttributes) theProcessor
							.getProcessedObject();
				} catch (Exception e) {

				}

				return dbTicketAttributes;
			}
		};

		DBTicketAttributes dbTicketAttributes = AttributeKey
				.getWDWTicketAttributes(new BigInteger("3"));
		if (dbTicketAttributes == null) {
			fail("DBTicketAttributes can not be null");
		}

	}

	private void setResultSet(ResultSet rs) throws Exception {
		EasyMock.expect(rs.getString(EasyMock.anyObject(String.class)))
				.andReturn("1").anyTimes();

		EasyMock.expect(rs.getLong(EasyMock.anyObject(String.class)))
				.andReturn(1L).anyTimes();

		EasyMock.expect(rs.getDouble(EasyMock.anyObject(String.class)))
				.andReturn(1.0).anyTimes();

		EasyMock.expect(rs.getInt(EasyMock.anyObject(String.class)))
				.andReturn(1).anyTimes();

		EasyMock.expect(rs.getTimestamp(EasyMock.anyObject(String.class)))
				.andReturn(new Timestamp(System.currentTimeMillis()))
				.anyTimes();

		EasyMock.expect(rs.getDate(EasyMock.anyObject(String.class)))
				.andReturn(new Date(System.currentTimeMillis())).anyTimes();
		EasyMock.expect(rs.getBoolean(EasyMock.anyObject(String.class)))
				.andReturn(true).anyTimes();

	}

	@Test
	public void testGetEntAttribtues() {
		DTITransactionTO dtiTxn = null;
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);

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

		new MockUp<DAOHelper>() {

			@SuppressWarnings("unchecked")
			@Mock
			protected Object processQuery(Object[] values) {

				HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
				ResultSetProcessor theProcessor = new AttributeResult();
				try {
					theProcessor.processNextResultSet(attributeRs);
					theProcessor.processNextResultSet(attributeRs1);
					theProcessor.processNextResultSet(attributeRs2);
					attributeMap = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) theProcessor
							.getProcessedObject();
				} catch (Exception e) {

				}

				return attributeMap;
			}
		};
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
		
		///for calling getEntAttribtues getEntAttribtues(    DTITransactionTO dtiTxn, String tpiCode, long entityId)
		testGetEntAttributes();
		
		

	}
	
	
	private void testGetEntAttributes(){
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
			attributeMap=AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId);
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.FAILED_DB_OPERATION_SVC) {
				fail("Exception executing getEntAttributes expected");
			}

		}

		
		if (attributeMap == null) {
			fail("Attribute value is not retrieved");
		}
	}

	private void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
	}

	private void setAttributeResultSet(ResultSet rs, int i) throws Exception {

		EasyMock.expect(rs.getString("ATTR_VALUE")).andReturn("11111").times(3);

		EasyMock.expect(rs.getString("ACTOR")).andReturn("MGR").times(3);
		if (i == 0) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("OpArea")
					.times(3);

		} else if (i == 1) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("User")
					.times(3);
		} else {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("Pass")
					.times(3);
		}

		EasyMock.expect(rs.getString("ACTIVE_IND")).andReturn("T").times(3);
		EasyMock.expect(rs.getString("CMD_CODE")).andReturn("QueryReservation")
				.times(3);

	}

}
