package pvt.disney.dti.gateway.service.dtixml;

import java.io.ByteArrayInputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.request.xsd.AssociateMediaToAccountRequest;
import pvt.disney.dti.gateway.request.xsd.CommandHeader;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader;
import pvt.disney.dti.gateway.request.xsd.QueryReservationRequest;
import pvt.disney.dti.gateway.request.xsd.QueryTicketRequest;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest;
import pvt.disney.dti.gateway.request.xsd.TickerateEntitlementRequest;
import pvt.disney.dti.gateway.request.xsd.Transmission;
import pvt.disney.dti.gateway.request.xsd.UpdateTicketRequest;
import pvt.disney.dti.gateway.request.xsd.UpdateTransactionRequest;
import pvt.disney.dti.gateway.request.xsd.UpgradeAlphaRequest;
import pvt.disney.dti.gateway.request.xsd.UpgradeEntitlementRequest;
import pvt.disney.dti.gateway.request.xsd.VoidReservationRequest;
import pvt.disney.dti.gateway.request.xsd.VoidTicketRequest;
import pvt.disney.dti.gateway.request.xsd.RenewEntitlementRequest;

/**
 * This class is responsible for transformations between the DTI TiXML and the internal objects used by the DTI Application. This class manages the request version of the transmission.
 * 
 * This class is separate from the RespXML class because of the name collisions between request and response class names in the XSD's.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class TransmissionRqstXML {

  private static JAXBContext jc;
  private static SchemaFactory schemaFactory;
  private static Schema schema;
  private static Object thisInstance = null;
  /** Event logger. */
  private static final EventLogger logger = EventLogger
      .getLogger("pvt.disney.dti.gateway.service.dtixml.TransmissionRqstXML");

  /**
   * 
   * @throws JAXBException
   */
  private TransmissionRqstXML() throws JAXBException {

    jc = JAXBContext.newInstance("pvt.disney.dti.gateway.request.xsd");

    schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    schema = null;

    try {
      schema = schemaFactory.newSchema();
    }
    catch (SAXException se) {
      logger.sendException(
          "SAX Exception: " + XMLConstants.W3C_XML_SCHEMA_NS_URI,
          EventType.EXCEPTION, ErrorCode.XML_PARSE_EXCEPTION, se,
          this);
      se.printStackTrace();
    }

    return;
  }

  /**
   * When passed the JAXB object, return the DTI application object.
   * 
   * @param requestXMLin
   *            The inbound request XML
   * @param requestType
   *            The transaction request type (enumeration)
   * @return The DTI Application Object
   * @throws JAXBException
   *             should any parsing errors occur.
   */
  public static DTITransactionTO getTO(String requestXMLin,
      TransactionType requestType, String tktBroker) throws JAXBException {

    if (thisInstance == null) {
      thisInstance = new TransmissionRqstXML();
    }

    DTITransactionTO dtiTransTO;

    // NOTE: Unmarshaller does not appear to be thread-safe. It must be recreated
    // for each use.
    Unmarshaller unmarshaller;
    unmarshaller = jc.createUnmarshaller();
    unmarshaller.setSchema(schema);

    Transmission jaxbReq;
    jaxbReq = (Transmission) unmarshaller
        .unmarshal(new ByteArrayInputStream(requestXMLin.getBytes()));

    dtiTransTO = new DTITransactionTO(requestType);
    dtiTransTO.setTktBroker(tktBroker);

    DTIRequestTO dtiRequestTO = new DTIRequestTO();

    // Get the payload header TO
    Transmission.Payload payload = jaxbReq.getPayload();
    PayloadHeader payloadHeader = payload.getPayloadHeader();
    PayloadHeaderTO payloadHeaderTO = PayloadHeaderXML.getTO(payloadHeader);
    dtiRequestTO.setPayloadHeader(payloadHeaderTO);

    // Get the command header TO
    CommandHeader commandHeader = payload.getCommand().getCommandHeader();
    CommandHeaderTO commandHeaderTO = CommandHeaderXML.getTO(commandHeader);
    dtiRequestTO.setCommandHeader(commandHeaderTO);

    // Get the command body TO
    CommandBodyTO commandBodyTO = null;
    switch (requestType) {

    case QUERYTICKET:
      QueryTicketRequest queryReq = payload.getCommand()
          .getQueryTicketRequest();
      commandBodyTO = QueryTicketXML.getTO(queryReq);
      break;

    case RESERVATION:
      ReservationRequest resReq = payload.getCommand()
          .getReservationRequest();
      commandBodyTO = ReservationXML.getTO(resReq);
      break;

    case UPGRADEALPHA:
      UpgradeAlphaRequest uaReq = payload.getCommand()
          .getUpgradeAlphaRequest();
      commandBodyTO = UpgradeAlphaXML.getTO(uaReq);
      break;

    case VOIDTICKET:
      VoidTicketRequest vtReq = payload.getCommand()
          .getVoidTicketRequest();
      commandBodyTO = VoidTicketXML.getTO(vtReq);
      break;

    case CREATETICKET:
      CreateTicketRequest ctReq = payload.getCommand()
          .getCreateTicketRequest();
      commandBodyTO = CreateTicketXML.getTO(ctReq);
      break;

    case UPDATETICKET:
      UpdateTicketRequest uTktReq = payload.getCommand()
          .getUpdateTicketRequest();
      commandBodyTO = UpdateTicketXML.getTO(uTktReq);
      break;

    case UPDATETRANSACTION:
      UpdateTransactionRequest uTxnReq = payload.getCommand()
          .getUpdateTransactionRequest();
      commandBodyTO = UpdateTransactionXML.getTO(uTxnReq);
      break;

    case QUERYRESERVATION:
      QueryReservationRequest qResReq = payload.getCommand()
          .getQueryReservationRequest();
      commandBodyTO = QueryReservationXML.getTO(qResReq);
      break;

    case UPGRADEENTITLEMENT: // 2.10
      UpgradeEntitlementRequest uEntReq = payload.getCommand()
          .getUpgradeEntitlementRequest();
      commandBodyTO = UpgradeEntitlementXML.getTO(uEntReq);
      break;

    case ASSOCIATEMEDIATOACCOUNT: // 2.16.1 BIEST001
      AssociateMediaToAccountRequest amReq = payload.getCommand()
          .getAssociateMediaToAccountRequest();
      commandBodyTO = AssociateMediaToAccountXML.getTO(amReq);
      break;

    case TICKERATEENTITLEMENT: // 2.16.1 BIEST001
      TickerateEntitlementRequest teReq = payload.getCommand()
          .getTickerateEntitlementRequest();
      commandBodyTO = TickerateEntitlementXML.getTO(teReq);
      break;

    case RENEWENTITLEMENT: // as of 2.16.1, JTL
      RenewEntitlementRequest rEntReq = payload.getCommand()
          .getRenewEntitlementRequest();
      commandBodyTO = RenewEntitlementXML.getTO(rEntReq);
      break;
      
    case VOIDRESERVATION: // as of 2.16.3, JTL
      VoidReservationRequest vResReq = payload.getCommand().getVoidReservationRequest();
      commandBodyTO = VoidReservationXML.getTO(vResReq);
      break;      

    default:
      break;

    }

    dtiRequestTO.setCommandBody(commandBodyTO);

    dtiTransTO.setRequest(dtiRequestTO);

    return dtiTransTO;

  }
}
