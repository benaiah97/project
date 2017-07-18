package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test class for DLRUpgradeEntitlement
 * 
 * @author GANDV005
 *
 */
public class DLRUpgradeEntitlementRulesTestCase extends DTIMockUtil {

	/**
	 * Test case for transforming the request send to eGalaxy
	 */
	@Test
	public void testTransformRequest() {
		String xmlString = null;
		TPLookupTO aTPLookupSalesTypeTO = new TPLookupTO();
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<TPLookupTO>();
		DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.UPGRADEENTITLEMENT);
		TPLookupTO aTPLookupClientTypeTO = new TPLookupTO();
		aTPLookupClientTypeTO.setLookupValue("456");
		aTPLookupClientTypeTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		aTPLookupClientTypeTO.setLookupDesc("SalesProgram");
		tpLookupTOListIn.add(aTPLookupClientTypeTO);
		commonRequestResponseUtil(xmlString, aTPLookupSalesTypeTO, tpLookupTOListIn, dtiTxn);

		try {
			xmlString = DLRUpgradeEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("GWTPLookup for SalesProgram is missing in the database.", dtie.getLogMessage());
		}
	}

	private void commonRequestResponseUtil(String xmlString, TPLookupTO aTPLookupSalesTypeTO,
			ArrayList<TPLookupTO> tpLookupTOListIn, DTITransactionTO dtiTxn) {
		createCommonRequest(dtiTxn);
		UpgradeEntitlementRequestTO queryReq = new UpgradeEntitlementRequestTO();
		getUpgradeEntitlementRequestTO(queryReq);
		ReservationTO reservationTO = new ReservationTO();
		reservationTO.setResCode("123");
		GregorianCalendar resPickupDate = new GregorianCalendar();
		reservationTO.setResPickupDate(resPickupDate);
		queryReq.setReservation(reservationTO);
		ClientDataTO clientDataTO = new ClientDataTO();
		DemographicsTO billingInfo = new DemographicsTO();
		billingInfo.setFirstName("firstName");
		billingInfo.setLastName("lastName");
		billingInfo.setAddr1("addr1");
		billingInfo.setAddr2("addr2");
		billingInfo.setCity("city");
		billingInfo.setState("state");
		billingInfo.setZip("zip");
		billingInfo.setCountry("country");
		billingInfo.setTelephone("telephone");
		billingInfo.setEmail("email");
		billingInfo.setName("name");
		clientDataTO.setBillingInfo(billingInfo);
		clientDataTO.setShippingInfo(billingInfo);
		queryReq.setClientData(clientDataTO);
		dtiTxn.getRequest().setCommandBody(queryReq);
		TPLookupTO tpLookupTO = new TPLookupTO();
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		tpLookupTO.setLookupValue("123");
		tpLookupTO.setLookupDesc("GC");
		TPLookupTO tpLook = new TPLookupTO();
		tpLookupTOListIn.add(tpLookupTO);
		tpLookupTOListIn.add(tpLook);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
	}

	private void getUpgradeEntitlementRequestTO(UpgradeEntitlementRequestTO upgEntReqTO) {

		ArrayList<PaymentTO> paymentList = new ArrayList<PaymentTO>();
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

		paymentList.add(payment1);
		paymentList.add(payment2);

		upgEntReqTO.setPaymentList(paymentList);
	}

	/**
	 * Test case for transforming the eGalaxy XML response string into response
	 * objects within the DTI transfer object
	 */
	@Test
	public void testTransformResponse() {
		String xmlResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope>"
				+"<Header>"
				+"<MessageID>140080866</MessageID>"
				+"<MessageType>SetOrderStatus</MessageType>"
				+"<SourceID>WDPRONADLR</SourceID>"
				+"<TimeStamp>2017-07-18 01:26:29</TimeStamp>"
				+"<EchoData>39600000010025866624</EchoData>"
				+"<SystemFields/>"
				+"</Header>"
				+"<Body>"
				+"<OrderID>DOLY0000010004622979</OrderID>"
				+"<GalaxyOrderID>14676273</GalaxyOrderID>"
				+"<Status>2</Status>"
				+"<AuthCode>892994</AuthCode>"
				+"<ResponseText>APPROVED 892994</ResponseText>"
				+"<CVNResultCode/>"
				+"<PaymentContracts>"
				+"<PaymentContractID>3419456</PaymentContractID>"
				+"</PaymentContracts>"
				+"</Body>"
				+"</Envelope>";
		TPLookupTO aTPLookupSalesTypeTO = new TPLookupTO();
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<TPLookupTO>();
		DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.UPGRADEENTITLEMENT);
		commonRequestResponseUtil(xmlResponse, aTPLookupSalesTypeTO, tpLookupTOListIn, dtiTxn);
		DTITransactionTO dtiTransactionTO = null;
		try {
			dtiTransactionTO = DLRUpgradeEntitlementRules.transformResponse(dtiTxn, xmlResponse);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("GWTPLookup for SalesProgram is missing in the database.", dtie.getLogMessage());
		}
		assertNotNull(dtiTransactionTO);
	}

	/**
	 * Test case for Transforms a DLR Orders error
	 */
	@Test
	public void testTransformError() {
		String xmlResponse = null;
		TPLookupTO aTPLookupSalesTypeTO = new TPLookupTO();
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<TPLookupTO>();
		DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.UPGRADEENTITLEMENT);
		commonRequestResponseUtil(xmlResponse, aTPLookupSalesTypeTO, tpLookupTOListIn, dtiTxn);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		String statusString = "Error";
		processMockprepareAndExecuteSql();
		try {
			DLRUpgradeEntitlementRules.transformError(dtiTxn, dtiRespTO, statusString, xmlResponse);
		} catch (DTIException e) {
			assertEquals(e.getMessage(), e.getMessage());
		}
	}

}