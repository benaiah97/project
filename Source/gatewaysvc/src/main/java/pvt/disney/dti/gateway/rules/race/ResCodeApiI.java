package pvt.disney.dti.gateway.rules.race;

/**
 * The Interface ResCodeAPI.
 * 
 * @author MOONS012
 */
public interface ResCodeApiI {
	
	/**
	 * Creates the res code for payload id.
	 *
	 * @param prefix the prefix
	 * 
	 * @return the string rescode
	 */
	public String generateResCode(String prefix);


}
