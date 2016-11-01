package pvt.disney.dti.gateway.provider.wdw.xml;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Update Ticket Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 * 
 */
public class OTUpdateTicketXML {

	/**
	 * Adds the transacation body of update ticket to the command stanza when provided with the appropriate transfer object.
	 * 
	 * @param otUpdtTktTO
	 *            The Omni Ticket Update Ticket Transfer Object.
	 * @param commandStanza
	 *            The command stanza needing the update ticket stanza.
	 */
	static void addTxnBodyElement(OTUpdateTicketTO otUpdtTktTO,
			Element commandStanza) {

		// UpdateTicket
		Element updTktStanza = commandStanza.addElement("UpdateTicket");

		// WorkRules
		ArrayList<String> tagList = otUpdtTktTO.getTagsList();
		OTCommandXML.addWorkRules(updTktStanza, tagList);

		// SiteNumber
		updTktStanza.addElement("SiteNumber").addText(
				otUpdtTktTO.getSiteNumber().toString());

		// TicketInfo
		ArrayList<OTTicketInfoTO> tktList = otUpdtTktTO.getTktInfoList();
		for /* each */(OTTicketInfoTO otTicketInfo : /* in */tktList) {

			Element ticketInfoStanza = updTktStanza.addElement("TicketInfo");

			Element ticketStanza = ticketInfoStanza.addElement("Ticket");

			// Item
			ticketStanza.addElement("Item").addText(
					otTicketInfo.getItem().toString());

			// TicketSearchMode
			Element tktSrchModeStanza = ticketStanza
					.addElement("TicketSearchMode");

			OTTicketTO otTicket = otTicketInfo.getTicketSearchMode();
			ArrayList<TicketIDType> otTicketIdTypeList = otTicket
					.getTktIdentityTypes();
			TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

			switch (otTicketIdType) {

			case MAGCODE:
				tktSrchModeStanza.addElement("MagCode").addText(
						otTicket.getMagTrack());
				break;

			case TDSSN:
				Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");
				tdssnElement.addElement("Site")
						.addText(otTicket.getTdssnSite());
				tdssnElement.addElement("Station").addText(
						otTicket.getTdssnStation());
				GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnElement.addElement("Date").addText(tdssnDate);
				tdssnElement.addElement("TicketId").addText(
						otTicket.getTdssnTicketId());
				break;

			case TCOD:
				tktSrchModeStanza.addElement("TCOD")
						.addText(otTicket.getTCOD());
				break;

			case BARCODE:
				tktSrchModeStanza.addElement("BarCode").addText(
						otTicket.getBarCode());
				break;

			case EXTERNALTICKETCODE:
				tktSrchModeStanza.addElement("ExternalTicketCode").addText(
						otTicket.getExternalTicketCode());
				break;
			}

			// Validity
			if ((otTicketInfo.getValidityStartDate() != null) || (otTicketInfo
					.getValidityEndDate() != null)) {
				Element validityElement = ticketInfoStanza
						.addElement("Validity");

				if (otTicketInfo.getValidityStartDate() != null) {
					GregorianCalendar startDateCal = otTicketInfo
							.getValidityStartDate();
					String startDateString = CustomDataTypeConverter
							.printCalToNexusDateFormat(startDateCal);
					validityElement.addElement("StartDate").addText(
							startDateString);
				}

				if (otTicketInfo.getValidityEndDate() != null) {
					GregorianCalendar endDateCal = otTicketInfo
							.getValidityEndDate();
					String endDateString = CustomDataTypeConverter
							.printCalToNexusDateFormat(endDateCal);
					validityElement.addElement("EndDate")
							.addText(endDateString);
				}

			}

			// TktNote
			if (otTicketInfo.getTicketNote() != null) {
				ticketInfoStanza.addElement("TicketNote").addText(
						otTicketInfo.getTicketNote());
			}

			// VoidCode
			if (otTicketInfo.getVoidCode() != null) {
				ticketInfoStanza.addElement("VoidCode").addText(
						otTicketInfo.getVoidCode().toString());
			}

			// TicketAttribute
			if (otTicketInfo.getTicketAttribute() != null) {
				ticketInfoStanza.addElement("TicketAttribute").addText(
						otTicketInfo.getTicketAttribute().toString());
			}

			// ZipCode
			if (otTicketInfo.getZipCode() != null) {
				ticketInfoStanza.addElement("ZipCode").addText(
						otTicketInfo.getZipCode());
			}

			// BiometricLevel
			if (otTicketInfo.getBiometricLevel() != null) {
				ticketInfoStanza.addElement("BiometricLevel").addText(
						otTicketInfo.getBiometricLevel().toString());
			}

		} // ticketInfo loop

		return;
	}

	/**
	 * When provided with the parsed XML, creates the transfer object for the update ticket.
	 * 
	 * @param updtTktResStanza
	 *            the parsed XML containing the update ticket response.
	 * @return The Omni Ticket Update Ticket Transfer Object.
	 * @throws DTIException
	 *             for any missing required fields or other parsing errors.
	 */
	static OTUpdateTicketTO getTO(Node updtTktNode) throws DTIException {

		OTUpdateTicketTO otUpdtTktTO = new OTUpdateTicketTO();

		OTErrorTO otErrorTO = OTCommandXML.getErrorTO(updtTktNode);

		// Set error object
		otUpdtTktTO.setError(otErrorTO);

		// UpdTicketInfo (ignored)

		return otUpdtTktTO;
	}

}
