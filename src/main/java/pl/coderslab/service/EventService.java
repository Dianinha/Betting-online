package pl.coderslab.service;

import java.time.LocalDate;
import java.util.List;

import pl.coderslab.model.Event;
import pl.coderslab.model.League;

public interface EventService {

	void createEvents(String from, String to, League league);

	List<Event> liveEvent();

	List<Event> findByDate(LocalDate date);

}

// from Start date (yyyy-mm-dd)
// to Stop date (yyyy-mm-dd)
// country_id Country ID - if set only leagues from specific country will be
// returned (Optional)
// league_id League ID - if set events from specific league will be returned
// (Optional)
// match_id Match ID - if set only details from specific match will be returned
// (Optional)