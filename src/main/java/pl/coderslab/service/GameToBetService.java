package pl.coderslab.service;

import java.math.BigDecimal;
import java.util.List;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;

public interface GameToBetService {

	// public void createGamesToBet();

	GameToBet findByEvent(Event event);

	GameToBet findById(long id);

	void createGamesToBetFromEvents(List<Event> events);

	void updateGamesToBet(List<Event> liveEvents);

	void updateLiveEventsGamesToBet();

	List<GameToBet> findByListOfEvents(List<Event> events);

	String getTeamNameByBetOn(GameToBet game, String betOn);

	BigDecimal getRateByBetOn(GameToBet game, String betOn);

}
