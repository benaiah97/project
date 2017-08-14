package pvt.disney.dti.gateway.data.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;

/**
 * @author MISHP012 Test class for TicketTO
 */
public class TicketTOTestCase {

	private static TicketTO ticketTO;
	private String str = "1234";
	private BigInteger bi = new BigInteger(str);
	private BigDecimal bd = new BigDecimal(str);
	private String date = "2017-05-01";

	/**
	 * Setup for ticketTO-1
	 */
	private void setupTO1(TicketTO ticketTO) {
		ticketTO.setTktItem(bi);
		ticketTO.setProdCode(str);
		ticketTO.setFromProdCode(str);
		ticketTO.setProdQty(bi);
	}

	/**
	 * Setup for ticketTO-2
	 */
	private void setupTO2(TicketTO ticketTO2) {
		ticketTO.setProdPrice(bd);
		ticketTO.setUpgrdPrice(bd);
		ticketTO.setFromPrice(bd);
		ticketTO.setTktSecurityLevel(str);
		ticketTO.setTktMarket(str);
		ticketTO.setTktShell(str);
		ticketTO.setTktValidityValidStart(new GregorianCalendar());
		ticketTO.setTktValidityValidEnd(new GregorianCalendar());
		ticketTO.setTktNote(str);
		try {
			ticketTO.setDssn(date, null, str, str);
			ticketTO.setDssn(date, str, str, str);
		} catch (ParseException e) {

		}
		ticketTO.setBarCode(null);
		ticketTO.setBarCode(str);
		ticketTO.setTktNID(null);
		ticketTO.setTktNID(str);
		ticketTO.setExternal(null);
		ticketTO.setExternal(str);
		ticketTO.setTktPrice(bd);
		ticketTO.setTktTax(bd);
		ticketTO.setAgeGroup(str);
		ticketTO.setMediaType(str);
		ticketTO.setPassType(str);
		ticketTO.setPassClass(str);
		ticketTO.setResident(str);
		ticketTO.setPassRenew(str);
		ticketTO.setLastDateUsed(new GregorianCalendar());
		ticketTO.setTimesUsed(bi);
		ticketTO.setReplacedByPass(str);
		ticketTO.setTktTran(new TicketTransactionTO());
		ticketTO.setProviderTicketType(bi);
		ticketTO.setVisualId(str);
		ticketTO.setAccountId(str);
		ticketTO.setMag(str);
		ticketTO.setMag(null, str);
		ticketTO.setMag(str, str);
		ticketTO.setDssn(new GregorianCalendar(), str, str, str);
		ticketTO.setShowGroup(str);
		ticketTO.setExistingTktID(new TicketTO());
		ticketTO.setTicketDemoList(new ArrayList<DemographicsTO>());
		ticketTO.setTicketAssignmets(new ArrayList<TktAssignmentTO>());
		ticketTO.setPassName(str);
		try {
			ticketTO.setDssn(str, str, str, str);
		} catch (ParseException e) {

		}
		ticketTO.addTicketDemographic(new DemographicsTO());
		ticketTO.addTicketStatus(ticketTO.new TktStatusTO());
	}

	/**
	 * Instantiation for TicketTO
	 */
	@BeforeClass
	public static void beforeClass() {
		ticketTO = new TicketTO();
	}

	/**
	 * Test case for TicketTO clone() with Setup 1
	 */
	@Test
	public void testClone1() {
		try {
			setupTO1(ticketTO);
			ticketTO.clone();
		} catch (CloneNotSupportedException e) {

		}
	}

	/**
	 * Test case for TicketTO clone() with Setup 2
	 */
	@Test
	public void testClone2() {
		try {
			setupTO2(ticketTO);
			ticketTO.clone();
		} catch (CloneNotSupportedException e) {

		}
	}

	/**
	 * Test case for TicketTO tktStatus()
	 */
	@Test
	public void testTktStatus() {
		try {
			TicketTO.TktStatusTO tktStatusTO = ticketTO.new TktStatusTO();
			tktStatusTO.setStatusItem(str);
			tktStatusTO.setStatusValue(str);
		} catch (Exception e) {

		}
	}

	/**
	 * Test case for TicketTO tktAssignment()
	 */
	@Test
	public void testTktAssignment() {
		try {
			TicketTO.TktAssignmentTO tktStatusTO = ticketTO.new TktAssignmentTO();
			tktStatusTO.setAccountItem(bi);
			tktStatusTO.setProdQty(bi);
		} catch (Exception e) {

		}
	}

	/**
	 * Test case for TicketTO Enumeration
	 */
	@Test
	public void testTktEnum() {
		TicketTO.TicketIdType.valueOf("DSSN_ID");
	}

	/**
	 * Test case for TicketTO Object
	 */
	@Test
	public void testTktObj() {
		Assert.assertNotNull(ticketTO.getExistingTktID());
		Assert.assertNotNull(ticketTO.getAccountId());
		Assert.assertNotNull(ticketTO.getVisualId());
		Assert.assertNotNull(ticketTO.getFromPrice());
		Assert.assertNotNull(ticketTO.getUpgrdPrice());
		Assert.assertNotNull(ticketTO.getFromProdCode());
		Assert.assertNotNull(ticketTO.getTicketDemoList());
		Assert.assertNotNull(ticketTO.getTicketAssignmets());
		Assert.assertNotNull(ticketTO.getTimesUsed());
		Assert.assertNotNull(ticketTO.getReplacedByPass());
		Assert.assertNotNull(ticketTO.getPassName());
		Assert.assertNotNull(ticketTO.getLastDateUsed());
		Assert.assertNotNull(ticketTO.getProviderTicketType());
		Assert.assertNotNull(ticketTO.getPassRenew());
		Assert.assertNotNull(ticketTO.getTktSecurityLevel());
		Assert.assertNotNull(ticketTO.getTktMarket());
		Assert.assertNotNull(ticketTO.getTktShell());
		Assert.assertNotNull(ticketTO.getTktNote());
		Assert.assertNotNull(ticketTO.getProdQty());
		Assert.assertNotNull(ticketTO.getProdPrice());
		Assert.assertNotNull(ticketTO.getProdCode());
		Assert.assertNotNull(ticketTO.getResident());
		Assert.assertNotNull(ticketTO.getTktStatusList());
		Assert.assertNotNull(ticketTO.getTktValidityValidStart());
		Assert.assertNotNull(ticketTO.getTktValidityValidEnd());
		Assert.assertNotNull(ticketTO.getTktTax());
		Assert.assertNotNull(ticketTO.getTktPrice());
		Assert.assertNotNull(ticketTO.getPassType());
		Assert.assertNotNull(ticketTO.getPassClass());
		Assert.assertNotNull(ticketTO.getMediaType());
		Assert.assertNotNull(ticketTO.getAgeGroup());
		Assert.assertNotNull(ticketTO.getTktItem());
		Assert.assertNotNull(ticketTO.getTicketTypes());
		Assert.assertNotNull(ticketTO.getBarCode());
		Assert.assertNotNull(ticketTO.getDssnDate());
		Assert.assertNotNull(ticketTO.getTktNID());
		Assert.assertNotNull(ticketTO.getExternal());
		Assert.assertNotNull(ticketTO.getMagTrack1());
		Assert.assertNotNull(ticketTO.getMagTrack2());
		Assert.assertNotNull(ticketTO.getDssnStation());
		Assert.assertNotNull(ticketTO.getDssnSite());
		Assert.assertNotNull(ticketTO.getDssnNumber());
		Assert.assertNotNull(ticketTO.getDssnDateString());
		Assert.assertNotNull(TicketTO
				.getDssnDateString(new GregorianCalendar()));
		Assert.assertNotNull(ticketTO.getTktTran());
		Assert.assertNotNull(ticketTO.getShowGroup());
		Assert.assertNull(ticketTO.new TktStatusTO().getStatusItem());
		Assert.assertNull(ticketTO.new TktStatusTO().getStatusValue());
		Assert.assertNull(ticketTO.new TktAssignmentTO().getAccountItem());
		Assert.assertNull(ticketTO.new TktAssignmentTO().getProdQty());
	}

}