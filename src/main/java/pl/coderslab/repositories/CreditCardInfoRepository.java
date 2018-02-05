package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.Wallet;

@Repository
public interface CreditCardInfoRepository extends JpaRepository<CreditCardInfo, Long>{

	CreditCardInfo findByWallet(Wallet wallet);
}
