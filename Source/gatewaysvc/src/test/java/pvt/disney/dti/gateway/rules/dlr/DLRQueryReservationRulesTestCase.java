package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author RASTA006 Test case for DLRQueryReservationRules
 * 
 */
public class DLRQueryReservationRulesTestCase extends CommonTestUtils {

	/**
	 * test Case for transformRequest
	 */
	@Test
	public void testTransformRequest() {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYRESERVATION);
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO queryReq = new QueryReservationRequestTO();
		queryReq.setResCode("1");
		dtiTxn.getRequest().setCommandBody(queryReq);
		dtiTxn.setTpRefNum(new Integer(1));
		try {
			DLRQueryReservationRules.transformRequest(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}

	/**
	 * test Case for transformResponse
	 */
	@Test
	public void testTransformResponse() throws URISyntaxException,
			FileNotFoundException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYRESERVATION);
		String xmlResponse = null;
		InputStream fileName = this.getClass().getResourceAsStream(DLR_XML_PATH+"DLR_QueryTicket_01_Rsp_Error.xml");
		xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		QueryReservationRequestTO queryReq = new QueryReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		dtiTxn.setTpRefNum(new Integer(1));
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			dtiTxn = DLRQueryReservationRules.transformResponse(dtiTxn,
					xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE,
					dtie.getDtiErrorCode());
		}
		fileName = this.getClass().getResourceAsStream(DLR_XML_PATH+"DLR_QueryReservation_01_Rsp.xml");
		xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			dtiTxn = DLRQueryReservationRules.transformResponse(dtiTxn,
					xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}
	}

	/**
	 * test Case for transformError
	 */
	// @Test TODO
	public void testTransformError() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYRESERVATION);
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
		String statusString = "statusString";
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil.mockDtiErrorCodeScope("COMMAND");
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO queryReq = new QueryReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		try {
			dtiTxn = DLRQueryReservationRules.transformError(dtiTxn, dtiRespTO,
					statusString, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("unexpected exception occured" + dtie.getLogMessage());
		}
	}
}
