package pl.coderslab.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.CreditCardInfoRepository;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.WalletService;

@Service
public class CreditCardInfoImpl implements CreditCardService {

	@Autowired
	private CreditCardInfoRepository creditCardRepo;

	@Autowired
	private WalletService walletService;

	@Override
	public List<CreditCardInfo> findByUser(User user) {
		Wallet usersWallet = walletService.findByUser(user);
		return creditCardRepo.findCreditCardInfosByWallet(usersWallet);
	}

	@Override
	public List<CreditCardInfo> findByWallet(Wallet wallet) {
		return creditCardRepo.findCreditCardInfosByWallet(wallet);
	}

	@Override
	public CreditCardInfo save(CreditCardInfo creditCard) {
		return creditCardRepo.save(creditCard);
	}

	@Override
	public CreditCardInfo create(CreditCardInfo creditCard, User user) {
		creditCard.setWallet(walletService.findByUser(user));
		return creditCardRepo.save(creditCard);
	}

}
