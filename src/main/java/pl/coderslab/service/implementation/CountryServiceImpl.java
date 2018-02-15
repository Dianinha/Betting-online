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
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.CountryRepository;
import pl.coderslab.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private APIRepository apiRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Override
	public void createCountries() {
		JSONParser parser = new JSONParser();
		String url = "https://apifootball.com/api/?action=get_countries&APIkey="
				+ apiRepository.findOne((long) 1).getKeyCode();
		try {
			URL getDataFrom = new URL(url);
			URLConnection urlConn = getDataFrom.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONArray jsonCountries = (JSONArray) parser.parse(inputLine);
				// Loop through each item
				for (Object country : jsonCountries) {
					JSONObject countryJson = (JSONObject) country;

					Long id = Long.parseLong((String) countryJson.get("country_id"));

					String name = (String) countryJson.get("country_name");
					Country countryCreated = new Country();
					countryCreated.setId(id);
					countryCreated.setName(name);
					countryRepository.save(countryCreated);
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			LOGGER.error("Bad url in countries", e);
		} catch (IOException e) {
			LOGGER.error("Error in connection in countries", e);
		} catch (ParseException e) {
			LOGGER.error("Error in parsing countries", e);
		}
	}
}
