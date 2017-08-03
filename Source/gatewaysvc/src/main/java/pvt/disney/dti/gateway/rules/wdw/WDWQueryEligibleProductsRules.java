package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import org.apache.commons.lang.StringUtils;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.dao.data.GuestProductTO;
import pvt.disney.dti.gateway.dao.data.UpgradeCatalogTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.ResultStatusTo.ResultType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for three major functions for WDW Query Eligible
 * Product :<BR>
 * 1. Defining the business rules specific to WDW query Eligible Products.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * 
 */
public class WDWQueryEligibleProductsRules {

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(WDWQueryEligibleProductsRules.class.getCanonicalName());

	/** Object instance used for logging. */
	private static final WDWQueryEligibleProductsRules THISINSTANCE = new WDWQueryEligibleProductsRules();

	/** Request type header constant. */
	private final static String REQUEST_TYPE_QUERY = "Query";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_QUERYTICKET = "QueryTicket";

	/** Constant indicating all tags should be created. */
	private final static String ALL_TAGS = "All";

	/** Constant indicating the Manage Reservation XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "dtigatewayrequest.xsd";

	/** Constant text for ITEM ONE (1). */
	private final static String ITEMONE = "1";

	/** The Constant WDW_TPS_CODE. */
	private final static String WDW_TPS_CODE = "WDW-ATS";

	/** The paylan. */
	private static final String PAYLAN = "PAYPLAN";

	/** The paylan. */
	private static final String BIOMETRIC = "BIOMET";

	/** YES. */
	private static final String YES = "YES";

	/** NO. */
	private static final String NO = "NO";

	/** The Constant NUMBER_FOR_DAYCOUNT. */
	private static final int NUMBER_FOR_DAYCOUNT = 24 * 60 * 60 * 1000;

	/**
	 * Transform the DTITransactionTO value object to the provider value objects
	 * and then pass those to XML Marshaling routines to create an XML string.
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
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiCmdBody;

		/* Command Level */
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.QUERYTICKET);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		/* Header Level */
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERYTICKET);
		atsCommand.setHeaderTO(hdr);

		/* Query Ticket Level */
		OTQueryTicketTO otQryTkt = new OTQueryTicketTO();

		/* Tags */
		ArrayList<String> tagList = otQryTkt.getTagsList();

		/* Adding ALL_Tag in case of Query Eligible */
		tagList.add(ALL_TAGS);

		/* SiteNumber */
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otQryTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		} else {
			otQryTkt.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}
		/* Ticket Information */
		ArrayList<TicketTO> dtiTicketList = dtiReq.getTktList();

		TicketTO dtiTicket = dtiTicketList.get(0);

		/* putting ticket into ticket info list */
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

		/* Set the Query Ticket TO on the command */
		atsCommand.setQueryTicketTO(otQryTkt);

		/* Get the XML String */
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;
	}

	/**
	 * Transforms a QueryEligibleResponse response string from the WDW provider
	 * and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param otCmdTO
	 *            the ot cmd TO
	 * @param dtiRespTO
	 *            the dti resp TO
	 * @return The DTITransactionTO object, enriched with the response
	 *         information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error
	 *             response to the seller.
	 */
	@SuppressWarnings("unused")
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {
		/*
		 * TODO space this out, need to change format/style, Todd 06-23-2017 JTL
		 * will share the formatter with everyone
		 */

		/* Adding QueryEligibleProductsRequestTO to look if this can be used */
		QueryEligibleProductsRequestTO dtiReq = (QueryEligibleProductsRequestTO) dtiTxn
				.getRequest().getCommandBody();

		QueryEligibilityProductsResponseTO dtiResRespTO = new QueryEligibilityProductsResponseTO();

		OTQueryTicketTO otQryTicketTO = otCmdTO.getQueryTicketTO();

		dtiRespTO.setCommandBody(dtiResRespTO);

		/* global Upgrade Catalog product */
		UpgradeCatalogTO globalUpgradeProduct = null;

		/* new and final List of Upgrade able Product after filter */
		ArrayList<DBProductTO> newProductcatalogList = null;

		/* ResponseType */
		ArrayList<TicketTO> ticketListTo = dtiResRespTO.getTicketList();

		/* Query Ticket InfoInlist */
		ArrayList<OTTicketInfoTO> otTicketList = otQryTicketTO.getTicketInfoList();

		/* Iterate for each of the ticketInfo */
		if ((otTicketList != null) && (otTicketList.size() > 0)) {

			for (OTTicketInfoTO otTicketInfo : otTicketList) {

				TicketTO dtiTicketTO = new TicketTO();

				ArrayList<BigInteger> tktNbr = new ArrayList<BigInteger>();

				if (otTicketInfo.getTicketType() != null) {
					tktNbr.add(otTicketInfo.getTicketType());
				} else {
					logger.sendEvent("Ticket type not found ",EventType.DEBUG,THISINSTANCE);
					throw new DTIException(WDWQueryEligibleProductsRules.class,DTIErrorCode.TICKET_INVALID,"Ticket Provided is incorrect");
				}
				/*
				 * Call this method get the list of DB products for the guest
				 * Step 1
				 */
				GuestProductTO guestproductTO = setGuestProductDetails(otTicketInfo, dtiTicketTO, tktNbr);

				/*
				 * if no guest Type information is found then ticket is
				 * INELIGIBLE and transaction stops
				 */
				if (guestproductTO.getDbproductTO() == null) {
					
					logger.sendEvent("Guest Type information not found ",EventType.DEBUG,THISINSTANCE);
					dtiTicketTO.setResultType(ResultType.INELIGIBLE);
					dtiResRespTO.add(dtiTicketTO);
					return;
				}

				/*
				 * Step 2 :: Putting a check to avoid multiple calls in case of
				 * multiple tickets
				 */
				if (globalUpgradeProduct == null) {

					globalUpgradeProduct = ProductKey.getAPUpgradeCatalog(dtiTxn.getEntityTO(), WDW_TPS_CODE);
				}

				/*
				 * Step 3 Pass the DbProductTO to next level of validation
				 */

				if (validateInEligibleProducts(otTicketInfo,
						guestproductTO.getDbproductTO())) {
					
					logger.sendEvent("DbProductTO next level validation fail. ",EventType.DEBUG,THISINSTANCE);
					dtiTicketTO.setResultType(ResultType.INELIGIBLE);
				}

				/* Step 4:: get the Upgraded List */
				if (globalUpgradeProduct != null) {

					newProductcatalogList = globalUpgradeProduct.getProductList();
					logger.sendEvent("product found in new product list is."+newProductcatalogList,EventType.DEBUG,THISINSTANCE);
					/* if no product is found return No Product */
					if (newProductcatalogList == null) {
						logger.sendEvent("No product found in new product list .",EventType.DEBUG,THISINSTANCE);
						dtiTicketTO.setResultType(ResultType.INELIGIBLE);
					}
				} else {
					logger.sendEvent("No product found in upgraded product .",EventType.DEBUG,THISINSTANCE);
					dtiTicketTO.setResultType(ResultType.NOPRODUCTS);

				}

				/* Step 5 setting up the detail in Ticket TO */
				setQueryEligibleResponseCommand(guestproductTO, dtiTicketTO,globalUpgradeProduct.getProductList());

				dtiResRespTO.add(dtiTicketTO);
			}
		}

	}

	/**
	 * Sets the guest product details.
	 * 
	 * @param otTicketInfo
	 *            the ot ticket info
	 * @param dtiTktTO
	 *            the TicketTO
	 * @param tktNbr
	 *            the ticket Number
	 * @return the guest product TO
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static GuestProductTO setGuestProductDetails(
			OTTicketInfoTO otTicketInfo, TicketTO dtiTktTO,
			ArrayList<BigInteger> tktNbr) throws DTIException {

		/* This method is used for processing step 1 */
		GuestProductTO guestProductTO = new GuestProductTO();

		ArrayList<DBProductTO> dbProductTO = null;

		/* Retrieving the product details for GuestProduct */
		dbProductTO = ProductKey.getProductsfromTktNbr(tktNbr);

		if ((dbProductTO != null) && (dbProductTO.size() > 0)) {

			logger.sendEvent("GuestProduct found as ."+dbProductTO.toString(), EventType.DEBUG,THISINSTANCE);

			dtiTktTO.setResultType(ResultType.ELIGIBLE);

			guestProductTO.setDbproductTO(dbProductTO.get(0));

			/* Setting up GWDataRequestRespTO in case of DLR */
			guestProductTO.setOtTicketInfo(otTicketInfo);

		} else {
			logger.sendEvent("Not able to find any Ticket Information in DTI.",EventType.DEBUG, THISINSTANCE);
			dtiTktTO.setResultType(ResultType.INELIGIBLE);

		}
		/* Return the GuestProductTO */
		return guestProductTO;
	}

	/**
	 * Validate in eligible products.
	 * 
	 * @param otTicketInfo
	 *            the ot ticket info
	 * @param productTO
	 *            the product TO
	 * @return true, if successful
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static boolean validateInEligibleProducts(
			OTTicketInfoTO otTicketInfo, DBProductTO productTO)
			throws DTIException {

		boolean isInEligibleProductFlag = false;

		GregorianCalendar date = null;

		ArrayList<OTUsagesTO> usagesTOs = null;

		String biometricTemplet = null;

		GregorianCalendar firstUseDate = null;
		/* Getting Ticket details */

		/* Void Code */
		Integer voidCode = otTicketInfo.getVoidCode();

		/* End Date */
		GregorianCalendar endDate = otTicketInfo.getValidityEndDate();

		/* Usages */
		usagesTOs = otTicketInfo.getUsagesList();

		/* Bio metric Template */
		biometricTemplet = otTicketInfo.getBiometricTemplate();

		/* Getting the usages details */

		List<GregorianCalendar> useDates = new ArrayList<GregorianCalendar>();

		if ((usagesTOs != null) && (usagesTOs.size() > 0)) {

			for (OTUsagesTO otUsagesTO : usagesTOs) {
				// find first use date by pulling in Item Number 1 (first use)
				// assumption is that Items are sorted by ATS based on their use date
				if ((otUsagesTO.getDate() != null) && otUsagesTO.getItem() == 1) {
					firstUseDate = otUsagesTO.getDate();
				}
				
				
				useDates.add(date);
			}
		}
		/*
		 * VoidCode in the ATS Query Ticket response greater than 0 or less than
		 * or equal to 100
		 */
		if ((voidCode != null) && ((voidCode > 0) && (voidCode <= 100))) {
			logger.sendEvent("VoidCode validation fails.",EventType.DEBUG, THISINSTANCE);
			return isInEligibleProductFlag = true;
		}
		/* Usages must have one entry and BiometricTemplate must have one entry */
		if (((usagesTOs != null) && ((usagesTOs.size() == 0)) || (biometricTemplet == null))) {
			logger.sendEvent("usagesTOs or biometricTemplet validation fails.",EventType.DEBUG, THISINSTANCE);
			return isInEligibleProductFlag = true;
		}

		if (productTO != null) {
			try {
				/* UpgrdPathId is 0 */
				if ((productTO.getUpgrdPathId() != null)
						&& (productTO.getUpgrdPathId().intValue() == 0)) {
					logger.sendEvent("UpgrdPathId validation fails.",EventType.DEBUG, THISINSTANCE);
					isInEligibleProductFlag = true;
				}
				/* ResidentInd is N and Validity EndDate less than today */
				if ((endDate != null) && (productTO.isResidentInd() == false)
						&& (getDayDifference(endDate) > 0)) {
					logger.sendEvent("ResidentInd and Validity EndDate validation fails.",EventType.DEBUG, THISINSTANCE);
					isInEligibleProductFlag = true;
				}
				/*
				 * ResidentInd is Y and DayCount = 1 and the first use date is
				 * older than 14 days
				 */
				if ((firstUseDate != null)
						&& (productTO.isResidentInd() == true)
						&& (productTO.getDayCount() == 1)
						&& (getDayDifference(firstUseDate) > 14)) {
					logger.sendEvent("ResidentInd and first use date validation fails.",EventType.DEBUG, THISINSTANCE);
					isInEligibleProductFlag = true;
				}
				/*
				 * resident flag is 'Y' and DayCount > 1, and the first use date
				 * is older than six months (185 days).
				 */
				if ((firstUseDate != null)
						&& (productTO.isResidentInd() == true)
						&& (productTO.getDayCount() > 1)
						&& (getDayDifference(firstUseDate) > 185)) {
					isInEligibleProductFlag = true;
					logger.sendEvent("ResidentInd ,first use date and DayCount validation fails.",EventType.DEBUG, THISINSTANCE);
				}
			} catch (Exception e) {
				logger.sendEvent("Exception executing validateInEligibleProducts: "+ e.toString(), EventType.EXCEPTION,THISINSTANCE);
				throw new DTIException(WDWQueryEligibleProductsRules.class,DTIErrorCode.DTI_DATA_ERROR,"Exception executing validateInEligibleProducts");
			}
		}
		logger.sendEvent("validateInEligibleProducts method end returing:"+isInEligibleProductFlag,EventType.DEBUG, THISINSTANCE);
		return isInEligibleProductFlag;

	}

	/**
	 * To get the day difference for the GregorianCalendar argument.
	 * 
	 * @param calendar
	 *            the calendar
	 * @return the day difference
	 */
	private static long getDayDifference(GregorianCalendar calendar) {

		long difference = (new Date().getTime() - calendar.getTime().getTime())
				/ NUMBER_FOR_DAYCOUNT;
		return difference;
	}

	/**
	 * Sets the query eligible response command.
	 * 
	 * @param guestProductTO
	 *            the guest product TO
	 * @param dtiTicketTO
	 *            the dti ticket TO
	 * @param upgradedProductTOList
	 *            the upgraded product TO list
	 * @throws DTIException
	 *             the DTI exception
	 */
	private static void setQueryEligibleResponseCommand(
			GuestProductTO guestProductTO, TicketTO dtiTicketTO,
			ArrayList<DBProductTO> upgradedProductTOList) throws DTIException {

		OTTicketInfoTO otTicketInfo = guestProductTO.getOtTicketInfo();

		/* dbProductTO from GuestProductTO */
		DBProductTO dbProductTO = guestProductTO.getDbproductTO();

		OTTicketTO otTicketTO = otTicketInfo.getTicket();

		/* Tkt Item */
		dtiTicketTO.setTktItem(otTicketInfo.getItem());

		/* Prod Code */
		dtiTicketTO.setProdCode(dbProductTO.getPdtCode());

		/* ProdGuestType */
		dtiTicketTO.setGuestType(dbProductTO.getGuestType());

		/* Ticket Demo */
		DemographicsTO dtiDemoTO = new DemographicsTO();
		ArrayList<OTFieldTO> otFieldList = new ArrayList<OTFieldTO>();
		if ((otTicketInfo != null)
				&& (otTicketInfo.getSeasonPassDemo() != null)) {
			otFieldList = otTicketInfo.getSeasonPassDemo().getDemoDataList();
		}
		logger.sendEvent("demoDataList found :"+otFieldList,EventType.DEBUG, THISINSTANCE);
		for (/* each */OTFieldTO anOTField : /* in */otFieldList) {

			/* FirstName */
			/* LastName */
			if (anOTField.getFieldIndex().equals(
					OTFieldTO.WDW_TKTDEMO_LASTFIRST)) {

				String lastFirstString = anOTField.getFieldValue();
				if (lastFirstString.contains("/")) {
					String lastNameString = lastFirstString.substring(0,
							lastFirstString.indexOf("/"));
					String firstNameString = lastFirstString
							.substring(lastFirstString.indexOf("/") + 1);

					if ((lastNameString != null)
							&& (lastNameString.length() != 0)) {
						dtiDemoTO.setLastName(lastNameString);
					}

					if ((firstNameString != null)
							&& (lastNameString.length() != 0)) {
						dtiDemoTO.setFirstName(firstNameString);
					}
				}
			}

			/* Addr1 */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ADDRESS_ONE)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setAddr1(anOTField.getFieldValue());
				}
			}

			/* Addr2 */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ADDRESS_TWO)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setAddr2(anOTField.getFieldValue());
				}
			}

			/* City */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_CITY)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setCity(anOTField.getFieldValue());
				}
			}

			/* State */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_STATE)) {
				if (anOTField.getFieldValue() != null) {
					String stateString = anOTField.getFieldValue();
					dtiDemoTO.setState(stateString.substring(0, 2));
				}
			}

			/* ZIP */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_ZIP)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setZip(anOTField.getFieldValue());
				}
			}

			/* Country */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_COUNTRY)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setCountry(anOTField.getFieldValue());
				}
			}

			/* Telephone */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_PHONE)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setTelephone(anOTField.getFieldValue());
				}
			}

			/* Email */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_EMAIL)) {
				if (anOTField.getFieldValue() != null) {
					dtiDemoTO.setEmail(anOTField.getFieldValue());
				}
			}

			/*
			 * (Gender) - Not to be included in the response unless "ALL" was
			 * specified
			 */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_GENDER)) {
				if ((anOTField.getFieldValue() != null)) {
					dtiDemoTO.setGender(anOTField.getFieldValue());
				}
			}

			/* (DOB) */
			if (anOTField.getFieldIndex().equals(OTFieldTO.WDW_TKTDEMO_DTE_OF_BIRTH)) {
				if ((anOTField.getFieldValue() != null)) {

					String dobString = anOTField.getFieldValue();
					try {
						dtiDemoTO.setDateOfBirth(OTCommandTO.convertTicketDOB(dobString));
					} catch (ParseException pe) {
						logger.sendEvent("Date formate is not of proper type ",EventType.EXCEPTION,THISINSTANCE);
						throw new DTIException(
								WDWQueryEligibleProductsRules.class,
								DTIErrorCode.DTI_DATA_ERROR,
								"Ticket provider returned XML with an invalid ticket demographic date of birth: "
										+ pe.toString());
					}
				}
			}

		}

		dtiTicketTO.addTicketDemographic(dtiDemoTO);

		/* Ticket Validity */
		if (otTicketInfo.getValidityStartDate() != null) {
			dtiTicketTO.setTktValidityValidStart(otTicketInfo
					.getValidityStartDate());

		}
		if (otTicketInfo.getValidityEndDate() != null) {
			dtiTicketTO.setTktValidityValidEnd(otTicketInfo
					.getValidityEndDate());
		}

		/* Prod Price */
		dtiTicketTO.setTktPrice(dbProductTO.getStandardRetailPrice());

		dtiTicketTO.setTktTax(dbProductTO.getStandardRetailTax());

		/* Tkt Status (BIOMETRIC and PAYPLAN) */
		ArrayList<TktStatusTO> tktStatusList = dtiTicketTO.getTktStatusList();

		TktStatusTO tktStatus = dtiTicketTO.new TktStatusTO();

		/* BIOMETRIC */
		tktStatus.setStatusItem(BIOMETRIC);

		if ((otTicketInfo.getBiometricTemplate() != null)
				&& (otTicketInfo.getBiometricTemplate()
						.compareToIgnoreCase(YES) == 0)) {
			tktStatus.setStatusValue(YES);
		} else {
			tktStatus.setStatusValue(NO);
		}
		/* payplan */
		tktStatus.setStatusItem(PAYLAN);
		if ((otTicketInfo.getPayPlan() != null)
				&& (otTicketInfo.getPayPlan().compareToIgnoreCase(YES) == 0)) {
			tktStatus.setStatusValue(YES);
		} else {
			tktStatus.setStatusValue(NO);
		}

		/* Add the tktStatus to ticket */
		tktStatusList.add(tktStatus);

		GregorianCalendar dssnDate = otTicketTO.getTdssnDate();

		String site = otTicketTO.getTdssnSite();

		String station = otTicketTO.getTdssnStation();

		String number = otTicketTO.getTdssnTicketId();

		if ((StringUtils.isNotBlank(site)) && (StringUtils.isNotBlank(station))
				&& (StringUtils.isNotBlank(number))) {
			dtiTicketTO.setDssn(dssnDate, site, station, number);
		}
		if (StringUtils.isNotBlank(otTicketTO.getMagTrack())) {
			dtiTicketTO.setMag(otTicketTO.getMagTrack());
		}
		if (StringUtils.isNotBlank(otTicketTO.getBarCode())) {
			dtiTicketTO.setBarCode(otTicketTO.getBarCode());
		}
		if (StringUtils.isNotBlank(otTicketTO.getTCOD())) {
			dtiTicketTO.setTktNID(otTicketTO.getTCOD());
		}
		if (StringUtils.isNotBlank(otTicketTO.getExternalTicketCode())) {
			dtiTicketTO.setExternal(otTicketTO.getExternalTicketCode());
		}

		dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
		dtiTicketTO.setTktTax(otTicketInfo.getTax());

		logger.sendEvent("upgradedProductTOList found :"+upgradedProductTOList,EventType.DEBUG, THISINSTANCE);
		/* Eligible product */
		if (upgradedProductTOList != null) {
			for (/* each */DBProductTO productTO : /* in */upgradedProductTOList) {
				EligibleProductsTO eligibleProductsTO = new EligibleProductsTO();

				/* set the product code */
				eligibleProductsTO.setProdCode(productTO.getPdtCode());

				/* set the actual product price */
				BigDecimal prodPrice = productTO.getUnitPrice();

				eligibleProductsTO.setProdPrice(prodPrice.toString());

				/* set the actual product tax */
				BigDecimal prodTax = productTO.getTax();

				eligibleProductsTO.setProdTax(prodTax);

				/* set the upgraded product price */
				BigDecimal prodUpgradePrice = prodPrice
						.subtract(guestProductTO.getDbproductTO()
								.getStandardRetailPrice() == null ? new BigDecimal(
								0) : guestProductTO.getDbproductTO()
								.getStandardRetailPrice());
				if (prodUpgradePrice.longValue() < 0) {
					eligibleProductsTO.setUpgrdPrice("0");
				} else {
					eligibleProductsTO.setUpgrdPrice(prodUpgradePrice
							.toString());
				}
				/* set the upgraded product tax */
				BigDecimal prodUpgrdTax = prodTax
						.subtract(guestProductTO.getDbproductTO()
								.getStandardRetailTax() == null ? new BigDecimal(
								0) : guestProductTO.getDbproductTO()
								.getStandardRetailTax());
				if (prodUpgradePrice.longValue() < 0) {
					eligibleProductsTO.setUpgrdTax("0");
				} else {
					eligibleProductsTO.setUpgrdTax(prodUpgrdTax.toString());
				}
				/* set the validity */
				try {
					Integer dayCount = productTO.getDayCount();

					Calendar calendar = Calendar.getInstance();
					if (dbProductTO.getStartSaleDate() != null) {
						calendar.setTime(dbProductTO.getStartSaleDate()
								.getTime());
						calendar.add(Calendar.DAY_OF_MONTH, dayCount);

						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(calendar.getTime());

						eligibleProductsTO.setValidEnd(DatatypeFactory
								.newInstance().newXMLGregorianCalendar(gc));
					}
				} catch (Exception e) {
					throw new DTIException(WDWQueryEligibleProductsRules.class,
							DTIErrorCode.DTI_DATA_ERROR,
							"Provider responded with a non-numeric status code.");
				}
				/* add the eligible product element to the list */
				dtiTicketTO.addEligibleProducts(eligibleProductsTO);
			}
		}

	}

}
