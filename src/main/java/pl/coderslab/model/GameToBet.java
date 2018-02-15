package pl.coderslab.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pl.coderslab.service.GameToBetService;

/**
 * This class represents the bet possibility, it stores actual rates for bets
 * and odds for different results (home win, away win or draw) happen. It is
 * created through {@link GameToBetService}
 * 
 * Every {@link SingleBet} must be related to this class.
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li>make this an Interface and have specific classes like FootBallGameToBetOn
 * or TennisGameToBetOn implementing it</li>
 * <li>interesting idea : make it so diverse so it can even apply to horse
 * racing</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
public class GameToBet {

	@Id
	private long id;

	@JsonIgnore
	@OneToOne
	private Event event;

	@JsonIgnore
	@OneToMany(mappedBy = "game")
	private List<SingleBet> bet;

	private double oddsToWinHome;

	private double oddsToWinDraw;

	private double oddsToWinAway;

	private BigDecimal rateHome;

	private BigDecimal rateDraw;

	private BigDecimal rateAway;

	private boolean active;

	public GameToBet() {
		super();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<SingleBet> getBet() {
		return bet;
	}

	public void setBet(List<SingleBet> bet) {
		this.bet = bet;
	}

	public double getOddsToWinHome() {
		return oddsToWinHome;
	}

	public void setOddsToWinHome(double oddsToWinHome) {
		this.oddsToWinHome = oddsToWinHome;
	}

	public double getOddsToWinDraw() {
		return oddsToWinDraw;
	}

	public void setOddsToWinDraw(double oddsToWinDraw) {
		this.oddsToWinDraw = oddsToWinDraw;
	}

	public double getOddsToWinAway() {
		return oddsToWinAway;
	}

	public void setOddsToWinAway(double oddsToWinAway) {
		this.oddsToWinAway = oddsToWinAway;
	}

	public BigDecimal getRateHome() {
		return rateHome;
	}

	public void setRateHome(BigDecimal rateHome) {
		this.rateHome = rateHome;
	}

	public BigDecimal getRateDraw() {
		return rateDraw;
	}

	public void setRateDraw(BigDecimal rateDraw) {
		this.rateDraw = rateDraw;
	}

	public BigDecimal getRateAway() {
		return rateAway;
	}

	public void setRateAway(BigDecimal rateAway) {
		this.rateAway = rateAway;
	}

	@Override
	public String toString() {
		return "GameToBet [id=" + id + ", oddsToWinHome=" + getOddsToWinHome() + ", oddsToWinDraw=" + oddsToWinDraw
				+ ", oddsToWinAway=" + oddsToWinAway + ", rateHome=" + rateHome + ", rateDraw=" + rateDraw
				+ ", rateAway=" + rateAway + ", active=" + active + "]";
	}

}
