package pvt.disney.dti.gateway.rules;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.ValueConstants;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * The class ProductRules is responsible for implementing the various product rules such as availability, correct shells, etc.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class ProductRules {

  /** Single character denoting male gender. */
  public static final String MALE_GENDER = "M";
  /** Single character denoting female gender. */
  public static final String FEMALE_GENDER = "F";
  /** Single character denoting female gender. */
  public static final String UNSPECIFIED_GENDER = "U";
  /** String indicating a "Shipping" day class. */
  public static final String SHIPPING = "SHIP";

  /**
   * Validate products can be sold. <BR>
   * Enforces the following rules: <BR>
   * 1. Does the database know about any of the products on the order?<BR>
   * 2. Does the database know about all of the products on the order?<BR>
   * 3. Are all products on the order active? <BR>
   * 4. Are any of the products on the order sold out? <BR>
   * 5. Can all of the products on the order be sold today? <BR>
   * 6. Is there one shipping product on the order? (as of 2.12) <BR>
   * 
   * @param tktListTO
   *            the ticket transfer object list
   * @param dbProdList
   *            the database product list
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void validateProductsAreSellable(
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbProdList) throws DTIException {

    validateProductsAreSellable(tktListTO, dbProdList,
        ValueConstants.TYPE_CODE_SELL);

  }

  /**
   * 
   * @param tktListTO
   * @param dbProdList
   * @param typeCode
   * @throws DTIException
   */
  public static void validateProductsAreSellable(
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbProdList,
      String typeCode) throws DTIException {

    // RULE: Does the database know about any of the products on the order?
    if ((dbProdList == null) || (dbProdList.size() == 0) || (typeCode == null)) throw new DTIException(
        ProductRules.class, DTIErrorCode.INVALID_PRODUCT_CODE,
        "Unable to find any of the order's product codes in the database.");

    if ((tktListTO == null) || (tktListTO.size() == 0)) throw new DTIException(
        ProductRules.class, DTIErrorCode.INVALID_PRODUCT_CODE,
        "Unable to find any of the order's product codes in the order itself.");

    // Create a set of unique product code strings
    HashSet<String> productCodeSet = new HashSet<String>();

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      if (typeCode.equals(ValueConstants.TYPE_CODE_SELL)) if (aTicketTO
          .getProdCode() != null) productCodeSet.add(aTicketTO
          .getProdCode());

      if (typeCode.equals(ValueConstants.TYPE_CODE_UPGRADE)) if (aTicketTO
          .getFromProdCode() != null) productCodeSet.add(aTicketTO
          .getFromProdCode());

    }

    // RULE: Does the database know about all of the products on the order?
    if (productCodeSet.size() != dbProdList.size()) throw new DTIException(
        ProductRules.class, DTIErrorCode.INVALID_PRODUCT_CODE,
        "Unable to find all of the order's product codes in the database.");

    // RULE: Are all products on the order active?
    // RULE: Are any of the products on the order sold out?
    // RULE: Can all of the products on the order be sold today?
    for /* each */(DBProductTO aProduct : /* in */dbProdList) {

      if (!aProduct.isActive()) throw new DTIException(
          BusinessRules.class,
          DTIErrorCode.PRODUCT_UNAVAILABLE,
          "Product " + aProduct.getPdtCode() + " is not active in the database.");

      if (aProduct.isSoldOut()) throw new DTIException(
          ProductRules.class,
          DTIErrorCode.PRODUCT_SOLD_OUT,
          "Product " + aProduct.getPdtCode() + " is sold out in the database.");

      Date startSaleDate = null;
      Date endSaleDate = null;

      // Start dates must be adjusted to 00:00:00 of the date specified.
      if (aProduct.getStartSaleDate() != null) {
        startSaleDate = aProduct.getStartSaleDate().getTime();
        Calendar startDateCalendar = Calendar.getInstance();
        startDateCalendar.setTime(startSaleDate);
        startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startDateCalendar.set(Calendar.MINUTE, 0);
        startDateCalendar.set(Calendar.SECOND, 0);
        startSaleDate = startDateCalendar.getTime();
      }

      // End dates must be adjusted to 23:59:59 of the date specified.
      if (aProduct.getEndSaleDate() != null) {
        endSaleDate = aProduct.getEndSaleDate().getTime();
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(endSaleDate);
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endDateCalendar.set(Calendar.MINUTE, 59);
        endDateCalendar.set(Calendar.SECOND, 59);
        endSaleDate = endDateCalendar.getTime();
      }

      if (!DateTimeRules.isNowWithinDate(startSaleDate, endSaleDate)) throw new DTIException(
          ProductRules.class,
          DTIErrorCode.PRODUCT_EXPIRED,
          "Product " + aProduct.getPdtCode() + " can't be sold because of start and/or end sale dates.");

    }

    return;

  }

  /**
   * RULE: Is there one shipping product on the order? (as of 2.12)
   * 
   * @param tktListTO
   * @param dbProdList
   * @throws DTIException
   * @since 2.12
   */
  public static void validateOneShipProduct(ArrayList<TicketTO> tktListTO,
      ArrayList<DBProductTO> dbProdList) throws DTIException {
    // RULE: Is there one shipping product on the order? (as of 2.12)
    // Includes having more than one shipping product or
    // having one repeat multiple times as errors.
    int numberOfShippingProducts = 0;
    String shippingProduct = null;
    for /* each */(DBProductTO aProduct : /* in */dbProdList) {
      if (aProduct.getDayClass() != null) {
        if (aProduct.getDayClass().compareToIgnoreCase(SHIPPING) == 0) {
          shippingProduct = aProduct.getPdtCode();
          numberOfShippingProducts++;
        }
      }
    }
    if (shippingProduct == null) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_CODE,
          "Reservation does not have at least one product with DAY_CLASS of SHIP (meaning shipping).");
    }
    if (numberOfShippingProducts > 1) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_CODE,
          "Reservation has more than one product with DAY_CLASS of SHIP (meaning shipping).");
    }
    numberOfShippingProducts = 0;
    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {
      if (aTicketTO.getProdCode().compareTo(shippingProduct) == 0) {
        numberOfShippingProducts++;
      }
    }
    if (numberOfShippingProducts > 1) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_CODE,
          "Shipping product " + shippingProduct + " appears multiple times on this reservation.");
    }

    return;
  }

  /**
   * Validates that all products have a valid quantity. <BR>
   * Enforces the following rules: <BR>
   * 1. Does the product have a quantity of 1 or greater?<BR>
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void validateNonZeroProductQuantity(
      ArrayList<TicketTO> tktListTO) throws DTIException {

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      if ((aTicketTO.getProdQty() == null) || (aTicketTO.getProdQty()
          .longValue() < 1)) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.INVALID_PRODUCT_QTY,
            "Product " + aTicketTO.getProdCode() + " can't have a null or < 1 quantity.");
      }
    }

    return;
  }

  /**
   * For each product, determine if it has a valid sales relationship in either the entity X product cross-reference or the entity X product group cross-reference. If one of those products does not have that relationship, then throw an
   * exception.<BR>
   * Enforces the following rules: <BR>
   * 1. Does entity have permission in the database to sell all of the order products?
   * 
   * @param dbProdList
   *            the db prod list
   * @param entityTO
   *            the entity transfer object
   * @param allowedPdtIdList
   *            the allowed product id list
   * 
   * @throws DTIException
   *             the DTI exception
   */
  public static void validateEntityCanSellProducts(EntityTO entityTO,
      ArrayList<DBProductTO> dbProdList,
      ArrayList<BigInteger> allowedPdtIdList) throws DTIException {

    if ((allowedPdtIdList == null) || (allowedPdtIdList.size() == 0)) throw new DTIException(
        ProductRules.class,
        DTIErrorCode.PRODUCT_NOT_PERMITTED,
        "Entity " + entityTO.getTsMac() + "/" + entityTO
            .getTsLocation() + " doesn't have any products or product groups related to it to sell.");

    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {

      if (!allowedPdtIdList.contains(aDBProduct.getPdtid())) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRODUCT_NOT_PERMITTED,
            "Product " + aDBProduct.getPdtCode() + " can't be sold because entity " + entityTO
                .getTsMac() + "/" + entityTO.getTsLocation() + " doesn't have it as a related product or a product in a related product group.");

      }
    }

    return;
  }

  public static void validateEntityCanUpgradeProducts(EntityTO entityTO,
      ArrayList<DBProductTO> dbProdList,
      ArrayList<BigInteger> allowedPdtIdList) throws DTIException {

    if ((allowedPdtIdList == null) || (allowedPdtIdList.size() == 0)) throw new DTIException(
        ProductRules.class,
        DTIErrorCode.PRODUCT_NOT_PERMITTED,
        "Entity " + entityTO.getTsMac() + "/" + entityTO
            .getTsLocation() + " doesn't have any products or product groups related to it to upgrade.");

    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {

      if (!allowedPdtIdList.contains(aDBProduct.getPdtid())) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRODUCT_NOT_PERMITTED,
            "Product " + aDBProduct.getPdtCode() + " can't be upgraded because entity " + entityTO
                .getTsMac() + "/" + entityTO.getTsLocation() + " doesn't have it as a related product or a product in a related product group.");

      }
    }

    return;

  }

  /**
   * Validate product price.<BR>
   * 
   * @param entityTO
   *            the entity transfer object.
   * @param tktListTO
   *            the ticket transfer object list.
   * @param dbProdList
   *            the database prod list
   * 
   * @return PriceMismatchType
   * 
   * @throws DTIException
   *             should any rule fail.
   * 
   * @see ProductRules#validateProductPrice(EntityTO, ArrayList, ArrayList, String)
   * 
   */
  public static boolean validateProductPrice(EntityTO entityTO,
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbProdList) throws DTIException {

    return validateProductPrice(entityTO, tktListTO, dbProdList, false);

  }

  /**
   * Validate product price.<BR>
   * Enforces the following rules: <BR>
   * 1. Can the entity sell products off price? <BR>
   * 2. Can the products on the order be sold off price? <BR>
   * 3. Is the price mismatch configuration on the product complete in the database?<BR>
   * 4. If allowed, is the price mismatch within the acceptable range?
   * 
   * @param entityTO
   *            the entity transfer object.
   * @param tktListTO
   *            the ticket transfer object list.
   * @param dbProdList
   *            the database prod list
   * @param isTaxExempt
   *            true if the seller is tax exempt
   * 
   * @return PriceMismatchType
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static boolean validateProductPrice(EntityTO entityTO,
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbProdList,
      boolean isTaxExempt) throws DTIException {

    boolean isPriceMismatched = false;
    boolean entityAllows = entityTO.isPriceMismatchAllowed();

    // Put DB products in a HashMap for quick access
    HashMap<String, DBProductTO> dbProdMap = new HashMap<String, DBProductTO>();

    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
      dbProdMap.put(aDBProduct.getPdtCode(), aDBProduct);
    }

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      String ticketPdtCode = aTicketTO.getProdCode();
      DBProductTO dbProduct = dbProdMap.get(ticketPdtCode);

      BigDecimal validPrice;
      if (isTaxExempt) {
        validPrice = dbProduct.getPrintedPrice().subtract(dbProduct.getTax());
      }
      else { 
        validPrice = dbProduct.getPrintedPrice();
      }

      // As of 2.9 - if no price is provided, a price of zero is presumed.
      if (aTicketTO.getProdPrice() == null) {
        aTicketTO.setProdPrice(new BigDecimal(0.0));
      }

      if (aTicketTO.getProdPrice().compareTo(validPrice) != 0) {

        if (!entityAllows) throw new DTIException(
            ProductRules.class,
            DTIErrorCode.INVALID_PRICE,
            "Entity " + entityTO.getTsMac() + "/" + entityTO
                .getTsLocation() + " disallowed from selling product (" + dbProduct
                .getPdtCode() + " at " + validPrice.toString() + ") off price (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");

        if (!dbProduct.isPriceMismatchAllowed()) throw new DTIException(
            ProductRules.class,
            DTIErrorCode.INVALID_PRICE,
            "Product " + dbProduct.getPdtCode() + " (at " + validPrice
                .toString() + ") can't be sold off price (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");

        if ((dbProduct.getMisMatchTolType() == DBProductTO.MismatchToleranceType.UNDEFINED) || (dbProduct
            .getMisMatchTol() == null)) throw new DTIException(
            ProductRules.class,
            DTIErrorCode.INVALID_PRICE,
            "Product " + dbProduct.getPdtCode() + " can't be sold off price because of an incomplete mismatch set-up in table.");

        boolean ticketMismatched = validatePriceMismatch(aTicketTO,
            dbProduct, validPrice, isTaxExempt);
        if (ticketMismatched) {
          isPriceMismatched = true;
        }

      }

    }

    return isPriceMismatched;
  }

  /**
   * Validates the Upgrade product price.<BR>
   * Enforces the following rules: <BR>
   * 1. Is the from product price the standard retail price or unit price. <BR>
   * 
   * @param entityTO
   *            the entity transfer object.
   * @param tktListTO
   *            the ticket transfer object list.
   * @param dbFromProdList
   *            the database prod list
   * @param isTaxExempt
   *            true if the seller is tax exempt
   * 
   *            IMPORTANT NOTE: In DTI product administration, there are a couple of exceptions regarding how product pricing is set up. The "printed price" used for sales validations sometimes represents the "retail" value of the sale
   *            (i.e. for Disney Stores). Guests must pay upgrades against the Unit/Net price or the Standard Retail Price, only. Actual retail prices are irrelevant in the upgrade process. (JTL per Craig Stuart)
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void validateUpgradeProductPrice(EntityTO entityTO,
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbFromProdList) throws DTIException {

    // Put DB products in a HashMap for quick access
    HashMap<String, DBProductTO> dbProdMap = new HashMap<String, DBProductTO>();

    for /* each */(DBProductTO aDBProduct : /* in */dbFromProdList) {
      dbProdMap.put(aDBProduct.getPdtCode(), aDBProduct);
    }

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      String ticketFromPdtCode = aTicketTO.getFromProdCode();
      BigDecimal ticketFromPdtPrice = aTicketTO.getFromPrice();
      DBProductTO dbFromProduct = dbProdMap.get(ticketFromPdtCode);
      BigDecimal dbFromUnitPrice = dbFromProduct.getUnitPrice();
      BigDecimal dbFromSRPrice = dbFromProduct.getStandardRetailPrice();

      if (dbFromUnitPrice == null) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.DTI_CONFIG_ERROR,
            "Product code " + dbFromProduct.getPdtCode() + " has no Unit Price set in the database.  Please refer to Ticket Administration.");
      }

      if (dbFromSRPrice == null) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.DTI_CONFIG_ERROR,
            "Product code " + dbFromProduct.getPdtCode() + " has no Standard Retail Price set in the database.  Please refer to Ticket Administration.");
      }

      if ((ticketFromPdtPrice.compareTo(dbFromUnitPrice) != 0) && (ticketFromPdtPrice
          .compareTo(dbFromSRPrice) != 0)) {

        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.INVALID_PRICE,
            "Entity " + entityTO.getTsMac() + "/" + entityTO
                .getTsLocation() + " cannot upgrade from product " + dbFromProduct
                .getPdtCode() + " with from price of " + ticketFromPdtPrice
                .toString() + ".  Valid from prices for upgrade are: " + dbFromUnitPrice
                .toString() + " (NET) and " + dbFromSRPrice
                .toString() + " (Standard Retail Price).");
      }

    }

    return;
  }

  /**
   * Validates if a price falls correctly within the price mismatch range. Note: Mismatching price ranges are inclusive.
   * 
   * @param dbProduct
   *            the db product
   * @param aTicketTO
   *            the a ticket transfer object
   * @param isTaxExempt
   *            true if the seller is tax exempt
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  private static boolean validatePriceMismatch(TicketTO aTicketTO,
      DBProductTO dbProduct, BigDecimal validPrice, boolean isTaxExempt) throws DTIException {

    boolean isMismatchedOnPrice = false;

    BigDecimal ticketPrice = aTicketTO.getProdPrice();

    DBProductTO.MismatchToleranceType mismatchType = dbProduct
        .getMisMatchTolType();
    BigDecimal lowestPriceAllowed = null;
    BigDecimal highestPriceAllowed = null;
    BigDecimal basePriceAmount = validPrice;
    BigDecimal variance = new BigDecimal("0.0");

    if (mismatchType == DBProductTO.MismatchToleranceType.AMOUNT) {
      variance = dbProduct.getMisMatchTol();
    }
    else {
      BigDecimal percent = dbProduct.getMisMatchTol().divide(
          new BigDecimal("100.0"));
      variance = basePriceAmount.multiply(percent);
    }

    highestPriceAllowed = basePriceAmount.add(variance);
    lowestPriceAllowed = basePriceAmount.subtract(variance);

    // Is price too high?
    if (ticketPrice.compareTo(highestPriceAllowed) > 0) {

      if (isTaxExempt) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRICE_MISMATCH_ERROR_HI,
            "Product " + dbProduct.getPdtCode() + " can't be sold for more than (tax exempt adjusted) " + highestPriceAllowed
                .toString() + " (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");
      }
      else {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRICE_MISMATCH_ERROR_HI,
            "Product " + dbProduct.getPdtCode() + " can't be sold for more than " + highestPriceAllowed
                .toString() + " (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");
      }
    }

    // Is price too low?
    if (ticketPrice.compareTo(lowestPriceAllowed) < 0) {

      if (isTaxExempt) {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRICE_MISMATCH_ERROR_LO,
            "Product " + dbProduct.getPdtCode() + " can't be sold for less than (tax exempt adjusted) " + highestPriceAllowed
                .toString() + " (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");
      }
      else {
        throw new DTIException(
            ProductRules.class,
            DTIErrorCode.PRICE_MISMATCH_ERROR_LO,
            "Product " + dbProduct.getPdtCode() + " can't be sold for less than " + highestPriceAllowed
                .toString() + " (sale at " + aTicketTO
                .getProdPrice().toString() + " was attempted.)");
      }
    }

    isMismatchedOnPrice = true;

    return isMismatchedOnPrice;
  }

  /**
   * Validate mapped provider ticket types. <BR>
   * Enforces the following rules: <BR>
   * 1. Does the product have an active ticket provider product affiliated with it?
   * 
   * @param dbProdList
   *            the db prod list
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void validateMappedProviderTicketTypes(
      ArrayList<DBProductTO> dbProdList) throws DTIException {

    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {

      if (aDBProduct.getMappedProviderTktName() == null) throw new DTIException(
          ProductRules.class,
          DTIErrorCode.MISSING_TICKET_TYPE,
          "Product " + aDBProduct.getPdtCode() + " is missing its reference to a provider ticket type.");

      if (!aDBProduct.isMappedProviderTktActive()) throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INACTIVE_TICKET_TYPE,
          "Product " + aDBProduct.getPdtCode() + " is mapped to inactive ticket type " + aDBProduct
              .getMappedProviderTktName());
    }

    return;

  }

  /**
   * Validate that those ticket products requiring demographics have them. <BR>
   * Enforces the following rules: <BR>
   * 1. Does the product require demographics? If so, are they provided in the request? <BR>
   * 2. Do the number of demographics provided equate to the quantity of products? <BR>
   * 3. If a product is NOT supposed to have demographics, it must not have them. <BR>
   * 4. Ensure Gender (a required field) is of an acceptable value range.
   * 
   * @param tktListTO
   *            the ticket transfer object list
   * @param dbProdList
   *            the database product list
   * @throws DTIException
   *             should any rule fail.
   * @since 2.9
   */
  public static void validateProductsHaveDemographics(
      ArrayList<TicketTO> tktListTO, ArrayList<DBProductTO> dbProdList) throws DTIException {

    // Set up a hash-map reference of products to booleans - are the
    // demographics
    // required for this product.
    HashMap<String, Boolean> aProdMap = new HashMap<String, Boolean>();

    for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
      Boolean bool = aDBProduct.areDemographicsRequired();
      aProdMap.put(aDBProduct.getPdtCode(), bool);
    }

    // Loop through each "ticket" and validate demographics are present, and
    // in
    // the right numbers.
    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      String aProdCode = aTicketTO.getProdCode();
      Boolean demographicsRequired = aProdMap.get(aProdCode);
      ArrayList<DemographicsTO> demoList = aTicketTO.getTicketDemoList();

      if (demographicsRequired.booleanValue()) {

        // Check for presence of demographics.
        if ((demoList == null) || (demoList.size() == 0)) {
          throw new DTIException(
              ProductRules.class,
              DTIErrorCode.INVALID_TICKET_DEMOGRAPHICS,
              "Product " + aProdCode + " does not have ticket level demographics supplied, and such are required.");
        }

        // Check for quantity of demographics.
        int tktQtyCount = aTicketTO.getProdQty().intValue();
        int demoCount = demoList.size();

        if (tktQtyCount != demoCount) {
          throw new DTIException(
              ProductRules.class,
              DTIErrorCode.INVALID_TICKET_DEMOGRAPHICS,
              "Product " + aProdCode + " has an invalid number of demographics supplied (" + demoCount + "); does not match prod quantity (" + tktQtyCount + ").");
        }
      }
      else { // Demographics NOT required or allowed

        if (demoList != null) {
          if (demoList.size() > 0) {
            throw new DTIException(
                ProductRules.class,
                DTIErrorCode.INVALID_TICKET_DEMOGRAPHICS,
                "Product " + aProdCode + " does not permit demographics to be supplied, " + "and demographics are present.");
          }
        }
      } // End if demo required

      /** Validate demographics internals. */
      for /* each */(DemographicsTO aDemo : /* in */demoList) {

        // Gender must be male, female, or unspecified by business rule.
        // Note, that when building to TO, any invalid value will default to
        // NOTPRESENT.
        if (aDemo.getGenderType() == GenderType.NOTPRESENT) {
          throw new DTIException(
              ProductRules.class,
              DTIErrorCode.INVALID_TICKET_DEMOGRAPHICS,
              "On Product " + aProdCode + ", ticket demographics supplied did not meet required " + " values of M or F or U (unspecified).");
        }

      }

    } // tktListTO

    return;
  }

  /**
   * Validates if upgrading one entitlement to a different entitlement is valid; Is the to MSRP equal or greater than the from MSRP?
   * 
   * 2.10
   * 
   * @param dbProdList
   *            the db prod list
   * 
   * @throws DTIException
   *             should any rule fail.
   */
  public static void validateUpgradePricing(String fromProdCode,
      BigDecimal fromProdPrice, String toProdCode,
      BigDecimal toProdPrice, BigDecimal upgrdPrice) throws DTIException {

    // RULE: FROM price cannot be greater than TO price. (Corrected by JTL
    // in
    // 2.11)
    if (fromProdPrice.compareTo(toProdPrice) > 0) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_UPGRADE_PATH,
          "From product: " + fromProdCode + " at " + fromProdPrice
              .toString() + " is not allowed to be upgraded to product: " + toProdCode + " at " + toProdPrice
              .toString() + " because the upgrade is to a product of lesser value.");
    }

    // RULE: fromProdPrice + upgrdPrice == prodPrice
    if (toProdPrice.compareTo(fromProdPrice.add(upgrdPrice)) != 0) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_UPGRADE_PATH,
          "From product (" + fromProdCode + ") price of " + fromProdPrice
              .toString() + " plus upgrade amount " + upgrdPrice
              .toString() + " does not equal target product code (" + toProdCode + ") price of " + toProdPrice
              .toString() + ".");
    }

    return;
  }

  /**
   * Validate that the products listed as targeted upgrades are, in fact, valid.
   * 
   * @throws DTIException
   */
  public static void validateUpgradeProducts(
      ArrayList<DBProductTO> dbUpgrdProdList) throws DTIException {

    if (dbUpgrdProdList.size() == 0) {
      throw new DTIException(ProductRules.class,
          DTIErrorCode.INVALID_PRODUCT_CODE,
          "Upgrade product list from database is empty: invalid FROM product(s).");
    }

    return;
  }

  /**
   * Validate that the telephone is present on WDW Demo tickets.
   * 
   * @param tktListTO
   * @throws DTIException
   */
  public static void validateWdwTicketDemo(ArrayList<TicketTO> tktListTO) throws DTIException {
    
    for (/* each */TicketTO aTicketTO : /* in */tktListTO) {
      
      // Telephone 
      if (aTicketTO.getTicketDemoList().size() != 0) {
        
        for /* each */(DemographicsTO aDemoTO : /* in */aTicketTO.getTicketDemoList()) {
          
          String park = "WDW"; 
          boolean optional = false;
          boolean required = true;
          BigInteger itemNumber = aTicketTO.getTktItem();

          // FirstName (XSD required, 1 - 15 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              20, true, itemNumber);

          
          // LastName (XSD required, 1 - 20 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              20, true, itemNumber);
          
          // DateOfBirth (required, date) 
          if (aDemoTO.getDateOfBirth() == null) {
            throwFieldMissExcpt(park,"DateOfBirth",aTicketTO.getTktItem());
          }
          
          // Gender (required, 1 char)
          validateStringDemo( park, "Gender", aDemoTO.getGender(), 1, 1, required, itemNumber); 
          
          // Addr1 (required, 1 - 35 char)
          validateStringDemo( park, "Addr1", aDemoTO.getAddr1(), 1, 35, required, itemNumber);
          
          // Addr2 (optional, 1 - 35 char)
          validateStringDemo( park, "Addr2", aDemoTO.getAddr2(), 1, 35, optional, itemNumber);
          
          // City (required, 1 - 20 char)
          validateStringDemo( park, "City", aDemoTO.getCity(), 1, 20, required, itemNumber);
          
          // State (optional, 2 char)
          validateStringDemo( park, "State", aDemoTO.getState(), 2, 2, optional, itemNumber);          
          
          // Zip (required, 1 - 10 char)
          validateStringDemo( park, "Zip", aDemoTO.getZip(), 1, 10, required, itemNumber);
          
          // Country (required, 1 - 20 char) 
          validateStringDemo( park, "Country", aDemoTO.getCountry(), 1, 20, required, itemNumber);
          
          // Telephone (required, 1 - 14 char)
          // Validate that if other ticket demographics have been provided, phone has been provided, as well.
          // As of 2.16.1 APMP JTL          
          validateStringDemo( park, "Telephone", aDemoTO.getTelephone(), 1, 14, required, itemNumber);
          
          // Email (optional, 1 - 50 char) 
          validateStringDemo( park, "Email", aDemoTO.getEmail(), 1, 50, optional, itemNumber);
          
          // OptInSolicit (required, enum)
          if (aDemoTO.getOptInSolicit() == null) {
            throwFieldMissExcpt(park,"OptInSolicit",aTicketTO.getTktItem());
          }
          
        }
      }
    }

    return;
  }
  
  /**
   * Validate that the telephone is present on DLR Demo tickets.
   * 
   * @param tktListTO
   * @throws DTIException
   */
  public static void validateDlrTicketDemo(ArrayList<TicketTO> tktListTO) throws DTIException {
    
    for (/* each */TicketTO aTicketTO : /* in */tktListTO) {
      
      // Telephone 
      if (aTicketTO.getTicketDemoList().size() != 0) {
        
        for /* each */(DemographicsTO aDemoTO : /* in */aTicketTO.getTicketDemoList()) {
          
          String park = "DLR"; 
          boolean optional = false;
          boolean required = true;
          BigInteger itemNumber = aTicketTO.getTktItem();

          // FirstName (XSD required, 1 - 15 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              20, true, itemNumber);

          
          // LastName (XSD required, 1 - 20 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              20, true, itemNumber);
          
          // DateOfBirth (required, date) 
          if (aDemoTO.getDateOfBirth() == null) {
            throwFieldMissExcpt(park,"DateOfBirth",aTicketTO.getTktItem());
          }
          
          // Gender (required, 1 char)
          validateStringDemo( park, "Gender", aDemoTO.getGender(), 1, 1, required, itemNumber); 
          
          // Addr1 (required, 1 - 35 char)
          validateStringDemo( park, "Addr1", aDemoTO.getAddr1(), 1, 35, required, itemNumber);
          
          // Addr2 (optional, 1 - 35 char)
          validateStringDemo( park, "Addr2", aDemoTO.getAddr2(), 1, 35, optional, itemNumber);
          
          // City (required, 1 - 20 char)
          validateStringDemo( park, "City", aDemoTO.getCity(), 1, 20, required, itemNumber);
          
          // State (optional, 2 char)
          validateStringDemo( park, "State", aDemoTO.getState(), 2, 2, optional, itemNumber);          
          
          // Zip (required, 1 - 10 char)
          validateStringDemo( park, "Zip", aDemoTO.getZip(), 1, 10, required, itemNumber);
          
          // Country (required, 2 char) 
          // RULE: If tickets have demographics, the country code must be of
          // length = 2. This is an oddity in the galaxy specification, but
          // still compliant to the ISO codes.
          validateStringDemo( park, "Country", aDemoTO.getCountry(), 2, 2, required, itemNumber);          
          
          // Telephone (required, 1 - 14 char)
          validateStringDemo( park, "Telephone", aDemoTO.getTelephone(), 1, 14, optional, itemNumber);
          
          // Email (optional, 1 - 50 char) 
          validateStringDemo( park, "Email", aDemoTO.getEmail(), 1, 50, optional, itemNumber);
          
          // OptInSolicit (required, enum)
          if (aDemoTO.getOptInSolicit() == null) {
            throwFieldMissExcpt(park,"OptInSolicit",aTicketTO.getTktItem());
          }
          
        }
      }
    }

    return;
  }
  
  /**
   * Validate that the telephone is present on HKDL Demo tickets.
   * 
   * @param tktListTO
   * @throws DTIException
   */
  public static void validateHkdTicketDemo(ArrayList<TicketTO> tktListTO) throws DTIException {
    
    for (/* each */TicketTO aTicketTO : /* in */tktListTO) {
      
      // Telephone 
      if (aTicketTO.getTicketDemoList().size() != 0) {
        
        for /* each */(DemographicsTO aDemoTO : /* in */aTicketTO.getTicketDemoList()) {
          
          String park = "HKD"; 
          boolean optional = false;
          boolean required = true;
          BigInteger itemNumber = aTicketTO.getTktItem();

          // FirstName (XSD required, 1 - 35 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              35, required, itemNumber);

          
          // LastName (XSD required, 1 - 35 char)
          validateStringDemo( park, "LastName", aDemoTO.getLastName(), 1, 
              35, required, itemNumber);
          
          // Gender (optional, 1 - 10 char)
          validateStringDemo( park, "Gender", aDemoTO.getGender(), 1, 10, optional, itemNumber); 
          
          // Email (optional, 1 - 50 char) 
          validateStringDemo( park, "Email", aDemoTO.getEmail(), 1, 50, optional, itemNumber);
      
          // DateOfBirth (optional, date) (Type-checked by XSD)
          
          // Cell Phone (optional, 1 - 35 char)
          validateStringDemo( park, "CellPhone", aDemoTO.getCellPhone(), 1, 15, optional, itemNumber);
          
          // Telephone (optional, 1 - 50 char)
          validateStringDemo( park, "Telephone", aDemoTO.getTelephone(), 1, 50, optional, itemNumber);
          
          // SellerRef (optional, 1 - 50 char)
          validateStringDemo( park, "SellerRef", aDemoTO.getCellPhone(), 1, 50, optional, itemNumber);
           
        }
      }
    }

    return;
  }
  
  /**
   * 
   * @throws DTIException
   */
  private static void throwFieldMissExcpt(String park, String field, BigInteger item) throws DTIException {
    
    throw new DTIException(
        ProductRules.class,
        DTIErrorCode.INVALID_MSG_CONTENT,
        "For " + park + ", " + field + " is a mandatory field for TktDemoData.  TktItem failing was " + item + ".");
    
  }
  
  /**
   * 
   * @param park
   * @param fieldName
   * @param fieldValue
   * @param minLength
   * @param maxLength
   * @param isRequired
   * @param item
   * @throws DTIException
   */
  private static void validateStringDemo(String park, String fieldName, String fieldValue, int minLength, 
      int maxLength, boolean isRequired, BigInteger item) throws DTIException {
    
    if ((isRequired) && (fieldValue == null)) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "For " + park + ", " + fieldName + " is a mandatory field for TktDemoData.  TktItem failing was " + item + ".");
    } else if (!isRequired) {
      return;
    }
    
    int strLen = fieldValue.length();
    
    if ((strLen > maxLength) || (strLen < minLength)) {
      throw new DTIException(
          ProductRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "For " + park + ", " + fieldName + " has invalid length for TktDemoData (expected "+ minLength +
          " to " + maxLength + ", but found a length of " + strLen + ").");
    }
    
    return;
    
  }

}
