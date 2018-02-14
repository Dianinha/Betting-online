package pl.coderslab.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class GroupBet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bet_id")
	private long id;

	@OneToMany
	@JoinTable(name = "groupBet_bet", joinColumns = @JoinColumn(name = "groupBet_id"), inverseJoinColumns = @JoinColumn(name = "bet_id"))
	private List<SingleBet> bet;

	@ManyToMany
	@JoinTable(name = "groupBet_User", joinColumns = @JoinColumn(name = "groupBet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;

	@Column(unique = true)
	private String betCode;
	
	private BigDecimal joinedRating;
	
	private BigDecimal joinedAmount;

	public GroupBet() {
		super();
	}
	
	private BetStatus status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<SingleBet> getBet() {
		return bet;
	}

	public void setBet(List<SingleBet> bet) {
		this.bet = bet;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getBetCode() {
		return betCode;
	}

	public void setBetCode(String betCode) {
		this.betCode = betCode;
	}

	public BigDecimal getJoinedRating() {
		return joinedRating;
	}

	public void setJoinedRating(BigDecimal joinedRating) {
		this.joinedRating = joinedRating;
	}

	public BetStatus getStatus() {
		return status;
	}

	public void setStatus(BetStatus status) {
		this.status = status;
	}

	public BigDecimal getJoinedAmount() {
		return joinedAmount;
	}

	public void setJoinedAmount(BigDecimal joinedAmount) {
		this.joinedAmount = joinedAmount;
	}

	
	
	
}
