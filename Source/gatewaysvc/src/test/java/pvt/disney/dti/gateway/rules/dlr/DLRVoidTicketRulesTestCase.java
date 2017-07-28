package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.assertNotNull;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for DLRVoidTicketRules
 */
public class DLRVoidTicketRulesTestCase extends CommonTestUtils {

	/**
	 * JUnit for transformRequest
	 */
	@Test
	public void testTransformRequest() {
		String xmlString = null;
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO voidReq = new VoidTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(voidReq);
		voidReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.setTpRefNum(new Integer(1));
		/* scenario : 1 TransactionType is VOIDTICKET */
		try {
			xmlString = DLRVoidTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}

	/**
	 * JUnit for transformResponse
	 */
	@Test
	public void testTransformResponse() throws DTIException,
			FileNotFoundException, URISyntaxException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.VOIDTICKET);
		String xmlResponse = null;
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO queryReq = new VoidTicketRequestTO();
		TicketTO ticketTO = new TicketTO();
		ArrayList<TicketTO> arrTicketTO = new ArrayList<TicketTO>();
		arrTicketTO.add(ticketTO);
		queryReq.setTktList(arrTicketTO);
		dtiTxn.getRequest().setCommandBody(queryReq);
		dtiTxn.setTpRefNum(222);
		InputStream	fileName = null;
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* scenario : 1 TransactionType is VOIDTICKET with status code=0 */
		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_VoidTicketResponse.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			DLRVoidTicketRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception" + dtie.getMessage());
		}
		/* scenario : 2 TransactionType is VOIDTICKET with status code>0 */
		
		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_VoidTicketResponse_1.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			DLRVoidTicketRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception" + dtie.getMessage());
		}
	}

	/**
	 * JUnit for transformError
	 */
	@Test
	public void testTransformError() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		String xmlResponse = "<Envelope> "
				+ "<Header>"
				+ "<MessageID>290118</MessageID>"
				+ "<MessageType>QueryTicketResponse</MessageType>"
				+ "<SourceID>WDPRODLRNA</SourceID>"
				+ "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "<EchoData />"
				+ "<SystemFields />"
				+ "</Header>"
				+ "<Body><Status><StatusCode>1300</StatusCode><StatusText>QueryTicket request error</StatusText></Status>"
				+ "<QueryTicketErrors><Errors><Error><ErrorCode>1306</ErrorCode><ErrorText>Can not process messages from source WDPRODLRNA, source is inactive</ErrorText>"
				+ "</Error></Errors><DataRequestErrors /></QueryTicketErrors></Body></Envelope>";
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		String statusCode = "100";
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil.mockDtiErrorCodeScope("TICKET");
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO queryReq = new VoidTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		try {
			dtiTxn = DLRVoidTicketRules.transformError(dtiTxn, dtiRespTO,
					statusCode, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}
	/**
	 * JUnit for applyDLRVoidTicketRules
	 */
	@Test
	public void testApplyDLRVoidTicketRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO voidReq = new VoidTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(voidReq);
		voidReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		try {
			DLRVoidTicketRules.applyDLRVoidTicketRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}
}
