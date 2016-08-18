package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

/**
 * This class is responsible for three major functions for WDW update transaction:<BR>
 * 1. Defining the business rules specific to WDW update transaction.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 */
public class WDWUpdateTransactionRules {

	/** Request type header constant. */
	private final static String REQUEST_TYPE_UPDATE = "Update";

	/** Request subtype header constant. */
	private final static String REQUEST_SUBTYPE_UPDATE_TXN = "UpdateTransaction";

	/** Constant indicating the Update Transaction XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "UpdateTransactionRequest.xsd";

	/** Constant indicating the first item. */
	private final static BigInteger ITEMONE = new BigInteger("1");

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
		CommandHeaderTO dtiCmdHeader = dtiRequest.getCommandHeader();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		UpdateTransactionRequestTO dtiUpdateTxnReq = (UpdateTransactionRequestTO) dtiCmdBody;

		// === Command Level ===
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.UPDATETRANSACTION);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// === Header Level ===
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_UPDATE, REQUEST_SUBTYPE_UPDATE_TXN);
		atsCommand.setHeaderTO(hdr);

		// === Update Transacation Level ===
		OTUpdateTransactionTO otUpdtTxn = new OTUpdateTransactionTO();

		// SiteNumber
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otUpdtTxn.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		}
		else {
			otUpdtTxn.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		// Item
		otUpdtTxn.setItem(ITEMONE);

		// TransactionSearchMode
		TicketTransactionTO dtiTktTxn = dtiUpdateTxnReq.getTransaction();
		if (dtiTktTxn.getTxnIdType() == TicketTransactionTO.TransactionIDType.TRANSDSSN) {
			OTTransactionDSSNTO otTxnDSSN = new OTTransactionDSSNTO();
			otTxnDSSN.setDate(dtiTktTxn.getDssnDate());
			otTxnDSSN.setSite(dtiTktTxn.getDssnSite());
			otTxnDSSN.setStation(dtiTktTxn.getDssnStation());
			otTxnDSSN
					.setTransactionId(Integer.decode(dtiTktTxn.getDssnNumber()));
			otUpdtTxn.setTransactionDSSN(otTxnDSSN);
		}
		else {
			otUpdtTxn.setTransactionCOD(dtiTktTxn.getTranNID());
		}

		// TransactionNote (as of 2.10, always the payload ID per Financial Systems's request JTL
		otUpdtTxn.setTransactionNote(dtiRequest.getPayloadHeader()
				.getPayloadID());
		// if (dtiCmdHeader.getCmdNote() != null) {
		// otUpdtTxn.setTransactionNote(dtiCmdHeader.getCmdNote());
		// }

		// Seller
		if (dtiUpdateTxnReq.getSalesRep() != null) {
			otUpdtTxn.setSeller(dtiUpdateTxnReq.getSalesRep());
		}

		// ZipCode
		if (dtiCmdHeader.getCmdMarket() != null) {
			otUpdtTxn.setZipCode(dtiCmdHeader.getCmdMarket());
		}

		// IATA
		if (dtiUpdateTxnReq.getAgency() != null) {
			AgencyTO dtiAgency = dtiUpdateTxnReq.getAgency();

			if (dtiAgency.getIATA() != null) {
				otUpdtTxn.setIata(dtiAgency.getIATA());
			}

		}

		// InTransactionAttributes
		ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otUpdtTxn
				.getInTxnAttrList();
		WDWBusinessRules.setOTInTransactionAttributes(dtiTxn, inTxnAttrList);

		// Set the Create Transaction TO on the command
		atsCommand.setUpdateTransactionTO(otUpdtTxn);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;
	}

	/**
	 * Transforms a update ticket response string from the WDW provider and updates the DTITransactionTO object with the response information. In thise case, there are no fields which need to be transformed.
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

		return;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
	 * No rules are implemented at the present time.
	 * 
	 * @param dtiTxn
	 *            the transaction object for this request
	 * @throws DTIException
	 *             for any rules error.
	 */
	public static void applyWDWUpdateTransactionRules(DTITransactionTO dtiTxn) throws DTIException {
		return;
	}

}
