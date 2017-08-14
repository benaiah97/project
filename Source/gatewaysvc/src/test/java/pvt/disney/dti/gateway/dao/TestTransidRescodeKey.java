package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.TransidRescodeTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test case for TransidRescodeKey
 * @author RASTA006
 *
 */
public class TestTransidRescodeKey extends CommonTestDao{
	/**
	 * Test Case for getTransIdResCode
	 */
	@Test
	public void testGetTransIdResCode(){

		String transid=null;
		ArrayList<TransidRescodeTO> result = null;
		/* Scenario::1 Passing transid as null*/
		try {
			TransidRescodeKey.getTransIdResCode(transid);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("getTransidRescodeFromDB DB routine found a null or empty transid.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		transid="1";
		try {
			TransidRescodeKey.getTransIdResCode(transid);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getTransidRescodeFromDB",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		/*try {
			result=TransidRescodeKey.getTransIdResCode(transid);
			assertNotNull(result);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getTransidRescodeFromDB",
					dtie.getLogMessage());
		}*/
	}
	/**
	 * Test case for insertTransIdRescode
	 */
	@Test
	public void testInsertTransIdRescode() {
		Integer transIdITS = 1;
		String ts_transid = null;
		String rescode = null;
		ArrayList<TransidRescodeTO> result = null;
		/* Scenario::1 Passing tpiCode as null */
		try {
			TransidRescodeKey.insertTransIdRescode(transIdITS,ts_transid,rescode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals(
					"insertTransIdRescode DB routine given null or empty parameters: .ts_transid:'null' , rescode:'null'.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		ts_transid = "1";
		rescode = "1";
		try {
			TransidRescodeKey.insertTransIdRescode(transIdITS,ts_transid,rescode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing insertTransIdRescode",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			TransidRescodeKey.insertTransIdRescode(transIdITS,ts_transid,rescode);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

}

