package pl.coderslab.web;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;
import pl.coderslab.repositories.RoleRepository;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.UserService;

/**
 * This controller is for home page and login, register actions
 * 
 * @author dianinha
 *
 */
@Controller
public class HomePageController {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	UserService userService;

	// TO BE DELETED:
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AddressService addressService;

	/**
	 * Displays home page
	 * 
	 * @return
	 */
	@RequestMapping(path = { "", "/home" })
	public String homePage() {
		return "index";
	}

	/**
	 * Log in page
	 * 
	 * @return
	 */
	@RequestMapping(path = "/login")
	public String log() {
		return "/access/login";
	}

	/**
	 * Allows User to register
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerNewUser(Model model) {
		model.addAttribute("user", new User());
		return "/access/register";
	}

	/**
	 * Process registration
	 * 
	 * @param model
	 * @param user
	 * @param result
	 * @param session
	 * @param over18
	 * @param agreement
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registration(Model model, @Valid User user, BindingResult result, HttpSession session,
			@RequestParam(value = "over18", required = false) String over18,
			@RequestParam(value = "agreeToCon", required = false) String agreement) {
		if (user.getPasswordConfirmed() != null) {
			if (!user.getPasswordConfirmed().equals(user.getPassword())) {
				model.addAttribute("message", "Passwords do not match");
				return "/access/register";
			}

		} else {
			model.addAttribute("message", "Please confirm password");
			return "/access/register";
		}
		if (result.hasErrors()) {
			return "/access/register";
		}
		if (over18 == null) {
			model.addAttribute("failureMessage", "You have to be at least 18 years old to register.");
			return "/access/register";
		}
		if (agreement == null) {
			model.addAttribute("failureMessage", "Please agree to our site conditions");
			return "/access/register";
		}
		try {
			Set<User> friends = new HashSet<>();
			friends.add(userService.findByUsername("service"));
			user.setFriends(friends);
			User savedUser = userService.createUser(user);
			session.setAttribute("user", savedUser);
			return "redirect:/registerAddress";
		} catch (Exception e) {
			if (userService.findByEmail(user.getEmail()) != null) {
				model.addAttribute("failureMessage", "User with this e-mail is already registered");
				LOGGER.info("User tried to create account with taken email.");
				return "/access/register";
			} else if (userService.findByUsername(user.getUsername()) != null) {
				model.addAttribute("failureMessage", "This username is already taken. Please pick another one.");
				LOGGER.info("User tried to create account with taken username.");
				return "/access/register";
			}

		}
		return "redirect:/failure";
	}

	/**
	 * Address registration
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/registerAddress", method = RequestMethod.GET)
	public String registerAddress(Model model) {
		model.addAttribute("address", new Address());
		return "/access/registerAddress";
	}

	/**
	 * Processing address registration
	 * 
	 * @param model
	 * @param address
	 * @param result
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/registerAddress", method = RequestMethod.POST)
	public String registerAddressPost(Model model, @Valid Address address, BindingResult result, HttpSession session) {
		if (result.hasErrors()) {
			return "/access/registerAddress";
		}
		try {
			User user = (User) session.getAttribute("user");
			Address userAddress = addressService.create(address, user);
			user.setAddress(userAddress);
			userService.saveUser(user);
			return "redirect:/success";

		} catch (Exception e) {
			LOGGER.info("Failed attempt to create address.");
		}
		return "redirect:/failure";
	}

	/**
	 * Success page
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String test(Model model, HttpSession session) {
		return "/access/success";
	}

	/**
	 * Failure page
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/failure", method = RequestMethod.GET)
	public String test2(Model model) {
		return "failure";
	}

}
