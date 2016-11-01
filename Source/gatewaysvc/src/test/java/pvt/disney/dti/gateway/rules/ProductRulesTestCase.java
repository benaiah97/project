package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Tests the product rules.
 * 
 * @author lewit019
 */
public class ProductRulesTestCase {

  /** The Constant DAYMILS. */
  public final static long DAYMILS = 86400000L;
  
  /** The Constant LASTWEEK. */
  public final static Date LASTWEEK = new Date(new Date().getTime() - (DAYMILS * 7));
  
  /** The Constant YESTERDAY. */
  public final static Date YESTERDAY = new Date(new Date().getTime() - DAYMILS);
  
  /** The Constant TOMORROW. */
  public final static Date TOMORROW = new Date(new Date().getTime() + DAYMILS);
  
  /** The Constant NEXTWEEK. */
  public final static Date NEXTWEEK = new Date(new Date().getTime() + (DAYMILS * 7));
  
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
   * Test sellable products bad lists.
   */
  @Test
  public final void testSellableProductsBadLists() {

    ArrayList<TicketTO> tktListTO;
    ArrayList<DBProductTO> dbProdList = null;

    // test null dbproduct list
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setProdCode("AAA01");
    tktListTO = new ArrayList<TicketTO>();
    tktListTO.add(createTestTicketTO("AAA01", "100.00"));

    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("A null dbProdList should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRODUCT_CODE)
        fail("Expected INVALID_PRODUCT_CODE in null dbproduct list test.");
    }

    // test empty dbproduct list
    dbProdList = new ArrayList<DBProductTO>();
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("An empty dbProdList should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRODUCT_CODE)
        fail("Expected INVALID_PRODUCT_CODE in empty dbproduct list test.");
    }

    // test null tktList
    DBProductTO aDBProduct = new DBProductTO();
    dbProdList.add(aDBProduct);
    tktListTO = null;
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("A null tktListTO should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRODUCT_CODE)
        fail("Expected INVALID_PRODUCT_CODE in null tktListTO list test.");
    }

    // test empty tktList
    tktListTO = new ArrayList<TicketTO>();
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("An empty tktListTO should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRODUCT_CODE)
        fail("Expected INVALID_PRODUCT_CODE in empty tktListTO list test.");
    }

    return;

  }

  /**
   * Test sellable products not found.
   */
  @Test
  public final void testSellableProductsNotFound() {

    // test products missing from db list
    ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

    dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("CCC03", "3", "300.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));

    tktListTO.add(createTestTicketTO("AAA01", "100.00"));
    tktListTO.add(createTestTicketTO("BBB02", "200.00"));
    tktListTO.add(createTestTicketTO("CCC03", "300.00"));
    tktListTO.add(createTestTicketTO("DDD04", "400.00"));

    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("More ticket products than can be found in DB should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRODUCT_CODE)
        fail("Expected INVALID_PRODUCT_CODE in a missing DBProductTO list test.");
    }

  }

  /**
   * Test sellable products attributes.
   */
  @Test
  public final void testSellableProductsAttributes() {

    ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

    // test active and available and within sale dates (happy path)
    dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    tktListTO.add(createTestTicketTO("AAA01", "100.00"));
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Active product should not have thrown exception: " + dtie);
    }

    // test inactive
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", INACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    tktListTO.add(createTestTicketTO("BBB02", "200.00"));
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("Attempting to sell an inactive product should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_UNAVAILABLE)
        fail("Expected PRODUCT_UNAVAILABLE in inactive product test.");
    }

    // test sold out
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList
        .add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, SOLDOUT, YESTERDAY, TOMORROW));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("Attempting to sell a sold out product should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_SOLD_OUT)
        fail("Expected PRODUCT_SOLD_OUT in sold out product test.");
    }

    // test sale date before
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE, NOTSOLDOUT, LASTWEEK,
        YESTERDAY));
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("Attempting to sell a product too late should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_EXPIRED)
        fail("Expected PRODUCT_EXPIRED in product sold too late test.");
    }

    // test sale date after
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE, NOTSOLDOUT, TOMORROW,
        NEXTWEEK));
    try {
      ProductRules.validateProductsAreSellable(tktListTO, dbProdList);
      fail("Attempting to sell a product too early should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_EXPIRED)
        fail("Expected PRODUCT_EXPIRED in product sold too early test.");
    }

  }

  /**
   * Test entity product sales.
   */
  @Test
  public final void testEntityProductSales() {

    EntityTO entityTO = new EntityTO();
    entityTO.setTsMac("WDPRONA");
    entityTO.setTsLocation("351");

    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
    ArrayList<BigInteger> allowedPdtIdList = null;

    dbProdList.add(createTestDBProduct("AAA01", "1", "100.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));
    dbProdList.add(createTestDBProduct("CCC03", "3", "300.00", ACTIVE, NOTSOLDOUT, YESTERDAY,
        TOMORROW));

    // Test no products allowed - list null.
    try {
      ProductRules.validateEntityCanSellProducts(entityTO, dbProdList, allowedPdtIdList);
      fail("Having no allowed product list should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_NOT_PERMITTED)
        fail("Expected PRODUCT_NOT_PERMITTED in null allowed product list test.");
    }

    // Test no products allowed - empty list.
    allowedPdtIdList = new ArrayList<BigInteger>();
    try {
      ProductRules.validateEntityCanSellProducts(entityTO, dbProdList, allowedPdtIdList);
      fail("Having empty allowed product list should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_NOT_PERMITTED)
        fail("Expected PRODUCT_NOT_PERMITTED in empty allowed product list test.");
    }

    // Test some products missing
    allowedPdtIdList.add(new BigInteger("1"));
    allowedPdtIdList.add(new BigInteger("2"));
    try {
      ProductRules.validateEntityCanSellProducts(entityTO, dbProdList, allowedPdtIdList);
      fail("Having missing products in allowed product list should result in an exception in ProductRules.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRODUCT_NOT_PERMITTED)
        fail("Expected PRODUCT_NOT_PERMITTED in an incomplete allowed product list test.");
    }

    // Test all products allowed
    allowedPdtIdList.add(new BigInteger("3"));
    try {
      ProductRules.validateEntityCanSellProducts(entityTO, dbProdList, allowedPdtIdList);
    } catch (DTIException dtie) {
      fail("Should not have received exception on valid allowed product test: " + dtie.toString());
    }

    return;

  }

  /**
   * Test price matching.
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

    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    dbProdList.add(createTestDBProduct("BBB02", "2", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));

    tktListTO.add(createTestTicketTO("AAA01", "200.00"));
    tktListTO.add(createTestTicketTO("BBB02", "200.00"));

    // Test on-price with all mismatch allowed
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Should not have received exception on on-price test 1: " + dtie.toString());
    }

    // Test on-price all products with no mismatch allowed
    entityTO.setPriceMismatchAllowed(false);
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Should not have received exception on on-price test 2: " + dtie.toString());
    }

    // Test off price with no mismatch allowed by entity
    entityTO.setPriceMismatchAllowed(false);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "210.00"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected INVALID_PRICE where seller isn't allowed to sell off price.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in entity disallowed from price mismatch test.");
    }

    // Test off price with no mismatch allowed by product
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", DISALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "210.00"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected INVALID_PRICE where product isn't allowed to be sold off price.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in product disallowed from price mismatch test.");
    }

    // Test off price with product not configured properly
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, null));
    tktListTO.add(createTestTicketTO("AAA01", "210.00"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected INVALID_PRICE where product isn't configured to be sold off price.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in product not configured for price mismatch test.");
    }

    // Test off price where mismatch too high amount
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "210.01"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected PRICE_MISMATCH_ERROR_HI where product's price is beyond what's allowed amount.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRICE_MISMATCH_ERROR_HI)
        fail("Expected PRICE_MISMATCH_ERROR_HI in product price mismatch too high amount test.");
    }

    // Test off price where mismatch too high percentage
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.PERCENT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "220.01"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected PRICE_MISMATCH_ERROR_HI where product's price is beyond what's allowed percentage.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRICE_MISMATCH_ERROR_HI)
        fail("Expected PRICE_MISMATCH_ERROR_HI in product price mismatch too high percentage test.");
    }

    // Test off price with mismatch too low amount
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "189.99"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected PRICE_MISMATCH_ERROR_LO where product's price is below what's allowed amount.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRICE_MISMATCH_ERROR_LO)
        fail("Expected PRICE_MISMATCH_ERROR_LO in product price mismatch too low amount test.");
    }

    // Test off price with mismatch too low percentage
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.PERCENT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "179.99"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList);
      fail("Expected PRICE_MISMATCH_ERROR_LO where product's price is below what's allowed percentage.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.PRICE_MISMATCH_ERROR_LO)
        fail("Expected PRICE_MISMATCH_ERROR_LO in product price mismatch too low percentage test");
    }

    // Test off price mismatch high warning amount
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "210.00"));
    try {
      if (!ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList)) 
         fail("Ticket not properly marked for a high mismatch warning.");
    } catch (DTIException dtie) {
      fail("Exception not expected in product price mismatch high amount test:" + dtie.toString());
    }

    // Test off price mismatch high warning percentage
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.PERCENT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "220.00"));
    try {
      if (!ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList))
        fail("Ticket not properly marked for a high mismatch warning.");
    } catch (DTIException dtie) {
      fail("Exception not expected in product price mismatch high percentage test:"
          + dtie.toString());
    }

    // Test off price mismatch low warning amount
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "190.00"));
    try {
      if (!ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList))
        fail("Ticket not properly marked for a low mismatch warning.");
    } catch (DTIException dtie) {
      fail("Exception not expected in product price mismatch low amount test:" + dtie.toString());
    }

    // Test off price mismatch low warning percentage
    entityTO.setPriceMismatchAllowed(true);
    tktListTO = new ArrayList<TicketTO>();
    dbProdList = new ArrayList<DBProductTO>();
    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "0.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.PERCENT, "10.00"));
    tktListTO.add(createTestTicketTO("AAA01", "180.00"));
    try {
      if (!ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList))
        fail("Ticket not properly marked for a low mismatch warning.");
    } catch (DTIException dtie) {
      fail("Exception not expected in product price mismatch low percentage test:"
          + dtie.toString());
    }
  }

  /**
   * Test tax exempt.
   */
  @Test
  public final void testTaxExempt() {

    // Entity Information
    EntityTO entityTO = new EntityTO();
    entityTO.setTsMac("WDPRONA");
    entityTO.setTsLocation("351");
    entityTO.setPriceMismatchAllowed(false);

    ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

    dbProdList.add(createTestDBProduct("AAA01", "1", "200.00", "10.00", ALLOWMISMATCH,
        DBProductTO.MismatchToleranceType.AMOUNT, "10.00"));

    tktListTO.add(createTestTicketTO("AAA01", "200.00"));
    boolean isTaxExempt = false;

    // Test 1: regular price, not tax exempt
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList, isTaxExempt);
    } catch (DTIException dtie) {
      fail("Should not have received exception on Test 1: on-price, not tax exempt: "
          + dtie.toString());
    }
    
    // Test 2: regular price, tax exempt
    isTaxExempt = true;
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList, isTaxExempt);
      fail("Should  have received exception on Test 2: regular price, tax exempt.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in Test 2: regular price, tax exempt.");
    }
    
    // Test 3: tax reduced price, tax exempt
    tktListTO.clear();
    tktListTO.add(createTestTicketTO("AAA01", "190.00"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList, isTaxExempt);
    } catch (DTIException dtie) {
      fail("Should not have received exception on Test 3: tax reduced price, tax exempt: "
          + dtie.toString());
    }
    
    // Test 4: price too low, tax exempt
    tktListTO.clear();
    tktListTO.add(createTestTicketTO("AAA01", "189.99"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList, isTaxExempt);
      fail("Should  have received exception on Test 4: price too low, tax exempt.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in Test 4: price too low, tax exempt.");
    }
    
    // Test 5: price too high, tax exempt
    tktListTO.clear();
    tktListTO.add(createTestTicketTO("AAA01", "190.01"));
    try {
      ProductRules.validateProductPrice(entityTO, tktListTO, dbProdList, isTaxExempt);
      fail("Should  have received exception on Test 5: price too high, tax exempt.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_PRICE)
        fail("Expected INVALID_PRICE in Test 5: price too high, tax exempt.");
    }
  
    return;
  }
  

  /**
   * Test validate mapped provider ticket types.
   */
  @Test
  public final void testValidateMappedProviderTicketTypes() {

    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();

    // Test 1: All products mapped
    DBProductTO aDBProduct = new DBProductTO();
    aDBProduct.setMappedProviderTktName("AAA 01 Ticket");
    aDBProduct.setMappedProviderTktActive(true);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setMappedProviderTktName("BBB 02 Ticket");
    aDBProduct.setMappedProviderTktActive(true);
    dbProdList.add(aDBProduct);

    try {
      ProductRules.validateMappedProviderTicketTypes(dbProdList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1:  All products mapped: " + dtie.toString());
    }

    // Test 2: Product not mapped
    aDBProduct = new DBProductTO();
    dbProdList.add(aDBProduct);

    try {
      ProductRules.validateMappedProviderTicketTypes(dbProdList);
      fail("Expected exception on Test 2:  Product not mapped.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.MISSING_TICKET_TYPE)
        fail("Expected error MISSING_TICKET_TYPE on Test 2:  Product not mapped.");
    }

    // Test 3: All mapped, one inactive
    dbProdList.clear();
    aDBProduct = new DBProductTO();
    aDBProduct.setMappedProviderTktName("AAA 01 Ticket");
    aDBProduct.setMappedProviderTktActive(true);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setMappedProviderTktName("BBB 02 Ticket");
    aDBProduct.setMappedProviderTktActive(false);
    dbProdList.add(aDBProduct);

    try {
      ProductRules.validateMappedProviderTicketTypes(dbProdList);
      fail("Expected exception on Test 3:  All mapped, one inactive.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INACTIVE_TICKET_TYPE)
        fail("Expected error MISSING_TICKET_TYPE on Test 3:  All mapped, one inactive.");
    }

    return;
  }

  /**
   * Creates the test db product.
   * 
   * @param pdtCode the pdt code
   * @param pdtIdString the pdt id string
   * @param printedPrice the printed price
   * @param mismatchInd the mismatch ind
   * @param mismatchType the mismatch type
   * @param misMatchTol the mis match tol
   * @param tax the tax
   * 
   * @return the DB product to
   */
  private DBProductTO createTestDBProduct(String pdtCode, String pdtIdString, String printedPrice,
      String tax, boolean mismatchInd, DBProductTO.MismatchToleranceType mismatchType,
      String misMatchTol) {

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
   * @param pdtCode the pdt code
   * @param prodPrice the prod price
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
   * @param pdtCode the pdt code
   * @param pdtIdString the pdt id string
   * @param printedPrice the printed price
   * @param active the active
   * @param soldOut the sold out
   * @param startSaleDate the start sale date
   * @param endSaleDate the end sale date
   * 
   * @return the DB product to
   */
  private DBProductTO createTestDBProduct(String pdtCode, String pdtIdString, String printedPrice,
      boolean active, boolean soldOut, Date startSaleDate, Date endSaleDate) {

    DBProductTO dbProd = new DBProductTO();
    dbProd.setPdtCode(pdtCode);
    dbProd.setPdtid(new BigInteger(pdtIdString));
    dbProd.setPrintedPrice(new BigDecimal(printedPrice));
    dbProd.setActive(active);
    dbProd.setSoldOut(soldOut);
    GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar.getInstance();
    gCal.setTime(startSaleDate);
    dbProd.setStartSaleDate(gCal);
    gCal = (GregorianCalendar) GregorianCalendar.getInstance();
    gCal.setTime(endSaleDate);
    dbProd.setEndSaleDate(gCal);

    return dbProd;
  }

}
