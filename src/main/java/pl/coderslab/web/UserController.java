package pl.coderslab.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import pl.coderslab.model.Message;
import pl.coderslab.model.Operation;
import pl.coderslab.model.Request;
import pl.coderslab.model.User;
import pl.coderslab.model.UserSimple;
import pl.coderslab.model.Wallet;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.RequestService;
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

	@Autowired
	private RequestService requestService;

	@RequestMapping(path = "/menu")
	public String menu() {
		return "/user/menu";
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String maimUserPageGet(HttpSession session, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		return "/user/menu";
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public String maimUserPage(HttpSession session, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		return "/user/menu";
	}

	@RequestMapping(value = "/wallet", method = RequestMethod.GET)
	public String wallet(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		model.addAttribute("wallet", walletService.findByUser(user));

		return "/user/wallet";
	}

	@RequestMapping(value = "/addCreditCard", method = RequestMethod.GET)
	public String creditCardAdd(Model model, Authentication authentication) {
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
		if (userCreditCards.size() != 0) {
			model.addAttribute("creditCards", userCreditCards);
		}

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
			wallet = walletService.addFunds(wallet, amount);
			operationService.createAddOperation(wallet, amount, creditCardInfo.getLastFourDigits());
			session.setAttribute("currentFunds", wallet.getAmount());
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
		model.addAttribute("user", new UserSimple(user));
		return "/user/editData";
	}

	@RequestMapping(value = "/editData", method = RequestMethod.POST)
	public String editUserDataPost(@Valid UserSimple user, BindingResult result, Authentication authentication) {
		User userFromDb = null;
		try {
			String name = authentication.getName();
			userFromDb = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}

		if (result.hasErrors()) {
			return "/user/editData";
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

	@RequestMapping(value = "/payWith", method = RequestMethod.GET)
	public String payWithCard(Model model, Authentication authentication, @RequestParam("id") long id,
			HttpSession session) {
		model.addAttribute("creditCard", creditService.findById(id));
		session.setAttribute("creditCardId", id);

		return "/user/creditCardConfirmation";
	}

	@RequestMapping(value = "/payWith", method = RequestMethod.POST)
	public String payWithCardPost(Model model, Authentication authentication, HttpSession session,
			@RequestParam("cvv") int cvv) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		CreditCardInfo creditCard = creditService.findById((Long) session.getAttribute("creditCardId"));
		if (creditCard.getCvv() != cvv) {
			model.addAttribute("creditCard", creditCard);
			model.addAttribute("message", "Authentication failed.");
			return "/user/creditCardConfirmation";
		}
		try {
			Wallet wallet = walletService.findByUser(user);
			Integer amountInInteger = (Integer) session.getAttribute("fundsToAdd");
			BigDecimal amount = BigDecimal.valueOf(amountInInteger);
			wallet = walletService.addFunds(wallet, amount);
			operationService.createAddOperation(wallet, amount, creditCard.getLastFourDigits());
			session.setAttribute("currentFunds", wallet.getAmount());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/wallet";
	}

	@RequestMapping(value = "/deleteCreditCard", method = RequestMethod.GET)
	public String deleteCreditCard(Model model, Authentication authentication, HttpSession session,
			@RequestParam("cardId") long id) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		boolean flag = creditService.deleteById(id);
		if (flag) {
			return "redirect:/user/wallet";
		} else {
			return "redirect:user/creditCards";
		}

	}

	@RequestMapping(value = "/addFriends", method = RequestMethod.GET)
	public String addFriends(Model model, Authentication authentication, HttpSession session) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		Set<User> friends = user.getFriends();
		model.addAttribute("friends", friends);

		return "user/addFriends";
	}

	@RequestMapping(value = "/addFriends", method = RequestMethod.POST)
	public String addFriendsPost(Model model, Authentication authentication,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email) {
		User user = null;
		try {
			String name = authentication.getName();
			user = userService.findByUsername(name);
		} catch (Exception e) {
			System.out.println("Auth failed");
		}
		Set<User> friends = user.getFriends();
		model.addAttribute("friends", friends);
		System.out.println(username);
		System.out.println(email);
		if (email != null) {
			model.addAttribute("searchResults", userService.findByEmailStartsWith(email));
		}
		if (username != null && !username.equals("")) {
			model.addAttribute("searchResults", userService.findByUSernameStartsWith(username));
		}
		return "user/addFriends";
	}

	@RequestMapping(value = "/deleteFriend", method = RequestMethod.GET)
	public String deleteFriends(Model model, Authentication authentication, @RequestParam("username") String username) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<User> friends = user.getFriends();
		User userToDelete = userService.findByUsername(username);
		friends.remove(userToDelete);
		userToDelete.getFriends().remove(user);
		userService.saveUser(user);
		userService.saveUser(userToDelete);
		model.addAttribute("friends", friends);
		return "redirect:/user/addFriends";
	}

	@RequestMapping(value = "/sendInvite", method = RequestMethod.GET)
	public String sendInvite(Model model, Authentication authentication, @RequestParam("username") String username) {
		User user = userService.getAuthenticatedUser(authentication);
		User userToInvite = userService.findByUsername(username);
		Message message = new Message();
		message.setSender(user);
		List<User> recievers = new ArrayList<>();
		recievers.add(userToInvite);
		message.setRecievers(recievers);
		message.setTitle("Friend request from: " + user.getUsername());
		message.setContent("User " + user.getUsername()
				+ " wants to be on Your friends list. Would You accept? Check Your invites to accept or discard the invite");
		messageService.sendMessage(message);
		Request request = new Request();
		request.setStatus(true);
		request.setSender(user);
		request.setReciever(userToInvite);
		requestService.createRequest(request);

		return "redirect:/user/addFriends";
	}

	@RequestMapping(value = "/invites", method = RequestMethod.GET)
	public String invites(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		if (!requestService.findByUser(user).isEmpty()) {
			model.addAttribute("invites", requestService.findByUser(user));
		}

		return "user/invites";
	}

	@RequestMapping(value = "/acceptInvite", method = RequestMethod.GET)
	public String acceptInvite(Model model, @RequestParam("invite") long id) {
		Request request = requestService.findById(id);
		requestService.acceptRequest(request);
		User reciever = request.getReciever();
		User sender = request.getSender();
		Message message = new Message();
		message.setTitle("Invitation accepted");
		message.setContent("Your invite has been accepted: " + reciever.getUsername()
				+ " is Your new friends. You can now send messages to each other and invite to group bets.");
		message.setSender(reciever);
		List<User> recievers = new ArrayList<>();
		recievers.add(sender);
		message.setRecievers(recievers);
		messageService.sendMessage(message);
		return "redirect:/user/invites";
	}

	@RequestMapping(value = "/discardInvite", method = RequestMethod.GET)
	public String discardInvite(Model model, @RequestParam("invite") long id) {
		requestService.discardRequest(requestService.findById(id));
		return "redirect:/user/invites";
	}
	
	@RequestMapping(value = "/friendsList", method = RequestMethod.GET)
	public String friendsList(Model model, Authentication authentication, HttpSession session) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<User> friends = user.getFriends();
		model.addAttribute("friends", friends);

		return "user/friendsList";
	}

}
