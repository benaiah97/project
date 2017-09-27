package pvt.disney.dti.gateway.util.flood;

import java.util.Properties;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;

/**
 * This class is used to enable the key match flood control (which is an abstract class)
 * to be tested.
 * @author lewit019
 *
 */
public class TestImplementor extends KeyMatchFloodControl {

  public TestImplementor() throws FloodControlInitException {
    super();
  }
 
  /**
   * A simple deriveKey method that returns the transaction as the key.
   */
  @Override
  public Object deriveKey(Object txn) throws KeyDerivationException {
    
	  FloodMatchSignatureTO key = (FloodMatchSignatureTO)txn;
    //if (key.length() > 0) {
      return key;
  //  } else 
    //  throw new KeyDerivationException("Zero length key.");

  }

}
