package pvt.disney.dti.gateway.test.util;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.request.xsd.CommandHeader;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader.Comm;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader.TktSeller;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket.TktID;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest;
import pvt.disney.dti.gateway.request.xsd.Transmission.Payload;
import pvt.disney.dti.gateway.request.xsd.Transmission.Payload.Command;

public class CommonUtil {
	
	protected static String CTXML_XML_PATH = "/xml/CreateTicket/";
	protected static String QTXML_XML_PATH = "/xml/QueryTicket/";
	protected static String RXML_XML_PATH ="/xml/Reservation/";
	protected static String UTXML_XML_PATH ="/xml/UpdateTicket/";
	protected static String UTRAXML_XML_PATH ="/xml/UpdateTransaction/";
	protected static String UAXML_XML_PATH ="/xml/UpgradeAlpha/";
	protected static String VTXML_XML_PATH ="/xml/VoidTicket/";
	
	public static Comm getComm() {
		Comm comm = new Comm();
		comm.setMethod("Network");
		comm.setProtocol("IP");
		return comm;
	}

	public static TktSeller getTktSeller() {
		TktSeller seller = new TktSeller();
		seller.setTSMAC("DA6626");
		seller.setTSLocation("001");
		seller.setTSSecurity("WaltDisney1");
		seller.setTSSystem("CC");
		return seller;
	}

	public static PayloadHeader getPayloadHeader()
			throws DatatypeConfigurationException {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		Date date=new Date();
		gregorianCalendar.setGregorianChange(date);
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory
				.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		PayloadHeader header = new PayloadHeader();
		header.setPayloadID("99999999999999999999");
		header.setTarget("PROD-WDW");
		header.setVersion("1.0");
		header.setComm(getComm());
		header.setTktSeller(getTktSeller());
		header.setTransmitDate(xmlGregorianCalendar);
		header.setTransmitTime("02:41:45.955");
		return header;
	}

	

	public static Command getCommand() throws DatatypeConfigurationException {
		Command command = new Command();
		CommandHeader commandHeader = new CommandHeader();
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.set(2017, 3, 29);
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory
				.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		commandHeader.setCmdItem(new BigInteger("1"));
		commandHeader.setCmdInvoice("2018895220000011272");
		commandHeader.setCmdDevice("PAULH");
		commandHeader.setCmdOperator("PAULH");
		commandHeader.setCmdActor("SYS");
		commandHeader.setCmdDate(xmlGregorianCalendar);
		commandHeader.setCmdTime("02:41:45.955");
		commandHeader.setCmdTimeout(new BigInteger("45"));
		command.setCommandHeader(commandHeader);
		return command;
	}

	public static Payload getCommonRequestPayload() throws DatatypeConfigurationException {
		Payload payload = new Payload();
		payload.setPayloadHeader(getPayloadHeader());
		payload.setCommand(getCommand());
		return payload;
	}

}
