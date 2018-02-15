package pl.coderslab.service;

import java.time.LocalDate;
import java.util.List;

import pl.coderslab.model.Event;

public interface EventService {

	/**
	 * Created events for given dates. USE ONCE FOR DAY PLEASE. It does not checks if there is already event in database. 
	 * This method is paring data from API.
	 * @param from
	 * @param to
	 */
	void createEvents(String from, String to);

	/**
	 * Updates todays events.
	 */
	void updateliveEvents();

	/**Finds events from given date
	 * 
	 * @param date
	 * @return List of events that takes place on given date
	 */
	List<Event> findByDate(LocalDate date);

	/**
	 * Finds events that takes place between two dates.
	 * @param from
	 * @param to
	 * @return List of events that takes place during this period of time.
	 */
	List<Event> findByDateBetween(LocalDate from, LocalDate to);
	
	/**
	 * Checks if event has ended.
	 * @param event
	 * @return true if it ended, false otherwise.
	 */
	boolean checkIfEventHasEnded(Event event);

	/**
	 * Creates fake events and changes its score and time in the game
	 */
	void createFakeEvents();
	
	Event findById(long id);

}
