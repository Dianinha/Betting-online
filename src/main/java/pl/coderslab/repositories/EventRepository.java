package pl.coderslab.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	List<Event> findAllByDate(LocalDate date);
	
	List<Event> findByDateGreaterThan(LocalDate date);
	
	List<Event> findByDateBetween(LocalDate start, LocalDate to);
}
