package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertNotNull;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 JUnit WDWCreateTicketRules
 */
public class WDWCreateTicketRulesTestCase extends CommonTestUtils {
	@Before
	public void setUp() throws Exception {
		setMockProperty();
	}

	/**
	 * JUnit for transformRequest
	 */  
	@Test
	public void testTransformRequest() {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		createCommonRequest(dtiTxn);
		CreateTicketRequestTO dtiCreateTktReq = new CreateTicketRequestTO();
		dtiCreateTktReq.setTicketList(getTicketList(TicketIdType.MAG_ID));
		dtiCreateTktReq.setEligibilityGroup("1");
		dtiCreateTktReq.setEligibilityMember("1");
		dtiTxn.getRequest().setCommandBody(dtiCreateTktReq);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiCreateTktReq.setDefaultAccount("AccountPerTicket");
		String xmlString = null;
		/* mocking prepareAndExecuteSql */
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* Scenario:: 1 passing the dtTxn Object */
		try {
			xmlString = WDWCreateTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		/* Scenario:: 2 DefaultAccount is AccountPerOrder */
		dtiCreateTktReq.setDefaultAccount("AccountPerOrder");
		dtiCreateTktReq.setAuditNotation("1");

		try {
			xmlString = WDWCreateTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		/* Scenario:: 3 EligibilityMember is not a number */
		dtiCreateTktReq.setEligibilityMember("ONE");
		try {
			xmlString = WDWCreateTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		/* Scenario:: 4 EligibilityGroup is DVC */
		dtiCreateTktReq.setEligibilityGroup("DVC");
		try {
			xmlString = WDWCreateTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * JUnit for transformResponseBody
	 * 
	 * @throws ParseException
	 */ 
	@Test
	public void testtransformResponseBody() throws ParseException {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.CREATETRANSACTION);
		getOTCommad(otCmdTO);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		createCommonRequest(dtiTxn);
		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
		/* Scenario :: 1 TktInfoList is null */
		otCmdTO.getCreateTransactionTO().setTktInfoList(null);

		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
		/* Scenario :: 2 TktInfoList size is zero */
		ArrayList<OTTicketInfoTO> tktInfoList = new ArrayList<>();
		otCmdTO.getCreateTransactionTO().setTktInfoList(tktInfoList);
		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyWDWCreateTicketRules
	 */
	@Test
	public void testApplyWDWCreateTicketRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		CreateTicketRequestTO createTicketRequestTO = new CreateTicketRequestTO();
		DTIRequestTO request = new DTIRequestTO();
		request.setCommandBody(createTicketRequestTO);
		dtiTxn.setRequest(request);
		/* Scenario :: 1 Exception not expected */
		try {
			WDWCreateTicketRules.applyWDWCreateTicketRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}

	/**
	 * @param otCmdTO
	 * @throws ParseException
	 */
	private void getOTCommad(OTCommandTO otCmdTO) throws ParseException {
		OTCreateTransactionTO createTransactionTO = new OTCreateTransactionTO();
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		OTTransactionDSSNTO transactionDSSN = new OTTransactionDSSNTO();
		transactionDSSN.setDate("10-10-10");
		transactionDSSN.setTransactionId(23);
		OTTicketTO ticket = new OTTicketTO();
		otTicketInfoTO.setTicket(ticket);
		otTicketInfoTO.setItem(new BigInteger("2"));
		otTicketInfoTO.setTransactionDSSN(transactionDSSN);
		otTicketInfoTO.setValidityStartDate(new GregorianCalendar());
		otTicketInfoTO.setValidityEndDate(new GregorianCalendar());
		otTicketInfoTO.setTicketNote("CREATETAG");
		ArrayList<OTTicketInfoTO> tktInfoList = new ArrayList<>();
		tktInfoList.add(otTicketInfoTO);
		createTransactionTO.setTktInfoList(tktInfoList);
		createTransactionTO.setTransactionDSSN(transactionDSSN);
		otCmdTO.setCreateTransactionTO(createTransactionTO);

		
		
	}
}
