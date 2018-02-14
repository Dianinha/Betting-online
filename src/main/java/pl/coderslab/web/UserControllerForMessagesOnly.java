package pl.coderslab.web;

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

import pl.coderslab.model.Message;
import pl.coderslab.model.User;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.repositories.MultipleBetRepository;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.BetService;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.RequestService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

@Controller
@RequestMapping(value = "/user")
public class UserControllerForMessagesOnly {

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

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;

	@Autowired
	private BetService betService;

	@Autowired
	private MultipleBetRepository multiBetRepository;

	@Autowired
	AddressService addressService;

	@Autowired
	GroupBetRepository groupBetRepository;

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
		User user = userService.getAuthenticatedUser(authentication);
		model.addAttribute("message", new Message());
		model.addAttribute("friends", user.getFriends());
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

}
