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

/**
 * This class represents a bet that is placed on multiple events together. All
 * bets must be won for user to win this bet.
 * 
 * <p>
 * Possible future modification:
 * <ul>
 * <li>make bet an interface and different types of bets like SingleBet or
 * MultipleBet or GroupBet should implement it</li>
 * <li>RENAME joinedAmount because it is misleading. Now there is no time for it
 * because it is in too many views. It should be called "amount"</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
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

	/**
	 * Refers to possibility to make this bet a group bet. If event one of the
	 * events have already started there is no option to change a MultipleBet to
	 * GroupBet.
	 */
	private boolean groupBetPossible;

	/**
	 * True if it was converted to group bet, false otherwise
	 */
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
		return "MultipleBet [id=" + id + ", user=" + user.getUsername() + ", joinedRating=" + joinedRating
				+ ", joinedAmount=" + joinedAmount + ", status=" + status + ", result=" + result + ", groupBetPossible="
				+ groupBetPossible + ", isItAGroupBet=" + isItAGroupBet + "]";
	}

}
