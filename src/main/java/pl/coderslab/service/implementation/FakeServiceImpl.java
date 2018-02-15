package pl.coderslab.service.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.repositories.EventRepository;
import pl.coderslab.service.EventService;
import pl.coderslab.service.FakeService;
import pl.coderslab.service.GameToBetService;

@Service
public class FakeServiceImpl implements FakeService {

	@Autowired
	EventService eventService;

	@Autowired
	GameToBetService gameService;

	@Autowired
	EventRepository eventRepository;

	@Override
	public void createFakeEvents() {
		long[] pickedEventsArr = { 255294, 255875, 255923, 256138, 255920 };
		for (int i = 0; i < pickedEventsArr.length; i++) {
			Event event = eventService.findById(pickedEventsArr[i]);
			event.setId(5 + i);
			event.setDate(LocalDate.now());
			int hour = LocalDateTime.now().getHour();
			int min = LocalDateTime.now().getMinute();
			event.setTime(hour + ":" + min);
			event.setStatus("1'");
			event.setAwayTeamScore(0);
			event.setHomeTeamScore(0);
			eventRepository.save(event);
		}

	}

	@Override
	public void symulateFakeEvents() {
		Event[] events = { eventRepository.findOne(5L), eventRepository.findOne(6L), eventRepository.findOne(7L),
				eventRepository.findOne(8L), eventRepository.findOne(9L) };
		Random r = new Random();
		for (Event event : events) {
			if (!event.getStatus().equals("FT")) {
				String timeIn = event.getStatus().substring(0, event.getStatus().length() - 1);
				Integer timeInInt = Integer.parseInt(timeIn);
				if (timeInInt >= 80) {
					event.setStatus("FT");
				} else {
					timeInInt = timeInInt + 10;
					event.setStatus(timeInInt + "'");
					int goal = r.nextInt(100) + 1;
					GameToBet game = event.getGame();
					if (goal < game.getOddsToWinHome() * 100) {
						event.setHomeTeamScore(event.getHomeTeamScore() + 1);
					}
					goal = r.nextInt(100) + 1;
					if (goal < game.getOddsToWinAway() * 100) {
						event.setAwayTeamScore(event.getAwayTeamScore() + 1);
					}
				}
				eventRepository.save(event);

			}
		}

	}

	@Override
	public void createFakeGameToBet() {
		List<Event> events = new ArrayList<>();
		events.add(eventRepository.findOne(5L));
		events.add(eventRepository.findOne(6L));
		events.add(eventRepository.findOne(7L));
		events.add(eventRepository.findOne(8L));
		events.add(eventRepository.findOne(9L));
		gameService.createGamesToBetFromEvents(events);

	}

}
