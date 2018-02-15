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
/**
 * This class coresponds to bet that is placed by User.
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "bets")
public class SingleBet {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bet_id")
	private long id;

	@ManyToOne
	@JoinColumn
	private User user;

	@ManyToOne
	@JoinColumn
	private GameToBet game;
/**
 * Possible values: draw, home or away
 */
	private String betOn;

	private BigDecimal amount;

	// how much user wins for 1 pln
	private BigDecimal rate;

	private BetStatus status;
	
	private boolean isItGroupBet;
	
	private boolean isItMultiBet;
	
	private String betResult;

	// Constructor
	public SingleBet() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public GameToBet getGame() {
		return game;
	}

	public void setGame(GameToBet game) {
		this.game = game;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public boolean isItGroupBet() {
		return isItGroupBet;
	}

	public void setItGroupBet(boolean isItGroupBet) {
		this.isItGroupBet = isItGroupBet;
	}
	

	public String getBetResult() {
		return betResult;
	}

	public void setBetResult(String betResult) {
		this.betResult = betResult;
	}

	public boolean isItMultiBet() {
		return isItMultiBet;
	}

	public void setItMultiBet(boolean isItMultiBet) {
		this.isItMultiBet = isItMultiBet;
	}

	public String getBetOn() {
		return betOn;
	}

	public void setBetOn(String betOn) {
		this.betOn = betOn;
	}

	public BetStatus getStatus() {
		return status;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		SingleBet other = (SingleBet) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (id != other.id)
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
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
		return "Bet [id=" + id + ", user=" + user + ", amount=" + amount + ", rate=" + rate + "]";
	}

}
