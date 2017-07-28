package pvt.disney.dti.gateway.data.common;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import org.junit.Before;
import org.junit.Test;

/**@author MISHP012
 * JUnit for ProviderTicketTO
 */
public class ProviderTicketTOTestCase {
	ProviderTicketTO providerTicketTO = new ProviderTicketTO();
	BigInteger bigI = new BigInteger("1");

	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUp() {
		providerTicketTO.setMappedProviderTktActive(true);
		providerTicketTO.setMappedProviderTktName("mappedProviderTktName");
		providerTicketTO.setMappedProviderTktNbr(bigI);
		providerTicketTO.setPdtCode("pdtCode");
	}

	/**
	 * TestCase for providerTicketTOGettermethods
	 */
	@Test
	public void testProviderTicketTo() {
		assertEquals(providerTicketTO.getMappedProviderTktName(),
				"mappedProviderTktName");
		assertEquals(providerTicketTO.getPdtCode(), "pdtCode");
		assertEquals(providerTicketTO.getMappedProviderTktNbr(), bigI);
	}

	/**
	 * TestCase for isMappedProviderTktActive
	 */
	@Test
	public void testIsMappedProviderTktActive() {
		providerTicketTO.isMappedProviderTktActive();
	}
}
