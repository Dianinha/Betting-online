package pl.coderslab.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class stores {@link User} credit card information. In real life this
 * data should be secured or send to some payment service (for example PayPal). 
 * By any reason CVV should not be stored in database! This is just for make up data.
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li> connecting it to some real payment methods </li>
 * <li> hash and secure this information in database </li>
 * <li> credit card number should be corected now there is weird error message caused by BigDecimal if number is null</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "creditCardInfos")
public class CreditCardInfo {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "creditCardInfo_id")
	private long id;

	@ManyToOne
	@JoinColumn
	private Wallet wallet;

	@Digits(fraction = 0, integer = 16, message = "Credit card number must contain 16 digits.")
	@DecimalMin(value = "1000000000000000", inclusive = true, message = "Credit card number must contain 16 digits.")
	@NotNull(message = "Credit card number cannot be empty.")
	private BigDecimal creditCardNumber;

	private String lastFourDigits;

	@Min(value = 001, message = "CVV must contains 3 digits. See information on the right panel")
	@Max(value = 999, message = "CVV must contains 3 digits. See information on the right panel")
	@NotNull(message = "CVV cannot be empty.")
	private Integer cvv;

	@Min(value = 1, message = "Please pick value from 1-12")
	@Max(value = 12, message = "Please pick value from 1-12")
	@NotNull(message = "Month cannot be empty.")
	private Integer expirationMonth;

	@Min(value = 18, message = "Please pick valid credit card")
	@Max(value = 99, message = "Please put just 2 last digits")
	@NotNull(message = "Year cannot be empty.")
	private Integer expirationYear;

	@NotBlank(message = "Name cannot be empty.")
	private String ownerName;

	@NotBlank(message = "Surname cannot be empty.")
	private String ownerSurname;

	// Visa or Mastercard
	private String type;

	// Constructor
	public CreditCardInfo() {
		super();
	}

	// getters and setters

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public BigDecimal getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(BigDecimal creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
		setLastFourDigits();
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits() {
		String card = creditCardNumber.toString();
		this.lastFourDigits = card.substring(card.length() - 4, card.length());
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public Integer getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public Integer getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerSurname() {
		return ownerSurname;
	}

	public void setOwnerSurname(String ownerSurname) {
		this.ownerSurname = ownerSurname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// simplified to string
	@Override
	public String toString() {
		return "CreditCardInfo [id=" + id + ", wallet=" + wallet.getId() + ", creditCardNumber=" + creditCardNumber
				+ ", lastFourDigits=" + lastFourDigits + ", cvv=" + cvv + ", ownerName=" + ownerName + ", ownerSurname="
				+ ownerSurname + ", type=" + type + "]";
	}

}
