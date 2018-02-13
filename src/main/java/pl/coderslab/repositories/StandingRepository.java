package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.League;
import pl.coderslab.model.Standing;
import pl.coderslab.model.User;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long>{

	Standing findByTeamName(String teamName);
	
	//@Query("select s from Standing s where s.teamName = ?1 and s.league = 2?")
	Standing findByTeamNameAndLeague(String teamName, League league);
	
}
