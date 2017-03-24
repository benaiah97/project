package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTRenewTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 JUnit WDWRenewEntitlementRules
 * 
 */
public class WDWRenewEntitlementRulesTestCase extends CommonTestUtils {
	@Before
	public void setUp() throws Exception {
		setMockProperty();
	}

	/**
	 * JUnit for transformRequest
	 */
	@Test
	public void testRransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn);
		RenewEntitlementRequestTO entitlementRequestTO = new RenewEntitlementRequestTO();
		entitlementRequestTO.setEligibilityGroup("DVC");
		entitlementRequestTO.setEligibilityMember("1");
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {
			ticket.addTicketDemographic(getBillingInfo());
			ticket.setExistingTktID(ticket);
			ticket.setBarCode("123456789");
		}
		/*mocking prepareAndExecuteSql*/ 
		DTIMockUtil.processMockprepareAndExecuteSql();
		entitlementRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(entitlementRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		String xmlstring = null;
		/*Scenario:: 1 passing the dtTxn Object*/
		try {
			xmlstring = WDWRenewEntitlementRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlstring);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformResponseBody
	 * 
	 * @throws ParseException
	 */
	@Test
	public void testTransformResponseBody() throws ParseException

	{
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {

			ticket.addTicketDemographic(getBillingInfo());
			ticket.setExistingTktID(ticket);
			ticket.setBarCode("123123");
		}
		/*mocking prepareAndExecuteSql*/ 
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIResponseTO response = new DTIResponseTO();
		dtiTxn.setResponse(response);
		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.RENEWTICKET);
		createCommonResponse(dtiTxn);
		getOTCommad(otCmdTO);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		/*Scenario:: 1 passing the dtTxn ,otCmdT, dtiRespTO OObject*/
		try {
			WDWRenewEntitlementRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}

	}

	/**
	 * JUnit for applyWDWRenewEntitlementRules
	 */
	@Test
	public void testApplyWDWRenewEntitlementRules() {
		PaymentTO paymentTO = new PaymentTO();
		paymentTO.setPayType(PaymentType.INSTALLMENT);
		ArrayList<PaymentTO> paymentList = new ArrayList<>();
		paymentList.add(paymentTO);
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		RenewEntitlementRequestTO entitlementRequestTO = new RenewEntitlementRequestTO();
		entitlementRequestTO.setPaymentList(paymentList);
		createCommonRequest(dtiTxn);
		dtiTxn.getRequest().setCommandBody(entitlementRequestTO);
		/*Scenario:: 1 passing the dtTxn Object*/
		try {
			WDWRenewEntitlementRules.applyWDWRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		entitlementRequestTO.getPaymentList().get(0)
				.setPayType(PaymentType.GIFTCARD);
		/*Scenario:: 2 PaymentType is GIFTCARD not INSTALLMENT*/
		try {
			WDWRenewEntitlementRules.applyWDWRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * @param otCmdTO
	 * @throws ParseException
	 */
	private void getOTCommad(OTCommandTO otCmdTO) throws ParseException {
		OTTicketTO ticket = new OTTicketTO();
		OTTicketInfoTO infoTO = new OTTicketInfoTO();
		infoTO.setItem(new BigInteger("2"));
		infoTO.setTicket(ticket);
		infoTO.setValidityStartDate(new GregorianCalendar());
		infoTO.setValidityEndDate(new GregorianCalendar());
		ArrayList<OTTicketInfoTO> ticketInfoList = new ArrayList<>();
		ticketInfoList.add(infoTO);
		OTTransactionDSSNTO transactionDSSN = new OTTransactionDSSNTO();
		transactionDSSN.setDate(new GregorianCalendar());
		transactionDSSN.setSite("site");
		transactionDSSN.setStation("station");
		transactionDSSN.setTransactionId(2);
		OTRenewTicketTO renewTicketTO = new OTRenewTicketTO();
		renewTicketTO.setRenewTicketInfoList(ticketInfoList);
		renewTicketTO.setTransactionDSSN(transactionDSSN);
		otCmdTO.setRenewTicketTO(renewTicketTO);

	}
}
