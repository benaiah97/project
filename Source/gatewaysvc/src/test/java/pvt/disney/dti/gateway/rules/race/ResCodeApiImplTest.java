/**
 * 
 */
package pvt.disney.dti.gateway.rules.race;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.rules.race.ResCodeApiImpl;

/**
 * The Class ResCodeApiTest.
 *
 * @author MOONS012
 */
public class ResCodeApiImplTest {

	private static final String PREFIX = "XP";
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

	//retrieveResCodebyPayloadId
	//createResCodeForPayloadId
	//clone
	

	/**
	 * Gets the instance test.
	 *
	 * 
	 */
	@Test
	public void getInstanceTest() {
		ResCodeApiImpl.getInstance();
	}
	
	/**
	 * Clone test.
	 *
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	@Test(expected = CloneNotSupportedException.class)
	public void cloneTest() throws CloneNotSupportedException {
		ResCodeApiImpl.getInstance().clone();
	}
	
	/**
	 * Generate res code test.
	 */
	@Test
	public void generateResCodeTest() {
		String resCode = ResCodeApiImpl.getInstance().generateResCode(PREFIX);
		
		//check the length
		assertTrue("Rescode length should be 12, but was " + resCode.length(), resCode.length() == 12);
		//check the prefix
		assertTrue("ResCode should start with '" + PREFIX + "' but was " + resCode, resCode.startsWith(PREFIX));
		//check the format
		//AN AN N N AN AN N N AN AN N   N - make sure the numbers are really numbers
		//0  1  2 3 4  5  6 7 9  0  10  11 : 2,3,6,7,10,11 the position should all be numbers
		assertTrue("Expected format AN AN N N AN AN N N AN AN N N but was, '" + resCode + "'", isCorrectFormat(resCode));
	}
	
	/**
	 * Private test helper format. Checks if is correct format.
	 *
	 * @param resCode the res code
	 * @return true, if is correct format
	 */
	private boolean isCorrectFormat(String resCode) {
		//start assumming it is correct, but check each element
		boolean formatCorrect = true;
		
		//check for spaces
		if (resCode.contains("_")) {
			formatCorrect = false;
			System.out.println("Rescode contained a space.");
		}
		//check charcater 2,3,6,7,10,11 to be numbers
		try {
			Integer.parseInt(resCode.substring(2,3)); 
			Integer.parseInt(resCode.substring(3,4));
			Integer.parseInt(resCode.substring(6,7));
			Integer.parseInt(resCode.substring(7,8));
			Integer.parseInt(resCode.substring(10,11));
			Integer.parseInt(resCode.substring(11,12));
		} catch (NumberFormatException nfe) {
			formatCorrect = false;
			System.out.println("One of the rescode characters that was expected to be numeric was not.");
		}
		
		return formatCorrect;
	}

}
