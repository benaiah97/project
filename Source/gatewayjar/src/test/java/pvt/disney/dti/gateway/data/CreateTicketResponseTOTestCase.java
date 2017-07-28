package pvt.disney.dti.gateway.data;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * @author MISHP012 JUnit for CreateTicketResponseTO
 */
public class CreateTicketResponseTOTestCase {

	CreateTicketResponseTO createTicketResponseTO = new CreateTicketResponseTO();
	ArrayList<TicketTO> ticketList = new ArrayList<>();
	PaymentTO aPayment = new PaymentTO();
	TicketTO aTicket = new TicketTO();
	ProductTO aProduct = new ProductTO();
	ArrayList<ProductTO> productList = new ArrayList<>();

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		createTicketResponseTO.setProductList(productList);
		createTicketResponseTO.setTicketList(ticketList);
	}

	/**
	 * TestCase for addPayment
	 */
	@Test
	public void testAddPayment() {
		createTicketResponseTO.addPayment(aPayment);
	}

	/**
	 * TestCase for addProduct
	 */
	@Test
	public void testAddProduct() {
		createTicketResponseTO.addProduct(aProduct);
	}

	/**
	 * TestCase for addTickets
	 */
	@Test
	public void testAddTicket() {
		createTicketResponseTO.addTicket(aTicket);
	}

	/**
	 * TestCase for CreateTicketResponseTOGetterMethods
	 */
	@Test
	public void testCreateTicketResponseTO() {
		Assert.assertEquals(createTicketResponseTO.getProductList(),
				productList);
		Assert.assertEquals(createTicketResponseTO.getTicketList(), ticketList);
		Assert.assertNotNull(createTicketResponseTO.getPaymentList().add(
				aPayment));
	}

}
