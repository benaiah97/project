package pvt.disney.dti.gateway.util.flood;

import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.util.flood.FloodControlKeyStore.Type;


/**
 * @author MISHP012
 *Test Class for FloodControlKeyStore 
 */
public class FloodControlKeyStoreTestCase {

	private String message = "DATABASE";

	/**
	 * Test case FloodControlKeyStore Enum 
	 */
	@Test
	public void testMemory() {
		Type[] db = FloodControlKeyStore.Type.values();
		Assert.assertNotNull(db);
	}

	/**
	 * Test case for FloodControlKeyStore Value of Enum
	 */
	@Test
	public void testDataBase() {
		Assert.assertNotNull(FloodControlKeyStore.Type.valueOf(message));
	}

}