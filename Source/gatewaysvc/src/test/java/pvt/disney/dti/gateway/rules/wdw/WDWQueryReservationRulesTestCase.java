package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInstallmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for WDWQueryReservationRules.
 *
 * @author ARORT002
 */
public class WDWQueryReservationRulesTestCase extends CommonTestUtils {

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
	}

	/**
	 * test case for transformRequest method.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryReservationRequestTO queryReservationRequestTO = new QueryReservationRequestTO();
		queryReservationRequestTO.setResCode("123");

		dtiTxn.getRequest().setCommandBody(queryReservationRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString = null;

		try {
			xmlString = WDWQueryReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		queryReservationRequestTO.setResCode(null);
		queryReservationRequestTO.setResNumber("456");
		dtiTxn.getRequest().setCommandBody(queryReservationRequestTO);

		try {
			xmlString = WDWQueryReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test case for transformResponseBody.
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.QUERYTICKET;

		OTManageReservationTO otMngResTO = new OTManageReservationTO();
		otMngResTO.setCommandType("commandType");

		ArrayList<OTTicketInfoTO> otTicketList = new ArrayList<OTTicketInfoTO>();
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		otTicketList.add(otTicketInfoTO);

		// otMngResTO no setter for ticketInfoList in OTManageReservationTO

		ArrayList<OTProductTO> dtiProdList = new ArrayList<OTProductTO>();
		OTProductTO productTO = new OTProductTO();
		productTO.setItem(BigInteger.valueOf(1));
		productTO.setTicketType(BigInteger.valueOf(1));
		productTO.setQuantity(BigInteger.valueOf(1));
		productTO.setPrice(BigDecimal.valueOf(2));
		productTO.setTax(BigDecimal.valueOf(2));
		productTO.setDescription("as");
		
		dtiProdList.add(productTO);
		otMngResTO.setProductList(dtiProdList);

		
		ArrayList<OTPaymentTO> otPmtList = new ArrayList<OTPaymentTO>();
		OTPaymentTO otPaymentTO = new OTPaymentTO();
		otPaymentTO.setPayType(OTPaymentTO.PaymentType.INSTALLMENT);
		OTInstallmentTO otinstall = new OTInstallmentTO();
		otinstall.setContractId("123");
		otPaymentTO.setInstallment(otinstall);
		otPmtList.add(otPaymentTO);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setReservationCode("ABC");
		//otMngResTO.
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		otCmdTO.setManageReservationTO(otMngResTO);
		OTTicketInfoTO ottTcket=new OTTicketInfoTO();
		ottTcket.setItemNumCode(new BigInteger("1"));
		ottTcket.setTicket(getOTicket());
		otMngResTO.getTicketInfoList().add(ottTcket);

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		/*if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}*/
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		otPmtList.clear();
		OTPaymentTO otPaymentTOCrdCard = new OTPaymentTO();
		otPaymentTOCrdCard.setPayType(OTPaymentTO.PaymentType.CREDITCARD);
		otPaymentTOCrdCard.setPayAmount(BigDecimal.valueOf(20));
		
		OTCreditCardTO otCreditCardTO = new OTCreditCardTO();
		otCreditCardTO.setGiftCardIndicator(true);
		otCreditCardTO.setAuthErrorCode("123");
		otCreditCardTO.setAuthNumber("123");
		otCreditCardTO.setCcNumber("123");
		otCreditCardTO.setRemainingBalance(BigDecimal.valueOf(20));
		otCreditCardTO.setPromoExpDate(new GregorianCalendar());
		otPaymentTOCrdCard.setCreditCard(otCreditCardTO);
		
		otPmtList.add(otPaymentTOCrdCard);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		otPmtList.clear();
		otPaymentTOCrdCard.setPayType(OTPaymentTO.PaymentType.CREDITCARD);
		otPaymentTOCrdCard.setPayAmount(BigDecimal.valueOf(20));
		
		otCreditCardTO.setGiftCardIndicator(false);
		otCreditCardTO.setAuthErrorCode("123");
		otCreditCardTO.setAuthNumber("123");
		otCreditCardTO.setCcNumber("123");
		otCreditCardTO.setRemainingBalance(BigDecimal.valueOf(20));
		otCreditCardTO.setPromoExpDate(new GregorianCalendar());
		otPaymentTOCrdCard.setCreditCard(otCreditCardTO);
		
		otPmtList.add(otPaymentTOCrdCard);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		QueryReservationRequestTO dtiResReq = new QueryReservationRequestTO();
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otPmtList.clear();
		otPaymentTOCrdCard.setPayType(OTPaymentTO.PaymentType.CREDITCARD);
		otPaymentTOCrdCard.setPayAmount(BigDecimal.valueOf(20));
		
		otCreditCardTO.setGiftCardIndicator(false);
		otCreditCardTO.setAuthErrorCode("123");
		otCreditCardTO.setAuthNumber("123");
		otCreditCardTO.setCcNumber("123");
		otCreditCardTO.setRemainingBalance(BigDecimal.valueOf(20));
		otCreditCardTO.setPromoExpDate(new GregorianCalendar());
		otPaymentTOCrdCard.setCreditCard(otCreditCardTO);
		
		otPmtList.add(otPaymentTOCrdCard);
		
		OTClientDataTO otClientDataTO = new OTClientDataTO();
		otClientDataTO.setClientUniqueId(123);
		
		ArrayList<OTFieldTO> otFieldTOList = new ArrayList<OTFieldTO>();
		OTFieldTO otFieldTOFirstName = new OTFieldTO(1, "ABC");
		otFieldTOList.add(otFieldTOFirstName);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOLastName = new OTFieldTO(2, "ABC");
		otFieldTOList.add(otFieldTOLastName);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTO = new OTFieldTO(3, "ABC");
		otFieldTOList.add(otFieldTO);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOAddr1 = new OTFieldTO(4, "ABC");
		otFieldTOList.add(otFieldTOAddr1);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOAddr2 = new OTFieldTO(5, "ABC");
		otFieldTOList.add(otFieldTOAddr2);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOCity = new OTFieldTO(6, "ABC");
		otFieldTOList.add(otFieldTOCity);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOState = new OTFieldTO(7, "ABC");
		otFieldTOList.add(otFieldTOState);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOZip = new OTFieldTO(8, "ABC");
		otFieldTOList.add(otFieldTOZip);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOcountry = new OTFieldTO(9, "ABC");
		otFieldTOList.add(otFieldTOcountry);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOtelephone = new OTFieldTO(10, "ABC");
		otFieldTOList.add(otFieldTOtelephone);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipName = new OTFieldTO(11, "ABC");
		otFieldTOList.add(otFieldTOShipName);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOEmail = new OTFieldTO(26, "ABC");
		otFieldTOList.add(otFieldTOEmail);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOResNbr = new OTFieldTO(27, "ABC");
		otFieldTOList.add(otFieldTOResNbr);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipLastName = new OTFieldTO(12, "ABC");
		otFieldTOList.add(otFieldTOShipLastName);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipFirstName = new OTFieldTO(13, "ABC");
		otFieldTOList.add(otFieldTOShipFirstName);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipAddr1 = new OTFieldTO(14, "ABC");
		otFieldTOList.add(otFieldTOShipAddr1);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipAddr2 = new OTFieldTO(15, "ABC");
		otFieldTOList.add(otFieldTOShipAddr2);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOShipCity = new OTFieldTO(16, "ABC");
		otFieldTOList.add(otFieldTOShipCity);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOshipState = new OTFieldTO(17, "ABC");
		otFieldTOList.add(otFieldTOshipState);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOshipZip = new OTFieldTO(18, "ABC");
		otFieldTOList.add(otFieldTOshipZip);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOshipCountry = new OTFieldTO(19, "ABC");
		otFieldTOList.add(otFieldTOshipCountry);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiResReq.setIncludeResDemographics(true);
		dtiTxn.getRequest().setCommandBody(dtiResReq);
		otClientDataTO.setClientUniqueId(123);
		
		otFieldTOList.clear();
		OTFieldTO otFieldTOshipTel = new OTFieldTO(20, "ABC");
		otFieldTOList.add(otFieldTOshipTel);
		otClientDataTO.setDemographicData(otFieldTOList);
		
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setClientData(otClientDataTO);
		otCmdTO.setManageReservationTO(otMngResTO);

		
		
		try {
			WDWQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Test apply wdw associate media to account rules.
	 */
	@Test
	public void testApplyWDWQueryReservationRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		QueryTicketRequestTO reqTO = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(reqTO);
		try {
			WDWQueryReservationRules.applyWDWQueryReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

}
