package pl.coderslab.service.implementation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.Operation;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.BetRepository;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.repositories.MultipleBetRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.WalletService;

@Service
public class BetServiceImpl implements BetService {

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");
	
	@Autowired
	BetRepository betRepository;

	@Autowired
	MultipleBetRepository multipleBetRepository;

	@Autowired
	GroupBetRepository groupBetRepository;

	@Autowired
	GameToBetService gameService;

	@Autowired
	EventService eventService;

	@Autowired
	WalletService walletService;

	@Autowired
	OperationService operationService;

	/**
	 * Method saves {@link SingleBet}
	 * 
	 * @param singleBet
	 *            to save
	 * 
	 * 
	 */

	@Override
	public void placeBet(SingleBet singleBet) {

		betRepository.save(singleBet);

	}

	/**
	 * This method checks if rate attribute in {@link SingleBet} placed by
	 * {@link User} is the same as attribute rate in {@link GameToBet} depending on
	 * what User betted on - choices: home, away and draw - attribute betOn in
	 * {@link SingleBet}
	 * 
	 * @param singleBet
	 *            to check rate
	 * 
	 * @return boolean true if attributes match and false if not
	 */
	@Override
	public boolean isBetRateAccurate(SingleBet singleBet) {
		boolean result = false;
		GameToBet game = gameService.findById(singleBet.getGame().getId());
		if (singleBet.getBetOn().equals("home")) {
			result = singleBet.getRate().equals(game.getRateHome());
		} else if (singleBet.getBetOn().equals("away")) {
			result = singleBet.getRate().equals(game.getRateAway());
		} else {
			result = singleBet.getRate().equals(game.getRateDraw());
		}
		return result;
	}

	/**
	 * Method checks if {@link SingleBet} has been already placed by User during
	 * adding more bets to {@link MultipleBet} This prevents {@link User} to place
	 * two {@link SingleBet} on the same {@link GameToBet}.
	 * 
	 * @param singleBet
	 *            {@link SingleBet} to check
	 * @param betsInMultipleBet
	 *            {@link List} of bets that are already placed in
	 *            {@link MultipleBet}
	 * 
	 * @return boolean true if {@link SingleBet} is already placed and false if not
	 */

	@Override
	public boolean isBetAlreadyPlaced(SingleBet singleBet, List<SingleBet> betsInMultipleBet) {

		for (SingleBet bet : betsInMultipleBet) {
			if (singleBet.getGame().getId() == bet.getGame().getId()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Method finds List of {@link SingleBet} by {@link User} and {@link BetStatus}
	 * 
	 * <p>
	 * In the result only {@link SingleBet} that are included are the ones that have
	 * attributes isItMultiBet and isItGroupBet set to false
	 * </p>
	 * 
	 * @param status
	 *            {@link BetStasus} Enum {@link BetStatus}
	 * @param user
	 *            {@link User} that placed bets
	 * 
	 * @return {@link List} of {@link SingleBet} that met the criteria
	 */

	@Override
	public List<SingleBet> findBetsByUserAndStatus(BetStatus status, User user) {
		return betRepository.findByUser(status, user);
	}

	/**
	 * This method check {@link SingleBet} placed on todays {@link Event} and
	 * changes their {@link BetStatus} and optionally finalize the win if
	 * {@link SingleBet} was won.
	 * 
	 * <p>
	 * This method finds {@link Event} that are played today and get according
	 * {@link GameToBet}. If the {@link Event} has ended is checks the games and
	 * iterates through all the {@link SingleBet} placed on this game. In the result
	 * only {@link SingleBet} that are included are the ones that have attributes
	 * isItMultiBet and isItGroupBet set to false. Also it looks only for placed
	 * {@link SingleBet}. It changes the {@link SingleBet} {@link BetStatus} and
	 * finalize the winnings. Also it created {@link Operation} in {@link Users}
	 * {@link Wallet}
	 * </p>
	 */

	@Override
	public void checkBetsForTodayGames() {

		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		List<GameToBet> games = gameService.findByListOfEvents(liveEvents);
		for (GameToBet gameToBet : games) {
			Event eventRelatedToGameToBet = gameToBet.getEvent();
			if (eventService.checkIfEventHasEnded(eventRelatedToGameToBet)) {
				List<SingleBet> singleBets = betRepository.findSinglesByGameAndStatus(gameToBet, BetStatus.PLACED);
				for (SingleBet singleBet : singleBets) {
					try {
						finalizeSingleBet(singleBet, eventRelatedToGameToBet);

					} catch (Exception e) {
						LOGGER.info("Failed to finalize singleber in check bets for todays games");
					}
				}
			}
		}

	}

	/**
	 * Method finalized {@link MultipleBet} of {@link Event} that are finished.
	 * 
	 * <p>
	 * Method takes all {@link MultipleBet} that have {@link BetStatus} PLACED. Then
	 * it iterates through each {@link SingleBet} in {@link MultipleBet} and checks
	 * if it has ended. If so it checks the result of {@link MultipleBet} and
	 * finalizes it.
	 * </p>
	 */

	@Override
	public void checkMultiBetsForTodayGames() {

		List<MultipleBet> multibetsPlaced = multipleBetRepository.findByStatus(BetStatus.PLACED);
		for (MultipleBet multipleBet : multibetsPlaced) {
			List<SingleBet> singleBets = multipleBet.getBets();
			changeGroupBetPossibilityForMultipleBet(multipleBet);
			changeStatusOfSingleBetIfEventHasEnded(singleBets);

			List<SingleBet> updatedSingleBets = multipleBetRepository.findOne(multipleBet.getId()).getBets();
			if (checkIfAllBetsInMultipleBetAreEnded(updatedSingleBets)) {

				finalizeMultipleBet(multipleBet, resultOfMultipleBet(updatedSingleBets));

			}

		}

	}

	private void changeGroupBetPossibilityForMultipleBet(MultipleBet multipleBet) {
		List<SingleBet> singleBets = multipleBet.getBets();
		if (checkIfAnyEventInListHaveStarted(singleBets)) {
			multipleBet.setGroupBetPossible(false);
			multipleBetRepository.save(multipleBet);
		}
	}

	private void changeStatusOfSingleBetIfEventHasEnded(List<SingleBet> singleBets) {
		for (SingleBet singleBet : singleBets) {
			if (singleBet.getStatus().equals(BetStatus.PLACED)) {
				Event betsEvent = singleBet.getGame().getEvent();
				changeStatusAndResultOfSingleBetInMultipleBet(singleBet, betsEvent);
			}
		}
	}

	/**
	 * Method that finalizes {@link MultipleBet} result. If {@link User} won the bet
	 * it adds funds to his/her {@link Wallet} and creates {@link Operation}
	 * 
	 * @param multipleBet
	 *            that is going to be finalized
	 * @param result
	 *            of this {@link MultipleBet}
	 */
	private void finalizeMultipleBet(MultipleBet multipleBet, boolean result) {
		if (result) {
			User user = multipleBet.getUser();
			Wallet wallet = walletService.findByUser(user);
			BigDecimal prize = multipleBet.getJoinedAmount().multiply(multipleBet.getJoinedRating());
			walletService.addFunds(wallet, prize);
			operationService.createPlaceMultipleBetOperation(wallet, prize, multipleBet.getBets());
			multipleBet.setResult("WON");
		} else {
			multipleBet.setResult("LOST");

		}
		multipleBet.setStatus(BetStatus.FINALIZED);
		multipleBetRepository.save(multipleBet);

	}

	/**
	 * This method returns the result of {@link MultipleBet} If even one of
	 * {@link SingleBet} in the bet is lost whole {@link MultipleBet} is lost.
	 * 
	 * @param multipleBetBets
	 * 
	 * @return true if {@link MultipleBet} is won, false otherwise
	 */

	private boolean resultOfMultipleBet(List<SingleBet> multipleBetBets) {
		for (SingleBet singleBet : multipleBetBets) {
			if (singleBet.getBetResult().equals("LOST")) {
				return false;
			}

		}
		return true;

	}

	/**
	 * The method change the result of {@link SingleBet} in {@link MultipleBet} and
	 * also saves it. It takes place only if {@link Event} has ended.
	 * <p>
	 * Important - it changes the status and also saves the {@link SingleBet}
	 * </p>
	 * 
	 * @param singleBet
	 * @param event
	 */

	private void changeStatusAndResultOfSingleBetInMultipleBet(SingleBet singleBet, Event event) {
		if (eventService.checkIfEventHasEnded(event)) {
			if (checkIfBetWasWon(singleBet, event)) {
				singleBet.setBetResult("WON");
			}

			else {

				singleBet.setBetResult("LOST");

			}
			singleBet.setStatus(BetStatus.ENDED_IN_MULTIBET);
			betRepository.save(singleBet);
		}

	}

	/**
	 * This method checks if all the {@link SingleBet} in the given {@link List} has
	 * ended.
	 * 
	 * @param betsInMultipleBet
	 * @return true is all the {@link SingleBet} has the {@link BetStatus}
	 *         ENDED_IN_MULTIBET, false otherwise
	 */
	private boolean checkIfAllBetsInMultipleBetAreEnded(List<SingleBet> betsInMultipleBet) {
		for (SingleBet singleBet : betsInMultipleBet) {
			if (!singleBet.getStatus().equals(BetStatus.ENDED_IN_MULTIBET)) {
				return false;
			}
		}
		return true;

	}

	/**
	 * This method checks who won the {@link Event}.
	 * 
	 * @param event
	 * @return {@link String} one of the following: home, away or draw
	 */
	private String getEventResult(Event event) {
		String result = "";
		int homeScore = event.getHomeTeamScore();
		int awayScore = event.getAwayTeamScore();

		if (homeScore > awayScore) {
			result = "home";
		}

		else if (homeScore < awayScore) {
			result = "away";
		} else {
			result = "draw";
		}

		return result;

	}

	/**
	 * This method check if the {@link SingleBet} was placed is the same result as
	 * {@link Event} result
	 * 
	 * @param singleBet
	 * @param event
	 * @return true if {@link SingleBet} was won, false otherwise
	 */
	private boolean checkIfBetWasWon(SingleBet singleBet, Event event) {

		return (singleBet.getBetOn().equals(getEventResult(event))) ? true : false;

	}

	/**
	 * This method finalizes the {@link SingleBet}. If it was won it adds funds to
	 * {@link User} {@link Wallet} and creates according {@link Operation}
	 * 
	 * <p>
	 * Important! This method also saves the {@link SingleBet}
	 * </p>
	 * 
	 * @param singleBet
	 * @param event
	 */

	private void finalizeSingleBet(SingleBet singleBet, Event event) {
		if (checkIfBetWasWon(singleBet, event)) {
			User user = singleBet.getUser();
			Wallet wallet = walletService.findByUser(user);
			BigDecimal prize = singleBet.getAmount().multiply(singleBet.getRate());
			walletService.addFunds(wallet, prize);
			operationService.createPrizeOperation(wallet, singleBet);
			singleBet.setBetResult("WON");
		} else {
			singleBet.setBetResult("LOST");

		}
		singleBet.setStatus(BetStatus.FINALIZED);
		betRepository.save(singleBet);

	}

	@Override
	public SingleBet findById(long id) {
		return betRepository.findOne(id);
	}

	/**
	 * This method changes {@link SingleBet} attribute setItGroupBet to true and
	 * also it saves the {@link SingleBet}
	 * 
	 * <p>
	 * Important - this method saves the {@link SingleBet}
	 * </p>
	 * 
	 * @param bet
	 *            to change attribute
	 */
	@Override
	public void changeBetToGroupBet(SingleBet bet) {

		bet.setItGroupBet(true);
		betRepository.save(bet);
	}

	/**
	 * This method calculates the joined Rate for the {@link List} of
	 * {@link SingleBet}
	 * 
	 * @param bets
	 *            that will be used for calculations
	 * @return {@link BigDecimal} of multiplied rates of {@link SingleBet} bets
	 */

	@Override
	public BigDecimal calculateRateInMultipleBet(List<SingleBet> bets) {
		BigDecimal result = BigDecimal.valueOf(1.00);
		for (SingleBet singleBet : bets) {
			result = result.multiply(singleBet.getRate());
		}
		return result;
	}

	/**
	 * This method calculates the possible win from rate and amount
	 * 
	 * @return {@link BigDecimal} of multiplied two values
	 */
	@Override
	public BigDecimal calculatePossilbeWinInMultipleBet(BigDecimal rate, BigDecimal amount) {
		BigDecimal result = amount;
		return result.multiply(rate);
	}

	@Override
	public BigDecimal calculatePossilbeWinInGroupBet(GroupBet groupBet) {
		double numberOfPeople = (double) groupBet.getUsers().size();
		double rate = numberOfPeople / 100.00 + 1.00;
		BigDecimal regularWin = groupBet.getJoinedAmount().multiply(groupBet.getJoinedRating());
		BigDecimal wholeWin = regularWin.multiply(BigDecimal.valueOf(rate));
		return wholeWin.divide(BigDecimal.valueOf(numberOfPeople), 2);
	}

	@Override
	public void addUserToGroupBet(User user, GroupBet groupBet) {
		List<User> users = groupBet.getUsers();
		users.add(user);
		groupBet.setUsers(users);
		groupBetRepository.save(groupBet);
	}

	@Override
	public boolean checkIfAnyEventInListHaveStarted(List<SingleBet> bets) {
		for (SingleBet singleBet : bets) {
			if (!singleBet.getGame().getEvent().getStatus().equals("")) {
				return true;
			}

		}
		return false;
	}

	@Override
	public List<MultipleBet> findMultipleBetsByUserAndStatus(BetStatus status, User user) {
		return multipleBetRepository.findByUser(status, user);
	}

	@Override
	public List<GroupBet> findGroupBetByUser(User user) {
		return groupBetRepository.findByUsersIn(user);
	}

}
