package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.rules.dlr.DLRQueryEligibilityProductsRules;
import pvt.disney.dti.gateway.service.dtixml.DTITestUtil;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * The Class DLRQueryEligibilityProductRulesTestCase.
 * @author AGARS017
 */

public class DLRQueryEligibilityProductRulesTestCase {

	/**
	 * Test transform response.
	 */
	@Test
	public void testTransformResponse(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYELIGIBLEPRODUCTS);
		String xmlResponse= "<?xml version=\"1.0\"?>"
				+ " <Envelope>"
				+ " <Header>"
				+ "<SourceID>1</SourceID>"
				+ "<MessageID>1</MessageID>"
				+ "<MessageType>QueryTicketResponse</MessageType>"
				+ "<TimeStamp>2017-05-11 08:00:00</TimeStamp>"
				+ " </Header> "
				+ "<Body> "
				+ "<Status> "
				+ "<StatusCode>0</StatusCode> "
				+ "<StatusText>OK</StatusText> "
				+ "</Status> "
				+ "<QueryTicketResponse>"
				+ "<DataRequestResponse> "
				+ "<ItemKind>1</ItemKind>"
				+ "<Status>0</Status>"
				+ "<Exchangeable>NO</Exchangeable>"
				+ "<Returnable>YES</Returnable>"
				+ "<PLU>TICKET0010101</PLU>"
				+ "<Price>8.00</Price>"
				+ "<RemainingPrice>0.00</RemainingPrice>"
				+ "<Tax>0</Tax>"
				+ "<RemainingTax>0</RemainingTax>"
				+ "<TaxMethods>NNNNNNNN</TaxMethods>"
				+ "<AccessCode>10</AccessCode>"
				+ "<AccessCodeName>ADULT</AccessCodeName>"
				+ "<TicketDate/>"
				+ "<LockedOut>No</LockedOut>"
				+ "<UseCount>0</UseCount>"
				+ "<RemainingUse>1</RemainingUse>"
				+ "<UpdateStatus>0</UpdateStatus>"
				+ "<NodeNo>82</NodeNo>"
				+ "<TransNo>1234</TransNo>"
				+ "<DateSold>2004-02-01 00:00:00</DateSold>"
				+ "<OrderID>1234</OrderID>"
				+ "<CustomerID>19</CustomerID>"
				+ "<UpgradePLUList>"
				+ "<Item>"                     
				+ "<PLU>3GZ00601</PLU>"
				+ "<Price>80.00</Price>"
				+ "<UpgradePrice>67.20</UpgradePrice>"
				+ " </Item>"
				+ "</UpgradePLUList>"
				+ "</DataRequestResponse>"
				+ "</QueryTicketResponse>"
				+ "</Body>"
				+ "</Envelope>";
		DTIRequestTO request=new DTIRequestTO();
		PayloadHeaderTO payloadHeader=new PayloadHeaderTO();
		CommandHeaderTO commandHeader=new CommandHeaderTO();
		CommandBodyTO bodyTO=new QueryEligibleProductsRequestTO();
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
		dtiTxn.setTpRefNum(45244552);
		dtiTxn.setTktBroker(DTITestUtil.TKTBROKER);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			DLRQueryEligibilityProductsRules.transformResponse(dtiTxn, xmlResponse);
		} catch (DTIException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
	}
}
