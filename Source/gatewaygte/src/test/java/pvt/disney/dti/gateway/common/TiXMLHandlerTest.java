package pvt.disney.dti.gateway.common;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;

/**
 * Test class for TiXMLHandler.
 * 
 * @author lewit019
 * 
 */
public class TiXMLHandlerTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public final void testTiXMLHandler() {
  }

  @Test
  public final void testParseDocQueryTicket() {

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/QueryTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load QueryTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String,Object> map = TiXMLHandler.parseDoc(doc);

    // Check PayloadID
    String payloadID = (String) map.get(TiXMLHandler.PAYLOAD_ID);
    if (payloadID == null)
      fail("No PayloadID found in map for QueryTicket.");
    if (!payloadID.equals("35100016830274230440"))
      fail("Invalid PayloadID value (" + payloadID + ") found in map for QueryTicket.");

    // Check TSMAC
    String tsmac = (String) map.get(TiXMLHandler.TS_MAC);
    if (tsmac == null)
      fail("No TSMAC found in map for QueryTicket.");
    if (!tsmac.equals("WDPRONA"))
      fail("Invalid TSMAC value (" + tsmac + ") found in map for QueryTicket.");

    // Check TS_LOCATION
    String tsLocation = (String) map.get(TiXMLHandler.TS_LOCATION);
    if (tsLocation == null)
      fail("No TSLocation found in map for QueryTicket.");
    if (!tsLocation.equals("352"))
      fail("Invalid TSLocation value (" + tsLocation + ") found in map for QueryTicket.");

    // Check TS_ENVIRONMENT
    String target = (String) map.get(TiXMLHandler.TS_ENVIRONMENT);
    if (target == null)
      fail("No Target found in map for QueryTicket.");
    if (!target.equals("Test"))
      fail("Invalid Target value (" + target + ") found in map for QueryTicket.");

    // Check TS_VERSION
    String tsVersion = (String) map.get(TiXMLHandler.TS_VERSION);
    if (tsVersion == null)
      fail("No Version found in map for QueryTicket.");
    if (!tsVersion.equals("1.0"))
      fail("Invalid Version value (" + tsVersion + ") found in map for QueryTicket.");

    // Check ACTION
    String action = (String) map.get(TiXMLHandler.ACTION);
    if (action == null)
      fail("No transaction type found in map for QueryTicket.");
    if (!action.equals("QueryTicket            "))
      fail("Invalid transaction type value (" + action + ") found in map for QueryTicket.");

    // Check Transaction Type
    TransactionType txnType = (TransactionType) map.get(TiXMLHandler.TXN_TYPE);
    if (txnType == null)
      fail("No transaction type enumeration found in map for QueryTicket.");
    if (txnType != TransactionType.QUERYTICKET)
      fail("Invalid transaction type enumeration found in map for QueryTicket.");

    // Check Tickets
    String tickets = (String) map.get(TiXMLHandler.TICKETS);
    if (tickets == null)
      fail("No tickets found in map for QueryTicket.");
    if (!tickets.equals("2004-05-04XMK00711"))
      fail("Invalid tickets found in map for QueryTicket.");

  }

  @Test
  public final void testParseDocVoidTicket() {

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/VoidTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load VoidTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String,Object> map = TiXMLHandler.parseDoc(doc);

    // Check PayloadID
    String payloadID = (String) map.get(TiXMLHandler.PAYLOAD_ID);
    if (payloadID == null)
      fail("No PayloadID found in map for VoidTicket.");
    if (!payloadID.equals("999200809011415001"))
      fail("Invalid PayloadID value (" + payloadID + ") found in map for VoidTicket.");

    // Check TSMAC
    String tsmac = (String) map.get(TiXMLHandler.TS_MAC);
    if (tsmac == null)
      fail("No TSMAC found in map for VoidTicket.");
    if (!tsmac.equals("DS6469P"))
      fail("Invalid TSMAC value (" + tsmac + ") found in map for VoidTicket.");

    // Check TS_LOCATION
    String tsLocation = (String) map.get(TiXMLHandler.TS_LOCATION);
    if (tsLocation == null)
      fail("No TSLocation found in map for VoidTicket.");
    if (!tsLocation.equals("44008"))
      fail("Invalid TSLocation value (" + tsLocation + ") found in map for VoidTicket.");

    // Check TS_ENVIRONMENT
    String target = (String) map.get(TiXMLHandler.TS_ENVIRONMENT);
    if (target == null)
      fail("No Target found in map for VoidTicket.");
    if (!target.equals("Test-WDW"))
      fail("Invalid Target value (" + target + ") found in map for VoidTicket.");

    // Check TS_VERSION
    String tsVersion = (String) map.get(TiXMLHandler.TS_VERSION);
    if (tsVersion == null)
      fail("No Version found in map for VoidTicket.");
    if (!tsVersion.equals("1.0"))
      fail("Invalid Version value (" + tsVersion + ") found in map for VoidTicket.");

    // Check ACTION
    String action = (String) map.get(TiXMLHandler.ACTION);
    if (action == null)
      fail("No transaction type found in map for VoidTicket.");
    if (!action.equals("VoidTicket             "))
      fail("Invalid transaction type value (" + action + ") found in map for VoidTicket.");

    // Check Transaction Type
    TransactionType txnType = (TransactionType) map.get(TiXMLHandler.TXN_TYPE);
    if (txnType == null)
      fail("No transaction type enumeration found in map for VoidTicket.");
    if (txnType != TransactionType.VOIDTICKET)
      fail("Invalid transaction type enumeration found in map for VoidTicket.");

    // Check Tickets
    String tickets = (String) map.get(TiXMLHandler.TICKETS);
    if (tickets == null)
      fail("No tickets found in map for VoidTicket.");
    if (!tickets
        .equals(" AFEPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIPFPPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIP "))
      fail("Invalid tickets found in map for VoidTicket.");

  }

  @Test
  public final void testParseDocUpgradeAlpha() {

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/UpgradeAlpha.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load UpgradeAlpha for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String,Object> map = TiXMLHandler.parseDoc(doc);

    // Check PayloadID
    String payloadID = (String) map.get(TiXMLHandler.PAYLOAD_ID);
    if (payloadID == null)
      fail("No PayloadID found in map for UpgradeAlpha.");
    if (!payloadID.equals("945501784131637"))
      fail("Invalid PayloadID value (" + payloadID + ") found in map for UpgradeAlpha.");

    // Check TSMAC
    String tsmac = (String) map.get(TiXMLHandler.TS_MAC);
    if (tsmac == null)
      fail("No TSMAC found in map for UpgradeAlpha.");
    if (!tsmac.equals("AAA084"))
      fail("Invalid TSMAC value (" + tsmac + ") found in map for UpgradeAlpha.");

    // Check TS_LOCATION
    String tsLocation = (String) map.get(TiXMLHandler.TS_LOCATION);
    if (tsLocation == null)
      fail("No TSLocation found in map for UpgradeAlpha.");
    if (!tsLocation.equals("33682062"))
      fail("Invalid TSLocation value (" + tsLocation + ") found in map for UpgradeAlpha.");

    // Check TS_ENVIRONMENT
    String target = (String) map.get(TiXMLHandler.TS_ENVIRONMENT);
    if (target == null)
      fail("No Target found in map for UpgradeAlpha.");
    if (!target.equals("Prod-WDW"))
      fail("Invalid Target value (" + target + ") found in map for UpgradeAlpha.");

    // Check TS_VERSION
    String tsVersion = (String) map.get(TiXMLHandler.TS_VERSION);
    if (tsVersion == null)
      fail("No Version found in map for UpgradeAlpha.");
    if (!tsVersion.equals("1.0"))
      fail("Invalid Version value (" + tsVersion + ") found in map for UpgradeAlpha.");

    // Check ACTION
    String action = (String) map.get(TiXMLHandler.ACTION);
    if (action == null)
      fail("No transaction type found in map for UpgradeAlpha.");
    if (!action.equals("UpgradeAlpha           "))
      fail("Invalid transaction type value (" + action + ") found in map for UpgradeAlpha.");

    // Check Transaction Type
    TransactionType txnType = (TransactionType) map.get(TiXMLHandler.TXN_TYPE);
    if (txnType == null)
      fail("No transaction type enumeration found in map for UpgradeAlpha.");
    if (txnType != TransactionType.UPGRADEALPHA)
      fail("Invalid transaction type enumeration found in map for UpgradeAlpha.");

    // Check Products
    String products = (String) map.get(TiXMLHandler.PRODUCTS);
    if (products == null)
      fail("No tickets found in map for UpgradeAlpha.");
    if (!products.equals("BJA11BJA01BJA01"))
      fail("Invalid products found in map for UpgradeAlpha.");

  }

  @Test
  public final void testParseDocReservation() {

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/ReservationRequest.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load ReservationRequest for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);

    // Check PayloadID
    String payloadID = (String) map.get(TiXMLHandler.PAYLOAD_ID);
    if (payloadID == null)
      fail("No PayloadID found in map for Reservation.");
    if (!payloadID.equals("39900000000002019329"))
      fail("Invalid PayloadID value (" + payloadID + ") found in map for Reservation.");

    // Check TSMAC
    String tsmac = (String) map.get(TiXMLHandler.TS_MAC);
    if (tsmac == null)
      fail("No TSMAC found in map for Reservation.");
    if (!tsmac.equals("WDPRONADLR"))
      fail("Invalid TSMAC value (" + tsmac + ") found in map for Reservation.");

    // Check TS_LOCATION
    String tsLocation = (String) map.get(TiXMLHandler.TS_LOCATION);
    if (tsLocation == null)
      fail("No TSLocation found in map for Reservation.");
    if (!tsLocation.equals("351"))
      fail("Invalid TSLocation value (" + tsLocation + ") found in map for Reservation.");

    // Check TS_ENVIRONMENT
    String target = (String) map.get(TiXMLHandler.TS_ENVIRONMENT);
    if (target == null)
      fail("No Target found in map for Reservation.");
    if (!target.equals("Test-DLR"))
      fail("Invalid Target value (" + target + ") found in map for Reservation.");

    // Check TS_VERSION
    String tsVersion = (String) map.get(TiXMLHandler.TS_VERSION);
    if (tsVersion == null)
      fail("No Version found in map for Reservation.");
    if (!tsVersion.equals("1.0"))
      fail("Invalid Version value (" + tsVersion + ") found in map for Reservation.");

    // Check ACTION
    String action = (String) map.get(TiXMLHandler.ACTION);
    if (action == null)
      fail("No transaction type found in map for Reservation.");
    if (!action.equals("Reservation            "))
      fail("Invalid transaction type value (" + action + ") found in map for Reservation.");

    // Check Transaction Type
    TransactionType txnType = (TransactionType) map.get(TiXMLHandler.TXN_TYPE);
    if (txnType == null)
      fail("No transaction type enumeration found in map for Reservation.");
    if (txnType != TransactionType.RESERVATION)
      fail("Invalid transaction type enumeration found in map for Reservation.");

    // Check Products
    String products = (String) map.get(TiXMLHandler.PRODUCTS);
    if (products == null)
      fail("No tickets found in map for Reservation.");
    if (!products.equals("20876PAH20025"))
      fail("Invalid products found in map for Reservation.");

  }
	/*
	 * Test harness for pvt.disney.dti.gateway.client.HttpController class. 
	 * public static void main(String[] args) {
	 * 
	 * HttpController httpCtrl = new
	 * HttpController();
	 * 
	 * String ts = new String(" <?xml version=\"1.0\"?> <Envelope>"); ts =
	 * ts.concat(" <Header> <SourceID>SWY </SourceID>"); ts = ts.concat("
	 * <MessageID> 22411 </MessageID>"); ts = ts.concat("
	 * <MessageType>ActivateTicket </MessageType>"); ts = ts.concat("
	 * <TimeStamp>2004-08-25 18:20:16.54 </TimeStamp>"); ts = ts.concat("
	 * </Header> <Body> <TicketActivation>"); ts = ts.concat(" <Command>Activate
	 * </Command>"); ts = ts.concat(" <CustomerID>CustomerID </CustomerID>"); ts =
	 * ts.concat(" <Tickets> <Ticket> <VisualID>6000100000000046 </VisualID>");
	 * ts = ts.concat(" <ItemCode>10016 </ItemCode> <Price>49.75 </Price>
	 * </Ticket>"); ts = ts.concat(" </Tickets> </TicketActivation> <Payments>
	 * <Payment>"); ts = ts.concat(" <PaymentCode>61 </PaymentCode>"); ts =
	 * ts.concat(" <Description>Voucher </Description>"); ts = ts.concat("
	 * <Endorsement>8784 </Endorsement> <Amount>49.75 </Amount>"); ts =
	 * ts.concat(" </Payment> </Payments> </Body> </Envelope>");
	 * 
	 * System.out.println(ts);
	 * 
	 * ByteArrayInputStream bais = new ByteArrayInputStream(ts.getBytes());
	 * 
	 * Document d = null; d = httpCtrl.send(bais);
	 * 
	 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); OutputFormat of =
	 * new OutputFormat(d, "UTF-8", true); XMLSerializer xmls = new
	 * XMLSerializer(baos, of); try { xmls.serialize(d); } catch (IOException
	 * ioe) { System.out.println( "IOException on xmls.serialize(document): " +
	 * ioe.toString()); } System.out.println(baos.toString()); }
	 */

}
