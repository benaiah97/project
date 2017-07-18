package pvt.disney.dti.gateway.rules.dlr;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO.CmdAttributeTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * @author MISHP012 JUnit for DLRBusinessRules
 */
public class DLRBusinessRulesTestCase extends CommonTestUtils {
	
	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormat() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		getTicketDetails(ticketList);
		queryTicketRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(queryTicketRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		String xmlRequest = null;
		/* scenario :1 TransactionType is QUERYTICKET */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
			;
		}
	}

	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormatUpdate() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO alphaRequestTO = new UpgradeAlphaRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		getTicketDetails(ticketList);
		alphaRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(alphaRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		PaymentLookupTO lookupTO = new PaymentLookupTO();
		lookupTO.setPymtCode("2");
		lookupTO.setLookupValue("3");
		ArrayList<PaymentLookupTO> paymentLookupTOList = new ArrayList<>();
		paymentLookupTOList.add(lookupTO);
		dtiTxn.setPaymentLookupTOList(paymentLookupTOList);
		EntityTO entityTO = new EntityTO();
		entityTO.setCustomerId("2");
		dtiTxn.setEntityTO(entityTO);
		String xmlRequest = null;
		/* scenario :1 TransactionType is UPGRADEALPHA */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormatVoid() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO voidTicketRequestTO = new VoidTicketRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		getTicketDetails(ticketList);
		voidTicketRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(voidTicketRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		EntityTO entityTO = new EntityTO();
		entityTO.setCustomerId("2");
		dtiTxn.setEntityTO(entityTO);
		String xmlRequest = null;
		/* scenario :1 TransactionType is VOIDTICKET */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormatReservation() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RESERVATION);
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
		clientData.setBillingInfo(getBillingInfo());
		reservationRequestTO.setClientData(clientData);
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
		tpLookupTO.setLookupValue("details");
		TPLookupTO tpLook = new TPLookupTO();
		tpLook.setLookupType(TPLookupTO.TPLookupType.SHIP_DETAIL);
		tpLook.setLookupValue("lookupValue");
		ArrayList<TPLookupTO> tpLookupTOListIn = new ArrayList<>();
		tpLookupTOListIn.add(tpLookupTO);
		tpLookupTOListIn.add(tpLook);
		dtiTxn.setTpLookupTOList(tpLookupTOListIn);
		String xmlRequest = null;
		/* scenario :1 TransactionType is RESERVATION */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormatQueryReservation() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYRESERVATION);
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO queryReservationRequestTO = new QueryReservationRequestTO();
		queryReservationRequestTO.setResCode("3");
		dtiTxn.getRequest().setCommandBody(queryReservationRequestTO);
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
		String xmlRequest = null;
		/* scenario :1 TransactionType is QUERYRESERVATION */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for changeToDLRProviderFormat
	 */
	@Test
	public void testChangeToDLRProviderFormatQueryRenewentitlement() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn);
		RenewEntitlementRequestTO entitlementRequestTO = new RenewEntitlementRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
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
			ticket.setExistingTktID(ticket);
		}
		getTicketDetails(ticketList);
		entitlementRequestTO.setTktList(ticketList);
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("22");
		reservation.setResPickupDate(new GregorianCalendar());
		entitlementRequestTO.setReservation(reservation);
		ClientDataTO clientData = new ClientDataTO();
		clientData.setBillingInfo(getBillingInfo());
		entitlementRequestTO.setClientData(clientData);
		dtiTxn.getRequest().setCommandBody(entitlementRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		EntityTO entityTO = new EntityTO();
		entityTO.setCustomerId("2");
		dtiTxn.setEntityTO(entityTO);
		String xmlRequest = null;
		/* scenario :1 TransactionType is RENEWENTITLEMENT */
		try {
			xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
			Assert.assertNotNull(xmlRequest);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/*
		 * scenario :2 TransactionType is UNDEFINED expected exception is
		 * Invalid DLR transaction type sent to DTI Gateway. Unsupported.
		 */
		dtiTxn = new DTITransactionTO(TransactionType.UNDEFINED);
		try {
			DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Invalid DLR transaction type sent to DTI Gateway.  Unsupported.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for changeDLRProviderFormatToDti
	 */
	@Test
	public void testChangeDLRProviderFormatToDtiQuery()
			throws URISyntaxException, FileNotFoundException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		getTicketDetails(ticketList);
		queryTicketRequestTO.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(queryTicketRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiTxn.setProvider(DTITransactionTO.ProviderType.DLRGATEWAY);
		// CommandHeaderTO header = new CommandHeaderTO();
		CmdAttributeTO cmdAttributeTO = dtiTxn.getRequest().getCommandHeader().new CmdAttributeTO();
		cmdAttributeTO.setAttribValue("1");
		ArrayList<CmdAttributeTO> cmdAttributeList = new ArrayList<>();
		cmdAttributeList.add(cmdAttributeTO);
		dtiTxn.getRequest().getCommandHeader()
				.setCmdAttributeList(cmdAttributeList);
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* scenario :1 TransactionType is QUERYTICKET */
		String xmlResponse = "";
		InputStream fileName = this.getClass().getResourceAsStream(
				DLR_XML_PATH + "DLR_QueryTicket_01_Rsp.xml");
		DTITransactionTO dtiTransactionTO = null;
		try {
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* scenario :2 TransactionType is UPGRADEALPHA */
		dtiTxn = new DTITransactionTO(TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO alphaRequestTO = new UpgradeAlphaRequestTO();
		alphaRequestTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(alphaRequestTO);
		dtiTxn.setTpRefNum(2222);
		dtiTransactionTO = null;

		try {
			fileName = this.getClass().getResourceAsStream(DLR_XML_PATH
									+ "DLR_UpgradeAlphaResponse_01_TestTransformResponse.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		dtiTxn = new DTITransactionTO(TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO queryReq = new VoidTicketRequestTO();
		TicketTO ticketTO = new TicketTO();
		ArrayList<TicketTO> arrTicketTO = new ArrayList<TicketTO>();
		arrTicketTO.add(ticketTO);
		queryReq.setTktList(arrTicketTO);
		dtiTxn.getRequest().setCommandBody(queryReq);
		dtiTxn.setTpRefNum(222);
		dtiTransactionTO = null;
		/* scenario :3 TransactionType is VOIDTICKET */

		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_VoidTicketResponse.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		dtiTxn = new DTITransactionTO(TransactionType.QUERYRESERVATION);
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO queryResReq = new QueryReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(queryResReq);
		dtiTxn.setTpRefNum(2222);
		dtiTransactionTO = null;
		/* scenario :4 TransactionType is QUERYRESERVATION */

		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_QueryReservation_01_Rsp.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		dtiTxn = new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn);
		RenewEntitlementRequestTO renewRequestTO = new RenewEntitlementRequestTO();
		dtiTxn.getRequest().setCommandBody(renewRequestTO);
		dtiTxn.setTpRefNum(2222);
		dtiTransactionTO = null;
		/* scenario :5 TransactionType is RENEWENTITLEMENT */
		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_RenewEntitlement_Res_01.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		ReservationRequestTO requestTO = new ReservationRequestTO();
		ReservationTO reservation = new ReservationTO();
		reservation.setResCode("resCode");
		requestTO.setReservation(reservation);
		dtiTxn = new DTITransactionTO(TransactionType.RESERVATION);
		createCommonRequest(dtiTxn);
		dtiTxn.getRequest().setCommandBody(requestTO);
		dtiTxn.setTpRefNum(2222);
		dtiTransactionTO = null;
		dtiTxn.setProvider(DTITransactionTO.ProviderType.HKDNEXUS);
		/* scenario :6 TransactionType is RESERVATION */
		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_Reservation_Res.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
			Assert.assertNotNull(dtiTransactionTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		dtiTxn = new DTITransactionTO(TransactionType.UNDEFINED);
		dtiTxn.setTpRefNum(2222);
		/*
		 * scenario :7 TransactionType is UNDEFINED expected exception is
		 * Invalid DLR transaction type sent to Java version of DTI Gateway.
		 * Unsupported.
		 */

		try {
			fileName = this.getClass().getResourceAsStream(
					DLR_XML_PATH + "DLR_Reservation_Res.xml");
			xmlResponse = DTITestUtilities.getXMLFromFile(fileName);
			dtiTransactionTO = DLRBusinessRules.changeDLRProviderFormatToDti(
					dtiTxn, xmlResponse);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Invalid DLR transaction type sent to Java version of DTI Gateway.  Unsupported.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for validateInBoundDLRTickets
	 */
	@Test
	public void testValidateInBoundDLRTickets() {
		ArrayList<TicketTO> aTktList = new ArrayList<>();
		TicketTO ticketTO = new TicketTO();
		aTktList.add(ticketTO);
		/*
		 * scenario :1 TicketTypes >=1 expected exception is In-bound dLR txn
		 * with <> 1 TktId: 0
		 */
		try {
			DLRBusinessRules.validateInBoundDLRTickets(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals("In-bound dLR txn with <> 1 TktId: 0",
					dtie.getLogMessage());
		}
		/*
		 * scenario :2 TicketIdType is EXTERNAL_ID expected exception is
		 * In-bound dLR txn with an invalid TicketIdType: BARCODE_ID
		 */
		ticketTO.setBarCode("Bar");
		try {
			DLRBusinessRules.validateInBoundDLRTickets(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"In-bound dLR txn with an invalid TicketIdType: BARCODE_ID",
					dtie.getLogMessage());
		}
		/*
		 * scenario :3 EXTERNAL_ID length is >40 expected exception is In-bound
		 * DLR txn with invalid External (VisualID) length: 44
		 */
		ticketTO.setBarCode(null);
		ticketTO.setExternal("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		try {
			DLRBusinessRules.validateInBoundDLRTickets(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"In-bound DLR txn with invalid External (VisualID) length: 44",
					dtie.getLogMessage());
		}
		/* scenario :4 aTktList size is 0 */
		aTktList = new ArrayList<>();
		try {
			DLRBusinessRules.validateInBoundDLRTickets(aTktList);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
		/* scenario :5 EXTERNAL_ID length is < 40 */
		ticketTO = new TicketTO();
		aTktList = new ArrayList<>();
		aTktList.add(ticketTO);
		ticketTO.setExternal("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		try {
			DLRBusinessRules.validateInBoundDLRTickets(aTktList);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for applyBusinessRules
	 */
	@Test
	public void testApplyBusinessRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		try {
			DLRBusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			dtie.getLogMessage();
		}
	}

	/**
	 * @param ticketList
	 * @return
	 */
	private ArrayList<TicketTO> getTicketDetails(ArrayList<TicketTO> ticketList) {
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
		return ticketList;
	}
	
}