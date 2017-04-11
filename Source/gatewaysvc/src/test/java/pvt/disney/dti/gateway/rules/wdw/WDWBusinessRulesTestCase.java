package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.fail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;

/**
 * Tests the WDW specific business rules.
 * 
 * @author lewit019
 * 
 */
public class WDWBusinessRulesTestCase extends CommonTestUtils {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		setMockProperty();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Validates enforcement of the following rules: <BR>
	 * 1. If the TicketIdType is MAG, then it must be 73 characters long with an
	 * 'F' in position 37. <BR>
	 * 2. If the TicketIdType is DSSN, then all four components must be filled
	 * out. <BR>
	 * 3. If the TicketIdType is TKTNID, then it must be 17 characters long. <BR>
	 * 4. If the TicketIdType is BARCODE, then it must be 20 characters long
	 * (new). <BR>
	 * 5. There may only be one TicketIdType per in-bound ticket.
	 */
	@Test
	public final void testValidateInBoundWDWTickets() {

		ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();

		/* scenario : 1 Mag < 73 */
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setMag("NOTLONGENOUGH");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with invalid Mag length: 13");
		} catch (DTIException dtie) {
			Assert.assertEquals("In-bound WDW txn with invalid Mag length: 13",
					dtie.getLogMessage());
		}
		/* scenario : 2 Valid Mag Stripe */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO
				.setMag(" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB ");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception on Test 2: Valid Mag Stripe:"
					+ dtie.getLogMessage());
		}
		/* scenario : 3 No F in position 38. */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO
				.setMag(" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAXBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB ");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with invalid midpoint char: X");
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"In-bound WDW txn with invalid midpoint char: X",
					dtie.getLogMessage());
		}
		/* scenario : 4 Valid DSSN */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setDssn((GregorianCalendar) GregorianCalendar.getInstance(),
				"site", "station", "33");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules
					.validateInBoundWDWTickets(getTicketList(TicketIdType.MAG_ID));
		} catch (DTIException dtie) {
			fail("Unexpected exception on  Valid DSSN: "
					+ dtie.getLogMessage());
		}
		/* scenario : 5 Invalid DSSN */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setDssn((GregorianCalendar) GregorianCalendar.getInstance(),
				"", "station", "number");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with invalid DSSN.");
		} catch (DTIException dtie) {
			Assert.assertEquals("In-bound WDW txn with invalid DSSN.",
					dtie.getLogMessage());
		}
		/* scenario : 6 Valid TktNID */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setTktNID("12345678901234567");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception on Valid TktNID: "
					+ dtie.getLogMessage());
		}
		/* scenario : 7 Invalid TktNID */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setTktNID("123456789012345678");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with invalid TktNID length: 18");
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"In-bound WDW txn with invalid TktNID length: 18",
					dtie.getLogMessage());
		}
		/* scenario : 8 Valid Barcode */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setBarCode("12345678901234567890");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception on  Valid Barcode:"
					+ dtie.getLogMessage());
		}
		/* scenario : 9 Invalid TktNID */
		aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setBarCode("1234567890123456789");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with invalid Barcode length: 19");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"In-bound WDW txn with invalid Barcode length: 19",
					dtie.getLogMessage());
		}
		/*
		 * scenario : 10 Two ticket Id's on the same ticket
		 */aTktList = new ArrayList<TicketTO>();
		aTicketTO = new TicketTO();
		aTicketTO.setBarCode("12345678901234567890");
		aTicketTO.setTktNID("12345678901234567");
		aTktList.add(aTicketTO);
		try {
			WDWBusinessRules.validateInBoundWDWTickets(aTktList);
			fail("In-bound WDW txn with <> 1 TktId: 2");
		} catch (DTIException dtie) {
			Assert.assertEquals("In-bound WDW txn with <> 1 TktId: 2",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test validate void ticket actor.
	 */
	@Test
	public final void testValidateVoidTicketActor() {

		CommandHeaderTO cmdHeader = new CommandHeaderTO();
		/* scenario : 1 No actor in CmdHeader */
		try {
			WDWBusinessRules.validateVoidTicketActor(cmdHeader);
			fail("No CmdActor tag provided where required.");
		} catch (DTIException dtie) {
			Assert.assertEquals("No CmdActor tag provided where required.",
					dtie.getLogMessage());
		}
		/* scenario : 2 Actor but invalid value */
		cmdHeader.setCmdActor("BOB");
		try {
			WDWBusinessRules.validateVoidTicketActor(cmdHeader);
			fail("CmdActor tag of BOB not authorized for void (MGR or SYS only");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"CmdActor tag of BOB not authorized for void (MGR or SYS only)",
					dtie.getLogMessage());
		}
		/* scenario : 3 MGR value */
		cmdHeader.setCmdActor("MGR");
		try {
			WDWBusinessRules.validateVoidTicketActor(cmdHeader);
		} catch (DTIException dtie) {
			fail("Unexpected exception in  MGR value:"
					+ dtie.getLogMessage());
		}
		/* scenario : 4 SYS value */
		cmdHeader.setCmdActor("SYS");
		try {
			WDWBusinessRules.validateVoidTicketActor(cmdHeader);
		} catch (DTIException dtie) {
			fail("Unexpected exception in  SYS value:"
					+ dtie.getLogMessage());
		}
	}

	/**
	 * Test validate ticket order shells.
	 */
	@Test
	public final void testValidateTicketOrderShells() {

		ArrayList<TicketTO> aTktListTO = new ArrayList<TicketTO>();
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("1"));
		aTicketTO.setMag("BOB", "12=123456");
		aTktListTO.add(aTicketTO);
		/* scenario : 1 Valid shell number */
		try {
			WDWBusinessRules.validateTicketOrderShells(aTktListTO);
		} catch (DTIException dtie) {
			fail("Unexpected exception in  Valid shell number: "
					+ dtie.getLogMessage());
		}
		/* scenario : 2 Empty shell number */
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("2"));
		aTicketTO.setMag("BOB", "=123456");
		aTktListTO.add(aTicketTO);
		try {
			WDWBusinessRules.validateTicketOrderShells(aTktListTO);
			fail("Ticket item 2 Mag Track 2 contains non-numeric shell number:");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 2 Mag Track 2 contains non-numeric shell number: ",
					dtie.getLogMessage());
		}
		/* scenario : 3 Non numeric shell number */
		aTktListTO.clear();
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("1"));
		aTicketTO.setMag("BOB", "ABC=123456");
		aTktListTO.add(aTicketTO);
		try {
			WDWBusinessRules.validateTicketOrderShells(aTktListTO);
			fail("Ticket item 1 Mag Track 2 contains non-numeric shell number: ABC");
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Ticket item 1 Mag Track 2 contains non-numeric shell number: ABC",
					dtie.getLogMessage());
		}
		/* scenario : 4 Shell number too large */
		aTktListTO.clear();
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("1"));
		aTicketTO.setMag("BOB", "1000000=123456");
		aTktListTO.add(aTicketTO);
		try {
			WDWBusinessRules.validateTicketOrderShells(aTktListTO);
			fail("Ticket item 1 Mag Track 2 contains invalid shell number: 1000000");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 1 Mag Track 2 contains invalid shell number: 1000000",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test validate ticket shell active.
	 */
	@Test
	public final void testValidateTicketShellActive() {

		HashSet<Integer> orderShells = new HashSet<Integer>();
		ArrayList<Integer> activeShells = null;
		/* scenario : 1 No order shells */
		try {
			WDWBusinessRules.validateTicketShellActive(orderShells,
					activeShells);
		} catch (DTIException dtie) {
			fail("Unexpected exception on  No order shells: "
					+ dtie.getLogMessage());
		}
		/* scenario : 2 No active shells */
		orderShells.add(new Integer(23));
		try {
			WDWBusinessRules.validateTicketShellActive(orderShells,
					activeShells);
			fail("No shells on ticket order are known to the database..");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"No shells on ticket order are known to the database.",
					dtie.getLogMessage());
		}
		/* scenario : 3 Matching shells */
		activeShells = new ArrayList<Integer>();
		activeShells.add(new Integer(23));
		try {
			WDWBusinessRules.validateTicketShellActive(orderShells,
					activeShells);
		} catch (DTIException dtie) {
			fail("Unexpected exception on  Matching shells: "
					+ dtie.getLogMessage());
		}
		/* scenario : 4 Nonmatching shells */
		orderShells.add(new Integer(42));
		activeShells.add(new Integer(41));
		try {
			WDWBusinessRules.validateTicketShellActive(orderShells,
					activeShells);
			fail("Shell 42 is not known or is not active in the database.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Shell 42 is not known or is not active in the database.",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test validate ticket shell to product.
	 */
	@Test
	public final void testValidateTicketShellToProduct() {

		ArrayList<TicketTO> aTktListTO = new ArrayList<TicketTO>();
		HashMap<String, ArrayList<Integer>> prodShellsXRef = new HashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> aListOfGoodShells = new ArrayList<Integer>();
		aListOfGoodShells.add(new Integer(1));
		aListOfGoodShells.add(new Integer(2));
		aListOfGoodShells.add(new Integer(98));
		aListOfGoodShells.add(new Integer(99));
		prodShellsXRef.put("AAA01", aListOfGoodShells);
		/* scenario : 1 No shell on tickets */
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("1"));
		aTicketTO.setMag("BOB");
		aTktListTO.add(aTicketTO);
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("2"));
		aTicketTO.setBarCode("IMABARCODE");
		aTktListTO.add(aTicketTO);
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("3"));
		aTicketTO.setMag("BOB", "IAMMEALPLANINFO");
		aTktListTO.add(aTicketTO);
		try {
			WDWBusinessRules.validateTicketShellToProduct(aTktListTO,
					prodShellsXRef);
		} catch (DTIException dtie) {
			fail("Unexpected exception on  No shell on tickets:"
					+ dtie.getLogMessage());
		}
		/* scenario : 2 Good product shell mapping */
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("4"));
		aTicketTO.setMag("BOB", "98=123456");
		aTicketTO.setProdCode("AAA01");
		aTktListTO.add(aTicketTO);

		try {
			WDWBusinessRules.validateTicketShellToProduct(aTktListTO,
					prodShellsXRef);
		} catch (DTIException dtie) {
			fail("Unexpected exception on  Good product shell mapping:"
					+ dtie.getLogMessage());
		}
		/* scenario : 3 Invalid product shell mapping */
		aTicketTO = new TicketTO();
		aTicketTO.setTktItem(new BigInteger("5"));
		aTicketTO.setMag("BOB", "97=123456");
		aTicketTO.setProdCode("AAA01");
		aTktListTO.add(aTicketTO);
		try {
			WDWBusinessRules.validateTicketShellToProduct(aTktListTO,
					prodShellsXRef);
			fail("Expected exception on Invalid product shell mapping.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Shell 97 not associated with product AAA01 in the database.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for transformInstallmentDemoData
	 */
	@Test
	public void testTransformInstallmentDemoData() {

		InstallmentDemographicsTO installDemoTO = new InstallmentDemographicsTO();
		installDemoTO.setMiddleName("middleName");
		installDemoTO.setDateOfBirth(new GregorianCalendar());
		installDemoTO.setAddr2("addr2");
		installDemoTO.setAltTelephone("123654789");
		OTFieldTO fieldTO = new OTFieldTO(1, "fieldValueIn");
		ArrayList<OTFieldTO> otDemoList = new ArrayList<>();
		otDemoList.add(fieldTO);
		WDWBusinessRules
				.transformInstallmentDemoData(installDemoTO, otDemoList);
	}

	/**
	 * JUnit for createOTPaymentList
	 */
	@Test
	public void testCreateOTPaymentList() {

		ArrayList<OTPaymentTO> otPaymentList = new ArrayList<>();
		ArrayList<PaymentTO> dtiPayList = new ArrayList<>();
		EntityTO entityTO = new EntityTO();
		entityTO.setDefPymtId(3);
		entityTO.setDefPymtData("defPymtData");
		entityTO.setECommerceValue("commerceValue");
		/* scenario : 1 dtiPayList size is Zero */
		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario : 2 Paytype is creditCard */
		PaymentTO paymentTO = new PaymentTO();
		CreditCardTO creditCard = new CreditCardTO();
		getCreditCard(creditCard);
		dtiPayList.add(paymentTO);
		paymentTO.setCreditCard(creditCard);
		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario : 3 Paytype is giftCard */
		GiftCardTO giftCard = new GiftCardTO();
		giftCard.setGcNbr("123654");
		paymentTO.setGiftCard(giftCard);
		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		InstallmentTO installment = new InstallmentTO();
		installment.setForRenewal(true);
		/* scenario : 4 installmentcreditcard type is CCMANUAL */
		InstallmentCreditCardTO creditCardTO = new InstallmentCreditCardTO();
		creditCardTO.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		creditCardTO.setCcExpiration("ccExpiration");
		installment.setCreditCard(creditCardTO);
		paymentTO.setInstallment(installment);
		paymentTO.setPayAmount(new BigDecimal("1236547"));
		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
		/* scenario : 5 installmentcreditcard type is CCSWIPE */
		creditCardTO.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCSWIPE);
		creditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCSWIPE);
		creditCard.setPosTermID("236589");
		creditCard.setExtnlDevSerial("extnlDevSerial");
		paymentTO.setCreditCard(creditCard);
		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);
	}

	/**
	 * @param creditCard
	 */
	private void getCreditCard(CreditCardTO creditCard)

	{
		creditCard.setCcManualOrSwipe(CreditCardTO.CreditCardType.CCMANUAL);
		creditCard.setCcExpiration("2010");
		creditCard.setCcVV("123654");
		creditCard.setCcCAVV("123658");
		creditCard.setPreApprovedCC(true);
		creditCard.setCcAuthCode("1236");
		creditCard.setCcSha1("sha1");
		creditCard.setCcSubCode("ccSubCode");
		creditCard.setCcEcommerce("ccEcommerce");
	}

	/**
	 * JUnit for transformEntitlementExternalReferenceType
	 */
	@Test
	public void testTransformEntitlementExternalReferenceType() {
		WDWBusinessRules.transformEntitlementExternalReferenceType("XBANDID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("GXP_LINK_ID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("XBAND_EXTERNAL_NUMBER");
		WDWBusinessRules.transformEntitlementExternalReferenceType("SWID");
		WDWBusinessRules.transformEntitlementExternalReferenceType("GUID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("XBMS_LINK_ID");
		WDWBusinessRules.transformEntitlementExternalReferenceType("XPASSID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("TRANSACTIONAL_GUEST_ID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("ADMISSION_LINK_ID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("PAYMENT_LINK_ID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("MEDIA_LINK_ID");
		WDWBusinessRules.transformEntitlementExternalReferenceType("XID");
		WDWBusinessRules
				.transformEntitlementExternalReferenceType("DME_LINK_ID");
		WDWBusinessRules.transformEntitlementExternalReferenceType("SECURE_ID");
		WDWBusinessRules.transformEntitlementExternalReferenceType("TXN_GUID");
	}
}
