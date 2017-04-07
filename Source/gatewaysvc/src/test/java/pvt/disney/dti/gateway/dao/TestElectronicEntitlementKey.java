package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test case for ElectronicEntitlementKey
 * @author RASTA006
 *
 */
public class TestElectronicEntitlementKey extends CommonTestDao{
	/**
	 * Test case for queryEntitlementVisualId
	 */
	@Test
	public void testQueueEntitlementsForProcessing() {
		String result = null;
		TicketTO aTicket = null;
		/* Scenario ::1 when ticket is passed as null */
		result = ElectronicEntitlementKey.queryEntitlementVisualId(aTicket);
		assertNull(result);
		/* Scenario ::2 when complete ticket is passed  */
		try {
			aTicket = getTicketTO(true, true);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
		
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		result = ElectronicEntitlementKey.queryEntitlementVisualId(aTicket);
		assertNotNull(result);
	}
	/**
	 * Test case for insertUpgradedEntitlement
	 */
	@Test
	public void testInsertUpgradedEntitlement(){
		Integer inboundTSID=new Integer(1);
		TicketTO aTicket=null; String payloadID=null;int entityId=1;
		try {
			aTicket = getTicketTO(true, true);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
		/* Scenario ::1 calling insert without mocking */
		ElectronicEntitlementKey.insertUpgradedEntitlement(inboundTSID, aTicket, payloadID, entityId);
		/* Scenario ::2 mocking insert*/
		DTIMockUtil.processMockprepareAndExecuteSql();
		ElectronicEntitlementKey.insertUpgradedEntitlement(inboundTSID, aTicket, payloadID, entityId);
	}
	/**
	 * Test case for insertVoidedEntitlement
	 */
	@Test
	public void testInsertVoidedEntitlement(){

		Integer inboundTSID=new Integer(1);
		TicketTO aTicket=null; String payloadID=null;int entityId=1;
		try {
			aTicket = getTicketTO(true, true);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
		/* Scenario ::1 calling insert without mocking */
		ElectronicEntitlementKey.insertVoidedEntitlement(inboundTSID, aTicket, payloadID, entityId);
		/* Scenario ::2 mocking insert*/
		DTIMockUtil.processMockprepareAndExecuteSql();
		ElectronicEntitlementKey.insertVoidedEntitlement(inboundTSID, aTicket, payloadID, entityId);
	
		
	}
}