/* StatAppsResultSetProcessor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.disney.exception.WrappedException;

public class StatAppsResultSetProcessor implements ResultSetProcessor
{
    private ArrayList appNames = null;
    
    public StatAppsResultSetProcessor() {
	appNames = new ArrayList();
    }
    
    public void processNextResultSet(ResultSet rs)
	throws SQLException, WrappedException {
	String appName = rs.getString(1);
	appNames.add(appName);
    }
    
    public Object getProcessedObject() throws WrappedException {
	return appNames;
    }
}
