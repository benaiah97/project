package pvt.disney.dti.gateway.service.dtixml;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.DBProductTO.GuestType;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.UpgradeEligibilityStatusType;
import pvt.disney.dti.gateway.request.xsd.CommandHeader;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader.Comm;
import pvt.disney.dti.gateway.request.xsd.PayloadHeader.TktSeller;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket.TktID;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket.TktID.Mag;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest.Ticket.TktID.TktDSSN;
import pvt.disney.dti.gateway.request.xsd.Transmission;
import pvt.disney.dti.gateway.request.xsd.Transmission.Payload;
import pvt.disney.dti.gateway.request.xsd.Transmission.Payload.Command;
import pvt.disney.dti.gateway.response.xsd.QueryEligibleProductsResponse;

/**
 * @author AGARS017
 *JUnit TestCases For TransmissionRqstXML with respect to TransactionType-QUERYELIGIBLEPRODUCTS
 */
public class QueryEligibleProductsTestCase {
	
	/**
	 * JUnit TestCase for getDtiTransactionTo with respect to TransactionType-QUERYELIGIBLEPRODUCTS
	 * @throws DatatypeConfigurationException
	 */
	@Test
	public void testQueryEligibleProductsGetDtiTransactionTo() throws DatatypeConfigurationException
	{
		TransactionType requestType=TransactionType.QUERYELIGIBLEPRODUCTS;
		String tktBroker= new String(DTITestUtil.TKTBROKER);
		Transmission jaxbReq=new Transmission();
		Comm comm=new Comm();
		QueryEligibleProductsRequest queryEligibleProductReqest=new QueryEligibleProductsRequest();
		Ticket tkt=new Ticket();
		TktID tktID=new TktID();
		Mag mag=new Mag();
		TktDSSN tktDSSN=new TktDSSN();
		comm.setMethod("Network");
		comm.setProtocol("IP");
		PayloadHeader header=new PayloadHeader();
		header.setPayloadID("99920170220144000002");
		header.setTarget("Test-WDW");
		header.setVersion("1.0");
		header.setComm(comm);
		GregorianCalendar gregorianCalendar=new GregorianCalendar();
		gregorianCalendar.set(2017, 3, 29);
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		header.setTransmitDate(xmlGregorianCalendar);
		header.setTransmitTime("02:41:45.955");
		TktSeller seller=new TktSeller();
		seller.setTSMAC("DA6626");
		seller.setTSLocation("001");
		seller.setTSSecurity("WaltDisney1");
		seller.setTSSystem("CC");
		header.setTktSeller(seller);
		Payload payload=new Payload();
		payload.setPayloadHeader(header);
		Command command=new Command();
		CommandHeader commandHeader=new CommandHeader();
		commandHeader.setCmdItem(new BigInteger("1"));
		commandHeader.setCmdInvoice("2018895220000011272");
		commandHeader.setCmdDevice("PAULH");
		commandHeader.setCmdOperator("PAULH");
		commandHeader.setCmdActor("SYS");
		commandHeader.setCmdDate(xmlGregorianCalendar);
		commandHeader.setCmdTime("02:41:45.955");
		commandHeader.setCmdTimeout(new BigInteger("45"));
		command.setCommandHeader(commandHeader);
		tktID.setBarcode("54544");
		tkt.setTktID(tktID);
		tkt.setSaleType("Saletype");
		queryEligibleProductReqest.getTicket().add(tkt);
		command.setQueryEligibleProductsRequest(queryEligibleProductReqest);
		payload.setCommand(command);
		jaxbReq.setPayload(payload);
		/*Scenario 1::TktID with BarCode*/
		try {
			DTITransactionTO dTITransactionTO=TransmissionRqstXML.getDtiTransactionTo(requestType, tktBroker, jaxbReq);
			assertNotNull(dTITransactionTO);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured");
		}
		/*Scenario 2::TktID with External*/
		tktID.setBarcode(null);
		tktID.setExternal("544546");
		try {
			DTITransactionTO dTITransactionTO=TransmissionRqstXML.getDtiTransactionTo(requestType, tktBroker, jaxbReq);
			assertNotNull(dTITransactionTO);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured");
		}
		/*Scenario 3::TktID with TktNID*/
		tktID.setExternal(null);
		tktID.setTktNID("245645");
		try {
			DTITransactionTO dTITransactionTO=TransmissionRqstXML.getDtiTransactionTo(requestType, tktBroker, jaxbReq);
			assertNotNull(dTITransactionTO);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured");
		}
	/*	Scenario 4::TktID with Mag */
		tktID.setTktNID(null);
		tktID.setMag(mag);
		try {
			DTITransactionTO dTITransactionTO=TransmissionRqstXML.getDtiTransactionTo(requestType, tktBroker, jaxbReq);
			assertNotNull(dTITransactionTO);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured");
		}
		/*	Scenario 5::TktID with TktDSSN() */
		tktID.setMag(null);
		tktDSSN.setTktDate(xmlGregorianCalendar);
		tktDSSN.setTktNbr("545");
		tktDSSN.setTktSite("site");
		tktDSSN.setTktStation("station");
		tktID.setTktDSSN(tktDSSN);
		try {
			DTITransactionTO dTITransactionTO=TransmissionRqstXML.getDtiTransactionTo(requestType, tktBroker, jaxbReq);
			assertNotNull(dTITransactionTO);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured");
		}
	}
	
	/**
	 * TestCase for getJaxb
	 */
	@Test
	public void testGetJaxb() {
		QueryEligibilityProductsResponseTO qryResRespTO = new QueryEligibilityProductsResponseTO();
		QueryEligibleProductsResponse queryEligibleProductsResponse;
		BigInteger errorCodeIn = new BigInteger("52");
		String errorClassIn = "errorClassIn";
		String errorTextIn = "errorTextIn";
		String errorTypeIn = "errorTypeIn";
		DTIErrorTO errorTO = new DTIErrorTO(errorCodeIn, errorClassIn,
				errorTextIn, errorTypeIn);
		TicketTO ticketTO = new TicketTO();
		GregorianCalendar gregorianCalendar=new GregorianCalendar();
		gregorianCalendar.set(2017, 3, 29);
		XMLGregorianCalendar xmlGregorianCalendar;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e2) {
			Assert.fail(e2.getMessage());
		}
		DemographicsTO demographicsTO = new DemographicsTO();
		EligibleProductsTO eligibleProductsTO=new EligibleProductsTO();
		ArrayList<EligibleProductsTO> eligibleProducts=new ArrayList<>();
		demographicsTO.setName("name");
		demographicsTO.setFirstName("firstName");
		demographicsTO.setLastName("lastName");
		demographicsTO.setAddr1("addr1");
		demographicsTO.setAddr2("addr2");
		demographicsTO.setCity("city");
		demographicsTO.setState("state");
		demographicsTO.setCountry("country");
		demographicsTO.setZip("225445");
		demographicsTO.setGender(GenderType.MALE);
		demographicsTO.setTelephone("454546");
		demographicsTO.setEmail("email@domain.com");
		GregorianCalendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.set(2013, 2, 14);
		demographicsTO.setDateOfBirth(dateOfBirth);
		ArrayList<DemographicsTO> ticketDemoList = new ArrayList<>();
		ticketDemoList.add(demographicsTO);
		ticketTO.setTicketDemoList(ticketDemoList);
		ticketTO.getTicketTypes().add(TicketIdType.DSSN_ID);
			try {
			ticketTO.setDssn("2012-12-10", "site", "station", "5");
		} catch (java.text.ParseException e1) {
			Assert.fail(e1.getMessage());
		}
			eligibleProductsTO.setProdCode("1214");
			eligibleProductsTO.setProdPrice("535.15");
			eligibleProductsTO.setProdTax(new BigDecimal("12.00"));
			eligibleProductsTO.setUpgrdPrice("720.23");
			eligibleProductsTO.setUpgrdTax("27.23");
			//eligibleProductsTO.setValidEnd(xmlGregorianCalendar);
		eligibleProducts.add(eligibleProductsTO);
		ticketTO.setEligibleProducts(eligibleProducts);
		ticketTO.getTicketTypes().add(TicketIdType.TKTNID_ID);
		ticketTO.setTktNID("TktNID");
		ticketTO.getTicketTypes().add(TicketIdType.EXTERNAL_ID);
		ticketTO.setExternal("External");
		ticketTO.setTktPrice(new BigDecimal("111255.002"));
		ticketTO.setTktTax(new BigDecimal("5.25"));
		ticketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.ELIGIBLE);
		ticketTO.setGuestType(DBProductTO.GuestType.ADULT);
		ticketTO.setProdCode("prodCode");
		ticketTO.setUpgrdPrice(new BigDecimal("254543.245"));
		ticketTO.setProdPrice(new BigDecimal("2121414.5454"));
		GregorianCalendar tktValidityValidStart = new GregorianCalendar();
		tktValidityValidStart.set(2017, 2, 14);
		ticketTO.setTktValidityValidStart(tktValidityValidStart);
		GregorianCalendar tktValidityValidEnd = new GregorianCalendar();
		tktValidityValidEnd.set(2017, 12, 31);
		ticketTO.setTktValidityValidEnd(tktValidityValidEnd);
		TicketTO.TktStatusTO newStatus = ticketTO.new TktStatusTO();
		newStatus.setStatusItem("statusItem");
		newStatus.setStatusValue("statusValue");
		ticketTO.addTicketStatus(newStatus);
		ticketTO.setTktItem(new BigInteger("1"));
		qryResRespTO.getTicketList().add(ticketTO);
		/*Scenario 1 :: Guesttype= ADULT*/
		try {
			 queryEligibleProductsResponse	=QueryEligibleProductsXML.getJaxb(qryResRespTO, errorTO);
			assertNotNull(queryEligibleProductsResponse);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
		/*Scenario 2 :: Guesttype= Child*/
		ticketTO.setGuestType(DBProductTO.GuestType.CHILD);
		try {
			 queryEligibleProductsResponse	=QueryEligibleProductsXML.getJaxb(qryResRespTO, errorTO);
			assertNotNull(queryEligibleProductsResponse);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
		/*Scenario 3 :: Guesttype= Any*/
		ticketTO.setGuestType(DBProductTO.GuestType.ANY);
		try {
			 queryEligibleProductsResponse	=QueryEligibleProductsXML.getJaxb(qryResRespTO, errorTO);
			assertNotNull(queryEligibleProductsResponse);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
		/*Scenario 4 :: ResultType= NOPRODUCTS*/
		ticketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);
		try {
			 queryEligibleProductsResponse	=QueryEligibleProductsXML.getJaxb(qryResRespTO, errorTO);
			assertNotNull(queryEligibleProductsResponse);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
		/*Scenario 5 :: ResultType= INELIGIBLE*/
		ticketTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);
		try {
			 queryEligibleProductsResponse	=QueryEligibleProductsXML.getJaxb(qryResRespTO, errorTO);
			assertNotNull(queryEligibleProductsResponse);
		} catch (JAXBException e) {
			Assert.fail("UnExpected Exception Occured"+e.getMessage());
		}
	}
	
}
