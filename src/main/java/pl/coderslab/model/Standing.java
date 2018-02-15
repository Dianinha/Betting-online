package pl.coderslab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.coderslab.service.StandingService;
/**
 * Corresponds to standing in {@link League} for one team in one season.
 * Data is gathered from API through {@link StandingService}
 * 
 * @author dianinha
 *
 */
@Entity
public class Standing {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn
	private Country country;

	@ManyToOne
	@JoinColumn
	private League league;

	@Column(unique = true)
	private String season;

	private String teamName;

	private int leaguePosition;

	private int matchesPlayed;

	private int matchesWon;

	private int matchesDraw;

	private int matchesLost;

	private int goalsScored;

	private int goalsLost;

	private int points;

	// ---------------------

	private int homeLeaguePosition;

	private int homeMatchesPlayed;

	private int homeMatchesWon;

	private int homeMatchesDraw;

	private int homeMatchesLost;

	private int homeGoalsScored;

	private int homeGoalsLost;

	private int homePoints;

	// ---------------------

	private int awayLeaguePosition;

	private int awayMatchesPlayed;

	private int awayMatchesWon;

	private int awayMatchesDraw;

	private int awayMatchesLost;

	private int awayGoalsScored;

	private int awayGoalsLost;

	private int awayPoints;

	public Standing() {
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

	public League getLeague() {
		return league;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getLeaguePosition() {
		return leaguePosition;
	}

	public void setLeaguePosition(int leaguePosition) {
		this.leaguePosition = leaguePosition;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getMatchesWon() {
		return matchesWon;
	}

	public void setMatchesWon(int matchesWon) {
		this.matchesWon = matchesWon;
	}

	public int getMatchesDraw() {
		return matchesDraw;
	}

	public void setMatchesDraw(int matchesDraw) {
		this.matchesDraw = matchesDraw;
	}

	public int getMatchesLost() {
		return matchesLost;
	}

	public void setMatchesLost(int matchesLost) {
		this.matchesLost = matchesLost;
	}

	public int getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}

	public int getGoalsLost() {
		return goalsLost;
	}

	public void setGoalsLost(int goalsLost) {
		this.goalsLost = goalsLost;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getHomeLeaguePosition() {
		return homeLeaguePosition;
	}

	public void setHomeLeaguePosition(int homeLeaguePosition) {
		this.homeLeaguePosition = homeLeaguePosition;
	}

	public int getHomeMatchesPlayed() {
		return homeMatchesPlayed;
	}

	public void setHomeMatchesPlayed(int homeMatchesPlayed) {
		this.homeMatchesPlayed = homeMatchesPlayed;
	}

	public int getHomeMatchesWon() {
		return homeMatchesWon;
	}

	public void setHomeMatchesWon(int homeMatchesWon) {
		this.homeMatchesWon = homeMatchesWon;
	}

	public int getHomeMatchesDraw() {
		return homeMatchesDraw;
	}

	public void setHomeMatchesDraw(int homeMatchesDraw) {
		this.homeMatchesDraw = homeMatchesDraw;
	}

	public int getHomeMatchesLost() {
		return homeMatchesLost;
	}

	public void setHomeMatchesLost(int homeMatchesLost) {
		this.homeMatchesLost = homeMatchesLost;
	}

	public int getHomeGoalsScored() {
		return homeGoalsScored;
	}

	public void setHomeGoalsScored(int homeGoalsScored) {
		this.homeGoalsScored = homeGoalsScored;
	}

	public int getHomeGoalsLost() {
		return homeGoalsLost;
	}

	public void setHomeGoalsLost(int homeGoalsLost) {
		this.homeGoalsLost = homeGoalsLost;
	}

	public int getHomePoints() {
		return homePoints;
	}

	public void setHomePoints(int homePoints) {
		this.homePoints = homePoints;
	}

	public int getAwayLeaguePosition() {
		return awayLeaguePosition;
	}

	public void setAwayLeaguePosition(int awayLeaguePosition) {
		this.awayLeaguePosition = awayLeaguePosition;
	}

	public int getAwayMatchesPlayed() {
		return awayMatchesPlayed;
	}

	public void setAwayMatchesPlayed(int awayMatchesPlayed) {
		this.awayMatchesPlayed = awayMatchesPlayed;
	}

	public int getAwayMatchesWon() {
		return awayMatchesWon;
	}

	public void setAwayMatchesWon(int awayMatchesWon) {
		this.awayMatchesWon = awayMatchesWon;
	}

	public int getAwayMatchesDraw() {
		return awayMatchesDraw;
	}

	public void setAwayMatchesDraw(int awayMatchesDraw) {
		this.awayMatchesDraw = awayMatchesDraw;
	}

	public int getAwayMatchesLost() {
		return awayMatchesLost;
	}

	public void setAwayMatchesLost(int awayMatchesLost) {
		this.awayMatchesLost = awayMatchesLost;
	}

	public int getAwayGoalsScored() {
		return awayGoalsScored;
	}

	public void setAwayGoalsScored(int awayGoalsScored) {
		this.awayGoalsScored = awayGoalsScored;
	}

	public int getAwayGoalsLost() {
		return awayGoalsLost;
	}

	public void setAwayGoalsLost(int awayGoalsLost) {
		this.awayGoalsLost = awayGoalsLost;
	}

	public int getAwayPoints() {
		return awayPoints;
	}

	public void setAwayPoints(int awayPoints) {
		this.awayPoints = awayPoints;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + homePoints;
		result = prime * result + awayGoalsLost;
		result = prime * result + awayGoalsScored;
		result = prime * result + awayLeaguePosition;
		result = prime * result + awayMatchesDraw;
		result = prime * result + awayMatchesLost;
		result = prime * result + awayMatchesPlayed;
		result = prime * result + awayMatchesWon;
		result = prime * result + awayPoints;
		result = prime * result + goalsLost;
		result = prime * result + goalsScored;
		result = prime * result + homeGoalsLost;
		result = prime * result + homeGoalsScored;
		result = prime * result + homeLeaguePosition;
		result = prime * result + homeMatchesDraw;
		result = prime * result + homeMatchesLost;
		result = prime * result + homeMatchesPlayed;
		result = prime * result + homeMatchesWon;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + leaguePosition;
		result = prime * result + matchesDraw;
		result = prime * result + matchesLost;
		result = prime * result + matchesPlayed;
		result = prime * result + matchesWon;
		result = prime * result + points;
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
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
		Standing other = (Standing) obj;
		if (homePoints != other.homePoints)
			return false;
		if (awayGoalsLost != other.awayGoalsLost)
			return false;
		if (awayGoalsScored != other.awayGoalsScored)
			return false;
		if (awayLeaguePosition != other.awayLeaguePosition)
			return false;
		if (awayMatchesDraw != other.awayMatchesDraw)
			return false;
		if (awayMatchesLost != other.awayMatchesLost)
			return false;
		if (awayMatchesPlayed != other.awayMatchesPlayed)
			return false;
		if (awayMatchesWon != other.awayMatchesWon)
			return false;
		if (awayPoints != other.awayPoints)
			return false;
		if (goalsLost != other.goalsLost)
			return false;
		if (goalsScored != other.goalsScored)
			return false;
		if (homeGoalsLost != other.homeGoalsLost)
			return false;
		if (homeGoalsScored != other.homeGoalsScored)
			return false;
		if (homeLeaguePosition != other.homeLeaguePosition)
			return false;
		if (homeMatchesDraw != other.homeMatchesDraw)
			return false;
		if (homeMatchesLost != other.homeMatchesLost)
			return false;
		if (homeMatchesPlayed != other.homeMatchesPlayed)
			return false;
		if (homeMatchesWon != other.homeMatchesWon)
			return false;
		if (id != other.id)
			return false;
		if (leaguePosition != other.leaguePosition)
			return false;
		if (matchesDraw != other.matchesDraw)
			return false;
		if (matchesLost != other.matchesLost)
			return false;
		if (matchesPlayed != other.matchesPlayed)
			return false;
		if (matchesWon != other.matchesWon)
			return false;
		if (points != other.points)
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Standing [id=" + id + ", country=" + country + ", league=" + league + ", teamName=" + teamName
				+ ", leaguePosition=" + leaguePosition + ", matchesPlayed=" + matchesPlayed + ", matchesWon="
				+ matchesWon + ", matchesDraw=" + matchesDraw + ", matchesLost=" + matchesLost + ", goalsScored="
				+ goalsScored + ", goalsLost=" + goalsLost + ", points=" + points + ", homeLeaguePosition="
				+ homeLeaguePosition + ", homeMatchesPlayed=" + homeMatchesPlayed + ", homeMatchesWon=" + homeMatchesWon
				+ ", homeMatchesDraw=" + homeMatchesDraw + ", homeMatchesLost=" + homeMatchesLost + ", homeGoalsScored="
				+ homeGoalsScored + ", homeGoalsLost=" + homeGoalsLost + ", HomePoints=" + homePoints
				+ ", awayLeaguePosition=" + awayLeaguePosition + ", awayMatchesPlayed=" + awayMatchesPlayed
				+ ", awayMatchesWon=" + awayMatchesWon + ", awayMatchesDraw=" + awayMatchesDraw + ", awayMatchesLost="
				+ awayMatchesLost + ", awayGoalsScored=" + awayGoalsScored + ", awayGoalsLost=" + awayGoalsLost
				+ ", awayPoints=" + awayPoints + "]";
	}

}
