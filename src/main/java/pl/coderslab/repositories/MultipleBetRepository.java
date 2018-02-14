package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.User;

@Repository
public interface MultipleBetRepository extends JpaRepository<MultipleBet, Long>{

	MultipleBet findByUser(User user);
	
	@Query("select b from MultipleBet b where b.status=?1 and b.user = ?2 AND isItAGroupBet=false")
	List<MultipleBet> findByUser(BetStatus status, User user);
	
	@Query("select b from MultipleBet b where b.status=?1 AND isItAGroupBet=false")
	List<MultipleBet> findByStatus(BetStatus status);
}
