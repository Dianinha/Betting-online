package pl.coderslab.service;

import java.time.LocalDate;
import java.util.List;

import pl.coderslab.model.Event;

public interface EventService {

	void createEvents(String from, String to);

	//List<Event> liveEvent();
	
	void updateliveEvents();

	List<Event> findByDate(LocalDate date);

	List<Event> findByDateBetween(LocalDate from, LocalDate to);
	
	boolean checkIfEventHasEnded(Event event);

}
