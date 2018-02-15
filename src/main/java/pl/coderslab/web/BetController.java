package pl.coderslab.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.GroupBetRequest;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.repositories.GroupBetRequestRepository;
import pl.coderslab.repositories.MultipleBetRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.GroupBetRequestService;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

/**
 * Controller for {@link SingleBet}. {@link MultipleBet} and {@link GroupBet}
 * related actions.
 * 
 * @author dianinha
 *
 */

@Controller
@RequestMapping(path = "/bet")
public class BetController {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

	@Autowired
	GameToBetService gameService;

	@Autowired
	UserService userService;

	@Autowired
	BetService betService;

	@Autowired
	WalletService walletService;

	@Autowired
	GroupBetRequestService groupBetRequestService;

	@Autowired
	OperationService operationService;

	@Autowired
	MultipleBetRepository multiBetRepository;

	@Autowired
	GroupBetRepository groupBetRepository;

	@Autowired
	MessageService messageService;

	@Autowired
	GroupBetRequestRepository groupBetRequestRepository;

	/*
	 * This method is redirecting user to page with confirmation for bet placing.
	 * <p> Bet is not created. Some attributes are added to session: - bet's rate -
	 * what is bet on - game id - team that bet is on name
	 * 
	 * Attributes passed to the model: - bet's rate - team name that bet is placed
	 * on
	 * 
	 * If there is no longer possibility to place bets on the game user should get
	 * according message and is redirected to the main user page. </p>
	 * 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBetGet(@RequestParam("gameId") long gameId, @RequestParam("betOn") String betOn, Model model,
			HttpSession session) {
		GameToBet game = gameService.findById(gameId);
		if (!game.isActive()) {
			model.addAttribute("message", "Betting on this game is no longer possible. Please choose other game.");
			return "user/menu";
		}

		BigDecimal rate = null;
		String teamName = "";
		rate = gameService.getRateByBetOn(game, betOn);
		teamName = gameService.getTeamNameByBetOn(game, betOn);
		model.addAttribute("rate", rate);
		model.addAttribute("team", teamName);

		session.setAttribute("lastGameBettingOn", game.getId());
		session.setAttribute("lastRate", rate);
		session.setAttribute("betOn", betOn);

		return "/bet/addBet";

	}

	/**
	 * This method process the bet after Users confirmation. If creates
	 * {@link SingleBet} and stores it in session. If {@link User} changes the
	 * amount of bet it is changed.
	 * 
	 * <p>
	 * Stores in session the current amount of bets called "currentAmount"
	 * 
	 * Creates the bet. And adds it to {@link List} of bets stored in session called
	 * "currentBetCart".
	 * </p>
	 * 
	 * @param model
	 * @param session
	 * @param auth
	 * @param amount
	 *            - if it is a first bet in the session that is the bet amount
	 * @param newAmount
	 *            - if it is not the first bet in session and User want to change
	 *            the amount this is the amount of bet
	 * @return
	 */
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
				amountOfBet = (BigDecimal) session.getAttribute("currentAmount");
			}
		}
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
			if (amountOfBet != null) {
				session.setAttribute("currentAmount", amountOfBet);
			} else {
				session.setAttribute("currentAmount", 0);
			}

		}
		bets.add(bet);

		BigDecimal multipliedRated = betService.calculateRateInMultipleBet(bets);

		BigDecimal possibleWin = betService.calculatePossilbeWinInMultipleBet(multipliedRated, amountOfBet);
		session.setAttribute("currentBetCart", bets);
		session.setAttribute("possibleWin", possibleWin);

		return "/bet/betPlaced";

	}

	/**
	 * This method saves bets stored in the session.
	 * 
	 * <p>
	 * Method checks if the User have sufficient funds in the wallet and place the
	 * bet.
	 * </p>
	 * 
	 * @param model
	 * @param session
	 * @param auth
	 * @return
	 */
	@RequestMapping(value = "/finalizeBets", method = RequestMethod.POST)
	public String finalizeBets(Model model, HttpSession session, Authentication auth) {

		User user = userService.getAuthenticatedUser(auth);
		Wallet wallet = walletService.findByUser(user);

		@SuppressWarnings("unchecked")
		List<SingleBet> bets = (List<SingleBet>) session.getAttribute("currentBetCart");

		// This is MultipleBet
		if (bets.size() > 1) {
			List<SingleBet> userBets = new ArrayList<>();
			for (SingleBet bet : bets) {
				bet.setItGroupBet(false);
				bet.setItMultiBet(true);
				try {
					long gameid = bet.getGame().getId();
					GameToBet game = gameService.findById(gameid);
					if (!game.isActive()) {
						session.setAttribute("currentBetCart", null);
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
						session.setAttribute("currentBetCart", null);
						model.addAttribute("message",
								"Game You are betting on changed rate. Please pick different bet.");
						return "/bet/finalized";
					}
				} catch (Exception e) {
					session.setAttribute("currentBetCart", null);
					model.addAttribute("message", "Something went wrong. Please place bet again.");
					LOGGER.info("Failed to finalize SingleBet saving");
					return "/bet/finalized";
				}
			}

			MultipleBet multiBet = new MultipleBet();
			multiBet.setItAGroupBet(false);
			multiBet.setBets(userBets);
			multiBet.setUser(user);
			multiBet.setGroupBetPossible(true);
			multiBet.setJoinedAmount(userBets.get(0).getAmount());
			multiBet.setJoinedRating(betService.calculateRateInMultipleBet(userBets));

			if (walletService.hasWalletSufficientFunds(wallet, multiBet.getJoinedAmount())) {
				walletService.substractFunds(wallet, multiBet.getJoinedAmount());
				operationService.createPlaceMultipleBetOperation(wallet, multiBet.getJoinedAmount(), bets);

			} else {
				model.addAttribute("message",
						"You do not have sufficient funds to place this bet. Please add funds to Your account.");
				return "/bet/finalized";
			}
			for (SingleBet singleBet2 : userBets) {
				betService.placeBet(singleBet2);
			}

			multiBet.setStatus(BetStatus.PLACED);
			multiBetRepository.save(multiBet);
			model.addAttribute("message", "Your bets have been placed. Good luck!");

		}
		// This is for SingleBet only
		else if (bets.size() == 1) {
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
					session.setAttribute("currentBetCart", null);
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
					session.setAttribute("currentBetCart", null);
					session.setAttribute("lastGameBettingOn", null);
					session.setAttribute("lastRate", null);
					session.setAttribute("betOn", null);
					model.addAttribute("message", "Game You are betting on changed rate. Please pick different bet.");
					return "/bet/finalized";
				}
			} catch (Exception e) {
				session.setAttribute("currentBetCart", null);
				session.setAttribute("lastGameBettingOn", null);
				session.setAttribute("lastRate", null);
				session.setAttribute("betOn", null);
				model.addAttribute("message", "Something went wrong. Please place bet again.");
				LOGGER.info("Failed to finalize MultipleBet saving.");
				return "/bet/finalized";
			}

		}

		else {
			session.setAttribute("currentBetCart", null);
			session.setAttribute("lastGameBettingOn", null);
			session.setAttribute("lastRate", null);
			session.setAttribute("betOn", null);
			model.addAttribute("message", "Something went wrong. Please place bet again.");
			return "/bet/finalized";
		}
		session.setAttribute("currentBetCart", null);
		session.setAttribute("lastGameBettingOn", null);
		session.setAttribute("lastRate", null);
		session.setAttribute("betOn", null);
		return "/bet/finalized";

	}

	/**
	 * This method convert {@link SingleBet} or {@link MultipleBet} to
	 * {@link GroupBet}
	 * 
	 * @param model
	 * @param session
	 * @param auth
	 * @param id
	 * @param type
	 *            - refers to bet type. "single" for SingleBets and "multi" for
	 *            MultipleBet
	 * @return
	 */
	@RequestMapping(value = "/convertToGroupBet", method = RequestMethod.GET)
	public String convertToGroupBet(Model model, HttpSession session, Authentication auth, @RequestParam("id") long id,
			@RequestParam("type") String type) {

		List<SingleBet> bets = new ArrayList<>();
		GroupBet groupBet = new GroupBet();
		if (type.equals("single")) {

			SingleBet bet = betService.findById(id);
			GameToBet game = bet.getGame();
			if (!game.isActive()) {
				model.addAttribute("message", "Game is not longer active. You cannot change it to group bet");
				return "user/myBets";
			}
			bets.add(bet);
			groupBet.setJoinedAmount(bet.getAmount());
			groupBet.setJoinedRating(bet.getRate());
			betService.changeBetToGroupBet(bet);
		} else if (type.equals("multi")) {
			MultipleBet multiBet = multiBetRepository.findOne(id);
			if (multiBet.isGroupBetPossible() == false) {
				model.addAttribute("message",
						"One of the games has already started. You cannot change it to group bet");
				return "user/myBets";
			}
			bets = multiBet.getBets();
			for (SingleBet singleBet : bets) {
				if (!singleBet.getGame().isActive()) {
					model.addAttribute("message",
							"One of the games is not longer active. You cannot change it to group bet");
					return "user/myBets";
				}
			}
			multiBet.setItAGroupBet(true);
			groupBet.setJoinedAmount(multiBet.getJoinedAmount());
			groupBet.setJoinedRating(multiBet.getJoinedRating());

			for (SingleBet singleBet : bets) {
				betService.changeBetToGroupBet(singleBet);
			}
			multiBetRepository.delete(multiBet);

		}
		groupBet.setBet(bets);
		groupBet.setStatus(BetStatus.PLACED);
		groupBet.setBetCode(groupBet.toString().replace("pl.coderslab.model.", ""));
		List<User> betUsers = new ArrayList<>();
		betUsers.add(userService.getAuthenticatedUser(auth));
		groupBet.setUsers(betUsers);
		groupBetRepository.save(groupBet);
		return "redirect:/user/groupBets";
	}

	/**
	 * This method get's {@link User} friends and let the user invite them to
	 * {@link GroupBet}
	 * 
	 * @param model
	 * @param session
	 * @param auth
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/inviteToGroupBet", method = RequestMethod.GET)
	public String inviteToGroupBet(Model model, HttpSession session, Authentication auth, @RequestParam("id") long id) {

		User user = userService.getAuthenticatedUser(auth);
		model.addAttribute("friends", user.getFriends());
		session.setAttribute("gbId", id);
		return "/user/inviteTogroupBet";
	}

	/**
	 * This method send {@link GroupBet} invites to {@link User} friends. It also
	 * sends them a message bout {@link GroupBet}
	 * 
	 * @param model
	 * @param session
	 * @param auth
	 * @param recieversArr
	 * @return
	 */
	@RequestMapping(value = "/inviteToGroupBet", method = RequestMethod.POST)
	public String inviteToGroupBetPost(Model model, HttpSession session, Authentication auth,
			@RequestParam("selectedFriends") String[] recieversArr) {

		User sender = userService.getAuthenticatedUser(auth);
		long groubBetId = (long) session.getAttribute("gbId");
		session.setAttribute("gbId", null);
		GroupBet groupBet = groupBetRepository.findOne(groubBetId);
		List<User> recievers = new ArrayList<>();
		for (int i = 0; i < recieversArr.length; i++) {
			recievers.add(userService.findByUsername(recieversArr[i]));
		}
		messageService.createAndSendGroupBetInvitationMessage(sender, recievers, groupBet);
		groupBetRequestService.sendGroupBetRequests(groupBet, recievers, sender);

		return "redirect:/user/groupBets";
	}

	/**
	 * This method passes to the model list of {@link User} invites to
	 * {@link GroupBet}
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/invites", method = RequestMethod.GET)
	public String invites(Model model, Authentication authentication) {
		User user = userService.getAuthenticatedUser(authentication);
		if (!groupBetRequestRepository.findByUser(user).isEmpty()) {
			model.addAttribute("invites", groupBetRequestService.getRequestsByUser(user));
		}

		return "bet/invites";
	}

	/**
	 * This method allows {@link User} to accept {@link GroupBetRequest} and join
	 * the {@link GroupBet}
	 * 
	 * @param model
	 * @param id
	 * @param auth
	 * @return
	 */
	@RequestMapping(value = "/acceptInvite", method = RequestMethod.GET)
	public String acceptInvite(Model model, @RequestParam("invite") long id, Authentication auth) {
		User user = userService.getAuthenticatedUser(auth);
		GroupBetRequest request = groupBetRequestRepository.findOne(id);
		GroupBet groupBet = request.getGroupBet();
		Wallet wallet = walletService.findByUser(user);
		if (!groupBetRequestService.checkIfYouCanJoinTheGroupBet(request)) {
			model.addAttribute("message",
					"Sorry, You cannot join this Group Bet anymore. Maximum number of people already has joined or Group Bet has already ended.");
		} else if (walletService.hasWalletSufficientFunds(wallet, groupBet.getJoinedAmount())) {
			walletService.substractFunds(wallet, groupBet.getJoinedAmount());
			betService.addUserToGroupBet(user, groupBet);
			groupBetRequestService.discardRequest(request);
			model.addAttribute("message", "You have joined the group bet");
			operationService.joinGroupBetOperation(wallet, groupBet);
		} else {
			model.addAttribute("message", "You do not have sufficient funds to join this group bet.");
		}

		return "bet/invites";

	}

	/**
	 * This method discard unwanted {@link GroupBetRequest}. It's status is set to
	 * false.
	 * 
	 * @param model
	 * @param id
	 *            of GroupBetRequest
	 * @return
	 */
	@RequestMapping(value = "/discardInvite", method = RequestMethod.GET)
	public String discardGroupBetInvite(Model model, @RequestParam("invite") long id) {
		groupBetRequestService.discardRequest(groupBetRequestService.findById(id));
		model.addAttribute("message", "The invite for the Group Bet was dissmissed.");
		return "redirect:/bet/invites";
	}

}
