package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;

/**
 * This class represents a transfer object containing information about an Omni Ticket Ticket.
 * 
 * @author lewit019
 */
public class OTTicketTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/**
	 * The Enum TicketIDType.
	 */
	public enum TicketIDType {
		/** The TDSSN. */
		TDSSN,
		/** The TCOD. */
		TCOD,
		/** The BARCODE. */
		BARCODE,
		/** The MAGCODE. */
		MAGCODE,
		/** The EXTERNALTICKETCODE. */
		EXTERNALTICKETCODE,
		/** The UNDEFINED. */
		UNDEFINED
	};

	// TktID Variations
	/** The mag track. */
	private String magTrack = "";

	/** The bar code. */
	private String barCode = "";

	/** The tdssn date. */
	private GregorianCalendar tdssnDate;

	/** The tdssn site. */
	private String tdssnSite = "";

	/** The tdssn station. */
	private String tdssnStation = "";

	/** The tdssn ticket id. */
	private String tdssnTicketId = "";

	/** The t cod. */
	private String tCOD = "";

	/** The external ticket code. */
	private String externalTicketCode = "";

	/** The tkt identity types. */
	private ArrayList<TicketIDType> tktIdentityTypes = new ArrayList<TicketIDType>();

	/**
	 * Gets the bar code.
	 * 
	 * @return the barCode
	 */
	public String getBarCode() {
		return barCode;
	}

	/**
	 * Gets the external ticket code.
	 * 
	 * @return the externalTicketCode
	 */
	public String getExternalTicketCode() {
		return externalTicketCode;
	}

	/**
	 * Gets the mag track.
	 * 
	 * @return the magTrack
	 */
	public String getMagTrack() {
		return magTrack;
	}

	/**
	 * Gets the tcod.
	 * 
	 * @return the tCOD
	 */
	public String getTCOD() {
		return tCOD;
	}

	/**
	 * Gets the tdssn date.
	 * 
	 * @return the tdssnDate
	 */
	public GregorianCalendar getTdssnDate() {
		return tdssnDate;
	}

	/**
	 * Gets the tdssn site.
	 * 
	 * @return the tdssnSite
	 */
	public String getTdssnSite() {
		return tdssnSite;
	}

	/**
	 * Gets the tdssn station.
	 * 
	 * @return the tdssnStation
	 */
	public String getTdssnStation() {
		return tdssnStation;
	}

	/**
	 * Gets the tdssn ticket id.
	 * 
	 * @return the tdssnTicketId
	 */
	public String getTdssnTicketId() {
		return tdssnTicketId;
	}

	/**
	 * Gets the tkt identity types.
	 * 
	 * @return the tktIdentityTypes
	 */
	public ArrayList<TicketIDType> getTktIdentityTypes() {
		return tktIdentityTypes;
	}

	/**
	 * Sets the bar code.
	 * 
	 * @param barCodeIn
	 *            the bar code in
	 */
	public void setBarCode(String barCodeIn) {
		this.barCode = barCodeIn;

		if (!tktIdentityTypes.contains(TicketIDType.BARCODE)) tktIdentityTypes
				.add(TicketIDType.BARCODE);
		if (barCodeIn == null) tktIdentityTypes.remove(TicketIDType.BARCODE);

		return;

	}

	/**
	 * Sets the external ticket code.
	 * 
	 * @param externalTicketCodeIn
	 *            the external ticket code in
	 */
	public void setExternalTicketCode(String externalTicketCodeIn) {
		this.externalTicketCode = externalTicketCodeIn;

		if (!tktIdentityTypes.contains(TicketIDType.EXTERNALTICKETCODE)) tktIdentityTypes
				.add(TicketIDType.EXTERNALTICKETCODE);
		if (externalTicketCodeIn == null) tktIdentityTypes
				.remove(TicketIDType.EXTERNALTICKETCODE);

		return;

	}

	/**
	 * Sets the mag track.
	 * 
	 * @param magTrackIn
	 *            the mag track in
	 */
	public void setMagTrack(String magTrackIn) {
		this.magTrack = magTrackIn;

		if (!tktIdentityTypes.contains(TicketIDType.MAGCODE)) tktIdentityTypes
				.add(TicketIDType.MAGCODE);
		if (magTrackIn == null) tktIdentityTypes.remove(TicketIDType.MAGCODE);

		return;

	}

	/**
	 * Sets the tcod.
	 * 
	 * @param tcodIn
	 *            the tcod in
	 */
	public void setTCOD(String tcodIn) {
		tCOD = tcodIn;

		if (!tktIdentityTypes.contains(TicketIDType.TCOD)) tktIdentityTypes
				.add(TicketIDType.TCOD);
		if (tcodIn == null) tktIdentityTypes.remove(TicketIDType.TCOD);

		return;

	}

	/**
	 * Sets the DSSN for a ticket using a string-based version of the date. Format used is yy-MM-dd (such as 09-01-31).
	 * 
	 * @param date
	 *            String version of the date
	 * @param site
	 *            the site
	 * @param station
	 *            the station
	 * @param number
	 *            the number
	 * 
	 * @throws ParseException
	 *             the parse exception
	 */
	public void setTDssn(String date, String site, String station, String number) throws ParseException {

		tdssnDate = OTCommandTO.convertOmniYYDate(date);
		setSsn(site, station, number);

		return;
	}

	/**
	 * Sets the DSSN for a ticket using the GregorianCalendar version of the date.
	 * 
	 * @param date
	 *            GregorianCalendar version of the date
	 * @param site
	 *            the site
	 * @param station
	 *            the station
	 * @param number
	 *            the number
	 */
	public void setTDssn(GregorianCalendar date, String site, String station,
			String number) {
		tdssnDate = date;
		setSsn(site, station, number);
	}

	/**
	 * Sets the site, station, and number of a ticket..
	 * 
	 * @param site
	 *            the site
	 * @param station
	 *            the station
	 * @param number
	 *            the number
	 */
	private void setSsn(String site, String station, String number) {
		tdssnSite = site;
		tdssnStation = station;
		this.tdssnTicketId = number;
		if (!tktIdentityTypes.contains(TicketIDType.TDSSN)) tktIdentityTypes
				.add(TicketIDType.TDSSN);
		if (site == null) tktIdentityTypes.add(TicketIDType.TDSSN);

		return;
	}

}
