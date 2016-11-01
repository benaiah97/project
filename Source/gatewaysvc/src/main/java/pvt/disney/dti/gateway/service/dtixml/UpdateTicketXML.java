package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.UpdateTicketResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.request.xsd.UpdateTicketRequest;
import pvt.disney.dti.gateway.response.xsd.UpdateTicketResponse;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public class UpdateTicketXML {

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param utktReq
	 *            the parsed JAXB structure.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	public static UpdateTicketRequestTO getTO(UpdateTicketRequest utktReq) throws JAXBException {

		UpdateTicketRequestTO uTktReqTO = new UpdateTicketRequestTO();

		// Ticket
		List<UpdateTicketRequest.Ticket> ticketList = utktReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(UpdateTicketRequest.Ticket aTicket : /* in */ticketList) {
			setTOTicketList(ticketListTO, aTicket);
		}
		uTktReqTO.setTicketList(ticketListTO);

		return uTktReqTO;
	}

	/**
	 * Gets the jaxb structure from the response transfer object.
	 * 
	 * @param utktRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated jaxb object
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	public static UpdateTicketResponse getJaxb(
			UpdateTicketResponseTO utktRespTO, DTIErrorTO errorTO) throws JAXBException {

		UpdateTicketResponse uTktResp = new UpdateTicketResponse();
		List<UpdateTicketResponse.Ticket> tktList = uTktResp.getTicket();

		ArrayList<TicketTO> ticketListTO = utktRespTO.getTicketList();

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			UpdateTicketResponse.Ticket aTicket = new UpdateTicketResponse.Ticket();

			// Required fields
			aTicket.setTktItem(aTicketTO.getTktItem().toString());

			// Ticket Error
			if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

				UpdateTicketResponse.Ticket.TktError tktError = new UpdateTicketResponse.Ticket.TktError();
				tktError.setTktErrorCode(errorTO.getErrorCode());
				tktError.setTktErrorType(errorTO.getErrorType());
				tktError.setTktErrorClass(errorTO.getErrorClass());
				tktError.setTktErrorText(errorTO.getErrorText());

				aTicket.setTktError(tktError);
			}

			tktList.add(aTicket);
		}

		return uTktResp;
	}

	/**
	 * Sets the transfer object ticket list.
	 * 
	 * @param ticketListTO
	 *            the transfer object ticket list.
	 * @param aTicket
	 *            a JAXB parsed ticket.
	 */
	private static void setTOTicketList(ArrayList<TicketTO> ticketListTO,
			UpdateTicketRequest.Ticket aTicket) {
		TicketTO aTicketTO = new TicketTO();

		// Required items
		List<JAXBElement<?>> stanzaList = aTicket
				.getTktItemOrTktIDOrTktStatus();

		for /* each */(JAXBElement<?> stanza : /* in */stanzaList) {

			// TktItem
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktItem")) {
				aTicketTO.setTktItem((BigInteger) stanza.getValue());
			}

			// TktID
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktID")) {
				UpdateTicketRequest.Ticket.TktID tktID = (UpdateTicketRequest.Ticket.TktID) stanza
						.getValue();

				/*
				 * According to XML Specification, only one ticket identity is to be passed. If multiple or present, then accept the first one in this order: barcode, external, nid, mag, dssn
				 */
				if (tktID.getBarcode() != null) {
					aTicketTO.setBarCode(tktID.getBarcode());
				}
				else {
					if (tktID.getExternal() != null) {
						aTicketTO.setExternal(tktID.getExternal());
					}
					else {
						if (tktID.getTktNID() != null) {
							aTicketTO.setTktNID(tktID.getTktNID());
						}
						else {
							if (tktID.getMag() != null) {
								UpdateTicketRequest.Ticket.TktID.Mag tktMag = tktID
										.getMag();
								String mag1 = tktMag.getMagTrack1();
								String mag2 = tktMag.getMagTrack2();

								if (mag2 != null) {
									aTicketTO.setMag(mag1, mag2);
								}
								else {
									aTicketTO.setMag(mag1);
								}
							}
							else {
								if (tktID.getTktDSSN() != null) {
									UpdateTicketRequest.Ticket.TktID.TktDSSN tktDssn = tktID
											.getTktDSSN();
									XMLGregorianCalendar tXCal = tktDssn
											.getTktDate();
									GregorianCalendar tempCalendar = new GregorianCalendar(
											tXCal.getEonAndYear().intValue(),
											tXCal.getMonth() - 1,
											tXCal.getDay());
									aTicketTO.setDssn(tempCalendar,
											tktDssn.getTktSite(),
											tktDssn.getTktStation(),
											tktDssn.getTktNbr());
								}
							}
						}
					}
				}
			} // TktID

			// TktStatus
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktStatus")) {
				ArrayList<TktStatusTO> tktStatusListTO = aTicketTO
						.getTktStatusList();
				UpdateTicketRequest.Ticket.TktStatus aTktStatus = (UpdateTicketRequest.Ticket.TktStatus) stanza
						.getValue();
				TicketTO.TktStatusTO aTktStatusTO = aTicketTO.new TktStatusTO();
				aTktStatusTO.setStatusItem(aTktStatus.getStatusItem());
				aTktStatusTO.setStatusValue(aTktStatus.getStatusValue());
				tktStatusListTO.add(aTktStatusTO);
			}

			// TktValidity
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktValidity")) {
				UpdateTicketRequest.Ticket.TktValidity aTktValidity = (UpdateTicketRequest.Ticket.TktValidity) stanza
						.getValue();

				List<JAXBElement<XMLGregorianCalendar>> aValidityList = aTktValidity
						.getValidStartOrValidEnd();

				for /* each */(JAXBElement<XMLGregorianCalendar> aValidityDate : /* in */aValidityList) {

					QName validityFieldName = aValidityDate.getName();

					if (validityFieldName.getLocalPart().equalsIgnoreCase(
							"ValidStart")) {
						if (aValidityDate.getValue() != null) {

							XMLGregorianCalendar tXCal = aValidityDate
									.getValue();
							GregorianCalendar tempCalendar = new GregorianCalendar(
									tXCal.getEonAndYear().intValue(),
									tXCal.getMonth() - 1, tXCal.getDay());
							aTicketTO.setTktValidityValidStart(tempCalendar);

						}
					}

					if (validityFieldName.getLocalPart().equalsIgnoreCase(
							"ValidEnd")) {
						if (aValidityDate.getValue() != null) {
							XMLGregorianCalendar tXCal = aValidityDate
									.getValue();
							GregorianCalendar tempCalendar = new GregorianCalendar(
									tXCal.getEonAndYear().intValue(),
									tXCal.getMonth() - 1, tXCal.getDay());
							aTicketTO.setTktValidityValidEnd(tempCalendar);
						}
					}

				}
			} // TktValidity

			// TktSecurity
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktSecurity")) {

				UpdateTicketRequest.Ticket.TktSecurity tktSecurity = (UpdateTicketRequest.Ticket.TktSecurity) stanza
						.getValue();

				List<JAXBElement<String>> aSecurityList = (List<JAXBElement<String>>) tktSecurity
						.getTypeOrLevelOrTemplate();

				for /* each */(JAXBElement<String> aSecurityEntry : /* in */aSecurityList) {
					QName fieldName = aSecurityEntry.getName();

					if (fieldName.getLocalPart().equals("Level")) {
						aTicketTO.setTktSecurityLevel((String) aSecurityEntry
								.getValue());
					}
				}
			} // TktSecurity

			// TktMarket
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktMarket")) {
				aTicketTO.setTktMarket((String) stanza.getValue());
			}

			// TktNote
			if (stanza.getName().getLocalPart().equalsIgnoreCase("TktNote")) {
				aTicketTO.setTktNote((String) stanza.getValue());
			}

		} // for

		ticketListTO.add(aTicketTO);

		return;
	}

}
