package pl.coderslab.service;

import java.math.BigDecimal;
import java.util.List;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.service.implementation.BetServiceImpl;

public interface BetService {

	/**
	 * Method saves {@link SingleBet}
	 * 
	 * @param singleBet
	 *            to save
	 * 
	 * 
	 */
	public void placeBet(SingleBet singleBet);

	/**
	 * Checs if bet rate is the same as actual rate in the corresponding
	 * {@link GameToBet}
	 * 
	 * <br>
	 * See implememtation {@linkplain BetServiceImpl} for more information.
	 * 
	 * @param bet
	 * @return true if it maches, false otherwise
	 */
	boolean isBetRateAccurate(SingleBet bet);

	/**
	 * Checks if the bet is placed on the same game as any of the bets in the list.
	 * 
	 * @param bet
	 *            {@link SingleBet} to check
	 * @param bets
	 *            {@link List} of {@link SingleBet} to check
	 * @return true if the bet on this game was already placed, false otherwise
	 */
	boolean isBetAlreadyPlaced(SingleBet bet, List<SingleBet> bets);

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
	List<SingleBet> findBetsByUserAndStatus(BetStatus status, User user);

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
	public void checkBetsForTodayGames();

	/**
	 * Pretty much self explained.
	 * 
	 * @param id
	 * @return SingleBet with this id
	 */
	SingleBet findById(long id);

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
	void changeBetToGroupBet(SingleBet bet);

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
	void checkMultiBetsForTodayGames();

	/**
	 * This method calculates the joined Rate for the {@link List} of
	 * {@link SingleBet}
	 * 
	 * @param bets
	 *            that will be used for calculations
	 * @return {@link BigDecimal} of multiplied rates of {@link SingleBet} bets
	 */
	BigDecimal calculateRateInMultipleBet(List<SingleBet> bets);

	/**
	 * This method calculates the possible win from rate and amount
	 * 
	 * @return {@link BigDecimal} of multiplied two values
	 */
	BigDecimal calculatePossilbeWinInMultipleBet(BigDecimal rate, BigDecimal amount);

	/**
	 * Calculats the possible win in {@link GroupBet} per User
	 * 
	 * @param groupBet
	 * @return {@link BigDecimal}
	 */
	BigDecimal calculatePossilbeWinInGroupBet(GroupBet groupBet);

	/**
	 * Adds User to GroupBet. It also saves this GroupBet
	 * 
	 * @param user
	 * @param groupBet
	 */
	void addUserToGroupBet(User user, GroupBet groupBet);

	/**
	 * Checks if ANY of the event corresponding to the bets have started
	 * 
	 * @param bets
	 * @return true if ANY of the events started, false otherwise
	 */
	boolean checkIfAnyEventInListHaveStarted(List<SingleBet> bets);

	/**
	 * Finds {@link MultipleBet} by given {@link BetStatus} and {@link User}
	 * 
	 * @param status
	 * @param user
	 * @return {@link List} of {@link MultipleBet}
	 */
	List<MultipleBet> findMultipleBetsByUserAndStatus(BetStatus status, User user);

	/**
	 * Finds {@link GroupBet} by given {@link User}
	 * 
	 * @param user
	 * @return {@link List} of {@link GroupBet} the user is participating in
	 */
	List<GroupBet> findGroupBetByUser(User user);

}
