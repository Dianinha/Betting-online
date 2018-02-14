package pl.coderslab.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Message;
import pl.coderslab.model.Request;
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
public class UserControllerForSocialOnly {

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
