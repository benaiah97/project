package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class Step9VO.
 * 
 * @author MOONS012
 */
public class Step9VO extends ParentStepVO {

	/** The (partial) rescode as calculated for step9. */
	private char[] step9ResCode; 

	/**
	 * Instantiates a new step nine VO.
	 */
	public Step9VO() {
	}

	/**
	 * Gets the final res code.
	 *
	 * @return the step9ResCode
	 */
	public char[] getStep9ResCode() {
		return step9ResCode;
	}

	/**
	 * Sets the final res code.
	 *
	 * @param step9ResCode the step9ResCode to set
	 */
	public void setStep9ResCode(char[] resCode) {
		this.step9ResCode = resCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step9VO: { ");
		buff.append(System.lineSeparator());
		buff.append("step9ResCode=");
		buff.append(step9ResCode);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
