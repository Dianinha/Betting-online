package pl.coderslab.service.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Country;
import pl.coderslab.model.League;
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.CountryRepository;
import pl.coderslab.repositories.LeagueRepository;
import pl.coderslab.service.LeagueService;

@Service
public class LeagueServiceImpl implements LeagueService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	private static final String url = "https://apifootball.com/api/?action=get_leagues&APIkey=";

	@Autowired
	APIRepository apiRepository;

	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private CountryRepository countryRepository;

	/**Creates leagues. If country is null league is not created.
	 * 
	 */
	@Override
	public void createLeagues() {
		JSONParser parser = new JSONParser();
		try {
			URL getDataFrom = new URL(url + apiRepository.findOne(1L).getKeyCode());
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonLeagues = (JSONArray) parser.parse(inputLine);
				// Loop through each item
				for (Object lig : jsonLeagues) {
					JSONObject ligueJson = (JSONObject) lig;

					Long countryId = Long.parseLong((String) ligueJson.get("country_id"));
					Long laeugueId = Long.parseLong((String) ligueJson.get("league_id"));
					String leagueName = (String) ligueJson.get("league_name");
					try {
						Country country = countryRepository.findOne(countryId);
						if (country!= null) {
							League leaugue = new League();
							leaugue.setId(laeugueId);
							leaugue.setName(leagueName);
							leaugue.setCountry(country);
							leagueRepository.save(leaugue);
						}
						else {
							LOGGER.info("League was not created - no country");
						}
					} catch (Exception e) {
						LOGGER.info("League was not created - mistake");
					}
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public League findById(long id) {
		return leagueRepository.findOne(id);
	}
}
