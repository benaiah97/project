package pvt.disney.dti.gateway.util;

import static org.junit.Assert.fail;

import java.util.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.test.util.TestUtilities;

/**
 * 
 * @author lewit019
 * 
 */
public class PCIControlTestCase {

  private final static String MAGTRACKMASK = "ZZZZZZZZZZZZZZZZZZZZ";

  private final static String CREDITCARDMASK = "ZZZZZZZZZZZZ1111";

  private final static String GIFTCARDMASK = "111111111111ZZZZ";

  private final static String FULLCARDMASK = "ZZZZZZZZZZZZZZZZ";

  private final static String LENGTH3MASK = "ZZZ";

  /**
   * Sets the up before class.
   * 
   * @throws Exception
   *           the exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * Tear down after class.
   * 
   * @throws Exception
   *           the exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * Sets the up.
   * 
   * @throws Exception
   *           the exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * Tear down.
   * 
   * @throws Exception
   *           the exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test
   */
  @Test
  public final void testGetXmlWithPciTags() {

    // ***** Read the test file. *****
    String dirtyXML = null;
    String inputFileName = new String("./config/PCIControlTest01.xml");

    try {
      dirtyXML = TestUtilities.getXMLfromFile(inputFileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + inputFileName + ". " + e.toString());
    }

    String answerXML = null;
    String answerFileName = new String("./config/PCIControlTest01Answer.xml");
    try {
      answerXML = TestUtilities.getXMLfromFile(answerFileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + answerFileName + ". " + e.toString());
    }

    String cleanXML = null;
    try {
      cleanXML = PCIControl.overwritePciDataInXML(dirtyXML);
    } catch (DTIException dtie) {
      fail("DTIException occurred while cleaning XML: " + dtie.toString());
    }

    // Validate changes to XML
    try {
      validateXMLOverwrites(cleanXML);
    } catch (Exception e) {
      fail("DTIException occurred while validating clean XML: " + e.toString());
    }

    // Validate the reproduction of the XML.
    try {
      TestUtilities.compareXML(cleanXML, answerXML, "XMLTest");
    } catch (Exception e) {
      fail("XML was altered beyond any PCI tags were present:" + e.toString());
    }
    
    return;
  }

  /**
   * Test
   */
  @Test
  public final void testGetXmlWithNoPciTags() {

    // ***** Read the test file. *****
    String dirtyXML = null;
    String fileName = new String("./config/PCIControlTest02.xml");

    try {
      dirtyXML = TestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName + ". " + e.toString());
    }

    String cleanXML = null;

    try {
      cleanXML = PCIControl.overwritePciDataInXML(dirtyXML);
    } catch (DTIException dtie) {
      fail("DTIException occurred while cleaning XML: " + dtie.toString());
    }

    try {
      TestUtilities.compareXML(dirtyXML, cleanXML, "Transmission");
    } catch (Exception e) {
      fail("XML was altered although no PCI tags were present:" + e.toString());
    }

    return;
  }

  /**
   * Test
   */
  @Test
  public final void testGetXmlWithBadXML() {

    // ***** Read the test file. *****
    String dirtyXML = null;
    String fileName = new String("./config/PCIControlTest03.xml");

    try {
      dirtyXML = TestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName + ". " + e.toString());
    }

    try {
      PCIControl.overwritePciDataInXML(dirtyXML);
      fail("DTIException expected when parsing bad XML.");
    } catch (DTIException dtie) {
    }

    return;
  }
  
  /**
   * Uses non-Dom4j methods to check the results, ensuring a cross-check.
   * 
   * @param cleanXML
   * @throws Exception
   */
  private void validateXMLOverwrites(String cleanXML) throws Exception {

    // CCNbr
    PCIControlTestCase.validateFieldMask(cleanXML, "CCNbr", CREDITCARDMASK);

    // CCNumber
    PCIControlTestCase.validateFieldMask(cleanXML, "CCNumber", CREDITCARDMASK);

    // CCVV
    PCIControlTestCase.validateFieldMask(cleanXML, "CCVV", LENGTH3MASK);

    // GCNbr
    PCIControlTestCase.validateFieldMask(cleanXML, "GCNbr", GIFTCARDMASK);

    // Endorsement
    ArrayList<String> endList = TestUtilities.getRecurringTagData(cleanXML, "Endorsement");
    for /* each */(String aString : /* in */endList)
      if (aString.compareTo(CREDITCARDMASK) != 0)
        throw new Exception("Endorsement not properly masked.");

    // CCTrack1
    PCIControlTestCase.validateFieldMask(cleanXML, "CCTrack1", MAGTRACKMASK);

    // CCTrack2
    PCIControlTestCase.validateFieldMask(cleanXML, "CCTrack2", MAGTRACKMASK);

    // GCTrack1
    PCIControlTestCase.validateFieldMask(cleanXML, "GCTrack1", MAGTRACKMASK);

    // GCTrack2
    PCIControlTestCase.validateFieldMask(cleanXML, "GCTrack2", MAGTRACKMASK);

    // CVN
    PCIControlTestCase.validateFieldMask(cleanXML, "CVN", LENGTH3MASK);

    // Track1
    PCIControlTestCase.validateFieldMask(cleanXML, "Track1", MAGTRACKMASK);

    // Track2
    PCIControlTestCase.validateFieldMask(cleanXML, "Track2", MAGTRACKMASK);

    // CVV
    PCIControlTestCase.validateFieldMask(cleanXML, "CVV", LENGTH3MASK);

    // CardNumber
    PCIControlTestCase.validateFieldMask(cleanXML, "CardNumber", FULLCARDMASK);

    // CAVVValue
    PCIControlTestCase.validateFieldMask(cleanXML, "CAVVValue", LENGTH3MASK);

    return;

  }

  /**
   * 
   * @param xml
   * @param tag
   * @param expectedValue
   * @throws Exception
   */
  private static void validateFieldMask(String xml, String tag, String expectedValue)
      throws Exception {

    String aString = TestUtilities.getTagData(xml, tag);
    if (aString.compareTo(expectedValue) != 0)
      throw new Exception(tag + " not properly masked.");

    return;
  }

}
