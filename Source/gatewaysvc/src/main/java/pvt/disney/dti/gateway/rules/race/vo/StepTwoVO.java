package pvt.disney.dti.gateway.rules.race.vo;


/**
 * The Class StepTwoVO.
 *  A Value Object that holds results of algorithm calculations for Step Two 
 * @author MOONS012
 */
public class StepTwoVO extends ParentStepVO {
	
	/** The seed array used for seed in step 2. */
	private int[][] seedArray = new int[1][3];
	
	/** The determinant. */
	private int determinant;
	
	/** The result. Holds the final result for step 2 of algorithm*/
	private int result;
	
	/**
	 * Instantiates a new step two VO.
	 */
	public StepTwoVO() {
		super();
	}
	
	/**
	 * @return the seedArray
	 */
	public int[][] getSeedArray() {
		return seedArray;
	}
	/**
	 * @param seedArray the seedArray to set
	 */
	public void setSeedArray(int[][] seedArray) {
		this.seedArray = seedArray;
	}

	/**
	 * @return the determinant
	 */
	public int getDeterminant() {
		return determinant;
	}

	/**
	 * @param determinant the determinant to set
	 */
	public void setDeterminant(int determinant) {
		this.determinant = determinant;
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
		buff.append("StepTwoVO: { ");
		buff.append(System.lineSeparator());
		buff.append("seedArray:");
		buff.append(System.lineSeparator());
		buff.append(this.convert2DIntArrayToString(seedArray));
		buff.append(System.lineSeparator());
		buff.append("determinant=");
		buff.append(determinant);
		buff.append(System.lineSeparator());
		buff.append("result=");
		buff.append(result);
		buff.append(System.lineSeparator());
		buff.append(" }");
		
		return buff.toString();
	}
}
