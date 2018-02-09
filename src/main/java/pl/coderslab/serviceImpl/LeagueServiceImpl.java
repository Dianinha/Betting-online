package pl.coderslab.serviceImpl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Country;
import pl.coderslab.model.League;
import pl.coderslab.repositories.CountryRepository;
import pl.coderslab.repositories.LeagueRepository;
import pl.coderslab.service.LeagueService;

@Service
public class LeagueServiceImpl implements LeagueService {

	private static final String url = "https://apifootball.com/api/?action=get_leagues&APIkey=69e25fed4be4381276cb4d5f30e7b2a66a53c71a3f62dcac640e2c1d69f8d1c1";
	
	@Autowired
	private LeagueRepository leagueRepository;
	@Autowired
	private CountryRepository countryRepository;

	@Override
	public void createLeagues() {
		JSONParser parser = new JSONParser();
		try {
			URL getDataFrom = new URL(url);
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
						League leaugue = new League();
						leaugue.setId(laeugueId);
						leaugue.setName(leagueName);
						leaugue.setCountry(country);
						System.out.println(leaugue);
						leagueRepository.save(leaugue);
					} catch (Exception e) {
						e.printStackTrace();
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
