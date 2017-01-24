package pvt.disney.dti.gateway.rules.race.vo;


/**
 * The Class Step10WdwVO.
 */
public class Step10WdwVO extends ParentStepVO {

	/** The res code. */
	public char[] draftCode;
	
	/**
	 * Instantiates a new step 10 VO.
	 */
	public Step10WdwVO(char[] draftCode) {
		this.draftCode = draftCode;
	}

	/**
	 * @return the draftCode
	 */
	public char[] draftcode() {
		return draftCode;
	}

	/**
	 * @return the draftCode
	 */
	public char[] getDraftCode() {
		return draftCode;
	}

	/**
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
		buff.append("Step10WdwVO: { ");
		buff.append(System.lineSeparator());
		buff.append("draftCode=");
		buff.append(String.valueOf(draftCode));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}

}
