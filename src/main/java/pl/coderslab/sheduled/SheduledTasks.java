package pl.coderslab.sheduled;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.coderslab.model.Event;
import pl.coderslab.service.BetService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.FakeService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.StandingService;

@Component
public class SheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;

	@Autowired
	private BetService betService;

	@Autowired
	private StandingService standingService;

	@Autowired
	FakeService faker;

	@Scheduled(cron = "0 18 18 1/1 * ?")
	public void createEvents() {
		LocalDate dateStart = LocalDate.now().plusDays(1);
		String startDate = dateStart.toString();
		LocalDate dateStop = LocalDate.now().plusDays(4);
		String stopDate = dateStop.toString();
		eventService.createEvents(startDate, stopDate);
		List<Event> createdEvents = eventService.findByDateBetween(dateStart, dateStop);
		// standingService.createStandingsOnceForDay();
		gameService.createGamesToBetFromEvents(createdEvents);
		System.out.println("Events and games are created!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	//@Scheduled(fixedRate = 30000)
	//public void updateLiveEvents() {
		//eventService.updateliveEvents();
		//gameService.updateLiveEventsGamesToBet();
		//System.out.println("games upadted");
	//}

	@Scheduled(fixedRate = 30000)
	public void checkBets() {
		betService.checkBetsForTodayGames();
		betService.checkMultiBetsForTodayGames();
		System.out.println("bets updated");
	}

	// @Scheduled(fixedRate = 60000)
	// public void fakeEvents() {
	// eventService.createFakeEvents();
	// List<Event> events = eventService.findByDate(LocalDate.now());
	// gameService.createGamesToBetFromEvents(events);
	// gameService.updateLiveEventsGamesToBet();
	// System.out.println("created fake events");
	// }
//
	@Scheduled(cron = "55 21 00 1/1 * ?")
	public void fakeIt() {
		faker.createFakeEvents();
		faker.createFakeGameToBet();
	}
	@Scheduled(fixedRate = 15000)
	public void fakeItAndSimulateIt() {
		faker.symulateFakeEvents();
		gameService.updateLiveEventsGamesToBet();
	}
	
	
}
