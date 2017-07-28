package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO.CmdAttributeTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 JUnit WDWUpdateTransactionRules
 */
public class WDWUpdateTransactionRulesTestCase extends CommonTestUtils {
	/**
	 * @throws Exception
	 */
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
				TransactionType.UPDATETRANSACTION);
		UpdateTransactionRequestTO requestTO = new UpdateTransactionRequestTO();
		createCommonRequest(dtiTxn);
		TicketTransactionTO transaction = new TicketTransactionTO();
		AgencyTO agency = new AgencyTO();
		agency.setIATA("iata");
		transaction.setDssn(new GregorianCalendar(), "1", "2", " 2");
		requestTO.setTransaction(transaction);
		requestTO.setSalesRep("salesRep");
		requestTO.setAgency(agency);
		transaction.setDssnNumber("2");
		dtiTxn.getRequest().setCommandBody(requestTO);
		CommandHeaderTO header = new CommandHeaderTO();
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		/* getting header parameters */
		getHeader(header);
		dtiTxn.getRequest().setCommandHeader(header);
		String xmlString = null;
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* Scenario:: 1 passing the dtTxn Object */
		try {
			xmlString = WDWUpdateTransactionRules.transformRequest(dtiTxn);
			Assert.assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
		/* Scenario:: 2 CmdMarke and SalesRep is null */
		transaction.setTranNID("tranNID");
		header.setCmdMarket(null);
		requestTO.setSalesRep(null);
		try {
			xmlString = WDWUpdateTransactionRules.transformRequest(dtiTxn);
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
				TransactionType.UPDATETRANSACTION);

		OTCommandTO otCmdTO = new OTCommandTO(
				OTCommandTO.OTTransactionType.UPDATETRANSACTION);
		/* Scenario:: 1 passing the dtTxn,otCmdTO,dtiRespTO Object */
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		try {
			WDWUpdateTransactionRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyWDWUpdateTransactionRules
	 */
	@Test
	public void testApplyWDWUpdateTransactionRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPDATETRANSACTION);
		/* Scenario:: 1 passing the dtTxn Object */
		try {
			WDWUpdateTransactionRules.applyWDWUpdateTransactionRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Excption" + dtie.getLogMessage());
		}
	}

	/**
	 * @param header
	 */
	private void getHeader(CommandHeaderTO header) {
		CmdAttributeTO cmdAttributeTO = header.new CmdAttributeTO();
		cmdAttributeTO.setAttribValue("1");
		ArrayList<CmdAttributeTO> cmdAttributeList = new ArrayList<>();
		cmdAttributeList.add(cmdAttributeTO);
		header.setCmdAttributeList(cmdAttributeList);
		header.setCmdOperator("1");
		header.setCmdActor("MGR");
		header.setCmdDevice("device");
		header.setCmdInvoice("invoice");
		header.setCmdMarket("cmdMarket");

	}
}
