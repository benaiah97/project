/**
 * 
 */
package pvt.disney.dti.gateway.rules.race;

import pvt.disney.dti.gateway.rules.race.utility.AlgorithmUtility;

/**
 * The Class ResCodeApi.
 *
 * @author MOONS012
 */
public class ResCodeApiImpl implements ResCodeApi {

	/** The instance. */
	private static ResCodeApiImpl instance = new ResCodeApiImpl();
	
	/**
	 * Instantiates a new res code api.
	 */
	//private constructor to prevent instantiation
	private ResCodeApiImpl() {};
	
	/**
	 * Gets the single instance of ResCodeApiImpl.
	 *
	 * @return single instance of ResCodeApiImpl
	 */
	public static ResCodeApiImpl getInstance() {
		return instance;
	}
	
	/**
	 * Creates the res code for payload id.
	 *
	 * @param prefix the prefix
	 * @return the string
	 */
	public String generateResCode(String prefix) {
		String resCode = AlgorithmUtility.generateResCode(prefix);
		
		return resCode;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
    protected Object clone() throws CloneNotSupportedException {
 
        /*
         * Here forcibly throws the exception for preventing to be cloned
         */
        throw new CloneNotSupportedException("No cloning around");
    }
}
