package pvt.disney.dti.gateway.provider.dlr.xml;

import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.rules.TransformConstants;

public class GWEnvelopeXML implements TransformConstants {

   /**
    * Process the request from DTI to the provider
    * 
    * @param gwEnvTO
    *           The envelope transfer object.
    * @return the XML string value of the request.
    * @throws DTIException
    *            should any marshalling issue appear.
    */
   public static String getXML(GWEnvelopeTO gwEnvTO) throws DTIException {

      GWHeaderTO gwHdrTO = null;
      GWBodyTO gwBodyTO = null;
      String xmlString = null;

      if (gwEnvTO != null) {
         gwHdrTO = gwEnvTO.getHeaderTO();
         gwBodyTO = gwEnvTO.getBodyTO();
      } else {
         throw new DTIException(GWEnvelopeXML.class, DTIErrorCode.INVALID_MSG_CONTENT,
                  "Null GWEnvelopeTO passed to getXML.  Can't render XML.");
      }

      Document document = DocumentHelper.createDocument();
      Element envelopeStanza = document.addElement("Envelope");

      GWHeaderXML.addHeaderElement(gwHdrTO, envelopeStanza);
      GWBodyXML.addBodyElement(gwBodyTO, envelopeStanza, gwEnvTO.getTxnType());
      xmlString = document.asXML();

      return xmlString;
   }

   /**
    * Process the response from the provider, back to DTI
    * 
    * @param xmlResponse
    *           String value of the response.
    * @return the completely filled-in envelope transfer object.
    * @throws DTIException
    *            should any unmarshalling error occur.
    */
   @SuppressWarnings("unchecked")
   public static GWEnvelopeTO getTO(String xmlResponse) throws DTIException {

      Document document = null;
      GWEnvelopeTO gwEnvTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.UNDEFINED);
      GWHeaderTO gwHdrTO = null;
      GWBodyTO gwBodyTO = null;

      try {
         document = DocumentHelper.parseText(xmlResponse);
      } catch (DocumentException de) {
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Unable to parse XML from provider: " + de.toString());
      }

      Element envelope = document.getRootElement();

      if (envelope.getName().compareTo("Envelope") != 0)
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned XML without a Envelope element.");

      // Locate the Header and Body Sections
      for (Iterator<org.dom4j.Element> i = envelope.elementIterator(); i.hasNext();) {

         Element element = i.next();

         if (element.getName().compareTo("Header") == 0) {
            gwHdrTO = GWHeaderXML.getTO(element);

            if (gwHdrTO.getMessageType().compareTo(GW_QRY_TKT_RSP_MSG_TYPE) == 0) {
               gwEnvTO.setTxnType(GWEnvelopeTO.GWTransactionType.QUERYTICKET);
            } else if (gwHdrTO.getMessageType().compareTo(GW_TKT_ACTIVATE_MSG_TYPE) == 0) {
               gwEnvTO.setTxnType(GWEnvelopeTO.GWTransactionType.TICKETACTIVATION);
               // responses have a different msg typ in the header than requests
            } else if (gwHdrTO.getMessageType().compareTo(GW_ORDERS_RESPONSE_MESSAGE_TYPE) == 0) {
               gwEnvTO.setTxnType(GWEnvelopeTO.GWTransactionType.ORDERS);
            } else if (gwHdrTO.getMessageType().compareTo(GW_QUERY_ORDER_RESPONSE_MESSAGE_TYPE) == 0) {
               gwEnvTO.setTxnType(GWEnvelopeTO.GWTransactionType.QUERYORDER);
            } else {
               StringBuffer errorBuffer = new StringBuffer("Ticket provider returned unknown Header,Message type: ");
               if (gwHdrTO.getMessageType() == null) {
                  errorBuffer.append("null");
               } else {
                  errorBuffer.append(gwHdrTO.getMessageType());
               }
               throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE, errorBuffer.toString());
            }
         }

         GWEnvelopeTO.GWTransactionType txnType = gwEnvTO.getTxnType();
         if (element.getName().compareTo("Body") == 0) {
            gwBodyTO = GWBodyXML.getTO(element, txnType);
         }

      }

      // if header found, add to envelope. If not found, error out.
      if (gwHdrTO != null)
         gwEnvTO.setHeaderTO(gwHdrTO);
      else {
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned XML without a Header element.");
      }

      // if body found, add to envelope. If not found, error out.
      if (gwBodyTO != null)
         gwEnvTO.setBodyTO(gwBodyTO);
      else {
         throw new DTIException(GWHeaderTO.class, DTIErrorCode.TP_INTERFACE_FAILURE,
                  "Ticket provider returned XML without a Body element.");
      }

      return gwEnvTO;
   }

}
