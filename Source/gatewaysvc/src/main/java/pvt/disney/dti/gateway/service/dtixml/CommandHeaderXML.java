package pvt.disney.dti.gateway.service.dtixml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.request.xsd.CommandHeader;
import pvt.disney.dti.gateway.response.xsd.CommandHeader.CmdError;

/**
 * This class is responsible for transformations between the DTI TiXML and the internal objects used by the DTI Application. This class manages the CommandHeader clause.
 * 
 * @author lewit019
 * 
 */
public abstract class CommandHeaderXML {

	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param commandHeader
	 *            The JAXB version of the command header.
	 * @return the CommandHeaderTO object used by the application.
	 * @throws JAXBException
	 *             Should any parsing errors occur.
	 */
	public static CommandHeaderTO getTO(CommandHeader commandHeader) throws JAXBException {

		CommandHeaderTO commandHeaderTO = new CommandHeaderTO();

		commandHeaderTO.setCmdItem(commandHeader.getCmdItem());
		commandHeaderTO.setCmdTimeout(commandHeader.getCmdTimeout());

		// cmdDateTime
		XMLGregorianCalendar tXCalDate = commandHeader.getCmdDate();
		String timeString = commandHeader.getCmdTime();
		GregorianCalendar tempCalendar = UtilXML.formatXMLDateTime(tXCalDate,
				timeString);
		commandHeaderTO.setCmdDateTime(tempCalendar);

		// cmdInvoice
		commandHeaderTO.setCmdInvoice(commandHeader.getCmdInvoice());
		commandHeaderTO.setCmdDevice(commandHeader.getCmdDevice());
		commandHeaderTO.setCmdOperator(commandHeader.getCmdOperator());
		commandHeaderTO.setCmdActor(commandHeader.getCmdActor());

		commandHeaderTO.setCmdNote(commandHeader.getCmdNote());
		commandHeaderTO.setCmdMarket(commandHeader.getCmdMarket());

		List<CommandHeader.CmdAttribute> cmdAttrList = commandHeader
				.getCmdAttribute();
		ArrayList<CommandHeaderTO.CmdAttributeTO> cmdAttrListTO = commandHeaderTO
				.getCmdAttributeList();
		for /* each */(CommandHeader.CmdAttribute anAttribute : /* in */cmdAttrList) {
			CommandHeaderTO.CmdAttributeTO cmdAttrTO = commandHeaderTO.new CmdAttributeTO();
			cmdAttrTO.setAttribName(anAttribute.getAttribName());
			cmdAttrTO.setAttribValue(anAttribute.getAttribValue());
			cmdAttrListTO.add(cmdAttrTO);
		}

		return commandHeaderTO;

	}

	/**
	 * When passed the DTI application object, return the JAXB version.
	 * 
	 * @param commandHeaderTO
	 *            The DTI version of the command header.
	 * @return the CommandHeader JAX object.
	 * @throws JAXBException
	 *             Should any parsing error occur.
	 */
	public static pvt.disney.dti.gateway.response.xsd.CommandHeader getJaxb(
			CommandHeaderTO commandHeaderTO, DTIErrorTO errorTO) throws JAXBException {

		pvt.disney.dti.gateway.response.xsd.CommandHeader commandHeader = new pvt.disney.dti.gateway.response.xsd.CommandHeader();

		commandHeader.setCmdItem(commandHeaderTO.getCmdItem());
		commandHeader.setCmdDuration(commandHeaderTO.getCmdDuration());

		GregorianCalendar gCal = commandHeaderTO.getCmdDateTime();

		DatatypeFactory aFactory = null;
		try {
			aFactory = DatatypeFactory.newInstance();
		}
		catch (DatatypeConfigurationException dce) {
			throw new JAXBException("Cannot instantiate datatype factory.");
		}

		XMLGregorianCalendar xCalDate = aFactory.newXMLGregorianCalendarDate(
				gCal.get(GregorianCalendar.YEAR),
				gCal.get(GregorianCalendar.MONTH) + 1,
				gCal.get(GregorianCalendar.DAY_OF_MONTH),
				DatatypeConstants.FIELD_UNDEFINED);

		commandHeader.setCmdDate(xCalDate);

		// Because of ticket seller limitations, the time format must be HH:mm:ss.SS, but .SSS is
		// always generated. The string is therefore clipped at the end to make it acceptable.
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		String tempCalendarString = sdf.format(gCal.getTime());
		tempCalendarString = tempCalendarString.substring(0,
				(tempCalendarString.length() - 1));

		commandHeader.setCmdTime(tempCalendarString);

		if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.COMMAND)) {

			CmdError error = new CmdError();
			error.setCmdErrorCode(errorTO.getErrorCode());
			error.setCmdErrorType(errorTO.getErrorType());
			error.setCmdErrorClass(errorTO.getErrorClass());
			error.setCmdErrorText(errorTO.getErrorText());
			commandHeader.setCmdError(error);
		}

		return commandHeader;

	}
}
