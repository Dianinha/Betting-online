package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface CreditCardService {

	List<CreditCardInfo> findByUser(User user);

	List<CreditCardInfo> findByWallet(Wallet wallet);

	CreditCardInfo save(CreditCardInfo creditCard);
	
	CreditCardInfo create(CreditCardInfo creditCard, User user);
	
	CreditCardInfo findById(Long id);
	
	boolean deleteById(Long id);
}
