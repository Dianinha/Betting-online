package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Bet;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

}
