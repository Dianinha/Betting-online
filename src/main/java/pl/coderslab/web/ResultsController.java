package pl.coderslab.web;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.League;
import pl.coderslab.model.Standing;
import pl.coderslab.repositories.LeagueRepository;
import pl.coderslab.service.CountryService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.LeagueService;
import pl.coderslab.service.StandingService;
import pl.coderslab.serviceImpl.StandingServiceImpl;

@Controller
@RequestMapping(value = "/results")
public class ResultsController {

	@Autowired
	CountryService countryService;

	@Autowired
	LeagueService leagueService;

	@Autowired
	EventService eventService;

	@Autowired
	StandingService ssi;

	@Autowired
	GameToBetService gameService;

	@Autowired
	private LeagueRepository leagueRepository;

	@RequestMapping(value = "/countries")
	@ResponseBody
	public String countries() {
		countryService.createCountry();
		return "Hello countries";
	}

	@RequestMapping(value = "/gameToBet")
	@ResponseBody
	public String games() {
		gameService.createGamesToBet();
		return "Hello games";
	}

	@RequestMapping(value = "/leagues")
	@ResponseBody
	public String legaues() {
		leagueService.createLeagues();

		return "Hello leagues";
	}

	@RequestMapping(value = "/createstandings")
	@ResponseBody
	public String createstandings() {
		League league = leagueRepository.findOne((long) 137);
		List<Standing> stands = ssi.createStandings(league);
		ssi.saveStandings(stands);
		league = leagueRepository.findOne((long) 376);
		stands = ssi.createStandings(league);
		ssi.saveStandings(stands);
		return "Hello standings";
	}

	@RequestMapping(value = "/standings")
	@ResponseBody
	public String standings() {
		// League league = leagueRepository.findOne((long) 137);
		// List<Standing> stands = ssi.createStandings(league);
		// ssi.saveStandings(stands);
		League league = leagueRepository.findOne((long) 376);
		List<Standing> stands = stands = ssi.createStandings(league);
		ssi.saveStandings(stands);
		return "Hello standings";
	}

	@RequestMapping(value = "/events")
	@ResponseBody
	public String events() {
		League league = leagueRepository.findOne((long) 137);
		eventService.createEvents("aaa", "aaa", league);
		return "Hello events";
	}

	@RequestMapping(value = "/tryLive")
	public String tryLive(Model model, HttpSession session) {
		List<Event> liveEvents = eventService.liveEvent();
		model.addAttribute("liveEvents", liveEvents);
		return "/live/eventlive";
	}

	@RequestMapping(value = "/updateResults")
	@ResponseBody
	public String updateResults() {
		List<Event> liveEvents = eventService.liveEvent();
		JSONArray list = new JSONArray();
		for (Event event : liveEvents) {
			JSONObject obj = new JSONObject();
			obj.put(event.getId(), event);
			list.add(obj);
		}
		return list.toJSONString();
	}

	@RequestMapping(value = "/updateJsonResults")
	@ResponseBody
	public JSONArray updateJsonResults() {
		List<Event> liveEvents = eventService.liveEvent();
		JSONArray list = new JSONArray();
		for (Event event : liveEvents) {
			JSONObject obj = new JSONObject();
			obj.put("date", event.getDate().toString());
			obj.put("time", event.getTime());
			League league = event.getLegaue();
			obj.put("leagueName", league.getName());
			obj.put("homeTeamName", event.getHomeTeamName());
			obj.put("awayTeamName", event.getAwayTeamName());
			obj.put("homeTeamScore", event.getHomeTeamScore());
			obj.put("awayTeamScore", event.getAwayTeamScore());
			obj.put("homeTeamScoreHalfTime", event.getHomeTeamScoreHalfTime());
			obj.put("awayTeamScoreHalfTime", event.getAwayTeamScoreHalfTime());
			list.add(obj);
		}
		return list;
	}

	@RequestMapping(value = "/updateHtmlResults")
	@ResponseBody
	public String updateStringResults() {
		String myHtml = "";
		List<Event> liveEvents = eventService.liveEvent();

		for (Event event : liveEvents) {
			String date = event.getDate().toString();
			String time = event.getTime();
			String in = "NOTHING";
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();

			int matchHour = Integer.parseInt(time.substring(0, time.indexOf(':')));
			System.out.println(hour);
			int matchMin = Integer.parseInt(time.substring((time.indexOf(':')) + 1), time.length());

			if (matchHour > hour) {
				in = "NOT STARTED";
			} else if (matchHour == hour) {
				if (matchMin > min) {
					in = "NOT STARTED";
				} else {
					in = event.getStatus();
				}
			} else if (event.getStatus().equals("FT")) {
				in = "FINISHED";
			} else {
				in = event.getStatus();
			}

			String league = event.getLegaue().getName();

			String score = event.getHomeTeamScore() + ":" + event.getAwayTeamScore();

			String teams = event.getHomeTeamName() + " vs. " + event.getAwayTeamName();

			GameToBet game = gameService.findByEvent(event);

			myHtml = myHtml + "<tr><td>" + date + "</td><td>" + time + "</td><td>" + in + "</td><td>" + league
					+ "</td><td>" + score + "</td><td>" + teams + "</td><td>"
					+ "<a href=\"http://localhost:5555/bet/addBet?gameId=" + game.getId()
					+ "&betOn=home\"><button type=\"button\">" + game.getRateHome() + "</button></a>" + "</td><td>"
					+ "<a href=\"http://localhost:5555/bet/addBet?gameId=" + game.getId()
					+ "&betOn=draw\"><button type=\"button\">" + game.getRateDraw() + "</button></a>" + "</td><td>"
					+ "<a href=\"http://localhost:5555/bet/addBet?gameId=" + game.getId()
					+ "&betOn=away\"><button type=\"button\">" + game.getRateAway() + "</button></a>" + "</td></tr>";

		}
		return myHtml;
	}

}
