/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.vo;

/**
 * @author MOONS012
 *
 */
public class ParentStepVO {

	/**
	 * 
	 */
	public ParentStepVO() {
		super();
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
