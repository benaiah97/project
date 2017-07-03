package com.disney.dti.automation;

import java.util.ResourceBundle;

import org.junit.Assert;
import org.junit.Test;

import com.eviware.soapui.tools.SoapUITestCaseRunner;

/**
 * Test class for SoapUI Automation
 * 
 * @author GANDV005
 *
 */
public class DTISoapUITest {

	/**
	 * Test case for DTI-GQE SoapUI Automation
	 */
	@Test
	public void testDTISoapUI() {

		ResourceBundle resourceBundle = ResourceBundle.getBundle("com.disney.dti.automation.xmlPath");
		String GQE_XML_PATH = resourceBundle.getString("GQE_XML_PATH");

		SoapUITestCaseRunner runner = new SoapUITestCaseRunner();
		runner.setSettingsFile(GQE_XML_PATH);
		runner.setProjectFile(GQE_XML_PATH);

		try {
			runner.run();
			if (!runner.getAssertionResults().isEmpty()) {
				throw new Exception();
			}
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}

	}

}
