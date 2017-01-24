/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 *
 */
public class Step3VO extends ParentStepVO {

	/** The result. */
	private int result;
	
	/**
	 * Default Constructor
	 */
	public Step3VO() {
		super();
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(int result) {
		this.result = result;
	}
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step3VO: { ");
		buff.append(System.lineSeparator());
		buff.append("result=");
		buff.append(result);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
