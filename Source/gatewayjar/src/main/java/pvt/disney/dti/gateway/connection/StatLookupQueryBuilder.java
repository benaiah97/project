/* StatLookupQueryBuilder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import com.disney.exception.WrappedException;

public class StatLookupQueryBuilder implements QueryBuilder
{
    private static final String SERVER_LIST_TOKEN = "<SERVER_LIST>";
    
    public String getQuery
	(String propertiesFileQuery, Object[] queryValues)
	throws WrappedException {
	int total = queryValues.length;
	int numInList = total - 2;
	StringBuffer qList = new StringBuffer();
	for (int i = 0; i < numInList; i++) {
	    if (i != 0)
		qList.append(", ");
	    qList.append("?");
	}
	int serverListIdx = propertiesFileQuery.indexOf("<SERVER_LIST>");
	String newQuery = propertiesFileQuery;
	if (serverListIdx > 0)
	    newQuery = (newQuery.substring(0, serverListIdx) + qList.toString()
			+ newQuery.substring(serverListIdx
					     + "<SERVER_LIST>".length()));
	return newQuery;
    }
}
