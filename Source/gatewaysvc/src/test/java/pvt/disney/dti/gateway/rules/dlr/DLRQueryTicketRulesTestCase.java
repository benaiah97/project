package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.DTIFormatter;

/**
 * Test Case for DLRQueryTicketRules
 * @author RASTA006
 *
 */
public class DLRQueryTicketRulesTestCase extends CommonTestUtils{
	/**
	 * Test Case for transformRequest
	 */
	@Test
	public void testTransformRequest(){
		String xmlString=null;
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.setTpRefNum(new Integer(1));
		try{
			xmlString=DLRQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		
	}
	@Test
	public void testTransformResponse(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
		String xmlResponse=null;
		InputStream fileName = this.getClass().getResourceAsStream(DLR_XML_PATH+"DLR_QueryTicket_01_Rsp_Error.xml");
		xmlResponse=DTITestUtilities.getXMLFromFile(fileName);
		
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.setTpRefNum(new Integer(1));
		DTIMockUtil.processMockprepareAndExecuteSql();
		try{
			dtiTxn=DLRQueryTicketRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE,dtie.getDtiErrorCode());
		}
		
		fileName = this.getClass().getResourceAsStream(DLR_XML_PATH+"DLR_QueryTicket_01_Rsp.xml");
		xmlResponse=DTITestUtilities.getXMLFromFile(fileName);
		queryReq.setIncludeRenewalAttributes(true);
		queryReq.setIncludePassAttributes(true);
		try{
			dtiTxn=DLRQueryTicketRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		fileName = this.getClass().getResourceAsStream(DLR_XML_PATH+"DLR_QueryTicket_03_Rsp.xml");
		xmlResponse=DTITestUtilities.getXMLFromFile(fileName);
		try{
			dtiTxn=DLRQueryTicketRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}
	/**
	 * test Case for transformError
	 */
	@Test
	public void testTransformError(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
		String xmlResponse="<Envelope> "+
	"<Header>"+
		"<MessageID>290118</MessageID>"+
		"<MessageType>QueryTicketResponse</MessageType>"+
		"<SourceID>WDPRODLRNA</SourceID>"+
		"<TimeStamp>"+DLRTestUtil.getDLRTimeStamp()+"</TimeStamp>"+
		"<EchoData />"+
		"<SystemFields />"+
	"</Header>"+
	"<Body><Status><StatusCode>1300</StatusCode><StatusText>QueryTicket request error</StatusText></Status>"+
		"<QueryTicketErrors><Errors><Error><ErrorCode>1306</ErrorCode><ErrorText>Can not process messages from source WDPRODLRNA, source is inactive</ErrorText>"+
				"</Error></Errors><DataRequestErrors /></QueryTicketErrors></Body></Envelope>";
		DTIResponseTO dtiRespTO=new DTIResponseTO(); String statusCode=null;
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil.mockDtiErrorCodeScope("TICKET");
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		try{
			dtiTxn=DLRQueryTicketRules.transformError(dtiTxn, dtiRespTO, statusCode, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		
	}
	/**
	 * Test Case for applyDLRQueryTicketRules
	 */
	@Test
	public void testApplyDLRQueryTicketRules(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		try{
			DLRQueryTicketRules.applyDLRQueryTicketRules(dtiTxn);
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
		
	}
	

}
