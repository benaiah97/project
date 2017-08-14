package pvt.disney.dti.gateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.test.util.TestUtilities;

/**
 * @author MISHP012 JUnit for UtlityXML
 */
public class UtilityXMLTestCase {

	/**
	 * TastCase for getGCalFromEGalaxyDate
	 */
	@Test
	public void testGetGCalFromEGalaxyDate() {
		String dateString = "2013-12-22 12:12:12";
		Assert.assertNotNull(UtilityXML.getGCalFromEGalaxyDate(dateString));
		dateString = "12-22 12:12:12";
		UtilityXML.getGCalFromEGalaxyDate(dateString);
	}

	/**
	 * TastCase for getEGalaxyDateFromGCal
	 */
	@Test
	public void testGetEGalaxyDateFromGCal() {
		GregorianCalendar timeStamp = new GregorianCalendar();
		Date date = new Date(12 - 12 - 12);
		timeStamp.setTime(date);
		Assert.assertNotNull(UtilityXML.getEGalaxyDateFromGCal(timeStamp));
	}

	/**
	 * TastCase for getNexusDateFromGCal
	 */
	@Test
	public void testGetNexusDateFromGCal() {
		GregorianCalendar dateStamp = new GregorianCalendar();
		Date date = new Date(2012 - 03 - 12);
		dateStamp.setTime(date);
		Assert.assertNotNull(UtilityXML.getNexusDateFromGCal(dateStamp));
	}

	/**
	 * TastCase for getGCalFromNexusDate
	 */
	@Test
	public void testGetGCalFromNexusDate() {
		String dateString = "2016-12-06";
		Assert.assertNotNull(UtilityXML.getGCalFromNexusDate(dateString));
		dateString = "12-06";
		UtilityXML.getGCalFromNexusDate(dateString);
	}

	/**
	 * TastCase for getCurrentDTITime
	 */
	@Test
	public void testGetCurrentDTITime() {
		Assert.assertNotNull(UtilityXML.getCurrentDTITime());
	}

	/**
	 * TastCase for getEGalaxyDateFromGCal
	 */
	@Test
	public void testGetCurrentDTIDate() {
		Assert.assertNotNull(UtilityXML.getCurrentDTIDate());
	}

	/**
	 * TastCase for isTagPresent
	 */
	@Test
	public void testIsTagPresent() throws IOException {
		String tagName = null;

		InputStream fileName = this.getClass().getResourceAsStream(
				"/pci/PCIControlTest01.xml");
		String baselineXML = TestUtilities.getXMLFromFile(fileName);
		tagName = "Endorsment";
		UtilityXML.isTagPresent(baselineXML, tagName);
		tagName = "Endorsement";
		UtilityXML.isTagPresent(baselineXML, tagName);
	}

	/**
	 * TastCase for getStringToInputStream
	 */
	@Test
	public void testGetStringToInputStream() {
		String str = "test";
		Assert.assertNotNull(UtilityXML.getStringToInputStream(str));
	}

	/**
	 * TastCase for getTagData
	 */

	@Test
	public void testGetTagData() throws IOException {
		InputStream fileName = this.getClass().getResourceAsStream(
				"/pci/PCIControlTest01.xml");
		String baselineXML = TestUtilities.getXMLFromFile(fileName);
		String tagName = null;
		tagName = "Endorsment";
		UtilityXML.getTagData(baselineXML, tagName);
		tagName = "Endorsement";
		UtilityXML.getTagData(baselineXML, tagName);
	}

	/**
	 * TastCase for getRecurringTagData
	 */
	@Test
	public void testGetRecurringTagData() throws IOException {
		InputStream fileName = this.getClass().getResourceAsStream(
				"/pci//PCIControlTest01.xml");
		String baselineXML = TestUtilities.getXMLFromFile(fileName);
		String tagName = null;
		tagName = "Endorsement";
		UtilityXML.getRecurringTagData(baselineXML, tagName);
	}
}
