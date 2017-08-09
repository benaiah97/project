package pvt.disney.dti.gateway.dao.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;

import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * This class is the representation of the upgrade possibilities. This class is
 * NOT threadsafe. Each thread should create its own copy.
 * 
 * @author lewit019
 * @since 2.17.3
 * 
 */
public class UpgradeCatalogTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant WMK. */
	private static final String WMK = "71";

	/** The Constant WEC. */
	private static final String WEC = "72";

	/** The Constant WST. */
	private static final String WST = "73";

	/** The Constant WPI. */
	@SuppressWarnings("unused")
	private static final String WPI = "74";

	/** The Constant WTL. */
	private static final String WTL = "75";

	/** The Constant WDI. */
	@SuppressWarnings("unused")
	private static final String WDI = "76";

	/** The Constant WRC. */
	@SuppressWarnings("unused")
	private static final String WRC = "77";

	/** The Constant WBB. */
	private static final String WBB = "78";

	/** The Constant WDR. */
	@SuppressWarnings("unused")
	private static final String WDR = "79";

	/** The Constant WTS. */
	@SuppressWarnings("unused")
	private static final String WTS = "80";

	/** The Constant WNR. */
	@SuppressWarnings("unused")
	private static final String WNR = "84";

	/** The Constant WSC. */
	private static final String WSC = "85";

	/** The Constant WAK. */
	private static final String WAK = "86";

	/** The Constant WFP. */
	@SuppressWarnings("unused")
	private static final String WFP = "88";

	/** The Constant WCQ. */
	@SuppressWarnings("unused")
	private static final String WCQ = "89";

	/** The Constant WRS. */
	@SuppressWarnings("unused")
	private static final String WRS = "90";

	/** The Constant WSG. */
	@SuppressWarnings("unused")
	private static final String WSG = "91";

	/** The Constant WOR. */
	@SuppressWarnings("unused")
	private static final String WOR = "92";

	/** The Constant WDW. */
	@SuppressWarnings("unused")
	private static final String WDW = "93";

	/** The Constant WEA. */
	@SuppressWarnings("unused")
	private static final String WEA = "83";

	/** The Constant WSA. */
	@SuppressWarnings("unused")
	private static final String WSA = "81";

	/** The Constant WMA. */
	@SuppressWarnings("unused")
	private static final String WMA = "82";

	/** The Constant WAA. */
	@SuppressWarnings("unused")
	private static final String WAA = "87";

	/** The Constant PLAT. */
	private static final String PLAT = "PLAT";

	/** The Constant PLATPLUS. */
	private static final String PLATPLUS = "PLATPLUS";

	/** The Constant EPCTAFT4. */
	private static final String EPCTAFT4 = "EPCTAFT4";

	/** The Constant GOLD. */
	private static final String GOLD = "GOLD";

	/** The Constant SILVER. */
	private static final String SILVER = "SILVER";

	/** The Constant WEEKDAY. */
	private static final String WEEKDAY = "WEEKDAY";

	/** The Constant WP. */
	private static final String WP = "WP";

	/** The Constant WPAFT2. */
	private static final String WPAFT2 = "WPAFT2";

	/** The plat list. */
	private static String PLAT_LIST[] = { WTL, WBB, WSC };

	/** The epctaft4 list. */
	private static String EPCTAFT4_LIST[] = { WTL, WBB, WSC, WMK, WST, WAK };

	/** The gold list. */
	private static String GOLD_LIST[] = { WTL, WBB, WSC };

	/** The silver list. */
	private static String SILVER_LIST[] = { WTL, WBB, WSC };

	/** The weekday list. */
	private static String WEEKDAY_LIST[] = { WTL, WBB, WSC };

	/** The wp list. */
	private static String WP_LIST[] = { WEC, WSC, WMK, WST, WAK };

	/** The wpaft2 list. */
	private static String WPAFT2_LIST[] = { WEC, WSC, WMK, WST, WAK };
   
   /**
	 * Properties file
	 */
	private Properties props;
   
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
    * on the caller to know the correct fields. STEP 2 - S283274 As a client of
    * the DTI, my identity provides a limited set of AP products I have access
    * to.
    * 
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
    * 
    * @param The
    *           product list to set
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

      ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

      if (productList.size() == 0) {
         return productList.size();
      } else {

         for (/* each */DBProductTO aProduct : /* in */productList) {
            if (!subclass.equalsIgnoreCase(aProduct.getDaySubclass())) {
               newList.add(aProduct);
            }
         }

         productList = newList;

         return productList.size();
      }

   }

   /**
    * Keep all of the day subclasses in the upgrade catalog that are also in the
    * day subclass list. If a null or empty list is passed in, there are no
    * changes to the upgrade catalog. If the catalog is empty, there are no
    * changes to the catalog. STEP 4A WDW
    * 
    * @param daySubclassList
    */
   public int keepDaySubclasses(ArrayList<String> daySubclassList) {

      ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

      if ((daySubclassList == null) || (daySubclassList.size() == 0) || (productList.size() == 0)) {
         return productList.size();
      }

      HashSet<String> subclasses = new HashSet<String>();
      for (/* each */String daySubclass : /* in */daySubclassList) {
         subclasses.add(daySubclass);
      }

      for (/* each */DBProductTO aProduct : /* in */productList) {
         if (subclasses.contains(aProduct.getDaySubclass())) {
            newList.add(aProduct);
         }
      }

      productList = newList;

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

      ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

      for (/* each */DBProductTO aProduct : /* in */productList) {

         if (aProduct.getUnitPrice().compareTo(minimumPrice) > -1) {
            newList.add(aProduct);
         }

      }

      productList = newList;

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

      ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

      HashSet<String> upgradablePLUset = new HashSet<String>();
      for (/* each */String upgradeablePLU : /* in */listOfUpgradeablePLUs) {
         upgradablePLUset.add(upgradeablePLU);
      }

      for (/* each */DBProductTO aProduct : /* in */productList) {
         if (upgradablePLUset.contains(aProduct.getMappedProviderTktName())) {
            newList.add(aProduct);
         }
      }

      productList = newList;

      return productList.size();
   }
   
   /**
    * Retain positive ap upgrades.
    *
    * @param unitPrice the unit price
    * @return the int
    */
   public int retainPositiveApUpgrades(BigDecimal standardRetailPrice){

	      ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

	      for (/* each */DBProductTO aProduct : /* in */productList) {
	    	  if(aProduct.getUnitPrice().subtract(standardRetailPrice).compareTo(BigDecimal.ZERO)>0){
	    		  newList.add(aProduct);
	    	  }
	      }
	      productList = newList;
	      return productList.size();
	   }
   
   /**
    * Retain site details, will check for disqualification rule for each of the subclass in the productList 
    *
    * @param siteList the site list
    * @return the int
    */
	public int disqualifyProduct(ArrayList<String> siteList) {

		// read the details from property file
	   //	readProperties(); 
		ArrayList<DBProductTO> newList = new ArrayList<DBProductTO>();

		HashSet<String> usageSiteSet = new HashSet<String>();
		for (/* each */String siteDetail : /* in */siteList) {
			usageSiteSet.add(siteDetail);
		}

		for (/* each */DBProductTO aProduct : /* in */productList) {

			// iterate through each of the productList to check the subclass
			switch (aProduct.getDaySubclass()) {

			// PLAT
			case PLAT:
				if (checkProduct(usageSiteSet, PLAT_LIST)) {
					newList.add(aProduct);
				}
				break;

			// PLAT PLUS
			case PLATPLUS:
				newList.add(aProduct);
				break;

			// EPCTAFT4
			case EPCTAFT4:
				if (checkProduct(usageSiteSet, EPCTAFT4_LIST)) {
					newList.add(aProduct);
				}
				break;

			// GOLD
			case GOLD:
				if (checkProduct(usageSiteSet, GOLD_LIST)) {
					newList.add(aProduct);
				}
				break;

			// SILVER
			case SILVER:
				if (checkProduct(usageSiteSet, SILVER_LIST)) {
					newList.add(aProduct);
				}
				break;

			// WEEKDAY
			case WEEKDAY:
				if (checkProduct(usageSiteSet, WEEKDAY_LIST)) {
					newList.add(aProduct);
				}
				break;

			// WP
			case WP:
				if (checkProduct(usageSiteSet, WP_LIST)) {
					newList.add(aProduct);
				}
				break;

			// WPAFT2
			case WPAFT2:
				if (checkProduct(usageSiteSet, WPAFT2_LIST)) {
					newList.add(aProduct);
				}
				break;
			}

		}
		productList = newList;
		return productList.size();
	}

	
	/**
	 * Check product.
	 *
	 * @param usageSiteSet the usage site set
	 * @param siteList the site list
	 * @return true, if successful
	 */
	private boolean checkProduct(HashSet<String> usageSiteSet, String[] siteList) {
		int count = 0;
		boolean disqualifyFlag=false;
		for (String site : siteList) {
			if (usageSiteSet.contains(site)) {
				count++;
			}
		}
		if (count == 0) {
			disqualifyFlag=true;
		}
		return disqualifyFlag;
	}
	
	
	/**
	 * Read properties.
	 */
	private void readProperties() {
		
		// Read Properties 
		ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
		props = ResourceLoader.convertResourceBundleToProperties(rb);
		
		String plat = PropertyHelper.readPropsValue("PLAT_LIST", props, null);
		String epctaft4 = PropertyHelper.readPropsValue("EPCTAFT4_LIST", props, null);
		String gold = PropertyHelper.readPropsValue("GOLD_LIST", props, null);
		String silver = PropertyHelper.readPropsValue("SILVER_LIST", props, null);
		String weekday = PropertyHelper.readPropsValue("WEEKDAY_LIST", props, null);
		String wp = PropertyHelper.readPropsValue("WP_LIST", props, null);
		String wpaft2 = PropertyHelper.readPropsValue("WPAFT2_LIST", props, null);

		PLAT_LIST = plat.split(",");
		EPCTAFT4_LIST = epctaft4.split(",");
		GOLD_LIST = gold.split(",");
		SILVER_LIST = silver.split(",");
		WEEKDAY_LIST = weekday.split(",");
		WP_LIST = wp.split(",");
		WPAFT2_LIST = wpaft2.split(",");

	}

}