package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.AttributeKey;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
<<<<<<< HEAD
=======

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
>>>>>>> develop

/**
 * This class is responsible for three major functions for WDW query tickets:<BR>
 * 1. Defining the business rules specific to WDW query tickets.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 * 
 */
public class WDWQueryTicketRules {

<<<<<<< HEAD
  /** Request type header constant. */
  private final static String REQUEST_TYPE_QUERY = "Query";

  /** Request subtype header constant. */
  private final static String REQUEST_SUBTYPE_QUERYTICKET = "QueryTicket";

  /** Constant indicating all tags should be returned. */
  private final static String ALL_TAGS = "All";

  /** Constant indicating limited tags should be created. */
  private final static String[] QT_TAGS = { "Tax", "VoidCode", "TicketFlag", "TicketAttribute", "AlreadyUsed" };

  /**
   * Tag exception -- This is hard-coded, but when TDS gets rid of their old
   * system, we'll be able to remove this. JTL
   */
  private final static String EXCEPTION_TSMAC = "TDSNA";

  /** Constant indicating the Query Ticket XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "QueryTicketRequest.xsd";

  /** Constant text for VOIDABLE. */
  private final static String VOIDABLE = "Voidable";

  /** Constant text for ACTIVE. */
  private final static String ACTIVE = "Active";

  /** Constant text for Status Item. */
  private final static String ELECTRONICELIGIBLE = "ElectronicEligible";

  /** Constant text for Status Item. */
  private final static String ELECTRONICENTITLEMENT = "ElectronicEntitlement";

  /** Constant text for YES. */
  private final static String YES = "YES";

  /** Constant text for NO. */
  private final static String NO = "NO";

  /** Constant text for ITEM ONE (1). */
  private final static String ITEMONE = "1";
  
  /** Constant text for Renewal Code Type (as of 2.16.1, JTL) */
  private final static String RENEWAL_CODETYPE = "R";
  
  /** Constant text for Charter Renewal Code (as of 2.16.1, JTL) */
  private final static String CHARTER_CODETYPE = "C";
  
  /** Constant text for PassRenew Status INELIGIBLE (as of 2.16.1, JTL) */
  private final static String PASSRENEW_INELIGIBLE = "INELIGIBLE";
  
  /** Constant text for PassRenew Status STANDARD (as of 2.16.1, JTL) */
  private final static String PASSRENEW_STANDARD = "STANDARD";
  
  /** Constant text for PassRenew Status CHARTER (as of 2.16.1, JTL) */
  private final static String PASSRENEW_CHARTER = "CHARTER";
  
  /** Constant integer indicating the number of days before a 
   * pass expires during which it can be renewed (in days).
   */
  private final static int PRERENEWAL_WINDOW = 60;
  
  /** Constant integer indicating the number of days after a 
   * pass expires during which it can be renewed (in days).
   */
  private final static int POSTRENEWAL_WINDOW = 30;

  /**
   * Transform the DTITransactionTO value object to the provider value objects
   * and then pass those to XML Marshalling routines to create an XML string.
   * 
   * @param dtiTxn
   *          the DTI Transaction object.
   * @return the XML string version of the provider request.
   * @throws DTIException
   *           when any transformation error is encountered.
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlString = null;
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    QueryTicketRequestTO dtiQryTkt = (QueryTicketRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(OTCommandTO.OTTransactionType.QUERYTICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn, REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERYTICKET);
    atsCommand.setHeaderTO(hdr);

    // === Query Ticket Level ===
    OTQueryTicketTO otQryTkt = new OTQueryTicketTO();

    // Tags
    ArrayList<String> tagList = otQryTkt.getTagsList();
    if (EXCEPTION_TSMAC.compareTo(payloadHdr.getTktSeller().getTsMac()) == 0) {
      for (int i = 0; i < QT_TAGS.length; i++) {
        tagList.add(QT_TAGS[i]);
      }
    } else {
      tagList.add(ALL_TAGS);
    }

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();
    AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otQryTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    } else {
      otQryTkt.setSiteNumber(Integer.parseInt(anAttributeTO.getAttrValue()));
    }

    // Ticket Information
    ArrayList<TicketTO> dtiTicketList = dtiQryTkt.getTktList();
    TicketTO dtiTicket = dtiTicketList.get(0);
    ArrayList<OTTicketInfoTO> otTicketList = otQryTkt.getTicketInfoList();
    OTTicketTO otTicket = new OTTicketTO();

    OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();
    otTicketInfo.setItem(new BigInteger(ITEMONE));

    ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();
    TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

    switch (dtiTicketType) {

    case DSSN_ID:
      otTicket.setTDssn(dtiTicket.getDssnDate(), dtiTicket.getDssnSite(), dtiTicket.getDssnStation(),
          dtiTicket.getDssnNumber());
      break;
    case TKTNID_ID:
      otTicket.setTCOD(dtiTicket.getTktNID());
      break;
    case BARCODE_ID:
      otTicket.setBarCode(dtiTicket.getBarCode());
      break;
    case MAG_ID:
      otTicket.setMagTrack(dtiTicket.getMagTrack1());
      break;
    case EXTERNAL_ID:
      otTicket.setExternalTicketCode(dtiTicket.getExternal());
      break;
    }

    otTicketInfo.setTicketSearchMode(otTicket);
    otTicketList.add(otTicketInfo);

    // Set the Query Ticket TO on the command
    atsCommand.setQueryTicketTO(otQryTkt);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
  }

  /**
   * Transforms a query ticket response string from the WDW provider and updates
   * the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *          The transaction object for this request.
   * @param xmlResponse
   *          The WDW provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response
   *         information.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  static void transformResponseBody(DTITransactionTO dtiTxn, OTCommandTO otCmdTO, DTIResponseTO dtiRespTO)
      throws DTIException {

    QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    OTTicketInfoTO otTktInfo = null;
    QueryTicketResponseTO dtiQryTktResp = new QueryTicketResponseTO();
    dtiRespTO.setCommandBody(dtiQryTktResp);

    // Is there any ticket on the response (position zero presumed).
    OTQueryTicketTO otQryTktTO = otCmdTO.getQueryTicketTO();
    if ((otQryTktTO.getTicketInfoList() != null) && (otQryTktTO.getTicketInfoList().size() > 0)) {
      ArrayList<OTTicketInfoTO> otTktInfoList = otQryTktTO.getTicketInfoList();
      if ((otTktInfoList != null) && (otTktInfoList.size() > 0)) {
        otTktInfo = otTktInfoList.get(0);
      }
    }

    if (otTktInfo == null) {
      return;
    }

    TicketTO dtiTkt = new TicketTO();

    // Item
    dtiTkt.setTktItem(otTktInfo.getItem());

    // TktId
    OTTicketTO otTkt = otTktInfo.getTicket();

    // TktId.DSSN
    if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.TDSSN)) {
      dtiTkt.setDssn(otTkt.getTdssnDate(), otTkt.getTdssnSite(), otTkt.getTdssnStation(), otTkt.getTdssnTicketId());
    }

    // TktId.TktNID
    if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.TCOD)) {
      dtiTkt.setTktNID(otTkt.getTCOD());
    }

    // TktId.BarCode
    if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.BARCODE)) {
      dtiTkt.setBarCode(otTkt.getBarCode());
    }

    // TktId.Mag
    if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.MAGCODE)) {
      dtiTkt.setMag(otTkt.getMagTrack());
    }

    // TktId.External
    if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.EXTERNALTICKETCODE)) {
      dtiTkt.setExternal(otTkt.getExternalTicketCode());
    }

    // TktPrice
    dtiTkt.setTktPrice(otTktInfo.getPrice());

    // TktTax
    dtiTkt.setTktTax(otTktInfo.getTax());

    // TktStatus Voidable
    TicketTO.TktStatusTO voidable = dtiTkt.new TktStatusTO();
    voidable.setStatusItem(VOIDABLE);

    boolean isVoidable = true;
    if (otTktInfo.getVoidCode() == null) {
      isVoidable = false;
    } else if ((otTktInfo.getVoidCode().intValue() > 0) && (otTktInfo.getVoidCode().intValue() <= 100)) {
      isVoidable = false;
    }
    // Removed per request from Charles Vayianos 12/2 14:18 PM
    // if ((otTktInfo.getPrice() == null)
    // || (otTktInfo.getPrice().compareTo(new BigDecimal(ZEROPRICE)) == 0)) {
    // isVoidable = false;
    // }

    if (otTktInfo.getTicketAttribute() == null) {
      isVoidable = false;
    }

    if (otTktInfo.getAlreadyused() != null) {
      if (otTktInfo.getAlreadyused()) {
        isVoidable = false;
      }
    }

    if (isVoidable) {
      voidable.setStatusValue(YES);
    } else {
      voidable.setStatusValue(NO);
    }
    dtiTkt.addTicketStatus(voidable);

    // set status for eligible and entitlement
    if (queryReq.isIncludeElectronicAttributes()) {
      TicketTO.TktStatusTO eligible = dtiTkt.new TktStatusTO();
      eligible.setStatusItem(ELECTRONICELIGIBLE);
      eligible.setStatusValue(YES);
      TicketTO.TktStatusTO electronic = dtiTkt.new TktStatusTO();
      electronic.setStatusItem(ELECTRONICENTITLEMENT);
      electronic.setStatusValue(NO);
      if (otTktInfo.getDenyMultiEntitlementFunctions() != null) {
        if (otTktInfo.getDenyMultiEntitlementFunctions()) {
          eligible.setStatusValue(NO);
        }
      }
      if (otTktInfo.getAccountId() != null) {
        if (otTktInfo.getAccountId().longValue() > 0) {
          electronic.setStatusValue(YES);
        }
      }
      dtiTkt.addTicketStatus(eligible);
      dtiTkt.addTicketStatus(electronic);
    }

    // Is this ticket an annual pass?
    boolean isAPass = false;
    DBTicketAttributes dbTktAttr = null;
    if ((otTktInfo.getTicketType() != null) && (otTktInfo.getTicketType().intValue() > 0)) {
      dbTktAttr = AttributeKey.getWDWTicketAttributes(otTktInfo.getTicketType());

      if (dbTktAttr != null) {
        if ((dbTktAttr.getTktCode() != null) && (dbTktAttr.getPassClass() != null)) {
          isAPass = true;
        }
      }
    }

    // If this is a pass and validity dates were supplied, is the pass still
    // active?
    // Note, these rules must run in order to effective. DO NOT REORDER.
    if ((isAPass) && (otTktInfo.getValidityStartDate() != null) && (otTktInfo.getValidityEndDate() != null)) {

      TicketTO.TktStatusTO active = dtiTkt.new TktStatusTO();
      active.setStatusItem(ACTIVE);
      boolean isActive = false;

      GregorianCalendar gCalStart = otTktInfo.getValidityStartDate();
      Date startDate = gCalStart.getTime();

      // End date has to be moved to true end of day on the end date
      GregorianCalendar gCalEnd = otTktInfo.getValidityEndDate();
      gCalEnd.set(Calendar.HOUR_OF_DAY, 23);
      gCalEnd.set(Calendar.MINUTE, 59);
      gCalEnd.set(Calendar.SECOND, 59);
      Date endDate = gCalEnd.getTime();

      Date todaysDate = new Date();

      if ((todaysDate.getTime() >= startDate.getTime()) && (todaysDate.getTime() <= endDate.getTime())) {
        isActive = true;
      }

      if ((otTktInfo.getVoidCode().intValue() > 0) && (otTktInfo.getVoidCode().intValue() <= 100)) {
        isActive = false;
      }
      if (isActive) {
        active.setStatusValue(YES);
      } else {
        active.setStatusValue(NO);
      }
      dtiTkt.addTicketStatus(active);
    }

    // If this is a pass, and demographics are requested (either "RENEWAL" or "demographics") and the 
    // seasons pass demo isn't null...
    if ((isAPass) && ( (queryReq.isIncludeRenewalAttributes()) || 
        (queryReq.isIncludeTktDemographics()) ) 
        && (otTktInfo.getSeasonPassDemo() != null)) {

      DemographicsTO dtiDemoTO = new DemographicsTO();

      ArrayList<OTFieldTO> otFieldList = otTktInfo.getSeasonPassDemo().getDemoDataList();
      
      for (/* each */OTFieldTO anOTField : /* in */otFieldList) {

        // FirstName
        // LastName
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_LASTFIRST)) {

          String lastFirstString = anOTField.getFieldValue();
          if (lastFirstString.contains("/")) {
            String lastNameString = lastFirstString.substring(0, lastFirstString.indexOf("/"));
            String firstNameString = lastFirstString.substring(lastFirstString.indexOf("/") + 1);

            if ((lastNameString != null) && (lastNameString.length() != 0)) {
              dtiDemoTO.setLastName(lastNameString);
            }

            if ((firstNameString != null) && (lastNameString.length() != 0)) {
              dtiDemoTO.setFirstName(firstNameString);
            }
          }
        }

        // Addr1
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_ADDRESS_ONE)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setAddr1(anOTField.getFieldValue());
          }
        }

        // Addr2
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_ADDRESS_TWO)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setAddr2(anOTField.getFieldValue());
          }
        }

        // City
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_CITY)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setCity(anOTField.getFieldValue());
          }
        }

        // State
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_STATE)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setState(anOTField.getFieldValue());
          }
        }

        // ZIP
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_ZIP)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setZip(anOTField.getFieldValue());
          }
        }

        // Country
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_COUNTRY)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setCountry(anOTField.getFieldValue());
          }
        }

        // Telephone
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_TELEPHONE)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setTelephone(anOTField.getFieldValue());
          }
        }

        // Email
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_EMAIL)) {
          if (anOTField.getFieldValue() != null) {
            dtiDemoTO.setEmail(anOTField.getFieldValue());
          }
        }

        // (Gender) - Not to be included in the response unless "ALL" was specified
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_GENDER)) {
          if ((anOTField.getFieldValue() != null) && 
              (queryReq.isIncludeRenewalAttributes())) {
            dtiDemoTO.setGender(anOTField.getFieldValue());
          }
        }

        // (DOB) - Not to be included in the response unless "ALL" was specified 
        if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_DATE_OF_BIRTH)) {
          if ((anOTField.getFieldValue() != null) && 
              (queryReq.isIncludeRenewalAttributes()) ) {

            String dobString = anOTField.getFieldValue();
            try {
              dtiDemoTO.setDateOfBirth(OTCommandTO.convertTicketDOB(dobString)); 
            } catch (ParseException pe) {
              throw new DTIException(WDWQueryTicketRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned XML with an invalid ticket demographic date of birth: " + pe.toString());
            }
          }
        }

      }

      dtiTkt.addTicketDemographic(dtiDemoTO);
    }

    // Validity.StartDate
    if (otTktInfo.getValidityStartDate() != null)
      dtiTkt.setTktValidityValidStart(otTktInfo.getValidityStartDate());

    // Validity.EndDate
    if (otTktInfo.getValidityEndDate() != null)
      dtiTkt.setTktValidityValidEnd(otTktInfo.getValidityEndDate());

    if (dbTktAttr != null) {

      // TktAttribute - AgeGroup
      if (dbTktAttr.getAgeGroup() != null)
        dtiTkt.setAgeGroup(dbTktAttr.getAgeGroup());

      // TktAttribute - MediaType
      if (dbTktAttr.getMediaType() != null)
        dtiTkt.setMediaType(dbTktAttr.getMediaType());

      // TktAttribute - PassType
      if (dbTktAttr.getPassType() != null)
        dtiTkt.setPassType(dbTktAttr.getPassType());

      // Create Rule Set for renew if DEMO "ALL" is set (as of 2.16.1, JTL)
      if (queryReq.isIncludeRenewalAttributes()) {
        
        String renewalStatus = PASSRENEW_INELIGIBLE;
        
        // RULE 1: Void code must be zero.
        if (otTktInfo.getVoidCode().intValue() == 0) {
          
          // RULE 2: Renewal window (As comparison is exclusive, pad one day each way).
          GregorianCalendar startOfWindowGC = (GregorianCalendar) otTktInfo.getValidityEndDate().clone();
          startOfWindowGC.add(Calendar.DAY_OF_YEAR, ((PRERENEWAL_WINDOW + 1)*-1));
          GregorianCalendar endOfWindowGC = (GregorianCalendar) otTktInfo.getValidityEndDate().clone();
          endOfWindowGC.add(Calendar.DAY_OF_YEAR, (POSTRENEWAL_WINDOW + 1));
          
          Date startOfWindow = startOfWindowGC.getTime();
          Date endOfWindow = endOfWindowGC.getTime();
          
          if (DateTimeRules.isNowWithinDate(startOfWindow, endOfWindow)) {
            
            // Rule 3:  Determine type of renewal
            if (dbTktAttr.getPassRenew() == null)  {
              renewalStatus = PASSRENEW_STANDARD;
            } else if (dbTktAttr.getPassRenew().equalsIgnoreCase(RENEWAL_CODETYPE)) {
              renewalStatus = PASSRENEW_STANDARD;
            } else if (dbTktAttr.getPassRenew().equalsIgnoreCase(CHARTER_CODETYPE)) {
              renewalStatus = PASSRENEW_CHARTER;
            } else {
              renewalStatus = PASSRENEW_STANDARD;
            }

          } 
          
        }
        
        dtiTkt.setPassRenew(renewalStatus);
        
      } else { 
        // Perform old functionality 
      
        // TktAttribute - PassRenew
        if (dbTktAttr.getPassRenew() != null)
          dtiTkt.setPassRenew(dbTktAttr.getPassRenew());
      
      }

      // TktAttribute - PassClass
      if (dbTktAttr.getPassClass() != null)
        dtiTkt.setPassClass(dbTktAttr.getPassClass());

      // TktAttribute - Resident
      if (dbTktAttr.getResident() != null)
        dtiTkt.setResident(dbTktAttr.getResident());

    }

    dtiQryTktResp.addTicket(dtiTkt);

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules,
   * implement them here, but if there are a very limited set of rules, mostly
   * common to both providers, implement in the BusinessRules in the parent
   * package.<BR>
   * Currently implements the following rules: <BR>
   * RULE: Validate that ticket presented is of valid format.
   * 
   * @param dtiTxn
   *          the transaction object for this request
   * @throws DTIException
   *           for any rules error.
   */
  public static void applyWDWQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    QueryTicketRequestTO reqTO = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    ArrayList<TicketTO> aTktList = reqTO.getTktList();

    // Validate that ticket presented is of valid format.
    WDWBusinessRules.validateInBoundWDWTickets(aTktList);

    return;
  }
=======
	/** Request type header constant. */
	private final static String REQUEST_TYPE_QUERY = "Query";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_QUERYTICKET = "QueryTicket";

	/** Constant indicating all tags should be returned. */
	private final static String ALL_TAGS = "All";

	/** Constant indicating limited tags should be created. */
	private final static String[] QT_TAGS = { "Tax",
			"VoidCode",
			"TicketFlag",
			"TicketAttribute",
			"AlreadyUsed" };

	/**
	 * Tag exception -- This is hard-coded, but when TDS gets rid of their old system, we'll be able to remove this. JTL
	 */
	private final static String EXCEPTION_TSMAC = "TDSNA";

	/** Constant indicating the Query Ticket XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "QueryTicketRequest.xsd";

	/** Constant text for VOIDABLE. */
	private final static String VOIDABLE = "Voidable";

	/** Constant text for ACTIVE. */
	private final static String ACTIVE = "Active";

	/** Constant text for Status Item. */
	private final static String ELECTRONICELIGIBLE = "ElectronicEligible";

	/** Constant text for Status Item. */
	private final static String ELECTRONICENTITLEMENT = "ElectronicEntitlement";

	/** Constant text for YES. */
	private final static String YES = "YES";

	/** Constant text for NO. */
	private final static String NO = "NO";

	/** Constant text for ITEM ONE (1). */
	private final static String ITEMONE = "1";

	/** Constant text for Renewal Code Type (as of 2.16.1, JTL) */
	private final static String RENEWAL_CODETYPE = "R";

	/** Constant text for Charter Renewal Code (as of 2.16.1, JTL) */
	private final static String CHARTER_CODETYPE = "C";

	/** Constant text for PassRenew Status INELIGIBLE (as of 2.16.1, JTL) */
	private final static String PASSRENEW_INELIGIBLE = "INELIGIBLE";

	/** Constant text for PassRenew Status STANDARD (as of 2.16.1, JTL) */
	private final static String PASSRENEW_STANDARD = "STANDARD";

	/** Constant text for PassRenew Status CHARTER (as of 2.16.1, JTL) */
	private final static String PASSRENEW_CHARTER = "CHARTER";

	/**
	 * Constant integer indicating the number of days before a pass expires during which it can be renewed (in days).
	 */
	private final static int PRERENEWAL_WINDOW = 60;

	/**
	 * Constant integer indicating the number of days after a pass expires during which it can be renewed (in days).
	 */
	private final static int POSTRENEWAL_WINDOW = 30;

	private static final EventLogger logger = EventLogger.getLogger(WDWQueryTicketRules.class.getCanonicalName());

	/**
	 * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshalling routines to create an XML string.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object.
	 * @return the XML string version of the provider request.
	 * @throws DTIException
	 *             when any transformation error is encountered.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlString = null;
		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		QueryTicketRequestTO dtiQryTkt = (QueryTicketRequestTO) dtiCmdBody;

		// === Command Level ===
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.QUERYTICKET);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// === Header Level ===
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERYTICKET);
		atsCommand.setHeaderTO(hdr);

		// === Query Ticket Level ===
		OTQueryTicketTO otQryTkt = new OTQueryTicketTO();

		// Tags
		ArrayList<String> tagList = otQryTkt.getTagsList();
		if (EXCEPTION_TSMAC.compareTo(payloadHdr.getTktSeller().getTsMac()) == 0) {
			for (int i = 0; i < QT_TAGS.length; i++) {
				tagList.add(QT_TAGS[i]);
			}
		}
		else {
			tagList.add(ALL_TAGS);
		}

		// SiteNumber
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otQryTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		}
		else {
			otQryTkt.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		// Ticket Information
		ArrayList<TicketTO> dtiTicketList = dtiQryTkt.getTktList();
		TicketTO dtiTicket = dtiTicketList.get(0);
		ArrayList<OTTicketInfoTO> otTicketList = otQryTkt.getTicketInfoList();
		OTTicketTO otTicket = new OTTicketTO();

		OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();
		otTicketInfo.setItem(new BigInteger(ITEMONE));

		ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();
		TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

		switch (dtiTicketType) {

		case DSSN_ID:
			otTicket.setTDssn(dtiTicket.getDssnDate(), dtiTicket.getDssnSite(),
					dtiTicket.getDssnStation(), dtiTicket.getDssnNumber());
			break;
		case TKTNID_ID:
			otTicket.setTCOD(dtiTicket.getTktNID());
			break;
		case BARCODE_ID:
			otTicket.setBarCode(dtiTicket.getBarCode());
			break;
		case MAG_ID:
			otTicket.setMagTrack(dtiTicket.getMagTrack1());
			break;
		case EXTERNAL_ID:
			otTicket.setExternalTicketCode(dtiTicket.getExternal());
			break;
		}

		otTicketInfo.setTicketSearchMode(otTicket);
		otTicketList.add(otTicketInfo);

		// Set the Query Ticket TO on the command
		atsCommand.setQueryTicketTO(otQryTkt);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;
	}

	/**
	 * Transforms a query ticket response string from the WDW provider and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param xmlResponse
	 *            The WDW provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

		QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		OTTicketInfoTO otTktInfo = null;

		QueryTicketResponseTO dtiQryTktResp = new QueryTicketResponseTO();
		dtiRespTO.setCommandBody(dtiQryTktResp);

		// Is there any ticket on the response (position zero presumed).
		OTQueryTicketTO otQryTktTO = otCmdTO.getQueryTicketTO();
		if ((otQryTktTO.getTicketInfoList() != null) && (otQryTktTO
				.getTicketInfoList().size() > 0)) {
			ArrayList<OTTicketInfoTO> otTktInfoList = otQryTktTO
					.getTicketInfoList();
			if ((otTktInfoList != null) && (otTktInfoList.size() > 0)) {
				otTktInfo = otTktInfoList.get(0);
			}
		}

		if (otTktInfo == null) {
			return;
		}

		TicketTO dtiTkt = new TicketTO();

		// Item
		dtiTkt.setTktItem(otTktInfo.getItem());

		// TktId
		OTTicketTO otTkt = otTktInfo.getTicket();

		// TktId.DSSN
		if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.TDSSN)) {
			dtiTkt.setDssn(otTkt.getTdssnDate(), otTkt.getTdssnSite(),
					otTkt.getTdssnStation(), otTkt.getTdssnTicketId());
		}

		// TktId.TktNID
		if (otTkt.getTktIdentityTypes().contains(OTTicketTO.TicketIDType.TCOD)) {
			dtiTkt.setTktNID(otTkt.getTCOD());
		}

		// TktId.BarCode
		if (otTkt.getTktIdentityTypes().contains(
				OTTicketTO.TicketIDType.BARCODE)) {
			dtiTkt.setBarCode(otTkt.getBarCode());
		}

		// TktId.Mag
		if (otTkt.getTktIdentityTypes().contains(
				OTTicketTO.TicketIDType.MAGCODE)) {
			dtiTkt.setMag(otTkt.getMagTrack());
		}

		// TktId.External
		if (otTkt.getTktIdentityTypes().contains(
				OTTicketTO.TicketIDType.EXTERNALTICKETCODE)) {
			dtiTkt.setExternal(otTkt.getExternalTicketCode());
		}

		// TktPrice
		dtiTkt.setTktPrice(otTktInfo.getPrice());

		// TktTax
		dtiTkt.setTktTax(otTktInfo.getTax());

		// TktStatus Voidable
		TicketTO.TktStatusTO voidable = dtiTkt.new TktStatusTO();
		voidable.setStatusItem(VOIDABLE);

		boolean isVoidable = true;
		if (otTktInfo.getVoidCode() == null) {
			isVoidable = false;
		}
		else if ((otTktInfo.getVoidCode().intValue() > 0) && (otTktInfo
				.getVoidCode().intValue() <= 100)) {
			isVoidable = false;
		}
		// Removed per request from Charles Vayianos 12/2 14:18 PM
		// if ((otTktInfo.getPrice() == null)
		// || (otTktInfo.getPrice().compareTo(new BigDecimal(ZEROPRICE)) == 0)) {
		// isVoidable = false;
		// }

		if (otTktInfo.getTicketAttribute() == null) {
			isVoidable = false;
		}

		if (otTktInfo.getAlreadyused() != null) {
			if (otTktInfo.getAlreadyused()) {
				isVoidable = false;
			}
		}

		if (isVoidable) {
			voidable.setStatusValue(YES);
		}
		else {
			voidable.setStatusValue(NO);
		}
		dtiTkt.addTicketStatus(voidable);

		// set status for eligible and entitlement
		if (queryReq.isIncludeElectronicAttributes()) {
			TicketTO.TktStatusTO eligible = dtiTkt.new TktStatusTO();
			eligible.setStatusItem(ELECTRONICELIGIBLE);
			eligible.setStatusValue(YES);
			TicketTO.TktStatusTO electronic = dtiTkt.new TktStatusTO();
			electronic.setStatusItem(ELECTRONICENTITLEMENT);
			electronic.setStatusValue(NO);
			if (otTktInfo.getDenyMultiEntitlementFunctions() != null) {
				if (otTktInfo.getDenyMultiEntitlementFunctions()) {
					eligible.setStatusValue(NO);
				}
			}
			if (otTktInfo.getAccountId() != null) {
				if (otTktInfo.getAccountId().length() > 0) {
					electronic.setStatusValue(YES);
				}
			}
			dtiTkt.addTicketStatus(eligible);
			dtiTkt.addTicketStatus(electronic);
		}

		// 2.16.1 BIEST001
		// if the IncludeEntitlementAccount flag = true on the request, populate the AccountId tag in the response.
		// **This flag is independent of the "IncludeElectronicAttributes".**
		if (queryReq.isIncludeEntitlementAccount()) {

			if (otTktInfo.getAccountId() != null) {
				if (otTktInfo.getAccountId().length() > 0) {
					dtiTkt.setAccountId(otTktInfo.getAccountId());
				}
			}
		}

		// Is this ticket an annual pass?
		boolean isAPass = false;
		DBTicketAttributes dbTktAttr = null;
		if ((otTktInfo.getTicketType() != null) && (otTktInfo.getTicketType()
				.intValue() > 0)) {
			dbTktAttr = AttributeKey.getWDWTicketAttributes(otTktInfo
					.getTicketType());

			if (dbTktAttr != null) {
				if ((dbTktAttr.getTktCode() != null) && (dbTktAttr
						.getPassClass() != null)) {
					isAPass = true;
				}
			}
		}

		// If this is a pass and validity dates were supplied, is the pass still
		// active?
		// Note, these rules must run in order to effective. DO NOT REORDER.
		if ((isAPass) && (otTktInfo.getValidityStartDate() != null) && (otTktInfo
				.getValidityEndDate() != null)) {

			TicketTO.TktStatusTO active = dtiTkt.new TktStatusTO();
			active.setStatusItem(ACTIVE);
			boolean isActive = false;

			GregorianCalendar gCalStart = otTktInfo.getValidityStartDate();
			Date startDate = gCalStart.getTime();

			// End date has to be moved to true end of day on the end date
			GregorianCalendar gCalEnd = otTktInfo.getValidityEndDate();
			gCalEnd.set(Calendar.HOUR_OF_DAY, 23);
			gCalEnd.set(Calendar.MINUTE, 59);
			gCalEnd.set(Calendar.SECOND, 59);
			Date endDate = gCalEnd.getTime();

			Date todaysDate = new Date();

			if ((todaysDate.getTime() >= startDate.getTime()) && (todaysDate
					.getTime() <= endDate.getTime())) {
				isActive = true;
			}

			if ((otTktInfo.getVoidCode().intValue() > 0) && (otTktInfo
					.getVoidCode().intValue() <= 100)) {
				isActive = false;
			}
			if (isActive) {
				active.setStatusValue(YES);
			}
			else {
				active.setStatusValue(NO);
			}
			dtiTkt.addTicketStatus(active);
		}

		// If this is a pass, and demographics are requested (either "RENEWAL" or "demographics") and the
		// seasons pass demo isn't null...
		if ((isAPass) && ((queryReq.isIncludeRenewalAttributes()) || (queryReq
				.isIncludeTktDemographics())) && (otTktInfo.getSeasonPassDemo() != null)) {

			DemographicsTO dtiDemoTO = new DemographicsTO();

			ArrayList<OTFieldTO> otFieldList = otTktInfo.getSeasonPassDemo()
					.getDemoDataList();

			for (/* each */OTFieldTO anOTField : /* in */otFieldList) {

				// FirstName
				// LastName
				if (anOTField.getFieldIndex().equals(
						OTFieldTO.TKT_DEMO_LASTFIRST)) {

					String lastFirstString = anOTField.getFieldValue();
					if (lastFirstString.contains("/")) {
						String lastNameString = lastFirstString.substring(0,
								lastFirstString.indexOf("/"));
						String firstNameString = lastFirstString
								.substring(lastFirstString.indexOf("/") + 1);

						if ((lastNameString != null) && (lastNameString
								.length() != 0)) {
							dtiDemoTO.setLastName(lastNameString);
						}

						if ((firstNameString != null) && (lastNameString
								.length() != 0)) {
							dtiDemoTO.setFirstName(firstNameString);
						}
					}
				}

				// Addr1
				if (anOTField.getFieldIndex().equals(
						OTFieldTO.TKT_DEMO_ADDRESS_ONE)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setAddr1(anOTField.getFieldValue());
					}
				}

				// Addr2
				if (anOTField.getFieldIndex().equals(
						OTFieldTO.TKT_DEMO_ADDRESS_TWO)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setAddr2(anOTField.getFieldValue());
					}
				}

				// City
				if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_CITY)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setCity(anOTField.getFieldValue());
					}
				}

				// State
				if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_STATE)) {
					if (anOTField.getFieldValue() != null) {
						// as of 2.16.1, JTL - trunc'd per WDPRO request
						String stateString = anOTField.getFieldValue();
						dtiDemoTO.setState(stateString.substring(0, 2));
					}
				}

				// ZIP
				if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_ZIP)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setZip(anOTField.getFieldValue());
					}
				}

				// Country
				if (anOTField.getFieldIndex()
						.equals(OTFieldTO.TKT_DEMO_COUNTRY)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setCountry(anOTField.getFieldValue());
					}
				}

				// Telephone
				if (anOTField.getFieldIndex().equals(
						OTFieldTO.TKT_DEMO_TELEPHONE)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setTelephone(anOTField.getFieldValue());
					}
				}

				// Email
				if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_EMAIL)) {
					if (anOTField.getFieldValue() != null) {
						dtiDemoTO.setEmail(anOTField.getFieldValue());
					}
				}

				// (Gender) - Not to be included in the response unless "ALL" was specified
				if (anOTField.getFieldIndex().equals(OTFieldTO.TKT_DEMO_GENDER)) {
					if ((anOTField.getFieldValue() != null) && (queryReq.isIncludeRenewalAttributes())) {
						dtiDemoTO.setGender(anOTField.getFieldValue());
					}
				}

				// (DOB) - Not to be included in the response unless "ALL" was specified
				if (anOTField.getFieldIndex().equals(
						OTFieldTO.TKT_DEMO_DATE_OF_BIRTH)) {
					if ((anOTField.getFieldValue() != null) && (queryReq
							.isIncludeRenewalAttributes())) {

						String dobString = anOTField.getFieldValue();
						try {
							dtiDemoTO.setDateOfBirth(OTCommandTO
									.convertTicketDOB(dobString));
						}
						catch (ParseException pe) {
							throw new DTIException(
									WDWQueryTicketRules.class,
									DTIErrorCode.TP_INTERFACE_FAILURE,
									"Ticket provider returned XML with an invalid ticket demographic date of birth: " + pe
											.toString());
						}
					}
				}

			}

			dtiTkt.addTicketDemographic(dtiDemoTO);
		}

		// Validity.StartDate
		if (otTktInfo.getValidityStartDate() != null) dtiTkt
				.setTktValidityValidStart(otTktInfo.getValidityStartDate());

		// Validity.EndDate
		if (otTktInfo.getValidityEndDate() != null) dtiTkt
				.setTktValidityValidEnd(otTktInfo.getValidityEndDate());

		if (dbTktAttr != null) {

			// TktAttribute - AgeGroup
			if (dbTktAttr.getAgeGroup() != null) dtiTkt.setAgeGroup(dbTktAttr
					.getAgeGroup());

			// TktAttribute - MediaType
			if (dbTktAttr.getMediaType() != null) dtiTkt.setMediaType(dbTktAttr
					.getMediaType());

			// TktAttribute - PassType
			if (dbTktAttr.getPassType() != null) dtiTkt.setPassType(dbTktAttr
					.getPassType());

			// Create Rule Set for renew if DEMO "ALL" is set (as of 2.16.1, JTL)
			if (queryReq.isIncludeRenewalAttributes()) {

				String renewalStatus = PASSRENEW_INELIGIBLE;

				// RULE 1: Void code must be zero.
				if (otTktInfo.getVoidCode().intValue() == 0) {

					// RULE 2: Renewal window (As comparison is exclusive, pad one day each way).
					GregorianCalendar startOfWindowGC = (GregorianCalendar) otTktInfo
							.getValidityEndDate().clone();
					startOfWindowGC.add(Calendar.DAY_OF_YEAR,
							((PRERENEWAL_WINDOW + 1) * -1));
					GregorianCalendar endOfWindowGC = (GregorianCalendar) otTktInfo
							.getValidityEndDate().clone();
					endOfWindowGC.add(Calendar.DAY_OF_YEAR,
							(POSTRENEWAL_WINDOW + 1));

					Date startOfWindow = startOfWindowGC.getTime();
					Date endOfWindow = endOfWindowGC.getTime();

					if (DateTimeRules.isNowWithinDate(startOfWindow,
							endOfWindow)) {

						// Rule 3: Determine type of renewal
						if (dbTktAttr.getPassRenew() == null) {
							renewalStatus = PASSRENEW_STANDARD;
						}
						else if (dbTktAttr.getPassRenew().equalsIgnoreCase(
								RENEWAL_CODETYPE)) {
							renewalStatus = PASSRENEW_STANDARD;
						}
						else if (dbTktAttr.getPassRenew().equalsIgnoreCase(
								CHARTER_CODETYPE)) {
							renewalStatus = PASSRENEW_CHARTER;
						}
						else {
							renewalStatus = PASSRENEW_STANDARD;
						}

					}

				}

				dtiTkt.setPassRenew(renewalStatus);

			}
			else {
				// Perform old functionality

				// TktAttribute - PassRenew
				if (dbTktAttr.getPassRenew() != null) dtiTkt
						.setPassRenew(dbTktAttr.getPassRenew());

			}

			// TktAttribute - PassClass
			if (dbTktAttr.getPassClass() != null) dtiTkt.setPassClass(dbTktAttr
					.getPassClass());

			// TktAttribute - Resident
			if (dbTktAttr.getResident() != null) dtiTkt.setResident(dbTktAttr
					.getResident());

		}

		dtiQryTktResp.addTicket(dtiTkt);

		return;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
	 * Currently implements the following rules: <BR>
	 * RULE: Validate that ticket presented is of valid format.
	 * 
	 * @param dtiTxn
	 *            the transaction object for this request
	 * @throws DTIException
	 *             for any rules error.
	 */
	public static void applyWDWQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

		QueryTicketRequestTO reqTO = (QueryTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();

		// Validate that ticket presented is of valid format.
		WDWBusinessRules.validateInBoundWDWTickets(aTktList);

		return;
	}
>>>>>>> develop

}
