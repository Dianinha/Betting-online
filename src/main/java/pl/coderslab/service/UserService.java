package pl.coderslab.service;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

public interface UserService {

	User saveUser(User user);

	User createUser(User user);

	User findByEmail(String email);

	User findByUsername(String username);
	
	User finfByWallet(Wallet wallet);
	
	User changePassword(String password, User user);
	
	boolean checkPassword (String password, User user);

}
