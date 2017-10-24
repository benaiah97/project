package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.service.dtixml.DTITestUtil;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * The Class DLRQueryEligibilityProductRulesTestCase.
 * 
 * @author AGARS017
 */

public class DLRQueryEligibilityProductRulesTestCase {

	@Test
	public void transformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		DTIRequestTO request = new DTIRequestTO();
		PayloadHeaderTO payloadHeader = new PayloadHeaderTO();
		CommandHeaderTO commandHeader = new CommandHeaderTO();
		QueryEligibleProductsRequestTO bodyTO = new QueryEligibleProductsRequestTO();
		TicketTO ticketTo = new TicketTO();
		TktSellerTO tktSeller = new TktSellerTO();
		tktSeller.setTsMac("tsMac");
		ticketTo.setExternal("BGYD28686");
		payloadHeader.setPayloadID("68688989686786767");
		payloadHeader.setTarget("target");
		payloadHeader.setCommProtocol("protocol");
		payloadHeader.setCommMethod("method");
		payloadHeader.setVersion("version");
		payloadHeader.setTktSeller(tktSeller);
		commandHeader.setCmdActor("Actor");
		commandHeader.setCmdDevice("cmdDevice");
		commandHeader.setCmdInvoice("cmdInvoice");
		commandHeader.setCmdItem(new BigInteger("4"));
		commandHeader.setCmdMarket("cmdMarket");
		bodyTO.getTktList().add(ticketTo);
		request.setPayloadHeader(payloadHeader);
		request.setCommandHeader(commandHeader);
		request.setCommandBody(bodyTO);
		dtiTxn.setRequest(request);
		dtiTxn.setTpRefNum(5634);
		try {
			String requestform = DLRQueryEligibilityProductsRules.transformRequest(dtiTxn);
			Assert.assertNotNull(requestform);
		} catch (DTIException e) {
			Assert.fail("UnExpected Exception Occured" + e.getMessage());
		}
	}

	/**
	 * Test transform response.
	 */
	@Test
	public void testTransformResponse() {
	   /*
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		String itemKind1 = "<ItemKind>1</ItemKind>", itemKind2 = "<ItemKind>2</ItemKind>";
		String xmlResponse1 = "<?xml version=\"1.0\"?>" + " <Envelope>" + " <Header>" + "<SourceID>1</SourceID>"
				+ "<MessageID>1</MessageID>" + "<MessageType>QueryTicketResponse</MessageType>"
				+ "<TimeStamp>2017-05-11 08:00:00</TimeStamp>" + " </Header> " + "<Body> " + "<Status> "
				+ "<StatusCode>0</StatusCode> " + "<StatusText>OK</StatusText> " + "</Status> "
				+ "<QueryTicketResponse>" + "<DataRequestResponse> " + itemKind1 + "<Status>0</Status>"
				+ "<Exchangeable>NO</Exchangeable>" + "<Returnable>YES</Returnable>" + "<PLU>TICKET0010101</PLU>"
				+ "<Price>8.00</Price>" + "<RemainingPrice>0.00</RemainingPrice>" + "<Tax>0</Tax>"
				+ "<RemainingTax>0</RemainingTax>" + "<TaxMethods>NNNNNNNN</TaxMethods>" + "<AccessCode>10</AccessCode>"
				+ "<AccessCodeName>ADULT</AccessCodeName>" + "<TicketDate>2004-02-01 00:00:00</TicketDate>"
				+ "<ValidUntil>2017-08-01 00:00:00</ValidUntil>" + "<LockedOut>No</LockedOut>"
				+ "<UseCount>0</UseCount>" + "<RemainingUse>1</RemainingUse>" + "<UpdateStatus>0</UpdateStatus>"
				+ "<NodeNo>82</NodeNo>" + "<TransNo>1234</TransNo>" + "<DateSold>2004-02-01 00:00:00</DateSold>"
				+ "<DateOpened>2004-02-01 00:00:00</DateOpened>" + "<OrderID>1234</OrderID>"
				+ "<CustomerID>19</CustomerID>" + "<UpgradePLUList>" + "<Item>" + "<PLU>1</PLU>"
				+ "<Price>80.00</Price>" + "<UpgradePrice>67.20</UpgradePrice>" + " </Item>" + "</UpgradePLUList>"
				+ "<Contact>" + "<FirstName>Wes</FirstName>" + "<MiddleName/>" + "<LastName>Moser</LastName>"
				+ "<IdentificationNo/>" + "<Street1>445 County Line Rd</Street1>" + "<Street2/>" + "<Street3/>"
				+ "<City>Gilbertsville</City>" + "<State>PA</State>" + "<ZIP>19525</ZIP>"
				+ "<CountryCode>US</CountryCode>" + "<Phone>6104730000</Phone>" + "<Fax/>" + "<Cell/>"
				+ "<Email>wmoser@gatewayticketing.com</Email>" + "<ExternalID/>"
				+ "<ContactGUID>{87333B90-FCB6-4202-962E-D0FE44776308}</ContactGUID>"
				+ "<GalaxyContactID>4442</GalaxyContactID>" + "<JobTitle/>" + "<Primary>NO</Primary>" + "<ContactNote/>"
				+ "<NameTitleID>0</NameTitleID>" + "<NameSuffixID>0</NameSuffixID>"
				+ "<TotalPaymentContracts>0</TotalPaymentContracts>" + "<AllowEmail>NO</AllowEmail>"
				+ "<AllowMailings>NO</AllowMailings>" + "<DOB>1899-12-30 00:00:00</DOB>" + "<AgeGroup>0</AgeGroup>"
				+ "<Gender>0</Gender>" + "</Contact>" + "<FirstName>Wes</FirstName>" + "<MiddleName/>"
				+ "<LastName>Moser</LastName>" + "<IdentificationNo/>" + "<Street1>445 County Line Rd</Street1>"
				+ "<Street2/>" + "<Street3/>" + "<City>Gilbertsville</City>" + "<State>PA</State>" + "<ZIP>19525</ZIP>"
				+ "<CountryCode>US</CountryCode>" + "<Phone>6104730000</Phone>" + "<Fax/>" + "<Cell/>"
				+ "<Email>wmoser@gatewayticketing.com</Email>" + "<ExternalID/>"
				+ "<ContactGUID>{87333B90-FCB6-4202-962E-D0FE44776308}</ContactGUID>"
				+ "<GalaxyContactID>4442</GalaxyContactID>" + "<JobTitle/>" + "<Primary>NO</Primary>" + "<ContactNote/>"
				+ "<NameTitleID>0</NameTitleID>" + "<NameSuffixID>0</NameSuffixID>"
				+ "<TotalPaymentContracts>0</TotalPaymentContracts>" + "<AllowEmail>NO</AllowEmail>"
				+ "<AllowMailings>NO</AllowMailings>" + "<DOB>1899-12-30 00:00:00</DOB>" + "<AgeGroup>0</AgeGroup>"
				+ "<Gender>0</Gender>" + "</DataRequestResponse>" + "</QueryTicketResponse>" + "</Body>"
				+ "</Envelope>";
		
		String xmlResponse2 = "<?xml version=\"1.0\"?>" + " <Envelope>" + " <Header>" + "<SourceID>1</SourceID>"
				+ "<MessageID>1</MessageID>" + "<MessageType>QueryTicketResponse</MessageType>"
				+ "<TimeStamp>2017-05-11 08:00:00</TimeStamp>" + " </Header> " + "<Body> " + "<Status> "
				+ "<StatusCode>0</StatusCode> " + "<StatusText>OK</StatusText> " + "</Status> "
				+ "<QueryTicketResponse>" + "<DataRequestResponse> " + itemKind2 + "<Status>0</Status>"
				+ "<Exchangeable>NO</Exchangeable>" + "<Returnable>YES</Returnable>" + "<PLU>TICKET0010101</PLU>"
				+ "<Price>8.00</Price>" + "<RemainingPrice>0.00</RemainingPrice>" + "<Tax>0</Tax>"
				+ "<RemainingTax>0</RemainingTax>" + "<TaxMethods>NNNNNNNN</TaxMethods>" + "<AccessCode>10</AccessCode>"
				+ "<AccessCodeName>ADULT</AccessCodeName>" + "<TicketDate>2004-02-01 00:00:00</TicketDate>"
				+ "<ValidUntil>2017-08-01 00:00:00</ValidUntil>" + "<LockedOut>No</LockedOut>"
				+ "<UseCount>0</UseCount>" + "<RemainingUse>1</RemainingUse>" + "<UpdateStatus>0</UpdateStatus>"
				+ "<NodeNo>82</NodeNo>" + "<TransNo>1234</TransNo>" + "<DateSold>2004-02-01 00:00:00</DateSold>"
				+ "<DateOpened>2004-02-01 00:00:00</DateOpened>" + "<OrderID>1234</OrderID>"
				+ "<CustomerID>19</CustomerID>" + "<UpgradePLUList>" + "<Item>" + "<PLU>1</PLU>"
				+ "<Price>80.00</Price>" + "<UpgradePrice>67.20</UpgradePrice>" + " </Item>" + "</UpgradePLUList>"
				+ "<Contact>" + "<FirstName>Wes</FirstName>" + "<MiddleName/>" + "<LastName>Moser</LastName>"
				+ "<IdentificationNo/>" + "<Street1>445 County Line Rd</Street1>" + "<Street2/>" + "<Street3/>"
				+ "<City>Gilbertsville</City>" + "<State>PA</State>" + "<ZIP>19525</ZIP>"
				+ "<CountryCode>US</CountryCode>" + "<Phone>6104730000</Phone>" + "<Fax/>" + "<Cell/>"
				+ "<Email>wmoser@gatewayticketing.com</Email>" + "<ExternalID/>"
				+ "<ContactGUID>{87333B90-FCB6-4202-962E-D0FE44776308}</ContactGUID>"
				+ "<GalaxyContactID>4442</GalaxyContactID>" + "<JobTitle/>" + "<Primary>NO</Primary>" + "<ContactNote/>"
				+ "<NameTitleID>0</NameTitleID>" + "<NameSuffixID>0</NameSuffixID>"
				+ "<TotalPaymentContracts>0</TotalPaymentContracts>" + "<AllowEmail>NO</AllowEmail>"
				+ "<AllowMailings>NO</AllowMailings>" + "<DOB>1899-12-30 00:00:00</DOB>" + "<AgeGroup>0</AgeGroup>"
				+ "<Gender>0</Gender>" + "</Contact>" + "<FirstName>Wes</FirstName>" + "<MiddleName/>"
				+ "<LastName>Moser</LastName>" + "<IdentificationNo/>" + "<Street1>445 County Line Rd</Street1>"
				+ "<Street2/>" + "<Street3/>" + "<City>Gilbertsville</City>" + "<State>PA</State>" + "<ZIP>19525</ZIP>"
				+ "<CountryCode>US</CountryCode>" + "<Phone>6104730000</Phone>" + "<Fax/>" + "<Cell/>"
				+ "<Email>wmoser@gatewayticketing.com</Email>" + "<ExternalID/>"
				+ "<ContactGUID>{87333B90-FCB6-4202-962E-D0FE44776308}</ContactGUID>"
				+ "<GalaxyContactID>4442</GalaxyContactID>" + "<JobTitle/>" + "<Primary>NO</Primary>" + "<ContactNote/>"
				+ "<NameTitleID>0</NameTitleID>" + "<NameSuffixID>0</NameSuffixID>"
				+ "<TotalPaymentContracts>0</TotalPaymentContracts>" + "<AllowEmail>NO</AllowEmail>"
				+ "<AllowMailings>NO</AllowMailings>" + "<DOB>1899-12-30 00:00:00</DOB>" + "<AgeGroup>0</AgeGroup>"
				+ "<Gender>0</Gender>" + "</DataRequestResponse>" + "</QueryTicketResponse>" + "</Body>"
				+ "</Envelope>";
		DTIRequestTO request = new DTIRequestTO();
		PayloadHeaderTO payloadHeader = new PayloadHeaderTO();
		CommandHeaderTO commandHeader = new CommandHeaderTO();
		CommandBodyTO bodyTO = new QueryEligibleProductsRequestTO();
		payloadHeader.setPayloadID("68688989686786767");
		payloadHeader.setTarget("target");
		payloadHeader.setCommProtocol("protocol");
		payloadHeader.setCommMethod("method");
		payloadHeader.setVersion("version");
		commandHeader.setCmdActor("Actor");
		commandHeader.setCmdDevice("cmdDevice");
		commandHeader.setCmdInvoice("cmdInvoice");
		commandHeader.setCmdItem(new BigInteger("4"));
		commandHeader.setCmdMarket("cmdMarket");
		request.setPayloadHeader(payloadHeader);
		request.setCommandHeader(commandHeader);
		request.setCommandBody(bodyTO);
		dtiTxn.setRequest(request);
		dtiTxn.setEntityTO(new EntityTO());
		dtiTxn.setTpRefNum(45244552);
		dtiTxn.setTktBroker(DTITestUtil.TKTBROKER);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try { */
			/* For ItemKind 1 */  /*
			DTITransactionTO transactionTo1 = DLRQueryEligibilityProductsRules.transformResponse(dtiTxn, xmlResponse1);
			Assert.assertNotNull(transactionTo1);
			QueryEligibilityProductsResponseTO qtResp = (QueryEligibilityProductsResponseTO) transactionTo1
					.getResponse().getCommandBody();
			Assert.assertNotEquals(qtResp.getTicketList(), 0);
			Assert.assertEquals(qtResp.getTicketList().get(0).getResultType(), "NOPRODUCTS");

			/* For ItemKind 2 */ /*
			DTITransactionTO transactionTo2 = DLRQueryEligibilityProductsRules.transformResponse(dtiTxn, xmlResponse2);
			Assert.assertNotNull(transactionTo2);
		} catch (DTIException e) {
			// TODO
		} */
	}

	/**
	 * Test get upgraded product.
	 */
	// @Test
	public void testGetUpgradedProduct() {
		ArrayList<String> listofUpgradedPLUs = new ArrayList<String>();

		TicketTO dtiTktTO = new TicketTO();
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		EntityTO entity = new EntityTO();
		entity.setEntityId(1);
		dtiTxn.setEntityTO(entity);
		/* Scenario:: 1 when listfUpgradedPLUs is empty */
		/*try {

			ArrayList<DBProductTO> productDBList = DLRQueryEligibilityProductsRules
					.getUpgradedProduct(listofUpgradedPLUs, dtiTktTO, dtiTxn);
			Assert.assertNull(productDBList);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured" + e.getMessage());
		}*/
		/*
		 * Scenario::2 when when listfUpgradedPLUs is not empty :: and plu is
		 * not present in DBproductList
		 */
		/*try {
			listofUpgradedPLUs.add("3GZ00601");
			DTIMockUtil.processMockprepareAndExecuteSql();
			ArrayList<DBProductTO> productDBList = DLRQueryEligibilityProductsRules
					.getUpgradedProduct(listofUpgradedPLUs, dtiTktTO, dtiTxn);
			// Expected Null result
			if (productDBList != null && productDBList.size() == 0) {
				Assert.assertTrue(true);
			}
			Assert.assertNotNull(dtiTktTO.getResultType());
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured" + e.getMessage());
		}*/
	}

	/**
	 * Test set guest product details. when UpgradePLUList !null and
	 * guestProductTO !null
	 */
	// @Test
	/*
	 * public void testSetGuestProductDetails() { GWDataRequestRespTO
	 * gwDataRespTO = new GWDataRequestRespTO();
	 * gwDataRespTO.setFirstName("firstName");
	 * gwDataRespTO.setLastName("lastName");
	 * gwDataRespTO.setEmail("email@domain.com"); TicketTO dtiTktTO = new
	 * TicketTO(); String plu = "3GZ00601"; UpgradePLUList upgradePLU =
	 * gwDataRespTO.new UpgradePLUList(); ArrayList<UpgradePLUList>
	 * upgradePLUList = new ArrayList<>(); upgradePLUList.add(upgradePLU);
	 * gwDataRespTO.setUpgradePLUList(upgradePLUList); // Scenario :: 1
	 * Expecting Exception try { //GuestProductTO guestProductTO =
	 * DLRQueryEligibilityProductsRules.setGuestProductDetails(gwDataRespTO,
	 * dtiTktTO, plu); Assert.fail("Expecting Exception");
	 * Assert.assertNull(guestProductTO);
	 * Assert.assertNotNull(dtiTktTO.getResultType());
	 * 
	 * } catch (DTIException e) { Assert.assertTrue(true);
	 * Assert.assertNull(dtiTktTO.getResultType()); } // Scenario ::2 mocking
	 * and passing the PLU DTIMockUtil.processMockprepareAndExecuteSql(); try {
	 * //GuestProductTO guestProductTO =
	 * DLRQueryEligibilityProductsRules.setGuestProductDetails(gwDataRespTO,
	 * dtiTktTO, plu); Assert.assertNotNull(guestProductTO);
	 * Assert.assertNotNull(guestProductTO.getDbproductTO());
	 * Assert.assertNotNull(guestProductTO.getGwDataRespTO());
	 * Assert.assertNotNull(guestProductTO.getGwDataRespTO().getFirstName());
	 * Assert.assertNotNull(dtiTktTO.getResultType()); } catch (DTIException e)
	 * { Assert.fail("UnExpected Exception Occured" + e.getMessage()); } }
	 */
	
	@Test
   public void testGetPLUDetailsFromGalaxy() {
      
      String xmlresponse="<?xml version=\"1.0\"?>" + " <Envelope>" + " <Header>" + "<SourceID>1</SourceID>"
            + "<MessageID>1</MessageID>" + "<MessageType>QueryTicketResponse</MessageType>"
            + "<TimeStamp>2017-05-11 08:00:00</TimeStamp>" + " </Header> " + "<Body> " + "<Status> "
            + "<StatusCode>0</StatusCode> " + "<StatusText>OK</StatusText> " + "</Status> "
            + "<QueryTicketResponse>" + "<DataRequestResponse> " + "itemKind2" + "<Status>0</Status>"
            + "<Exchangeable>NO</Exchangeable>" + "<Returnable>YES</Returnable>" + "<PLU>TICKET0010101</PLU>"
            + "<Price>8.00</Price>" + "<RemainingPrice>0.00</RemainingPrice>" + "<Tax>0</Tax>"
            + "<RemainingTax>0</RemainingTax>" + "<TaxMethods>NNNNNNNN</TaxMethods>" + "<AccessCode>10</AccessCode>"
            + "<AccessCodeName>ADULT</AccessCodeName>" + "<TicketDate>2004-02-01 00:00:00</TicketDate>"
            + "<ValidUntil>2017-08-01 00:00:00</ValidUntil>" + "<LockedOut>No</LockedOut>"
            + "<UseCount>0</UseCount>" + "<RemainingUse>1</RemainingUse>" + "<UpdateStatus>0</UpdateStatus>"
            + "<NodeNo>82</NodeNo>" + "<TransNo>1234</TransNo>" + "<DateSold>2004-02-01 00:00:00</DateSold>"
            + "<DateOpened>2004-02-01 00:00:00</DateOpened>" + "<OrderID>1234</OrderID>"
            + "<CustomerID>19</CustomerID>" + "<UpgradePLUList>" + "<Item>" + "<PLU>1</PLU>"
            + "<Price>80.00</Price>" + "<UpgradePrice>67.20</UpgradePrice>" + "<PaymentPlans><PaymentPlan><PaymentPlanID>3</PaymentPlanID>"
            + "<Description>Down payment of $97 and 12 monthly payments as low as $20.17 per month for a Southern California Select Annual Passport; prices for other passports may vary." 
            + "Contract Purchaser/Annual Passholder must reside in Southern California zip code area.</Description><Name>Monthly Payment Option - Southern California</Name></PaymentPlan></PaymentPlans>"
            + "</Item>" + "</UpgradePLUList>"
            + "<UseCount>1</UseCount><VisualID>468480050500000563</VisualID><UsageRequestResponse><UsageRecords><UsageRecord><UseNo>1</UseNo><UseTime>2017-09-19 10:53:22</UseTime></UsageRecord></UsageRecords></UsageRequestResponse>"
            +  "</DataRequestResponse>" + "</QueryTicketResponse>" + "</Body>"
            + "</Envelope>";
      DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
      DLRQueryEligibilityProductsRules rules = new DLRQueryEligibilityProductsRules();
      DTIRequestTO request = new DTIRequestTO();
      PayloadHeaderTO payloadHeader = new PayloadHeaderTO();
      CommandHeaderTO commandHeader = new CommandHeaderTO();
      CommandBodyTO bodyTO = new QueryEligibleProductsRequestTO();
      payloadHeader.setPayloadID("68688989686786767");
      payloadHeader.setTarget("target");
      payloadHeader.setCommProtocol("protocol");
      payloadHeader.setCommMethod("method");
      payloadHeader.setVersion("version");
      commandHeader.setCmdActor("Actor");
      commandHeader.setCmdDevice("cmdDevice");
      commandHeader.setCmdInvoice("cmdInvoice");
      commandHeader.setCmdItem(new BigInteger("4"));
      commandHeader.setCmdMarket("cmdMarket");
      request.setPayloadHeader(payloadHeader);
      request.setCommandHeader(commandHeader);
      request.setCommandBody(bodyTO);
      dtiTxn.setRequest(request);
      dtiTxn.setEntityTO(new EntityTO());
      dtiTxn.setTpRefNum(45244552);
      dtiTxn.setTktBroker(DTITestUtil.TKTBROKER);
      DTIMockUtil.mockGetProductsByTktName();
      DTIMockUtil.mockGetAPUpgradeCatalog();
      try {
         DTITransactionTO dtiTxnTest = null;
         dtiTxnTest = rules.transformResponse(dtiTxn, xmlresponse);
         if(dtiTxnTest==null){
            Assert.fail("Resonse can not be null"); 
         }
         
         QueryEligibilityProductsResponseTO res=(QueryEligibilityProductsResponseTO)dtiTxnTest.getResponse().getCommandBody();
         if(res.getTicketList().get(0).getDlrPLU()==null && res.getTicketList().get(0).getDlrPLU().compareTo("TICKET0010101")!=0){
            Assert.fail("PLU does not match with TICKET0010101"); 
         }
        
      }catch(DTIException dtie){
         Assert.fail("UnExpected Exception Occured" + dtie.getMessage());
      }
      
      

   }
}