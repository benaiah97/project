/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 *
 */
public class Step5VO extends ParentStepVO {

	/** The final alpha result for the suffix on the reservation code. */
	private String step5AlphaResult;
	
	/**
	 * Default constructor
	 */
	public Step5VO() {
		super();
	}


	/**
	 * @return the result
	 */
	public String getStep5AlphaResult() {
		return step5AlphaResult;
	}

	/**
	 * @param finalAlphaValue the result to set
	 */
	public void setStep5AlphaResult(String finalAlphaValue) {
		this.step5AlphaResult = finalAlphaValue;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step5VO: { ");
		buff.append(System.lineSeparator());
		buff.append("step5AlphaResult=");
		buff.append(step5AlphaResult);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
