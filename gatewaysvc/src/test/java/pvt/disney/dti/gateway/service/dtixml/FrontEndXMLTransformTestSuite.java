package pvt.disney.dti.gateway.service.dtixml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Sets up the XML test suite.
 * @author lewit019
 *
 */
@RunWith(value=Suite.class)
@SuiteClasses(value={HeadersTestCase.class, QueryTicketTestCase.class, ReservationTestCase.class, VoidTicketTestCase.class,
    UpgradeAlphaTestCase.class, CreateTicketTestCase.class, UpdateTicketTestCase.class, 
    UpdateTransactionTestCase.class})
public class FrontEndXMLTransformTestSuite {
}
