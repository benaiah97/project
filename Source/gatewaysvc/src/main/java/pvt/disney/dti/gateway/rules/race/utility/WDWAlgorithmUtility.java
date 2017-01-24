package pvt.disney.dti.gateway.rules.race.utility;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pvt.disney.dti.gateway.rules.race.vo.Step10WdwVO;
import pvt.disney.dti.gateway.rules.race.vo.Step1VO;
import pvt.disney.dti.gateway.rules.race.vo.Step2VO;
import pvt.disney.dti.gateway.rules.race.vo.Step3VO;
import pvt.disney.dti.gateway.rules.race.vo.Step4VO;
import pvt.disney.dti.gateway.rules.race.vo.Step5VO;
import pvt.disney.dti.gateway.rules.race.vo.Step6VO;
import pvt.disney.dti.gateway.rules.race.vo.Step7VO;
import pvt.disney.dti.gateway.rules.race.vo.Step8VO;
import pvt.disney.dti.gateway.rules.race.vo.Step9VO;

public class WDWAlgorithmUtility extends AlgorithmUtility {

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(WDWAlgorithmUtility.class);
	
	

	/**
	 * Generate res code for WDW
	 *
	 * @param tktSellerPrefix the ticket seller prefix
	 * @return the string
	 */
	public static String generateResCode() {		
		// setup to get the date we will use for step one
		Calendar theDate = new GregorianCalendar();
		logger.debug("Begin generateRescode:{}", theDate.getTime());
		
		// Algorithm Step 1 - uses parent class method 
		Step1VO stepOneVO = AlgorithmUtility.stepOne(theDate);
	
		// Algorithm Step 2. - uses parent class method
		Step2VO stepTwoVO = AlgorithmUtility.stepTwo(stepOneVO);
		
		// 3. - uses parent class method
		Step3VO stepThreeVO = AlgorithmUtility.stepThree(stepTwoVO);

		// 4.	Subtract the value (deteriminant) from Step 3 from each element of our date/time matrix (from Step 1), and apply
		//an absolute value over each difference (no negative numbers can be in our reservation code)
		// uses parent class method
		Step4VO stepFourVO  = AlgorithmUtility.stepFour(stepOneVO, stepThreeVO);
		
		//5. Compute the following: “Step 2 Determinant” using MOD - Uses Parent method
		Step5VO stepFiveVO = AlgorithmUtility.stepFive(stepTwoVO, ALPHA_MOD_CONSTANT, ALPHA_ARRAY);

		// 6. Compute the determinate of the “result” matrix from Step 4 using MOD - uses parent method
		Step6VO stepSixVO = AlgorithmUtility.stepSix(stepFourVO, ALPHA_MOD_CONSTANT, ALPHA_ARRAY);
		
		//step 7 uses a wdw variation instead of parent AlgoritmUtility step
		//for last to chars of rescode at index 10 and 11
		Step7VO stepSevenVO = WDWAlgorithmUtility.stepSevenWDW(stepFourVO, stepFiveVO, stepSixVO);
		
		//step 8 uses a wdw varation intead of parent AlgorithUtility step	
		// 8. Take each matrix element (from the “result” matrix), ADD the third “seed” value (prime) to the element’s 
		// MOD the result by 20.  Translate L and U to the ALPH_ARRAY   
		Step8VO stepEightVO = AlgorithmUtility.stepEight(stepTwoVO, stepFourVO, ALPHA_MOD_CONSTANT); //W(stepTwoVO, stepFourVO);
		
		//Step 9 uses a WDW variation
		// 9. The remaining array elements (from Step 8) will be used to fill in any empty numeric in the reservation 
		//code.
		Step9VO stepNineVO = WDWAlgorithmUtility.stepNineWdw(stepSevenVO, stepEightVO);

		Step10WdwVO step10WdwVO = WDWAlgorithmUtility.stepTenWdw(stepTwoVO, stepNineVO);
		//get the rescode from the last step and finish the rescode to return
		//resCode.append(stepNineVO.getStep9ResCode());
				
		//log each step
		logger.debug("StepOne: {}", stepOneVO);
		logger.debug("StepTwo: {}", stepTwoVO);
		logger.debug("StepThree: {}", stepThreeVO);
		logger.debug("StepFour: {}", stepFourVO);
		logger.debug("StepFiveWdw: {}", stepFiveVO);
		logger.debug("StepSix: {}", stepSixVO);
		logger.debug("StepSevenWdw: {}", stepSevenVO);
		logger.debug("StepEight: {}", stepEightVO);
		logger.debug("StepNineWdw: {}", stepNineVO);
		logger.debug("Step10Wdw: {}", step10WdwVO);
		logger.debug("End generateRescode:{}", theDate.getTime());
				
		String draftResCode = new String ( step10WdwVO.getDraftCode() );
		
		//check for prohibited word and start over if one is found
		if(prohibitedWordCheck(draftResCode)) {
			logger.warn("Probibited word found: regenerating reservation code");
			draftResCode = WDWAlgorithmUtility.generateResCode();
		}
		
		return draftResCode;
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
	protected static Step7VO stepSevenWDW(Step4VO stepFourVO, Step5VO stepFive, Step6VO stepSix) {
		//create the vo we will return
		Step7VO stepSeven = new Step7VO();
		
		//create the draft code we will add to the vo
		char[] draftCode = new char[12];
		
		//set the first tw0 alpha characters for the wdw rescode as the first 4 chars of the draft code
		//these come from the step that gave us chars 4,5
		draftCode[0] = stepFive.getStep5AlphaResult().charAt(0);
		draftCode[1] = stepSix.getStep6Result().charAt(0); 
	
		draftCode[2] = ' '; //determined in STEP 8 - similar to chars 8,9 on the hKDL variation
		draftCode[3] = ' '; //determined IN STEP 8 - similar to chars 8,9 on the hKDL variation

		
		//set the 8 numeric characters		
/*
		draftCode[4] - determined in step 10, will be prepended to numericChars, unique to WDW
		draftCode[5] - determined in step 10, will be prepended to numericChars,unique to WDW
*/	
		//next two numeric codes
		String val6And7 = String.format("%02d",stepFourVO.getResultMatrix()[0][1]);
		draftCode[6] = val6And7.charAt(0); //calculated as per char 6 for HKDL
		draftCode[7] = val6And7.charAt(1);  //calculated as per char 7 for HKDL
		
		stepSeven.appendNumericChar(val6And7.charAt(0));
		stepSeven.appendNumericChar(val6And7.charAt(1));
		
		//next two numeric codes
		String val8and9 =  String.format("%02d", stepFourVO.getResultMatrix()[1][0]);

		draftCode[8] = val8and9.charAt(0); //calculated as per char 10 for HKDL
		draftCode[9] = val8and9.charAt(1); //calculated as per char 11 for HKDL
		
		stepSeven.appendNumericChar(val8and9.charAt(0));
		stepSeven.appendNumericChar(val8and9.charAt(1));
		
		String val10and11 = String.format("%02d", stepFourVO.getResultMatrix()[0][0]); 
		draftCode[10] = val10and11.charAt(0); // calculated as per char 2 for HKDL
		draftCode[11] = val10and11.charAt(1); // calculated as per char 3 for HKDL
		stepSeven.appendNumericChar(val10and11.charAt(0));
		stepSeven.appendNumericChar(val10and11.charAt(1));

		stepSeven.setDraftCode(draftCode);

		//and return it
		return stepSeven;
	}
	
	
	/**
	 * Step nine.
	 *
	 * @param stepSevenVO the step seven VO
	 * @param stepEightVO the step eight VO
	 * @return the object
	 */
	protected static Step9VO stepNineWdw(Step7VO stepSevenVO, Step8VO stepEightVO) {
		//create our vo to return
		Step9VO step9 = new Step9VO();
		
		//start building our final rescode from the draft code
		char[] step9DraftResCode = stepSevenVO.getDraftCode();
		
		//finalResCode[4 and [5] are blank, set them using some thing special (this is the avoid
		//legacy conflict)
		step9DraftResCode[4] = ' ';
		step9DraftResCode[5] = ' ';
		
		System.out.println("checking step8vo:" + stepEightVO);
		
		//from step 8
		step9DraftResCode[2] = stepEightVO.getDraftChars()[0];
		step9DraftResCode[3] = stepEightVO.getDraftChars()[1];
		
		
		
		//convert to string and set on our vo
		step9.setStep9ResCode(step9DraftResCode);

		return step9;
	}
	
	protected static Step10WdwVO stepTenWdw(Step2VO step2, Step9VO step9) {
		Step10WdwVO step10 = new Step10WdwVO(step9.getStep9ResCode());
		char[] draftCode = step9.getStep9ResCode();
		

		//calculate missing 2 digits
		char[] step10Numbers = new char[2];
		
		//get new matrix to create to new numeric characters (to avoid duplication from previous steps.
		//and to allow us to create a numeric code that starts at a higher value
		//we need a new step one
		// Algorithm Step 1. Perform   matrix multiplication (mm/dd/yy * hh:mm:ss) to derive a 2x2 matrix. 
		// setup to get the date we will use for step one
		Calendar theDate = new GregorianCalendar();
		Step1VO newStepOneVO = AlgorithmUtility.stepOne(theDate);
		
		int[][] newMatrix = WDWAlgorithmUtility.buildStep10Matrix(newStepOneVO);
		
		
		int[][] legacyMatrix = newMatrix; //used to create a numeric character to avoid dups in legacy ats ticket system
		int[][] numericMatrix = newMatrix; //used to create a random numeric character

		int seed3PrimeDirective = WDWAlgorithmUtility.buildStep10SeedDeterminant(newStepOneVO);

		
		//loop through matrix and multiply eache element by seed and mod by appropriate value
		for (int row = 0; row < newMatrix.length; row++) {
			for (int col = 0; col < newMatrix[row].length; col++) 
			{ 	
				legacyMatrix[row][col] =  flooredModulo((legacyMatrix[row][col] * seed3PrimeDirective), LEGACY_NUMERIC_MOD_CONSTANT);				
				numericMatrix[row][col] =  flooredModulo((numericMatrix[row][col] * seed3PrimeDirective), NUMERIC_MOD_CONSTANT);
			} 
		}	
		
		int legacyInt = legacyMatrix[0][0];
		int nonLegacyInt =  numericMatrix[0][1];

		//translate value from matrix to characters and set that on the VO too
		step10Numbers[0] = legacyInt < LEGACY_NUMERIC_MOD_CONSTANT  ? 
				(  NUMERIC_ARRAY[ 9 - (legacyMatrix[0][0]) ]) : NUMERIC_ARRAY[ legacyMatrix[0][0] ];	 
		step10Numbers[1] = NUMERIC_ARRAY[ numericMatrix[0][1]];
		
		//set the draft rescode update
		draftCode[4] = step10Numbers[0];
		draftCode[5] = step10Numbers[1]; 
				
		step10.setDraftCode(draftCode);
		
		return step10;
	} 
	//
	
	/**
	 * Helper method.Builds the step 10 matrix. This is needed so we use a different seed matrix for calculating
	 * two numeric values in order to avoid duplication of 2 digits within the rescode from previous steps.
	 *
	 * @return the int[][]
	 */
	protected static int[][] buildStep10Matrix(Step1VO stepOneVO) {
		//we reuse step 1 to 4 to rebuild a seed matrix.

			
		// Algorithm Step 2. Compute the determinant of the 3x3 matrix consisting of the hh:mm:ss
		Step2VO stepTwoVO = AlgorithmUtility.stepTwo(stepOneVO);

		// 3.	Compute the following:   (“Step 2 Determinant” + Seed 1) MOD prime number.  
		// Adding the “seed” value slightly skews the determinant, and MODing by the prime will limit
		// our results to one and two digit numbers.
		Step3VO stepThreeVO = AlgorithmUtility.stepThree(stepTwoVO);
		
	    // 4.	Subtract the value (deteriminant) from Step 3 from each element of our date/time matrix (from Step 1), and apply
		//an absolute value over each difference (no negative numbers can be in our reservation code)
		Step4VO stepFourVO  = AlgorithmUtility.stepFour(stepOneVO, stepThreeVO);
		
		return stepFourVO.getResultMatrix();
	}
	
	/**
	 * Helper method Builds the step 10 seed determinant.
	 *
	 * @param newStepOneVO the new step one VO
	 * @return the int
	 */
	protected static int buildStep10SeedDeterminant(Step1VO newStepOneVO) {
		Step2VO newStepTwoVO = AlgorithmUtility.stepTwo(newStepOneVO);
		
		return newStepTwoVO.getSeedArray()[2][2];
	}

	/**
	 * Helper meathod Prohibited word check.
	 *
	 * @param draftResCode the draft res code
	 * @return true, if successful
	 */
	protected static boolean prohibitedWordCheck (String draftResCode) {
		boolean containsProhibitedword = false;
		
		WordCipher cipher = new WordCipher();
		
		//check if rescode contains any prohibited words
		try {
			for (String word : cipher.getWordCollection()) {
				if (draftResCode.contains(word)) {
					containsProhibitedword = true;
					break;
				}
			}
		} catch (IOException e) {
			logger.error("IOExceptionrror using WordCipher to check for prohibited words.");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			logger.error("URISyntaxException using WordCipher to check for prohibited words.");
			e.printStackTrace();
		}

		return containsProhibitedword;
	}
}
