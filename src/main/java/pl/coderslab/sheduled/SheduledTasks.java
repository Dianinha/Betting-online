package pl.coderslab.sheduled;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.coderslab.model.Event;
import pl.coderslab.model.Standing;
import pl.coderslab.service.BetService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.StandingService;

@Component
public class SheduledTasks {

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;
	
	@Autowired
	private BetService betService;
	
	@Autowired
	private StandingService standingService;

	@Scheduled(cron = "30 11 11 1/1 * ?")
	public void createEvents() {
		LocalDate dateStart = LocalDate.now();
		String startDate = dateStart.toString();
		LocalDate dateStop = LocalDate.now();
		String stopDate = dateStop.toString();
		eventService.createEvents(startDate, stopDate);
		List<Event> createdEvents = eventService.findByDateBetween(dateStart, dateStop);
		standingService.createStandingsOnceForDay();
		gameService.createGamesToBetFromEvents(createdEvents);
		System.out.println("Events and games are created!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void updateLiveEvents() {
		eventService.updateliveEvents();
		gameService.updateLiveEventsGamesToBet();
		}
	
	@Scheduled(cron = "0 0/5 * 1/1 * ?")
	public void checkBets() {
		betService.checkBetsForTodayGames();
		System.out.println("bets updated");
		}
}
