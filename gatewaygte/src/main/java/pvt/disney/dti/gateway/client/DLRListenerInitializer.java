package pvt.disney.dti.gateway.client;

import com.disney.util.AbstractInitializer;

/** The initializer for the DLR Listener
 * @author tao3
 * @version %version: 2 %
 */
public class DLRListenerInitializer extends AbstractInitializer {

  private static final String APP_NAME_SYS_VAR = "WAS_APP";
  private static final String DEFAULT_PROPS_FILE = "DLRCLIENT.properties";
  private static final String PROPS_DIR_SYS_VAR = "APP_HOME";

  /**
   * Constructor (only to be used by Parent class)
   */
  protected DLRListenerInitializer() {
  }

  /**
   * Method getAppNameSystemVar.
   * @return String Command Line parameter to specify app server instance name
   */
  public String getAppNameSystemVar() {
    return APP_NAME_SYS_VAR;
  }

  /**
   * Method getDefaultPropsFileName.
   * @return String Properties file name with app 
   */
  public String getDefaultPropsFileName() {
    return DEFAULT_PROPS_FILE;
  }

  /**
   * Method getPropsDirSystemVar.
   * @return String
   */
  public String getPropsDirSystemVar() {
    return PROPS_DIR_SYS_VAR;
  }

}
