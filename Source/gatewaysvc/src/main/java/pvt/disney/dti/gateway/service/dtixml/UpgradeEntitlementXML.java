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
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.UpgradeEntitlementRequest;
import pvt.disney.dti.gateway.request.xsd.UpgradeEntitlementRequest.Ticket;
import pvt.disney.dti.gateway.request.xsd.UpgradeEntitlementRequest.Ticket.ProdDemoData;
import pvt.disney.dti.gateway.request.xsd.UpgradeEntitlementRequest.Ticket.ProdDemoData.TktDemoData;
import pvt.disney.dti.gateway.response.xsd.UpgradeEntitlementResponse;
import pvt.disney.dti.gateway.response.xsd.UpgradeEntitlementResponse.Payment;

/**
 * This class contains all of the translation logic for handling XML -> object transformations.
 * @author HUFFM017
 * 
 */

public class UpgradeEntitlementXML {
	
	/**
	 * Gets the DTI transfer object from the JAXB version of the Upgrade Entitlement Request.
	 * 
	 * @param upgradeEntitlementReq
	 * @return
	 * @throws JAXBException
	 */
	public static UpgradeEntitlementRequestTO getTO(
			UpgradeEntitlementRequest upgradeEntitlementReq) throws JAXBException {

		UpgradeEntitlementRequestTO upgradeEntitlementReqTO = new UpgradeEntitlementRequestTO();

		// A Ticket
		List<UpgradeEntitlementRequest.Ticket> ticketList = upgradeEntitlementReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(UpgradeEntitlementRequest.Ticket aTicket : /* in */ticketList) {
			setTOTicketList(ticketListTO, aTicket);
		}
		upgradeEntitlementReqTO.setTicketList(ticketListTO);

		// Payment List
		List<UpgradeEntitlementRequest.Payment> paymentList = upgradeEntitlementReq
				.getPayment();
		ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
		for /* each */(UpgradeEntitlementRequest.Payment aPayment : /* in */paymentList) {
			setTOPaymentList(paymentListTO, aPayment);
		}
		upgradeEntitlementReqTO.setPaymentList(paymentListTO);

		// AuditNotation (as of 2.12)
		if (upgradeEntitlementReq.getAuditNotation() != null) {
			upgradeEntitlementReqTO.setAuditNotation(upgradeEntitlementReq.getAuditNotation());
		}

		// Reservation
	    if (upgradeEntitlementReq.getReservation() != null) {
	    	UpgradeEntitlementRequest.Reservation renewEnt = upgradeEntitlementReq
	          .getReservation();
	      ReservationTO resvTO = new ReservationTO();
	      List<JAXBElement<?>> aReservationList = renewEnt.getResCodeOrResCreateDateOrResPickupDate();
	      for /* each */(JAXBElement<?> aReservationEntry : /* in */aReservationList) {
	        setTOReservation(resvTO, aReservationEntry);
	      }
	      upgradeEntitlementReqTO.setReservation(resvTO);
	    }

	    // ClientData
	    if (upgradeEntitlementReq.getClientData() != null) {
	    	UpgradeEntitlementRequest.ClientData clientData = upgradeEntitlementReq.getClientData();
	      ClientDataTO clientDataTO = new ClientDataTO();
	      setTOClientData(clientData, clientDataTO);
	      upgradeEntitlementReqTO.setClientData(clientDataTO);
	    }
	    
	    // Eligibility
	    if (upgradeEntitlementReq.getEligibility() != null) {
	       UpgradeEntitlementRequest.Eligibility elig = upgradeEntitlementReq.getEligibility();
	       upgradeEntitlementReqTO.setEligibilityGroup(elig.getGroup());
	       upgradeEntitlementReqTO.setEligibilityMember(elig.getMember());
	    }
	   	   	    
		return upgradeEntitlementReqTO;
	}
	
	/**
	   * Sets the to reservation.
	   * 
	   * @param resvTO
	   *            the resv transfer object
	   * @param aReservationEntry
	   *            a JAXB reservation entry
	   */
	  private static void setTOReservation(ReservationTO resvTO,
	      JAXBElement<?> aReservationEntry) {

	    QName fieldName = aReservationEntry.getName();

	    if (fieldName.getLocalPart().equalsIgnoreCase("ResCode")) {
	      resvTO.setResCode((String) aReservationEntry.getValue());
	    }
	    else if (fieldName.getLocalPart().equalsIgnoreCase("ResNumber")) {
	      resvTO.setResNumber((String) aReservationEntry.getValue());
	    }
	    else if (fieldName.getLocalPart().equalsIgnoreCase("ResCreateDate")) {
	      XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aReservationEntry.getValue();
	      GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
	      resvTO.setResCreateDate(tempCalendar);
	    }
	    else if (fieldName.getLocalPart().equalsIgnoreCase("ResPickupDate")) {
	      XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aReservationEntry.getValue();
	      GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
	      resvTO.setResPickupDate(tempCalendar);
	    }
	    else if (fieldName.getLocalPart().equalsIgnoreCase("ResPickupArea")) {
	      resvTO.setResPickupArea((String) aReservationEntry.getValue());
	    }
	    else if (fieldName.getLocalPart().equalsIgnoreCase("ResSalesType")) {
	      resvTO.setResSalesType((String) aReservationEntry.getValue());
	    }

	    return;
	  }
	  
	  /**
	   * Sets the to client data.
	   * 
	   * @param clientData
	   *            the client data parsed from JAXB
	   * @param clientDataTO
	   *            the client data transfer object
	   */
	  private static void setTOClientData(
			  UpgradeEntitlementRequest.ClientData clientData,
	      ClientDataTO clientDataTO) {

	    // Optional fields
	    if (clientData.getClientType() != null) {
	       clientDataTO.setClientType(clientData.getClientType());
	    }
	    if (clientData.getClientCategory() != null) {
	       clientDataTO.setClientCategory(clientData.getClientCategory());
	    }
	    if (clientData.getDemoLanguage() != null) {
	       clientDataTO.setDemoLanguage(clientData.getDemoLanguage());
	    }

	    // Optional Demographics
	    if (clientData.getDemoData() != null) {

	    	UpgradeEntitlementRequest.ClientData.DemoData demoData = clientData
	          .getDemoData();

	      if (demoData.getBill() != null) {
	        DemographicsTO demoTO = new DemographicsTO();
	        UpgradeEntitlementRequest.ClientData.DemoData.Bill billData = demoData.getBill();

	        // Optional Attributes
	        if (billData.getName() != null) {
	           demoTO.setName(billData.getName());
	        }
	        if (billData.getLastName() != null) {
	           demoTO.setLastName(billData.getLastName());
	        }
	        if (billData.getFirstName() != null) {
	           demoTO.setFirstName(billData.getFirstName());
	        }
	        if (billData.getAddr1() != null) {
	           demoTO.setAddr1(billData.getAddr1());
	        }
	        if (billData.getAddr2() != null) {
	           demoTO.setAddr2(billData.getAddr2());
	        }
	        if (billData.getCity() != null) {
	           demoTO.setCity(billData.getCity());
	        }
	        if (billData.getState() != null) {
	           demoTO.setState(billData.getState());
	        }
	        if (billData.getZip() != null) {
	           demoTO.setZip(billData.getZip());
	        }
	        if (billData.getCountry() != null) {
	           demoTO.setCountry(billData.getCountry());
	        }
	        if (billData.getTelephone() != null) {
	           demoTO.setTelephone(billData.getTelephone());
	        }
	        if (billData.getEmail() != null) {
	           demoTO.setEmail(billData.getEmail());
	        }
	        if (billData.getSellerResNbr() != null) {
	           demoTO.setSellerResNbr(billData.getSellerResNbr());
	        }

	        clientDataTO.setBillingInfo(demoTO);

	      }

	      if (demoData.getShip() != null) {

	        DemographicsTO demoTO = new DemographicsTO();
	        UpgradeEntitlementRequest.ClientData.DemoData.Ship shipData = demoData.getShip();

	        // Optional Attributes
	        if (shipData.getName() != null) {
	           demoTO.setName(shipData.getName());
	        }
	        if (shipData.getLastName() != null) {
	           demoTO.setLastName(shipData.getLastName());
	        }
	        if (shipData.getFirstName() != null) {
	           demoTO.setFirstName(shipData.getFirstName());
	        }
	        if (shipData.getAddr1() != null) {
	           demoTO.setAddr1(shipData.getAddr1());
	        }
	        if (shipData.getAddr2() != null) {
	           demoTO.setAddr2(shipData.getAddr2());
	        }
	        if (shipData.getCity() != null) {
	           demoTO.setCity(shipData.getCity());
	        }
	        if (shipData.getState() != null) {
	           demoTO.setState(shipData.getState());
	        }
	        if (shipData.getZip() != null) {
	           demoTO.setZip(shipData.getZip());
	        }
	        if (shipData.getCountry() != null) {
	           demoTO.setCountry(shipData.getCountry());
	        }
	        if (shipData.getTelephone() != null) {
	           demoTO.setTelephone(shipData.getTelephone());
	        }
	        if (shipData.getEmail() != null) {
	           demoTO.setEmail(shipData.getEmail());
	        }

	        clientDataTO.setShippingInfo(demoTO);

	      }

	    }

	    return;
	  }

	/**
	 * Sets the Transfer Object ticket list for the upgrade entitlement command. 
	 * @param ticketListTO
	 * @param aTicket
	 */
   private static void setTOTicketList(ArrayList<TicketTO> ticketListTO, Ticket aTicket) {

		TicketTO aTicketTO = new TicketTO();

		// TktItem
      if (aTicket.getTktItem() != null)
         aTicketTO.setTktItem(aTicket.getTktItem());

		// TktId
		if (aTicket.getTktID() != null) {

			UpgradeEntitlementRequest.Ticket.TktID aTktId = aTicket.getTktID();

			// Mag
			if (aTktId.getMag() != null) {

            UpgradeEntitlementRequest.Ticket.TktID.Mag aMag = aTktId.getMag();

            if (aMag != null) {
               aTicketTO.setMag(aMag.getMagTrack1(), aMag.getMagTrack2());
            }
			}

			// BarCode
         if (aTktId.getBarcode() != null) {
            aTicketTO.setBarCode(aTktId.getBarcode());
         }

			// TktDSSN
			if (aTktId.getTktDSSN() != null) {

            UpgradeEntitlementRequest.Ticket.TktID.TktDSSN aDssn = aTktId.getTktDSSN();

				// TktDate | TktSite | TktStation | TktNbr
				XMLGregorianCalendar tXCal = aDssn.getTktDate();

            GregorianCalendar tempCalendar = new GregorianCalendar(tXCal.getEonAndYear().intValue(),
                     (tXCal.getMonth() - 1), tXCal.getDay());

            aTicketTO.setDssn(tempCalendar, aDssn.getTktSite(), aDssn.getTktStation(), aDssn.getTktNbr());
			}

			// TktNID
         if (aTktId.getTktNID() != null) {
            aTicketTO.setTktNID(aTktId.getTktNID());
         }

			// External
         if (aTktId.getExternal() != null) {
            aTicketTO.setExternal(aTktId.getExternal());
         }

		}

		// (To) ProdCode
      if (aTicket.getProdCode() != null) {
         aTicketTO.setProdCode(aTicket.getProdCode());
      }

		// (To) ProdPrice
      if (aTicket.getProdPrice() != null) {
         aTicketTO.setProdPrice(new BigDecimal(aTicket.getProdPrice()));
      }

		// (To) ProdDemoData (as of 2.15, JTL)
		// Optional items
		// ProdDemoData (opt)
		if (aTicket.getProdDemoData() != null) {

			ProdDemoData aProdDemoData = aTicket.getProdDemoData();

			if (aProdDemoData.getTktDemoData() != null) {

				TktDemoData aTktDemographic = aProdDemoData.getTktDemoData();
				DemographicsTO aTicketDemoTO = new DemographicsTO();

				// NOTE: In this specific case, the XSD enforces minimum lengths,
				// so we know the length will never be zero.

        // FirstName
        if (aTktDemographic.getFirstName() != null) {
           aTicketDemoTO.setFirstName(aTktDemographic.getFirstName());
        }

        // LastName
        if (aTktDemographic.getLastName() != null) {
           aTicketDemoTO.setLastName(aTktDemographic.getLastName());
        }

        // DateOfBirth
        if (aTktDemographic.getDateOfBirth() != null) {
           XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aTktDemographic.getDateOfBirth();
           GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
           aTicketDemoTO.setDateOfBirth(tempCalendar);
        }
        
        // Gender
        if (aTktDemographic.getGender() != null) {
           aTicketDemoTO.setGender(aTktDemographic.getGender());
        }

        // Addr1
        if (aTktDemographic.getAddr1() != null) {
           aTicketDemoTO.setAddr1(aTktDemographic.getAddr1());
        }

        // Addr2 (opt)
        if (aTktDemographic.getAddr2() != null) {
          aTicketDemoTO.setAddr2(aTktDemographic.getAddr2());
        }

        // City
        if (aTktDemographic.getCity() != null) {
        aTicketDemoTO.setCity(aTktDemographic.getCity());
        }

        // State (opt)
        if (aTktDemographic.getState() != null) {
          aTicketDemoTO.setState(aTktDemographic.getState());
        }

        // ZIP
        if (aTktDemographic.getZip() != null) {
           aTicketDemoTO.setZip(aTktDemographic.getZip());
        }

        // Country
            if (aTktDemographic.getCountry() != null) {
        aTicketDemoTO.setCountry(aTktDemographic.getCountry());
            }

        // Telephone (opt) as of 2.16.1 APMP (JTL)
        if (aTktDemographic.getTelephone() != null) {
          aTicketDemoTO.setTelephone(aTktDemographic.getTelephone());
        }

        // Email (opt)
        if (aTktDemographic.getEmail() != null) {
          aTicketDemoTO.setEmail(aTktDemographic.getEmail());
        }

        // OptInSolicit (2.10)
        if (aTktDemographic.getOptInSolicit() != null) {
          if (aTktDemographic.getOptInSolicit().compareTo("YES") == 0) {
            aTicketDemoTO.setOptInSolicit(new Boolean(true));
          } else {
            aTicketDemoTO.setOptInSolicit(new Boolean(false));
          }
        }

        // CellPhone (as of 2.16.3, JTL)
        if (aTktDemographic.getCellPhone() != null) {
          aTicketDemoTO.setCellPhone(aTktDemographic.getCellPhone());
        }
        
        // Seller Ref (as of 2.16.3, JTL)
        if (aTktDemographic.getSellerRef() != null) {
          aTicketDemoTO.setSellerRef(aTktDemographic.getSellerRef());
        }

				aTicketTO.addTicketDemographic(aTicketDemoTO);

			}

		}

		// UpgrdPrice
      if (aTicket.getUpgrdPrice() != null) {
         aTicketTO.setUpgrdPrice(new BigDecimal(aTicket.getUpgrdPrice()));
      }

		// FromProdCode
      if (aTicket.getFromProdCode() != null) {
         aTicketTO.setFromProdCode(aTicket.getFromProdCode());
      }
      
      // FromDLRPLU // As of 2.17.3, JTL
      if (aTicket.getFromDLRPLU() != null) {
         aTicketTO.setFromDlrPLU(aTicket.getFromDLRPLU());
      }

		// FromPrice
      if (aTicket.getFromPrice() != null) {
         aTicketTO.setFromPrice(new BigDecimal(aTicket.getFromPrice()));
      }

      // (as of 2.15, JTL) On an upgrade, it is implicit that the quantity is
      // one.
		// As this is a purchase, however, the quantity of purchased items must be
		// set.
		aTicketTO.setProdQty(new BigInteger("1"));

		// TktNote
      if (aTicket.getTktNote() != null) {
         aTicketTO.setTktNote(aTicket.getTktNote());
      }

		// add ticket to list
		ticketListTO.add(aTicketTO);

		return;
	}

	/**
	 * Sets the transfer object payment list for the Upgrade Entitlement transaction.
	 * 
	 * @param paymentListTO
	 * @param aPayment
	 */
	private static void setTOPaymentList(ArrayList<PaymentTO> paymentListTO,
			UpgradeEntitlementRequest.Payment aPayment) {

		PaymentTO aPaymentTO = new PaymentTO();

		// Required items
		// PayItem
		aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

		// Optional Items
		// PayType
		UpgradeEntitlementRequest.Payment.PayType payType = aPayment
				.getPayType();

		if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD

			CreditCardTO creditCardTO = new CreditCardTO();

			UpgradeEntitlementRequest.Payment.PayType.CreditCard creditCard = payType
					.getCreditCard();

			if (creditCard.getCCManual() != null) {

				UpgradeEntitlementRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard
						.getCCManual();

				// Required fields
				creditCardTO.setCcNbr(ccManual.getCCNbr());
				creditCardTO.setCcExpiration(ccManual.getCCExpiration());

				// Optional fields
				if (ccManual.getCCVV() != null) {
				   creditCardTO.setCcVV(ccManual.getCCVV());
				}
				if (ccManual.getCCName() != null) {
				   creditCardTO.setCcName(ccManual.getCCName());
				}
				if (ccManual.getCCStreet() != null) {
				   creditCardTO.setCcStreet(ccManual.getCCStreet());
				}
				if (ccManual.getCCZipcode() != null) {
				   creditCardTO.setCcZipCode(ccManual.getCCZipcode());
				}
				if (ccManual.getCCCAVV() != null) {
				   creditCardTO.setCcCAVV(ccManual.getCCCAVV());
				}
				if (ccManual.getCCEcommerce() != null) {
				   creditCardTO.setCcEcommerce(ccManual.getCCEcommerce());
				}
				if (ccManual.getCCType() != null) {
				   creditCardTO.setCcType(ccManual.getCCType());
				}

			}
			else if (creditCard.getCCSwipe() != null) {

				UpgradeEntitlementRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard
						.getCCSwipe();

				// Required fields
				creditCardTO.setCcTrack1(ccSwipe.getCCTrack1());
				creditCardTO.setCcTrack2(ccSwipe.getCCTrack2());

				// Optional fields
				if (ccSwipe.getCCVV() != null) {
				   creditCardTO.setCcVV(ccSwipe.getCCVV());
				}
				if (ccSwipe.getPosTerminal() != null) {
				   creditCardTO.setPosTermID(ccSwipe.getPosTerminal());
				}
				if (ccSwipe.getExtnlDevSerial() != null) {
				   creditCardTO.setExtnlDevSerial(ccSwipe.getExtnlDevSerial());
				}

			}
			else if (creditCard.getCCWireless() != null) { // as of 2.12

				UpgradeEntitlementRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard
						.getCCWireless();
				creditCardTO.setWireless(true);

				// Required fields
				creditCardTO.setCcTrack1(ccWireless.getCCTrack1());
				creditCardTO.setCcTrack2(ccWireless.getCCTrack2());

				// Optional fields
				if (ccWireless.getCCVV() != null) {
				   creditCardTO.setCcVV(ccWireless.getCCVV());
				}
				if (ccWireless.getPosTerminal() != null) {
				   creditCardTO.setPosTermID(ccWireless.getPosTerminal());
				}
				if (ccWireless.getExtnlDevSerial() != null) {
				   creditCardTO.setExtnlDevSerial(ccWireless.getExtnlDevSerial());
				}
			}

			aPaymentTO.setCreditCard(creditCardTO);

		}
		else if (payType.getVoucher() != null) { // THIS IS A VOUCHER
			VoucherTO voucherTO = new VoucherTO();

			// Required fields
			voucherTO.setMainCode(payType.getVoucher().getMainCode());

			// Optional fields
			if (payType.getVoucher().getUniqueCode() != null) {
			   voucherTO.setUniqueCode(payType.getVoucher().getUniqueCode());
			}

			aPaymentTO.setVoucher(voucherTO);

		}
		else if (payType.getGiftCard() != null) { // THIS IS A GIFT CARD

			GiftCardTO giftCardTO = new GiftCardTO();

			if (payType.getGiftCard().getGCManual() != null) {
				UpgradeEntitlementRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
						.getGiftCard().getGCManual();

				// Required fields
				giftCardTO.setGcNbr(gcManual.getGCNbr());

				// Optional fields
				if (gcManual.getGCStartDate() != null) {
					XMLGregorianCalendar tXCal = (XMLGregorianCalendar) gcManual
							.getGCStartDate();
					GregorianCalendar tempCalendar = UtilXML
							.convertFromXML(tXCal);
					giftCardTO.setGcStartDate(tempCalendar);
				}

				aPaymentTO.setGiftCard(giftCardTO);

			}
			else if (payType.getGiftCard().getGCSwipe() != null) {

				UpgradeEntitlementRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType
						.getGiftCard().getGCSwipe();

				// Required fields
				giftCardTO.setGcTrack2(gcSwipe.getGCTrack2());

				// Optional fields
				if (gcSwipe.getGCTrack1() != null) {
					giftCardTO.setGcTrack1(gcSwipe.getGCTrack1());
				}

				aPaymentTO.setGiftCard(giftCardTO);
			}
      } else if (payType.getInstallment() != null) { // (As of 2.17.3, JTL)

         InstallmentTO installmentTO = new InstallmentTO();
         UpgradeEntitlementRequest.Payment.PayType.Installment installment = payType.getInstallment();
         UpgradeEntitlementRequest.Payment.PayType.Installment.InstallmentCreditCard installCard = installment
                  .getInstallmentCreditCard();

         InstallmentCreditCardTO installCCTO = installmentTO.getCreditCard();

         if (installCard.getCCManual() != null) {
            installCCTO.setCcNbr(installCard.getCCManual().getCCNbr());
            installCCTO.setCcExpiration(installCard.getCCManual().getCCExpiration());
            installCCTO.setCcName(installCard.getCCManual().getCCName());

            if (installCard.getCCManual().getCCVV() != null) {
               installCCTO.setCcVV(installCard.getCCManual().getCCVV());
            }
            if (installCard.getCCManual().getCCStreet() != null) {
               installCCTO.setCcStreet(installCard.getCCManual().getCCStreet());
            }
            if (installCard.getCCManual().getCCZipcode() != null) {
               installCCTO.setCcZipCode(installCard.getCCManual().getCCZipcode());
            }
            if (installCard.getCCManual().getCCType() != null) {
               installCCTO.setCcType(installCard.getCCManual().getCCType());
            }

         } else {
            installCCTO.setCcTrack1(installCard.getCCSwipe().getCCTrack1());
            installCCTO.setCcTrack2(installCard.getCCSwipe().getCCTrack2());
         }

         UpgradeEntitlementRequest.Payment.PayType.Installment.InstallmentDemoData installDemo = installment
                  .getInstallmentDemoData();
         InstallmentDemographicsTO installDemoTO = installmentTO.getInstllDemo();

         // First Name
         installDemoTO.setFirstName(installDemo.getFirstName());

         // Middle Name (opt)
         if (installDemo.getMiddleName() != null) {
            installDemoTO.setMiddleName(installDemo.getMiddleName());
         }

         // Last Name
         installDemoTO.setLastName(installDemo.getLastName());

         // DOB (opt)
         if (installDemo.getDateOfBirth() != null) {
            XMLGregorianCalendar tXCal = (XMLGregorianCalendar) installDemo.getDateOfBirth();
            GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
            installDemoTO.setDateOfBirth(tempCalendar);
         }

         // Addr1
         installDemoTO.setAddr1(installDemo.getAddr1());

         // Addr2 (opt)
         if (installDemo.getAddr2() != null) {
            installDemoTO.setAddr2(installDemo.getAddr2());
         }

         // City
         installDemoTO.setCity(installDemo.getCity());

         // State
         installDemoTO.setState(installDemo.getState());

         // ZIP
         installDemoTO.setZip(installDemo.getZip());

         // Country
         installDemoTO.setCountry(installDemo.getCountry());

         // Telephone
         installDemoTO.setTelephone(installDemo.getTelephone());

         // AltTelephone (opt)
         if (installDemo.getAltTelephone() != null) {
            installDemoTO.setAltTelephone(installDemo.getAltTelephone());
         }

         // Email
         installDemoTO.setEmail(installDemo.getEmail());

         // Set the installment
         aPaymentTO.setInstallment(installmentTO);

      }

		// PayAmount
		if (aPayment.getPayAmount() != null) aPaymentTO
				.setPayAmount(new BigDecimal(aPayment.getPayAmount()));

		// Add payment to list
		paymentListTO.add(aPaymentTO);

	}

	/**
	 * Creates the JAXB upgrade entitlement response.
	 * 
	 * @param uEntRespTO
	 * @param errorTO
	 * @return
	 * @throws JAXBException
	 */
	public static UpgradeEntitlementResponse getJaxb(
			UpgradeEntitlementResponseTO uEntRespTO, DTIErrorTO errorTO) throws JAXBException {

		UpgradeEntitlementResponse uEntResp = new UpgradeEntitlementResponse();

		// Ticket
		if (uEntRespTO.getTicketList() != null) {
			List<UpgradeEntitlementResponse.Ticket> ticketList = uEntResp
					.getTicket();
			ArrayList<TicketTO> ticketListTO = uEntRespTO.getTicketList();
			setJaxbTicketList(ticketList, ticketListTO, errorTO);
		}

		// Payment
		if (uEntRespTO.getPaymentList() != null) {
			List<UpgradeEntitlementResponse.Payment> paymentList = uEntResp
					.getPayment();
			ArrayList<PaymentTO> paymentListTO = uEntRespTO.getPaymentList();
			setJaxbPaymentList(paymentList, paymentListTO);
		}
		
		return uEntResp;
	}

	/**
	 * Sets the ticket list in JAXB.
	 * 
	 * @param ticketList
	 * @param ticketListTO
	 * @param errorTO
	 * @throws JAXBException
	 */
	@SuppressWarnings("unchecked")
	private static void setJaxbTicketList(
			List<pvt.disney.dti.gateway.response.xsd.UpgradeEntitlementResponse.Ticket> ticketList,
			ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			UpgradeEntitlementResponse.Ticket aTicket = new UpgradeEntitlementResponse.Ticket();

			// Ticket Item
			if (aTicketTO.getTktItem() != null) {
			   aTicket.setTktItem(aTicketTO.getTktItem());
			}

			// TktID
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO.getTicketTypes();

			if (typeList.size() > 0) {

				UpgradeEntitlementResponse.Ticket.TktID tktIdObj = new UpgradeEntitlementResponse.Ticket.TktID();

				// Mag Code?
				if (typeList.contains(TicketTO.TicketIdType.MAG_ID)) {
					String tktMagTO = aTicketTO.getMagTrack1();
					QName qName = new QName("Mag");
					JAXBElement<String> tktMag = new JAXBElement<String>(qName,
							(Class<String>) tktMagTO.getClass(), tktMagTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktMag);
				}

				// Bar Code?
				if (typeList.contains(TicketTO.TicketIdType.BARCODE_ID)) {
					String tktBarTO = aTicketTO.getBarCode();
					QName qName = new QName("Barcode");
					JAXBElement<String> tktBar = new JAXBElement<String>(qName,
							(Class<String>) tktBarTO.getClass(), tktBarTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktBar);
				}

				// Tkt DSSN?
				if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {

					UpgradeEntitlementResponse.Ticket.TktID.TktDSSN tktDssn = new UpgradeEntitlementResponse.Ticket.TktID.TktDSSN();

					tktDssn.setTktDate(aTicketTO.getDssnDateString());
					tktDssn.setTktNbr(aTicketTO.getDssnNumber());
					tktDssn.setTktSite(aTicketTO.getDssnSite());
					tktDssn.setTktStation(aTicketTO.getDssnStation());
					QName qName = new QName("TktDSSN");
					@SuppressWarnings("rawtypes")
					JAXBElement<String> tktDssnElement = new JAXBElement(qName,
							tktDssn.getClass(), tktDssn);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktDssnElement);
				}

				// Tkt NID ?
				if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {
					String tktNidTO = aTicketTO.getTktNID();
					QName qName = new QName("TktNID");
					JAXBElement<String> tktNid = new JAXBElement<String>(qName,
							(Class<String>) tktNidTO.getClass(), tktNidTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktNid);

				}

				// External?
				if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {
					String tktExtTO = aTicketTO.getExternal();
					QName qName = new QName("External");
					JAXBElement<String> tktExt = new JAXBElement<String>(qName,
							(Class<String>) tktExtTO.getClass(), tktExtTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktExt);
				}

				// Add whatever ticket versions were found to the response.
				aTicket.setTktID(tktIdObj);

			}

			// Ticket Price
			if (aTicketTO.getTktPrice() != null) {
			   aTicket.setTktPrice(aTicketTO.getTktPrice());
			}

			// Ticket Tax
			if (aTicketTO.getTktTax() != null) {
			   aTicket.setTktTax(aTicketTO.getTktTax());
			}

			// Ticket Validity
			if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO
					.getTktValidityValidEnd() != null)) {

				UpgradeEntitlementResponse.Ticket.TktValidity tktValidity = new UpgradeEntitlementResponse.Ticket.TktValidity();

				GregorianCalendar tXCal = aTicketTO.getTktValidityValidStart();

				QName qName = new QName("ValidStart");
				@SuppressWarnings("rawtypes")
				JAXBElement<XMLGregorianCalendar> tktValStart = new JAXBElement(
						qName, tXCal.getClass(), tXCal);
				tktValidity.setValidEnd(tktValStart.getValue());

				tXCal = aTicketTO.getTktValidityValidEnd();

				qName = new QName("ValidEnd");
				@SuppressWarnings("rawtypes")
				JAXBElement<XMLGregorianCalendar> tktValEnd = new JAXBElement(qName, tXCal.getClass(), tXCal);
				tktValidity.setValidEnd(tktValEnd.getValue());

				aTicket.setTktValidity(tktValidity);
			}

			// Ticket Error
			if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

				UpgradeEntitlementResponse.Ticket.TktError tktError = new UpgradeEntitlementResponse.Ticket.TktError();

				tktError.setTktErrorCode(errorTO.getErrorCode());
				tktError.setTktErrorType(errorTO.getErrorType());
				tktError.setTktErrorClass(errorTO.getErrorClass());
				tktError.setTktErrorText(errorTO.getErrorText());

				aTicket.setTktError(tktError);
			}

			// Ticket (To) Prod Code
			if (aTicketTO.getProdCode() != null) {
			   aTicket.setTktProdCode(aTicketTO.getProdCode());
			}

			// TktTransaction
			if (aTicketTO.getTktTran() != null) {

				UpgradeEntitlementResponse.Ticket.TktTransaction tktTran = new UpgradeEntitlementResponse.Ticket.TktTransaction();
				TicketTransactionTO tktTranTO = aTicketTO.getTktTran();

				// TranDSSN
				if (tktTranTO.getDssnDate() != null) {

					UpgradeEntitlementResponse.Ticket.TktTransaction.TranDSSN tranDssn = new UpgradeEntitlementResponse.Ticket.TktTransaction.TranDSSN();

					// Required Fields
					tranDssn.setTranDate(tktTranTO.getDssnDateString());
					tranDssn.setTranSite(tktTranTO.getDssnSite());
					tranDssn.setTranStation(tktTranTO.getDssnStation());
					tranDssn.setTranNbr(tktTranTO.getDssnNumber());

					tktTran.setTranDSSN(tranDssn);
				}

				// TranNID
				if (tktTranTO.getTranNID() != null) {
				   tktTran.setTranNID(tktTranTO.getTranNID());
				}

				aTicket.setTktTransaction(tktTran);

			}
			
         ticketList.add(aTicket);

		} // end for loop
		return;
	}

	/**
	 * Sets the payment list in JAXB.
	 * 
	 * @param paymentList
	 * @param paymentListTO
	 * @throws JAXBException
	 */
	private static void setJaxbPaymentList(List<Payment> paymentList,
			ArrayList<PaymentTO> paymentListTO) throws JAXBException {

		for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

			UpgradeEntitlementResponse.Payment aPayment = new UpgradeEntitlementResponse.Payment();

			aPayment.setPayItem(aPaymentTO.getPayItem());

			UpgradeEntitlementResponse.Payment.PayType aPayType = new UpgradeEntitlementResponse.Payment.PayType();

			if (aPaymentTO.getCreditCard() != null) {

				UpgradeEntitlementResponse.Payment.PayType.CreditCard aCreditCard = new UpgradeEntitlementResponse.Payment.PayType.CreditCard();
				CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();

				// Required fields
				aCreditCard.setCCAuthCode(aCreditCardTO.getCcAuthCode());

				// Optional fields
				if (aCreditCardTO.getCcAuthNumber() != null) {
				   aCreditCard.setCCAuthNumber(aCreditCardTO.getCcAuthNumber());
				}
				if (aCreditCardTO.getCcAuthSysResponse() != null) {
				   aCreditCard.setCCAuthSysResponse(aCreditCardTO.getCcAuthSysResponse());
				}
				if (aCreditCardTO.getCcNumber() != null) {
					aCreditCard.setCCNumber(aCreditCardTO.getCcNumber());
				}

				aPayType.setCreditCard(aCreditCard);

			}
			else if (aPaymentTO.getGiftCard() != null) {

				UpgradeEntitlementResponse.Payment.PayType.GiftCard aGiftCard = new UpgradeEntitlementResponse.Payment.PayType.GiftCard();

				GiftCardTO aGiftCardTO = aPaymentTO.getGiftCard();

				// Required fields
				aGiftCard.setGCAuthCode(aGiftCardTO.getGcAuthCode());

				// Optional fields
				if (aGiftCardTO.getGcAuthNumber() != null) {
				   aGiftCard.setGCAuthNumber(aGiftCardTO.getGcAuthNumber());
				}
				if (aGiftCardTO.getGcAuthSysResponse() != null) {
				   aGiftCard.setGCAuthSysResponse(aGiftCardTO.getGcAuthSysResponse());
				}
				if (aGiftCardTO.getGcNumber() != null) {
				   aGiftCard.setGCNumber(aGiftCardTO.getGcNumber());
				}
				if (aGiftCardTO.getGcRemainingBalance() != null) {
				   aGiftCard.setGCRemainingBalance(aGiftCardTO.getGcRemainingBalance());
				}
				if (aGiftCardTO.getGcPromoExpDate() != null) {
					aGiftCard.setGCPromoExpDate(UtilXML.convertToXML(aGiftCardTO.getGcPromoExpDate()));
				}

				aPayType.setGiftCard(aGiftCard);

			}

			aPayment.setPayType(aPayType);

			paymentList.add(aPayment);
		}

	}

}
