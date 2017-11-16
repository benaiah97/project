package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.UpgradeCatalogTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Tests the product rules.
 * 
 * @author lewit019
 */
public class ProductRulesTestCase extends CommonBusinessTest {

	/** The Constant DAYMILS. */
	public final static long DAYMILS = 86400000L;

	/** The Constant LASTWEEK. */
	public final static Date LASTWEEK = new Date(new Date().getTime()
			- (DAYMILS * 7));

	/** The Constant YESTERDAY. */
	public final static Date YESTERDAY = new Date(new Date().getTime()
			- DAYMILS);

	/** The Constant TOMORROW. */
	public final static Date TOMORROW = new Date(new Date().getTime() + DAYMILS);

	/** The Constant NEXTWEEK. */
	public final static Date NEXTWEEK = new Date(new Date().getTime()
			+ (DAYMILS * 7));

	/** The Constant ACTIVE. */
	public final static boolean ACTIVE = true;

	/** The Constant INACTIVE. */
	public final static boolean INACTIVE = false;

	/** The Constant SOLDOUT. */
	public final static boolean SOLDOUT = true;

	/** The Constant NOTSOLDOUT. */
	public final static boolean NOTSOLDOUT = false;

	/** The Constant ALLOWMISMATCH. */
	public final static boolean ALLOWMISMATCH = true;

	/** The Constant DISALLOWMISMATCH. */
	public final static boolean DISALLOWMISMATCH = false;

	/**
	 * JUnit for validateProductsAreSellable for sellable products bad lists.
	 */
	@Test
	public final void testSellableProductsBadLists() {

		ArrayList<TicketTO> tktListTO;
		ArrayList<DBProductTO> dbProdList = null;
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setProdCode("AAA01");
		tktListTO = new ArrayList<TicketTO>();
		tktListTO.add(createTestTicketTO("AAA01", "100.00"));
		/*
		 * Scenario :: 1 Expected Exception is Unable to find any of the order's
		 * product codes in the database. null dbproduct list
		 */
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("A null dbProdList should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to find any of the order's product codes in the database.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Unable to find any of the order's
		 * product codes in the database. empty dbproduct list
		 */
		dbProdList = new ArrayList<DBProductTO>();
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("An empty dbProdList should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to find any of the order's product codes in the database.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Unable to find any of the order's
		 * product codes in the order itself. null tktList
		 */
		DBProductTO aDBProduct = new DBProductTO();
		dbProdList.add(aDBProduct);
		tktListTO = null;
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("A null tktListTO should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to find any of the order's product codes in the order itself.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected Exception is Unable to find any of the order's
		 * product codes in the order itself. empty tktList
		 */
		tktListTO = new ArrayList<TicketTO>();
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("An empty tktListTO should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to find any of the order's product codes in the order itself.",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test sellable products not found. JUnit validateProductsAreSellable
	 */
	@Test
	public final void testSellableProductsNotFound() {

		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("CCC03", "3", "300.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));

		tktListTO.add(createTestTicketTO("AAA01", "100.00"));
		tktListTO.add(createTestTicketTO("BBB02", "200.00"));
		tktListTO.add(createTestTicketTO("CCC03", "300.00"));
		tktListTO.add(createTestTicketTO("DDD04", "400.00"));
		/*
		 * Scenario :: 1 Expected Exception is Unable to find all of the order's
		 * product codes in the database. products missing from db list
		 */
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("More ticket products than can be found in DB should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to find all of the order's product codes in the database.",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test sellable products attributes. JUnit validateProductsAreSellable
	 */
	@Test
	public final void testSellableProductsAttributes() {

		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		/*
		 * Scenario :: 1 active and available and within sale dates (happy path)
		 */
		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		tktListTO.add(createTestTicketTO("AAA01", "100.00"));
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
		} catch (DTIException dtie) {
			Assert.fail("Active product should not have thrown exception: "
					+ dtie);
		}
		/*
		 * Scenario :: 2 Expected Exception is Product BBB02 is not active in
		 * the database. inactive
		 */

		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", INACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		tktListTO.add(createTestTicketTO("BBB02", "200.00"));
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("Attempting to sell an inactive product should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals("Product BBB02 is not active in the database.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Product AAA01 is sold out in the
		 * database. sold out
		 */

		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				SOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("Attempting to sell a sold out product should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals("Product AAA01 is sold out in the database.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected Exception is Product BBB02 can't be sold
		 * because of start and/or end sale dates. sale date before
		 */

		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE,
				NOTSOLDOUT, LASTWEEK, YESTERDAY));
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("Attempting to sell a product too late should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product BBB02 can't be sold because of start and/or end sale dates.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 5 Expected Exception is Product BBB02 can't be sold
		 * because of start and/or end sale dates. sale date after
		 */

		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE,
				NOTSOLDOUT, TOMORROW, NEXTWEEK));
		try {
			ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
			Assert.fail("Attempting to sell a product too early should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product BBB02 can't be sold because of start and/or end sale dates.",
					dtie.getLogMessage());
		}
	}

	/**
	 * Test entity product sales. JUnit validateEntityCanSellProducts
	 */
	@Test
	public final void testEntityProductSales() {

		EntityTO entityTO = new EntityTO();
		entityTO.setTsMac("WDPRONA");
		entityTO.setTsLocation("351");
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		ArrayList<BigInteger> allowedPdtIdList = null;

		dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		dbProdList.add(createTestDBProduct("CCC03", "3", "300.00", ACTIVE,
				NOTSOLDOUT, YESTERDAY, TOMORROW));
		/*
		 * Scenario :: 1 Expected Exception is Entity WDPRONA/351 doesn't have
		 * any products or product groups related to it to sell. no products
		 * allowed - list null.
		 */

		try {
			ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
					allowedPdtIdList);
			Assert.fail("Having no allowed product list should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 doesn't have any products or product groups related to it to sell.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Entity WDPRONA/351 doesn't have
		 * any products or product groups related to it to sell. no products
		 * allowed - empty list.
		 */
		allowedPdtIdList = new ArrayList<BigInteger>();
		try {
			ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
					allowedPdtIdList);
			Assert.fail("Having empty allowed product list should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 doesn't have any products or product groups related to it to sell.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Product CCC03 can't be sold
		 * because entity WDPRONA/351 doesn't have it as a related product or a
		 * product in a related product group. some products missing
		 */
		allowedPdtIdList.add(new BigInteger("1"));
		allowedPdtIdList.add(new BigInteger("2"));
		try {
			ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
					allowedPdtIdList);
			Assert.fail("Having missing products in allowed product list should result in an exception in ProductRules.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product CCC03 can't be sold because entity WDPRONA/351 doesn't have it as a related product or a product in a related product group.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 all products allowed
		 */
		allowedPdtIdList.add(new BigInteger("3"));
		try {
			ProductRules.validateEntityCanSellProducts(entityTO, dbProdList,
					allowedPdtIdList);
		} catch (DTIException dtie) {
			Assert.fail("Should not have received exception on valid allowed product  "
					+ dtie.toString());
		}
	}

	/**
	 * Test price matching. JUnit validateProductPrice
	 */
	@Test
	public final void testPriceMatching() {

		// Entity Information
		EntityTO entityTO = new EntityTO();
		entityTO.setTsMac("WDPRONA");
		entityTO.setTsLocation("351");
		entityTO.setPriceMismatchAllowed(true);

		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "200.00"));
		tktListTO.add(createTestTicketTO("BBB02", "200.00"));
		/*
		 * Scenario :: 1 price with all mismatch allowed
		 */
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
		} catch (DTIException dtie) {
			Assert.fail("Should not have received exception on on-price : "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 2 price all products with no mismatch allowed
		 */
		entityTO.setPriceMismatchAllowed(false);
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
		} catch (DTIException dtie) {
			Assert.fail("Should not have received exception on on-price : "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 3 Expected Exception is Entity WDPRONA/351 disallowed
		 * from selling product (AAA01 at 200.00) off price (sale at 210.00 was
		 * attempted.). off price with no mismatch allowed by entity
		 */
		entityTO.setPriceMismatchAllowed(false);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "210.00"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected INVALID_PRICE where seller isn't allowed to sell off price.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 disallowed from selling product (AAA01 at 200.00) off price (sale at 210.00 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected Exception is Product AAA01 (at 200.00) can't
		 * be sold off price (sale at 210.00 was attempted.) off price with no
		 * mismatch allowed by product
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				DISALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "210.00"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected INVALID_PRICE where product isn't allowed to be sold off price.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 (at 200.00) can't be sold off price (sale at 210.00 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 5 Expected Exception is Product AAA01 (at 200.00) can't
		 * be sold off price (sale at 210.00 was attempted.) off price with
		 * product not configured properly
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT, null));
		tktListTO.add(createTestTicketTO("AAA01", "210.00"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected INVALID_PRICE where product isn't configured to be sold off price.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold off price because of an incomplete mismatch set-up in table.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 6 Expected Exception is Product AAA01 (at 200.00) can't
		 * be sold off price (sale at 210.00 was attempted.) off price where
		 * mismatch too high amount
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "210.01"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected PRICE_MISMATCH_ERROR_HI where product's price is beyond what's allowed amount.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for more than 210.00 (sale at 210.01 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 7 Expected Exception is Product AAA01 can't be sold for
		 * more than 220.000 (sale at 220.01 was attempted.) off price where
		 * mismatch too high percentage
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.PERCENT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "220.01"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected PRICE_MISMATCH_ERROR_HI where product's price is beyond what's allowed percentage.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for more than 220.000 (sale at 220.01 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 8 Expected Exception is Product AAA01 can't be sold for
		 * less than 210.00 (sale at 189.99 was attempted.) off price with
		 * mismatch too low amount
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "189.99"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected PRICE_MISMATCH_ERROR_LO where product's price is below what's allowed amount.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for less than 210.00 (sale at 189.99 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 9 Expected Exception is Product AAA01 can't be sold for
		 * less than 220.000 (sale at 179.99 was attempted.) off price with
		 * mismatch too low percentage
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.PERCENT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "179.99"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
			Assert.fail("Expected PRICE_MISMATCH_ERROR_LO where product's price is below what's allowed percentage.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for less than 220.000 (sale at 179.99 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 10 off price mismatch high warning amount
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "210.00"));
		try {
			if (!ProductRules.validateProductPrice(entityTO, tktListTO,
					dbProdList))
				Assert.fail("Ticket not properly marked for a high mismatch warning.");
		} catch (DTIException dtie) {
			Assert.fail("Exception not expected in product price mismatch high amount "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 11 off price mismatch high warning percentage
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.PERCENT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "220.00"));
		try {
			if (!ProductRules.validateProductPrice(entityTO, tktListTO,
					dbProdList))
				Assert.fail("Ticket not properly marked for a high mismatch warning.");
		} catch (DTIException dtie) {
			Assert.fail("Exception not expected in product price mismatch high percentage "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 12 off price mismatch low warning amount
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "190.00"));
		try {
			if (!ProductRules.validateProductPrice(entityTO, tktListTO,
					dbProdList))
				Assert.fail("Ticket not properly marked for a low mismatch warning.");
		} catch (DTIException dtie) {
			Assert.fail("Exception not expected in product price mismatch low amount"
					+ dtie.toString());
		}
		/*
		 * Scenario :: 13 off price mismatch low warning percentage
		 */
		entityTO.setPriceMismatchAllowed(true);
		tktListTO = new ArrayList<TicketTO>();
		dbProdList = new ArrayList<DBProductTO>();
		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.PERCENT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "180.00"));
		try {
			if (!ProductRules.validateProductPrice(entityTO, tktListTO,
					dbProdList))
				Assert.fail("Ticket not properly marked for a low mismatch warning.");
		} catch (DTIException dtie) {
			Assert.fail("Exception not expected in product price mismatch low percentage "
					+ dtie.toString());
		}
	}

	/**
	 * Test tax exempt. JUnit validateProductPrice
	 */
	@Test
	public final void testTaxExempt() {

		EntityTO entityTO = new EntityTO();
		entityTO.setTsMac("WDPRONA");
		entityTO.setTsLocation("351");
		entityTO.setPriceMismatchAllowed(false);
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

		dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "10.00",
				ALLOWMISMATCH, DBProductTO.MismatchToleranceType.AMOUNT,
				"10.00"));
		tktListTO.add(createTestTicketTO("AAA01", "200.00"));
		boolean isTaxExempt = false;
		/*
		 * Scenario :: 1 regular price, not tax exempt
		 */
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
		} catch (DTIException dtie) {
			Assert.fail("Should not have received exception on Test 1: on-price, not tax exempt: "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 2 Expected Exception is Entity WDPRONA/351 disallowed
		 * from selling product (AAA01 at 190.00) off price (sale at 200.00 was
		 * attempted.)regular price, tax exempt
		 */
		isTaxExempt = true;
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
			Assert.fail("Should  have received exception on Test 2: regular price, tax exempt.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 disallowed from selling product (AAA01 at 190.00) off price (sale at 200.00 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 tax reduced price, tax exempt
		 */
		tktListTO.clear();
		tktListTO.add(createTestTicketTO("AAA01", "190.00"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
		} catch (DTIException dtie) {
			Assert.fail("Should not have received exception on Test 3: tax reduced price, tax exempt: "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 4 Expected Exception is Entity WDPRONA/351 disallowed
		 * from selling product (AAA01 at 190.00) off price (sale at 189.99 was
		 * attempted.)price too low, tax exempt
		 */
		tktListTO.clear();
		tktListTO.add(createTestTicketTO("AAA01", "189.99"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
			Assert.fail("Should  have received exception : price too low, tax exempt.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 disallowed from selling product (AAA01 at 190.00) off price (sale at 189.99 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 5 Expected Exception is Entity WDPRONA/351 disallowed
		 * from selling product (AAA01 at 190.00) off price (sale at 190.01 was
		 * attempted.) price too high, tax exempt
		 */
		tktListTO.clear();
		tktListTO.add(createTestTicketTO("AAA01", "190.01"));
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
			Assert.fail("Should  have received exception : price too high, tax exempt.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity WDPRONA/351 disallowed from selling product (AAA01 at 190.00) off price (sale at 190.01 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 6 Expected Exception is Product AAA01 can't be sold for
		 * more than (tax exempt adjusted) 200.00 (sale at 201.00 was
		 * attempted.)
		 */
		tktListTO.clear();
		tktListTO.add(createTestTicketTO("AAA01", "201.00"));
		entityTO.setPriceMismatchAllowed(true);
		isTaxExempt = true;
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
			Assert.fail("Should  have received exception : regular price, tax exempt.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for more than (tax exempt adjusted) 200.00 (sale at 201.00 was attempted.)",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 7 Expected Exception is Product AAA01 can't be sold for
		 * less than (tax exempt adjusted) 200.00 (sale at 1.00 was attempted.)
		 */
		tktListTO.clear();
		tktListTO.add(createTestTicketTO("AAA01", "1.00"));
		entityTO.setPriceMismatchAllowed(true);
		isTaxExempt = true;
		try {
			ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList,
					isTaxExempt);
			Assert.fail("Should  have received exception : regular price, tax exempt.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be sold for less than (tax exempt adjusted) 200.00 (sale at 1.00 was attempted.)",
					dtie.getLogMessage());
		}

	}

	/**
	 * Test validate mapped provider ticket types. JUnit
	 * validateMappedProviderTicketTypes
	 */
	@Test
	public final void testValidateMappedProviderTicketTypes() {

		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		DBProductTO aDBProduct = new DBProductTO();
		aDBProduct.setMappedProviderTktName("AAA 01 Ticket");
		aDBProduct.setMappedProviderTktActive(true);
		dbProdList.add(aDBProduct);
		aDBProduct = new DBProductTO();
		aDBProduct.setMappedProviderTktName("BBB 02 Ticket");
		aDBProduct.setMappedProviderTktActive(true);
		dbProdList.add(aDBProduct);
		/*
		 * Scenario :: 1 All products mapped
		 */
		try {
			ProductRules.validateMappedProviderTicketTypes(dbProdList);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception :  All products mapped: "
					+ dtie.toString());
		}
		/*
		 * Scenario :: 2 Expected Exception is Product AAA01 is missing its
		 * reference to a provider ticket type. Product not mapped
		 */
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		dbProdList.add(aDBProduct);

		try {
			ProductRules.validateMappedProviderTicketTypes(dbProdList);
			Assert.fail("Expected exception :  Product not mapped.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 is missing its reference to a provider ticket type.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Product BBB01 is mapped to
		 * inactive ticket type BBB 02 Ticket All mapped, one inactive
		 */
		dbProdList.clear();
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("AAA01");
		aDBProduct.setMappedProviderTktName("AAA 01 Ticket");
		aDBProduct.setMappedProviderTktActive(true);
		dbProdList.add(aDBProduct);
		aDBProduct = new DBProductTO();
		aDBProduct.setPdtCode("BBB01");
		aDBProduct.setMappedProviderTktName("BBB 02 Ticket");
		aDBProduct.setMappedProviderTktActive(false);
		dbProdList.add(aDBProduct);
		try {
			ProductRules.validateMappedProviderTicketTypes(dbProdList);
			Assert.fail("Expected exception :  All mapped, one inactive.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product BBB01 is mapped to inactive ticket type BBB 02 Ticket",
					dtie.getLogMessage());
		}

	}

	/**
	 * JUnit validateOneShipProduct
	 */
	@Test
	public void testvalidateOneShipProduct()

	{
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
		TicketTO ticketTO = new TicketTO();
		ticketTO.setProdCode("AAA01");
		ticketTO.setFromProdCode("fromProdCode");
		DBProductTO dBProductTO = new DBProductTO();
		dBProductTO.setActive(true);
		dBProductTO.setStartSaleDate(new GregorianCalendar());
		dBProductTO.setEndSaleDate(new GregorianCalendar());
		dBProductTO.setDayClass("SHIP");
		dBProductTO.setPdtCode("AAA01");
		dbProdList.add(dBProductTO);
		tktListTO.add(ticketTO);
		/*
		 * Scenario :: 1 Expected Exception is Reservation does not have at
		 * least one product with DAY_CLASS of SHIP (meaning shipping).
		 */
		try {
			ProductRules.validateOneShipProduct(tktListTO, dbProdList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Reservation does not have at least one product with DAY_CLASS of SHIP (meaning shipping).",
					dtiException.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Reservation does not have at
		 * least one product with DAY_CLASS of SHIP (meaning shipping).
		 */
		dBProductTO.setPdtCode("AAA01");
		try {
			ProductRules.validateOneShipProduct(tktListTO, dbProdList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Reservation does not have at least one product with DAY_CLASS of SHIP (meaning shipping).",
					dtiException.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Reservation does not have at
		 * least one product with DAY_CLASS of SHIP (meaning shipping).
		 */
		dBProductTO.setDayClass(null);
		try {
			ProductRules.validateOneShipProduct(tktListTO, dbProdList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Reservation does not have at least one product with DAY_CLASS of SHIP (meaning shipping).",
					dtiException.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected Exception is Reservation has more than one
		 * product with DAY_CLASS of SHIP (meaning shipping).
		 */
		dBProductTO.setDayClass("SHIP");
		dbProdList.add(dBProductTO);
		try {
			ProductRules.validateOneShipProduct(tktListTO, dbProdList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Reservation has more than one product with DAY_CLASS of SHIP (meaning shipping).",
					dtiException.getLogMessage());
		}
		/*
		 * Scenario :: 5 Expected Exception is Shipping product AAA01 appears
		 * multiple times on this reservation.
		 */
		dbProdList.clear();
		tktListTO.add(ticketTO);
		dbProdList.add(dBProductTO);
		try {
			ProductRules.validateOneShipProduct(tktListTO, dbProdList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Shipping product AAA01 appears multiple times on this reservation.",
					dtiException.getLogMessage());
		}
	}

	/**
	 * JUnit validateNonZeroProductQuantity
	 */
	@Test
	public void testVlidateNonZeroProductQuantity() {
		TicketTO ticketTO = new TicketTO();
		ticketTO.setProdCode("AAA01");
		ticketTO.setProdQty(null);
		ArrayList<TicketTO> tktListTO = new ArrayList<>();
		tktListTO.add(ticketTO);
		/*
		 * Scenario :: 1 Expected Exception is Product AAA01 can't have a null
		 * or < 1 quantity.
		 */
		try {
			ProductRules.validateNonZeroProductQuantity(tktListTO);
		} catch (DTIException dtiException) {
			assertEquals("Product AAA01 can't have a null or < 1 quantity.",
					dtiException.getLogMessage());
		}
		/*
		 * Scenario :: 2 Exception not Expected
		 */
		ticketTO.setProdQty(new BigInteger("2"));
		tktListTO.add(ticketTO);
		try {
			ProductRules.validateNonZeroProductQuantity(tktListTO);
		} catch (DTIException dtiException) {
			Assert.fail("Unexpected error.");
		}
	}

	/**
	 * JUnit validateUpgradeProductPrice
	 */
	@Test
	public void testValidateUpgradeProductPrice() {
		EntityTO entityTO = new EntityTO();
		entityTO.setTsMac("DA6626");
		entityTO.setTsLocation("001");
		ArrayList<DBProductTO> dbFromProdList = new ArrayList<>();
		TicketTO ticketTO = new TicketTO();
		ticketTO.setProdCode("AAA01");
		ticketTO.setFromProdCode("AAA01");
		ticketTO.setFromPrice(new BigDecimal("1"));
		DBProductTO dBProductTO = new DBProductTO();
		dBProductTO.setActive(true);
		dBProductTO.setStartSaleDate(new GregorianCalendar());
		dBProductTO.setEndSaleDate(new GregorianCalendar());
		dBProductTO.setPdtCode("AAA01");
		dbFromProdList.add(dBProductTO);
		/*
		 * Scenario :: 1 Expected Exception is Product code AAA01 has no Unit
		 * Price set in the database. Please refer to Ticket Administration.
		 */
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		tktListTO.add(ticketTO);
		dBProductTO.setUnitPrice(null);
		try {
			ProductRules.validateUpgradeProductPrice(entityTO, tktListTO,
					dbFromProdList);
		} catch (DTIException dtie) {
			assertEquals(
					"Product code AAA01 has no Unit Price set in the database.  Please refer to Ticket Administration.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Product code AAA01 has no
		 * Standard Retail Price set in the database. Please refer to Ticket
		 * Administration.
		 */
		dBProductTO.setUnitPrice(new BigDecimal("2"));
		dBProductTO.setStandardRetailPrice(null);
		dbFromProdList.add(dBProductTO);
		try {
			ProductRules.validateUpgradeProductPrice(entityTO, tktListTO,
					dbFromProdList);
		} catch (DTIException dtie) {
			assertEquals(
					"Product code AAA01 has no Standard Retail Price set in the database.  Please refer to Ticket Administration.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Entity DA6626/001 cannot upgrade
		 * from product AAA01 with from price of 1. Valid from prices for
		 * upgrade are: 2 (NET) and 2 (Standard Retail Price).
		 */
		dBProductTO.setUnitPrice(new BigDecimal("2"));
		dBProductTO.setStandardRetailPrice(new BigDecimal("2"));
		dbFromProdList.add(dBProductTO);
		try {
			ProductRules.validateUpgradeProductPrice(entityTO, tktListTO,
					dbFromProdList);
		} catch (DTIException dtie) {
			assertEquals(
					"Entity DA6626/001 cannot upgrade from product AAA01 with from price of 1.  Valid from prices for upgrade are: 2 (NET) and 2 (Standard Retail Price).",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit validateProductsHaveDemographics
	 */
	@Test
	public void testValidateProductsHaveDemographics() {
		TicketTO ticketTO = new TicketTO();
		ticketTO.setProdCode("AAA01");
		ticketTO.setFromProdCode("AAA01");
		DBProductTO dBProductTO = new DBProductTO();
		dBProductTO.setActive(true);
		dBProductTO.setDemographicsInd(true);
		dBProductTO.setPdtCode("AAA01");
		ArrayList<DBProductTO> dbFromProdList = new ArrayList<>();
		dbFromProdList.add(dBProductTO);
		/*
		 * Scenario :: 1 Expected Exception is Product AAA01 does not have
		 * ticket level demographics supplied, and such are required.
		 */
		ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
		tktListTO.add(ticketTO);
		try {
			ProductRules.validateProductsHaveDemographics(tktListTO,
					dbFromProdList);
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 does not have ticket level demographics supplied, and such are required.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Product AAA01 has an invalid
		 * number of demographics supplied (1); does not match prod quantity
		 * (2).
		 */
		DemographicsTO demographicsTO = new DemographicsTO();
		ArrayList<DemographicsTO> ticketDemoList = new ArrayList<>();
		ticketDemoList.add(demographicsTO);
		ticketTO.setProdQty(new BigInteger("2"));
		tktListTO.add(ticketTO);
		ticketTO.setTicketDemoList(ticketDemoList);
		try {
			ProductRules.validateProductsHaveDemographics(tktListTO,
					dbFromProdList);
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 has an invalid number of demographics supplied (1); does not match prod quantity (2).",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is Product AAA01 does not permit
		 * demographics to be supplied, and demographics are present.
		 */
		dBProductTO.setDemographicsInd(false);
		dbFromProdList.add(dBProductTO);
		try {
			ProductRules.validateProductsHaveDemographics(tktListTO,
					dbFromProdList);
			Assert.fail("Product AAA01 does not permit demographics to be supplied, and demographics are present.");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 does not permit demographics to be supplied, and demographics are present.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 4 Expected Exception is On Product AAA01, ticket
		 * demographics supplied did not meet required values of M or F or U
		 * (unspecified).
		 */
		ticketTO.setProdQty(new BigInteger("1"));
		dBProductTO.setDemographicsInd(true);
		dbFromProdList.add(dBProductTO);
		try {
			ProductRules.validateProductsHaveDemographics(tktListTO,
					dbFromProdList);
			Assert.fail("On Product AAA01, ticket demographics supplied did not meet required  values of M or F or U (unspecified).");
		} catch (DTIException dtie) {
			assertEquals(
					"On Product AAA01, ticket demographics supplied did not meet required  values of M or F or U (unspecified).",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit validateUpgradePricing
	 */
	@Test
	public void testValidateUpgradePricing() {
		String fromProdCode = "AAA01";
		BigDecimal fromProdPrice = new BigDecimal("2");
		String toProdCode = "BBB01";
		BigDecimal toProdPrice = new BigDecimal("2");
		BigDecimal upgrdPrice = new BigDecimal("2");
		/*
		 * Scenario :: 1 Expected Exception is From product (AAA01) price of 2
		 * plus upgrade amount 2 does not equal target product code (BBB01)
		 * price of 2.
		 */
		try {
			ProductRules.validateUpgradePricing(fromProdCode, fromProdPrice,
					toProdCode, toProdPrice, upgrdPrice);
			Assert.fail("From product (AAA01) price of 2 plus upgrade amount 2 does not equal target product code (BBB01) price of 2.");
		} catch (DTIException dtie) {
			assertEquals(
					"From product (AAA01) price of 2 plus upgrade amount 2 does not equal target product code (BBB01) price of 2.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is From product: AAA01 at 3 is not
		 * allowed to be upgraded to product: BBB01 at 2 because the upgrade is
		 * to a product of lesser value.
		 */
		fromProdPrice = new BigDecimal("3");
		try {
			ProductRules.validateUpgradePricing(fromProdCode, fromProdPrice,
					toProdCode, toProdPrice, upgrdPrice);
			Assert.fail("From product: AAA01 at 3 is not allowed to be upgraded to product: BBB01 at 2 because the upgrade is to a product of lesser value.");
		} catch (DTIException dtie) {
			assertEquals(
					"From product: AAA01 at 3 is not allowed to be upgraded to product: BBB01 at 2 because the upgrade is to a product of lesser value.",
					dtie.getLogMessage());
		}

	}

	/**
	 * JUnit validateUpgradeProducts
	 * TODO:  Update this test to be accurate. :) JTL
	 */
	@Test
	@Ignore 
	public void testValidateUpgradeProducts() {
		/*
		 * Scenario :: 1 Expected Exception is Unable to identify specified DTI
		 * product codes in database: invalid FROM product(s).
		 */
		ArrayList<DBProductTO> dbUpgrdProdList = new ArrayList<>();
		ArrayList<TicketTO> ticketTOList = new ArrayList<TicketTO>();
		TicketTO aTicket = new TicketTO();
		aTicket.setDlrPLU("something");
		ticketTOList.add(aTicket);
		try {
			ProductRules.validateUpgradeProducts(dbUpgrdProdList,ticketTOList);
			Assert.fail("Unable to identify specified DTI product codes in database: invalid FROM product(s).");
		} catch (DTIException dtie) {
			assertEquals(
					"Unable to identify specified DTI product codes in database: invalid FROM product(s).",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit validateWdwTicketDemo
	 */
	@Test
	public void testValidateWdwTicketDemo() {
		ArrayList<TicketTO> tktListTO = new ArrayList<>();
		getDemographics(tktListTO);
      tktListTO.get(0).getTicketDemoList().get(0).setFirstName("firstName");
		/*
		 * Scenario :: 1 Expected Exception is For DLR, Zip has invalid length
		 * for TktDemoData (expected 1 to 10, but found a length of 12).
		 */
      try {
         ProductRules.validateWdwTicketDemo(tktListTO);
      } catch (DTIException dtie) {
      }
      tktListTO.get(0).getTicketDemoList().get(0).setZip("121212124525");
      try {
         ProductRules.validateDlrTicketDemo(tktListTO);
      } catch (DTIException dtie) {
         assertEquals(
               "For DLR, Zip has invalid length for TktDemoData (expected 1 to 10, but found a length of 12).",
               dtie.getLogMessage());
      }
		/*
		 * Scenario :: 2 Expected Exception is For DLR, OptInSolicit is a
		 * mandatory field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setZip("12345");
		tktListTO.get(0).getTicketDemoList().get(0).setOptInSolicit(null);
		try {
			ProductRules.validateDlrTicketDemo(tktListTO);
			Assert.fail("For DLR, OptInSolicit is a mandatory field for TktDemoData.  TktItem failing was null.");
		} catch (DTIException dtie) {
			assertEquals(
               "For DLR, OptInSolicit is a mandatory field for TktDemoData.  TktItem failing was null.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is For WDW, DateOfBirth is a
		 * mandatory field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setDateOfBirth(null);
		try {
			ProductRules.validateWdwTicketDemo(tktListTO);
			Assert.fail("For WDW, DateOfBirth is a mandatory field for TktDemoData.  TktItem failing was null.");
		} catch (DTIException dtie) {
			assertEquals(
               "For WDW, DateOfBirth is a mandatory field for TktDemoData.  TktItem failing was null.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit validateDlrTicketDemo
	 */
	@Test
	public void testValidateDlrTicketDemo() {
		ArrayList<TicketTO> tktListTO = new ArrayList<>();
		getDemographics(tktListTO);
		/*
		 * Scenario :: 1 Expected Exception is For DLR, LastName is a mandatory
		 * field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setLastName(null);
		try {
			ProductRules.validateDlrTicketDemo(tktListTO);
			Assert.fail("For DLR, LastName is a mandatory field for TktDemoData.  TktItem failing was null.");
		} catch (DTIException dtie) {
			assertEquals(
					"For DLR, FirstName is a mandatory field for TktDemoData.  TktItem failing was null.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is For DLR, OptInSolicit is a
		 * mandatory field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setLastName("LastName");
		tktListTO.get(0).getTicketDemoList().get(0).setOptInSolicit(null);
		try {
			ProductRules.validateDlrTicketDemo(tktListTO);
			Assert.fail("For DLR, OptInSolicit is a mandatory field for TktDemoData.  TktItem failing was null.");
		} catch (DTIException dtie) {
			assertEquals(
					"For DLR, FirstName is a mandatory field for TktDemoData.  TktItem failing was null.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 Expected Exception is For DLR, DateOfBirth is a
		 * mandatory field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setDateOfBirth(null);
		try {
			ProductRules.validateDlrTicketDemo(tktListTO);
			Assert.fail("For DLR, DateOfBirth is a mandatory field for TktDemoData.  TktItem failing was null.");
		} catch (DTIException dtie) {
			assertEquals(
					"For DLR, FirstName is a mandatory field for TktDemoData.  TktItem failing was null.",
					dtie.getLogMessage());
		}
	}

	/**
	 * JUnit validateHkdTicketDemo
	 */
	@Test
	public void testValidateHkdTicketDemo() {

		ArrayList<TicketTO> tktListTO = new ArrayList<>();
		getDemographics(tktListTO);
		/*
		 * Scenario :: 1 Expected Exception is For DLR, DateOfBirth is a
		 * mandatory field for TktDemoData. TktItem failing was null.
		 */
		tktListTO.get(0).getTicketDemoList().get(0).setCellPhone("0");
		try {
			ProductRules.validateHkdTicketDemo(tktListTO);
		} catch (DTIException dtie) {
			// TODO Somesh
			//Assert.fail("Unexpected Exception");
		}
	}

	/**
	 * JUnit validateEntityCanUpgradeProducts
	 */
	@Test
	public void testValidateEntityCanUpgradeProducts() {
		EntityTO entityTO = new EntityTO();
		entityTO.setTsMac("DA6626");
		entityTO.setTsLocation("001");
		DBProductTO dbProductTO = new DBProductTO();
		dbProductTO.setPdtCode("AAA01");
		dbProductTO.setPdtid(new BigInteger("2"));
		/*
		 * Scenario :: 1 Expected Exception is Entity DA6626/001 doesn't have
		 * any products or product groups related to it to upgrade.
		 */
		ArrayList<DBProductTO> dbProdList = new ArrayList<>();
		ArrayList<BigInteger> allowedPdtIdList = new ArrayList<>();
		try {
			ProductRules.validateEntityCanUpgradeProducts(entityTO, dbProdList,
					allowedPdtIdList);
			Assert.fail("Entity DA6626/001 doesn't have any products or product groups related to it to upgrade.");
		} catch (DTIException dtie) {
			assertEquals(
					"Entity DA6626/001 doesn't have any products or product groups related to it to upgrade.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 Expected Exception is Product AAA01 can't be upgraded
		 * because entity DA6626/001 doesn't have it as a related product or a
		 * product in a related product group.
		 */
		allowedPdtIdList.add(new BigInteger("3"));
		dbProdList.add(dbProductTO);
		try {
			ProductRules.validateEntityCanUpgradeProducts(entityTO, dbProdList,
					allowedPdtIdList);
			Assert.fail("Product AAA01 can't be upgraded");
		} catch (DTIException dtie) {
			assertEquals(
					"Product AAA01 can't be upgraded because entity DA6626/001 doesn't have it as a related product or a product in a related product group.",
					dtie.getLogMessage());
		}

	}

	/**
	 * @param tktListTO
	 * @return
	 */
	private ArrayList<TicketTO> getDemographics(ArrayList<TicketTO> tktListTO) {
		TicketTO ticketTO = new TicketTO();
		DemographicsTO demographicsTO = new DemographicsTO();
		demographicsTO.setLastName("LastName");
		demographicsTO.setDateOfBirth(new GregorianCalendar());
		demographicsTO.setGender("Male");
		demographicsTO.setAddr1("Address");
		demographicsTO.setAddr2("Address2");
		demographicsTO.setCity("City");
		demographicsTO.setState("state");
		demographicsTO.setZip("1234567891");
		demographicsTO.setCountry("US");
		demographicsTO.setTelephone("123654789");
		demographicsTO.setEmail("test@disney.com");
		demographicsTO.setOptInSolicit(true);
		ArrayList<DemographicsTO> ticketDemoList = new ArrayList<>();
		ticketDemoList.add(demographicsTO);
		ticketTO.setTicketDemoList(ticketDemoList);
		tktListTO.add(ticketTO);
		return tktListTO;

	}

	/**
	 * Creates the test db product.
	 * 
	 * @param pdtCode
	 *            the pdt code
	 * @param pdtIdString
	 *            the pdt id string
	 * @param printedPrice
	 *            the printed price
	 * @param mismatchInd
	 *            the mismatch ind
	 * @param mismatchType
	 *            the mismatch type
	 * @param misMatchTol
	 *            the mis match tol
	 * @param tax
	 *            the tax
	 * 
	 * @return the DB product to
	 */
	private DBProductTO createTestDBProduct(String pdtCode, String pdtIdString,
			String printedPrice, String tax, boolean mismatchInd,
			DBProductTO.MismatchToleranceType mismatchType, String misMatchTol) {

		DBProductTO dbProd = new DBProductTO();
		dbProd.setPdtCode(pdtCode);
		dbProd.setPdtid(new BigInteger(pdtIdString));
		dbProd.setPrintedPrice(new BigDecimal(printedPrice));
		dbProd.setPriceMismatchAllowed(mismatchInd);
		dbProd.setMisMatchTolType(mismatchType);
		dbProd.setTax(new BigDecimal(tax));
		if (misMatchTol != null)
			dbProd.setMisMatchTol(new BigDecimal(misMatchTol));

		return dbProd;
	}

	/**
	 * Creates the test ticket to.
	 * 
	 * @param pdtCode
	 *            the pdt code
	 * @param prodPrice
	 *            the prod price
	 * 
	 * @return the ticket to
	 */
	private TicketTO createTestTicketTO(String pdtCode, String prodPrice) {
		TicketTO aTicketTO = new TicketTO();
		aTicketTO.setProdCode(pdtCode);
		aTicketTO.setProdPrice(new BigDecimal(prodPrice));
		return aTicketTO;
	}

	/**
	 * Creates the test db product.
	 * 
	 * @param pdtCode
	 *            the pdt code
	 * @param pdtIdString
	 *            the pdt id string
	 * @param printedPrice
	 *            the printed price
	 * @param active
	 *            the active
	 * @param soldOut
	 *            the sold out
	 * @param startSaleDate
	 *            the start sale date
	 * @param endSaleDate
	 *            the end sale date
	 * 
	 * @return the DB product to
	 */
	private DBProductTO createTestDBProduct(String pdtCode, String pdtIdString,
			String printedPrice, boolean active, boolean soldOut,
			Date startSaleDate, Date endSaleDate) {
		DBProductTO dbProd = new DBProductTO();
		dbProd.setPdtCode(pdtCode);
		dbProd.setPdtid(new BigInteger(pdtIdString));
		dbProd.setPrintedPrice(new BigDecimal(printedPrice));
		dbProd.setActive(active);
		dbProd.setSoldOut(soldOut);
		GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
				.getInstance();
		gCal.setTime(startSaleDate);
		dbProd.setStartSaleDate(gCal);
		gCal = (GregorianCalendar) GregorianCalendar.getInstance();
		gCal.setTime(endSaleDate);
		dbProd.setEndSaleDate(gCal);

		return dbProd;
	}
	
	 @Test
		public void testdisqualifyProduct() {
			UpgradeCatalogTO upGradeCataTo = new UpgradeCatalogTO();
			ArrayList<DBProductTO> productList = new ArrayList<DBProductTO>();

			DBProductTO aProduct = new DBProductTO();
			aProduct.setPdtCode("1");
			aProduct.setUnitPrice(new BigDecimal(1.0));
			aProduct.setTax(new BigDecimal(1.0));
			aProduct.setDayClass("AP");
			aProduct.setDaySubclass("PLAT");
			productList.add(aProduct);
			aProduct = new DBProductTO();

			aProduct.setPdtCode("3");
			aProduct.setUnitPrice(new BigDecimal(3.0));
			aProduct.setTax(new BigDecimal(1.0));
			aProduct.setDayClass("AP");
			aProduct.setDaySubclass("EPCTAFT4");
			productList.add(aProduct);
			upGradeCataTo.setProductList(productList);
			ArrayList<String> siteList = new ArrayList<String>();
			
			siteList.add("78");
			siteList.add("81");
			ProductRules.disqualifyProduct(siteList,upGradeCataTo);
			assertEquals(upGradeCataTo.getProductListCount(),0);
			
			upGradeCataTo = new UpgradeCatalogTO();
			aProduct = new DBProductTO();
			aProduct.setPdtCode("1");
			aProduct.setUnitPrice(new BigDecimal(1.0));
			aProduct.setTax(new BigDecimal(1.0));
			aProduct.setDayClass("AP");
			aProduct.setDaySubclass("PLATPLUS");
			productList = new ArrayList<DBProductTO>();
			productList.add(aProduct);
			upGradeCataTo.setProductList(productList);
			
			 siteList = new ArrayList<String>();
			
			siteList.add("78");
			siteList.add("81");
			ProductRules.disqualifyProduct(siteList,upGradeCataTo);
			assertEquals(upGradeCataTo.getProductListCount(),1);
			
			upGradeCataTo = new UpgradeCatalogTO();
			aProduct = new DBProductTO();
			aProduct.setPdtCode("1");
			aProduct.setUnitPrice(new BigDecimal(1.0));
			aProduct.setTax(new BigDecimal(1.0));
			aProduct.setDayClass("AP");
			aProduct.setDaySubclass("GOLD");
			productList = new ArrayList<DBProductTO>();
			productList.add(aProduct);
			upGradeCataTo.setProductList(productList);
			
			 siteList = new ArrayList<String>();
			
			siteList.add("78");
			siteList.add("81");
			ProductRules.disqualifyProduct(siteList,upGradeCataTo);
			assertEquals(upGradeCataTo.getProductListCount(),0);
			
			
		}
	

}
