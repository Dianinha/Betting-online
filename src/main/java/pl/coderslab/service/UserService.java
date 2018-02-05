package pl.coderslab.service;

import pl.coderslab.model.User;

public interface UserService {

	User saveUser(User user);

	User createUser(User user);

	User findByEmail(String email);

	User findByUsername(String username);

}
