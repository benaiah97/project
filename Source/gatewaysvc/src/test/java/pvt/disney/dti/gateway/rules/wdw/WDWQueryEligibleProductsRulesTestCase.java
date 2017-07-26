package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.GuestProductTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.UpgradePLUList;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.rules.dlr.DLRQueryEligibilityProductsRules;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

public class WDWQueryEligibleProductsRulesTestCase {
	
	/**
	 * Test transform request.
	 */
	@Test
	public void testTransformRequest(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		DTIRequestTO request=new DTIRequestTO();
		PayloadHeaderTO header=new PayloadHeaderTO();
		request.setPayloadHeader(header);
		CommandBodyTO body=new QueryEligibleProductsRequestTO();
		request.setCommandBody(body);
		dtiTxn.setRequest(request);
		dtiTxn.setTpRefNum(2);
		 HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap=new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> ();
		 AttributeTO attributeTO=new AttributeTO();
		 AttributeTO attributeTO1=new AttributeTO();
		 AttributeTO attributeTO2=new AttributeTO();
		 AttributeTO attributeTO3=new AttributeTO();
		 attributeTO.setAttrValue("2454");
		 attributeTO1.setAttrValue("45245");
		 attributeTO2.setAttrValue("45255");
		 attributeTO3.setAttrValue("4555");
		 attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, attributeTO);
		 attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO1);
		 attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO2);
		 attributeTOMap.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER, attributeTO3);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		QueryEligibleProductsRequestTO queryEligibleProductsRequestTO=new QueryEligibleProductsRequestTO();
		TicketTO ticketTO=new TicketTO();
		
		queryEligibleProductsRequestTO.add(ticketTO);
		dtiTxn.getRequest().setCommandBody(queryEligibleProductsRequestTO);
		try {
			ticketTO.setDssn("2017-12-21", "site", "station", "4");
		} catch (ParseException e1) {
				System.out.println(e1.getMessage());
		}
		try {
			WDWQueryEligibleProductsRules.transformRequest(dtiTxn);
		} catch (DTIException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	/**
	 * Test transform response body.
	 */
	//@Test
	public void testTransformResponseBody()
	{
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		OTCommandTO otCmdTO=new OTCommandTO(OTCommandTO.OTTransactionType.ELIGIBLEPRODUCTS);
		DTIResponseTO dtiRespTO=new DTIResponseTO();
		DTIRequestTO request=new DTIRequestTO();
		dtiTxn.setRequest(request);
		CommandBodyTO body=new QueryEligibleProductsRequestTO(); 
		dtiTxn.getRequest().setCommandBody(body);
		OTQueryTicketTO queryTicketTO=new OTQueryTicketTO();
		OTTicketInfoTO oTTicketInfoTO=new OTTicketInfoTO();
		oTTicketInfoTO.setTicketType(new BigInteger("1"));
		queryTicketTO.getTicketInfoList().add(oTTicketInfoTO);
		otCmdTO.setQueryTicketTO(queryTicketTO);
		QueryEligibilityProductsResponseTO queryEligibilityProductsResponseTO=new QueryEligibilityProductsResponseTO();
		TicketTO ticketTO=new TicketTO();
		queryEligibilityProductsResponseTO.add(ticketTO);
		try {
			WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
		} catch (DTIException e) {
		System.out.println(e.getMessage());
		}
	}
	
	
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
   /**
	 * Test set guest product details.
	 * when UpgradePLUList !null and guestProductTO !null
	 */
	@Test 
	public void testSetGuestProductDetails(){
		TicketTO dtiTktTO=new TicketTO();
		ArrayList<BigInteger> tktNbr=new ArrayList<>();
		ArrayList<OTTicketInfoTO> otTicketList=new ArrayList<>();
		OTTicketInfoTO oTTicketInfoTO=new OTTicketInfoTO();
		oTTicketInfoTO.setItem(new BigInteger("1"));
		otTicketList.add(oTTicketInfoTO);
		//Scenario :: 1 Expecting Exception 
		try {
			GuestProductTO guestProductTO=WDWQueryEligibleProductsRules.setGuestProductDetails(otTicketList, dtiTktTO, tktNbr);
			Assert.fail("Expecting Exception");
			Assert.assertNull(guestProductTO);
			Assert.assertNotNull(dtiTktTO.getResultType());
		} catch (DTIException e) {
			Assert.assertTrue(true);
			Assert.assertNull(dtiTktTO.getResultType());
		}
		//Scenario ::2 
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			GuestProductTO guestProductTO=WDWQueryEligibleProductsRules.setGuestProductDetails(otTicketList, dtiTktTO, tktNbr);
			Assert.assertNotNull(dtiTktTO.getResultType());
			Assert.assertNotNull(guestProductTO);
			Assert.assertNotNull(guestProductTO.getDbproductTO());
		} catch (DTIException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
	}
}
