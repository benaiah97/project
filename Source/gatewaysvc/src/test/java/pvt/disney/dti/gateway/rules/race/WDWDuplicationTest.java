/**
 * 
 */
package pvt.disney.dti.gateway.rules.race;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import pvt.disney.dti.gateway.rules.race.utility.WDWAlgorithmUtility;

/**
 * The Class WDWDuplicationTest. Checks that tehre are no dups
 * and that numeric portion of rescode is valid and meets
 * requirements for compatibility with legacy systems.
 *
 * @author MOONS012
 */
//@PerfTest(invocations = 10, threads = 1)
//@Required(max = 1800, average = 850)
public class WDWDuplicationTest {
	

	/** The rule. */
	@Rule
	public ContiPerfRule i = new ContiPerfRule();
	
	/** The api. */
	//private static ResCodeApiImpl util = ResCodeApiImpl.getInstance(); 
	private static WDWAlgorithmUtility wdwutil = new WDWAlgorithmUtility();
	/** The shoud I run. */
	private static boolean shoudIRun = false;
	
	/** The rescode map. */
	private static HashMap<String,String> rescodeMap = new HashMap<String,String>();
	
	/** The duplicatefound. */
	private static boolean duplicatefound = false;
	
	private static final int iterationsToRun = Integer.valueOf(  ResourceBundle.getBundle("testData").getString("test.run.wdw.duplication.iterations") ) ;;
	
	/** The nbr duplicates. */
	private static int nbrDuplicates = 0;
	
	/** The test results file. */
	private static String testResultsFile;
	
	/** The should output duplicates. */
	private static boolean shouldOutputDuplicates;
	
	
	public void DuplicationTest() {
	

	}

	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("testData");
		shoudIRun = Boolean.getBoolean( rb.getString("test.run.wdw.duplication") );
		//iterationsToRun= Integer.valueOf( rb.getString("test.run.wdw.duplication.iterations") ) ;
		
		shouldOutputDuplicates = Boolean.getBoolean(rb.getString("test.run.wdw.duplication.outputresult") ) ;
		testResultsFile = rb.getString("test.run.wdw.results.filename");
		PrintWriter writer = new PrintWriter(testResultsFile, "UTF-8");
		 
		writer.println("===============================================================");
		writer.println("RESULTS FOR " + iterationsToRun + " ITERATIONS");
		writer.println("===============================================================");
		writer.close();
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test duplication.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@PerfTest(invocations = 1, threads = 1)
	//@Required(max = 2800, average = 1500)
	public void testDuplication() throws Exception {
		int MINIMUM_NUMERIC = 5;
		int INDEX_NUMERIC_START = 4;
		
		if ( !(shoudIRun) ) {
			System.out.println("Running test for duplication " + iterationsToRun + ". This could take awhile.");
			
			FileWriter fw = new FileWriter(testResultsFile, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw);          

			
			for(int i=0; i < iterationsToRun ;i++) {
				Thread.sleep(600);
				String rescode =  WDWAlgorithmUtility.generateResCode();
				int numericStart =  Integer.parseInt(rescode.substring(INDEX_NUMERIC_START, INDEX_NUMERIC_START+1));
				
				System.out.println ("TEST RESCODE: " + rescode);
				System.out.println("TEST stringNumericStart:" +rescode.charAt(INDEX_NUMERIC_START));
				System.out.println(" EST NumericStart:" +numericStart);

				//check that numeric starts with the minimum allowed number	
				boolean isValidNumeric = ( numericStart >= MINIMUM_NUMERIC ? true : false);
				assertTrue("The numeric portion of the code started with invalid value of " + rescode.charAt(INDEX_NUMERIC_START), isValidNumeric );
				out.println(rescode);
				System.out.println("rescodeMap.size=" + rescode.length());
				if (rescodeMap.containsKey(rescode)) {
					duplicatefound = true;
					nbrDuplicates++;
					rescodeMap.put(rescode, rescode);
					
					System.out.println("Duplicate rescode " + rescode +" created on iteration " + i);
				}
				rescodeMap.put(rescode, rescode);				
		    }
			//ResourceBundle rb = ResourceBundle.getBundle("testData");
			//testResultsFile = rb.getString("test.run.wdw.results.filename");
			 
			String testResultsString = iterationsToRun + " iterations run. " + nbrDuplicates + " duplicates generated";
			System.out.println(testResultsString);
			out.println(testResultsString);

			out.close();
			assertFalse("Duplicates found", duplicatefound);
		}
		else {
			System.out.println("Skipping duplication test.");
		}
	}
	
	/**
	 * Prints the rescode map.
	 *
	 * @param map the map
	 */
	private void printRescodeMap(HashMap<String,String> map) {
		Set<String> keys = map.keySet();
		for (String value : keys) {
			System.out.println(value);
		}
	}

}
