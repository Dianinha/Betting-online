package pl.coderslab.service.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Country;
import pl.coderslab.model.Event;
import pl.coderslab.model.GoalScorer;
import pl.coderslab.model.League;
import pl.coderslab.model.Player;
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.CategoryRepository;
import pl.coderslab.repositories.EventRepository;
import pl.coderslab.repositories.GoalScorerRepository;
import pl.coderslab.repositories.PlayerRepository;
import pl.coderslab.service.EventService;
import pl.coderslab.service.LeagueService;

@Service
public class EventServiceImpl implements EventService {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	PlayerRepository playerRepo;
	@Autowired
	GoalScorerRepository goalScorerRepo;
	@Autowired
	EventRepository eventRepo;
	@Autowired
	APIRepository apiRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	LeagueService leagueService;

	final String beginingUrl = "https://apifootball.com/api/?action=get_events&from=";
	final String toUrl = "&to=";
	String APIUrl = "&APIkey=";

	@Override
	public void createEvents(String from, String to) {
		// Completing URL
		String finalUrl = beginingUrl + from + toUrl + to + APIUrl + apiRepository.findOne(1L).getKeyCode();
		// JSON parser creation:
		JSONParser parser = new JSONParser();

		try {
			BufferedReader in = getJSONReader(finalUrl);
			String inputLine;

			// Reading JSON
			while ((inputLine = in.readLine()) != null) {
				// parsing Array of JSON
				JSONArray JSONEvents = (JSONArray) parser.parse(inputLine);

				// parsing every object
				for (Object jsonEvent : JSONEvents) {

					// Creating JSON object
					JSONObject eventJson = (JSONObject) jsonEvent;

					// creating new event

					Event event = new Event();
					// Id
					Long eventId = Long.parseLong((String) eventJson.get("match_id"));
					event.setId(eventId);

					// League
					Long leagueId = Long.parseLong((String) eventJson.get("league_id"));
					try {
						League eventsLeague = leagueService.findById(leagueId);
						event.setLegaue(eventsLeague);
						if (eventsLeague.getCountry() != null) {
							Country country = eventsLeague.getCountry();
							event.setCountry(country);
						}

					} catch (Exception e) {
						LOGGER.info("Failed attempt to create an event with league without country");
					}

					// Country

					try {
						event.setCategory(categoryRepository.findByName("Football"));
					} catch (Exception e) {
						LOGGER.info("If You see this log it means that You forget to make category Football");
					}

					// Formatting date from String
					String dateInString = (String) eventJson.get("match_date");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate eventDate = LocalDate.parse(dateInString, formatter);
					event.setDate(eventDate);

					// Event status = "FT" - ended, "HF" - half-time or in format /d/d' -> minutes
					// in the game
					event.setStatus((String) eventJson.get("match_status"));

					// Time of the event
					event.setTime((String) eventJson.get("match_time"));

					event.setHomeTeamName((String) eventJson.get("match_hometeam_name"));

					// In try in case of mistakes in API
					try {
						if (!eventJson.get("match_hometeam_score").equals("")) {
							event.setHomeTeamScore(Integer.parseInt((String) eventJson.get("match_hometeam_score")));
						}

					} catch (Exception e) {
						LOGGER.info("Failed to create home score in event");
					}

					// Away Team Name
					event.setAwayTeamName((String) eventJson.get("match_awayteam_name"));

					// In try in case of mistakes in API
					try {
						if (!eventJson.get("match_awayteam_score").toString().equals("")) {
							event.setAwayTeamScore(Integer.parseInt((String) eventJson.get("match_awayteam_score")));
						}
					} catch (Exception e) {
						LOGGER.info("Failed to create half time score in event");
						;
					}
					// In try in case of mistakes in API
					try {
						if (!eventJson.get("match_hometeam_halftime_score").toString().equals("")) {
							event.setHomeTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_hometeam_halftime_score")));
						}
					} catch (Exception e) {
						LOGGER.info("Failed to create home half time score in event");
					}
					// In try in case of mistakes in API
					try {
						if (!eventJson.get("match_awayteam_halftime_score").toString().equals("")) {
							event.setAwayTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_awayteam_halftime_score")));
						}
					} catch (Exception e) {
						LOGGER.info("Failed to create away half time score in event");
					}

					// Is it live?
					event.setMatchLive((String) eventJson.get("match_live"));

					JSONArray jsonArraygoals = (JSONArray) eventJson.get("goalscorer");
					List<GoalScorer> goalScorers = createGoalScorersForTheEvent(jsonArraygoals, eventId);

					// save event
					if (event.getLegaue() != null) {
						event = eventRepo.save(event);
						// Save goals
						saveGoalScorers(goalScorers, eventId);
					}

				}
			}
			in.close();
		} catch (MalformedURLException e) {
			LOGGER.error("URL Error in eventService", e);
		} catch (IOException e) {
			LOGGER.error("Conn Error in eventService", e);
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("Parse Error in eventService", e);
		}

	}

	@Override
	public void createFakeEvents() {
		// Completing URL
		String finalUrl = "http://localhost:5555/results/fakeEventsLive";
		// JSON parser creation:
		JSONParser parser = new JSONParser();

		try {
			BufferedReader in = getJSONReader(finalUrl);
			String inputLine;

			// Reading JSON
			while ((inputLine = in.readLine()) != null) {
				// parsing Array of JSON
				JSONArray JSONEvents = (JSONArray) parser.parse(inputLine);

				// parsing every object
				for (Object jsonEvent : JSONEvents) {

					// Creating JSON object
					JSONObject eventJson = (JSONObject) jsonEvent;

					// creating new event

					Event event = new Event();
					// Id
					Long eventId = (Long) eventJson.get("match_id");
					event.setId(eventId);

					// League
					Long leagueId = (Long) eventJson.get("league_id");
					try {
						League eventsLeague = leagueService.findById(leagueId);
						event.setLegaue(eventsLeague);
						if (eventsLeague.getCountry() != null) {
							Country country = eventsLeague.getCountry();
							event.setCountry(country);
						}

					} catch (Exception e) {
						LOGGER.info("Failed attempt to create an event with league without country");
					}

					// Country

					event.setCategory(categoryRepository.findByName("Football"));

					// Formatting date from String
					String dateInString = (String) eventJson.get("match_date");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate eventDate = LocalDate.parse(dateInString, formatter);
					event.setDate(eventDate);

					// Event status = "FT" - ended, "HF" - half-time or in format /d/d' -> minutes
					// in the game
					event.setStatus((String) eventJson.get("match_status"));

					// Time of the event
					event.setTime((String) eventJson.get("match_time"));

					event.setHomeTeamName((String) eventJson.get("match_hometeam_name"));

					// In try in case of mistakes in API
					Random r = new Random();
					event.setHomeTeamScore(r.nextInt(4));

					// Away Team Name
					event.setAwayTeamName((String) eventJson.get("match_awayteam_name"));

					// In try in case of mistakes in API
					event.setAwayTeamScore(r.nextInt(4));
					// In try in case of mistakes in API

					// Is it live?
					event.setMatchLive((String) eventJson.get("match_live"));

					JSONArray jsonArraygoals = (JSONArray) eventJson.get("goalscorer");
					List<GoalScorer> goalScorers = createGoalScorersForTheEvent(jsonArraygoals, eventId);

					// save event
					if (event.getLegaue() != null) {
						event = eventRepo.save(event);
						// Save goals
						saveGoalScorers(goalScorers, eventId);
					}

				}
			}
			in.close();
		} catch (

		MalformedURLException e) {
			LOGGER.error("URL Error in eventService FAKE", e);
		} catch (IOException e) {
			LOGGER.error("Conn Error in eventService FAKE", e);
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("Parse Error in eventService FAKE", e);
		}

	}

	@Override
	public List<Event> findByDate(LocalDate date) {
		return eventRepo.findAllByDate(date);
	}

	// Method for creating JSON Reader
	private BufferedReader getJSONReader(String url) throws IOException {
		URL getDataFrom = new URL(url);
		URLConnection urlConn = getDataFrom.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		return in;
	}

	// Method for creating goalScorers
	private List<GoalScorer> createGoalScorersForTheEvent(JSONArray jsonArraygoals, long eventId) {
		List<GoalScorer> goalScorers = new ArrayList<>();
		for (Object jsonGoalScorer : jsonArraygoals) {
			JSONObject goalJson = (JSONObject) jsonGoalScorer;
			GoalScorer goalScorer = new GoalScorer();

			// Time of goal
			goalScorer.setTime((String) goalJson.get("time"));
			// Goal id = event id + time of goal
			goalScorer.setId(eventId + goalScorer.getTime());

			// Two homeScorers names but one is empty
			String homeScorer = (String) goalJson.get("home_scorer");
			String awayScorer = (String) goalJson.get("away_scorer");

			// Matching player with score
			if (!homeScorer.equals("")) {
				Player player = checkIfExistsIfNotCreateNew(homeScorer);
				goalScorer.setHomeScorer(player);
			}
			if (!awayScorer.equals("")) {
				Player player = checkIfExistsIfNotCreateNew(awayScorer);
				goalScorer.setAwayScorer(player);
			}

			// Finally setting score
			goalScorer.setScore((String) goalJson.get("score"));
			goalScorers.add(goalScorer);
		}
		return goalScorers;

	}

	private Player checkIfExistsIfNotCreateNew(String playerName) {
		Player player = null;
		player = playerRepo.findByName(playerName);
		if (player == null) {
			player = new Player();
			player.setName(playerName);
			player = playerRepo.save(player);
		}
		return player;
	}

	private void saveGoalScorers(List<GoalScorer> goalScorers, long eventId) {
		for (GoalScorer gs : goalScorers) {
			gs.setEvent(eventRepo.findOne(eventId));
		}
		goalScorerRepo.save(goalScorers);
	}

	@Override
	public List<Event> findByDateBetween(LocalDate from, LocalDate to) {
		return eventRepo.findByDateBetween(from, to);
	}

	@Override
	public void updateliveEvents() {

		LocalDate today = LocalDate.now();
		String date = today.toString();
		String url = beginingUrl + date + toUrl + date + APIUrl + apiRepository.findOne(1L).getKeyCode();
		JSONParser parser = new JSONParser();

		try {
			BufferedReader in = getJSONReader(url);
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonEvent = (JSONArray) parser.parse(inputLine);

				for (Object jsonEvenObject : jsonEvent) {

					JSONObject eventJson = (JSONObject) jsonEvenObject;

					String eventId = (String) eventJson.get("match_id");
					long id = Long.parseLong(eventId);
					if (eventRepo.findOne(id) != null) {

						Event event = eventRepo.findOne(id);
						try {
							event.setStatus((String) eventJson.get("match_status"));
						} catch (Exception e) {
							LOGGER.info("Fail te crete match status in updateLiveEvents");
						}
						event.setCategory(categoryRepository.findByName("Football"));
						event.setTime((String) eventJson.get("match_time"));

						try {
							if (eventJson.get("match_hometeam_score").equals("")) {
								event.setHomeTeamScore(0);
							} else {
								event.setHomeTeamScore(
										Integer.parseInt((String) eventJson.get("match_hometeam_score")));
							}
						} catch (Exception e) {
							LOGGER.info("Fail to parse hometeam score updateLiveEvents");
						}

						try {
							if (eventJson.get("match_awayteam_score").equals("")) {
								event.setAwayTeamScore(0);
							} else {
								event.setAwayTeamScore(
										Integer.parseInt((String) eventJson.get("match_awayteam_score")));
							}

						} catch (Exception e) {
							LOGGER.info("Failed to parse awat team score updateLiveEvents");
						}

						try {
							event.setHomeTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_hometeam_halftime_score")));
						} catch (Exception e) {
							LOGGER.info("Failed to parse half score liveUpdateEvents");
						}
						try {
							event.setAwayTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_awayteam_halftime_score")));
						} catch (Exception e) {
							LOGGER.info("Failed to parse half score liveUpdateEvents");
						}
						JSONArray jsonArraygoals = (JSONArray) eventJson.get("goalscorer");
						List<GoalScorer> goalScorers = createGoalScorersForTheEvent(jsonArraygoals, id);
						eventRepo.save(event);
						saveGoalScorers(goalScorers, id);

					}
				}

			}
			in.close();
		} catch (MalformedURLException e) {
			LOGGER.error("URL Error in eventService UPDATE", e);
		} catch (IOException e) {
			LOGGER.error("Conn Error in eventService UPDATE", e);
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("Parse Error in eventService UPDATE", e);
		}

	}

	@Override
	public boolean checkIfEventHasEnded(Event event) {
		return (event.getStatus().equals("FT")) ? true : false;
	}

	@Override
	public Event findById(long id) {
		return eventRepo.findOne(id);
	}

}
