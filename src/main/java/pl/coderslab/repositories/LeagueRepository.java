package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.League;
@Repository
public interface LeagueRepository extends JpaRepository<League, Long>{

}
