package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO.PaymentMethod;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;

/**
 * This class tests the transform rules.
 * 
 * @author lewit019
 * 
 */
public class TransformRulesTestCase extends CommonBusinessTest {

	private static final Integer TPREFNUM = new Integer(123456);
	private static final String PAYLOADID = "1234567890";
	private static final String TARGET = "TARGET";
	private static final String VERSION = "VERSION";
	private static final String METHOD = "METHOD";
	private static final String PROTOCOL = "PROTOCOL";
	private static final String BROKER = "BROKER";
	private static final BigInteger CMDCOUNT = new BigInteger("1");
	private static final BigInteger CMDITEM = new BigInteger("1");

	/** Default constant for zero duration. */
	private final static BigDecimal ZERO_DURATION = new BigDecimal("0.0");

	/**
	 * Tests the create response payload header method.
	 */
	@Test
	public final void testCreateRespPayloadHdr() {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		DTIRequestTO dtiRqst = new DTIRequestTO();
		PayloadHeaderTO rqstHeader = new PayloadHeaderTO();
		dtiRqst.setPayloadHeader(rqstHeader);
		dtiTxn.setRequest(dtiRqst);

		// Set up data for test
		dtiTxn.setTpRefNum(TPREFNUM);
		rqstHeader.setPayloadID(PAYLOADID);
		rqstHeader.setTarget(TARGET);
		rqstHeader.setVersion(VERSION);
		rqstHeader.setCommMethod(METHOD);
		rqstHeader.setCommProtocol(PROTOCOL);
		dtiTxn.setTktBroker(BROKER);

		PayloadHeaderTO respHeader = TransformRules
				.createRespPayloadHdr(dtiTxn);
		// checking for null PayloadHeaderTO object
		assertNotNull(respHeader);

		// Evaluate responses.
		// PayloadID

		assertFalse(
				"Invalid value found in PayloadID:" + respHeader.getPayloadID(),
				respHeader.getPayloadID().compareTo(TPREFNUM.toString()) != 0);
		// TSPayloadID
		assertFalse(
				"Invalid value found in TSPayloadID: "
						+ respHeader.getTsPayloadID(), respHeader
						.getTsPayloadID().compareTo(PAYLOADID) != 0);

		// Target

		assertFalse("Invalid value found in Target: " + respHeader.getTarget(),
				respHeader.getTarget().compareTo(TARGET) != 0);

		// Version

		assertFalse(
				"Invalid value found in Version: " + respHeader.getVersion(),
				respHeader.getVersion().compareTo(VERSION) != 0);

		// CommMethod

		assertFalse(
				"Invalid value found in CommMethod: "
						+ respHeader.getCommMethod(), respHeader
						.getCommMethod().compareTo(METHOD) != 0);

		// CommProtocol

		assertFalse(
				"Invalid value found in CommProtocol: "
						+ respHeader.getCommProtocol(), respHeader
						.getCommProtocol().compareTo(PROTOCOL) != 0);

		// CommandCount

		assertFalse(
				"Invalid value found in CommandCount: "
						+ respHeader.getCommandCount(), respHeader
						.getCommandCount().compareTo(CMDCOUNT) != 0);

		// TktBroker

		assertFalse(
				"Invalid value found in TktBroker: "
						+ respHeader.getTktBroker(), respHeader.getTktBroker()
						.compareTo(BROKER) != 0);

		// TransmitDateTime

		assertFalse("TransmitDateTime was null.",
				respHeader.getTransmitDateTime() == null);

		return;
	}

	/**
	 * Tests the create response command header method.
	 */
	@Test
	public final void testCreateRespCmdHdr() {

		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		DTIRequestTO dtiRqst = new DTIRequestTO();
		CommandHeaderTO rqstHeader = new CommandHeaderTO();
		dtiRqst.setCommandHeader(rqstHeader);
		dtiTxn.setRequest(dtiRqst);

		rqstHeader.setCmdItem(CMDITEM);

		CommandHeaderTO respHeader = TransformRules.createRespCmdHdr(dtiTxn);
		// Checking for null CommandHeaderTO object
		assertNotNull(respHeader);

		// Evaluate responses.

		// CmdItem

		assertFalse(
				"Invalid value found in CmdItem: " + respHeader.getCmdItem(),
				respHeader.getCmdItem().compareTo(CMDITEM) != 0);

		// CmdDuration
		assertFalse(
				"Invalid value found in CmdDuration: "
						+ respHeader.getCmdDuration(), respHeader
						.getCmdDuration().compareTo(ZERO_DURATION) != 0);

		// CmdDateTime
		/*
		 * if (respHeader.getCmdDateTime() == null) {
		 * fail("CmdDateTime was null."); }
		 */
		assertFalse(
				"Invalid value found in CmdDuration: "
						+ respHeader.getCmdDuration(), respHeader
						.getCmdDuration().compareTo(ZERO_DURATION) != 0);

		return;
	}

	@Test
	public void testSetFulfillmentNoteDetails() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		DTIRequestTO dtiRqst = new DTIRequestTO();
		PayloadHeaderTO rqstHeader = new PayloadHeaderTO();
		ReservationRequestTO dtiCmdBody = new ReservationRequestTO();
		ClientDataTO clientData = new ClientDataTO();
		clientData
				.setGroupValidityValidStart((GregorianCalendar) GregorianCalendar
						.getInstance());
		clientData
				.setGroupValidityValidEnd((GregorianCalendar) GregorianCalendar
						.getInstance());
		clientData.setClientDeliveryInstructions("PASSED");
		clientData.setDeliveryInfo(getdlvAddrInfo());
		for (int i = 0; i < 11; i++) {
			setPaymentMethod(clientData, i);

			// clientData.setClientPaymentMethod(PaymentMethod.CREDITCARD);
			dtiCmdBody.setClientData(clientData);
			dtiCmdBody.setTaxExemptCode("TAX EXEMPT ");
			dtiRqst.setPayloadHeader(rqstHeader);
			dtiRqst.setCommandBody(dtiCmdBody);

			DBProductTO dbProduct = new DBProductTO();
			dbProduct.setValidityDateInfoRequired(true);

			ArrayList<DBProductTO> dbProductList = new ArrayList<DBProductTO>();
			dbProductList.add(dbProduct);
			dtiTxn.setDbProdList(dbProductList);

			dtiTxn.setRequest(dtiRqst);

			// Set up data for test
			dtiTxn.setTpRefNum(TPREFNUM);
			rqstHeader.setPayloadID(PAYLOADID);
			rqstHeader.setTarget(TARGET);
			rqstHeader.setVersion(VERSION);
			rqstHeader.setCommMethod(METHOD);
			rqstHeader.setCommProtocol(PROTOCOL);
			dtiTxn.setTktBroker(BROKER);
			try {
				String str = TransformRules.setFulfillmentNoteDetails(dtiTxn);
				assertNotNull(str);
			} catch (Exception e) {
				fail("Exception caught while executing TransformRules.setFulfillmentNoteDetails");
			}

		}
	}

	private void setPaymentMethod(ClientDataTO clientData, int i) {

		switch (i) {
		case 0:
			clientData.setClientPaymentMethod(PaymentMethod.CREDITCARD);
			break;
		case 1:
			clientData.setClientPaymentMethod(PaymentMethod.CASHIERSCHECK);
			break;
		case 2:
			clientData.setClientPaymentMethod(PaymentMethod.DISNEYGIFTCARD);
			break;
		case 3:
			clientData.setClientPaymentMethod(PaymentMethod.MASTERACCOUNT);
			break;
		case 4:
			clientData.setClientPaymentMethod(PaymentMethod.MONEYORDER);
			break;
		case 5:
			clientData
					.setClientPaymentMethod(PaymentMethod.ORGANIZATIONALCHECK);
			break;
		case 6:
			clientData.setClientPaymentMethod(PaymentMethod.PAYATPICKUP);
			break;
		case 7:
			clientData.setClientPaymentMethod(PaymentMethod.SAPJOBNUMBER);
			break;
		case 8:
			clientData.setClientPaymentMethod(PaymentMethod.TICKETEXCHANGE);
			break;
		case 9:
			clientData.setClientPaymentMethod(PaymentMethod.WIRETRANSFER);
			break;
		case 10:
			clientData.setClientPaymentMethod(PaymentMethod.UNDEFINED);
		}

	}

	private DemographicsTO getdlvAddrInfo() {
		DemographicsTO dlvAddr = new DemographicsTO();
		dlvAddr.setFirstName("XX");
		dlvAddr.setLastName("TT");
		dlvAddr.setName("XXTT");
		dlvAddr.setAddr1("ADDRESS1");
		dlvAddr.setAddr2("ADDRESS2");
		dlvAddr.setCity("TEST");
		dlvAddr.setState("TESTSTATE");
		dlvAddr.setZip("00001");
		dlvAddr.setCountry("US");
		dlvAddr.setTelephone("0123456789");
		dlvAddr.setEmail("test@test.com");
		return dlvAddr;
	}

	@Test
	public void testChangeToProviderFormat() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));

		dtiTxn.getRequest().setCommandBody(queryReq);
		dtiTxn.setTpRefNum(new Integer("1"));
		String xmlresponse = null;
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		try {
			xmlresponse = TransformRules.changeToProviderFormat(dtiTxn);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception");
		}
		dtiTxn.setProvider(ProviderType.HKDNEXUS);
		try {
			TransformRules.changeToProviderFormat(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.COMMAND_NOT_AUTHORIZED,
					dtie.getDtiErrorCode());
			assertEquals(
					"Invalid HKD transaction type sent to DTI Gateway.  Unsupported.",
					dtie.getLogMessage());
		}
		/*dtiTxn.setProvider(ProviderType.WDWNEXUS);
		try {
			TransformRules.changeToProviderFormat(dtiTxn);
		} catch (Exception e) {

		}*/
		
		

	}

}
