package pl.coderslab.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface UserService {

	/**
	 * Saves user
	 * 
	 * @param user
	 * @return saved user
	 */
	User saveUser(User user);

	/**
	 * Creates new User and creates a Wallet for him. It also adds a friend service
	 * to his or her friendslist.
	 * 
	 * @param user
	 * @return
	 */
	User createUser(User user);

	/**
	 * Finds User by email
	 * 
	 * @param email
	 * @return
	 */
	User findByEmail(String email);

	/**
	 * Finds user by username parameter.
	 * 
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * Finds user by given Wallet
	 * 
	 * @param wallet
	 * @return
	 */
	User findByWallet(Wallet wallet);

	/**
	 * Changes user password for given String
	 * 
	 * @param password
	 * @param user
	 * @return user with changed password
	 */
	User changePassword(String password, User user);

	/**
	 * Checks if given string is the same as users password
	 * 
	 * @param password
	 * @param user
	 * @return true if passwords match, false otherwise
	 */
	boolean checkPassword(String password, User user);

	/**
	 * Finds a list of users that email starts with given String
	 * 
	 * @param email
	 * @return
	 */
	List<User> findByEmailStartsWith(String email);

	/**
	 * Finds a list of users that username starts with given String
	 * 
	 * @param username
	 * @return
	 */
	List<User> findByUSernameStartsWith(String username);

	/**
	 * Returns user that is logged
	 * 
	 * @param auth
	 * @return
	 */
	User getAuthenticatedUser(Authentication auth);

	/**
	 * Checks if user with given username already exists in database
	 * 
	 * @param username
	 * @return true if username is not available, false otherwise
	 */
	boolean checkIfUsernameIsTaken(String username);

	/**
	 * Checks if user with given email already exists in database
	 * 
	 * @param email
	 * @return true if email is not available, false otherwise
	 */
	boolean checkIfEmailIsTakien(String email);

}
