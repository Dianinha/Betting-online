package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;

@Repository
public interface GameToBetRepository extends JpaRepository<GameToBet, Long>{

	GameToBet findByEvent(Event event);
	
}
