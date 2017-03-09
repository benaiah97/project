package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Tests the WDW specific business rules.
 * @author lewit019
 * 
 */
public class WDWBusinessRulesTestCase {

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

  /**
   * Validates enforcement of the following rules: <BR>
   * 1. If the TicketIdType is MAG, then it must be 73 characters long with an
   * 'F' in position 37. <BR>
   * 2. If the TicketIdType is DSSN, then all four components must be filled
   * out. <BR>
   * 3. If the TicketIdType is TKTNID, then it must be 17 characters long. <BR>
   * 4. If the TicketIdType is BARCODE, then it must be 20 characters long
   * (new). <BR>
   * 5. There may only be one TicketIdType per in-bound ticket.
   */
  //@Test
  public final void testValidateInBoundWDWTickets() {

    ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();

    // Test 1: Mag < 73
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setMag("NOTLONGENOUGH");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected on Test 1: Mag < 73");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 1: Mag < 73");
    }

    // Test 2: Valid Mag Stripe
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setMag(" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB ");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2: Valid Mag Stripe:" + dtie.toString());
    }

    // Test 3: No F in position 38.
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setMag(" AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAXBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB ");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected in Test 3: No F in position 38.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 3: No F in position 38.");
    }

    // Test 4: Valid DSSN
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setDssn((GregorianCalendar) GregorianCalendar.getInstance(), "site", "station",
        "number");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 4: Valid DSSN: " + dtie.toString());
    }

    // Test 5: Invalid DSSN
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setDssn((GregorianCalendar) GregorianCalendar.getInstance(), "", "station", "number");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected on Test 5: Invalid DSSN.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 5: Invalid DSSN.");
    }

    // Test 6: Valid TktNID
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setTktNID("12345678901234567");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 6: Valid TktNID: " + dtie.toString());
    }

    // Test 7: Invalid TktNID
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setTktNID("123456789012345678");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected on Test 7: Invalid TktNID.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 7: Invalid TktNID.");
    }

    // Test 8: Valid Barcode
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setBarCode("12345678901234567890");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 8: Valid Barcode:" + dtie.toString());
    }

    // Test 9: Invalid TktNID
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setBarCode("1234567890123456789");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected on Test 9: Invalid TktNID.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 9: Invalid TktNID.");
    }

    // Test 10: Two ticket Id's on the same ticket
    aTktList = new ArrayList<TicketTO>();
    aTicketTO = new TicketTO();
    aTicketTO.setBarCode("12345678901234567890");
    aTicketTO.setTktNID("12345678901234567");
    aTktList.add(aTicketTO);
    try {
      WDWBusinessRules.validateInBoundWDWTickets(aTktList);
      fail("Exception expected on Test 10: Two ticket Id's on the same ticket.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("DTI Error of INVALID_TICKET_ID expected on Test 10: Two ticket Id's on the same ticket.");
    }

    return;
  }

  /**
   * Test validate void ticket actor.
   */
  @Test
  public final void testValidateVoidTicketActor() {

    CommandHeaderTO cmdHeader = new CommandHeaderTO();

    // Test 1: No actor in CmdHeader
    try {
      WDWBusinessRules.validateVoidTicketActor(cmdHeader);
      fail("Exception expected in Test 1: No actor in CmdHeader");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.ACTOR_NOT_AUTHORIZED)
        fail("Error value of ACTOR_NOT_AUTHORIZED expected in Test 1: No actor in CmdHeader:"
            + dtie.toString());
    }

    // Test 2: Actor but invalid value
    cmdHeader.setCmdActor("BOB");
    try {
      WDWBusinessRules.validateVoidTicketActor(cmdHeader);
      fail("Exception expected in Test 2: Actor but invalid value");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.ACTOR_NOT_AUTHORIZED)
        fail("Error value of ACTOR_NOT_AUTHORIZED expected in Test 2: Actor but invalid value:"
            + dtie.toString());
    }

    // Test 3: MGR value
    cmdHeader.setCmdActor("MGR");
    try {
      WDWBusinessRules.validateVoidTicketActor(cmdHeader);
    } catch (DTIException dtie) {
      fail("Unexpected exception in Test 3: MGR value:" + dtie.toString());
    }

    // Test 4: SYS value
    cmdHeader.setCmdActor("SYS");
    try {
      WDWBusinessRules.validateVoidTicketActor(cmdHeader);
    } catch (DTIException dtie) {
      fail("Unexpected exception in Test 4: SYS value:" + dtie.toString());
    }

    return;
  }

  /**
   * Test validate ticket order shells.
   */
  @Test
  public final void testValidateTicketOrderShells() {

    ArrayList<TicketTO> aTktListTO = new ArrayList<TicketTO>();
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag("BOB", "12=123456");
    aTktListTO.add(aTicketTO);

    // Test 1: Valid shell number
    try {
      WDWBusinessRules.validateTicketOrderShells(aTktListTO);
    } catch (DTIException dtie) {
      fail("Unexpected exception in Test 1: Valid shell number: " + dtie.toString());
    }

    // Test 2: Empty shell number
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    aTicketTO.setMag("BOB", "=123456");
    aTktListTO.add(aTicketTO);
    try {
      WDWBusinessRules.validateTicketOrderShells(aTktListTO);
      fail("Exception expected in Test 2: Empty shell number.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_NUMBER)
        fail("DTI Error of INVALID_SHELL_NUMBER expected on Test 2: Empty shell number: "
            + dtie.toString());
    }

    // Test 3: Non numeric shell number
    aTktListTO.clear();
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag("BOB", "ABC=123456");
    aTktListTO.add(aTicketTO);
    try {
      WDWBusinessRules.validateTicketOrderShells(aTktListTO);
      fail("Exception expected in Test 3: Non numeric shell number.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_NUMBER)
        fail("DTI Error of INVALID_SHELL_NUMBER expected on Test 3: Non numeric shell number: "
            + dtie.toString());
    }

    // Test 4: Shell number too large
    aTktListTO.clear();
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag("BOB", "1000000=123456");
    aTktListTO.add(aTicketTO);
    try {
      WDWBusinessRules.validateTicketOrderShells(aTktListTO);
      fail("Exception expected in Test 4: Shell number too large.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_NUMBER)
        fail("DTI Error of INVALID_SHELL_NUMBER expected on Test 4: Shell number too large: "
            + dtie.toString());
    }

    return;
  }

  /**
   * Test validate ticket shell active.
   */
  @Test
  public final void testValidateTicketShellActive() {

    HashSet<Integer> orderShells = new HashSet<Integer>();
    ArrayList<Integer> activeShells = null;

    // Test 1: No order shells
    try {
      WDWBusinessRules.validateTicketShellActive(orderShells, activeShells);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1: No order shells: " + dtie.toString());
    }

    // Test 2: No active shells
    orderShells.add(new Integer(23));
    try {
      WDWBusinessRules.validateTicketShellActive(orderShells, activeShells);
      fail("Expected exception on Test 2: No active shells.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_NUMBER)
        fail("Expected DTI error INVALID_SHELL_NUMBER on Test 2: No active shells: "
            + dtie.toString());
    }

    // Test 3: Matching shells
    activeShells = new ArrayList<Integer>();
    activeShells.add(new Integer(23));
    try {
      WDWBusinessRules.validateTicketShellActive(orderShells, activeShells);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 3: Matching shells: " + dtie.toString());
    }

    // Test 4: Nonmatching shells
    orderShells.add(new Integer(42));
    activeShells.add(new Integer(41));
    try {
      WDWBusinessRules.validateTicketShellActive(orderShells, activeShells);
      fail("Expected exception on Test 4: Nonmatching shells.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_NUMBER)
        fail("Expected DTI error INVALID_SHELL_NUMBER on Test 4: Nonmatching shells: "
            + dtie.toString());
    }

  }

  /**
   * Test validate ticket shell to product.
   */
  @Test
  public final void testValidateTicketShellToProduct() {

    ArrayList<TicketTO> aTktListTO = new ArrayList<TicketTO>();
    HashMap<String, ArrayList<Integer>> prodShellsXRef = new HashMap<String, ArrayList<Integer>>();
    ArrayList<Integer> aListOfGoodShells = new ArrayList<Integer>();
    aListOfGoodShells.add(new Integer(1));
    aListOfGoodShells.add(new Integer(2));
    aListOfGoodShells.add(new Integer(98));
    aListOfGoodShells.add(new Integer(99));

    prodShellsXRef.put("AAA01", aListOfGoodShells);

    // Test 1: No shell on tickets
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag("BOB");
    aTktListTO.add(aTicketTO);

    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    aTicketTO.setBarCode("IMABARCODE");
    aTktListTO.add(aTicketTO);

    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("3"));
    aTicketTO.setMag("BOB", "IAMMEALPLANINFO");
    aTktListTO.add(aTicketTO);

    try {
      WDWBusinessRules.validateTicketShellToProduct(aTktListTO, prodShellsXRef);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1: No shell on tickets:" + dtie.toString());
    }

    // Test 2: Good product shell mapping
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("4"));
    aTicketTO.setMag("BOB", "98=123456");
    aTicketTO.setProdCode("AAA01");
    aTktListTO.add(aTicketTO);

    try {
      WDWBusinessRules.validateTicketShellToProduct(aTktListTO, prodShellsXRef);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2: Good product shell mapping:" + dtie.toString());
    }

    // Test 3: Invalid product shell mapping
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("5"));
    aTicketTO.setMag("BOB", "97=123456");
    aTicketTO.setProdCode("AAA01");
    aTktListTO.add(aTicketTO);

    try {
      WDWBusinessRules.validateTicketShellToProduct(aTktListTO, prodShellsXRef);
      fail("Expected exception on Test 3: Invalid product shell mapping.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_SHELL_FOR_PRODUCT)
        fail("Expected DTI Error of INVALID_SHELL_FOR_PRODUCT on Test 3: Invalid product shell mapping:"
            + dtie.toString());
    }

    return;

  }

}
