package pvt.disney.dti.gateway.util.flood;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Defines the test suite for Flood Control.
 * @author lewit019
 *
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={MemoryBasedKeyStoreTest.class,KeyMatchFloodControlTest.class,
    KeyMatchFloodControlActiveTest.class})
public class FloodControlTestSuite {
}
