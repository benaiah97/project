package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.data.common.DBProductTO.GuestType;


/**
 * This class encapsulates the various manifestations of a ticket (DSSN, TktNid,
 * magnetic track, etc.).
 * 
 * @author lewit019
 */
public class TicketTO implements Serializable, Cloneable {

	/** Serial Version UID */
	public final static long serialVersionUID = 9129231995L;

	/** Default constructor. */
	public TicketTO() {
		return;
	}

	/**
	 * Overrides Object.clone() to provide a shallow copy of the TicketTO
	 * object.
	 * 
	 * @return TicketTO
	 */
	@SuppressWarnings("unchecked")
	public TicketTO clone() throws CloneNotSupportedException {

		TicketTO aClone = new TicketTO();

		aClone.tktItem = new BigInteger(this.tktItem.toString());

		aClone.prodCode = this.prodCode;

		if (this.fromProdCode != null) {
			aClone.fromProdCode = this.fromProdCode;
		}

		aClone.prodQty = new BigInteger(this.prodQty.toString());

		if (this.prodPrice != null) {
			aClone.prodPrice = new BigDecimal(this.prodPrice.toString());
		}

		if (this.upgrdPrice != null) {
			aClone.upgrdPrice = new BigDecimal(this.upgrdPrice.toString());
		}

		if (this.fromPrice != null) {
			aClone.fromPrice = new BigDecimal(this.fromPrice.toString());
		}

		if (this.tktSecurityLevel != null) {
			aClone.tktSecurityLevel = this.tktSecurityLevel;
		}

		if (this.tktMarket != null) {
			aClone.tktMarket = this.tktMarket;
		}

		if (this.tktShell != null) {
			aClone.tktShell = this.tktShell;
		}

		if (this.tktValidityValidStart != null) {
			aClone.tktValidityValidStart = this.tktValidityValidStart;
		}

		if (this.tktValidityValidEnd != null) {
			aClone.tktValidityValidEnd = this.tktValidityValidEnd;
		}

		if (this.tktNote != null) {
			aClone.tktNote = this.tktNote;
		}

		if (this.magTrack1 != null) {
			aClone.magTrack1 = this.magTrack1;
		}

		if (this.magTrack2 != null) {
			aClone.magTrack2 = this.magTrack2;
		}

		if (this.barCode != null) {
			aClone.barCode = this.barCode;
		}

		if (this.dssnDate != null) {
			aClone.dssnDate = this.dssnDate;
		}

		if (this.dssnSite != null) {
			aClone.dssnSite = this.dssnSite;
		}

		if (this.dssnStation != null) {
			aClone.dssnStation = this.dssnStation;
		}

		if (this.dssnNumber != null) {
			aClone.dssnNumber = this.dssnNumber;
		}

		if (this.tktNID != null) {
			aClone.tktNID = this.tktNID;
		}

		if (this.external != null) {
			aClone.external = this.external;
		}

		aClone.tktIdentityTypes = (ArrayList<TicketIdType>) this.tktIdentityTypes.clone();

		// Output or status fields
		if (this.tktPrice != null) {
			aClone.tktPrice = new BigDecimal(this.tktPrice.toString());
		}

		if (this.tktTax != null) {
			aClone.tktTax = new BigDecimal(this.tktTax.toString());
		}

		aClone.tktStatusList = (ArrayList<TktStatusTO>) this.tktStatusList.clone();

		if (this.ageGroup != null) {
			aClone.ageGroup = this.ageGroup;
		}

		if (this.mediaType != null) {
			aClone.mediaType = this.mediaType;
		}

		if (this.passType != null) {
			aClone.passType = this.passType;
		}

		if (this.passClass != null) {
			aClone.passClass = this.passClass;
		}

		if (this.resident != null) {
			aClone.resident = this.resident;
		}

		if (this.passRenew != null) {
			aClone.passRenew = this.passRenew;
		}

		if (this.lastDateUsed != null) {
			aClone.lastDateUsed = this.lastDateUsed;
		}

		if (this.timesUsed != null) {
			aClone.timesUsed = new BigInteger(this.timesUsed.toString());
		}

		if (this.replacedByPass != null) {
			aClone.replacedByPass = this.replacedByPass;
		}

		if (this.tktTran != null) {
			try {
				aClone.tktTran = this.tktTran.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		aClone.attributed = this.isAttributed();

		if (this.providerTicketType != null) {
			this.providerTicketType = new BigInteger(this.providerTicketType.toString());
		}

		aClone.tktStatusList = (ArrayList<TktStatusTO>) this.tktStatusList.clone();

		aClone.ticketDemoList = (ArrayList<DemographicsTO>) this.ticketDemoList.clone();

		aClone.ticketAssignmets = (ArrayList<TktAssignmentTO>) this.ticketAssignmets.clone();

		if (this.visualId != null) {
			aClone.visualId = this.visualId;
		}

		if (this.accountId != null) {
			aClone.accountId = this.accountId;
		}

		return aClone;
	}

	// Input fields
	/** Defines what possible variations of ticket identities there are. */
	public enum TicketIdType {
		DSSN_ID, MAG_ID, BARCODE_ID, TKTNID_ID, EXTERNAL_ID
	};
	
	/**
	 * The UpgradeEligibilityStatusType for providing the upgrade eligibility information.
	 */
	public enum UpgradeEligibilityStatusType {
		INELIGIBLE, NOPRODUCTS, ELIGIBLE
	};
	
	/**
	 * The  PayPlanEligibilityStatusType for providing the pay plan eligibilty Status.
	 */
	public enum PayPlanEligibilityStatusType {
		YES,NO
	};

	/** The ticket item number. */
	private BigInteger tktItem;

	/** The product code. */
	private String prodCode;

	/** The from product code. */
	private String fromProdCode; // 2.10

	/** The product quantity. */
	private BigInteger prodQty;

	/** The product price. */
	private BigDecimal prodPrice;

	/** The upgrade price */
	private BigDecimal upgrdPrice; // 2.10

	/** The from price */
	private BigDecimal fromPrice; // 2.10

	/** The ticket security level. */
	private String tktSecurityLevel;

	/** The ticket market. */
	private String tktMarket;

	/** The ticket shell. */
	private String tktShell;

	/** The ticket validity start date. */
	private GregorianCalendar tktValidityValidStart;

	/** The ticket validity end date. */
	private GregorianCalendar tktValidityValidEnd;

	/** The ticket note. */
	private String tktNote;

	// TktID Variations
	/** The 1st magnetic track. */
	private String magTrack1;

	/** The 2nd magnetic track. */
	private String magTrack2;

	/** The bar code. */
	private String barCode;

	/** The DSSN date. */
	private GregorianCalendar dssnDate;

	/** The DSSN site. */
	private String dssnSite;

	/** The DSSN station. */
	private String dssnStation;

	/** The DSSN number. */
	private String dssnNumber;

	/** The ticket numeric identifier. */
	private String tktNID;

	/** The external ticket identifier. */
	private String external;

	/** The list of ticket identity types in this object. */
	private ArrayList<TicketIdType> tktIdentityTypes = new ArrayList<TicketIdType>();

	// Output or status fields
	/** The ticket price. */
	private BigDecimal tktPrice;

	/** The ticket tax. */
	private BigDecimal tktTax;

	/** The list of ticket status objects. */
	private ArrayList<TktStatusTO> tktStatusList = new ArrayList<TktStatusTO>();

	/** The age group. */
	private String ageGroup;

	/** The media type. */
	private String mediaType;

	/** The pass type. */
	private String passType;

	/** The pass class. */
	private String passClass;

	/** The resident value. */
	private String resident;

	/** The pass renew attribute value. */
	private String passRenew;

	/** The pass name as returned by the provider system. */
	private String passName;

	/** The last date this pass was used. */
	private GregorianCalendar lastDateUsed;

	/** The number of times this pass has been used. */
	private BigInteger timesUsed;

	/** If this pass has been replaced, then what pass replaced it. */
	private String replacedByPass;

	/** The upgrade eligibility status. */
	private UpgradeEligibilityStatusType upgradeEligibilityStatus=UpgradeEligibilityStatusType.ELIGIBLE ; // RASTA006 2.17.3
	
	/** get the sale Type for the current ticket */
	private String saleType;

	/** get the plu info */
	private String plu;

	/** get price information */
	private BigDecimal price;

	/** get upgraded information */
	private BigDecimal upgradePrice;

	/** get upgraded information */
	private ArrayList<EligibleProductsTO> eligibleProducts = new ArrayList<EligibleProductsTO>();

	/** get the guest type info */
	private GuestType guestType;
	
	/** The pay plan eligibility status. */
	private PayPlanEligibilityStatusType payPlanEligibilityStatus = PayPlanEligibilityStatusType.YES;

	/**
	 * @return the showGroup
	 */
	public String getShowGroup() {
		return showGroup;
	}

	/**
	 * @param showGroup
	 *            the showGroup to set
	 */
	public void setShowGroup(String showGroup) {
		this.showGroup = showGroup;
	}

	/** Show group. As of 2.17.1, JTL */
	private String showGroup;

	/** The ticket transaction object. */
	private TicketTransactionTO tktTran;

	/** Boolean indicating if the object has attributes. */
	private boolean attributed = false;

	/** The provider ticket type. */
	private BigInteger providerTicketType;

	/** Ticket level demographics. */
	private ArrayList<DemographicsTO> ticketDemoList = new ArrayList<DemographicsTO>();

	/** list of ticket assignments. */
	private ArrayList<TktAssignmentTO> ticketAssignmets = new ArrayList<TktAssignmentTO>();

	/** Visual Id of the entitlement (As of 2.15 JTL) */
	private String visualId = null;

	/**
	 * Account Id belonging to the entitlement (As of 2.16.1 BIEST001;
	 * 05/09/2016)
	 */
	private String accountId = null;

	/** Existing Ticket (as of 2.16.1, JTL) */
	private TicketTO existingTktID = null;

	/**
	 * Used to represent the pair of repeating status items and status values
	 * for tickets.
	 * 
	 * @author lewit019
	 */
	public class TktStatusTO implements Serializable {

		public final static long serialVersionUID = 9129231995L;

		protected String statusItem;
		protected String statusValue;

		/**
		 * @return the statusItem
		 */
		public String getStatusItem() {
			return statusItem;
		}

		/**
		 * @param statusItem
		 *            the statusItem to set
		 */
		public void setStatusItem(String statusItem) {
			this.statusItem = statusItem;
		}

		/**
		 * @return the statusValue
		 */
		public String getStatusValue() {
			return statusValue;
		}

		/**
		 * @param statusValue
		 *            the statusValue to set
		 */
		public void setStatusValue(String statusValue) {
			this.statusValue = statusValue;
		}

	}

	/**
	 * WDW ticket seller can assign each ticket to be associated with and
	 * entitlement account. This is an account and the number of these tickets
	 * to associate with it.
	 * 
	 * @author SHIEC014
	 * 
	 */
	public class TktAssignmentTO implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 2155628821737285305L;

		private BigInteger AccountItem;
		private BigInteger ProdQty;

		/**
		 * @param accountItem
		 *            the accountItem to set
		 */
		public void setAccountItem(BigInteger accountItem) {
			AccountItem = accountItem;
		}

		/**
		 * @return the accountItem
		 */
		public BigInteger getAccountItem() {
			return AccountItem;
		}

		/**
		 * @param prodQty
		 *            the prodQty to set
		 */
		public void setProdQty(BigInteger prodQty) {
			ProdQty = prodQty;
		}

		/**
		 * @return the prodQty
		 */
		public BigInteger getProdQty() {
			return ProdQty;
		}

	}

	// SETTERS

	/**
	 * Adds a status to the list.
	 */
	public void addTicketStatus(TktStatusTO newStatus) {
		tktStatusList.add(newStatus);
	}

	/**
	 * Setter for the bar code.
	 * 
	 * @param string
	 *            Bar code value.
	 */
	public void setBarCode(String string) {
		barCode = string;

		if (!tktIdentityTypes.contains(TicketIdType.BARCODE_ID))
			tktIdentityTypes.add(TicketIdType.BARCODE_ID);
		if (string == null)
			tktIdentityTypes.remove(TicketIdType.BARCODE_ID);
	}

	/**
	 * Setter for both mag tracks
	 */
	public void setMag(String magTrack1In, String magTrack2In) {
		magTrack1 = magTrack1In;
		magTrack2 = magTrack2In;
		if (!tktIdentityTypes.contains(TicketIdType.MAG_ID))
			tktIdentityTypes.add(TicketIdType.MAG_ID);
		if (magTrack1In == null)
			tktIdentityTypes.remove(TicketIdType.BARCODE_ID);
	}

	/**
	 * Setter for the 2nd mag track.
	 * 
	 * @param string
	 *            2nd mag track.
	 */
	public void setMag(String magTrack1In) {
		setMag(magTrack1In, null);
	}

	/**
	 * Setter for the tkt nid.
	 * 
	 * @param string
	 *            tkt nid.
	 */
	public void setTktNID(String string) {
		tktNID = string;
		if (!tktIdentityTypes.contains(TicketIdType.TKTNID_ID))
			tktIdentityTypes.add(TicketIdType.TKTNID_ID);
		if (string == null)
			tktIdentityTypes.remove(TicketIdType.TKTNID_ID);
	}

	/**
	 * Setter for the external representation of a ticket.
	 * 
	 * @param string
	 *            external representation of a ticket.
	 */
	public void setExternal(String string) {
		external = string;
		if (!tktIdentityTypes.contains(TicketIdType.EXTERNAL_ID))
			tktIdentityTypes.add(TicketIdType.EXTERNAL_ID);
		if (string == null)
			tktIdentityTypes.remove(TicketIdType.EXTERNAL_ID);
	}

	/**
	 * Sets the DSSN for a ticket using a string-based version of the date.
	 * 
	 * @param date
	 *            String version of the date
	 * @param site
	 * @param station
	 * @param number
	 * @throws ParseException
	 */
	public void setDssn(String date, String site, String station, String number) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date tempDate = sdf.parse(date);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(tempDate);

		dssnDate = gc;
		setSsn(site, station, number);
	}

	/**
	 * Sets the DSSN for a ticket using the GregorianCalendar version of the
	 * date.
	 * 
	 * @param date
	 *            GregorianCalendar version of the date
	 * @param site
	 * @param station
	 * @param number
	 */
	public void setDssn(GregorianCalendar date, String site, String station, String number) {
		dssnDate = date;
		setSsn(site, station, number);
	}

	/**
	 * @param tktItem
	 *            the tktItem to set
	 */
	public void setTktItem(BigInteger tktItem) {
		this.tktItem = tktItem;
	}

	/**
	 * Sets the site, station, and number of a ticket..
	 * 
	 * @param site
	 * @param station
	 * @param number
	 */
	private void setSsn(String site, String station, String number) {
		dssnSite = site;
		dssnStation = station;
		dssnNumber = number;
		if (!tktIdentityTypes.contains(TicketIdType.DSSN_ID)) {
			tktIdentityTypes.add(TicketIdType.DSSN_ID);
		}
		if (site == null) {
			tktIdentityTypes.add(TicketIdType.DSSN_ID);
		}
	}

	/**
	 * @param ageGroup
	 *            the ageGroup to set
	 */
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
		this.attributed = true;
	}

	/**
	 * @param mediaType
	 *            the mediaType to set
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
		this.attributed = true;
	}

	/**
	 * @param passClass
	 *            the passClass to set
	 */
	public void setPassClass(String passClass) {
		this.passClass = passClass;
		this.attributed = true;
	}

	/**
	 * @param passType
	 *            the passType to set
	 */
	public void setPassType(String passType) {
		this.passType = passType;
		this.attributed = true;
	}

	/**
	 * @param tktPrice
	 *            the tktPrice to set
	 */
	public void setTktPrice(BigDecimal tktPrice) {
		this.tktPrice = tktPrice;
	}

	/**
	 * @param tktTax
	 *            the tktTax to set
	 */
	public void setTktTax(BigDecimal tktTax) {
		this.tktTax = tktTax;
	}

	/**
	 * @param tktValidityValidStart
	 *            the tktValidityValidStart to set
	 */
	public void setTktValidityValidStart(GregorianCalendar tktValidityValidStart) {
		this.tktValidityValidStart = tktValidityValidStart;
	}

	/**
	 * @param tktValidityValidEnd
	 *            the tktValidityValidEnd to set
	 */
	public void setTktValidityValidEnd(GregorianCalendar tktValidityValidEnd) {
		this.tktValidityValidEnd = tktValidityValidEnd;
	}

	/**
	 * @param tktTran
	 *            the tktTran to set
	 */
	public void setTktTran(TicketTransactionTO tktTranIn) {
		this.tktTran = tktTranIn;
	}

	/**
	 * @param resident
	 *            the resident to set
	 */
	public void setResident(String resident) {
		this.resident = resident;
		this.attributed = true;
	}

	/**
	 * @return the tktTran
	 */
	public TicketTransactionTO getTktTran() {
		return tktTran;
	}

	/**
	 * Allows conversion of Gregorian Calendar to the DSSN Date format (string)
	 * 
	 * @param dateIn
	 * @return DTI Formatted Date
	 */
	public static String getDssnDateString(GregorianCalendar dateIn) {

		String dtiFormattedDate;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dtiFormattedDate = sdf.format(dateIn.getTime());

		return dtiFormattedDate;

	}

	/**
	 * Getter for the DSSN date.
	 * 
	 * @return DSSN Date as string.
	 */
	public String getDssnDateString() {
		String dtiFormattedDate;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dtiFormattedDate = sdf.format(dssnDate.getTime());

		return dtiFormattedDate;
	}

	/**
	 * Getter for the DSSN number.
	 * 
	 * @return DSNN Number as string.
	 */
	public String getDssnNumber() {
		return dssnNumber;
	}

	/**
	 * Getter for DSSN site.
	 * 
	 * @return DSSN site as string.
	 */
	public String getDssnSite() {
		return dssnSite;
	}

	/**
	 * Getter for the DSSN station.
	 * 
	 * @return DSSN station as a string.
	 */
	public String getDssnStation() {
		return dssnStation;
	}

	/**
	 * Getter for the external representation of a ticket.
	 * 
	 * @return The external representation of a ticket as a String.
	 */
	public String getExternal() {
		return external;
	}

	/**
	 * Getter for the 1st mag track value.
	 * 
	 * @return The 1st mag track value as a string.
	 */
	public String getMagTrack1() {
		return magTrack1;
	}

	/**
	 * Getter for the 2nd mag track value.
	 * 
	 * @return The 2nd mag track value as a string.
	 */
	public String getMagTrack2() {
		return magTrack2;
	}

	/**
	 * Getter for the tkt nid.
	 * 
	 * @return The tkt nid as a string.
	 */
	public String getTktNID() {
		return tktNID;
	}

	/**
	 * Gets the Dssn Date
	 * 
	 * @return Date type of Dssn date
	 */
	public GregorianCalendar getDssnDate() {
		return dssnDate;
	}

	/**
	 * Getter for Bar Code.
	 * 
	 * @return Bar code string.
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<TicketIdType> getTicketTypes() {
		return tktIdentityTypes;
	}

	/**
	 * @return the tktItem
	 */
	public BigInteger getTktItem() {
		return tktItem;
	}

	/**
	 * @return the ageGroup
	 */
	public String getAgeGroup() {
		return ageGroup;
	}

	/**
	 * @return the mediaType
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * @return the passClass
	 */
	public String getPassClass() {
		return passClass;
	}

	/**
	 * @return the passType
	 */
	public String getPassType() {
		return passType;
	}

	/**
	 * @return the tktPrice
	 */
	public BigDecimal getTktPrice() {
		return tktPrice;
	}

	/**
	 * @return the tktTax
	 */
	public BigDecimal getTktTax() {
		return tktTax;
	}

	/**
	 * @return the tktValidityValidEnd
	 */
	public GregorianCalendar getTktValidityValidEnd() {
		return tktValidityValidEnd;
	}

	/**
	 * @return the tktValidityValidStart
	 */
	public GregorianCalendar getTktValidityValidStart() {
		return tktValidityValidStart;
	}

	/**
	 * @return the tktStatusList
	 */
	public ArrayList<TktStatusTO> getTktStatusList() {
		return tktStatusList;
	}

	/**
	 * @return the resident
	 */
	public String getResident() {
		return resident;
	}

	/**
	 * @return the attributed
	 */
	public boolean isAttributed() {
		return attributed;
	}

	/**
	 * @return the prodCode
	 */
	public String getProdCode() {
		return prodCode;
	}

	/**
	 * @return the prodPrice
	 */
	public BigDecimal getProdPrice() {
		return prodPrice;
	}

	/**
	 * @return the prodQty
	 */
	public BigInteger getProdQty() {
		return prodQty;
	}

	/**
	 * @return the tktNote
	 */
	public String getTktNote() {
		return tktNote;
	}

	/**
	 * @return the tktShell
	 */
	public String getTktShell() {
		return tktShell;
	}

	/**
	 * @param prodCode
	 *            the prodCode to set
	 */
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	/**
	 * @param prodPrice
	 *            the prodPrice to set
	 */
	public void setProdPrice(BigDecimal prodPrice) {
		this.prodPrice = prodPrice;
	}

	/**
	 * @param prodQty
	 *            the prodQty to set
	 */
	public void setProdQty(BigInteger prodQty) {
		this.prodQty = prodQty;
	}

	/**
	 * @param tktNote
	 *            the tktNote to set
	 */
	public void setTktNote(String tktNote) {
		this.tktNote = tktNote;
	}

	/**
	 * @param tktShell
	 *            the tktShell to set
	 */
	public void setTktShell(String tktShell) {
		this.tktShell = tktShell;
	}

	/**
	 * @return the tktMarket
	 */
	public String getTktMarket() {
		return tktMarket;
	}

	/**
	 * @return the tktSecurityLevel
	 */
	public String getTktSecurityLevel() {
		return tktSecurityLevel;
	}

	/**
	 * @param tktMarket
	 *            the tktMarket to set
	 */
	public void setTktMarket(String tktMarket) {
		this.tktMarket = tktMarket;
	}

	/**
	 * @param tktSecurityLevel
	 *            the tktSecurityLevel to set
	 */
	public void setTktSecurityLevel(String tktSecurityLevel) {
		this.tktSecurityLevel = tktSecurityLevel;
	}

	/**
	 * @return the passRenew
	 */
	public String getPassRenew() {
		return passRenew;
	}

	/**
	 * @param passRenew
	 *            the passRenew to set
	 */
	public void setPassRenew(String passRenew) {
		this.passRenew = passRenew;
	}

	/**
	 * 
	 * @return the provider ticket type
	 */
	public BigInteger getProviderTicketType() {
		return providerTicketType;
	}

	/**
	 * Sets the provider ticket type.
	 * 
	 * @param providerTicketType
	 *            the provider ticket type to set.
	 */
	public void setProviderTicketType(BigInteger providerTicketType) {
		this.providerTicketType = providerTicketType;
	}

	public GregorianCalendar getLastDateUsed() {
		return lastDateUsed;
	}

	public void setLastDateUsed(GregorianCalendar lastDateUsed) {
		this.lastDateUsed = lastDateUsed;
	}

	public String getPassName() {
		return passName;
	}

	public void setPassName(String passName) {
		this.passName = passName;
	}

	public String getReplacedByPass() {
		return replacedByPass;
	}

	public void setReplacedByPass(String replacedByPass) {
		this.replacedByPass = replacedByPass;
	}

	public BigInteger getTimesUsed() {
		return timesUsed;
	}

	public void setTimesUsed(BigInteger timesUsed) {
		this.timesUsed = timesUsed;
	}

	/**
	 * @param ticketAssignmets
	 *            the ticketAssignmets to set
	 */
	public void setTicketAssignmets(ArrayList<TktAssignmentTO> ticketAssignmets) {
		this.ticketAssignmets = ticketAssignmets;
	}

	/**
	 * @return the ticketAssignmets
	 */
	public ArrayList<TktAssignmentTO> getTicketAssignmets() {
		return ticketAssignmets;
	}

	/**
	 * @return the ticketDemoList
	 */
	public ArrayList<DemographicsTO> getTicketDemoList() {
		return ticketDemoList;
	}

	/**
	 * @param ticketDemoList
	 *            the ticketDemoList to set
	 */
	public void setTicketDemoList(ArrayList<DemographicsTO> ticketDemoList) {
		this.ticketDemoList = ticketDemoList;
	}

	/**
	 * Adds a ticket demographic to the list.
	 */
	public void addTicketDemographic(DemographicsTO newDemo) {
		ticketDemoList.add(newDemo);
	}

	/**
	 * @param fromProdCode
	 *            the from product code to set
	 */
	public void setFromProdCode(String fromProdCode) {
		this.fromProdCode = fromProdCode;
	}

	/**
	 * @return the from product code
	 */
	public String getFromProdCode() {
		return fromProdCode;
	}

	/**
	 * @param upgrdPrice
	 *            the upgrade price to set
	 */
	public void setUpgrdPrice(BigDecimal upgrdPrice) {
		this.upgrdPrice = upgrdPrice;
	}

	/**
	 * @return the upgrade price
	 */
	public BigDecimal getUpgrdPrice() {
		return upgrdPrice;
	}

	/**
	 * @param fromPrice
	 *            the from price to set
	 */
	public void setFromPrice(BigDecimal fromPrice) {
		this.fromPrice = fromPrice;
	}

	/**
	 * @return the from price
	 */
	public BigDecimal getFromPrice() {
		return fromPrice;
	}

	/**
	 * @return the visualId
	 */
	public String getVisualId() {
		return visualId;
	}

	/**
	 * @param visualId
	 *            the visualId to set
	 */
	public void setVisualId(String visualId) {
		this.visualId = visualId;
	}

	/**
	 * 
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * 
	 * @param accountId
	 *            - the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the existingTktID
	 */
	public TicketTO getExistingTktID() {
		return existingTktID;
	}

	/**
	 * @param existingTktID
	 *            the existingTktID to set
	 */
	public void setExistingTktID(TicketTO existingTktID) {
		this.existingTktID = existingTktID;
	}

	/**
	 * @return the resultType
	 */
	public UpgradeEligibilityStatusType getUpgradeEligibilityStatus() {
		return upgradeEligibilityStatus;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public void setUpgradeEligibilityStatus(UpgradeEligibilityStatusType resultType) {
		this.upgradeEligibilityStatus = resultType;
	}

	/**
	 * @return the saleType
	 */
	public String getSaleType() {
		return saleType;
	}

	/**
	 * @param saleType
	 *            the saleType to set
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	/**
	 * @return the plu
	 */
	public String getPlu() {
		return plu;
	}

	/**
	 * @param plu
	 *            the plu to set
	 */
	public void setPlu(String plu) {
		this.plu = plu;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the upgradePrice
	 */
	public BigDecimal getUpgradePrice() {
		return upgradePrice;
	}

	/**
	 * @param upgradePrice
	 *            the upgradePrice to set
	 */
	public void setUpgradePrice(BigDecimal upgradePrice) {
		this.upgradePrice = upgradePrice;
	}

	/**
	 * @return the eligibleProducts
	 */
	public ArrayList<EligibleProductsTO> getEligibleProducts() {
		return eligibleProducts;
	}

	/**
	 * @param eligibleProducts
	 *            the eligibleProducts to set
	 */
	public void setEligibleProducts(ArrayList<EligibleProductsTO> eligibleProducts) {
		this.eligibleProducts = eligibleProducts;
	}

	/**
	 * Adds products to the list.
	 */
	public void addEligibleProducts(EligibleProductsTO eligibleProductsTO) {
		eligibleProducts.add(eligibleProductsTO);
	}

	/**
	 * @return the guestType
	 */
	public GuestType getGuestType() {
		return guestType;
	}

	/**
	 * @param guestType
	 *            the guestType to set
	 */
	public void setGuestType(GuestType guestType) {
		this.guestType = guestType;
	}

	/**
	 * @return the payPlanEligibilityStatus
	 */
	public PayPlanEligibilityStatusType getPayPlanEligibilityStatus() {
		return payPlanEligibilityStatus;
	}

	/**
	 * @param payPlanEligibilityStatus the payPlanEligibilityStatus to set
	 */
	public void setPayPlanEligibilityStatus(PayPlanEligibilityStatusType payPlanEligibilityStatus) {
		this.payPlanEligibilityStatus = payPlanEligibilityStatus;
	}
	

}