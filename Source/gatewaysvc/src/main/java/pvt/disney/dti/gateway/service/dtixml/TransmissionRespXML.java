package pvt.disney.dti.gateway.service.dtixml;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountResponseTO;
import pvt.disney.dti.gateway.data.CreateTicketResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryReservationResponseTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.RenewEntitlementResponseTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementResponseTO;
import pvt.disney.dti.gateway.data.UpdateTicketResponseTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementResponseTO;
import pvt.disney.dti.gateway.data.VoidReservationResponseTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.response.xsd.AssociateMediaToAccountResponse;
import pvt.disney.dti.gateway.response.xsd.CommandHeader;
import pvt.disney.dti.gateway.response.xsd.CreateTicketResponse;
import pvt.disney.dti.gateway.response.xsd.PayloadHeader;
import pvt.disney.dti.gateway.response.xsd.QueryTicketResponse;
import pvt.disney.dti.gateway.response.xsd.RenewEntitlementResponse;
import pvt.disney.dti.gateway.response.xsd.ReservationResponse;
import pvt.disney.dti.gateway.response.xsd.TickerateEntitlementResponse;
import pvt.disney.dti.gateway.response.xsd.Transmission;
import pvt.disney.dti.gateway.response.xsd.UpdateTicketResponse;
import pvt.disney.dti.gateway.response.xsd.UpgradeAlphaResponse;
import pvt.disney.dti.gateway.response.xsd.UpgradeEntitlementResponse;
import pvt.disney.dti.gateway.response.xsd.VoidReservationResponse;
import pvt.disney.dti.gateway.response.xsd.VoidTicketResponse;
import pvt.disney.dti.gateway.response.xsd.QueryReservationResponse;
import pvt.disney.dti.gateway.response.xsd.Transmission.Payload;
import pvt.disney.dti.gateway.response.xsd.Transmission.Payload.Command;

/**
 * This class is responsible for transformations between the DTI TiXML and the internal objects used by the DTI Application. This class manages the response version of the transmission clause.
 * 
 * This class is separate from the RqstXML class because of the name collisions between request and response class names in the XSD's.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class TransmissionRespXML {

  // JAXBContext is thread-safe.
  private static JAXBContext jc;
  private static Object thisInstance = null;

  /**
   * 
   * @throws JAXBException
   */
  private TransmissionRespXML() throws JAXBException {

    jc = JAXBContext.newInstance("pvt.disney.dti.gateway.response.xsd");

    return;
  }

  /**
   * When passed the DTI Application object, return the XML string using JAXB.
   * 
   * @param responseIn
   *            The DTI application object
   * @return the XML string
   * @throws JAXBException
   *             should any parsing errors occur.
   */
  public static String getXML(DTITransactionTO responseIn) throws JAXBException {

    if (thisInstance == null) {
      thisInstance = new TransmissionRespXML();
    }

    String xmlString = "";

    // Transmission
    Transmission jaxbResp = new Transmission();

    // Payload
    Payload payload = new Payload();

    DTIErrorTO dtiErrorTO = responseIn.getResponse().getDtiError();

    // Payload Header
    PayloadHeaderTO payloadHeaderTO = responseIn.getResponse()
        .getPayloadHeader();
    PayloadHeader payloadHeader = PayloadHeaderXML.getJaxb(payloadHeaderTO,
        dtiErrorTO);
    payload.setPayloadHeader(payloadHeader);

    Command cmd = null;
    if ((dtiErrorTO != null) && (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.PAYLOAD)) {
      // DO NOT SET THE COMMAND HEADER
    }
    else {

      // Get the Command Header TO

      // If there is a ticket level error and the command body is
      // null, then alter to a command level error. Better a misplaced error
      // than a null pointer exception! JTL 09/21/2009
      if ((dtiErrorTO != null) && (responseIn.getResponse()
          .getCommandBody() == null)) {
        if ((dtiErrorTO.getErrorScope() != DTIErrorCode.ErrorScope.TICKET) || (dtiErrorTO
            .getErrorScope() != DTIErrorCode.ErrorScope.MEDIA)) {
          dtiErrorTO.setErrorScope(DTIErrorCode.ErrorScope.COMMAND);
        }
      }

      cmd = new Command();
      CommandHeaderTO commandHeaderTO = responseIn.getResponse()
          .getCommandHeader();
      CommandHeader commandHeader = CommandHeaderXML.getJaxb(
          commandHeaderTO, dtiErrorTO);
      cmd.setCommandHeader(commandHeader);
    }

    // Get the command body TO
    // If there is a command error or payload error, do not include command
    // body.

    // An example of this would be for the price mismatch warning.

    if ((dtiErrorTO != null) && ((dtiErrorTO.getErrorScope() != DTIErrorCode.ErrorScope.TICKET) && (dtiErrorTO
        .getErrorScope() != DTIErrorCode.ErrorScope.MEDIA))) {

      if ((dtiErrorTO.getErrorCode().toString().compareTo(
              DTIErrorCode.PRICE_MISMATCH_WARNING.getErrorCode()) == 0) && (responseIn
          .getResponse().getCommandBody() != null)) {
        setCommandBodyXML(responseIn, cmd, dtiErrorTO);
      }
      else {

        if (cmd != null) {
          // Set an empty command body, to be compliant with XSDs.
          setEmptyCommandBodyXML(responseIn, cmd);
        }
        else {
          // DO NOT SET THE COMMAND BODY
        }

      }
    }
    else {
      setCommandBodyXML(responseIn, cmd, dtiErrorTO);
    }

    if (cmd != null) payload.setCommand(cmd);
    jaxbResp.setPayload(payload);

    // Create the XML from the jaxbResp
    // NOTE: Marshaler is NOT thread-safe and cannot be shared. It must be
    // recreated for each operation. 
    Marshaller marshaller;
    marshaller = jc.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//    marshaller
//        .setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);

    StringWriter objStringWriter = new StringWriter();
    marshaller.marshal(jaxbResp, objStringWriter);
    xmlString = objStringWriter.toString();

    xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlString;

    return xmlString;

  }

  /**
   * Sets the command body appropriately in the response JAX object.
   * 
   * @param responseIn
   *            the DTI Version of the response object
   * @param cmd
   *            The JAXB version of the command body
   * @throws JAXBException
   *             should any parsing errors occur.
   */
  private static void setCommandBodyXML(DTITransactionTO responseIn,
      Command cmd, DTIErrorTO errorTO) throws JAXBException {

    TransactionType responseType = responseIn.getTransactionType();

    switch (responseType) {

    case QUERYTICKET:
      QueryTicketResponseTO queryRespTO = (QueryTicketResponseTO) responseIn
          .getResponse().getCommandBody();
      QueryTicketResponse queryResp = QueryTicketXML.getJaxb(queryRespTO,
          errorTO);
      cmd.setQueryTicketResponse(queryResp);
      break;

    case RESERVATION:
      ReservationResponseTO resRespTO = (ReservationResponseTO) responseIn
          .getResponse().getCommandBody();
      ReservationResponse resResp = ReservationXML.getJaxb(resRespTO,
          errorTO);
      cmd.setReservationResponse(resResp);
      break;

    case UPGRADEALPHA:
      UpgradeAlphaResponseTO uaRespTO = (UpgradeAlphaResponseTO) responseIn
          .getResponse().getCommandBody();
      UpgradeAlphaResponse uaResp = UpgradeAlphaXML.getJaxb(uaRespTO,
          errorTO);
      cmd.setUpgradeAlphaResponse(uaResp);
      break;

    case VOIDTICKET:
      VoidTicketResponseTO vtRespTO = (VoidTicketResponseTO) responseIn
          .getResponse().getCommandBody();
      VoidTicketResponse vtResp = VoidTicketXML
          .getJaxb(vtRespTO, errorTO);
      cmd.setVoidTicketResponse(vtResp);
      break;

    case CREATETICKET:
      CreateTicketResponseTO ctRespTO = (CreateTicketResponseTO) responseIn
          .getResponse().getCommandBody();
      CreateTicketResponse ctResp = CreateTicketXML.getJaxb(ctRespTO,
          errorTO);
      cmd.setCreateTicketResponse(ctResp);
      break;

    case UPDATETICKET:
      UpdateTicketResponseTO uTktRespTO = (UpdateTicketResponseTO) responseIn
          .getResponse().getCommandBody();
      UpdateTicketResponse uTktResp = UpdateTicketXML.getJaxb(uTktRespTO,
          errorTO);
      cmd.setUpdateTicketResponse(uTktResp);
      break;

    case UPGRADEENTITLEMENT: // 2.10
      UpgradeEntitlementResponseTO uEntRespTO = (UpgradeEntitlementResponseTO) responseIn
          .getResponse().getCommandBody();
      UpgradeEntitlementResponse uEntResp = UpgradeEntitlementXML
          .getJaxb(uEntRespTO, errorTO);
      cmd.setUpgradeEntitlementResponse(uEntResp);
      break;

    case QUERYRESERVATION:
      QueryReservationResponseTO qResRespTO = (QueryReservationResponseTO) responseIn
          .getResponse().getCommandBody();
      QueryReservationResponse qResResp = QueryReservationXML.getJaxb(
          qResRespTO, errorTO);
      cmd.setQueryReservationResponse(qResResp);
      break;

    case ASSOCIATEMEDIATOACCOUNT: // 2.16.1 BIEST001
      AssociateMediaToAccountResponseTO associateMediaToAccountRespTO = (AssociateMediaToAccountResponseTO) responseIn
          .getResponse().getCommandBody();
      AssociateMediaToAccountResponse associateMediaToAccountResp = AssociateMediaToAccountXML
          .getJaxb(associateMediaToAccountRespTO, errorTO);
      cmd.setAssociateMediaToAccountResponse(associateMediaToAccountResp);
      break;

    case TICKERATEENTITLEMENT: // 2.16.1 BIEST001
      TickerateEntitlementResponseTO tickerateEntitlementRespTO = (TickerateEntitlementResponseTO) responseIn
          .getResponse().getCommandBody();
      TickerateEntitlementResponse tickerateEntitlementResp = TickerateEntitlementXML
          .getJaxb(tickerateEntitlementRespTO, errorTO);
      cmd.setTickerateEntitlementResponse(tickerateEntitlementResp);
      break;

    case RENEWENTITLEMENT: // As of 2.16.1, JTL
      RenewEntitlementResponseTO qRenewRespTO = (RenewEntitlementResponseTO) responseIn
          .getResponse().getCommandBody();
      RenewEntitlementResponse qRenewResp = RenewEntitlementXML.getJaxb(
          qRenewRespTO, errorTO);
      cmd.setRenewEntitlementResponse(qRenewResp);
      break;
      
    case VOIDRESERVATION: // As of 2.16.3, JTL
      VoidReservationResponseTO vResRespTO = (VoidReservationResponseTO) responseIn
          .getResponse().getCommandBody();
      VoidReservationResponse vResResp = VoidReservationXML.getJaxb(
          vResRespTO, errorTO);
      cmd.setVoidReservationResponse(vResResp);
      break;      

    default:
      break;

    /* Note: UpdateTransactionResponse does not have any members to translate. */

    }
  }

  /**
   * Sets the command body appropriately in the response JAX object.
   * 
   * @param responseIn
   *            the DTI Version of the response object
   * @param cmd
   *            The JAXB version of the command body
   * @throws JAXBException
   *             should any parsing errors occur.
   */
  private static void setEmptyCommandBodyXML(DTITransactionTO responseIn,
      Command cmd) throws JAXBException {
    TransactionType responseType = responseIn.getTransactionType();
    switch (responseType) {

    case QUERYTICKET:
      QueryTicketResponse queryResp = new QueryTicketResponse();
      cmd.setQueryTicketResponse(queryResp);
      break;

    case RESERVATION:
      // Old XSD's allowed responses w/o empty response body.
      // Old gateway responded that way a well.
      // Omitted for consistency with old system.
      break;

    case UPGRADEALPHA:
      UpgradeAlphaResponse uaResp = new UpgradeAlphaResponse();
      cmd.setUpgradeAlphaResponse(uaResp);
      break;

    case VOIDTICKET:
      VoidTicketResponse vtResp = new VoidTicketResponse();
      cmd.setVoidTicketResponse(vtResp);
      break;

    case CREATETICKET:
      CreateTicketResponse ctResp = new CreateTicketResponse();
      cmd.setCreateTicketResponse(ctResp);
      break;

    case UPDATETICKET:
      UpdateTicketResponse uTktResp = new UpdateTicketResponse();
      cmd.setUpdateTicketResponse(uTktResp);
      break;

    case QUERYRESERVATION:
      QueryReservationResponse qResResp = new QueryReservationResponse();
      cmd.setQueryReservationResponse(qResResp);
      break;

    case ASSOCIATEMEDIATOACCOUNT:
      AssociateMediaToAccountResponse associateMediaToAccountResp = new AssociateMediaToAccountResponse();
      cmd.setAssociateMediaToAccountResponse(associateMediaToAccountResp);
      break;

    case TICKERATEENTITLEMENT:
      TickerateEntitlementResponse tickerateEntitlementResp = new TickerateEntitlementResponse();
      cmd.setTickerateEntitlementResponse(tickerateEntitlementResp);
      break;

    case RENEWENTITLEMENT:
      RenewEntitlementResponse qRenewResp = new RenewEntitlementResponse();
      cmd.setRenewEntitlementResponse(qRenewResp);
      break;

    default:
      break;

    /* Note: UpdateTransactionResponse does not have any members to translate. */

    }

    return;
  }

}
