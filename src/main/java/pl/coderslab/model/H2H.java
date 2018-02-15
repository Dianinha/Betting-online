package pl.coderslab.model;

/**
 * H2H or Head to head corresponds to last scores of two teams. It is simplified
 * class that I make from a lot of data from jSON. It should be expanded and
 * stored in database.
 * 
 * Possible future modification:
 * <ul>
 * <li>store H2H in database</li>
 * <li>expand it with more info</li>
 * <li>make some safe solutions if teams never played with each other or if the
 * data is unavailable</li>
 * </ul>
 * 
 * @author dianinha
 *
 */
public class H2H {

	/**
	 * How many times in last games (that I have data of) team that is marked as
	 * HOME in the event that I am calculating H2H for finished the game with result
	 * of: DRAW.
	 */
	private int numberOfDrawHome;

	/**
	 * How many times in last games (that I have data of) team that is marked as
	 * AWAY in the event that I am calculating H2H for finished the game with result
	 * of: DRAW.
	 */
	private int numberOfDrawAway;

	/**
	 * How many times in last games (that I have data of) between two teams HOME and
	 * AWAY that will be playing in event that I am calculating H2H for game was
	 * finished with DRAW between these teams.
	 */
	private int numberOfDrawBetweenTeams;

	/**
	 * How many times in last games (that I have data of) team that is marked as
	 * HOME in the event that I am calculating H2H for finished the game with result
	 * of: WIN. It does not matter if team was playing as home or away.
	 */
	private int numberOfWinsHome;

	/**
	 * How many times in last games (that I have data of) team that is marked as
	 * AWAY in the event that I am calculating H2H for finished the game with result
	 * of: LOST. It does not matter if team was playing as home or away.
	 */
	private int numberOfLosesAway;

	/**
	 * How many number of last games played by team that is marked as HOME in the
	 * event that I am calculating H2H for I have data from my API. It should be 10
	 * by default.
	 */
	private int lastHomeGamesNumber;

	/**
	 * How many number of last games played by team that is marked as AWAY in the
	 * event that I am calculating H2H for I have data from my API. It should be 10
	 * by default.
	 */
	private int lastAwayGameNumber;

	/**
	 * How many times in last 5 games between the teams HOME and AWAY in the event
	 * that I am calculating H2H for the team marked as HOME in the upcoming event
	 * (NOT IN THE LAST GAMES) won with team marked as AWAY
	 */
	private int numberOfWinsHomevsAway;

	public H2H() {
		super();
	}

	public int getNumberOfDrawHome() {
		return numberOfDrawHome;
	}

	public void setNumberOfDrawHome(int numberOfDrawHome) {
		this.numberOfDrawHome = numberOfDrawHome;
	}

	public int getNumberOfDrawAway() {
		return numberOfDrawAway;
	}

	public void setNumberOfDrawAway(int numberOfDrawAway) {
		this.numberOfDrawAway = numberOfDrawAway;
	}

	public int getNumberOfDrawBetweenTeams() {
		return numberOfDrawBetweenTeams;
	}

	public void setNumberOfDrawBetweenTeams(int numberOfDrawBetweenTeams) {
		this.numberOfDrawBetweenTeams = numberOfDrawBetweenTeams;
	}

	public int getNumberOfWinsHome() {
		return numberOfWinsHome;
	}

	public void setNumberOfWinsHome(int numberOfWinsHome) {
		this.numberOfWinsHome = numberOfWinsHome;
	}

	public int getNumberOfLosesAway() {
		return numberOfLosesAway;
	}

	public void setNumberOfLosesAway(int numberOfLosesAway) {
		this.numberOfLosesAway = numberOfLosesAway;
	}

	public int getLastHomeGamesNumber() {
		return lastHomeGamesNumber;
	}

	public void setLastHomeGamesNumber(int lastHomeGamesNumber) {
		this.lastHomeGamesNumber = lastHomeGamesNumber;
	}

	public int getLastAwayGameNumber() {
		return lastAwayGameNumber;
	}

	public void setLastAwayGameNumber(int lastAwayGameNumber) {
		this.lastAwayGameNumber = lastAwayGameNumber;
	}

	public int getNumberOfWinsHomevsAway() {
		return numberOfWinsHomevsAway;
	}

	public void setNumberOfWinsHomevsAway(int numberOfWinsHomevsAAway) {
		this.numberOfWinsHomevsAway = numberOfWinsHomevsAAway;
	}

	@Override
	public String toString() {
		return "H2H [numberOfDrawHome=" + numberOfDrawHome + ", numberOfDrawAway=" + numberOfDrawAway
				+ ", numberOfDrawBetweenTeams=" + numberOfDrawBetweenTeams + ", numberOfWinsHome=" + numberOfWinsHome
				+ ", numberOfLosesAway=" + numberOfLosesAway + ", lastHomeGamesNumber=" + lastHomeGamesNumber
				+ ", lastAwayGameNumber=" + lastAwayGameNumber + ", numberOfWinsHomevsAAway=" + numberOfWinsHomevsAway
				+ "]";
	}

}
