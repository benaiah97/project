package pvt.disney.dti.gateway.dao.data;

import java.util.HashSet;

/**
 * The Class DBUtil contains any DB utilities that are required by the DAO.
 * 
 * @author lewit019
 */
public class DBUtil {

	/**
	 * has the responsibility of concatenating multiple values from a HashSet into a list.
	 * 
	 * @param set
	 *            the set of values
	 * 
	 * @return the string, quoted with commas between members of the list.
	 */
	@SuppressWarnings("rawtypes")
	public static String createSQLInList(HashSet set) {

		StringBuffer sBuff = new StringBuffer();

		if (set.size() == 0) return "";

		for /* each */(Object anObject : /* in */set) {

			if (sBuff.length() == 0) sBuff
					.append("'" + anObject.toString() + "'");
			else sBuff.append(", '" + anObject.toString() + "'");
		}

		return sBuff.toString();

	}

}
