package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;

public interface StandingService {

	public List<Standing> createStandings(League league);
	
	public void seeStandings(List<Standing> standings);
	
	public void saveStandings(List<Standing> standings);
}
