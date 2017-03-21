package pvt.disney.dti.gateway.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import mockit.Mock;
import mockit.MockUp;
import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;

public class CommonTestDao {

	protected void createCommonRequest(DTITransactionTO dtiTxn) {
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiTxn.setRequest(dtiRequestTO);
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequestTO.setPayloadHeader(payHeaderTO);
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);
		/*
		 * QueryTicketRequestTO queryTicketRequestTO = new
		 * QueryTicketRequestTO();
		 * dtiRequestTO.setCommandBody(queryTicketRequestTO);
		 */
		payHeaderTO.setPayloadID("1234567890");
		payHeaderTO.setTarget("TEST");

		payHeaderTO.setVersion("1.0");
		payHeaderTO.setCommProtocol("IP");
		payHeaderTO.setCommMethod("Network");
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		TktSellerTO tktSellerTo = new TktSellerTO();
		tktSellerTo.setTsMac("DA6626");
		tktSellerTo.setTsLocation("001");
		tktSellerTo.setTsSystem("CC");
		tktSellerTo.setTsSecurity("WaltDisney1");
		payHeaderTO.setTktSeller(tktSellerTo);
		payHeaderTO.setCommandCount(new BigInteger("1"));
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdItem(new BigInteger("1"));
		cmdHeaderTO.setCmdTimeout(new BigInteger("45"));
		cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdInvoice("2018895220000011272.");
		cmdHeaderTO.setCmdActor("SYS");
		EntityTO entityTO = new EntityTO();
		dtiTxn.setEntityTO(entityTO);
		entityTO.setActive(true);
		dtiTxn.setEntityTO(entityTO);
		entityTO.setEntityId(1);
		entityTO.setDefPymtId(1);
		entityTO.setDefPymtData("1");

		entityTO.setStartValidDate(new Date(System.currentTimeMillis()));
		entityTO.setEndValidDate(new Date(System.currentTimeMillis()));
		cmdHeaderTO.setCmdDevice("PAULH");
		cmdHeaderTO.setCmdOperator("PAULH");
	}

	protected void createCommonResponse(DTITransactionTO dtiTxn) {

		DTIResponseTO dtiResponseTO = new DTIResponseTO();
		dtiTxn.setResponse(dtiResponseTO);
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiResponseTO.setPayloadHeader(payHeaderTO);
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiResponseTO.setCommandHeader(cmdHeaderTO);
		/*
		 * QueryTicketRequestTO queryTicketRequestTO = new
		 * QueryTicketRequestTO();
		 * dtiRequestTO.setCommandBody(queryTicketRequestTO);
		 */
		payHeaderTO.setPayloadID("1234567890");
		payHeaderTO.setTarget("TEST");

		payHeaderTO.setVersion("1.0");
		payHeaderTO.setCommProtocol("IP");
		payHeaderTO.setCommMethod("Network");
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		TktSellerTO tktSellerTo = new TktSellerTO();
		tktSellerTo.setTsMac("DA6626");
		tktSellerTo.setTsLocation("001");
		tktSellerTo.setTsSystem("CC");
		tktSellerTo.setTsSecurity("WaltDisney1");
		payHeaderTO.setTktSeller(tktSellerTo);
		payHeaderTO.setCommandCount(new BigInteger("1"));
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdItem(new BigInteger("1"));
		cmdHeaderTO.setCmdTimeout(new BigInteger("45"));
		cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdInvoice("2018895220000011272.");
		cmdHeaderTO.setCmdActor("SYS");
		EntityTO entityTO = new EntityTO();
		dtiTxn.setEntityTO(entityTO);
		entityTO.setActive(true);
		dtiTxn.setEntityTO(entityTO);
		entityTO.setEntityId(1);
		entityTO.setDefPymtId(1);
		entityTO.setDefPymtData("1");

		entityTO.setStartValidDate(new Date(System.currentTimeMillis()));
		entityTO.setEndValidDate(new Date(System.currentTimeMillis()));
		cmdHeaderTO.setCmdDevice("PAULH");
		cmdHeaderTO.setCmdOperator("PAULH");

	}

	/**
	 * Common code for Ticket Creation
	 * 
	 * @return
	 */
	protected ArrayList<TicketTO> getTicketList(TicketIdType type) {
		TicketTO ticket = new TicketTO();
		switch (type) {
		case MAG_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setProdQty(new BigInteger("1"));
			// ticket.set
			ticket.setMag("AFTWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRWFFWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRW12");
			ticket.setProdCode("1");
			ticket.setProdPrice(new BigDecimal("1"));
			ticket.setFromPrice(new BigDecimal("1"));
			ticket.setFromProdCode("1");
			ticket.setUpgrdPrice(new BigDecimal("0"));
			break;
		case TKTNID_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setTktNID("12000507111600050");
			break;
		case EXTERNAL_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setExternal("1");
			ticket.setProdCode("1");
			ticket.setProdPrice(new BigDecimal("1"));
			break;
		}

		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		tktList.add(ticket);
		return tktList;
	}

	/**
	 * @return
	 * @throws ParseException
	 */
	protected TicketTO getTicketTO(boolean provider,boolean price) throws ParseException{
		TicketTO ticketTO = new TicketTO();
		ticketTO.setAccountId("123456");
		ticketTO.setTktItem(new BigInteger("1"));
		ticketTO.setProdCode("236548");
		ticketTO.setTktItem(new BigInteger("2"));
		ticketTO.setDssn("2017-10-10", "test", "test", "5");
		if(provider){
			ticketTO.setProviderTicketType(new BigInteger("2"));	
		}else{
			ticketTO.setProviderTicketType(new BigInteger("0"));	
		}
		if(price)
		ticketTO.setTktPrice(new BigDecimal(2));
		else
			ticketTO.setTktPrice(new BigDecimal(0));
		
		return ticketTO;

	}
	/**
	 * For Mocking DAOHelper processUpdate
	 */
	public void processMockUpdate() {
		new MockUp<DAOHelper>() {
			@Mock
			public int processUpdate(Object[] inputValues) {
				return 1;
			}
		};
	}
	

}
