package pvt.disney.dti.gateway.rules.race.utility;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DebugTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Calendar theDate = new GregorianCalendar();
		
		System.out.println("the Date:" + theDate.getTime());
		SimpleDateFormat format1 = new SimpleDateFormat("MM");
		String formatted = format1.format(theDate.getTime());
		System.out.println("month: " + formatted);
	}

}
