package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import org.junit.Assert;
import org.junit.Test;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author MISHP012 
 * Test Cases for ElectronicEntitlementRules
 */
public class ElectronicEntitlementRulesTestCase extends CommonBusinessTest {

	/**
	 * JUnit for recordShellEntitlements
	 */
	@Test
	public void testRecordShellEntitlements() {

		/* Scenario :: 1 if transaction type is UPGRADEALPHA */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		getDTITransactionTO(dtiTxn, false);
		/* mocking insertUpgradedEntitlement method of ElectronicEntitlementKey */
		DTIMockUtil.mockinsertUpgradedEntitlement();
		/* mocking insertVoidedEntitlement method of ElectronicEntitlementKey */
		DTIMockUtil.mockinsertVoidedEntitlement();
		getDTITransactionTO(dtiTxn, false);
		ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);
		/* Scenario :: 2 if transaction type is VOIDTICKET */
		dtiTxn = new DTITransactionTO(TransactionType.VOIDTICKET);
		getDTITransactionTO(dtiTxn, false);
		ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);
		/* Scenario :: 3 if there is no ticket */
		getDTITransactionTO(dtiTxn, true);
		ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);
		/* Scenario :: 4 if providertype is not WDWNEXUS */
		dtiTxn.setProvider(ProviderType.HKDNEXUS);
		ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);
		DTIErrorTO dTIErrorTO = new DTIErrorTO(new BigInteger("400"),
				"errorClassIn", "errorTextIn", "errorTypeIn");
		/*
		 * Scenario :: 5 if dtiError is not null and not equals to
		 * PRICE_MISMATCH_WARN
		 */
		dtiTxn.getResponse().setDtiError(dTIErrorTO);
		ElectronicEntitlementRules.recordShellEntitlements(dtiTxn);
	}

	/**
	 * JUnit for validateSpecifiedAccounts
	 */
	@Test
	public void testValidateSpecifiedAccounts() {

		SpecifiedAccountTO specifiedAccountTO = new SpecifiedAccountTO();
		specifiedAccountTO.setAccountItem(new BigInteger("2"));
		TicketIdTO ticketIdTO = new TicketIdTO();
		ArrayList<SpecifiedAccountTO> accountList = new ArrayList<>();
		accountList.add(specifiedAccountTO);
		/*
		 * Scenario :: 1 Expected exception is Specified Account 2 had neither a
		 * new account or search criteria for an old account. There must be, at
		 * least, one.
		 */
		try {
			ElectronicEntitlementRules.validateSpecifiedAccounts(accountList);
			Assert.fail("Expected exception :Specified Account 2 had neither a new account or search criteria for an old account.  There must be, at least, one.");
		} catch (DTIException dtie) {
			assertEquals(
					"Specified Account 2 had neither a new account or search criteria for an old account.  There must be, at least, one.",
					dtie.getLogMessage());
		}
		ticketIdTO.setBarCode("EBG3F113Q08EHGD5M");
		specifiedAccountTO.setAccountItem(new BigInteger("2"));
		specifiedAccountTO.setExistingAccountId("2233");
		specifiedAccountTO.setExistingAccountId("12356");
		specifiedAccountTO.setExistingMediaId("2");
		specifiedAccountTO.setNewExternalReferenceType("External");
		/*
		 * Scenario :: 2 Expected exception is Specified Account 2 had both a
		 * new account and search criteria for an old account. There can be only
		 * one.
		 */
		try {
			ElectronicEntitlementRules.validateSpecifiedAccounts(accountList);
			Assert.fail("Specified Account 2 had both a new account and search criteria for an old account.  There can be only one.");
		} catch (DTIException dtie) {
			assertEquals(
					"Specified Account 2 had both a new account and search criteria for an old account.  There can be only one.",
					dtie.getLogMessage());
		}
	}

	/**
	 * @param dtiTxn
	 * @param type
	 * @param ticketlist
	 */
	private final void getDTITransactionTO(DTITransactionTO dtiTxn,
			boolean ticketlist) {
		String errorClassIn = "errorClassIn";
		String errorTextIn = "errorTextIn";
		String errorTypeIn = "errorTypeIn";
		TicketTO ticketTO = new TicketTO();
		ticketTO = new TicketTO();
		ticketTO.setProdCode("1");
		ticketTO.setDssn(new GregorianCalendar(), "site", "station", "2");
		ticketTO.setTktItem(new BigInteger("1"));
		ArrayList<DBProductTO> dBProductTOList = new ArrayList<DBProductTO>();
		DBProductTO dBProductTO = new DBProductTO();
		dBProductTO.setPdtCode("1");
		dBProductTO.setPdtid(new BigInteger("3"));
		dBProductTOList.add(dBProductTO);
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityId(2);
		DTIErrorTO dTIErrorTO = new DTIErrorTO(null, errorClassIn, errorTextIn,
				errorTypeIn);
		DTIResponseTO dTIResponseTO = new DTIResponseTO();
		dTIResponseTO.setDtiError(dTIErrorTO);
		PayloadHeaderTO payloadHeaderTO = new PayloadHeaderTO();
		payloadHeaderTO.setPayloadID("305201607190004");
		DTIRequestTO dTIRequestTO = new DTIRequestTO();
		dTIRequestTO.setPayloadHeader(payloadHeaderTO);
		ProviderType providerType = ProviderType.WDWNEXUS;
		dtiTxn.setResponse(dTIResponseTO);
		dtiTxn.setProvider(providerType);
		dtiTxn.setTransIdITS(2);
		dtiTxn.setEntityTO(entityTO);
		dtiTxn.setRequest(dTIRequestTO);
		if (dtiTxn.getTransactionType().compareTo(TransactionType.UPGRADEALPHA) == 0) {
			ArrayList<TicketTO> ticketList = new ArrayList<>();
			ticketList.add(ticketTO);
			UpgradeAlphaRequestTO upgradeAlphaRequestTO = new UpgradeAlphaRequestTO();
			upgradeAlphaRequestTO.setTktList(ticketList);
			UpgradeAlphaResponseTO upgradeAlphaResponseTO = new UpgradeAlphaResponseTO();
			upgradeAlphaResponseTO.addTicket(ticketTO);
			dtiTxn.setResponse(dTIResponseTO);
			dTIRequestTO.setCommandBody(upgradeAlphaRequestTO);
			dTIResponseTO.setCommandBody(upgradeAlphaResponseTO);
			dtiTxn.setRequest(dTIRequestTO);
			dtiTxn.setResponse(dTIResponseTO);
			dtiTxn.setDbProdList(dBProductTOList);
		}
		if (dtiTxn.getTransactionType().compareTo(TransactionType.VOIDTICKET) == 0) {

			VoidTicketResponseTO voidTicketResponseTO = new VoidTicketResponseTO();
			if (ticketlist != true)
				voidTicketResponseTO.addTicket(ticketTO);
			dtiTxn.setEntityTO(entityTO);
			dTIResponseTO.setCommandBody(voidTicketResponseTO);
			dtiTxn.setRequest(dTIRequestTO);
			dtiTxn.setResponse(dTIResponseTO);
			dtiTxn.setDbProdList(dBProductTOList);
		}
	}
}
