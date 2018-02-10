package pl.coderslab.serviceImpl;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Country;
import pl.coderslab.model.Event;
import pl.coderslab.model.GoalScorer;
import pl.coderslab.model.League;
import pl.coderslab.model.Player;
import pl.coderslab.repositories.EventRepository;
import pl.coderslab.repositories.GoalScorerRepository;
import pl.coderslab.repositories.PlayerRepository;
import pl.coderslab.service.EventService;
import pl.coderslab.service.LeagueService;

@Service
public class EventServiceImpl implements EventService {

	final String beginingUrl = "https://apifootball.com/api/?action=get_events&from=";
	final String toUrl = "&to=";
	final String leagueUrl = "&league_id=";
	final String APIUrl = "&APIkey=69e25fed4be4381276cb4d5f30e7b2a66a53c71a3f62dcac640e2c1d69f8d1c1";
	final static String pattern = "[1-2][0-9]/d/d-[0,1]/d-[0,1,2,3]/d";

	@Autowired
	PlayerRepository playerRepo;
	@Autowired
	GoalScorerRepository goalScorerRepo;
	@Autowired
	EventRepository eventRepo;
	@Autowired
	LeagueService leagueService;

	@Override
	public void createEvents(String from, String to, League league) {

		String finalUrl = beginingUrl + from + toUrl + to + leagueUrl + league.getId() + APIUrl;
		JSONParser parser = new JSONParser();
		try {
			URL getDataFrom = new URL(finalUrl);
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonEvent = (JSONArray) parser.parse(inputLine);

				for (Object jsonEvenObject : jsonEvent) {
					JSONObject eventJson = (JSONObject) jsonEvenObject;
					Event event = new Event();
					event.setLegaue(league);
					Country country = league.getCountry();
					event.setCountry(country);
					String matchId = (String) eventJson.get("match_id");
					Long id = Long.parseLong(matchId);
					event.setId(id);
					String date = (String) eventJson.get("match_date");
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					// convert String to LocalDate
					LocalDate matchDate = LocalDate.parse(date, formatter);
					event.setDate(matchDate);
					event.setStatus((String) eventJson.get("match_status"));
					event.setTime((String) eventJson.get("match_time"));
					event.setHomeTeamName((String) eventJson.get("match_hometeam_name"));
					if (event.getStatus().equals("FT")) {
						try {
							event.setHomeTeamScore(Integer.parseInt((String) eventJson.get("match_hometeam_score")));
						} catch (Exception e) {
						}

					}
					event.setAwayTeamName((String) eventJson.get("match_awayteam_name"));
					if (event.getStatus().equals("FT")) {

						try {
							event.setAwayTeamScore(Integer.parseInt((String) eventJson.get("match_awayteam_score")));
							event.setHomeTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_hometeam_halftime_score")));
							event.setAwayTeamScoreHalfTime(
									Integer.parseInt((String) eventJson.get("match_awayteam_halftime_score")));
						} catch (Exception e) {
						}

					}

					event.setMatchLive((String) eventJson.get("match_live"));
					List<GoalScorer> goalScorers = new ArrayList<>();
					JSONArray jsonArraygoals = (JSONArray) eventJson.get("goalscorer");
					for (Object jsonGoalScorer : jsonArraygoals) {
						JSONObject goalJson = (JSONObject) jsonGoalScorer;
						GoalScorer goalScorer = new GoalScorer();

						goalScorer.setTime((String) goalJson.get("time"));
						goalScorer.setId(event.getId() + goalScorer.getTime());
						String homeScorer = (String) goalJson.get("home_scorer");
						String awayScorer = (String) goalJson.get("away_scorer");
						if (!homeScorer.equals("")) {
							Player player = null;
							try {
								player = playerRepo.findByName(homeScorer);
							} catch (Exception e) {
							}

							if (player == null) {
								player = new Player();
								player.setName(homeScorer);
								player = playerRepo.save(player);
								goalScorer.setHomeScorer(player);
							} else {
								goalScorer.setHomeScorer(player);
							}

						}
						if (!awayScorer.equals("")) {
							Player player = null;
							try {
								player = playerRepo.findByName(awayScorer);
							} catch (Exception e) {
							}

							if (player == null) {
								player = new Player();
								player.setName(awayScorer);
								player = playerRepo.save(player);
								goalScorer.setAwayScorer(player);
							} else {
								goalScorer.setAwayScorer(player);
							}

						}

						goalScorer.setScore((String) goalJson.get("score"));
						goalScorers.add(goalScorer);
					}
					
					event = eventRepo.save(event);
					
					for (GoalScorer gs : goalScorers) {
						gs.setEvent(eventRepo.findOne(event.getId()));
						System.out.println(gs);
					}
					goalScorerRepo.save(goalScorers);
					System.out.println(event);

					// -------------------

				}
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

	}

	@Override
	public List<Event> liveEvent() {
		List<Event> liveEvents = new ArrayList<>();
		LocalDate today = LocalDate.now();
		String date = today.toString();
		System.out.println(date);
		String url = "https://apifootball.com/api/?action=get_events&from=" + date + "&to=" + date
				+ "&APIkey=69e25fed4be4381276cb4d5f30e7b2a66a53c71a3f62dcac640e2c1d69f8d1c1";
		JSONParser parser = new JSONParser();
		try {
			URL getDataFrom = new URL(url);
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonEvent = (JSONArray) parser.parse(inputLine);

				for (Object jsonEvenObject : jsonEvent) {
					JSONObject eventJson = (JSONObject) jsonEvenObject;

					String matchId = (String) eventJson.get("match_id");

					Long id = Long.parseLong(matchId);
					Event event = eventRepo.findOne(id);
					long leaugueid = Long.parseLong((String) eventJson.get("league_id"));
					League league = leagueService.findById(leaugueid);
					event.setLegaue(league);
					event.setCountry(league.getCountry());
					event.setStatus((String) eventJson.get("match_status"));
					event.setTime((String) eventJson.get("match_time"));
					event.setHomeTeamName((String) eventJson.get("match_hometeam_name"));

					try {
						if (eventJson.get("match_hometeam_score").equals("")) {
							event.setHomeTeamScore(0);
						} else {
							event.setHomeTeamScore(Integer.parseInt((String) eventJson.get("match_hometeam_score")));
						}
					} catch (Exception e) {
					}

					event.setAwayTeamName((String) eventJson.get("match_awayteam_name"));

					try {
						if (eventJson.get("match_awayteam_score").equals("")) {
							event.setAwayTeamScore(0);
						} else {
							event.setAwayTeamScore(Integer.parseInt((String) eventJson.get("match_awayteam_score")));
						}

					} catch (Exception e) {
					}
					try {
						event.setHomeTeamScoreHalfTime(
								Integer.parseInt((String) eventJson.get("match_hometeam_halftime_score")));
					} catch (Exception e) {
					}
					try {
						event.setAwayTeamScoreHalfTime(
								Integer.parseInt((String) eventJson.get("match_awayteam_halftime_score")));
					} catch (Exception e) {
					}

					event.setMatchLive((String) eventJson.get("match_live"));
					List<GoalScorer> goalScorers = new ArrayList<>();
					JSONArray jsonArraygoals = (JSONArray) eventJson.get("goalscorer");
					for (Object jsonGoalScorer : jsonArraygoals) {
						JSONObject goalJson = (JSONObject) jsonGoalScorer;
						GoalScorer goalScorer = new GoalScorer();
						goalScorer.setTime((String) goalJson.get("time"));
						goalScorer.setId(event.getId() + goalScorer.getTime());
						String homeScorer = (String) goalJson.get("home_scorer");
						String awayScorer = (String) goalJson.get("away_scorer");
						if (!homeScorer.equals("")) {
							Player player = null;
							try {
								player = playerRepo.findByName(homeScorer);
							} catch (Exception e) {
							}

							if (player == null) {
								player = new Player();
								player.setName(homeScorer);
								player = playerRepo.save(player);
								goalScorer.setHomeScorer(player);
							} else {
								goalScorer.setHomeScorer(player);
							}

						}
						if (!awayScorer.equals("")) {
							Player player = null;
							try {
								player = playerRepo.findByName(awayScorer);
							} catch (Exception e) {
							}

							if (player == null) {
								player = new Player();
								player.setName(awayScorer);
								player = playerRepo.save(player);
								goalScorer.setAwayScorer(player);
							} else {
								goalScorer.setAwayScorer(player);
							}

						}
						goalScorer.setScore((String) goalJson.get("score"));
						goalScorers.add(goalScorer);
					}
					
					eventRepo.save(event);
					
					liveEvents.add(event);
					for (GoalScorer gs : goalScorers) {
						gs.setEvent(eventRepo.findOne(event.getId()));
						System.out.println(gs);
					}
					goalScorerRepo.save(goalScorers);
					System.out.println(event);

					// -------------------

				}
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("here1");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("here2");
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("here3");
		}

		return liveEvents;
	}

	@Override
	public List<Event> findByDate(LocalDate date) {
		return eventRepo.findAllByDate(date);
	}

}
