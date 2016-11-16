package pvt.disney.dti.gateway.connection;

import com.disney.util.AbstractInitializer;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class StatsRecorder {
	private static StatsRecorder instance = null;

	protected HashMap stats = null;

	private Date startUpTime;

	private String hostName;

	protected StatsRecorder() {
		hostName = AbstractInitializer.getInitializer().getAppName();
		startUpTime = new Date();
		stats = new HashMap();
	}

	private static void destroyInstance() {
		instance = null;
	}

	public static StatsRecorder getInstance() {
		if (instance == null) {
			synchronized (StatsRecorder.class) {
				if (instance == null) {
					instance = new StatsRecorder();
				}
			}
		}

		return instance;
	}

	public void postTransactionResult(String name,
			Calendar transactionStartedTime, Calendar transactionCompletedTime) {
		postTransactionResult(name, transactionCompletedTime.getTime(),
				transactionStartedTime.getTime());
	}

	public synchronized void postTransactionResult(String name,
			Date transactionStartedTime, Date transactionCompletedTime) {
		String deblankedName = name.replace(' ', '_');
		long elapsedTime = transactionCompletedTime.getTime()
				- transactionStartedTime.getTime();

		TransactionStats statGrp = null;
		synchronized (stats) {
			if (stats.containsKey(deblankedName)) {
				statGrp = (TransactionStats) stats.get(deblankedName);
			} else {
				statGrp = new TransactionStats(deblankedName);
				stats.put(deblankedName, statGrp);
			}

			statGrp.bumpTransaction(elapsedTime, transactionCompletedTime);
		}
	}

	public String toPropList() {
		StringBuffer sb = new StringBuffer();
		sb.append("StartUp=\"" + startUpTime.toString() + "\";");
		sb.append("TimeStamp=\"" + new Date().toString() + "\";");
		sb.append("HostName=\"" + hostName + "\";");

		Iterator it = stats.keySet().iterator();

		while (it.hasNext()) {

			String name = (String) it.next();
			TransactionStats stat = (TransactionStats) stats.get(name);
			sb.append(stat.toPropList());
		}

		return sb.toString();
	}

	public String toWebMonPropList() {
		return toWebMonPropList(false);
	}

	public String toWebMonPropList(boolean displayClear) {
		String KEY_VALUE = "D1sn3y0nl1n3D1sn3yAttract10ns";
		StringBuffer sb = new StringBuffer();

		String ss = toPropList();

		if (displayClear) {
			sb.append(ss);
		} else {
			sb.append("BEGIN_STATS");
			sb.append(URLEncoder.encode(xorCryptString(KEY_VALUE, ss)));
			sb.append("END_STATS");
		}

		return sb.toString();
	}

	private String xorCryptString(String key, String source) {
		StringBuffer dest = new StringBuffer();

		int ii = 0;
		int pp = 0;
		int cc = source.length();
		for (int kk = key.length(); ii < cc; ii++) {
			byte sbyte = (byte) source.charAt(ii);
			byte kbyte = (byte) key.charAt(pp);

			dest.append((char) (sbyte ^ kbyte));
			pp = (pp + 1) % kk;
		}

		return dest.toString();
	}

	public HashMap cloneStats() {
		return (HashMap) stats.clone();
	}

	public HashMap clearStats() {
		HashMap snapshot = null;
		synchronized (stats) {
			snapshot = stats;
			stats = new HashMap();
			startUpTime = new Date();
		}

		return snapshot;
	}

	public Date getStartUpTime() {
		return startUpTime;
	}
}

/*
 * Location:
 * C:\Users\BIEST001\.m2\repository\com\disney\shared\SharedStats\2.0.4
 * \SharedStats-2.0.4.jar Qualified Name: com.disney.stats.StatsRecorder Java
 * Class Version: 1.2 (46.0) JD-Core Version: 0.7.1
 */