package pvt.disney.dti.gateway.rules;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.data.common.DBProductTO;

/**
 * The Class EligibilityRules contains the logic for handling the eligibility business rules.
 * 
 * @author lewit019
 */
public class EligibilityRules {

	/**
	 * Determines if eligibility is required.
	 * 
	 * @param dbProdList
	 *            the db product list
	 * 
	 * @return true, if eligibility required
	 */
	public static boolean eligibilityRequired(ArrayList<DBProductTO> dbProdList) {

		if ((dbProdList == null) || (dbProdList.size() == 0)) return false;

		// Loop through and find one product that requires eligibility.
		for /* each */(DBProductTO aProduct : /* in */dbProdList) {

			if (aProduct.getEligGrpid() == null) continue;
			else {
				return true;
			}
		}

		return false;

	}

	/**
	 * Validate eligibility. <BR>
	 * Enforces the following rules: <BR>
	 * 1. Eligibility must be active (true).
	 * 
	 * @param eligActive
	 *            the elig active flag
	 * @param eligGrpCode
	 *            the elig grp code for logging
	 * @param eligMemberID
	 *            the elig member id for logging
	 * @throws DTIException
	 *             if the eligibility is not active (false).
	 */
	public static void validateEligibility(Boolean eligActive,
			String eligGrpCode, String eligMemberID) throws DTIException {

		if (eligActive.booleanValue() == false) throw new DTIException(
				EligibilityKey.class,
				DTIErrorCode.INACTIVE_ELIGIBILITY_NBR,
				"Eligibility is not active for Elig Group Code " + eligGrpCode + " and Elig Member ID " + eligMemberID);

		return;

	}

}
