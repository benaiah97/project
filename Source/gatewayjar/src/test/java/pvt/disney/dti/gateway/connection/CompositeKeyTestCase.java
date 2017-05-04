package pvt.disney.dti.gateway.connection;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

/**@author MISHP012
 * Test class for CompositeKey
 */
public class CompositeKeyTestCase {

	private static CompositeKey compositeKey1, compositeKey2;
	private static Map<CompositeKey, String> map;
	private String str = "HelloTest";
	private static Object obj[] = new Object[5];

	/**
	 * Setting Up Array, Instantiation of CompositeKey and HashMap 
	 */
	@BeforeClass
	public static void beforeClass() {
		setupArray();
		compositeKey1 = new CompositeKey(obj);
		compositeKey2 = new CompositeKey(null);
		map = new HashMap<CompositeKey, String>();
	}

	private static void setupArray() {
		for (int i = 0; i < obj.length; i++) {
			obj[i] = new Object();
		}
	}

	/**
	 * Test Case for CompositeKey hashCode()
	 */
	@Test
	public void testCompositeKeyHash() {
		map.put(compositeKey2, str);
	}

	/**
	 * Test Case for CompositeKey equals()
	 */
	@Test
	public void testCompositeKeyEquals() {
		compositeKey2.equals(compositeKey2);
		compositeKey2.equals(null);
		compositeKey2.equals(new Object());
		compositeKey2.equals(compositeKey1);
	}

	/**
	 * Test Case for CompositeKey toString()
	 */
	@Test
	public void testToString() {
		compositeKey2.toString();
	}

}