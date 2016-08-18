package pvt.disney.dti.gateway.rules;

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
@SuiteClasses(value = { ContentRulesTestCase.class, DateTimeRulesTestCase.class,
    EligibilityRulesTestCase.class, PaymentRulesTestCase.class,
    ProductRulesTestCase.class, TargetRulesTestCase.class,
    TransformRulesTestCase.class })
public class BusinessRulesTestSuite {
}
