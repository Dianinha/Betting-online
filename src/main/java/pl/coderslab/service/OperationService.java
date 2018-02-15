package pl.coderslab.service;

import java.math.BigDecimal;
import java.util.List;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.Operation;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.Wallet;

public interface OperationService {
	/**
	 * Saves operation.
	 * 
	 * @param operation
	 * @return saved operation.
	 */
	Operation save(Operation operation);

	/**
	 * Creates and saves operation for adding funds to User wallet.
	 * 
	 * @param wallet
	 * @param amount
	 * @param lastFourDigits
	 * @return saved Oparation.
	 */
	Operation createAddOperation(Wallet wallet, BigDecimal amount, String lastFourDigits);

	/**
	 * Creates and saves operation of substracting funds from wallet due to placing
	 * a bet.
	 * 
	 * @param wallet
	 * @param amount
	 * @param bet
	 * @return saved Oparation.
	 */
	Operation createPlaceBetOperation(Wallet wallet, BigDecimal amount, SingleBet bet);


	/**
	 * Creates and saves operation of substracting funds from wallet due to placing
	 * a multiple bet.
	 * 
	 * @param wallet
	 * @param amount
	 * @param bet
	 * @return saved Oparation.
	 */
	Operation createPlaceMultipleBetOperation(Wallet wallet, BigDecimal amount, List<SingleBet> bets);


	/**
	 * Creates and saves operation of withdrawal funds from wallet. 
	 * 
	 * @param wallet
	 * @param amount
	 * @param bet
	 * @return saved Oparation.
	 */
	Operation createWithdrawalOperation(Wallet wallet, BigDecimal amount);

	/**
	 * Creates and saves operation of adding funds to wallet due to won bet.
	 * 
	 * @param wallet
	 * @param bet
	 * @return saved Oparation.
	 */
	Operation createPrizeOperation(Wallet wallet, SingleBet bet);

	/**
	 * Finds all operations connected to Wallet.
	 * @param wallet
	 * @return {@link List} of connected Operations
	 */
	List<Operation> findAllOperationByWallet(Wallet wallet);

	/**
	 * Creates and saves operation of adding funds to wallet due to won {@link MultipleBet}.
	 * 
	 * @param wallet
	 * @param bet
	 * @return saved Oparation.
	 */
	Operation createPrizeForMultipleBetOperation(Wallet wallet, MultipleBet multipleBet);

	/**Creates and saves operation of substracting funds from wallet due to joining
	 * a {@link GroupBet}
	 * 
	 * @param wallet
	 * @param groupBet
	 * @return saved Oparation.
	 */
	Operation joinGroupBetOperation(Wallet wallet, GroupBet groupBet);
}
