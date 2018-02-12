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
import pl.coderslab.repositories.APIRepository;
import pl.coderslab.repositories.CountryRepository;
import pl.coderslab.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService{

	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private APIRepository apiRepository;
	
	@Override
	public Country createCountry() {
		 JSONParser parser = new JSONParser();
		String url = "https://apifootball.com/api/?action=get_countries&APIkey="+apiRepository.findOne((long) 1).getKeyCode();
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
	                    System.out.println(countryCreated);
	                    countryRepository.save(countryCreated);
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
//		JSONTokener jsonTokener = new JSONTokener(getDataFrom.openStream().toString());
//		JSONObject obj = new JSONObject("apifootball.com/api/?action=get_countries");
//		String pageName = obj.getJSONObject("pageInfo").getString("pageName");
//
//		JSONArray arr = obj.getJSONArray("posts");
//		for (int i = 0; i < arr.length(); i++)
//		{
//		    String post_id = arr.getJSONObject(i).getString("post_id");
//		}
	return null;	
	}
}
