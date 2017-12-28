package pvt.disney.dti.gateway.provider.dlr.xml;

import org.dom4j.Element;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;

public class GWBodyQueryProductXML {
	/**
	 * Process the request from DTI to the provider
	 * 
	 * @param gwBodyTO
	 *            The body value object for a DLR request.
	 * @param envelopeElement
	 *            the higher level element which needs to have the body element added.
	 * @return the body element.
	 * @throws DTIException
	 *             should any parsing or marshalling issues occur.
	 */
	public static Element addBodyElement(GWBodyTO gwBodyTO,
			Element envelopeElement, TransactionType dtiTxnType) throws DTIException {

      Element bodyElement = envelopeElement.addElement("Body");

      // Determine the type of transaction being passed.
      GWQueryTicketRqstTO qtReqTO = gwBodyTO.getQueryTicketRqstTO();
      if (qtReqTO == null) {
         throw new DTIException(GWBodyQueryProductXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "QueryTicket request did not have a fully formed value object.");
      }
      qtReqTO.setDtiTxnType(dtiTxnType);

      GWQueryTicketXML.addQueryTicketElement(qtReqTO, bodyElement);

      return bodyElement;
	}
	/**
	 * Unmarshalls the response into the transfer/value objects.
	 * 
	 * @param bodyElement
	 *            The body element as provided by the response.
	 * @param txnType
	 *            The transaction type of this response.
	 * @return The body transfer object with the appropriate information supplied.
	 * @throws DTIException
	 *             Should any unmarshalling problem occur.
	 */
	public static GWBodyTO getTO(Element bodyElement,
			GWEnvelopeTO.GWTransactionType txnType) throws DTIException {
		GWBodyTO gwBodyTO = new GWBodyTO();

		if (txnType == GWEnvelopeTO.GWTransactionType.UNDEFINED) {
			throw new DTIException(
					GWBodyXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned Body without Header, or Body before Header clause in XML.");
		}

		if (txnType == GWEnvelopeTO.GWTransactionType.QUERYTICKET) {
		   GWQueryTicketXML.setRespBodyTO(gwBodyTO, bodyElement);
		}
		else {
			throw new DTIException(GWBodyXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned Body without known transaction type in XML.");
		}
		return gwBodyTO;
	}

}

