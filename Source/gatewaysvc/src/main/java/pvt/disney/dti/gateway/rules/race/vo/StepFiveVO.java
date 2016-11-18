/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 *
 */
public class StepFiveVO extends ParentStepVO {

	/** The final alpha result for the suffix on the reservation code. */
	private String finalAlphaResult;
	
	/**
	 * Default constructor
	 */
	public StepFiveVO() {
		super();
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("StepFiveVO: { ");
		buff.append(System.lineSeparator());
		buff.append("finalAlphaResult=");
		buff.append(finalAlphaResult);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
