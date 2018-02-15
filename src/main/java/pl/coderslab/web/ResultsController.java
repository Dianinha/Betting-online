package pl.coderslab.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.service.BetService;
import pl.coderslab.service.CountryService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.LeagueService;
import pl.coderslab.service.StandingService;
import pl.coderslab.service.UserService;

/**
 * This controller manages results for other pages. User should not access it.
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/results")
public class ResultsController {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	CountryService countryService;

	@Autowired
	LeagueService leagueService;

	@Autowired
	UserService userService;

	@Autowired
	EventService eventService;

	@Autowired
	StandingService ssi;

	@Autowired
	GameToBetService gameService;

	@Autowired
	BetService betService;

	/**
	 * Create countries from API If You have just set up the application, please go
	 * to this address and create countries in database.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/countries")
	@ResponseBody
	public String countries() {
		countryService.createCountries();
		return "Hello countries";
	}

	/**
	 * Create leagues from API. If You have just set up the application, please go
	 * to this address and create countries in database.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/leagues")
	@ResponseBody
	public String legaues() {
		leagueService.createLeagues();

		return "Hello leagues";
	}

	@RequestMapping(value = "/tryLive")
	public String tryLive(Model model, HttpSession session) {
		return "/live/eventlive";
	}

	@RequestMapping(value = "/fakeEventsLive")
	@ResponseBody
	public JSONArray updateJsonResults() {
		LocalDate date = LocalDate.of(2018, 02, 13);
		long counter = 1;
		Random r = new Random();
		List<Event> liveEvents = eventService.findByDate(date);
		JSONArray list = new JSONArray();
		for (Event event : liveEvents) {
			JSONObject obj = new JSONObject();
			try {

				obj.put("match_id", counter);
				counter++;
				obj.put("league_id", event.getLegaue().getId());
				obj.put("match_date", LocalDate.now().toString());
				String status = r.nextInt(90) + "'";
				obj.put("match_status", status);
				int hour = LocalDateTime.now().getHour();
				String matchHour = hour + ":01";
				obj.put("match_time", matchHour);
				obj.put("match_hometeam_name", event.getHomeTeamName());
				obj.put("match_awayteam_name", event.getAwayTeamName());

				int homeScore = r.nextInt(5);
				int awayScore = r.nextInt(5);

				obj.put("match_hometeam_score", homeScore);
				obj.put("match_awayteam_score", awayScore);

				obj.put("match_live", event.getMatchLive());
				JSONArray jsonArraygoals = (JSONArray) new JSONArray();
				obj.put("goalscorer", jsonArraygoals);
				list.add(obj);

			} catch (Exception e) {
				LOGGER.info("Failed to create fake event.");
			}
		}
		return list;
	}

	@RequestMapping(value = "/updateHtmlResults")
	@ResponseBody
	public String updateStringResults() {
		String myHtml = "";
		LocalDate today = LocalDate.now().plusDays(3);
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
				LOGGER.info("Failed to parse String to Integer in updateStringResults method in Results Controller");
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

	/**
	 * This is method for updating home page result. This returns HTML code due to
	 * me not being able to have ajax method with jSON and Thymeleaf in one view. I
	 * did not want to waste time for looking for solution, so I just put the code
	 * in html.
	 * 
	 * @return
	 */
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
				LOGGER.info("Failed to parse String to Integer in getResultsForHomePage method in Results Controller");
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

	/**
	 * This updates user section "Live Results" on User main page.
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userLiveResults")
	@ResponseBody
	public String getResultsForUserPage() {
		String myHtml = "";
		LocalDate today = LocalDate.now();
		List<Event> liveEvents = eventService.findByDateBetween(today, today);
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				if (o1.getDate().compareTo(o2.getDate()) == 0) {
					if (o1.getTime().compareTo(o2.getTime()) == 0) {
						return o1.getHomeTeamName().compareTo(o2.getHomeTeamName());
					}
					return o1.getTime().compareTo(o2.getTime());
				}
				return o1.getDate().compareTo(o2.getDate());

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
			String betButtons = "";
			if (game.isActive()) {
				betButtons = "<td style=\"background-color: #df2935\">"
						+ "<a href=\"http://localhost:5555/bet/add?gameId=" + event.getId()
						+ "&betOn=home\" style=\"color: #ffffff !important; font-weight: bold;\">"
						+ event.getGame().getRateHome()
						+ "</a></td><td style=\"background-color: #001021\"><a href=\"http://localhost:5555/bet/add?gameId="
						+ event.getId() + "&betOn=draw\" style=\"color: #ffffff !important; font-weight: bold\">"
						+ event.getGame().getRateDraw()
						+ "</a></td><td style=\"background-color: #df2935\"><a href=\"http://localhost:5555/bet/add?gameId="
						+ event.getId() + "&betOn=away\" style=\"color: #ffffff !important; font-weight: bold\">"
						+ event.getGame().getRateAway() + "</a></td>";
			} else {
				betButtons = "<td></td><td></td><td></td>";
			}
			if (!in.equals("FINISHED")) {
				myHtml = myHtml + "<tr><td>" + event.getCategory().getName()
						+ "</td><td><span style=\"color: #df2935\">" + event.getLegaue().getName()
						+ "</span></td><td><span style=\"color: #df2935\">" + event.getHomeTeamName()
						+ "</span> vs. <span style=\"color: #df2935\">" + event.getAwayTeamName() + "</span></td><td>"
						+ event.getHomeTeamScore() + ":" + event.getAwayTeamScore() + "</td><td>" + in + "</td>"
						+ betButtons + "</tr>";

			}

		}
		return myHtml;
	}

	@RequestMapping(value = "/userLiveResultsPopularBets")
	@ResponseBody
	public String getResultsForUserPagePopularBets() {
		String myHtml = "";
		LocalDate today = LocalDate.now();
		LocalDate fewDaysFromNow = LocalDate.now().plusDays(3);
		List<Event> liveEvents = eventService.findByDateBetween(today, fewDaysFromNow);
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				if (o1.getDate().compareTo(o2.getDate()) == 0) {
					if (o1.getTime().compareTo(o2.getTime()) == 0) {
						return o1.getHomeTeamName().compareTo(o2.getHomeTeamName());
					}
					return o1.getTime().compareTo(o2.getTime());
				}
				return o1.getDate().compareTo(o2.getDate());

			}
		});
		for (int i = 0; i < liveEvents.size(); i++) {
			Event event = liveEvents.get(i);
			String time = event.getTime();
			String in = "NOTHING";
			time = time.trim();

			GameToBet game = gameService.findByEvent(event);
			String betButtons = "";
			try {
				if (game.isActive()) {
					betButtons = "<td style=\"background-color: #df2935\">"
							+ "<a href=\"http://localhost:5555/bet/add?gameId=" + event.getId()
							+ "&betOn=home\" style=\"color: #ffffff !important; font-weight: bold;\">"
							+ event.getGame().getRateHome()
							+ "</a></td><td style=\"background-color: #001021\"><a href=\"http://localhost:5555/bet/add?gameId="
							+ event.getId() + "&betOn=draw\" style=\"color: #ffffff !important; font-weight: bold\">"
							+ event.getGame().getRateDraw()
							+ "</a></td><td style=\"background-color: #df2935\"><a href=\"http://localhost:5555/bet/add?gameId="
							+ event.getId() + "&betOn=away\" style=\"color: #ffffff !important; font-weight: bold\">"
							+ event.getGame().getRateAway() + "</a></td>";
				} else {
					betButtons = "<td></td><td></td><td></td>";
				}
				if (!in.equals("FINISHED")) {
					myHtml = myHtml + "<tr><td><form action=\"/user/addToObserved?eventId=" + event.getId()
							+ "\" method=\"post\"> <div> <input type=\"submit\" value=\"+\" class=\"btn btn-outline-success\" style=\"font-size:10px;\"/> </div> </form></td><td>"
							+ event.getCategory().getName() + "</td><td><span style=\"color: #df2935\">"
							+ event.getLegaue().getName() + "</span></td><td><span style=\"color: #df2935\">"
							+ event.getHomeTeamName() + "</span> vs. <span style=\"color: #df2935\">"
							+ event.getAwayTeamName() + "</span></td><td>" + event.getHomeTeamScore() + ":"
							+ event.getAwayTeamScore() + "</td><td>" + event.getDate() + "</td><td>" + time + "</td>"
							+ betButtons + "</tr>";

				}
			} catch (Exception e) {
				LOGGER.info("Failed to create html in getResultsForUserPagePopularBets method in Results Controller");
			}

		}
		return myHtml;
	}

	@RequestMapping(value = "/userLiveResultsObserved")
	@ResponseBody
	public String getResultsForUserPageObserved(Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		List<Event> events = new ArrayList<>(user.getUserObservedGames());
		String myHtml = "";
		Collections.sort(events, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				if (o1.getDate().compareTo(o2.getDate()) == 0) {
					if (o1.getTime().compareTo(o2.getTime()) == 0) {
						return o1.getHomeTeamName().compareTo(o2.getHomeTeamName());
					}
					return o1.getTime().compareTo(o2.getTime());
				}
				return o1.getDate().compareTo(o2.getDate());

			}
		});
		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);
			String time = event.getTime();
			String in = "NOTHING";
			time = time.trim();

			GameToBet game = gameService.findByEvent(event);
			String betButtons = "";
			try {
				if (game.isActive()) {
					betButtons = "<td style=\"background-color: #df2935\">"
							+ "<a href=\"http://localhost:5555/bet/add?gameId=" + event.getId()
							+ "&betOn=home\" style=\"color: #ffffff !important; font-weight: bold;\">"
							+ event.getGame().getRateHome()
							+ "</a></td><td style=\"background-color: #001021\"><a href=\"http://localhost:5555/bet/add?gameId="
							+ event.getId() + "&betOn=draw\" style=\"color: #ffffff !important; font-weight: bold\">"
							+ event.getGame().getRateDraw()
							+ "</a></td><td style=\"background-color: #df2935\"><a href=\"http://localhost:5555/bet/add?gameId="
							+ event.getId() + "&betOn=away\" style=\"color: #ffffff !important; font-weight: bold\">"
							+ event.getGame().getRateAway() + "</a></td>";
				} else {
					betButtons = "<td></td><td></td><td></td>";
				}
				if (!in.equals("FINISHED")) {
					myHtml = myHtml + "<tr><td><form action=\"/user/removeFromObserved?eventId=" + event.getId()
							+ "\" method=\"post\"> <div> <input type=\"submit\" value=\"-\" class=\"btn btn-outline-success\" style=\"font-size:10px;\"/> </div> </form></td><td>"
							+ event.getCategory().getName() + "</td><td><span style=\"color: #df2935\">"
							+ event.getLegaue().getName() + "</span></td><td><span style=\"color: #df2935\">"
							+ event.getHomeTeamName() + "</span> vs. <span style=\"color: #df2935\">"
							+ event.getAwayTeamName() + "</span></td><td>" + event.getHomeTeamScore() + ":"
							+ event.getAwayTeamScore() + "</td><td>" + event.getDate() + "</td><td>" + time + "</td>"
							+ betButtons + "</tr>";

				}
			} catch (Exception e) {
				LOGGER.info("Failed to create html in getResultsForUserPageObserved method in Results Controller");
			}

		}
		return myHtml;
	}

	@RequestMapping(value = "/userLiveResultsActiveBets")
	@ResponseBody
	public String getResultsForUserPageBets(Authentication auth) {
		String myHtml = "";
		User user = userService.getAuthenticatedUser(auth);

		List<SingleBet> userSingles = betService.findBetsByUserAndStatus(BetStatus.PLACED, user);
		List<MultipleBet> userMultipleBets = betService.findMultipleBetsByUserAndStatus(BetStatus.PLACED, user);
		List<GroupBet> userGroupBet = betService.findGroupBetByUser(user);
		Set<SingleBet> joinedBets = new HashSet<>();
		joinedBets.addAll(userSingles);
		for (MultipleBet multipleBet : userMultipleBets) {
			joinedBets.addAll(multipleBet.getBets());
		}
		for (GroupBet groupBet : userGroupBet) {
			joinedBets.addAll(groupBet.getBet());
		}
		Set<Event> events = new HashSet<>();
		for (SingleBet singleBet : joinedBets) {

			GameToBet game = singleBet.getGame();
			Event event = game.getEvent();
			events.add(event);
		}
		List<Event> eventsUserHasBetsOn = new ArrayList<>(events);
		Collections.sort(eventsUserHasBetsOn, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				if (o1.getDate().compareTo(o2.getDate()) == 0) {
					if (o1.getTime().compareTo(o2.getTime()) == 0) {
						return o1.getHomeTeamName().compareTo(o2.getHomeTeamName());
					}
					return o1.getTime().compareTo(o2.getTime());
				}
				return o1.getDate().compareTo(o2.getDate());

			}
		});
		for (Event event2 : eventsUserHasBetsOn) {
			GameToBet game2 = event2.getGame();
			if (!eventService.checkIfEventHasEnded(game2.getEvent())) {
				String time = event2.getTime();
				if (!event2.getStatus().equals("")) {
					time = event2.getStatus();
				}
				myHtml = myHtml + "<tr><td>" + event2.getCategory().getName() + "</td><td>"
						+ event2.getLegaue().getName() + "</td><td><span style=\"color: #df2935\">"
						+ event2.getHomeTeamName() + "</span> vs. <span style=\"color: #df2935\">"
						+ event2.getAwayTeamName() + "</span></td><td>" + event2.getHomeTeamScore() + ":"
						+ event2.getAwayTeamScore() + "</td><td>" + time + "</td></tr>";

			}

		}

		return myHtml;
	}

}
