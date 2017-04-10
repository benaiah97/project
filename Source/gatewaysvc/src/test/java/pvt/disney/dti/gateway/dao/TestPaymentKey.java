package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for PaymentKey
 * @author RASTA006
 *
 */
public class TestPaymentKey extends CommonTestDao{
	/**
	 * Test Case for getPaymentLookup
	 */
	@Test
	public void testGetPaymentLookup(){

		String tpiCode=null;long paymentId=Long.valueOf("1");
		ArrayList<PaymentLookupTO> result = null;
		/* Scenario::1 Passing tpiCode as null*/
		try {
			PaymentKey.getPaymentLookup(tpiCode,paymentId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_CRITICAL_ERROR, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getPaymentLookup",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		tpiCode="1";
		try {
			DTIMockUtil.mockExceptionResultProcessor("pvt.disney.dti.gateway.dao.result.PaymentLookupResult");
			PaymentKey.getPaymentLookup(tpiCode,paymentId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getPaymentLookup",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.PaymentLookupResult");
		try {
			result=PaymentKey.getPaymentLookup(tpiCode,paymentId);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	
		
	}

}
