/**
 * 
 */
package pvt.disney.dti.gateway.rules.hkd; 

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.ReservationTO;

// TODO: Auto-generated Javadoc
/**
 * The Class testCreatePacakage.
 *
 * @author moons012
 */
public class TestCreatePacakage {
	  
	  /** The Constant PACKAGE_PICKUP_AREA. */
	  private final static String PACKAGE_PICKUP_AREA = "Offsite";

	  /** The Constant PACKAGE_SALE_TYPE. */
	  private final static String PACKAGE_SALE_TYPE = "Presale";
	  
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test helper method isPackage, expect true
	 */
	@Test
	public void testHelpIsPackageTrue() {
		ReservationTO res = new ReservationTO();
		res.setResPickupArea(PACKAGE_PICKUP_AREA);
		res.setResSalesType(PACKAGE_SALE_TYPE);
		
		boolean isPackage = HKDReservationRules.helpIsPackage(res);
		
		assertTrue("Expected a package, but isPackage was not true", isPackage);
	}

	/**
	 * Test helper method  helpGetPackageResSaleTypeList
	 */
	@Test
	public void testHelpGetPackageResSaleTypeList() {
		ArrayList<String> resSaleTypeList = HKDReservationRules.helpGetPackageResSaleTypeList();
		boolean result = false;
		if( (resSaleTypeList).contains(PACKAGE_SALE_TYPE) ) {
			result = true;
		}

		assertTrue("Expected list to contain OFFSITE but it did not", result);
	}

	/**
	 * Test helper method helpGetPackageResPickupAreaList.
	 */
	@Test
	public void testHelpGetPackageResPickupAreaList() {
		ArrayList<String> resPickupAreaList = HKDReservationRules.helpGetPackageResPickupAreaList();
		boolean result = false;
		if( (resPickupAreaList).contains(PACKAGE_PICKUP_AREA) ) {
			result = true;
		}
		assertTrue("Expected list to contain PRESALE but it did not", result);
	}
	
	/**
	 * Test helper method helpIsPackage, expect false.
	 */
	@Test
	public void TestHelpIsPackageFalse() {
		ReservationTO res = new ReservationTO();
		res.setResPickupArea("IAmNotAPackagePickupArea");
		res.setResSalesType("IAmNotAPackageSalesTypes");
	
		boolean result = HKDReservationRules.helpIsPackage(res);
		
		assertFalse("Expected isPackage = false but isPackage=" + result, HKDReservationRules.helpIsPackage(res));
		
	}
	
	
	
	
}
