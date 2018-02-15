package pl.coderslab.model;

/**
 * This class represents Group Bet. Group bet is a bet that is shared with other users. They benefit from it by gaining extra money for more people participating.
 * 
 * Possible future modification:
 * <ul>
 * <li> make bet an interface and different types of bets like SingleBet or MultipleBet or GroupBet should implement it</li>
 * <li> change the BetCode for something better than address in memory. Some String generator would be nice</li>
 * <li> RENAME joinedAmount because it is misleading. Now there is no time for it because it is in too many views. It should be called "amountPerUser"</li>
 *  <li> make bet code actually useful so people can join through writing it somewhere on webpage </li>
 * </ul>
 */
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
	/**
	 * Group bet can contain only one bet but it will be still stored in a list.
	 * Attention! Again misleading name : should be called "bets" or better
	 * "singleBets"
	 */
	@OneToMany
	@JoinTable(name = "groupBet_bet", joinColumns = @JoinColumn(name = "groupBet_id"), inverseJoinColumns = @JoinColumn(name = "bet_id"))
	private List<SingleBet> bet;

	/**
	 * Max size of that list is 20
	 */
	@ManyToMany
	@JoinTable(name = "groupBet_User", joinColumns = @JoinColumn(name = "groupBet_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;

	/**
	 * Useless for now.
	 */
	@Column(unique = true)
	private String betCode;

	private BigDecimal joinedRating;

	/**
	 * Attention! Misleading name. It should be called "amountPerUser". I have no
	 * time to change it because it is in too many views.
	 */
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
