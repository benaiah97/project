package pvt.disney.dti.gateway.util.flood;

/**
 * A KeyBlockException, when returned from flood control, means that a key has occurred with sufficient frequency that it is now (as of this transaction) blocked by flood control. The use of this exception is optional, as in some cases of
 * flood control, only supression (quiet disposing of flooding transaction) and not blocking (doing something to return an error to the one sending the flood) is desired.
 * 
 * @author lewit019
 * 
 */
public class KeyBlockException extends Exception {

	/** Standard serial version UID */
	private static final long serialVersionUID = 9129227490L;
}
