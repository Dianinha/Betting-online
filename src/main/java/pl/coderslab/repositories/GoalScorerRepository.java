package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.GoalScorer;

@Repository
public interface GoalScorerRepository extends JpaRepository<GoalScorer, String> {

}
