package pl.coderslab.sheduled;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.coderslab.model.Event;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;

@Component
public class SheduledTasks {

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;

	@Scheduled(cron = "0 0 13 1/1 * ?")
	public void createEvents() {
		LocalDate dateStart = LocalDate.now().plusDays(1);
		String startDate = dateStart.toString();
		LocalDate dateStop = LocalDate.now().plusDays(2);
		String stopDate = dateStop.toString();
		eventService.createEvents(startDate, stopDate);
		List<Event> createdEvents = eventService.findByDateBetween(dateStart, dateStop);
		gameService.createGamesToBetFromEvents(createdEvents);
		System.out.println("Events and games are created!!!!!!!!!!!!!!!!!!!!!!!!");

	}

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void updateLiveEvents() {
		eventService.updateliveEvents();
		//gameService.updateGamesToBet();
	}
}
