package pl.coderslab.service;

import java.math.BigDecimal;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface WalletService {

	Wallet saveWallet(Wallet wallet);

	Wallet createWallet(User user);

	Wallet findByUser(User user);

	Wallet addFunds(Wallet wallet, BigDecimal amount);

	Wallet substractFunds(Wallet wallet, BigDecimal amount);

	boolean hasWalletSufficientFunds(Wallet wallet, BigDecimal amount);
}
