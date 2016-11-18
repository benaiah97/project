package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class StepNineVO.
 * 
 * @author MOONS012
 */
public class StepNineVO extends ParentStepVO {

	/** The final res code. */
	private String finalResCode;
	
	/**
	 * Instantiates a new step nine VO.
	 */
	public StepNineVO() {
	}

	/**
	 * Gets the final res code.
	 *
	 * @return the finalResCode
	 */
	public String getFinalResCode() {
		return finalResCode;
	}

	/**
	 * Sets the final res code.
	 *
	 * @param finalResCode the finalResCode to set
	 */
	public void setFinalResCode(String finalResCode) {
		this.finalResCode = finalResCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("SteNineVO: { ");
		buff.append(System.lineSeparator());
		buff.append("finalResCode=");
		buff.append(finalResCode);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
