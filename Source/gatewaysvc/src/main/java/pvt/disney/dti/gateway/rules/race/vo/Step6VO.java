package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class Step6VO.
 * 
 * @author MOONS012
 */
public class Step6VO extends ParentStepVO {

	/** The result alpha numeric. */
	private String step6Result;
	
	/**
	 * Instantiates a new step six VO.
	 */
	public Step6VO() {
		super();
	}

	/**
	 * Gets the result alpha numeric.
	 *
	 * @return the step6Result
	 */
	public String getStep6Result() {
		return step6Result;
	}

	/**
	 * Sets the result alpha numeric.
	 *
	 * @param step6Result the step6Result to set
	 */
	public void setStep6Result(String resultAlphaNumeric) {
		this.step6Result = resultAlphaNumeric;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step6VO: { ");
		buff.append(System.lineSeparator());
		buff.append("step6Result=");
		buff.append(this.step6Result);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
