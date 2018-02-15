package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long>{

	Standing findByTeamName(String teamName);
	
	Standing findByTeamNameAndLeague(String teamName, League league);
	
}
