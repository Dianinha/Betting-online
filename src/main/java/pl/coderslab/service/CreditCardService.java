package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface CreditCardService {

	/**
	 * Find credit card information by its User
	 * 
	 * @param user
	 * @return List of credit card information
	 */
	List<CreditCardInfo> findByUser(User user);

	/**
	 * Find credit card information by its Wallet
	 * 
	 * @param wallet
	 * @return List of credit card information
	 */
	List<CreditCardInfo> findByWallet(Wallet wallet);

	/**
	 * Saves credit card information.
	 * 
	 * @param creditCard
	 * @return saved credit card information
	 */
	CreditCardInfo save(CreditCardInfo creditCard);

	/**
	 * Created new credit card information. Bings it to users wallet
	 * 
	 * @param creditCard
	 * @param user
	 * @return saved credit card info
	 */
	CreditCardInfo create(CreditCardInfo creditCard, User user);

	/**
	 * Finds credit cards by id.
	 * 
	 * @param id
	 * @return Credit card with coresponding id.
	 */
	CreditCardInfo findById(Long id);

	/**
	 * Completly deletes from database the credit cards.
	 * 
	 * @param id
	 * @return true if operation was a success, false otherwise
	 */
	boolean deleteById(Long id);
}
