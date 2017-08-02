package pvt.disney.dti.gateway.rules.hkd;

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
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTInstallmentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for HKDQueryReservationRules.
 *
 * @author ARORT002
 */
public class HKDQueryReservationRulesTestCase extends CommonTestUtils {

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
		String xmlString = null;
		QueryReservationRequestTO reservationRequestTO = new QueryReservationRequestTO();
		reservationRequestTO.setResCode("123");
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = HKDQueryReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		reservationRequestTO.setResCode(null);
		reservationRequestTO.setResNumber("456");
		dtiTxn.getRequest().setCommandBody(reservationRequestTO);
		try {
			xmlString = HKDQueryReservationRules.transformRequest(dtiTxn);
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

		HkdOTManageReservationTO otMngResTO = new HkdOTManageReservationTO();
		HkdOTTicketInfoTO otTicketInfo = new HkdOTTicketInfoTO();
		otTicketInfo.setItem(BigInteger.valueOf(1));
		HkdOTTicketTO otTicketTO = new HkdOTTicketTO();
		otTicketTO.setBarCode("ABC");
		otTicketInfo.setTicket(otTicketTO);
		otTicketInfo.setValidityStartDate(new GregorianCalendar());
		otTicketInfo.setValidityEndDate(new GregorianCalendar());
		otMngResTO.getTicketInfoList().add(otTicketInfo);

		ArrayList<HkdOTProductTO> otProductList = new ArrayList<HkdOTProductTO>();
		HkdOTProductTO hkdOTProductTO = new HkdOTProductTO();
		hkdOTProductTO.setItem(BigInteger.valueOf(1));
		hkdOTProductTO.setItemAlphaCode("123");
		hkdOTProductTO.setQuantity(BigInteger.valueOf(1));
		hkdOTProductTO.setPrice(BigDecimal.valueOf(1));
		hkdOTProductTO.setTax(BigDecimal.valueOf(1));
		hkdOTProductTO.setDescription("asasas");
		otProductList.add(hkdOTProductTO);
		otMngResTO.setProductList(otProductList);

		ArrayList<HkdOTPaymentTO> otPmtList = new ArrayList<HkdOTPaymentTO>();
		HkdOTPaymentTO hkdOTPaymentTO = new HkdOTPaymentTO();
		hkdOTPaymentTO.setPayType(HkdOTPaymentTO.PaymentType.INSTALLMENT);
		HkdOTInstallmentTO installment = new HkdOTInstallmentTO();
		installment.setContractId("123");
		hkdOTPaymentTO.setInstallment(installment);
		otPmtList.add(hkdOTPaymentTO);
		otMngResTO.setPaymentInfoList(otPmtList);
		otMngResTO.setReservationCode("ABC");
		HkdOTClientDataTO otClientDataTO = new HkdOTClientDataTO();
		otClientDataTO.setClientUniqueId(123);
		otMngResTO.setClientData(otClientDataTO);
		HkdOTCommandTO otCmdTO = new HkdOTCommandTO(
				pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO.OTTransactionType.MANAGERESERVATION);
		otCmdTO.setManageReservationTO(otMngResTO);

		DTIMockUtil.processMockprepareAndExecuteSql();

		DTIResponseTO dtiRespTO = new DTIResponseTO();
		try {
			HKDQueryReservationRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test apply hkd query reservation rules.
	 */
	@Test
	public void testApplyHKDQueryReservationRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		QueryReservationRequestTO queryResReqTO = new QueryReservationRequestTO();
		queryResReqTO.setPayloadID("123");
		dtiTxn.getRequest().setCommandBody(queryResReqTO);

		DTIMockUtil.mockGetTransidRescodeFromDB();
		try {
			HKDQueryReservationRules.applyHKDQueryReservationRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

}
