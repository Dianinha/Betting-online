package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	User findByUsername(String username);
	
	User findByWallet(Wallet wallet);
	
	@Query("select u from User u where u.username like ?1%")
	List<User> findByUsernameStatsWith(String username);
	
	@Query("select u from User u where u.email like ?1%")
	List<User> findByEmailStatsWith(String email);
	
	

}
