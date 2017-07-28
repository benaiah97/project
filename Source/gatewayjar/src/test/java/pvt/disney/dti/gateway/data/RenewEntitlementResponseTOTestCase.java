package pvt.disney.dti.gateway.data;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;

/**
 * @author MISHP012 Test Class for RenewEntitlementResponseTO
 */
public class RenewEntitlementResponseTOTestCase {

	private static RenewEntitlementResponseTO renewEntitlementResponseTO;
	private static ArrayList<TicketTO> ticketList;
	private static ArrayList<ProductTO> productList;
	private static ArrayList<PaymentTO> paymentList;
	private String contractId = "HelloTest";
	private static TicketTransactionTO tktTran;
	private static ClientDataTO clientData;
	private static PaymentTO aPayment;
	private static TicketTO aTicket;

	/**
	 * Instantiation of RenewEntitlementResponseTO and other required members
	 */
	@BeforeClass
	public static void beforeClass() {
		renewEntitlementResponseTO = new RenewEntitlementResponseTO();
		ticketList = new ArrayList<TicketTO>();
		productList = new ArrayList<ProductTO>();
		paymentList = new ArrayList<PaymentTO>();
		tktTran = new TicketTransactionTO();
		clientData = new ClientDataTO();
		aPayment = new PaymentTO();
		aTicket = new TicketTO();
	}

	private void setupRenewObj() {
		renewEntitlementResponseTO.setTicketList(ticketList);
		renewEntitlementResponseTO.setProductList(productList);
		renewEntitlementResponseTO.setPaymentList(paymentList);
		renewEntitlementResponseTO.setContractId(contractId);
		renewEntitlementResponseTO.setTktTran(tktTran);
		renewEntitlementResponseTO.setClientData(clientData);
		renewEntitlementResponseTO.addPayment(aPayment);
		renewEntitlementResponseTO.addTicket(aTicket);
	}

	/**
	 * To setup RenewEntitlementResponseTO Object
	 */
	@Before
	public void beforeTest() {
		setupRenewObj();
	}

	/**
	 * Test Case for RenewEntitlementResponseTO Object
	 */
	@Test
	public void testRenewObj() {
		renewEntitlementResponseTO.getTicketList();
		renewEntitlementResponseTO.getProductList();
		renewEntitlementResponseTO.getPaymentList();
		renewEntitlementResponseTO.getContractId();
		renewEntitlementResponseTO.getTktTran();
		renewEntitlementResponseTO.getClientData();
	}

}