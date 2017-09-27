package pvt.disney.dti.gateway.util.flood;

/**
 * The Flood Control KeyStore Factory class allows for the dynamic
 * (configuration based) assignment of a storage method to be used by style of
 * flood control.
 * 
 * @author lewit019
 * 
 */
public class FloodControlKeyStoreFactory {

   /**
    * Returns an instance of a keystore.
    * 
    * @param keyStoreTypeIn
    *           The type of keystore requested (in memory, database, etc.).
    * @return an instance of a keystore.
    * @throws FloodControlInitException
    *            for any mechanical error experienced while performing this
    *            operation.
    */
   public static FloodControlKeyStore getInstance(FloodControlKeyStore.Type keyStoreTypeIn)
            throws FloodControlInitException {

      return FloodControlKeyStoreFactory.getInstance(null, keyStoreTypeIn);

   }

   /**
    * Gets the single instance of FloodControlKeyStoreFactory.
    * 
    * @param maxConKeys
    *           the max ConcurrentKeys keys
    * @param keyStoreTypeIn
    *           the key store type in
    * @return single instance of FloodControlKeyStoreFactory
    * @throws FloodControlInitException
    *            the flood control init exception
    */
   public static FloodControlKeyStore getInstance(Integer maxConKeys, FloodControlKeyStore.Type keyStoreTypeIn)
            throws FloodControlInitException {

      FloodControlKeyStore aFloodControlKeyStore = null;

      if (keyStoreTypeIn == FloodControlKeyStore.Type.MEMORY) {

         if (maxConKeys == null) {
            aFloodControlKeyStore = MemoryBasedKeyStore.getInstance();
         } else {
            aFloodControlKeyStore = MemoryBasedKeyStore.getInstance(maxConKeys);
         }

      } else
         throw new FloodControlInitException("Only memory-based key stores are supported at present.");

      return aFloodControlKeyStore;
   }

}
