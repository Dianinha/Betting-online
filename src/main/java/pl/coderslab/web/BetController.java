package pl.coderslab.web;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Bet;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.service.BetService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

@Controller
@RequestMapping(path = "/bet")
public class BetController {

	@Autowired
	GameToBetService gameService;

	@Autowired
	UserService userService;

	@Autowired
	BetService betService;

	@Autowired
	WalletService walletService;

	@Autowired
	OperationService operationService;

	@RequestMapping(value = "/addBet", method = RequestMethod.GET)
	public String addBet(@RequestParam("gameId") long gameId, @RequestParam("betOn") String betOn, Model model,
			HttpSession session) {
		GameToBet game = gameService.findById(gameId);
		if (!game.isActive()) {
			return "redirect:/results/tryLive";
		}
		BigDecimal rate = null;
		String teamName = "";
		if (betOn.equals("home")) {
			rate = game.getRateHome();
			teamName = game.getEvent().getHomeTeamName();
		} else if (betOn.equals("draw")) {
			rate = game.getRateDraw();
			teamName = "DRAW";

		} else if (betOn.equals("away")) {
			rate = game.getRateAway();
			teamName = game.getEvent().getAwayTeamName();
		} else {
			return "redirect:/results/tryLive";
		}
		model.addAttribute("rate", rate);
		model.addAttribute("team", teamName);
		session.setAttribute("lastGameBettingOn", game);
		session.setAttribute("lastRate", rate);

		return "/bet/addBet";

	}

	@RequestMapping(value = "/addBet", method = RequestMethod.POST)
	public String addBetPost(Model model, HttpSession session, Authentication auth,
			@RequestParam("amount") double amount) {

		GameToBet game = (GameToBet) session.getAttribute("lastGameBettingOn");
		Bet bet = new Bet();
		User user = null;

		try {
			user = userService.findByUsername(auth.getName());
		} catch (Exception e) {
			System.out.println("No user found");
			return "redirect:/login";
		}

		Wallet wallet = walletService.findByUser(user);
		BigDecimal amountOfBet = new BigDecimal(amount);

		if (walletService.hasWalletSufficientFunds(wallet, amountOfBet)) {
			walletService.substractFunds(wallet, amountOfBet);
		} else {
			return "redirect:/user/wallet";
		}

		bet.setUser(user);
		bet.setAmount(amountOfBet);
		bet.setGame(game);

		BigDecimal rate = (BigDecimal) session.getAttribute("lastRate");
		bet.setRate(rate);
		betService.placeBet(bet);
		operationService.createPlaceBetOperation(wallet, amountOfBet, bet);
		return "/bet/betPlaced";

	}

}
