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
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.request.xsd.QueryTicketRequest;
import pvt.disney.dti.gateway.response.xsd.QueryTicketResponse;

/**
 * This class is responsible for transformations between the DTI TiXML and the
 * internal objects used by the DTI Application. This class manages the Query
 * Ticket clause.
 * 
 * @author lewit019
 * 
 */
public abstract class QueryTicketXML {

<<<<<<< HEAD
  /**
   * When passed the JAXB object, return the DTI application object.
   * 
   * @param queryReq
   *          the JAXB version of the object
   * @return the DTI application object
   * @throws JAXBException
   *           should any parsing errors occur.
   */
  public static QueryTicketRequestTO getTO(QueryTicketRequest queryReq)
      throws JAXBException {

    QueryTicketRequestTO queryReqTO = new QueryTicketRequestTO();

    List<QueryTicketRequest.Ticket> ticketList = queryReq.getTicket();
    ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();

    for /* each */(QueryTicketRequest.Ticket aTicket : /* in */ticketList) {

      TicketTO aTicketTO = new TicketTO();

      aTicketTO.setTktItem(aTicket.getTktItem());

      QueryTicketRequest.Ticket.TktID tktId = aTicket.getTktID();

      /*
       * According to XML Specification, only one ticket identity is to be
       * passed. If multiple or present, then accept the first one in this
       * order: barcode, external, nid, mag, dssn
       */
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
              QueryTicketRequest.Ticket.TktID.Mag tktMag = tktId.getMag();
              String mag1 = tktMag.getMagTrack1();
              String mag2 = tktMag.getMagTrack2();

              if (mag2 != null) {
                aTicketTO.setMag(mag1, mag2);
              } else {
                aTicketTO.setMag(mag1);
              }
            } else {
              if (tktId.getTktDSSN() != null) {
                QueryTicketRequest.Ticket.TktID.TktDSSN tktDssn = tktId
                    .getTktDSSN();
                XMLGregorianCalendar tXCal = tktDssn.getTktDate();
                GregorianCalendar tempCalendar = new GregorianCalendar(tXCal
                    .getEonAndYear().intValue(), (tXCal.getMonth() - 1),
                    tXCal.getDay());
                aTicketTO.setDssn(tempCalendar, tktDssn.getTktSite(),
                    tktDssn.getTktStation(), tktDssn.getTktNbr());
              }
            }
          }
        }
      }

      ticketListTO.add(aTicketTO);

    } // end while

    // IncludeTktDemographics 
    Boolean isTktDemo = queryReq.isIncludeTktDemographics();
    if (isTktDemo != null) {
      if (isTktDemo.booleanValue()) {
        queryReqTO.setIncludeTktDemographics(true);
      }
    }
    
    // IncludePassAttributes
    Boolean isPassAttr = queryReq.isIncludePassAttributes();
    if (isPassAttr != null) {
      if (isPassAttr.booleanValue()) {
        queryReqTO.setIncludePassAttributes(true);
      }
    }

    // IncludeElectronicAttributes
    Boolean isElectronic = queryReq.isIncludeElectronicAttributes();
    if (isElectronic != null) {
      if (isElectronic.booleanValue()) {
        queryReqTO.setIncludeElectronicAttributes(true);
      }
    }
    
    // IncludeTicketRedeemability
    Boolean isRedeemable = queryReq.isIncludeTicketRedeemability();
    if (isRedeemable != null) {
    	if (isRedeemable.booleanValue()) {
    		queryReqTO.setIncludeTicketRedeemability(true);
    	}
    }
    
    // IncludeRenewalAttributes
    Boolean isRenewalAttr = queryReq.isIncludeRenewalAttributes();
    if (isRenewalAttr != null) {
      if (isRenewalAttr.booleanValue()) {
        queryReqTO.setIncludeRenewalAttributes(true);
      }
    }

    queryReqTO.setTktList(ticketListTO);

    return queryReqTO;

  }

  /**
   * When passed the DTI Application object, return the JAXB object.
   * 
   * @param qryTktRespTO
   *          The query ticket response object (DTI Application object)
   * @return the JAXB version of the QueryTicketResponse
   * @throws JAXBException
   *           should any parsing errors occur.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static QueryTicketResponse getJaxb(QueryTicketResponseTO qryTktRespTO,
      DTIErrorTO errorTO) throws JAXBException {

    QueryTicketResponse response = new QueryTicketResponse();

    List<QueryTicketResponse.Ticket> ticketList = response.getTicket();
    ArrayList<TicketTO> respTOtktList = qryTktRespTO.getTicketList();

    for /* each */(TicketTO aTicketTO : /* in */respTOtktList) {

      QueryTicketResponse.Ticket aTicket = new QueryTicketResponse.Ticket();

      // Ticket Item
      BigInteger tktItemTO = aTicketTO.getTktItem();
      QName qName = new QName("TktItem");
      JAXBElement<BigInteger> tktItem = new JAXBElement(qName,
          tktItemTO.getClass(), tktItemTO);
      aTicket.getTktItemOrTktIDOrTktPrice().add(tktItem);

      // TktID
      ArrayList<TicketTO.TicketIdType> typeList = aTicketTO.getTicketTypes();

      if (typeList.size() > 0) {

        QueryTicketResponse.Ticket.TktID tktIdObj = new QueryTicketResponse.Ticket.TktID();

        // Mag Code?
        if (typeList.contains(TicketTO.TicketIdType.MAG_ID)) {
          String tktMagTO = aTicketTO.getMagTrack1();
          qName = new QName("Mag");
          JAXBElement<String> tktMag = new JAXBElement(qName,
              tktMagTO.getClass(), tktMagTO);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktMag);
        }

        // Bar Code?
        if (typeList.contains(TicketTO.TicketIdType.BARCODE_ID)) {
          String tktBarTO = aTicketTO.getBarCode();
          qName = new QName("Barcode");
          JAXBElement<String> tktBar = new JAXBElement(qName,
              tktBarTO.getClass(), tktBarTO);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktBar);
        }

        // Tkt DSSN?
        if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
          QueryTicketResponse.Ticket.TktID.TktDSSN tktDssn = new QueryTicketResponse.Ticket.TktID.TktDSSN();
          tktDssn.setTktDate(aTicketTO.getDssnDateString());
          tktDssn.setTktNbr(aTicketTO.getDssnNumber());
          tktDssn.setTktSite(aTicketTO.getDssnSite());
          tktDssn.setTktStation(aTicketTO.getDssnStation());
          qName = new QName("TktDSSN");
          JAXBElement<String> tktDssnElement = new JAXBElement(qName,
              tktDssn.getClass(), tktDssn);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktDssnElement);
        }

        // Tkt NID ?
        if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {
          String tktNidTO = aTicketTO.getTktNID();
          qName = new QName("TktNID");
          JAXBElement<String> tktNid = new JAXBElement(qName,
              tktNidTO.getClass(), tktNidTO);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktNid);

        }

        // External?
        if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {
          String tktExtTO = aTicketTO.getExternal();
          qName = new QName("External");
          JAXBElement<String> tktExt = new JAXBElement(qName,
              tktExtTO.getClass(), tktExtTO);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktExt);
        }

        // Add whatever ticket versions were found to the response.
        qName = new QName("TktID");
        JAXBElement<String> tktId = new JAXBElement(qName, tktIdObj.getClass(),
            tktIdObj);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktId);

      }

      // TktPrice?
      if (aTicketTO.getTktPrice() != null) {
        BigDecimal tktPriceTO = aTicketTO.getTktPrice();
        qName = new QName("TktPrice");
        JAXBElement<String> tktPrice = new JAXBElement(qName,
            tktPriceTO.getClass(), tktPriceTO);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktPrice);
      }

      // TktTax?
      if (aTicketTO.getTktTax() != null) {
        BigDecimal tktTaxTO = aTicketTO.getTktTax();
        qName = new QName("TktTax");
        JAXBElement<String> tktTax = new JAXBElement(qName,
            tktTaxTO.getClass(), tktTaxTO);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktTax);
      }

      // TktStatus?
      ArrayList<TktStatusTO> statusListTO = aTicketTO.getTktStatusList();
      if (statusListTO != null) {
        if (statusListTO.size() > 0) {
          for /* each */(TktStatusTO aStatusTO : /* in */statusListTO) {
            QueryTicketResponse.Ticket.TktStatus tktStatus = new QueryTicketResponse.Ticket.TktStatus();
            tktStatus.setStatusItem(aStatusTO.getStatusItem());
            tktStatus.setStatusValue(aStatusTO.getStatusValue());
            qName = new QName("TktStatus");
            JAXBElement<String> tktStatusElement = new JAXBElement(qName,
                tktStatus.getClass(), tktStatus);
            aTicket.getTktItemOrTktIDOrTktPrice().add(tktStatusElement);
          }
        }
      }

      // TktValidity?
      if ((aTicketTO.getTktValidityValidStart() != null)
          && (aTicketTO.getTktValidityValidEnd() != null)) {
        QueryTicketResponse.Ticket.TktValidity tktValidity = new QueryTicketResponse.Ticket.TktValidity();

        XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
            .getTktValidityValidStart());
        tktValidity.setValidStart(xCalDate);

        xCalDate = UtilXML.convertToXML(aTicketTO.getTktValidityValidEnd());
        tktValidity.setValidEnd(xCalDate);

        qName = new QName("TktValidity");
        JAXBElement<String> tktValidityElement = new JAXBElement(qName,
            tktValidity.getClass(), tktValidity);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktValidityElement);

      }

      // TktAttributes?
      if (aTicketTO.isAttributed()) {

        QueryTicketResponse.Ticket.TktAttribute tktAttribute = new QueryTicketResponse.Ticket.TktAttribute();

        if (aTicketTO.getResident() != null)
          tktAttribute.setResident(aTicketTO.getResident());

        if (aTicketTO.getPassRenew() != null)
          tktAttribute.setPassRenew(aTicketTO.getPassRenew());

        if (aTicketTO.getPassClass() != null)
          tktAttribute.setPassClass(aTicketTO.getPassClass());

        if (aTicketTO.getPassType() != null)
          tktAttribute.setPassType(aTicketTO.getPassType());

        if (aTicketTO.getMediaType() != null)
          tktAttribute.setMediaType(aTicketTO.getMediaType());

        if (aTicketTO.getAgeGroup() != null)
          tktAttribute.setAgeGroup(aTicketTO.getAgeGroup());

        if (aTicketTO.getPassName() != null)
          tktAttribute.setPassName(aTicketTO.getPassName());

        if (aTicketTO.getLastDateUsed() != null) {
          XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
              .getLastDateUsed());
          tktAttribute.setLastDateUsed(xCalDate);
        }

        if (aTicketTO.getTimesUsed() != null) {
          tktAttribute.setTimesUsed(aTicketTO.getTimesUsed());
        }

        if (aTicketTO.getReplacedByPass() != null)
          tktAttribute.setReplacedByPass(aTicketTO.getReplacedByPass());

        qName = new QName("TktAttribute");
        JAXBElement<String> tktAttributeElement = new JAXBElement(qName,
            tktAttribute.getClass(), tktAttribute);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktAttributeElement);

      }

      // Ticket Demographics
      if ((aTicketTO.getTicketDemoList() != null)
          && (aTicketTO.getTicketDemoList().size() == 1)) {

        if (aTicketTO.getTicketDemoList().get(0) != null) {

          DemographicsTO aDemoTO = aTicketTO.getTicketDemoList().get(0);
          QueryTicketResponse.Ticket.TktDemographics tktDemo = new QueryTicketResponse.Ticket.TktDemographics();
          QueryTicketResponse.Ticket.TktDemographics.DemoData demoData = new QueryTicketResponse.Ticket.TktDemographics.DemoData();

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
=======
	/**
	 * When passed the JAXB object, return the DTI application object.
	 * 
	 * @param queryReq
	 *            the JAXB version of the object
	 * @return the DTI application object
	 * @throws JAXBException
	 *             should any parsing errors occur.
	 */
	public static QueryTicketRequestTO getTO(QueryTicketRequest queryReq) throws JAXBException {

		QueryTicketRequestTO queryReqTO = new QueryTicketRequestTO();

		List<QueryTicketRequest.Ticket> ticketList = queryReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();

		for /* each */(QueryTicketRequest.Ticket aTicket : /* in */ticketList) {

			TicketTO aTicketTO = new TicketTO();

			aTicketTO.setTktItem(aTicket.getTktItem());

			QueryTicketRequest.Ticket.TktID tktId = aTicket.getTktID();

			/*
			 * According to XML Specification, only one ticket identity is to be passed. If multiple or present, then accept the first one in this order: barcode, external, nid, mag, dssn
			 */
			if (tktId.getBarcode() != null) {
				aTicketTO.setBarCode(tktId.getBarcode());
			}
			else {
				if (tktId.getExternal() != null) {
					aTicketTO.setExternal(tktId.getExternal());
				}
				else {
					if (tktId.getTktNID() != null) {
						aTicketTO.setTktNID(tktId.getTktNID());
					}
					else {
						if (tktId.getMag() != null) {
							QueryTicketRequest.Ticket.TktID.Mag tktMag = tktId
									.getMag();
							String mag1 = tktMag.getMagTrack1();
							String mag2 = tktMag.getMagTrack2();

							if (mag2 != null) {
								aTicketTO.setMag(mag1, mag2);
							}
							else {
								aTicketTO.setMag(mag1);
							}
						}
						else {
							if (tktId.getTktDSSN() != null) {
								QueryTicketRequest.Ticket.TktID.TktDSSN tktDssn = tktId
										.getTktDSSN();
								XMLGregorianCalendar tXCal = tktDssn
										.getTktDate();
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

			ticketListTO.add(aTicketTO);

		} // end while

		// IncludeTktDemographics
		Boolean isTktDemo = queryReq.isIncludeTktDemographics();
		if (isTktDemo != null) {
			if (isTktDemo.booleanValue()) {
				queryReqTO.setIncludeTktDemographics(true);
			}
		}

		// IncludePassAttributes
		Boolean isPassAttr = queryReq.isIncludePassAttributes();
		if (isPassAttr != null) {
			if (isPassAttr.booleanValue()) {
				queryReqTO.setIncludePassAttributes(true);
			}
		}

		// IncludeElectronicAttributes
		Boolean isElectronic = queryReq.isIncludeElectronicAttributes();
		if (isElectronic != null) {
			if (isElectronic.booleanValue()) {
				queryReqTO.setIncludeElectronicAttributes(true);
			}
		}

		// IncludeTicketRedeemability
		Boolean isRedeemable = queryReq.isIncludeTicketRedeemability();
		if (isRedeemable != null) {
			if (isRedeemable.booleanValue()) {
				queryReqTO.setIncludeTicketRedeemability(true);
			}
		}

		// IncludeEntitlementAccount
		Boolean includeAccountId = queryReq.isIncludeEntitlementAccount();
		if (includeAccountId != null) {
			if (includeAccountId.booleanValue()) {
				queryReqTO.setIncludeEntitlementAccount(true);
			}
		}

		// IncludeRenewalAttributes
		Boolean isRenewalAttr = queryReq.isIncludeRenewalAttributes();
		if (isRenewalAttr != null) {
			if (isRenewalAttr.booleanValue()) {
				queryReqTO.setIncludeRenewalAttributes(true);
			}
		}

		queryReqTO.setTktList(ticketListTO);

		return queryReqTO;

	}

	/**
	 * When passed the DTI Application object, return the JAXB object.
	 * 
	 * @param qryTktRespTO
	 *            The query ticket response object (DTI Application object)
	 * @return the JAXB version of the QueryTicketResponse
	 * @throws JAXBException
	 *             should any parsing errors occur.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryTicketResponse getJaxb(
			QueryTicketResponseTO qryTktRespTO, DTIErrorTO errorTO) throws JAXBException {

		QueryTicketResponse response = new QueryTicketResponse();

		List<QueryTicketResponse.Ticket> ticketList = response.getTicket();
		ArrayList<TicketTO> respTOtktList = qryTktRespTO.getTicketList();

		for /* each */(TicketTO aTicketTO : /* in */respTOtktList) {

			QueryTicketResponse.Ticket aTicket = new QueryTicketResponse.Ticket();

			// Ticket Item
			BigInteger tktItemTO = aTicketTO.getTktItem();
			QName qName = new QName("TktItem");
			JAXBElement<BigInteger> tktItem = new JAXBElement(qName,
					tktItemTO.getClass(), tktItemTO);
			aTicket.getTktItemOrTktIDOrTktPrice().add(tktItem);

			// TktID
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO
					.getTicketTypes();

			if (typeList.size() > 0) {

				QueryTicketResponse.Ticket.TktID tktIdObj = new QueryTicketResponse.Ticket.TktID();

				// Mag Code?
				if (typeList.contains(TicketTO.TicketIdType.MAG_ID)) {
					String tktMagTO = aTicketTO.getMagTrack1();
					qName = new QName("Mag");
					JAXBElement<String> tktMag = new JAXBElement(qName,
							tktMagTO.getClass(), tktMagTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktMag);
				}

				// Bar Code?
				if (typeList.contains(TicketTO.TicketIdType.BARCODE_ID)) {
					String tktBarTO = aTicketTO.getBarCode();
					qName = new QName("Barcode");
					JAXBElement<String> tktBar = new JAXBElement(qName,
							tktBarTO.getClass(), tktBarTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktBar);
				}

				// Tkt DSSN?
				if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
					QueryTicketResponse.Ticket.TktID.TktDSSN tktDssn = new QueryTicketResponse.Ticket.TktID.TktDSSN();
					tktDssn.setTktDate(aTicketTO.getDssnDateString());
					tktDssn.setTktNbr(aTicketTO.getDssnNumber());
					tktDssn.setTktSite(aTicketTO.getDssnSite());
					tktDssn.setTktStation(aTicketTO.getDssnStation());
					qName = new QName("TktDSSN");
					JAXBElement<String> tktDssnElement = new JAXBElement(qName,
							tktDssn.getClass(), tktDssn);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktDssnElement);
				}

				// Tkt NID ?
				if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {
					String tktNidTO = aTicketTO.getTktNID();
					qName = new QName("TktNID");
					JAXBElement<String> tktNid = new JAXBElement(qName,
							tktNidTO.getClass(), tktNidTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktNid);

				}

				// External?
				if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {
					String tktExtTO = aTicketTO.getExternal();
					qName = new QName("External");
					JAXBElement<String> tktExt = new JAXBElement(qName,
							tktExtTO.getClass(), tktExtTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktExt);
				}

				// Add whatever ticket versions were found to the response.
				qName = new QName("TktID");
				JAXBElement<String> tktId = new JAXBElement(qName,
						tktIdObj.getClass(), tktIdObj);
				aTicket.getTktItemOrTktIDOrTktPrice().add(tktId);

			}

			// TktPrice?
			if (aTicketTO.getTktPrice() != null) {
				BigDecimal tktPriceTO = aTicketTO.getTktPrice();
				qName = new QName("TktPrice");
				JAXBElement<String> tktPrice = new JAXBElement(qName,
						tktPriceTO.getClass(), tktPriceTO);
				aTicket.getTktItemOrTktIDOrTktPrice().add(tktPrice);
			}

			// TktTax?
			if (aTicketTO.getTktTax() != null) {
				BigDecimal tktTaxTO = aTicketTO.getTktTax();
				qName = new QName("TktTax");
				JAXBElement<String> tktTax = new JAXBElement(qName,
						tktTaxTO.getClass(), tktTaxTO);
				aTicket.getTktItemOrTktIDOrTktPrice().add(tktTax);
			}

			// TktStatus?
			ArrayList<TktStatusTO> statusListTO = aTicketTO.getTktStatusList();
			if (statusListTO != null) {
				if (statusListTO.size() > 0) {
					for /* each */(TktStatusTO aStatusTO : /* in */statusListTO) {
						QueryTicketResponse.Ticket.TktStatus tktStatus = new QueryTicketResponse.Ticket.TktStatus();
						tktStatus.setStatusItem(aStatusTO.getStatusItem());
						tktStatus.setStatusValue(aStatusTO.getStatusValue());
						qName = new QName("TktStatus");
						JAXBElement<String> tktStatusElement = new JAXBElement(
								qName, tktStatus.getClass(), tktStatus);
						aTicket.getTktItemOrTktIDOrTktPrice().add(
								tktStatusElement);
					}
				}
			}

			// TktValidity?
			if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO
					.getTktValidityValidEnd() != null)) {
				QueryTicketResponse.Ticket.TktValidity tktValidity = new QueryTicketResponse.Ticket.TktValidity();

				XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidStart());
				tktValidity.setValidStart(xCalDate);

				xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidEnd());
				tktValidity.setValidEnd(xCalDate);

				qName = new QName("TktValidity");
				JAXBElement<String> tktValidityElement = new JAXBElement(qName,
						tktValidity.getClass(), tktValidity);
				aTicket.getTktItemOrTktIDOrTktPrice().add(tktValidityElement);

			}

			// TktAttributes?
			if (aTicketTO.isAttributed()) {

				QueryTicketResponse.Ticket.TktAttribute tktAttribute = new QueryTicketResponse.Ticket.TktAttribute();

				if (aTicketTO.getResident() != null) tktAttribute
						.setResident(aTicketTO.getResident());

				if (aTicketTO.getPassRenew() != null) tktAttribute
						.setPassRenew(aTicketTO.getPassRenew());

				if (aTicketTO.getPassClass() != null) tktAttribute
						.setPassClass(aTicketTO.getPassClass());

				if (aTicketTO.getPassType() != null) tktAttribute
						.setPassType(aTicketTO.getPassType());

				if (aTicketTO.getMediaType() != null) tktAttribute
						.setMediaType(aTicketTO.getMediaType());

				if (aTicketTO.getAgeGroup() != null) tktAttribute
						.setAgeGroup(aTicketTO.getAgeGroup());

				if (aTicketTO.getPassName() != null) tktAttribute
						.setPassName(aTicketTO.getPassName());

				if (aTicketTO.getLastDateUsed() != null) {
					XMLGregorianCalendar xCalDate = UtilXML
							.convertToXML(aTicketTO.getLastDateUsed());
					tktAttribute.setLastDateUsed(xCalDate);
				}

				if (aTicketTO.getTimesUsed() != null) {
					tktAttribute.setTimesUsed(aTicketTO.getTimesUsed());
				}

				if (aTicketTO.getReplacedByPass() != null) tktAttribute
						.setReplacedByPass(aTicketTO.getReplacedByPass());

				qName = new QName("TktAttribute");
				JAXBElement<String> tktAttributeElement = new JAXBElement(
						qName, tktAttribute.getClass(), tktAttribute);
				aTicket.getTktItemOrTktIDOrTktPrice().add(tktAttributeElement);
>>>>>>> develop

          if (aDemoTO.getZip() != null) {
            demoData.setZip(aDemoTO.getZip());
          }

<<<<<<< HEAD
          if (aDemoTO.getCountry() != null) {
            demoData.setCountry(aDemoTO.getCountry());
          }
=======
			// Ticket Demographics
			if ((aTicketTO.getTicketDemoList() != null) && (aTicketTO
					.getTicketDemoList().size() == 1)) {
>>>>>>> develop

          if (aDemoTO.getTelephone() != null) {
            demoData.setTelephone(aDemoTO.getTelephone());
          }

<<<<<<< HEAD
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
=======
					DemographicsTO aDemoTO = aTicketTO.getTicketDemoList().get(
							0);
					QueryTicketResponse.Ticket.TktDemographics tktDemo = new QueryTicketResponse.Ticket.TktDemographics();
					QueryTicketResponse.Ticket.TktDemographics.DemoData demoData = new QueryTicketResponse.Ticket.TktDemographics.DemoData();
>>>>>>> develop

          qName = new QName("TktDemographics");

<<<<<<< HEAD
          tktDemo.setDemoData(demoData);
          JAXBElement<String> tktDemographicsElement = new JAXBElement(qName,
              tktDemo.getClass(), tktDemo);
          aTicket.getTktItemOrTktIDOrTktPrice().add(tktDemographicsElement);

        }
      }

      // Ticket Error
      if ((errorTO != null)
          && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

        QueryTicketResponse.Ticket.TktError tktError = new QueryTicketResponse.Ticket.TktError();
        tktError.setTktErrorCode(errorTO.getErrorCode());
        tktError.setTktErrorType(errorTO.getErrorType());
        tktError.setTktErrorClass(errorTO.getErrorClass());
        tktError.setTktErrorText(errorTO.getErrorText());

        qName = new QName("TktError");
        JAXBElement<String> tktErrorElement = new JAXBElement(qName,
            tktError.getClass(), tktError);

        aTicket.getTktItemOrTktIDOrTktPrice().add(tktErrorElement);
      }

      ticketList.add(aTicket);
    }

    return response;
  }
=======
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
					aTicket.getTktItemOrTktIDOrTktPrice().add(
							tktDemographicsElement);

				}
			}

			// Entitlement Account Id? 2.15.1 BIEST001
			if (aTicketTO.getAccountId() != null) {
				String tktAccountId = aTicketTO.getAccountId();
				qName = new QName("AccountId");
				JAXBElement<String> accountId = new JAXBElement(qName,
						tktAccountId.getClass(), tktAccountId);
				aTicket.getTktItemOrTktIDOrTktPrice().add(accountId);
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

				aTicket.getTktItemOrTktIDOrTktPrice().add(tktErrorElement);
			}

			ticketList.add(aTicket);
		}

		return response;
	}
>>>>>>> develop

}
