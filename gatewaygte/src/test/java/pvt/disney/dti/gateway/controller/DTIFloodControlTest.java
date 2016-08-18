package pvt.disney.dti.gateway.controller;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.util.flood.FloodControlInitException;
import pvt.disney.dti.gateway.util.flood.KeyDerivationException;

/**
 * Test class for DTI Flood Control.
 * @author lewit019
 *
 */
public class DTIFloodControlTest {

  private static Properties props = new Properties();
  private static DTIFloodControl floodControl = null;
  public static String keyFrequencyWindow = "6";
  public static String keyFrequencyLimit = "3";
  public static String keySuppressInterval = "9";
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    props = new Properties();

    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/DTIFloodControl.properties");
      props.load(inStream);
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load properties for test." + fnfe.toString());
    } catch (IOException ioe) {
      fail("Unable to load properties for test." + ioe.toString());
    }

    keyFrequencyWindow = props.getProperty("FloodControl.KeyFrequencyWindow");
    keyFrequencyLimit = props.getProperty("FloodControl.KeyFrequencyLimit");
    keySuppressInterval = props.getProperty("FloodControl.KeySuppressInterval");
    
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test derive key query ticket.
   */
  @Test
  public final void testDeriveKeyQueryTicket() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/QueryTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load QueryTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    String keyString = (String)key;
    
    if (keyString == null)
      fail("No key found in map for QueryTicket.");
    if (!keyString.equals("QTWDPRONA3522004-05-04XMK00711"))
      fail("Invalid key value (" + keyString + ") for QueryTicket.");
    
  }
  
  /**
   * Test derive key void ticket.
   */
  @Test
  public final void testDeriveKeyVoidTicket() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/VoidTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load VoidTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object>  map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    String keyString = (String)key;
    
    if (keyString == null)
      fail("No key found for VoidTicket.");
    if (!keyString.equals("VTDS6469P44008 AFEPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIPFPPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIP "))
      fail("Invalid key value (" + keyString + ") found for VoidTicket.");
    
  }
  
  /**
   * Test derive key upgrade alpha.
   */
  @Test
  public final void testDeriveKeyUpgradeAlpha() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/UpgradeAlpha.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load UpgradeAlpha for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object>  map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    String keyString = (String)key;
    
    if (keyString == null)
      fail("No key found for UpgradeAlpha.");
    if (!keyString.equals("UAAAA08433682062BJA11BJA01BJA01"))
      fail("Invalid key value (" + keyString + ") for UpgradeAlpha.");
    
  }
  
  /**
   * Test derive key reservation.
   */
  @Test
  public final void testDeriveKeyReservation() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/ReservationRequest.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load ReservationRequest for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object>  map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    String keyString = (String)key;
    
    if (keyString == null)
      fail("No key found for ReservationRequest.");
    if (!keyString.equals("RRWDPRONADLR35120876PAH20025"))
      fail("Invalid key value (" + keyString + ") for ReservationRequest.");
    
  }
  
  /**
   * Test derive key create ticket.
   */
  @Test
  public final void testDeriveKeyCreateTicket() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/CreateTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load CreateTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object>  map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    if (key != null)
      fail("Key dervied on excluded transaction.");
    
  }
  
  /**
   * Test derive key exception.
   */
  @Test
  public final void testDeriveKeyException() {

    try {
      floodControl = DTIFloodControl.getInstance(props);
    } catch (FloodControlInitException fcie) {
      fail("Exception creating KeyMatchFloodControl: " + fcie);
    }
    
    FileInputStream inStream = null;

    try {
      inStream = new FileInputStream("./config/ExpVoidTicket.txt");
    } catch (FileNotFoundException fnfe) {
      fail("Unable to load ExpVoidTicket for test." + fnfe.toString());
    }

    Document doc = null;

    try {
      doc = TiXMLHandler.parseXML(inStream);
      inStream.close();
    } catch (Exception e) {
      fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
    }

    HashMap<String, Object>  map = TiXMLHandler.parseDoc(doc);
    
    Object key = null;
    try {
      key = floodControl.deriveKey(map);
    } catch (KeyDerivationException kde) {
      fail("KeyDerivationException: " + kde.toString());
    }
    
    String keyString = (String)key;
    
    if (keyString != null)
      fail("Key was created when it should not have been.");
    
  }
  

}
