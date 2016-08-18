package pvt.disney.dti.gateway.util.flood;

/**
 * The purpose of a key derivation exception is to notify the caller when (for whatever reason) a key cannot be derived from the transactional data provided.
 * 
 * @author lewit019
 * 
 */
public class KeyDerivationException extends Exception {

	/** Standard serial version UID */
	private static final long serialVersionUID = 9129227490L;

	public KeyDerivationException(String message) {
		super(message);
	}

}
