package pl.coderslab.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import pl.coderslab.service.MessageService;
import pl.coderslab.service.UserService;

/**
 * This controls the message section of {@link User} account
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserControllerForMessagesOnly {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerForMessagesOnly.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	/**
	 * Displays the list of {@link User} {@link Message}
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.GET)
	public String messages(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		try {
			model.addAttribute("recieved", messageService.findMessagesByReciever(user));
			model.addAttribute("send", messageService.findMessagesBySender(user));
		} catch (Exception e) {
			LOGGER.info("Failed to get User messages.");
		}
		return "/user/messages";
	}

	/**
	 * Allows {@link User} to send new {@link Message}
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
	public String sendNewMessage(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		model.addAttribute("message", new Message());
		model.addAttribute("friends", user.getFriends());
		return "/user/addMessage";
	}

	/**
	 * Sends the {@link Message}
	 * 
	 * @param username
	 * @param model
	 * @param authentication
	 * @param message
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
	public String sendNewMessagePost(@RequestParam("username") String username, Model model,
			Authentication authentication, @Valid Message message, BindingResult result) {
		if (result.hasErrors()) {
			return "/user/addMessage";
		}
		User userToSendMessage = null;
		User sender = userService.getAuthenticatedUser(authentication);
			userToSendMessage = userService.findByUsername(username);

		try {
			message = messageService.addReciever(message, userToSendMessage);
			message = messageService.addSender(message, sender);
			messageService.sendMessage(message);
		} catch (Exception e) {
			LOGGER.info("Failed to send a message");
			return "/user/addMessage";
		}

		return "redirect:/user/messages";
	}

}
