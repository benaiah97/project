package pvt.disney.dti.gateway.data.dlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.math.BigInteger;
import java.util.HashMap;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.data.dlr.GWTPLookupTO.TPLookupType;

/**@author MISHP012
 *JUnit for GWTPLookupTO
 *
 */
public class GWTPLookupTOTestCase {
	GWTPLookupTO gWTPLookupTO = null;
	HashMap<String, String> dlrCardMap = new HashMap<>();
	BigInteger cmdId = new BigInteger("10");

	@Before
	public void setUp() {
		gWTPLookupTO = new GWTPLookupTO();
		gWTPLookupTO.setCmdCode("16");
		gWTPLookupTO.setCmdId(cmdId);
		dlrCardMap.put("key", "value");
		gWTPLookupTO.setDlrCardMap(dlrCardMap);
		gWTPLookupTO.setLookupDesc("lookupDesc");
		gWTPLookupTO.setLookupType(TPLookupType.MAX_LIMIT);
		gWTPLookupTO.setLookupValue("lookupValue");
	}

	@After
	public void tearDown() {
		gWTPLookupTO = null;
	}

	/**
	 * TestCase for gWTPLookupTO
	 */
	@Test
	public void testGWTPLookupTO() {
		assertNotNull(gWTPLookupTO);
		assertEquals(gWTPLookupTO.getCmdCode(), "16");
		assertEquals(gWTPLookupTO.getCmdId(), cmdId);
		assertEquals(gWTPLookupTO.getDlrCardMap(), dlrCardMap);
		assertEquals(gWTPLookupTO.getLookupDesc(), "lookupDesc");
		assertEquals(gWTPLookupTO.getLookupType(), TPLookupType.MAX_LIMIT);
		assertEquals(gWTPLookupTO.getLookupValue(), "lookupValue");
	}
	/**
	 * TestCase for addDlRCard
	 */
	@Test
	public void testAddDlRCard() {
		gWTPLookupTO.setDlrCardMap(dlrCardMap);
		String lookupValue = "male";
		String lookupDesc = "som";
		gWTPLookupTO.addDlRCard(lookupValue, lookupDesc);
		Assert.assertEquals("som", dlrCardMap.get(lookupValue));
	}
}
