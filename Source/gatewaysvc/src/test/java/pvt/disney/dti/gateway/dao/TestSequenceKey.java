package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.TransactionSequences;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for SequenceKey
 * 
 * @author RASTA006
 *
 */
public class TestSequenceKey extends CommonTestDao {
	/**
	 * Test Case for getTpRefNum
	 */
	@Test
	public void testGetTpRefNum() {
		Integer result = null;
		/* Scenario::1 Passing object without mocking DB */
		try {
			//DTIMockUtil.mockExceptionResultProcessor("");
			SequenceKey.getTpRefNum();
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getTpRefNum",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		
		try {
			result = SequenceKey.getTpRefNum();
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test case for getITSTransId()
	 */
	@Test
	public void testGetITSTransId() {
		Integer result = null;
		/* Scenario::1 Passing object without mocking DB */
		try {
			DTIMockUtil.mockExceptionResultProcessor("");
			SequenceKey.getITSTransId();
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getITSTransId",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result = SequenceKey.getITSTransId();
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for getITPTransId
	 */
	@Test
	public void testGetITPTransId() {
		Integer result = null;
		/* Scenario::1 Passing object without mocking DB */
		try {
			DTIMockUtil.mockExceptionResultProcessor("");
			SequenceKey.getITPTransId();
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getITPTransId",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result = SequenceKey.getITPTransId();
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test Case for getOTPTransId
	 */
	@Test
	public void testGetOTPTransId() {
		Integer result = null;
		/* Scenario::1 Passing object without mocking DB */
			try {
				DTIMockUtil.mockExceptionResultProcessor("");
			SequenceKey.getOTPTransId();
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getOTPTransId",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result = SequenceKey.getOTPTransId();
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for getOTSTransId
	 */
	 @Test
	public void testGetOTSTransId() {

		Integer result = null;
		/* Scenario::1 Passing object without mocking DB */
		try {
			DTIMockUtil.mockExceptionResultProcessor("");
			SequenceKey.getOTSTransId();
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getOTSTransId",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			result = SequenceKey.getOTSTransId();
			assertNotNull(result);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test Case for setItpOtpOtsSequenceNumbers
	 */
	@Test
	public void testSetItpOtpOtsSequenceNumbers() {
		TransactionSequences result = null;
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		/* Scenario::1 Passing object without mocking DB */
		try {
			SequenceKey.setItpOtpOtsSequenceNumbers(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing setItpOtpOtsSequenceNumbers",
					dtie.getLogMessage());
		}

	}

}
