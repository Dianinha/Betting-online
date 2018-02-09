package pl.coderslab.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Event {

	@Id
	private long id;

	@ManyToOne
	@JoinColumn
	private Country country;

	@ManyToOne
	@JoinColumn
	private League legaue;

	private LocalDate date;

	private String status;

	private String time;

	private String homeTeamName;

	private int homeTeamScore;

	private String awayTeamName;

	private int awayTeamScore;

	private int homeTeamScoreHalfTime;

	private int awayTeamScoreHalfTime;

	private String matchLive;

	@OneToMany(mappedBy = "event")
	private List<GoalScorer> goalScorrers;

	@OneToMany(mappedBy = "event")
	private List<Statistics> stats;

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

	public List<Statistics> getStats() {
		return stats;
	}

	public void setStats(List<Statistics> stats) {
		this.stats = stats;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", country=" + country + ", legaue=" + legaue + ", date=" + date + ", status="
				+ status + ", time=" + time + ", homeTeamName=" + homeTeamName + ", homeTeamScore=" + homeTeamScore
				+ ", awayTeamName=" + awayTeamName + ", awayTeamScore=" + awayTeamScore + ", homeTeamScoreHalfTime="
				+ homeTeamScoreHalfTime + ", awayTeamScoreHalfTime=" + awayTeamScoreHalfTime + ", matchLive="
				+ matchLive + "]";
	}

}
