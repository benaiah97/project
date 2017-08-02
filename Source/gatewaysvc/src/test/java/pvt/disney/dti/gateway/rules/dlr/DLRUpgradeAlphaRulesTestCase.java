package pvt.disney.dti.gateway.rules.dlr;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for DLRUpgradeAlphaRules
 */
public class DLRUpgradeAlphaRulesTestCase extends CommonTestUtils {
	/**
	 * JUnit for transformRequest
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO alphaRequestTO = new UpgradeAlphaRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {
			ticket.addTicketDemographic(getBillingInfo());
			ticket.setTktValidityValidStart(new GregorianCalendar());
			ticket.setTktValidityValidEnd(new GregorianCalendar());
			TktStatusTO newStatus = ticket.new TktStatusTO();
			newStatus.setStatusValue("status");
			ticket.addTicketStatus(newStatus);
			ticket.setTktMarket("6");
			ticket.setExternal("external");
			ticket.setTktSecurityLevel("2");
		}
		alphaRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(alphaRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		PaymentLookupTO lookupTO = new PaymentLookupTO();
		lookupTO.setPymtCode("2");
		lookupTO.setLookupValue("3");
		ArrayList<PaymentLookupTO> paymentLookupTOList = new ArrayList<>();
		paymentLookupTOList.add(lookupTO);
		dtiTxn.setPaymentLookupTOList(paymentLookupTOList);
		EntityTO entityTO = new EntityTO();
		entityTO.setCustomerId("2");
		dtiTxn.setEntityTO(entityTO);
		/*
		 * scenario :1 Passing dtiTxn , expecting xmlRequest should not be null
		 */
		String xmlRequest = null;
		try {
			xmlRequest = DLRUpgradeAlphaRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformResponse
	 * 
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testTransformResponse() throws URISyntaxException, FileNotFoundException {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		populateTxn(dtiTxn);
		dtiTxn.setTpRefNum(2222);
		DTIMockUtil.processMockprepareAndExecuteSql();
		InputStream fileName = null;
		/*
		 * scenario :1 Passing dtiTxn and xmlResponse , expecting
		 * dtiTransactionTO should not be null
		 */
		String xmlResponse = null;
		try {
			fileName = this.getClass()
					.getResourceAsStream(DLR_XML_PATH + "DLR_UpgradeAlphaResponse_01_TestTransformResponse.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			DTITransactionTO dtiTransactionTO = DLRUpgradeAlphaRules.transformResponse(dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/*
		 * scenario :2 Passing dtiTxn ,xmlResponse and status code non-numeric,
		 * expecting exception Provider responded with a non-numeric status
		 * code.
		 */
		try {
			fileName = this.getClass()
					.getResourceAsStream(DLR_XML_PATH + "DLR_UpgradeAlphaResponse_02_TestTransformResponse.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			DLRUpgradeAlphaRules.transformResponse(dtiTxn, xmlResponse);
			Assert.fail("Unexpected error:Provider responded with a non-numeric status code.");
		} catch (DTIException dtie) {
			Assert.assertEquals("Provider responded with a non-numeric status code.", dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyDLRUpgradeAlphaRules
	 */
	@Test
	public void testApplyDLRUpgradeAlphaRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.UPGRADEALPHA);
		UpgradeAlphaRequestTO alphaRequestTO = new UpgradeAlphaRequestTO();
		alphaRequestTO.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		DTIRequestTO request = new DTIRequestTO();
		request.setCommandBody(alphaRequestTO);
		/* scenario :1 Passing dtiTxn */
		dtiTxn.setRequest(request);
		try {
			DLRUpgradeAlphaRules.applyDLRUpgradeAlphaRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformError
	 * 
	 * @throws URISyntaxException
	 * @throws FileNotFoundException
	 */
	// @Test TODO
	public void testTransformError() throws URISyntaxException, FileNotFoundException {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.UPGRADEALPHA);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		String statusString = "";
		DTIMockUtil.processMockprepareAndExecuteSql();
		createCommonRequest(dtiTxn);
		populateTxn(dtiTxn);
		String xmlResponse = "<Envelope> " + "<Header>" + "<MessageID>290118</MessageID>"
				+ "<MessageType>QueryTicketResponse</MessageType>" + "<SourceID>WDPRODLRNA</SourceID>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp() + "</TimeStamp>" + "<EchoData />" + "<SystemFields />" + "</Header>"
				+ "<Body><Status><StatusCode>1300</StatusCode><StatusText>QueryTicket request error</StatusText></Status>"
				+ "<QueryTicketErrors><Errors><Error><ErrorCode>1306</ErrorCode><ErrorText>Can not process messages from source WDPRODLRNA, source is inactive</ErrorText>"
				+ "</Error></Errors><DataRequestErrors /></QueryTicketErrors></Body></Envelope>";
		DTIMockUtil.mockDtiErrorCodeScope("TICKET");
		/*
		 * scenario :1 Passing dtiTxn and xmlResponse , expecting
		 * dtiTransactionTO should not be null
		 */
		try {
			DTITransactionTO dtiTransactionTO = DLRUpgradeAlphaRules.transformError(dtiTxn, dtiRespTO, statusString,
					xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * @param dtiTxn
	 * @return
	 */
	private void populateTxn(DTITransactionTO dtiTxn) {
		UpgradeAlphaRequestTO alphaRequestTO = new UpgradeAlphaRequestTO();
		alphaRequestTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(alphaRequestTO);
	}
}
