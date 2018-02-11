package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.H2H;

public interface GameToBetService {

	public void createGamesToBet();

	GameToBet findByEvent(Event event);

	GameToBet findById(long id);

	void createGamesToBetFromEvents(List<Event> events);

	void updateGamesToBet();



}
