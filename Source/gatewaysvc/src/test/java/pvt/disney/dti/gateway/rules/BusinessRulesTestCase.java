package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import mockit.Mock;
import mockit.MockUp;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ArchiveKey;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

public class BusinessRulesTestCase extends CommonBusinessTest{
	 private static CalmRules calmRules = null;
	 ResultSet attributeRs = null;
		ResultSet attributeRs1 = null;
		ResultSet attributeRs2 = null;
	
	private  Properties props=null;
	@Before
	public void setUp()throws Exception {

		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
		
	/*	attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);
		
		//setAttributeRS();
*/		

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
				EntityTO entityTO=new EntityTO();
				dtiTxn.setEntityTO(entityTO);
				try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.ENTITY_NOT_ACTIVE)
					fail("Entity Not active");
				}
				// Validate that the entity is active
			    
			    entityTO.setActive(true);
			    dtiTxn.setEntityTO(entityTO);
			    try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_ENTITY)
					fail("Invalid entity Id expected");
				}
			    entityTO.setEntityId(1);
			    try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.FAILED_DB_OPERATION_SVC)
					fail("Invalid failed operation expected");
				}
			    DTIMockUtil.mockAttributeKey();
			    try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
					fail("Invalid Ticket Count operation expected");
				}
			    
			    QueryTicketRequestTO queryReq=new QueryTicketRequestTO();
			    TicketTO ticket=new TicketTO();
			    ticket.setTktItem(new BigInteger("1"));
			    ArrayList<TicketTO> tktList=new ArrayList<TicketTO>(); 
			    tktList.add(ticket);
			    queryReq.setTktList(tktList);
			    dtiTxn.getRequest().setCommandBody(queryReq);
			    try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
					fail("In-bound WDW txn with <> 1 TktId expected ");
				}
			    
			    
			    TicketIdType ticketIdType= TicketIdType.TKTNID_ID; 
			    ticket.setTktItem(new BigInteger("1"));
			    ticket.setTktNID("12000507111600050");
			    //ArrayList<TicketTO> tktList=new ArrayList<TicketTO>(); 
			    tktList.remove(ticket);
			    tktList.add(ticket);
			    queryReq.setTktList(tktList);
			    dtiTxn.getRequest().setCommandBody(queryReq);
			    try {
					BusinessRules.applyBusinessRules(dtiTxn);
					
				} catch (DTIException dtie) {
					
					fail("In-bound WDW txn with <> 1 TktId expected ");
				}
			    
	    
	  }
	
	/*private void MockProcessQuery(){
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);
		new MockUp<DAOHelper>() {

			@SuppressWarnings("unchecked")
			@Mock
			protected Object processQuery(Object[] values) {

				HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
				ResultSetProcessor theProcessor = new AttributeResult();
				try {
					theProcessor.processNextResultSet(attributeRs);
					theProcessor.processNextResultSet(attributeRs1);
					theProcessor.processNextResultSet(attributeRs2);
					attributeMap = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) theProcessor
							.getProcessedObject();
				} catch (Exception e) {

				}

				return attributeMap;
			}
		};
	}
	private void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
	}

	private void setAttributeResultSet(ResultSet rs, int i) throws Exception {

		EasyMock.expect(rs.getString("ATTR_VALUE")).andReturn("11111").times(3);

		EasyMock.expect(rs.getString("ACTOR")).andReturn("MGR").times(3);
		if (i == 0) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("OpArea")
					.times(3);

		} else if (i == 1) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("User")
					.times(3);
		} else {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("Pass")
					.times(3);
		}

		EasyMock.expect(rs.getString("ACTIVE_IND")).andReturn("T").times(3);
		EasyMock.expect(rs.getString("CMD_CODE")).andReturn("QueryReservation")
				.times(3);

	}*/
	
	public void testApplyUpgradeAlphaRules(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.UPGRADEALPHA);
		DTIRequestTO request=new DTIRequestTO();
		
		UpgradeAlphaRequestTO upgrade=new UpgradeAlphaRequestTO(); 
		
		

		
		 TicketTO ticket=new TicketTO();
		 ticket.setTktItem(new BigInteger("1"));
		 ticket.setTktNID("12000507111600050");
		    ArrayList<TicketTO> tktList=new ArrayList<TicketTO>(); 
		    tktList.add(ticket);
		    upgrade.setTktList(tktList);
		    request.setCommandBody(upgrade);
		dtiTxn.setRequest(request);
		try{
			BusinessRules.applyUpgradeAlphaRules(dtiTxn);	
		}catch(DTIException dtie){
			fail("");
		}
		
		
	}
	@Test
	public void testApplyVoidTicketRules(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDTICKET);
		DTIRequestTO request=new DTIRequestTO();
		
		VoidTicketRequestTO upgrade=new VoidTicketRequestTO(); 
		
		 TicketTO ticket=new TicketTO();
		 ticket.setTktItem(new BigInteger("1"));
		 ticket.setTktNID("12000507111600050");
		    ArrayList<TicketTO> tktList=new ArrayList<TicketTO>(); 
		    tktList.add(ticket);
		    upgrade.setTktList(tktList);
		    PaymentTO payment=new PaymentTO();
			ArrayList<PaymentTO> payListTO=new ArrayList<PaymentTO>();
			payListTO.add(payment);
			upgrade.setPaymentList(payListTO);
		    request.setCommandBody(upgrade);
		dtiTxn.setRequest(request);
		dtiTxn.setProvider(ProviderType.WDWNEXUS);
		EntityTO entityTO=new EntityTO();
		entityTO.setActive(true);
		dtiTxn.setEntityTO(entityTO);
		
		new MockUp<ArchiveKey>() {
			@Mock
			public void insertVoidTicketRequest(DTITransactionTO dtiTxn) {

				return;

			}

		};
		
		try{
			BusinessRules.applyVoidTicketRules(dtiTxn);	
		}catch(DTIException dtie){
			fail("");
		}
	}

}
