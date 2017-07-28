package pvt.disney.dti.gateway.connection;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**@author MISHP012
 * Test class for OutParameter
 */
public class OutParameterTestCase {

	private static OutParameter outParameter;
	private static int sqlTypeParam = 20;

	/**
	 * Instantiation of OutParameter Object
	 */
	@BeforeClass
	public static void beforeClass() {
		outParameter = new OutParameter(sqlTypeParam);
	}

	/**
	 * Test case for OutParameter toString() before setting SqlInParameter
	 */
	@Test
	public void testToString() {
		outParameter.toString();
	}

	/**
	 * Test case for OutParameter getSqlType()
	 */
	@Test
	public void testGetSqlType() {
		Assert.assertNotNull(outParameter.getSqlType());
	}

	/**
	 * Test case for OutParameter SqlInParameter()
	 */
	@Test
	public void testSqlInParameter() {
		outParameter.setSqlInParameter(new Object());
		outParameter.toString();
		Assert.assertNotNull(outParameter.getSqlInParameter());
	}

}