package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;

@Repository
public interface GameToBetRepository extends JpaRepository<GameToBet, Long>{

	GameToBet findByEvent(Event event);
	
	List<GameToBet> findByEventIn(List<Event> events);
	
	List<GameToBet> findByActive(boolean active);
	 
}
