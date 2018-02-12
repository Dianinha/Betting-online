package pl.coderslab.serviceImpl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.H2H;
import pl.coderslab.model.Standing;
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.GameToBetRepository;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.StandingService;

@Service
public class GameToBetServiceImpl implements GameToBetService {

	@Autowired
	EventService eventService;

	@Autowired
	GameToBetRepository gameRepository;

	@Autowired
	StandingService standingService;
	
	@Autowired
	APIRepository apiRepository;

	// @Override
	// public void createGamesToBet() {
	// List<Event> futureEvents = eventService.findByDate(LocalDate.now());
	// for (Event event : futureEvents) {
	// GameToBet game = new GameToBet();
	//
	// System.out.println(event.getStatus());
	// if (event.getStatus().equals("FT")) {
	// game.setActive(false);
	// } else {
	// game.setActive(true);
	// }
	// game.setId(event.getId());
	// game.setEvent(event);
	// Standing home =
	// standingService.findStanfingByTeamName(event.getHomeTeamName());
	// Standing away =
	// standingService.findStanfingByTeamName(event.getAwayTeamName());
	// System.out.println(home);
	// System.out.println(away);
	// game.setOddsToWinHome(0.5);
	//
	// game.setOddsToWinAway(0.5);
	//
	// game.setOddsToWinDraw(0.2);
	//
	// game.setRateHome(new BigDecimal((1.00 - game.getOddsToWinHome()) + 0.80));
	// game.setRateDraw(new BigDecimal((1.00 - game.getOddsToWinDraw()) + 0.80));
	// game.setRateAway(new BigDecimal((1 - game.getOddsToWinAway()) + 0.80));
	//
	// System.out.println(game);
	// gameRepository.save(game);
	// }
	//
	// }

	@Override
	public void createGamesToBetFromEvents(List<Event> events) {
		List<Event> futureEvents = events;
		for (Event event : futureEvents) {
			GameToBet game = new GameToBet();
			System.out.println(event.getStatus());
			if (event.getStatus().equals("FT")) {
				game.setActive(false);
			} else {
				game.setActive(true);
			}
			game.setId(event.getId());
			game.setEvent(event);
			gameRepository.save(game);

		}
		updateGamesToBet(futureEvents);

	}

	@Override
	public GameToBet findByEvent(Event event) {
		return gameRepository.findByEvent(event);
	}

	@Override
	public GameToBet findById(long id) {
		return gameRepository.findOne(id);
	}

	@Override
	public void updateGamesToBet(List<Event> liveEvents) {

		for (Event event : liveEvents) {
			if (eventService.checkIfEventHasEnded(event)) {
				GameToBet game = gameRepository.findByEvent(event);
				game.setActive(false);
				gameRepository.save(game);
			} else {
				GameToBet game = gameRepository.findByEvent(event);
				gameRepository.save(recalculateOdds(game, event));
			}

		}

	}

	private GameToBet recalculateOdds(GameToBet game, Event event) {
		String homeTeamName = event.getHomeTeamName();
		String awayTeamName = event.getAwayTeamName();
		Standing homeStanding = standingService.findStanfingByTeamName(homeTeamName);
		Standing awayStanding = standingService.findStanfingByTeamName(awayTeamName);
		H2H headToHead = createH2H(homeTeamName, awayTeamName);
		game.setOddsToWinDraw(calculateOddsToDraw(homeTeamName, awayTeamName, headToHead));
		game.setOddsToWinHome(calculateOddsToWinHome(homeTeamName, awayTeamName, headToHead));
		game.setOddsToWinAway(1 - game.getOddsToWinDraw() - game.getOddsToWinHome());
		game.setRateHome(generateRate(game.getOddsToWinHome()));
		game.setRateAway(generateRate(game.getOddsToWinAway()));
		game.setRateDraw(generateRate(game.getOddsToWinDraw()));
		return game;
	}

	private BigDecimal generateRate(double odd) {
		double y = 1 / odd * 0.92;
		if (y < 1) {
			return new BigDecimal(1.00);
		} else {
			return new BigDecimal(y);
		}

	}

	private double calculateOddsToDraw(String home, String away, H2H head) {
		Standing homeStanding = standingService.findStanfingByTeamName(home);
		Standing awayStanding = standingService.findStanfingByTeamName(away);
		double odds = 0.25;
		if (homeStanding.getMatchesPlayed() < 7) {
			odds = 0.3 * head.getNumberOfDrawHome() / head.getLastHomeGamesNumber()
					+ 0.3 * head.getNumberOfDrawAway() / head.getLastAwayGameNumber()
					+ 0.3 * head.getNumberOfDrawBetweenTeams() / 5 + 0.025;
		} else {
			odds = 0.1 * homeStanding.getMatchesDraw() / homeStanding.getMatchesPlayed()
					+ 0.1 * awayStanding.getMatchesDraw() / awayStanding.getMatchesPlayed()
					+ 0.15 * homeStanding.getHomeMatchesDraw() / homeStanding.getHomeMatchesPlayed()
					+ 0.15 * awayStanding.getAwayMatchesDraw() / awayStanding.getAwayMatchesPlayed()
					+ 0.15 * head.getNumberOfDrawHome() / head.getLastHomeGamesNumber()
					+ 0.15 * head.getNumberOfDrawAway() / head.getLastAwayGameNumber()
					+ 0.15 * head.getNumberOfDrawBetweenTeams() / 5 + 0.0125;

		}
		System.out.println(odds + "Draw");
		return odds;
	}

	private double calculateOddsToWinHome(String home, String away, H2H head) {
		Standing homeStanding = standingService.findStanfingByTeamName(home);
		Standing awayStanding = standingService.findStanfingByTeamName(away);
		System.out.println(homeStanding);
		System.out.println(awayStanding);
		double odds = 0.25;
		if (homeStanding.getMatchesPlayed() < 7) {
			odds = 0.3 * head.getNumberOfWinsHome() / head.getLastHomeGamesNumber()
					+ 0.3 * head.getNumberOfLosesAway() / head.getLastAwayGameNumber()
					+ 0.3 * head.getNumberOfWinsHomevsAAway() / 5 + 0.025;
		} else {
			odds = 0.05 * homeStanding.getMatchesWon() / homeStanding.getMatchesPlayed()
					+ 0.05 * awayStanding.getMatchesLost() / awayStanding.getMatchesPlayed()
					+ 0.2 * homeStanding.getHomeMatchesWon() / homeStanding.getHomeMatchesPlayed()
					+ 0.2 * awayStanding.getAwayMatchesLost() / awayStanding.getAwayMatchesPlayed()
					+ 0.15 * head.getNumberOfWinsHome() / head.getLastHomeGamesNumber()
					+ 0.15 * head.getNumberOfLosesAway() / head.getLastAwayGameNumber()
					+ 0.15 * head.getNumberOfWinsHomevsAAway() / 5 + 0.0125;

		}
		System.out.println(odds + " Home win");
		return odds;

	}

	private H2H createH2H(String home, String away) {
		H2H result = new H2H();
		String url = "https://apifootball.com/api/?action=get_H2H&firstTeam=" + home.replace(' ', '+') + "&secondTeam="
				+ away.replace(' ', '+') + "&APIkey="+ apiRepository.findOne((long)1).getKeyCode();
		JSONParser parser = new JSONParser();
		int numberOfDrawCounter = 0;
		int numberOfDrawHomeCounter = 0;
		int numberOfDrawAwayCounter = 0;
		int numberOfGamesHome = 0;
		int numberOfGamesAway = 0;
		int numberofWonLastGamesHome = 0;
		int numberofLostLastGamesAway = 0;
		int numberofWonLastGamesHomeVSAway = 0;

		try {
			URL getDataFrom = new URL(url);
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;

			// Reading JSON
			while ((inputLine = in.readLine()) != null) {
				// parsing Array of JSON
				JSONObject JSONh2h = (JSONObject) parser.parse(inputLine);

				JSONArray jsonfirstVsSecond = (JSONArray) JSONh2h.get("firstTeam_VS_secondTeam");
				JSONArray jsonHomeTeam = (JSONArray) JSONh2h.get("firstTeam_lastResults");
				JSONArray jsonAwayTeam = (JSONArray) JSONh2h.get("secondTeam_lastResults");

				for (Object object : jsonfirstVsSecond) {
					JSONObject match = (JSONObject) object;
					int homeScore = Integer.parseInt((String) match.get("match_hometeam_score"));

					int awayScore = Integer.parseInt((String) match.get("match_awayteam_score"));
					if (homeScore == awayScore) {
						numberOfDrawCounter++;
					}
					String whoIsHomeTeam = (String) match.get("match_hometeam_name");
					if (homeScore > awayScore && whoIsHomeTeam.equals(home)) {
						numberofWonLastGamesHomeVSAway++;
					}
					if (awayScore > homeScore && !whoIsHomeTeam.equals(home)) {
						numberofWonLastGamesHome++;
					}
				}

				for (Object object : jsonHomeTeam) {
					JSONObject match = (JSONObject) object;
					int homeScore = Integer.parseInt((String) match.get("match_hometeam_score"));
					int awayScore = Integer.parseInt((String) match.get("match_awayteam_score"));
					if (homeScore == awayScore) {
						numberOfDrawHomeCounter++;
					}
					String whoIsHomeTeam = (String) match.get("match_hometeam_name");
					if (homeScore > awayScore && whoIsHomeTeam.equals(home)) {
						numberofWonLastGamesHome++;
					}
					if (awayScore > homeScore && !whoIsHomeTeam.equals(home)) {
						numberofWonLastGamesHome++;
					}
				}
				numberOfGamesHome = jsonHomeTeam.size();

				for (Object object : jsonAwayTeam) {
					JSONObject match = (JSONObject) object;
					int homeScore = Integer.parseInt((String) match.get("match_hometeam_score"));
					int awayScore = Integer.parseInt((String) match.get("match_awayteam_score"));
					if (homeScore == awayScore) {
						numberOfDrawAwayCounter++;
					}
					String whoIsHomeTeam = (String) match.get("match_hometeam_name");
					if (homeScore < awayScore && whoIsHomeTeam.equals(away)) {
						numberofLostLastGamesAway++;
					}
					if (awayScore < homeScore && !whoIsHomeTeam.equals(away)) {
						numberofLostLastGamesAway++;
					}
				}
				numberOfGamesAway = jsonAwayTeam.size();
			}
			in.close();
		} catch (

		MalformedURLException e) {
			e.printStackTrace();
			System.out.println("here1");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("here2");
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("here3");
		}
		result.setNumberOfDrawBetweenTeams(numberOfDrawCounter);
		result.setNumberOfDrawHome(numberOfDrawHomeCounter);
		result.setNumberOfDrawAway(numberOfDrawAwayCounter);
		result.setLastAwayGameNumber(numberOfGamesAway);
		result.setLastHomeGamesNumber(numberOfGamesHome);
		result.setNumberOfLosesAway(numberofLostLastGamesAway);
		result.setNumberOfWinsHome(numberofWonLastGamesHome);
		result.setNumberOfWinsHomevsAAway(numberofWonLastGamesHomeVSAway);

		System.out.println(result);

		return result;

	}

	@Override
	public void updateLiveEventsGamesToBet() {
		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		for (Event event : liveEvents) {
			Integer minutesIn =0;
			if (eventService.checkIfEventHasEnded(event)) {
				GameToBet game = gameRepository.findByEvent(event);
				game.setActive(false);
				gameRepository.save(game);
			} else {
				GameToBet game = gameRepository.findByEvent(event);
				String status = event.getStatus();
				if (status.equals("HT")) {
					
				}
				else if (status.startsWith("9")) {
					game.setActive(false);
				}
				else {
					try {
						minutesIn = Integer.parseInt(status.substring(0, status.length()-1));
						int homeScore = event.getHomeTeamScore();
						int awayScore = event.getAwayTeamScore();
						
						//here something
						
					} catch (Exception e) {
						System.out.println("Cannot parse: " + status);
					}
				}
			}

		}
	}


}
