package pl.coderslab.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.coderslab.model.User;
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

	@Autowired
	private UserService userService;

	@Autowired
	AddressService addressService;

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

}
