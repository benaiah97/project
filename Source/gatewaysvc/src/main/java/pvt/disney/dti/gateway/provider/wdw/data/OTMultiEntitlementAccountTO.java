package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMultiEntitlementAccountTO.CommandType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMultiEntitlementAccountTO.SubCommandType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;

/**
 * The transfer object class for Omni Ticket's "MultiEntitlementAccount". Currently built out to handle CreateEntitlement (TickerateEntitlement) and ManageMedia (AssociateMediaToAccount)
 * 
 * 2.16.1
 * 
 * @author biest001
 */

// Omni Ticket MultiEntitlementAccount Transfer Object
public class OTMultiEntitlementAccountTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8042774668686493413L;

	// Work Rules 1 to 8
	private ArrayList<String> tagsList = new ArrayList<String>();

	// Site Number
	private Integer siteNumber;

	// NGE Command Type
	private CommandType commandType;

	// NGE Sub-Command Type
	private SubCommandType subCommandType;

	// Generate Event to SFOV; XSD defaults this to false
	private Boolean generateEvent = false;

	// *********************************
	// ACCOUNT Search Filter

	// Existing Media Id
	private String existingMediaId = null;

	// Account Id (aka ATS Link Id)
	private String accountId = null;

	// Ticket Search Mode
	private OTTicketInfoTO ticketInfo = null;

	// *********************************
	// Ticket Info 0 to 250 (request and/or response)
	private ArrayList<OTTicketInfoTO> ticketInfoList = new ArrayList<OTTicketInfoTO>();

	// *********************************
	// Media Data 0 to 250 in the request/response
	private ArrayList<OTMediaDataTO> mediaData = new ArrayList<OTMediaDataTO>();

	// *********************************
	// External Reference
	private String accountExternalReference = null;
	private Byte accountExternalReferenceType;
	private Boolean overwriteExternalReference = false;

	// *********************************
	// Association Info
	private OTAssociationInfoTO associationInfo = null;

	// *********************************
	// Demographic Data 1 to 30
	private OTDemographicData demoData = null;

	// *********************************
	// Account level details in the response
	private Integer ticketsOnAccount = 0;
	private Integer mediaOnAccount = 0;

	// Transaction Error
	private OTErrorTO error;

	// ***************************************************************
	// Properties - Getters/Setters

	public ArrayList<String> getTagsList() {
		return tagsList;
	}

	public void setTagsList(ArrayList<String> tagsList) {
		this.tagsList = tagsList;
	}

	public Integer getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(Integer siteNumber) {
		this.siteNumber = siteNumber;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public Boolean getGenerateEvent() {
		return generateEvent;
	}

	public void setGenerateEvent(Boolean generateEvent) {
		this.generateEvent = generateEvent;
	}

	public ArrayList<OTTicketInfoTO> getTicketInfoList() {
		return ticketInfoList;
	}

	public void setTicketInfoList(ArrayList<OTTicketInfoTO> ticketInfoList) {
		this.ticketInfoList = ticketInfoList;
	}

	public String getExistingMediaId() {
		return existingMediaId;
	}

	public void setExistingMediaId(String existingMediaId) {
		this.existingMediaId = existingMediaId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public OTTicketInfoTO getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(OTTicketInfoTO ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public ArrayList<OTMediaDataTO> getMediaData() {
		return mediaData;
	}

	public void setMediaData(ArrayList<OTMediaDataTO> mediaData) {
		this.mediaData = mediaData;
	}

	public String getAccountExternalReference() {
		return accountExternalReference;
	}

	public void setAccountExternalReference(String accountExternalReference) {
		this.accountExternalReference = accountExternalReference;
	}

	public Byte getAccountExternalReferenceType() {
		return accountExternalReferenceType;
	}

	public void setAccountExternalReferenceType(
			Byte accountExternalReferenceType) {
		this.accountExternalReferenceType = accountExternalReferenceType;
	}

	public Boolean getOverwriteExternalReference() {
		return overwriteExternalReference;
	}

	public void setOverwriteExternalReference(Boolean overwriteExternalReference) {
		this.overwriteExternalReference = overwriteExternalReference;
	}

	public OTAssociationInfoTO getAssociationInfo() {
		return associationInfo;
	}

	public void setAssociationInfo(OTAssociationInfoTO associationInfo) {
		this.associationInfo = associationInfo;
	}

	public OTDemographicData getDemoData() {
		return demoData;
	}

	public void setDemoData(OTDemographicData demoData) {
		this.demoData = demoData;
	}

	public OTErrorTO getError() {
		return error;
	}

	public void setError(OTErrorTO error) {
		this.error = error;
	}

	public Integer getTicketsOnAccount() {
		return ticketsOnAccount;
	}

	public void setTicketsOnAccount(Integer ticketsOnAccount) {
		this.ticketsOnAccount = ticketsOnAccount;
	}

	public Integer getMediaOnAccount() {
		return mediaOnAccount;
	}

	public void setMediaOnAccount(Integer mediaOnAccount) {
		this.mediaOnAccount = mediaOnAccount;
	}

	public SubCommandType getSubCommandType() {
		return subCommandType;
	}

	public void setSubCommandType(SubCommandType subCommandType) {
		this.subCommandType = subCommandType;
	}

}
