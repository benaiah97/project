package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Properties;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012
 * Test Case for Calm Rules
 */
public class CalmRulesTestCase extends CommonBusinessTest {
	private static CalmRules calmRules = null;
	/* CALM.QueryTicket.ReplyMACs property value */
	private static String tsMacWadm = "WDWADMIN";
	private static String tsMacWdp = "WDPRONA";

	/**
	 * Unit test for CheckContingencyActionsLogicModule
	 * 
	 */
	@Test
	public void testCheckContingencyActionsLogicModule() throws DTIException,
			DTICalmException {
		calmRules = CalmRules.getInstance(setConfigProperty());
		super.setMockProperty();
		DTITransactionTO dTITransactionTO = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		getDTITransactionTO(dTITransactionTO);

		/* Mocking exists method of file */
		DTIMockUtil.mockFileExists();
		/* JUnit for executeWDWDownRules */
		/*
		 * Scenario :: 1 Expected Exception is WDW Request attempted when
		 * WDWDown outage wall file is present (CALM).
		 */
		dTITransactionTO.getRequest().getPayloadHeader().getTktSeller()
				.setTsMac(tsMacWadm);
		dTITransactionTO.setProvider(DTITransactionTO.ProviderType.WDWNEXUS);

		try {
			calmRules.checkContingencyActionsLogicModule(dTITransactionTO);
		} catch (DTIException dtie) {

			assertEquals(
					"WDW Request attempted when WDWDown outage wall file is present (CALM).",
					dtie.getLogMessage());
		}
		/* JUnit for createAPQueryWDWTicketResp */
		/*
		 * Scenario :: 2 Expected Exception is WDW Request attempted when
		 * WDWDown outage wall file is present (CALM).
		 */
		dTITransactionTO.getRequest().getPayloadHeader().getTktSeller()
				.setTsMac(tsMacWdp);

		try {
			calmRules.checkContingencyActionsLogicModule(dTITransactionTO);
		} catch (DTIException dtie) {

			assertEquals(
					"WDW Request attempted when WDWDown outage wall file is present (CALM).",
					dtie.getLogMessage());
		} catch (DTICalmException dtic) {
			assertEquals(DTICalmException.class, dtic.getClass());
		}
		/* JUnit for executeDLRDownRules */
		/*
		 * Scenario :: 3 Expected Exception is DLR Request attempted when
		 * DLRDown outage wall file is present (CALM).
		 */
		dTITransactionTO.getRequest().getPayloadHeader().getTktSeller()
				.setTsMac(tsMacWadm);
		dTITransactionTO.setProvider(ProviderType.DLRGATEWAY);
		try {
			calmRules.checkContingencyActionsLogicModule(dTITransactionTO);
		} catch (DTIException dtie) {
			assertEquals(
					"DLR Request attempted when DLRDown outage wall file is present (CALM).",
					dtie.getLogMessage());
		}
		/* JUnit for createAPQueryDLRTicketResp */
		/* Scenario :: 4 when ProviderType is DLRGATEWAY */
		dTITransactionTO.getRequest().getPayloadHeader().getTktSeller()
				.setTsMac(tsMacWdp);
		dTITransactionTO.setProvider(ProviderType.DLRGATEWAY);
		try {
			calmRules.checkContingencyActionsLogicModule(dTITransactionTO);
		} catch (DTICalmException dtic) {
			assertEquals(DTICalmException.class, dtic.getClass());
		}
		/* JUnit for executeHKDDownRules */
		/* Scenario :: 4 when ProviderType is HKDNEXUS */
		dTITransactionTO.setProvider(ProviderType.HKDNEXUS);
		try {
			calmRules.checkContingencyActionsLogicModule(dTITransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected error.");
		}
	}

	/**
	 * @param dTITransactionTO
	 */
	private final void getDTITransactionTO(DTITransactionTO dTITransactionTO) {
		TicketTO ticketTO = new TicketTO();
		ArrayList<TicketTO> tktList = new ArrayList<>();
		tktList.add(ticketTO);
		TktSellerTO tktSellerTO = new TktSellerTO();
		PayloadHeaderTO payloadHeaderTO = new PayloadHeaderTO();
		payloadHeaderTO.setTktSeller(tktSellerTO);
		CommandHeaderTO commandHeaderTO = new CommandHeaderTO();
		DTIRequestTO dTIRequestTO = new DTIRequestTO();
		dTIRequestTO.setPayloadHeader(payloadHeaderTO);
		dTIRequestTO.setCommandHeader(commandHeaderTO);
		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		queryTicketRequestTO.setTktList(getTicketList(TicketIdType.TKTNID_ID));
		dTIRequestTO.setCommandBody(queryTicketRequestTO);
		dTITransactionTO.setRequest(dTIRequestTO);
	}

	public Properties setConfigProperty() {
		props = new Properties();
		props.setProperty("CALM.WDWDownFileName",
				"/var/opt/apps/WDPRApps/DTI/gateway/wdwdown");
		props.setProperty("CALM.DLRDownFileName",
				"/var/opt/apps/WDPRApps/DTI/gateway/dlrdown");
		props.setProperty("CALM.HKDDownFileName",
				"/var/opt/apps/WDPRApps/DTI/gateway/hkddown");
		props.setProperty("CALM.QueryTicket.ReplyMACs",
				"WDPRONA,WDPRONADLR,DLRMerch,WDWMerch");
		return props;
	}

}
