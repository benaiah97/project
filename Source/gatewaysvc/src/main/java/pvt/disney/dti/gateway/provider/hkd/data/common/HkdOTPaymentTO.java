package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class represents an Omni Ticket Payment.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTPaymentTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** Credit Card Type */
	public final static int CREDITCARDTYPE = 2;

	/** Voucher Type */
	public final static int VOUCHERTYPE = 6;

	/** Installment Type (as of 2.15, JTL) */
	public final static int INSTALLMENTTYPE = 18;
	
	/** Unsupported Type */
	public final static int UNSUPPORTEDTYPE = 0;

	/** Defines what possible variations of payments there are. */
	public enum PaymentType {
		CREDITCARD,
		VOUCHER,
		INSTALLMENT,
		UNSUPPORTED
	};

	/** The pay item. */
	private BigInteger payItem;

	/** The pay type. */
	private PaymentType payType = PaymentType.UNSUPPORTED;

	/** The Omni Ticket Credit Card Transfer Object. */
	private HkdOTCreditCardTO creditCard;

	/** The Omni Ticket Voucher object. */
	private HkdOTVoucherTO voucher;

	/** The Omni Ticket Installment object. (As of 2.15, JTL) */
	private HkdOTInstallmentTO installment;

	/** The payment amount. */
	private BigDecimal payAmount;
	
	/**
	 * @return the creditCard
	 */
	public HkdOTCreditCardTO getCreditCard() {
		return creditCard;
	}

	/**
	 * @return the payAmount
	 */
	public BigDecimal getPayAmount() {
		return payAmount;
	}

	/**
	 * @return the payItem
	 */
	public BigInteger getPayItem() {
		return payItem;
	}

	/**
	 * @return the payType
	 */
	public PaymentType getPayType() {
		return payType;
	}

	/**
	 * @return integer value Omni expects for the paytype.
	 */
	public Integer getPayTypeInt() {

		if (payType == PaymentType.CREDITCARD) {
			return CREDITCARDTYPE;
		}

		if (payType == PaymentType.VOUCHER) {
			return VOUCHERTYPE;
		}

		if (payType == PaymentType.INSTALLMENT) {
			return INSTALLMENTTYPE;
		}

		return UNSUPPORTEDTYPE;
	}

	/**
	 * @return the voucher
	 */
	public HkdOTVoucherTO getVoucher() {
		return voucher;
	}

	/**
	 * @param creditCard
	 *            the creditCard to set
	 */
	public void setCreditCard(HkdOTCreditCardTO creditCard) {
		this.payType = PaymentType.CREDITCARD;
		this.creditCard = creditCard;
	}

	/**
	 * @param payAmount
	 *            the payAmount to set
	 */
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	/**
	 * @param payItem
	 *            the payItem to set
	 */
	public void setPayItem(BigInteger payItem) {
		this.payItem = payItem;
	}

	/**
	 * @param payType
	 *            the payType to set
	 */
	public void setPayType(PaymentType payType) {
		this.payType = payType;
	}

	/**
	 * NOTE: For use on response processing only! Not for use on request.
	 * 
	 * @param payTypeInt
	 *            the pay type expressed as an integer value from Omni Ticket.
	 */
	public void setPayTypeInt(int payTypeInt) {

		if (payTypeInt == CREDITCARDTYPE) {
			this.payType = PaymentType.CREDITCARD;
			return;
		}

		if (payTypeInt == VOUCHERTYPE) {
			this.payType = PaymentType.VOUCHER;
			return;
		}

		if (payTypeInt == INSTALLMENTTYPE) {
			this.payType = PaymentType.INSTALLMENT;
			return;
		}

		this.payType = PaymentType.UNSUPPORTED;

		return;

	}

	/**
	 * @param voucher
	 *            the voucher to set
	 */
	public void setVoucher(HkdOTVoucherTO voucher) {
		this.payType = PaymentType.VOUCHER;
		this.voucher = voucher;
	}

	/**
	 * @return the installment
	 */
	public HkdOTInstallmentTO getInstallment() {
		return installment;
	}

	/**
	 * @param installment
	 *            the installment to set
	 */
	public void setInstallment(HkdOTInstallmentTO installment) {
		payType = PaymentType.INSTALLMENT;
		this.installment = installment;
	}

}
