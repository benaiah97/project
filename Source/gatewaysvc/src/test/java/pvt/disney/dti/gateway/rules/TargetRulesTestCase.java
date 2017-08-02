package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * Tests the logic used to determine if the target is valid or not.
 * 
 * @author lewit019
 * 
 */
public class TargetRulesTestCase {

  /** Constant value for Production (default WDW) */
  private static String PROD = "PROD";

  /** Constant value for Production WDW */
  private static String PRODWDW = "PROD-WDW";

  /** Constant value for Production DLR */
  private static String PRODDLR = "PROD-DLR";

  /** Constant value for Test (default WDW) */
  private static String TEST = "TEST";

  /** Constant value for Test WDW */
  private static String TESTWDW = "TEST-WDW";

  /** Constant value for Test DLR */
  private static String TESTDLR = "TEST-DLR";
  
  /** Constant value for Invalid Target */
  private static String TESTHKDL = "TEST-HKDL";

  /**
   * Tests the validate provider target rule.
   * 
   */
  
  @Mocked
	ResourceBundle resourceBundle;
	Properties props=null;
	
	@Before
	public void setUp(){
		
		
		
		props = new Properties();

	    InputStream inStream = null;

	    try {
	    	
	      inStream = this.getClass().getResourceAsStream("/dtiApp.properties");
	      props.load(inStream);
	    } catch (FileNotFoundException fnfe) {
		      fail("Unable to load properties for test." + fnfe.toString());
		    } catch (IOException ioe) {
		      fail("Unable to load properties for test." + ioe.toString());
		    }
	    
		 new MockUp<ResourceLoader>() {
           @Mock
           public Properties convertResourceBundleToProperties(
                        ResourceBundle Key) {
                 return props;

           }
    };

    new MockUp<ResourceLoader>() {
           @Mock
           public ResourceBundle getResourceBundle(String prop) {

                 return resourceBundle;
           }
    };
    new MockUp<ContentRules>() {
        @Mock
        public String getProperty(String key) {

              return "Test";
        }
 };
	}
  //@Test
  public final void testValidateProviderTarget() {

    DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    DTIRequestTO dtiRqst = new DTIRequestTO();
    PayloadHeaderTO payHeader = new PayloadHeaderTO();
    dtiRqst.setPayloadHeader(payHeader);
    dtiTxn.setRequest(dtiRqst);
    
    // Test 1:  all valid targets, upper and lower case
    try {
      
     /* payHeader.setTarget(PROD);
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(PROD.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(PRODWDW);
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(PRODWDW.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(PRODDLR);
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(PRODDLR.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);*/
      
      payHeader.setTarget(TEST);
      ContentRules.validateProviderTarget(dtiTxn);
      
    /*  payHeader.setTarget(TEST.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(TESTWDW);
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(TESTWDW.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(TESTDLR);
      ContentRules.validateProviderTarget(dtiTxn);
      
      payHeader.setTarget(TESTDLR.toLowerCase());
      ContentRules.validateProviderTarget(dtiTxn);*/
      
    } catch (DTIException dtie) {
      fail("Unexpected exception thrown in Test 1: " + dtie.toString());
    }
    
    // Test 2:  Invalid target
    try {
      
      payHeader.setTarget(TESTHKDL);
      ContentRules.validateProviderTarget(dtiTxn);
 
      fail("Expected exception to be thrown in Test 1: Invalid Target.");
      
    } catch (DTIException dtie) {
      
    }

    return;

  }

}
