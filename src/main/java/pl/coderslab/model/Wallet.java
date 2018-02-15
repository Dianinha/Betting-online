package pl.coderslab.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Class that represents virtual {@link User} wallet. Has relations to
 * {@link User} as its owner, stored {@link CreditCardInfo} and its own history
 * of {@link Operation}
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "wallets")
public class Wallet {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wallet_id")
	private long id;

	private BigDecimal amount;

	@OneToMany(mappedBy = "wallet")
	private Set<CreditCardInfo> creditCards;

	@OneToOne
	private User user;

	@OneToMany(mappedBy = "wallet")
	private List<Operation> history;

	// Constructor
	public Wallet() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Set<CreditCardInfo> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Set<CreditCardInfo> creditCards) {
		this.creditCards = creditCards;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Operation> getHistory() {
		return history;
	}

	public void setHistory(List<Operation> history) {
		this.history = history;
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((creditCards == null) ? 0 : creditCards.hashCode());
		result = prime * result + ((history == null) ? 0 : history.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Wallet other = (Wallet) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (creditCards == null) {
			if (other.creditCards != null)
				return false;
		} else if (!creditCards.equals(other.creditCards))
			return false;
		if (history == null) {
			if (other.history != null)
				return false;
		} else if (!history.equals(other.history))
			return false;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	// simplified to string
	@Override
	public String toString() {
		return "Wallet [id=" + id + ", amount=" + amount + ", user=" + user.getId() + "]";
	}

}
