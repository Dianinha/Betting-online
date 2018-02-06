package pl.coderslab.service;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface WalletService {

	Wallet saveWallet(Wallet wallet);
	
	Wallet createWallet(User user);
	
	Wallet findByUser(User user);
}
