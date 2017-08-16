package pvt.disney.dti.gateway.dao.data;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * 
 * @author lewit019 This is the test case for UpgradeCatalogTO.
 */
public class UpgradeCatalogTOTestCase extends CommonTestUtils {

	public final static String PDTCODE1 = "AAA01";
	public final static String PDTCODE2 = "BBB02";
	public final static String PDTCODE3 = "CCC03";
	public final static String PDTCODE4 = "DDD04";
	public final static String PDTCODE5 = "EEE05";

	@Before
	public void setUp() {
		setMockProperty();
	}

	// int addDLRProduct(String pdtCode, BigDecimal unitPrice, BigDecimal tax,
	// String dayClass, String daySubclass, String plu)
	// ArrayList<DBProductTO> getProductList()
	// int getProductListCount()
	@Test
	public final void testAddDLRProduct() {

		String pdtCode = "ABY01";
		BigDecimal unitPrice = new BigDecimal("203.10");
		BigDecimal tax = new BigDecimal("10.31");
		String dayClass = "AP";
		String daySubclass = "GOLD";
		String plu = "AJDB0314223";

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();

		if (upgrdCtlg.getProductList() == null) {
			fail("Upgrade Catalog TO has a null ProductList after construction.");
		}

		upgrdCtlg.addDLRProduct(pdtCode, unitPrice, tax, dayClass, daySubclass, plu);

		if (upgrdCtlg.getProductListCount() != 1) {
			fail("Upgrade Catalog TO Added PLU to upgrade catalog and product list count doesn't reflect the addition.");
		}

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList == null) {
			fail("Upgrade Catalog TO product list null after plu add!");
		}

		DBProductTO thisProduct = prodList.get(0);

		if (thisProduct == null) {
			fail("Upgrade Catalog TO added PLU not in initial position.  Null in index 0.");
		}

		if (!thisProduct.getPdtCode().equals(pdtCode)) {
			fail("Upgrade Catalog TO added PLU pdtCode doesn't match what was inserted!");
		}

		return;
	}

	// int addWDWProduct(String pdtCode, BigDecimal unitPrice, BigDecimal tax,
	// String dayClass, String daySubclass, BigInteger tktNbr)
	@Test
	public final void testAddWDWProduct() {

		String pdtCode = "ABY01";
		BigDecimal unitPrice = new BigDecimal("203.10");
		BigDecimal tax = new BigDecimal("10.31");
		String dayClass = "AP";
		String daySubclass = "GOLD";
		BigInteger tktNbr = new BigInteger("10");

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();

		if (upgrdCtlg.getProductList() == null) {
			fail("Upgrade Catalog TO has a null ProductList after construction.");
		}

		upgrdCtlg.addWDWProduct(pdtCode, unitPrice, tax, dayClass, daySubclass, tktNbr);

		if (upgrdCtlg.getProductListCount() != 1) {
			fail("Upgrade Catalog TO Added WDW product to upgrade catalog and product list count doesn't reflect the addition.");
		}

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList == null) {
			fail("Upgrade Catalog TO product list null after WDW product add!");
		}

		DBProductTO thisProduct = prodList.get(0);

		if (thisProduct == null) {
			fail("Upgrade Catalog TO added WDW product not in initial position.  Null in index 0.");
		}

		if (!thisProduct.getPdtCode().equals(pdtCode)) {
			fail("Upgrade Catalog TO added WDW pdtCode doesn't match what was inserted!");
		}

	}

	// int addProduct(DBProductTO aProduct)
	@Test
	public final void testAddProduct() {

		String pdtCode = "ABY01";
		BigDecimal unitPrice = new BigDecimal("203.10");
		BigDecimal tax = new BigDecimal("10.31");
		String dayClass = "AP";
		String daySubclass = "GOLD";
		BigInteger tktNbr = new BigInteger("10");

		DBProductTO newProd = new DBProductTO();
		newProd.setPdtCode(pdtCode);
		newProd.setUnitPrice(unitPrice);
		newProd.setTax(tax);
		newProd.setDayClass(dayClass);
		newProd.setDaySubclass(daySubclass);
		newProd.setMappedProviderTktNbr(tktNbr);

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();

		upgrdCtlg.addProduct(newProd);

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList == null) {
			fail("Upgrade Catalog TO product list null after DB product add!");
		}

		DBProductTO thisProduct = prodList.get(0);

		if (thisProduct == null) {
			fail("Upgrade Catalog TO added DB product not in initial position.  Null in index 0.");
		}

		if (!thisProduct.getPdtCode().equals(pdtCode)) {
			fail("Upgrade Catalog TO added DB pdtCode doesn't match what was inserted!");
		}

	}

	// void setProductList(ArrayList<DBProductTO> aProdList)
	@Test
	public final void testSetProductList() {

		ArrayList<DBProductTO> aProdList = createTestSet();

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();
		upgrdCtlg.setProductList(aProdList);

		if (upgrdCtlg.getProductListCount() != 5) {
			fail("Upgrade Catalog TO Set setProductList and product list count doesn't reflect accurate count.");
		}

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList == null) {
			fail("Upgrade Catalog TO product list null after setProductList!");
		}

		DBProductTO thisProduct0 = prodList.get(0);
		if (thisProduct0 == null) {
			fail("Upgrade Catalog TO setProductList product not in initial position.  Null in index 0.");
		}
		if (!thisProduct0.getPdtCode().equals(PDTCODE1)) {
			fail("Upgrade Catalog TO setProductList pdtCode doesn't match what was inserted at index 0!");
		}

		DBProductTO thisProduct1 = prodList.get(1);
		if (thisProduct1 == null) {
			fail("Upgrade Catalog TO added WDW product not in initial position.  Null in index 1.");
		}
		if (!thisProduct1.getPdtCode().equals(PDTCODE2)) {
			fail("Upgrade Catalog TO setProductList pdtCode doesn't match what was inserted at index 1!");
		}

		DBProductTO thisProduct2 = prodList.get(2);
		if (thisProduct2 == null) {
			fail("Upgrade Catalog TO added WDW product not in initial position.  Null in index 2.");
		}
		if (!thisProduct2.getPdtCode().equals(PDTCODE3)) {
			fail("Upgrade Catalog TO setProductList pdtCode doesn't match what was inserted at index 2!");
		}

		return;

	}

	// int removeDaySubclass(String subclass)
	@Test
	public final void testRemoveDaySubclass() {

		ArrayList<DBProductTO> aProdList = createTestSet();

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();
		upgrdCtlg.setProductList(aProdList);

		upgrdCtlg.removeDaySubclass("EPCOTAF4");

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList.size() == 5) {
			fail("Upgrade Catalog TO remove day subclass didn't remove anything.");
		}

		if (prodList.size() < 4) {
			fail("Upgrade Catalog TO remove day subclass removed too many items.");
		}

	}

	// int keepDaySubclasses(ArrayList<String> daySubclassList)
	@Test
	public final void testKeepDaySubclasses() {

		ArrayList<DBProductTO> aProdList = createTestSet();

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();
		upgrdCtlg.setProductList(aProdList);

		ArrayList<String> aStringList = new ArrayList<String>();

		aStringList.add("GOLD");
		aStringList.add("SILVER");

		upgrdCtlg.keepDaySubclasses(aStringList);

		ArrayList<DBProductTO> prodList = upgrdCtlg.getProductList();

		if (prodList.size() == 5) {
			fail("Upgrade Catalog TO keep only certain day subclasses didn't remove anything.");
		}

		if (prodList.size() != 3) {
			fail("Upgrade Catalog TO keep only certain day subclasses removed the wrong number of items.");
		}

		return;

	}

	// ArrayList<String> getDaySubclasses()
	@Test
	public final void testGetDaySubclasses() {

		ArrayList<DBProductTO> aProdList = createTestSet();

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();
		upgrdCtlg.setProductList(aProdList);

		ArrayList<String> subclassList = upgrdCtlg.getDaySubclasses();

		if (subclassList.size() != 4) {
			fail("Upgrade Catalog TO list of subclasses is not accurate.");
		}

		return;

	}

	// int removeProductsLowerThan(BigDecimal minimumPrice)
	@Test
	public final void testRemoveProductsLowerThan() {

		ArrayList<DBProductTO> aProdList = createTestSet();

		UpgradeCatalogTO upgrdCtlg = new UpgradeCatalogTO();
		upgrdCtlg.setProductList(aProdList);

		int size = upgrdCtlg.removeProductsLowerThan(new BigDecimal("303.34"));

		if (size != 2) {
			fail("Upgrade Catalog TO remove prdoucts lower than did not remove the right number of products.");
		}

	}

	// int retainDLRPLUs(ArrayList<String> listOfUpgradeablePLUs)
	@Test
	public final void testRetainDLRPLUs() {
	}

	private ArrayList<DBProductTO> createTestSet() {

		DBProductTO newProd1 = new DBProductTO();
		newProd1.setPdtCode(PDTCODE1);
		newProd1.setUnitPrice(new BigDecimal("101.11"));
		newProd1.setTax(new BigDecimal("11.11"));
		newProd1.setDayClass("AP");
		newProd1.setDaySubclass("WEEKDAY");
		newProd1.setMappedProviderTktNbr(new BigInteger("11"));
		newProd1.setMappedProviderTktName("PLU1");

		ArrayList<DBProductTO> aProdList = new ArrayList<DBProductTO>();
		aProdList.add(newProd1);

		DBProductTO newProd2 = new DBProductTO();
		newProd2.setPdtCode(PDTCODE2);
		newProd2.setUnitPrice(new BigDecimal("202.22"));
		newProd2.setTax(new BigDecimal("22.22"));
		newProd2.setDayClass("AP");
		newProd2.setDaySubclass("SILVER");
		newProd2.setMappedProviderTktNbr(new BigInteger("22"));
		newProd2.setMappedProviderTktName("PLU2");

		aProdList.add(newProd2);

		DBProductTO newProd3 = new DBProductTO();
		newProd3.setPdtCode(PDTCODE3);
		newProd3.setUnitPrice(new BigDecimal("303.33"));
		newProd3.setTax(new BigDecimal("33.33"));
		newProd3.setDayClass("AP");
		newProd3.setDaySubclass("GOLD");
		newProd3.setMappedProviderTktNbr(new BigInteger("33"));
		newProd3.setMappedProviderTktName("PLU3");

		aProdList.add(newProd3);

		DBProductTO newProd4 = new DBProductTO();
		newProd4.setPdtCode(PDTCODE4);
		newProd4.setUnitPrice(new BigDecimal("404.44"));
		newProd4.setTax(new BigDecimal("44.44"));
		newProd4.setDayClass("AP");
		newProd4.setDaySubclass("GOLD");
		newProd4.setMappedProviderTktNbr(new BigInteger("44"));
		newProd4.setMappedProviderTktName("PLU4");

		aProdList.add(newProd4);

		DBProductTO newProd5 = new DBProductTO();
		newProd5.setPdtCode(PDTCODE5);
		newProd5.setUnitPrice(new BigDecimal("505.55"));
		newProd5.setTax(new BigDecimal("55.55"));
		newProd5.setDayClass("AP");
		newProd5.setDaySubclass("EPCOTAF4");
		newProd5.setMappedProviderTktNbr(new BigInteger("55"));
		newProd5.setMappedProviderTktName("PLU5");

		aProdList.add(newProd5);

		return aProdList;
	}

	@Test
	public final void testRetainNegativeUpgrade() {
		BigDecimal greaterPrice = new BigDecimal("2.00");
		BigDecimal equalPrice = new BigDecimal("1.00");
		EntityTO entityTO = new EntityTO();
		String tpsCode = "WDW-ATS";
		UpgradeCatalogTO upgradeCatalogTO = new UpgradeCatalogTO();
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			upgradeCatalogTO = ProductKey.getAPUpgradeCatalog(entityTO, tpsCode);
		} catch (DTIException e) {
			Assert.fail("UnExpected");
		}

		/*
		 * Scenario 1:: When guestproductTO's standardRetailPrice is greater then
		 * DBProductTO's unitPrice
		 */

		upgradeCatalogTO.removeProductsLowerThan(greaterPrice);
		assertEquals(0, upgradeCatalogTO.getProductList().size());

		/*
		 * Scenario 2:: When guestproductTO's standardRetailPrice == DBProductTO's
		 * unitPrice
		 */

		upgradeCatalogTO.removeProductsLowerThan(equalPrice);
		assertEquals(0, upgradeCatalogTO.getProductList().size());

	}

	@Test
	public void testRetainPostiveUpgrade() {
		BigDecimal lesserPrice = new BigDecimal("0.00");
		EntityTO entityTO = new EntityTO();
		String tpsCode = "WDW-ATS";
		UpgradeCatalogTO upgradeCatalogTO = new UpgradeCatalogTO();
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			upgradeCatalogTO = ProductKey.getAPUpgradeCatalog(entityTO, tpsCode);
		} catch (DTIException e) {
			Assert.fail("UnExpected");
		}

		/*
		 * Scenario 3:: When guestproductTO's standardRetailPrice == DBProductTO's
		 * unitPrice
		 */

		upgradeCatalogTO.removeProductsLowerThan(lesserPrice);
		assertEquals(1, upgradeCatalogTO.getProductList().size());

	}

}
