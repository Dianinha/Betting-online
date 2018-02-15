package pl.coderslab.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;
import pl.coderslab.model.UserSimple;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.UserService;

/**
 * This controller is only for managing {@link User} data such as
 * {@link Address}} or password.
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserControllerForUserDataOnly {


	@Autowired
	private UserService userService;

	@Autowired
	AddressService addressService;

	/**
	 * Displays view for editing basic {@link User} data.
	 * 
	 * <p>
	 * Attributes possible to edit: name surname userName e-mail general
	 * subscription
	 * </p>
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/editData", method = RequestMethod.GET)
	public String editUserData(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		model.addAttribute("user", new UserSimple(user));
		return "/user/editData";
	}

	/**
	 * This method process changes in the {@link User} data and merge the changes.
	 * If User wants to change userName or email he or her need to log in again
	 * 
	 * @param user
	 * @param result
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/editData", method = RequestMethod.POST)
	public String editUserDataPost(@Valid UserSimple user, BindingResult result, Authentication authentication) {
		User userFromDb = userService.getAuthenticatedUser(authentication);

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

		return "redirect:/user";
	}

	/**
	 * Displays view to change password
	 * 
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword() {

		return "/user/changePassword";
	}

	/**
	 * Saves new password in database. Checks if old password match and also if the
	 * confirmed new password matches the new password.
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmNewPassword
	 * @param auth
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePasswordPost(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmNewPassword") String confirmNewPassword, Authentication auth, Model model) {

		User userFromDb = userService.getAuthenticatedUser(auth);

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

	/**
	 * Displays the view where {@link User} can change the {@link Address}
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/editAddress", method = RequestMethod.GET)
	public String editUserAddress(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		Address address = addressService.findByUser(user);
		model.addAttribute("address", address);
		return "/user/editAddress";
	}

	@RequestMapping(value = "/editAddress", method = RequestMethod.POST)
	public String editUserAddressPost(@Valid Address address, BindingResult result, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);

		if (result.hasErrors()) {
			return "/user/editAddress";
		}
		Address oldAddress = addressService.findByUser(user);
		address.setId(oldAddress.getId());
		address.setUser(user);
		addressService.save(address);

		return "redirect:/user";
	}

}
