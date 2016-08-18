package com.disney.admin.statistics;

import java.util.Date;

import org.apache.log4j.Level;

import com.disney.admin.SystemStartupListener;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.Event;
import com.disney.logging.audit.EventType;
import com.disney.logging.audit.MessageInfo;
import com.disney.logging.audit.TraceInfo;

/**
 * Test that can be run as a SystemStarupistener that delivers
 * a series of START and STOP events to the logging system.
 * 
 * @author FAV2
 */
public class StatisticsTestStartupListener implements SystemStartupListener
{
	public static final StatisticsTestStartupListener instance = new StatisticsTestStartupListener();
	public static final MyEventType CORE_START = instance.new MyEventType("CORE_START", Level.INFO);
	public static final MyEventType CORE_STOP = instance.new MyEventType("CORE_STOP", Level.INFO);
	public static final MyEventType MAJOR_START = instance.new MyEventType("MAJOR_START", Level.INFO);
	public static final MyEventType MAJOR_STOP = instance.new MyEventType("MAJOR_STOP", Level.INFO);

	public class MyEventType extends EventType
	{
		protected MyEventType(String value, Level level)
		{
			super(value, level);
		}
	}
	
	/**
	 * Constructor for StatisticsTestStartupListener.
	 */
	public StatisticsTestStartupListener()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see com.disney.admin.SystemStartupListener#onSystemStartup()
	 */
	public void onSystemStartup()
	{
		EventLogger evl = EventLogger.getLogger("STATS");

		MessageInfo mInfo = new MessageInfo();
		mInfo.setConversationId("CONV_1234567890");
		mInfo.setMessageId("MSGID_1234567890");
		mInfo.setServiceType("SampleService");

		TraceInfo domainTraceInfo = new TraceInfo();
		domainTraceInfo.setComponentId("domain");

		TraceInfo sampleTraceInfo = new TraceInfo();
		sampleTraceInfo.setComponentId("sample");

		long time = System.currentTimeMillis();

		Event coreStart = new Event();
		coreStart.setType(CORE_START);
		coreStart.setLogLevel(10);
		coreStart.setLogObjectName("SampleAppObject");
		coreStart.setMessageInfo(mInfo);
		coreStart.setTraceInfo(domainTraceInfo);
		coreStart.setCreationDate(new Date(time));
		evl.sendEvent(coreStart);

		time += 1000;
		Event majorStart = new Event();
		majorStart.setType(MAJOR_START);
		majorStart.setLogLevel(10);
		majorStart.setLogObjectName("SampleAppObject");
		majorStart.setMessageInfo(mInfo);
		majorStart.setTraceInfo(sampleTraceInfo);
		majorStart.setCreationDate(new Date(time));
		evl.sendEvent(majorStart);

		time += 1500;
		Event esStart = new Event();
		esStart.setType(CORE_START);
		esStart.setLogLevel(10);
		esStart.setLogObjectName("SampleExternalSystemObject");
		esStart.setMessageInfo(mInfo);
		esStart.setTraceInfo(sampleTraceInfo);
		esStart.setCreationDate(new Date(time));
		evl.sendEvent(esStart);

		time += 5000;
		Event esStop = new Event();
		esStop.setType(CORE_STOP);
		esStop.setLogLevel(10);
		esStop.setLogObjectName("SampleExternalSystemObject");
		esStop.setMessageInfo(mInfo);
		esStop.setTraceInfo(sampleTraceInfo);
		esStop.setCreationDate(new Date(time));
		evl.sendEvent(esStop);

		time += 500;
		Event majorStop = new Event();
		majorStop.setType(MAJOR_STOP);
		majorStop.setLogLevel(10);
		majorStop.setLogObjectName("SampleAppObject");
		majorStop.setMessageInfo(mInfo);
		majorStop.setTraceInfo(sampleTraceInfo);
		majorStop.setCreationDate(new Date(time));
		evl.sendEvent(majorStop);

		time += 1000;
		Event coreStop = new Event();
		coreStop.setType(CORE_STOP);
		coreStop.setLogLevel(10);
		coreStop.setLogObjectName("SampleAppObject");
		coreStop.setMessageInfo(mInfo);
		coreStop.setTraceInfo(domainTraceInfo);
		coreStop.setCreationDate(new Date(time));
		evl.sendEvent(coreStop);
	}

}
