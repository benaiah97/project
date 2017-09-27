package pvt.disney.dti.gateway.test.util;

import java.util.Properties;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * The Class CommonTestUtils.
 * 
 * @author rasta006
 */
public class CommonTestUtils {
	
	protected static String DLR_XML_PATH = "/xml/dlr/";

	/** The resource bundle. */
	@Mocked
	ResourceBundle resourceBundle;

	/** Properties. */
	Properties props = null;
	public static final String hkdXMLURls="/xml/hkd/Test_01_HKD_01.xml";

	/* for setting up the properties from dtiapp.properties */
	/**
	 * Sets the config property.
	 *
	 * @return the properties
	 */
	public Properties setConfigProperty() {
		props = new Properties();
		props.setProperty("DtiApp.dataSourceURL","http://Disney.com.com");
		props.setProperty("DtiApp.dbUser","USER");
		props.setProperty("DtiApp.dbPassword","Test");
		props.setProperty("DtiApp.TSMACExclusion", "WDWADMIN");
		props.setProperty("DtiApp.FloodControlExceptionTsLoc",
				"mkl2,VoidStore,97016000002");
		props.setProperty("POS.target", "Test");
		props.setProperty("POS.tktBroker", "DTIDV");
		props.setProperty("ATS.SiteNumber", "120");
		props.setProperty("ATS.MaxEncodeAllCount", "41");
		props.setProperty("iago.endpoint", "http://aa.com");
		props.setProperty("iago.socketTimeout", "40000");
		props.setProperty("iago.connectionTimeout", "8000");
		props.setProperty("iago.socketTimeout.renewal", "100000");
		props.setProperty("ACTIVECONN", "DEFAULTURL");
		props.setProperty("DEFAULTURL", "http://aa.com");
		props.setProperty("CONNECT_TIMEOUT_MILLIS", "5000");
		props.setProperty("READ_TIMEOUT_MILLIS", "35000");
		props.setProperty("READ_TIMEOUT_MILLIS.RENEWAL", "60000");
		props.setProperty("DtiApp.Application", "DTIGateway");
		props.setProperty("DtiApp.Environment", "Latest");
		
		return props;
	}

	/* Mocking the ResourceBundle getResourceBundle */
	/**
	 * Sets the mock property.
	 */
	public void setMockProperty() {
		new MockUp<ResourceLoader>() {
			@Mock
			public ResourceBundle getResourceBundle(String props) {
				return resourceBundle;
			}
		};
		/* Mocking the ResourceBundle convertResourceBundleToProperties */
		new MockUp<ResourceLoader>() {
			@Mock
			public Properties convertResourceBundleToProperties(
					ResourceBundle Key) {
				/* Returning the values of property from property */
				return setConfigProperty();
			}
		};
	}
	
}
