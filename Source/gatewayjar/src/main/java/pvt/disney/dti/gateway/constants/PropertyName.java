package pvt.disney.dti.gateway.constants;


/**
 * Interface containing constants which map to the actual key names in the INI file. Useful, since it ensures typo's don't cause a problem.
 * 
 * @author lewit019, moons012
 * @since 2.16.3
 */
public interface PropertyName {

	/** The Constant FLOOD_CONTROL_EXCEPTION_TSLOC. */
	public static final String FLOOD_CONTROL_EXCEPTION_TSLOC = "DtiApp.FloodControlExceptionTsLoc";
	
	/** The Constant DATA_SOURCE. */
	public static final String DATA_SOURCE = "DtiApp.dataSource";
	
	/** The Constant DATA_SOURCE_URL. */
	public static final String DATA_SOURCE_URL = "DtiApp.dataSourceURL";
	
	/** The Constant DBUSER. */
	public static final String DBUSER = "DtiApp.dbUser";
	
	/** The Constant DBPASSWORD. */
	public static final String DBPASSWORD = "DtiApp.dbPassword";
	
	/** The Constant TSMAC_EXCLUSION. */
	public static final String TSMAC_EXCLUSION = "DtiApp.TSMACExclusion"; // 2.11
	
	/** The Constant POS_TARGET. */
	public static final String POS_TARGET = "POS.target";
	
	/** The Constant POS_TKT_BROKER. */
	public static final String POS_TKT_BROKER = "POS.tktBroker";

   /** The Constant CALM_HKD_DOWN_FILENAME. */
   public static final String CALM_HKD_DOWN_FILENAME = "CALM.HKDDownFileName"; // As of 2.16.3, JTL
   
   /** The Constant CALM_QUERYTICKET_REPLYMACS. */
   public static final String CALM_QUERYTICKET_REPLYMACS = "CALM.QueryTicket.ReplyMACs";

	/** The Constant ATS_SITE_NUMBER. */
	public static final String ATS_SITE_NUMBER = "ATS.SiteNumber";
	
	/** The Constant ATS_MAX_ENCODE_ALL_COUNT. */
	public static final String ATS_MAX_ENCODE_ALL_COUNT = "ATS.MaxEncodeAllCount"; // As of 2.11
	
	/** The Constant DTI_APPLICATION. */
   public static final String DTI_APPLICATION = "DtiApp.Application";

   /** The Constant DTI_ENVIRONMENT. */
   public static final String DTI_ENVIRONMENT = "DtiApp.Environment";
   
   /** The Constant for the CALM section. */
   public static final String DTI_CALM_SECTION = "WALL";
   
   /** The Constant for the CALM key. */
   public static final String DTI_CALM_KEY = "WALLRAISED";

}
