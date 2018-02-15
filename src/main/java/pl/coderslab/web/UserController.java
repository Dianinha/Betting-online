package pl.coderslab.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Event;
import pl.coderslab.model.User;
import pl.coderslab.repositories.EventRepository;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.UserService;

/**
 * This is controller for basic {@link User} actions as User main page.
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");
	
	@Autowired
	private UserService userService;

	@Autowired
	AddressService addressService;

	@Autowired
	EventRepository eventService;

	@RequestMapping(path = { "", "/menu" }, method = RequestMethod.GET)
	public String mainUserPageGet(HttpSession session, Authentication authentication, Model model) {
		User user = userService.getAuthenticatedUser(authentication);
		if (user == null) {
			return "redirect:/login";
		}
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		return "/user/menu";
	}

	@RequestMapping(path = { "", "/menu" }, method = RequestMethod.POST)
	public String mainUserPage(HttpSession session, Authentication authentication, Model model) {
		User user = userService.getAuthenticatedUser(authentication);
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		return "/user/menu";
	}

	@RequestMapping(path = "/addToObserved", method = RequestMethod.POST)
	public String addToOberved(Authentication authentication, Model model, @RequestParam("eventId") long eventId) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<Event> observedEvents = user.getUserObservedGames();
		if (observedEvents == null) {
			observedEvents = new HashSet<>();
		}
		observedEvents.add(eventService.findOne(eventId));
		user.setUserObservedGames(observedEvents);
		userService.saveUser(user);
		return "redirect:/user";
	}

	@RequestMapping(path = "/removeFromObserved", method = RequestMethod.POST)
	public String removeFromOberved(Authentication authentication, Model model, @RequestParam("eventId") long eventId) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<Event> observedEvents = user.getUserObservedGames();
		try {
			observedEvents.remove(eventService.findOne(eventId));
		} catch (Exception e) {
			LOGGER.info("Failed remove observed events");
		}

		user.setUserObservedGames(observedEvents);
		userService.saveUser(user);
		return "redirect:/user";
	}

}
