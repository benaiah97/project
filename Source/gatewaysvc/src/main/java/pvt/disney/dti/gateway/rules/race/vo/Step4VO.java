/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 *
 */
public class Step4VO extends ParentStepVO {

	/** The result matrix. */
	private int[][] resultMatrix;
	
	/**
	 * Default Constructor
	 */
	public Step4VO() {
		super();
	}

	/**
	 * @return the resultMatrix
	 */
	public int[][] getResultMatrix() {
		return resultMatrix;
	}

	/**
	 * @param resultMatrix the resultMatrix to set
	 */
	public void setResultMatrix(int[][] resultMatrix) {
		this.resultMatrix = resultMatrix;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step4VO: { ");
		buff.append(System.lineSeparator());
		buff.append("resultMatrix:");
		buff.append(System.lineSeparator());
		buff.append(convert2DIntArrayToString(resultMatrix));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
