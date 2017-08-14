package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for ErrorKey
 *
 */
public class TestErrorKey extends CommonTestDao {
	/**
	 * Test Case for getErrorDetail
	 */
	@Test
	public void testGetErrorDetail() throws DTIException {
		DTIErrorCode dtiErrCde = DTIErrorCode.EVENT_INVALID;
		;
		DTIErrorTO result = null;
		/* Scenario::1 Passing through DB without Mocking */
		try {
			result = ErrorKey.getErrorDetail(dtiErrCde);
			assertNotNull(result);
		} catch (Exception dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		/* Scenario::2 Mock to get Null from DB */
		DTIMockUtil
				.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.ErrorDetailResult");
		try {
			result = ErrorKey.getErrorDetail(dtiErrCde);
			assertNotNull(result);
		} catch (Exception dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		/* Scenario::3 Passing through DB after Mocking */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result = ErrorKey.getErrorDetail(dtiErrCde);
			assertNotNull(result);
		} catch (Exception dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}

	/**
	 * Test Case for getTPErrorMap
	 */
	@Test
	public void testGetTPErrorMap() {
		DTIErrorTO result = null;
		String tpErrorCode = null;
		/* Scenario::1 Passing tpErrorCode as null */
		try {
			ErrorKey.getTPErrorMap(tpErrorCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals(
					"TP Error Code of null was passed into getTPErrorMap.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing through DB without Mocking */
		tpErrorCode = "1";
		try {
			ErrorKey.getTPErrorMap(tpErrorCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
				}

		/* Scenario::3 Mock to get Null from DB */
		DTIMockUtil
				.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.TPErrorResult");
		try {
			result = ErrorKey.getTPErrorMap(tpErrorCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("TP Error Code of 1 is not mapped in database.",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing through DB after Mocking */
		DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.TPErrorResult");
		try {
			result = ErrorKey.getTPErrorMap(tpErrorCode);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}

	}

}
