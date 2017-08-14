package pvt.disney.dti.gateway.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for ConnectionException
 */
public class ConnectionExceptionTestCase {

	private static String exception = "Exception";
	private static ConnectionException connectionException;
	private static SQLException sql;
	private static Throwable th;
	private String path = "./src/test/resources/ConnectionException.txt";

	private FileOutputStream out() {
		File file = new File(path);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {

		}
		return out;
	}

	/**
	 * ConnectionException, Throwable and SQLException Objects
	 */
	@BeforeClass
	public static void beforeClass() {
		connectionException = new ConnectionException(exception);
		sql = new SQLException();
		th = new Throwable();
	}

	/**
	 * Test Case for ConnectionException 1 - Single Arg Constructor
	 */
	@Test
	public void testPrintStackTraceStream1() {
		connectionException.printStackTrace(new PrintStream(out()));
		connectionException.printStackTrace(new PrintWriter(out()));
	}

	/**
	 * Test Case for ConnectionException 2 - Double Arg Constructor with SQL
	 * Exception set with next
	 */
	@Test
	public void testPrintStackTraceStream2() {
		sql.setNextException(sql);
		connectionException = new ConnectionException(exception, sql);
		connectionException.printStackTrace(new PrintStream(out()));
		connectionException.printStackTrace(new PrintWriter(out()));
	}

	/**
	 * Test Case for ConnectionException 3 - Double Arg Constructor with
	 * Throwable
	 */
	@Test
	public void testPrintStackTraceStream3() {
		connectionException = new ConnectionException(exception, th);
		connectionException.printStackTrace(new PrintStream(out()));
		connectionException.printStackTrace(new PrintWriter(out()));
	}
}