package pvt.disney.dti.gateway.rules;

import java.util.ArrayList;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;

/**
 * The class MediaRules implements the various business rules regarding media.
 * 
 * @author biest001
 * @since 2.16.1
 * 
 */
public class MediaRules {

	/**
	 * Enforces the following rules: <BR>
	 * 1. At least 1 media must be present in the media list.
	 * 
	 * @param aMediaList
	 *            the ticket list
	 * 
	 * @throws DTIException
	 *             should there be at least 1 media on a request.
	 */
	public static void validateMinOneMediaOnRequest(
			ArrayList<NewMediaDataTO> aMediaList) throws DTIException {

		if (aMediaList.size() == 0) throw new DTIException(MediaRules.class,
				DTIErrorCode.INVALID_MEDIA_COUNT,
				"Transaction required to have at least 1 media.");

		return;

	}

	/**
	 * Enforces the following rules: <BR>
	 * 1. Up to 250 media may be present in the media list.
	 * 
	 * @param aMediaList
	 *            the media list
	 * 
	 * @throws DTIException
	 *             should there be more than 250 media on a request.
	 */
	public static void validateMax250MediaOnRequest(
			ArrayList<NewMediaDataTO> aMediaList) throws DTIException {

		if (aMediaList.size() > 250) throw new DTIException(
				TicketRules.class,
				DTIErrorCode.INVALID_MEDIA_COUNT,
				"Transaction required to have at no more than 250 media; had " + aMediaList
						.size() + " media.");

		return;

	}

}