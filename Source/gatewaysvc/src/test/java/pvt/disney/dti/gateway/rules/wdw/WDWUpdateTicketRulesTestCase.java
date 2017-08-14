package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO.CmdAttributeTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 JUnit WDWUpdateTicketRules
 */
public class WDWUpdateTicketRulesTestCase extends CommonTestUtils {
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
				TransactionType.UPDATETICKET);
		UpdateTicketRequestTO requestTO = new UpdateTicketRequestTO();
		createCommonRequest(dtiTxn);
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {

			ticket.addTicketDemographic(getBillingInfo());
			ticket.setTktValidityValidStart(new GregorianCalendar());
			ticket.setTktValidityValidEnd(new GregorianCalendar());
			TktStatusTO newStatus = ticket.new TktStatusTO();
			newStatus.setStatusValue("status");
			ticket.addTicketStatus(newStatus);
			ticket.setTktMarket("6");
			ticket.setTktSecurityLevel("2");
		}
		requestTO.setTicketList(ticketList);
		dtiTxn.getRequest().setCommandBody(requestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiTxn.setProvider(DTITransactionTO.ProviderType.DLRGATEWAY);
		String xmlString = null;
		CommandHeaderTO header = new CommandHeaderTO();
		CmdAttributeTO cmdAttributeTO = header.new CmdAttributeTO();
		cmdAttributeTO.setAttribValue("3");
		ArrayList<CmdAttributeTO> cmdAttributeList = new ArrayList<>();
		cmdAttributeList.add(cmdAttributeTO);
		header.setCmdAttributeList(cmdAttributeList);
		dtiTxn.getRequest().setCommandHeader(header);
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* Scenario:: 1 passing the dtTxn Object */
		try {
			xmlString = WDWUpdateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}

		/* Scenario:: 2 TktSecurityLevel and TktMarket is null */
		for (TicketTO ticket : ticketList) {
			ticket.setTktSecurityLevel(null);
			ticket.setTktMarket(null);
			ticket.setTktNote(null);
		}
		requestTO.setTicketList(ticketList);
		try {
			xmlString = WDWUpdateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
		/* Scenario:: 3 TktSecurityLevel is 0 */
		for (TicketTO ticket : ticketList) {
			ticket.setTktSecurityLevel("");
		}
		requestTO.setTicketList(ticketList);
		try {
			xmlString = WDWUpdateTicketRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}

	}

	/**
	 * JUnit for transformResponseBody
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPDATETICKET);

		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.UPDATETRANSACTION);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		/* Scenario:: 1 passing the dtTxn,otCmdTO,dtiRespTO Object */
		try {
			WDWUpdateTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyWDWUpdateTicketRules
	 */
	@Test
	public void testApplyWDWUpdateTicketRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPDATETRANSACTION);
		UpdateTicketRequestTO requestTO = new UpdateTicketRequestTO();
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiRequestTO.setCommandBody(requestTO);
		dtiTxn.setRequest(dtiRequestTO);
		/* Scenario:: 1 passing the dtTxn Object */
		try {
			WDWUpdateTicketRules.applyWDWUpdateTicketRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}
}
