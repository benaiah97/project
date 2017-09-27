package pvt.disney.dti.gateway.util.flood;

/**
 * This class represents an exception that occurred during the initialization phases of flood control set-up.
 * 
 * @author lewit019
 */
public class FloodControlInitException extends Exception {

	/** The standard serial version UID. */
	private static final long serialVersionUID = -2394848186682199309L;

	/**
	 * Constructs a new flood control init exception with the specified detail message.
	 * 
	 * @param message
	 */
	public FloodControlInitException(String message) {
		super(message);
	}

}
