package pvt.disney.dti.gateway.rules;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.dao.BarricadeKey;
import pvt.disney.dti.gateway.dao.PropertyKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.BarricadeTO;
import pvt.disney.dti.gateway.data.common.CosGrpTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PropertyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.util.CosUtil;
import pvt.disney.dti.gateway.util.DTIFormatter;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/**
 * The Class CalmRules.
 * 
 * @author lewit019, moons012
 * @since 2.16.3
 */
public class CalmRules {

	/** The standard core logging mechanism. */
	private EventLogger logger = EventLogger.getLogger(this.getClass());

	/** The hkd down file name. */
	private static String hkdDownFileName = null;

	/** The this obj. */
	private static CalmRules thisObj = null;

	/** The hkd calm active. */
	private static boolean hkdCalmActive = false;

	/** The query ticket reply macs. */
	private static ArrayList<String> queryTicketReplyMacs = new ArrayList<String>();

	/** The application. */
	private static String application = null;

	/** The environment. */
	private static String environment = null;

	/** The tpo id. */
	private static Integer tpoId = 0;

	/**
	 * Instantiates a new calm rules.
	 *
	 * @param props the props
	 * @throws DTIException the DTI exception
	 */

   private CalmRules(Properties props) throws DTIException {
      
      // Added HDK as of 2.16.3, JTL
      hkdDownFileName = PropertyHelper.readPropsValue(PropertyName.CALM_HKD_DOWN_FILENAME, props, null);
      if (hkdDownFileName == null) {
         hkdCalmActive = false;
      } else {
         hkdCalmActive = true;
      }

      String qtMacs = PropertyHelper.readPropsValue(PropertyName.CALM_QUERYTICKET_REPLYMACS, props, null);
      if (qtMacs != null) {
         String[] result = qtMacs.split(",");
         for (int x = 0; x < result.length; x++) {
            queryTicketReplyMacs.add(result[x]);
         }
      }
      
      application = PropertyHelper.readPropsValue(PropertyName.DTI_APPLICATION, props, null);
      environment = System.getProperty("APP_ENV");

      logger.sendEvent("Contingency Actions Logic Module (CALM) rules initialized.", EventType.DEBUG, this);

		return;

	}

	/**
	 * Gets the single instance of CalmRules.
	 *
	 * @param props the props
	 * @return single instance of CalmRules
	 * @throws DTIException the DTI exception
	 */
	public static CalmRules getInstance(Properties props) throws DTIException {

		if (thisObj == null) {
			thisObj = new CalmRules(props);
		}

		return thisObj;
	}

	/**
	 * Contingency Actions Logic Module (CALM).
	 *
	 * @param dtiTxn the dti txn
	 * @throws DTIException the DTI exception
	 * @throws DTICalmException the DTI calm exception
	 */
	public void checkContingencyActionsLogicModule(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

      String tpiCode = dtiTxn.getTpiCode();
      File downFile = null;

      if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) {

         tpoId = 2;
         if (isWallRaised()) {
            executeWDWDownRules(dtiTxn);
         } else if (isBarricadeRaised(dtiTxn)) {
            throw new DTIException(CalmRules.class, DTIErrorCode.INVALID_SALES_DATE_TIME,
                     "WDW Request attempted when Barricade is raised.");
         }
      } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) {

         tpoId = 1;
         if (isWallRaised()) {
            executeDLRDownRules(dtiTxn);
         } else if (isBarricadeRaised(dtiTxn)) {
            throw new DTIException(CalmRules.class, DTIErrorCode.INVALID_SALES_DATE_TIME,
                     "DLR Request attempted when Barricade is raised.");
         }
      } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_HKD) && hkdCalmActive) {
         downFile = new File(hkdDownFileName);

         if (downFile.exists()) {
            executeHKDDownRules(dtiTxn);
		  }
		}

		return;
	}

	/**
	 * Contingency Actions Logic Module (CALM).
	 *
	 * @param dtiTxn the dti txn
	 * @throws DTIException the DTI exception
	 * @throws DTICalmException the DTI calm exception
	 */
	protected void executeWDWDownRules(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		TktSellerTO tktSeller = payloadHdr.getTktSeller();
		String tsMac = tktSeller.getTsMac();

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) being checked for " + tsMac,
				EventType.DEBUG, this);

		if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

			boolean containsMac = false;
			for (int i = 0; i < queryTicketReplyMacs.size(); i++) {
				String replyMac = queryTicketReplyMacs.get(i);

				if (replyMac.compareToIgnoreCase(tsMac) == 0) {
					containsMac = true;
					break;
				}
			}

			if (containsMac) {
				createAPQueryWDWTicketResp(dtiTxn);
				throw new DTICalmException(dtiTxn);
			}
		}
		throw new DTIException(BusinessRules.class,
				DTIErrorCode.INVALID_SALES_DATE_TIME,
				"WDW Request attempted when WDWDown outage wall property is present in the database (CALM).");
	}

	/**
	 * Contingency Actions Logic Module (CALM).
	 *
	 * @param dtiTxn the dti txn
	 * @throws DTIException the DTI exception
	 * @throws DTICalmException the DTI calm exception
	 */
	protected void executeDLRDownRules(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		TktSellerTO tktSeller = payloadHdr.getTktSeller();
		String tsMac = tktSeller.getTsMac();

		if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

			boolean containsMac = false;
			for (int i = 0; i < queryTicketReplyMacs.size(); i++) {
				String replyMac = queryTicketReplyMacs.get(i);
				if (replyMac.compareToIgnoreCase(tsMac) == 0) {
					containsMac = true;
					break;
				}
			}

			if (containsMac) {
				createAPQueryDLRTicketResp(dtiTxn);
				throw new DTICalmException(dtiTxn);
			}
		}

      throw new DTIException(CalmRules.class,
				DTIErrorCode.INVALID_SALES_DATE_TIME,
				"DLR Request attempted when DLRDown outage wall property is present in the database (CALM).");
	}

	/**
	 * Creates a simulated Annual Pass Query WDW Ticket Response based on input. Presumes that basic editing (DTI Rules) has already taken place.
	 *
	 * @param dtiTxn the dti txn
	 */
	private void createAPQueryWDWTicketResp(DTITransactionTO dtiTxn) {

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) generating AP Query WDW Response.",
				EventType.DEBUG, this);

		/** Create the response in the DTI Transaction object. */
		DTIResponseTO dtiResp = new DTIResponseTO();
		dtiTxn.setResponse(dtiResp);

		// Create Payload Header and Command Header
		DTIFormatter.formatDefaultDTIResponseHeaders(dtiTxn, dtiResp);

		// Create Command body
		QueryTicketRequestTO qryTktRqst = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		QueryTicketResponseTO qryTktResp = new QueryTicketResponseTO();
		dtiResp.setCommandBody(qryTktResp);

		// Get the first ticket and place it into response.
		ArrayList<TicketTO> requestTktList = qryTktRqst.getTktList();
		TicketTO ticket = requestTktList.get(0);
		qryTktResp.addTicket(ticket);

		ticket.setTktPrice(new BigDecimal("0.0"));
		ticket.setTktTax(new BigDecimal("0.0"));

		TktStatusTO tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Voidable");
		tktStatusTO.setStatusValue("NO");
		ticket.addTicketStatus(tktStatusTO);

		tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Active");
		tktStatusTO.setStatusValue("YES");
		ticket.addTicketStatus(tktStatusTO);

		// Default pass dates to start 5 days prior and end 5 days from now.
		Date now = new Date();
		long startTime = now.getTime() - (5 * (86400 * 1000));
		Date startDate = new Date();
		startDate.setTime(startTime);
		GregorianCalendar startValidity = new GregorianCalendar();
		startValidity.setTime(startDate);
		ticket.setTktValidityValidStart(startValidity);

		long endTime = now.getTime() + (5 * (86400 * 1000));
		Date endDate = new Date();
		endDate.setTime(endTime);
		GregorianCalendar endValidity = new GregorianCalendar();
		endValidity.setTime(endDate);
		ticket.setTktValidityValidEnd(endValidity);

		ticket.setResident("MOK");
		ticket.setPassType("ANNUAL ");
		ticket.setPassClass("CALM");
		ticket.setMediaType("A");
		ticket.setAgeGroup("A");

		return;
	}

	/**
	 * Creates a simulated Annual Pass Query DLR Ticket Response based on input. Presumes that basic editing (DTI Rules) has already taken place.
	 *
	 * @param dtiTxn the dti txn
	 */
	private void createAPQueryDLRTicketResp(DTITransactionTO dtiTxn) {

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) generating AP Query DLR Response.",
				EventType.DEBUG, this);

		/** Create the response in the DTI Transaction object. */
		DTIResponseTO dtiResp = new DTIResponseTO();
		dtiTxn.setResponse(dtiResp);

		QueryTicketRequestTO qryTktRqst = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();

		// Create Payload Header and Command Header
		DTIFormatter.formatDefaultDTIResponseHeaders(dtiTxn, dtiResp);

		QueryTicketResponseTO qryTktResp = new QueryTicketResponseTO();
		dtiResp.setCommandBody(qryTktResp);

		// Get the first ticket and place it into response.
		ArrayList<TicketTO> requestTktList = qryTktRqst.getTktList();
		TicketTO ticket = requestTktList.get(0);
		qryTktResp.addTicket(ticket);

		ticket.setTktPrice(new BigDecimal("0.0"));
		ticket.setTktTax(new BigDecimal("0.0"));

		TktStatusTO tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Voidable");
		tktStatusTO.setStatusValue("NO");
		ticket.addTicketStatus(tktStatusTO);

		tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Active");
		tktStatusTO.setStatusValue("YES");
		ticket.addTicketStatus(tktStatusTO);

		// Default pass dates to start 5 days prior and end 5 days from now.
		Date now = new Date();
		long startTime = now.getTime() - (5 * (86400 * 1000));
		Date startDate = new Date();
		startDate.setTime(startTime);
		GregorianCalendar startValidity = new GregorianCalendar();
		startValidity.setTime(startDate);
		ticket.setTktValidityValidStart(startValidity);

		long endTime = now.getTime() + (5 * (86400 * 1000));
		Date endDate = new Date();
		endDate.setTime(endTime);
		GregorianCalendar endValidity = new GregorianCalendar();
		endValidity.setTime(endDate);
		ticket.setTktValidityValidEnd(endValidity);

		ticket.setPassType("SIGNTUREPL"); // As of 2.15.3 - JTL
		// ticket.setPassType("ANNUAL ");
		ticket.setPassClass("CALM");

		return;
	}

	 /**
 	 * Contingency Actions Logic Module (CALM).
 	 *
 	 * @param dtiTxn the dti txn
 	 * @throws DTIException TODO:  Define rules
 	 * @throws DTICalmException the DTI calm exception
 	 */
  protected void executeHKDDownRules(DTITransactionTO dtiTxn) throws DTIException,
      DTICalmException {
    
	  DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		TktSellerTO tktSeller = payloadHdr.getTktSeller();
		String tsMac = tktSeller.getTsMac();

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) being checked for " + tsMac,
				EventType.DEBUG, this);

		if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

			boolean containsMac = false;
			for (int i = 0; i < queryTicketReplyMacs.size(); i++) {
				String replyMac = queryTicketReplyMacs.get(i);

				if (replyMac.compareToIgnoreCase(tsMac) == 0) {
					containsMac = true;
					break;
				}
			}

			if (containsMac) {
				createAPQueryWDWTicketResp(dtiTxn);
				throw new DTICalmException(dtiTxn);
			}
		}

      throw new DTIException(CalmRules.class,
            DTIErrorCode.INVALID_SALES_DATE_TIME,
            "HKD Request attempted when HKDDown outage wall file is present (CALM).");
  }

   /**
    * Checks if is wall raised.
    *
    * @return the boolean
    */
   private Boolean isWallRaised() {

      Boolean wallRaised = false;
      List<PropertyTO> propertyList;
      
      try {
         propertyList = PropertyKey.getProperties(application, tpoId, environment, PropertyName.DTI_CALM_SECTION);
         if ((propertyList != null) && propertyList.size() > 0) {
            logger.sendEvent("The WALLRAISED property has been found in the database.", EventType.DEBUG, this);

            // this should be a list of size 1. There is only a single value (boolean) for
            // each campus that indicates if the wall is raised or not
            PropertyTO wallRaisedProperty = propertyList.get(0);
            wallRaised = Boolean.parseBoolean(wallRaisedProperty.getPropSetValue());
         } else {
            logger.sendEvent("Wall Properties are not defined in database.", EventType.WARN, this);
         }
      } catch (Exception ex) {
         logger.sendEvent("Wall Properties are not defined in database.", EventType.WARN, this);
      }
      return wallRaised;

   }

  
   /**
    * Checks if barricade is raised.
    * @param dtiTxn the dti txn
    * @return the boolean
    */
   private boolean isBarricadeRaised(DTITransactionTO dtiTxn) {
      
      boolean isBarricadeAvailable = false;

      try {
         // barricadeTOs
         List<BarricadeTO> barricadeTOs;
         // ownerId
         String ownerId = dtiTxn.getProvider().toString().substring(0, 3);

         // To get the CosGrpid
         CosGrpTO cosGrpTO = CosUtil.lookupCosGrp(dtiTxn);

         // To get the Barricade details for COS group Id and OwnerId
         barricadeTOs = BarricadeKey.getBarricadeLookup(cosGrpTO.getCosgrpid(), ownerId);
         
         if (null != barricadeTOs && barricadeTOs.size() > 0) {

            isBarricadeAvailable = checkBarricade(dtiTxn, barricadeTOs);
         }
      } catch (Exception ex) {
         logger.sendEvent("Exception executing isBarricadeRaised ", EventType.WARN, this);
      }
      return isBarricadeAvailable;
   }

   /**
    * Iterating through barricadeTO list and checking if barricade is active
    * 
    * @param dtiTxn
    * @param isActive
    * @param barricadeTOs
    * @return the boolean
    */
   private boolean checkBarricade(DTITransactionTO dtiTxn, List<BarricadeTO> barricadeTOs) {
       
      for (BarricadeTO barricadeTO : barricadeTOs) {

         if(isBarricadeActive(dtiTxn, barricadeTO)){
            return true;
         }
      }
      return false;
   }

   /**
    * Checks barricadeTO with dtiTxnTO to identify if barricade is active
    * @param dtiTxn
    * @param barricadeTO
     @return the boolean
    */
   private boolean isBarricadeActive(DTITransactionTO dtiTxn, BarricadeTO barricadeTO) {
      
      // TsMac attribute and TsLoc attribute of a Barricade are null
      if ((null == barricadeTO.getTsMacID()) && (null == barricadeTO.getTsLocID())) {
         return true;
      }

      // TsMac attribute is not null and TsLoc is null
      if ((null != barricadeTO.getTsMacID()) && (null == barricadeTO.getTsLocID())) {

         if (Integer.parseInt(barricadeTO.getTsMacID()) == dtiTxn.getEntityTO().getMacEntityId()) {
            return true;
         }
      }

      // TsMac attribute and the barricade TsLoc attribute are not null
      if ((null != barricadeTO.getTsMacID()) && (null != barricadeTO.getTsLocID())) {

         // Barricade TsMac and TsLoc matches with TsMac and TsLoc in
         // the DTI transaction request
         if ((Integer.parseInt(barricadeTO.getTsMacID()) == dtiTxn.getEntityTO().getMacEntityId())
                  && (Integer.parseInt(barricadeTO.getTsLocID()) == dtiTxn.getEntityTO().getEntityId())) {
            return true;
         }
         
      }
      return false;
   }

}

