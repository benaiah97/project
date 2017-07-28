package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for EligibiltyKey
 * @author RASTA006
 *
 */
public class TestEligibiltyKey extends CommonTestDao{
	/**
	 * Test case for getOrderEligibility
	 */
	@Test
	public void testGetOrderEligibility(){
		ArrayList<DBProductTO> dbProdList=null; String eligGrpCode="012345678900";
		String eligMemberID="1";
		Boolean result=false;
		/*Scenario:: 1 when dbProdList is send as null*/
		try{
			result=EligibilityKey.getOrderEligibility(dbProdList, eligGrpCode, eligMemberID);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.INVALID_ELIGIBILITY_NBR,dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getOrderEligibility.", dtie.getLogMessage());
		}
		/*Scenario:: 2 setting null as pdtCode for each of the dbProdList  */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		for(DBProductTO dbProduct:dbProdList){
			dbProduct.setPdtCode(null);
		}
		try{
			result=EligibilityKey.getOrderEligibility(dbProdList, eligGrpCode, eligMemberID);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.INVALID_ELIGIBILITY_NBR,dtie.getDtiErrorCode());
			assertEquals("getOrderEligibility called with dbProdList containing no products needing eligibility.", dtie.getLogMessage());
		}
		/*Scenario:: 3 pulling the record from DB without mocking   */
		dbProdList=DTIMockUtil.fetchDBTicketTypeList();
		try{
			result=EligibilityKey.getOrderEligibility(dbProdList, eligGrpCode, eligMemberID);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,dtie.getDtiErrorCode());
			assertEquals("Exception executing getOrderEligibility", dtie.getLogMessage());
		}
		/*Scenario:: 4 fetching null from DB  */
		DTIMockUtil.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.EligibilityResult");
		
		try{
			result=EligibilityKey.getOrderEligibility(dbProdList, eligGrpCode, eligMemberID);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.INVALID_ELIGIBILITY_NBR,dtie.getDtiErrorCode());
			assertEquals("getOrderEligibility could not find valid elig set-up on product 1 and elig group code of 0123456789", dtie.getLogMessage());
		}
		/*Scenario:: 5 Mocking the DB process for fecthing from DB  */
		DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.EligibilityResult");;
		DTIMockUtil.processMockprepareAndExecuteSql();
		try{
			result=EligibilityKey.getOrderEligibility(dbProdList, eligGrpCode, eligMemberID);
			assertTrue(result);
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
	/**
	 * Test case for getEligibilityAssocId
	 */
	@Test
	public void testGetEligibilityAssocId(){
		String eligGrpCode=null;
		Integer result=null;
		/*Scenario:: 1 when eligGrpCode is send as null*/
		try{
			result=EligibilityKey.getEligibilityAssocId(eligGrpCode);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.INVALID_ELIGIBILITY_NBR,dtie.getDtiErrorCode());
			assertEquals("Insufficient parameters to execute getEligibilityAssocId.", dtie.getLogMessage());
		}
		/*Scenario:: 2 pulling the record from DB without mocking   */
		eligGrpCode="012345678900";
		try{
			result=EligibilityKey.getEligibilityAssocId(eligGrpCode);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,dtie.getDtiErrorCode());
			assertEquals("Exception executing getEligibilityAssocId", dtie.getLogMessage());
		}
		/*Scenario:: 3 pulling null from DB   */
		DTIMockUtil.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.AssociationResult");
		try{
			result=EligibilityKey.getEligibilityAssocId(eligGrpCode);
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.INVALID_ELIGIBILITY_NBR,dtie.getDtiErrorCode());
			assertEquals("getEligibilityAssocId could not find valid elig group 0123456789", dtie.getLogMessage());
		}
		/*Scenario:: 4 Mocking the DB process for fetching from DB  */
		
		DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.AssociationResult");
		
		try{
			result=EligibilityKey.getEligibilityAssocId(eligGrpCode);
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

}
