package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;

public interface StandingService {

	 List<Standing> createStandings(League league);
	
	
	 void saveStandings(List<Standing> standings);
	 
	 Standing findStanfingByTeamName(String teamName);
	 
	 void createStandingsOnceForDay();
}
