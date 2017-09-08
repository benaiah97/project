package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class TicketSearchModeTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 18573895855L;

	private TicketTO ticketSearchTO;

	
	public TicketTO getTicketSearchTO() {
		return ticketSearchTO;
	}

	public void setTicketSearchTO(TicketTO ticketSearchTO) {
		this.ticketSearchTO = ticketSearchTO;
	}

	
	

}
