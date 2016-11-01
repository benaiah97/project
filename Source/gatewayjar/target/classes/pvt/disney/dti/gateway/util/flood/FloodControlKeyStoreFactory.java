package pvt.disney.dti.gateway.util.flood;

import java.util.Properties;

/**
 * The Flood Control KeyStore Factory class allows for the dynamic (configuration based) assignment of a storage method to be used by style of flood control.
 * 
 * @author lewit019
 * 
 */
public class FloodControlKeyStoreFactory {

	/**
	 * Returns an instance of a keystore.
	 * 
	 * @param keyStoreTypeIn
	 *            The type of keystore requested (in memory, database, etc.).
	 * @return an instance of a keystore.
	 * @throws FloodControlInitException
	 *             for any mechanical error experienced while performing this operation.
	 */
	public static FloodControlKeyStore getInstance(
			FloodControlKeyStore.Type keyStoreTypeIn) throws FloodControlInitException {

		return FloodControlKeyStoreFactory.getInstance(null, keyStoreTypeIn);

	}

	/**
	 * Returns an instance of a keystore, while enabling the passing of properties to initialize it.
	 * 
	 * @param props
	 *            Properties used to initialize the keystore.
	 * @param keyStoreTypeIn
	 *            The type of keystore requested (in memory, database, etc.).
	 * @return an instance of a keystore.
	 * @throws FloodControlInitException
	 *             for any mechanical error experienced while performing this operation.
	 */
	public static FloodControlKeyStore getInstance(Properties props,
			FloodControlKeyStore.Type keyStoreTypeIn) throws FloodControlInitException {

		FloodControlKeyStore aFloodControlKeyStore = null;

		if (keyStoreTypeIn == FloodControlKeyStore.Type.MEMORY) {

			if (props == null) {
				aFloodControlKeyStore = MemoryBasedKeyStore.getInstance();
			}
			else {
				aFloodControlKeyStore = MemoryBasedKeyStore.getInstance(props);
			}

		}
		else throw new FloodControlInitException(
				"Only memory-based key stores are supported at present.");

		return aFloodControlKeyStore;
	}

}
