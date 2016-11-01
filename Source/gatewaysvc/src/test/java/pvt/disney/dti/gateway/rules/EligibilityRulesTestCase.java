package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;

/**
 * Tests the eligibility rules.
 * @author lewit019
 *
 */
public class EligibilityRulesTestCase {

  /**
   * Test eligibility required.
   */
  @Test
  public final void testEligibilityRequired() {

    ArrayList<DBProductTO> dbProdList = null;
    boolean isRequired = true;

    // Test 1: Null dbProdList
    isRequired = EligibilityRules.eligibilityRequired(dbProdList);
    if (isRequired)
      fail("False expected for Test 1:  Null dbProdList.");

    // Test 2: Empty dbProdlist
    dbProdList = new ArrayList<DBProductTO>();
    isRequired = true;
    isRequired = EligibilityRules.eligibilityRequired(dbProdList);
    if (isRequired)
      fail("False expected for Test 2:  Empty dbProdlist.");

    // Test 3: dbProdList with no eligibility
    DBProductTO aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setEligGrpid(null);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("BBB02");
    aDBProduct.setEligGrpid(null);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("CCC03");
    aDBProduct.setEligGrpid(null);
    dbProdList.add(aDBProduct);

    isRequired = EligibilityRules.eligibilityRequired(dbProdList);
    if (isRequired)
      fail("False expected for Test 3:  dbProdList with no eligibility");

    // Test 4:  one item requiring eligibility
    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("CCC03");
    aDBProduct.setEligGrpid(new BigInteger("42"));
    dbProdList.add(aDBProduct);

    isRequired = EligibilityRules.eligibilityRequired(dbProdList);
    if (!isRequired)
      fail("False expected for Test 4:  one item requiring eligibility");

    return;

  }

  /**
   * Test validate eligibility.
   */
  @Test
  public final void testValidateEligibility() {

    String eligGrpCode = new String("GroupCode");
    String eligMemberID = new String("EligMemberID");
    Boolean eligFlag = new Boolean(true);
    
    // Test 1:  Eligibility match found
    try {
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode, eligMemberID);
    } catch (DTIException dtie) {
      fail("Unexpected exception in Test 1:  Eligibility match found: " + dtie.toString());
    }

    // Test 2:  No eligibility match found
    eligFlag = new Boolean(false);
    try {
      EligibilityRules.validateEligibility(eligFlag, eligGrpCode, eligMemberID);
      fail("Expected exception in Test 2:  No eligibility match found.");
    } catch (DTIException dtie) {
       if (dtie.getDtiErrorCode() != DTIErrorCode.INACTIVE_ELIGIBILITY_NBR)
         fail("Expected INACTIVE_ELIGIBILITY_NBR error in Test 2:  No eligibility match found.");
    }

    return;

  }

}
