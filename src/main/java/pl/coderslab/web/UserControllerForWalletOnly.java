package pl.coderslab.web;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpSession;
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

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.Operation;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

/**
 * This method is for controlling the {@link Wallet} related issues.
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserControllerForWalletOnly {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	private CreditCardService creditService;

	@Autowired
	private UserService userService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private OperationService operationService;

	@RequestMapping(path = "/wallet")
	public String badUrlCatcher() {
		return "redirect:/user";
	}

	/**
	 * This method show User acquiring funds option
	 * 
	 * @return
	 */
	@RequestMapping(path = "/funds")
	public String showFundsAmounts() {
		return "/user/fundsOption";
	}

	/**
	 * This method is responsible for storing in session amount of money that
	 * {@link User} want to add to his or her {@link Wallet}
	 * 
	 * @param val
	 * @param session
	 * @return
	 */
	@RequestMapping(path = "/addFunds")
	public String addFunds(@RequestParam("val") int val, HttpSession session) {

		session.setAttribute("fundsToAdd", val);
		return "redirect:/user/creditCardOption";
	}

	/**
	 * This method asks User if he want to make purchase with new credit card , pick
	 * one from its list or pay withour saving.
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(path = "/creditCardOption")
	public String creditCardOption(Model model, Authentication authentication) {
		List<CreditCardInfo> userCreditCards = null;
		User user = userService.getAuthenticatedUser(authentication);
		userCreditCards = creditService.findByUser(user);
		if (userCreditCards.size() != 0) {
			model.addAttribute("creditCards", userCreditCards);
		}

		return "/user/creditCardOption";
	}

	/**
	 * This method displays a view for adding new {@link CreditCardInfo}
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/addCreditCard", method = RequestMethod.GET)
	public String creditCardAdd(Model model) {
		model.addAttribute("creditCardInfo", new CreditCardInfo());
		return "/user/addCreditCard";
	}

	/**
	 * This method adds new {@link CreditCardInfo} and connects it to {@link User}
	 * 
	 * @param creditCardInfo
	 * @param result
	 * @param session
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/addCreditCard", method = RequestMethod.POST)
	public String creditCardAddPost(@Valid CreditCardInfo creditCardInfo, BindingResult result, HttpSession session,
			Authentication authentication) {
		if (result.hasErrors()) {
			return "/user/addCreditCard";
		}
		CreditCardInfo creditCardMade = null;
		try {
			User user = userService.getAuthenticatedUser(authentication);
			creditCardMade = creditService.create(creditCardInfo, user);
		} catch (Exception e) {
			LOGGER.info("Failed to create new credit card information.");
		}

		Integer fundsToAdd = (Integer) session.getAttribute("fundsToAdd");

		if (fundsToAdd != null) {
			return "redirect:/user/payWith?id=" + creditCardMade.getId();

		}

		return "redirect:/user/creditCards";
	}

	/**
	 * This method retrieves information about the credit card that user want to pay
	 * for his or her funds.
	 * 
	 * @param model
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/payWith", method = RequestMethod.GET)
	public String payWithCard(Model model, @RequestParam("id") long id, HttpSession session) {
		model.addAttribute("creditCard", creditService.findById(id));
		session.setAttribute("creditCardId", id);

		return "/user/creditCardConfirmation";
	}

	/**
	 * This method checks if given CVV checks with the one in the database. If so it
	 * finalized the purchase.
	 * 
	 * @param model
	 * @param authentication
	 * @param session
	 * @param cvv
	 * @return
	 */
	@RequestMapping(value = "/payWith", method = RequestMethod.POST)
	public String payWithCardPost(Model model, Authentication authentication, HttpSession session,
			@RequestParam("cvv") int cvv) {

		User user = userService.getAuthenticatedUser(authentication);
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
			session.setAttribute("fundsToAdd", null);
			session.setAttribute("creditCard", null);
			session.setAttribute("creditCardId", null);

		} catch (Exception e) {
			LOGGER.info("Failed to pay with credit card. ");
		}

		return "redirect:/user";
	}

	/**
	 * This method is for paying for funds without saved credit card
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public String paymentWithoutSavingCard(Model model, HttpSession session) {
		model.addAttribute("creditCardInfo", new CreditCardInfo());
		return "/user/payment";
	}

	/**
	 * This method finalizes the purchase of funds without saved credit card. Only
	 * the basic data for credit card must be right.
	 * 
	 * @param creditCardInfo
	 * @param result
	 * @param model
	 * @param session
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/payment", method = RequestMethod.POST)
	public String paymentWithoutSavingCardPost(@Valid CreditCardInfo creditCardInfo, BindingResult result, Model model,
			HttpSession session, Authentication authentication) {
		if (result.hasErrors()) {
			return "/user/payment";
		}
		User user = userService.getAuthenticatedUser(authentication);

		try {
			Wallet wallet = walletService.findByUser(user);
			Integer amountInInteger = (Integer) session.getAttribute("fundsToAdd");
			BigDecimal amount = BigDecimal.valueOf(amountInInteger);
			wallet = walletService.addFunds(wallet, amount);
			operationService.createAddOperation(wallet, amount, creditCardInfo.getLastFourDigits());
			session.setAttribute("currentFunds", wallet.getAmount());
		} catch (Exception e) {
			LOGGER.info("Failed to finalize the payment.");
		}
		return "redirect:/user";
	}

	/**
	 * This method displays {@link User} {@link CreditCardInfo}
	 * 
	 * @param model
	 * @param session
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/creditCards", method = RequestMethod.GET)
	public String creditCards(Model model, HttpSession session, Authentication authentication) {
		List<CreditCardInfo> userCreditCards = null;
		User user = userService.getAuthenticatedUser(authentication);
		userCreditCards = creditService.findByUser(user);
		model.addAttribute("creditCards", userCreditCards);
		return "/user/creditCards";
	}

	/**
	 * This method displays all {@link User} {@link Operation}
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/operationHistory", method = RequestMethod.GET)
	public String operationHistory(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		Wallet wallet = walletService.findByUser(user);
		List<Operation> usersOperations = operationService.findAllOperationByWallet(wallet);
		model.addAttribute("usersOperations", usersOperations);
		return "/user/operationHistory";
	}

	/**
	 * This method deletes {@link User} {@link CreditCardInfo} by its id
	 * 
	 * @param model
	 * @param id
	 *            of Credit Card Information
	 * @return
	 */
	@RequestMapping(value = "/deleteCreditCard", method = RequestMethod.GET)
	public String deleteCreditCard(Model model, @RequestParam("cardId") long id) {
		boolean flag = creditService.deleteById(id);
		if (flag) {
			return "redirect:/user";
		} else {
			model.addAttribute("message", "Something was wrong. Credit card data was not deleted.");
			return "user/creditCards";
		}

	}

	@RequestMapping(value = "/withdrawal", method = RequestMethod.GET)
	public String withdrawalOfFunds(Model model) {
		return "user/withdrawal";

	}

	@RequestMapping(value = "/withdrawal", method = RequestMethod.POST)
	public String withdrawalOfFundsPOST(Model model, Authentication authentication,
			@RequestParam("amount") double amount, @RequestParam("account") Long account, HttpSession session) {
		User user = userService.getAuthenticatedUser(authentication);
		Wallet wallet = walletService.findByUser(user);
		if (walletService.hasWalletSufficientFunds(wallet, BigDecimal.valueOf(amount+20))) {
			walletService.substractFunds(wallet, BigDecimal.valueOf(amount));
			model.addAttribute("message", "Money has been transfered.");
			session.setAttribute("currentFunds", wallet.getAmount());
		}
		else {
			model.addAttribute("message", "You do not have sufficient funds for this operation. Remenber that You cannot transfer extra 20 pln gift for registration back");
		}
		return "user/withdrawal";

	}
}
