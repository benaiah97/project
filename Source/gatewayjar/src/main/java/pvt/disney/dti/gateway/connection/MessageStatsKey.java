package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MessageStatsKey implements QueryBuilder {
	private static final String JDBC_PROPS_FILE = "jdbc.properties";
	private final EventLogger evl = EventLogger.getLogger("DAO");

	private String messageIdFilter = null;
	private String conversationIdFilter = null;
	private String afterFilter = null;
	private String beforeFilter = null;
	private Date afterObjFilter = null;
	private Date beforeObjFilter = null;

	public String getConversationIdFilter() {
		return conversationIdFilter;
	}

	public void setConversationIdFilter(String conversationIdFilter) {
		this.conversationIdFilter = conversationIdFilter;
	}

	public String getMessageIdFilter() {
		return messageIdFilter;
	}

	public void setMessageIdFilter(String messageIdFilter) {
		this.messageIdFilter = messageIdFilter;
	}

	public String getAfterFilter() {
		return afterFilter;
	}

	public void setAfterFilter(String afterFilter) {
		this.afterFilter = afterFilter;
	}

	public String getBeforeFilter() {
		return beforeFilter;
	}

	public void setBeforeFilter(String beforeFilter) {
		this.beforeFilter = beforeFilter;
	}

	public Date getAfterObjFilter() {
		return afterObjFilter;
	}

	public void setAfterObjFilter(Date afterObjFilter) {
		this.afterObjFilter = afterObjFilter;
	}

	public Date getBeforeObjFilter() {
		return beforeObjFilter;
	}

	public void setBeforeObjFilter(Date beforeObjFilter) {
		this.beforeObjFilter = beforeObjFilter;
	}

	public MessageStats getMessageStats() throws WrappedException {
		MessageStats msgStats = null;

		try {
			Object[] values = { getMessageIdFilter(), getConversationIdFilter() };

			DAOHelper dao = DAOHelper.getInstance("MESSAGE_STATS_LOOKUP");

			msgStats = (MessageStats) dao.processQuery(values, values, this);
		} catch (WrappedException we) {
			throw we;
		} catch (Throwable th) {
			WrappedException we = new WrappedException(
					"getMessageStats Exception", th,
					ErrorCode.APPLICATION_EXCEPTION);
			evl.sendException(EventType.EXCEPTION,
					ErrorCode.APPLICATION_EXCEPTION, we, this);
			throw we;
		}

		return msgStats;
	}

	public String getQuery(String query, Object[] queryValues)
			throws WrappedException {
		if ((queryValues == null) || (queryValues.length < 2)) {
			WrappedException we = new WrappedException(
					"Not enough values supplied to QueryBuilder: "
							+ queryValues, ErrorCode.DATABASE_SQL_EXCEPTION);
			throw we;
		}

		try {
			int whereIndex = query.indexOf("WHERE") + 5;

			StringBuffer sbWhere = new StringBuffer();

			String messageId = (String) queryValues[0];

			if (messageId != null) {
				sbWhere.append(" message_id = ?");
			} else {
				sbWhere.append(" message_id like ?");
				queryValues[0] = "%";
			}

			String conversationId = (String) queryValues[1];

			if (conversationId != null) {
				sbWhere.append(" AND conversation_id = ?");
			}

			sbWhere.append(getDatePart());

			StringBuffer newQuery = new StringBuffer();
			newQuery.append(query.substring(0, whereIndex));
			newQuery.append(sbWhere.toString());
			newQuery.append(query.substring(whereIndex));
			return newQuery.toString();
		} catch (Throwable th) {
			WrappedException we = new WrappedException("getQuery Exception",
					th, ErrorCode.APPLICATION_EXCEPTION);
			evl.sendException(EventType.EXCEPTION,
					ErrorCode.APPLICATION_EXCEPTION, we, this);
			throw we;
		}
	}

	protected String getDatePart() {
		StringBuffer datePart = new StringBuffer();

		String usePart = PropertyHelper.readPropsValue("USE_PARTITIONS",
				"jdbc.properties", null, "FALSE");

		Boolean part = new Boolean(usePart);
		if (part.booleanValue()) {
			Calendar cal = new GregorianCalendar();
			int dow = 0;

			datePart.append(" AND DATEPART = ");

			if (getAfterObjFilter() != null) {

				cal.setTime(getAfterObjFilter());
				dow = cal.get(7) - 2;
			} else if (getBeforeObjFilter() != null) {

				cal.setTime(getBeforeObjFilter());
				dow = cal.get(7) - 2;

			} else {
				Date date = new Date();
				cal.setTime(date);
				dow = cal.get(7) - 2;
			}

			if (dow < 0) {
				datePart.append("6");
			} else {
				datePart.append(dow);
			}

		} else {
			return "";
		}

		return datePart.toString();
	}
}
