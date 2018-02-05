package pl.coderslab.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "operations")
public class Operation {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "operation_id")
	private long id;

	private LocalDateTime timeOfOperation;

	private BigDecimal amount;

	private String creditCardLastForDigits;

	@ManyToOne
	@JoinColumn
	private Wallet wallet;

	private String optionalComment;

	// Constructor
	public Operation() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getTimeOfOperation() {
		return timeOfOperation;
	}

	public void setTimeOfOperation(LocalDateTime timeOfOperation) {
		this.timeOfOperation = timeOfOperation;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCreditCardLastForDigits() {
		return creditCardLastForDigits;
	}

	public void setCreditCardLastForDigits(String creditCardLastForDigits) {
		this.creditCardLastForDigits = creditCardLastForDigits;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public String getOptionalComment() {
		return optionalComment;
	}

	public void setOptionalComment(String optionalComment) {
		this.optionalComment = optionalComment;
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((creditCardLastForDigits == null) ? 0 : creditCardLastForDigits.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((optionalComment == null) ? 0 : optionalComment.hashCode());
		result = prime * result + ((timeOfOperation == null) ? 0 : timeOfOperation.hashCode());
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
		Operation other = (Operation) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (creditCardLastForDigits == null) {
			if (other.creditCardLastForDigits != null)
				return false;
		} else if (!creditCardLastForDigits.equals(other.creditCardLastForDigits))
			return false;
		if (id != other.id)
			return false;
		if (optionalComment == null) {
			if (other.optionalComment != null)
				return false;
		} else if (!optionalComment.equals(other.optionalComment))
			return false;
		if (timeOfOperation == null) {
			if (other.timeOfOperation != null)
				return false;
		} else if (!timeOfOperation.equals(other.timeOfOperation))
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
		return "Operation [id=" + id + ", timeOfOperation=" + timeOfOperation + ", amount=" + amount
				+ ", creditCardLastForDigits=" + creditCardLastForDigits + ", wallet=" + wallet.getId()
				+ ", optionalComment=" + optionalComment + "]";
	}

}
