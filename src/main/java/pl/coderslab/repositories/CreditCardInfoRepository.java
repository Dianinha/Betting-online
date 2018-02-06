package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.Wallet;

@Repository
public interface CreditCardInfoRepository extends JpaRepository<CreditCardInfo, Long>{

	List<CreditCardInfo> findCreditCardInfosByWallet(Wallet wallet);
	
}
