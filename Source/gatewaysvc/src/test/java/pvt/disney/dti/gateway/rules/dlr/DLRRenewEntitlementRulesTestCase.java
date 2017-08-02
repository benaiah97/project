package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author RASTA006 Test Case for Renew EntitlementRules
 *
 */
public class DLRRenewEntitlementRulesTestCase extends CommonTestUtils {
	/**
	 * Test Case for transformRequest
	 */
	@Test
	public void testTransformRequest() {
		String xmlString = null;
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn);
		RenewEntitlementRequestTO queryReq = new RenewEntitlementRequestTO();
		getRenewEntitlementRequestTO(queryReq);
		dtiTxn.getRequest().setCommandBody(queryReq);

		TPLookupTO aTPLookupSalesTypeTO = new TPLookupTO();
		aTPLookupSalesTypeTO.setLookupValue("123");
		aTPLookupSalesTypeTO.setLookupType(TPLookupTO.TPLookupType.SALES_TYPE);
		aTPLookupSalesTypeTO.setLookupDesc("SalesProgram");
		TPLookupTO aTPLookupLanguageTO = new TPLookupTO();
		aTPLookupLanguageTO.setLookupValue("123");
		aTPLookupLanguageTO.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<TPLookupTO>();
		aTPLookupLanguageTO.setLookupDesc("SalesProgram");
		tpLookupTOListIn.add(aTPLookupSalesTypeTO);
		tpLookupTOListIn.add(aTPLookupLanguageTO);
		TPLookupTO aTPClientTypeTO = new TPLookupTO();
		aTPClientTypeTO.setLookupValue("123");
		aTPClientTypeTO.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		aTPClientTypeTO.setLookupDesc("CcType");
		tpLookupTOListIn.add(aTPClientTypeTO);
		TPLookupTO aTPClientTypeTO1 = new TPLookupTO();
		aTPClientTypeTO1.setLookupValue("GC");
		aTPClientTypeTO1.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		aTPClientTypeTO1.setLookupDesc("CcType");
		tpLookupTOListIn.add(aTPClientTypeTO1);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		// queryReq.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		try {
			xmlString = DLRRenewEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("GWTPLookup for SalesProgram is missing in the database.", dtie.getLogMessage());

		}
		TPLookupTO aTPLookupClientTypeTO = new TPLookupTO();
		aTPLookupClientTypeTO.setLookupValue("123");
		aTPLookupClientTypeTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		aTPLookupClientTypeTO.setLookupDesc("SalesProgram");
		tpLookupTOListIn.add(aTPLookupClientTypeTO);
		try {
			xmlString = DLRRenewEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("GWTPLookup for SOCARenewPlan or CARenewPlan is missing in the database.",
					dtie.getLogMessage());

		}

		TPLookupTO aTPLookupPickUpTypeTO = new TPLookupTO();
		aTPLookupPickUpTypeTO.setLookupValue("123");
		aTPLookupPickUpTypeTO.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		aTPLookupPickUpTypeTO.setLookupDesc("CARenewPlan");
		tpLookupTOListIn.add(aTPLookupPickUpTypeTO);

		try {
			xmlString = DLRRenewEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_CRITICAL_ERROR, dtie.getDtiErrorCode());
			assertEquals("Internal Error:  DLR Installmnt paymentPlan not in TP_LOOKUP Table for CARenewPlan.",
					dtie.getLogMessage());

		}
		queryReq.setEligibilityMember("SOCA_RES");
		aTPLookupPickUpTypeTO = new TPLookupTO();
		aTPLookupPickUpTypeTO.setLookupValue("123");
		aTPLookupPickUpTypeTO.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		aTPLookupPickUpTypeTO.setLookupDesc("SOCARenewPlan");
		tpLookupTOListIn.add(aTPLookupPickUpTypeTO);

		try {
			xmlString = DLRRenewEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_CRITICAL_ERROR, dtie.getDtiErrorCode());
			assertEquals("Internal Error:  DLR Installmnt paymentPlan not in TP_LOOKUP Table for SOCARenewPlan.",
					dtie.getLogMessage());

		}
		queryReq.setEligibilityMember("CA_RES");
		aTPLookupPickUpTypeTO = new TPLookupTO();
		aTPLookupPickUpTypeTO.setLookupValue("123");
		aTPLookupPickUpTypeTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		aTPLookupPickUpTypeTO.setLookupDesc("CARenewPlan");
		tpLookupTOListIn.add(aTPLookupPickUpTypeTO);

		try {
			xmlString = DLRRenewEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception:: " + dtie.getLogMessage());
		}

	}

	private void getRenewEntitlementRequestTO(RenewEntitlementRequestTO renewEntReqTO) {

		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.EXTERNAL_ID);
		TicketTO ticket = new TicketTO();
		ticket.setTktItem(new BigInteger("1"));
		ticket.setExternal("1");
		ticket.setProdCode("1");
		ticket.setProdPrice(new BigDecimal("1"));
		for (TicketTO ticketTo : ticketList) {
			ticketTo.getTicketDemoList().add(getBillingInfo());
			ticketTo.setProdPrice(new BigDecimal("1"));
			ticketTo.setProdQty(new BigInteger("1"));
			ticketTo.setExistingTktID(ticket);
		}
		renewEntReqTO.setTktList(ticketList);
		renewEntReqTO.setEligibilityGroup("1");
		renewEntReqTO.setEligibilityMember("CA_RES");
		renewEntReqTO.setInstallmentRenewRequest(true);
		ClientDataTO clientData = new ClientDataTO();
		getClientData(clientData);
		renewEntReqTO.setClientData(clientData);
		PaymentTO payment1 = new PaymentTO();
		payment1.setPayType(PaymentType.CREDITCARD);
		CreditCardTO cCardTO = new CreditCardTO();
		cCardTO.setCcNbr("1234567890");
		cCardTO.setCcType("123");
		cCardTO.setCcStreet("Street1");
		cCardTO.setCcVV("1234");
		payment1.setCreditCard(cCardTO);
		PaymentTO payment2 = new PaymentTO();
		InstallmentCreditCardTO insCardTO = new InstallmentCreditCardTO();
		insCardTO.setCcNbr("1234567890");
		insCardTO.setCcType("123");
		insCardTO.setCcStreet("Street1");
		insCardTO.setCcVV("1234");
		InstallmentTO installment = new InstallmentTO();
		installment.setCreditCard(insCardTO);
		payment2.setInstallment(installment);
		PaymentTO payment3 = new PaymentTO();
		GiftCardTO gCardTO = new GiftCardTO();
		gCardTO.setGcNbr("1234567890");
		payment3.setGiftCard(gCardTO);
		ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();
		paymentList.add(payment1);
		paymentList.add(payment2);
		paymentList.add(payment3);

		renewEntReqTO.setPaymentList(paymentList);
		ReservationTO reserv = new ReservationTO();
		reserv.setResPickupArea("ABC");
		reserv.setResSalesType("123");
		reserv.setResCode("ABCD");
		reserv.setResPickupDate(new GregorianCalendar());
		renewEntReqTO.setReservation(reserv);
	}

	@Test
	public void testTransformResponse() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		String xmlResponse = null;
		InputStream fileName = this.getClass().getResourceAsStream(DLR_XML_PATH + "DLR_QueryTicket_01_Rsp_Error.xml");
		xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		RenewEntitlementRequestTO queryReq = new RenewEntitlementRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.setTpRefNum(new Integer(1));
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			dtiTxn = DLRRenewEntitlementRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE, dtie.getDtiErrorCode());
		}

		fileName = this.getClass().getResourceAsStream(DLR_XML_PATH + "DLR_RenewEntitlement_Res_01.xml");
		xmlResponse = DTITestUtilities.getXMLFromFile(fileName);

		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			dtiTxn = DLRRenewEntitlementRules.transformResponse(dtiTxn, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}

	}

	@Test
	public void testApplyDLRRenewEntitlementRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn);
		RenewEntitlementRequestTO queryReq = new RenewEntitlementRequestTO();
		// getRenewEntitlementRequestTO(queryReq);
		dtiTxn.getRequest().setCommandBody(queryReq);
		try {
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
		}
		ReservationTO reserv = new ReservationTO();
		reserv.setResPickupArea("ABC");
		// reserv.setResSalesType("123");
		// reserv.setResCode("ABCD");
		reserv.setResPickupDate(new GregorianCalendar());
		queryReq.setReservation(reserv);
		try {
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
		}
		ClientDataTO clientData = new ClientDataTO();
		getClientData(clientData);
		queryReq.setClientData(clientData);
		try {
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
		}
		reserv.setResCode("ABCD");
		try {
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
		}
		reserv.setResSalesType("123");

		DemographicsTO aDemo = new DemographicsTO();
		aDemo.setCountry("1234");

		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticketTo : ticketList) {
			ticketTo.getTicketDemoList().add(aDemo);
		}
		queryReq.setTktList(ticketList);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_MSG_CONTENT, dtie.getDtiErrorCode());
		}
		aDemo.setCountry("12");
		for (TicketTO ticketTo : ticketList) {
			ticketTo.getTicketDemoList().add(aDemo);
		}
		queryReq.setTktList(ticketList);
		PaymentTO payment2 = new PaymentTO();
		InstallmentCreditCardTO insCardTO = new InstallmentCreditCardTO();
		insCardTO.setCcNbr("1234567890");
		insCardTO.setCcType("123");
		insCardTO.setCcStreet("Street1");
		insCardTO.setCcVV("1234");
		InstallmentTO installment = new InstallmentTO();
		installment.setCreditCard(insCardTO);
		payment2.setInstallment(installment);
		ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();
		paymentList.add(payment2);
		queryReq.setPaymentList(paymentList);
		try {
			DLRRenewEntitlementRules.applyDLRRenewEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			// TODO Somesh
			// Assert.fail("Unexpected Exception:: " + dtie.getLogMessage());
		}
	}

	/**
	 * test Case for transformError
	 */
	// @Test TODO
	public void testTransformError() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		String xmlResponse = "<Envelope> " + "<Header>" + "<MessageID>290118</MessageID>"
				+ "<MessageType>QueryTicketResponse</MessageType>" + "<SourceID>WDPRODLRNA</SourceID>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp() + "</TimeStamp>" + "<EchoData />" + "<SystemFields />" + "</Header>"
				+ "<Body><Status><StatusCode>1300</StatusCode><StatusText>QueryTicket request error</StatusText></Status>"
				+ "<QueryTicketErrors><Errors><Error><ErrorCode>1306</ErrorCode><ErrorText>Can not process messages from source WDPRODLRNA, source is inactive</ErrorText>"
				+ "</Error></Errors><DataRequestErrors /></QueryTicketErrors></Body></Envelope>";
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		String statusCode = "1";
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil.mockDtiErrorCodeScope("COMMAND");
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		queryReq.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		try {
			dtiTxn = DLRRenewEntitlementRules.transformError(dtiTxn, dtiRespTO, statusCode, xmlResponse);
			assertNotNull(dtiTxn.getResponse());
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getMessage());
		}

	}

}
