package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;

public interface StandingService {
	/**
	 * Creates the standings from database.
	 * 
	 * @param league
	 * @return
	 */
	List<Standing> createStandings(League league);

	/**
	 * Saves given standings
	 * 
	 * @param standings
	 */
	void saveStandings(List<Standing> standings);

	/**
	 * Finds standing by given team name and league
	 * 
	 * @param teamName
	 * @param league
	 * @return
	 */
	Standing findStangingByTeamNameAndLeague(String teamName, League league);

	/**
	 * Please use just once for day. It updates the standings. All of them. Very
	 * resource-intensive.
	 */
	void createStandingsOnceForDay();
}
