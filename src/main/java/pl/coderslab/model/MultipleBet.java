package pl.coderslab.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class MultipleBet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bet_id")
	private long id;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "multipleBet_bet", joinColumns = @JoinColumn(name = "groupBet_id"), inverseJoinColumns = @JoinColumn(name = "bet_id"))
	private List<SingleBet> bets;

	@ManyToOne
	@JoinColumn
	private User user;

	private BigDecimal joinedRating;

	private BigDecimal joinedAmount;

	private BetStatus status;

	private String result;

	private boolean groupBetPossible;

	private boolean isItAGroupBet;

	public MultipleBet() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<SingleBet> getBets() {
		return bets;
	}

	public void setBets(List<SingleBet> bet) {
		this.bets = bet;
	}

	public boolean isItAGroupBet() {
		return isItAGroupBet;
	}

	public void setItAGroupBet(boolean isItAGroupBet) {
		this.isItAGroupBet = isItAGroupBet;
	}

	public User getUsers() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BigDecimal getJoinedRating() {
		return joinedRating;
	}

	public void setJoinedRating(BigDecimal joinedRating) {
		this.joinedRating = joinedRating;
	}

	public BigDecimal getJoinedAmount() {
		return joinedAmount;
	}

	public void setJoinedAmount(BigDecimal joinedAmount) {
		this.joinedAmount = joinedAmount;
	}

	public boolean isGroupBetPossible() {
		return groupBetPossible;
	}

	public void setGroupBetPossible(boolean groupBetPossible) {
		this.groupBetPossible = groupBetPossible;
	}

	public BetStatus getStatus() {
		return status;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "MultipleBet [id=" + id + ", bet=" + bets + ", user=" + user + ", joinedRating=" + joinedRating
				+ ", joinedAmount=" + joinedAmount + "]";
	}

}
