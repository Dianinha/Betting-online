package pl.coderslab.serviceImpl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Role;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.RoleRepository;
import pl.coderslab.repositories.UserRepository;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WalletService walletService;
	

	public UserServiceImpl() {
		super();
	}

	@Override
	public User saveUser(User user) {
		
		return userRepository.save(user);
		
	}

	@Override
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> userRoles= new HashSet<>();
		userRoles.add(roleRepository.findByName("ROLE_USER"));
		user.setRoles(userRoles);
		User savedUser = userRepository.save(user);
		Wallet wallet = walletService.createWallet(savedUser);
		savedUser.setWallet(wallet);
		savedUser = userRepository.save(savedUser);
		return savedUser;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User finfByWallet(Wallet wallet) {
		return userRepository.findByWallet(wallet);
	}

}
