package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryEligibleProductsTo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

public class OTQueryEligibleProductsXML {
	
	  private static EventLogger logger = EventLogger
		      .getLogger(OTQueryEligibleProductsXML.class);
	/**
	   * Adds the transaction body of renew ticket to the command stanza when provided with the appropriate transfer object.
	   * 
	   * @param otQryEligPrds
	   *            The Omni Ticket Update Transaction Transfer Object.
	   * @param commandStanza
	   *            The command stanza needing the update transaction stanza.
	   */
	static void addTxnBodyElement(OTQueryEligibleProductsTo otQryEligPrds,
		      Element commandStanza) {
		Element qryEligblePrdsStanza = commandStanza.addElement("QueryEligibleProducts");
		
		// WorkRules
		ArrayList<String> tagList = otQryEligPrds.getTagsList();
	    OTCommandXML.addWorkRules(qryEligblePrdsStanza, tagList);
	    
	 // SiteNumber
	    qryEligblePrdsStanza.addElement("SiteNumber").addText(
	    		otQryEligPrds.getSiteNumber().toString());
	    
	 // Sales Type
	    
	    qryEligblePrdsStanza.addElement("SaleType").addText(otQryEligPrds.getSaletype().toString());
	 // TicketInfo
	    ArrayList<OTTicketInfoTO> tktList = otQryEligPrds.getTicketInfoList();
	    for /* each */(OTTicketInfoTO otTicketInfo : /* in */tktList) {

	      Element ticketInfoStanza = qryEligblePrdsStanza.addElement("TicketInfo");

	      // Item
	      ticketInfoStanza.addElement("Item").addText(
	          otTicketInfo.getItem().toString());

	      // TicketSearchMode
	      Element tktSrchModeStanza = ticketInfoStanza
	          .addElement("TicketSearchMode");

	      OTTicketTO otTicket = otTicketInfo.getTicketSearchMode();
	      ArrayList<TicketIDType> otTicketIdTypeList = otTicket
	          .getTktIdentityTypes();
	      TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

	      switch (otTicketIdType) {

	      case MAGCODE:
	        tktSrchModeStanza.addElement("MagCode").addText(
	            otTicket.getMagTrack());
	        break;

	      case TDSSN:
	        Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");
	        tdssnElement.addElement("Site")
	            .setText(otTicket.getTdssnSite());
	        String station = otTicket.getTdssnStation();
	        if (station.length() > 2) {
	          String prefix = station.substring(0, 3);
	          if (prefix.compareTo("CAS") == 0) tdssnElement.addElement(
	              "Station").setText(otTicket.getTdssnStation());
	          else tdssnElement.addElement("Station").setText(
	              "CAS" + otTicket.getTdssnStation());
	        }
	        else tdssnElement.addElement("Station").setText(
	            "CAS" + otTicket.getTdssnStation());

	        GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
	        String tdssnDate = CustomDataTypeConverter
	            .printCalToDTIDateFormat(tdssnDateCal);
	        tdssnElement.addElement("Date").setText(tdssnDate);
	        tdssnElement.addElement("TicketId").setText(
	            otTicket.getTdssnTicketId());
	        break;

	      case TCOD:
	        tktSrchModeStanza.addElement("TCOD")
	            .addText(otTicket.getTCOD());
	        break;

	      case BARCODE:
	        tktSrchModeStanza.addElement("BarCode").addText(
	            otTicket.getBarCode());
	        break;

	      case EXTERNALTICKETCODE:
	        tktSrchModeStanza.addElement("ExternalTicketCode").addText(
	            otTicket.getExternalTicketCode());
	        break;
	      }

	    }


	}
	 /**
	   * When provided with the renew ticket XML section of a parsed XML, fills out the transfer object.
	   * 
	   * @param renewTicketNode
	   *            The parsed section of the XML containing the
	   * @return The Omni Ticket Renew Ticket Transfer Object.
	   * @throws DTIException
	   *             for any parsing errors or missing required fields.
	   */
	  @SuppressWarnings("unchecked")
	  public static OTQueryEligibleProductsTo getTO(Node queryEligPrdsNode) throws DTIException{

		  OTQueryEligibleProductsTo otQueryEligPrdsTO = new OTQueryEligibleProductsTo();

	    // Error
	    Node errorNode = queryEligPrdsNode.selectSingleNode("Error");
	      otQueryEligPrdsTO.setError(OTCommandXML.setOTErrorTO(errorNode));

	    // Ticketinfo
	    List<Node> ticketInfoNodeList = queryEligPrdsNode
	        .selectNodes("TicketInfo");
	    try{
	    	 setOTTicketInfoTOList(ticketInfoNodeList, otQueryEligPrdsTO.getTicketInfoList());
	    }catch(ParseException e){
	    	
	    }
	   

	   
	    return otQueryEligPrdsTO;
	  }
	  
	  /**
	   * Sets the ticket info list transfer objects based upon the parsed XML that's passed in.
	   * 
	   * @param ticketInfoNodeList
	   *            the parsed XML section containing the ticket info list.
	   * @param otTktList
	   *            The list of Omni Ticket Ticket Info Transfer Objects.
	   * @throws DTIException
	   *             for any missing required fields or other parsing errors.
	   */
	  @SuppressWarnings("unchecked")
	  private static void setOTTicketInfoTOList(List<Node> ticketInfoNodeList,
	      ArrayList<OTTicketInfoTO> otTktList) throws DTIException,ParseException {

	    List<Node> ticketList = (List<Node>) ticketInfoNodeList;

	    for /* each */(Node aNode : /* in */ticketList) {

	      OTTicketInfoTO otTktInfoTO = new OTTicketInfoTO();
	   // TktValidity?
	     
	   // Validity
	      Node validityNode = aNode.selectSingleNode("Validity");
	      if (validityNode != null) {

	        Node startDateNode = validityNode.selectSingleNode("StartDate");
	        Node endDateNode = validityNode.selectSingleNode("EndDate");

	        if ((startDateNode == null) || (endDateNode == null)) throw new DTIException(
	            OTManageReservationXML.class,
	            DTIErrorCode.TP_INTERFACE_FAILURE,
	            "Ticket provider returned XML with an incomplete Validity element.");

	        String startDateString = startDateNode.getText();
	        String endDateString = endDateNode.getText();

	        try {
	        	otTktInfoTO.setValidityStartDate(startDateString);
	        	otTktInfoTO.setValidityEndDate(endDateString);
	        }
	        catch (ParseException pe) {
	          throw new DTIException(
	              OTManageReservationXML.class,
	              DTIErrorCode.TP_INTERFACE_FAILURE,
	              "Ticket provider returned XML with an invalid Validity element: " + pe
	                  .toString());
	        }
	      }
	    
	      // Item
	      Node itemNode = aNode.selectSingleNode("Item");
	      if (itemNode != null)  {
	        String inText = itemNode.getText();
	        otTktInfoTO.setItem(new BigInteger(inText));
	      }

	      // TktStatus
	      Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
	      if (itemStatusNode != null) {
	    	  otTktInfoTO.setItemStatus(new Integer(itemStatusNode.getText()));
	    }
	     
	      // ResultStatus
	      Node ticketTypeNode = aNode.selectSingleNode("TicketType");
	      if (ticketTypeNode != null) {
	        String inText = ticketTypeNode.getText();
	        otTktInfoTO.setTicketType(new BigInteger(inText));	      }

	      // Price
	      Node priceNode = aNode.selectSingleNode("Price");
	      if (priceNode != null) {
	        String inText = priceNode.getText();
	        otTktInfoTO.setPrice(new BigDecimal(inText));
	      }

	      // Tax
	      Node taxNode = aNode.selectSingleNode("Tax");
	      if (taxNode != null) {
	        String inText = taxNode.getText();
	        otTktInfoTO.setTax(new BigDecimal(inText));
	      }

	      // Ticket
	      Node ticketNode = aNode.selectSingleNode("Ticket");
	      if (ticketNode != null) otTktInfoTO.setTicket(OTCommandXML
	          .setOTTicketTO(ticketNode));

	      // RemainingValue (ignored)

	     /* // VoidCode
	      Node voidCodeNode = aNode.selectSingleNode("ProdCode");
	      if (voidCodeNode != null) {
	        String inText = voidCodeNode.getText();
	        otTktInfoTO.setVoidCode(Integer.decode(inText));
	      }*/

	      // Validity
	      OTCommandXML.setOTTicketInfoValidity(aNode, otTktInfoTO);

	      // TicketFlag (ignored)
	      // Usages (ignored)

	     
	      // AlreadyUsed
	      Node alreadyUsedNode = aNode.selectSingleNode("AlreadyUsed");
	      if (alreadyUsedNode != null) {
	        String inText = alreadyUsedNode.getText();
	        otTktInfoTO.setAlreadyused(inText);
	      }

	      // DebitCardAuth (ignored)
	      // BiometricInfo (ignored)

	      // SeasonPassInfo (as of 2.16.1)
	      Node seasonPassInfoNode = aNode.selectSingleNode("SeasonPassInfo");
	      if (seasonPassInfoNode != null) {

	        Node demoDataNode = seasonPassInfoNode
	            .selectSingleNode("DemographicData");
	        if (demoDataNode != null) {

	          OTDemographicData otDemoData = new OTDemographicData();

	          List<Node> aFieldList = demoDataNode.selectNodes("Field");
	          for (/* each */Node aFieldNode : /* in */aFieldList) {

	            String fieldIdString = aFieldNode.selectSingleNode(
	                "FieldId").getText();
	            String fieldValueString = aFieldNode.selectSingleNode(
	                "FieldValue").getText();

	            Integer fieldId = null;

	            try {
	              fieldId = new Integer(fieldIdString);
	            }
	            catch (NumberFormatException nfe) {
	              logger.sendEvent(
	                  "Invalid FieldId sent by Iago.  Check standard err logs.",
	                  EventType.WARN, new OTQueryTicketXML());
	              nfe.printStackTrace();
	            }

	            OTFieldTO newOTField = new OTFieldTO(fieldId,
	                fieldValueString);

	            otDemoData.addOTField(newOTField);

	          }

	          otTktInfoTO.setSeasonPassDemo(otDemoData); // Done
	        }

	      }

	     
	      otTktList.add(otTktInfoTO);
	    }

	    return;
	  }


}
