package pvt.disney.dti.gateway.rules.dlr;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * @author smoon
 *
 */
public class DLRRulesTestCase {

	/**
	 *  Validates enforcement of the following rules: <BR>
	 * 1. If the TicketIdType is External, then it must be less than 40
	 *  characters long.<BR>
	 */
	@Test
	  public final void testValidateInBoundWDWTickets() {

	    ArrayList<TicketTO> aTktList = new ArrayList<TicketTO>();

	    // Test 1: Visual ID > 40
	    TicketTO aTicketTO1 = new TicketTO();
	    aTicketTO1.setExternal("012345689012345689012345689012345689012345689");
	    aTktList.add(aTicketTO1);
	    try {
	      DLRBusinessRules.validateInBoundDLRTickets(aTktList);
	      fail("Exception expected on Test 1: External > 40");
	    } catch (DTIException dtie) {
	      if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TICKET_ID)
	        fail("DTI Error of INVALID_TICKET_ID expected on Test 1: Visual ID > 40");
	    }
	    
	}
}
