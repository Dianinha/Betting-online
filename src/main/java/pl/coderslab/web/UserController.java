package pl.coderslab.web;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.User;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private CreditCardService creditService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private WalletService walletrService;

	@RequestMapping(path = "/menu")
	public String menu() {
		return "/user/menu";
	}

	@RequestMapping(value = "/wallet", method = RequestMethod.GET)
	public String wallet(Model model, Authentication authentication) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
		}
		model.addAttribute("wallet", walletrService.findByUser(user));

		return "/user/wallet";
	}

	@RequestMapping(value = "/addCreditCard", method = RequestMethod.GET)
	public String creditCardAdd(Model model) {
		model.addAttribute("creditCardInfo", new CreditCardInfo());
		return "/user/addCreditCard";
	}

	@RequestMapping(value = "/addCreditCard", method = RequestMethod.POST)
	public String creditCardAddPost(@Valid CreditCardInfo creditCardInfo, BindingResult result, HttpSession session,
			Authentication authentication) {
		if (result.hasErrors()) {
			return "/user/addCreditCard";
		}
		try {
			String name = authentication.getName();
			User user = userService.findByUsername(name);
			creditService.create(creditCardInfo, user);
		} catch (Exception e) {
		}

		return "/user/wallet";
	}

	@RequestMapping(path = "/addFunds")
	public String addFunds(@RequestParam("val") int val, HttpSession session) {
		session.setAttribute("fundsToAdd", val);
		return "redirect:/user/creditCardOption";
	}

	@RequestMapping(path = "/funds")
	public String buy() {
		return "/user/fundsOption";
	}

	@RequestMapping(path = "/creditCardOption")
	public String creditCardOption(HttpSession session, Model model, Authentication authentication) {
		List<CreditCardInfo> userCreditCards = null;
		try {
			String name = authentication.getName();
			User user = userService.findByUsername(name);
			userCreditCards = creditService.findByUser(user);
		} catch (Exception e) {
		}
		model.addAttribute("creditCards", userCreditCards);
		return "/user/creditCardOption";
	}

}
