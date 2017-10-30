/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.utility;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author moons012
 *
 */
public class WDWAlgorithmUtilityTest {

	
	public static WDWAlgorithmUtility wdwUtility;
	
	public static String payloadId = "99920170913073547426";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		wdwUtility = new WDWAlgorithmUtility();
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
	 * Test method for {@link pvt.disney.dti.gateway.rules.race.utility.WDWAlgorithmUtilityTest#generateResCode()}.
	 */
	@Test
	public void testGenerateResCode() {
		String resCode = wdwUtility.generateResCode(payloadId);
		System.out.println("wdw resCode='" + resCode +"");
	}
	
	@Test
	public void testIsValidFormat () {
		
		String testValidsCode = wdwUtility.generateResCode(payloadId);
		System.out.println("Test RESCODE" + testValidsCode);
		boolean isValidFormat = true;
		
		StringBuffer msg = new StringBuffer("testIsValidResCode: '");
		msg.append(testValidsCode);
		msg.append(", length:");
		msg.append(testValidsCode.length());
		System.out.println(msg.toString());
		//Character.isDigit
		//Character.isAlphabetic(0)
		//some value = (some t/f expression) ? : atrue : bfalse;
		System.out.println("TEST RESCODE WDW: '" + testValidsCode + "'");
		
		// char at 0 - Alpha
		isValidFormat = ( Character.isAlphabetic(testValidsCode.charAt(0)) && isValidFormat) ? true : false;

		// char at 1 - Alpha
		isValidFormat = ( Character.isAlphabetic(testValidsCode.charAt(1)) && isValidFormat) ? true : false;
		
		// char at 2 - Alpha
		isValidFormat = ( Character.isAlphabetic(testValidsCode.charAt(2)) && isValidFormat) ? true : false;
		
		// char at 3 - Alpha
		isValidFormat = ( Character.isAlphabetic(testValidsCode.charAt(3)) && isValidFormat) ? true : false;
		
		// char at 4 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(4)) && isValidFormat) ? true : false;
		
		// char at 5 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(5)) && isValidFormat) ? true : false;
		
		// char at 6 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(6)) && isValidFormat) ? true : false;
		
		// char at 7 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(7)) && isValidFormat) ? true : false;
		
		// char at 8 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(8)) && isValidFormat) ? true : false;
		
		// char at 9 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(9)) && isValidFormat) ? true : false;
		
		// char at 10 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(10)) && isValidFormat) ? true : false;
		
		// char at 11 - Numeric
		isValidFormat = ( Character.isDigit(testValidsCode.charAt(11)) && isValidFormat) ? true : false;
		System.out.println("TEST RESCODE WDW: '" + testValidsCode + "'");
		
		
		assertTrue("Invalid format, expected AAAANNNNNNNN but got '" + testValidsCode + "'", isValidFormat);
	}

}
