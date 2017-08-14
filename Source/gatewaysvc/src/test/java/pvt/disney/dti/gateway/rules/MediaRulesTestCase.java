package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;

/**
 * @author MISHP012
 *  JUnit for MediaRules
 */
public class MediaRulesTestCase {

	/**
	 * JUnit for validateMinOneMediaOnRequest 
	 */
	@Test
	public void testValidateMinOneMediaOnRequest() {
		ArrayList<NewMediaDataTO> aMediaList = new ArrayList<NewMediaDataTO>();
		/*Scenario :: 1 Expected Exception
		 * is Transaction required to have at least 1 media. aMediaList size is ==
		 * zero*/
		try {
			MediaRules.validateMinOneMediaOnRequest(aMediaList);
		} catch (DTIException dtiException) {
			assertEquals("Transaction required to have at least 1 media.",
					dtiException.getLogMessage());
		}
	}

	/**
	 * JUnit for validateMax250MediaOnRequest
	 */
	@Test
	public void testValidateMax250MediaOnRequest() {
		ArrayList<NewMediaDataTO> aMediaList = new ArrayList<NewMediaDataTO>();
		NewMediaDataTO newMediaDataTO = new NewMediaDataTO();
		/* Scenario :: 1 Expected Exception
		 * is Transaction required to have at no more than 250 media; had 251 media.
		 * aMediaList size is>250*/
		for (int i = 0; i <= 250; i++)
			aMediaList.add(newMediaDataTO);
		try {
			MediaRules.validateMax250MediaOnRequest(aMediaList);
		} catch (DTIException dtiException) {
			assertEquals(
					"Transaction required to have at no more than 250 media; had 251 media.",
					dtiException.getLogMessage());
		}
	}
}
