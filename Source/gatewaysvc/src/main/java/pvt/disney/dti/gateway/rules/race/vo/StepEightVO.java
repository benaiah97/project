package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class StepEightVO.
 * 
 * @author MOONS012
 * @since 2.16.3
 */
public class StepEightVO {

	/** The matrix. */
	private int[][] matrix;
	
	/** The final chars. */
	private char[] finalChars;
	
	/**
	 * Instantiates a new step eight VO.
	 */
	public StepEightVO() {
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

	
}
