package pl.coderslab.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import pl.coderslab.model.Role;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.RoleRepository;
import pl.coderslab.repositories.UserRepository;
import pl.coderslab.service.implementation.UserServiceImpl;

public class UserServiceImplTest {

	private UserService userService;
	private UserRepository repository;
	private RoleRepository roleRepository;
	private WalletService walletService;
	User user1;
	User user2;
	User user3;

	BCryptPasswordEncoder passwordEncoder;

	@Before
	public void setUp() {
		passwordEncoder = mock(BCryptPasswordEncoder.class);
		repository = mock(UserRepository.class);
		roleRepository = mock(RoleRepository.class);
		walletService = mock(WalletService.class);
		userService = new UserServiceImpl(repository, passwordEncoder, roleRepository, walletService);
		user1 = new User();
		user1.setEmail("diana@wp.pl");
		user1.setPassword("diana");
		user1.setName("Diana");
		user1.setSurname("Wiszowata");
		user1.setUsername("dianinha");
		user2 = new User();
		user2.setEmail("anna15@gmail.com");
		user2.setPassword("anna");
		user2.setName("Anna");
		user2.setSurname("Kowalska");
		user2.setUsername("annaanna");
		user3 = new User();
		user3.setEmail("dariaM@gmail.com");
		user3.setPassword("daria");
		user3.setName("Daria");
		user3.setSurname("Nowak");
		user3.setUsername("dariah");

	}

	@Test
	public void testCreateUser() {
		// given
		when(repository.save(user1)).thenReturn(user1);
		Role role = new Role();
		when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
		Wallet wallet = new Wallet();
		when(walletService.createWallet(user1)).thenReturn(wallet);
		when(passwordEncoder.encode(user1.getPassword())).thenReturn("sss");
		// when
		User result = userService.createUser(user1);
		// then
		assertEquals(result.getName(), user1.getName());
	}

	// @Test
	// public void testSaveUser {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFindByEmail() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFindByUsername() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFindByWallet() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testChangePassword() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCheckPassword() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFindByEmailStartsWith() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFindByUSernameStartsWith() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetAuthenticatedUser() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCheckIfUsernameIsTaken() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testCheckIfEmailIsTakien() {
	// fail("Not yet implemented");
	// }

}
