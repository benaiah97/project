package pvt.disney.dti.gateway.service.dtixml;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.request.xsd.UpdateTransactionRequest;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public class UpdateTransactionXML {

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param uTranReq
	 *            the parsed JAXB structure.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	@SuppressWarnings("rawtypes")
	public static UpdateTransactionRequestTO getTO(
			UpdateTransactionRequest uTranReq) throws JAXBException {

		UpdateTransactionRequestTO uTranReqTO = new UpdateTransactionRequestTO();

		List stanzaList = uTranReq.getTransactionOrSalesRepOrAgency();

		for /* each */(Object stanza : /* in */stanzaList) {

			// Required items
			// Transaction
			// TktItem

			if (stanza.getClass().getSimpleName()
					.equalsIgnoreCase("Transaction")) {
				UpdateTransactionRequest.Transaction tran = (UpdateTransactionRequest.Transaction) stanza;
				TicketTransactionTO tktTranTO = new TicketTransactionTO();

				if (tran.getTranID().getTranDSSN() != null) {

					UpdateTransactionRequest.Transaction.TranID.TranDSSN tranDssn = tran
							.getTranID().getTranDSSN();

					String tranDssnDateString = tranDssn.getTranDate();
					GregorianCalendar tranDssnDate = CustomDataTypeConverter
							.parseYYYYMMDDDate(tranDssnDateString);
					tktTranTO.setDssnDate(tranDssnDate);
					tktTranTO.setDssnSite(tranDssn.getTranSite());
					tktTranTO.setDssnStation(tranDssn.getTranStation());
					tktTranTO.setDssnNumber(tranDssn.getTranNbr());
				}

				if (tran.getTranID().getTranNID() != null) {
					tktTranTO.setTranNID(tran.getTranID().getTranNID());
				}

				uTranReqTO.setTransaction(tktTranTO);
			}

			// Optional items
			// SalesRep
			if (stanza.getClass().getSimpleName().equalsIgnoreCase("String")) {
				String salesRep = (String) stanza;
				uTranReqTO.setSalesRep(salesRep);
			}

			// Agency
			if (stanza.getClass().getSimpleName().equalsIgnoreCase("Agency")) {
				UpdateTransactionRequest.Agency agency = (UpdateTransactionRequest.Agency) stanza;
				AgencyTO agencyTO = new AgencyTO();

				List<JAXBElement<String>> innerStanzaList = agency
						.getIATAOrOTA();

				for /* each */(JAXBElement<String> innerStanza : /* in */innerStanzaList) {

					if (innerStanza.getName().getLocalPart()
							.equalsIgnoreCase("IATA")) agencyTO
							.setIATA(innerStanza.getValue());

				}

				uTranReqTO.setAgency(agencyTO);

			}

		} // for

		return uTranReqTO;
	}

	/** Note that update transaction has no response component. */

}
