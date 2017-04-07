package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.HashMap;

import org.junit.Assert;
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
	/**
	 * @throws DTIException
	 */
	@Test
	public void testGetWDWTicketAttributes() throws DTIException {
		/* Scenario:: 1 when null is passes as an argument */
		try {
			AttributeKey.getWDWTicketAttributes(null);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE,
					dtie.getDtiErrorCode());
			assertEquals("No ticketNumber provided to getWDWTicketAttributes",
					dtie.getLogMessage());
		}
		/*
		 * Scenario:: 2 when 3 is passes as an argument and without mocking DAO
		 * class
		 */
		try {
			AttributeKey.getWDWTicketAttributes(new BigInteger("3"));
		} catch (DTIException dtie) {
			assertEquals("Exception executing getWDWTicketAttributes",
					dtie.getLogMessage());
		}
		/* Scenario:: 3 when 3 is passes as an argument and mocking DAO class */
		/*DTIMockUtil
				.mockResultProcessor("pvt.disney.dti.gateway.dao.result.TicketAttributeResult");*/
		DTIMockUtil.processMockprepareAndExecuteSql();
		DBTicketAttributes dbTicketAttributes = AttributeKey
				.getWDWTicketAttributes(new BigInteger("3"));
		if (dbTicketAttributes == null) {
			Assert.fail("DBTicketAttributes can not be null");
		}
	}

	/**
	 * @param type
	 */
	private void testGetEntAttribtues(TransactionType type) {
		DTITransactionTO dtiTxn = null;
		String tpiCode = "NEX01";
		long entityId = 1;
		String actor = "1";
		/*Scenario 1:: when dtiTxn is passed as null*/
		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId, actor);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntAttribtues.",dtie.getLogMessage());
			
		}
		/*Scenario 2:: when dtiTxn is passed as not null and without mock Dao object*/
		dtiTxn = new DTITransactionTO(type);
		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId, actor);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntAttributes",dtie.getLogMessage());

		}
		/*Scenario 3:: when dtiTxn is passed as not null and mock Dao object*/
		/*DTIMockUtil
		.mockResultProcessor("pvt.disney.dti.gateway.dao.result.AttributeResult");*/
		DTIMockUtil.processMockprepareAndExecuteSql();
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
		try {
			attributeMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
					entityId, actor);
		} catch (DTIException dtie) {
			Assert.fail("Exception executing getEntAttributes expected");
		}
		if (attributeMap == null) {
			Assert.fail("Attribute value is not retrieved");
		}
		
	}

	/** Test Case for getEntAttribtues(
	 * @param type
	 */
	private void testGetEntAttributeswithoutActor(TransactionType type) {
		DTITransactionTO dtiTxn = null;
		String tpiCode = "NEX01";
		long entityId = 1;
		/*Scenario 1:: when dtiTxn is passed as null*/
		try {
			AttributeKey.getEntAttribtues(dtiTxn, tpiCode, entityId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntAttribtues.",dtie.getLogMessage());
		}
		
		
		/*Scenario 2:: when dtiTxn is passed as not null and mock Dao object*/
	/*	DTIMockUtil
		.mockResultProcessor("pvt.disney.dti.gateway.dao.result.AttributeResult");*/
		DTIMockUtil.processMockprepareAndExecuteSql();
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		try {
			attributeMap = AttributeKey.getEntAttribtues(dtiTxn, tpiCode,
					entityId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntAttributes",dtie.getLogMessage());
		}
		if (attributeMap == null) {
			Assert.fail("Attribute value is not retrieved");
		}
	}
	
	/**
	 * For testing for QUERYTICKET Transaction Type 
	 */
	@Test
	public void testGenEntAttributeQueryTicket() {
		testGetEntAttribtues(TransactionType.QUERYTICKET);
		testGetEntAttributeswithoutActor(TransactionType.QUERYTICKET);
	}
	/**
	 * For testing for UPGRADEALPHA Transaction Type 
	 */
	@Test
	public void testGenEntAttributeUpgradeAlpha() {
		testGetEntAttribtues(TransactionType.UPGRADEALPHA);
		testGetEntAttributeswithoutActor(TransactionType.UPGRADEALPHA);
	}
	/**
	 * For testing for VOIDTICKET Transaction Type 
	 */
	@Test
	public void testGenEntAttributeVoidTicket() {
		testGetEntAttribtues(TransactionType.VOIDTICKET);
		testGetEntAttributeswithoutActor(TransactionType.VOIDTICKET);
	}
	/**
	 * For testing for RESERVATION Transaction Type 
	 */
	@Test
	public void testGenEntAttributeReservation() {
		testGetEntAttribtues(TransactionType.RESERVATION);
		testGetEntAttributeswithoutActor(TransactionType.RESERVATION);
	}
	/**
	 * For testing for CREATETICKET Transaction Type 
	 */
	@Test
	public void testGenEntAttributeCreateTicket() {
		testGetEntAttribtues(TransactionType.CREATETICKET);
		testGetEntAttributeswithoutActor(TransactionType.CREATETICKET);
	}
	/**
	 * For testing for UPDATETICKET Transaction Type 
	 */
	@Test
	public void testGenEntAttributeUpdateTicket() {
		testGetEntAttribtues(TransactionType.UPDATETICKET);
		testGetEntAttributeswithoutActor(TransactionType.UPDATETICKET);
		
	}
	/**
	 * For testing for UPDATETRANSACTION Transaction Type 
	 */
	@Test
	public void testGenEntAttributeUpdateTransaction() {
		testGetEntAttribtues(TransactionType.UPDATETRANSACTION);
		testGetEntAttributeswithoutActor(TransactionType.UPDATETRANSACTION);
	}
	/**
	 * For testing for QUERYRESERVATION Transaction Type 
	 */
	@Test
	public void testGenEntAttributeQueryReservation() {
		testGetEntAttribtues(TransactionType.QUERYRESERVATION);
		testGetEntAttributeswithoutActor(TransactionType.QUERYRESERVATION);
	}
	/**
	 * For testing for UPGRADEENTITLEMENT Transaction Type 
	 */
	@Test
	public void testGenEntAttributeUpdgradeEntitlement() {
		testGetEntAttribtues(TransactionType.UPGRADEENTITLEMENT);
		testGetEntAttributeswithoutActor(TransactionType.UPGRADEENTITLEMENT);
	}
	/**
	 * For testing for ASSOCIATEMEDIATOACCOUNT Transaction Type 
	 */
	@Test
	public void testGenEntAttributeAssociateAccount() {
		testGetEntAttribtues(TransactionType.ASSOCIATEMEDIATOACCOUNT);
		testGetEntAttributeswithoutActor(TransactionType.ASSOCIATEMEDIATOACCOUNT);
	}
	/**
	 * For testing for TICKERATEENTITLEMENT Transaction Type 
	 */
	@Test
	public void testGenEntAttributeTicketEntitlemet() {
		testGetEntAttribtues(TransactionType.TICKERATEENTITLEMENT);
		testGetEntAttributeswithoutActor(TransactionType.TICKERATEENTITLEMENT);
	}
	/**
	 * For testing for RENEWENTITLEMENT Transaction Type 
	 */
	@Test
	public void testGenEntAttributeRenewEntitlement() {
		testGetEntAttribtues(TransactionType.RENEWENTITLEMENT);
		testGetEntAttributeswithoutActor(TransactionType.RENEWENTITLEMENT);
	}
	/**
	 * For testing for VOIDRESERVATION Transaction Type 
	 */
	@Test
	public void testGenEntAttributeVoidReservation() {
		testGetEntAttribtues(TransactionType.VOIDRESERVATION);
		testGetEntAttributeswithoutActor(TransactionType.VOIDRESERVATION);
	}
	/**
	 * For testing for UNDEFINED Transaction Type 
	 */
	@Test
	public void testGenEntAttributeNUll() {
		testGetEntAttribtues(TransactionType.UNDEFINED);
		testGetEntAttributeswithoutActor(TransactionType.UNDEFINED);
	}

	
}
