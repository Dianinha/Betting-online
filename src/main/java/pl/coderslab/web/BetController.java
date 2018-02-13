package pl.coderslab.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.MultipleBetRepository;
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
	
	@Autowired
	MultipleBetRepository multiBetRepository;

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
		SingleBet bet = new SingleBet();
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

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(@RequestParam("gameId") long gameId, @RequestParam("betOn") String betOn, Model model,
			HttpSession session) {
		GameToBet game = gameService.findById(gameId);
		if (!game.isActive()) {
			return "redirect:/user";
		}
		BigDecimal rate = null;
		String betOnParam = betOn;
		String teamName = "";
		if (betOn.equals("home")) {
			rate = game.getRateHome();
			teamName = game.getEvent().getHomeTeamName();
		} else if (betOn.equals("draw")) {
			rate = game.getRateDraw();
			teamName = "draw: " + game.getEvent().getHomeTeamName() + " vs. " + game.getEvent().getAwayTeamName();

		} else if (betOn.equals("away")) {
			rate = game.getRateAway();
			teamName = game.getEvent().getAwayTeamName();
		} else {
			return "redirect:/user";
		}
		model.addAttribute("rate", rate);
		model.addAttribute("team", teamName);

		session.setAttribute("lastGameBettingOn", game.getId());
		session.setAttribute("lastRate", rate);
		session.setAttribute("betOn", betOnParam);
		System.out.println("Attributes setted?");

		return "/bet/addBet";

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Model model, HttpSession session, Authentication auth,
			@RequestParam(name = "amount", required = false) Double amount,
			@RequestParam(name = "newAmount", required = false) Double newAmount) {
		long gameId = (Long) session.getAttribute("lastGameBettingOn");
		GameToBet game = gameService.findById(gameId);
		SingleBet bet = new SingleBet();
		User user = userService.getAuthenticatedUser(auth);
		String betOn = (String) session.getAttribute("betOn");
		BigDecimal amountOfBet = null;
		if (newAmount != null) {
			amountOfBet = new BigDecimal(newAmount);
		} else {

			if (amount != null) {
				amountOfBet = new BigDecimal(amount);
			} else {
				amountOfBet = BigDecimal.valueOf((double) session.getAttribute("currentAmount"));
			}
		}
		System.out.println(betOn);
		bet.setUser(user);
		bet.setAmount(amountOfBet);
		bet.setGame(game);

		bet.setBetOn(betOn);

		bet.setStatus(BetStatus.NOT_APPROVED);

		BigDecimal rate = (BigDecimal) session.getAttribute("lastRate");
		bet.setRate(rate);

		List<SingleBet> bets = null;
		if (session.getAttribute("currentBetCart") != null) {
			bets = (List<SingleBet>) session.getAttribute("currentBetCart");
			for (SingleBet singleBet : bets) {
				singleBet.setAmount(amountOfBet);
			}
			if (betService.isBetAlreadyPlaced(bet, bets)) {
				model.addAttribute("message", "You have already betted on this game!");
				return "bet/finalized";
			}

		} else {
			bets = new ArrayList<>();
			if (amount != null) {
				session.setAttribute("currentAmount", amount);
			} else {
				session.setAttribute("currentAmount", 0);
			}

		}

		bets.add(bet);
		BigDecimal possibleWin = new BigDecimal(0);
		BigDecimal betAmount = bet.getAmount();
		BigDecimal multipliedRated = new BigDecimal(1);
		for (SingleBet singleBet : bets) {
			multipliedRated = multipliedRated.multiply(singleBet.getRate());
		}
		possibleWin = possibleWin.add(betAmount.multiply(multipliedRated));

		session.setAttribute("currentBetCart", bets);
		session.setAttribute("possibleWin", possibleWin);

		return "/bet/betPlaced";

	}

	@RequestMapping(value = "/finalizeBets", method = RequestMethod.POST)
	public String finalizeBets(Model model, HttpSession session, Authentication auth) {

		User user = userService.getAuthenticatedUser(auth);
		Wallet wallet = walletService.findByUser(user);

		List<SingleBet> bets = (List<SingleBet>) session.getAttribute("currentBetCart");
		if (bets.size() > 1) {
			List<SingleBet> userBets = new ArrayList<>();

			// here

			for (SingleBet bet : bets) {
				bet.setItGroupBet(false);
				bet.setItMultiBet(true);
				try {
					long gameid = bet.getGame().getId();
					GameToBet game = gameService.findById(gameid);
					if (!game.isActive()) {
						model.addAttribute("message",
								"One of games You are betting on is no longer active. Please pick different bet.");
						return "/bet/finalized";
					}
					if (betService.isBetRateAccurate(bet)) {
						bet.setStatus(BetStatus.PLACED);
						bet.setItMultiBet(true);
						bet.setItGroupBet(false);
						userBets.add(bet);
					} else {
						model.addAttribute("message",
								"Game You are betting on changed rate. Please pick different bet.");
						return "/bet/finalized";
					}
				} catch (Exception e) {
					model.addAttribute("message", "Something went wrong. Please place bet again.");
					return "/bet/finalized";
				}
			}
			if (walletService.hasWalletSufficientFunds(wallet, userBets.get(0).getAmount())) {
				walletService.substractFunds(wallet, userBets.get(0).getAmount());
				operationService.createPlaceMultipleBetOperation(wallet, userBets.get(0).getAmount(), bets);

			} else {
				model.addAttribute("message",
						"You do not have sufficient funds to place this bet. Please add funds to Your account.");
				return "/bet/finalized";
			}
			BigDecimal joinedMultiplyer = new BigDecimal(1);
			for (SingleBet singleBet : userBets) {
				joinedMultiplyer= joinedMultiplyer.multiply(singleBet.getRate());
				betService.placeBet(singleBet);
			}
			MultipleBet multiBet = new MultipleBet();
			multiBet.setBets(userBets);
			multiBet.setUser(user);
			multiBet.setJoinedAmount(userBets.get(0).getAmount());
			multiBet.setJoinedRating(joinedMultiplyer);
			multiBet.setStatus(BetStatus.PLACED);
			multiBetRepository.save(multiBet);
			model.addAttribute("message", "Your bets have been placed. Good luck!");

		} else if (bets.size() == 1) {
			SingleBet bet = bets.get(0);
			bet.setItGroupBet(false);
			bet.setItMultiBet(false);
			try {
				long gameid = bet.getGame().getId();
				GameToBet game = gameService.findById(gameid);
				if (!game.isActive()) {
					session.setAttribute("lastGameBettingOn", null);
					session.setAttribute("lastRate", null);
					session.setAttribute("betOn", null);
					model.addAttribute("message",
							"Game You are betting on is no longer active. Please pick different bet.");
					return "/bet/finalized";
				}
				if (betService.isBetRateAccurate(bet)) {

					if (walletService.hasWalletSufficientFunds(wallet, bet.getAmount())) {
						bet.setStatus(BetStatus.PLACED);
						betService.placeBet(bet);
						model.addAttribute("message", "Bet was placed. Good luck!");
						walletService.substractFunds(wallet, bet.getAmount());
						operationService.createPlaceBetOperation(wallet, bet.getAmount(), bet);

					} else {
						model.addAttribute("message",
								"You do not have sufficient funds to place this bet. Please add funds to Your account.");
						return "/bet/finalized";
					}

				} else {
					session.setAttribute("lastGameBettingOn", null);
					session.setAttribute("lastRate", null);
					session.setAttribute("betOn", null);
					model.addAttribute("message", "Game You are betting on changed rate. Please pick different bet.");
					return "/bet/finalized";
				}
			} catch (Exception e) {
				session.setAttribute("lastGameBettingOn", null);
				session.setAttribute("lastRate", null);
				session.setAttribute("betOn", null);
				model.addAttribute("message", "Something went wrong. Please place bet again.");
				return "/bet/finalized";
			}

		}

		else {
			session.setAttribute("lastGameBettingOn", null);
			session.setAttribute("lastRate", null);
			session.setAttribute("betOn", null);
			model.addAttribute("message", "Something went wrong. Please place bet again.");
			return "/bet/finalized";
		}

		session.setAttribute("lastGameBettingOn", null);
		session.setAttribute("lastRate", null);
		session.setAttribute("betOn", null);
		return "/bet/finalized";

	}

}
