package pvt.disney.dti.gateway.constants;

/**
 * Interface containing constants which map to the actual key names in the INI file. Useful, since it ensures typo's don't cause a problem.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public interface PropertyName {

	public static final String FLOOD_CONTROL_EXCEPTION_TSLOC = "DtiApp.FloodControlExceptionTsLoc";
	public static final String DATA_SOURCE = "DtiApp.dataSource";
	public static final String DATA_SOURCE_URL = "DtiApp.dataSourceURL";
	public static final String DBUSER = "DtiApp.dbUser";
	public static final String DBPASSWORD = "DtiApp.dbPassword";
	public static final String TSMAC_EXCLUSION = "DtiApp.TSMACExclusion"; // 2.11
	public static final String POS_TARGET = "POS.target";
	public static final String POS_TKT_BROKER = "POS.tktBroker";

	public static final String CALM_DLR_DOWN_FILENAME = "CALM.DLRDownFileName";
	public static final String CALM_WDW_DOWN_FILENAME = "CALM.WDWDownFileName";
    public static final String CALM_HKD_DOWN_FILENAME = "CALM.HKDDownFileName"; // As of 2.16.3, JTL	
	public static final String CALM_QUERYTICKET_REPLYMACS = "CALM.QueryTicket.ReplyMACs";

	public static final String ATS_SITE_NUMBER = "ATS.SiteNumber";
	public static final String ATS_MAX_ENCODE_ALL_COUNT = "ATS.MaxEncodeAllCount"; // As of 2.11

}
