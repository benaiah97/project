package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.DBProductTO.GuestType;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.data.common.TicketTO.UpgradeEligibilityStatusType;
import pvt.disney.dti.gateway.request.xsd.QueryEligibleProductsRequest;
import pvt.disney.dti.gateway.response.xsd.QueryEligibleProductsResponse;
import pvt.disney.dti.gateway.response.xsd.QueryTicketResponse;

/**
 * This class is responsible for transforming the JAXB parsed request into a
 * transfer object as well as transforming the transfer object response back
 * into a JAXB structure.
 *
 */
public class QueryEligibleProductsXML {
	
	private static final String ADULT = "Adult";
	private static final String CHILD = "Child";
	private static final String ANY = "Any";
	private static final String INELIGIBLE = "INELIGIBLE";
	private static final String NOPRODUCTS = "NOPRODUCTS";
	private static final String ELIGIBLE = "ELIGIBLE";
	
	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param queryReq
	 *           the JAXB version of the object
	 * @return the DTI application object
	 * @throws JAXBException
	 *            should any parsing errors occur.
	 */
	public static QueryEligibleProductsRequestTO getTO(QueryEligibleProductsRequest queryReq) throws JAXBException {

		QueryEligibleProductsRequestTO queryEligTo = new QueryEligibleProductsRequestTO();

		List<QueryEligibleProductsRequest.Ticket> ticketList = queryReq.getTicket();

		for /* each */(QueryEligibleProductsRequest.Ticket aTicket : /* in */ticketList) {

			TicketTO aTicketTO = new TicketTO();

			aTicketTO.setTktItem(aTicket.getTktItem());

			/*
			 * According to XML Specification, only one ticket identity is to be
			 * passed. If multiple or present, then accept the first one in this
			 * order: barcode, external, nid, mag, dssn
			 */
			// Ticket
			// Barcode
			if (aTicket.getTktID().getBarcode() != null) {
				aTicketTO.setBarCode(aTicket.getTktID().getBarcode());
			}
			// External
			else if (aTicket.getTktID().getExternal() != null) {
				aTicketTO.setExternal(aTicket.getTktID().getExternal());
			}
			// TCod
			else if (aTicket.getTktID().getTktNID() != null) {
				aTicketTO.setTktNID(aTicket.getTktID().getTktNID());
			}
			// MagCode (looking for both MagTrack1, MagTrack2)
			else if (aTicket.getTktID().getMag() != null) {
				setTicketMag(aTicketTO, aTicket);
			}
			// DSSN
			else if (aTicket.getTktID().getTktDSSN() != null) {
				setTicketDSSN(aTicketTO, aTicket);
			}

			aTicketTO.setSaleType(aTicket.getSaleType());
			// Adding aTicketTO to the EligibleProductRequestTO
			queryEligTo.add(aTicketTO);
		}

		return queryEligTo;

	}

	/**
	 * if ticket is of type DSSN
	 * 
	 * @param aTicketTO
	 * @param tktId
	 */
	private static void setTicketDSSN(TicketTO aTicketTO, QueryEligibleProductsRequest.Ticket aTicket) {

		// fetching the value of TktDSSN from ticket
		QueryEligibleProductsRequest.Ticket.TktID.TktDSSN tktDssn = aTicket.getTktID().getTktDSSN();

		XMLGregorianCalendar tXCal = tktDssn.getTktDate();
		GregorianCalendar tempCalendar = new GregorianCalendar(tXCal.getEonAndYear().intValue(), (tXCal.getMonth() - 1),
					tXCal.getDay());

		aTicketTO.setDssn(tempCalendar, tktDssn.getTktSite(), tktDssn.getTktStation(), tktDssn.getTktNbr());
	}

	
	/**
	 * Sets the ticket mag.
	 *
	 * @param aTicketTO the a ticket TO
	 * @param aTicket the a ticket
	 */
	private static void setTicketMag(TicketTO aTicketTO, QueryEligibleProductsRequest.Ticket aTicket) {

		// fetching the value of tktMag from ticket
		QueryEligibleProductsRequest.Ticket.TktID.Mag tktMag = aTicket.getTktID().getMag();

		String mag1 = tktMag.getMagTrack1();
		String mag2 = tktMag.getMagTrack2();

		if (mag2 != null) {
			aTicketTO.setMag(mag1, mag2);
		} else {
			aTicketTO.setMag(mag1);
		}
	}

	/**
	 * Gets the JAXB structure from the response transfer object.
	 * 
	 * @param qryResRespTO
	 *           The response transfer object.
	 * 
	 * @return the populated JAXB object
	 * 
	 * @throws JAXBException
	 *            for any JAXB parsing issue.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryEligibleProductsResponse getJaxb(QueryEligibilityProductsResponseTO qryResRespTO,
				DTIErrorTO errorTO) throws JAXBException {

		QueryEligibleProductsResponse queryEligPrdResp = new QueryEligibleProductsResponse();

		ArrayList<TicketTO> aTicketTOList = qryResRespTO.getTicketList();

		QueryEligibleProductsResponse.Ticket aTicket = new QueryEligibleProductsResponse.Ticket();

		QName qName = null;

		// Can have multiple Ticket
		for /* each */(TicketTO aTicketTO : /* in */aTicketTOList) {

			// Ticket Item
			BigInteger tktItemTO = aTicketTO.getTktItem();
			qName = new QName("TktItem");
			JAXBElement<BigInteger> tktItem = new JAXBElement(qName, tktItemTO.getClass(), tktItemTO);
			aTicket.getTktItemOrTktIDOrProdCode().add(tktItem);

			// TktID ??
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO.getTicketTypes();

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

               JAXBElement<String> tktDssnElement = new JAXBElement(qName, tktDssn.getClass(), tktDssn);

               tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktDssnElement);
            }
            // Tkt NID ?
            if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {

               String tktNidTO = aTicketTO.getTktNID();

               qName = new QName("TktNID");

               JAXBElement<String> tktNid = new JAXBElement(qName, tktNidTO.getClass(), tktNidTO);
               tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktNid);

            }

            // External?
            if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {

               String tktExtTO = aTicketTO.getExternal();

               qName = new QName("External");

               JAXBElement<String> tktExt = new JAXBElement(qName, tktExtTO.getClass(), tktExtTO);
               tktIdObj.getTktDSSNOrTktNIDOrExternal().add(tktExt);
            }

            // Add whatever ticket versions were found to the response.
            qName = new QName("TktID");

            JAXBElement<String> tktId = new JAXBElement(qName, tktIdObj.getClass(), tktIdObj);

            aTicket.getTktItemOrTktIDOrProdCode().add(tktId);

         }

			// Product code
			if (aTicketTO.getProdCode() != null) {
				String prodCode = aTicketTO.getProdCode();
				qName = new QName("ProdCode");

				JAXBElement<String> pdtCode = new JAXBElement(qName, prodCode.getClass(), prodCode);
				aTicket.getTktItemOrTktIDOrProdCode().add(pdtCode);
			}
			
			// DLR PLU , added for off-line codes from galaxy 
         if (aTicketTO.getDlrPLU() != null) {
            String pluCode = aTicketTO.getDlrPLU();
            qName = new QName("DLRPLU");

            JAXBElement<String> dlrPLU = new JAXBElement(qName, pluCode.getClass(), pluCode);
            aTicket.getTktItemOrTktIDOrProdCode().add(dlrPLU);
         }
         
			// Product Guest type
			if (aTicketTO.getGuestType() != null) {
				String prodGuestType = null;
				if (aTicketTO.getGuestType().compareTo(GuestType.ADULT) == 0) {
					prodGuestType = ADULT;
				} else if (aTicketTO.getGuestType().compareTo(GuestType.CHILD) == 0) {
					prodGuestType = CHILD;
				} else {
					prodGuestType = ANY;
				}
				qName = new QName("ProdGuestType");
				JAXBElement<String> pdtGuest = new JAXBElement(qName, prodGuestType.getClass(), prodGuestType);
				aTicket.getTktItemOrTktIDOrProdCode().add(pdtGuest);
			}

			// TktStatus?
			ArrayList<TktStatusTO> statusListTO = aTicketTO.getTktStatusList();
         if ((statusListTO != null) && (statusListTO.size() > 0)) {
            for /* each */(TktStatusTO aStatusTO : /* in */statusListTO) {
               QueryEligibleProductsResponse.Ticket.TktStatus tktStatus = new QueryEligibleProductsResponse.Ticket.TktStatus();

               // Setting up the status item
               tktStatus.setStatusItem(aStatusTO.getStatusItem());
               tktStatus.setStatusValue(aStatusTO.getStatusValue());
               qName = new QName("TktStatus");

               JAXBElement<String> tktStatusElement = new JAXBElement(qName, tktStatus.getClass(), tktStatus);
               aTicket.getTktItemOrTktIDOrProdCode().add(tktStatusElement);
            }
         }
			// Ticket Demographics
			if ((aTicketTO.getTicketDemoList() != null) && (aTicketTO.getTicketDemoList().size() == 1)) {
				if (aTicketTO.getTicketDemoList().get(0) != null) {

					DemographicsTO aDemoTO = aTicketTO.getTicketDemoList().get(0);

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
						XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aDemoTO.getDateOfBirth());
						demoData.setDateOfBirth(xCalDate);
					}

					// As of 2.16.1, JTL
					if (aDemoTO.getGender() != null) {
						demoData.setGender(aDemoTO.getGender());
					}

					qName = new QName("TktDemographics");

					tktDemo.setDemoData(demoData);
					JAXBElement<String> tktDemographicsElement = new JAXBElement(qName, tktDemo.getClass(), tktDemo);
					aTicket.getTktItemOrTktIDOrProdCode().add(tktDemographicsElement);

				}
			}
			// TktValidity?
			if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO.getTktValidityValidEnd() != null)) {

			   QueryEligibleProductsResponse.Ticket.TktValidity tktValidity = new QueryEligibleProductsResponse.Ticket.TktValidity();
				XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO.getTktValidityValidStart());
				tktValidity.setValidStart(xCalDate);
				xCalDate = UtilXML.convertToXML(aTicketTO.getTktValidityValidEnd());
				tktValidity.setValidEnd(xCalDate);
				qName = new QName("TktValidity");

				JAXBElement<String> tktValidityElement = new JAXBElement(qName, tktValidity.getClass(), tktValidity);
				aTicket.getTktItemOrTktIDOrProdCode().add(tktValidityElement);

			}

			// SRPPrice
			if (aTicketTO.getTktPrice() != null) {
				BigDecimal tktPriceTO = aTicketTO.getTktPrice();
				qName = new QName("SRPPrice");

				JAXBElement<String> sRPPrice = new JAXBElement(qName, tktPriceTO.getClass(), tktPriceTO);
				aTicket.getTktItemOrTktIDOrProdCode().add(sRPPrice);

			}

			// SRTax
			if (aTicketTO.getTktTax() != null) {
				BigDecimal tktTax = aTicketTO.getTktTax();
				qName = new QName("SRPTax");

				JAXBElement<String> sRTax = new JAXBElement(qName, tktTax.getClass(), tktTax);
				aTicket.getTktItemOrTktIDOrProdCode().add(sRTax);

			}

			// ResultStatus
			String result = null;
			if ((aTicketTO.getUpgradeEligibilityStatus() != null) && (errorTO == null)) {
				if (aTicketTO.getUpgradeEligibilityStatus().compareTo(UpgradeEligibilityStatusType.NOPRODUCTS) == 0) {
					result = NOPRODUCTS;
				} else if (aTicketTO.getUpgradeEligibilityStatus().compareTo(UpgradeEligibilityStatusType.ELIGIBLE) == 0) {
					result = ELIGIBLE;
				} else {
					result = INELIGIBLE;
				}
				qName = new QName("ResultStatus");

				JAXBElement<String> resultStatus = new JAXBElement(qName, result.getClass(), result);
				aTicket.getTktItemOrTktIDOrProdCode().add(resultStatus);

			}

			// EligibleProducts
			if ((aTicketTO.getEligibleProducts() != null) && (aTicketTO.getEligibleProducts().size() > 0)) {
				for (EligibleProductsTO eligibleProduct : aTicketTO.getEligibleProducts()) {
					QueryEligibleProductsResponse.Ticket.EligibleProducts eligibleproduct = new QueryEligibleProductsResponse.Ticket.EligibleProducts();
					
					// Prod Code
					eligibleproduct.setProdCode(eligibleProduct.getProdCode());
					
					// Prod Price
					eligibleproduct.setProdPrice(eligibleProduct.getProdPrice());
					
					// Prod Tax
					eligibleproduct.setProdTax(eligibleProduct.getProdTax());
					
					// Upgrade price
					eligibleproduct.setUpgrdPrice(eligibleProduct.getUpgrdPrice());
					
					// Upgrade Tax
					eligibleproduct.setUpgrdTax(eligibleProduct.getUpgrdTax());
					
					// Valid End
					XMLGregorianCalendar xCalDate = UtilXML.convertToXML(eligibleProduct.getValidEnd());
					eligibleproduct.setValidEnd(xCalDate);
					
					qName = new QName("EligibleProducts");

					JAXBElement<String> eligible = new JAXBElement(qName, eligibleproduct.getClass(), eligibleproduct);
					aTicket.getTktItemOrTktIDOrProdCode().add(eligible);
				}
			}
			
			// Ticket Error
	      if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

	        QueryTicketResponse.Ticket.TktError tktError = new QueryTicketResponse.Ticket.TktError();
	        tktError.setTktErrorCode(errorTO.getErrorCode());
	        tktError.setTktErrorType(errorTO.getErrorType());
	        tktError.setTktErrorClass(errorTO.getErrorClass());
	        tktError.setTktErrorText(errorTO.getErrorText());

	        qName = new QName("TktError");
	        JAXBElement<String> tktErrorElement = new JAXBElement(qName,
	            tktError.getClass(), tktError);
	        aTicket.getTktItemOrTktIDOrProdCode().add(tktErrorElement);
	      }
	      
	      // Adding the ticket details in Query Eligible Product Response
	   	queryEligPrdResp.getTicket().add(aTicket);
		}

		return queryEligPrdResp;

	}

}
