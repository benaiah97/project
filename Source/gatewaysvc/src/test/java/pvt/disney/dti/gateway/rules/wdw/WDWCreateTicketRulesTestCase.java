package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.rules.CommonBusinessTest;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 JUnit WDWCreateTicketRules
 */
public class WDWCreateTicketRulesTestCase extends CommonBusinessTest {

	/**
	 * JUnit for transformRequest
	 *  
	 * @throws ParseException
	 */
	@Test
	public void testTransformRequest() throws ParseException {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.TICKERATEENTITLEMENT);
		/* mocking transformOTHeader */
		DTIMockUtil.mocktransformOTHeader();
		/* mocking getEligibilityAssocId */
		DTIMockUtil.mockgetEligibilityAssocId();
		/* mocking addHeaderElement */
		DTIMockUtil.mockaddHeaderElement();
		/* mocking addTxnBodyElement */
		DTIMockUtil.mockaddTxnBodyElement();
		/* mocking getProductList */
		DTIMockUtil.mockgetProductList();
		/* mocking transformTicketDemoData */
		DTIMockUtil.mocktransformTicketDemoData();
		/* mocking createOTPaymentList */
		DTIMockUtil.mockcreateOTPaymentList();
		/* mocking ResourceBundle getResourceBundle */
		setMockProperty();
		/* setting the dtitran req */
		getDtiTransation(dtiTxn);
		try {
			String response = WDWCreateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(response);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
		/*Scenario :: 1 EligibilityMember is not a number */
		CreateTicketRequestTO ticketRequestTO = new CreateTicketRequestTO();
		ticketRequestTO.setDefaultAccount("AccountPerTicket");
		ticketRequestTO.setEligibilityMember("ONE");
		ticketRequestTO.setEligibilityGroup("Group");
		dtiTxn.getRequest().setCommandBody(ticketRequestTO);
		try {
			String response = WDWCreateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(response);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
		/* Scenario :: 2 EligibilityMember is a number */
		ticketRequestTO.setEligibilityMember("3");
		dtiTxn.getRequest().setCommandBody(ticketRequestTO);
		try {
			String response = WDWCreateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(response);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
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
				TransactionType.TICKERATEENTITLEMENT);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.CREATETRANSACTION);
		getOTCommad(otCmdTO);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		getDtiTransation(dtiTxn);
		DTIMockUtil.mockgetTicketList();
		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
		/* Scenario :: 1 TktInfoList is null */
		otCmdTO.getCreateTransactionTO().setTktInfoList(null);

		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
		/* Scenario :: 2 TktInfoList size is zero */
		ArrayList<OTTicketInfoTO> tktInfoList = new ArrayList<>();
		otCmdTO.getCreateTransactionTO().setTktInfoList(tktInfoList);
		try {
			WDWCreateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
	}

	/**
	 * JUnit for applyWDWCreateTicketRules
	 */
	@Test
	public void testApplyWDWCreateTicketRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.TICKERATEENTITLEMENT);
		CreateTicketRequestTO createTicketRequestTO = new CreateTicketRequestTO();
		DTIRequestTO request = new DTIRequestTO();
		request.setCommandBody(createTicketRequestTO);
		dtiTxn.setRequest(request);
		/* Scenario :: 1 Exception not expected */
		try {
			WDWCreateTicketRules.applyWDWCreateTicketRules(dtiTxn);
		} catch (DTIException e) {
			Assert.fail("Unexpected Excption");
		}
	}

	/**
	 * @param dtiTxn
	 * @throws ParseException
	 */
	private void getDtiTransation(DTITransactionTO dtiTxn)
			throws ParseException {

		DemographicsTO demographicsTO = new DemographicsTO();
		ArrayList<TktAssignmentTO> ticketAssignmets = new ArrayList<>();
		ArrayList<DemographicsTO> ticketDemoList = new ArrayList<>();
		ticketDemoList.add(demographicsTO);
		ticketDemoList.add(demographicsTO);
		TicketTO ticketTO = new TicketTO();
		ticketTO.setTktItem(new BigInteger("2"));
		ticketTO.setProdCode("AAA01");
		ticketTO.setProdQty(new BigInteger("1"));
		ticketTO.setProdPrice(new BigDecimal("2"));
		ticketTO.setTktNote("tkNote");
		ticketTO.setTktShell("23");
		ticketTO.setTktValidityValidStart(new GregorianCalendar());
		ticketTO.setTktValidityValidEnd(new GregorianCalendar());
		ticketTO.setTicketDemoList(ticketDemoList);
		TktAssignmentTO assignmentTO = ticketTO.new TktAssignmentTO();
		assignmentTO.setProdQty(new BigInteger("2"));
		assignmentTO.setAccountItem(new BigInteger("2"));
		ticketAssignmets.add(assignmentTO);
		ticketTO.setTicketAssignmets(ticketAssignmets);
		ArrayList<TicketTO> ticketList = new ArrayList<>();
		ticketList.add(ticketTO);
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setPdtCode("AAA01");
		dbProductTO.setValidityDateInfoRequired(true);
		dbProductTO.setConsumable(false);
		ArrayList<DBProductTO> dbProdListIn = new ArrayList<>();
		dbProdListIn.add(dbProductTO);
		dtiTxn.setDbProdList(dbProdListIn);
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		CreateTicketRequestTO ticketRequestTO = new CreateTicketRequestTO();
		ticketRequestTO.setTicketList(ticketList);
		ticketRequestTO.setDefaultAccount("AccountPerOrder");
		ticketRequestTO.setEligibilityGroup("DVC");
		ticketRequestTO.setEligibilityMember("2");
		PaymentTO paymentTO = new PaymentTO();
		ArrayList<PaymentTO> paymentList = new ArrayList<>();
		paymentList.add(paymentTO);
		ticketRequestTO.setPaymentList(paymentList);
		dtiRequestTO.setCommandBody(ticketRequestTO);
		PayloadHeaderTO header = new PayloadHeaderTO();
		header.setPayloadID("123456");
		dtiRequestTO.setPayloadHeader(header);
		dtiTxn.setRequest(dtiRequestTO);
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap = new HashMap<>();
		AttributeTO attributeTO = new AttributeTO();
		attributeTO.setAttrValue("123");
		attributeTOMap
				.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER, attributeTO);
		dtiTxn.setAttributeTOMap(attributeTOMap);
		EntityTO entityTO = new EntityTO();
		dtiTxn.setEntityTO(entityTO);
	}

	/**
	 * @param otCmdTO
	 * @throws ParseException
	 */
	private void getOTCommad(OTCommandTO otCmdTO) throws ParseException
	{
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
