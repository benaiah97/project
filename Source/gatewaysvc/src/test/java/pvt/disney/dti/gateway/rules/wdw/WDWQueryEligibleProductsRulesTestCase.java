package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

public class WDWQueryEligibleProductsRulesTestCase {

	/**
	 * Test transform request.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYELIGPRODUCTS);
		DTIRequestTO request = new DTIRequestTO();
		PayloadHeaderTO header = new PayloadHeaderTO();
		request.setPayloadHeader(header);
		CommandBodyTO body = new QueryEligibleProductsRequestTO();
		CommonTestUtils commonTestUtils = new CommonTestUtils();
		request.setCommandBody(body);
		dtiTxn.setRequest(request);
		dtiTxn.setTpRefNum(2);
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();
		AttributeTO attributeTO = new AttributeTO();
		AttributeTO attributeTO1 = new AttributeTO();
		AttributeTO attributeTO2 = new AttributeTO();
		AttributeTO attributeTO3 = new AttributeTO();
		attributeTO.setAttrValue("2454");
		attributeTO1.setAttrValue("45245");
		attributeTO2.setAttrValue("45255");
		attributeTO3.setAttrValue("4555");
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, attributeTO);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.USER, attributeTO1);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.PASS, attributeTO2);
		attributeTOMap.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER,
				attributeTO3);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		QueryEligibleProductsRequestTO queryEligibleProductsRequestTO = new QueryEligibleProductsRequestTO();
		TicketTO ticketTO = new TicketTO();
		queryEligibleProductsRequestTO.add(ticketTO);
		dtiTxn.getRequest().setCommandBody(queryEligibleProductsRequestTO);
		commonTestUtils.setMockProperty();
		/* Scenario 1::when dtiTicketType=BARCODE_ID */
		ticketTO.setBarCode("2124");
		try {
			WDWQueryEligibleProductsRules.transformRequest(dtiTxn);
		} catch (DTIException e) {
			Assert.fail(e.getMessage());
		}
		/* Scenario 2::when dtiTicketType=TKTNID_ID */
		ticketTO.setBarCode(null);
		ticketTO.setTktNID("545");
		try {
			WDWQueryEligibleProductsRules.transformRequest(dtiTxn);
		} catch (DTIException e) {
			Assert.fail(e.getMessage());
		}
		/* Scenario 3::when dtiTicketType=MAG_ID */
		ticketTO.setTktNID(null);
		ticketTO.setMag("magTrack1In");
		try {
			WDWQueryEligibleProductsRules.transformRequest(dtiTxn);
		} catch (DTIException e) {
			Assert.fail(e.getMessage());
		}
		/* Scenario 4::when dtiTicketType=EXTERNAL_ID */
		/*
		 * ticketTO.setMag(null); ticketTO.setExternal("256"); try {
		 * WDWQueryEligibleProductsRules.transformRequest(dtiTxn); } catch
		 * (DTIException e) { Assert.fail(e.getMessage()); } Scenario 5::when
		 * dtiTicketType=DSSN_ID ticketTO.setMag(null); try {
		 * ticketTO.setDssn("2017-12-21", "site", "station", "4"); } catch
		 * (ParseException e1) { Assert.fail(e1.getMessage()); } try {
		 * WDWQueryEligibleProductsRules.transformRequest(dtiTxn); } catch
		 * (DTIException e) { Assert.fail(e.getMessage()); }
		 */

	}


	

	
	/**
	 * Test transform response body.
	 * Result type =INELIGIBLE
	 */

	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYELIGPRODUCTS);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.ELIGIBLEPRODUCTS);
		CommonTestUtils common=new CommonTestUtils();
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		DTIRequestTO request = new DTIRequestTO();
		TicketTO ticketTO = new TicketTO();
		EntityTO entityTO = new EntityTO();
		OTQueryTicketTO queryTicketTO = new OTQueryTicketTO();
		OTTicketInfoTO oTTicketInfoTO = new OTTicketInfoTO();
		CommandBodyTO body = new QueryEligibleProductsRequestTO();
		OTDemographicData demographicData = new OTDemographicData();
		QueryEligibilityProductsResponseTO queryEligibilityProductsResponseTO = new QueryEligibilityProductsResponseTO();
		ArrayList<OTUsagesTO> usagesList=new ArrayList<OTUsagesTO>();
		OTUsagesTO oTUsagesTO=new OTUsagesTO();
		OTTicketTO ticket = new OTTicketTO();
		OTFieldTO oTFieldTO=new OTFieldTO(1, 	1, "Str/ing");
		OTFieldTO addroTFieldTO=new OTFieldTO(4, 	1, "Address");
		OTFieldTO addoTFieldTO=new OTFieldTO(5, 	1, "Address");
		GregorianCalendar date=new GregorianCalendar();
		GregorianCalendar validityEndDate=new GregorianCalendar();
		GregorianCalendar validityStartDate=new GregorianCalendar();
		try {
			ticket.setTDssn("12-10-2017", "site", "station", "number");
		} catch (ParseException e1) {
			Assert.fail("Parsing Exception occured");
		}
		ticketTO.setAccountId("1");
		ticketTO.setBarCode("5668764");
		validityStartDate.set(2017, 01, 31);
		oTUsagesTO.setSiteNumber(1);
		validityEndDate.set(2017, 12,03);
		date.set(2017, 07, 22);
		oTUsagesTO.setDate(date);
		usagesList.add(oTUsagesTO);
		demographicData.getDemoDataList().add(oTFieldTO);
		demographicData.getDemoDataList().add(addroTFieldTO);
		demographicData.getDemoDataList().add(addoTFieldTO);
		dtiTxn.setRequest(request);
		dtiTxn.getRequest().setCommandBody(body);
		oTTicketInfoTO.setValidityStartDate(validityStartDate);
		oTTicketInfoTO.setTicketType(new BigInteger("1"));
		oTTicketInfoTO.setBiometricTemplate("biometricTemplate");
		oTTicketInfoTO.setPayPlan("YES");
		oTTicketInfoTO.setUsagesList(usagesList);
		oTTicketInfoTO.setSeasonPassDemo(demographicData);
		oTTicketInfoTO.setTicket(ticket);
		queryTicketTO.getTicketInfoList().add(oTTicketInfoTO);
		otCmdTO.setQueryTicketTO(queryTicketTO);
		queryEligibilityProductsResponseTO.add(ticketTO);
		dtiTxn.setEntityTO(entityTO);
		DTIMockUtil.processMockprepareAndExecuteSql();
		common.setMockProperty();
	try {
			WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn,
					otCmdTO, dtiRespTO);
		} catch (DTIException e) {
			Assert.fail(e.getMessage());
		}
	}


	/**
	 * Test transform response body.
	 * when TicketType=null
	 */
	//@Test
	public void testTransformResponseBodyTicketTypeNull() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYELIGPRODUCTS);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.ELIGIBLEPRODUCTS);
		OTQueryTicketTO queryTicketTO=new OTQueryTicketTO();
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		DTIRequestTO request = new DTIRequestTO();
		CommandBodyTO body = new QueryEligibleProductsRequestTO();
		OTTicketInfoTO oTTicketInfoTO=new OTTicketInfoTO();
		queryTicketTO.getTicketInfoList().add(oTTicketInfoTO);
		otCmdTO.setQueryTicketTO(queryTicketTO);
		dtiTxn.setRequest(request);
		dtiTxn.getRequest().setCommandBody(body);
		
		try {
			WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn,
					otCmdTO, dtiRespTO);
		} catch (DTIException e) {
			assertEquals("Ticket Provided is incorrect",e.getLogMessage());
		}
	}

	/**
	 * Test transform response body.
	 * when ProductsTktNbr=null

	 */
	//@Test
	public void testTransformResponseBodydbProductsTktNbNull() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYELIGPRODUCTS);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.ELIGIBLEPRODUCTS);
		OTQueryTicketTO queryTicketTO=new OTQueryTicketTO();
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		DTIRequestTO request = new DTIRequestTO();
		CommandBodyTO body = new QueryEligibleProductsRequestTO();
		OTTicketInfoTO oTTicketInfoTO=new OTTicketInfoTO();
		queryTicketTO.getTicketInfoList().add(oTTicketInfoTO);
		oTTicketInfoTO.setTicketType(new BigInteger("1"));
		otCmdTO.setQueryTicketTO(queryTicketTO);
		dtiTxn.setRequest(request);
		dtiTxn.getRequest().setCommandBody(body);
		
		try {
			WDWQueryEligibleProductsRules.transformResponseBody(dtiTxn,
					otCmdTO, dtiRespTO);
		} catch (DTIException e) {
			assertEquals("getProductsTktNbr tktNbr is null.",e.getLogMessage());
		}
	}
	
	
	

	/**
	 * Test Case for ValidateInEligibleProducts
	 */
	//@Test
	public void testValidateInEligibleProducts() {

		OTUsagesTO otUsagesTO = new OTUsagesTO();
		ArrayList<OTUsagesTO> usagesList = new ArrayList<>();
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		otTicketInfoTO.setTicketType(new BigInteger("0"));
		otTicketInfoTO.setValidityEndDate(new GregorianCalendar());
		otTicketInfoTO.setUsagesList(usagesList);
		otTicketInfoTO.setBiometricTemplate("biometricTemplate");
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setResidentInd(true);
		dbProductTO.setDayCount(1);
		otTicketInfoTO.setVoidCode(new Integer(1));
		boolean inElegibleFlag = false;

		/*
		 * Scenario 1 : VoidCode in the ATS Query Ticket response greater than 0
		 * or less than or equal to 100
		 */
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertTrue(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}
		/*
		 * Scenario 2 :ResidentInd is Y and DayCount = 1 and the first use date
		 * is older than 14 days
		 */
		inElegibleFlag = false;
		try {
			otUsagesTO.setDate("17-07-22");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		otTicketInfoTO.setVoidCode(new Integer(101));
		usagesList.add(otUsagesTO);
		otTicketInfoTO.setUsagesList(usagesList);
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertTrue(inElegibleFlag);
			Assert.assertTrue(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}

		/* Scenario 3 : UpgrdPathId is 0 */
		inElegibleFlag = false;
		dbProductTO.setUpgrdPathId(new BigInteger("0"));
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertTrue(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}

		/*
		 * Scenario 4: Usages must have one entry and BiometricTemplate must
		 * have one entry
		 */
		inElegibleFlag = false;
		otTicketInfoTO.setBiometricTemplate(null);
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertTrue(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}

		/* Scenario 5 :ResidentInd is N and Validity EndDate less than today */
		try {
			otTicketInfoTO.setValidityEndDate("17-07-20");
		} catch (ParseException e1) {
		}

		inElegibleFlag = false;
		dbProductTO.setUpgrdPathId(new BigInteger("1"));
		otTicketInfoTO.setBiometricTemplate("biometricTemplate");
		dbProductTO.setResidentInd(false);
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertTrue(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}

		/*
		 * Scenario 6: resident flag is 'Y' and DayCount > 1, and the first use
		 * date is older than six months (185 days).
		 */
		inElegibleFlag = false;
		try {
			otUsagesTO.setDate("12-07-06");
			otUsagesTO.setDate("17-07-06");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbProductTO.setUpgrdPathId(new BigInteger("2"));
		otTicketInfoTO.setBiometricTemplate("biometricTemplate");
		dbProductTO.setDayCount(2);
		dbProductTO.setResidentInd(true);
		try {
			inElegibleFlag = WDWQueryEligibleProductsRules
					.validateGuestProductHasValue(otTicketInfoTO, dbProductTO);
			Assert.assertFalse(inElegibleFlag);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getMessage());
		}
	}

	/**
	 * Test set guest product details. when UpgradePLUList !null and
	 * guestProductTO !null
	 */
	/*
	 * //@Test public void testSetGuestProductDetails() { TicketTO dtiTktTO =
	 * new TicketTO(); ArrayList<BigInteger> tktNbr = new ArrayList<>();
	 * ArrayList<OTTicketInfoTO> otTicketList = new ArrayList<>();
	 * OTTicketInfoTO oTTicketInfoTO = new OTTicketInfoTO();
	 * oTTicketInfoTO.setItem(new BigInteger("1"));
	 * otTicketList.add(oTTicketInfoTO); // Scenario :: 1 Expecting Exception
	 * try { //GuestProductTO guestProductTO =
	 * WDWQueryEligibleProductsRules.setGuestProductDetails(otTicketList,
	 * dtiTktTO, tktNbr); Assert.fail("Expecting Exception");
	 * Assert.assertNull(guestProductTO);
	 * Assert.assertNotNull(dtiTktTO.getResultType()); } catch (DTIException e)
	 * { Assert.assertTrue(true); Assert.assertNull(dtiTktTO.getResultType()); }
	 * // Scenario ::2 DTIMockUtil.processMockprepareAndExecuteSql(); try {
	 * //GuestProductTO guestProductTO =
	 * WDWQueryEligibleProductsRules.setGuestProductDetails(otTicketList,
	 * dtiTktTO, tktNbr); Assert.assertNotNull(dtiTktTO.getResultType());
	 * Assert.assertNotNull(guestProductTO);
	 * Assert.assertNotNull(guestProductTO.getDbproductTO()); } catch
	 * (DTIException e) { Assert.fail("UnExpected Exception Occured" +
	 * e.getMessage()); } }
	 */
}
