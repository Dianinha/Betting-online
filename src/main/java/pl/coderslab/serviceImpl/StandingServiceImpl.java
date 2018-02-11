package pl.coderslab.serviceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;
import pl.coderslab.repositories.StandingRepository;
import pl.coderslab.service.StandingService;

@Service
public class StandingServiceImpl implements StandingService {

	@Autowired
	StandingRepository standingRepo;

	@Override
	public List<Standing> createStandings(League league) {
		String urlBegining = "https://apifootball.com/api/?action=get_standings&league_id=";
		String urlEnding = "&APIkey=69e25fed4be4381276cb4d5f30e7b2a66a53c71a3f62dcac640e2c1d69f8d1c1";

		String finalUrl = urlBegining + league.getId() + urlEnding;
		JSONParser parser = new JSONParser();
		List<Standing> standings = new ArrayList<>();
		try {
			URL getDataFrom = new URL(finalUrl);
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonStandings = (JSONArray) parser.parse(inputLine);

				for (Object standing : jsonStandings) {
					JSONObject standingJson = (JSONObject) standing;
					Standing standingObject = new Standing();

					standingObject.setCountry(league.getCountry());

					standingObject.setLeague(league);
					standingObject.setTeamName((String) standingJson.get("team_name"));
					standingObject
							.setLeaguePosition(Integer.parseInt((String) standingJson.get("overall_league_position")));
					standingObject
							.setMatchesPlayed(Integer.parseInt((String) standingJson.get("overall_league_payed")));
					standingObject.setMatchesWon(Integer.parseInt((String) standingJson.get("overall_league_W")));
					standingObject.setMatchesDraw(Integer.parseInt((String) standingJson.get("overall_league_D")));
					standingObject.setMatchesLost(Integer.parseInt((String) standingJson.get("overall_league_L")));
					standingObject.setGoalsScored(Integer.parseInt((String) standingJson.get("overall_league_GF")));
					standingObject.setGoalsLost(Integer.parseInt((String) standingJson.get("overall_league_GA")));
					standingObject.setPoints(Integer.parseInt((String) standingJson.get("overall_league_PTS")));

					// ------------------------------------------------------------
					standingObject
							.setHomeLeaguePosition(Integer.parseInt((String) standingJson.get("home_league_position")));
					standingObject
							.setHomeMatchesPlayed(Integer.parseInt((String) standingJson.get("home_league_payed")));
					standingObject.setHomeMatchesWon(Integer.parseInt((String) standingJson.get("home_league_W")));
					standingObject.setHomeMatchesDraw(Integer.parseInt((String) standingJson.get("home_league_D")));
					standingObject.setHomeMatchesLost(Integer.parseInt((String) standingJson.get("home_league_L")));
					standingObject.setHomeGoalsScored(Integer.parseInt((String) standingJson.get("home_league_GF")));
					standingObject.setHomeGoalsLost(Integer.parseInt((String) standingJson.get("home_league_GA")));
					standingObject.setHomePoints(Integer.parseInt((String) standingJson.get("home_league_PTS")));
					// --------------------------------------------------------------
					standingObject
							.setAwayLeaguePosition(Integer.parseInt((String) standingJson.get("away_league_position")));
					standingObject
							.setAwayMatchesPlayed(Integer.parseInt((String) standingJson.get("away_league_payed")));
					standingObject.setAwayMatchesWon(Integer.parseInt((String) standingJson.get("away_league_W")));
					standingObject.setAwayMatchesDraw(Integer.parseInt((String) standingJson.get("away_league_D")));
					standingObject.setAwayMatchesLost(Integer.parseInt((String) standingJson.get("away_league_L")));
					standingObject.setAwayGoalsScored(Integer.parseInt((String) standingJson.get("away_league_GF")));
					standingObject.setAwayGoalsLost(Integer.parseInt((String) standingJson.get("away_league_GA")));
					standingObject.setAwayPoints(Integer.parseInt((String) standingJson.get("away_league_PTS")));
					int year = LocalDate.now().getYear();
					int nextyear = year + 1;
					int previousyear = year - 1;
					if (LocalDate.now().isAfter(LocalDate.of(year, 06, 26))) {
						standingObject.setSeason(year + "/" + nextyear + " " + standingObject.getTeamName());
					} else {
						standingObject.setSeason(previousyear + "/" + year + " " + standingObject.getTeamName());
					}

					standings.add(standingObject);

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

		return standings;

	}

	@Override
	public void seeStandings(List<Standing> standings) {

		for (Standing standing : standings) {
			System.out.println(standing);
		}
		System.out.println(standingRepo);
	}

	@Override
	public void saveStandings(List<Standing> standings) {

		for (int i = 0; i < standings.size(); i++) {
			if (standingRepo.findOne(standings.get(i).getId()) != null) {
				Standing standingFromDb = standingRepo.findOne(standings.get(i).getId());
				standingFromDb = merge(standingFromDb, standings.get(i));
			} else {
				standingRepo.save(standings.get(i));
			}
		}

	}

	@Override
	public Standing findStanfingByTeamName(String teamName) {
		return standingRepo.findByTeamName(teamName);
	}

	private Standing merge(Standing standing, Standing newStanding) {
		standing.setLeaguePosition(newStanding.getLeaguePosition());
		standing.setMatchesPlayed(newStanding.getMatchesPlayed());
		standing.setMatchesWon(newStanding.getMatchesWon());
		standing.setMatchesLost(newStanding.getMatchesLost());
		standing.setMatchesDraw(newStanding.getMatchesDraw());
		standing.setGoalsScored(newStanding.getGoalsScored());
		standing.setGoalsLost(newStanding.getGoalsLost());
		standing.setPoints(newStanding.getPoints());

		standing.setHomeLeaguePosition(newStanding.getHomeLeaguePosition());
		standing.setHomeMatchesPlayed(newStanding.getHomeMatchesPlayed());
		standing.setHomeMatchesWon(newStanding.getHomeMatchesWon());
		standing.setHomeMatchesDraw(newStanding.getHomeMatchesDraw());
		standing.setHomeMatchesLost(newStanding.getHomeMatchesLost());
		standing.setHomeGoalsScored(newStanding.getHomeGoalsScored());
		standing.setHomeGoalsLost(newStanding.getHomeGoalsLost());
		standing.setHomePoints(newStanding.getHomePoints());

		standing.setAwayLeaguePosition(newStanding.getAwayLeaguePosition());
		standing.setAwayMatchesPlayed(newStanding.getAwayMatchesPlayed());
		standing.setAwayMatchesWon(newStanding.getAwayMatchesWon());
		standing.setAwayMatchesDraw(newStanding.getAwayMatchesDraw());
		standing.setAwayMatchesLost(newStanding.getAwayMatchesLost());
		standing.setAwayGoalsScored(newStanding.getAwayGoalsScored());
		standing.setAwayGoalsLost(newStanding.getAwayGoalsLost());
		standing.setAwayPoints(newStanding.getAwayPoints());

		return standingRepo.save(standing);

	}
}
