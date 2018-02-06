package pl.coderslab.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;

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

	@Digits(fraction = 1, integer = 16)
	private BigDecimal creditCardNumber;

	@Transient
	private String lastFourDigits;

	private int cvv;

	private int expirationMonth;

	private int expirationYear;

	@NotBlank
	private String ownerName;

	@NotBlank
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
		this.setLastFourDigits();
	}

	public String getLastFourDigits() {
		return lastFourDigits;
	}

	public void setLastFourDigits(String lastFourDigits) {
		this.lastFourDigits = lastFourDigits;
	}
	
	public void setLastFourDigits() {
		String card = getCreditCardNumber().toString();
		this.lastFourDigits = card.substring((card.length()-4), card.length());
	}

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	public int getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public int getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(int expirationYear) {
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

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime * result + cvv;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastFourDigits == null) ? 0 : lastFourDigits.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		result = prime * result + ((ownerSurname == null) ? 0 : ownerSurname.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((wallet == null) ? 0 : wallet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreditCardInfo other = (CreditCardInfo) obj;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (cvv != other.cvv)
			return false;
		if (id != other.id)
			return false;
		if (lastFourDigits == null) {
			if (other.lastFourDigits != null)
				return false;
		} else if (!lastFourDigits.equals(other.lastFourDigits))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		if (ownerSurname == null) {
			if (other.ownerSurname != null)
				return false;
		} else if (!ownerSurname.equals(other.ownerSurname))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (wallet == null) {
			if (other.wallet != null)
				return false;
		} else if (!wallet.equals(other.wallet))
			return false;
		return true;
	}

	// simplified to string
	@Override
	public String toString() {
		return "CreditCardInfo [id=" + id + ", wallet=" + wallet.getId() + ", creditCardNumber=" + creditCardNumber
				+ ", lastFourDigits=" + lastFourDigits + ", cvv=" + cvv + ", ownerName=" + ownerName + ", ownerSurname="
				+ ownerSurname + ", type=" + type + "]";
	}

}
