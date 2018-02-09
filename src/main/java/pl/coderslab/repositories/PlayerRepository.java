package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{

	Player findByName(String name);
}
