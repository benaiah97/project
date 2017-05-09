/**
 * 
 */
package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * The Class TestCalmRules.
 *
 * @author moons012
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TestCalmRules.class})
public class TestCalmRules {
	
	//SETUP ALL OUR PRE-REQUISITE OBJECTS FOR TESTING

	/** The campus down wdw file. */
	private static  String CAMPUS_DOWN_WDW_FILE;
	
	/** The campus down dlr file. */
	private static  String CAMPUS_DOWN_DLR_FILE ;
	
	/** The campus down hkd file. */
	private static  String CAMPUS_DOWN_HKD_FILE;
	
	/** The test props. */
	private static Properties testProps;
	
	/** The down filelist. */
	private static ArrayList<File> DOWN_FILELIST;
	
	/** The wdw txn. */
	private static DTITransactionTO WDW_TXN;
	
	/** The dlr txn. */
	private static DTITransactionTO DLR_TXN;
	
	/** The hkd txn. */
	private static DTITransactionTO HKD_TXN;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//get our app properties for testing
		testProps = TestCalmRules.helpGetPropeties();
		
		//setup our calm files
		TestCalmRules.helpWriteCampusDownFiles();

		//setup our DTITransaction TOs for testing
		TestCalmRules.helpCreateDtiTransactions();
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//clean up after ourselves
		TestCalmRules.helpCleanupCampusDownfiles();
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
	 * Test method for {@link pvt.disney.dti.gateway.rules.CalmRules#checkContingencyActionsLogicModule(pvt.disney.dti.gateway.data.DTITransactionTO)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckContingencyActionsLogicModuleForHkd() throws Exception {
		System.out.println("Testing HKD Calm Rules");
		CalmRules rules = CalmRules.getInstance(testProps);
		
		try {
			rules.checkContingencyActionsLogicModule(HKD_TXN);
			fail("No Calm Exception thrown for HKD");
		} catch (DTIException e) {
			assertThat(e.getDtiErrorCode().getErrorCode(), org.hamcrest.Matchers.containsString(DTIErrorCode.INVALID_SALES_DATE_TIME.getErrorCode()));
		}
	}
	
	/**
	 * Test method for {@link pvt.disney.dti.gateway.rules.CalmRules#checkContingencyActionsLogicModule(pvt.disney.dti.gateway.data.DTITransactionTO)}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckContingencyActionsLogicModuleForWdw() throws Exception {
		System.out.println("Testing WDW Calm Rules");
		
		CalmRules rules = CalmRules.getInstance(testProps);
		
		try {
			rules.checkContingencyActionsLogicModule(WDW_TXN);
			fail("No Calm Exception thrown for WDW");
		} catch (DTIException e) {
			assertThat(e.getDtiErrorCode().getErrorCode(), org.hamcrest.Matchers.containsString(DTIErrorCode.INVALID_SALES_DATE_TIME.getErrorCode()));
		}
	}
	
	/**
	 * Test method for {@link pvt.disney.dti.gateway.rules.CalmRules#checkContingencyActionsLogicModule(pvt.disney.dti.gateway.data.DTITransactionTO)}.
	 *
	 * @throws Exception the exception
	 */
	@Test 
	public void testCheckContingencyActionsLogicModuleForDlr() throws Exception {
		System.out.println("Testing DLR Calm Rules");

		CalmRules rules = CalmRules.getInstance(testProps);
		try {
			rules.checkContingencyActionsLogicModule(DLR_TXN);
			fail("No Calm Exception thrown for DLR");
		} catch (DTIException e) {
			assertThat(e.getDtiErrorCode().getErrorCode(), org.hamcrest.Matchers.containsString(DTIErrorCode.INVALID_SALES_DATE_TIME.getErrorCode()));
		}
	}
	
	/**
	 * Help setup campus down file names.
	 */
	private static void helpSetupCampusDownFileNames() {
		//setup our filenames
		CAMPUS_DOWN_WDW_FILE = PropertyHelper.readPropsValue(PropertyName.CALM_WDW_DOWN_FILENAME, testProps, null);
		CAMPUS_DOWN_DLR_FILE = PropertyHelper.readPropsValue(PropertyName.CALM_DLR_DOWN_FILENAME, testProps, null);
		CAMPUS_DOWN_HKD_FILE = PropertyHelper.readPropsValue(PropertyName.CALM_HKD_DOWN_FILENAME, testProps, null);
	
	}
	
	/**
	 * Help write campus down files.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void helpWriteCampusDownFiles() throws IOException{
		TestCalmRules.helpSetupCampusDownFileNames();
		//setup the list of files
		DOWN_FILELIST = new ArrayList<File>();
		
		//create the file class and add it to the list
		File wdwFile = new File(CAMPUS_DOWN_WDW_FILE);
		DOWN_FILELIST.add(wdwFile);			
		File dlrFile = new File(CAMPUS_DOWN_DLR_FILE);
		DOWN_FILELIST.add(dlrFile);	
		File hkdFile = new File(CAMPUS_DOWN_HKD_FILE);
		DOWN_FILELIST.add(hkdFile);
		
		//iterate through the file classes and write the files to disk
		for (File file : DOWN_FILELIST) {
			file.createNewFile();
		}
	}
	
	/**
	 * Help cleanup campus downfiles.
	 */
	private static void helpCleanupCampusDownfiles() {		
		//iterate through the file classes and delete the files on disk
		for (File file : DOWN_FILELIST) {
			file.delete();
		}
	}

	/**
	 * Help get properties.
	 *
	 * @return the properties
	 * @throws Exception the exception
	 */
	private static final Properties helpGetPropeties() {		
		ResourceBundle rb = ResourceBundle.getBundle("dtiApp_calm");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb); 
	    return props;
	}
	
	/**
	 * Help create dti transactions.
	 */
	private static void helpCreateDtiTransactions() {
		WDW_TXN = helpCreateWDWTransaction();
		DLR_TXN = helpCreateDLRTransaction();
		HKD_TXN = helpCreateHKDLTransaction();
	}
	
	private static DTITransactionTO helpCreateHKDLTransaction() {
		HKD_TXN = new DTITransactionTO(TransactionType.QUERYTICKET);
		HKD_TXN.setProvider(ProviderType.HKDNEXUS);
		
		DTIRequestTO dtiRequest = new DTIRequestTO();
		TktSellerTO tktSellerTO = new TktSellerTO();
		tktSellerTO.setTsMac("CITYLINE");
		
		PayloadHeaderTO payloadHdr = new PayloadHeaderTO();
		payloadHdr.setTktSeller(tktSellerTO);
		
		dtiRequest.setPayloadHeader(payloadHdr);
		HKD_TXN.setRequest(dtiRequest);
		
		return HKD_TXN;
	}
	
	private static DTITransactionTO helpCreateWDWTransaction() {
		WDW_TXN = new DTITransactionTO(TransactionType.QUERYTICKET);
		WDW_TXN.setProvider(ProviderType.WDWNEXUS);
		
		DTIRequestTO dtiRequest = new DTIRequestTO();
		
		TktSellerTO tktSellerTO = new TktSellerTO();
		tktSellerTO.setTsMac("WDPRONAWDW");
		
		PayloadHeaderTO payloadHdr = new PayloadHeaderTO();
		payloadHdr.setTktSeller(tktSellerTO);
		
		dtiRequest.setPayloadHeader(payloadHdr);
		WDW_TXN.setRequest(dtiRequest);
		
		return WDW_TXN;	
	}	
	
	private static DTITransactionTO helpCreateDLRTransaction() {
		DLR_TXN = new DTITransactionTO(TransactionType.QUERYTICKET);
		DLR_TXN.setProvider(ProviderType.DLRGATEWAY);
		DTIRequestTO dtiRequest = new DTIRequestTO();
		
		TktSellerTO tktSellerTO = new TktSellerTO();
		tktSellerTO.setTsMac("WDPRONAWDW");
		
		PayloadHeaderTO payloadHdr = new PayloadHeaderTO();
		payloadHdr.setTktSeller(tktSellerTO);
		
		dtiRequest.setPayloadHeader(payloadHdr);
		DLR_TXN.setRequest(dtiRequest);
		
		return DLR_TXN;	
	}
	
}
