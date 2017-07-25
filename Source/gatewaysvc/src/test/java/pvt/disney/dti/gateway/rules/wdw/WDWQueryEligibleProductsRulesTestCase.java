package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;

public class WDWQueryEligibleProductsRulesTestCase {

   
   /**
    * Test Case for ValidateInEligibleProducts
    */
   @Test
   public void testValidateInEligibleProducts() {
      OTUsagesTO otUsagesTO = new OTUsagesTO();
      ArrayList<OTUsagesTO> usagesList = new ArrayList<>();
      OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
      otTicketInfoTO.setTicketType(new BigInteger("0"));
      otTicketInfoTO.setVoidCode(new Integer(1));
      otTicketInfoTO.setValidityEndDate(new GregorianCalendar());
      otTicketInfoTO.setUsagesList(usagesList);
      otTicketInfoTO.setBiometricTemplate("biometricTemplate");

      OTQueryTicketTO infoT = new OTQueryTicketTO();
      infoT.getTicketInfoList().add(otTicketInfoTO);
      DBProductTO dbProductTO = new DBProductTO();
      dbProductTO.setUpgrdPathId(new BigInteger("0"));
      dbProductTO.setResidentInd(true);
      dbProductTO.setDayCount("1");
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otUsagesTO.setDate("17-07-22");
      otTicketInfoTO.setVoidCode(new Integer(101));
      usagesList.add(otUsagesTO);
      otTicketInfoTO.setUsagesList(usagesList);
      infoT.getTicketInfoList().add(otTicketInfoTO);
      
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otTicketInfoTO.setBiometricTemplate(null);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      try {
         otTicketInfoTO.setValidityEndDate("17-07-20");
      } catch (ParseException e1) {
      }
      dbProductTO.setUpgrdPathId(new BigInteger("1"));
      otTicketInfoTO.setBiometricTemplate("biometricTemplate");
     // otTicketInfoTO.setValidityEndDate(new GregorianCalendar());
      dbProductTO.setResidentInd(false);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otUsagesTO.setDate("17-07-06");
      dbProductTO.setUpgrdPathId(new BigInteger("1"));
      otTicketInfoTO.setBiometricTemplate("biometricTemplate");
      dbProductTO.setResidentInd(true);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otUsagesTO.setDate("14-07-06");
      dbProductTO.setUpgrdPathId(new BigInteger("2"));
      otTicketInfoTO.setBiometricTemplate("biometricTemplate");
      dbProductTO.setDayCount("2");
      dbProductTO.setResidentInd(true);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otTicketInfoTO.setVoidCode(1);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
      otTicketInfoTO.setVoidCode(100);
      try {
         WDWQueryEligibleProductsRules.validateInEligibleProducts(infoT, dbProductTO);
      } catch (DTIException e) {
      }
   }
}
