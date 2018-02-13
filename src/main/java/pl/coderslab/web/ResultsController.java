package pl.coderslab.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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

	
	@RequestMapping(value = "/leagues")
	@ResponseBody
	public String legaues() {
		leagueService.createLeagues();

		return "Hello leagues";
	}

	

	@RequestMapping(value = "/standings")
	@ResponseBody
	public String standings() {
		ssi.createStandingsOnceForDay();
		return "Hello standings";
	}

	@RequestMapping(value = "/events")
	@ResponseBody
	public String events() {
		// eventService.createEvents("2018-02-11", "2018-02-11");
		List<Event> events = eventService.findByDateBetween(LocalDate.now(), LocalDate.now());
		for (Event event : events) {
			System.out.println(event);
		}
		return "Hello events";
	}

	@RequestMapping(value = "/tryLive")
	public String tryLive(Model model, HttpSession session) {
		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		model.addAttribute("liveEvents", liveEvents);
		return "/live/eventlive";
	}



//	@RequestMapping(value = "/updateJsonResults")
//	@ResponseBody
//	public JSONArray updateJsonResults() {
//		List<Event> liveEvents = eventService.liveEvent();
//		JSONArray list = new JSONArray();
//		for (Event event : liveEvents) {
//			JSONObject obj = new JSONObject();
//			obj.put("date", event.getDate().toString());
//			obj.put("time", event.getTime());
//			League league = event.getLegaue();
//			obj.put("leagueName", league.getName());
//			obj.put("homeTeamName", event.getHomeTeamName());
//			obj.put("awayTeamName", event.getAwayTeamName());
//			obj.put("homeTeamScore", event.getHomeTeamScore());
//			obj.put("awayTeamScore", event.getAwayTeamScore());
//			obj.put("homeTeamScoreHalfTime", event.getHomeTeamScoreHalfTime());
//			obj.put("awayTeamScoreHalfTime", event.getAwayTeamScoreHalfTime());
//			list.add(obj);
//		}
//		return list;
//	}

	@RequestMapping(value = "/updateHtmlResults")
	@ResponseBody
	public String updateStringResults() {
		String myHtml = "";
		LocalDate today = LocalDate.now();
		List<Event> liveEvents = eventService.findByDateBetween(today, today);

		for (Event event : liveEvents) {
			String date = event.getDate().toString();
			String time = event.getTime();
			String in = "NOTHING";
			time = time.trim();
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			int matchHour = Integer.parseInt(time.substring(0, time.indexOf(':')));
			String why = (time.substring(((time.indexOf(':')) + 1), time.length()));
			int matchMin = 0;
			try {
				matchMin = Integer.parseInt(time.substring((time.indexOf(':')) + 1), time.length());
			} catch (Exception e) {
				if (why.equals("45")) {
					matchMin = 45;
				}
			}

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

	@RequestMapping(value = "/homePageResults")
	@ResponseBody
	public String getResultsForHomePage() {
		String myHtml = "";
		LocalDate today = LocalDate.now();
		List<Event> liveEvents = eventService.findByDateBetween(today, today);
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {

				return o1.getTime().compareTo(o2.getTime());
			}
		});
		int size = 4;
		if (size > liveEvents.size()) {
			size = liveEvents.size();
		}
		for (int i = 0; i < size; i++) {
			Event event = liveEvents.get(i);
			String time = event.getTime();
			String in = "NOTHING";
			time = time.trim();
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			int matchHour = Integer.parseInt(time.substring(0, time.indexOf(':')));
			String why = (time.substring(((time.indexOf(':')) + 1), time.length()));
			int matchMin = 0;
			try {
				matchMin = Integer.parseInt(time.substring((time.indexOf(':')) + 1), time.length());
			} catch (Exception e) {
				if (why.equals("45")) {
					matchMin = 45;
				}
			}

			if (matchHour > hour) {
				in = time;
			} else if (matchHour == hour) {
				if (matchMin > min) {
					in = time;
				} else {
					in = event.getStatus();
				}
			} else if (event.getStatus().equals("FT")) {
				in = "FINISHED";

			} else {
				in = event.getStatus();
			}

			String score = event.getHomeTeamScore() + ":" + event.getAwayTeamScore();

			GameToBet game = gameService.findByEvent(event);

			if (!in.equals("FINISHED")) {
				myHtml = myHtml + "<div class=\"row\"><div class=\"col-1\"></div>\n"
						+ "	 <div class=\"col-10 match mb-1\">\n" + "	 <div class=\"row\">\n"
						+ "	 <div class=\"col-2\">\n" + "	 <p class=\"now my-auto py-2 font-weight-bold\">" + score
						+ "</p>\n" + "	 </div>\n" + "	 <div class=\"col-4\">\n" + "	 <p class=\"now my-auto py-2\">"
						+ event.getHomeTeamName() + "<span style=\"color: #001021\"> vs </span>"
						+ event.getAwayTeamName() + " </p>\n" + "	 </div>\n" + "	 <div class=\"col-2\">\n"
						+ "	 <p class=\"my-auto py-2\" style=\"color: #001021\">" + in + " </p>\n" + "	 </div>\n"
						+ "	 <div class=\"col-4\">\n" + "	 <p class=\"my-auto py-2\">\n"
						+ "	 <span style=\"color: #001021\"> Home: </span><span\n" + "	class=\"now font-weight-bold\">"
						+ game.getRateHome() + " </span> <span\n" + "	 style=\"color: #001021\">Draw: </span><span\n"
						+ "	 class=\"now font-weight-bold\">" + game.getRateDraw() + "</span><span\n"
						+ "	 style=\"color: #001021\"> Away: </span><span\n" + "	 class=\"now font-weight-bold\">"
						+ game.getRateAway() + " </span>\n" + "	 </p>\n" + "	 </div>\n" + "	 </div>\n"
						+ "	 </div><div class=\"col-1\"></div>\n" + "			</div>";

			} else {
				if (size + 1 <= liveEvents.size()) {
					size = size + 1;
				}
			}
		}
		return myHtml;
	}

	@RequestMapping(value = "/userLiveResults")
	@ResponseBody
	public String getResultsForUserPage() {
		String myHtml = "";
		LocalDate today = LocalDate.now();
		List<Event> liveEvents = eventService.findByDateBetween(today, today);
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {

				return o1.getTime().compareTo(o2.getTime());
			}
		});
		for (int i = 0; i < liveEvents.size(); i++) {
			Event event = liveEvents.get(i);
			String time = event.getTime();
			String in = "NOTHING";
			time = time.trim();
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			int matchHour = Integer.parseInt(time.substring(0, time.indexOf(':')));
			String why = (time.substring(((time.indexOf(':')) + 1), time.length()));
			int matchMin = 0;
			try {
				matchMin = Integer.parseInt(time.substring((time.indexOf(':')) + 1), time.length());
			} catch (Exception e) {
				if (why.equals("45")) {
					matchMin = 45;
				}
			}

			if (matchHour > hour) {
				in = time;
			} else if (matchHour == hour) {
				if (matchMin > min) {
					in = time;
				} else {
					in = event.getStatus();
				}
			} else if (event.getStatus().equals("FT")) {
				in = "FINISHED";

			} else {
				in = event.getStatus();
			}

			String score = event.getHomeTeamScore() + ":" + event.getAwayTeamScore();

			GameToBet game = gameService.findByEvent(event);
			if (!in.equals("FINISHED")) {
				myHtml = myHtml + "<tr><td>" + event.getCategory().getName()
						+ "</td><td><span style=\"color: #df2935\">" + event.getLegaue().getName()
						+ "</span></td><td><span style=\"color: #df2935\">" + event.getHomeTeamName()
						+ "</span> vs. <span style=\"color: #df2935\">" + event.getAwayTeamName() + "</span></td><td>"
						+ event.getHomeTeamScore() + ":" + event.getAwayTeamScore() + "</td><td>" + in
						+ "</td><td style=\"background-color: #df2935\">"
						+ "<a href=\"http://localhost:5555/bet/add?gameId="+event.getId()+"&betOn=home\" style=\"color: #ffffff !important; font-weight: bold;\">" + event.getGame().getRateHome()
						+ "</a></td><td style=\"background-color: #001021\"><a href=\"http://localhost:5555/bet/add?gameId="+event.getId()+"&betOn=draw\" style=\"color: #ffffff !important; font-weight: bold\">"
						+ event.getGame().getRateDraw()
						+ "</a></td><td style=\"background-color: #df2935\"><a href=\"http://localhost:5555/bet/add?gameId="+event.getId()+"&betOn=away\" style=\"color: #ffffff !important; font-weight: bold\">"
						+ event.getGame().getRateAway() + "</a></td></tr>";

			}

			
		}
		return myHtml;
	}
}
