package pvt.disney.dti.gateway.rules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import com.disney.util.PropertyHelper;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.EnvironmentType;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * This class has the responsibility of encapsulating all of the rules to validate the minimum contents of a message are all present or are in proper relationship to other (having this field means you need that field).
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class ContentRules {

	/** Constant value for Production (default WDW) */
	private final static String PROD = "PROD";

	/** Constant value for Production WDW */
	private final static String PRODWDW = "PROD-WDW";

	/** Constant value for Production DLR */
	private final static String PRODDLR = "PROD-DLR";

	 /** Constant value for Production HKD (as of 2.16.3, JTL) */
  private final static String PRODHKD = "PROD-HKD";
	
	/** Constant value for Test (default WDW) */
	private final static String TEST = "TEST";

	/** Constant value for Test WDW */
	private final static String TESTWDW = "TEST-WDW";

	/** Constant value for Test DLR */
	private final static String TESTDLR = "TEST-DLR";
	
	/** Constant value for Test HKD (as of 2.16.3, JTL) */	
	private final static String TESTHDK = "TEST-HKD";
	
  /** The maximum number of note details (20). */
  public final static int MAX_NUMBER_OF_NOTE_DETAILS = 20;

  /** The maximum note detail line length (50). */
  public final static int MAX_NOTE_DETAIL_LINE_LENGTH = 50;

	/** Current Target */
	private static DTITransactionTO.EnvironmentType brokerEnvType = DTITransactionTO.EnvironmentType.UNDEFINED;

	/** Properties variable to store properties */
	private static Properties props;

	/**
	 * A string of comma separated values indicating TSMAC's that are not permitted on this end-point.
	 */
	private static String tsMacExcludeList = "";

	/**
	 * Ensures that all of the Payload Header fields are present and populated.
	 * 
	 * @param payHeaderTO
	 *            the pay header transfer object.
	 * 
	 * @throws DTIException
	 *             should one of the required fields not be present.
	 */
	public static void validatePayHdrFields(PayloadHeaderTO payHeaderTO) throws DTIException {

		DTIErrorCode errCode = DTIErrorCode.INVALID_PAYLOAD_HDR;

	    ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
	    props = ResourceLoader.convertResourceBundleToProperties(rb);
		
		// PayloadId
		String payloadId = payHeaderTO.getPayloadID();
		if (payloadId == null) {
			throw new DTIException(ContentRules.class, errCode,
					"PayloadId was null.");
		}
		if ((payloadId.length() == 0) || (payloadId.length() > 20)) {
			throw new DTIException(ContentRules.class, errCode,
					"PayloadId was of invalid length.");
		}

		// Target
		String target = payHeaderTO.getTarget();
		if (target == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Target was null.");
		}
		if (target.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"Target was of zero length.");
		}

		// Version
		String version = payHeaderTO.getVersion();
		if (version == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Version was null.");
		}
		if (version.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"Version was of zero length.");
		}

		// Comm Protocol
		String commProtocol = payHeaderTO.getCommProtocol();
		if (commProtocol == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Comm Protocol was null.");
		}
		if (commProtocol.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"Comm Protocol was of zero length.");
		}

		// CommMethod
		String commMethod = payHeaderTO.getCommMethod();
		if (commMethod == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Comm Method was null.");
		}
		if (commMethod.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"Comm Method was of zero length.");
		}

		// TransmitDate/Time
		GregorianCalendar transDateTime = payHeaderTO.getTransmitDateTime();
		if (transDateTime == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Trans Date or Time was null.");
		}

		// TktSellerTO missing
		if (payHeaderTO.getTktSeller() == null) throw new DTIException(
				ContentRules.class, errCode, "TktSeller object was null.");

		// TSMAC
		String tsMac = payHeaderTO.getTktSeller().getTsMac();
		if (tsMac == null) {
			throw new DTIException(ContentRules.class, errCode,
					"TSMAC was null.");
		}
		if (tsMac.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"TSMAC was of zero length.");
		}

		// TSLocation
		String tsLocation = payHeaderTO.getTktSeller().getTsLocation();
		if (tsLocation == null) {
			throw new DTIException(ContentRules.class, errCode,
					"TSLocation was null.");
		}
		if (tsLocation.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"TSLocation was of zero length.");
		}

		// Ensure entity is permitted to communicate with this end-point (as of 2.11).
		/** Forbid certain sellers from reaching this end-point. (moved here as of 2.12) */
		tsMacExcludeList = PropertyHelper.readPropsValue(
				PropertyName.TSMAC_EXCLUSION, props, null);

		if (tsMacExcludeList != null) {
			if (tsMacExcludeList.contains(tsMac)) {
				throw new DTIException(
						EntityKey.class,
						DTIErrorCode.INVALID_GATEWAY_ENDPOINT,
						"Entity with TSMac " + tsMac + " and TSLocation " + tsLocation + " is not allowed service on this instance.");
			}
		}

		// TSSystem
		String tsSystem = payHeaderTO.getTktSeller().getTsSystem();
		if (tsSystem == null) {
			throw new DTIException(ContentRules.class, errCode,
					"TSSystem was null.");
		}
		if (tsSystem.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"TSSystem was of zero length.");
		}

		// TsSecurity
		String tsSecurity = payHeaderTO.getTktSeller().getTsSecurity();
		if (tsSecurity == null) {
			throw new DTIException(ContentRules.class, errCode,
					"TSSecurity was null.");
		}
		if (tsSecurity.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"TSSecurity was of zero length.");
		}

		// CommandCount
		BigInteger commandCount = payHeaderTO.getCommandCount();
		if (commandCount == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CommandCount was null.");
		}
		if (commandCount.intValue() <= 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CommandCount was of zero or negative value.");
		}

		return;

	}

	/**
	 * Ensures that all of the Command Header fields are present.
	 * 
	 * @param cmdHeaderTO
	 *            the cmd header transfer object
	 * 
	 * @throws DTIException
	 *             should one of the required fields not be present.
	 */
	public static void validateCmdHdrFields(CommandHeaderTO cmdHeaderTO) throws DTIException {

		DTIErrorCode errCode = DTIErrorCode.INVALID_COMMAND_HDR;

		// CmdItem
		BigInteger cmdItem = cmdHeaderTO.getCmdItem();
		if (cmdItem == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdItem was null.");
		}
		if (cmdItem.intValue() <= 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdItem was of zero value.");
		}

		// CmdTimeout
		BigInteger cmdTimeout = cmdHeaderTO.getCmdTimeout();
		if (cmdTimeout == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CommandTimeout was null.");
		}
		if (cmdTimeout.intValue() <= 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdItem was of zero value.");
		}

		// CmdDateTime
		GregorianCalendar cmdDateTime = cmdHeaderTO.getCmdDateTime();
		if (cmdDateTime == null) {
			throw new DTIException(ContentRules.class, errCode,
					"Cmd Date or Time was null.");
		}

		// CmdInvoice
		String invoice = cmdHeaderTO.getCmdInvoice();
		if (invoice == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdInvoice was null.");
		}
		if (invoice.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdInvoice was of zero length.");
		}

		// CmdDevice
		String device = cmdHeaderTO.getCmdDevice();
		if (device == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdDevice was null.");
		}
		if (device.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdDevice was of zero length.");
		}

		// CmdOperator
		String operator = cmdHeaderTO.getCmdOperator();
		if (operator == null) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdOperator was null.");
		}
		if (operator.length() == 0) {
			throw new DTIException(ContentRules.class, errCode,
					"CmdOperator was of zero length.");
		}

		return;

	}

	/**
	 * Updates the DTITransactionTO object with the provider and environment information based upon the target provided. Note: will also update the value of the actual target field to update it from legacy ("Prod") to standard ("PROD-WDW").
	 * 
	 * @param dtiTxn
	 *            The request transaction.
	 * 
	 * @return The updated DTITransactionTO object.
	 * 
	 * @throws DTIException
	 *             If the ticket provider is invalid (not supported or not known).
	 */
	public static DTITransactionTO validateProviderTarget(
			DTITransactionTO dtiTxn) throws DTIException {

		// Get the property file value for target if needed.
		if (brokerEnvType == DTITransactionTO.EnvironmentType.UNDEFINED) {
			String brokerTargetString = getProperty(PropertyName.POS_TARGET);
			if (brokerTargetString.compareToIgnoreCase(PROD) == 0) {
				brokerEnvType = DTITransactionTO.EnvironmentType.PRODUCTION;
			}
			else {
				brokerEnvType = DTITransactionTO.EnvironmentType.TEST;
			}
		}

		String target = dtiTxn.getRequest().getPayloadHeader().getTarget();

		if (target == null) {
			throw new DTIException(ContentRules.class,
					DTIErrorCode.INVALID_TARGET_VERSION,
					"Null target of sent to DTI.");
		}

		if (target.length() < 4) {
			throw new DTIException(ContentRules.class,
					DTIErrorCode.INVALID_TARGET_VERSION,
					"Invalid target of " + target + " sent to DTI.");
		}

		if ((target.compareToIgnoreCase(PROD) == 0) || (target
				.compareToIgnoreCase(PRODWDW) == 0)) {
			dtiTxn.setProvider(ProviderType.WDWNEXUS);
			dtiTxn.setEnvironment(EnvironmentType.PRODUCTION);
			dtiTxn.getRequest().getPayloadHeader().setTarget(PRODWDW);
		}
		else

		if (target.compareToIgnoreCase(PRODDLR) == 0) {
			dtiTxn.setProvider(ProviderType.DLRGATEWAY);
			dtiTxn.setEnvironment(EnvironmentType.PRODUCTION);
		}
		else

		if ((target.compareToIgnoreCase(TEST) == 0) || (target
				.compareToIgnoreCase(TESTWDW) == 0)) {
			dtiTxn.setProvider(ProviderType.WDWNEXUS);
			dtiTxn.setEnvironment(EnvironmentType.TEST);
			dtiTxn.getRequest().getPayloadHeader().setTarget(TESTWDW);
		}
		else

		if (target.compareToIgnoreCase(TESTDLR) == 0) {
			dtiTxn.setProvider(ProviderType.DLRGATEWAY);
			dtiTxn.setEnvironment(EnvironmentType.TEST);
		}
		
		// Adding HKD (as of 2.16.3, JTL)
		else 
		
		if (target.compareToIgnoreCase(PRODHKD) == 0) {
      dtiTxn.setProvider(ProviderType.HKDNEXUS);
      dtiTxn.setEnvironment(EnvironmentType.PRODUCTION);
		}
		
		else
		  
		if (target.compareToIgnoreCase(TESTHDK) == 0) {
		  dtiTxn.setProvider(ProviderType.HKDNEXUS);
		  dtiTxn.setEnvironment(EnvironmentType.TEST);
		}
		// End of HKD
		
		else {
			throw new DTIException(ContentRules.class,
					DTIErrorCode.INVALID_TARGET_VERSION,
					"Invalid target of " + target + " sent to DTI.");
		}

		if (dtiTxn.getEnvironment() != brokerEnvType) {
			throw new DTIException(ContentRules.class,
					DTIErrorCode.INVALID_TARGET_VERSION,
					"Invalid target of " + target + " sent to DTI.");
		}

		return dtiTxn;
	}

	/**
	 * Validate DTI request structure.
	 * 
	 * @param dtiTxn
	 *            the DTI transaction
	 * 
	 * @throws DTIException
	 *             should any major component of the DTI transaction be missing.
	 */
	public static void validateDTIRequestStructure(DTITransactionTO dtiTxn) throws DTIException {

		DTIRequestTO dtiRequestTO = dtiTxn.getRequest();
		if (dtiRequestTO == null) throw new DTIException(ContentRules.class,
				DTIErrorCode.INVALID_MSG_ELEMENT,
				"DTITransactionTO has no DTIRequestTO object associated.");

		PayloadHeaderTO payHeaderTO = dtiRequestTO.getPayloadHeader();
		if (payHeaderTO == null) throw new DTIException(ContentRules.class,
				DTIErrorCode.INVALID_MSG_ELEMENT,
				"DTIRequestTO has no PayloadHeaderTO object associated.");

		CommandHeaderTO cmdHeaderTO = dtiRequestTO.getCommandHeader();
		if (cmdHeaderTO == null) throw new DTIException(ContentRules.class,
				DTIErrorCode.INVALID_MSG_ELEMENT,
				"DTIRequestTO has no CommandHeaderTO object associated.");

		CommandBodyTO cmdBodyTO = dtiRequestTO.getCommandBody();
		if (cmdBodyTO == null) throw new DTIException(ContentRules.class,
				DTIErrorCode.INVALID_MSG_ELEMENT,
				"DTIRequestTO has no CommandBodyTO object associated.");

		return;

	}

	/**
	 * Determines if the target string supplied is for WDW (prod or test). This should be the target from DTITransactionTO.getRequest().getPayloadHeader().getTarget(); A NULL value returns false.
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isTargetWDW(String target) {
		return PRODWDW.equals(target) || PROD.equals(target) || TESTWDW
				.equals(target) || TEST.equals(target);

	}

	/**
	 * Determines if the target string supplied is for DLR (prod or test). This should be the target from DTITransactionTO.getRequest().getPayloadHeader().getTarget(); A NULL value returns false.
	 * 
	 * @param target
	 * @return
	 */
	public static boolean isTargetDLR(String target) {
		return PRODDLR.equals(target) || TESTDLR.equals(target);

	}

	/**
	 * Returns properties loaded at runtime. Normally, you wouldn't incur the penalty of a second stack call here, but the properties are scattered throughout the XML. In those difficult areas, this call allows for cleaner code.
	 * 
	 * @param key
	 *            java.lang.String
	 */
	private static String getProperty(String key) {

		String value = PropertyHelper.readPropsValue(key, props, null);
		return value;

	}

	
	/** 
	 * Validate that the note details being passed to ATS will not cause a system 
	 * failure by exceeding the maximum size/count.
	 * @author lewit019
	 * @since 2.17.2
	 * @param noteArray
	 */
	public static void validateATSNoteDetails(ReservationRequestTO dtiResReq) throws DTIException {
	  
	  ArrayList<String> noteArray = dtiResReq.getNoteList();
	  
	  if (noteArray != null) {
	    
	    if (noteArray.size() > MAX_NUMBER_OF_NOTE_DETAILS) {
	      throw new DTIException(ContentRules.class,
            DTIErrorCode.INVALID_MSG_ELEMENT,
            "Reservation Note count exceeds maximum allowable of " + MAX_NUMBER_OF_NOTE_DETAILS);
	    }
	    
	    for (/*each*/ String aNote: /*in*/ noteArray) {
	      if (aNote.length() > MAX_NOTE_DETAIL_LINE_LENGTH) {
	        throw new DTIException(ContentRules.class,
	            DTIErrorCode.INVALID_MSG_ELEMENT,
	            "Reservation Note length exceeds maximum allowable of " + MAX_NOTE_DETAIL_LINE_LENGTH);
	      }
	    }
	    
	  }
	  
	  return;

	}
	
	
	
	
}
