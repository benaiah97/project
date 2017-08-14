package pvt.disney.dti.gateway.rules.dlr;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for DLRReservationRules
 * 
 */
public class DLRReservationRulesTestCase extends CommonTestUtils {
	/**
	 * JUnit for transformRequest
	 */
	@Test
	public void testTransformRequest() {

		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		ReservationRequestTO reservationRequestTO = new ReservationRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		getTicketDetails(ticketList);
		reservationRequestTO.setTktList(ticketList);
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("22");
		reservation.setResPickupDate(new GregorianCalendar());
		reservationRequestTO.setReservation(reservation);
		ArrayList<String> noteList = new ArrayList<>();
		noteList.add("Personal Message");
		reservationRequestTO.setNoteList(noteList);
		ClientDataTO clientData = new ClientDataTO();
		getClientData(clientData);
		reservationRequestTO.setClientData(clientData);
		reservationRequestTO.setEligibilityGroup("DLR");
		reservationRequestTO.setEligibilityMember("SOCA_RES");
		reservationRequestTO.setInstallmentResRequest(true);
		reservationRequestTO.setInstallmentDownpayment(new BigDecimal("22"));
		reservationRequestTO.setAPPassInfo("passInfo");
		PaymentTO paymentTO = new PaymentTO();
		paymentTO.setPayType(PaymentTO.PaymentType.CREDITCARD);
		CreditCardTO creditCard = new CreditCardTO();
		creditCard.setCcNbr("123");
		creditCard.setCcType("123");
		creditCard.setCcZipCode("1236547889");
		creditCard.setCcStreet("1236547889");
		creditCard.setCcVV("1236547889");
		paymentTO.setCreditCard(creditCard);
		ArrayList<PaymentTO> paymentList = new ArrayList<>();
		paymentList.add(paymentTO);
		reservationRequestTO.setPaymentList(paymentList);
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiTxn.setProvider(DTITransactionTO.ProviderType.DLRGATEWAY);
		PaymentLookupTO lookupTO = new PaymentLookupTO();
		lookupTO.setPymtCode("2");
		lookupTO.setLookupValue("3");
		ArrayList<PaymentLookupTO> paymentLookupTOList = new ArrayList<>();
		paymentLookupTOList.add(lookupTO);
		dtiTxn.setPaymentLookupTOList(paymentLookupTOList);
		EntityTO entityTO = new EntityTO();
		entityTO.setCustomerId("2");
		dtiTxn.setEntityTO(entityTO);
		TPLookupTO tpLookupTO = new TPLookupTO();
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.SHIP_METHOD);
		tpLookupTO.setLookupValue("SOCAPurchPlan");
		tpLookupTO.setLookupDesc("SOCAPurchPlan");
		TPLookupTO tpLookupInstall = new TPLookupTO();
		tpLookupInstall.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		tpLookupInstall.setLookupValue("details");
		tpLookupInstall.setLookupDesc("SalesProgram");
		TPLookupTO tpLookupInstallment = new TPLookupTO();
		tpLookupInstallment.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		tpLookupInstallment.setLookupValue("details");
		tpLookupInstallment.setLookupDesc("SOCAPurchPlan");
		TPLookupTO tpLook = new TPLookupTO();
		tpLook.setLookupType(TPLookupTO.TPLookupType.SHIP_DETAIL);
		tpLook.setLookupValue("SOCAPurchPlan");
		tpLook.setLookupDesc("SOCAPurchPlan");
		TPLookupTO tplookupCcType = new TPLookupTO();
		tplookupCcType.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
		tplookupCcType.setLookupValue("123");
		tplookupCcType.setLookupDesc("123");
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<>();
		tpLookupTOListIn.add(tpLookupTO);
		tpLookupTOListIn.add(tpLook);
		tpLookupTOListIn.add(tpLookupInstall);
		tpLookupTOListIn.add(tpLookupInstallment);
		tpLookupTOListIn.add(tplookupCcType);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		/* scenario :1 Passing dtiTxn in request */
		try {
			DLRReservationRules.transformRequest(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexcpected Exception" + dtie.getLogMessage());
		}
		tpLookupTOListIn = new ArrayList<>();
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		/*
		 * scenario :2 Excepted exception is GWTPLookup for SalesProgram is
		 * missing in the database.
		 */
		try {
			DLRReservationRules.transformRequest(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("GWTPLookup for SalesProgram is missing in the database.", dtie.getLogMessage());
		}
		/*
		 * scenario :3 Excepted exception is GWTPLookup for SOCAPurchPlan or
		 * CAPurchPlan is missing in the database.
		 */
		reservationRequestTO.setEligibilityGroup("BOLT");
		tpLookupTOListIn.add(tpLookupInstall);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		try {
			DLRReservationRules.transformRequest(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("GWTPLookup for SOCAPurchPlan or CAPurchPlan is missing in the database.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformResponse
	 */
	@Test
	public void testTransformResponse() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.QUERYRESERVATION);
		/* scenario :1 TransactionType is QUERYRESERVATION */
		createCommonRequest(dtiTxn);
		ReservationRequestTO requestTO = new ReservationRequestTO();
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("resCode");
		requestTO.setReservation(reservation);
		dtiTxn = new DTITransactionTO(TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		dtiTxn.getRequest().setCommandBody(requestTO);
		dtiTxn.setTpRefNum(2222);
		dtiTxn.setProvider(DTITransactionTO.ProviderType.HKDNEXUS);
		try {
			InputStream fileName = this.getClass().getResourceAsStream(DLR_XML_PATH + "DLR_Reservation_Res.xml");
			String xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			DLRReservationRules.transformResponse(dtiTxn, xmlResponse);
		} catch (DTIException dtie) {
			Assert.fail("Unexcpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyDLRReservationRules
	 */
	@Test
	public void testApplyDLRReservationRules() {

		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		ReservationRequestTO requestTO = new ReservationRequestTO();
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("resCode");
		reservation.setResSalesType("resSalesType");
		requestTO.setReservation(reservation);
		requestTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		ClientDataTO clientData = new ClientDataTO();
		clientData.setBillingInfo(getBillingInfo());
		requestTO.setClientData(clientData);
		dtiTxn.setTpRefNum(new Integer(1));
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setDayClass("SHIP");
		dbProductTO.setPdtCode("pdtCode");
		ArrayList<DBProductTO> dbProdListIn = new ArrayList<>();
		dbProdListIn.add(dbProductTO);
		dtiTxn.setDbProdList(dbProdListIn);
		dtiTxn.getRequest().setCommandBody(requestTO);
		dtiTxn.setProvider(DTITransactionTO.ProviderType.DLRGATEWAY);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexcpected Exception" + dtie.getLogMessage());
		}
		DemographicsTO demographicsTO = new DemographicsTO();
		/* scenario :1 FirstName is null */
		demographicsTO.setFirstName(null);
		clientData.setBillingInfo(demographicsTO);
		requestTO.setClientData(clientData);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing first name.", dtie.getLogMessage());
		}
		/* scenario :2 LastName is null */
		demographicsTO.setFirstName("firstName");
		demographicsTO.setLastName(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing last name.", dtie.getLogMessage());
		}
		/* scenario :3 Addr1 is null */
		demographicsTO.setLastName("lastName");
		demographicsTO.setAddr1(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing street address 1.", dtie.getLogMessage());
		}
		/* scenario :4 City is null */
		demographicsTO.setAddr1("Addr1");
		demographicsTO.setCity(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing city.", dtie.getLogMessage());
		}
		/* scenario :5 Zip is null */
		demographicsTO.setCity("city");
		demographicsTO.setZip(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing ZIP.", dtie.getLogMessage());
		}
		/* scenario :6 country is null */
		demographicsTO.setCountry(null);
		demographicsTO.setZip("zip");
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing country.", dtie.getLogMessage());
		}
		/* scenario :7 Telephone is null */
		demographicsTO.setCountry("country");
		demographicsTO.setTelephone(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing phone.", dtie.getLogMessage());
		}
		/* scenario :8 Email is null */
		demographicsTO.setTelephone("123654789");
		demographicsTO.setEmail(null);
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("DLR Reservation missing required billing email.", dtie.getLogMessage());
		}
		/* scenario :9 setting email */
		demographicsTO.setEmail("d@Disney.com");
		clientData.setBillingInfo(demographicsTO);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("Unexcpected Exception", dtie.getLogMessage());
		}
		/* scenario :10 ResSalesType is null */
		reservation.setResSalesType(null);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("Reservation getResSalesType cannot be null.", dtie.getLogMessage());
		}
		/* scenario :11 ResCode is null */
		reservation.setResCode(null);
		try {
			DLRReservationRules.applyDLRReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals("ResCode is not between required lengths (1 - 20) for DLR.", dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformError
	 */
	// @Test TODO
	public void testTransformError() {
		DTITransactionTO dtiTxn = new DTITransactionTO(TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		dtiTxn.setProvider(DTITransactionTO.ProviderType.DLRGATEWAY);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		String statusString = "statusString";
		String xmlResponse = "<Envelope> " + "<Header>" + "<MessageID>290118</MessageID>"
				+ "<MessageType>QueryTicketResponse</MessageType>" + "<SourceID>WDPRODLRNA</SourceID>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp() + "</TimeStamp>" + "<EchoData />" + "<SystemFields />" + "</Header>"
				+ "<Body><Status><StatusCode>1300</StatusCode><StatusText>QueryTicket request error</StatusText></Status>"
				+ "<QueryTicketErrors><Errors><Error><ErrorCode>1306</ErrorCode><ErrorText>Can not process messages from source WDPRODLRNA, source is inactive</ErrorText>"
				+ "</Error></Errors><DataRequestErrors /></QueryTicketErrors></Body></Envelope>";
		try {
			DLRReservationRules.transformError(dtiTxn, dtiRespTO, statusString, xmlResponse);
		} catch (DTIException dtie) {
			Assert.fail("Unexcpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * @param ticketList
	 * @return
	 */
	private void getTicketDetails(ArrayList<TicketTO> ticketList) {
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
	}
}
