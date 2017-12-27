package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest;
import pvt.disney.dti.gateway.request.xsd.ExternalReferenceType;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Agency;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.ClientGroupValidity;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.DemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.DemoData.Bill;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.DemoData.Delivery;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Eligibility;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.AccountDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount.TktID.TktDSSN;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.NewMediaData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ExtTxnIdentifier;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.CreditCard;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.CreditCard.CCManual;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.CreditCard.CCSwipe;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.CreditCard.CCWireless;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.GiftCard;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.GiftCard.GCManual;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.GiftCard.GCSwipe;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.Installment;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Payment.PayType.Voucher;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Reservation;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Show;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.ProdDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.ProdDemoData.TktDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.TktAssignment;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.TktValidity;
import pvt.disney.dti.gateway.request.xsd.Transmission;
import pvt.disney.dti.gateway.request.xsd.Transmission.Payload;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.DemoData.Ship;
import pvt.disney.dti.gateway.test.util.CommonUtil;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the reservation transformations.
 * 
 * @author lewit019
 */
public class ReservationTest extends CommonUtil {

	/**
	 * Sets the up before class.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test getTo.
	 */
	@Test
	public final void testGetTO() {
		DTITransactionTO dtiTxn = null;
		String tktBroker = new String(DTITestUtil.TKTBROKER);
		DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;
		Payload payload = null;
		Payment aPayment = new Payment();
		PayType payType = new PayType();
		ReservationRequest resReq = new ReservationRequest();
		XMLGregorianCalendar xmlGregorianCalendar = null;
		Transmission jaxbReq = new Transmission();
		Ticket aTicket = new Ticket();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		CreditCard creditCard = new CreditCard();
		CCManual cCManual = new CCManual();
		Eligibility eligibility = new Eligibility();
		Reservation reservation = new Reservation();
		ClientData clientData = new ClientData();
		Agency agency = new Agency();
		EntitlementAccount entitlementAccount = new EntitlementAccount();
		ExtTxnIdentifier extTxnIdentifier = new ExtTxnIdentifier();
		Show show = new Show();
		TktValidity tktValidity = new TktValidity();
		ProdDemoData prodDemoData = new ProdDemoData();
		TktDemoData tktDemoData = new TktDemoData();
		TktAssignment tktAssignment = new TktAssignment();
		CCSwipe cCSwipe = new CCSwipe();
		CCWireless cCWireless = new CCWireless();
		GCManual gCManual = new GCManual();
		GiftCard giftCard = new GiftCard();
		GCSwipe gCSwipe = new GCSwipe();
		Voucher voucher = new Voucher();
		Installment installment = new Installment();
		InstallmentCreditCard installmentCreditCard = new InstallmentCreditCard();
		ReservationRequest.Payment.PayType.Installment.InstallmentDemoData installmentDemoData = new ReservationRequest.Payment.PayType.Installment.InstallmentDemoData();
		ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard.CCManual iCCManual = new ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard.CCManual();
		ExistingAccount existingAccount = new ExistingAccount();
		SpecifiedAccount specifiedAccount = new SpecifiedAccount();
		NewMediaData newMediaData = new NewMediaData();
		ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount.TktID rtktID = new ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount.TktID();
		TktDSSN tktDSSN = new TktDSSN();
		ExternalReferenceType newAccount = new ExternalReferenceType();
		ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard.CCSwipe rCCSwipe = new ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard.CCSwipe();
		AccountDemoData accountDemoData = new AccountDemoData();
		ClientGroupValidity clientGroupValidity = new ClientGroupValidity();
		DemoData demoData = new DemoData();
		Bill bill = new Bill();
		Ship ship = new Ship();
		Delivery delivery = new Delivery();
		QName name = new QName("IATA");

		JAXBElement<String> jax1 = new JAXBElement<String>(name, String.class,
				null, tktBroker);

		try {
			payload = CommonUtil.getCommonRequestPayload();
			xmlGregorianCalendar = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e1) {
			Assert.fail(e1.toString());
		}

		agency.getIATAOrAgent().add(jax1);

		tktDemoData.setFirstName("FirstName");
		tktDemoData.setLastName("LastName");
		tktDemoData.setGender("Male");
		tktDemoData.setDateOfBirth(xmlGregorianCalendar);
		tktDemoData.setAddr1("Addr1");
		tktDemoData.setAddr2("Addr2");
		tktDemoData.setCity("City");
		tktDemoData.setState("State");
		tktDemoData.setCountry("Country");
		tktDemoData.setZip("2423535");
		tktDemoData.setTelephone("353565464");
		tktDemoData.setEmail("email@domain.com");
		tktDemoData.setSellerRef("212453");
		tktDemoData.setCellPhone("56654547787");
		tktDemoData.setOptInSolicit("YES");
		prodDemoData.getTktDemoData().add(tktDemoData);
		aTicket.setTktItem(new BigInteger("56345"));
		aTicket.setTktValidity(tktValidity);
		aTicket.setProdCode("21524");
		aTicket.setProdQty(new BigInteger("4524"));
		aTicket.setProdDemoData(prodDemoData);
		aTicket.setProdPrice("125246");
		aTicket.setTktValidity(tktValidity);
		aTicket.getTktAssignment().add(tktAssignment);
		aTicket.setShowGroup("showgrp");
		aTicket.setProdPriceQuoteToken("prodpricequotetoken");
		aPayment.setPayItem("365456");
		cCManual.setCCAuthCode("656");
		cCManual.setCCCAVV("4456");
		cCManual.setCCEcommerce("1453463");
		cCManual.setCCExpiration("24-10-2018");
		cCManual.setCCName("Name");
		cCManual.setCCNbr("656544542123");
		cCManual.setCCPreApproved(true);
		cCManual.setCCSHA1("H1");
		cCManual.setCCStreet("Street");
		cCManual.setCCSubCode("231564");
		cCManual.setCCType("cctype");
		cCManual.setCCVV("545");
		cCManual.setCCZipcode("822116");
		creditCard.setCCManual(cCManual);
		payType.setCreditCard(creditCard);
		aPayment.setPayType(payType);
		aPayment.setPayAmount(444554554.55);
		resReq.setRequestType("reservation");
		resReq.setAPPassInfo("APPASS");
		resReq.getTicket().add(aTicket);
		resReq.setEligibility(eligibility);
		resReq.setReservation(reservation);
		resReq.setAgency(agency);
		bill.setFirstName("Fname");
		bill.setAddr1("Addr1");
		bill.setAddr2("addr2");
		bill.setCity("city");
		bill.setCountry("country");
		bill.setEmail("email@domain.com");
		bill.setFirstNameCH("fnCh");
		bill.setLastName("LastName");
		bill.setName("name");
		bill.setSellerResNbr("3234543");
		bill.setState("state");
		bill.setTelephone("1524562212");
		bill.setZip("5341223");

		ship.setFirstName("Fname");
		ship.setAddr1("Addr1");
		ship.setAddr2("addr2");
		ship.setCity("city");
		ship.setCountry("country");
		ship.setEmail("email@domain.com");
		ship.setFirstNameCH("fnCh");
		ship.setLastName("LastName");
		ship.setName("name");
		ship.setState("state");
		ship.setTelephone("1524562212");
		ship.setZip("5341223");
		ship.setNote("note");
		delivery.setFirstName("Fname");
		delivery.setAddr1("Addr1");
		delivery.setAddr2("addr2");
		delivery.setCity("city");
		delivery.setCountry("country");
		delivery.setEmail("email@domain.com");
		delivery.setLastName("LastName");
		delivery.setName("name");
		delivery.setState("state");
		delivery.setTelephone("1524562212");
		delivery.setZip("5341223");
		demoData.setDelivery(delivery);
		demoData.setBill(bill);
		demoData.setShip(ship);
		clientData.setClientId("54521");
		clientData.setClientCategory("Category");
		clientData.setClientDeliveryInstructions("DInst");
		clientData.setClientFulfillmentMethod("FullfillMethod");
		clientData.setClientGroupValidity(clientGroupValidity);
		clientData.setClientPaymentMethod("paymentmethod");
		clientData.setClientSalesContact("464523546");
		clientData.setClientType("clienttype");
		clientData.setDemoData(demoData);
		clientData.setDemoLanguage("langauage");
		clientData.setTimeRequirement("255646410");
		resReq.setClientData(clientData);
		resReq.setTaxExemptCode("Tax");
		resReq.getNote().add("Note");
		entitlementAccount.setDefaultAccount("AccountPerOrder");
		resReq.setEntitlementAccount(entitlementAccount);
		resReq.setExtTxnIdentifier(extTxnIdentifier);
		show.setShowGroup("showgroup");
		show.setShowPerformance("performance");
		show.setShowQuota("quota");
		resReq.setShow(show);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		cCSwipe.setCCTrack1("track1");
		cCSwipe.setCCTrack2("track2");
		cCSwipe.setCCVV("3211");
		cCSwipe.setExtnlDevSerial("ExtnlDevSerial");
		cCSwipe.setPosTerminal("PosTerminal");
		creditCard.setCCManual(null);
		creditCard.setCCSwipe(cCSwipe);
		payType.setCreditCard(creditCard);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		cCWireless.setCCTrack1("Track1");
		cCWireless.setCCTrack2("Track2");
		cCWireless.setCCVV("454");
		cCWireless.setExtnlDevSerial("ExtnlDevSerial");
		cCWireless.setPosTerminal("PosTerminal");
		creditCard.setCCSwipe(null);
		creditCard.setCCWireless(cCWireless);
		payType.setCreditCard(creditCard);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		gCManual.setGCNbr("544");
		gCManual.setGCStartDate(xmlGregorianCalendar);
		giftCard.setGCManual(gCManual);
		payType.setCreditCard(null);
		payType.setGiftCard(giftCard);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		gCSwipe.setGCTrack1("Track1");
		gCSwipe.setGCTrack2("track2");
		giftCard.setGCManual(null);
		giftCard.setGCSwipe(gCSwipe);
		payType.setGiftCard(giftCard);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		voucher.setMainCode("24545");
		voucher.setUniqueCode("U-35644122");
		payType.setGiftCard(null);
		payType.setVoucher(voucher);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		installmentDemoData.setFirstName("FName");
		installmentDemoData.setMiddleName("MiddleName");
		installmentDemoData.setLastName("LastName");
		installmentDemoData.setDateOfBirth(xmlGregorianCalendar);
		installmentDemoData.setAddr1("Addr1");
		installmentDemoData.setAddr2("Addr2");
		installmentDemoData.setCity("City");
		installmentDemoData.setState("State");
		installmentDemoData.setCountry("Country");
		installmentDemoData.setZip("2423535");
		installmentDemoData.setTelephone("353565464");
		installmentDemoData.setAltTelephone("411353565464");
		installmentDemoData.setEmail("email@domain.com");
		iCCManual.setCCName("CCName");
		iCCManual.setCCNbr("356");
		iCCManual.setCCExpiration("25-02-2017");
		installmentCreditCard.setCCManual(iCCManual);
		installment.setInstallmentCreditCard(installmentCreditCard);
		installment.setInstallmentDemoData(installmentDemoData);
		payType.setVoucher(null);
		payType.setInstallment(installment);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		rCCSwipe.setCCTrack1("Track1");
		rCCSwipe.setCCTrack2("track2");
		installmentCreditCard.setCCManual(null);
		installmentCreditCard.setCCSwipe(rCCSwipe);
		installment.setInstallmentCreditCard(installmentCreditCard);
		payType.setInstallment(installment);
		aPayment.setPayType(payType);
		resReq.getPayment().add(aPayment);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		accountDemoData.setTitle("Mr");
		accountDemoData.setFirstName("firstName");
		accountDemoData.setMiddleName("MiddleName");
		accountDemoData.setLastName("LastName");
		accountDemoData.setDateOfBirth(xmlGregorianCalendar);
		accountDemoData.setAddr1("Addr1");
		accountDemoData.setAddr2("Addr2");
		accountDemoData.setCity("City");
		accountDemoData.setState("State");
		accountDemoData.setCountry("Country");
		accountDemoData.setZip("2423535");
		accountDemoData.setEmail("email@domain.com");
		accountDemoData.setOptInSolicit("YES");
		accountDemoData.setSuffix("Dr");
		existingAccount.setExistingMediaId("mediaId");
		specifiedAccount.setAccountItem(new BigInteger("5456"));
		specifiedAccount.setExistingAccount(existingAccount);
		specifiedAccount.getNewMediaData().add(newMediaData);
		specifiedAccount.setAccountDemoData(accountDemoData);
		entitlementAccount.setDefaultAccount(null);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		rtktID.setMag("Mag");
		existingAccount.setExistingMediaId(null);
		existingAccount.setTktID(rtktID);
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		rtktID.setMag(null);
		rtktID.setBarcode("566463");
		existingAccount.setTktID(rtktID);
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		rtktID.setBarcode(null);
		rtktID.setExternal("external");
		existingAccount.setTktID(rtktID);
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		rtktID.setExternal(null);
		rtktID.setTktNID("ID");
		existingAccount.setTktID(rtktID);
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		tktDSSN.setTktNbr("54564643");
		tktDSSN.setTktSite("site");
		tktDSSN.setTktDate("12-01-2017");
		tktDSSN.setTktStation("station");
		rtktID.setTktNID(null);
		rtktID.setTktDSSN(tktDSSN);
		existingAccount.setTktID(rtktID);
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		existingAccount.setTktID(null);
		existingAccount.setAccountId("accId");
		specifiedAccount.setExistingAccount(existingAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		newAccount.setExternalReferenceType("ExternalReferenceType");
		newAccount.setExternalReferenceValue("ExternalReferenceValue");
		specifiedAccount.setExistingAccount(null);
		specifiedAccount.setNewAccount(newAccount);
		entitlementAccount.getSpecifiedAccount().add(specifiedAccount);
		resReq.setEntitlementAccount(entitlementAccount);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("CREDIT CARD");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("DISNEY GIFT CARD");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("ORGANIZATIONAL CHECK");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("CASHIER'S CHECK");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("MONEY ORDER");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("WIRE TRANSFER");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("PAY AT PICKUP");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("MASTER ACCOUNT");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("TICKET EXCHANGE");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("AccountPerTicket");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}
		clientData.setClientPaymentMethod("AccountPerOrder");
		resReq.setClientData(clientData);
		payload.getCommand().setReservationRequest(resReq);
		jaxbReq.setPayload(payload);
		try {
			dtiTxn = TransmissionRqstXML.getDtiTransactionTo(requestType,
					tktBroker, jaxbReq);
			assertNotNull(dtiTxn);
		} catch (Exception e) {
			Assert.fail("UnExpected Exception Occured : " + e.getMessage());
		}

		/*
		 * // ***** Read the test file. ***** String requestXML = null; String
		 * fileName = new
		 * String("./config/Reservation/Reservation_01_Req_XML.xml");
		 * 
		 * try { requestXML = DTITestUtilities.getXMLfromFile(fileName); } catch
		 * (Exception e) { fail("Unable to read test file: " + fileName); }
		 */

		/*
		 * // ***** Create the test object ***** try {
		 * 
		 * dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType,
		 * tktBroker);
		 * 
		 * if (dtiTxn == null) throw new
		 * DTIException("Null request transfer object returned from parser.");
		 * 
		 * } catch (JAXBException je) {
		 * fail("JAXBException creating the request object: " + je.toString());
		 * } catch (DTIException qe) {
		 * ContextManager.getInstance().removeContext();
		 * fail("DTIException creating the request object: " + qe.toString()); }
		 */
		/*
		 * // ***** Evaluate the test transfer object ***** PayloadHeaderTO
		 * payloadHdrTO = dtiTxn.getRequest().getPayloadHeader(); try {
		 * DTITestUtil.validatePayloadHeaderReqTO(payloadHdrTO); } catch
		 * (Exception e) { fail("Failure in validating payload header: " +
		 * e.toString()); }
		 * 
		 * CommandHeaderTO commandHdrTO =
		 * dtiTxn.getRequest().getCommandHeader(); try {
		 * DTITestUtil.validateCommandHeaderReqTO(commandHdrTO); } catch
		 * (Exception e) { fail("Failure in validating command header: " +
		 * e.toString()); }
		 * 
		 * ReservationRequestTO reservationReqTO = (ReservationRequestTO)
		 * dtiTxn.getRequest() .getCommandBody(); try {
		 * validateReservationReqTO(reservationReqTO); } catch (Exception e) {
		 * fail("Failure in validating reservation request: " + e.toString()); }
		 */
		return;

	}

	/**
	 * Test get to empty.
	 */
	//@Test
	public final void testGetTOEmpty() {

		// ***** Read the test file. *****
		String requestXML = null;
		String fileName = new String(
				"./config/Reservation/Reservation_02_Req_XML.xml");

		try {
			requestXML = DTITestUtilities.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		DTITransactionTO dtiTxn = null;
		String tktBroker = new String(DTITestUtil.TKTBROKER);
		DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;

		// ***** Create the test object *****
		try {

			dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType,
					tktBroker);

			if (dtiTxn == null)
				throw new DTIException(
						"Null request transfer object returned from parser.");

		} catch (JAXBException je) {
			fail("JAXBException creating the request object: " + je.toString());
		} catch (DTIException qe) {
			ContextManager.getInstance().removeContext();
			fail("DTIException creating the request object: " + qe.toString());
		}

		return;

	}

	/**
	 * Test get to parse only.
	 */
	//@Test
	public final void testGetTOParseOnly() {

		// ***** Read the test file. *****
		String requestXML = null;
		String fileName = new String(
				"./config/Reservation/Reservation_03_Req_XML.xml");

		try {
			requestXML = DTITestUtilities.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		DTITransactionTO dtiTxn = null;
		String tktBroker = new String(DTITestUtil.TKTBROKER);
		DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;

		// ***** Create the test object *****
		try {

			dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType,
					tktBroker);

			if (dtiTxn == null)
				throw new DTIException(
						"Null request transfer object returned from parser.");

		} catch (JAXBException je) {
			fail("JAXBException creating the request object: " + je.toString());
		} catch (DTIException qe) {
			ContextManager.getInstance().removeContext();
			fail("DTIException creating the request object: " + qe.toString());
		}

		try {
			FileOutputStream fos = new FileOutputStream(
					"C:\\var\\data\\ResReq.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dtiTxn);
			oos.close();
		} catch (Exception e) {
			fail("Unable to write object to file: " + e.toString());
		}

		return;

	}

	/**
	 * Validate reservation req to.
	 * 
	 * @param rReqTO
	 *            the r req to
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private static void validateReservationReqTO(ReservationRequestTO rReqTO)
			throws Exception {

		// RequestType
		if (rReqTO.getRequestType().compareTo(DTITestUtil.REQUESTTYPE) != 0)
			throw new Exception("Bad RequestType Ticket Value: "
					+ rReqTO.getRequestType());

		// Ticket
		ArrayList<TicketTO> tktListTO = rReqTO.getTktList();
		if (tktListTO.size() != 2)
			throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

		for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

			BigInteger itemNumObj = aTicketTO.getTktItem();
			if (itemNumObj == null)
				throw new Exception("ItemNum is null.");
			int itemNum = itemNumObj.intValue();

			if (itemNum == 1) {

				if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
					throw new Exception("Invalid ProdCode: "
							+ aTicketTO.getProdCode());

				if (aTicketTO.getProdQty().compareTo(
						new BigInteger(DTITestUtil.PRODQTY)) != 0)
					throw new Exception("Invalid ProdQty: "
							+ aTicketTO.getProdQty());

				if (aTicketTO.getProdPrice().compareTo(
						new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
					throw new Exception("Invalid ProdPrice: "
							+ aTicketTO.getProdPrice());

				if (aTicketTO.getExternal().compareTo(DTITestUtil.TKTEXTERNAL) != 0)
					throw new Exception("Invalid TktID.External: "
							+ aTicketTO.getExternal());

				if (aTicketTO.getTktStatusList().size() == 0)
					throw new Exception("Invalid TktStatus (none provided).");

				TktStatusTO tktStatus = aTicketTO.getTktStatusList().get(0);
				if ((tktStatus.getStatusItem()
						.compareTo(DTITestUtil.STATUSITEM) != 0)
						|| (tktStatus.getStatusValue().compareTo(
								DTITestUtil.STATUSVALUE) != 0))
					throw new Exception("Invalid StatusItem or StatusValue");

				GregorianCalendar gCalValidStartTO = aTicketTO
						.getTktValidityValidStart();
				GregorianCalendar gCalValidStartTst = CustomDataTypeConverter
						.parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
				if (gCalValidStartTO.compareTo(gCalValidStartTst) != 0)
					throw new Exception(
							"Invalid TktValidity.ValidStart value: "
									+ CustomDataTypeConverter
											.printCalToDTIDateFormat(gCalValidStartTO));

				GregorianCalendar gCalValidEndTO = aTicketTO
						.getTktValidityValidEnd();
				GregorianCalendar gCalValidEndTst = CustomDataTypeConverter
						.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
				if (gCalValidEndTO.compareTo(gCalValidEndTst) != 0)
					throw new Exception(
							"Invalid TktValidity.ValidEnd value: "
									+ CustomDataTypeConverter
											.printCalToDTIDateFormat(gCalValidEndTO));

				if (DTITestUtil.TKTSECURITYLEVEL.compareTo(aTicketTO
						.getTktSecurityLevel()) != 0)
					throw new Exception("Invalid TktSecurity.");

				if (DTITestUtil.TKTSHELL.compareTo(aTicketTO.getTktShell()) != 0)
					throw new Exception("Invalid TktShell.");

				if (DTITestUtil.TKTMARKET.compareTo(aTicketTO.getTktMarket()) != 0)
					throw new Exception("Invalid TktMarket.");

				if (DTITestUtil.TKTNOTE.compareTo(aTicketTO.getTktNote()) != 0)
					throw new Exception("Invalid TktNote.");

			} else if (itemNum == 2) {

				if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
					throw new Exception("Invalid ProdCode: "
							+ aTicketTO.getProdCode());

				if (aTicketTO.getProdQty().compareTo(
						new BigInteger(DTITestUtil.PRODQTY)) != 0)
					throw new Exception("Invalid ProdQty: "
							+ aTicketTO.getProdQty());

				if (aTicketTO.getProdPrice().compareTo(
						new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
					throw new Exception("Invalid ProdPrice: "
							+ aTicketTO.getProdPrice());

			} else
				throw new Exception("Invalid TktItem: " + itemNum);

		}

		// APPassInfo
		if (rReqTO.getAPPassInfo().compareTo(DTITestUtil.APPASSINFO) != 0)
			throw new Exception("Invalid APPassINfo: " + rReqTO.getAPPassInfo());

		// Payment
		ArrayList<PaymentTO> payListTO = rReqTO.getPaymentList();
		if (payListTO.size() != 5)
			throw new Exception("Invalid PayListTO.size(): " + tktListTO.size());

		for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

			BigInteger itemNumObj = aPaymentTO.getPayItem();
			if (itemNumObj == null)
				throw new Exception("ItemNum is null.");
			int itemNum = itemNumObj.intValue();

			if (itemNum == 1) { // CCManual

				DTITestUtil.validateCCManualPayment(aPaymentTO);

				if (aPaymentTO.getCreditCard().getCcType()
						.compareTo(DTITestUtil.CCTYPE) != 0)
					throw new Exception("Invalid CCType: "
							+ aPaymentTO.getCreditCard().getCcType());

			} else if (itemNum == 2) { // CCSwipe

				DTITestUtil.validateCCSwipePayment(aPaymentTO);

			} else if (itemNum == 3) { // Voucher

				DTITestUtil.validateVoucherPayment(aPaymentTO);

			} else if (itemNum == 4) { // Gift Card Manual

				DTITestUtil.validateGCManualPayment(aPaymentTO);

			} else if (itemNum == 5) { // Gift Card Swipe

				DTITestUtil.validateGCSwipePayment(aPaymentTO);

			} else
				throw new Exception("Invalid PayItem: " + itemNum);
		}

		// Eligibility
		if (rReqTO.getEligibilityGroup().compareTo(DTITestUtil.ELIGGROUP) != 0)
			throw new Exception("Invalid Eligibility.Group: "
					+ rReqTO.getEligibilityGroup());
		if (rReqTO.getEligibilityMember().compareTo(DTITestUtil.ELIGMEMBER) != 0)
			throw new Exception("Invalid Eligibility.Member: "
					+ rReqTO.getEligibilityMember());

		// Reservation
		ReservationTO resTO = rReqTO.getReservation();
		if (resTO == null)
			throw new Exception("Missing Reservation section.");
		if (resTO.getResCode().compareTo(DTITestUtil.RESCODE) != 0)
			throw new Exception("Invalid ResCode.");
		if (resTO.getResNumber().compareTo(DTITestUtil.RESNUMBER) != 0)
			throw new Exception("Invalid ResNumber.");
		GregorianCalendar gCalTestDate = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.RESCREATEDATE);
		if (resTO.getResCreateDate().compareTo(gCalTestDate) != 0)
			throw new Exception("Invalid ResCreateDate.");
		gCalTestDate = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.RESPICKUPDATE);
		if (resTO.getResPickupDate().compareTo(gCalTestDate) != 0)
			throw new Exception("Invalid ResPickupDate.");
		if (resTO.getResPickupArea().compareTo(DTITestUtil.RESPICKUPAREA) != 0)
			throw new Exception("Invalid ResPickupArea.");
		if (resTO.getResSalesType().compareTo(DTITestUtil.RESSALESTYPE) != 0)
			throw new Exception("Invalid ResSalesType.");

		// ClientData
		ClientDataTO cl = rReqTO.getClientData();
		if (cl.getClientId().compareTo(DTITestUtil.CLIENTID) != 0)
			throw new Exception("Invalid ClientId.");
		if (cl.getClientType().compareTo(DTITestUtil.CLIENTTYPE) != 0)
			throw new Exception("Invalid ClientType: " + cl.getClientType());
		if (cl.getClientCategory().compareTo(DTITestUtil.CLIENTCATEGORY) != 0)
			throw new Exception("Invalid ClientCategory: "
					+ cl.getClientCategory());
		if (cl.getDemoLanguage().compareTo(DTITestUtil.DEMOLANGUAGE) != 0)
			throw new Exception("Invalid DemoLanguage: " + cl.getDemoLanguage());

		// Bill Info
		DemographicsTO bill = cl.getBillingInfo();
		if (bill.getAddr1().compareTo(DTITestUtil.BILLADDR1) != 0)
			throw new Exception("Invalid Bill Addr1: " + bill.getAddr1());
		if (bill.getAddr2().compareTo(DTITestUtil.BILLADDR2) != 0)
			throw new Exception("Invalid Bill Addr2: " + bill.getAddr2());
		if (bill.getCity().compareTo(DTITestUtil.BILLCITY) != 0)
			throw new Exception("Invalid Bill City: " + bill.getCity());
		if (bill.getCountry().compareTo(DTITestUtil.BILLCOUNTRY) != 0)
			throw new Exception("Invalid Bill Country: " + bill.getCountry());
		if (bill.getEmail().compareTo(DTITestUtil.BILLEMAIL) != 0)
			throw new Exception("Invalid Bill Email: " + bill.getEmail());
		if (bill.getFirstName().compareTo(DTITestUtil.BILLFIRSTNAME) != 0)
			throw new Exception("Invalid Bill FirstName: "
					+ bill.getFirstName());
		if (bill.getLastName().compareTo(DTITestUtil.BILLLASTNAME) != 0)
			throw new Exception("Invalid Bill LastName: " + bill.getLastName());
		if (bill.getName().compareTo(DTITestUtil.BILLNAME) != 0)
			throw new Exception("Invalid Bill Name: " + bill.getName());
		if (bill.getState().compareTo(DTITestUtil.BILLSTATE) != 0)
			throw new Exception("Invalid Bill State: " + bill.getState());
		if (bill.getTelephone().compareTo(DTITestUtil.BILLTELEPHONE) != 0)
			throw new Exception("Invalid Bill Telephone: "
					+ bill.getTelephone());
		if (bill.getZip().compareTo(DTITestUtil.BILLZIP) != 0)
			throw new Exception("Invalid Bill Zip: " + bill.getZip());
		// Ship Info
		DemographicsTO ship = cl.getShippingInfo();
		if (ship.getAddr1().compareTo(DTITestUtil.SHIPADDR1) != 0)
			throw new Exception("Invalid Ship Addr1: " + ship.getAddr1());
		if (ship.getAddr2().compareTo(DTITestUtil.SHIPADDR2) != 0)
			throw new Exception("Invalid Ship Addr2: " + ship.getAddr2());
		if (ship.getCity().compareTo(DTITestUtil.SHIPCITY) != 0)
			throw new Exception("Invalid Ship City: " + ship.getCity());
		if (ship.getCountry().compareTo(DTITestUtil.SHIPCOUNTRY) != 0)
			throw new Exception("Invalid Ship Country: " + ship.getCountry());
		if (ship.getEmail().compareTo(DTITestUtil.SHIPEMAIL) != 0)
			throw new Exception("Invalid Ship Email: " + ship.getEmail());
		if (ship.getFirstName().compareTo(DTITestUtil.SHIPFIRSTNAME) != 0)
			throw new Exception("Invalid Ship FirstName: "
					+ ship.getFirstName());
		if (ship.getLastName().compareTo(DTITestUtil.SHIPLASTNAME) != 0)
			throw new Exception("Invalid Ship LastName: " + ship.getLastName());
		if (ship.getName().compareTo(DTITestUtil.SHIPNAME) != 0)
			throw new Exception("Invalid Ship Name: " + ship.getName());
		if (ship.getState().compareTo(DTITestUtil.SHIPSTATE) != 0)
			throw new Exception("Invalid Ship State: " + ship.getState());
		if (ship.getTelephone().compareTo(DTITestUtil.SHIPTELEPHONE) != 0)
			throw new Exception("Invalid Ship Telephone: "
					+ ship.getTelephone());
		if (ship.getZip().compareTo(DTITestUtil.SHIPZIP) != 0)
			throw new Exception("Invalid Ship Zip: " + ship.getZip());

		// Agency
		AgencyTO agencyTO = rReqTO.getAgency();
		if (agencyTO.getIATA().compareTo(DTITestUtil.IATA) != 0)
			throw new Exception("Invalid Agency.IATA: " + agencyTO.getIATA());
		if (agencyTO.getAgent().compareTo(DTITestUtil.AGENT) != 0)
			throw new Exception("Invalid Agency.Agent: " + agencyTO.getAgent());

		// TaxExemptCode
		String taxExemptCode = rReqTO.getTaxExemptCode();
		if (taxExemptCode.compareTo(DTITestUtil.TAXEXEMPTCODE) != 0)
			throw new Exception("Invalid TaxExemptCode: " + taxExemptCode);

		// Note
		ArrayList<String> noteList = rReqTO.getNoteList();
		if (noteList.size() != 3)
			throw new Exception("Invalid length of note list array: "
					+ noteList.size());
		for /* each */(String aNote : /* in */noteList) {

			if (aNote.compareTo(DTITestUtil.NOTE) != 0)
				throw new Exception("Invalid Note: " + aNote);
		}

		return;
	}

	/**
	 * Test get jaxb.
	 */
	//@Test
	public final void testGetJaxb() {
		/*
		 * // ***** Read the test file. ***** String baselineXML = null; String
		 * fileName = new String(
		 * "./config/Reservation/Reservation_01_Rsp_XML.xml");
		 * 
		 * try { baselineXML = DTITestUtilities.getXMLfromFile(fileName); }
		 * catch (Exception e) { fail("Unable to read test file: " + fileName);
		 * }
		 */

		// ***** Read the test file. *****
		InputStream fileName = null;
		fileName = this.getClass().getResourceAsStream(
				RXML_XML_PATH + "Reservation_01_Rsp_XML.xml");
		// ***** Get the XML from file *****
		String baselineXML = null;
		baselineXML = DTITestUtilities.getXMLFromFile(fileName);
		DTITransactionTO dtiTxn = createReservationRespTO();
		String newXML = null;

		try {

			newXML = TransmissionRespXML.getXML(dtiTxn);

		} catch (JAXBException je) {

			fail("JAXBException creating the response XML: " + je.toString());
		}

		// Validate the XML Headers (Payload & Command)
		try {
			DTITestUtil.validateXMLHeaders(baselineXML, newXML);
		} catch (Exception e) {
			fail("Exception validating response XML headers: " + e.toString());
		}

		// Validate Reservation Response
		String resRespBaseline = DTITestUtilities.findTag(baselineXML,
				"ReservationResponse");
		String resRespNew = DTITestUtilities.findTag(newXML,
				"ReservationResponse");

		try {
			DTITestUtilities.compareXML(resRespBaseline, resRespNew,
					"ReservationResponse");
		} catch (Exception e) {
			fail("Exception validating ReservationResponse section: "
					+ e.toString());
		}

	}

	/**
	 * Creates the reservation resp to.
	 * 
	 * @return the dTI transaction to
	 */
	private static DTITransactionTO createReservationRespTO() {

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Payload Header
		PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
		dtiRespTO.setPayloadHeader(payHdrTO);

		// Command Header
		CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
		dtiRespTO.setCommandHeader(cmdHdrTO);

		// ReservationResponse
		ReservationResponseTO rsRespTO = new ReservationResponseTO();
		rsRespTO.setResponseType(DTITestUtil.RESPONSETYPE);

		// Ticket 1
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("1"));
		aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
		aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
		GregorianCalendar dssnDate = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
		aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE,
				DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
		aTicketTO.setTktNID(DTITestUtil.TKTNID);
		aTicketTO.setExternal(DTITestUtil.TKTEXTERNAL);
		aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
		aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));

		TicketTransactionTO tktTranIn = new TicketTransactionTO();
		tktTranIn.setTktProvider(DTITestUtil.TKTPROVIDER);
		dssnDate = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
		tktTranIn.setDssn(dssnDate, DTITestUtil.TKTTRANSITE,
				DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
		tktTranIn.setTranNID(DTITestUtil.TKTTRANNID);
		aTicketTO.setTktTran(tktTranIn);

		TktStatusTO tktStatus = aTicketTO.new TktStatusTO();
		tktStatus.setStatusItem(DTITestUtil.STATUSITEM);
		tktStatus.setStatusValue(DTITestUtil.STATUSVALUE);
		aTicketTO.addTicketStatus(tktStatus);

		GregorianCalendar validStart = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
		aTicketTO.setTktValidityValidStart(validStart);
		GregorianCalendar validEnd = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
		aTicketTO.setTktValidityValidEnd(validEnd);
		aTicketTO.setTktNote(DTITestUtil.TKTNOTE);
		rsRespTO.addTicket(aTicketTO);

		// Ticket 2
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("2"));
		aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
		dssnDate = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
		aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE,
				DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
		aTicketTO.setTktNID(DTITestUtil.TKTNID);
		aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
		aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));
		validStart = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
		aTicketTO.setTktValidityValidStart(validStart);
		validEnd = CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
		aTicketTO.setTktValidityValidEnd(validEnd);
		rsRespTO.addTicket(aTicketTO);

		// Product 1
		ProductTO aProduct = new ProductTO();
		aProduct.setProdItem(new BigInteger("1"));
		aProduct.setProdCode(DTITestUtil.PRODCODE);
		aProduct.setProdQty(new BigInteger(DTITestUtil.PRODQTY));
		aProduct.setProdTax1(new BigDecimal(DTITestUtil.PRODTAX1));
		aProduct.setProdTax2(new BigDecimal(DTITestUtil.PRODTAX2));
		aProduct.setProdPrice(new BigDecimal(DTITestUtil.PRODPRICE));
		aProduct.setProdParts(DTITestUtil.PRODPARTS);
		aProduct.setProdDescription(DTITestUtil.PRODDESC);
		aProduct.setProdReceiptMsg(DTITestUtil.PRODRECEIPTMSG);
		rsRespTO.addProduct(aProduct);

		// Payment 1
		PaymentTO aPaymentTO = new PaymentTO();
		aPaymentTO.setPayItem(new BigInteger("1"));
		CreditCardTO cc = new CreditCardTO();
		cc.setCcAuthCode(DTITestUtil.CCAUTHCODE);
		cc.setCcAuthNumber(DTITestUtil.CCAUTHNUMBER);
		cc.setCcAuthSysResponse(DTITestUtil.CCAUTHSYSRESPONSE);
		cc.setCcNumber(DTITestUtil.CCNUMBER);
		aPaymentTO.setCreditCard(cc);
		rsRespTO.addPayment(aPaymentTO);

		// Payment 2
		aPaymentTO = new PaymentTO();
		aPaymentTO.setPayItem(new BigInteger("2"));
		GiftCardTO gc = new GiftCardTO();
		gc.setGcAuthCode(DTITestUtil.GCAUTHCODE);
		gc.setGcAuthNumber(DTITestUtil.GCAUTHNUMBER);
		gc.setGcAuthSysResponse(DTITestUtil.GCAUTHSYSRESPONSE);
		gc.setGcNumber(DTITestUtil.GCNUMBER);
		gc.setGcRemainingBalance(new BigDecimal(DTITestUtil.GCREMAININGBALANCE));
		gc.setGcPromoExpDate(CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.GCPROMOEXPDATE));
		aPaymentTO.setGiftCard(gc);
		rsRespTO.addPayment(aPaymentTO);

		// Receipt Message
		rsRespTO.setReceiptMessage(DTITestUtil.RECEIPTMESSAGE);

		// Reservation
		ReservationTO rs = new ReservationTO();
		rs.setResCode(DTITestUtil.RESCODE);
		rs.setResNumber(DTITestUtil.RESNUMBER);
		rs.setResCreateDate(CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.RESCREATEDATE));
		rs.setResPickupDate(CustomDataTypeConverter
				.parseYYYYMMDDDate(DTITestUtil.RESPICKUPDATE));
		rs.setResPickupArea(DTITestUtil.RESPICKUPAREA);
		rs.setResSalesType(DTITestUtil.RESSALESTYPE);
		rsRespTO.setReservation(rs);

		// ClientData
		ClientDataTO cd = new ClientDataTO();
		cd.setClientId(DTITestUtil.CLIENTID);
		rsRespTO.setClientData(cd);

		dtiRespTO.setCommandBody(rsRespTO);

		// Complete the response
		DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(
				DTITestUtil.TKTERRORCODE), DTITestUtil.TKTERRORCLASS,
				DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
		dtiRespTO.setDtiError(tktErrorTO);
		DTITransactionTO dtiTxnTO = new DTITransactionTO(
				DTITransactionTO.TransactionType.RESERVATION);
		dtiTxnTO.setResponse(dtiRespTO);

		return dtiTxnTO;
	}

}
