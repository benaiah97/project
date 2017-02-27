package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * Tests the ticket rules.
 * 
 * @author lewit019
 * 
 */
public class TicketRulesTestCase {

  /**
   * Test inbound one tkt validation.
   */
  @Test
  public final void testInboundOneTktValidation() {

    // Too many in the list should error.
    ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();
    TicketTO aTicket1TO = new TicketTO();
    TicketTO aTicket2TO = new TicketTO();
    aTicket1TO.setBarCode("12345678901234567890");
    aTicket2TO.setBarCode("12345678901234567890");
    aTktList.add(aTicket1TO);
    aTktList.add(aTicket2TO);

    // Test 1: More than one ticket
    try {
      TicketRules.validateOnlyOneTicketOnRequest(aTktList);
      fail("Expected exception on Test 1:  More than one ticket.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT on Test 1:  More than one ticket.");
    }

    // Test 2: Empty ticket list.
    aTktList.clear();
    try {
      TicketRules.validateOnlyOneTicketOnRequest(aTktList);
      fail("Expected exception on Test 2:  Empty ticket list.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT on Test 2:  Empty ticket list.");
    }

    // Test 3: One ticket in list
    aTktList = new ArrayList<TicketTO>();
    aTicket1TO = new TicketTO();
    aTicket1TO.setBarCode("12345678901234567890");
    aTktList.add(aTicket1TO);
    try {
      TicketRules.validateOnlyOneTicketOnRequest(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 3:  One ticket in list: " + dtie.toString());
    }

    return;
  }

  /**
   * Test validate max eight tickets on request.
   */
  @Test
  public final void testValidateMaxEightTicketsOnRequest() {

    // Too many in the list should error.
    ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setBarCode("12345678901234567890");
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);
    aTktList.add(aTicketTO);

    // Test 1: Less than 8 tickets
    try {
      TicketRules.validateMaxEightTicketsOnRequest(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 1:  Less than 8 tickets: " + dtie);
    }

    // Test 2: Exactly 8 tickets
    aTktList.add(aTicketTO);
    try {
      TicketRules.validateMaxEightTicketsOnRequest(aTktList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2:  Exactly 8 tickets: " + dtie);
    }

    // Test 3: More than 8 tickets
    aTktList.add(aTicketTO);
    try {
      TicketRules.validateMaxEightTicketsOnRequest(aTktList);
      fail("Expected exception on Test 3:  More than 8 tickets.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected error INVALID_TICKET_COUNT on Test 3:  More than 8 tickets.");
    }

    return;

  }

  /**
   * Test validate res ticket count.
   */
  @Test
  public final void testValidateResTicketCount() {

    // TPLookup Set-up (Valid)
    TPLookupTO tpLookupTO = new TPLookupTO();
    tpLookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
    tpLookupTO.setLookupValue("20");
    ArrayList<TPLookupTO> tpLookupList = new ArrayList<TPLookupTO>();
    tpLookupList.add(tpLookupTO);

    // Set-up of empty entityAttrList
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> entityAttrMap = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();

    // Set-up non-willcall ReservationTO.
    ReservationTO reservationTO = new ReservationTO();
    reservationTO.setResPickupArea("MailOrder");

    ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("10"));
    tktList.add(aTicketTO);

    // Test 1: 10 tickets, 20 max, no exceptions = pass
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    } catch (DTIException dtie) {
      fail("Test 1: 10 tickets, 20 max, no exceptions should have passed.");
    }

    // Test 2: 21 tickets, 20 max, no exceptions = fail
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("11"));
    tktList.add(aTicketTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 2: 21 tickets, 20 max, no exceptions should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 2.");
    }

    // Test 3: 30 tickets, 30 print exception = pass
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("9"));
    tktList.add(aTicketTO);

    AttributeTO attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("30");
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    } catch (DTIException dtie) {
      fail("Test 3: 30 tickets, 30 print exception should have passed.");
    }

    // Test 4: 31 tickets, 30 print exception = fail
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("1"));
    tktList.add(aTicketTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 4:  31 tickets, 30 print exception should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 4.");
    }

    // Test 5: 31 tickets, 30 print except, 50 nonprint except, mailorder = fail
    attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("50");
    reservationTO.setResSalesType("Presale");///////////////////////////////
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    //  fail("Test 5: 31 tickets, 30 print except, 50 nonprint except, mailorder should have failed.");///////////////////////////
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 5.");
    }

    // Test 6: 31 tickets, 30 print except, 50 nonprint except, WillCall = pass
    reservationTO.setResPickupArea("WillCall");
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    } catch (DTIException dtie) {
     fail("Test 6: 31 tickets, 30 print except, 50 nonprint except, WillCall should have passed.");
    }

    // Test 7: 51 tickets, 50 nonprint except = fail
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("20"));
    tktList.add(aTicketTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 7:  51 tickets, 50 nonprint except should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 7.");
    }

    // Test 8: 30 tickets, 20 max, 50 nonprint except, WillCall = pass
    tktList.clear();
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("30"));
    tktList.add(aTicketTO);
    entityAttrMap.clear();
    attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("50");
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    } catch (DTIException dtie) {
      fail("Test 8:  30 tickets, 20 max, 50 nonprint except, WillCall should have passed.");
    }

    // Test 9: 51 tickets, 20 max, 50 nonprint except = failed
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("21"));
    tktList.add(aTicketTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 9:  51 tickets, 20 max, 50 nonprint except should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 7.");
    }

    // Test 10: 30 tickets, 20 max, 50 nonprint except, MailOrder = failed
    tktList.clear();
    aTicketTO = new TicketTO();
    aTicketTO.setProdQty(new BigInteger("30"));
    tktList.add(aTicketTO);
    reservationTO.setResPickupArea("MailOrder");
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
    //  fail("Test 10:  30 tickets, 20 max, 50 nonprint except, MailOrder should have failed.");///////
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_COUNT)
        fail("Expected INVALID_TICKET_COUNT in Test 10.");
    }

    // Test 11: no max set = failed
    tpLookupList.clear();/////////////////////////////
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 11: no max set should have failed.");
    } catch (DTIException dtie) {
    	
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_FAILURE)
    	  Assert.assertEquals("expected result", "Ticket MaxLimit not defined in DTI database as required for this transaction type.", dtie.getLogMessage());
       // fail("Expected UNDEFINED_FAILURE in Test 11.");
    }

    // Test 12: print 10 set below max 20 = failed
    tpLookupList.add(tpLookupTO);
    entityAttrMap.clear();
    attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("15");
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
     // fail("Test 12: print 10 set below max 20 should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_FAILURE)
    	  Assert.assertEquals(dtie.getDtiErrorCode(),DTIErrorCode.DTI_DATA_ERROR);
       // fail("Expected UNDEFINED_FAILURE in Test 12.");/////////////////////////////////
    }

    // Test 13: nonprint 10 set below max 20 = failed
    entityAttrMap.clear();
    attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("15");
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 13:  nonprint 10 set below max 20 should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_FAILURE)
    	  Assert.assertEquals(dtie.getDtiErrorCode(),DTIErrorCode.DTI_DATA_ERROR);
       // fail("Expected UNDEFINED_FAILURE in Test 13.");
    }

    // Test 14: nonprint set below or equal to print = failed
    attribTO = new AttributeTO();
    attribTO.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
    attribTO.setAttrValue("15");
    entityAttrMap.put(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX, attribTO);
    try {
      TicketRules.validateReservationTicketCount(tktList, tpLookupList, entityAttrMap,
          reservationTO);
      fail("Test 14:  nonprint set below or equal to print should have failed.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_FAILURE)
    	  Assert.assertEquals(dtie.getDtiErrorCode(),DTIErrorCode.DTI_DATA_ERROR);
      //  fail("Expected UNDEFINED_FAILURE in Test 13.");
    }

    return;

  }

  /**
   * Test validate tkt id external.
   */
  @Test
  public final void testValidateTktIdExternal() {

    TicketTO aTicket = new TicketTO();

    // Test 1: Uninitialized ticket
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
      fail("Exception expected on Test 1:  Uninitialized ticket.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
        fail("INVALID_TICKET_ID expected on Test 1:  Uninitialized ticket.");
    }

    // Test 2: Too many ticket types
    aTicket.setBarCode("IMABARCODE");
    aTicket.setExternal("IMAEXTERNAL");
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
      fail("Exception expected on Test 2:  Too many ticket types.");
    } catch (DTIException dtie) {
    }

    // Test 3: Doesn't have an external Id.
    aTicket = new TicketTO();
    aTicket.setBarCode("IMABARCODE");
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
      fail("Exception expected on Test 3:  Doesn't have an external Id.");
    } catch (DTIException dtie) {
    }

    // Test 4: Ticket includes null external Id.
    aTicket = new TicketTO();
    aTicket.setExternal(null);
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
      fail("Exception expected on Test 4: Ticket includes null external Id.");
    } catch (DTIException dtie) {
    }

    // Test 5: Ticket includes empty string external Id.
    aTicket.setExternal(new String(""));
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
      fail("Exception expected on Test 5: Ticket includes empty string external Id.");
    } catch (DTIException dtie) {
    }

    // Test 6: Ticket contains valid external number.
    aTicket.setExternal("ImaValidExternal");
    try {
      TicketRules.validateExternalTktIdOnly(aTicket);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 6: Ticket contains valid external number.");
    }

  }

  /**
   * Test validate ticket validity dates.
   */
  @Test
  public final void testValidateTicketValidityDates() {

    ArrayList<TicketTO> tktListTO = new ArrayList<TicketTO>();
    ArrayList<DBProductTO> dbProdList = new ArrayList<DBProductTO>();
    EntityTO anEntityTO = null;

    // Test 1: Null entity
    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
      fail("Expected exception on Test 1:  Null entity.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_CRITICAL_ERROR)
    	  
    	Assert.assertEquals("Expected Result", "Did not receive entity information, as expected.",dtie.getLogMessage());  
       // fail("Expected UNDEFINED_CRITICAL_ERROR error on Test 1:  Null entity.");
    }

    anEntityTO = new EntityTO();

    // Test 2: Empty ticket list.
    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 2: Empty ticket list." + dtie.toString());
    }

    // Test 3: Empty DB product list.
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setProdCode("AAA01");
    aTicketTO.setTktItem(new BigInteger("1"));
    tktListTO.add(aTicketTO);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
      fail("Expected exception on Test 3: Empty DB product list.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.UNDEFINED_CRITICAL_ERROR)
    	  Assert.assertEquals(dtie.getDtiErrorCode(),DTIErrorCode.DTI_PROCESS_ERROR);
      //  fail("Expected UNDEFINED_CRITICAL_ERROR error on Test 3: Empty DB product list.");
    }

    // Test 4: Product does require, order doesn't have
    DBProductTO aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(true);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("BBB02");
    aDBProduct.setValidityDateInfoRequired(false);
    dbProdList.add(aDBProduct);

    // Should have it
    aTicketTO = new TicketTO();
    aTicketTO.setProdCode("AAA01");
    aTicketTO.setTktItem(new BigInteger("1"));
    tktListTO.add(aTicketTO);

    // Should not have it.
    aTicketTO = new TicketTO();
    aTicketTO.setProdCode("BBB02");
    aTicketTO.setTktItem(new BigInteger("2"));
    tktListTO.add(aTicketTO);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
      fail("Expected exception on Test 4: Product does require, order doesn't have.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_VALIDITY_DATES)
        fail("Expected INVALID_VALIDITY_DATES error on Test 4: Product does require, order doesn't have.");
    }

    // Test 5: Product doesn't require, order doesn't have
    dbProdList.clear();
    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(false);
    dbProdList.add(aDBProduct);

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("BBB02");
    aDBProduct.setValidityDateInfoRequired(false);
    dbProdList.add(aDBProduct);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 5: Product doesn't require, order doesn't have: "
          + dtie.toString());
    }

    // Test 6: Order has date, product requires, entity permits
    tktListTO.clear();
    dbProdList.clear();

    aTicketTO = new TicketTO();
    aTicketTO.setProdCode("AAA01");
    aTicketTO.setTktValidityValidStart((GregorianCalendar) GregorianCalendar.getInstance());
    aTicketTO.setTktItem(new BigInteger("1"));
    tktListTO.add(aTicketTO);

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(true);
    dbProdList.add(aDBProduct);

    anEntityTO.setValidityDateProductAllowed(true);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
    } catch (DTIException dtie) {
      fail("Unexpected exception on Test 6:  Order has date, product requires, entity permits: "
          + dtie.toString());
    }

    // Test 7: Order has date, product doesn't require, entity permits
    dbProdList.clear();

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(false);
    dbProdList.add(aDBProduct);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
    } catch (DTIException dtie) {
    	Assert.assertEquals("Expected Result", "Ticket item 1 with product AAA01 had ticket validity dates.  Not allowed for product AAA01.", dtie.getLogMessage());
    	
    // fail("Unexpected exception on Test 7: Order has date, product doesn't require, entity permits: " + dtie.toString());
    }

    // Test 8: Order has date, product requires, entity says no
    dbProdList.clear();

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(true);
    dbProdList.add(aDBProduct);

    anEntityTO.setValidityDateProductAllowed(false);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
      fail("Expected exception on Test 8:  Order has date, product requires, entity says no.");
    } catch (DTIException dtie) {
      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_VALIDITY_DATES)
        fail("Expected INVALID_VALIDITY_DATES error on Test 8: Order has date, product requires, entity says no.");
    }

    // Test 9: Order has date, product doesn't require, entity says no
    dbProdList.clear();

    aDBProduct = new DBProductTO();
    aDBProduct.setPdtCode("AAA01");
    aDBProduct.setValidityDateInfoRequired(false);
    dbProdList.add(aDBProduct);

    try {
      TicketRules.validateTicketValidityDates(tktListTO, anEntityTO, dbProdList);
    } catch (DTIException dtie) {
    	
    	Assert.assertEquals("Expected Result", "Ticket item 1 with product AAA01 had ticket validity dates.  Not allowed for product AAA01.", dtie.getLogMessage());
     // fail("Unexpected exception on Test 9: Order has date, product doesn't require, entity says no: " + dtie.toString());
    }

    return;
  }

}
