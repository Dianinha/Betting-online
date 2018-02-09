package pl.coderslab.service;

import pl.coderslab.model.League;

public interface LeagueService {
	public void createLeagues();

	League findById(long id);
}
