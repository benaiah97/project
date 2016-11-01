package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class represents a Payment.
 * 
 * @author lewit019
 */
public class PaymentTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** Defines what possible variations of payments there are. */
	public enum PaymentType {
		CREDITCARD,
		VOUCHER,
		GIFTCARD,
		INSTALLMENT,
		UNSUPPORTED
	};

	/** The pay item number. */
	private BigInteger payItem;

	/** The type of payment. */
	private PaymentType payType = PaymentType.UNSUPPORTED;

	/** The credit card object. */
	private CreditCardTO creditCard;

	/** The voucher object. */
	private VoucherTO voucher;

	/** The gift card object. */
	private GiftCardTO giftCard;

	/** The installment object. */
	private InstallmentTO installment;

	/** The payment amount. */
	private BigDecimal payAmount;

	/**
	 * @return the creditCard
	 */
	public CreditCardTO getCreditCard() {
		return creditCard;
	}

	/**
	 * @return the giftCard
	 */
	public GiftCardTO getGiftCard() {
		return giftCard;
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
	 * @return the voucher
	 */
	public VoucherTO getVoucher() {
		return voucher;
	}

	/**
	 * @param creditCard
	 *            the creditCard to set
	 */
	public void setCreditCard(CreditCardTO creditCard) {
		this.payType = PaymentType.CREDITCARD;
		this.creditCard = creditCard;
	}

	/**
	 * @param giftCard
	 *            the giftCard to set
	 */
	public void setGiftCard(GiftCardTO giftCard) {
		this.payType = PaymentType.GIFTCARD;
		this.giftCard = giftCard;
	}

	/**
	 * @param voucher
	 *            the voucher to set
	 */
	public void setVoucher(VoucherTO voucher) {
		this.payType = PaymentType.VOUCHER;
		this.voucher = voucher;
	}

	/**
	 * @param installment
	 *            the installment to set
	 */
	public void setInstallment(InstallmentTO installment) {
		this.payType = PaymentType.INSTALLMENT;
		this.installment = installment;
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
	 * @return the installment
	 */
	public InstallmentTO getInstallment() {
		return installment;
	}

}
