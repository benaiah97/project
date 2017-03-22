package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTVoidTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVTicketTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
/**
 * Test Case for WDWVoidTicketRules
 * @author RASTA006
 *
 */
public class WDWVoidTicketRulesTestCase extends CommonTestUtils{
	@Before
	public void setUp() throws Exception {

		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
	}
	/**
	 * test case for transformRequest method
	 */
	@Test
	public void testTransformRequest(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO dtiVoidTkt=new VoidTicketRequestTO();
		dtiVoidTkt.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(dtiVoidTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString=null;
		/*Scenario:: 1 passing the dtTxn Object*/
		try{
			xmlString=WDWVoidTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
	}
	/**
	 * Test case for transformResponseBody
	 */
	@Test
	public void testTransformResponseBody(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDTICKET);
		//reateCommonResponse(dtiTxn);
		OTTransactionType txType=OTTransactionType.VOIDTICKET;
		OTCommandTO otCmdTO=new OTCommandTO(txType);
		OTVoidTicketTO voidTicketTO=new OTVoidTicketTO();
		ArrayList<OTVTicketTO> ticketList=new ArrayList<OTVTicketTO>();
		otCmdTO.setVoidTicketTO(voidTicketTO);
		DTIResponseTO dtiRespTO=new DTIResponseTO();
		/*Scenario:: 1 when VTicketList is empty */
		try{
			WDWVoidTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
		/*Scenario:: 2 when VTicketList is not empty  */
		voidTicketTO.setVTicketList(getVoidticketList(ticketList));
		OTTransactionDSSNTO transactionDSSN=new OTTransactionDSSNTO();
		transactionDSSN.setDate((GregorianCalendar) GregorianCalendar
				.getInstance());
		transactionDSSN.setSite("1");
		transactionDSSN.setStation("1");
		transactionDSSN.setTransactionId(new Integer(1));
		voidTicketTO.setTransactionDSSN(transactionDSSN);
		voidTicketTO.setTransactionCOD("1");
		try{
			WDWVoidTicketRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
	}
	/**
	 * Test case for applyWDWVoidTicketRules
	 */
	@Test
	public void testApplyWDWVoidTicketRules(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO dtiVoidTkt=new VoidTicketRequestTO();
		dtiVoidTkt.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(dtiVoidTkt);
		try{
			WDWVoidTicketRules.applyWDWVoidTicketRules(dtiTxn);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
	}
	
	/**
	 * Common Method for creation of OTVTicketTO list
	 * @param ticketList
	 * @return
	 */
	private ArrayList<OTVTicketTO> getVoidticketList(ArrayList<OTVTicketTO> ticketList){
		OTVTicketTO otVticket=new OTVTicketTO();
		otVticket.setProviderTicketType(new BigInteger("1"));
		otVticket.setPrice(new BigDecimal("1.00"));
		otVticket.setTicket(getOTicket());
		
		//ArrayList<OTVTicketTO> ticketList=new ArrayList<OTVTicketTO>();
		ticketList.add(otVticket);
		return ticketList;
	}
	/**
	 * Common request for creation of ticket
	 * @return
	 */
	private OTTicketTO getOTicket(){
		OTTicketTO ticket=new OTTicketTO();
		ticket.setTCOD("12000507111600050");
		ticket.setTDssn((GregorianCalendar) GregorianCalendar
				.getInstance(), "1", "1", "1");
		
		return ticket;
	}

}
