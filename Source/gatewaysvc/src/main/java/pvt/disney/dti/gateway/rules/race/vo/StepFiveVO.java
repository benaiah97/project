/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 * @since 2.16.3
 *
 */
public class StepFiveVO {

	/** The final alpha result for the suffix on the reservation code. */
	private String finalAlphaResult;
	
	/**
	 * Default constructor
	 */
	public StepFiveVO() {
	}


	/**
	 * @return the result
	 */
	public String getfinalAlphaResult() {
		return finalAlphaResult;
	}

	/**
	 * @param finalAlphaValue the result to set
	 */
	public void setFinalAlphaResult(String finalAlphaValue) {
		this.finalAlphaResult = finalAlphaValue;
	}
}
