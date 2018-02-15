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

import pl.coderslab.model.Request;
import pl.coderslab.model.User;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.RequestService;
import pl.coderslab.service.UserService;

/**
 * Controller for actions connected to {@link User} social possibilities such as
 * inviting friends, managing invites, etc.
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserControllerForSocialOnly {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RequestService requestService;

	/**
	 * Displays view for looking for friends.
	 * 
	 * @param model
	 * @param authentication
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addFriends", method = RequestMethod.GET)
	public String addFriends(Model model, Authentication authentication, HttpSession session) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<User> friends = user.getFriends();
		model.addAttribute("friends", friends);
		return "user/addFriends";
	}

	/**
	 * Displays the seatch results for users in database. It is also possible to
	 * send the invite.
	 * 
	 * @param model
	 * @param authentication
	 * @param username
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/addFriends", method = RequestMethod.POST)
	public String addFriendsPost(Model model, Authentication authentication,
			@RequestParam(name = "username", required = false) String username,
			@RequestParam(name = "email", required = false) String email) {
		User user = userService.getAuthenticatedUser(authentication);

		Set<User> friends = user.getFriends();

		model.addAttribute("friends", friends);
		if (email != null) {
			model.addAttribute("searchResults", userService.findByEmailStartsWith(email));
		}
		if (username != null && !username.equals("")) {
			model.addAttribute("searchResults", userService.findByUSernameStartsWith(username));
		}
		return "user/addFriends";
	}

	/**
	 * This action allows {@link User} to delete friend from his or her friends
	 * list. The connection will be deleted for both Users.
	 * 
	 * @param model
	 * @param authentication
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/deleteFriend", method = RequestMethod.GET)
	public String deleteFriends(Model model, Authentication authentication, @RequestParam("username") String username) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<User> friends = user.getFriends();
		User userToDelete = userService.findByUsername(username);
		friends.remove(userToDelete);
		Set<User> friendsOfFriendToDelete = userToDelete.getFriends();
		friendsOfFriendToDelete.remove(user);
		userToDelete.setFriends(friendsOfFriendToDelete);
		userService.saveUser(user);
		userService.saveUser(userToDelete);
		model.addAttribute("friends", friends);
		return "redirect:/user/addFriends";
	}

	/**
	 * Sending an invite for friend list.
	 * 
	 * @param model
	 * @param authentication
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/sendInvite", method = RequestMethod.GET)
	public String sendInvite(Model model, Authentication authentication, @RequestParam("username") String username) {
		User user = userService.getAuthenticatedUser(authentication);
		User userToInvite = userService.findByUsername(username);
		List<User> recievers = new ArrayList<>();
		recievers.add(userToInvite);
		messageService.createUserRequestInvitation(user, recievers);
		requestService.createRequest(user, userToInvite);
		return "redirect:/user/addFriends";
	}

	/**
	 * Displays pending invites to friend lists.
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/invites", method = RequestMethod.GET)
	public String invites(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		if (!requestService.findByUser(user).isEmpty()) {
			model.addAttribute("invites", requestService.findByUser(user));
		}
		return "user/invites";
	}

	/**
	 * Allows User to accept friend {@link Request}
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/acceptInvite", method = RequestMethod.GET)
	public String acceptInvite(Model model, @RequestParam("invite") long id) {
		Request request = requestService.findById(id);
		requestService.acceptRequest(request);
		User reciever = request.getReciever();
		User sender = request.getSender();
		messageService.createInvitationAcceptedMessage(reciever, sender);
		return "redirect:/user/invites";
	}

	/**
	 * Allows User to discard the {@link Request} for friend list
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/discardInvite", method = RequestMethod.GET)
	public String discardInvite(Model model, @RequestParam("invite") long id) {
		requestService.discardRequest(requestService.findById(id));
		return "redirect:/user/invites";
	}

	/**
	 * Displays actual list of {@link User} friends
	 * 
	 * @param model
	 * @param authentication
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/friendsList", method = RequestMethod.GET)
	public String friendsList(Model model, Authentication authentication, HttpSession session) {
		User user = userService.getAuthenticatedUser(authentication);
		Set<User> friends = user.getFriends();
		model.addAttribute("friends", friends);

		return "user/friendsList";
	}
}
