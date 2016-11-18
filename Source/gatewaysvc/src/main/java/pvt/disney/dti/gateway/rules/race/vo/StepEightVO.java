package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class StepEightVO.
 * 
 * @author MOONS012
 */
public class StepEightVO extends ParentStepVO {

	/** The matrix. */
	private int[][] matrix;
	
	/** The final chars. */
	private char[] finalChars;
	
	/**
	 * Instantiates a new step eight VO.
	 */
	public StepEightVO() {
		super();
	}

	/**
	 * Gets the matrix.
	 *
	 * @return the matrix
	 */
	public int[][] getMatrix() {
		return this.matrix;
	}

	/**
	 * Sets the matrix.
	 *
	 * @param matrix the matrix to set
	 */
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the finalChars
	 */
	public char[] getFinalChars() {
		return finalChars;
	}

	/**
	 * @param finalChars the finalChars to set
	 */
	public void setFinalChars(char[] finalChars) {
		this.finalChars = finalChars;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("StepEightVO: { ");
		buff.append(System.lineSeparator());
		buff.append("matrix:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(matrix));
		buff.append(System.lineSeparator());
		buff.append("finalChars=");
		buff.append(String.valueOf(finalChars));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
