package pl.coderslab.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	User user1;
	User user2;
	User user3;

	@Before
	public void setUp() {
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
		entityManager.persist(user1);
		entityManager.persist(user2);
		entityManager.persist(user3);
	}

	@Test
	public void testFindByEmail() {
		// given

		// when
		User result = userRepository.findByEmail("diana@wp.pl");
		// then
		assertEquals(result, user1);

	}

	@Test
	public void testFindByUsername() {
		// given

		// when
		User result = userRepository.findByUsername("dianinha");
		// then
		assertEquals(result, user1);
	}

	@Test
	public void testFindByWallet() {
		// given
		Wallet wallet = new Wallet();
		wallet.setUser(user1);
		user1.setWallet(wallet);
		entityManager.persist(wallet);
		entityManager.persist(user1);

		// when
		User result = userRepository.findByWallet(wallet);
		// then
		assertEquals(result, user1);
	}

	@Test
	public void testFindByUsernameStatsWith() {
		// given

		// when
		List<User> result = userRepository.findByUsernameStatsWith("d");
		List<User> expected = new ArrayList<>();
		expected.add(user1);
		expected.add(user3);
		// then
		assertThat(result).hasSameElementsAs(expected);
	}

	@Test
	public void testFindByEmailStatsWith() {
		// given

		// when
		List<User> result = userRepository.findByEmailStatsWith("di");
		List<User> expected = new ArrayList<>();
		expected.add(user1);
		// then
		assertThat(result).hasSameElementsAs(expected);
	}

}
