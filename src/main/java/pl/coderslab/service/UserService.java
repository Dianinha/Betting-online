package pl.coderslab.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface UserService {

	User saveUser(User user);

	User createUser(User user);

	User findByEmail(String email);

	User findByUsername(String username);

	User finfByWallet(Wallet wallet);

	User changePassword(String password, User user);

	boolean checkPassword(String password, User user);

	List<User> findByEmailStartsWith(String email);

	List<User> findByUSernameStartsWith(String username);

	User getAuthenticatedUser(Authentication auth);

}
