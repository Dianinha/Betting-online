package pl.coderslab.model;

public class H2H {

	private int numberOfDrawHome;

	private int numberOfDrawAway;

	private int numberOfDrawBetweenTeams;

	private int numberOfWinsHome;

	private int numberOfLosesAway;

	private int lastHomeGamesNumber;

	private int lastAwayGameNumber;

	private int numberOfWinsHomevsAAway;

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



	public int getNumberOfWinsHomevsAAway() {
		return numberOfWinsHomevsAAway;
	}



	public void setNumberOfWinsHomevsAAway(int numberOfWinsHomevsAAway) {
		this.numberOfWinsHomevsAAway = numberOfWinsHomevsAAway;
	}



	@Override
	public String toString() {
		return "H2H [numberOfDrawHome=" + numberOfDrawHome + ", numberOfDrawAway=" + numberOfDrawAway
				+ ", numberOfDrawBetweenTeams=" + numberOfDrawBetweenTeams + ", numberOfWinsHome=" + numberOfWinsHome
				+ ", numberOfLosesAway=" + numberOfLosesAway + ", lastHomeGamesNumber=" + lastHomeGamesNumber
				+ ", lastAwayGameNumber=" + lastAwayGameNumber + ", numberOfWinsHomevsAAway=" + numberOfWinsHomevsAAway
				+ "]";
	}

}
