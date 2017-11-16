package pvt.disney.dti.gateway.service.dtixml;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.TickerateEntitlementRequestTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.request.xsd.TickerateEntitlementRequest;
import pvt.disney.dti.gateway.request.xsd.Ticket;
import pvt.disney.dti.gateway.request.xsd.Ticket.TktID.TktDSSN;
import pvt.disney.dti.gateway.request.xsd.MediaData;
import pvt.disney.dti.gateway.response.xsd.EntitlementAccount;
import pvt.disney.dti.gateway.response.xsd.TickerateEntitlementResponse;
import pvt.disney.dti.gateway.response.xsd.TktError;
import pvt.disney.dti.gateway.response.xsd.Ticket.TktID;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author biest001
 * @since 2.16.1
 * 
 */
public abstract class TickerateEntitlementXML {

	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param teReq
	 *            - the JAXB version of the tickerate entitlement request object
	 * @return the DTI application object
	 * @throws JAXBException
	 *             - should any parsing errors occur.
	 */
	public static TickerateEntitlementRequestTO getTO(
			TickerateEntitlementRequest tickerateReq) throws JAXBException {

		// declarations for the transfer object (TO)
		TickerateEntitlementRequestTO teReqTO = new TickerateEntitlementRequestTO();
		ArrayList<TicketTO> ticketTOList = new ArrayList<TicketTO>();
		ArrayList<NewMediaDataTO> mediaTOList = new ArrayList<NewMediaDataTO>();

		// **************************************************
		// Tickets (Standard Entitlements to be made Electronic)

		// for each ticket in the incoming JAXB collection
		for (Ticket ticket : tickerateReq.getTicket()) {

			TicketTO legacyTicket = new TicketTO();

			// TktItem
			legacyTicket.setTktItem(ticket.getTktItem());

			// Magcode (MagTrack1 only... NGE signatures don't use track2)
			if (ticket.getTktID().getMag() != null) {
				legacyTicket.setMag(ticket.getTktID().getMag().toString());
			}
			// BarCode
			else if (ticket.getTktID().getBarcode() != null) {
				legacyTicket.setBarCode(ticket.getTktID().getBarcode());
			}
			// TCod
			else if (ticket.getTktID().getTktNID() != null) {
				legacyTicket.setTktNID(ticket.getTktID().getTktNID());
			}
			// External
			else if (ticket.getTktID().getExternal() != null) {
				legacyTicket.setExternal(ticket.getTktID().getExternal());
			}
			// DSSN
			else if (ticket.getTktID().getTktDSSN() != null) {

				// temporary JAXB ticket DSSN (short-cut object)
				TktDSSN jaxbDSSN = ticket.getTktID().getTktDSSN();
				GregorianCalendar tempCalendar = UtilXML
						.convertFromXML(jaxbDSSN.getTktDate());
				legacyTicket.setDssn(tempCalendar, jaxbDSSN.getTktSite(),
						jaxbDSSN.getTktStation(), jaxbDSSN.getTktNbr());

			}

			ticketTOList.add(legacyTicket);

		}
		teReqTO.setTickets(ticketTOList);

		// **************************************************
		// New Account
		if (tickerateReq.getNewAccount() != null) {

			// External Reference Type
			if (tickerateReq.getNewAccount().getExternalReferenceType() != null) {
				teReqTO.setNewExternalReferenceType(tickerateReq
						.getNewAccount().getExternalReferenceType());
			}

			// External Reference Value
			if (tickerateReq.getNewAccount().getExternalReferenceValue() != null) {
				teReqTO.setNewExternalReferenceValue(tickerateReq
						.getNewAccount().getExternalReferenceValue());
			}
		}

		// **************************************************
		// ExistingAccount
		if (tickerateReq.getExistingAccount() != null) {

			// What search criteria was selected? Existing Media Id, Account Id, or Ticket Identifier??
			if (tickerateReq.getExistingAccount().getExistingMediaId() != null) {
				teReqTO.setExistingMediaId(tickerateReq.getExistingAccount()
						.getExistingMediaId());
			}
			else if (tickerateReq.getExistingAccount().getAccountId() != null) {
				teReqTO.setAccountId(tickerateReq.getExistingAccount()
						.getAccountId());
			}
			else if (tickerateReq.getExistingAccount().getTicket() != null) {

				// temporary JAXB ticket object (short-cut object)
				Ticket jaxbTicket = tickerateReq.getExistingAccount()
						.getTicket();

				TicketTO ticketSearch = new TicketTO();

				ticketSearch.setTktItem(jaxbTicket.getTktItem());

				// FYI... NGE signatures are strictly MagTrack1...track2 is not used/recognized
				if (jaxbTicket.getTktID().getMag() != null) {
					ticketSearch.setMag(jaxbTicket.getTktID().getMag()
							.toString());
				}
				else if (jaxbTicket.getTktID().getBarcode() != null) {
					ticketSearch.setBarCode(jaxbTicket.getTktID().getBarcode());
				}
				else if (jaxbTicket.getTktID().getTktNID() != null) {
					ticketSearch.setTktNID(jaxbTicket.getTktID().getTktNID());
				}
				else if (jaxbTicket.getTktID().getExternal() != null) {
					ticketSearch.setExternal(jaxbTicket.getTktID()
							.getExternal());
				}
				else if (jaxbTicket.getTktID().getTktDSSN() != null) {

					// temporary JAXB ticket DSSN (short-cut object)
					TktDSSN jaxbDSSN = jaxbTicket.getTktID().getTktDSSN();

					XMLGregorianCalendar tXCal = jaxbDSSN.getTktDate();
					GregorianCalendar tempCalendar = new GregorianCalendar(
							tXCal.getEonAndYear().intValue(),
							(tXCal.getMonth() - 1), tXCal.getDay());
					ticketSearch.setDssn(tempCalendar, jaxbDSSN.getTktSite(),
							jaxbDSSN.getTktStation(), jaxbDSSN.getTktNbr());

				}

				teReqTO.setTicket(ticketSearch);
			}

		}

		// **************************************************
		// MediaData

		// for each media in the incoming JAXB collection
		for (MediaData media : tickerateReq.getNewMediaData()) {

			// temporary media object
			NewMediaDataTO newMedia = new NewMediaDataTO();

			newMedia.setMediaId(media.getMediaId());
			newMedia.setMfrId(media.getMfrId());
			newMedia.setVisualId(media.getVisualId());

			mediaTOList.add(newMedia);
		}
		teReqTO.setMediaData(mediaTOList);

		// return the populated transfer object
		return teReqTO;

	}

	/**
	 * When passed the DTI Application object, return the JAXB object.
	 * 
	 * @param teRespTO
	 *            - The tickerate entitlement response object (DTI Application object)
	 * @return the JAXB version of the QueryTicketResponse
	 * @throws JAXBException
	 *             - should any parsing errors occur.
	 * 
	 *             ---IMPORTANT---
	 * 
	 *             Please do not remove the fully-qualified object references {Ticket, TktDSSN, and MediaData}. Doing so will result in a JAXB collision with the respective "request" objects of the same name.
	 * 
	 */
	public static TickerateEntitlementResponse getJaxb(
			TickerateEntitlementResponseTO teRespTO, DTIErrorTO errorTO) throws JAXBException {

		// declarations for the JAXB object
		TickerateEntitlementResponse tickerateResponse = new TickerateEntitlementResponse();
		EntitlementAccount newAccount = new EntitlementAccount();

		// **************************************************
		// AccountId
		if (teRespTO.getAccountId() != null) {
			newAccount.setAccountId(teRespTO.getAccountId());
		}

		// **************************************************
		// TicketsOnAccount
		if (teRespTO.getTicketsOnAccount() != null) {
			newAccount.setTicketsOnAccount(teRespTO.getTicketsOnAccount());
		}

		// **************************************************
		// MediaOnAccount
		if (teRespTO.getMediaOnAccount() != null) {
			newAccount.setMediaOnAccount(teRespTO.getMediaOnAccount());
		}

		// **************************************************
		// Ticket

		// loop through transfer object ticket list
		for (TicketTO ticket : teRespTO.getTickets()) {

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
			newAccount.getTicket().add(jaxbTicket);
		}

		// **************************************************
		// MediaData

		// loop through transfer object media list
		for (NewMediaDataTO media : teRespTO.getMediaData()) {

			pvt.disney.dti.gateway.response.xsd.MediaData jaxbMedia = new pvt.disney.dti.gateway.response.xsd.MediaData();

			jaxbMedia.setMediaId(media.getMediaId());
			jaxbMedia.setMfrId(media.getMfrId());
			jaxbMedia.setVisualId(media.getVisualId());

			newAccount.getMediaData().add(jaxbMedia);

		}

		// set the populated account object
		tickerateResponse.setNewAccount(newAccount);

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

				tickerateResponse.setTktError(tktError);
			}
		}

		// return the populated JAXB object
		return tickerateResponse;

	}
}