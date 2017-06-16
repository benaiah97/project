package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTo;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTo;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.VoidReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryEligibleProductsTo;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

import com.disney.logging.EventLogger;

/**
 * This class is responsible for three major functions for WDW query reservation:<BR>
 * 1. Defining the business rules specific to WDW query Eligible Products.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author JTL
 * @since 2.16.3
 * 
 */
public class WDWQueryEligibleProductsRules {

	  /** Request type header constant. */
	 private final static String REQUEST_TYPE_QUERY = "Query";

	  /** Request sub-type header constant. */
	 private final static String REQUEST_SUBTYPE_QUERY_ELIGBLE_PRODUCTS = "QueryEligibleProducts";

	  /** Constant indicating all tags should be created. */
	  private final static String ALL_TAGS = "All";

	  /** Constant indicating the Manage Reservation XSD. */
	  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "dtigatewayrequest.xsd";

	  /**
	   * Constant indicating the Void variant of the Manage Reservation command.
	   */
	  private final static String COMMAND_TYPE = "Void";
	  
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
	  
	  /** Constant text for ITEM ONE (1). */
	  private final static String ITEMONE = "1";

	  @SuppressWarnings("unused")
	  private static final EventLogger logger = EventLogger
	      .getLogger(WDWQueryEligibleProductsRules.class.getCanonicalName());

	  /**
	   * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshaling routines to create an XML string.
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
	    QueryEligibleProductsRequestTo dtiReq = (QueryEligibleProductsRequestTo) dtiCmdBody;

	    // === Command Level ===
	    OTCommandTO atsCommand = new OTCommandTO(
	        OTCommandTO.OTTransactionType.ELIGIBLEPRODUCTS);
	    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
	    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

	    // === Header Level ===
	    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
	    		REQUEST_TYPE_QUERY, REQUEST_SUBTYPE_QUERY_ELIGBLE_PRODUCTS);
	    atsCommand.setHeaderTO(hdr);

	    // === Manage Reservation Level ===
	    OTQueryEligibleProductsTo otQryEligPrd = new OTQueryEligibleProductsTo();

	    // Tags
	    ArrayList<String> tagList = otQryEligPrd.getTagsList();
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
	      otQryEligPrd.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
	    }
	    else {
	      otQryEligPrd.setSiteNumber(Integer.parseInt(anAttributeTO
	          .getAttrValue()));
	    }
	    // SalesType
	    otQryEligPrd.setSaletype(dtiReq.getSaleType());

	    // Ticket Information
	    TicketTO dtiTicket = dtiReq.getTicketTO();
	    //TicketTO dtiTicket = dtiTicketList.get(0);
	    ArrayList<OTTicketInfoTO> otTicketList = otQryEligPrd.getTicketInfoList();
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

	    // Set the Query Eligible Products TO on the command
	    atsCommand.setQueryEligbleProductsTO(otQryEligPrd);

	    // Get the XML String
	    xmlString = OTCommandXML.getXML(atsCommand);

	    return xmlString;
	  }

	  /**
	   * Transforms a QueryEligibleResponse response string from the WDW provider and updates the DTITransactionTO object with the response information.
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

	    QueryEligibilityProductsResponseTo dtiResRespTO = new QueryEligibilityProductsResponseTo();
	    OTQueryEligibleProductsTo otMngResTO = otCmdTO.getQueryEligbleProductsTO();
	    dtiRespTO.setCommandBody(dtiResRespTO);

	    // Price mismatch warning (invalid for void res - omitted)    

	    // ResponseType
	   

	    TicketTO ticketTo=dtiResRespTO.getTicketTO();
	    ArrayList<OTTicketInfoTO> otTicketList = otMngResTO.getTicketInfoList();
	    if ((otTicketList != null) && (otTicketList.size() > 0)) {

	      for (OTTicketInfoTO otTicketInfo : otTicketList) {

	        TicketTO dtiTicketTO = new TicketTO();
	        TicketTO.TktStatusTO newStatus = dtiTicketTO.new TktStatusTO();
	        newStatus.setStatusItem(otTicketInfo.getItem().toString());
	        newStatus.setStatusValue(otTicketInfo.getItemStatus().toString());
	        OTTicketTO otTicketTO = otTicketInfo.getTicket();

	        dtiTicketTO.setTktItem(otTicketInfo.getItem());
	        dtiTicketTO.setProdCode(otTicketInfo.getItem().toString());
	        dtiTicketTO.setProdPrice(otTicketInfo.getPrice());
	        
	        dtiTicketTO.addTicketStatus(newStatus);
	        GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
	        String site = otTicketTO.getTdssnSite();
	        String station = otTicketTO.getTdssnStation();
	        String number = otTicketTO.getTdssnTicketId();
	        if(site!=null&&site!=""&&station!=null&&station!=""&&number!=null&&number!=""){
	        	dtiTicketTO.setDssn(dssnDate, site, station, number);	
	        }
	        
	        if(otTicketTO.getMagTrack()!=null&&otTicketTO.getMagTrack()!=""){
	        	dtiTicketTO.setMag(otTicketTO.getMagTrack());	
	        }
	        if(otTicketTO.getBarCode()!=null&&otTicketTO.getBarCode()!=""){
	        	dtiTicketTO.setBarCode(otTicketTO.getBarCode());
	        }
	        if(otTicketTO.getTCOD()!=null&&otTicketTO.getTCOD()!=""){
	        	dtiTicketTO.setTktNID(otTicketTO.getTCOD());	
	        }
	        if(otTicketTO.getExternalTicketCode()!=null&&otTicketTO.getExternalTicketCode()!=""){
	        	dtiTicketTO.setExternal(otTicketTO.getExternalTicketCode());	
	        }
	        
	        
	        dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
	        dtiTicketTO.setTktTax(otTicketInfo.getTax());

	        if (otTicketInfo.getValidityStartDate() != null) dtiTicketTO
	            .setTktValidityValidStart(otTicketInfo
	                .getValidityStartDate());

	        if (otTicketInfo.getValidityEndDate() != null) dtiTicketTO
	            .setTktValidityValidEnd(otTicketInfo
	                .getValidityEndDate());

/*	        // AccountId
	        dtiTicketTO.setAccountId(otTicketInfo.getAccountId());
*/	        dtiResRespTO.setTicketTO(dtiTicketTO);

	      
	      }
	      
	    }
	    return;
	  }

	 
}
