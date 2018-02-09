package pl.coderslab.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class GameToBet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne
	private Event event;

	@OneToMany(mappedBy = "game")
	private List<Bet> bet;

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

	public List<Bet> getBet() {
		return bet;
	}

	public void setBet(List<Bet> bet) {
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
		return "GameToBet [id=" + id + ", oddsToWinHome=" + oddsToWinHome + ", oddsToWinDraw=" + oddsToWinDraw
				+ ", oddsToWinAway=" + oddsToWinAway + ", rateHome=" + rateHome + ", rateDraw=" + rateDraw
				+ ", rateAway=" + rateAway + ", active=" + active + "]";
	}
	

}
