package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO.TPLookupType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWMemberDemographicsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderContactTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderLineTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRespTO.TicketRecord;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentContractTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.UtilityXML;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeUpgradeEntitlementXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.ProductRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

public class DLRUpgradeEntitlementRules implements TransformConstants {
	
	/**
	 * Transforms the request in the DTI transfer object into a valid eGalaxy
	 * request string.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object
	 * @return eGalaxy request string
	 * @throws DTIException
	 *             should a problem with conversion occur
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.ORDERS);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWOrdersRqstTO ordersReqTO = new GWOrdersRqstTO();

		UpgradeEntitlementRequestTO upgradeRequest = (UpgradeEntitlementRequestTO) dtiTxn.getRequest().getCommandBody();

		// CREATE the ORDER TO
		GWOrderTO orderTO = createOrderTO(dtiTxn);

		// Installment Fields - Set Contract clause and sales program in the
		// Order
		// clause
		if (upgradeRequest.isInstallmentRequest()) {

			GWPaymentContractTO contract = new GWPaymentContractTO();

			// Set Constants
			contract.setRecurrenceType(GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE);
			contract.setInterval(GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL);
			contract.setRenewContract(GW_ORDERS_CONTRACT_RENEW_CONTRACT);
			contract.setPaymentContractStatusID(GW_ORDERS_CONTRACT_STATUS_ID);
			if (upgradeRequest.getInstallmentDownpayment() != null) {
				String downPaymentAmount = upgradeRequest.getInstallmentDownpayment().toString();
				contract.setDownPaymentAmount(downPaymentAmount);
			}
			contract.setContactMethod(GW_ORDERS_CONTRACT_CONTACT_METHOD);

			// Get TP Lookups - note, must be list used here, not hash as
			// several installment values use the same key...
			ArrayList<TPLookupTO> tpLookupList = dtiTxn.getTpLookupTOList();

			// Loop through the tpList and build an "installment" hash map.
			HashMap<String, String> instLookupMap = new HashMap<String, String>();
			for (/* each */TPLookupTO aTPLookup : /* in */tpLookupList) {
				if (aTPLookup.getLookupType() == TPLookupType.INSTALLMENT) {
					instLookupMap.put(aTPLookup.getLookupDesc(), aTPLookup.getLookupValue());
				}
			}

			// Sales Program (order level, not contract level)
			String salesProgram = instLookupMap.get(GW_ORDERS_SALESPROGRAM_DBKEY);
			if (salesProgram == null) {
				throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"Internal Error:  DLR Installmnt SalesProgram not in TP_LOOKUP Table.");
			}
			orderTO.setSalesProgram(salesProgram);

			GregorianCalendar gCal = new GregorianCalendar();

			// Day of Month
			contract.setDayOfMonth(Integer.valueOf(gCal.get(Calendar.DAY_OF_MONTH)));

			// Start Date (Today +1 month)
			gCal.add(Calendar.MONTH, 1);
			String startDate = UtilityXML.getEGalaxyDateFromGCalNoTime(gCal);
			contract.setStartDate(startDate);

			// End Date (Start date + 10 months (11 months from today))
			gCal.add(Calendar.MONTH, 10);
			String endDate = UtilityXML.getEGalaxyDateFromGCalNoTime(gCal);
			contract.setEndDate(endDate);

			// Add the contract to the order
			orderTO.addPaymentContract(contract);

		} // is installment

		// Set OrderLine(s), product info
		ArrayList<TicketTO> dtiTktList = upgradeRequest.getTicketList();

		// Get the product map so for various product related value lookups
		HashMap<String, DBProductTO> dtiProductMap = dtiTxn.getDbProdMap();

		// Set the order lines, returning the order total (required by the
		// header)
		BigDecimal orderTotal = setProductOrderLines(upgradeRequest, orderTO, dtiProductMap, dtiTktList);

		// Order>OrderTotal: Set the order total based on the products
		orderTO.setOrderTotal(orderTotal);

		// Set OrderLine(s), payments info
		// use this to get order line item that are payments
		HashMap<String, String> payCardMap = dtiTxn.getPaymentCardMap();
		boolean missingCvv = setPaymentOrderLines(upgradeRequest, orderTO, orderTotal, payCardMap);

		// Set the source ID to the TS MAC
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller().getTsMac();
		if (missingCvv) {
			// credit card w/o CVV, and from WDPro send to secondary DLR source
			if (GW_WDPRO.compareTo(sourceID) == 0)
				sourceID = GW_WDPRO_NO_CVV;
		}
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader().getPayloadID());

		// Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
		headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_ORDERS_MESSAGE_TYPE);

		ordersReqTO.addOrder(orderTO);

		bodyTO.setOrdersRqstTO(ordersReqTO);
		envelopeTO.setHeaderTO(headerTO);
		envelopeTO.setBodyTO(bodyTO);

		xmlRequest = GWEnvelopeUpgradeEntitlementXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * 
	 * @param dtiTxn
	 * @return
	 * @throws DTIException
	 */
	private static GWOrderTO createOrderTO(DTITransactionTO dtiTxn) throws DTIException {

		GWOrderTO orderTO = new GWOrderTO();

		UpgradeEntitlementRequestTO resReq = (UpgradeEntitlementRequestTO) dtiTxn.getRequest().getCommandBody();

		// OrderID
		orderTO.setOrderID(resReq.getReservation().getResCode());
		
		// CustomerID
		// if the override customer ID is present, use it, otherwise use what's
		// in the database
		// TODO resReq.getEligibilityGroup()
		/*if (resReq.getEligibilityGroup() != null
				&& resReq.getEligibilityGroup().equalsIgnoreCase(TransformConstants.GW_ORDERS_DLR_ELIGIBILITY_GROUP)) {
			orderTO.setCustomerID(resReq.getEligibilityMember());
		} else {
			orderTO.setCustomerID(dtiTxn.getEntityTO().getCustomerId());
		}*/
		orderTO.setCustomerID(dtiTxn.getEntityTO().getCustomerId());

		// OrderDate
		orderTO.setOrderDate(UtilityXML.getEGalaxyDateFromGCal(DateTimeRules.getGMTDateNow()));

		// OrderStatus ( 1 if unpaid, 2 if paid )
		ArrayList<PaymentTO> payListTO = resReq.getPaymentList();
		if (payListTO.size() > 0) {
			orderTO.setOrderStatus(TransformConstants.GW_ORDERS_PAID_ORDER_STATUS);
		} else {
			orderTO.setOrderStatus(TransformConstants.GW_ORDERS_UNPAID_ORDER_STATUS);
		}

		// SalesProgram (include if the order is installment) as of 2.16.1, JTL
		// Also, since values will be required later, put together a map of keys
		// and values for ready access.
		HashMap<String, String> instTPLookupMap = new HashMap<String, String>();
		if (resReq.isInstallmentRequest()) {

			ArrayList<TPLookupTO> tpLookupList = dtiTxn.getTpLookupTOList();
			String salesProgram = null;
			for (/* each */TPLookupTO aTPLookup : /* in */tpLookupList) {

				if ((aTPLookup.getLookupDesc().equalsIgnoreCase("SalesProgram"))
						&& (aTPLookup.getLookupType() == TPLookupTO.TPLookupType.INSTALLMENT)) {
					salesProgram = aTPLookup.getLookupValue();
				}

				instTPLookupMap.put(aTPLookup.getLookupDesc(), aTPLookup.getLookupValue());

			} // for loop

			if (salesProgram == null) {
				throw new DTIException(DLRReservationRules.class, DTIErrorCode.UNDEFINED_FAILURE,
						"GWTPLookup for SalesProgram is missing in the database.");
			} else {
				orderTO.setSalesProgram(salesProgram);
			}

		} // If is installment request

		// OrderTotal (as of 2.16.1, JTL)
		if (resReq.isInstallmentRequest()) {
			orderTO.setOrderTotal(resReq.getTotalOrderAmount());
		}

		// OrderReference
		// If the order is a "BOLT" order, then put the LMS number in Order
		// Reference.
		// TODO resReq.getEligibilityGroup()
		/*if (resReq.getEligibilityGroup() != null) {
			if (resReq.getEligibilityGroup().equalsIgnoreCase(GW_ORDERS_DLR_BOLT_GROUP)) {
				if (resReq.getEligibilityMember() != null) {
					orderTO.setOrderReference(resReq.getEligibilityMember());
				}
			}
		}*/

		// PaymentContracts (as of 2.16.1, JTL)
		if (resReq.isInstallmentRequest()) {

			GWPaymentContractTO payContract = new GWPaymentContractTO();

			// RecurrenceType
			payContract.setRecurrenceType(GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE);

			// DayOfMonth (Set to today's day).
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
			String dayString = dateFormat.format(new Date());
			payContract.setDayOfMonth(new Integer(dayString));

			// Interval
			payContract.setInterval(GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL);

			// StartDate (+1 month from today including last day of month logic)
			GregorianCalendar startCal = new GregorianCalendar();
			startCal.add(Calendar.MONTH, 1);
			payContract.setStartDate(UtilityXML.getEGalaxyDateFromGCal(startCal));

			// EndDate (+11 months from today)
			GregorianCalendar endCal = new GregorianCalendar();
			startCal.add(Calendar.MONTH, 11);
			payContract.setEndDate(UtilityXML.getEGalaxyDateFromGCal(endCal));

			// PaymentPlanID
			String paymentPlan = null;
			// TODO resReq.getEligibilityGroup()
			/*if (resReq.getEligibilityMember().equalsIgnoreCase(SOCA_RES)) {
				paymentPlan = instTPLookupMap.get(GW_ORDERS_SOCARENEWPLAN);
			} else {
				paymentPlan = instTPLookupMap.get(GW_ORDERS_CARENEWPLAN);
			}
			if (paymentPlan == null) {
				throw new DTIException(DLRReservationRules.class, DTIErrorCode.UNDEFINED_FAILURE, "GWTPLookup for "
						+ GW_ORDERS_SOCARENEWPLAN + " or " + GW_ORDERS_CARENEWPLAN + " is missing in the database.");
			}*/
			payContract.setPaymentPlanID(paymentPlan);

			// Renew Contract
			payContract.setRenewContract(GW_ORDERS_CONTRACT_RENEW_CONTRACT);

			// PaymentContractStatusID
			payContract.setPaymentContractStatusID(GW_ORDERS_CONTRACT_STATUS_ID);

			// ContactMethod
			payContract.setContactMethod(GW_ORDERS_CONTRACT_CONTACT_METHOD);

		}

		// BEGIN OrderContactInfo field
		// set order contact info using billing demographics info
		DemographicsTO demoTO = resReq.getClientData().getBillingInfo();
		GWOrderContactTO orderContactTO = new GWOrderContactTO();
		orderContactTO.setFirstName(demoTO.getFirstName());
		orderContactTO.setLastName(demoTO.getLastName());
		orderContactTO.setStreet1(demoTO.getAddr1());
		if (demoTO.getAddr2() != null) {
			orderContactTO.setStreet2(demoTO.getAddr2());
		}

		// Since 2.11 - If group is "BOLT", then "org name" goes to Street 3.
		// TODO resReq.getEligibilityGroup()
		/*if (resReq.getEligibilityGroup() != null) {
			if (resReq.getEligibilityGroup().equalsIgnoreCase(GW_ORDERS_DLR_BOLT_GROUP)) {
				if (demoTO.getName() != null) {
					orderContactTO.setStreet3(demoTO.getName());
				}
			}
		}*/

		orderContactTO.setCity(demoTO.getCity());
		// WDPRO FIX
		if (demoTO.getState() != null && demoTO.getState().length() > 0) {
			orderContactTO.setState(demoTO.getState());
		}
		orderContactTO.setZip(demoTO.getZip());
		orderContactTO.setCountry(demoTO.getCountry());
		orderContactTO.setPhone(demoTO.getTelephone());
		orderContactTO.setEmail(demoTO.getEmail());
		// add the contact on the order
		orderTO.setOrderContact(orderContactTO);
		// END OrderContactInfo field

		// BEGIN ShipToContact field
		// set ship to contact info using shipping info
		GWOrderContactTO shipContactTO = new GWOrderContactTO();
		DemographicsTO shippingTO = resReq.getClientData().getShippingInfo();

		if (shippingTO != null) { // As of 2.12 , permit Shipping to be absent.
			if (shippingTO.getFirstName() != null) {
				shipContactTO.setFirstName(shippingTO.getFirstName());
			}
			if (shippingTO.getLastName() != null) {
				shipContactTO.setLastName(shippingTO.getLastName());
			}
			shipContactTO.setStreet1(shippingTO.getAddr1());
			if (shippingTO.getAddr2() != null) {
				shipContactTO.setStreet2(shippingTO.getAddr2());
			}

			// Since 2.11 - If group is "BOLT", then "org name" goes to Street
			// 3.
			// TODO resReq.getEligibilityGroup()
			/*if (resReq.getEligibilityGroup() != null) {
				if (resReq.getEligibilityGroup().equalsIgnoreCase(GW_ORDERS_DLR_BOLT_GROUP)) {
					if (shippingTO.getName() != null) {
						shipContactTO.setStreet3(shippingTO.getName());
					}
				}
			}*/

			shipContactTO.setCity(shippingTO.getCity());
			// WDPROFIX
			if (shippingTO.getState() != null && shippingTO.getState().length() > 0) {
				shipContactTO.setState(shippingTO.getState());
			}
			shipContactTO.setZip(shippingTO.getZip());
			shipContactTO.setCountry(shippingTO.getCountry());
			shipContactTO.setPhone(shippingTO.getTelephone());
			shipContactTO.setEmail(shippingTO.getEmail());
			// add the contact on the order
			orderTO.setShipToContact(shipContactTO);
			// END ShipToContact field
		} else { // as of 2.16.1, JTL
			if (resReq.isInstallmentRequest()) {
				shipContactTO.setSameAsOrderContact(true);
			}
		}

		// BEGIN Shipping field & children, ShipDetails, ShipMethod
		// use the lookup map for details & lookup our various values
		try {

			orderTO.setShipDeliveryDetails(GW_AUTO_ACTIVATION);
			orderTO.setShipDeliveryMethod(GW_SHIP_DELIVERY); // delivery method
																// for renewals.

			String resPickup = UtilityXML.getEGalaxyDateFromGCalNoTime(resReq.getReservation().getResPickupDate());
			orderTO.setGroupVisitDate(resPickup + "00:00:00");

			// Since 2.9 Put bill name in group description (per Art Wightman)
			// Change for 2.12 - No longer check to see if shipping method is 1,
			// 2, or
			// 6.
			// No longer require a payment clause to be present on the in-bound
			// request.
			if ((resReq.getClientData().getBillingInfo() != null)
					&& (resReq.getClientData().getBillingInfo().getName() != null)) {

				orderTO.setGroupVisitDescription(resReq.getClientData().getBillingInfo().getName());

				// Populate Group Visit Reference only for BOLT style orders.
				// TODO resReq.getEligibilityGroup()
				/*if (resReq.getEligibilityGroup().equalsIgnoreCase(GW_ORDERS_DLR_BOLT_GROUP)) {
					orderTO.setGroupVisitReference(resReq.getClientData().getBillingInfo().getName());
				}*/

			}

		} catch (NumberFormatException nfe) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.UNDEFINED_FAILURE,
					"TPLookup value ShipType or ShipDetail was not a valid integer in the database.");
		}

		return orderTO;
	}

	/**
	 * Transforms the eGalaxy XML response string into response objects within
	 * the DTI transfer object.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object without any response information from
	 *            eGalaxy.
	 * @param xmlResponse
	 *            the eGalaxy XML response string
	 * @return the DTI transfer object that has response information from
	 *         eGalaxy.
	 * @throws DTIException
	 *             should any issues during conversion occur
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {

		GWEnvelopeTO gwEnvRespTO = GWEnvelopeUpgradeEntitlementXML.getTO(xmlResponse);

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		UpgradeEntitlementResponseTO dtiupGradeEntRespTO = new UpgradeEntitlementResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules.createRespPayloadHdr(dtiTxn);
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);

		dtiRespTO.setPayloadHeader(payloadHdrTO);
		dtiRespTO.setCommandHeader(commandHdrTO);

		// get the body and order response TOs
		GWBodyTO gwBodyTO = gwEnvRespTO.getBodyTO();

		// BEGIN ERROR CHECKING
		// search for blatant error first
		if (gwBodyTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null body.");
		}

		// check status related errors
		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status.");
		}

		String statusString = gwStatusTO.getStatusCode();
		if (statusString == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status code.");
		}

		// Get the provider response status code
		int statusCode = -1;

		try {
			statusCode = Integer.parseInt(statusString);
			dtiRespTO.setProviderErrCode(statusString);
		} catch (NumberFormatException e) {
			throw new DTIException(TransformRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Provider responded with a non-numeric status code.");
		}

		// If the provider had an error, map it and generate the response.
		// Copy the ticket identity and default the TktStatus Voidable to No
		if (statusCode != 0) {
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString, xmlResponse);
			return dtiTxn; // NOTE: multiple return points is an anti-pattern,
			// this
			// should be re-factored
		}

		// Price mismatch warning
		if (dtiTxn.isPriceMismatch()) {
			DTIErrorTO mismatchWarn = ErrorKey.getErrorDetail(DTIErrorCode.PRICE_MISMATCH_WARNING);
			dtiRespTO.setDtiError(mismatchWarn);
		}

		// END ERROR CHECKING

		GWOrdersRespTO gwOrdResp = gwEnvRespTO.getBodyTO().getOrdersRespTO();

		String galaxyOrderID = gwOrdResp.getGalaxyOrderID();

		// don't be confused by the use of authorization code here...
		// it is used to set CCAuthNumber on the DTI response, not CCAuthCode
		String authNumber = gwOrdResp.getAuthCode();

		PaymentTO paymentTO = new PaymentTO();

		// PER WMB, if authorization code == 1
		if (authNumber != null) {
			paymentTO.setPayItem(new BigInteger("1"));
			CreditCardTO dtiCredCardTO = new CreditCardTO();
			dtiCredCardTO.setCcAuthCode("0"); // Hard-coded as in WMB
			dtiCredCardTO.setCcAuthNumber(authNumber);
			paymentTO.setCreditCard(dtiCredCardTO);
		}

		dtiupGradeEntRespTO.getPaymentList().add(paymentTO);

		// Add tickets if present
		ArrayList<TicketRecord> gwTicketArray = gwOrdResp.getTicketArray();
		if (gwTicketArray.size() > 0) {

			// ArrayList<TicketTO> dtiTicketArray =
			// dtiResRespTO.getTicketList();
			for (int i = 0; gwTicketArray.size() > i; i++) {

				TicketRecord gwTicket = gwTicketArray.get(i);

				TicketTO dtiTicket = new TicketTO();

				// TktItem
				BigInteger tktItem = new BigInteger(Integer.toString((i + 1)));
				dtiTicket.setTktItem(tktItem);

				// Visual ID to BarCode
				dtiTicket.setBarCode(gwTicket.getVisualID());

				dtiupGradeEntRespTO.addTicket(dtiTicket);

			}

		}

		// set TOs back on various places
		dtiRespTO.setCommandBody(dtiupGradeEntRespTO);
		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * Apply DLR Upgrade entitlement rules.
	 * 
	 * @param dtiTxn
	 *            the DTI transaction
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static void applyDLRUpgradeEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		UpgradeEntitlementRequestTO dtiUpgradeReq = (UpgradeEntitlementRequestTO) dtiCmdBody;

		// DLR upgrade transactions must have a Reservation Section
		if (dtiUpgradeReq.getReservation() == null) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitlement transaction did not have a Reservation section.");
		}

		// DLR upgrade transaction must have Client Data
		if (dtiUpgradeReq.getClientData() == null) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitlement transaction did not have a ClientData section.");
		}

		// Validate that resCode is not null and not 0 length
		if (dtiUpgradeReq.getReservation() != null) {
			String resCode = dtiUpgradeReq.getReservation().getResCode();

			if (resCode == null || resCode.length() == 0 || resCode.length() > 20) {
				throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
						"ResCode is not between required lengths (1 - 20) for DLR.");
			}
		}

		// Validate that billing demographics are present
		// FirstName, LastName, Street1, City, ZIP, Country, Phone, Email
		DemographicsTO billingTO = dtiUpgradeReq.getClientData().getBillingInfo();
		String firstName = billingTO.getFirstName();
		String lastName = billingTO.getLastName();
		String street1 = billingTO.getAddr1();
		String city = billingTO.getCity();
		String zip = billingTO.getZip();
		String country = billingTO.getCountry();
		String phone = billingTO.getTelephone();
		String email = billingTO.getEmail();

		if ((firstName == null) || (firstName.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing first name.");
		}
		if ((lastName == null) || (lastName.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing last name.");
		}
		if ((street1 == null) || (street1.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing street address 1.");
		}
		if ((city == null) || (city.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing city.");
		}
		if ((zip == null) || (zip.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing ZIP.");
		}
		if ((country == null) || (country.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing country.");
		}
		if ((phone == null) || (phone.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing phone.");
		}
		if ((email == null) || (email.length() == 0)) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"DLR Upgrade Entitelment missing required billing email.");
		}

		// max limit is enforced in TicketRules
		String tpiCode = dtiTxn.getTpiCode();
		TransactionType txnType = dtiTxn.getTransactionType();
		ReservationTO dtiResTO = dtiUpgradeReq.getReservation();
		String shipDetail = dtiResTO.getResSalesType();
		String shipMethod = dtiResTO.getResSalesType();

		if (shipDetail == null) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"Reservation getResSalesType cannot be null.");
		}
		if (shipMethod == null) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
					"Reservation getResSalesType cannot be null.");
		}

		// Get the TP Lookups
		ArrayList<TPLookupTO> tpLookups = LookupKey.getGWTPCommandLookup(tpiCode, txnType, shipMethod, shipDetail);

		if (tpLookups.size() == 0) {
			throw new DTIException(DLRReservationRules.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"TPCommandLookup query on Upgrade entitlement did not return with responses as expected (set-up incomplete): "
							+ tpLookups.size());
		}
		dtiTxn.setTpLookupTOList(tpLookups);

		// Note: Upgrade don't have a down payment.

		// RULE: If tickets have demographics, the country code must be of
		// length = 2. This is an oddity in the galaxy specification, but
		// still compliant to the ISO codes.
		ArrayList<TicketTO> tktListTO = dtiUpgradeReq.getTicketList();
		for /* each */ (TicketTO aTicketTO : /* in */tktListTO) {

			if (aTicketTO.getTicketDemoList() != null) {

				ArrayList<DemographicsTO> demoListTO = aTicketTO.getTicketDemoList();

				for /* each */ (DemographicsTO aDemo : /* in */demoListTO) {
					if (aDemo.getCountry() != null) {
						if (aDemo.getCountry().length() != 2) {
							throw new DTIException(DLRReservationRules.class, DTIErrorCode.INVALID_MSG_CONTENT,
									"DLR Demographics country code must be length 2 - length found was "
											+ aDemo.getCountry().length());
						}
					}
				} // for each demographic
			}
		} // for each ticket

		// Determine if this is an installment transaction, and if so, mark it
		// in
		// the transfer object.
		ArrayList<PaymentTO> payListTO = dtiUpgradeReq.getPaymentList();
		for (/* each */PaymentTO aPayment : /* in */payListTO) {

			if (aPayment.getPayType() == PaymentType.INSTALLMENT) {
				dtiUpgradeReq.setInstallmentRequest(true);
			}

		}

		// RULE: Validate ticket level demographics for DLR. (As of 2.17.2, JTL)
		ProductRules.validateDlrTicketDemo(tktListTO);

		return;

	}

	/**
	 * Sets the product-based order lines on the out-going request.
	 * 
	 * @param renEntReq
	 * @param orderTO
	 * @param dtiProductMap
	 * @param dtiFlatTktList
	 * @return the order total (needed in the GW header)
	 */
	private static BigDecimal setProductOrderLines(UpgradeEntitlementRequestTO upgradeRequest, GWOrderTO orderTO,
			HashMap<String, DBProductTO> dtiProductMap, ArrayList<TicketTO> dtiFlatTktList) {

		BigDecimal orderTotal = new BigDecimal("0.00");

		// iterate through the DTI tickets
		Iterator<TicketTO> iter = dtiFlatTktList.iterator();
		while (iter.hasNext()) {

			TicketTO dtiTicket = iter.next();

			DBProductTO dtiProduct = dtiProductMap.get(dtiTicket.getProdCode());

			GWOrderLineTO gwLineItemTO = new GWOrderLineTO();
			gwLineItemTO.setAmount(String.valueOf(dtiTicket.getProdPrice()));

			gwLineItemTO.setDescription(dtiProduct.getPdtDesc());

			// Set the detail type based on the type of product
			if ((dtiProduct.getDaySubclass().equalsIgnoreCase(GW_RENEW))
					|| (dtiProduct.getDaySubclass().equalsIgnoreCase(GW_PASS))) {
				gwLineItemTO.setDetailType(TransformConstants.GW_ORDERS_PASS_ORDER_LINE_ITEM);
			} else if ((dtiProduct.getDaySubclass().equalsIgnoreCase(GW_UPGRADE))) {
				gwLineItemTO.setDetailType(TransformConstants.GW_ORDERS_UPGRADE_ORDER_LINE_ITEM);
			} else {
				gwLineItemTO.setDetailType(TransformConstants.GW_ORDERS_TICKET_ORDER_LINE_ITEM);
			}
			// TO DO need to work on the detail Type for Upgrade

			// PLU (mapped from Ticket Name, not ticket code because of length)
			DBProductTO dtiDbProduct = dtiProductMap.get(dtiTicket.getProdCode());
			gwLineItemTO.setPlu(dtiDbProduct.getMappedProviderTktName());

			// PaymentPlanID
			if (upgradeRequest.isInstallmentRequest()) {
				GWPaymentContractTO contract = orderTO.getPaymntContractList().get(0);
				String paymentPlanID = contract.getPaymentPlanID();
				gwLineItemTO.setPaymentPlanID(paymentPlanID);
			}

			// Qty
			gwLineItemTO.setQty(String.valueOf(dtiTicket.getProdQty()));
			gwLineItemTO.setTaxCode(""); // FUTURE EXPANSION, TaxCode not used

			// figure out the product total
			BigDecimal prodCost = dtiTicket.getProdPrice();
			BigDecimal productTotal = prodCost.multiply(new BigDecimal(dtiTicket.getProdQty()));

			// add it to the total cost of the order
			orderTotal = orderTotal.add(productTotal);

			// set the total on the line item
			gwLineItemTO.setTotal(String.valueOf(productTotal));

			// Add demographics, if present (note: because of activity above,
			// only one expected per line). (As of 2.16.1, JTL)
			if (dtiTicket.getTicketDemoList().size() != 0) {

				DemographicsTO dtiDemo = dtiTicket.getTicketDemoList().get(0); // One
				// and
				// only
				// one.

				GWMemberDemographicsTO gwDemo = new GWMemberDemographicsTO();

				// First Name
				if (dtiDemo.getFirstName() != null) {
					gwDemo.setFirstName(dtiDemo.getFirstName());
				}

				// Last Name
				if (dtiDemo.getLastName() != null) {
					gwDemo.setLastName(dtiDemo.getLastName());
				}

				// Street 1
				if (dtiDemo.getAddr1() != null) {
					gwDemo.setStreet1(dtiDemo.getAddr1());
				}

				// Street 2
				if (dtiDemo.getAddr2() != null) {
					gwDemo.setStreet2(dtiDemo.getAddr2());
				}

				// City
				if (dtiDemo.getCity() != null) {
					gwDemo.setCity(dtiDemo.getCity());
				}

				// State
				if (dtiDemo.getState() != null) {
					gwDemo.setState(dtiDemo.getState());
				}

				// ZIP
				if (dtiDemo.getZip() != null) {
					gwDemo.setZip(dtiDemo.getZip());
				}

				// CountryCode
				if (dtiDemo.getCountry() != null) {
					gwDemo.setCountryCode(dtiDemo.getCountry());
				}

				// Phone Number
				if (dtiDemo.getTelephone() != null) {
					gwDemo.setPhone(dtiDemo.getTelephone());
				}

				// E-mail
				if (dtiDemo.getEmail() != null) {
					gwDemo.seteMail(dtiDemo.getEmail());
				}

				// Date of Birth
				if (dtiDemo.getDateOfBirth() != null) {
					String dateOfBirth = UtilityXML.getEGalaxyDateFromGCal(dtiDemo.getDateOfBirth());
					gwDemo.setDateOfBirth(dateOfBirth);
				}

				// Gender (Added default for unspecified, as of 2.16.1)
				if (dtiDemo.getGenderType() != DemographicsTO.GenderType.NOTPRESENT) {
					if (dtiDemo.getGenderType() == DemographicsTO.GenderType.MALE) {
						gwDemo.setGender("1");
					} else if (dtiDemo.getGenderType() == DemographicsTO.GenderType.FEMALE) {
						gwDemo.setGender("2");
					} else {
						gwDemo.setGender("0");
					}

					gwLineItemTO.addMember(gwDemo);
				}

				// Visual ID (as of 2.16.1, JTL)
				if (dtiTicket.getExistingTktID().getExternal() != null) {
					gwDemo.setVisualID(dtiTicket.getExistingTktID().getExternal());
				}

			}

			// add to orderTO
			orderTO.addOrderLine(gwLineItemTO);
		}
		// END OrderLine(s), product info
		return orderTotal;
	}

	/**
	 * Sets the payment order lines, based on the DTI input.
	 * 
	 * @param renewEntReq
	 * @param orderTO
	 * @param orderTotal
	 * @param payCardMap
	 * @return true or false if the CVV was found or not.
	 */
	private static boolean setPaymentOrderLines(UpgradeEntitlementRequestTO upgradeRequest, GWOrderTO orderTO,
			BigDecimal orderTotal, HashMap<String, String> payCardMap) {
		String paymentCode = null;
		boolean missingCvv = false;

		Iterator<PaymentTO> paymentIter = upgradeRequest.getPaymentList().iterator();

		while (paymentIter.hasNext()) {
			// lookup TO is used to setting paymentCode on the order line to
			PaymentTO paymentTO = paymentIter.next();

			GWOrderLineTO gwOrderLinePayTO = new GWOrderLineTO();
			gwOrderLinePayTO.setDetailType(TransformConstants.GW_ORDERS_PAYMENT_ORDER_LINE_ITEM);

			// CREDITCARD, VOUCHER, GIFTCARD, or INSTALLMENT/UNSUPPORTED

			if (paymentTO.getPayType() == PaymentType.CREDITCARD) {
				// set the credit card stuff
				CreditCardTO cCardTO = paymentTO.getCreditCard();
				gwOrderLinePayTO.setEndorsement(cCardTO.getCcNbr());
				gwOrderLinePayTO.setDescription(cCardTO.getCcType());

				// As of 2.16.2, JTL
				if (cCardTO.getCcStreet() != null) {
					gwOrderLinePayTO.setBillingStreet(cCardTO.getCcStreet());
				}

				gwOrderLinePayTO.setBillingZip(cCardTO.getCcZipCode());
				// eGalaxy requires MMYY
				gwOrderLinePayTO.setExpDate(cCardTO.getCcExpiration());
				if (cCardTO.getCcVV() != null && cCardTO.getCcVV().length() > 0) {
					gwOrderLinePayTO.setCvn(cCardTO.getCcVV());
				} else {
					missingCvv = true;
				}
				// use the map to set payment type,get card type to get
				// paymentCode,
				// set after if conditions below
				paymentCode = payCardMap.get(cCardTO.getCcType());

				// set pay total
				gwOrderLinePayTO.setTotal(String.valueOf(orderTotal));

				// set pay amount
				gwOrderLinePayTO.setAmount(String.valueOf(paymentTO.getPayAmount()));
			}

			if (paymentTO.getPayType() == PaymentType.INSTALLMENT) {

				// set the credit card stuff
				InstallmentCreditCardTO cCardTO = paymentTO.getInstallment().getCreditCard();
				gwOrderLinePayTO.setEndorsement(cCardTO.getCcNbr());
				gwOrderLinePayTO.setDescription(cCardTO.getCcType());

				// As of 2.16.2, JTL
				if (cCardTO.getCcStreet() != null) {
					gwOrderLinePayTO.setBillingStreet(cCardTO.getCcStreet());
				}

				gwOrderLinePayTO.setBillingZip(cCardTO.getCcZipCode());
				// eGalaxy requires MMYY
				gwOrderLinePayTO.setExpDate(cCardTO.getCcExpiration());
				if (cCardTO.getCcVV() != null && cCardTO.getCcVV().length() > 0) {
					gwOrderLinePayTO.setCvn(cCardTO.getCcVV());
				} else {
					missingCvv = true;
				}
				// use the map to set payment type, get card type to get
				// paymentCode, set after if conditions below
				paymentCode = payCardMap.get(cCardTO.getCcType());

				// set pay total
				gwOrderLinePayTO.setTotal("0.00");

				// set pay amount
				gwOrderLinePayTO.setAmount("0.00");
			}

			if (paymentTO.getPayType() == PaymentType.GIFTCARD) {
				// set the gift card stuff
				GiftCardTO gCardTO = paymentTO.getGiftCard();
				gwOrderLinePayTO.setDescription(TransformConstants.GW_ORDERS_ORDER_LINE_ITEM_DESCRIPTION_GIFT_CARD);
				gwOrderLinePayTO.setEndorsement(paymentTO.getGiftCard().getGcNbr());
				// Hard-coded (as was done in WMB)
				paymentCode = payCardMap.get(TransformConstants.GW_ORDERS_ORDER_LINE_ITEM_DESCRIPTION_GIFT_CARD);
				paymentTO.setGiftCard(gCardTO);

				// set pay total
				gwOrderLinePayTO.setTotal(String.valueOf(orderTotal));

				// set pay amount
				gwOrderLinePayTO.setAmount(String.valueOf(paymentTO.getPayAmount()));
			}

			// set the payment code
			gwOrderLinePayTO.setPaymentCode(paymentCode);

			// set payment date
			gwOrderLinePayTO.setPaymentDate(UtilityXML.getCurrentEgalaxyDate());

			// add back to the order
			orderTO.addOrderLine(gwOrderLinePayTO);
		}
		// END OrderLine(s), payments info
		return missingCvv;
	}

	/**
	 * Transforms a DLR Orders error
	 * 
	 * @param dtiTxn
	 *            The DTITransacationTO object representing this transaction.
	 * @param dtiRespTO
	 *            The parsed version of the received response.
	 * @param statusString
	 *            get to get the TP provider error information.
	 * @throws DTIException
	 *             Should the routine be unable to find the TP error lookup.
	 */
	static DTITransactionTO transformError(DTITransactionTO dtiTxn, DTIResponseTO dtiRespTO, String statusString,
			String xmlResponse) throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(dtiTxn.getTransactionType(), statusString, xmlResponse);

		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);

		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.COMMAND) {

			if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {
				UpgradeEntitlementRequestTO resReq = (UpgradeEntitlementRequestTO) dtiTxn.getRequest().getCommandBody();
				UpgradeEntitlementResponseTO resResp = new UpgradeEntitlementResponseTO();

				TicketTO dtiTktTO = new TicketTO();

				// Ticket Item
				dtiTktTO.setTktItem(new BigInteger(ITEM_1));

				// Ticket Identity
				if (resReq.getTicketList().size() != 0) {
					TicketTO dtiTktTOReq = resReq.getTicketList().get(0);
					if (dtiTktTOReq != null)
						dtiTktTO.setExternal(dtiTktTOReq.getExternal());
				}

				// Add the ticket to the response
				resResp.addTicket(dtiTktTO);

				dtiRespTO.setCommandBody(resReq);
			}
		}

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

}