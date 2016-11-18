package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class StepSixVO.
 * 
 * @author MOONS012
 */
public class StepSixVO extends ParentStepVO {

	/** The result alpha numeric. */
	private String resultAlphaNumeric;
	
	/**
	 * Instantiates a new step six VO.
	 */
	public StepSixVO() {
		super();
	}

	/**
	 * Gets the result alpha numeric.
	 *
	 * @return the resultAlphaNumeric
	 */
	public String getResultAlphaNumeric() {
		return resultAlphaNumeric;
	}

	/**
	 * Sets the result alpha numeric.
	 *
	 * @param resultAlphaNumeric the resultAlphaNumeric to set
	 */
	public void setResultAlphaNumeric(String resultAlphaNumeric) {
		this.resultAlphaNumeric = resultAlphaNumeric;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("StepSixVO: { ");
		buff.append(System.lineSeparator());
		buff.append("resultAlphaNumeric=");
		buff.append(this.resultAlphaNumeric);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
