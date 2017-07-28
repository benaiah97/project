package pvt.disney.dti.gateway.data.common;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

/**
 * @author MISHP012 JUnit for ProductTO
 */
public class ProductTOTestCase {
	ProductTO productTO = new ProductTO();
	BigInteger bigi = new BigInteger("2");
	BigDecimal bigd = new BigDecimal("12.00");

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		productTO.setProdCode("prodCode");
		productTO.setProdDescription("prodDescription");
		productTO.setProdItem(bigi);
		productTO.setProdParts("prodParts");
		productTO.setProdPrice(bigd);
		productTO.setProdQty(bigi);
		productTO.setProdReceiptMsg("prodReceiptMsg");
		productTO.setProdTax1(bigd);
		productTO.setProdTax2(bigd);
	}

	/**
	 * TestCase for productTOGettermethods
	 */
	@Test
	public void testProviderTicketTo() {
		assertEquals(productTO.getProdCode(), "prodCode");
		assertEquals(productTO.getProdDescription(), "prodDescription");
		assertEquals(productTO.getProdItem(), bigi);
		assertEquals(productTO.getProdParts(), "prodParts");
		assertEquals(productTO.getProdPrice(), bigd);
		assertEquals(productTO.getProdQty(), bigi);
		assertEquals(productTO.getProdReceiptMsg(), "prodReceiptMsg");
		assertEquals(productTO.getProdTax1(), bigd);
		assertEquals(productTO.getProdTax2(), bigd);
	}
}