package pl.coderslab.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import pl.coderslab.service.EventService;

/**
 * This class represents real life football event. The data is created from the
 * API through {@link EventService}
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li>make the Event class more diverse and be a parent to many specific event
 * classes, for example FootballEvent, BasketBallEvent, etc.</li>
 * <li>OR make Event an Interface and have specific classes like FootBallEvent
 * implement it. Could be even better</li>
 * <li>change relation with {@link Category} to ManyToMany so event can belong
 * to many categories</li>
 * <li>add statistics (possible by API)</li>
 * <li>add cards (possible by API)</li>
 * <li>add line ups (possible by API)</li>
 * <li>actually use the goal scorers data that I gather</li>
 * <li>also use the half time score data that I gather</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
public class Event {

	@Id
	private long id;

	/**
	 * Country that event is related to : usually the country of league
	 */
	@ManyToOne
	@JoinColumn
	private Country country;

	/**
	 * League that Event is played in
	 */
	@ManyToOne
	@JoinColumn
	private League legaue;

	private LocalDate date;

	/**
	 * Status shows event status. It can be:
	 * <ul>
	 * <li>"FT" : event has ended</li>
	 * <li>"" : empty {@link String}, event has not yet begun
	 * <li>
	 * <li>"HT" : half time</li>
	 * <li>"XX'" : where X represents one digit. It is a number of minutes in a
	 * game, for example: "9'" stands for nine minutes in the game</li>
	 * <li>"XX'+X" : where X represents one digit. It is a number of minutes in a
	 * game plus added time, for example: "45'+3" stands for 45 minutes in a game
	 * plus third minute from extra time</li>
	 * <ul>
	 */
	private String status;

	/**
	 * Time that Event starts
	 */
	private String time;

	private String homeTeamName;

	@OneToOne
	@JoinColumn
	private GameToBet game;

	private int homeTeamScore;

	private String awayTeamName;

	private int awayTeamScore;

	/**
	 * Unfortunately not used yet in my application
	 */
	private int homeTeamScoreHalfTime;

	/**
	 * Unfortunately not used yet in my application
	 */
	private int awayTeamScoreHalfTime;

	private String matchLive;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Category category;

	/**
	 * Unfortunately not used yet in my application
	 */
	@OneToMany(mappedBy = "event")
	private List<GoalScorer> goalScorrers;

	public Event() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public League getLegaue() {
		return legaue;
	}

	public void setLegaue(League legaue) {
		this.legaue = legaue;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public GameToBet getGame() {
		return game;
	}

	public void setGame(GameToBet game) {
		this.game = game;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}

	public int getHomeTeamScore() {
		return homeTeamScore;
	}

	public void setHomeTeamScore(int homeTeamScore) {
		this.homeTeamScore = homeTeamScore;
	}

	public String getAwayTeamName() {
		return awayTeamName;
	}

	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}

	public int getAwayTeamScore() {
		return awayTeamScore;
	}

	public void setAwayTeamScore(int awayTeamScore) {
		this.awayTeamScore = awayTeamScore;
	}

	public int getHomeTeamScoreHalfTime() {
		return homeTeamScoreHalfTime;
	}

	public void setHomeTeamScoreHalfTime(int homeTeamScoreHalfTime) {
		this.homeTeamScoreHalfTime = homeTeamScoreHalfTime;
	}

	public int getAwayTeamScoreHalfTime() {
		return awayTeamScoreHalfTime;
	}

	public void setAwayTeamScoreHalfTime(int awayTeamScoreHalfTime) {
		this.awayTeamScoreHalfTime = awayTeamScoreHalfTime;
	}

	public String getMatchLive() {
		return matchLive;
	}

	public void setMatchLive(String matchLive) {
		this.matchLive = matchLive;
	}

	public List<GoalScorer> getGoalScorrers() {
		return goalScorrers;
	}

	public void setGoalScorrers(List<GoalScorer> goalScorrers) {
		this.goalScorrers = goalScorrers;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", country=" + country + ", legaue=" + legaue + ", date=" + date + ", status="
				+ status + ", time=" + time + ", homeTeamName=" + homeTeamName + ", game=" + game + ", homeTeamScore="
				+ homeTeamScore + ", awayTeamName=" + awayTeamName + ", awayTeamScore=" + awayTeamScore
				+ ", homeTeamScoreHalfTime=" + homeTeamScoreHalfTime + ", awayTeamScoreHalfTime="
				+ awayTeamScoreHalfTime + ", matchLive=" + matchLive + ", category=" + category + ", goalScorrers="
				+ goalScorrers + "]";
	}

}
