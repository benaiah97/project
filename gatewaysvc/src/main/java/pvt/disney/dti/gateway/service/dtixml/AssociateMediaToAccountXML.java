package pvt.disney.dti.gateway.service.dtixml;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountRequestTO;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.request.xsd.AssociateMediaToAccountRequest;
import pvt.disney.dti.gateway.request.xsd.Ticket;
import pvt.disney.dti.gateway.request.xsd.Ticket.TktID.TktDSSN;
import pvt.disney.dti.gateway.request.xsd.MediaData;
import pvt.disney.dti.gateway.response.xsd.AssociateMediaToAccountResponse;
import pvt.disney.dti.gateway.response.xsd.EntitlementAccount;
import pvt.disney.dti.gateway.response.xsd.TktError;
import pvt.disney.dti.gateway.response.xsd.Ticket.TktID;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author biest001
 * @since 2.16.1
 * 
 */
public abstract class AssociateMediaToAccountXML {

	private static final EventLogger logger = EventLogger
			.getLogger(AssociateMediaToAccountXML.class.getCanonicalName());

	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param amReq
	 *            - the JAXB version of the associate media request transfer object
	 * @return the DTI application object
	 * @throws JAXBException
	 *             - should any parsing errors occur.
	 */
	public static AssociateMediaToAccountRequestTO getTO(
			AssociateMediaToAccountRequest associateReq) throws JAXBException {

		// declarations for the transfer object (TO)
		AssociateMediaToAccountRequestTO amReqTO = new AssociateMediaToAccountRequestTO();
		ArrayList<NewMediaDataTO> mediaTOList = new ArrayList<NewMediaDataTO>();

		// **************************************************
		// ExistingAccount
		if (associateReq.getExistingAccount() != null) {

			// What search criteria was selected? Existing Media Id, Account Id, or Ticket Identifier??
			if (associateReq.getExistingAccount().getExistingMediaId() != null) {
				amReqTO.setExistingMediaId(associateReq.getExistingAccount()
						.getExistingMediaId());
			}
			else if (associateReq.getExistingAccount().getAccountId() != null) {
				amReqTO.setAccountId(associateReq.getExistingAccount()
						.getAccountId());
			}
			else if (associateReq.getExistingAccount().getTicket() != null) {

				// temporary JAXB ticket object (short-cut object)
				Ticket jaxbTicket = associateReq.getExistingAccount()
						.getTicket();

				TicketTO ticketSearch = new TicketTO();

				ticketSearch.setTktItem(jaxbTicket.getTktItem());

				// MagCode (NGE signatures are strictly MagTrack1...track2 is not used/recognized)
				if (jaxbTicket.getTktID().getMag() != null) {
					ticketSearch.setMag(jaxbTicket.getTktID().getMag()
							.toString());
				}
				// Barcode
				else if (jaxbTicket.getTktID().getBarcode() != null) {
					ticketSearch.setBarCode(jaxbTicket.getTktID().getBarcode());
				}
				// TCod
				else if (jaxbTicket.getTktID().getTktNID() != null) {
					ticketSearch.setTktNID(jaxbTicket.getTktID().getTktNID());
				}
				// External
				else if (jaxbTicket.getTktID().getExternal() != null) {
					ticketSearch.setExternal(jaxbTicket.getTktID()
							.getExternal());
				}
				// DSSN
				else if (jaxbTicket.getTktID().getTktDSSN() != null) {

					// temporary JAXB ticket DSSN (short-cut object)
					TktDSSN jaxbDSSN = jaxbTicket.getTktID().getTktDSSN();
					GregorianCalendar tempCalendar = UtilXML
							.convertFromXML(jaxbDSSN.getTktDate());
					ticketSearch.setDssn(tempCalendar, jaxbDSSN.getTktSite(),
							jaxbDSSN.getTktStation(), jaxbDSSN.getTktNbr());

				}

				amReqTO.setTicket(ticketSearch);
			}

		}

		// **************************************************
		// MediaData

		// for each media in the incoming JAXB collection
		for (MediaData media : associateReq.getMediaData()) {

			// temporary media object
			NewMediaDataTO newMedia = new NewMediaDataTO();

			newMedia.setMediaId(media.getMediaId());
			newMedia.setMfrId(media.getMfrId());
			newMedia.setVisualId(media.getVisualId());

			mediaTOList.add(newMedia);
		}
		amReqTO.setMediaData(mediaTOList);

		// return the populated transfer object
		return amReqTO;

	}

	/**
	 * When passed the DTI Application object, return the JAXB object.
	 * 
	 * @param amRespTO
	 *            - The associate media response object (DTI Application object)
	 * @return the JAXB version of the QueryTicketResponse
	 * @throws JAXBException
	 *             - should any parsing errors occur.
	 * 
	 *             ---IMPORTANT---
	 * 
	 *             Please do not remove the fully-qualified object references {Ticket, TktDSSN, and MediaData}. Doing so will result in a JAXB collision with the respective "request" objects of the same name.
	 * 
	 */
	public static AssociateMediaToAccountResponse getJaxb(
			AssociateMediaToAccountResponseTO amRespTO, DTIErrorTO errorTO) throws JAXBException {

		// declarations for the JAXB object
		AssociateMediaToAccountResponse associateResponse = new AssociateMediaToAccountResponse();
		EntitlementAccount existingAccount = new EntitlementAccount();

		// **************************************************
		// AccountId
		if (amRespTO.getAccountId() != null) {
			existingAccount.setAccountId(amRespTO.getAccountId());
		}

		// **************************************************
		// TicketsOnAccount
		if (amRespTO.getTicketsOnAccount() != null) {
			existingAccount.setTicketsOnAccount(amRespTO.getTicketsOnAccount());
		}

		// **************************************************
		// MediaOnAccount
		if (amRespTO.getMediaOnAccount() != null) {
			existingAccount.setMediaOnAccount(amRespTO.getMediaOnAccount());
		}

		// **************************************************
		// Ticket

		// loop through transfer object ticket list
		for (TicketTO ticket : amRespTO.getTickets()) {

			pvt.disney.dti.gateway.response.xsd.Ticket jaxbTicket = new pvt.disney.dti.gateway.response.xsd.Ticket();

			// Ticket Item
			jaxbTicket.setTktItem(ticket.getTktItem());

			// Ticket Id object {Magcode, Barcode, TCod, External, DSSN object}
			TktID jaxbTicketId = new TktID();

			jaxbTicketId.setMag(ticket.getMagTrack1());
			jaxbTicketId.setBarcode(ticket.getBarCode());
			jaxbTicketId.setTktNID(ticket.getTktNID());
			jaxbTicketId.setExternal(ticket.getExternal());

			// DSSN object
			pvt.disney.dti.gateway.response.xsd.Ticket.TktID.TktDSSN jaxbDSSN = new pvt.disney.dti.gateway.response.xsd.Ticket.TktID.TktDSSN();

			jaxbDSSN.setTktSite(ticket.getDssnSite());
			jaxbDSSN.setTktStation(ticket.getDssnStation());
			jaxbDSSN.setTktNbr(ticket.getDssnNumber());

			XMLGregorianCalendar xgDSSN = UtilXML.convertToXML(ticket
					.getDssnDate());
			jaxbDSSN.setTktDate(xgDSSN);

			// set the DSSN and Ticket Id objects
			jaxbTicketId.setTktDSSN(jaxbDSSN);
			jaxbTicket.setTktID(jaxbTicketId);

			// add the populated JAXB ticket to the list associated to the MyMagic Account
			existingAccount.getTicket().add(jaxbTicket);
		}

		// **************************************************
		// MediaData

		// loop through transfer object media list
		for (NewMediaDataTO media : amRespTO.getMediaData()) {

			pvt.disney.dti.gateway.response.xsd.MediaData jaxbMedia = new pvt.disney.dti.gateway.response.xsd.MediaData();

			jaxbMedia.setMediaId(media.getMediaId());
			jaxbMedia.setMfrId(media.getMfrId());
			jaxbMedia.setVisualId(media.getVisualId());

			existingAccount.getMediaData().add(jaxbMedia);

		}

		// set the populated account object
		associateResponse.setExistingAccount(existingAccount);

		// Error

		// if an error exists, populate appropriately and set the object in the response
		if (errorTO != null) {

			if ((errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) || (errorTO
					.getErrorScope() == DTIErrorCode.ErrorScope.MEDIA)) {

				TktError tktError = new TktError();

				tktError.setTktErrorCode(errorTO.getErrorCode());
				tktError.setTktErrorType(errorTO.getErrorType());
				tktError.setTktErrorClass(errorTO.getErrorClass());
				tktError.setTktErrorText(errorTO.getErrorText());

				associateResponse.setTktError(tktError);
			}
		}

		// return the populated JAXB object
		return associateResponse;

	}
}