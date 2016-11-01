package pvt.disney.dti.gateway.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public  class ResourceLoader {

	//default Constructor
	private ResourceLoader () {
	}
	
	static public ResourceBundle getResourceBundle(String bundleName) {

		ResourceBundle requestedRB = null;

		try {

			// Gateway_Config is Java property defined in the Tomcat
			// configuration

			String Gateway_Config = System.getProperty("Gateway_Config");

			File file = new File(Gateway_Config);

			URL[] urls = { file.toURI().toURL() };
			ClassLoader loader = new URLClassLoader(urls);
			requestedRB = ResourceBundle.getBundle(bundleName,
					Locale.getDefault(), loader);

		} catch (Exception ex) {
			System.out.println("Property Bundle: " + bundleName
					+ " could not be located.");
			ex.printStackTrace();
		}
		return requestedRB;

	}

	/**
	 * Convert ResourceBundle into a Properties object.
	 * 
	 * @param resource
	 *            a resource bundle to convert.
	 * @return Properties a properties version of the resource bundle.
	 */
	@SuppressWarnings("rawtypes")
	static public Properties convertResourceBundleToProperties(
			ResourceBundle resource) {
		Properties properties = new Properties();

		Enumeration keys = resource.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			properties.put(key, resource.getString(key));
		}

		return properties;
	}
}
