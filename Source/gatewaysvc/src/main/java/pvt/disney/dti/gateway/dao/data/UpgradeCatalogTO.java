package pvt.disney.dti.gateway.dao.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import pvt.disney.dti.gateway.data.common.DBProductTO;

/**
 * This class is the representation of the upgrade possibilities. NOTE: THIS IS
 * IN DRAFT; while it compiles and provides the interface calls as a reference,
 * it may not actually WORK! Also, this class is NOT threadsafe. Each thread
 * should create its own copy.
 * 
 * @author lewit019
 * @since 2.17.3
 * 
 */
public class UpgradeCatalogTO implements Serializable {

   private static final long serialVersionUID = 1L;

   /** The list of products that are eligible for upgrade. */
   private ArrayList<DBProductTO> productList = new ArrayList<DBProductTO>();

   /**
    * Adds a specific DLR product to the Upgrade Catalog. Preferred method since
    * it enumerates exactly what is needed in the catalog for a DLR product.
    * STEP 2
    * 
    * @param pdtCode
    *           The DTI product code.
    * @param unitPrice
    *           The sale price of the product (tax inclusive).
    * @param tax
    *           The tax on the product.
    * @param dayClass
    *           The day class of the product (i.e. AP).
    * @param daySubclass
    *           The day subclass of the product (i.e. GOLD)
    * @param plu
    * @return Size of the catalog after the add.
    */
   public int addDLRProduct(String pdtCode, BigDecimal unitPrice, BigDecimal tax, String dayClass, String daySubclass,
            String plu) {

      DBProductTO aProduct = new DBProductTO();

      aProduct.setPdtCode(pdtCode);
      aProduct.setUnitPrice(unitPrice);
      aProduct.setTax(tax);
      aProduct.setDayClass(dayClass);
      aProduct.setDaySubclass(daySubclass);
      aProduct.setMappedProviderTktName(plu);

      productList.add(aProduct);

      return productList.size();

   }

   /**
    * Adds a specific WDW product to the Upgrade Catalog. Preferred method since
    * it enumerates exactly what is needed in the catalog for a WDW product.
    * STEP 2 - S283274 As a client of the DTI, my identity provides a limited
    * set of AP products I have access to.
    * 
    * @param pdtCode
    *           The DTI product code.
    * @param unitPrice
    *           The sale price of the product (tax inclusive).
    * @param tax
    *           The tax on the product.
    * @param dayClass
    *           The day class of the product (i.e. AP).
    * @param daySubclass
    *           The day subclass of the product (i.e. GOLD)
    * @param tktNbr
    *           The ATS internal ticket number of the product
    * @return Size of the catalog after the add.
    */
   public int addWDWProduct(String pdtCode, BigDecimal unitPrice, BigDecimal tax, String dayClass, String daySubclass,
            BigInteger tktNbr) {
      DBProductTO aProduct = new DBProductTO();

      aProduct.setPdtCode(pdtCode);
      aProduct.setUnitPrice(unitPrice);
      aProduct.setTax(tax);
      aProduct.setDayClass(dayClass);
      aProduct.setDaySubclass(daySubclass);
      aProduct.setMappedProviderTktNbr(tktNbr);

      productList.add(aProduct);

      return productList.size();
   }

   /**
    * A less preferred method for adding a product to the catalog, as it relies
    * on the caller to know the correct fields.
    * STEP 2 - S283274 As a client of the DTI, my identity provides a limited
    * set of AP products I have access to.
    * @param aProduct
    * @return Size of the catalog after the add.
    */
   public int addProduct(DBProductTO aProduct) {

      productList.add(aProduct);

      return productList.size();

   }

   /**
    * A simple method to get the product list.
    * 
    * @return Upgrade Catalog Product list array
    */
   public ArrayList<DBProductTO> getProductList() {
      return productList;
   }
   
   /**
    * A simple method to set the product list.
    * @param The product list to set
    */
   public void setProductList(ArrayList<DBProductTO> aProdList) {
      productList = aProdList;
      return;
   }

   /**
    * A simple method to get the product list size/length.
    * 
    * @return Size of the catalog
    */
   public int getProductListCount() {
      return productList.size();
   }

   /**
    * A convenience method to remove a single day subclass of products from the
    * product catalog. STEP 4B WDW
    * 
    * @param subclass
    * @return Size of the catalog
    */
   public int removeDaySubclass(String subclass) {

      if (productList.size() == 0) {
         return productList.size();
      } else {
         ArrayList<String> daySubclassList = new ArrayList<String>();
         daySubclassList.add(subclass);
         return removeDaySubclasses(daySubclassList);
      }

   }

   /**
    * Remove all of the day subclasses from the upgrade catalog. If a null or
    * empty list is passed in, there are no changes to the upgrade catalog. If
    * the catalog is empty, there are no changes to the catalog. STEP 4A WDW
    * 
    * @param daySubclassList
    */
   public int removeDaySubclasses(ArrayList<String> daySubclassList) {

      if ((daySubclassList == null) || (daySubclassList.size() == 0) || (productList.size() == 0)) {
         return productList.size();
      }

      HashSet<String> subclasses = new HashSet<String>();
      for (/* each */String daySubclass : /* in */daySubclassList) {
         subclasses.add(daySubclass);
      }

      for (/* each */DBProductTO aProduct : /* in */productList) {
         if (subclasses.contains(aProduct.getDaySubclass())) {
            productList.remove(aProduct);
         }
      }

      return productList.size();
   }

   /**
    * A simple method to return the list of subclasses presently found in the
    * catalog. Should return a unique list of day subclasses present in upgrade
    * catalog.
    * 
    * @return A list of day subclasses
    */
   public ArrayList<String> getDaySubclasses() {

      ArrayList<String> subclassList = new ArrayList<String>();

      // If there are no products in the catalog, return empty list.
      if (productList.size() == 0) {
         return subclassList;
      }

      HashSet<String> subclasses = new HashSet<String>();
      for (/* each */DBProductTO aProduct : /* in */productList) {
         subclasses.add(aProduct.getDaySubclass());
      }

      if (subclasses.size() != 0) {

         Iterator<String> itr = subclasses.iterator();

         while (itr.hasNext()) {
            subclassList.add(itr.next());
         }

         return subclassList;
      }

      return subclassList;
   }

   /**
    * Removes items from the upgrade catalog where the price is less than the
    * minimum price. WDW STEP 4C
    * 
    * @param minimumPrice
    * @return The size of the catalog.
    */
   public int removeProductsLowerThan(BigDecimal minimumPrice) {

      for (/* each */DBProductTO aProduct : /* in */productList) {

         if (aProduct.getUnitPrice().compareTo(minimumPrice) == -1) {
            productList.remove(aProduct);
         }

      }

      return productList.size();
   }

   /**
    * Only retains those items in the product catalog whose PLUs
    * (mappedProviderTktName) are in the list of upgradable PLUs passed in. DLR
    * STEP 4
    * 
    * @param listOfUpgradeablePLUs
    * @return The size of the catalog.
    */
   public int retainDLRPLUs(ArrayList<String> listOfUpgradeablePLUs) {

      HashSet<String> upgradablePLUset = new HashSet<String>();
      for (/* each */String upgradeablePLU : /* in */listOfUpgradeablePLUs) {
         upgradablePLUset.add(upgradeablePLU);
      }

      for (/* each */DBProductTO aProduct : /* in */productList)
         if (!upgradablePLUset.contains(aProduct.getMappedProviderTktName())) {
            productList.remove(aProduct);
         }

      return productList.size();
   }

}