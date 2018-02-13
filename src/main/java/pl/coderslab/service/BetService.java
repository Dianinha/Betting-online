package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;

public interface BetService {

	public void placeBet(SingleBet bet);

	boolean isBetRateAccurate(SingleBet bet);

	boolean isBetAlreadyPlaced(SingleBet bet, List<SingleBet> bets);

	List<SingleBet> findBetsByUserAndStatus(BetStatus status, User user);
	
	public void checkBetsForTodayGames();

}
