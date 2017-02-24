package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.rules.wdw.WDWReservationRules;

public class BusinessRulesTestCase extends CommonBusinessTest{
	 private static CalmRules calmRules = null;
	
	private  Properties props=null;
	@Before
	public void setUp() {
		props=setConfigProperty();
		setMockProperty();

	}
	@Test
	public void testInitBusinessRules() throws DTIException {

		try {
			BusinessRules.initBusinessRules(props);
		} catch (DTIException dtie) {
			fail("Exception occured while initializing business Rules ");
		}

	}
	@Test
	public void testApplyBusinessRules() throws DTICalmException, DTIException {
		DTITransactionTO dtiTxn=null;
	
		 dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
		try{
			BusinessRules.applyBusinessRules(dtiTxn);
			fail("DTIRequestTO object Required");
		}catch(DTIException dtie){
			if(dtie.getDtiErrorCode()!=DTIErrorCode.INVALID_MSG_ELEMENT){
				fail("Test 1:  No DTIRequestTO object should have triggered INVALID_MSG_ELEMENT");
			}
		}
		//Test 2
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiTxn.setRequest(dtiRequestTO);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			fail("Test 2:  DTIRequestTO with no children should have failed.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
				fail("Test 2:  DTIRequestTO with no children should have triggered INVALID_MSG_ELEMENT");
		}
		
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequestTO.setPayloadHeader(payHeaderTO);

		// Test 3: DTIRequestTO missing cmdHdr and Body
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			fail("Test 3:  DTIRequestTO missing cmdHdr and Body should have failed.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
				fail("Test 3:  DTIRequestTO missing cmdHdr and Body should have triggered INVALID_MSG_ELEMENT");
		}

		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);

		// Test 4: DTIRequestTO missing Body
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			fail("Test 4:  DTIRequestTO missing Body should have failed.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
				fail("Test 4:  DTIRequestTO missing Body should have triggered INVALID_MSG_ELEMENT");
		}

		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		dtiRequestTO.setCommandBody(queryTicketRequestTO);

		// Test 5 PayLoad Id null
		try {
			BusinessRules.applyBusinessRules(dtiTxn);

		} catch (DTIException dtie) {
			if(payHeaderTO.getPayloadID()!=null){
				fail("Pay Load Id has to be null");
			}
		}
		
		
		//Test 6 Version Missing
		payHeaderTO.setPayloadID("1234567890");
		try {
			BusinessRules.applyBusinessRules(dtiTxn);

		} catch (DTIException dtie) {
			if(payHeaderTO.getTarget()!=null){
				fail("Target has to be null");
			}
		}
		
		// Test 7: Version missing
				payHeaderTO.setTarget("Target");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(payHeaderTO.getVersion()!=null){
						fail("Version has to be null");
					}
				}
				
				// Test 8: Protocol missing
				payHeaderTO.setVersion("Version");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(payHeaderTO.getCommProtocol()!=null){
						fail("Comm Protocol has to be null");
					}
				}
				
				// Test 9: Comm Method missing
				payHeaderTO.setCommProtocol("Protocol");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(payHeaderTO.getCommMethod()!=null){
						fail("Comm Method has to be null");
					}
				}
				
				
				// Test 9: Comm Method missing
				payHeaderTO.setCommMethod("Method");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(payHeaderTO.getTransmitDateTime()!=null){
						fail("Expected exception   TransmitDate/Time missing.");
					}
				}
				// Tkt seller
				payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
						.getInstance());
				
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(payHeaderTO.getTktSeller()!=null){
						fail("TKT Seller has to be null");
					}
				}
				
				TktSellerTO tktSellerTo = new TktSellerTO();
				tktSellerTo.setTsMac("TSMAC");
				tktSellerTo.setTsLocation("TsLocation");
				tktSellerTo.setTsSystem("TsSystem");
				tktSellerTo.setTsSecurity("TsSecurity");
				payHeaderTO.setTktSeller(tktSellerTo);
				payHeaderTO.setCommandCount(new BigInteger("1"));
				payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
						.getInstance());
				
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdItem()!=null){
						fail("CMD Id has to be null ");
					}
				}
				
				cmdHeaderTO.setCmdItem(new BigInteger("1"));
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdTimeout()!=null){
						fail("CmdTimeout is missing. ");
					}
				}
				
				cmdHeaderTO.setCmdTimeout(new BigInteger("1"));
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdDateTime()!=null){
						fail("CmdDateTime is missing.. ");
					}
				}
				cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
						.getInstance());
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdInvoice()!=null){
						fail("CmdInvoice is missing... ");
					}
				}
				
				cmdHeaderTO.setCmdInvoice("AnInvoice.");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdDevice()!=null){
						fail("CmdDevice is missing... ");
					}
				}
				
				cmdHeaderTO.setCmdDevice("CmdDevice");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if(cmdHeaderTO.getCmdOperator()!=null){
						fail("CmdOperator is missing... ");
					}
				}
				cmdHeaderTO.setCmdOperator("Bob");
				
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TARGET_VERSION)
					fail("Invalid Target expected");
				}
				payHeaderTO.setTarget("TEST");
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.ENTITY_NOT_ACTIVE)
					fail("Entity Not active");
				}
				// Validate that the entity is active
			    EntityTO entityTO=new EntityTO();
			    entityTO.setActive(true);
				
	    
	  }

}
