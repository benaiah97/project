package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.ResultStatusTo;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest;
import pvt.disney.dti.gateway.response.xsd.QueryEligibleProductsResponse;

/**
 * This class is responsible for transforming the JAXB parsed request into a
 * transfer object as well as transforming the transfer object response back
 * into a JAXB structure.
 *
 */
public class QueryEligibleProductsXML {
	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param queryReq
	 *            the JAXB version of the object
	 * @return the DTI application object
	 * @throws JAXBException
	 *             should any parsing errors occur.
	 */
	public static QueryEligibleProductsRequestTO getTO(
			QueryEligibleProductsRequest queryReq) throws JAXBException {
		QueryEligibleProductsRequestTO queryEligTo = new QueryEligibleProductsRequestTO();
		List<QueryEligibleProductsRequest.Ticket> ticketList = queryReq.getTicket();
				
		for /* each */(QueryEligibleProductsRequest.Ticket aTicket : /* in */ticketList){
			TicketTO aTicketTO = new TicketTO();
			aTicketTO.setTktItem(aTicket.getTktItem());
			QueryEligibleProductsRequest.Ticket.TktID tktId = aTicket.getTktID();
			/*
			 * According to XML Specification, only one ticket identity is to be
			 * passed. If multiple or present, then accept the first one in this
			 * order: barcode, external, nid, mag, dssn
			 */
			//Ticket
			// TODO formatting/commenting for each if statement to space it out and comment on logic
			if (tktId.getBarcode() != null) {
				aTicketTO.setBarCode(tktId.getBarcode());
			} else {
				if (tktId.getExternal() != null) {
					aTicketTO.setExternal(tktId.getExternal());
				} else {
					if (tktId.getTktNID() != null) {
						aTicketTO.setTktNID(tktId.getTktNID());
					} else {
						if (tktId.getMag() != null) {
							QueryEligibleProductsRequest.Ticket.TktID.Mag tktMag = tktId
									.getMag();
							String mag1 = tktMag.getMagTrack1();
							String mag2 = tktMag.getMagTrack2();

							if (mag2 != null) {
								aTicketTO.setMag(mag1, mag2);
							} else {
								aTicketTO.setMag(mag1);
							}
						} else {
							// TODO can this be broken out into separate method?
							if (tktId.getTktDSSN() != null) {
								QueryEligibleProductsRequest.Ticket.TktID.TktDSSN tktDssn = tktId
										.getTktDSSN();
								XMLGregorianCalendar tXCal = tktDssn.getTktDate();
								GregorianCalendar tempCalendar = new GregorianCalendar(
										tXCal.getEonAndYear().intValue(),
										(tXCal.getMonth() - 1), tXCal.getDay());
								aTicketTO.setDssn(tempCalendar,
										tktDssn.getTktSite(),
										tktDssn.getTktStation(),
										tktDssn.getTktNbr());
							}
						}
					}
				}
			}
			aTicketTO.setSaleType(aTicket.getSaleType());
			//Adding aTicketTO to the EligibleProductRequestTO
			queryEligTo.add(aTicketTO);
		}

		return queryEligTo;

	}

	/**
	 * Gets the JAXB structure from the response transfer object.
	 * 
	 * @param qryResRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated JAXB object
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryEligibleProductsResponse getJaxb(
			QueryEligibilityProductsResponseTO qryResRespTO, DTIErrorTO errorTO)
			throws JAXBException {
		QueryEligibleProductsResponse resp = new QueryEligibleProductsResponse();
		ArrayList<TicketTO> aTicketTOList = qryResRespTO.getTicketList();
		QueryEligibleProductsResponse.Ticket aTicket = new QueryEligibleProductsResponse.Ticket();
		QName qName = null;
		List<QueryEligibleProductsResponse.Ticket> ticketList = resp
				.getTicket();
		for /* each */(TicketTO aTicketTO : /* in */aTicketTOList) {
			// TktStatus?
			ArrayList<TktStatusTO> statusListTO = aTicketTO.getTktStatusList();
			if (statusListTO != null) {
				if (statusListTO.size() > 0) {
					for /* each */(TktStatusTO aStatusTO : /* in */statusListTO) {
						QueryEligibleProductsResponse.Ticket.TktStatus tktStatus = new QueryEligibleProductsResponse.Ticket.TktStatus();
						tktStatus.setStatusItem(aStatusTO.getStatusItem());
						tktStatus.setStatusValue(aStatusTO.getStatusValue());
						qName = new QName("TktStatus");
						JAXBElement<String> tktStatusElement = new JAXBElement(
								qName, tktStatus.getClass(), tktStatus);
						aTicket.getTktItemOrTktIDOrProdCode().add(
								tktStatusElement);

					}
				}
			}
			// Ticket Demographics
			// TODO add all demographic including optin/out
			if ((aTicketTO.getTicketDemoList() != null)
					&& (aTicketTO.getTicketDemoList().size() == 1)) {

				if (aTicketTO.getTicketDemoList().get(0) != null) {

					DemographicsTO aDemoTO = aTicketTO.getTicketDemoList().get(
							0);
					QueryEligibleProductsResponse.Ticket.TktDemographics tktDemo = new QueryEligibleProductsResponse.Ticket.TktDemographics();
					QueryEligibleProductsResponse.Ticket.TktDemographics.DemoData demoData = new QueryEligibleProductsResponse.Ticket.TktDemographics.DemoData();

					if (aDemoTO.getName() != null) {
						demoData.setName(aDemoTO.getName());
					}

					if (aDemoTO.getFirstName() != null) {
						demoData.setFirstName(aDemoTO.getFirstName());
					}

					if (aDemoTO.getLastName() != null) {
						demoData.setLastName(aDemoTO.getLastName());
					}

					if (aDemoTO.getAddr1() != null) {
						demoData.setAddr1(aDemoTO.getAddr1());
					}

					if (aDemoTO.getAddr2() != null) {
						demoData.setAddr2(aDemoTO.getAddr2());
					}

					if (aDemoTO.getCity() != null) {
						demoData.setCity(aDemoTO.getCity());
					}

					if (aDemoTO.getState() != null) {
						demoData.setState(aDemoTO.getState());
					}

					if (aDemoTO.getZip() != null) {
						demoData.setZip(aDemoTO.getZip());
					}

					if (aDemoTO.getCountry() != null) {
						demoData.setCountry(aDemoTO.getCountry());
					}

					if (aDemoTO.getTelephone() != null) {
						demoData.setTelephone(aDemoTO.getTelephone());
					}

					if (aDemoTO.getEmail() != null) {
						demoData.setEmail(aDemoTO.getEmail());
					}

					// As of 2.16.1, JTL 052316
					if (aDemoTO.getDateOfBirth() != null) {
						XMLGregorianCalendar xCalDate = UtilXML
								.convertToXML(aDemoTO.getDateOfBirth());
						demoData.setDateOfBirth(xCalDate);
					}

					// As of 2.16.1, JTL
					if (aDemoTO.getGender() != null) {
						demoData.setGender(aDemoTO.getGender());
					}

					qName = new QName("TktDemographics");

					tktDemo.setDemoData(demoData);
					JAXBElement<String> tktDemographicsElement = new JAXBElement(
							qName, tktDemo.getClass(), tktDemo);
					aTicket.getTktItemOrTktIDOrProdCode().add(
							tktDemographicsElement);

				}
			}
			// TktValidity?
			if ((aTicketTO.getTktValidityValidStart() != null)
					&& (aTicketTO.getTktValidityValidEnd() != null)) {
				QueryEligibleProductsResponse.Ticket.TktValidity tktValidity = new QueryEligibleProductsResponse.Ticket.TktValidity();

				XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidStart());
				tktValidity.setValidStart(xCalDate);

				xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidEnd());
				tktValidity.setValidEnd(xCalDate);

				qName = new QName("TktValidity");
				JAXBElement<String> tktValidityElement = new JAXBElement(qName,
						tktValidity.getClass(), tktValidity);
				aTicket.getTktItemOrTktIDOrProdCode().add(tktValidityElement);

			}

			// TktID ??
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO
					.getTicketTypes();

			if (typeList.size() > 0) {

				QueryEligibleProductsResponse.Ticket.TktID tktIdObj = new QueryEligibleProductsResponse.Ticket.TktID();
				
				// MagCode and Barcode are intentionally omitted				

				// Tkt DSSN?
				if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
					QueryEligibleProductsResponse.Ticket.TktID.TktDSSN tktDssn = new QueryEligibleProductsResponse.Ticket.TktID.TktDSSN();
					tktDssn.setTktDate(aTicketTO.getDssnDateString());
					tktDssn.setTktNbr(aTicketTO.getDssnNumber());
					tktDssn.setTktSite(aTicketTO.getDssnSite());
					tktDssn.setTktStation(aTicketTO.getDssnStation());
					qName = new QName("TktDSSN");
					JAXBElement<String> tktDssnElement = new JAXBElement(qName,
							tktDssn.getClass(), tktDssn);
					tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktDssnElement);
				}
				// Tkt NID ?
				if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {
					String tktNidTO = aTicketTO.getTktNID();
					qName = new QName("TktNID");
					JAXBElement<String> tktNid = new JAXBElement(qName,
							tktNidTO.getClass(), tktNidTO);
					tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktNid);

				}

				// External?
				if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {
					String tktExtTO = aTicketTO.getExternal();
					qName = new QName("External");
					JAXBElement<String> tktExt = new JAXBElement(qName,
							tktExtTO.getClass(), tktExtTO);
					tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktExt);
				}

				// Add whatever ticket versions were found to the response.
				qName = new QName("TktID");
				JAXBElement<String> tktId = new JAXBElement(qName,
						tktIdObj.getClass(), tktIdObj);
				aTicket.getTktItemOrTktIDOrProdCode().add(tktId);

			}
			// SRPPrice
			if (aTicketTO.getTktPrice() != null) {
				BigDecimal tktPriceTO = aTicketTO.getTktPrice();
				qName = new QName("SRPPrice");
				JAXBElement<String> sRPPrice = new JAXBElement(qName,
						tktPriceTO.getClass(), tktPriceTO);
				aTicket.getTktItemOrTktIDOrProdCode().add(sRPPrice);

			}
			// ResultStatus
			ResultStatusTo result = null;
			if (aTicketTO.getResultType() != null) {
				result = aTicketTO.getResultType();
				qName = new QName("ResultStatus");
				JAXBElement<String> resultStatus = new JAXBElement(qName,
						result.getClass(), result);
				aTicket.getTktItemOrTktIDOrProdCode().add(resultStatus);
			}
			// EligibleProducts
			if (aTicketTO.getProdCode() != null
					&& aTicketTO.getProdPrice() != null) {
				String prodCode = aTicketTO.getProdCode();
				QueryEligibleProductsResponse.Ticket.EligibleProducts eligibleproduct = new QueryEligibleProductsResponse.Ticket.EligibleProducts();
				eligibleproduct.setProdCode(prodCode);
				eligibleproduct.setProdPrice(String.valueOf(aTicketTO
						.getProdPrice()));
				eligibleproduct.setProdTax(aTicketTO.getTktTax());
				eligibleproduct.setUpgrdPrice(String.valueOf(aTicketTO
						.getUpgrdPrice()));
				// eligibleproduct.setUpgrdTax(aTicketTO.get);
				qName = new QName("EligibleProducts");
				JAXBElement<String> eligible = new JAXBElement(qName,
						eligibleproduct.getClass(), eligibleproduct);
				aTicket.getTktItemOrTktIDOrProdCode().add(eligible);
			}
			resp.getTicket().add(aTicket);
		}

		return resp;

	}

}
