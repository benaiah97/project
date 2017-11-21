package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * test case for EntityKey
 * 
 * @author RASTA006
 *
 */
public class TestEntityKey extends CommonTestDao {
	/**
	 * Test case for getEntity
	 */
	@Test
	public void testGetEntity() {
		String tsMac = null;
		String tsLocation = null;
		EntityTO result = null;
		DTIMockUtil.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.EntityResult");
		
		/* Scenario::1 Passing null as tsMac */
		try {
			result = EntityKey.getEntity(tsMac, tsLocation);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntity.",
					dtie.getLogMessage());
		}
		
		/*Scenario::2 Passing the object by mocking DB and getting value of
		  result as not null.  */
	
		tsMac = "1";
		tsLocation = "1";
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result = EntityKey.getEntity(tsMac, tsLocation);
			assertNotNull(result);
		} catch (DTIException dtie) {
         Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		
		/*
       * Scenario::3 Passing the object by mocking DB and getting null as
       * result
       */
      
      try {
         result = EntityKey.getEntity(tsMac, tsLocation);
         assertNotNull(result);
      } catch (DTIException dtie) {
         assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
         assertEquals("No Entity found for given TSMac "+tsMac+ "and TSLocation "+tsLocation +".", 
                  dtie.getLogMessage());
      }
	}

	/**
	 * Test case for getEntityActive
	 */
	@Test
	public void testGetEntityActive() {
		String tsMac = null;
		String tsLocation = null;
		Boolean result = null;
		DTIMockUtil
		.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.EntityActiveResult");
		/* Scenario::1 Passing null as tsMac */
		try {
			EntityKey.getEntityActive(tsMac, tsLocation);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityActive.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing the object without mocking DB */
		tsMac = "1";
		tsLocation = "1";
		try {
			result = EntityKey.getEntityActive(tsMac, tsLocation);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("No entity retrieved for tsMac 1 tsLocation 1",
					dtie.getLogMessage());
		}
		/*
		 * Scenario::3 Passing the object by mocking DB and getting null as
		 * result
		 */
		
		try {
			
			result = EntityKey.getEntityActive(tsMac, tsLocation);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("No entity retrieved for tsMac 1 tsLocation 1",
					dtie.getLogMessage());
		}
		/*
		 * Scenario::4 Passing the object by mocking DB and getting value as
		 * result
		 */
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			result = EntityKey.getEntityActive(tsMac, tsLocation);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getEntityProducts
	 */
	@Test
	public void testGetEntityProducts() {
		EntityTO entityTO = null;
		ArrayList<DBProductTO> dbProdList = null;
		ArrayList<BigInteger> result=null;
		/* Scenario::1 Passing null as entityTO,dbProdList */
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityProducts.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Setting pdtId as null */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		entityTO=new EntityTO();
		for(DBProductTO dbProduct:dbProdList){
			dbProduct.setPdtid(null);
		}
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProducts",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing the values to DB without mocking  */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProducts",
					dtie.getLogMessage());
		}
		/* Scenario::4 Passing the values to DB by mocking*/
		
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			 result=EntityKey.getEntityProducts(entityTO, dbProdList);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getEntityProducts with Upgrade
	 */
	@Test
	public void testGetEntityProductsWithUpgrade(){

		EntityTO entityTO = null;
		ArrayList<DBProductTO> dbProdList = null;
		String typeCode=null;
		ArrayList<BigInteger> result=null;
		/* Scenario::1 Passing null as entityTO,dbProdList */
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityProducts with upgrade.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Setting pdtId as null */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		entityTO=new EntityTO();
		typeCode="U";
		for(DBProductTO dbProduct:dbProdList){
			dbProduct.setPdtid(null);
		}
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProducts",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing the values to DB without mocking  */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			EntityKey.getEntityProducts(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProducts with upgrade",
					dtie.getLogMessage());
		}
		/* Scenario::4 Passing the values to DB and mocking the DB  */
		
		try {
			DTIMockUtil.processMockprepareAndExecuteSql();
			 result=EntityKey.getEntityProducts(entityTO, dbProdList,typeCode);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::5 making the type code as S */
		typeCode="S";
			try {
			EntityKey.getEntityProducts(entityTO, dbProdList,typeCode);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getEntityProductGroups
	 */
	@Test
	public void testGetEntityProductGroups() {
		EntityTO entityTO = null;
		ArrayList<DBProductTO> dbProdList = null;
		ArrayList<BigInteger> result=null;
		/* Scenario::1 Passing null as entityTO,dbProdList */
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityProductGroups.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Setting pdtId as null */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		entityTO=new EntityTO();
		
		for(DBProductTO dbProduct:dbProdList){
			dbProduct.setPdtid(null);
		}
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProductGroups",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing the values to DB without mocking  */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProductGroups",
					dtie.getLogMessage());
		}
		/* Scenario::4 Passing the values to DB and mocking the DB  */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			 result=EntityKey.getEntityProductGroups(entityTO, dbProdList);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getEntityProductGroups withUpgrade
	 */
	@Test
	public void testGetEntityProductGroupswithUpgrade() {
		EntityTO entityTO = null;
		ArrayList<DBProductTO> dbProdList = null;
		String typeCode=null;
		ArrayList<BigInteger> result=null;
		/* Scenario::1 Passing null as entityTO,dbProdList */
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityProductGroups with upgrade.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Setting pdtId as null */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		entityTO=new EntityTO();
		typeCode="U";
		
		for(DBProductTO dbProduct:dbProdList){
			dbProduct.setPdtid(null);
		}
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PRODUCT_CODE, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProductGroups",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing the values to DB without mocking  */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try {
			EntityKey.getEntityProductGroups(entityTO, dbProdList,typeCode);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityProductGroups with upgrade",
					dtie.getLogMessage());
		}
		/* Scenario::4 Passing the values to DB and mocking the DB  */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result=EntityKey.getEntityProductGroups(entityTO, dbProdList,typeCode);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario::4 Making the typecode as S */
		typeCode="S";
		try {
			result=EntityKey.getEntityProductGroups(entityTO, dbProdList,typeCode);
			 assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test Case for getEntityReceipt
	 */
	@Test
	public void testGetEntityReceipt(){
		Long entityId=null;
		String result = null;
		/* Scenario::1 Passing null as entityId */
		try {
			EntityKey.getEntityReceipt(entityId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_ENTITY, dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEntityReceipt.",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing through DB without Mocking */
		entityId=Long.valueOf("1");
		try {
			EntityKey.getEntityReceipt(entityId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Exception executing getEntityReceipt",
					dtie.getLogMessage());
		}
		/* Scenario::3 Mocking the DB  */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			result=EntityKey.getEntityReceipt(entityId);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}


}
