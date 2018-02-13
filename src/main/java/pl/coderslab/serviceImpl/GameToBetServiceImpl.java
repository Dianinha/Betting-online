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
import pl.coderslab.model.League;
import pl.coderslab.model.Standing;
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.EventRepository;
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

	@Autowired
	EventRepository eventRepository;

	@Override
	public void createGamesToBetFromEvents(List<Event> events) {
		List<Event> futureEvents = events;
		for (Event event : futureEvents) {
			if (event.getLegaue().getCountry() == null) {
				GameToBet game = new GameToBet();
				game.setActive(false);
				game.setId(event.getId());
				game.setEvent(event);
				game.setRateAway(new BigDecimal(1.0));
				game.setRateHome(new BigDecimal(1.0));
				game.setRateDraw(new BigDecimal(1.0));
				gameRepository.save(game);
				event.setGame(game);
				eventRepository.save(event);
			} else {
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
				event.setGame(game);
				eventRepository.save(event);

			}
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
				if (event.getLegaue().getCountry() == null) {

					// liga mistrzów tutaj
				} else {
					GameToBet game = gameRepository.findByEvent(event);
					if (event.getLegaue().getCountry() != null || event.getLegaue().getName().contains("GROUP")) {

						gameRepository.save(recalculateOdds(game, event));
					}
				}
			}

		}

	}

	private GameToBet recalculateOdds(GameToBet game, Event event) {
		String homeTeamName = event.getHomeTeamName();
		String awayTeamName = event.getAwayTeamName();
		Standing homeStanding = standingService.findStangingByTeamNameAndLeague(homeTeamName, event.getLegaue());
		Standing awayStanding = standingService.findStangingByTeamNameAndLeague(awayTeamName, event.getLegaue());
		H2H headToHead = createH2H(homeTeamName, awayTeamName);
		game.setOddsToWinDraw(calculateOddsToDraw(homeTeamName, awayTeamName, headToHead, event.getLegaue()));
		game.setOddsToWinHome(calculateOddsToWinHome(homeTeamName, awayTeamName, headToHead, event.getLegaue()));
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

	private double calculateOddsToDraw(String home, String away, H2H head, League league) {
		Standing homeStanding = standingService.findStangingByTeamNameAndLeague(home, league);
		Standing awayStanding = standingService.findStangingByTeamNameAndLeague(away, league);
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

	private double calculateOddsToWinHome(String home, String away, H2H head, League league) {
		Standing homeStanding = standingService.findStangingByTeamNameAndLeague(home, league);
		Standing awayStanding = standingService.findStangingByTeamNameAndLeague(away, league);
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
				+ away.replace(' ', '+') + "&APIkey=" + apiRepository.findOne((long) 1).getKeyCode();
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
			Integer minutesIn = 0;
			if (eventService.checkIfEventHasEnded(event)) {
				GameToBet game = gameRepository.findByEvent(event);
				game.setActive(false);
				gameRepository.save(game);
			} else {
				GameToBet game = gameRepository.findByEvent(event);
				String status = event.getStatus();
				if (status.equals("HT") || status.equals("")) {
				} else {
					try {
						status = status.replace("'", "");
						if (status.contains("+")) {
							String statusMin = status.substring(0, status.indexOf('+'));
							minutesIn = Integer.parseInt(statusMin);
						} else {
							minutesIn = Integer.parseInt(status.substring(0, status.length() - 1));
						}
						int homeScore = event.getHomeTeamScore();
						int awayScore = event.getAwayTeamScore();
						double mySecretX = 0;
						int scoreDifference = homeScore - awayScore;
						double oddsHome = event.getGame().getOddsToWinHome();
						double oddsAway = event.getGame().getOddsToWinAway();
						double oddsDraw = event.getGame().getOddsToWinDraw();

						if (minutesIn > 80) {
							game.setActive(false);
						} else {
							double homeProbabilityFromTime = 0.00;
							double drawProbabilityFromTime = 0.00;
							if (scoreDifference >= 3) {
								mySecretX= 0.2;
								homeProbabilityFromTime = 0.989;
								drawProbabilityFromTime = 0.01;

							} else if (scoreDifference == 2) {
								mySecretX=0.6;
								homeProbabilityFromTime = 0.95;
								drawProbabilityFromTime = 0.04;
								mySecretX=0.85;
							} else if (scoreDifference == 1) {

								homeProbabilityFromTime = 0.7;
								drawProbabilityFromTime = 0.26;

							} else if (scoreDifference == 0) {
								mySecretX=1.0;
								homeProbabilityFromTime = 0.3 * (game.getOddsToWinHome()
										/ (game.getOddsToWinHome() + game.getOddsToWinAway()));
								drawProbabilityFromTime = 0.7;

							} else if (scoreDifference == -1) {
								mySecretX=0.85;
								homeProbabilityFromTime = 0.04;
								drawProbabilityFromTime = 0.26;

							} else if (scoreDifference == -2) {
								mySecretX=0.6;
								homeProbabilityFromTime = 0.01;
								drawProbabilityFromTime = 0.04;
							} else if (scoreDifference < -3) {
								mySecretX=0.2;
								homeProbabilityFromTime = 0.001;
								drawProbabilityFromTime = 0.01;
							}

							int minutesToBetEnd = 80 - minutesIn;
							double percentOfGameThatIsLeft = minutesToBetEnd / 80;
							double oddsForHome = mySecretX*(game.getOddsToWinHome() * percentOfGameThatIsLeft) + (2.0-mySecretX)*((1 - percentOfGameThatIsLeft) * homeProbabilityFromTime);
							double oddsForDraw =  mySecretX* game.getOddsToWinDraw() * percentOfGameThatIsLeft + (2.0-mySecretX)*((1 - percentOfGameThatIsLeft) * homeProbabilityFromTime);
							double oddsForAway = 1-oddsForHome-oddsForDraw;
							game.setRateHome(generateRate(oddsForHome));
							game.setRateDraw(generateRate(oddsForDraw));
							game.setRateAway(generateRate(oddsForAway));
							gameRepository.save(game);
						}

					} catch (Exception e) {
						System.out.println("Cannot parse: " + status);
					}
				}
			}

		}
	}

	@Override
	public List<GameToBet> findByListOfEvents(List<Event> events) {
		return gameRepository.findByEventIn(events);
	}

}
