package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long>{

	Wallet findByUser(User user);
}
