package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;

/**
 * This class tests the transform rules.
 * 
 * @author lewit019
 * 
 */
public class TransformRulesTestCase {

  private static final Integer TPREFNUM = new Integer(123456);
  private static final String PAYLOADID = "1234567890";
  private static final String TARGET = "TARGET";
  private static final String VERSION = "VERSION";
  private static final String METHOD = "METHOD";
  private static final String PROTOCOL = "PROTOCOL";
  private static final String BROKER = "BROKER";
  private static final BigInteger CMDCOUNT = new BigInteger("1");
  private static final BigInteger CMDITEM = new BigInteger("1");

  /** Default constant for zero duration. */
  private final static BigDecimal ZERO_DURATION = new BigDecimal("0.0");

  /**
   * Tests the create response payload header method.
   */
  @Test
  public final void testCreateRespPayloadHdr() {

    DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    DTIRequestTO dtiRqst = new DTIRequestTO();
    PayloadHeaderTO rqstHeader = new PayloadHeaderTO();
    dtiRqst.setPayloadHeader(rqstHeader);
    dtiTxn.setRequest(dtiRqst);

    // Set up data for test
    dtiTxn.setTpRefNum(TPREFNUM);
    rqstHeader.setPayloadID(PAYLOADID);
    rqstHeader.setTarget(TARGET);
    rqstHeader.setVersion(VERSION);
    rqstHeader.setCommMethod(METHOD);
    rqstHeader.setCommProtocol(PROTOCOL);
    dtiTxn.setTktBroker(BROKER);

    PayloadHeaderTO respHeader = TransformRules.createRespPayloadHdr(dtiTxn);

    // Evaluate responses.

    // PayloadID
    if (respHeader.getPayloadID().compareTo(TPREFNUM.toString()) != 0) {
      fail("Invalid value found in PayloadID: " + respHeader.getPayloadID());
    }

    // TSPayloadID
    if (respHeader.getTsPayloadID().compareTo(PAYLOADID) != 0) {
      fail("Invalid value found in TSPayloadID: " + respHeader.getTsPayloadID());
    }

    // Target
    if (respHeader.getTarget().compareTo(TARGET) != 0) {
      fail("Invalid value found in Target: " + respHeader.getTarget());
    }

    // Version
    if (respHeader.getVersion().compareTo(VERSION) != 0) {
      fail("Invalid value found in Version: " + respHeader.getVersion());
    }

    // CommMethod
    if (respHeader.getCommMethod().compareTo(METHOD) != 0) {
      fail("Invalid value found in CommMethod: " + respHeader.getCommMethod());
    }

    // CommProtocol
    if (respHeader.getCommProtocol().compareTo(PROTOCOL) != 0) {
      fail("Invalid value found in CommProtocol: " + respHeader.getCommProtocol());
    }

    // CommandCount
    if (respHeader.getCommandCount().compareTo(CMDCOUNT) != 0) {
      fail("Invalid value found in CommandCount: " + respHeader.getCommandCount());
    }

    // TktBroker
    if (respHeader.getTktBroker().compareTo(BROKER) != 0) {
      fail("Invalid value found in TktBroker: " + respHeader.getTktBroker());
    }

    // TransmitDateTime
    if (respHeader.getTransmitDateTime() == null) {
      fail("TransmitDateTime was null.");
    }

    return;
  }

  /**
   * Tests the create response command header method.
   */
  @Test
  public final void testCreateRespCmdHdr() {

    DTITransactionTO dtiTxn = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    DTIRequestTO dtiRqst = new DTIRequestTO();
    CommandHeaderTO rqstHeader = new CommandHeaderTO();
    dtiRqst.setCommandHeader(rqstHeader);
    dtiTxn.setRequest(dtiRqst);

    rqstHeader.setCmdItem(CMDITEM);

    CommandHeaderTO respHeader = TransformRules.createRespCmdHdr(dtiTxn);

    // Evaluate responses.

    // CmdItem
    if (respHeader.getCmdItem().compareTo(CMDITEM) != 0) {
      fail("Invalid value found in CmdItem: " + respHeader.getCmdItem());
    }

    // CmdDuration
    if (respHeader.getCmdDuration().compareTo(ZERO_DURATION) != 0) {
      fail("Invalid value found in CmdDuration: " + respHeader.getCmdDuration());
    }

    // CmdDateTime
    if (respHeader.getCmdDateTime() == null) {
      fail("CmdDateTime was null.");
    }

    return;
  }

}
