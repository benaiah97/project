package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;

/**
 * Tests the ticket rules.
 * 
 * @author lewit019
 * 
 */
public class TicketRulesTestCase {

	/**
	 * JUnit for validateOnlyOneTicketOnRequest
	 */
	@Test
	public final void testInboundOneTktValidation() {

		ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();
		TicketTO aTicket1TO = new TicketTO();
		TicketTO aTicket2TO = new TicketTO();
		aTicket1TO.setBarCode("12345678901234567890");
		aTicket2TO.setBarCode("12345678901234567890");
		aTktList.add(aTicket1TO);
		aTktList.add(aTicket2TO);
		/*
		 * Scenario :: 1 More than one ticket Expected exception Transaction
		 * required to have at least 1 and no more than 1 had 2 tickets.
		 */
		try {
			TicketRules.validateOnlyOneTicketOnRequest(aTktList);
			fail("Expected exception on Test 1:  More than one ticket.");
		} catch (DTIException dtie) {
			assertEquals(
					"Transaction required to have at least 1 and no more than 1 had 2 tickets.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Empty ticket list. Expected exception Transaction
		 * required to have at least 1 and no more than 1 had 0 tickets.
		 */
		aTktList.clear();
		try {
			TicketRules.validateOnlyOneTicketOnRequest(aTktList);
			fail("Expected exception on Test 2:  Empty ticket list.");
		} catch (DTIException dtie) {

			assertEquals(
					"Transaction required to have at least 1 and no more than 1 had 0 tickets.",
					dtie.getLogMessage());
		}
		/* Scenario :: 3 One ticket in list */
		aTktList = new ArrayList<TicketTO>();
		aTicket1TO = new TicketTO();
		aTicket1TO.setBarCode("12345678901234567890");
		aTktList.add(aTicket1TO);
		try {
			TicketRules.validateOnlyOneTicketOnRequest(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception on Test 3:  One ticket in list: "
					+ dtie.toString());
		}
		return;
	}

	/**
	 * JUnit for validateMaxEightTicketsOnRequest
	 */
	@Test
	public final void testValidateMaxEightTicketsOnRequest() {

		ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setBarCode("12345678901234567890");
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		aTktList.add(aTicketTO);
		/* Scenario :: 1 Less than 8 tickets */
		try {
			TicketRules.validateMaxEightTicketsOnRequest(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception  Less than 8 tickets: " + dtie);
		}
		/* Scenario :: 2 Exactly 8 tickets */
		aTktList.add(aTicketTO);
		try {
			TicketRules.validateMaxEightTicketsOnRequest(aTktList);
		} catch (DTIException dtie) {
			fail("Unexpected exception Exactly 8 tickets: " + dtie);
		}
		/*
		 * Scenario :: 3 More than 8 tickets Expected Exception Transaction
		 * required to have at no more than 8 had 9 tickets.
		 */
		aTktList.add(aTicketTO);
		try {
			TicketRules.validateMaxEightTicketsOnRequest(aTktList);
			fail("More than 8 tickets.");
		} catch (DTIException dtie) {
			assertEquals(
					"Transaction required to have at no more than 8 had 9 tickets.",
					dtie.getLogMessage());
		}
		return;

	}

	/**
	 * JUnit for validateReservationTicketCount
	 */
	@Test
	public final void testValidateResTicketCount() {

		/* TPLookup Set-up (Valid) */
		TPLookupTO tpLookupTO = new TPLookupTO();
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
		tpLookupTO.setLookupValue("20");
		ArrayList<TPLookupTO> tpLookupList = new ArrayList<TPLookupTO>();
		tpLookupList.add(tpLookupTO);

		/* Set-up of empty entityAttrList */
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> entityAttrMap = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();

		/* Set-up non-willcall ReservationTO. */
		ReservationTO reservationTO = new ReservationTO();
		reservationTO.setResPickupArea("MailOrder");

		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("10"));
		tktList.add(aTicketTO);

		/*
		 * Scenario :: 1 21 tickets, 20 max, Expected exception Transaction
		 * exceeded max ticket count of 20: 21
		 */
		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("11"));
		tktList.add(aTicketTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("21 tickets, 20 max, no exceptions should have failed.");
		} catch (DTIException dtie) {
			assertEquals("Transaction exceeded max ticket count of 20: 21",
					dtie.getLogMessage());
		}

		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("9"));
		tktList.add(aTicketTO);

		AttributeTO attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("30");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX,
				attribTO);
		/*
		 * Scenario :: 2 51 tickets, 50 nonprint Expected exception Transaction
		 * exceeded printed ticket exception count of 30: 50
		 */
		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("20"));
		tktList.add(aTicketTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Transaction exceeded printed ticket exception count of 30: 50");
		} catch (DTIException dtie) {
			assertEquals(
					"Transaction exceeded printed ticket exception count of 30: 50",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 3 numberOfTickets > noPrntTktCntMaxException Expected
		 * exception Transaction exceeded non-printed ticket exception count of
		 * 50: 51
		 */
		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("1"));
		tktList.add(aTicketTO);
		attribTO = new AttributeTO();
		aTicketTO.setProdQty(new BigInteger("1"));
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("50");
		reservationTO.setResSalesType("Presale");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX,
				attribTO);

		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Transaction exceeded non-printed ticket exception count of 50: 51");
		} catch (DTIException dtie) {
			assertEquals(
					"Transaction exceeded non-printed ticket exception count of 50: 51",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 4 Expected exception Data set-up failure: PrntTktCntMax
		 * greater than NoPrntTktCntMax, 60 versus 50
		 */
		/* Ensure the correct leveling of exceptions when both are specified */
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("60");
		reservationTO.setResSalesType("Presale");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX,
				attribTO);

		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Data set-up failure:  PrntTktCntMax greater than NoPrntTktCntMax, 60 versus 50");
		} catch (DTIException dtie) {

			assertEquals(
					"Data set-up failure:  PrntTktCntMax greater than NoPrntTktCntMax, 60 versus 50",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 5 Expected exception Entity Attr NoPrntTktCntMax in DTI
		 * database is not a parseable integer: TEN
		 */
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("TEN");
		reservationTO.setResSalesType("Presale");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX,
				attribTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Entity Attr NoPrntTktCntMax in DTI database is not a parseable integer: TEN");
		} catch (DTIException dtie) {

			assertEquals(
					"Entity Attr NoPrntTktCntMax in DTI database is not a parseable integer: TEN",
					dtie.getLogMessage());
		}

		entityAttrMap.clear();
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("50");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX,
				attribTO);

		/*
		 * Scenario :: 6 Expected exception Transaction exceeded non-printed
		 * ticket exception count of 50: 72
		 */
		aTicketTO = new TicketTO();
		aTicketTO.setProdQty(new BigInteger("21"));
		tktList.add(aTicketTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Transaction exceeded non-printed ticket exception count of 50: 72");
		} catch (DTIException dtie) {
			assertEquals(
					"Transaction exceeded non-printed ticket exception count of 50: 72",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 7 Expected exception Data set-up failure: PrntTktCntMax
		 * is less or equal to MaxTicket, 15 versus 20
		 */
		tpLookupList.add(tpLookupTO);
		entityAttrMap.clear();
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("15");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX,
				attribTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Data set-up failure:  PrntTktCntMax is less or equal to MaxTicket, 15 versus 20");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Data set-up failure:  PrntTktCntMax is less or equal to MaxTicket, 15 versus 20",
					dtie.getLogMessage());

		}

		/*
		 * Scenario :: 8 Expected exception Data set-up failure: NoPrntTktCntMax
		 * is less or equal to MaxTicket, 15 versus 20
		 */
		entityAttrMap.clear();
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("15");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX,
				attribTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Data set-up failure:  NoPrntTktCntMax is less or equal to MaxTicket, 15 versus 20");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Data set-up failure:  NoPrntTktCntMax is less or equal to MaxTicket, 15 versus 20",
					dtie.getLogMessage());

		}

		/*
		 * Scenario :: 9 Expected exception Data set-up failure: PrntTktCntMax
		 * is less or equal to MaxTicket, 15 versus 20
		 */
		attribTO = new AttributeTO();
		attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
		attribTO.setAttrValue("15");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX,
				attribTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Data set-up failure:  PrntTktCntMax is less or equal to MaxTicket, 15 versus 20");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Data set-up failure:  PrntTktCntMax is less or equal to MaxTicket, 15 versus 20",
					dtie.getLogMessage());

		}
		/*
		 * Scenario :: 10 Expected exception Entity Attr PrntTktCntMax in DTI database is not a parseable integer: ONE
		 */
		attribTO.setAttrValue("ONE");
		entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX,
				attribTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Entity Attr PrntTktCntMax in DTI database is not a parseable integer: ONE");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Entity Attr PrntTktCntMax in DTI database is not a parseable integer: ONE",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 11 Expected exception Reservation transaction had a
		 * ticket clause with no quantity. Not allowed.
		 */
		/* getProdQty is null */
		aTicketTO.setProdQty(null);
		tktList.add(aTicketTO);
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Reservation transaction had a ticket clause with no quantity. Not allowed.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Reservation transaction had a ticket clause with no quantity. Not allowed.",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 12 Expected exception Ticket MaxLimit not defined in DTI
		 * database as required for this transaction type.
		 */
		aTicketTO.setProdQty(new BigInteger("2"));
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
		tpLookupTO.setLookupValue("-2");
		tpLookupList.add(tpLookupTO);

		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Ticket MaxLimit not defined in DTI database as required for this transaction type.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket MaxLimit not defined in DTI database as required for this transaction type.",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 13 Expected exception Ticket MaxLimit in DTI database is
		 * not a parseable integer: ONE
		 */
		aTicketTO.setProdQty(new BigInteger("2"));
		tpLookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
		tpLookupTO.setLookupValue("ONE");
		tpLookupList.add(tpLookupTO);

		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Ticket MaxLimit in DTI database is not a parseable integer: ONE");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket MaxLimit in DTI database is not a parseable integer: ONE",
					dtie.getLogMessage());
		}

		/*
		 * Scenario :: 14 Expected exception Transaction has no tickets that
		 * could be counted. Not allowed.
		 */
		aTicketTO.setProdQty(new BigInteger("10"));
		tktList.clear();
		try {
			TicketRules.validateReservationTicketCount(tktList, tpLookupList,
					entityAttrMap, reservationTO);
			fail("Transaction has no tickets that could be counted. Not allowed.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Transaction has no tickets that could be counted. Not allowed.",
					dtie.getLogMessage());
		}

		return;

	}

	/**
	 * JUnit for validateExternalTktIdOnly
	 * 
	 * @throws ParseException
	 */
	@Test
	public final void testValidateTktIdExternal() throws ParseException {

		TicketTO aTicket = new TicketTO();

		/* Uninitialized ticket */
		/*
		 * Scenario :: 1 Expected exception Ticket did not include any ticket
		 * ids.
		 */
		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
			fail("Exception expected on Test 1:  Uninitialized ticket.");
		} catch (DTIException dtie) {
			Assert.assertEquals("Ticket did not include any ticket ids.",
					dtie.getLogMessage());
		}

		/* Too many ticket types */
		/*
		 * Scenario :: 2 Expected exception Ticket included more ticket types
		 * than external ID.
		 */
		aTicket.setBarCode("IMABARCODE");
		aTicket.setExternal("IMAEXTERNAL");
		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
			fail("Too many ticket types.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket included more ticket types than external ID.",
					dtie.getLogMessage());
		}

		/* Doesn't have an external Id. */
		/*
		 * Scenario :: 3 Expected exception Ticket did not include external
		 * ticket id
		 */
		aTicket = new TicketTO();
		aTicket.setBarCode("IMABARCODE");
		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
			fail("Doesn't have an external Id.");
		} catch (DTIException dtie) {
			Assert.assertEquals("Ticket did not include external ticket id.",
					dtie.getLogMessage());
		}

		/* Ticket includes null external Id. */
		/*
		 * Scenario :: 4 Expected exception Ticket did not include any ticket
		 * ids
		 */
		aTicket = new TicketTO();
		aTicket.setExternal(null);

		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
			fail("Ticket did not include any ticket ids.");
		} catch (DTIException dtie) {
			Assert.assertEquals("Ticket did not include any ticket ids.",
					dtie.getLogMessage());
		}

		/* Ticket includes empty string external Id. */
		/*
		 * Scenario :: 5 Expected exception Ticket included invalid external
		 * ticket id.
		 */
		aTicket.setExternal(new String(""));
		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
			fail("Ticket included invalid external ticket id.");
		} catch (DTIException dtie) {
			Assert.assertEquals("Ticket included invalid external ticket id.",
					dtie.getLogMessage());
		}

		/* Scenario :: 6 Ticket contains valid external number. */
		aTicket.setExternal("ImaValidExternal");
		try {
			TicketRules.validateExternalTktIdOnly(aTicket);
		} catch (DTIException dtie) {
			fail("Unexpected exception on Test 6: Ticket contains valid external number.");
		}

	}

	/**
	 * JUnit for validateTicketValidityDates
	 */
	@Test
	public final void testValidateTicketValidityDates() {

		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		EntityTO anEntityTO = null;
		/*
		 * Scenario :: 1 Expected exception Did not receive entity information,
		 * as expected.
		 */
		/* Null entity */
		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
			fail("Did not receive entity information, as expected.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_CRITICAL_ERROR)

				Assert.assertEquals(
						"Did not receive entity information, as expected.",
						dtie.getLogMessage());

		}

		anEntityTO = new EntityTO();

		/* Scenario :: 2 Empty ticket list. */
		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
		} catch (DTIException dtie) {
			fail("Unexpected exception : Empty ticket list." + dtie.toString());
		}
		/*
		 * Scenario :: 3 Expected exception Ticket item 1 with product AAA01
		 * does not have an entry, as expected, in the DB Product list.
		 */
		/* Empty DB product list. */
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setTktItem(new BigInteger("1"));
		tktListTO.add(aTicketTO);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
			fail("Ticket item 1 with product AAA01 does not have an entry, as expected, in the DB Product list.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 1 with product AAA01 does not have an entry, as expected, in the DB Product list.",
					dtie.getLogMessage());

		}
		/*
		 * Scenario :: 4 Expected exception Ticket item 1 with product AAA01 did
		 * not have ticket validity dates as required.
		 */
		/* Product does require, order doesn't have */
		DBProductTO aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(true);
		dbProdList.add(aDBProduct);

		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("BBB02");
		aDBProduct.setValidityDateInfoRequired(false);
		dbProdList.add(aDBProduct);

		// Should have it
		aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO.setTktItem(new BigInteger("1"));
		tktListTO.add(aTicketTO);

		// Should not have it.
		aTicketTO = new TicketTO();
		aTicketTO.setProdCode("BBB02");
		aTicketTO.setTktItem(new BigInteger("2"));
		tktListTO.add(aTicketTO);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
			fail("Expected exception on Test 4: Product does require, order doesn't have.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 1 with product AAA01 did not have ticket validity dates as required.",
					dtie.getLogMessage());
		}
		/* Scenario :: 5 Product doesn't require, order doesn't have */
		dbProdList.clear();
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(false);
		dbProdList.add(aDBProduct);

		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("BBB02");
		aDBProduct.setValidityDateInfoRequired(false);
		dbProdList.add(aDBProduct);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
		} catch (DTIException dtie) {
			fail("Unexpected exception : Product doesn't require, order doesn't have: "
					+ dtie.toString());
		}

		/* Scenario :: 6 Order has date, product requires, entity permits */
		tktListTO.clear();
		dbProdList.clear();

		aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		aTicketTO
				.setTktValidityValidStart((GregorianCalendar) GregorianCalendar
						.getInstance());
		aTicketTO.setTktItem(new BigInteger("1"));
		tktListTO.add(aTicketTO);

		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(true);
		dbProdList.add(aDBProduct);

		anEntityTO.setValidityDateProductAllowed(true);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
		} catch (DTIException dtie) {
			fail("Unexpected exception: Order has date, product requires, entity permits: "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 7 Expected exception Ticket item 1 with product AAA01 had
		 * ticket validity dates. Not allowed for product AAA01.
		 */
		/* Order has date, product doesn't require, entity permits */
		dbProdList.clear();
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(false);
		dbProdList.add(aDBProduct);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 1 with product AAA01 had ticket validity dates.  Not allowed for product AAA01.",
					dtie.getLogMessage());

		}
		/*
		 * Scenario :: 8 Expected exception Ticket item 1 with product AAA01 had
		 * ticket validity dates. Not allowed for entity null.
		 */
		/* Order has date, product requires, entity says no */
		dbProdList.clear();

		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(true);
		dbProdList.add(aDBProduct);

		anEntityTO.setValidityDateProductAllowed(false);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
			fail("Expected exception on Test 8:  Order has date, product requires, entity says no.");
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket item 1 with product AAA01 had ticket validity dates.  Not allowed for entity null.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 9 Expected exception Ticket item 1 with product AAA01 had
		 * ticket validity dates. Not allowed for product AAA01.
		 */
		/* Order has date, product doesn't require, entity says no */
		dbProdList.clear();
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setValidityDateInfoRequired(false);
		dbProdList.add(aDBProduct);

		try {
			TicketRules.validateTicketValidityDates(tktListTO, anEntityTO,
					dbProdList);
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Ticket item 1 with product AAA01 had ticket validity dates.  Not allowed for product AAA01.",
					dtie.getLogMessage());
		}

		return;
	}

	/**
	 * JUnit for validateMinOneTicketOnRequest
	 */
	@Test
	public void testValidateMinOneTicketOnRequest() {
		/*
		 * Scenario :: 1 Expected exception Transaction required to have at
		 * least 1 ticket.
		 */
		ArrayList<TicketTO> aTktList = new ArrayList<>();
		try {
			TicketRules.validateMinOneTicketOnRequest(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Transaction required to have at least 1 ticket.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for validateMax250TicketsOnRequest
	 */
	@Test
	public void testValidateMax250TicketsOnRequest() {
		/*
		 * Scenario :: 1 Expected exception Transaction required to have at no
		 * more than 250 tickets; had 251 tickets.
		 */
		TicketTO ticketTO = new TicketTO();
		ArrayList<TicketTO> aTktList = new ArrayList<>();
		for (int i = 0; i <= 250; i++)
			aTktList.add(ticketTO);
		try {
			TicketRules.validateMax250TicketsOnRequest(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Transaction required to have at no more than 250 tickets; had 251 tickets.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for validateCreateTicketCount
	 */
	@Test
	public void testValidateCreateTicketCount() {

		TicketTO ticketTO = new TicketTO();
		ArrayList<TicketTO> aTktList = new ArrayList<>();
		aTktList.add(ticketTO);
		TPLookupTO lookupTO = new TPLookupTO();
		lookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
		ArrayList<TPLookupTO> tpLookupList = new ArrayList<>();
		tpLookupList.add(lookupTO);
		/*
		 * Scenario :: 1 Expected exception Reservation transaction had a ticket
		 * clause with no quantity. Not allowed.
		 */
		try {
			TicketRules.validateCreateTicketCount(aTktList, tpLookupList);
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Reservation transaction had a ticket clause with no quantity. Not allowed.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected exception Ticket MaxLimit in DTI database is
		 * not a parseable integer: ONE
		 */
		lookupTO.setLookupValue("ONE");
		ticketTO.setProdQty(new BigInteger("2"));
		aTktList.add(ticketTO);
		try {
			TicketRules.validateCreateTicketCount(aTktList, tpLookupList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Ticket MaxLimit in DTI database is not a parseable integer: ONE",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected exception Ticket MaxLimit not defined in DTI
		 * database as required for this transaction type.
		 */
		lookupTO.setLookupValue("-1");
		aTktList.add(ticketTO);
		try {
			TicketRules.validateCreateTicketCount(aTktList, tpLookupList);
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Ticket MaxLimit not defined in DTI database as required for this transaction type.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected exception Transaction exceeded max ticket
		 * count of 0: 8
		 */
		lookupTO.setLookupValue("0");
		aTktList.add(ticketTO);
		try {
			TicketRules.validateCreateTicketCount(aTktList, tpLookupList);
		} catch (DTIException dtie) {

			Assert.assertEquals(
					"Transaction exceeded max ticket count of 0: 8",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 5 Expected exception Transaction has no tickets that
		 * could be counted. Not allowed.
		 */
		aTktList.clear();
		try {
			TicketRules.validateCreateTicketCount(aTktList, tpLookupList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Transaction has no tickets that could be counted. Not allowed.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for validateTicketAssignment
	 */
	@Test
	public void testValidateTicketAssignment() {

		ArrayList<TktAssignmentTO> ticketAssignmetList = new ArrayList<>();

		TicketTO ticketTO = new TicketTO();
		TicketTO.TktAssignmentTO ticketAssignmets = ticketTO.new TktAssignmentTO();
		ticketAssignmets.setProdQty(null);
		ticketAssignmetList.add(ticketAssignmets);
		ticketTO.setTicketAssignmets(ticketAssignmetList);
		ticketTO.setProdQty(null);
		ArrayList<TicketTO> aTktList = new ArrayList<>();
		aTktList.add(ticketTO);
		/*
		 * Scenario :: 1 Expected exception Invalid Ticket Assignment Product
		 * Quantity provided.
		 */
		try {
			TicketRules.validateTicketAssignment(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"Invalid Ticket Assignment Product Quantity provided.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected exception validateTicketAssignment: Invalid
		 * Ticket Product Quantity provided.
		 */
		ticketAssignmets.setProdQty(new BigInteger("3"));
		ticketTO.setProdQty(null);
		try {
			TicketRules.validateTicketAssignment(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"validateTicketAssignment: Invalid Ticket Product Quantity provided.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected exception validateTicketAssignment: Ticket
		 * Product Quantity must match the sum of Assigned Products.
		 */
		ticketAssignmets.setProdQty(new BigInteger("5"));
		ticketTO.setProdQty(new BigInteger("3"));
		try {
			TicketRules.validateTicketAssignment(aTktList);
		} catch (DTIException dtie) {
			Assert.assertEquals(
					"validateTicketAssignment: Ticket Product Quantity must match the sum of Assigned Products.",
					dtie.getLogMessage());
		}

	}
}
