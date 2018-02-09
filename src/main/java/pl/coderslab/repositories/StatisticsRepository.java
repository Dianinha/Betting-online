package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Statistics;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long>{

}
