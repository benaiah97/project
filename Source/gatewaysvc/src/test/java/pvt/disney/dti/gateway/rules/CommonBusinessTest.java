package pvt.disney.dti.gateway.rules;

import java.util.Properties;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import pvt.disney.dti.gateway.util.ResourceLoader;

public class CommonBusinessTest {
	@Mocked
	ResourceBundle resourceBundle;
	Properties props=null;
	
	public Properties setConfigProperty(){
		props = new Properties();
		props.setProperty("DtiApp.TSMACExclusion", "WDWADMIN");
		props.setProperty("DtiApp.FloodControlExceptionTsLoc", "mkl2,VoidStore,97016000002");
		props.setProperty("POS.target", "Test");
		props.setProperty("POS.tktBroker","DTIDV");
		props.setProperty("ATS.SiteNumber","120");
		props.setProperty("ATS.MaxEncodeAllCount","41");
		return props;
	}
	public void setMockProperty(){
	
		
		

	 
	    
		 new MockUp<ResourceLoader>() {
             @Mock
             public Properties convertResourceBundleToProperties(
                          ResourceBundle Key) {
                   return setConfigProperty();

             }
      };

      new MockUp<ResourceLoader>() {
             @Mock
             public ResourceBundle getResourceBundle(String prop) {

                   return resourceBundle;
             }
      };
	}

}
