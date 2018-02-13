package pl.coderslab.sheduled;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.coderslab.model.Event;
import pl.coderslab.service.BetService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;

@Component
public class SheduledTasks {

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;
	
	@Autowired
	private BetService betService;

	@Scheduled(cron = "20 15 10 1/1 * ?")
	public void createEvents() {
		LocalDate dateStart = LocalDate.now();
		String startDate = dateStart.toString();
		LocalDate dateStop = LocalDate.now();
		String stopDate = dateStop.toString();
		eventService.createEvents(startDate, stopDate);
		List<Event> createdEvents = eventService.findByDateBetween(dateStart, dateStop);
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
