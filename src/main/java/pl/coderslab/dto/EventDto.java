package pl.coderslab.dto;

import java.time.LocalDate;

import pl.coderslab.model.GameToBet;

public class EventDto {

	private long id;

	private String country;

	private String legaue;

	private LocalDate date;

	private String status;

	private String time;

	private String homeTeamName;

	private long game;

	private int homeTeamScore;

	private String awayTeamName;

	private int awayTeamScore;

	private String matchLive;

	private String category;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLegaue() {
		return legaue;
	}

	public void setLegaue(String legaue) {
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

	public long getGame() {
		return game;
	}

	public void setGame(long game) {
		this.game = game;
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

	public String getMatchLive() {
		return matchLive;
	}

	public void setMatchLive(String matchLive) {
		this.matchLive = matchLive;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public EventDto(long id, String country, String legaue, LocalDate date, String status, String time,
			String homeTeamName, long game, int homeTeamScore, String awayTeamName, int awayTeamScore, String matchLive,
			String category) {
		super();
		this.id = id;
		this.country = country;
		this.legaue = legaue;
		this.date = date;
		this.status = status;
		this.time = time;
		this.homeTeamName = homeTeamName;
		this.game = game;
		this.homeTeamScore = homeTeamScore;
		this.awayTeamName = awayTeamName;
		this.awayTeamScore = awayTeamScore;
		this.matchLive = matchLive;
		this.category = category;
	}

	public EventDto() {
		super();
	}

}
