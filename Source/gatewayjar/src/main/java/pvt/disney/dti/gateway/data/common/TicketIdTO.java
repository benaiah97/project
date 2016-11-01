package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;

/**
 * This class encapsulates the various manifestations of a ticket (DSSN, TktNid, mag track, etc.).
 * 
 * @author shiec014
 */
public class TicketIdTO implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5960911079153624841L;

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
	private ArrayList<TicketTO.TicketIdType> tktIdentityTypes = new ArrayList<TicketTO.TicketIdType>();

	// SETTERS

	/**
	 * Setter for the bar code.
	 * 
	 * @param string
	 *            Bar code value.
	 */
	public void setBarCode(String string) {
		barCode = string;

		if (!tktIdentityTypes.contains(TicketIdType.BARCODE_ID)) tktIdentityTypes
				.add(TicketIdType.BARCODE_ID);
		if (string == null) tktIdentityTypes.remove(TicketIdType.BARCODE_ID);
	}

	/**
	 * Setter for both mag tracks
	 */
	public void setMag(String magTrack1In, String magTrack2In) {
		magTrack1 = magTrack1In;
		magTrack2 = magTrack2In;
		if (!tktIdentityTypes.contains(TicketIdType.MAG_ID)) tktIdentityTypes
				.add(TicketIdType.MAG_ID);
		if (magTrack1In == null) tktIdentityTypes
				.remove(TicketIdType.BARCODE_ID);
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
		if (!tktIdentityTypes.contains(TicketIdType.TKTNID_ID)) tktIdentityTypes
				.add(TicketIdType.TKTNID_ID);
		if (string == null) tktIdentityTypes.remove(TicketIdType.TKTNID_ID);
	}

	/**
	 * Setter for the external representation of a ticket.
	 * 
	 * @param string
	 *            external representation of a ticket.
	 */
	public void setExternal(String string) {
		external = string;
		if (!tktIdentityTypes.contains(TicketIdType.EXTERNAL_ID)) tktIdentityTypes
				.add(TicketIdType.EXTERNAL_ID);
		if (string == null) tktIdentityTypes.remove(TicketIdType.EXTERNAL_ID);
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
	 * Sets the DSSN for a ticket using the GregorianCalendar version of the date.
	 * 
	 * @param date
	 *            GregorianCalendar version of the date
	 * @param site
	 * @param station
	 * @param number
	 */
	public void setDssn(GregorianCalendar date, String site, String station,
			String number) {
		dssnDate = date;
		setSsn(site, station, number);
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
		if (!tktIdentityTypes.contains(TicketIdType.DSSN_ID)) tktIdentityTypes
				.add(TicketIdType.DSSN_ID);
		if (site == null) tktIdentityTypes.add(TicketIdType.DSSN_ID);
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

}
