package pvt.disney.dti.gateway.constants;

/**
 * Interface containing constants which map to the actual key names in the INI file. Useful, since it ensures typo's don't cause a problem.
 * 
 * @author lewit019
 * @version %version: %
 */
public interface PropertyName {

	public static final String FLOOD_CONTROL_ACTIVE = "DtiApp.floodControlActive";
	public static final String ENABLE_FLOOD_CONTROL_DENY = "DtiApp.floodControlDeny";
	public static final String FLOOD_CONTROL_EXCEPTION_TSLOC = "DtiApp.FloodControlExceptionTsLoc";
	public static final String DATA_SOURCE = "DtiApp.dataSource";
	public static final String REPLACE_DELIMITER = "DtiApp.REPLACEDELIMITER";
	public static final String REPLACE_TAG = "DtiApp.REPLACETAG";
	public static final String DATA_SOURCE_URL = "DtiApp.dataSourceURL";
	public static final String DBUSER = "DtiApp.dbUser";
	public static final String DBPASSWORD = "DtiApp.dbPassword";
	public static final String TSMAC_EXCLUSION = "DtiApp.TSMACExclusion"; // 2.11
	public static final String POS_TARGET = "POS.target";
	public static final String POS_VERSION = "POS.version";
	public static final String POS_PROTOCOL = "POS.protocol";
	public static final String POS_METHOD = "POS.method";
	public static final String POS_TKT_BROKER = "POS.tktBroker";
	public static final String POS_COMMAND_COUNT = "POS.commandCount";

	public static final String CALM_DLR_DOWN_FILENAME = "CALM.DLRDownFileName";
	public static final String CALM_WDW_DOWN_FILENAME = "CALM.WDWDownFileName";
  public static final String CALM_HKD_DOWN_FILENAME = "CALM.HKDDownFileName"; // As of 2.16.3, JTL	
	public static final String CALM_QUERYTICKET_REPLYMACS = "CALM.QueryTicket.ReplyMACs";

	public static final String ERROR_CODE_VALIDATION_WELL_FORMED = "DtiError.ValidationCodeWellFormed";
	public static final String ERROR_CODE_VALIDATION_VALID = "DtiError.ValidationCodeValid";
	public static final String ERROR_CODE_POS = "DtiError.DTICode";
	public static final String ERROR_CODE_DTI = "DtiError.POSCode";
	public static final String ERROR_INBOUND_LOG = "DtiError.DBInbound";
	public static final String ERROR_OUTBOUND_LOG = "DtiError.DBOutbound";
	public static final String ERROR_CODE_TARGET_VERSION = "DtiError.TargetVersionCode";

	public static final String ERROR_POS_DEFAULT_CODE = "DtiError.DefaultCode";
	public static final String ERROR_POS_DEFAULT_TYPE = "DtiError.DefaultType";
	public static final String ERROR_POS_DEFAULT_CLASS = "DtiError.DefaultClass";
	public static final String ERROR_POS_DEFAULT_TEXT = "DtiError.DefaultText";

	public static final String XML_FILE = "XML.tempFile";
	public static final String REWORK_TEXT = "Rework.text";

	public static final String ATS_SITE_NUMBER = "ATS.SiteNumber";
	public static final String ATS_MAX_ENCODE_ALL_COUNT = "ATS.MaxEncodeAllCount"; // As of 2.11

}
