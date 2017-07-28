package pvt.disney.dti.gateway.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * @author MISHP012 JUnit for UpdateTicketResponseTO
 */
public class UpdateTicketResponseTOTestCase {
	UpdateTicketResponseTO updateTicketResponseTO = new UpdateTicketResponseTO();
	TicketTO ticketTO = new TicketTO();
	ArrayList<TicketTO> ticketList = new ArrayList<>();

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		updateTicketResponseTO.setTicketList(ticketList);
	}

	/**
	 * TestCase for UpdateTicketResponseToGetterMethods
	 */
	@Test
	public void testUpdateTicketResponseTo() {
		assertEquals(updateTicketResponseTO.getTicketList(), ticketList);
	}
	/**
	 * TestCase for addTickets
	 */
	@Test
	public void testAddTicket() {
		updateTicketResponseTO.addTicket(ticketTO);
	}
}
