package pl.coderslab.service;

import pl.coderslab.model.League;

public interface LeagueService {
	
	/**This creates leagues from API. Please do it only once AFTER You have created all the countries. This is method for setup only.
	 * 
	 */
	public void createLeagues();

	/**
	 * Finds league by its id
	 * @param id
	 * @return League with corresponding id
	 */
	League findById(long id);
}
