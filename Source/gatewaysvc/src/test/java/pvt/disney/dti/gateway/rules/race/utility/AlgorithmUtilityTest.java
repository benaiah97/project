/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.utility;



import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvt.disney.dti.gateway.rules.race.utility.AlgorithmUtility;
import pvt.disney.dti.gateway.rules.race.vo.StepEightVO;
import pvt.disney.dti.gateway.rules.race.vo.StepFiveVO;
import pvt.disney.dti.gateway.rules.race.vo.StepFourVO;
import pvt.disney.dti.gateway.rules.race.vo.StepNineVO;
import pvt.disney.dti.gateway.rules.race.vo.StepOneVO;
import pvt.disney.dti.gateway.rules.race.vo.StepSevenVO;
import pvt.disney.dti.gateway.rules.race.vo.StepSixVO;
import pvt.disney.dti.gateway.rules.race.vo.StepThreeVO;
import pvt.disney.dti.gateway.rules.race.vo.StepTwoVO;

/**
 * Unit test for algorithm utility. Tests each step and final outcome. Test calculations are derived directly
 * from the demonstration of the algorithm documented at https://confluence.disney.com/display/SET/Reservation+Code+Algorithm
 * 
 * @author MOONS012
 *
 */
public class AlgorithmUtilityTest {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmUtilityTest.class);
	
	/** The util. */
	private static AlgorithmUtility util;
	
	//VALUES PASSED TO ALGORITM UTIL METHODS BEING TESTED TO MAKE CODE MORE HUMAN FRIENDLY
	//MOST TEST VALUES DERIVED DIRECTLY FROM ALGORITHM SPEC DOCUMENT
	//A FEW ADDITIONAL VALUES WERE ADDED FOR TESTING BORDER CONDITIONS SINCE AS SINGLE AND DOUBLE DIGIT DATE/TIME VALUES
	//DEFAULT TEST DATE is 03/09/2012:14:50:50:07
	//DEFAULT TICKET SELLER IS EXPEDIA
	final String DEFAULT_SELLER = "XP";
	final int DEFAULT_YEAR = 2012;
	final int DEFAULT_MONTH = 3, SINGLE_DIGIT_MONTH = 3, DOUBLE_DIGIT_MONTH=10;
	final int DEFAULT_DAY_OF_MONTH = 9, SINGLE_DIGIT_DAY_OF_MONTH = 9, DOUBLE_DIGIT_DAY_OF_MONTH = 10;
	final int DEFAULT_HOUR = 14, SINGLE_DIGIT_HOUR = 1, DOUBLE_DIGIT_HOUR = 14;
	final int DEFAULT_MINUTE = 50, SINGLE_DIGIT_MINUTE= 4, DOUBLE_DIGIT_MINUTE = 50;
	final int DEFAULT_SECOND = 50, SINGLE_DIGIT_SECOND = 8, DOUBLE_DIGIT_SECOND = 50;
	final int DEFAULT_MILLIS = 7;
	
	//not final as wee need to set millisconds
	Calendar DEFAULT_CALENDAR = new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY_OF_MONTH, DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
	
	
	//INDEX VALUES FOR LOCATING ELEMENTS TO MAKE TESTS MORE HUMAN READABLE
	final int HOUR1_ROW_INDEX    = 0;
	final int HOUR1_COLUMN_INDEX = 0;	
	final int HOUR2_ROW_INDEX    = 0;
	final int HOUR2_COLUMN_INDEX = 1;
	
	final int MINUTE1_ROW_INDEX    = 1;
	final int MINUTE1_COLUMN_INDEX = 0;	
	final int MINUTE2_ROW_INDEX    = 1;
	final int MINUTE2_COLUMN_INDEX = 1;
	
	final int SECONDS1_ROW_INDEX    = 2;
	final int SECONDS1_COLUMN_INDEX = 0;
	final int SECONDS2_ROW_INDEX    = 2;
	final int SECONDS2_COLUMN_INDEX = 1;

	final int MONTH1_ROW_INDEX    = 0;
	final int MONTH1_COLUMN_INDEX = 0;
	final int MONTH2_ROW_INDEX    = 1;
	final int MONTH2_COLUMN_INDEX = 0; 
	
	final int DAY1_ROW_INDEX 	  = 0;
	final int DAY1_COLUMN_INDEX   = 1;
	final int DAY2_ROW_INDEX	   = 1;
	final int DAY2_COLUMN_INDEX    = 1;
	
	final int YEAR1_ROW_INDEX      = 0;
	final int YEAR1_COLUMN_INDEX   = 2;
	final int YEAR2_ROW_INDEX      = 1;
	final int YEAR2_COLUMN_INDEX   = 2;
	
	//TEST RESULT VALUES, USED INTERNALLY IN TEST METHODS, EXPECTED VALUES TO TEST AGAINST
	final int TEST_SINGLE_DIGIT_HOUR1_VALUE        = 0;	
	final int TEST_SINGLE_DIGIT_HOUR2_VALUE        = 1;
	
	final int TEST_SINGLE_DIGIT_MINUTE1_VALUE        = 0;
	final int TEST_SINGLE_DIGIT_MINUTE2_VALUE        = 4;
	
	final int TEST_SINGLE_DIGIT_SECONDS1_VALUE        = 0;
	final int TEST_SINGLE_DIGIT_SECONDS2_VALUE        = 8;
	
	final int TEST_SINGLE_DIGIT_MONTH1_VALUE       = 0;
	final int TEST_SINGLE_DIGIT_MONTH2_VALUE 	   = 3;
	
	final int TEST_SINGLE_DIGIT_DAY1_VALUE		  = 0;
	final int TEST_SINGLE_DIGIT_DAY2_VALUE		   = 9;
	
	final int TEST_DOUBLE_DIGIT_HOUR1_VALUE        = 1;
	final int TEST_DOUBLE_DIGIT_HOUR2_VALUE        = 4;
	
	final int TEST_DOUBLE_DIGIT_MINUTE1_VALUE        = 5;
	final int TEST_DOUBLE_DIGIT_MINUTE2_VALUE        = 0;
	
	final int TEST_DOUBLE_DIGIT_SECONDS1_VALUE        = 5;
	final int TEST_DOUBLE_DIGIT_SECONDS2_VALUE        = 0;
	
	final int TEST_DOUBLE_DIGIT_MONTH1_VALUE        = 1;	
	final int TEST_DOUBLE_DIGIT_MONTH2_VALUE 		  = 0;
	
	final int TEST_DOUBLE_DIGIT_DAY1_VALUE		  = 1;
	final int TEST_DOUBLE_DIGIT_DAY2_VALUE		   = 0;
	
	final int TEST_DEFAULT_HOUR1_VALUE = 1;
	final int TEST_DEFAULT_HOUR2_VALUE = 4;
	final int TEST_DEFAULT_MINUTE1_VALUE = 5;
	final int TEST_DEFAULT_MINUTE2_VALUE = 0;
	final int TEST_DEFAULT_SECONDS1_VALUE = 5;
	final int TEST_DEFAULT_SECONDS2_VALUE = 0;
	
	final int TEST_DEFAULT_MONTH1_VALUE = 0;
	final int TEST_DEFAULT_MONTH2_VALUE = 3;
    final int TEST_DEFAULT_DAY1_VALUE = 0;
    final int TEST_DEFAULT_DAY2_VALUE = 9;		
	//years are never single or double, so one set of values suffices for testing
	final int TEST_YEAR1_VALUE          = 1;
	final int TEST_YEAR2_VALUE          = 2;
	//no single or double digits needed, one value suffices for testing
	final int TEST_MILLIS= 7;
	
	final int TEST_COUNTER = 1;
	
	//test VO holders
	private static StepOneVO TEST_STEPONE;
	private static StepTwoVO TEST_STEPTWO;
	private static StepThreeVO TEST_STEPTHREE;
	private static StepFourVO TEST_STEPFOUR;
	private static StepFiveVO TEST_STEPFIVE;
	private static StepSixVO TEST_STEPSIX;
	private static StepSevenVO TEST_STEPSEVEN;
	private static StepEightVO TEST_STEPEIGHT;

	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		util = new AlgorithmUtility();
		//cobertura has a bug where two char[] getters in the same class aren't testing if called sequentially
		//this is just to fix that issue
		StepEightVO cover = new StepEightVO();
		cover.getMatrix();
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
	 * Test step 1.
	 */
	@Test 
	public void testStep1() {
		//setup default calendar. when it generates it does not include milliseconds
		//so we add our default millisecond value for testing the default seed
		//from the algorithm doc
		DEFAULT_CALENDAR.set(Calendar.MILLISECOND, DEFAULT_MILLIS);
		
		StepOneVO step1 = util.stepOne(DEFAULT_CALENDAR);
		SimpleDateFormat format1 = new SimpleDateFormat("MM");
		String formatted = format1.format(DEFAULT_CALENDAR.getTime());

		//check date array
		int[][] dateArray = step1.getDateArray();
		
		//check the date array
		//check month values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of month in element to be" + TEST_DEFAULT_MONTH1_VALUE+ " but was " + dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX], dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX] == TEST_DEFAULT_MONTH1_VALUE);
		assertTrue("Expected 2nd digit of month in element to be" + TEST_DEFAULT_MONTH2_VALUE + "but was " + dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX], dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX] == TEST_DEFAULT_MONTH2_VALUE);
			
		//check day values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of day in element  to be " + TEST_SINGLE_DIGIT_DAY1_VALUE + " but was " + dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX], dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX] == TEST_DEFAULT_DAY1_VALUE);
		assertTrue("Expected 2nd digit of day in element  to be " + TEST_SINGLE_DIGIT_DAY2_VALUE + " but was"  + dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX], dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX] == TEST_DEFAULT_DAY2_VALUE);
			
		//check year values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of year in element to be " + TEST_YEAR1_VALUE + " but was" + dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX], dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX] == TEST_YEAR1_VALUE);
		assertTrue("Expected 2nd digit of year in element to be " + TEST_YEAR2_VALUE + " but was" + dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX], dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX] == TEST_YEAR2_VALUE);

		//check time array
		int[][] timeArray = step1.getTimeArray();
		//check the hour columns
		//check the time array
		//check the hour columns
		assertTrue("Expected 1st digit of the hour to be " + TEST_DEFAULT_HOUR1_VALUE + " but was " + timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX], timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX] == TEST_DEFAULT_HOUR1_VALUE);
		assertTrue("Expected 2nd digit of the hour to be " + TEST_DEFAULT_HOUR2_VALUE + " but was " + timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX], timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX] == TEST_DEFAULT_HOUR2_VALUE);
				
		//check the minute columns
		assertTrue("Expected 1st digit of the minute to be " + TEST_DEFAULT_MINUTE1_VALUE + " but was " + timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX], timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX] == TEST_DEFAULT_MINUTE1_VALUE);
		assertTrue("Expected 2nd digit of the minute to be " + TEST_DEFAULT_MINUTE2_VALUE + " but was " + timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX], timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX] == TEST_DEFAULT_MINUTE2_VALUE);
				
		//check the seconds columns
		//check the minute columns
		assertTrue("Expected 1st digit of the seconds to be " + TEST_DEFAULT_SECONDS1_VALUE + " but was " + timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX], timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_SECONDS1_VALUE);
		assertTrue("Expected 2nd digit of the seconds to be " + TEST_DEFAULT_SECONDS2_VALUE + " but was " + timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX], timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_SECONDS2_VALUE);
			
		//check the multiplied array
		int[][] multipliedArray = step1.getResultMultipliedDateTimeArray();
		
		//now check for expected values
		//multipliedArray[0][0] should = 5
		assertTrue("Expected value for cell [0][0] was 5, but was found to be " + multipliedArray[0][0], multipliedArray[0][0] == 5);
		
		//multipliedArray[0][1] should = 0
		assertTrue("Expected value for cell [0][1] was 0, but was found to be " + multipliedArray[0][1], multipliedArray[0][1] == 0);
		
		//multipliedArray[1][0] should = 58
		assertTrue("Expected value for cell [1][0] was 58, but was found to be " + multipliedArray[1][0], multipliedArray[1][0] == 58);
		
		//multipliedArray[1][1] should = 12
		assertTrue("Expected value for cell [1][1] was 12, but was found to be " + multipliedArray[1][1], multipliedArray[1][1] == 12);
		
		TEST_STEPONE = step1;

		logger.debug("TEST_STEP_ONE:{}", step1);
	}

	/**
	 * Test step 2.
	 */
	@Test 
	public void testStep2() {
		//get argument for step 2
		//neeed to add milliseconds to calendar
		DEFAULT_CALENDAR.set(Calendar.MILLISECOND, DEFAULT_MILLIS);
		
		StepOneVO step1 = util.stepOne(DEFAULT_CALENDAR);
		int forcePrime = 41;
		
		//get the step 2 vo
		StepTwoVO step2 = util.stepTwoSpecificPrime(step1, forcePrime);
		
		//grab the values to check
		int determinant = step2.getDeterminant();
		int determinantPlusSeed = step2.getResult();
		int[][] seedArray = step2.getSeedArray();
		
		//check 1st row built correctly
		assertTrue("Expected seed[0][0] to be 1 but was " + seedArray[0][0], seedArray[0][0] == 1);
		assertTrue("Expected seed[0][1] to be 5 but was " + seedArray[0][1], seedArray[0][1] == 5);
		assertTrue("Expected seed[0][2] to be 5 but was " + seedArray[0][2], seedArray[0][2] == 5);
		
		//check 2nd row built correctly
		assertTrue("Expected seedArray[1][0] to be 4 but was " + seedArray[1][0], seedArray[1][0] == 4);
		assertTrue("Expected seedArray[1][1] to be 0 but was " + seedArray[1][1], seedArray[1][1] == 0);
		assertTrue("Expected seedArray[1][2] to be 0 but was " + seedArray[1][2], seedArray[1][2] == 0);
		
		//check the 3rd row seed array was built correctly
		assertTrue("Expected seed[2][0] to be 7 but was " + seedArray[2][0], seedArray[2][0] == 7);
		assertTrue("Expected seed[2][1] to be 1 but was " + seedArray[2][1],  seedArray[2][1] == 1);
		assertTrue("Expected seed[2][2] to be 41 but was " + seedArray[2][2], seedArray[2][2] == 41);
		
		
		//check determinant is -800 per running test using example values in algorithm spec
		assertTrue("Determinant should be -800 but was " + determinant, determinant == -800);
		
		//check that the result of adding the determinant and seed was corect, per spec should -793
		assertTrue("Determinant plus seed1 should be -793 but was " + determinant, determinantPlusSeed == -793);
		
		TEST_STEPTWO = step2; //save for later tests
		
		logger.debug("TEST_STEPTWO:{}", step2);
	}

	/**
	 * Test step 3.
	 */
	@Test //@Ignore
	public void testStep3() {
		System.out.println("testing 3");
		StepThreeVO step3 = util.stepThree(TEST_STEPTWO);
		
		//get things to test
		int result = step3.getResult();
		
		assertTrue("Expeceted 27 but got " + result, result == 27);
		//set for later tests
		TEST_STEPTHREE = step3; //save for later tests 

		logger.debug("TEST_STEPTHREE:{}", step3);
	}

	/**
	 * Test step 4.
	 */
	@Test
	public void testStep4() {
		StepFourVO step4 = util.stepFour(TEST_STEPONE, TEST_STEPTHREE);
		//expect matrix this result 
		// 22 27
		// 31 15
		int[][] resultMatrix = step4.getResultMatrix();
		assertTrue("Expected cell [0][0] to be 22 but was " + resultMatrix[0][0], resultMatrix[0][0] == 22);
		assertTrue("Expected cell [0][1] to be 27 but was " + resultMatrix[0][1], resultMatrix[0][1] == 27);
		assertTrue("Expected cell [1][0] to be 31 but was " + resultMatrix[1][0], resultMatrix[1][0] == 31);
		assertTrue("Expected cell [1][1] to be 15 but was " + resultMatrix[1][1], resultMatrix[1][1] == 15);
		//set for later tests
		TEST_STEPFOUR = step4;

		logger.debug("TEST_STEPFOUR:{}", step4);
	}

	/**
	 * Test step 5.
	 */
	@Test
	public void testStep5() {
		StepFiveVO step5 = util.stepFive(TEST_STEPTWO);
		//expect this result: alphanumeric of 9
		assertTrue("Expect the alphanumeric character 9, but got " + step5.getfinalAlphaResult(), step5.getfinalAlphaResult().equals("9") );
		//set for later steps
		TEST_STEPFIVE = step5;
		
		logger.debug("TEST_STEPFIVE:{}", step5);
	}

	/**
	 * Test step 6.
	 */
	@Test 
	public void testStep6() {
		StepSixVO step6 = util.stepSix(TEST_STEPFOUR);
		//expect this result: alphanumeric of Y
		assertTrue("Expect the alphanumeric character of Y but got " + step6.getResultAlphaNumeric(), step6.getResultAlphaNumeric().equals("Y"));
		
		//set for later tests
		TEST_STEPSIX = step6;
		
		logger.debug("TEST_STEPSIX:{}", step6);
	}

	/**
	 * Test step 7.
	 */
	@Test
	public void testStep7() {
		StepSevenVO step7 = util.stepSeven(TEST_STEPFOUR, TEST_STEPFIVE, TEST_STEPSIX, DEFAULT_SELLER);
		char[] draftCode = step7.getDraftCode();
		assertTrue("expected X", draftCode[0] == ('X') );
		assertTrue("expected P", draftCode[1] == ('P') );
		assertTrue("expected 2", draftCode[2] == ('2') );
		assertTrue("expected 2", draftCode[3] == ('2') );
		assertTrue("expected 9", draftCode[4] == ('9') );
		assertTrue("expected Y", draftCode[5] == ('Y') );
		assertTrue("expected 2", draftCode[6] == ('2') );
		assertTrue("expected 7 but got " + String.valueOf(draftCode[7]), draftCode[7] == ('7') );
		assertTrue("expected empty:", draftCode[8] ==  Character.MIN_VALUE);
		assertTrue("expected empty", draftCode[9] ==   Character.MIN_VALUE);
		assertTrue("expected 3", draftCode[10] == ('3') );
		assertTrue("expected 1", draftCode[11] == ('1') );
		
		//save for later tests
		TEST_STEPSEVEN = step7;

		logger.debug("TEST_STEPSEVEN:{}", step7);
	}

	/**
	 * Test step 8.
	 */
	@Test 
	public void testStep8() {
		StepEightVO step8 = util.stepEight(TEST_STEPTWO, TEST_STEPFOUR);
		//expect these values
		//11  18
		//17  21
		int[][] thematrix = step8.getMatrix();
		assertTrue("Expected 11 but got " + thematrix[0][0], thematrix[0][0] == 11);
		assertTrue("Expected 18 but got " + thematrix[0][1], thematrix[0][1] == 18);
		assertTrue("Expected 17 but got " + thematrix[1][0], thematrix[1][0] == 17);
		assertTrue("Expected 21 but got " + thematrix[1][1], thematrix[1][1] == 21);
		//M V
		char[] chars = step8.getFinalChars();
		assertTrue("Expected M but got " + chars[0], chars[0] == 'M');
		assertTrue("Expected V but got " + chars[1], chars[1] == 'V');
		
		TEST_STEPEIGHT = step8;
		
		logger.debug("TEST_STEPEIGHT:{}", step8);
	}

	/**
	 * Test step nine.
	 */
	@Test 
	public void testStepNine() {
		StepNineVO step9 = util.stepNine(TEST_STEPSEVEN, TEST_STEPEIGHT);
		
		String expectedCode = "XP229Y27MV31";
		assertTrue("expected " + expectedCode + " but got " + step9.getFinalResCode(), step9.getFinalResCode().equals(expectedCode));
	
		
		logger.debug("TEST_STEPNINE:{}", step9);
	}

	/**
	 * Test that the method used to build a date array but called in the stepOne is built correctly when using single digit days and single digit months.
	 */
	@Test  
	public void testBuildDateArrayUsingSingleDigits() {
		//setup to get the date we will use for check
		Calendar THE_DATE =  new GregorianCalendar(DEFAULT_YEAR, SINGLE_DIGIT_MONTH, SINGLE_DIGIT_DAY_OF_MONTH, DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
		
		//and then construct it
		int[][] dateArray = util.buildDateArray(THE_DATE);
		
		//check the date array
		//check month values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of month in element to be" + TEST_SINGLE_DIGIT_MONTH1_VALUE+ " but was " + dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX], dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX] == TEST_SINGLE_DIGIT_MONTH1_VALUE);
		assertTrue("Expected 2nd digit of month in element to be" + TEST_SINGLE_DIGIT_MONTH2_VALUE + "but was " + dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX], dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX] == TEST_SINGLE_DIGIT_MONTH2_VALUE);
		
		//check day values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of day in element  to be " + TEST_SINGLE_DIGIT_DAY1_VALUE + " but was " + dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX], dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX] == TEST_SINGLE_DIGIT_DAY1_VALUE);
		assertTrue("Expected 2nd digit of day in element  to be " + TEST_SINGLE_DIGIT_DAY2_VALUE + " but was"  + dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX], dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX] == TEST_SINGLE_DIGIT_DAY2_VALUE);
		
		//check year values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of year in element to be " + TEST_YEAR1_VALUE + " but was" + dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX], dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX] == TEST_YEAR1_VALUE);
		assertTrue("Expected 2nd digit of year in element to be " + TEST_YEAR2_VALUE + " but was" + dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX], dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX] == TEST_YEAR2_VALUE);
	
	
	}
	
	/**
	 * Test that the method used to build a date array but called in the stepOne is built correctly when using double digit days and months.
	 */
	@Test
	public void testBuildDateArrayUsingDoubleDigits() {
		//setup to get the date we will use for check
		//use default for year, double digit for month and day of month, and default for hours/minutes/seconds
		Calendar THE_DATE =  new GregorianCalendar(DEFAULT_YEAR, DOUBLE_DIGIT_MONTH, DOUBLE_DIGIT_DAY_OF_MONTH, DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
		
		//and construct the date
		int[][] dateArray = util.buildDateArray(THE_DATE);
		
		//check the date array
		
		//check month values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of month in element to be" + TEST_DOUBLE_DIGIT_MONTH1_VALUE+ " but was " + dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX], dateArray[MONTH1_ROW_INDEX][MONTH1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_MONTH1_VALUE);
		assertTrue("Expected 2nd digit of month in element to be" + TEST_DOUBLE_DIGIT_MONTH2_VALUE + " but was " + dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX], dateArray[MONTH2_ROW_INDEX][MONTH2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_MONTH2_VALUE);
		
		//check day values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of day in element  to be " + TEST_DOUBLE_DIGIT_DAY1_VALUE + " but was " + dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX], dateArray[DAY1_ROW_INDEX][DAY1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_DAY1_VALUE);
		assertTrue("Expected 2nd digit of day in element  to be " + TEST_DOUBLE_DIGIT_DAY2_VALUE + " but was"  + dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX], dateArray[DAY2_ROW_INDEX][DAY2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_DAY2_VALUE);
		
		//check year values - using some local named variables to make the assert logic more human readable
		assertTrue("Expected 1st digit of year in element to be " + TEST_YEAR1_VALUE + " but was" + dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX], dateArray[YEAR1_ROW_INDEX][YEAR1_COLUMN_INDEX] == TEST_YEAR1_VALUE);
		assertTrue("Expected 2nd digit of year in element to be " + TEST_YEAR2_VALUE + " but was" + dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX], dateArray[YEAR2_ROW_INDEX][YEAR2_COLUMN_INDEX] == TEST_YEAR2_VALUE);
	}
	
	/**
	 * Test build time array using single digits.
	 * Test that the method used to build a time array but called in the stepOne is built correctly when using single digit hours/minutes/seconds,
	 * ie ones that begin with 0, like 03:04:09 for 3:4:9 AM.
	 */
	@Test 
	public void testBuildTimeArrayUsingSingleDigits() {
		
		//setup to get the date we will use for check, use default for day/month/year, single digit for hours/minutes/seconds 
		Calendar THE_DATE =  new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY_OF_MONTH, SINGLE_DIGIT_HOUR, SINGLE_DIGIT_MINUTE, SINGLE_DIGIT_SECOND);
		
		//and then call build method
		int[][] timeArray = util.buildTimeArray(THE_DATE);
		
		//check the time array
		//check the hour columns
		assertTrue("Expected 1st digit of the hour to be " + TEST_SINGLE_DIGIT_HOUR1_VALUE + " but was " + timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX], timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX] == TEST_SINGLE_DIGIT_HOUR1_VALUE);
		assertTrue("Expected 2nd digit of the hour to be " + TEST_SINGLE_DIGIT_HOUR2_VALUE + " but was " + timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX], timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX] == TEST_SINGLE_DIGIT_HOUR2_VALUE);
		
		//check the minute columns
		assertTrue("Expected 1st digit of the minute to be " + TEST_SINGLE_DIGIT_MINUTE1_VALUE + " but was " + timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX], timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX] == TEST_SINGLE_DIGIT_MINUTE1_VALUE);
		assertTrue("Expected 2nd digit of the minute to be " + TEST_SINGLE_DIGIT_MINUTE2_VALUE + " but was " + timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX], timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX] == TEST_SINGLE_DIGIT_MINUTE2_VALUE);
		
		
		//check the seconds columns
		//check the minute columns
		assertTrue("Expected 1st digit of the seconds to be " + TEST_SINGLE_DIGIT_SECONDS1_VALUE + " but was " + timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX], timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX] == TEST_SINGLE_DIGIT_SECONDS1_VALUE);
		assertTrue("Expected 2nd digit of the seconds to be " + TEST_SINGLE_DIGIT_SECONDS2_VALUE + " but was " + timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX], timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX] == TEST_SINGLE_DIGIT_SECONDS2_VALUE);
	}
	
	/**
	 * Test build time array using double digits.
	 * Test that the method used to build a time array but called in the stepOne is built correctly when using double digit hours/minutes/seconds.
	 */
	@Test 
	public void testBuildTimeArrayUsingDoubleDigits() {
	
		//setup to get the date we will use for check
		//default for year, month, day, double digit for hour minute second
		Calendar THE_DATE =  new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY_OF_MONTH, DOUBLE_DIGIT_HOUR, DOUBLE_DIGIT_MINUTE, DOUBLE_DIGIT_SECOND);
		
		//and then call build method
		int[][] timeArray = util.buildTimeArray(THE_DATE);
		
		//check the time array
		//check the hour columns
		assertTrue("Expected 1st digit of the hour to be " + TEST_DOUBLE_DIGIT_HOUR1_VALUE + " but was " + timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX], timeArray[HOUR1_ROW_INDEX][HOUR1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_HOUR1_VALUE);
		assertTrue("Expected 2nd digit of the hour to be " + TEST_DOUBLE_DIGIT_HOUR2_VALUE + " but was " + timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX], timeArray[HOUR2_ROW_INDEX][HOUR2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_HOUR2_VALUE);
		
		//check the minute columns
		assertTrue("Expected 1st digit of the minute to be " + TEST_DOUBLE_DIGIT_MINUTE1_VALUE + " but was " + timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX], timeArray[MINUTE1_ROW_INDEX][MINUTE1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_MINUTE1_VALUE);
		assertTrue("Expected 2nd digit of the minute to be " + TEST_DOUBLE_DIGIT_MINUTE2_VALUE + " but was " + timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX], timeArray[MINUTE2_ROW_INDEX][MINUTE2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_MINUTE2_VALUE);
		
		//check the seconds columns
		//check the minute columns
		assertTrue("Expected 1st digit of the seconds to be " + TEST_DOUBLE_DIGIT_SECONDS1_VALUE + " but was " + timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX], timeArray[SECONDS1_ROW_INDEX][SECONDS1_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_SECONDS1_VALUE);
		assertTrue("Expected 2nd digit of the seconds to be " + TEST_DOUBLE_DIGIT_SECONDS2_VALUE + " but was " + timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX], timeArray[SECONDS2_ROW_INDEX][SECONDS2_COLUMN_INDEX] == TEST_DOUBLE_DIGIT_SECONDS2_VALUE);
	}
	

		/**
		 * Test multiply date and time arrays.
		 */
		@Test
		public void testMultiplyDateAndTimeArrays() {
			//WE WILL USE DEFAULT VALUES WHICH ARE CONSISTENT WITH EXAMPLES IN SPEC DOCUMENT
			
			// setup to get the date we will use for check
			Calendar THE_DATE = new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY_OF_MONTH, DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);

			// and then date and time arrays
			int[][] dateArray = util.buildDateArray(THE_DATE);
			int[][] timeArray = util.buildTimeArray(THE_DATE);
			
			//now get the multipled array
			int[][] multipliedArray = util.multiplyByMatrix(dateArray, timeArray);
				
			//now check for expected values
			//multipliedArray[0][0] should = 5
			assertTrue("Expected value for cell [0][0] was 5, but was found to be " + multipliedArray[0][0], multipliedArray[0][0] == 5);
			
			//multipliedArray[0][1] should = 0
			assertTrue("Expected value for cell [0][1] was 0, but was found to be " + multipliedArray[0][1], multipliedArray[0][1] == 0);
			
			//multipliedArray[1][0] should = 58
			assertTrue("Expected value for cell [1][0] was 58, but was found to be " + multipliedArray[1][0], multipliedArray[1][0] == 58);
			
			//multipliedArray[1][1] should = 12
			assertTrue("Expected value for cell [1][1] was 12, but was found to be " + multipliedArray[1][1], multipliedArray[1][1] == 12);
		}
		
		/**
		 * Test build milli seed array.
		 */
		@Test
		public void testBuildSeedArray() {
			// setup to get the date we will use for check
			Calendar date = new GregorianCalendar(DEFAULT_YEAR, DEFAULT_MONTH, DEFAULT_DAY_OF_MONTH, DEFAULT_HOUR, DEFAULT_MINUTE, DEFAULT_SECOND);
			date.set(Calendar.MILLISECOND , DEFAULT_MILLIS);
		
			//reset counter to 0
			util.setSeedCounter(0);
			
			//seedArray should be milliseconds, counter, prime
			int[][] seedArray = util.buildSeedArray(date);
			
			//TODO check row 1
			//TODO Check row 2
			
			//check row 3
			//check first value at [2][0]
			assertTrue("Expected seed [2][0] (milliseconds) to be " + TEST_MILLIS + " but was found to be " + seedArray[2][0],  TEST_MILLIS == seedArray[2][0]);		
			//check 2nd (counter) value at [2][1]
			assertTrue("Expected milli [2][1] (counter) to be " + TEST_COUNTER + ", but was found to be " + seedArray[2][1],  TEST_COUNTER == seedArray[2][1] );
			//check 3rd value at [0][2]
			assertTrue("Expected milli [2][2] to be PRIME, but was found to be not prime, " + seedArray[2][2],  true == isPrime(seedArray[2][2]));
		}
		
		/**
		 * Test build random prime.
		 */
		@Test
		public void testBuildRandomPrime() {
			int shouldBePrime = util.buildRandomPrime();
			
			assertTrue("Expected a prime but got " + shouldBePrime, true == isPrime(shouldBePrime));
		}
		
		/**
		 * Test counter reset logic.
		 */
		@Test
		public void testCounterResetLogic() {
			util.setSeedCounter(Integer.MAX_VALUE);
			
			util.buildCounter();
			
			assertTrue("expected build counter to be reset to zero but was " + util.getSeedCounter(), util.getSeedCounter() == 0);
		}
		
		/**
		 * Private helper method to test prime value in seed array. Checks if a number is prime.
		 *
		 * @param n the n
		 * @return true, if is prime
		 */
		private boolean isPrime(int n) {
			//ordinarily multiple exist points from a method is bad practice, but this is short and fast
		    //check if n is a multiple of 2
		    if (n%2==0) return false;
		    //if not, then just check the odds
		    for(int i=3;i*i<=n;i+=2) {
		        if(n%i==0)
		            return false;
		    }
		    return true;
		}
	
}


