package pl.coderslab.service.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger("DianinhaLogger");

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

	@Override
	public CreditCardInfo findById(Long id) {
		return creditCardRepo.findOne(id);
	}

	@Override
	public boolean deleteById(Long id) {
		boolean result = false;
		try {
			creditCardRepo.delete(id);
			result = true;
		} catch (Exception e) {
			LOGGER.info("Failed to delete credit card.");
		}

		return result;
	}

}
