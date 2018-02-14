package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.User;
@Repository
public interface GroupBetRepository extends JpaRepository<GroupBet, Long>{

	List<GroupBet> findByUsersIn(User user);
}
