package pvt.disney.dti.gateway.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author MISHP012
 * 
 */
public class ResourceLoaderTestCase {

	/**
	 * JUnit for GetResourceBundle
	 */
	@Test
	public void testGetResourceBundle() {

		new MockUp<System>() {
			@Mock
			public String getProperty(String props) {
				return "test";
			}
		};
		Assert.assertNotNull(ResourceLoader
				.getResourceBundle("FloodControlVolumeTestCase"));
	}

	/**
	 * @throws MalformedURLException
	 */
	@Test
	public void testConvertResourceBundleToProperties()
			throws MalformedURLException {
		File file = new File("test");

		URL[] urls = { file.toURI().toURL() };
		ClassLoader loader = new URLClassLoader(urls);
		ResourceBundle requestedRB = ResourceBundle.getBundle(
				"FloodControlVolumeTestCase", Locale.getDefault(), loader);
		Assert.assertNotNull(ResourceLoader
				.convertResourceBundleToProperties(requestedRB));
	}
}
