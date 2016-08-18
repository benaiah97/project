/**
 * 
 */
package prototyping;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author smoon
 * 
 */
public class DLRRulesTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	public static void main(String[] args) {
		String dlrTS = null;
		// <TimeStamp>2004-02-04 18:01:09</TimeStamp>
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss:S");

		dlrTS = formatter.format(date);
		Logger.getAnonymousLogger().info("Timestamp=" + dlrTS);
		Logger.getAnonymousLogger().info("Grrr....ignores milliseconds format");
	}
}
