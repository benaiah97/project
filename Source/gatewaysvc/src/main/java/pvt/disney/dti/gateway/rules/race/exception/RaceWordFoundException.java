/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author moons012
 *
 */
public class RaceWordFoundException {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(RaceWordFoundException.class);
	
	/**
	 * 
	 */
	public RaceWordFoundException() {
		  super();
	}


    /**
     * Constructor.
     *
     * @param errorCode String
     * @param cause Throwable
     */
    public RaceWordFoundException(Throwable cause) {
    	logger.error("RaceWordFoundException: " + cause.getMessage());
    }

    /**
     * Constructor.
     *
     * @param errorCode String
     * @param message String
     */
    public RaceWordFoundException(String message) {
        logger.error("RaceWordFoundException: "  + message);
    }

   
}
