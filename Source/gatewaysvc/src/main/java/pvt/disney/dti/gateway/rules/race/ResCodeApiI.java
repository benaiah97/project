package pvt.disney.dti.gateway.rules.race;

/**
 * The Interface ResCodeAPI.
 * 
 * @author MOONS012
 * @since 2.16.3
 */
public interface ResCodeApiI {
	
	/**
	 * Creates the res code for payload id.
	 *
	 * @param prefix the prefix
	 * @param payloadId the payload id
	 * @return the string rescode
	 */
	public String generateResCode(String prefix);

}
