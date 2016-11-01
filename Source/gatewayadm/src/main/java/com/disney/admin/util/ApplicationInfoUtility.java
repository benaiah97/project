package com.disney.admin.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Loader;

/**
 * Factory used for providing Application Specific Information in 
 * a standard format.  Each Application should create their own 
 * implementation of this class.
 * 
 * @author FAV2
 * Created on Sep 9, 2003
 */
public class ApplicationInfoUtility
{
	private final static String APP_INFO_UTIL = "APP_INFO_UTIL";
	private final static ApplicationInfoUtility instance = new ApplicationInfoUtility();
	private static String appName = null;
	private static String buildVersion = null;
	private static String buildDate = null;
	private static String logoImageFileName = null;
	
	/**
	 * Constructor for ApplicationInfoUtility.
	 */
	protected ApplicationInfoUtility()
	{
		super();
	}

	/**
	 * Returns an Instance of ApplicationInfoUtility.
	 * Looks in the admin.properties file for key: APP_INFO_UTIL
	 * ... if not found, returns the default implementation.
	 * 
	 * @return ApplicationInfoUtility
	 */
	public static ApplicationInfoUtility getInstance()
	{
	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
		String aiuClassName = props.getProperty(APP_INFO_UTIL, null);
		
		ApplicationInfoUtility aiu = null;
		if (aiuClassName != null)
		{
			try
			{
				Class aiuClass = Loader.loadClass(aiuClassName);
				aiu = (ApplicationInfoUtility) aiuClass.newInstance();
			}
			catch (Exception e)
			{
				EventLogger evl = EventLogger.getLogger("ADMIN");
				evl.sendException("Error loading ApplicationInfoUtility:" + aiuClassName, 
					EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, instance);
			}
		}
		
		if (aiu == null)
		{
			aiu = new ApplicationInfoUtility();
      ApplicationInfoUtility.appName = props.getProperty("APP_NAME", "Application");
      ApplicationInfoUtility.buildVersion = props.getProperty("BUILD_VERSION", "1.0");
      ApplicationInfoUtility.buildDate = props.getProperty("BUILD_DATE", "");
			if (ApplicationInfoUtility.buildDate.equals(""))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MMddyy HHmm");
        ApplicationInfoUtility.buildDate = sdf.format(new Date());
			}
      ApplicationInfoUtility.logoImageFileName = props.getProperty("LOGO_FILE", "dd_logo.jpg");
		}
		
		return aiu;
	}

	public String getApplicationName()
	{
		return appName;
	}

	public String getAppServerName()
	{
		return AbstractInitializer.getInitializer().getAppName();
	}

	public String getBuildVersion()
	{
		return buildVersion;
	}

	public String getBuildDate()
	{
		return buildDate;
	}

	public String getLogoImageFileName()
	{
		return logoImageFileName;
	}
}
