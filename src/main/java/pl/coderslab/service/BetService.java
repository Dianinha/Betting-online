package pl.coderslab.service;

import java.math.BigDecimal;
import java.util.List;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;

public interface BetService {

	/**
	 * Method saves {@link SingleBet}
	 * 
	 * @param singleBet  to save
	 * 
	 * 
	 */
	public void placeBet(SingleBet singleBet);

	boolean isBetRateAccurate(SingleBet bet);

	boolean isBetAlreadyPlaced(SingleBet bet, List<SingleBet> bets);

	List<SingleBet> findBetsByUserAndStatus(BetStatus status, User user);

	public void checkBetsForTodayGames();

	SingleBet findById(long id);

	void changeBetToGroupBet(SingleBet bet);

	void checkMultiBetsForTodayGames();

	BigDecimal calculateRateInMultipleBet(List<SingleBet> bets);

	BigDecimal calculatePossilbeWinInMultipleBet(BigDecimal rate, BigDecimal amount);
	
	BigDecimal calculatePossilbeWinInGroupBet(GroupBet groupBet);
	
	void addUserToGroupBet (User user, GroupBet groupBet);
	
	boolean checkIfAnyEventInListHaveStarted(List<SingleBet> bets);
	
	List<MultipleBet> findMultipleBetsByUserAndStatus (BetStatus status, User user);
	
	List<GroupBet> findGroupBetByUser (User user);

}
