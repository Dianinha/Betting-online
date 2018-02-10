package pl.coderslab.web;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.Message;
import pl.coderslab.model.Operation;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.OperationService;
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
	private WalletService walletService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private MessageService messageService;

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
		model.addAttribute("wallet", walletService.findByUser(user));

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

		return "redirect:/user/wallet";
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

	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String paymentWithoutSavingCard(Model model, HttpSession session) {
		model.addAttribute("creditCardInfo", new CreditCardInfo());
		return "/user/payment";
	}

	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String paymentWithoutSavingCardPost(@Valid CreditCardInfo creditCardInfo, BindingResult result, Model model,
			HttpSession session, Authentication authentication) {
		if (result.hasErrors()) {
			return "/user/payment";
		}
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
		}
		try {
			Wallet wallet = walletService.findByUser(user);
			Integer amountInInteger = (Integer) session.getAttribute("fundsToAdd");
			BigDecimal amount = BigDecimal.valueOf(amountInInteger);
			walletService.addFunds(wallet, amount);
			operationService.createAddOperation(wallet, amount, creditCardInfo.getLastFourDigits());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/wallet";
	}

	@RequestMapping(value = "/creditCards", method = RequestMethod.GET)
	public String creditCards(Model model, HttpSession session, Authentication authentication) {
		List<CreditCardInfo> userCreditCards = null;
		try {
			String name = authentication.getName();
			User user = userService.findByUsername(name);
			userCreditCards = creditService.findByUser(user);
		} catch (Exception e) {
		}
		model.addAttribute("creditCards", userCreditCards);
		return "/user/creditCards";
	}

	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public String messages(Model model, HttpSession session, Authentication authentication) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		try {
			model.addAttribute("recieved", messageService.findMessagesByReciever(user));
			model.addAttribute("send", messageService.findMessagesBySender(user));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Did not work");
		}
		return "/user/messages";
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
	public String sendNewMessage(Model model, HttpSession session, Authentication authentication) {
		model.addAttribute("message", new Message());
		return "/user/addMessage";
	}

	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public String sendNewMessagePost(@RequestParam("username") String username, Model model, HttpSession session,
			Authentication authentication, @Valid Message message, BindingResult result) {
		if (result.hasErrors()) {
			return "/user/addMessage";
		}
		User userToSendMessage = null;
		User sender = null;
		try {
			userToSendMessage = userService.findByUsername(username);
		} catch (Exception e) {
			System.out.println("No username found");
			return "/user/addMessage";
		}

		try {
			String name = authentication.getName();
			sender = userService.findByUsername(name);
		} catch (Exception e) {
		}
		try {
			message = messageService.addReciever(message, userToSendMessage);
			message = messageService.addSender(message, sender);
			messageService.sendMessage(message);
		} catch (Exception e) {
			System.out.println("Couldnt send message");
		}

		return "redirect:/user/messages";
	}

	@RequestMapping(value = "/editData", method = RequestMethod.GET)
	public String editUserData(Model model, Authentication authentication) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		model.addAttribute("user", user);
		return "/user/editData";
	}

	@RequestMapping(value = "/editData", method = RequestMethod.POST)
	public String editUserDataPost(@ModelAttribute User user, Authentication authentication) {
		User userFromDb = null;
		try {
			String name = authentication.getName();
			userFromDb = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		userFromDb.setName(user.getName());
		userFromDb.setSurname(user.getSurname());
		userFromDb.setGeneralSubscription(user.isGeneralSubscription());

		if (!userFromDb.getEmail().equals(user.getEmail()) || !userFromDb.getUsername().equals(user.getUsername())) {
			userFromDb.setUsername(user.getUsername());
			userFromDb.setEmail(user.getEmail());
			userService.saveUser(userFromDb);
			return "redirect:/login";
		}

		userService.saveUser(userFromDb);

		return "redirect:/user/menu";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword() {

		return "/user/changePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePasswordPost(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword, Authentication auth, Model model) {

		User userFromDb = null;
		try {
			String name = auth.getName();
			userFromDb = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
			return "redirect:/login";
		}

		if (!confirmNewPassword.equals(newPassword)) {
			model.addAttribute("message", "Your passwords does not match");
			return "/user/changePassword";
		}
		if (!userService.checkPassword(oldPassword, userFromDb)) {
			model.addAttribute("message", "Your password is incorrect");
			return "/user/changePassword";
		}

		userService.changePassword(newPassword, userFromDb);

		return "redirect:/login";
	}

	@RequestMapping(value = "/operationHistory", method = RequestMethod.GET)
	public String operationHistory(Model model, Authentication authentication) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		Wallet wallet = walletService.findByUser(user);
		List<Operation> usersOperations = operationService.findAllOperationByWallet(wallet);
		model.addAttribute("usersOperations", usersOperations);
		return "/user/operationHistory";
	}

}
