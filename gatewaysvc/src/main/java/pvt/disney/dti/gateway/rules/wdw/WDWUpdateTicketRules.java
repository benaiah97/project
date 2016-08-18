package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.UpdateTicketResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO.CmdAttributeTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.TicketRules;

/**
 * This class is responsible for three major functions for WDW update tickets:<BR>
 * 1. Defining the business rules specific to WDW update tickets.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 */
public class WDWUpdateTicketRules {

<<<<<<< HEAD
  /** Request type header constant. */
  private final static String REQUEST_TYPE_UPDATE_TKT = "Update";

  /** Request subtype header constant. */
  private final static String REQUEST_SUBTYPE_UPDATE_TKT = "UpdateTicket";

  /** Constant indicating all tags should be created. */
  private final static String[] UT_TAGS = { "UpdateTicketInfo" };

  /** Constant indicating the Update Ticket XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "UpdateTicketRequest.xsd";

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
    CommandHeaderTO dtiCmdHdr = dtiRequest.getCommandHeader();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    UpdateTicketRequestTO dtiUpdtTktReq = (UpdateTicketRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(OTCommandTO.OTTransactionType.UPDATETICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn, REQUEST_TYPE_UPDATE_TKT,
        REQUEST_SUBTYPE_UPDATE_TKT);
    atsCommand.setHeaderTO(hdr);

    // === Update Ticket Level ===
    OTUpdateTicketTO otUpdtTkt = new OTUpdateTicketTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    for (int i = 0; i < UT_TAGS.length; i++) {
      tagList.add(UT_TAGS[i]);
    }
    otUpdtTkt.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();
    AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
    	otUpdtTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    } else {
    	otUpdtTkt.setSiteNumber(Integer.parseInt(anAttributeTO.getAttrValue()));
    }

    // UpdateTicketInfo
    ArrayList<TicketTO> dtiTktList = dtiUpdtTktReq.getTktList();
    ArrayList<OTTicketInfoTO> otTktInfoList = otUpdtTkt.getTktInfoList();
    for /* each */(TicketTO dtiTicket : /* in */dtiTktList) {

      OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

      // Item
      otTicketInfo.setItem(dtiTicket.getTktItem());

      // TicketSearchMode
      ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();
      TicketIdType dtiTicketType = dtiTicketTypeList.get(0);
      OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(dtiTicket, dtiTicketType);
      otTicketInfo.setTicketSearchMode(otTicket);

      // Validity
      if (dtiTicket.getTktValidityValidStart() != null)
        otTicketInfo.setValidityStartDate(dtiTicket.getTktValidityValidStart());
      if (dtiTicket.getTktValidityValidEnd() != null)
        otTicketInfo.setValidityEndDate(dtiTicket.getTktValidityValidEnd());

      // TktNote
      if (dtiTicket.getTktNote() != null)
        otTicketInfo.setTicketNote(dtiTicket.getTktNote());

      // VoidCode
      ArrayList<TicketTO.TktStatusTO> dtiStatusList = dtiTicket.getTktStatusList();
      if (dtiStatusList.size() > 0) {
        TicketTO.TktStatusTO aDTIStatus = dtiStatusList.get(0);
        String statusValue = aDTIStatus.getStatusValue();
        String tpiCode = dtiTxn.getTpiCode();
        Integer voidCode = LookupKey.getVoidLookup(statusValue, tpiCode);
        otTicketInfo.setVoidCode(voidCode);
      }

      // TicketAttribute
      ArrayList<CmdAttributeTO> dtiCmdAttrList = dtiCmdHdr.getCmdAttributeList();
      if ((dtiCmdAttrList != null) && (dtiCmdAttrList.size() > 0)) {
        CmdAttributeTO dtiCmdAttr = dtiCmdAttrList.get(0);
        String dtiAttr = dtiCmdAttr.getAttribValue();
        if (dtiAttr != null) {
          otTicketInfo.setTicketAttribute(Integer.decode(dtiAttr));
        }
      }

      // ZipCode
      if (dtiTicket.getTktMarket() != null) {
        otTicketInfo.setZipCode(dtiTicket.getTktMarket());
      }

      // BiometricLevel
      if ((dtiTicket.getTktSecurityLevel() != null)
          && (dtiTicket.getTktSecurityLevel().compareTo("") != 0)) {
        otTicketInfo.setBiometricLevel(Integer.decode(dtiTicket.getTktSecurityLevel()));
      }

      otTktInfoList.add(otTicketInfo);

    } // End While

    // Set the Query Ticket TO on the command
    atsCommand.setUpdateTicketTO(otUpdtTkt);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
  }

  /**
   * Transforms a update ticket response string from the WDW provider and
   * updates the DTITransactionTO object with the response information. In thise
   * case, there are no fields which need to be transformed, but to ensure
   * consistency and future expandability, an empty update ticket response is
   * attached to the command body.
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
  static void transformResponseBody(DTITransactionTO dtiTxn, OTCommandTO otCmdTO,
      DTIResponseTO dtiRespTO) throws DTIException {

    UpdateTicketResponseTO dtiUpdtTktResp = new UpdateTicketResponseTO();
    dtiRespTO.setCommandBody(dtiUpdtTktResp);

    return;

  }

  /**
   * If a type of transaction has a specific number of provider centric rules,
   * implement them here, but if there are a very limited set of rules, mostly
   * common to both providers, implement in the BusinessRules in the parent
   * package. <BR>
   * 
   * RULE:  Only eight tickets maximum may be on this list.
   * 
   * @param dtiTxn
   *          The transaction object for this request.
   * @throws DTIException
   *           for any rules error.
   */
  public static void applyWDWUpdateTicketRules(DTITransactionTO dtiTxn) throws DTIException {
    
    UpdateTicketRequestTO utReqTO = (UpdateTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    ArrayList<TicketTO> tktListTO = utReqTO.getTktList();
    
    // RULE: Are the number of products/tickets on the order within tolerance?
    TicketRules.validateMaxEightTicketsOnRequest(tktListTO);
    
    return;
  }
=======
	/** Request type header constant. */
	private final static String REQUEST_TYPE_UPDATE_TKT = "Update";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_UPDATE_TKT = "UpdateTicket";

	/** Constant indicating all tags should be created. */
	private final static String[] UT_TAGS = { "UpdateTicketInfo" };

	/** Constant indicating the Update Ticket XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "UpdateTicketRequest.xsd";

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
		CommandHeaderTO dtiCmdHdr = dtiRequest.getCommandHeader();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		UpdateTicketRequestTO dtiUpdtTktReq = (UpdateTicketRequestTO) dtiCmdBody;

		// === Command Level ===
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.UPDATETICKET);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// === Header Level ===
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_UPDATE_TKT, REQUEST_SUBTYPE_UPDATE_TKT);
		atsCommand.setHeaderTO(hdr);

		// === Update Ticket Level ===
		OTUpdateTicketTO otUpdtTkt = new OTUpdateTicketTO();

		// Tags
		ArrayList<String> tagList = new ArrayList<String>();
		for (int i = 0; i < UT_TAGS.length; i++) {
			tagList.add(UT_TAGS[i]);
		}
		otUpdtTkt.setTagsList(tagList);

		// SiteNumber
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otUpdtTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		}
		else {
			otUpdtTkt.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		// UpdateTicketInfo
		ArrayList<TicketTO> dtiTktList = dtiUpdtTktReq.getTktList();
		ArrayList<OTTicketInfoTO> otTktInfoList = otUpdtTkt.getTktInfoList();
		for /* each */(TicketTO dtiTicket : /* in */dtiTktList) {

			OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

			// Item
			otTicketInfo.setItem(dtiTicket.getTktItem());

			// TicketSearchMode
			ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket
					.getTicketTypes();
			TicketIdType dtiTicketType = dtiTicketTypeList.get(0);
			OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(dtiTicket,
					dtiTicketType);
			otTicketInfo.setTicketSearchMode(otTicket);

			// Validity
			if (dtiTicket.getTktValidityValidStart() != null) otTicketInfo
					.setValidityStartDate(dtiTicket.getTktValidityValidStart());
			if (dtiTicket.getTktValidityValidEnd() != null) otTicketInfo
					.setValidityEndDate(dtiTicket.getTktValidityValidEnd());

			// TktNote
			if (dtiTicket.getTktNote() != null) otTicketInfo
					.setTicketNote(dtiTicket.getTktNote());

			// VoidCode
			ArrayList<TicketTO.TktStatusTO> dtiStatusList = dtiTicket
					.getTktStatusList();
			if (dtiStatusList.size() > 0) {
				TicketTO.TktStatusTO aDTIStatus = dtiStatusList.get(0);
				String statusValue = aDTIStatus.getStatusValue();
				String tpiCode = dtiTxn.getTpiCode();
				Integer voidCode = LookupKey
						.getVoidLookup(statusValue, tpiCode);
				otTicketInfo.setVoidCode(voidCode);
			}

			// TicketAttribute
			ArrayList<CmdAttributeTO> dtiCmdAttrList = dtiCmdHdr
					.getCmdAttributeList();
			if ((dtiCmdAttrList != null) && (dtiCmdAttrList.size() > 0)) {
				CmdAttributeTO dtiCmdAttr = dtiCmdAttrList.get(0);
				String dtiAttr = dtiCmdAttr.getAttribValue();
				if (dtiAttr != null) {
					otTicketInfo.setTicketAttribute(Integer.decode(dtiAttr));
				}
			}

			// ZipCode
			if (dtiTicket.getTktMarket() != null) {
				otTicketInfo.setZipCode(dtiTicket.getTktMarket());
			}

			// BiometricLevel
			if ((dtiTicket.getTktSecurityLevel() != null) && (dtiTicket
					.getTktSecurityLevel().compareTo("") != 0)) {
				otTicketInfo.setBiometricLevel(Integer.decode(dtiTicket
						.getTktSecurityLevel()));
			}

			otTktInfoList.add(otTicketInfo);

		} // End While

		// Set the Query Ticket TO on the command
		atsCommand.setUpdateTicketTO(otUpdtTkt);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;
	}

	/**
	 * Transforms a update ticket response string from the WDW provider and updates the DTITransactionTO object with the response information. In thise case, there are no fields which need to be transformed, but to ensure consistency and
	 * future expandability, an empty update ticket response is attached to the command body.
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

		UpdateTicketResponseTO dtiUpdtTktResp = new UpdateTicketResponseTO();
		dtiRespTO.setCommandBody(dtiUpdtTktResp);

		return;

	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package. <BR>
	 * 
	 * RULE: Only eight tickets maximum may be on this list.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @throws DTIException
	 *             for any rules error.
	 */
	public static void applyWDWUpdateTicketRules(DTITransactionTO dtiTxn) throws DTIException {

		UpdateTicketRequestTO utReqTO = (UpdateTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		ArrayList<TicketTO> tktListTO = utReqTO.getTktList();

		// RULE: Are the number of products/tickets on the order within tolerance?
		TicketRules.validateMaxEightTicketsOnRequest(tktListTO);

		return;
	}
>>>>>>> develop

}
