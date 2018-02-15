package pl.coderslab.service;

import java.math.BigDecimal;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface WalletService {

	/**
	 * Saves wallet.
	 * 
	 * @param wallet
	 * @return saved wallet.
	 */
	Wallet saveWallet(Wallet wallet);

	/**
	 * Create new Wallet for given user. New users are granted a bonus of 20 pln
	 * 
	 * @param user
	 * @return created Wallet
	 */
	Wallet createWallet(User user);

	/**
	 * Finds wallet by user related to it.
	 * @param user
	 * @return found wallet.
	 */
	Wallet findByUser(User user);

	/**
	 * Add funds and SAVES the Wallet.
	 * @param wallet
	 * @param amount
	 * @return Wallet with added funds
	 */
	Wallet addFunds(Wallet wallet, BigDecimal amount);

	/**
	 * Substracts given amount from Wallet if it have sufficient funds.
	 * @param wallet
	 * @param amount
	 * @return Wallet with substracted funds or given wallet if founds are not sufficient
	 */
	Wallet substractFunds(Wallet wallet, BigDecimal amount);

	/**
	 * Check if wallet have sufficient founds.
	 * @param wallet
	 * @param amount
	 * @return true is amount stored in wallet is sufficient, false otherwise.
	 */
	boolean hasWalletSufficientFunds(Wallet wallet, BigDecimal amount);
}
