package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class Step8VO.
 * 
 * @author MOONS012
 */
public class Step8VO extends ParentStepVO {

	/** The matrix. */
	private int[][] matrix;
	
	/** The final chars. */
	private char[] draftChars;
	
	/**
	 * Instantiates a new step eight VO.
	 */
	public Step8VO() {
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
	 * @return the draftChars
	 */
	public char[] getDraftChars() {
		return draftChars;
	}

	/**
	 * @param draftChars the draftChars to set
	 */
	public void setDraftChars(char[] finalChars) {
		this.draftChars = finalChars;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append(System.lineSeparator());
		buff.append("Step8VO: { ");
		buff.append(System.lineSeparator());
		buff.append("matrix:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(matrix));
		buff.append(System.lineSeparator());
		buff.append("draftChars=");
		buff.append(String.valueOf(draftChars));
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
