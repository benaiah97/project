/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.utility;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * The Class AlgorithmUtility. Contains all the matrix &amp;
 * math calculations used by the RaceAPI.
 *
 * @author MOONS012
 */
public class AlgorithmUtility {
	
	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(AlgorithmUtility.class);
	
	/** The seed counter. Accessor methods are protected as we don't really won't them used outside of the race module.
	 * Incrementation and reset to zer0 are handled by the buildCounter method. */
	protected static int seedCounter = 0;
	
	/** The Constant MOD_CONSTANT. */
	private static final int MOD_CONSTANT=33;
	
	/** The last time
	
	/** CONSTANT array is used to translate a number to its equivalent alpha or numeric representation in several steps.
	 * _ char is added at 0 index so we can start at one to more easily match algorithm spec document
	 * */
	protected static final char[] ALPHA_ARRAY = "ABCDEFGHJKLMNPQRTUVWXYZ0123456789".toCharArray(); 

	/**
	 * Generate res code.
	 *
	 * @param tktSellerPrefix the ticket seller prefix
	 * @return the string
	 */
	public static String generateResCode(String tktSellerPrefix) {
		//It may seem like this method is passing around a lot of heavy VO when running through each algorithm step.
		//This is intentional. Each critical calculation that is part of the over all algorithm is set on the
		//VO for that step. This was done so that comprehensive unit tests could be written that would be
		//able to test the calculations of each step and within each step.
		StringBuffer resCode = new StringBuffer();
		
		// setup to get the date we will use for step one
		Calendar theDate = new GregorianCalendar();
	
		logger.debug("Begin generateRescode:{}", theDate.getTime());
	
		// Algorithm Step 1. Perform   matrix multiplication (mm/dd/yy * hh:mm:ss) to derive a 2x2 matrix. 
		StepOneVO stepOneVO = AlgorithmUtility.stepOne(theDate);
	
		// Algorithm Step 2. Compute the determinant of the 3x3 matrix consisting of the hh:mm:ss
		StepTwoVO stepTwoVO = AlgorithmUtility.stepTwo(stepOneVO);

		// 3.	Compute the following:   (“Step 2 Determinant” + Seed 1) MOD prime number.  
		// Adding the “seed” value slightly skews the determinant, and MODing by the prime will limit
		// our results to one and two digit numbers.
		StepThreeVO stepThreeVO = AlgorithmUtility.stepThree(stepTwoVO);

		// 4.	Subtract the value (deteriminant) from Step 3 from each element of our date/time matrix (from Step 1), and apply
		//an absolute value over each difference (no negative numbers can be in our reservation code)
		StepFourVO stepFourVO  = AlgorithmUtility.stepFour(stepOneVO, stepThreeVO);
		
		// 5.	Compute the following: “Step 2 Determinant” MOD 33
		StepFiveVO stepFiveVO = AlgorithmUtility.stepFive(stepTwoVO);
		
		// 6. Compute the determinate of the “result” matrix from Step 4.  
		StepSixVO stepSixVO = AlgorithmUtility.stepSix(stepFourVO);
				
		// 7. Appending:  (1) the Ticket Seller prefix, (2) the “result” matrix elements, and (3) the two “tag” values, 
		// will yield our “draft” reservation code. At a minimum, there will be two remaining alphanumeric values left
		// to fill.  
		StepSevenVO stepSevenVO = AlgorithmUtility.stepSeven(stepFourVO, stepFiveVO, stepSixVO, tktSellerPrefix);

		// 8. Take each matrix element (from the “result” matrix), ADD the third “seed” value (prime) to the element’s 
		// MOD the result by 33.  Translate L and U to the ALPHA_ARRAY   
		StepEightVO stepEightVO = AlgorithmUtility.stepEight(stepTwoVO, stepFourVO);
		
		// 9. The remaining array elements (from Step 8) will be used to fill in any empty numeric in the reservation 
		//code.
		StepNineVO stepNineVO = AlgorithmUtility.stepNine(stepSevenVO, stepEightVO);
		//get the rescode from the last step
		
		//and finally return the created rescode
		resCode.append(stepNineVO.getFinalResCode());
		
		//log each step
		logger.debug("StepOne: {}", stepOneVO);
		logger.debug("StepTwo: {}", stepTwoVO);
		logger.debug("StepThree: {}", stepThreeVO);
		logger.debug("StepFour: {}", stepFourVO);
		logger.debug("StepFive: {}", stepFiveVO);
		logger.debug("StepSix: {}", stepSixVO);
		logger.debug("StepSeven: {}", stepSevenVO);
		logger.debug("StepEight: {}", stepEightVO);
		logger.debug("StepNine: {}", stepNineVO);
		logger.debug("End generateRescode:{}", theDate.getTime());
		
		return resCode.toString();
	}
	
	/**
	 * Step one.
	 * Derives a 2x2 matrix from matrix multiplication of date (mm/dd/yy) and time (hh/mm/ss)
	 *
	 * @param startDateTime the start date time
	 * @return the object
	 */
	protected static StepOneVO  stepOne(Calendar startDateTime) {
		StepOneVO stepOneVO = new StepOneVO();

		//1) set the date on the vo
		stepOneVO.setCalendar(startDateTime);
		
		// 2) build 3x2 date array and set on vo
		int[][] dateArrayMatrix = AlgorithmUtility.buildDateArray(stepOneVO.getCalendar());
		stepOneVO.setDateArray(dateArrayMatrix);
		
		// 3) build 2x3 time array and set on vo
		int[][] timeArrayMatrix = AlgorithmUtility.buildTimeArray(stepOneVO.getCalendar());
		stepOneVO.setTimeArray(timeArrayMatrix);
		
		//4 multiple date array by Times Array and set on vo
		int [][] multipliedArray = multiplyByMatrix(dateArrayMatrix, timeArrayMatrix);
		
		stepOneVO.setResultMultipliedDateTimeArray(multipliedArray);
		
		return stepOneVO;
	}
	 
	/**
	 * Step two. Compute the determinant of the 3x3 matrix consisting of the hh:mm:ss
	 * (due to the range of values being so diverse), in addition to a “seed” value.
	 *  The first element of the “seed” will be the milliseconds in the timestamp; 
	 *  the second element will be a “counter”;  third element will be a prime number (1 or 2 digit prime only).
	 *
	 * @param stepOne the step one
	 * @return the object
	 */
	protected static StepTwoVO stepTwo(StepOneVO stepOne) {
		StepTwoVO stepTwo = new StepTwoVO();
		int MILLI_ROW = 2, MILLI_COL = 0;
		
		//build the millisecond seed array and set it on step two vo (overriding the prime)
		int[][] seedArray = buildSeedArray(stepOne.getCalendar());
		stepTwo.setSeedArray(seedArray);
		
		//compute the determinant using seed matrix
		int  milliSeedValue = seedArray[MILLI_ROW][MILLI_COL]; 
		int determinant = computeStepTwoDeterminant(seedArray);
		//add the determinant and the millisecond value from the seedArrray
		int result = determinant + milliSeedValue;
		
		//set the computed values on the returned vo for this step
		stepTwo.setDeterminant(determinant);
		stepTwo.setResult(result);

		return stepTwo;
	}	
	
	/**
	 * Step two specific prime. - used for testing only
	 *
	 * @param stepOne the step one
	 * @param breakPrimeDirective the prime
	 * @return the step two VO
	 */
	protected static StepTwoVO stepTwoSpecificPrime(StepOneVO stepOne, int breakPrimeDirective) {
		StepTwoVO stepTwo = new StepTwoVO();
		int MILLI_ROW = 2, MILLI_COL = 0;
		
		//build the millisecond seed array and set it on step two vo (overriding the prime)
		int[][] seedArray = buildSeedArray(stepOne.getCalendar());
		seedArray[2][2] = breakPrimeDirective;
		stepTwo.setSeedArray(seedArray);
		
		//compute the determinant using seed matrix
		int  milliSeedValue = seedArray[MILLI_ROW][MILLI_COL]; 
		int determinant = computeStepTwoDeterminant(seedArray);
		//add the determinant and the millisecond value from the seedArrray
		int result = determinant + milliSeedValue;
		
		//set the computed values on the returned vo for this step
		stepTwo.setDeterminant(determinant);
		stepTwo.setResult(result);

		return stepTwo;
	}	
	
	
	/**
	 * Private helper method. Used in step two to compute step two determinant.
	 *
	 * @param seedArray the seed array
	 * @return the int
	 */
	protected static int computeStepTwoDeterminant(int[][] seedArray) {		
		//grab the values from the matrices to make this slightly more human readable
		//row 1
		int hourDigit1 = seedArray[0][0];
		int minDigit1  = seedArray[0][1];
		int secDigit1 = seedArray[0][2];
		
		//row 2
		int hourDigit2 = seedArray[1][0];
		int minDigit2 = seedArray[1][1];
		int secDegit2 = seedArray[1][2];
		
		//row 3
		int seed1Milli = seedArray[2][0]; //millisecond seed
		int seed2Counter = seedArray[2][1]; //couu
		int seed3Prime = seedArray[2][2];
		
		//check intermediate values
		int val1 = hourDigit1 * ( (minDigit2 * seed3Prime) - (seed2Counter * secDegit2) );
		int val2 = (minDigit1)*( (hourDigit2 * seed3Prime) - (seed1Milli * secDegit2));
		int val3 = secDigit1 *( (hourDigit2 * seed2Counter) - (seed1Milli * secDegit2) );
	
		//calculate determinant
		int determinant = hourDigit1 * ( (minDigit2 * seed3Prime) - (seed2Counter * secDegit2) ) - (minDigit1)*( (hourDigit2 * seed3Prime) - (seed1Milli * secDegit2)) + secDigit1 *( (hourDigit2 * seed2Counter) - (seed1Milli * minDigit2) );

		return determinant;
	}

	/**
	 * Step three.
	 * 	Compute the following:   (“Step 2 Determinant” + Seed 1 ie StepTwo.result) MOD prime number. 
	 * Adding the “seed” value slightly skews the determinant, and MODing by the prime will limit
	 * our results to one and two digit numbers.
	 *
	 * @param stepTwo the step two
	 * @return the object
	 */
	protected static StepThreeVO stepThree(StepTwoVO stepTwo) {
		// create our return VO, calculation and result holders
		StepThreeVO stepThree = new StepThreeVO();
		int seed3PrimeDirective = stepTwo.getSeedArray()[2][2];
		int stepTwoResult = stepTwo.getResult();
		
		//make our calculation
		int stepThreeResult = flooredModulo(stepTwoResult, seed3PrimeDirective);
		
		//set the result on our vo
		stepThree.setResult(stepThreeResult);
		
		//return the VO
		return stepThree;
	}
	
	/**
	 * Step four. Subtracts the determinant from each element of the matrix in step 1, 
	 * taking the absolute value as a result.
	 *
	 * @param stepOne the step one
	 * @param stepThree the step three
	 * @return the StepFourVO
	 */
	protected static StepFourVO stepFour(StepOneVO stepOne, StepThreeVO stepThree) {
		StepFourVO stepFour = new StepFourVO();

		int[][] resultMatrix = stepOne.getResultMultipliedDateTimeArray();
		// let's loop through array to subtract the step 3 results
		for (int row = 0; row < resultMatrix.length; row++) 
		{ 
			for (int col = 0; col < resultMatrix[row].length; col++) { 
				resultMatrix[row][col] = Math.abs(resultMatrix[row][col] - stepThree.getResult()); 
			}
		}

		//set this on our returned VO
		stepFour.setResultMatrix(resultMatrix);
		//and return
		return stepFour;
	}	
	
	/**
	 * Step five. Compute the following: “Step 2 Determinant” MOD 33 (we’ll translate this last number into its 
	 * 	   equivalent alpha or numeric representation from this array:  ABCDEFGHJKLMNPQRTUVWXYZ0123456789). 
	 * 	   We’ll append this “tag” value as an alphanumeric in the “draft” reservation code.
	 *
	 * @param stepTwo the step two
	 * @return the object
	 */
	protected static StepFiveVO stepFive(StepTwoVO stepTwo) {
		//create our return vo
		StepFiveVO stepFive = new StepFiveVO();

		//calculate the final alpha value
		int numericValue  =flooredModulo(stepTwo.getResult(), MOD_CONSTANT);
		//int numericValue  =flooredModulo(stepTwo.getResult(), buildRandomPrime());
		
		//convert to alphanumeric
		String finalAlphaValue = String.valueOf(ALPHA_ARRAY[numericValue]);
		
		//set on vo
		stepFive.setFinalAlphaResult(finalAlphaValue);
		
		//and return
		return stepFive;
	}
	
	/**
	 * Step six. Compute the determinate of the “result” matrix from Step 4.  We will MOD this determinate by 33
	 * (in the same manner as Step 5), in order to derive another alphanumeric “tag” for the reservation code.
	 *
	 * @param stepFour the step four
	 * @return the object
	 */
	protected static StepSixVO stepSix(StepFourVO stepFour) {
		//create our return object
		StepSixVO stepSix = new StepSixVO();
		
		//compute intermediate values, then determinant
		int value1 = stepFour.getResultMatrix()[0][0] * stepFour.getResultMatrix()[1][1];
		int value2 = stepFour.getResultMatrix()[1][0] * stepFour.getResultMatrix()[0][1];
		int determinant = value1 - value2;
		
		//mod with mod constant
		int numericValue = flooredModulo(determinant, MOD_CONSTANT);
		//int numericValue = flooredModulo(determinant, buildRandomPrime());
		
		//convert to alphanumeric
		String finaNumericlAlphaValue = String.valueOf(ALPHA_ARRAY[numericValue]);
		
		//set for return
		stepSix.setResultAlphaNumeric(finaNumericlAlphaValue);
		
		//and return it
		return stepSix  ;
	}	
	
	/**
	 * Step seven.  Appending:  (1) the Ticket Seller prefix, (2) the “result” matrix elements, and 
	 * (3) the two “tag” values, will yield our “draft” reservation code. At a minimum, there will be
	 *  two remaining alphanumeric values left to fill.   
	 *
	 * @param stepFourVO the step four VO
	 * @param stepFive the step five
	 * @param stepSix the step six
	 * @param tktSellerPrefix the tkt seller prefix
	 * @return the object
	 */
	protected static StepSevenVO stepSeven(StepFourVO stepFourVO, StepFiveVO stepFive, StepSixVO stepSix, String tktSellerPrefix) {
		//create the vo we will return
		StepSevenVO stepSeven = new StepSevenVO();
		
		//create the draft code we will add to the vo
		char[] draftCode = new char[12];
		
		//set the prefix on the first two chars of the draft rescode
		draftCode[0]= tktSellerPrefix.charAt(0);
		draftCode[1]= tktSellerPrefix.charAt(1);
		
		//we need to check each individual value,  format for leading zeros
		//set the next two characters from the stepFour matrix first cell [0][0]
		String val2And3 = String.format("%02d", stepFourVO.getResultMatrix()[0][0]); ;		
		draftCode[2] = val2And3.charAt(0);
		draftCode[3] = val2And3.charAt(1);
		
	
		//set the next value from the step 5 alpha numeric translation
		draftCode[4] = stepFive.getfinalAlphaResult().charAt(0);
		
		//set the next char from the step 6 alpha numeric translation
		draftCode[5] = stepSix.getResultAlphaNumeric().charAt(0);
		
		//set the next 2 char from the step 4 matrix cell [0][1], again format for leading zeros
		String val6And7 = String.format("%02d",stepFourVO.getResultMatrix()[0][1]);
		draftCode[6] = val6And7.charAt(0);
		draftCode[7] = val6And7.charAt(1);	
		
		//the next two are left blank
		//skipping draftCode[8] and draftCode[9]
		
		//set the final two chars from the step 4 [1][0] again format for leading zeros
		String val10And11 =  String.format("%02d", stepFourVO.getResultMatrix()[1][0]);
		draftCode[10] = val10And11.charAt(0);
		draftCode[11] = val10And11.charAt(1);
		
		//add the draftcode to the vo
		stepSeven.setDraftCode(draftCode);

		//and return it
		return stepSeven;
	}
	
	/**
	 * Step eight. Take each matrix element (from the “result” matrix), multiply by the third “seed” value (prime)
	 *  to the element’s value (in order to skew the element slightly), MOD the result by 33.  
	 *  We’ll translate the first row’s elements into their equivalent alpha or numeric representation from
	 *  this array:  AlgorithmUtility.ALPHA_ARRAY ABCDEFGHJKLMNPQRTUVWXYZ0123456789.    
	 *  
	 *
	 * @param step2 the step 2
	 * @param step4 the step 4
	 * @return the object
	 */
	protected static StepEightVO stepEight(StepTwoVO step2, StepFourVO step4) {
		//create our vo to return
		StepEightVO step8 = new StepEightVO();
		
		int[][] matrix = step4.getResultMatrix();
		int seed3PrimeDirective = step2.getSeedArray()[2][2];
		char[] finalChars = new char[2];
		
		//loop through matrix and multiply eache element by seed and mod by 33
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[row].length; col++) 
			{ 	
				
				matrix[row][col] =  flooredModulo((matrix[row][col] * seed3PrimeDirective), MOD_CONSTANT);				
			} 
		}	
		//set it on the vo
		step8.setMatrix(matrix);
		//translate value from matrix to characters and set that on the VO too
		finalChars[0] = ALPHA_ARRAY[ matrix[0][0] ];
		finalChars[1] = ALPHA_ARRAY[ matrix[0][1] ];
		step8.setFinalChars(finalChars);
		//and return
		return step8;
	}

	
	/**
	 * Step nine.
	 *
	 * @param stepSevenVO the step seven VO
	 * @param stepEightVO the step eight VO
	 * @return the object
	 */
	protected static StepNineVO stepNine(StepSevenVO stepSevenVO, StepEightVO stepEightVO) {
		//create our vo to return
		StepNineVO step9 = new StepNineVO();
		
		//start building our final rescode from the draft code
		char[] finalResCode = stepSevenVO.getDraftCode();
		
		//finalResCode[8] and [9] are blanke, set them using char translation from step eightVO
		finalResCode[8] = stepEightVO.getFinalChars()[0];
		finalResCode[9] = stepEightVO.getFinalChars()[1];
		
		//convert to string and set on our vo
		step9.setFinalResCode(new String(finalResCode));

		return step9;
	}
	
	/**
	 * Helper method. Builds a date array (used by step one of the algorithm).
	 * It is protected rather than private so that our test class can test it.
	 *
	 * @param calendar the calendar
	 * @return the int[][]
	 */
	protected static int[][] buildDateArray(Calendar calendar){
		// arrays of date data, number of rows and columns for the date array, 2x3
		final int NBR_ROWS = 2;
		final int NBR_COLUMNS = 3;
		
		int[][] dateArray = new int[NBR_ROWS][NBR_COLUMNS];
		// this format
		// m1 d1 y1
		// m2 d2 y2

		// set invididual array values
		// set column one (month 1st digit and month 2nd digit)
		dateArray[0][0] = AlgorithmUtility.buildMonthDigit1(calendar); //m1
		dateArray[1][0] = AlgorithmUtility.buildMonthDigit2(calendar); //m2
		
		// set column two (day 1st digit and day 2nd digit)
		dateArray[0][1] = AlgorithmUtility.buildDayDigit1(calendar); //d1
		dateArray[1][1] = AlgorithmUtility.buildDayDigit2(calendar); //d2 
		
		// set column 3 (year 1st digit and year 2nd digit)
		dateArray[0][2] = AlgorithmUtility.buildYearDigit1(calendar); //y1 
		dateArray[1][2] = AlgorithmUtility.buildYearDigit2(calendar); //y2 
		
		return dateArray;
	}
	
	/**
	 * Builds the time array.
	 *
	 * @param calendar the calendar
	 * @return the int[][]
	 */
	protected static int[][] buildTimeArray(Calendar calendar) {
		//arrays of time data, 3x2
		final int NBR_ROWS = 3;
		final int NBR_COLUMNS = 2;
		
		int[][] timeArray = new int[NBR_ROWS][NBR_COLUMNS]; 
		//this format
		// h1 h2 
		// m1 m2
		// s1 s2

	
		//unlike date, we set up by row instead of colum
		//setup hour row
		timeArray[0][0] = AlgorithmUtility.buildHourDigit1(calendar);
		timeArray[0][1] = AlgorithmUtility.buildHourDigit2(calendar);
		
		//setup minute row
		timeArray[1][0] = AlgorithmUtility.buildMinuteDigit1(calendar);
		timeArray[1][1] = AlgorithmUtility.buildMinuteDigit2(calendar);
		
		//setup second row
		timeArray[2][0] = AlgorithmUtility.buildSecondsDigit1(calendar);
		timeArray[2][1] = AlgorithmUtility.buildSecondsDigit2(calendar);
		
		return timeArray;
	}
	
	/**
	 * Private helper method Builds the  seed array using the milliseconds from our time, a per instance counter,
	 * and a randomly selected prime #.
	 *
	 * @param calendar the calendar
	 * @return the int[][]
	 */
	protected static int[][] buildSeedArray(Calendar calendar) {
		int[][] seedArray = new int[3][3];
	
		//row 1: hour digit 1, minute digit 1, sec digit 1
		seedArray[0][0] = AlgorithmUtility.buildHourDigit1(calendar) ; //hour 1
		seedArray[0][1] = AlgorithmUtility.buildMinuteDigit1(calendar) ; //min 1
		seedArray[0][2]	= AlgorithmUtility.buildSecondsDigit1(calendar) ; //sec 1
		//row 2: hour digit 2, minute digit 2, seconds digit 2		
		seedArray[1][0] = AlgorithmUtility.buildHourDigit2(calendar);
		seedArray[1][1] = AlgorithmUtility.buildMinuteDigit2(calendar);
		seedArray[1][2] = AlgorithmUtility.buildSecondsDigit2(calendar);
		//row 3: seed 1 millis, seed2 counter, seed3 random primve				
		seedArray[2][0] =  AlgorithmUtility.buildMilliSeconds(calendar);
		seedArray[2][1] = AlgorithmUtility.buildCounter();
		seedArray[2][2] = AlgorithmUtility.buildRandomPrime();
				
		return seedArray;
	}
	
	/**
	 * Private helper method. Gets the first digit of a 2 digit month from a calendar object. 
	 *
	 * @param cal the cal
	 * @return the m1
	 */
	private static int buildMonthDigit1(Calendar cal) {
		int monthDigitOne;
		
		//format the calendar date as a two digit month
		SimpleDateFormat format1 = new SimpleDateFormat("MM");
		String formatted = format1.format(cal.getTime());
		
		//get the first character of the two digit data
		monthDigitOne = Integer.parseInt( formatted.substring(0,1) ) ;
		
		return monthDigitOne;
	}
	
	/**
	 * Private helper method. Gets the 2nd digit of a 2digit month from a calendar object.
	 *
	 * @param cal the cal
	 * @return the m2
	 */
	private static int buildMonthDigit2(Calendar cal) {
		int monthDigitTwo;
		
		int MONTH_OFFSET = 1; //CALENDARS START MONTHS AT ZERO SO WE NEED TO OFFSET THEM BY ONE
		
		//format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("MM");
		String formatted = format1.format(cal.getTime());

		//get the last character of the two digit data
		monthDigitTwo = Integer.parseInt( formatted.substring(formatted.length()-1) ) - MONTH_OFFSET ;
		
		return monthDigitTwo;
	}
	
	/**
	 * Private Helper method. Gets the first digit of the 2 digit day of the month from a Calendar. 
	 *
	 * @param cal the cal
	 * @return the d1
	 */
	private static int buildDayDigit1(Calendar cal) {
		int dayDigitOne;		
		
		//format the calendar day as a two digit day of the month
		SimpleDateFormat format1 = new SimpleDateFormat("dd");
		String formatted = format1.format(cal.getTime());
		
		//get the first character of the two digit data
		dayDigitOne = Integer.parseInt( formatted.substring(0,1) );
		
		return dayDigitOne;
	}

	/**
	 * Private Helper method. Gets the 2nd digit of the 2 digit day of the month from a Calendar. 
	 *
	 * @param cal the cal
	 * @return the d2
	 */
	private static int buildDayDigit2(Calendar cal) {
		int dayDigitTwo;
		
		//format the calendar day as a two digit day of the month
		SimpleDateFormat format1 = new SimpleDateFormat("dd");
		String formatted = format1.format(cal.getTime());
		
		//get the last character of the two digit data
		dayDigitTwo = Integer.parseInt( formatted.substring(formatted.length()-1) ) ;
		
		return dayDigitTwo;
	}
	
	/**
	 * Private helper method. Returns the first digit of the 2 digit year from a calendar.
	 *
	 * @param cal the cal
	 * @return the y1
	 */
	private static int buildYearDigit1(Calendar cal) {
		int yearDigitOne;

		// format the calendar date as a two digit year
		SimpleDateFormat format1 = new SimpleDateFormat("yy");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		yearDigitOne = Integer.parseInt(formatted.substring(0, 1));

		return yearDigitOne;
	}
	
	/**
	 * Private helper method. Returns the 2nd digit of the 2 digit year from a calendar.
	 *
	 * @param cal the cal
	 * @return the year digit 2
	 */
	private static int buildYearDigit2(Calendar cal) {
		int yearDigitTwo;
		
		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("yy");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		yearDigitTwo =  Integer.parseInt( formatted.substring(formatted.length()-1) ) ;

		return yearDigitTwo;
	}
	
	/**
	 * Private helper method. Returns the first digit of the 2 digit hour from a calendar.
	 *
	 * @param cal the cal
	 * @return the hour digit 1
	 */
	private static int buildHourDigit1(Calendar cal) {
		int hourDigitOne;
		
		// format the calendar date as a two digit year
		SimpleDateFormat format1 = new SimpleDateFormat("HH");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		hourDigitOne = Integer.parseInt(formatted.substring(0, 1));
		
		return hourDigitOne;
	}
	
	/**
	 * Private helper method. Returns the 2nd digit of the 2 digit hour from a calendar.
	 *
	 * @param cal the cal
	 * @return the hour digit 2
	 */
	private static int buildHourDigit2(Calendar cal) {
		int yearDigitTwo;
		
		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("HH");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		yearDigitTwo =  Integer.parseInt( formatted.substring(formatted.length()-1) ) ;
		
		return yearDigitTwo;
	}
	
	/**
	 * Private helper method. Returns the first digit of the 2 digit minute from a calendar.
	 *
	 * @param cal the cal
	 * @return the minute digit 1
	 */
	private static int buildMinuteDigit1(Calendar cal) {
		int minuteDigitOne;

		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("mm");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		minuteDigitOne = Integer.parseInt(formatted.substring(0, 1));
		
		return minuteDigitOne;
	}
	
	/**
	 * Private helper method. Returns the 2nd digit of the 2 digit minute from a calendar.
	 *
	 * @param cal the cal
	 * @return the minute digit 2
	 */
	private static int buildMinuteDigit2(Calendar cal) {
		int minuteDigitTwo;

		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("mm");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		minuteDigitTwo = Integer.parseInt(formatted.substring(formatted.length() - 1));

		return minuteDigitTwo;
	}
	
	/**
	 * Private helper method. Returns the first digit of the 2 digit seconds from a calendar.
	 *
	 * @param cal the cal
	 * @return the seconds digit 1
	 */
	private static int buildSecondsDigit1(Calendar cal) {
		int secondsDigitOne;

		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("ss");
		String formatted = format1.format(cal.getTime());

		// get the first character of the two digit data
		secondsDigitOne = Integer.parseInt(formatted.substring(0, 1));

		return secondsDigitOne;
	}
	
	/**
	 * Private helper method. Returns the 2nd digit of the 2 digit seconds from a calendar.
	 *
	 * @param cal the cal
	 * @return the seconds digit 2
	 */
	private static int buildSecondsDigit2(Calendar cal) {
		int secondsDigitTwo;
		
		// format the calendar date as a two digit dat
		SimpleDateFormat format1 = new SimpleDateFormat("ss");
		String formatted = format1.format(cal.getTime());

		// get the second character of the two digit data
		secondsDigitTwo = Integer.parseInt(formatted.substring(formatted.length() - 1));

		return secondsDigitTwo;
	}
	
	/**
	 * Private helper method. Returns the milliseconds from a calendar
	 *
	 * @param cal the cal
	 * @return the milliseconds
	 */
	private static int buildMilliSeconds(Calendar cal) {
		int milliSeconds;
		
		//format the calendar date as three digit milliseconds
		SimpleDateFormat format1 = new SimpleDateFormat("SSS");
		String formatted = format1.format(cal.getTime());
		milliSeconds = Integer.parseInt(formatted);
		
		return milliSeconds;
	}
	
	/**
	 * Private helper method. Increments instance counter and returns it for the seed arrray 2nd element
	 *
	 * @return the seed counter
	 */
	protected static int buildCounter() {
		//if the seed counter has reached the max allowed system value reset it, otherwise increment it
		seedCounter = (seedCounter == Integer.MAX_VALUE) ? 0 : seedCounter + 1;
		
		return seedCounter;
	}
	
	/**
	 * protected helper method. Calulates a prime number with a bit length of 5 using a secure random seed.
	 *
	 * @return a random prime
	 */
	protected static int buildRandomPrime() {
		int randomPrime;
		final int  BIT_LENGTH = 5; //arbitrary choise for getting probably prime
		
		Random rnd = new SecureRandom(); 
        randomPrime = (BigInteger.probablePrime(BIT_LENGTH, rnd)).intValue();
        
		return randomPrime;		
	}

	/**
	 * Multiply by matrix.
	 *
	 * @param matrix1 the matrix 1
	 * @param matrix2 the matrix 2
	 * @return the int[][]
	 */
	protected static int[][] multiplyByMatrix(int[][] matrix1, int[][] matrix2) {
    	logger.debug("entering multiplyMatrix");
		int[][] matrixResult = new int[matrix1.length][matrix2[0].length];

		Integer matrix1Rows; // rows in matrix1
		Integer matrix1Cols; // cols in matrix1
		Integer matrix2Cols; // cols in matrix2

		for (matrix2Cols = 0; matrix2Cols <= matrix2[0].length - 1; matrix2Cols++) {
			for (matrix1Rows = 0; matrix1Rows <= matrix1.length - 1; matrix1Rows++) {
				for (matrix1Cols = 0; matrix1Cols <= matrix1[0].length - 1; matrix1Cols++) {

					logger.debug("matrix1[" + matrix1Rows + "][" + matrix1Cols + "]: " + matrix1[matrix1Rows][matrix1Cols]);
					logger.debug("matrix2[" + matrix1Cols + "][" + matrix2Cols + "]: " + matrix2[matrix1Cols][matrix2Cols]);
					
					matrixResult[matrix1Rows][matrix2Cols] += matrix1[matrix1Rows][matrix1Cols]
							* matrix2[matrix1Cols][matrix2Cols];
					
					logger.debug("iteration  " + matrix1Cols.toString() + ": " + matrix1[matrix1Rows][matrix1Cols] * matrix2[matrix1Cols][matrix2Cols]);
					logger.debug("aggregated value: " + matrixResult[matrix1Rows][matrix2Cols]);
				}
				logger.debug("  *** matrixResult[" + matrix1Rows + "][" + matrix2Cols + "]: " + matrixResult[matrix1Rows][matrix2Cols]);

			}
		}
    	logger.debug("exiting multiplyMatrix");
		return matrixResult;
	}
	
	/** Returns the internal seedCounter. Intentionally protected instead of private for testability.
	 * @return the seedCounter
	 */
	protected static int getSeedCounter() {
		return seedCounter;
	}

	/**
	 * Sets the internal seed counter. Intentionally protected instead of private for testability.
	 * @param seedCounter the seedCounter to set
	 */
	protected static void setSeedCounter(int seedCounter) {
		AlgorithmUtility.seedCounter = seedCounter;
	}
	
	/**
	 * Floored modulo. Adding because jdk 1.7 doesn't have the new Math abilities in 1.8 like Math.floorMod()
	 *
	 * @param a the a
	 * @param n the n
	 * @return the int
	 */
	private static int flooredModulo(int a, int n){
		  return  n<0 ? -flooredModulo(-a, -n) : mod(a, n);
		}
	
	/**
	 * Mod. Adding to support floored Modulo
	 *
	 * @param a the a
	 * @param n the n
	 * @return the int
	 */
	static int mod(int a, int n){    
		  return a<0 ? (a%n + n)%n : a%n;
		}
}
