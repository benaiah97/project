package pvt.disney.dti.gateway.provider.wdw.xml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Sets up the XML test suite for the WDW transactions.
 * 
 * @author lewit019
 * 
 */
@RunWith(value = Suite.class)
@SuiteClasses(value = { OTQueryTicketXMLTestCase.class, OTManageReservationXMLTestCase.class,
    OTVoidTicketXMLTestCase.class, OTUpgradeTicketXMLTestCase.class,
    OTUpdateTicketXMLTestCase.class, OTUpdateTransactionXMLTestCase.class,
    OTCreateTransactionXMLTestCase.class })
public class BackEndWDWXMLTransformTestSuite {
}
