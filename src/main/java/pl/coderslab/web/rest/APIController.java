package pl.coderslab.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.coderslab.dto.EventDto;
import pl.coderslab.dto.UserAddressDto;
import pl.coderslab.model.Address;
import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.User;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.UserService;

@RestController
@RequestMapping("/api")
public class APIController {

	@Autowired
	GameToBetService gameService;

	@Autowired
	EventService eventService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;

	@GetMapping(value = "/activeGamesToBet")
	public ResponseEntity<List<GameToBet>> getActiveGamesToBet(@RequestParam(name = "id", required = false) Long id) {
		if (id != null) {
			List<GameToBet> games = new ArrayList<>();
			games.add(gameService.findById(id));
			return ResponseEntity.ok(games);
		}

		return ResponseEntity.ok(gameService.findActiveGames());
	}

	@RequestMapping(path = "/getCorrespondingEvent/{id}", method = RequestMethod.GET)
	public EventDto getEventById(@PathVariable Long id) {
		Event event = eventService.findById(id);
		return new EventDto(event.getId(), event.getCountry().getName(), event.getLegaue().getName(), event.getDate(),
				event.getStatus(), event.getTime(), event.getHomeTeamName(), event.getGame().getId(),
				event.getHomeTeamScore(), event.getAwayTeamName(), event.getAwayTeamScore(), event.getMatchLive(),
				event.getCategory().getName());
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(path = "/addNewUser", method = RequestMethod.PUT)
	public ResponseEntity addNewUser(@RequestBody @Valid UserAddressDto userAddress) {
		if (userService.checkIfUsernameIsTaken(userAddress.getUsername())) {
			return ResponseEntity.badRequest().body("Username already taken");
		}
		if (userService.checkIfEmailIsTakien(userAddress.getEmail())) {
			return ResponseEntity.badRequest().body("Email already taken");
		}
		
		User user = new User();
		user.setName(userAddress.getName());
		user.setSurname(userAddress.getSurname());
		user.setEmail(userAddress.getEmail());
		user.setUsername(userAddress.getUsername());
		user.setGeneralSubscription(userAddress.isGeneralSubscription());
		user.setPassword(userAddress.getPassword());
		Address address = new Address();
		address.setCountry(userAddress.getCountry());
		address.setCity(userAddress.getCity());
		address.setAdditionalLetterToNumber(userAddress.getAdditionalLetterToNumber());
		address.setFlatNumber(userAddress.getFlatNumber());
		address.setStreetNumber(userAddress.getStreetNumber());
		address.setStreetName(userAddress.getStreetName());
		address.setZipCode(userAddress.getZipCode());
		try {
			user = userService.createUser(user);
			addressService.create(address, user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Creation failed.");
		}
		return ResponseEntity.ok("User and Address created");

	}

}
