package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for ProductKey
 * @author RASTA006
 *
 */
public class TestProductkey extends CommonTestDao{
	/**
	 * Test Case for getOrderProducts
	 */
	//@Test
	public void testGetOrderProducts(){
		ArrayList<TicketTO> tktListTO=null;
		ArrayList<DBProductTO> result = null;
		/* Scenario::1 Passing null as tktListTO */
		try {
			ProductKey.getOrderProducts(tktListTO);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getOrderProducts DB routine found an empty ticket list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing through DB without Mocking */
		tktListTO=getTicketList(TicketIdType.TKTNID_ID);
		try {
			ProductKey.getOrderProducts(tktListTO);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getOrderProducts",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing through DB without Mocking */
		DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.ProductDetailResult");
		try {
			result=ProductKey.getOrderProducts(tktListTO);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getOrderProducts with TypeCode 
	 */
	//@Test
	public void testGetOrderProductsWithTypeCode(){
		ArrayList<TicketTO> tktListTO=null;
		ArrayList<DBProductTO> result = null;
		String typCode=null;
		/* Scenario::1 Passing null as tktListTO */
		try {
			ProductKey.getOrderProducts(tktListTO,typCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getOrderProducts DB routine found an empty ticket list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing through DB without Mocking */
		typCode="S";
		tktListTO=getTicketList(TicketIdType.MAG_ID);
		try {
			ProductKey.getOrderProducts(tktListTO,typCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getOrderProducts",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing through DB without Mocking */
		
		try {
			//DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.ProductDetailResult");
			DTIMockUtil.processMockprepareAndExecuteSql();
			result=ProductKey.getOrderProducts(tktListTO,typCode);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::4 Changing the type Code to U */
		typCode="U";
		try {
			result=ProductKey.getOrderProducts(tktListTO,typCode);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getProductTicketTypes
	 */
	@Test
	public void testGetProductTicketTypes(){
		ArrayList<DBProductTO> dbProdList=null;
		ArrayList<DBProductTO> resultList = null;
		/* Scenario::1 Passing null as dbProdList */
		try {
			ProductKey.getProductTicketTypes(dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getProductTicketTypes DB routine found an empty db product list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			ProductKey.getProductTicketTypes(dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getProductTicketTypes",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			resultList=ProductKey.getProductTicketTypes(dbProdList);
			assertNotNull(resultList);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getActiveShells
	 */
	@Test
	public void testGetActiveShells(){
		HashSet<Integer> shellSet=null;
		ArrayList<Integer> result = null;
		/* Scenario::1 Passing null as shellSet */
		try {
			ProductKey.getActiveShells(shellSet);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getActiveShells DB routine found an empty ticket list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		shellSet=new HashSet<Integer>();
		shellSet.add(Integer.valueOf("1"));
		try {
			ProductKey.getActiveShells(shellSet);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getActiveShells",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			result=ProductKey.getActiveShells(shellSet);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getProductShellXref
	 */
	@Test
	public void testGetProductShellXref(){
		ArrayList<DBProductTO> dbProdList=null;
		HashMap<String, ArrayList<Integer>> result = null;
		/* Scenario::1 Passing null as dbProdList */
		try {
			ProductKey.getProductShellXref(dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getProductShellXref DB routine found an empty db product list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			ProductKey.getProductShellXref(dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getProductShellXref",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			result=ProductKey.getProductShellXref(dbProdList);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for insertProductDetail
	 */
	@Test
	public void testInsertProductDetail() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		/* Scenario::1 When dtiTxn.getDbProdList() is empty */
		try {
			ProductKey.insertProductDetail(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::2 When dtiTxn is of transaction type CREATETICKET */
		ArrayList<DBProductTO> dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		for(DBProductTO dbproduct:dbProdList){
			dbproduct.setQuantity(new BigInteger("1"));
		}
		dtiTxn.setDbProdList(dbProdList);
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			ProductKey.insertProductDetail(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::3 When dtiTxn is of transaction type QUERYTICKET */
		dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		dtiTxn.setDbProdList(dbProdList);
		try {
			ProductKey.insertProductDetail(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing insertProductDetail",
					dtie.getLogMessage());
		}
		/* Scenario::4 Passing object after mocking DB */
		
		try {
			ProductKey.insertProductDetail(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getProductReceipt
	 */
	@Test
	public void testGetProductReceipt(){
		String pdtCode=null;
		String result = null;
		/* Scenario::1 Passing pdtCode as null*/
		try {
			ProductKey.getProductReceipt(pdtCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getProductReceipt.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		pdtCode="1";
		try {
			ProductKey.getProductReceipt(pdtCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getProductReceipt",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			result=ProductKey.getProductReceipt(pdtCode);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getProductFromTicketType
	 */
	@Test
	public void testGetProductFromTicketType(){
		String providerTicketType=null;
		String result = null;
		/* Scenario::1 Passing providerTicketType as null*/
		try {
			ProductKey.getProductFromTicketType(providerTicketType);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.MISSING_TICKET_TYPE, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getProductFromTicketType.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		providerTicketType="1";
		try {
			ProductKey.getProductFromTicketType(providerTicketType);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getProductFromTicketType",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		try {
			DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.TktTypeProductResult");
			result=ProductKey.getProductFromTicketType(providerTicketType);
			assertNotNull(result);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getProductCodeFromTktNbr
	 */
	@Test
	public void testGetProductCodeFromTktNbr(){

		BigInteger tktNbr=null;
		ArrayList<DBProductTO> result = null;
		/* Scenario::1 Passing providerTicketType as null*/
		try {
			ProductKey.getProductCodeFromTktNbr(tktNbr);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("getOrderProducts DB routine found an empty ticket list.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		tktNbr=new BigInteger("1");
		try {
			ProductKey.getProductCodeFromTktNbr(tktNbr);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getProductCodeFromTktNbr",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		//
		
		try {
			DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.PdtCodeTktNbrResult");
			result=ProductKey.getProductCodeFromTktNbr(tktNbr);
			assertNotNull(result);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	
		
	}

}
