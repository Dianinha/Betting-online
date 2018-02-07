package pl.coderslab.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.model.League;
import pl.coderslab.repositories.LeagueRepository;
import pl.coderslab.service.CountryService;
import pl.coderslab.service.LeagueService;
import pl.coderslab.serviceImpl.StandingServiceImpl;

@Controller
@RequestMapping(value="/results")
public class ResultsController {
	@Autowired
	CountryService countryService;
	
	@Autowired
	LeagueService leagueService;
	
	@Autowired
	private LeagueRepository leagueRepository;

	@RequestMapping(value="/countries")
	@ResponseBody
	public String countries() {
		countryService.createCountry();
		return "Hello countries";
	}
	
	@RequestMapping(value="/leagues")
	@ResponseBody
	public String legaues() {
		leagueService.createLeagues();
		StandingServiceImpl ssi = new StandingServiceImpl();
		League league = leagueRepository.findOne((long) 137);
		ssi.saveStandings(ssi.createStandings(league));
		return "Hello countries";
	}
}
