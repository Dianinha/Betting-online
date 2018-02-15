package pl.coderslab.service.implementation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.WalletRepository;
import pl.coderslab.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Override
	public Wallet saveWallet(Wallet wallet) {
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet createWallet(User user) {
		Wallet wallet = new Wallet();
		wallet.setUser(user);
		wallet.setAmount(BigDecimal.valueOf(20.00));
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet findByUser(User user) {
		return walletRepository.findByUser(user);
	}

	@Override
	public Wallet addFunds(Wallet wallet, BigDecimal amount) {
		BigDecimal currentAmount = wallet.getAmount();
		BigDecimal newAmount = currentAmount.add(amount);
		wallet.setAmount(newAmount);

		return walletRepository.save(wallet);
	}

	@Override
	public Wallet substractFunds(Wallet wallet, BigDecimal amount) {
		if (hasWalletSufficientFunds(wallet, amount)) {
			BigDecimal currentAmount = wallet.getAmount();
			BigDecimal newAmount = currentAmount.subtract(amount);
			wallet.setAmount(newAmount);
			return walletRepository.save(wallet);
		}
		return wallet;
		 
	}

	@Override
	public boolean hasWalletSufficientFunds(Wallet wallet, BigDecimal amount) {
		int result = wallet.getAmount().compareTo(amount);
		return (result<0)?false:true;
	}
	

}
