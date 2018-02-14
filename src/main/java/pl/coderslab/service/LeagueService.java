package pl.coderslab.service;

import pl.coderslab.model.League;

public interface LeagueService {
	
	/**This creates leagues from API. Please do it only once AFTER You have created all the countries. This is method for setup only.
	 * 
	 */
	public void createLeagues();

	League findById(long id);
}
