package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class StepSevenVO.
 * 
 * @author MOONS012
 */
public class StepSevenVO extends ParentStepVO {

	/** The draft code. */
	private char[] draftCode;
	
	/**
	 * Instantiates a new step seven VO.
	 */
	public StepSevenVO() {
		super();
	}

	/**
	 * Gets the draft code.
	 *
	 * @return the draftCode
	 */
	public char[] getDraftCode() {
		return draftCode;
	}

	/**
	 * Sets the draft code.
	 *
	 * @param draftCode the draftCode to set
	 */
	public void setDraftCode(char[] draftCode) {
		this.draftCode = draftCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("StepSevenVO: { ");
		buff.append(System.lineSeparator());
		buff.append("draftCode=");
		buff.append(String.valueOf(draftCode));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
