package pvt.disney.dti.gateway.util.flood;

import java.util.Properties;

/**
 * This class is used to enable the key match flood control (which is an abstract class)
 * to be tested.
 * @author lewit019
 *
 */
public class TestImplementor extends KeyMatchFloodControl {

  public TestImplementor(Properties props) throws FloodControlInitException {
    super(props);
  }
 
  /**
   * A simple deriveKey method that returns the transaction as the key.
   */
  @Override
  public Object deriveKey(Object txn) throws KeyDerivationException {
    
    String key = (String)txn;
    if (key.length() > 0) {
      return key;
    } else 
      throw new KeyDerivationException("Zero length key.");

  }

}
