package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;

@Repository
public interface BetRepository extends JpaRepository<SingleBet, Long> {

	@Query("select b from SingleBet b where b.status=?1 and b.user = ?2 and b.isItGroupBet=false and b.isItMultiBet=false")
	List<SingleBet> findByUser(BetStatus status, User user);
	
	List<SingleBet> findByGame(GameToBet game);
	
	List<SingleBet> findByGameAndStatus(GameToBet game, BetStatus status);

	@Query("select b from SingleBet b where b.game=?1 and b.status = ?2 and b.isItGroupBet=false and b.isItMultiBet=false")
	List<SingleBet> findSinglesByGameAndStatus(GameToBet game, BetStatus status);
	
	@Query("select b from SingleBet b where b.game=?1 and b.status = ?2 and b.isItGroupBet=false and b.isItMultiBet=false")
	List<SingleBet> findMultisByGameAndStatus(GameToBet game, BetStatus status);
	
	@Query("select b from SingleBet b where b.game=?1 and b.status = ?2 and b.isItGroupBet=false and b.isItMultiBet=false")
	List<SingleBet> findGroupByGameAndStatus(GameToBet game, BetStatus status);
}
