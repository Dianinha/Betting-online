package pl.coderslab.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
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

/**
 * Implementation for {@link GameToBetService}
 * 
 * @author dianinha
 *
 */
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

	/**
	 * Creating {@link GameToBet} from {@link List} of {@link Event}
	 * 
	 */
	@Override
	public void createGamesToBetFromEvents(List<Event> events) {
		List<Event> futureEvents = new ArrayList<>();
		for (Event event : events) {
				GameToBet game = new GameToBet();
				if (event.getStatus().equals("FT")) {
					game.setActive(false);
				} else {
					game.setActive(true);
				}
				game.setId(event.getId());
				game.setEvent(event);
				gameRepository.save(game);
				event.setGame(game);
				Event improvedEvent = eventRepository.save(event);
				futureEvents.add(improvedEvent);
			}
		updateGamesToBet(futureEvents);

	}

	/**
	 * Finding {@link GameToBet} by {@link Event}
	 * 
	 */
	@Override
	public GameToBet findByEvent(Event event) {
		return gameRepository.findByEvent(event);
	}

	/**
	 * Find {@link GameToBet} by id
	 * 
	 */
	@Override
	public GameToBet findById(long id) {
		return gameRepository.findOne(id);
	}

	/**
	 * Updates already created {@link GameToBet}
	 * <p>
	 * If Event has ended there is no longer possibility to place bets If there is
	 * no country for the event do nothing
	 * </p>
	 * 
	 * 
	 */
	@Override
	public void updateGamesToBet(List<Event> liveEvents) {

		for (Event event : liveEvents) {
			GameToBet game = gameRepository.findByEvent(event);
			if (eventService.checkIfEventHasEnded(event)) {
				game.setActive(false);
				gameRepository.save(game);
			} else {
				if (event.getLegaue().getCountry() != null) {
					gameRepository.save(recalculateOdds(game, event));
				}
			}

		}

	}

	/**
	 * Recalculating odds for {@link GameToBet}
	 * 
	 * @param game
	 * @param event
	 * @return
	 */
	private GameToBet recalculateOdds(GameToBet game, Event event) {
		String homeTeamName = event.getHomeTeamName();
		String awayTeamName = event.getAwayTeamName();
		Standing homeStanding = null;
		Standing awayStanding = null;
		try {
			homeStanding = standingService.findStangingByTeamNameAndLeague(homeTeamName, event.getLegaue());
			awayStanding = standingService.findStangingByTeamNameAndLeague(awayTeamName, event.getLegaue());
		} catch (Exception e) {
			System.out.println("Unable to find standing for: " + homeTeamName + " or " + awayTeamName);
		}

		H2H headToHead = createH2H(homeTeamName, awayTeamName);
		game.setOddsToWinDraw(calculateOddsToDraw(homeTeamName, awayTeamName, headToHead, event.getLegaue()));
		game.setOddsToWinHome(calculateOddsToWinHome(homeTeamName, awayTeamName, headToHead, event.getLegaue()));
		game.setOddsToWinAway(1 - game.getOddsToWinDraw() - game.getOddsToWinHome());
		game.setRateHome(generateRate(game.getOddsToWinHome()));
		game.setRateAway(generateRate(game.getOddsToWinAway()));
		game.setRateDraw(generateRate(game.getOddsToWinDraw()));
		System.out.println(game);
		return game;
	}

	/**
	 * Generates how much can be won for 1 PLN from odd.
	 * 
	 * <p>
	 * The rate cannot be lower than 1.00 and bigger than 41.00
	 * </p>
	 * 
	 * @param odd
	 * @return
	 */
	private BigDecimal generateRate(double odd) {
		double y = 1 / odd * 0.92;
		if (y < 1) {
			return BigDecimal.valueOf(1.00);
		} else if (y > 41) {
			return BigDecimal.valueOf(41.00);
		} else {
			return BigDecimal.valueOf(y);
		}

	}

	/**
	 * Calculates what are basis odd to match end in draw. This is my own method.
	 * 
	 * <p>
	 * Sometimes there are problems like the team have not played with each other.
	 * That is why constHome and contsAway are created, so I will not divide by
	 * zero.
	 * </p>
	 * 
	 * <p>
	 * The algorithm is goes like that: IF there was less than 1 game (in league)
	 * for any of the teams saved in my database then I have no data. So the draw
	 * rate is 0.25 as typical draw rate.
	 * 
	 * IF less than 7 matched where played by any of the Teams I just get numbers
	 * from H2H (if I have any)
	 * 
	 * IF I have decent data I add: weight: 0.1 draws for Home team divided by whole
	 * matches played from league standing weight: 0.1 draws for Away team divided
	 * by whole matches played from league standing weight 0.15 draws in home played
	 * games for home team divided by matched played in home weight 0.15 draws in
	 * away played games for away team divided by matched played in away weight 0.15
	 * from H2H number of draws for Home team divided by number of last game played
	 * by Home (should be 10) weight 0.15 from H2H number of draws for Away team
	 * divided by number of last game played by Away (should be 10) weight 0.15
	 * number of draws between two teams divided by 5 (in my H2H i get last 5 games)
	 * 0.0125 which is calculated 0.05 multiplied by 0.25 which is constant for
	 * draws
	 * </p>
	 * 
	 * @param home
	 * @param away
	 * @param head
	 * @param league
	 * @return
	 */
	private double calculateOddsToDraw(String home, String away, H2H head, League league) {
		Standing homeStanding = standingService.findStangingByTeamNameAndLeague(home, league);
		Standing awayStanding = standingService.findStangingByTeamNameAndLeague(away, league);
		double odds = 0.25;

		int constHome = head.getLastHomeGamesNumber();
		int constAway = head.getLastAwayGameNumber();
		if (constHome == 0) {
			constHome = 1;
		}
		if (constAway == 0) {
			constAway = 1;
		}
		if (homeStanding.getMatchesPlayed() < 1 || constHome < 2 || constAway < 2
				|| awayStanding.getMatchesPlayed() < 1) {

			odds = 0.25;
			// odds = 0.3 * head.getNumberOfDrawHome() / constHome + 0.3 *
			// head.getNumberOfDrawAway() / constAway
			// + 0.3 * head.getNumberOfDrawBetweenTeams() / 5 + 0.025;

		} else if (homeStanding.getMatchesPlayed() < 7 || awayStanding.getMatchesPlayed() < 7) {
			odds = 0.3 * head.getNumberOfDrawHome() / constHome + 0.3 * head.getNumberOfDrawAway() / constAway
					+ 0.3 * head.getNumberOfDrawBetweenTeams() / 5 + 0.025;
		} else {
			odds = 0.1 * homeStanding.getMatchesDraw() / homeStanding.getMatchesPlayed()
					+ 0.1 * awayStanding.getMatchesDraw() / awayStanding.getMatchesPlayed()
					+ 0.15 * homeStanding.getHomeMatchesDraw() / homeStanding.getHomeMatchesPlayed()
					+ 0.15 * awayStanding.getAwayMatchesDraw() / awayStanding.getAwayMatchesPlayed()
					+ 0.15 * head.getNumberOfDrawHome() / constHome + 0.15 * head.getNumberOfDrawAway() / constAway
					+ 0.15 * head.getNumberOfDrawBetweenTeams() / 5 + 0.0125;

		}
		return odds;
	}

	/**
	 * This method calculate odd for Home team to win.
	 * <p>
	 * Sometimes there are problems like the team have not played with each other.
	 * That is why constHome and contsAway are created, so I will not divide by
	 * zero.
	 * </p>
	 * If I have sufficient data I calculate if: weight 0.05 home all won matches in
	 * league divided by all played weight 0.05 away all lost matches divided by
	 * away all played weight 0.2 home matches won home in league divided by all
	 * played in home weight 0.2 away matches lost in away in league divided by all
	 * played in away weight 0.15 from H2H number of last wins for home divided by
	 * number of games (should be 10) weight 0.15 from H2H number of last loses for
	 * away divided by number of games (should be 10) weight 0.15 number of times
	 * that home team won with away team in last 5 games 0.0125 which is calculated
	 * 0.05 multiplied by 0.25 which is constant
	 * 
	 * 
	 * @param home
	 * @param away
	 * @param head
	 * @param league
	 * @return
	 */
	private double calculateOddsToWinHome(String home, String away, H2H head, League league) {
		Standing homeStanding = standingService.findStangingByTeamNameAndLeague(home, league);
		Standing awayStanding = standingService.findStangingByTeamNameAndLeague(away, league);
		double odds = 0.375;
		int constHome = head.getLastHomeGamesNumber();
		int constAway = head.getLastAwayGameNumber();
		if (constHome == 0) {
			constHome = 1;
		}
		if (constAway == 0) {
			constAway = 1;
		}
		if (homeStanding.getMatchesPlayed() < 1 || constHome < 2 || constAway < 2
				|| awayStanding.getMatchesPlayed() < 1) {
			odds = 0.375;
		} else if (homeStanding.getMatchesPlayed() < 7 || awayStanding.getMatchesPlayed() < 7) {
			odds = 0.3 * head.getNumberOfWinsHome() / constHome + 0.3 * head.getNumberOfLosesAway() / constAway
					+ 0.3 * head.getNumberOfWinsHomevsAAway() / 5 + 0.025;
		} else {
			odds = 0.05 * homeStanding.getMatchesWon() / homeStanding.getMatchesPlayed()
					+ 0.05 * awayStanding.getMatchesLost() / awayStanding.getMatchesPlayed()
					+ 0.2 * homeStanding.getHomeMatchesWon() / homeStanding.getHomeMatchesPlayed()
					+ 0.2 * awayStanding.getAwayMatchesLost() / awayStanding.getAwayMatchesPlayed()
					+ 0.15 * head.getNumberOfWinsHome() / constHome + 0.15 * head.getNumberOfLosesAway() / constAway
					+ 0.15 * head.getNumberOfWinsHomevsAAway() / 5 + 0.0125;

		}
		return odds;

	}

	/**
	 * Creating H2H from jSON
	 * 
	 * @param home
	 * @param away
	 * @return
	 */
	private H2H createH2H(String home, String away) {
		H2H result = new H2H();
		home = home.replace("'", "");
		away = away.replace("'", "");
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.setNumberOfDrawBetweenTeams(numberOfDrawCounter);
		result.setNumberOfDrawHome(numberOfDrawHomeCounter);
		result.setNumberOfDrawAway(numberOfDrawAwayCounter);
		result.setLastAwayGameNumber(numberOfGamesAway);
		result.setLastHomeGamesNumber(numberOfGamesHome);
		result.setNumberOfLosesAway(numberofLostLastGamesAway);
		result.setNumberOfWinsHome(numberofWonLastGamesHome);
		result.setNumberOfWinsHomevsAAway(numberofWonLastGamesHomeVSAway);

		return result;

	}

	/**
	 * Changes the rates in get according to the game score.
	 * 
	 */
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
							minutesIn = Integer.parseInt(statusMin.trim());
						} else {
							minutesIn = Integer.parseInt(status.substring(0, status.length()));
						}
						int homeScore = event.getHomeTeamScore();
						int awayScore = event.getAwayTeamScore();
						double mySecretX = 0;
						int scoreDifference = homeScore - awayScore;

						if (minutesIn > 80) {
							game.setActive(false);
							gameRepository.save(game);
						} else {
							double homeProbabilityFromTime = 0.00;
							double drawProbabilityFromTime = 0.00;
							/**
							 * Depending on score I have different constants that represents the weights of
							 * probabilities mySecretX is very secret and I cannot talk about it. Sorry.
							 * OK... so it fixes the problem that occurs if there is like 10 minutes in the
							 * game and one team already scored like 3 goals. So the probability from just
							 * standings and H2H stops matter and the actual score is more important. Not
							 * perfect but still it works.
							 * 
							 */
							if (scoreDifference >= 3) {
								mySecretX = 0.2;
								homeProbabilityFromTime = 0.989;
								drawProbabilityFromTime = 0.01;

							} else if (scoreDifference == 2) {
								mySecretX = 0.6;
								homeProbabilityFromTime = 0.95;
								drawProbabilityFromTime = 0.04;
								
							} else if (scoreDifference == 1) {
								mySecretX = 0.85;
								homeProbabilityFromTime = 0.7;
								drawProbabilityFromTime = 0.26;

							} else if (scoreDifference == 0) {
								mySecretX = 1.0;
								homeProbabilityFromTime = 0.3 * (game.getOddsToWinHome()
										/ (game.getOddsToWinHome() + game.getOddsToWinAway()));
								drawProbabilityFromTime = 0.7;

							} else if (scoreDifference == -1) {
								mySecretX = 0.85;
								homeProbabilityFromTime = 0.04;
								drawProbabilityFromTime = 0.26;

							} else if (scoreDifference == -2) {
								mySecretX = 0.6;
								homeProbabilityFromTime = 0.01;
								drawProbabilityFromTime = 0.04;
							} else if (scoreDifference <= -3) {
								mySecretX = 0.2;
								homeProbabilityFromTime = 0.001;
								drawProbabilityFromTime = 0.01;
							}
							int minutesToBetEnd = 80 - minutesIn;
							double percentOfGameThatIsLeft = (double) (minutesToBetEnd) / 80.00;
							double oddsForHome = mySecretX * (game.getOddsToWinHome() * percentOfGameThatIsLeft)
									+ (2.0 - mySecretX) * ((1 - percentOfGameThatIsLeft) * homeProbabilityFromTime);
							double oddsForDraw = mySecretX * game.getOddsToWinDraw() * percentOfGameThatIsLeft
									+ (2.0 - mySecretX) * ((1 - percentOfGameThatIsLeft) * drawProbabilityFromTime);
							double oddsForAway = 1 - oddsForHome - oddsForDraw;
							game.setRateHome(generateRate(oddsForHome));
							game.setRateDraw(generateRate(oddsForDraw));
							game.setRateAway(generateRate(oddsForAway));
							gameRepository.save(game);
						}

					} catch (Exception e) {
					}
				}
			}

		}
	}

	/**
	 * Finds {@link List} of {@link GameToBet} from list of {@link Event}
	 * 
	 */
	@Override
	public List<GameToBet> findByListOfEvents(List<Event> events) {
		return gameRepository.findByEventIn(events);
	}

	/**
	 * Retrieves the String what user placed bet on. For display method only.
	 * 
	 */
	@Override
	public String getTeamNameByBetOn(GameToBet game, String betOn) {
		if (betOn.equals("home")) {
			return "home in game : " + game.getEvent().getHomeTeamName() + " vs. " + game.getEvent().getAwayTeamName();
		} else if (betOn.equals("draw")) {
			return "draw in game: " + game.getEvent().getHomeTeamName() + " vs. " + game.getEvent().getAwayTeamName();

		} else if (betOn.equals("away")) {
			return "away in game: " + game.getEvent().getHomeTeamName() + " vs. " + game.getEvent().getAwayTeamName();
		} else {
			return "Bet unrecognized";
		}

	}

	/**
	 * Returns the rate of bet.
	 * 
	 */
	@Override
	public BigDecimal getRateByBetOn(GameToBet game, String betOn) {
		if (betOn.equals("home")) {
			return game.getRateHome();
		} else if (betOn.equals("draw")) {
			return game.getRateDraw();
		} else if (betOn.equals("away")) {
			return game.getRateAway();
		} else {
			return BigDecimal.valueOf(0);
		}
	}

}
