package pvt.disney.dti.gateway.provider.wdw.xml;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Update Transaction Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 * 
 */
public class OTUpdateTransactionXML {

	/**
	 * Adds the transacation body of update transaction to the command stanza when provided with the appropriate transfer object.
	 * 
	 * @param otUpdtTxnTO
	 *            The Omni Ticket Update Transaction Transfer Object.
	 * @param commandStanza
	 *            The command stanza needing the update transaction stanza.
	 */
	static void addTxnBodyElement(OTUpdateTransactionTO otUpdtTxnTO,
			Element commandStanza) {

		// UpdateTransaction
		Element updTxnStanza = commandStanza.addElement("UpdateTransaction");

		// SiteNumber
		updTxnStanza.addElement("SiteNumber").addText(
				otUpdtTxnTO.getSiteNumber().toString());

		// Transaction
		Element txnStanza = updTxnStanza.addElement("Transaction");
		txnStanza.addElement("Item").addText(otUpdtTxnTO.getItem().toString());

		// TransactionSearchMode TransactionDssn TransactionCOD
		Element txnSrchMdStanza = txnStanza.addElement("TransactionSearchMode");
		if (otUpdtTxnTO.getTransactionDSSN() != null) {
			OTTransactionDSSNTO otTxn = otUpdtTxnTO.getTransactionDSSN();

			Element txnDSSNStanza = txnSrchMdStanza
					.addElement("TransactionDSSN");
			txnDSSNStanza.addElement("Site").addText(otTxn.getSite());
			txnDSSNStanza.addElement("Station").addText(otTxn.getStation());
			GregorianCalendar tdssnDateCal = otTxn.getDate();
			String tdssnDate = CustomDataTypeConverter
					.printCalToDTIDateFormat(tdssnDateCal);
			txnDSSNStanza.addElement("Date").addText(tdssnDate);
			txnDSSNStanza.addElement("TransactionId").addText(
					otTxn.getTransactionId().toString());
		}
		else {
			txnSrchMdStanza.addElement("TransactionCOD").addText(
					otUpdtTxnTO.getTransactionCOD());
		}

		// TransactionNote
		if (otUpdtTxnTO.getTransactionNote() != null) {
			updTxnStanza.addElement("TransactionNote").addText(
					otUpdtTxnTO.getTransactionNote());
		}

		// Seller
		if (otUpdtTxnTO.getSeller() != null) {
			updTxnStanza.addElement("Seller").addText(otUpdtTxnTO.getSeller());
		}

		// ZipCode
		if (otUpdtTxnTO.getZipCode() != null) {
			updTxnStanza.addElement("ZipCode")
					.addText(otUpdtTxnTO.getZipCode());
		}

		// IATA
		if (otUpdtTxnTO.getIata() != null) {
			updTxnStanza.addElement("IATA").addText(otUpdtTxnTO.getIata());
		}

		// InTransactionAttribute
		ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otUpdtTxnTO
				.getInTxnAttrList();
		OTCommandXML.addInTransactionAttributeStanzas(updTxnStanza,
				inTxnAttrList);

		return;
	}

	/**
	 * When provided with the parsed XML, creates the transfer object for the update transaction.
	 * 
	 * @param updtTxnResStanza
	 *            the parsed XML containing the update transaction response.
	 * @return The Omni Ticket Update Transaction Transfer Object.
	 * @throws DTIException
	 *             for any missing required fields or other parsing errors.
	 */
	static OTUpdateTransactionTO getTO(Node updtTxnResNode) throws DTIException {

		OTUpdateTransactionTO otUpdtTxnTO = new OTUpdateTransactionTO();

		// Error
		Node errorNode = updtTxnResNode.selectSingleNode("Error");
		if (errorNode == null) throw new DTIException(
				OTUpdateTransactionXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a UpdateTransaction,Error element.");
		else otUpdtTxnTO.setError(OTCommandXML.setOTErrorTO(errorNode));

		return otUpdtTxnTO;
	}

}
