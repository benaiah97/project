package pvt.disney.dti.gateway.dao.result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;

import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DBProductTO.GuestType;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;

// TODO: Auto-generated Javadoc
/**
 * This class is used by the DAO to process the result sets from Product Detail
 * Result queries.
 * 
 * @author lewit019
 * 
 */
public class ProductDetailResult implements ResultSetProcessor {

	/** List of database products. */
	private ArrayList<DBProductTO> results = new ArrayList<DBProductTO>();

	/** The Constant ADULTCODE. */
	public final static String ADULTCODE = "A";

	/** The Constant CHILDCODE. */
	public final static String CHILDCODE = "C";

	/**
	 * Constructor for ProductDetailResult.
	 */
	public ProductDetailResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 *
	 * @return the processed object
	 * @throws WrappedException
	 *            the wrapped exception
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return results;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 *
	 * @param rs
	 *           the rs
	 * @throws SQLException
	 *            the SQL exception
	 * @throws WrappedException
	 *            the wrapped exception
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {

		DBProductTO aProduct = new DBProductTO();

		// PDT_CODE
		aProduct.setPdtCode(rs.getString("PDT_CODE"));

		// PDTID
		aProduct.setPdtid(new BigInteger(rs.getString("PDTID")));

		// ELIG_GRPID
		String eligGrpidString = rs.getString("ELIG_GRPID");
		if (eligGrpidString != null)
			aProduct.setEligGrpid(new BigInteger(eligGrpidString));

		// PDT_DESC
		aProduct.setPdtDesc(rs.getString("PDT_DESC"));

		// TAX
		aProduct.setTax(rs.getBigDecimal("TAX"));

		// TAX1
		aProduct.setTax1(rs.getBigDecimal("TAX1"));

		// TAX2
		aProduct.setTax2(rs.getBigDecimal("TAX2"));

		// PRINTED_PRICE
		aProduct.setPrintedPrice(rs.getBigDecimal("PRINTED_PRICE"));

		// UNIT_PRICE
		aProduct.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));

		// ACTIVE_IND
		String activeIndString = rs.getString("ACTIVE_IND");
		if (activeIndString.compareToIgnoreCase("T") == 0)
			aProduct.setActive(true);
		else
			aProduct.setActive(false);

		// SOLD_OUT
		String soldOutString = rs.getString("SOLD_OUT");
		if (soldOutString == null)
			aProduct.setSoldOut(false);
		else
			aProduct.setSoldOut(true);

		// MISMATCH_IND
		String mismatchIndString = rs.getString("MISMATCH_IND");
		if (mismatchIndString.compareToIgnoreCase("T") == 0)
			aProduct.setPriceMismatchAllowed(true);
		else
			aProduct.setPriceMismatchAllowed(false);

		// MISMATCH_TOL_TYPE
		String mismatchTolTypeString = rs.getString("MISMATCH_TOL_TYPE");

		if (mismatchTolTypeString != null) {
			if (mismatchTolTypeString.compareTo("Amount") == 0)
				aProduct.setMisMatchTolType(DBProductTO.MismatchToleranceType.AMOUNT);
			if (mismatchTolTypeString.compareTo("Percent") == 0)
				aProduct.setMisMatchTolType(DBProductTO.MismatchToleranceType.PERCENT);
		} else {
			aProduct.setMisMatchTolType(DBProductTO.MismatchToleranceType.UNDEFINED);
		}

		// MISMATCH_TOL
		BigDecimal mismatchTol = rs.getBigDecimal("MISMATCH_TOL");
		if (mismatchTol != null)
			aProduct.setMisMatchTol(mismatchTol);

		// START_SALE_DATE
		Date startSaleDate = rs.getDate("START_SALE_DATE");
		if (startSaleDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar.getInstance();
			gCal.setTime(startSaleDate);
			aProduct.setStartSaleDate(gCal);
		}

		// END_SALE_DATE
		Date endSaleDate = rs.getDate("END_SALE_DATE");
		if (endSaleDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar.getInstance();
			gCal.setTime(endSaleDate);
			aProduct.setEndSaleDate(gCal);
		}

		// VALIDITY_DATES_IND
		String validityDatesInd = rs.getString("VALIDITY_DATES_IND");
		aProduct.setValidityDateInfoRequired(false);
		if (validityDatesInd != null) {
			if (validityDatesInd.compareToIgnoreCase("T") == 0)
				aProduct.setValidityDateInfoRequired(true);
		}

		// START_SALE_DATE
		Date startValidDate = rs.getDate("START_VALID_DATE");
		if (startValidDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar.getInstance();
			gCal.setTime(startValidDate);
			aProduct.setStartValidDate(gCal);
		}

		// END_SALE_DATE
		Date endValidDate = rs.getDate("END_VALID_DATE");
		if (endValidDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar.getInstance();
			gCal.setTime(endValidDate);
			aProduct.setEndValidDate(gCal);
		}

		// CONSUMABLE_IND
		String consumableIndString = rs.getString("CONSUMABLE_IND");
		if (consumableIndString.compareToIgnoreCase("T") == 0)
			aProduct.setConsumable(true);
		else
			aProduct.setConsumable(false);

		// DEMOGRAPHIC_IND (As of 2.9)
		String demographicIndString = rs.getString("DEMOGRAPHIC_IND");
		if (demographicIndString == null) {
			aProduct.setDemographicsInd(false);
		} else {
			if (demographicIndString.compareToIgnoreCase("T") == 0)
				aProduct.setDemographicsInd(true);
			else
				aProduct.setDemographicsInd(false);
		}

		// DAY_CLASS (As of 2.12)
		String dayClassString = rs.getString("DAY_CLASS");
		if (dayClassString != null) {
			aProduct.setDayClass(dayClassString);
		} else {
			aProduct.setDayClass("NONE");
		}

		// DAY_SUBCLASS (As of 2.16.1)
		String daySubclassString = rs.getString("DAY_SUBCLASS");
		if (daySubclassString != null) {
			aProduct.setDaySubclass(daySubclassString);
		} else {
			aProduct.setDaySubclass("NONE");
		}

		// STANDARD_RETAIL_PRICE (As of 2.12)
		BigDecimal standardRetailPrice = rs.getBigDecimal("STANDARD_RETAIL_PRICE");
		if (standardRetailPrice != null) {
			aProduct.setStandardRetailPrice(standardRetailPrice);
		}

		/*// RESIDENT_IND
		String residentIndString = rs.getString("RESIDENTID");
		if (residentIndString.compareToIgnoreCase("T") == 0) {
			aProduct.setResidentInd(true);
		} else {
			aProduct.setResidentInd(false);
		}*/

		/*// STANDARD_RETAIL_TAX
		BigDecimal standardRetailTax = rs.getBigDecimal("STANDARD_RETAIL_TAX");
		if (standardRetailTax != null) {
			aProduct.setStandardRetailTax(standardRetailTax);
		}

		// UPGRD_PATH_ID
		BigInteger upGrdPathId = new BigInteger(rs.getString("UPGRD_PATH_ID"));
		aProduct.setUpgrdPathId(upGrdPathId);*/

		// GUEST TYPE
		String guestType = rs.getString("GUEST_TYPE");
		if (guestType != null) {
			
			
			if (guestType.compareTo(ADULTCODE)==0) {
				aProduct.setGuestType(GuestType.ADULT);
			} else if (guestType.compareTo(CHILDCODE)==0) {
				aProduct.setGuestType(GuestType.CHILD);
			} else {
				aProduct.setGuestType(GuestType.ANY);
			}
		}
		
		int dayCount = rs.getInt("DAY_COUNT");
		aProduct.setDayCount(dayCount);
		
		String tktName=rs.getString("TKTSYSID");
		if(tktName !=null){
			aProduct.setMappedProviderTktName(tktName);
		}
		
		
		/* Adding the result set to result */
		results.add(aProduct);

		return;

	}

}
