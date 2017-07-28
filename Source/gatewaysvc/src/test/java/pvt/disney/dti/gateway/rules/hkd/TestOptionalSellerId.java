/**
 * 
 */
package pvt.disney.dti.gateway.rules.hkd;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;

/**
 * @author moons012
 *
 */
public class TestOptionalSellerId {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link pvt.disney.dti.gateway.rules.hkd.HKDReservationRules#helpGetSellerId(pvt.disney.dti.gateway.data.common.TktSellerTO, pvt.disney.dti.gateway.data.common.EntityTO)}.
	 */
	@Test
	public void testHelpGetSellerIdUseRequestId() {
		//helpGetSellerId(TktSellerTO tktSellerTO,  EntityTO entityTO)
		Long expectedSalesId=1L;
		
		//create the objects for the helper method
		//create a tktseller to and set the id to 1
		TktSellerTO tktSellerTO = new TktSellerTO();
		tktSellerTO.setSellerId("1");
		//create an entityTO and set the id to 2
		EntityTO entityTO = new EntityTO();
		entityTO.setDefSalesRepId(2L);
		
		Long actualSalesId=HKDReservationRules.helpGetSellerId(tktSellerTO, entityTO); 
		
		assertEquals("Expected " + expectedSalesId +" but was " + actualSalesId, expectedSalesId, actualSalesId);
	}
	
	/**
	 * Test method for {@link pvt.disney.dti.gateway.rules.hkd.HKDReservationRules#helpGetSellerId(pvt.disney.dti.gateway.data.common.TktSellerTO, pvt.disney.dti.gateway.data.common.EntityTO)}.
	 */
	@Test
	public void testHelpGetSellerIdUseEntityId() {
		Long expectedSalesId=2L;
		
		//create the objects for the helper method
		TktSellerTO tktSellerTO = new TktSellerTO(); //don't set a sellerId
		EntityTO entityTO = new EntityTO();
		entityTO.setDefSalesRepId(2L);
		
		Long actualSalesId=HKDReservationRules.helpGetSellerId(tktSellerTO, entityTO); 
		
		assertEquals("Expected " + expectedSalesId +" but was " + actualSalesId, expectedSalesId, actualSalesId);
	}

}
