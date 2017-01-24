/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * The Class ParentStepVO.
 *
 * @author MOONS012
 */
public class ParentStepVO {

	/**
	 * Instantiates a new parent step VO.
	 */
	public ParentStepVO() {
		super();
	}
	
	/**  Holds the numeric chars that are assembled into a reservation code at algorithm completion. */
	protected StringBuilder numericChars = new StringBuilder();
	
	/** Holds the alpha chars that are assembled into a reservation code at algorithm completion. */
	protected StringBuilder alphaChars = new StringBuilder();
	
	/** Holds the alpha numeric chars that are assembled into a reservation code at algorithm completion. */
	protected StringBuilder alphaNumericChars = new StringBuilder(); 

	
	
	
	/**
	 * Convenience method. Append numeric chars to numeric char[] used to build rescode.
	 *
	 * @param chars the chars
	 */
	public void appendNumericChars(char[] chars) {
		this.numericChars.append(chars);
	}
	
	/**
	 * Convenience method. Append numeric char to numeric char[] used to build rescode.
	 *
	 * @param c the c
	 */
	public void appendNumericChar(char c) {
		this.numericChars.append(c);
	}	
	
	
	/**
	 * Convenience method. Append alpha chars.
	 *
	 * @param chars the chars
	 */
	public void appendAlphaChars(char[] chars) {
		this.alphaChars.append(chars);
	}
	
	/**
	 * Convenience method. Append alhpa char.
	 *
	 * @param c the c
	 */
	public void appendAlhpaChar(char c) {
		this.alphaChars.append(c);
	}
	
	/**
	 * Convenience method. Append alpha numeric chars.
	 *
	 * @param chars the chars
	 */
	public void appendAlphaNumericChars(char[] chars) {
		this.alphaNumericChars.append(chars);
	}
	
	/**
	 * Convenience method. Append alpha numeric char.
	 *
	 * @param c the c
	 */
	public void appendAlphaNumericChar(char c) {
		this.alphaNumericChars.append(c);
	}
	
	
	/**
	 * Convenience method. Append alpha numeric chars.
	 *
	 * @param chars the chars
	 */
	public void appendAlphaNumericString(String s) {
		this.alphaNumericChars.append(s);
	}
	
	/**
	 * Convert 2 D int array to string.
	 *
	 * @param array the array
	 * @return the string
	 */
	protected String convert2DIntArrayToString(int[][] array) { 
		StringBuffer buff = new StringBuffer();
		String result = new String();
		 for(int i=0; i<array.length; i++) {
		        for(int j=0; j<array[i].length; j++) {
		            buff.append(String.valueOf(array[i][j]));
		            buff.append(" \t");
		        }
		        buff.append(System.lineSeparator());
		    }

		return buff.toString();
	}
}
