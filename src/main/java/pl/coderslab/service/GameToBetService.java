package pl.coderslab.service;

import java.math.BigDecimal;
import java.util.List;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.service.implementation.GameToBetServiceImpl;

/**
 * For more details please see {@link GameToBetServiceImpl}
 * 
 * @author dianinha
 *
 */
public interface GameToBetService {

	/**
	 * Finds GameToBet by given Event
	 * 
	 * @param event
	 * @return
	 */
	GameToBet findByEvent(Event event);

	/**
	 * Finds GamToBet by id
	 * 
	 * @param id
	 *            long
	 * @return
	 */
	GameToBet findById(long id);

	/**
	 * Create new game to bet from given list of events
	 * 
	 * @param events
	 */
	void createGamesToBetFromEvents(List<Event> events);

	/**
	 * Updates the rates in gamesToBet corresponding to given events
	 * 
	 * @param liveEvents
	 */
	void updateGamesToBet(List<Event> liveEvents);

	/**
	 * Updates all todays games
	 * 
	 */
	void updateLiveEventsGamesToBet();

	/**
	 * Finds list of GameToBet by corresponding list of events
	 * 
	 * @param events
	 * @return
	 */
	List<GameToBet> findByListOfEvents(List<Event> events);

	/**
	 * Retrieves the String what user placed bet on. For display method only.
	 * 
	 * @param game
	 * @param betOn
	 * @return String of what bet is placed in
	 */
	String getTeamNameByBetOn(GameToBet game, String betOn);

	/**
	 * Returns the rate of bet.
	 * 
	 * @param game
	 * @param betOn
	 * @return String
	 */
	BigDecimal getRateByBetOn(GameToBet game, String betOn);

	/**Returns {@link List} of {@link GameToBet} that have parameter "active" set to true
	 * 
	 * @return list of GameToBet
	 */
	List<GameToBet> findActiveGames();

}
