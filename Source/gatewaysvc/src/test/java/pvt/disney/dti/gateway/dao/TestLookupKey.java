package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for LookupKey
 * @author RASTA006
 *
 */
public class TestLookupKey extends CommonTestDao{
	
	/**
	 * Test case for getPaymentLookup
	 */
	@Test
	public void testGetPaymentLookup() {
		String commandCode = null;
		String ticketProviderId = null;
		long entityId = 1;
		String actor = null;
		ArrayList<PaymentLookupTO> result = null;
		/* Scenario::1 Passing commandCode,ticketProviderId as null*/
		try {
			LookupKey.getPaymentLookup(commandCode,ticketProviderId,entityId,actor);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getPaymentLookup.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		commandCode="1";
		ticketProviderId="1";
		actor="1";
		try {
			LookupKey.getPaymentLookup(commandCode,ticketProviderId,entityId,actor);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getPaymentLookup",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result=LookupKey.getPaymentLookup(commandCode,ticketProviderId,entityId,actor);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	
	/**
	 * Test case for getTPCommandLookup
	 */
	@Test
	public void testGetTPCommandLookup() {
		String tpiCode = null;
		TransactionType txnType = null;
		ArrayList<TPLookupTO> result = null;
		/* Scenario::1 Passing tpiCode,txnType as null*/
		try {
			LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getTPCommandLookup.",
					dtie.getLogMessage());
		}
		tpiCode="1";
		txnType=TransactionType.QUERYTICKET;
		/* Scenario::2 Passing object without mocking DB */
		try {
			LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getPaymentLookup",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::4 Passing the TransactionType.CREATETICKET*/
		txnType=TransactionType.CREATETICKET;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::5 Passing the TransactionType.ASSOCIATEMEDIATOACCOUNT*/
		txnType=TransactionType.ASSOCIATEMEDIATOACCOUNT;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::6 Passing the TransactionType.QUERYRESERVATION*/
		txnType=TransactionType.QUERYRESERVATION;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::7 Passing the TransactionType.QUERYTICKET*/
		txnType=TransactionType.QUERYTICKET;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::8 Passing the TransactionType.RENEWENTITLEMENT*/
		txnType=TransactionType.RENEWENTITLEMENT;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::9 Passing the TransactionType.RESERVATION*/
		txnType=TransactionType.RESERVATION;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::10 Passing the TransactionType.TICKERATEENTITLEMENT*/
		txnType=TransactionType.TICKERATEENTITLEMENT;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::11 Passing the TransactionType.UPDATETICKET*/
		txnType=TransactionType.UPDATETICKET;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::12 Passing the TransactionType.UPDATETRANSACTION*/
		txnType=TransactionType.UPDATETRANSACTION;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::13 Passing the TransactionType.UPGRADEALPHA*/
		txnType=TransactionType.UPGRADEALPHA;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::13 Passing the TransactionType.UPGRADEENTITLEMENT*/
		txnType=TransactionType.UPGRADEENTITLEMENT;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::14 Passing the TransactionType.VOIDRESERVATION*/
		txnType=TransactionType.VOIDRESERVATION;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::15 Passing the TransactionType.VOIDTICKET*/
		txnType=TransactionType.VOIDTICKET;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::15 Passing the TransactionType.UNDEFINED*/
		txnType=TransactionType.UNDEFINED;
		try {
			result=LookupKey.getTPCommandLookup(tpiCode, txnType, "NONE", "NONE", "NONE",
			        "NONE");
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getSimpleTPLookup
	 */
	@Test
	public void testGetSimpleTPLookup() {
		Integer ticketProviderId = 1;
		String lookup_key = null;
		String lookup_type = null;
		String result = null;
		/* Scenario::1 Passing lookup_key,lookup_type as null*/
		try {
			LookupKey.getSimpleTPLookup(ticketProviderId, lookup_key, lookup_type);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getSimpleTPLookup.",
					dtie.getLogMessage());
		}
		lookup_key="1";
		lookup_type="1";
		
		/* Scenario::2 Passing object without mocking DB */
		try {
			LookupKey.getSimpleTPLookup(ticketProviderId, lookup_key, lookup_type);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getSimpleTPLookup",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
			try {
			result=LookupKey.getSimpleTPLookup(ticketProviderId, lookup_key, lookup_type);
			assertNotNull(result);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getVoidLookup
	 */
	@Test
	public void testGetVoidLookup(){
		String statusValue=null;
	      String ticketProviderId=null;
	      Integer result = null;
	      /* Scenario::1 Passing lookup_key,lookup_type as null*/
			try {
				LookupKey.getVoidLookup(statusValue, ticketProviderId);
			} catch (DTIException dtie) {
				assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
				assertEquals("Insufficient parameters to execute getVoidLookup.",
						dtie.getLogMessage());
			}
			statusValue="1";
			ticketProviderId="1";
			
			/* Scenario::2 Passing object without mocking DB */
			try {
				LookupKey.getVoidLookup(statusValue, ticketProviderId);
			} catch (DTIException dtie) {
				assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
				assertEquals("Exception executing getVoidLookup",
						dtie.getLogMessage());
			}
			/* Scenario::3 Passing object after mocking DB */
			DTIMockUtil.processMockprepareAndExecuteSql();
			try {
				result=LookupKey.getVoidLookup(statusValue, ticketProviderId);
				assertNotNull(result);
			} catch (DTIException dtie) {
				Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
			}
	}
	
	/**
	 * Test Case for getGWTPCommandLookup
	 */
	@Test
	public void testGetGWTPCommandLookup() {
		String tpiCode = null;
		DTITransactionTO.TransactionType txnType = null;
		String shipMethod = null;
		String shipDetail = null;
		ArrayList<TPLookupTO> result = null;
		/* Scenario::1 Passing tpiCode,txnType as null*/
		try {
			LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getGWTPCommandLookup.",
					dtie.getLogMessage());
		}
		tpiCode="1";
		shipMethod="1";
		shipDetail="1";
		txnType=TransactionType.QUERYTICKET;
		/* Scenario::2 Passing object without mocking DB */
		try {
			LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getPaymentLookup",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::4 Passing the TransactionType.CREATETICKET*/
		txnType=TransactionType.CREATETICKET;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::5 Passing the TransactionType.ASSOCIATEMEDIATOACCOUNT*/
		txnType=TransactionType.ASSOCIATEMEDIATOACCOUNT;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::6 Passing the TransactionType.QUERYRESERVATION*/
		txnType=TransactionType.QUERYRESERVATION;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::7 Passing the TransactionType.QUERYTICKET*/
		txnType=TransactionType.QUERYTICKET;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::8 Passing the TransactionType.RENEWENTITLEMENT*/
		txnType=TransactionType.RENEWENTITLEMENT;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::9 Passing the TransactionType.RESERVATION*/
		txnType=TransactionType.RESERVATION;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::10 Passing the TransactionType.TICKERATEENTITLEMENT*/
		txnType=TransactionType.TICKERATEENTITLEMENT;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::11 Passing the TransactionType.UPDATETICKET*/
		txnType=TransactionType.UPDATETICKET;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::12 Passing the TransactionType.UPDATETRANSACTION*/
		txnType=TransactionType.UPDATETRANSACTION;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::13 Passing the TransactionType.UPGRADEALPHA*/
		txnType=TransactionType.UPGRADEALPHA;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::13 Passing the TransactionType.UPGRADEENTITLEMENT*/
		txnType=TransactionType.UPGRADEENTITLEMENT;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::14 Passing the TransactionType.VOIDRESERVATION*/
		txnType=TransactionType.VOIDRESERVATION;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::15 Passing the TransactionType.VOIDTICKET*/
		txnType=TransactionType.VOIDTICKET;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::15 Passing the TransactionType.UNDEFINED*/
		txnType=TransactionType.UNDEFINED;
		try {
			result=LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	
	}
}
