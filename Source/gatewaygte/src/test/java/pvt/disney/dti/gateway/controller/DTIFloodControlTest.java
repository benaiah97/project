package pvt.disney.dti.gateway.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import pvt.disney.dti.gateway.common.TiXMLHandler;
import pvt.disney.dti.gateway.dao.PropertyKey;
import pvt.disney.dti.gateway.data.common.FloodMatchSignatureTO;
import pvt.disney.dti.gateway.data.common.PropertyTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.util.flood.KeyDerivationException;

/**
 * Test class for DTI Flood Control.
 * 
 * @author lewit019
 * 
 */
public class DTIFloodControlTest {

   private static DTIFloodControl floodControl = null;
   public static String keyFrequencyWindow = "6";
   public static String keyFrequencyLimit = "3";
   public static String keySuppressInterval = "9";

   @AfterClass
   public static void tearDownAfterClass() throws Exception {
   }

   @Before
   public void setUp() {
      mockGetProperties();
   }

   @After
   public void tearDown() throws Exception {

   }

   /**
    * Mock get properties.
    */
   public static void mockGetProperties() {
      try {
         new MockUp<PropertyKey>() {
            @Mock
            public List<PropertyTO> getProperties(String application, Integer tpoId, String environment, String section) {
               ArrayList<PropertyTO> propertyList = new ArrayList<PropertyTO>();
               PropertyTO propertyTo = new PropertyTO();
               propertyTo.setPropSetKey("KEY_STORE_TYPE");
               propertyTo.setPropSetValue("1");
               propertyList.add(propertyTo);
               propertyTo = new PropertyTO();
               propertyTo.setPropSetKey("MAX_CONCURRENT_KEYS");
               propertyTo.setPropSetValue("50");
               propertyList.add(propertyTo);

               return propertyList;
            }
         };
      } catch (Exception e) {
      }
   }

   /**
    * Test derive key query ticket.
    */
   @Test
   public final void testDeriveKeyQueryTicket() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/QueryTicket.txt");
      Document doc = null;
      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }
      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
      try {
         Object Obj = floodControl.deriveKey(map);
         Assert.assertNotNull(Obj);
      } catch (KeyDerivationException e) {
         Assert.fail(e.toString());
      }
   }

   /**
    * Test derive key Create ticket should return null.
    */
   @Test
   public final void testDeriveKeyCreateTicket() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/CreateTicket.txt");
      Document doc = null;
      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }
      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
      try {
         Object Obj = floodControl.deriveKey(map);
         Assert.assertNotNull(Obj);
      } catch (KeyDerivationException e) {
         Assert.fail(e.toString());
      }
   }

   /**
    * Test derive key Reservation Request ticket should return null.
    */
   @Test
   public final void testDeriveKeyReservationRequest() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/ReservationRequest.txt");
      Document doc = null;
      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }
      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
      try {
         Object Obj = floodControl.deriveKey(map);
         Assert.assertNotNull(Obj);
      } catch (KeyDerivationException e) {
         Assert.fail(e.toString());
      }
   }

   /**
    * Test derive key VoidReservation ticket should return null.
    */
   @Test
   public final void testDeriveKeyVoidReservation() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/VoidReservation.txt");
      Document doc = null;
      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }
      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
      try {
         Object Obj = floodControl.deriveKey(map);
         Assert.assertNotNull(Obj);
      } catch (KeyDerivationException e) {
         Assert.fail(e.toString());
      }

   }

   /**
    * Test derive key VoidReservation ticket should return null.
    */
   @Test
   public final void testDeriveKeyAssociateMedia() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/AssociateMedia.txt");
      Document doc = null;
      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }
      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);
      try {
         Object Obj = floodControl.deriveKey(map);
         Assert.assertNotNull(Obj);
      } catch (KeyDerivationException e) {
         Assert.fail(e.toString());
      }
   }

   /**
    * Test derive key void ticket.
    */
   @Test
   public final void testDeriveKeyVoidTicket() {
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);

      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/VoidTicket.txt");

      Document doc = null;

      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }

      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);

      Object key = null;
      try {
         key = floodControl.deriveKey(map);
      } catch (KeyDerivationException kde) {
         Assert.fail("KeyDerivationException: " + kde.toString());
      }

      FloodMatchSignatureTO keyString = (FloodMatchSignatureTO) key;

      if (keyString == null)
         Assert.fail("No key found for VoidTicket.");
      if (!keyString.getSignature().equals(
               "VTTest-WDWDS6469P44008 AFEPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIPFPPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIP "))
         Assert.fail("Invalid key value (" + keyString + ") found for VoidTicket.");

   }

   /**
    * Test derive key upgrade alpha.
    */
   @Test
   public final void testDeriveKeyUpgradeAlpha() {

      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      mockGetProperties();
      DTIMockUtil.processMockprepareAndExecuteSql();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);

      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/UpgradeAlpha.txt");
      Document doc = null;

      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }

      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);

      Object key = null;
      try {
         key = floodControl.deriveKey(map);
      } catch (KeyDerivationException kde) {
         Assert.fail("KeyDerivationException: " + kde.toString());
      }

      FloodMatchSignatureTO keyString = (FloodMatchSignatureTO) key;

      if (keyString == null)
         Assert.fail("No key found for UpgradeAlpha.");
      if (!keyString.getSignature().equals("UAProd-WDWAAA08433682062BJA11BJA01BJA01"))
         Assert.fail("Invalid key value (" + keyString + ") for UpgradeAlpha.");

   }

   /**
    * Test derive key exception.
    */
   @Test
   public final void testDeriveKeyException() {
      DTIMockUtil.processMockprepareAndExecuteSql();
      String application = "DTIGateWay";
      String environment = "WDW";
      Integer tpoId = 0;
      mockGetProperties();
      floodControl = DTIFloodControl.getInstance(application, tpoId, environment);
      InputStream inStream = null;
      inStream = this.getClass().getResourceAsStream("/config/ExpVoidTicket.txt");
      Document doc = null;

      try {
         doc = TiXMLHandler.parseXML(inStream);
         inStream.close();
      } catch (Exception e) {
         Assert.fail("Unable to parse document from inStream to doc.  Exception: " + e.toString());
      }

      HashMap<String, Object> map = TiXMLHandler.parseDoc(doc);

      Object key = null;
      try {
         key = floodControl.deriveKey(map);
      } catch (KeyDerivationException kde) {
         Assert.fail("KeyDerivationException: " + kde.toString());
      }

      FloodMatchSignatureTO keyString = (FloodMatchSignatureTO) key;

      if (keyString == null)
         Assert.fail("No key found for UpgradeAlpha.");
      if (!keyString.getSignature().equals(
               "VTTest-WDWDS6469Pmkl2 AFEPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIPFPPK5JIJ2LMIKID5II1SCIQH5IS6IIIIIIP "))
         Assert.fail("Invalid key value (" + keyString + ") for KeyException.");
   }

   /**
    * Test get instance.
    */
   @Test
   public void testGetInstance() {
      
      Integer tpoId = 0;
      DTIMockUtil.processMockprepareAndExecuteSql();
      DTIFloodControl floodControlInstanceOne = DTIFloodControl.getInstance("DTIGateWay", tpoId, "Latest");
      DTIFloodControl floodControlInstanceTwo = DTIFloodControl.getInstance("DTIGateWay", tpoId, "Latest");
      DTIFloodControl floodControlInstanceThree = DTIFloodControl.getInstance("DTIGateWay", tpoId, "Latest");
      try {
         Thread.sleep(60);
      } catch (InterruptedException e) {
      }
      DTIFloodControl floodControlInstanceFour = DTIFloodControl.getInstance("DTIGateWay", tpoId, "Latest");
      Assert.assertEquals(floodControlInstanceOne, floodControlInstanceTwo);
      Assert.assertEquals(floodControlInstanceTwo, floodControlInstanceThree);
      Assert.assertEquals(floodControlInstanceThree, floodControlInstanceFour);

   }

}
