package pvt.disney.dti.gateway.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;

public class CommonTestDao extends CommonTestUtils{
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
}
